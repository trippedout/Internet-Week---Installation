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
	public static PFont			_scoreTextFont;
	public static PFont			_tweetFont;
	
	public static PImage[] 		_birdFrames;
	public static PImage[]		_deadBirdFrames;
	
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
		_leftScore		=	new PVector( 165, _pa.height - 85 );
		_rightScore		=	new PVector( _pa.width - 165, _pa.height - 85 );
	}

	private void initBGs()
	{
		_leftBG			=	_pa.loadImage("bg_left.png");
		_rightBG		=	_pa.loadImage("bg_right.png");
	}

	private void initFlockRects()
	{
		_leftRect		=	new Rect( -105, 75, _pa.width, _pa.height-275 );
		_rightRect		=	new Rect( 105, 75, _pa.width, _pa.height-275 );
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
    		_scoreFont = ApplicationFacade.app.loadFont("Meloriac-Regular-56.vlw");
    	}
    	
    	return _scoreFont;
    }
    
    public static PFont getScoreTextFont()
    {
    	if( _scoreTextFont == null )
    	{
    		_scoreTextFont = ApplicationFacade.app.loadFont("Meloriac-Regular-17.vlw");
    	}
    	
    	return _scoreTextFont;
    }
    
    public static PFont getTweetFont()
    {
    	if( _tweetFont == null )
    	{
    		_tweetFont = ApplicationFacade.app.loadFont("DroidSansMono-16.vlw");
    	}
    	
    	return _tweetFont;
    }
    
    public static void setBirdImage( PImage[] bi )
    {
    	_birdFrames = bi;
    }
    
    public static void setDeadBirdImage( PImage[] bi )
    {
    	_deadBirdFrames = bi;
    }
    
    public static PImage[] getBirdFrames()
    {
    	return _birdFrames;
    }
    
    public static PImage[] getDeadBirdFrames()
    {
    	return _deadBirdFrames;
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
