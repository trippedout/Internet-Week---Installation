package com.lbi.internetweek.view.components;

import java.util.ArrayList;

import processing.core.PFont;

import twitter4j.Status;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;
import com.lbi.internetweek.view.TweetsMediator;

public class TweetsView
{
	private Installation		_pa;
	private TweetsMediator		_mediator;
	
	private ArrayList<Tweet>	_queue		=	new ArrayList<Tweet>();
	private ArrayList<Tweet>	_tweets		=	new ArrayList<Tweet>();
	
	PFont						_font;
	Bird						_currentBird	=	null;
	Tweet						_currentTweet	=	null;
	
	public TweetsView(TweetsMediator tweetMediator)
	{
		_pa			=	ApplicationFacade.app;
		_mediator 	=	tweetMediator;
		
		_font		=	_pa.loadFont("DroidSansMono-16.vlw");
		_pa.textFont(_font);
		_pa.textSize(16);
	}

	public void addTweetToQueue(Status status)
	{
		String str 		=	getTweetString( status );
		
		_queue.add( new Tweet( str ) );
		
		checkQueue();
	}
	
	private void checkQueue()
	{		
		if( _currentTweet == null )
		{
			if( _queue.size() > 0 )
			{
				_mediator.getTweetBird();
				_currentTweet = _queue.remove(0);
			}
		}		
	}

	public void draw()
	{
		if( _currentTweet != null && _currentBird != null )
		{			
			if( _currentTweet.hasLife() ) 
				_currentTweet.draw(_currentBird.x, _currentBird.y);
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
		this._currentBird = bird;
	}

	private String getTweetString(Status status)
	{
		return "@" + status.getUser().getScreenName() + " - " + status.getText();
	}

}
