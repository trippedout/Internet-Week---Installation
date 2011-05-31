package com.lbi.internetweek.view;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import SimpleOpenNI.SimpleOpenNI;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;
import com.lbi.internetweek.controller.kinect.ContactsUpdatedCommand;
import com.lbi.internetweek.controller.kinect.FoundUserCommand;
import com.lbi.internetweek.controller.kinect.LostUserCommand;
import com.lbi.internetweek.utils.OpenNIWrapper;
import com.lbi.internetweek.view.components.KinectView;

public class KinectMediator extends Mediator
{
	final public static String NAME					=	"KinectMediator";
	final public static String CONTACTS_UPDATED 	=	"contacts_updated";
	final public static String NEW_USER			 	=	"new_user";
	final public static String LOST_USER			 =	"lost_user";
		
	boolean			DRAW_DEPTH_IMAGE		=	false;
	boolean			DRAW_COLOR_IMAGE		=	false;
	boolean			DRAW_SKELETON			=	false;
	boolean			DRAW_CONTACTS			=	true;

	Installation	_pa;

	OpenNIWrapper   _context;

	int[]           _rawDepth, _rawColor;
	int             _kWidth, _kHeight;
	int             _nearThreshold, _farThreshold;

	PVector 		jointPos 			= 	new PVector();
	PVector 		realPos				= 	new PVector();
	int				contactSize			=	20;
	int				contactColor		=	0xff0000;
	int 			currentUser;

	public PVector 	headVector			=	new PVector();
	public PVector 	leftHandVector		=	new PVector();
	public PVector 	rightHandVector		=	new PVector();

	private KinectView _kinect;

	// --------------------------------------------------------------------------------------------------------
	// SETUP FUNCTIONS
	// --------------------------------------------------------------------------------------------------------
	
	public KinectMediator()
	{
		super( NAME, null );
		
		_pa				=	ApplicationFacade.app;

		_nearThreshold      =  	0;
		_farThreshold       =  	3200;

		setupKinect();
		setupCommands();
	}	

	private void setupKinect() 
	{
		_kinect			=	new KinectView(this);
		
		_context = new OpenNIWrapper(_pa,SimpleOpenNI.RUN_MODE_MULTI_THREADED);
		_context.setCallbacks(this);
		_context.setMirror(true);
		_context.enableDepth();
		_context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
	}

	private void setupCommands()
	{
		this.facade.registerCommand( KinectMediator.NEW_USER, new FoundUserCommand() );
		this.facade.registerCommand( KinectMediator.LOST_USER, new LostUserCommand() );
		this.facade.registerCommand( KinectMediator.CONTACTS_UPDATED, new ContactsUpdatedCommand() );
	}

	// --------------------------------------------------------------------------------------------------------
	// PUBLIC FUNCTIONS
	// --------------------------------------------------------------------------------------------------------

	public void update() 
	{
		_context.update();
		
		PImage depthImage = _kinect.getDepthImage();

		int i, j, c1, c2, dpos, pos;
		int w=640, h=480;

		if( _pa.frameCount % 4 == 0 )
		{
			dpos = 0;
			pos = 0;

			_rawDepth 	=	_context.depthMap();
//			_rawColor	=	_context.rgbImage().pixels;

			for( i = 0; i < h; i += 2 )
			{
				for( j = 0; j < w; j += 2 )
				{
					dpos   =  ( i * w ) + j;

					c1      =  	_rawDepth[dpos];
//					c2		=	_rawColor[dpos];

					if( c1 > _nearThreshold && c1 < _farThreshold )
						depthImage.pixels[pos] = 0xffffff;
					else
						depthImage.pixels[pos] = 0;

//					_rgbImage.pixels[pos] = c2;

					pos++;
				}
			}

			depthImage.updatePixels();
//			_rgbImage.updatePixels();
		}
	}

	public void draw()
	{
		/**
		 * TODO - make it so that it can track any number of users/ids
		 */

		_kinect.drawOutlines();
				
		 if( _context.getNumberOfUsers() > 0 )
		 {
			 if( currentUser > 0)
				 updateContacts(currentUser);

			 /*
			for( i = 0; i < _context.getNumberOfUsers(); ++i )
			{
				if(_context.isTrackingSkeleton(i+1))
					updateContacts(i+1);
			}
			//*/
		 }

		 if( DRAW_DEPTH_IMAGE )
		 {
			 //draw depthImage quickly on screen
			 _pa.beginShape(_pa.QUADS);
			 _pa.texture(_kinect.getDepthImage());
			 _pa.vertex(0,0,0,0);
			 _pa.vertex(320,0,320,0);
			 _pa.vertex(320,240,320,240);
			 _pa.vertex(0,240,0,240);
			 _pa.endShape();
		 }

		 if( DRAW_COLOR_IMAGE )
		 {
			 _pa.beginShape(_pa.QUADS);
			 _pa.texture(_kinect.getRGBImage());
			 _pa.vertex(0,0,0,0);
			 _pa.vertex(320,0,320,0);
			 _pa.vertex(320,240,320,240);
			 _pa.vertex(0,240,0,240);
			 _pa.endShape();
		 }

		 if( DRAW_SKELETON )
		 {
			 if(_context.isTrackingSkeleton(1))
				 drawSkeleton(1);
		 }

		 if( DRAW_CONTACTS )
		 {
			 if( currentUser > 0)
				 drawContacts();
		 }
	}

	private void updateContacts(int userId) 
	{				
		_context.getJointPositionSkeleton( userId, SimpleOpenNI.SKEL_HEAD, jointPos );		
		_context.convertRealWorldToProjective(jointPos, realPos);
		headVector.x = mapXToScreen(realPos.x);
		headVector.y = mapYToScreen(realPos.y);		

		_context.getJointPositionSkeleton( userId, SimpleOpenNI.SKEL_RIGHT_HAND, jointPos );		
		_context.convertRealWorldToProjective(jointPos, realPos);
		rightHandVector.x = mapXToScreen(realPos.x);
		rightHandVector.y = mapYToScreen(realPos.y);		

		_context.getJointPositionSkeleton( userId, SimpleOpenNI.SKEL_LEFT_HAND, jointPos );		
		_context.convertRealWorldToProjective(jointPos, realPos);
		leftHandVector.x = mapXToScreen(realPos.x);
		leftHandVector.y = mapYToScreen(realPos.y);
		
		this.facade.sendNotification(CONTACTS_UPDATED, this);
	}
	
	public void killUsers()
	{
		if( _context.getNumberOfUsers() > 0 )
		{
			for( int i = 0; i < _context.getNumberOfUsers(); ++i )
			{
				if( _context.isCalibratedSkeleton(i) || _context.isCalibrationDataSkeleton(i) )
					_context.clearCalibrationDataSkeleton(i);

				if( _context.isCalibratedSkeleton(i) )
					_context.abortCalibrationSkeleton(i);

				if( _context.isTrackingSkeleton(i) )
					_context.stopTrackingSkeleton(i);					
			}
		}
	}

	// --------------------------------------------------------------------------------------------------------
	// SKELETAL
	// --------------------------------------------------------------------------------------------------------

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

	private void drawContacts() 
	{
		//_pa.println("drawContacts");

		_pa.fill( 255, 0, 0 );
		_pa.noStroke();
		_pa.ellipse(headVector.x, headVector.y, contactSize, contactSize);
		_pa.ellipse(rightHandVector.x, rightHandVector.y, contactSize, contactSize);
		_pa.ellipse(leftHandVector.x, leftHandVector.y, contactSize, contactSize);		
		_pa.noFill();		
	}

	private float mapXToScreen(float x) 
	{		
		return _pa.map(x, 0, 640, 0, _pa.width);
	}

	private float mapYToScreen(float y) 
	{		
		return _pa.map(y, 0, 480, 0, _pa.height);
	}

	// --------------------------------------------------------------------------------------------------------
	// PUBLIC
	// --------------------------------------------------------------------------------------------------------

	public void onNewUser(int userId)
	{
		_pa.println("onNewUser - userId: " + userId);
		_pa.println("--Start pose detection");

		_context.startPoseDetection("Psi",userId);		
	}

	public void onLostUser(int userId)
	{
		if( userId == currentUser ) currentUser = -1;

		_pa.println("onLostUser - userId: " + userId);
		
		this.facade.sendNotification(LOST_USER, userId);
	}

	public void onStartCalibration(int userId)
	{
		_pa.println("onStartCalibration - userId: " + userId);
	}

	public void onEndCalibration(int userId, boolean successfull)
	{
		_pa.println("onEndCalibration - userId: " + userId + ", successfull: " + successfull);

		if (successfull) 
		{ 
			_pa.println("--User calibrated !!!");
			_context.startTrackingSkeleton(userId);
			currentUser = userId;
			
			this.facade.sendNotification(NEW_USER, userId);
		} 
		else
		{ 
			_pa.println("--Failed to calibrate user !!!");
			_pa.println("--Start pose detection");
			_context.startPoseDetection("Psi",userId);
		}
	}

	public void onStartPose(String pose,int userId)
	{
		_pa.println("onStartPose - userId: " + userId + ", pose: " + pose);
		_pa.println("--Stop pose detection");

		_context.stopPoseDetection(userId); 
		_context.requestCalibrationSkeleton(userId, true);
	}

	public void onEndPose(String pose,int userId)
	{
		_pa.println("onEndPose - userId: " + userId + ", pose: " + pose);
	}

	// --------------------------------------------------------------------------------------------------------
	// GETTER SETTER
	// --------------------------------------------------------------------------------------------------------


}
