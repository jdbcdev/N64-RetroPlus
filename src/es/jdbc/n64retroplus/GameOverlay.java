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

import es.jdbc.n64retroplus.input.TouchController;
import es.jdbc.n64retroplus.input.map.VisibleTouchMap;
import es.jdbc.n64retroplus.util.Utility;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class GameOverlay extends View implements TouchController.OnStateChangedListener, CoreInterface.OnFpsChangedListener
{
    private VisibleTouchMap mTouchMap;
    private boolean mDrawingEnabled = true;
    private boolean mFpsEnabled = false;
    private int mHatRefreshPeriod = 0;
    private int mHatRefreshCount = 0;
    
    public GameOverlay( Context context, AttributeSet attribs )
    {
        super( context, attribs );
        requestFocus();
    }
    
    public void initialize( VisibleTouchMap touchMap, boolean drawingEnabled, int fpsRefreshPeriod, int hatRefreshPeriod )
    {
        mTouchMap = touchMap;
        mDrawingEnabled = drawingEnabled;
        mFpsEnabled = fpsRefreshPeriod > 0;
        mHatRefreshPeriod = hatRefreshPeriod;
        
        CoreInterface.setOnFpsChangedListener( this, fpsRefreshPeriod );
    }
    
    @Override
    public void onAnalogChanged( float axisFractionX, float axisFractionY )
    {
        if( mHatRefreshPeriod > 0 && mDrawingEnabled )
        {
            // Increment the count since last refresh
            mHatRefreshCount++;
            
            // If stick re-centered, always refresh
            if( axisFractionX == 0 && axisFractionY == 0 )
                mHatRefreshCount = 0;
            
            // Update the analog stick assets and redraw if required
            if( mHatRefreshCount % mHatRefreshPeriod == 0 && mTouchMap != null
                    && mTouchMap.updateAnalog( axisFractionX, axisFractionY ) )
            {
                postInvalidate();
            }
        }
    }

    @Override
    public void onAutoHold( boolean autoHold, int index )
    {
        // Update the AutoHold mask, and redraw if required
        if( mTouchMap != null && mTouchMap.updateAutoHold( autoHold , index) )
        {
            postInvalidate();
        }
    }
    
    @Override
    public void onFpsChanged( int fps )
    {
        // Update the FPS indicator assets, and redraw if required
        if( mTouchMap != null && mTouchMap.updateFps( fps ) )
            postInvalidate();
    }
    
    @Override
    protected void onSizeChanged( int w, int h, int oldw, int oldh )
    {
        // Recompute skin layout geometry
        if( mTouchMap != null )
            mTouchMap.resize( w, h, Utility.getDisplayMetrics( this ) );
        super.onSizeChanged( w, h, oldw, oldh );
    }
    
    @Override
    protected void onDraw( Canvas canvas )
    {
        if( mTouchMap == null || canvas == null )
            return;
        
        if( mDrawingEnabled )
        {
            // Redraw the static buttons
            mTouchMap.drawButtons( canvas );
        
            // Redraw the dynamic analog stick
            mTouchMap.drawAnalog( canvas );
            
            // Redraw the autoHold mask
            mTouchMap.drawAutoHold( canvas );
        }
        
        if( mFpsEnabled )
        {
            // Redraw the dynamic frame rate info
            mTouchMap.drawFps( canvas );
        }
    }
}
