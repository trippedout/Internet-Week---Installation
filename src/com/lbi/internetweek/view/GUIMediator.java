package com.lbi.internetweek.view;

import org.puremvc.java.patterns.mediator.Mediator;

import processing.core.PApplet;

import com.lbi.internetweek.controller.game.ScoreUpdatedCommand;
import com.lbi.internetweek.controller.game.ShowCalibrationCommand;
import com.lbi.internetweek.model.AppProxy;
import com.lbi.internetweek.model.GameProxy;
import com.lbi.internetweek.view.components.GUIView;

public class GUIMediator extends Mediator
{
	public static final String 		NAME 					= "GUIMediator";

	public static final String 		SHOW_CALIBRATION 		= "show_calibration";
	
	public static final int			NORMAL_CALIBRATION		= 0;
	public static final int			FAILED_CALIBRATION		= 1;
	public static final int			CORRECT_CALIBRATION		= 2;
	
	private AppProxy				_appProxy;
	private GUIView 				_gui;
		
	public GUIMediator()
	{
		super(NAME, null);
		
		this.facade.registerCommand(GameProxy.SCORE_UPDATED, new ScoreUpdatedCommand() );
		this.facade.registerCommand(SHOW_CALIBRATION, new ShowCalibrationCommand() );
		
		getGUIView().updateScore(0);
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
		getGUIView().updateScore(newScore);
	}
	
	public void updateScreenOrientation()
	{
		getGUIView()
			.updateScreen( 
					( (AppProxy) this.facade.retrieveProxy(AppProxy.NAME) )
						.getScoreVector()
					);
	}

	public void showCalibration(int type)
	{
		getGUIView().getCalibrationView().showCalibration(type);
	}

}
