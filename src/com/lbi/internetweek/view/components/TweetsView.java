package com.lbi.internetweek.view.components;

import java.util.ArrayList;
import java.util.Random;

import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;

import twitter4j.Status;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;
import com.lbi.internetweek.model.AppProxy;
import com.lbi.internetweek.view.TweetsMediator;

public class TweetsView
{
	private Installation		_pa;
	private TweetsMediator		_mediator;
	
	private ArrayList<Tweet>	_queue		=	new ArrayList<Tweet>();
	private ArrayList<Tweet>	_tweets		=	new ArrayList<Tweet>();
	
	public PImage				little, medium, large;
	
	PFont						_font;
	Bird						_currentBird	=	null;
	Tweet						_currentTweet	=	null;
	
	public TweetsView(TweetsMediator tweetMediator)
	{
		_pa			=	ApplicationFacade.app;
		_mediator 	=	tweetMediator;
		
		little   	= 	_pa.loadImage( "bubble/little.png" );
		medium   	= 	_pa.loadImage( "bubble/medium.png" );
		large    	=	_pa.loadImage( "bubble/large.png" );
		
//		_font		=	_pa.loadFont("DroidSansMono-16.vlw");
		_font		=	AppProxy.getScoreTextFont();
//		_pa.createFont("Arial", 10, true);	
		
		_pa.textAlign(PConstants.CENTER);
		_pa.textFont(_font);
	}

	public void addTweetToQueue(Status status)
	{
		String str 		=	getTweetString( status );
		
		_queue.add( new Tweet( str, this ) );
		
		checkQueue();
	}
	
	private void checkQueue()
	{		
		if( _currentTweet == null )
		{
			if( _queue.size() > 0 )
			{
				_currentTweet = _queue.remove(0);
				_mediator.getTweetBird();
				_currentTweet.animateIn();
			}
		}
	}

	public void draw()
	{
		if( _currentTweet != null && _currentBird != null )
		{			
			if( _currentTweet.hasLife() ) 
				_currentTweet.draw( _pa.max( 100, _pa.min( _currentBird.x, 900 )), _pa.max(100, _pa.min( 700, _currentBird.y )) );
			else
			{
				_currentTweet.kill();
				_currentTweet = null;
				
				_currentBird.setState(_currentBird.lastState);
				_currentBird = null;
				checkQueue();
			}
		}
	}

	public Bird getCurrentBird()
	{
		return _currentBird;
	}

	public void setCurrentBird(Bird bird)
	{
		this._currentBird 			= 	bird;		
		//this._currentBird.tweetRef 	=	_currentTweet;
		
		try 
		{
			//this._currentBird.callback = _currentTweet.getClass().getMethod( "animateIn", new Class[] {} );			
		} 
		catch (Exception e) 
		{
		}		
	}

	private String getTweetString(Status status)
	{
		return "@" + status.getUser().getScreenName() + " - " + status.getText();
	}

}
