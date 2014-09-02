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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Conversion
{
    private static final String LIST_DELIMITER = ","; //$NON-NLS-1$

    // hide constructor
    private Conversion()
    {
        
    }
    
    public static int string2int(String s, int defaultValue)
    {
        try
        {
            return Integer.parseInt(s);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    public static long string2long(String s, long defaultValue)
    {
        try
        {
            return Long.parseLong(s);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    public static double string2double(String s, double defaultValue)
    {
        try
        {
            return Double.parseDouble(s);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    public static List string2List(String s)
    {
        List result;
        StringTokenizer st;

        result = new ArrayList();

        if (s != null && s.length() > 0)
        {
            st = new StringTokenizer(s, LIST_DELIMITER);
            while (st.hasMoreTokens())
            {
                result.add(st.nextToken().trim());
            }
        }

        return result;
    }
}
