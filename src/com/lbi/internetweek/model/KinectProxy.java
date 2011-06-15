package com.lbi.internetweek.model;

import java.util.ArrayList;

import org.puremvc.java.patterns.proxy.Proxy;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

import SimpleOpenNI.SimpleOpenNI;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;
import com.lbi.internetweek.states.FlyingState;
import com.lbi.internetweek.utils.OpenNIWrapper;
import com.lbi.internetweek.utils.Vector2D;
import com.lbi.internetweek.view.PoofMediator;
import com.lbi.internetweek.view.components.Bird;

public class KinectProxy extends Proxy
{
	final public static String NAME					=	"KinectProxy";
	final public static String CONTACTS_UPDATED 	=	"contacts_updated";
	final public static String USER_CALIBRATED		=	"user_calibrated";
	final public static String LOST_USER			=	"lost_user";
	public static final String ADD_POINT_TO_SCORE 	=	"add_point_to_score";
	public static final String FOUND_NEW_USER 		=	"found_new_user";
	public static final String USER_FAILED_CALIBRATION 	=	"user_failed_calibration";

	Installation	_pa;

	OpenNIWrapper   _context;

	private PImage _depthImage;
	//private PImage _rgbImage;

	int[]           _rawDepth, _rawColor;
	int             _kWidth, _kHeight;
	int             _nearThreshold, _farThreshold;

	private PVector 		jointPos 				= 	new PVector();
	private PVector 		realPos					= 	new PVector();
	
	public int 				currentUser				=	-1;
	public int	 			currentlyCalibrating 	=	-1;
	private float 			lastMag;

	public Vector2D	headVector			=	new Vector2D();
	public Vector2D	leftHandVector		=	new Vector2D();
	public Vector2D	rightHandVector		=	new Vector2D();

	// --------------------------------------------------------------------------------------------------------
	// SETUP FUNCTIONS
	// --------------------------------------------------------------------------------------------------------

	public KinectProxy()
	{
		super( NAME );

		_pa				=	ApplicationFacade.app;

		_nearThreshold      =  	0;
		_farThreshold       =  	3200;

		setupKinect();
	}	

	private void setupKinect() 
	{
		_context = new OpenNIWrapper(_pa,SimpleOpenNI.RUN_MODE_MULTI_THREADED);
		_context.setCallbacks(this);
		_context.setMirror(true);
		_context.enableDepth();
		_context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);

		_depthImage   		=	_pa.createImage( 320, 240, PConstants.RGB );
		//_rgbImage   		=	_pa.createImage( 320, 240, _pa.RGB );
	}

	// --------------------------------------------------------------------------------------------------------
	// PUBLIC FUNCTIONS
	// --------------------------------------------------------------------------------------------------------
	
	int i, j, c1, c2, dpos, pos;
	int w=640, h=480;
	
	public void update() 
	{
		_context.update();

		

		//update depth image for use in opencv
		if( _pa.frameCount % 2 == 0 )
		{
			dpos = 0;
			pos = 0;

			_rawDepth 	=	_context.depthMap();
			//_rawColor	=	_context.rgbImage().pixels;

			for( i = 0; i < h; i += 2 )
			{
				for( j = 0; j < w; j += 2 )
				{
					dpos   =  ( i * w ) + j;

					c1      =  	_rawDepth[dpos];
					//c2		=	_rawColor[dpos];

					if( c1 > _nearThreshold && c1 < _farThreshold )
						_depthImage.pixels[pos] = 0xffffff;
					else
						_depthImage.pixels[pos] = 0;

					//_rgbImage.pixels[pos] = c2;

					pos++;
				}
			}

			_depthImage.updatePixels();
			//_rgbImage.updatePixels();
		}

		//update contacts
		if( _context.getNumberOfUsers() > 0 )
		{
			if( currentUser > 0)
				updateContacts(currentUser);
		}
	}

	private void updateContacts(int userId) 
	{
		if( _pa.frameCount % 2 == 0 )
		{
			_context.getJointPositionSkeleton( userId, SimpleOpenNI.SKEL_HEAD, jointPos );
			_context.convertRealWorldToProjective(jointPos, realPos);
			headVector.set( mapXToScreen(realPos.x), mapYToScreen(realPos.y), 0 );
			
			_context.getJointPositionSkeleton( userId, SimpleOpenNI.SKEL_RIGHT_HAND, jointPos );
			_context.convertRealWorldToProjective(jointPos, realPos);
			rightHandVector.set( mapXToScreen(realPos.x), mapYToScreen(realPos.y), 0 );
	
			_context.getJointPositionSkeleton( userId, SimpleOpenNI.SKEL_LEFT_HAND, jointPos );
			_context.convertRealWorldToProjective(jointPos, realPos);
			leftHandVector.set( mapXToScreen(realPos.x), mapYToScreen(realPos.y), 0 );			
		}
		
		//this.facade.sendNotification(CONTACTS_UPDATED, this);
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
	// SCORING
	// --------------------------------------------------------------------------------------------------------
	
	float dl, dr, lvmag, rvmag;
	PVector normal = new PVector(0,1);
	
	public void checkBirds(ArrayList<Bird> birds)
	{
		if( _context.getNumberOfUsers() > 0 )
		{
			if( _context.isTrackingSkeleton(currentUser) )
			{				
				for( int i = 0; i < birds.size(); ++i )
				{
					Bird b 		= 	birds.get(i);
					
					if( !b.isHurt && !b.isTweeting )// && b.state instanceof FlyingState )
					{
						dl	=	PVector.dist(b.getVec(), leftHandVector);
						dr	=	PVector.dist(b.getVec(), rightHandVector);
						
						lvmag =	leftHandVector.vmag();
						rvmag =	rightHandVector.vmag();
						
						if( dr < AppProxy.MIN_DIST && rvmag > AppProxy.MIN_POWER && rvmag != lastMag )
						{
							//PApplet.println( "\nBird: " + i + " dist: " + dr + " mag:" + rvmag );
							
							b.hurtState.startingVelocity = rightHandVector.getVelocity().normalize(normal);							
							b.setState(b.hurtState);
							
							lastMag = rvmag;
							
							this.facade.sendNotification( ADD_POINT_TO_SCORE );
							this.facade.sendNotification( PoofMediator.SHOW_POOF, b.hurtState.startingPosition );
							
							break;
						}
						
						if( dl < AppProxy.MIN_DIST && lvmag > AppProxy.MIN_POWER && lvmag != lastMag )
						{
							//PApplet.println( "\nBird: " + i + " dist: " + dl + " mag:" + lvmag );
							
							b.hurtState.startingVelocity = leftHandVector.getVelocity().normalize(normal);							
							b.setState(b.hurtState);
							
							lastMag = lvmag;
							
							this.facade.sendNotification( ADD_POINT_TO_SCORE );
							this.facade.sendNotification( PoofMediator.SHOW_POOF, b.hurtState.startingPosition );
							
							break;
						}
						
					}
				}
			}		
		}
	}

	// --------------------------------------------------------------------------------------------------------
	// SKELETAL
	// --------------------------------------------------------------------------------------------------------

	private float mapXToScreen(float x) 
	{		
		return PApplet.map(x, 0, 640, 0, _pa.width);
	}

	private float mapYToScreen(float y) 
	{		
		return PApplet.map(y, 0, 480, 0, _pa.height);
	}

	// --------------------------------------------------------------------------------------------------------
	// PUBLIC
	// --------------------------------------------------------------------------------------------------------

	public void onNewUser(int userId)
	{
		PApplet.println("onNewUser - userId: " + userId);
		PApplet.println("--Start pose detection");

		_context.startPoseDetection("Psi",userId);	
		
		this.facade.sendNotification(FOUND_NEW_USER, userId);
	}

	public void onLostUser(int userId)
	{
		if( userId == currentUser ) currentUser = -1;

		PApplet.println("onLostUser - userId: " + userId);

		this.facade.sendNotification(LOST_USER, userId);
	}

	public void onStartCalibration(int userId)
	{
		PApplet.println("onStartCalibration - userId: " + userId);
		currentlyCalibrating = userId;
	}

	public void onEndCalibration(int userId, boolean successfull)
	{
		PApplet.println("onEndCalibration - userId: " + userId + ", successfull: " + successfull);

		if (successfull) 
		{ 
			PApplet.println("--User calibrated !!!");
			_context.startTrackingSkeleton(userId);
			currentUser = userId;

			this.facade.sendNotification(USER_CALIBRATED, userId);
		} 
		else
		{ 
			PApplet.println("--Failed to calibrate user !!!");
			PApplet.println("--Start pose detection");
			_context.startPoseDetection("Psi",userId);
			
			this.facade.sendNotification(USER_FAILED_CALIBRATION, userId);
		}
		
		currentlyCalibrating = -1;
	}

	public void onStartPose(String pose,int userId)
	{
		PApplet.println("onStartPose - userId: " + userId + ", pose: " + pose);
		PApplet.println("--Stop pose detection");

		_context.stopPoseDetection(userId); 
		_context.requestCalibrationSkeleton(userId, true);
	}

	public void onEndPose(String pose,int userId)
	{
		PApplet.println("onEndPose - userId: " + userId + ", pose: " + pose);
	}

	public PImage getDepthImage()
	{
		return _depthImage;
	}
	
	public int getNumUsers()
	{
		return _context.getNumberOfUsers();
	}

	// --------------------------------------------------------------------------------------------------------
	// GETTER SETTER
	// --------------------------------------------------------------------------------------------------------


}
