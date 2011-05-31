package com.lbi.internetweek.controller.kinect;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import com.lbi.internetweek.view.BirdsMediator;
import com.lbi.internetweek.view.KinectMediator;

public class FoundUserCommand extends SimpleCommand
{
	public void execute(INotification note)
	{
		KinectMediator kinect = (KinectMediator) this.facade.retrieveMediator(KinectMediator.NAME );
		
		( (BirdsMediator) this.facade.retrieveMediator(BirdsMediator.NAME ) )
			.getBirds()
			.getFlock()
			.addObj( kinect.leftHandVector );
		
		( (BirdsMediator) this.facade.retrieveMediator(BirdsMediator.NAME ) )
			.getBirds()
			.getFlock()
			.addObj( kinect.rightHandVector );
	}
}
