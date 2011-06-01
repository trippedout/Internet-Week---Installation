package com.lbi.internetweek.view;

import org.puremvc.java.patterns.mediator.Mediator;

import com.lbi.internetweek.model.AppProxy;
import com.lbi.internetweek.model.KinectProxy;
import com.lbi.internetweek.view.components.BirdsView;

public class BirdsMediator extends Mediator
{
	final public static String NAME = "BirdsMediator";
	
	private BirdsView 			birds;
	private KinectProxy 		kinectProxy;
	
	public BirdsMediator()
	{
		super( NAME, null );
		
		kinectProxy = (KinectProxy) this.facade.retrieveProxy(KinectProxy.NAME);
	}
	
	public BirdsView getBirdsView()
	{
		if( birds == null )
		{
			birds = new BirdsView(this, kinectProxy);
		}
		
		return birds;
	}
	
}
