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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hamnett.adm.bluetooth.BluetoothDevice;
import org.hamnett.adm.bluetooth.BluetoothDiscoveryResult;
import org.hamnett.adm.bluetooth.BluetoothException;
import org.hamnett.adm.bluetooth.BluetoothPresenceListener;

public class MonitoringThreadImpl extends Thread
        implements
            DiscoveryListener,
            MonitoringThread
{
    private final Log logger = LogFactory.getLog(getClass());
    private static final String UNKNOWN_NAME = "<unknown>";
    private static final long DEFAULT_INTERVAL = 30000;

    private final DiscoveryAgent discoveryAgent;
    private final LocalDevice localDevice;
    private String localAddress;
    private String localName;
    
    private BluetoothDiscoveryResult lastDiscoveryResult;
    private Collection previousDevices;
    private Collection currentDevices;
    private final List monitoringListeners;
    private boolean discoveryInProgress;
    private boolean paused;
    private boolean die;
    private long interval = DEFAULT_INTERVAL;
    private int pass;

    /**
     * Creates a new instance.
     * 
     */
    MonitoringThreadImpl() throws BluetoothException
    {
        super("Bluetooth Monitoring"); //$NON-NLS-1$
        try
        {
            this.localDevice = LocalDevice.getLocalDevice();
        }
        catch (BluetoothStateException e)
        {
            throw new BluetoothException(e.getMessage());
        }

        this.localAddress = localDevice.getBluetoothAddress();
        this.localName = localDevice.getFriendlyName();
        this.discoveryAgent = localDevice.getDiscoveryAgent();

        logger.debug("LocalAdress: " + localAddress + ", Name: " + localName); //$NON-NLS-1$ //$NON-NLS-2$
        
        this.monitoringListeners = new ArrayList();
        this.discoveryInProgress = false;
        this.pass = 0;
    }

    /**
     * Creates a new instance.
     * 
     * @param discoveryAgent the bluetooth discovery agent to use
     * @param interval the time in ms to sleep between two discovery passes
     */
    MonitoringThreadImpl(DiscoveryAgent discoveryAgent, long interval) throws BluetoothException
    {
        this();
        this.interval = interval;
    }

    // implementation of the MonitoringThread interface

    public void run()
    {
        int attempts = 0;

        this.paused = false;
        this.die = false;
        this.discoveryInProgress = false;

        while (!die)
        {
            if (attempts > 0)
            {
                try
                {
                    sleep(interval);
                }
                catch (InterruptedException e1)
                {
                    // hmm ok
                }
            }
            attempts++;

            if (paused)
            {
                continue;
            }

            if (discoveryInProgress)
            {
                logger.debug("Discovery still in progress. skipping."); //$NON-NLS-1$
                continue;
            }

            this.discoveryInProgress = true;
            try
            {
                this.previousDevices = this.currentDevices;
                this.currentDevices = new ArrayList();
                discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);
            }
            catch (BluetoothStateException e)
            {
                this.discoveryInProgress = false;
                logger.debug("Unable to start discovery. skipping.", e); //$NON-NLS-1$
                continue;
            }
            try
            {
                synchronized (this)
                {
                    wait(); // wait for result
                }
            }
            catch (InterruptedException e)
            {
                // ok
            }
        }
    }

    public BluetoothDiscoveryResult getDiscoveryResult()
    {
        return lastDiscoveryResult;
    }

    public void pause()
    {
        this.paused = true;
    }

    public void unpause()
    {
        this.paused = false;
    }

    public void addMonitoringListener(
            BluetoothPresenceListener monitoringListener)
    {
        synchronized (monitoringListeners)
        {
            if (!monitoringListeners.contains(monitoringListener))
            {
                monitoringListeners.add(monitoringListener);
            }
        }
    }

    public void removeMonitoringListener(
            BluetoothPresenceListener monitoringListener)
    {
        synchronized (monitoringListeners)
        {
            if (monitoringListeners.contains(monitoringListener))
            {
                monitoringListeners.remove(monitoringListener);
            }
        }
    }

    // implementation of javax.bluetooth.DiscoveryListener

    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod)
    {
        synchronized (currentDevices)
        {
            currentDevices.add(btDevice);
        }
    }

    public void servicesDiscovered(int transID, ServiceRecord[] servRecord)
    {
        // not used
    }

    public void serviceSearchCompleted(int transID, int respCode)
    {
        // not used
    }

    public void inquiryCompleted(int discType)
    {
        boolean error;

        error = (discType != DiscoveryListener.INQUIRY_COMPLETED);

        pass++;
        logger.debug("Discovery " + pass + " completed: " + currentDevices); //$NON-NLS-1$ //$NON-NLS-2$

        lastDiscoveryResult = new BluetoothDiscoveryResultImpl(
                cloneCollection(currentDevices), error);

        // only check if discovery was successful
        if (!error && previousDevices != null)
        {
            checkDevices();
        }

        this.discoveryInProgress = false;

        synchronized (this)
        {
            this.notifyAll();
        }
    }

    // private helper methods

    /**
     * Checks which devices have appeared or disappeared since the last
     * discovery and calls fireDeviceAppeared and fireDeviceDisappeared
     * accordingly.
     */
    private void checkDevices()
    {
        synchronized (previousDevices)
        {
            Iterator previousDeviceIterator = previousDevices.iterator();
            while (previousDeviceIterator.hasNext())
            {
                RemoteDevice device;

                device = (RemoteDevice) previousDeviceIterator.next();
                if (!currentDevices.contains(device))
                {
                    fireDeviceDisappeared(device);
                }
            }
        }

        synchronized (currentDevices)
        {
            Iterator currentDeviceIterator = currentDevices.iterator();
            while (currentDeviceIterator.hasNext())
            {
                RemoteDevice device;

                device = (RemoteDevice) currentDeviceIterator.next();
                if (!previousDevices.contains(device))
                {
                    fireDeviceAppeared(device);
                }
            }
        }
    }

    private BluetoothDevice remoteDevice2BluetoothDevice(RemoteDevice device)
    {
        String address;
        String name;

        address = device.getBluetoothAddress();
        try
        {
            name = device.getFriendlyName(false);
        }
        catch (IOException e)
        {
            name = UNKNOWN_NAME;
        }

        return new BluetoothDeviceImpl(address, name);
    }

    /**
     * Inform listeners about a device that appeared.
     * 
     * @param device the device that appeared.
     */
    private void fireDeviceAppeared(RemoteDevice device)
    {
        BluetoothDevice bluetoothDevice;

        bluetoothDevice = remoteDevice2BluetoothDevice(device);
        logger.debug("Discovered device: " + bluetoothDevice); //$NON-NLS-1$

        synchronized (monitoringListeners)
        {
            Iterator i = monitoringListeners.iterator();
            while (i.hasNext())
            {
                BluetoothPresenceListener ml = (BluetoothPresenceListener) i
                        .next();
                ml.deviceAppeared(bluetoothDevice);
            }
        }
    }

    /**
     * Inform listeners about a device that disappeared.
     * 
     * @param device the device that disappeared.
     */
    private void fireDeviceDisappeared(RemoteDevice device)
    {
        BluetoothDevice bluetoothDevice;

        bluetoothDevice = remoteDevice2BluetoothDevice(device);
        logger.debug("Lost device: " + bluetoothDevice); //$NON-NLS-1$

        synchronized (monitoringListeners)
        {
            Iterator i = monitoringListeners.iterator();
            while (i.hasNext())
            {
                BluetoothPresenceListener ml = (BluetoothPresenceListener) i
                        .next();
                ml.deviceDisappeared(bluetoothDevice);
            }
        }
    }

    /**
     * Creates a copy of the given Collection.
     * 
     * @param c the Collection to clone
     * @return the cloned Collection
     */
    private Collection cloneCollection(Collection c)
    {
        Collection result;

        result = new ArrayList();
        synchronized (c)
        {
            Iterator i;

            i = c.iterator();
            while (i.hasNext())
            {
                result.add(i.next());
            }
        }

        return result;
    }
}
