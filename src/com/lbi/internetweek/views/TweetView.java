package com.lbi.internetweek.views;

import java.util.Timer;

import javax.swing.event.EventListenerList;

import processing.core.PApplet;
import twitter4j.Status;

import com.lbi.internetweek.events.TwitterEvent;
import com.lbi.internetweek.events.TwitterEventListener;

public class TweetView 
{
	EventListenerList 	_listenerList		=	new EventListenerList();

	PApplet 			_pa;
	
	Timer				_timer;
	
	public TweetView( PApplet p )
	{
		_pa			=	p;
		
		setTimer();
	}

	private void setTimer() 
	{
		_timer		=	new Timer();
		
	}

	public void showTweet(Status s) 
	{
		_pa.println( "showTweet() " + s.getText() );
	}
	
	public void draw(float x, float y)
	{
		_pa.pushMatrix();
			
			_pa.translate(x, x);
			
			_pa.fill(200,0,0);
			_pa.rect(-50, 20, 150, 50 );
			_pa.noFill();
		
		_pa.popMatrix();
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
