package com.lbi.internetweek.view.components;

import processing.core.PImage;
import processing.core.PVector;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;
import com.lbi.internetweek.model.AppProxy;
import com.lbi.internetweek.view.GUIMediator;

public class GUIView
{
	private Installation		_pa;
	private GUIMediator			_mediator;
	private AppProxy 			_appProxy;
		
	private PVector				_scorePosition;
	
	private GUIViewScore		_score;
	private GUIViewCalibration	_calibration;

	public GUIView(GUIMediator guiMediator)
	{
		_pa					=	ApplicationFacade.app;
		_mediator			=	guiMediator;
		
		_score				=	new GUIViewScore();
		_calibration		=	new GUIViewCalibration();
	}
	
	public void updateScore( int newScore )
	{
		_score.update(newScore);
	}
	
	public void draw()
	{
		_pa.pushMatrix();
			
			_pa.translate(_scorePosition.x, _scorePosition.y);
			_score.draw();
		
		_pa.popMatrix();
		
		_pa.pushMatrix();
		
			_pa.translate(_pa.width*.5f, _pa.height*.5f);
			_calibration.draw();
		
		_pa.popMatrix();
	}

	public void setScorePosition(PVector scorePosition)
	{
		this._scorePosition = scorePosition;
	}

	public PVector getScorePosition()
	{
		return _scorePosition;
	}

	public void updateScreen(PVector scoreVector)
	{
		_scorePosition = scoreVector;		
	}
	
	public GUIViewCalibration getCalibrationView()
	{
		return _calibration;
	}
	
}
