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
package org.hamnett.adm.util;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.hamnett.adm.Dazzl;
import org.hamnett.adm.Main;


public class CommonDialogs {
    // hide constructor
    private CommonDialogs() {
    }

    public static void showError(String message, Exception exception) {
        Status status;
        Dialog dialog;

        status = new Status(IStatus.ERROR, "dazzl", 1, message, exception);
        dialog = new ErrorDialog(Main.getShell(), null, null, status,
                IStatus.ERROR);
        dialog.open();
    }

    public static void showMessage(String title, String message) {
        Dialog dialog;

        dialog = new MessageDialog(Main.getShell(), title, null, message,
                MessageDialog.INFORMATION, new String[]{"OK"}, 0);
        dialog.open();
    }

    public static void showAbout() {
        Dialog dialog;

        dialog = new MessageDialog(Main.getShell(), "About - Dazzl", null,
                "Dazzl - http://github.com/dazzl\n\n(C) 2014 Richard Hamnett\n\nVersion: " + Dazzl.DAZZL_VERSION + "   " +
                "Build: " + Dazzl.DAZZL_BUILD_VERSION + "\n\n" +
                "Authors:\n\n\tRichard Hamnett\n",
                MessageDialog.INFORMATION, new String[]{"OK"}, 0);
        dialog.open();
    }



}
