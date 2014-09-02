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

import java.io.Serializable;

import org.hamnett.adm.bluetooth.BluetoothDevice;

public class BluetoothDeviceImpl implements BluetoothDevice, Serializable
{
    /**
     * Serial version identifier
     */
    private static final long serialVersionUID = 613287252048100330L;
    private final String address;
    private final String name;

    /**
     * Creates a new instance.
     * 
     * @param address bluetooth address of this device
     * @param name user friendly name of this device
     */
    BluetoothDeviceImpl(String address, String name)
    {
        this.address = address;
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public String getName()
    {
        return name;
    }

    public String toString()
    {
        StringBuffer sb;

        sb = new StringBuffer(getClass().getName() + ": ");
        sb.append("address='" + address + "'; ");
        sb.append("name='" + name + "'");

        return sb.toString();
    }
}
