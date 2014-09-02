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
package org.hamnett.adm.gui;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.hamnett.adm.Main;

public class ConfigurationMenuManager extends MenuManager
{
    private Action addAction;
    private Action editAction;
    private Action removeAction;

    /** 
     * Create the 'Configurations' menu and its submenus. 
     * 
     * @param preferenceStores The list of configuration names
     * @param currentPreferenceStore The name of the current configuration
     */
    public ConfigurationMenuManager(Set<String> preferenceStores, String currentPreferenceStore)
    {
        super(Messages.getString("ConfigurationsAction.label")); //$NON-NLS-1$

        buildMenu(preferenceStores, currentPreferenceStore);
    }

    /**
     * Always visible, even if no configuration is available.
     */
    public boolean isVisible()
    {
        return true;
    }

    /**
     * Always enabled, even if no configuration is available.
     */
    public boolean isEnabled()
    {
        return true;
    }

    /** 
     * (re)Build the 'Configurations' menu. 
     * 
     * @param configurations The list of configuration names
     * @param currentConfiguration The name of the current configuration.
     */
    public void buildMenu(final Set<String>configurations, final String currentConfiguration)
    {
        removeAll();

        TreeSet<String> sortedConfigurations = new TreeSet<String>(configurations);

        Iterator<String> configurationIterator = sortedConfigurations.iterator();
        while (configurationIterator.hasNext())
        {
            String configuration = configurationIterator.next();

            if (configuration == currentConfiguration)
            {
                add(
                        new Action(configuration,
                            ImageDescriptor.createFromFile
                            (ConfigurationMenuManager.class, "icons/current.png") //$NON-NLS-1$
                            )
                        {
                            public void run()
                            {
                                Main.setCurrentConfiguration(getText());
                                buildMenu(Main.getConfigurationList(), Main.getCurrentConfiguration());
                            }
                        }
                   );
            }
            else
            {
                add(
                        new Action(configuration)
                        {
                            public void run()
                            {
                                Main.setCurrentConfiguration(getText());
                                buildMenu(Main.getConfigurationList(), Main.getCurrentConfiguration());
                            }
                        }
                   );
            }
        }

        add(new Separator());

        addAction = new Action
            (Messages.getString("ConfigurationsAddAction.label")) //$NON-NLS-1$
            {
                public void run()
                {
                    String id;
                    id = Main.createConfiguration();
                    Main.editConfiguration(id);
                    buildMenu(Main.getConfigurationList(), Main.getCurrentConfiguration());
                }
            };
        add(addAction);

        editAction = new PreferencesAction();
        add(editAction);

        removeAction = new Action
            (Messages.getString("ConfigurationsRemoveAction.label")) //$NON-NLS-1$
            {
                public void run()
                {
                    Main.deleteConfiguration (Main.getCurrentConfiguration());
                    buildMenu(Main.getConfigurationList(), Main.getCurrentConfiguration());
                }
            };
        add(removeAction);

        update(true);
    }
}
