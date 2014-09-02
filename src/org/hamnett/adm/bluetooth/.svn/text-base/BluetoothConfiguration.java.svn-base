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

/**
 * Configuration for bluetooth.
 * 
 * @author srt
 * @version $Id: BluetoothConfiguration.java,v 1.2 2005-08-13 00:19:28 srt Exp $
 */
public interface BluetoothConfiguration
{
    /**
     * Determines if bluetooth monitoring is enabled.
     * 
     * @return <code>true</code> if bluetooth monitoring is enabled,
     *         <code>false</code> otherwise.
     */
    boolean isEnabled();

    /**
     * Enables or disables bluetooth monitoring.
     * 
     * @param enabled <code>true</code> to enable bluetooth monitoring,
     *            <code>false</code> to diable it.
     */
    void setEnabled(boolean enabled);

    /**
     * Returns the bluetooth address of the device to monitor.
     * 
     * @return the bluetooth address of the device to monitor.
     */
    String getDeviceAddress();

    /**
     * Sets the bluetooth address of the device to monitor.
     * 
     * @param deviceAddress the bluetooth address of the device to monitor.
     */
    void setDeviceAddress(String deviceAddress);

    /**
     * Returns the extension to redirect new incoming calls to if the monitored
     * device is not in range.
     * 
     * @return the extension to redirect new incoming calls to if the monitored
     *         device is not in range.
     */
    String getRedirectExtension();

    /**
     * Sets the extension to redirect new incoming calls to if the monitored
     * device is not in range.
     * 
     * @param redirectExtension the extension to redirect new incoming calls to
     *            if the monitored device is not in range.
     */
    void setRedirectExtension(String redirectExtension);
}
