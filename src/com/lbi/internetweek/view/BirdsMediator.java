package com.lbi.internetweek.view;

import org.puremvc.java.patterns.mediator.Mediator;

import com.lbi.internetweek.model.AppProxy;
import com.lbi.internetweek.view.components.BirdsView;

public class BirdsMediator extends Mediator
{
	final public static String NAME = "BirdsMediator";
	
	private BirdsView birds;
	
	public BirdsMediator()
	{
		super( NAME, null );
	}
	
	public BirdsView getBirdsView()
	{
		if( birds == null )
		{
			birds = new BirdsView(this);
		}
		
		return birds;
	}
	
}
