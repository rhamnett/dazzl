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
package org.hamnett.adm.bluetooth.gui;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.hamnett.adm.bluetooth.Messages;

public class BluetoothDisabledPreferencePage extends PreferencePage
{
    public BluetoothDisabledPreferencePage()
    {
        setDescription(Messages
                .getString("BluetoothDisabledPreferencePage.description")); //$NON-NLS-1$
    }

    /**
     * Creates the controls for this page
     */
    protected Control createContents(Composite parent)
    {
        return parent;
    }

    /**
     * Called when user clicks Restore Defaults
     */
    protected void performDefaults()
    {
    }

    /**
     * Called when user clicks Apply or OK
     * 
     * @return boolean
     */
    public boolean performOk()
    {
        // Return true to allow dialog to close
        return true;
    }
}
