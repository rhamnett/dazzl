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
package org.hamnett.adm.lightwaverf.impl;

import org.hamnett.adm.lightwaverf.LwrfConfiguration;

import java.util.Collection;

/**
 * Default implementation of CrmConfiguration.
 * 
 * @author mmbrich
 * @version $Id: CrmConfigurationImpl.java,v 1.4 2006-06-13 20:19:53 rick Exp $
 */
public class LwrfConfigurationImpl implements LwrfConfiguration
{
    private boolean enabled;
    private String browserPath;
    private Collection channels;
    

    public String getBrowserPath()
    {
        return browserPath;
    }

    public void setBrowserPath(String browserPath)
    {
        this.browserPath = browserPath;
    }
}
