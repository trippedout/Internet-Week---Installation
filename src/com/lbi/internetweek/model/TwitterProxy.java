package com.lbi.internetweek.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.event.EventListenerList;

import org.puremvc.java.patterns.observer.Notification;
import org.puremvc.java.patterns.proxy.Proxy;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;
import com.lbi.internetweek.events.TwitterEvent;
import com.lbi.internetweek.events.TwitterEventListener;

import twitter4j.*;
import twitter4j.auth.*;
import twitter4j.conf.*;

final public class TwitterProxy extends Proxy
{
	public static final String NAME = "TwitterProxy";

	String[] 			TRACKING_ARRAY		=	{"webbys","internetweek","lbinewyork"};
	Map<Long, String>	_map 				=	new HashMap<Long, String>();
	ArrayList<Status>	_tweets				=	new ArrayList<Status>();

	EventListenerList 	_listenerList		=	new EventListenerList();

	Installation		_parent;
	TwitterStream 		_stream;
	StatusListener 		_listener;

	boolean				_hasNewTweets		=	false;

	public TwitterProxy()
	{		
		super(NAME);

		_parent		=	ApplicationFacade.app;		
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

			checkStatusText( status.getText() );

			this.facade.notifyObservers(new Notification(ApplicationFacade.TWITTER_UPDATED, status));

			return true;
		}
	}

	private void checkStatusText(String text)
	{
		if( text.contains( "#gameTime" ) )
		{
			
		}
		else if( text.contains("#danceParty") )
		{
			
		}
		
		/*
		String[] stringArray = text.split(" ");

		for( int i = 0; i < stringArray.length; ++i )
		{
			String str = stringArray[i];
			
			System.out.println( str );
			
			if(  == "#gameTime" )
			{
				System.out.println( "TwitterProxy: checkStatusText() GAME TIME");
			}
		}
		//*/

	}	

}
