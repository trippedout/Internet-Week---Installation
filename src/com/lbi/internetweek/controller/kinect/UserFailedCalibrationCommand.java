package com.lbi.internetweek.controller.kinect;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import com.lbi.internetweek.model.KinectProxy;
import com.lbi.internetweek.view.GUIMediator;

public class UserFailedCalibrationCommand extends SimpleCommand
{
	public void execute(INotification note)
	{
		KinectProxy kinect = (KinectProxy) this.facade.retrieveProxy(KinectProxy.NAME );
		
		if( kinect.getNumUsers() < 1 )
		{
			this.facade.sendNotification(GUIMediator.SHOW_CALIBRATION, GUIMediator.FAILED_CALIBRATION );
		}
	}
}
