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

import org.apache.commons.lang.ArrayUtils;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;

public final class PrefUtil
{
    @SuppressWarnings( "deprecation" )
    public static void setOnPreferenceClickListener( PreferenceActivity activity, String key,
            OnPreferenceClickListener listener )
    {
        Preference preference = activity.findPreference( key );
        if( preference != null )
            preference.setOnPreferenceClickListener( listener );
    }
    
    @SuppressWarnings( "deprecation" )
    public static void enablePreference( PreferenceActivity activity, String key, boolean enabled )
    {
        Preference preference = activity.findPreference( key );
        if( preference != null )
            preference.setEnabled( enabled );
    }
    
    @SuppressWarnings( "deprecation" )
    public static void removePreference( PreferenceActivity activity, String keyParent,
            String keyChild )
    {
        Preference parent = activity.findPreference( keyParent );
        Preference child = activity.findPreference( keyChild );
        if( parent instanceof PreferenceGroup && child != null )
            ( (PreferenceGroup) parent ).removePreference( child );
    }

    public static void validateListPreference( Resources res, SharedPreferences prefs, String key, int defaultResId, int arrayResId )
    {
        String value = prefs.getString( key, null );
        String defValue = res.getString( defaultResId );
        String[] validValues = res.getStringArray( arrayResId );
        if( !ArrayUtils.contains( validValues, value ) )
        {
            prefs.edit().putString( key, defValue ).commit();
        }
    }
}
