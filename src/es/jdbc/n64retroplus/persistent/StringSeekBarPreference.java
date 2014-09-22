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
package es.jdbc.n64retroplus.persistent;

import es.jdbc.n64retroplus.util.SafeMethods;
import android.content.Context;
import android.util.AttributeSet;

/**
 * A simple extension of {@link SeekBarPreference} that persists to string rather than integer.
 */
public class StringSeekBarPreference extends SeekBarPreference
{
    public StringSeekBarPreference( Context context )
    {
        super( context );
    }
    
    public StringSeekBarPreference( Context context, AttributeSet attrs )
    {
        super( context, attrs );
    }
    
    @Override
    protected boolean persistInt( int value )
    {
        return persistString( String.valueOf( value ) );
    }
    
    @Override
    protected int getPersistedInt( int defaultReturnValue )
    {
        if( getSharedPreferences().contains( getKey() ) )
        {
            String strValue = getPersistedString( String.valueOf( defaultReturnValue ) );
            return SafeMethods.toInt( strValue, defaultReturnValue );
        }
        else
        {
            return defaultReturnValue;
        }
    }
}
