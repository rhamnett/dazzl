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
package org.hamnett.adm.bluetooth;

public interface BluetoothDevice
{
    /**
     * Returns the Bluetooth address of this device, for example "003B3A64C759".
     * 
     * @return the Bluetooth address of this device.
     */
    String getAddress();

    /**
     * Returns the user friendly name of this device as assigned by the owner of
     * the device.
     * 
     * @return the user friendly name of this device.
     */
    String getName();
}
