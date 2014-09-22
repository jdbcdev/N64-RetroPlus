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
 * 
 * References: http://developer.android.com/guide/topics/ui/settings.html#CustomSaveState
 */
package es.jdbc.n64retroplus.persistent;

import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.preference.Preference.BaseSavedState;

public class SavedStringState extends BaseSavedState
{
    String mValue;
    
    public SavedStringState( Parcel source )
    {
        super( source );
        mValue = source.readString();
    }
    
    @Override
    public void writeToParcel( Parcel dest, int flags )
    {
        super.writeToParcel( dest, flags );
        dest.writeString( mValue );
    }
    
    public SavedStringState( Parcelable superState )
    {
        super( superState );
    }
    
    public static final Parcelable.Creator<SavedStringState> CREATOR = new Parcelable.Creator<SavedStringState>()
    {
        @Override
        public SavedStringState createFromParcel( Parcel in )
        {
            return new SavedStringState( in );
        }

        @Override
        public SavedStringState[] newArray( int size )
        {
            return new SavedStringState[size];
        }
    };
    
    public static Parcelable onSaveInstanceState( final Parcelable superState,
            Preference preference, String value )
    {
        if( preference.isPersistent() )
        {
            // No need to save instance state since it's persistent
            return superState;
        }
        
        final SavedStringState myState = new SavedStringState( superState );
        myState.mValue = value;
        return myState;
    }
}
