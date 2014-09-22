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
package es.jdbc.n64retroplus;

/**
 * Just a simple class to consolidate all strings used as keys throughout the app. Keys are
 * typically used to pass data between activities (in the extras bundle) and to persist data (in
 * shared preferences, config files, etc.).
 */
public class Keys
{
    /**
     * Keys used to pass data to activities via the intent extras bundle. It's good practice to
     * namespace the keys to avoid conflicts with other apps. By convention this is usually the
     * package name but it's not a strict requirement. We'll use the fully qualified name of this
     * class since it's easy to get.
     */
    public static class Extras
    {
        private static final String NAMESPACE = Extras.class.getCanonicalName() + ".";
        //@formatter:off
        public static final String ROM_PATH     = NAMESPACE + "ROM_PATH";
        public static final String ROM_MD5      = NAMESPACE + "ROM_MD5";
        public static final String CHEAT_ARGS   = NAMESPACE + "CHEAT_ARGS";
        public static final String DO_RESTART   = NAMESPACE + "DO_RESTART";
        public static final String PROFILE_NAME = NAMESPACE + "PROFILE_NAME";
        //@formatter:on
    }
    
    public static class Prefs
    {
        
    }
    
    public static class Config
    {
        
    }
}
