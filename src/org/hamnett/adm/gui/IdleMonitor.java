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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.hamnett.adm.util.EventListenerList;

public class IdleMonitor implements Runnable
{
    public static final String IDLE_PROPERTY = "idle";

    private static final int DEFAULT_IDLE_THRESHOLD = 5;
    private static final int SCAN_INTERVAL = 10 * 1000;
    private static final int TOLERANCE = 5;

    private final Log logger = LogFactory.getLog(getClass());

    /**
     * Idle threshold is minutes.
     */
    private int idleThreshold;
    private Display display;
    private Point lastMousePosition;
    private long lastDetectedActive;
    private long lastChanged;
    private boolean idle;
    private EventListenerList listeners;

    public IdleMonitor(Display display, int idleThreshold)
    {
        this.display = display;
        this.idleThreshold = idleThreshold;
        this.lastMousePosition = new Point(-1, -1);
        this.lastDetectedActive = System.currentTimeMillis();
        this.lastChanged = lastDetectedActive;
        this.listeners = new EventListenerList();

        display.timerExec(SCAN_INTERVAL, this);
    }

    public IdleMonitor(Display display)
    {
        this(display, DEFAULT_IDLE_THRESHOLD);
    }

    /**
     * Sets the idle threshold.
     * 
     * @param idleThreshold minutes the user must be inactive before he is
     *            considered idle.
     */
    public void setIdleThreshold(int idleThreshold)
    {
        this.idleThreshold = idleThreshold;
    }

    /**
     * Returns the user's idle status.
     * 
     * @return <code>true</code> if the user is currently considered idle,
     *         <code>false</code> if he is not considered idle.
     */
    public boolean isIdle()
    {
        return idle;
    }

    /**
     * Clients can call this if they know the user's idle state.
     * 
     * @param idle <code>true</code> if the user is to be considered idle,
     *            <code>false</code> if he is to be considered not idle.
     */
    public void setIdle(boolean idle)
    {
        if (this.idle != idle)
        {
            if (idle)
            {
                lastDetectedActive = System.currentTimeMillis();
                startIdle();
            }
            else
            {
                stopIdle();
            }
        }
    }

    /**
     * Returns the point in time of the last idle status change.
     * 
     * @return the point in time of the last idle status change
     */
    public long getLastChanged()
    {
        return lastChanged;
    }

    public void addPropertyListener(IPropertyChangeListener l)
    {
        listeners.add(l);
    }

    public void removePropertyListener(IPropertyChangeListener l)
    {
        listeners.remove(l);
    }

    public void run()
    {
        Point mousePosition;
        long time;
        int distance;

        if (display.isDisposed())
        {
            return;
        }

        mousePosition = display.getCursorLocation();
        time = System.currentTimeMillis();

        distance = (int) Math.round(Math.sqrt(square(lastMousePosition.x
                - mousePosition.x)
                + square(lastMousePosition.y - mousePosition.y)));
        
        /*
         * Cancel idle mode if distance since last is greater than some random
         * drift.
         */
        if (distance > TOLERANCE)
        {
            lastDetectedActive = time;

            stopIdle();
        }
        else
        {
            if (time - lastDetectedActive > idleThreshold * 60 * 1000)
            {
                startIdle();
            }
        }

        lastMousePosition = mousePosition;

        display.timerExec(SCAN_INTERVAL, this);
    }

    private void startIdle()
    {
        if (!idle)
        {
            logger.trace("Entered idle mode");

            idle = true;
            lastChanged = System.currentTimeMillis();

            listeners.fireEvent(IPropertyChangeListener.class,
                    "propertyChange", new PropertyChangeEvent(this,
                            IDLE_PROPERTY, Boolean.FALSE, Boolean.TRUE));
        }
    }

    private void stopIdle()
    {
        if (idle)
        {
            logger.debug("Left idle mode");

            idle = false;
            lastChanged = System.currentTimeMillis();

            listeners.fireEvent(IPropertyChangeListener.class,
                    "propertyChange", new PropertyChangeEvent(this,
                            IDLE_PROPERTY, Boolean.TRUE, Boolean.FALSE));
        }
    }

    private static int square(int n)
    {
        return n * n;
    }
}
