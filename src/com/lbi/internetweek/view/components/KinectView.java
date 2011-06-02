package com.lbi.internetweek.view.components;

import hypermedia.video.Blob;
import hypermedia.video.OpenCV;

import SimpleOpenNI.SimpleOpenNI;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;
import com.lbi.internetweek.model.KinectProxy;
import com.lbi.internetweek.view.KinectMediator;

import processing.core.PApplet;
import processing.core.PImage;

public class KinectView
{
	//debug
	boolean DRAW_KINECT_BLOBS        	=	true;
	int COUNT_UNTIL_KILL_KINECT_USERS	=	120;

	boolean			DRAW_DEPTH_IMAGE		=	false;
	boolean			DRAW_COLOR_IMAGE		=	false;
	boolean			DRAW_SKELETON			=	false;
	boolean			DRAW_CONTACTS			=	true;

	private KinectMediator			_mediator;
	private KinectProxy				_proxy;
	private Installation			_pa;
	private OpenCV					_opencv;

	public PImage         	_depthImage, _rgbImage;
	
	private int				contactSize			=	20;
	private int				contactColor		=	0xff0000;
	
	private Blob[]          blobs;
	private Blob            blob;
	private int				blobCounter = 0;

	public KinectView(KinectMediator kinectMediator, KinectProxy kinectProxy)
	{
		_mediator		=	kinectMediator;
		_pa				=	ApplicationFacade.app;
		_proxy			=	kinectProxy;
		
		setDepthImage( _proxy.getDepthImage() );
		setupOpenCV();
	}

	private void setupOpenCV()
	{
		_opencv       =  new OpenCV(_pa);  
		_opencv.allocate( _depthImage.width, _depthImage.height );
	}

	public void draw()
	{
		drawOutlines();
		
		 if( DRAW_DEPTH_IMAGE )
		 {
			 //draw depthImage quickly on screen
			 _pa.beginShape(_pa.QUADS);
			 _pa.texture( _depthImage );
			 _pa.vertex(0,0,0,0);
			 _pa.vertex(320,0,320,0);
			 _pa.vertex(320,240,320,240);
			 _pa.vertex(0,240,0,240);
			 _pa.endShape();
		 }

		 if( DRAW_COLOR_IMAGE )
		 {
			 _pa.beginShape(_pa.QUADS);
			 _pa.texture( _rgbImage );
			 _pa.vertex(0,0,0,0);
			 _pa.vertex(320,0,320,0);
			 _pa.vertex(320,240,320,240);
			 _pa.vertex(0,240,0,240);
			 _pa.endShape();
		 }
		 
		 /*
		 if( DRAW_SKELETON )
		 {
			 if(_context.isTrackingSkeleton(1))
				 drawSkeleton(1);
		 }
		 //*/

		 if( DRAW_CONTACTS )
		 {
			 if( _proxy.currentUser > 0)
				 drawContacts();
		 }
		 //*/
	}
	
	private void drawOutlines()
	{
		//PImage rgb = _mediator.getRGBImage();		
		_opencv.copy( _depthImage );  

		//arbitrary size numbers for finding blobs big enough to be people
		blobs = _opencv.blobs( 75*75, 320*240, 10, false );

		if( blobs.length > 0 )
		{  
			int numBlobs, nx, ny;
			
			blobCounter = 0;

			if( DRAW_KINECT_BLOBS )
			{
				if( blobs.length <= 3 ) 
					numBlobs = blobs.length;
				else
					numBlobs = 3;
				
				for( int j = 0; j < numBlobs; ++j )
				{
					blob 	=	blobs[j];
					
					_pa.fill(255,80);
					//_pa.strokeWeight(3f);
					//_pa.stroke(255,0,0,185);
	
					_pa.beginShape();
					
					for( int i = 0; i < blob.points.length; i += 3 ) 
					{
						nx = _pa.parseInt( _pa.map( blob.points[i].x, 0, 320, 0, _pa.width ) );
						ny = _pa.parseInt( _pa.map( blob.points[i].y, 0, 240, 0, _pa.height ) );
	
						_pa.vertex( nx, ny );
					}			
					
					_pa.endShape(_pa.CLOSE);
					_pa.noStroke();
				}
			}
		}
		else
		{
			blob = null;
			blobCounter++;

			if( blobCounter == COUNT_UNTIL_KILL_KINECT_USERS)
			{
				//_mediator.killUsers();
			}
		}
	}
	
	/*
	private void drawSkeleton(int userId) 
	{
		_pa.stroke(0,255,0);
		_pa.strokeWeight(3);

		_context.drawLimb(userId, SimpleOpenNI.SKEL_HEAD, SimpleOpenNI.SKEL_NECK);

		_context.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_LEFT_SHOULDER);
		_context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_LEFT_ELBOW);
		_context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_ELBOW, SimpleOpenNI.SKEL_LEFT_HAND);

		_context.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_RIGHT_SHOULDER);
		_context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_RIGHT_ELBOW);
		_context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW, SimpleOpenNI.SKEL_RIGHT_HAND);

		_context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_TORSO);
		_context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_TORSO);

		_pa.noStroke();
	}
	//*/
	
	private void drawContacts() 
	{
		/*
		PApplet.println( "Left Veloctiy: " + _proxy.leftHandVector.getVelocity() 
						+ " Right Velocity: " + _proxy.rightHandVector.getVelocity() 
						);
		
		PApplet.println( "Left Mag: " + _proxy.leftHandVector.getVelocity().mag() 
						+ " Right Mag: " + _proxy.rightHandVector.getVelocity().mag() 
						);						
		//*/

		_pa.fill( 255, 0, 0 );
		_pa.noStroke();
//		_pa.ellipse(headVector.x, headVector.y, contactSize, contactSize);
		_pa.ellipse( _proxy.rightHandVector.x, _proxy.rightHandVector.y, contactSize, contactSize );
		_pa.ellipse( _proxy.leftHandVector.x, _proxy.leftHandVector.y, contactSize, contactSize );		
		_pa.noFill();
	}
	
	public PImage getRGBImage()
	{
		return _rgbImage;
	}

	public PImage getDepthImage() 
	{
		return _depthImage;
	}

	public void setDepthImage(PImage depthImage)
	{
		this._depthImage = depthImage;
	}
}
