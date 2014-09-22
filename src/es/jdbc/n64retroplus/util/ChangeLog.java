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

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import es.jdbc.n64retroplus.R;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.SparseArray;

public final class ChangeLog
{
    private static final String ASSET_NAME = "changelog.txt";
    
    private final SparseArray<ChangeNote> mMap = new SparseArray<ChangeNote>();
    
    public ChangeLog( AssetManager assetManager )
    {
        // IO objects, initialize null to eliminate lint error
        InputStream in = null;
        byte[] buffer = null;
        
        // Extract the text
        try
        {
            in = assetManager.open( ASSET_NAME );
            buffer = new byte[in.available()];
            in.read( buffer );
        }
        catch( IOException ignored )
        {
        }
        finally
        {
            if( in != null )
            {
                try
                {
                    in.close();
                }
                catch( IOException ignored )
                {
                }
            }
        }
        
        if( buffer != null )
        {
            Pattern pattern = Pattern.compile( "\\[(\\d+?):(.*?)\\]([^\\[]*)", Pattern.DOTALL );
            Matcher matcher = pattern.matcher( new String( buffer ) );
            while( matcher.find() )
            {
                int code = Integer.parseInt( matcher.group( 1 ) );
                String version = matcher.group( 2 ).trim();
                String body = matcher.group( 3 ).trim();
                mMap.put( code, new ChangeNote( code, version, body ) );
            }
        }
    }
    
    public boolean show( Context context, int startVersion, int endVersion )
    {
        String message = "";        
        for( int i = endVersion; i >= startVersion; i-- )
        {
            ChangeNote note = mMap.get( i );
            if( note != null )
            {
                message += note.toString() + "\n\n";
            }
        }
        
        if( TextUtils.isEmpty( message ) )
        {
            return false;
        }
        else
        {
            CharSequence title = context.getString( R.string.changeLog_dialogTitle );
            new Builder( context ).setTitle( title ).setMessage( message ).create().show();
            return true;
        }
    }
    
    public static final class ChangeNote
    {
        public final int versionCode;
        public final String versionString;
        public final String body;
        
        public ChangeNote( int code, String ver, String b )
        {
            versionCode = code;
            versionString = ver;
            body = b;
        }
        
        @Override
        public String toString()
        {
            return versionString + "\n" + body;
        }
    }
}
