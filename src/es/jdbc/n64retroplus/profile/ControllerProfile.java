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
package es.jdbc.n64retroplus.profile;

import es.jdbc.n64retroplus.input.map.InputMap;
import es.jdbc.n64retroplus.persistent.ConfigFile.ConfigSection;

public class ControllerProfile extends Profile
{
    public static final int DEFAULT_DEADZONE = 0;
    public static final int DEFAULT_SENSITIVITY = 100;
    
    private static final String KEY_MAP = "map";
    private static final String KEY_DEADZONE = "deadzone";
    private static final String KEY_SENSITIVITY = "sensitivity";
    
    public InputMap getMap()
    {
        return new InputMap( get( KEY_MAP ) );
    }
    
    public int getDeadzone()
    {
        return getInt( KEY_DEADZONE, DEFAULT_DEADZONE );
    }
    
    public int getSensitivity()
    {
        return getInt( KEY_SENSITIVITY, DEFAULT_SENSITIVITY );
    }
    
    public void putMap( InputMap map )
    {
        put( KEY_MAP, map.serialize() );
    }
    
    public void putDeadzone( int deadzone )
    {
        putInt( KEY_DEADZONE, deadzone );
    }
    
    public void putSensitivity( int sensitivity )
    {
        putInt( KEY_SENSITIVITY, sensitivity );
    }
    
    public ControllerProfile( boolean isBuiltin, String name, String comment )
    {
        super( isBuiltin, name, comment );
    }
    
    public ControllerProfile( boolean isBuiltin, ConfigSection section )
    {
        super( isBuiltin, section );
    }
}