package com.lbi.internetweek.controller.kinect;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import com.lbi.internetweek.model.KinectProxy;
import com.lbi.internetweek.view.BirdsMediator;

public class FoundUserCommand extends SimpleCommand
{
	public void execute(INotification note)
	{
		KinectProxy kinect = (KinectProxy) this.facade.retrieveProxy(KinectProxy.NAME );
		
		( (BirdsMediator) this.facade.retrieveMediator(BirdsMediator.NAME ) )
			.getBirdsView()
			.getFlock()
			.addObj( kinect.leftHandVector );
		
		( (BirdsMediator) this.facade.retrieveMediator(BirdsMediator.NAME ) )
			.getBirdsView()
			.getFlock()
			.addObj( kinect.rightHandVector );
	}
}
