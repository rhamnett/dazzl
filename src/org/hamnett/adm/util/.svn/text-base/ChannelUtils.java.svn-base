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

import java.util.Collection;

public class ChannelUtils
{
    // hide constructor
    private ChannelUtils()
    {

    }

    /**
     * Checks if a channel with the given name is contained in the given
     * Collection.
     * 
     * @param c the collection to check
     * @param channelName the name of the channel we look for
     * @return <code>true</code> if the channel is contained or the collection
     *         is empty, <code>false</code> otherwise.
     */
    public static boolean contains(Collection c, String channelName)
    {
        String s;

        // a channel should never be null
        if (channelName == null)
        {
            return false;
        }

        // empty channel list means react to every channel
        // maybe we should change this...
        synchronized (c)
        {
            if (c.isEmpty())
            {
                return true;
            }
        }

        if (channelName.indexOf('-') != -1)
        {
            s = channelName.substring(0, channelName.indexOf('-'));
        }
        else
        {
            s = channelName;
        }

        synchronized (c)
        {
            if (c.contains(s))
            {
                return true;
            }
        }

        return false;
    }
}
