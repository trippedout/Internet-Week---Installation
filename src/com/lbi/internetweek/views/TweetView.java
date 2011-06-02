package com.lbi.internetweek.views;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.event.EventListenerList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import twitter4j.Status;

import com.lbi.internetweek.events.TwitterEvent;
import com.lbi.internetweek.events.TwitterEventListener;
import com.lbi.internetweek.model.AppProxy;

public class TweetView 
{
	EventListenerList 	_listenerList		=	new EventListenerList();

	PApplet 			_pa;
	
	Timer				_timer;
	int					_tweetDuration		=	5000;
	
	PImage				_speechBubble;
	String				_tweet;
	
	public TweetView( PApplet p )
	{
		_pa					=	p;
		_speechBubble		=	_pa.loadImage( "speech_bubble.png" );
				
		setTimer();
	}

	private void setTimer() 
	{
		_timer		=	new Timer();		
	}

	public void showTweet(Status s) 
	{
		_pa.println( "showTweet() " + s.getText() );
		
		_tweet				=	"@" + s.getUser().getScreenName() + " - " + s.getText();
		
		_timer.schedule(new TimerTask() 
		{			
			@Override
			public void run() 
			{				
				dispatchEvent( new TwitterEvent(this) );
			}
		}, _tweetDuration);
	}

	public void killTweet() 
	{	
		
	}
	
	public void draw(float x, float y)
	{
		int w = _speechBubble.width,
			h = _speechBubble.height;
		
		int sx = -66 + 40,
			sy = -203 - 40;
		
		_pa.pushMatrix();
			
			_pa.translate(x, y);
			
			_pa.beginShape();
			_pa.texture(_speechBubble);
			_pa.vertex(sx, sy, 0, 0);
			_pa.vertex(sx+w, sy, w, 0);
			_pa.vertex(sx+w, sy+h, w, h);
			_pa.vertex(sx, sy+h, 0, h);
			_pa.endShape();
			
			_pa.fill(0x333333);
			_pa.textFont(AppProxy.getTweetFont());
			_pa.textAlign(_pa.CENTER);
			_pa.text(_tweet, sx + 20, sy + 40, w-25, h);
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
