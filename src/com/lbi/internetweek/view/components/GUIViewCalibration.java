package com.lbi.internetweek.view.components;

import processing.core.PConstants;
import processing.core.PImage;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;
import com.lbi.internetweek.view.GUIMediator;

public class GUIViewCalibration
{
	private Installation		_pa;

	private PImage				_normalImage;
	private PImage				_failedImage;
	private PImage				_completeImage;
	private PImage				_currentImage;
	
	private int					_imageLife		=	300;

	public GUIViewCalibration()
	{
		_pa			=	ApplicationFacade.app;

		loadImages();
	}

	private void loadImages()
	{
		_normalImage	=	_pa.loadImage("calibration.png");
		_failedImage	=	_pa.loadImage("calibration_failed.png");
		_completeImage	=	_pa.loadImage("calibration_complete.png");
		
		showCalibration(0);
	}
	
	public void showCalibration(int type)
	{
		switch(type)
		{
		case GUIMediator.NORMAL_CALIBRATION:
			_imageLife = 300;
			_currentImage = _normalImage;
			break;

		case GUIMediator.FAILED_CALIBRATION:
			_imageLife = 300;
			_currentImage = _failedImage;
			break;
			
		case GUIMediator.CORRECT_CALIBRATION:
			_imageLife = 120;
			_currentImage = _completeImage;
			break;
		}
	}

	public void draw()
	{
		if( _imageLife > 0 )
		{
			_pa.imageMode(PConstants.CENTER);
			_pa.image(_currentImage,0,0);
			
			_imageLife--;
		}
	}

}