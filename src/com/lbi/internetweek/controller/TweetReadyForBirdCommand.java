package com.lbi.internetweek.controller;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import twitter4j.Status;

import com.lbi.internetweek.view.BirdsMediator;
import com.lbi.internetweek.view.TweetsMediator;
import com.lbi.internetweek.view.components.Bird;

public class TweetReadyForBirdCommand extends SimpleCommand
{
	public void execute(INotification note)
	{
		Bird bird = ( (BirdsMediator) this.facade.retrieveMediator(BirdsMediator.NAME) )
						.getBirds()
						.getTweetingBird();			
		
		( (TweetsMediator) this.facade.retrieveMediator(TweetsMediator.NAME) )
			.getTweets()
			.setCurrentBird(bird);
	}
}
