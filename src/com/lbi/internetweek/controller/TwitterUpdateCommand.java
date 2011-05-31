package com.lbi.internetweek.controller;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import twitter4j.Status;

import com.lbi.internetweek.view.BirdsMediator;
import com.lbi.internetweek.view.TweetsMediator;
import com.lbi.internetweek.view.components.Bird;

public class TwitterUpdateCommand extends SimpleCommand
{
	public void execute(INotification note)
	{		
		( (TweetsMediator) this.facade.retrieveMediator(TweetsMediator.NAME) )
			.getTweets()
			.addTweetToQueue( (Status) note.getBody() );			
	}

}
