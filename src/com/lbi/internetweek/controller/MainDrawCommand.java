package com.lbi.internetweek.controller;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;
import org.puremvc.java.patterns.facade.Facade;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.view.BackgroundMediator;
import com.lbi.internetweek.view.BirdsMediator;
import com.lbi.internetweek.view.KinectMediator;
import com.lbi.internetweek.view.TweetsMediator;

public class MainDrawCommand extends SimpleCommand
{
	public void execute(INotification note)
	{
		( (BackgroundMediator) this.facade.retrieveMediator(BackgroundMediator.NAME) )
			.draw();
		
		( (KinectMediator) this.facade.retrieveMediator(KinectMediator.NAME) )
			.draw();
		
		( (BirdsMediator) this.facade.retrieveMediator(BirdsMediator.NAME) )
			.getBirds()
			.draw();
		
		( (TweetsMediator) this.facade.retrieveMediator(TweetsMediator.NAME) )
			.getTweets()
			.draw();
	}
}
