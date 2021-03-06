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

import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceManager;

public interface DazzlPlugin
{
    /**
     * Registeres the preference panels for this plugin with the global
     * preference manager.
     * 
     * @param preferenceManager the global preference manager to register with.
     */
    void registerPreferenceNodes(PreferenceManager preferenceManager);

    /**
     * Sets the default values for this plugin's preferences on the given
     * preference store.
     * 
     * @param store the preference store to set default values on.
     */
    void setConfigurationDefaults(IPreferenceStore store);

    /**
     * Loads the initial configuration based on the given preference store.
     * 
     * @param store the preference store to use for loading preferences.
     */
    void loadConfiguration(IPreferenceStore store);

    /**
     * Returns a list of IContributionItem and IAction objects that this plugin
     * contributes to ADM's main menu.
     * 
     * @return a list of IContributionItem and IAction objects that this plugin
     *         contributes to ADM's main menu or <code>null</code> if none are
     *         contributed.
     */
    List getMenuContributions();

    /**
     * Starts this plugin.
     * 
     * @throws DependencyNotSatisfiedException if one of this plugin's
     *             dependencies is not satisified.
     */
    void startup() throws DependencyNotSatisfiedException;

    /**
     * Stops this plugin.
     */
    void shutdown();
}
