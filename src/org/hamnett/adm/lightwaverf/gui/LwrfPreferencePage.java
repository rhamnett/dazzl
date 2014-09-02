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
package org.hamnett.adm.lightwaverf.gui;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.hamnett.adm.lightwaverf.Lwrf;
import org.hamnett.adm.lightwaverf.Messages;

public class LwrfPreferencePage extends FieldEditorPreferencePage
{
    private StringFieldEditor browserPath;

    /**
     * Creates a new instance
     */
    public LwrfPreferencePage()
    {
        super(Messages.getString("LwrfPreferencePage.title"), GRID); //$NON-NLS-1$
        setDescription(org.hamnett.adm.lightwaverf.Messages
                .getString("LwrfPreferencePage.description")); //$NON-NLS-1$
    }

    protected void createFieldEditors()
    {
	Composite p;
	p = getFieldEditorParent();

	browserPath = new StringFieldEditor(
                Lwrf.BROWSER_PATH,
                Messages.getString("LwrfPreferencePage.browserPath"), getFieldEditorParent()); //$NON-NLS-1$
	browserPath.getLabelControl(p).setToolTipText(
		Messages.getString("LwrfPreferencePage.serverAddress.tooltip"));
        addField(browserPath);

    }
}
