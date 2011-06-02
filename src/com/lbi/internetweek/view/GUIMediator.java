package com.lbi.internetweek.view;

import org.puremvc.java.patterns.mediator.Mediator;

import processing.core.PApplet;

import com.lbi.internetweek.controller.game.ScoreUpdatedCommand;
import com.lbi.internetweek.model.AppProxy;
import com.lbi.internetweek.model.GameProxy;
import com.lbi.internetweek.view.components.GUIView;

public class GUIMediator extends Mediator
{
	public static final String 		NAME = "GUIMediator";
	
	private AppProxy				_appProxy;
	private GUIView 				_gui;
		
	public GUIMediator()
	{
		super(NAME, null);
		
		this.facade.registerCommand(GameProxy.SCORE_UPDATED, new ScoreUpdatedCommand() );
	}
	
	public GUIView getGUIView()
	{
		if( _gui == null )
		{
			_gui = new GUIView(this);
		}
		
		return _gui;
	}
	
	public void updateScore( int newScore )
	{
		PApplet.println( "GUIMediator: updateScore() " + newScore );
	}

}
