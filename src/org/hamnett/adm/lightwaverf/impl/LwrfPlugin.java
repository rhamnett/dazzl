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
package org.hamnett.adm.lightwaverf.impl;

import java.util.ArrayList;
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
import org.hamnett.adm.gui.LightDimAction;
import org.hamnett.adm.gui.dialog.LightSlider;
import org.hamnett.adm.lightwaverf.Lwrf;
import org.hamnett.adm.lightwaverf.LwrfConfiguration;
import org.hamnett.adm.lightwaverf.Messages;
import org.hamnett.adm.lightwaverf.gui.LwrfPreferencePage;
import org.hamnett.adm.util.ChannelUtils;

/**
 * Open a browser window with the user's details (using the callerId in the querystring)
 */
public class LwrfPlugin implements DazzlPlugin, Lwrf, IPropertyChangeListener, Runnable {

    //private Asterisk asterisk;
    private static LwrfConfiguration configuration;
    //TODO
    private static final String UNKNOWN_NAME = "<unknown>";
    
    private final Log logger = LogFactory.getLog(getClass());


    /*public void setAsterisk(Asterisk asterisk) {
        this.asterisk = asterisk;
    }*/

    public void popup() {
    }

    public LwrfPlugin() {
        configuration = new LwrfConfigurationImpl();
    }

    public void registerPreferenceNodes(PreferenceManager preferenceManager) {
        String className;
        className = LwrfPreferencePage.class.getName();
        PreferenceNode lwrfNode = new PreferenceNode("lwrf", //$NON-NLS-1$
                Messages.getString("LwrfPreferencePage.title"), null, //$NON-NLS-1$
                className);
        preferenceManager.addToRoot(lwrfNode);
    }

    public void setConfigurationDefaults(IPreferenceStore store) {
        store.setDefault(BROWSER_PATH, "");
    }

    public void loadConfiguration(IPreferenceStore store) {

        configuration.setBrowserPath(store
                .getString(BROWSER_PATH));
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (BROWSER_PATH.equals(event.getProperty())) {
            configuration.setBrowserPath((String) event.getNewValue());
        }
    }

    public List getMenuContributions() {
    	List items;

		items = new ArrayList();
		items.add(new LightDimAction(configuration));
		return items;

    }

    public void startup() throws DependencyNotSatisfiedException {

    }

    public void shutdown() {
    }



    public void run() {
    }

  

}
