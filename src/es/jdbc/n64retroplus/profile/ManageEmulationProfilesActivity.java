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

import es.jdbc.n64retroplus.Keys;
import es.jdbc.n64retroplus.persistent.UserPrefs;
import android.content.Intent;

public class ManageEmulationProfilesActivity extends ManageProfilesActivity
{
    @Override
    protected String getConfigFilePath( boolean isBuiltin )
    {
        return isBuiltin ? mAppData.emulationProfiles_cfg : mUserPrefs.emulationProfiles_cfg;
    }
    
    @Override
    protected String getNoDefaultProfile()
    {
        return UserPrefs.DEFAULT_EMULATION_PROFILE_DEFAULT;
    }
    
    @Override
    protected String getDefaultProfile()
    {
        return mUserPrefs.getEmulationProfileDefault();
    }
    
    @Override
    protected void putDefaultProfile( String name )
    {
        mUserPrefs.putEmulationProfileDefault( name );
    }
    
    @Override
    protected void onEditProfile( Profile profile )
    {
        Intent intent = new Intent( this, EmulationProfileActivity.class );
        intent.putExtra( Keys.Extras.PROFILE_NAME, profile.name );
        startActivity( intent );
    }
}
