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
package org.hamnett.adm.lightwaverf;

import java.util.Collection;

/**
 * Configuration for crm.
 * 
 * @author srt
 * @version $Id: CrmConfiguration.java,v 1.2 2005-10-16 13:53:21 rick Exp $
 */
public interface LwrfConfiguration
{
 
    /**
     * Returns the executable of the browser.
     * 
     * @return the executable of the browser.
     */
    String getBrowserPath();

    /**
     * Sets the path of the browser.
     * 
     * @param browserPath the path of the browser.
     */
    void setBrowserPath(String browserPath);

 
}
