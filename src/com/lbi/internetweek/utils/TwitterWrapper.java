package com.lbi.internetweek.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.EventListenerList;

import com.lbi.internetweek.events.TwitterEvent;
import com.lbi.internetweek.events.TwitterEventListener;

import processing.core.PApplet;

import twitter4j.*;
import twitter4j.auth.*;
import twitter4j.conf.*;

final public class TwitterWrapper
{
	String[] 			TRACKING_ARRAY		=	{"webbys","internetweek","lbinewyork"};
	Map<Long, String>	_map 				=	new HashMap<Long, String>();
	ArrayList<Status>	_tweets				=	new ArrayList<Status>();
	
	EventListenerList 	_listenerList		=	new EventListenerList();
	
	PApplet 			_parent;
	TwitterStream 		_stream;
	StatusListener 		_listener;
	
	boolean				_hasNewTweets		=	false;
	
	public TwitterWrapper(PApplet p)
	{		
		_parent		=	p;
		_stream		=	new TwitterStreamFactory().getInstance();
				
		setListener();
		setup();		
	}

	private void setup()
	{
		_stream.setOAuthConsumer("GgpSB1jMpmx2nbJR882qQ", "J9YewDzqLRaQh7KMcJbVbXCUKCA7IoK0aydbkW80");
		_stream.setOAuthAccessToken(new AccessToken("144933835-W6Dln21DZRvNdhknCmu4qKk7shTl1WAHSZKGtrKe", "5ymzOUnjr5U2DWmOWWFJKYqFVZtxzATnOuaZCEfw4"));
		
		FilterQuery query		=	 new FilterQuery();
		query.track(TRACKING_ARRAY);
		
		_stream.filter(query);
	}	

	private void setListener() 
	{
		_listener = new StatusListener() 
		{
			public void onStatus(Status status) 
			{
				//System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
				_hasNewTweets = addToMap(status);
			}

			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
			}

			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
			}

			public void onScrubGeo(long userId, long upToStatusId) {
				System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
			}

			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};

		_stream.addListener(_listener);		
	}

	protected boolean addToMap(Status status) 
	{
		if( _map.containsKey(status.getId()) || _map.containsValue(status.getText()) )
			return false;
		else
		{			
			_map.put( status.getId(), status.getText() );
			
			dispatchEvent( new TwitterEvent(status) );
			
			return true;
		}
	}
	
	public void addListener( TwitterEventListener listener )
	{
		_listenerList.add(TwitterEventListener.class, listener);
	}
	
	public void removeListener( TwitterEventListener listener )
	{
		_listenerList.remove(TwitterEventListener.class, listener);
	}
	
	private void dispatchEvent(TwitterEvent evt) 
	{
        Object[] listeners = _listenerList.getListenerList();
        
        // Each listener occupies two elements - the first is the listener class
        // and the second is the listener instance        
        for (int i=0; i<listeners.length; i+=2) 
        {
            if(listeners[i]==TwitterEventListener.class) 
            {
                ( (TwitterEventListener) listeners[i+1] ).onEvent(evt);
            }
        }
    }	
	
}
