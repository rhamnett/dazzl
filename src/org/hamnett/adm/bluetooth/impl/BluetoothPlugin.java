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
package org.hamnett.adm.bluetooth.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.hamnett.adm.DazzlPlugin;
import org.hamnett.adm.DependencyNotSatisfiedException;
import org.hamnett.adm.bluetooth.Bluetooth;
import org.hamnett.adm.bluetooth.BluetoothConfiguration;
import org.hamnett.adm.bluetooth.BluetoothDevice;
import org.hamnett.adm.bluetooth.BluetoothException;
import org.hamnett.adm.bluetooth.BluetoothPresenceListener;
import org.hamnett.adm.bluetooth.Messages;
import org.hamnett.adm.bluetooth.gui.BluetoothDisabledPreferencePage;
import org.hamnett.adm.bluetooth.gui.BluetoothPreferencePage;

public class BluetoothPlugin
        implements
            Bluetooth,
            DazzlPlugin,
            BluetoothPresenceListener,
            IPropertyChangeListener
{
    private static final String MONITORING_THREAD_CLASS = "org.hamnett.adm.bluetooth.impl.MonitoringThreadImpl";

    private final Log logger = LogFactory.getLog(getClass());

    private static BluetoothPlugin instance;

    private MonitoringThread monitoringThread;

    private final BluetoothConfiguration configuration;
    private final boolean btAvailable;

    /**
     * Creates a new instance.
     */
    public BluetoothPlugin()
    {
        this.configuration = new BluetoothConfigurationImpl();
        instance = this;

        try
        {
            Class.forName("javax.bluetooth.LocalDevice"); //$NON-NLS-1$
        }
        catch (ClassNotFoundException e1)
        {
            this.btAvailable = false;
            logger.info("Bluetooth stack (javax.bluetooth) is not available."); //$NON-NLS-1$
            return;
        }

        this.btAvailable = true;
    }


    // implementation of Bluetooth

    public void startMonitoring()
    {
        if (!btAvailable)
        {
            logger.info("Unable to start monitoring: bluetooth stack " //$NON-NLS-1$
                    + "(javax.bluetooth) is not available."); //$NON-NLS-1$
        }

        if (monitoringThread == null)
        {
            MonitoringThread mt;

            try
            {
                mt = getMonitoringThread();
                mt.start();
            }
            catch (BluetoothException e)
            {
                logger.warn("Unable to start Monitoring Thread", e); //$NON-NLS-1$
                return;
            }
            mt.addMonitoringListener(this);
            monitoringThread = mt;
        }
        else
        {
            synchronized (monitoringThread)
            {
                monitoringThread.unpause();
            }
        }
    }

    private MonitoringThread getMonitoringThread() throws BluetoothException
    {
        MonitoringThread mt;

        if (!btAvailable)
        {
            throw new BluetoothException("Unable to create monitoring thread:" + //$NON-NLS-1$
                    " bluetooth not available"); //$NON-NLS-1$
        }

        try
        {
            Class clazz;
            clazz = Class.forName(MONITORING_THREAD_CLASS);
            mt = (MonitoringThread) clazz.newInstance();
        }
        catch (Exception e)
        {
            logger.error("Unable to create " + MONITORING_THREAD_CLASS, e); //$NON-NLS-1$
            throw new BluetoothException(
                    "Unable to create " + MONITORING_THREAD_CLASS); //$NON-NLS-1$
        }

        return mt;
    }

    public void stopMonitoring()
    {
        if (monitoringThread != null)
        {
            synchronized (monitoringThread)
            {
                monitoringThread.pause();
            }
        }
    }

    public boolean isMonitoring()
    {
        throw new UnsupportedOperationException();
    }

    // implementation of AdmPlugin

    public List getMenuContributions()
    {
        return null;
    }

    public void registerPreferenceNodes(PreferenceManager preferenceManager)
    {
        String className;

        //System.out.println("BT AVAIL: " + btAvailable + " and asterisk 1.2: "
        //        + asterisk.getVersion().contains("Asterisk 1.2"));

        //if (asterisk.supportsRedirectMode() && btAvailable)
        //TODO fix the detection
        if (btAvailable)
        {
            className = BluetoothPreferencePage.class.getName();
        }
        else
        {
            className = BluetoothDisabledPreferencePage.class.getName();
        }

        PreferenceNode bluetoothNode = new PreferenceNode("bluetooth", //$NON-NLS-1$
                Messages.getString("BluetoothPreferencePage.title"), null, //$NON-NLS-1$
                className);

        preferenceManager.addToRoot(bluetoothNode);
    }

    public void setConfigurationDefaults(IPreferenceStore store)
    {
        store.setDefault(ENABLED_PREFERENCE, false);
        store.setDefault(DEVICE_ADDRESS_PREFERENCE, "");
    }

    public void loadConfiguration(IPreferenceStore store)
    {
        configuration.setEnabled(store.getBoolean(ENABLED_PREFERENCE));
        configuration.setDeviceAddress(store
                .getString(DEVICE_ADDRESS_PREFERENCE));
        configuration.setRedirectExtension(store
                .getString(REDIRECT_EXTENSION_PREFERENCE));
    }

    public void shutdown()
    {

    }

    public void startup() throws DependencyNotSatisfiedException
    {
     /*   if (asterisk == null)
        {
            throw new DependencyNotSatisfiedException(
                    "AudioPlugin's mandatory depency on "
                            + "AsteriskPlugin is not satisied.");
        }*/

        if (configuration.isEnabled() && btAvailable)
        {
            startMonitoring();
        }
    }

    // implementation of BluetoothPresenceListener

    public void deviceAppeared(BluetoothDevice device)
    {

        synchronized (configuration)
        {
            if (configuration.isEnabled()
                    && configuration.getDeviceAddress() != null
                    && configuration.getDeviceAddress().equals(
                            device.getAddress()))
            {
                logger.info("Bluetooth: Device appeared - do something");
            }
        }

    }

    public void deviceDisappeared(BluetoothDevice device)
    {
        synchronized (configuration)
        {
            if (configuration.isEnabled()
                    && configuration.getRedirectExtension() != null
                    && !"".equals(configuration.getRedirectExtension())
                    && configuration.getDeviceAddress() != null
                    && configuration.getDeviceAddress().equals(
                            device.getAddress()))
            {
                // switch auto redirect mode on
                logger.info("Bluetooth: Device disappeared - do something  " + configuration.getRedirectExtension());
            }
        }
    }

    // implementation of IPropertyChangeListener

    public void propertyChange(PropertyChangeEvent event)
    {
        if (ENABLED_PREFERENCE.equals(event.getProperty()))
        {
            if (event.getNewValue() != null)
            {
                configuration.setEnabled(((Boolean) event.getNewValue())
                        .booleanValue());
            }
            else
            {
                configuration.setEnabled(false);
            }

            if (configuration.isEnabled())
            {
                startMonitoring();
            }
            else
            {
                stopMonitoring();
            }
        }
        else if (DEVICE_ADDRESS_PREFERENCE.equals(event.getProperty()))
        {
            configuration.setDeviceAddress((String) event.getNewValue());
        }
        else if (REDIRECT_EXTENSION_PREFERENCE.equals(event.getProperty()))
        {
            configuration.setRedirectExtension((String) event.getNewValue());
        }
    }

    public static BluetoothPlugin getDefault()
    {
        return instance;
    }

    public static void main(String args[]) throws Exception
    {
        BluetoothPlugin bluetooth;

        bluetooth = new BluetoothPlugin();

        bluetooth.startMonitoring();
    }
}
