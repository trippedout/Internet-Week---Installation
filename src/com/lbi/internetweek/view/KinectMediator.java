package com.lbi.internetweek.view;

import org.puremvc.java.patterns.mediator.Mediator;

import com.lbi.internetweek.model.KinectProxy;
import com.lbi.internetweek.view.components.KinectView;

public class KinectMediator extends Mediator
{
	final public static String NAME = "KinectMediator";
	
	private KinectView kinectView;
	private KinectProxy kinectProxy;

	public KinectMediator()
	{
		super(NAME, null);
		
		kinectProxy = (KinectProxy) this.facade.retrieveProxy(KinectProxy.NAME);		
	}
	
	public KinectView getKinectView()
	{
		if( kinectView == null )
		{
			kinectView = new KinectView(this, kinectProxy);
		}
		
		return kinectView;
	}

}
