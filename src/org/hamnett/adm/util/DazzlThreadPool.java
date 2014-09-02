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

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A handy thread pool to execute short running jobs asynchronously.
 * 
 * @author srt
 * @version $Id: AdmThreadPool.java,v 1.6 2006-06-01 16:23:23 rick Exp $
 */
public class DazzlThreadPool
{
    private static final int POOL_SIZE = 20;
    private static ThreadPoolExecutor pool = null;

    // hide constructor
    private DazzlThreadPool()
    {

    }

    private static ThreadPoolExecutor getPool()
    {
        if (pool == null)
        {
            pool = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE, 
                    50000L, TimeUnit.MILLISECONDS, 
                    new LinkedBlockingQueue<Runnable>());
        }

        return pool;
    }

    /**
     * Adds a new job to the pool. This will be picked up by the next available
     * active thread.
     */
    public static void addJob(Runnable runnable)
    {
        getPool().execute(runnable);
    }

    /**
     * Turn off the pool.
     */
    public static void shutdown()
    {
        if (pool != null)
        {
            pool.shutdown();
        }
    }
}
