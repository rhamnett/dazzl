package org.hamnett.adm.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by IntelliJ IDEA.
 * User: rick
 * Date: Jun 24, 2006
 * Time: 11:34:04 PM
 */
public class ConfigFilenameFilter implements FilenameFilter {

    public boolean accept(File dir, String name) {
        return (name.startsWith("cfg"));
    }

}
