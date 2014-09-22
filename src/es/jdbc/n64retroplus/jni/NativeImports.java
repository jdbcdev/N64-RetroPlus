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
package es.jdbc.n64retroplus.jni;

import es.jdbc.n64retroplus.CoreInterface;

/**
 * Call-ins made from the native ae-imports library to Java. Any function names changed here should
 * also be changed in the corresponding C code, and vice versa.
 * 
 * @see jni/ae-bridge/ae_imports.cpp
 * @see CoreInterface
 */
public class NativeImports extends CoreInterface
{
    /**
     * Callback for when an emulator's state/parameter has changed.
     * 
     * @param paramChanged The changed parameter's ID.
     * @param newValue The new value of the changed parameter.
     * @see jni/ae-bridge/ae_imports.cpp
     */
    public static void stateCallback( int paramChanged, int newValue )
    {
        synchronized( sStateCallbackLock )
        {
            for( int i = sStateCallbackListeners.size(); i > 0; i-- )
            {
                // Traverse the list backwards in case any listeners remove themselves
                sStateCallbackListeners.get( i - 1 ).onStateCallback( paramChanged, newValue );
            }
        }
    }
    
    /**
     * Returns the hardware type of a device. If the user has overridden the detected type in the
     * settings, the overridden value is returned.
     * 
     * @return The hardware type of the device, or the user-specified type if the user has
     *         overridden it in the settings.
     * @see jni/ae-bridge/ae_imports.cpp
     */
    public static int getHardwareType()
    {
        int autoDetected = sAppData.hardwareInfo.hardwareType;
        int overridden = sUserPrefs.videoHardwareType;
        return ( overridden < 0 ) ? autoDetected : overridden;
    }
    
    /**
     * Returns the custom polygon offset.
     * 
     * @return The polygon offset value.
     * @see jni/ae-bridge/ae_imports.cpp
     */
    public static float getCustomPolygonOffset()
    {
        return sUserPrefs.videoPolygonOffset;
    }
}
