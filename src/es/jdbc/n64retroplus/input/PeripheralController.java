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
package es.jdbc.n64retroplus.input;

import java.util.ArrayList;

import es.jdbc.n64retroplus.CoreInterface;
import es.jdbc.n64retroplus.input.map.InputMap;
import es.jdbc.n64retroplus.input.map.PlayerMap;
import es.jdbc.n64retroplus.input.provider.AbstractProvider;
import es.jdbc.n64retroplus.jni.NativeExports;
import es.jdbc.n64retroplus.persistent.AppData;
import es.jdbc.n64retroplus.util.SafeMethods;
import es.jdbc.n64retroplus.util.Utility;
import android.annotation.TargetApi;
import android.util.FloatMath;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;

/**
 * A class for generating N64 controller commands from peripheral hardware (gamepads, joysticks,
 * keyboards, mice, etc.).
 */
public class PeripheralController extends AbstractController implements
        AbstractProvider.OnInputListener
{
    /** The map from hardware identifiers to players. */
    private final PlayerMap mPlayerMap;
    
    /** The map from input codes to N64/Mupen commands. */
    private final InputMap mInputMap;
    
    /** The analog deadzone, between 0 and 1, inclusive. */
    private final float mDeadzoneFraction;
    
    /** The analog sensitivity, the amount by which to scale stick values, nominally 1. */
    private final float mSensitivityFraction;
    
    /** The user input providers. */
    private final ArrayList<AbstractProvider> mProviders;
    
    /** The positive analog-x strength, between 0 and 1, inclusive. */
    private float mStrengthXpos;
    
    /** The negative analog-x strength, between 0 and 1, inclusive. */
    private float mStrengthXneg;
    
    /** The positive analog-y strength, between 0 and 1, inclusive. */
    private float mStrengthYpos;
    
    /** The negative analogy-y strength, between 0 and 1, inclusive. */
    private float mStrengthYneg;
    
    /**
     * Instantiates a new peripheral controller.
     * 
     * @param player    The player number, between 1 and 4, inclusive.
     * @param playerMap The map from hardware identifiers to players.
     * @param inputMap  The map from input codes to N64/Mupen commands.
     * @param inputDeadzone The analog deadzone in percent.
     * @param inputSensitivity The analog sensitivity in percent.
     * @param providers The user input providers. Null elements are safe.
     */
    public PeripheralController( int player, PlayerMap playerMap, InputMap inputMap,
            int inputDeadzone, int inputSensitivity, AbstractProvider... providers )
    {
        setPlayerNumber( player );
        
        // Assign the maps
        mPlayerMap = playerMap;
        mInputMap = inputMap;
        mDeadzoneFraction = ( (float) inputDeadzone ) / 100f;
        mSensitivityFraction = ( (float) inputSensitivity ) / 100f;
        
        // Assign the non-null input providers
        mProviders = new ArrayList<AbstractProvider>();
        for( AbstractProvider provider : providers )
        {
            if( provider != null )
            {
                mProviders.add( provider );
                provider.registerListener( this );
            }
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see es.jdbc.n64retroplus.input.provider.AbstractProvider.Listener#onInput(int,
     * float, int)
     */
    @TargetApi( 16 )
    @Override
    public void onInput( int inputCode, float strength, int hardwareId )
    {
        // Process user inputs from keyboard, gamepad, etc.
        if( mPlayerMap.testHardware( hardwareId, mPlayerNumber ) )
        {
            // Update the registered vibrator for this player
            if( AppData.IS_JELLY_BEAN )
            {
                InputDevice device = InputDevice.getDevice( hardwareId );
                if( device != null )
                    CoreInterface.registerVibrator( mPlayerNumber, device.getVibrator() );
            }
            
            // Apply user changes to the controller state
            apply( inputCode, strength );
            
            // Notify the core that controller state has changed
            notifyChanged();
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see es.jdbc.n64retroplus.input.provider.AbstractProvider.Listener#onInput(int[],
     * float[], int)
     */
    @Override
    public void onInput( int[] inputCodes, float[] strengths, int hardwareId )
    {
        // Process multiple simultaneous user inputs from gamepad, keyboard, etc.
        if( mPlayerMap.testHardware( hardwareId, mPlayerNumber ) )
        {
            // Apply user changes to the controller state
            for( int i = 0; i < inputCodes.length; i++ )
                apply( inputCodes[i], strengths[i] );
            
            // Notify the core that controller state has changed
            notifyChanged();
        }
    }
    
    /**
     * Apply user input to the N64 controller state.
     * 
     * @param inputCode The universal input code that was dispatched.
     * @param strength  The input strength, between 0 and 1, inclusive.
     * 
     * @return True, if controller state changed.
     */
    private boolean apply( int inputCode, float strength )
    {
        boolean keyDown = strength > AbstractProvider.STRENGTH_THRESHOLD;
        int n64Index = mInputMap.get( inputCode );
        
        if( n64Index >= 0 && n64Index < NUM_N64_BUTTONS )
        {
            mState.buttons[n64Index] = keyDown;
            return true;
        }
        else if( n64Index < InputMap.NUM_N64_CONTROLS )
        {
            switch( n64Index )
            {
                case InputMap.AXIS_R:
                    mStrengthXpos = strength;
                    break;
                case InputMap.AXIS_L:
                    mStrengthXneg = strength;
                    break;
                case InputMap.AXIS_D:
                    mStrengthYneg = strength;
                    break;
                case InputMap.AXIS_U:
                    mStrengthYpos = strength;
                    break;
                default:
                    return false;
            }
            
            // Calculate the net position of the analog stick
            float rawX = mSensitivityFraction * ( mStrengthXpos - mStrengthXneg );
            float rawY = mSensitivityFraction * ( mStrengthYpos - mStrengthYneg );
            float magnitude = FloatMath.sqrt( ( rawX * rawX ) + ( rawY * rawY ) );
            
            // Update controller state
            if( magnitude > mDeadzoneFraction )
            {
                // Normalize the vector
                float normalizedX = rawX / magnitude;
                float normalizedY = rawY / magnitude;

                // Rescale strength to account for deadzone
                magnitude = ( magnitude - mDeadzoneFraction ) / ( 1f - mDeadzoneFraction );
                magnitude = Utility.clamp( magnitude, 0f, 1f );
                mState.axisFractionX = normalizedX * magnitude;
                mState.axisFractionY = normalizedY * magnitude;
            }
            else
            {
                // In the deadzone 
                mState.axisFractionX = 0;
                mState.axisFractionY = 0;
            }
        }
        else if( keyDown )
        {
            switch( n64Index )
            {
                case InputMap.FUNC_INCREMENT_SLOT:
                    Log.v( "PeripheralController", "FUNC_INCREMENT_SLOT" );
                    CoreInterface.incrementSlot();
                    break;
                case InputMap.FUNC_SAVE_SLOT:
                    Log.v( "PeripheralController", "FUNC_SAVE_SLOT" );
                    CoreInterface.saveSlot();
                    break;
                case InputMap.FUNC_LOAD_SLOT:
                    Log.v( "PeripheralController", "FUNC_LOAD_SLOT" );
                    CoreInterface.loadSlot();
                    break;
                case InputMap.FUNC_STOP:
                    Log.v( "PeripheralController", "FUNC_STOP" );
                    CoreInterface.shutdownEmulator();
                    break;
                case InputMap.FUNC_PAUSE:
                    Log.v( "PeripheralController", "FUNC_PAUSE" );
                    CoreInterface.togglePause();
                    break;
                case InputMap.FUNC_FAST_FORWARD:
                    Log.v( "PeripheralController", "FUNC_FAST_FORWARD" );
                    CoreInterface.fastForward( true );
                    break;
                case InputMap.FUNC_FRAME_ADVANCE:
                    Log.v( "PeripheralController", "FUNC_FRAME_ADVANCE" );
                    CoreInterface.advanceFrame();
                    break;
                case InputMap.FUNC_SPEED_UP:
                    Log.v( "PeripheralController", "FUNC_SPEED_UP" );
                    CoreInterface.incrementCustomSpeed();
                    break;
                case InputMap.FUNC_SPEED_DOWN:
                    Log.v( "PeripheralController", "FUNC_SPEED_DOWN" );
                    CoreInterface.decrementCustomSpeed();
                    break;
                case InputMap.FUNC_GAMESHARK:
                    Log.v( "PeripheralController", "FUNC_GAMESHARK" );
                    NativeExports.emuGameShark( true );
                    break;
                case InputMap.FUNC_SIMULATE_BACK:
                    String[] back_cmd = { "input", "keyevent", String.valueOf( KeyEvent.KEYCODE_BACK ) };
                    SafeMethods.exec( back_cmd, false );
                    break;
                case InputMap.FUNC_SIMULATE_MENU:
                    String[] menu_cmd = { "input", "keyevent", String.valueOf( KeyEvent.KEYCODE_MENU ) };
                    SafeMethods.exec( menu_cmd, false );
                    break;
                default:
                    return false;
            }
        }
        else // keyUp
        {
            switch( n64Index )
            {
                case InputMap.FUNC_FAST_FORWARD:
                    Log.v( "PeripheralController", "FUNC_FAST_FORWARD" );
                    CoreInterface.fastForward( false );
                    break;
                case InputMap.FUNC_GAMESHARK:
                    Log.v( "PeripheralController", "FUNC_GAMESHARK" );
                    NativeExports.emuGameShark( false );
                    break;
                default:
                    return false;
            }
        }
        return true;
    }
}
