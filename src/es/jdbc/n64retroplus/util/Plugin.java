/**
 * N64 Retro+, an N64 emulator for the Android platform
 * 
 * Copyright (C) 2014 JDBC
 * 
 * This file is part of N64 Retro+.
 * 
 * N64 Retro+ is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * N64 Retro+ is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with N64 Retro+. If
 * not, see <http://www.gnu.org/licenses/>.
 * 
 * Authors: jdbc
 */
package es.jdbc.n64retroplus.util;

import es.jdbc.n64retroplus.profile.Profile;
import android.content.SharedPreferences;

/**
 * A tiny class containing inter-dependent plug-in information.
 */
public class Plugin
{
    /** The name of the plug-in, with extension, without parent directory. */
    public final String name;
    
    /** The full absolute path name of the plug-in. */
    public final String path;
    
    /** True if the plug-in is enabled. */
    public final boolean enabled;
    
    /**
     * Instantiates a new plug-in meta-info object.
     * 
     * @param prefs The shared preferences containing plug-in information.
     * @param libsDir The directory containing the plug-in file.
     * @param key The shared preference key for the plug-in.
     */
    public Plugin( SharedPreferences prefs, String libsDir, String key )
    {
        name = prefs.getString( key, "" );
        enabled = name.endsWith( ".so" );
        path = enabled ? libsDir + name : "dummy";
    }
    
    /**
     * Instantiates a new plug-in meta-info object.
     * 
     * @param profile The shared preferences containing plug-in information.
     * @param libsDir The directory containing the plug-in file.
     * @param key The shared preference key for the plug-in.
     */
    public Plugin( Profile profile, String libsDir, String key )
    {
        name = profile.get( key, "" );
        enabled = name.endsWith( ".so" );
        path = enabled ? libsDir + name : "dummy";
    }
}