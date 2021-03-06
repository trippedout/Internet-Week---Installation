package com.lbi.internetweek.controller.kinect;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import com.lbi.internetweek.model.KinectProxy;
import com.lbi.internetweek.view.BirdsMediator;

public class LostUserCommand extends SimpleCommand
{
	public void execute(INotification note)
	{
		KinectProxy kinect = (KinectProxy) this.facade.retrieveProxy(KinectProxy.NAME);
		
		if( kinect.getNumUsers() <= 0 )
		{
			( (BirdsMediator) this.facade.retrieveMediator(BirdsMediator.NAME ) )
				.getBirdsView()
				.getFlock()
				.removeObjs();
		}
	}
}
