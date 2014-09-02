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
package org.hamnett.adm;

public interface Dazzl {

    static final String UPDATE_URL = "http://adm.hamnett.org/cgi-bin/update.cgi";

    static final String DAZZL_PREFERENCE_DIR = ".dazzl";
    static final String DAZZL_PREFERENCE_STORE_PREFIX = "cfg";
    static final String DAZZL_APPLICATION_UID = "dazzl.uid";
    static final String DAZZL_CONFIGURATION_NAME = "configuration.name";
    static final String DAZZL_APPLICATION_CONTEXT = "plugins.xml";

    static final String DAZZL_VERSION = "0.1";
    static final String DAZZL_BUILD_VERSION = "02032014";
}