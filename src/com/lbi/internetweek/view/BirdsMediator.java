package com.lbi.internetweek.view;

import org.puremvc.java.patterns.mediator.Mediator;

import com.lbi.internetweek.controller.ModeChangeCommand;
import com.lbi.internetweek.model.AppProxy;
import com.lbi.internetweek.view.components.BirdsView;

public class BirdsMediator extends Mediator
{
	final public static String NAME = "BirdsMediator";
	
	private BirdsView birds;
	
	public BirdsMediator()
	{
		super( NAME, null );

		//this.facade.registerCommand( AppProxy.MODE_CHANGE, new ModeChangeCommand() );
	}
	
	public BirdsView getBirds()
	{
		if( birds == null )
		{
			birds = new BirdsView(this);
		}
		
		return birds;
	}	

	public void setMode(int mode)
	{
		switch( mode )
		{
		case AppProxy.MODE_NORMAL:
			this.getBirds().setNormalMode();
			break;

		case AppProxy.MODE_DANCE:
			this.getBirds().setDanceMode();
			break;

		case AppProxy.MODE_GAME:
			this.getBirds().setGameMode();
			break;
		}
	}
	
}
