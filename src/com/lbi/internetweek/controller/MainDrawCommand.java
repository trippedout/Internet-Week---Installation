package com.lbi.internetweek.controller;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;
import org.puremvc.java.patterns.facade.Facade;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.view.BackgroundMediator;
import com.lbi.internetweek.view.BirdsMediator;
import com.lbi.internetweek.view.GUIMediator;
import com.lbi.internetweek.view.KinectMediator;
import com.lbi.internetweek.view.PoofMediator;
import com.lbi.internetweek.view.TweetsMediator;

public class MainDrawCommand extends SimpleCommand
{
	public void execute(INotification note)
	{
		( (BackgroundMediator) this.facade.retrieveMediator(BackgroundMediator.NAME) )
			.draw();
		
		( (KinectMediator) this.facade.retrieveMediator(KinectMediator.NAME) )
			.getKinectView()
			.draw();
		
		( (BirdsMediator) this.facade.retrieveMediator(BirdsMediator.NAME) )
			.getBirdsView()
			.draw();

		( (PoofMediator) this.facade.retrieveMediator(PoofMediator.NAME) )
			.getPoofView()
			.draw();
			
		( (GUIMediator) this.facade.retrieveMediator(GUIMediator.NAME) )
			.getGUIView()
			.draw();

		( (TweetsMediator) this.facade.retrieveMediator(TweetsMediator.NAME) )
			.getTweets()
			.draw();
	}
}
