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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hamnett.adm.util.CommonDialogs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class WebUpdateCheck implements Runnable
{
    private final Log logger = LogFactory.getLog(getClass());

    private URL url;
    private String uid;
    private String version;
    private String response;

    /**
     * Constructor
     * 
     * @param uid the applicationUID of this Dazzl instance.
     */
    public WebUpdateCheck(String uid)
    {
        try
        {
            this.url = new URL(Dazzl.UPDATE_URL);
        }
        catch (MalformedURLException e)
        {
            // this shouldn't happen as the URL is hard coded in Adm
            logger.error("Malformed Dazzl.UPDATE_URL fix the source", e);
        }

        this.uid = uid;
        this.version = Dazzl.DAZZL_VERSION;

    }

    /**
     * Checks if a new version of ADM is available and shows a dialog if this
     * version is out of date.
     */
    public void run()
    {
        if (url == null)
        {
            return;
        }

        try
        {
            //TODO include OS in update check
            String data = URLEncoder.encode("uid", "UTF-8") + "="
                    + URLEncoder.encode(uid, "UTF-8");
            data += "&" + URLEncoder.encode("version", "UTF-8") + "="
                    + URLEncoder.encode(version, "UTF-8");

            // Send data
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(
                    conn.getOutputStream());
            wr.write(data);
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null)
            {
                response = line;
            }
            wr.close();
            rd.close();
        }
        catch (Exception e)
        {
            // network error
            logger.info("Unable to check for new version: " + e.getMessage());
            return;
        }

        if (response.startsWith("Up to date") || response.equals(""))
        {
            logger.debug("Version is up to date");
        }
        else
        {
            logger.debug("Version out of date. Update needed");
            Main.getDisplay().asyncExec(new Runnable()
            {
                public void run()
                {
                    CommonDialogs.showMessage(
                            Messages.getString("updateAvailable.title"),
                            Messages.getString("updateAvailable.message")
                    );
                }
            });
        }
    }
}
