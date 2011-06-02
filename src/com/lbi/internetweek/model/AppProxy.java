package com.lbi.internetweek.model;

import java.util.Hashtable;

import org.puremvc.java.patterns.observer.Notification;
import org.puremvc.java.patterns.proxy.Proxy;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;

import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import toxi.geom.Rect;

public class AppProxy extends Proxy
{
	final public static String 	NAME = "AppProxy";	
	final public static String 	MODE_CHANGE		=	"mode_change";
	final public static String 	SCREEN_UPDATED 	= 	"screen_updated";
	
	final public static int		LEFT_SCREEN		=	0;
	final public static int		RIGHT_SCREEN	=	1;
	
	final public static int 	MODE_NORMAL		=	0;
	final public static int 	MODE_GAME		=	1;
	final public static int 	MODE_DANCE		=	2;
	
	public static final int 	NUM_BIRDS		=	25;
	public static final float 	BIRD_MIN_SCALE 	=	0.4f;
	
	public static final int 	MIN_DIST 		= 	65;
	public static final int 	MIN_POWER 		=	65;
	
	public static PFont			_scoreFont;
	
	public static PImage 		_birdImage;
	
	private Installation		_pa;
	
	//bg changing
	private PImage				_leftBG;
	private PImage				_rightBG;
	private PImage				_bg;
	
	//flocking
	private Rect 				_rightRect;
	private Rect 				_leftRect;
	private Rect				_flockRect;
	
	//scoring
	private PVector				_leftScore;
	private PVector				_rightScore;
	private PVector				_scoreVector;
	
	//private 
	private int					_mode			=	MODE_NORMAL;
	
	
	public AppProxy()
	{
		super(NAME);
		
		_pa			=	ApplicationFacade.app;
		
		initBGs();
		initFlockRects();
		initScoreVectors();
		setScreen(RIGHT_SCREEN);
	}
		
	private void initScoreVectors()
	{
		_leftScore		=	new PVector( 150, _pa.height - 120 );
		_rightScore		=	new PVector( _pa.width - 150, _pa.height - 120 );
	}

	private void initBGs()
	{
		_leftBG			=	_pa.loadImage("bg_left.png");
		_rightBG		=	_pa.loadImage("bg_right.png");
	}

	private void initFlockRects()
	{
		_leftRect		=	new Rect( -105, 40, _pa.width, _pa.height-200 );
		_rightRect		=	new Rect( 105, 40, _pa.width, _pa.height-200 );
	}

	public void setScreen(int screen)
	{
		switch(screen)
		{
		case LEFT_SCREEN:
			_bg 			= _leftBG;
			_flockRect 		= _leftRect;
			_scoreVector	= _leftScore;
			break;

		case RIGHT_SCREEN:
			_bg 			= _rightBG;
			_flockRect 		= _rightRect;
			_scoreVector	= _rightScore;
			break;
		}
		
		this.facade.sendNotification(SCREEN_UPDATED);
	}
        
    public PImage getBG()
    {
    	return _bg;
    }

    public static PFont getScoreFont()
    {
    	if( _scoreFont == null )
    	{
    		_scoreFont = ApplicationFacade.app.createFont("Arial", 72);
    	}
    	
    	return _scoreFont;
    }
    
    public static void setBirdImage( PImage bi )
    {
    	_birdImage = bi;
    }
    
    public static PImage getBirdImage()
    {
    	return _birdImage;
    }
    
    public void setMode(int mode)
    {
    	_mode = mode;
    	this.facade.notifyObservers( new Notification(MODE_CHANGE, _mode) );
    }

	public Rect getRect()
	{
		return _flockRect;
	}

	public PVector getScoreVector()
	{
		return _scoreVector;
	}
}
