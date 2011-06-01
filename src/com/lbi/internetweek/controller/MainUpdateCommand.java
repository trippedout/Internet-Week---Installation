package com.lbi.internetweek.controller;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;
import org.puremvc.java.patterns.facade.Facade;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.model.KinectProxy;
import com.lbi.internetweek.model.PhysicsProxy;
import com.lbi.internetweek.view.BirdsMediator;

public class MainUpdateCommand extends SimpleCommand
{
	public void execute(INotification note)
	{
		Facade f = ApplicationFacade.getInstance();
		
		( (PhysicsProxy) this.facade.retrieveProxy(PhysicsProxy.NAME) )
			.getPhysics()
			.update();
		
		( ( KinectProxy) this.facade.retrieveProxy(KinectProxy.NAME) )		
			.update();
		
		( (BirdsMediator) this.facade.retrieveMediator(BirdsMediator.NAME) )
			.getBirdsView()
			.update();
	}
}
