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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;

/**
 * Holds list oft EventListeners and allows event emitters fire events.
 * 
 * @author srt
 * @version $Id: EventListenerList.java,v 1.4 2006-06-27 15:10:25 rick Exp $
 */
public class EventListenerList
{
    private Log logger = LogFactory.getLog(getClass());
    private List listeners;

    public EventListenerList()
    {
        this.listeners = new ArrayList();
    }

    /**
     * Adds the given event listener if it has not already been added.
     * 
     * @param listener the listener to add.
     */
    public void add(EventListener listener)
    {
        synchronized (listeners)
        {
            if (!listeners.contains(listener))
            {
                listeners.add(listener);
            }
        }
    }

    /**
     * Removes the given event listener.
     * 
     * @param listener the listener to remove.
     */
    public void remove(EventListener listener)
    {
        synchronized (listeners)
        {
            listeners.remove(listener);
        }
    }

    /**
     * Fires the given event to all registered event listeners that implement or
     * extend the given class or interface.
     * 
     * @param clazz the class or interface of the listeners
     * @param methodName the name of the event handling method
     * @param event the event to fire
     */
    public void fireEvent(Class clazz, String methodName, Object event)
    {
        Iterator i;
        Method method;

        try
        {
            method = clazz.getMethod(methodName, new Class[]{event.getClass()});
        }
        catch (NoSuchMethodException e)
        {
            logger.error("Class " + clazz + " has no method " + methodName //$NON-NLS-1$ //$NON-NLS-2$
                    + " (" + event.getClass() + ")", e); //$NON-NLS-1$ //$NON-NLS-2$
            return;
        }

        synchronized (listeners)
        {
            i = listeners.iterator();

            while (i.hasNext())
            {
                EventListener listener;

                listener = (EventListener) i.next();

                if (!clazz.isAssignableFrom(listener.getClass()))
                {
                    continue;
                }

                try
                {
                    method.invoke(listener, new Object[]{event});
                }
                catch (IllegalAccessException e)
                {
                    logger.error("Unable to invoke method " + methodName //$NON-NLS-1$
                            + " on listener", e); //$NON-NLS-1$
                    return;
                }
                catch (InvocationTargetException e)
                {
                    logger.error("Unable to invoke method " + methodName //$NON-NLS-1$
                            + " on listener", e); //$NON-NLS-1$
                    return;
                }
            }
        }
    }
}
