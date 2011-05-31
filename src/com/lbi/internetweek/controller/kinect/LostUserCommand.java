package com.lbi.internetweek.controller.kinect;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import com.lbi.internetweek.view.BirdsMediator;
import com.lbi.internetweek.view.KinectMediator;

public class LostUserCommand extends SimpleCommand
{
	public void execute(INotification note)
	{
		( (BirdsMediator) this.facade.retrieveMediator(BirdsMediator.NAME ) )
			.getBirds()
			.getFlock()
			.removeObjs();
	}
}
