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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.widgets.Event;
import org.hamnett.adm.Main;

import java.io.IOException;


public class PreferencesAction extends Action
{
    private final Log logger = LogFactory.getLog(getClass());
    //private PreferenceStore preferenceStore;

    /**
     * Creates a new PreferencesAction.
     * 
     * @param store The preference store to use.
     */
    public PreferencesAction(/*PreferenceStore store*/)
    {
        super(Messages.getString("PreferencesAction.label")); //$NON-NLS-1$
        //this.preferenceStore = store;
    }

    public void runWithEvent(Event event)
    {
        PreferenceDialog dialog;
        PreferenceStore store;
        
        dialog = new PreferenceDialog(Main.getShell(), createPreferenceManager());
        store = Main.getConfiguration(Main.getCurrentConfiguration());
        dialog.setPreferenceStore(store);
        
        dialog.open();

        try
        {
            //store.save(System.out,"---- Saving ----\n");
            store.save();
        }
        catch (IOException e)
        {
            logger.warn("Unable to store preferences.", e); //$NON-NLS-1$
        }
    }
    
    public PreferenceManager createPreferenceManager()
    {
        PreferenceManager preferenceManager = new PreferenceManager();
        
        Main.populatePreferenceManager(preferenceManager);
        
        return preferenceManager;
    }
}
