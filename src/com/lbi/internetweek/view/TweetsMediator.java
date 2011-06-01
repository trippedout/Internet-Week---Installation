package com.lbi.internetweek.view;

import org.puremvc.java.patterns.mediator.Mediator;

import com.lbi.internetweek.controller.TweetReadyForBirdCommand;
import com.lbi.internetweek.model.AppProxy;
import com.lbi.internetweek.view.components.Bird;
import com.lbi.internetweek.view.components.TweetsView;

public class TweetsMediator extends Mediator
{
	final public static String NAME 					=	"TweetMediator";
	final public static String TWEET_READY_FOR_BIRD 	=	"tweet_ready_for_bird";
	
	private TweetsView _tweets;

	public TweetsMediator()
	{
		super(NAME, null);
		
		this.facade.registerCommand( TWEET_READY_FOR_BIRD, new TweetReadyForBirdCommand() );
	}
	
	public TweetsView getTweets()
	{
		if( _tweets == null )
		{
			_tweets = new TweetsView(this);
		}
		
		return _tweets;
	}
	
	public void getTweetBird()
	{
		this.facade.sendNotification(TWEET_READY_FOR_BIRD);
	}	
	
}
