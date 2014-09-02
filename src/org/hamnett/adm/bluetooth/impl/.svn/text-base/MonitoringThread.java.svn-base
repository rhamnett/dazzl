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
import org.hamnett.adm.bluetooth.BluetoothPresenceListener;

public interface MonitoringThread extends Runnable
{
    /**
     * Returns the result of the last discovery.
     * 
     * @return the result of the last discovery.
     */
    BluetoothDiscoveryResult getDiscoveryResult();

    /**
     * Suspends monitoring.
     */
    void pause();

    /**
     * Resumes monitoring.
     */
    void unpause();

    /**
     * Adds a new BluetoothMonitoringListener that gets informed about devices
     * that appear or dissapear.
     * 
     * @param monitoringListener the BluetoothMonitoringListener to add.
     */
    void addMonitoringListener(BluetoothPresenceListener monitoringListener);

    /**
     * Removes a BluetoothMonitoringListener.
     * 
     * @param monitoringListener the BluetoothMonitoringListener to remove.
     */
    void removeMonitoringListener(BluetoothPresenceListener monitoringListener);
    
    void start();
}
