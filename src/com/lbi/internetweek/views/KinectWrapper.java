package com.lbi.internetweek.views;

import SimpleOpenNI.*;
import processing.core.PApplet;
import processing.core.PImage;

public class KinectWrapper
{
	boolean			DRAW_DEPTH_IMAGE		=	false;
	boolean			DRAW_COLOR_IMAGE		=	false;
	boolean			DRAW_SKELETON			=	true;
	
	PApplet			_parent;
	
	SimpleOpenNI    _context;
	
	PImage          _depthImage;
	PImage          _rgbImage;
	
	int[]           _rawDepth;
	int[]			_rawColor;
	
	int             _kWidth, _kHeight;
	int             _nearThreshold, _farThreshold;
	
	// --------------------------------------------------------------------------------------------------------
	// SETUP FUNCTIONS
	// --------------------------------------------------------------------------------------------------------
	
	public KinectWrapper(PApplet parent) 
	{
		_parent				=	parent;
		
		_nearThreshold      =  	0;
		_farThreshold       =  	2500;
		
		setupKinect();
	}

	private void setupKinect() 
	{
		_context = new SimpleOpenNI(_parent,SimpleOpenNI.RUN_MODE_MULTI_THREADED);		 
		_context.setMirror(true);
		_context.enableDepth();
		_context.enableRGB();
		_context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);

		_depthImage   		=	_parent.createImage( 320, 240, _parent.RGB );
		_rgbImage   		=	_parent.createImage( 320, 240, _parent.RGB );
	}
	
	// --------------------------------------------------------------------------------------------------------
	// PUBLIC FUNCTIONS
	// --------------------------------------------------------------------------------------------------------
	
	public void update() 
	{
		_context.update();		
	}
	
	public void draw()
	{
		
		int i, j, c1, c2, dpos, pos;
		int w=640, h=480;
		  
		if( _parent.frameCount % 4 == 0 )
		{
			dpos = 0;
			pos = 0;
		    
		    _rawDepth 	=	_context.depthMap();
		    _rawColor	=	_context.rgbImage().pixels;
		    
		    for( i = 0; i < h; i += 2 )
		    {
		    	for( j = 0; j < w; j += 2 )
		    	{
		    		dpos   =  ( i * w ) + j;
		        
		    		c1      =  	_rawDepth[dpos];
		            c2		=	_rawColor[dpos];
		    		
		    		if( c1 > _nearThreshold && c1 < _farThreshold )
		    			_depthImage.pixels[pos] = 0xffffff;
		    		else
		    			_depthImage.pixels[pos] = 0;
		    		
		    		_rgbImage.pixels[pos] = c2;
		    		
		    		pos++;
		    	}
		    }
		    
		    _depthImage.updatePixels();
		    _rgbImage.updatePixels();
		}
		  
		if( DRAW_DEPTH_IMAGE )
		{
			//draw depthImage quickly on screen
			_parent.beginShape(_parent.QUADS);
			_parent.texture(_depthImage);
			_parent.vertex(0,0,0,0);
			_parent.vertex(320,0,320,0);
			_parent.vertex(320,240,320,240);
			_parent.vertex(0,240,0,240);
			_parent.endShape();
		}
		
		if( DRAW_COLOR_IMAGE )
		{
			_parent.beginShape(_parent.QUADS);
			_parent.texture(_rgbImage);
			_parent.vertex(0,0,0,0);
			_parent.vertex(320,0,320,0);
			_parent.vertex(320,240,320,240);
			_parent.vertex(0,240,0,240);
			_parent.endShape();
		}
		  
		if( DRAW_SKELETON )
		{
			if(_context.isTrackingSkeleton(1))
				drawSkeleton(1);
		}
	}
	
	public PImage getRGBImage()
	{
		return _rgbImage;
	}
	
	public void killUsers()
	{
		
	}
	
	// --------------------------------------------------------------------------------------------------------
	// SKELETAL
	// --------------------------------------------------------------------------------------------------------
	
	private void drawSkeleton(int userId) 
	{
		_parent.stroke(0,255,0);
		_parent.strokeWeight(3);
		
		_context.drawLimb(userId, SimpleOpenNI.SKEL_HEAD, SimpleOpenNI.SKEL_NECK);

		_context.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_LEFT_SHOULDER);
		_context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_LEFT_ELBOW);
		_context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_ELBOW, SimpleOpenNI.SKEL_LEFT_HAND);

		_context.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_RIGHT_SHOULDER);
		_context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_RIGHT_ELBOW);
		_context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW, SimpleOpenNI.SKEL_RIGHT_HAND);

		_context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_TORSO);
		_context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_TORSO);
		
		_parent.noStroke();
	}
	
	public void onNewUser(int userId)
	{
	  _parent.println("onNewUser - userId: " + userId);
	  _parent.println("--Start pose detection");
	  
	  _context.startPoseDetection("Psi",userId);
	}

	public void onLostUser(int userId)
	{
	  _parent.println("onLostUser - userId: " + userId);
	}

	public void onStartCalibration(int userId)
	{
	  _parent.println("onStartCalibration - userId: " + userId);
	}

	public void onEndCalibration(int userId, boolean successfull)
	{
	  _parent.println("onEndCalibration - userId: " + userId + ", successfull: " + successfull);
	  
	  if (successfull) 
	  { 
	    _parent.println("--User calibrated !!!");
	    _context.startTrackingSkeleton(userId); 
	  } 
	  else 
	  { 
	    _parent.println("--Failed to calibrate user !!!");
	    _parent.println("--Start pose detection");
	    _context.startPoseDetection("Psi",userId);
	  }
	}

	public void onStartPose(String pose,int userId)
	{
	  _parent.println("onStartPose - userId: " + userId + ", pose: " + pose);
	  _parent.println("--Stop pose detection");
	  
	  _context.stopPoseDetection(userId); 
	  _context.requestCalibrationSkeleton(userId, true);
	 
	}

	public void onEndPose(String pose,int userId)
	{
	  _parent.println("onEndPose - userId: " + userId + ", pose: " + pose);
	}
	
	// --------------------------------------------------------------------------------------------------------
	// GETTER SETTER
	// --------------------------------------------------------------------------------------------------------

	public PImage getDepthImage() 
	{
		return _depthImage;
	}
}
