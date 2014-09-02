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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.hamnett.adm.bluetooth.Bluetooth;
import org.hamnett.adm.bluetooth.Messages;

public class BluetoothPreferencePage extends FieldEditorPreferencePage
{
    private BooleanFieldEditor enabled;
    private StringFieldEditor deviceAddress;
    private StringFieldEditor redirectExtension;

    /**
     * Creates a new instance
     */
    public BluetoothPreferencePage()
    {
        super(Messages.getString("BluetoothPreferencePage.title"), GRID); //$NON-NLS-1$
        setDescription(org.hamnett.adm.bluetooth.Messages
                .getString("BluetoothPreferencePage.description")); //$NON-NLS-1$
    }

    protected void createFieldEditors()
    {
        enabled = new BooleanFieldEditor(
                Bluetooth.ENABLED_PREFERENCE,
                Messages.getString("BluetoothPreferencePage.enabled"), getFieldEditorParent()); //$NON-NLS-1$
        addField(enabled);

        deviceAddress = new StringFieldEditor(
                Bluetooth.DEVICE_ADDRESS_PREFERENCE,
                Messages.getString("BluetoothPreferencePage.deviceAddress"), getFieldEditorParent()); //$NON-NLS-1$
        addField(deviceAddress);

        redirectExtension = new StringFieldEditor(
                Bluetooth.REDIRECT_EXTENSION_PREFERENCE,
                Messages.getString("BluetoothPreferencePage.redirectExtension"), getFieldEditorParent()); //$NON-NLS-1$
        addField(redirectExtension);

        //TODO ADD REDIRECT PREFIX
    }
}
