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

import org.eclipse.jface.action.Action;
import org.hamnett.adm.Main;

public class QuitAction extends Action
{
    private final SystemTray tray;
    
    public QuitAction(SystemTray tray)
    {
        super(Messages.getString("QuitAction.label")); //$NON-NLS-1$
        this.tray = tray;
    }

    public void run()
    {
        tray.cleanup();
        Main.getDefault().shutdown();
    }
}
