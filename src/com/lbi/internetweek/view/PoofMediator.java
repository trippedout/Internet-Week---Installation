package com.lbi.internetweek.view;

import org.puremvc.java.patterns.mediator.Mediator;

import processing.core.PVector;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.controller.game.ShowPoofCommand;
import com.lbi.internetweek.view.components.PoofView;

public class PoofMediator extends Mediator
{
	final public static String NAME 		=	"PoofMediator";
	final public static String SHOW_POOF 	=	"show_poof";
	
	private PoofView 		_poof;
	
	public PoofMediator()
	{
		super(NAME, null);
		
		this.facade.registerCommand(SHOW_POOF, new ShowPoofCommand() );
	}

	public PoofView getPoofView()
	{
		if( _poof == null )
		{
			_poof = new PoofView(this);
		}
		return _poof;
	}
	
	public void onShowPoofCommand(PVector p)
	{
		this.getPoofView().showPoof(p);
	}
}
