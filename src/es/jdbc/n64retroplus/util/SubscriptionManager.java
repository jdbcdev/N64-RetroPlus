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

import java.util.ArrayList;
import java.util.List;

/**
 * A small class to encapsulate the management of subscriptions.
 * 
 * @param <Subscriber> The type of object to subscribe
 */
public final class SubscriptionManager<Subscriber>
{
    private final ArrayList<Subscriber> mSubscribers;

    /**
     * Constructor
     */
    public SubscriptionManager()
    {
        mSubscribers = new ArrayList<Subscriber>();
    }

    /**
     * Adds a {@link Subscriber} to the list of subscribers.
     *
     * @param subscriber The subscriber to add/subscribe.
     */
    public void subscribe( Subscriber subscriber )
    {
        if( ( subscriber != null ) && !mSubscribers.contains( subscriber ) )
            mSubscribers.add( subscriber );
    }

    /**
     * Unsubscribes a {@link Subscriber} from the list of subscribers.
     *
     * @param subscriber The subscriber to unsubscribe.
     */
    public void unsubscribe( Subscriber subscriber )
    {
        if( subscriber != null )
            mSubscribers.remove( subscriber ); 
    }

    /**
     * Unsubscribes all Subscribers from the list of subscribers.
     */
    public void unsubscribeAll()
    {
        mSubscribers.clear();            
    }

    /**
     * Gets the list of all current subscribers.
     *
     * @return A list of all current subscribers.
     */
    public List<Subscriber> getSubscribers()
    {
        return mSubscribers;
    }
}
