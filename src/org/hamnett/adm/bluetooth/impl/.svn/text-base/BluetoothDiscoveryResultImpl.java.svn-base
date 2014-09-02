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

import org.hamnett.adm.bluetooth.BluetoothDiscoveryResult;

import java.io.Serializable;
import java.util.Collection;

public class BluetoothDiscoveryResultImpl
        implements
            BluetoothDiscoveryResult,
            Serializable
{
    /**
     * Serial version identifier
     */
    private static final long serialVersionUID = -174450288251454319L;
    private final Collection devices;
    private final boolean error;

    /**
     * Creates a new instance.
     * 
     * @param devices collection of BluetoothDevice objects that have been
     *            discovered
     * @param error <code>true</code> if an error occured, <code>false</code>
     *            otherwise
     */
    BluetoothDiscoveryResultImpl(Collection devices, boolean error)
    {
        this.devices = devices;
        this.error = error;
    }

    public Collection getDevices()
    {
        return devices;
    }

    public boolean isError()
    {
        return error;
    }

}
