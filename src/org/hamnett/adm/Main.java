/*
 * This file is part of ADM, the Asterisk Desktop Manager.
 *
 * ADM is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * ADM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ADM; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.hamnett.adm;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.hamnett.adm.gui.SystemTray;
import org.hamnett.adm.util.CommonDialogs;
import org.hamnett.adm.util.ConfigFilenameFilter;
import org.hamnett.adm.util.DazzlThreadPool;
import org.hamnett.adm.util.uuid.UUIDGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.Lightwave.ReceiveUDP;

public class Main {
    private final Log logger = LogFactory.getLog(getClass());

    private static Main instance;
    private static File preferenceDirectory;
    private static TreeMap<String, PreferenceStore> preferenceStores;
    private static String currentPreferenceStore;

    private ApplicationContext appContext;
    private String applicationUID;
    private static Map plugins;
    private List<DazzlPlugin> startedPlugins;
    private Display display;
    private Shell shell;

    private static SystemTray tray;

    /**
     * Main launcher
     */
    private Main() {
        this.startedPlugins = new ArrayList<DazzlPlugin>();
        this.display = new Display();
        Display.setAppName("Dazzl"); //$NON-NLS-1$

        this.shell = new Shell(display);

        this.preferenceStores = new TreeMap<String, PreferenceStore>();
        this.preferenceDirectory = new File(
                System.getProperty("user.home") //$NON-NLS-1$
                        + File.separator
                        + Dazzl.DAZZL_PREFERENCE_DIR
        );
    
    }

    /**
     * Application entry method.
     *
     * @param args command line arguments
     */
    public static void main(String args[]) {
        if (instance != null) {
            System.err.println("Dazzl already running.");
            return;
        }
        instance = new Main();
        instance.startup();
    }

    /**
     * Loads the current configuration file, initializes and starts plugins
     * and launches the tray.
     */
    public void startup() {
        Iterator pluginIterator;
        List<Object> menuContributionItems;
        long startTime;
        long endTime;
        String[] preferenceFiles;

        boolean firstLaunch = false;

        startTime = System.currentTimeMillis();

        // Check if the configuration directory exists, create it if not
        if (!preferenceDirectory.exists()) {
            try {
                preferenceDirectory.mkdir();
            }
            catch (SecurityException e) {
                e.printStackTrace();
                System.exit(-1);
            }

            logger.debug("Created " + preferenceDirectory.getAbsolutePath()); //$NON-NLS-1$
        }

        // If there is already a file with the same name as the directory
        // we want to create, we're stuck...
        if (!preferenceDirectory.isDirectory()) {
            logger.debug("There is already a file named " //$NON-NLS-1$
                    + preferenceDirectory.getAbsolutePath());
            System.exit(-1);
        }

        FilenameFilter filter = new ConfigFilenameFilter();
        // List all files in this directory
        preferenceFiles = preferenceDirectory.list(filter);

        if (preferenceFiles == null) {
            System.exit(-1);
        }
        if (preferenceFiles == null || preferenceFiles.length == 0) {
            String newConfiguration = createConfiguration();
            Main.preferenceStores.put
                    (newConfiguration,
                            new PreferenceStore(
                                    System.getProperty("user.home") //$NON-NLS-1$
                                            + File.separator
                                            + Dazzl.DAZZL_PREFERENCE_DIR
                                            + File.separator
                                            + newConfiguration)
                    );
        } else {
            for (int i = 0; i < preferenceFiles.length; i++) {
                String fullfilename = System.getProperty("user.home") //$NON-NLS-1$
                        + File.separator
                        + Dazzl.DAZZL_PREFERENCE_DIR
                        + File.separator
                        + preferenceFiles[i];
                PreferenceStore p = new PreferenceStore(fullfilename);
                try {
                    p.load();
                }
                catch (IOException e) {
                    logger.debug("Unable load preference store."); //$NON-NLS-1$
                }
                Main.preferenceStores.put(preferenceFiles[i], p);
            }
        }
        TreeSet<String> set = new TreeSet<String>(preferenceStores.keySet());
        Main.currentPreferenceStore = set.first(); // load the first cfgXXX as the default config.

        try {
            preferenceStores.get(currentPreferenceStore).load();
        }
        catch (IOException e) {
            logger.debug("Unable load preference store."); //$NON-NLS-1$
        }

        applicationUID = preferenceStores.get(currentPreferenceStore).getString(Dazzl.DAZZL_APPLICATION_UID);
        if (applicationUID == null || "".equals(applicationUID)) {
            applicationUID = createUID();
            logger.debug("Generated application uid: " + applicationUID); //$NON-NLS-1$
            preferenceStores.get(currentPreferenceStore).setValue(Dazzl.DAZZL_APPLICATION_UID, applicationUID);
            try {
                preferenceStores.get(currentPreferenceStore).save();
            }
            catch (IOException e) {
                logger.warn("Unable to save generated application uid", e); //$NON-NLS-1$
            }

            firstLaunch = true;

        }


  
        // run update check in the background for better startup performance
        // and to stop network timeouts blocking adm.
        //logger.debug("Starting Update Check Thread"); //$NON-NLS-1$
        //DazzlThreadPool.addJob(new WebUpdateCheck(preferenceStores.get(currentPreferenceStore)
         //       .getString(Dazzl.DAZZL_APPLICATION_UID)));

        DazzlThreadPool.addJob(new ReceiveUDP());
     
        menuContributionItems = new ArrayList<Object>();

        logger.debug("Loading application context from " //$NON-NLS-1$
                + Dazzl.DAZZL_APPLICATION_CONTEXT);
        try {
            appContext = new ClassPathXmlApplicationContext(
                    new String[]{Dazzl.DAZZL_APPLICATION_CONTEXT});
        }
        catch (Exception e) {
            logger.warn("Invalid plugin definiton.", e); //$NON-NLS-1$
            CommonDialogs.showError(Messages.getString("invalidPluginDefinition.error"), e); //$NON-NLS-1$
            shutdown();
        }

        plugins = appContext.getBeansOfType(DazzlPlugin.class);

        // read configuration
        pluginIterator = plugins.values().iterator();
        while (pluginIterator.hasNext()) {
            DazzlPlugin plugin;

            plugin = (DazzlPlugin) pluginIterator.next();
            plugin.setConfigurationDefaults(preferenceStores.get(currentPreferenceStore));
            plugin.loadConfiguration(preferenceStores.get(currentPreferenceStore));
            if (plugin instanceof IPropertyChangeListener) {
                preferenceStores.get(currentPreferenceStore).addPropertyChangeListener((IPropertyChangeListener) plugin);
            }
        }

        // populate menu contributions list
        pluginIterator = plugins.values().iterator();
        while (pluginIterator.hasNext()) {
            DazzlPlugin plugin;
            List contributions;

            plugin = (DazzlPlugin) pluginIterator.next();
            contributions = plugin.getMenuContributions();
            if (contributions != null) {
                menuContributionItems.addAll(contributions);
            }
        }

        /*if (firstLaunch) {
            //popup prefs window
            CommonDialogs.showMessage(Messages.getString("firstTime.title"), Messages.getString("firstTime.message")); //$NON-NLS-1$ //$NON-NLS-2$
            PreferenceDialog dialog;
            PreferenceManager pm = new PreferenceManager();

            dialog = new PreferenceDialog(getShell(), pm);
            populatePreferenceManager(pm);
            dialog.setPreferenceStore(preferenceStores.get(currentPreferenceStore));
            dialog.open();
        }*/
        
        
        // and finally start the plugins
        pluginIterator = plugins.values().iterator();
        while (pluginIterator.hasNext()) {
            DazzlPlugin plugin;

            plugin = (DazzlPlugin) pluginIterator.next();
            logger.debug("Starting plugin " + plugin.getClass().getName()); //$NON-NLS-1$
            try {
                plugin.startup();
                startedPlugins.add(plugin);
            }
            catch (DependencyNotSatisfiedException e) {
                logger.warn("Dependencies of " + plugin.getClass().getName() //$NON-NLS-1$
                        + " not satisfied", e); //$NON-NLS-1$
                CommonDialogs.showError(Messages.getString("depsNotSatisfied.error"), e); //$NON-NLS-1$
                shutdown();
            }
        }

        logger.debug("Initializing System Tray"); //$NON-NLS-1$
        tray = new SystemTray(preferenceStores, currentPreferenceStore, menuContributionItems);

        //logger.debug("Starting Idle Monitor"); //$NON-NLS-1$
        //idleMonitor = new IdleMonitor(display, 5);

        endTime = System.currentTimeMillis();
        logger.debug("Startup completed in " + (endTime - startTime) + " ms"); //$NON-NLS-1$ //$NON-NLS-2$



        tray.run();

    }

    /**
     * Tries to shut down plugins gracefully
     */
    public void shutdown() {
        // shutdown started plugins in reverse order
        for (int i = startedPlugins.size() - 1; i >= 0; i--) {
            DazzlPlugin plugin;

            plugin = startedPlugins.get(i);
            logger.debug("Stopping plugin " + plugin.getClass().getName()); //$NON-NLS-1$
            try {
                plugin.shutdown();
            }
            catch (Exception e) {
                logger.warn("Shutdown of " + plugin.getClass().getName() //$NON-NLS-1$
                        + " failed", e); //$NON-NLS-1$
            }
        }
        System.exit(-1);
    }

    /**
     * Get the default instance of Main
     *
     * @return default instance
     */
    public static Main getDefault() {
        return instance;
    }

    /**
     * Get the shell from the default instance
     *
     * @return the tray's shell
     */
    public static Shell getShell() {
        return instance.shell;
    }

    /**
     * Get the display from the default instance
     *
     * @return the tray's display
     */
    public static Display getDisplay() {
        return instance.display;
    }

    /**
     * Populate the preference manager with nodes from
     * the running plugins
     *
     * @param preferenceManager the preference manager
     */
    public static void populatePreferenceManager(
            PreferenceManager preferenceManager) {
        synchronized (instance.plugins) {
            Iterator pluginIterator;

            pluginIterator = instance.plugins.values().iterator();
            while (pluginIterator.hasNext()) {
                DazzlPlugin plugin;

                plugin = (DazzlPlugin) pluginIterator.next();
                plugin.registerPreferenceNodes(preferenceManager);
            }
        }
    }

    /**
     * Get the unique application ID, for use in update checks
     *
     * @return the unique application id
     */
    public static String getApplicationUID() {
        return instance.applicationUID;
    }

    /**
     * Adds an idle activity listener
     *
     * @param l the property change listener
     */
    public void addIdleListener(IPropertyChangeListener l) {
        //idleMonitor.addPropertyListener(l);
    }

    /**
     * Remove the idle activity listener
     *
     * @param l the property change listener
     */
    public void removeIdleListener(IPropertyChangeListener l) {
        //idleMonitor.removePropertyListener(l);
    }

    /**
     * Creates a new UID for this ADM instance.
     *
     * @return the created UID
     */
    private String createUID() {
        return UUIDGenerator.getInstance().generateTimeBasedUUID().toString();
    }

    /**
     * Create a new configuration.
     *
     * @return a string that uniquely identifies the created configuration.
     */
    public static String createConfiguration() {
        int i;
        String filename = "";

        // Find the first "cfgXXX" available, 0 <= XXX <= 999.
        // This implies a maximum of 1000 configuration files (HARDCODED)
        for (i = 0; i <= 999; i++) {
            filename =
                    Dazzl.DAZZL_PREFERENCE_STORE_PREFIX
                            +
                            String.format("%03d", i); //$NON-NLS-1$

            if (preferenceStores.get(filename) == null) {
                break; // stop as soon as we have found an available envXXX
            }
        }

        if (i == 1000) {
            // there are already 1000 configuration files
            // it's hard to believe..
        } else {
            String fullfilename =
                    System.getProperty("user.home") //$NON-NLS-1$
                            + File.separator
                            + Dazzl.DAZZL_PREFERENCE_DIR
                            + File.separator
                            + filename;

            File newConfiguration = new File(fullfilename);
            try {
                newConfiguration.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            PreferenceStore store = new PreferenceStore(fullfilename);
            if (instance.applicationUID == null || "".equals(instance.applicationUID)) {
                instance.applicationUID = instance.createUID();
            }
            store.setValue(Dazzl.DAZZL_APPLICATION_UID, instance.applicationUID);
            preferenceStores.put(filename, store);
        }
        return filename;
    }

    /**
     * Pop up the preference panel using the specified configuration,
     * and sets this configuration as the current one.
     *
     * @param configuration The configuration to edit, as returned by
     *                      createConfiguration.
     */
    public static void editConfiguration(String configuration) {
        setCurrentConfiguration(configuration);

        PreferenceDialog dialog;
        PreferenceManager pm = new PreferenceManager();

        dialog = new PreferenceDialog(getShell(), pm);
        populatePreferenceManager(pm);
        dialog.setPreferenceStore(preferenceStores.get(configuration));
        dialog.open();
        try {
            preferenceStores.get(configuration).save();
        }
        catch (IOException e) {
            //logger.warn("Unable to save generated application uid", e); //$NON-NLS-1$
        }
    }

    /**
     * Delete the specified configuration (not implemented yet).
     *
     * @param configuration The name of the configuration to delete.
     */
    public static void deleteConfiguration(String configuration) {
        // Don't delete the last configuration left.
        if (preferenceStores.size() == 1) {
            CommonDialogs.showMessage(Messages.getString("deletingLastConfiguration.title"), //$NON-NLS-1$
                    Messages.getString("deletingLastConfiguration.message")); //$NON-NLS-1$
            return;
        } else {
            // Remove the configuration from the list
            preferenceStores.remove(configuration);
            // Remove the configuration from the disk as well
            String fullfilename =
                    System.getProperty("user.home") //$NON-NLS-1$
                            + File.separator
                            + Dazzl.DAZZL_PREFERENCE_DIR
                            + File.separator
                            + configuration;
            File deletedConfiguration = new File(fullfilename);
            try {
                deletedConfiguration.delete();
            }
            catch (SecurityException e) {
                e.printStackTrace();
            }

            // Choose the first configuration as the newly selected one.
            currentPreferenceStore = preferenceStores.firstKey();
        }
    }

    /**
     * Returns the name of the current configuration.
     *
     * @return The name of the current configuration.
     */
    public static String getCurrentConfiguration() {
        return currentPreferenceStore;
    }

    /**
     * Selects this configuration as the current one.
     *
     * @param configuration The name of the configuration to set as current.
     */
    public static void setCurrentConfiguration(String configuration) {
        currentPreferenceStore = configuration;

        Iterator pluginIterator = plugins.values().iterator();
        while (pluginIterator.hasNext()) {
            DazzlPlugin plugin;

            plugin = (DazzlPlugin) pluginIterator.next();
            plugin.setConfigurationDefaults(preferenceStores.get(configuration));
            plugin.loadConfiguration(preferenceStores.get(configuration));
            if (plugin instanceof IPropertyChangeListener) {
                preferenceStores.get(configuration).addPropertyChangeListener((IPropertyChangeListener) plugin);
            }
        }
    }

    /**
     * Return the PreferenceStore corresponding to the configuration
     * name given.
     *
     * @param configuration The name of the configuration to retrieve.
     * @return The PreferenceStore of that name.
     */
    public static PreferenceStore getConfiguration(String configuration) {
        return preferenceStores.get(configuration);
    }

    /**
     * Return a list of the names of the configurations.
     *
     * @return A set of configuration names.
     */
    public static Set<String> getConfigurationList() {
        return preferenceStores.keySet();
    }

    public static SystemTray getTray() {
        return tray;
    }


}
