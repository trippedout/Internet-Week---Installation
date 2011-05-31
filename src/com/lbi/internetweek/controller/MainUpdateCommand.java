package com.lbi.internetweek.controller;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;
import org.puremvc.java.patterns.facade.Facade;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.model.PhysicsProxy;
import com.lbi.internetweek.view.BirdsMediator;
import com.lbi.internetweek.view.KinectMediator;

public class MainUpdateCommand extends SimpleCommand
{
	public void execute(INotification note)
	{
		Facade f = ApplicationFacade.getInstance();
		
		PhysicsProxy physics	=	(PhysicsProxy) f.retrieveProxy(PhysicsProxy.NAME);
		physics.getPhysics().update();
		
		KinectMediator kinect	=	(KinectMediator) f.retrieveMediator(KinectMediator.NAME);		
		kinect.update();
		
		BirdsMediator birds 	=	(BirdsMediator) f.retrieveMediator(BirdsMediator.NAME);
		birds.getBirds().update();
	}
}
