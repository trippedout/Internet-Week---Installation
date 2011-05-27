package com.lbi.internetweek;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Timer;

import processing.core.*;
import processing.opengl.PGraphicsOpenGL;

import hypermedia.video.*;

import com.lbi.internetweek.events.TwitterEvent;
import com.lbi.internetweek.events.TwitterEventListener;
import com.lbi.internetweek.utils.Stats;
import com.lbi.internetweek.utils.TwitterWrapper;
import com.lbi.internetweek.views.Background;
import com.lbi.internetweek.views.Bird;
import com.lbi.internetweek.views.BirdsController;
import com.lbi.internetweek.views.KinectWrapper;

import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.behaviors.*;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import twitter4j.Status;

public class Installation extends PApplet 
{
	// --------------------------------------------------------------------------------------------------------
	// MAIN AND VERS
	
	public static void main(String args[]) 
	{
		PApplet.main(new String[] { "--present", "com.lbi.internetweek.Installation" });
	}

	// --------------------------------------------------------------------------------------------------------
	// VARS	
	Stats 			_stats;
		
	//debug
	boolean DRAW_KINECT_BLOBS        	=	true;
	int COUNT_UNTIL_KILL_KINECT_USERS	=	120;
	
	//fonts
	public PFont font;

	//images
	PImage pi_LBiLogo;

	//background
	Background  bg;
	
	//kinect
	KinectWrapper	kinect;
	
	//Birds
	BirdsController		birds;
	
	//twitter
	TwitterWrapper		twitter;
	ArrayList<Status>	tweets;

	//openCV
	OpenCV          opencv;
	Blob[]          blobs;
	Blob            blob;
	int				blobCounter = 0;

	//phsyics
	VerletPhysics2D       physics;
	VerletParticle2D[]    particles;
	
	// --------------------------------------------------------------------------------------------------------
	// PROCESSING
	
	public void setup()
	{
		//processing calls
		//-------------------------------------
		size( 1280, 960, OPENGL );
		  
		//font loading
		//-------------------------------------
		font		=	loadFont("DroidSansMono-16.vlw");
		textFont(font, 16);
		
		//instantiation
		//-------------------------------------
		bg				=	new Background(this);
		kinect			=	new KinectWrapper(this);
		
		setupTwitter();
		setupOpenCV();
		setupPhysics();
		setupBirds();
		  
		//extras
		//-------------------------------------		    
		_stats      =  new Stats(this);  
	}
	
	public void draw()
	{
		bg.draw();
		
		kinect.update();
		kinect.draw();
		
		drawOpenCV();
		 
		physics.update();
		
		birds.updatePhysics(blob);
		birds.draw();
		
		kinect.drawGuide();
		
		//if( mousePressed ) 
			_stats.draw(0,0);
	}

	public void mousePressed()
	{
		birds.testState();
	}
	
	// --------------------------------------------------------------------------------------------------------
	// SETUP FUNCTIONS
	// --------------------------------------------------------------------------------------------------------
	
	private void setupTwitter() 
	{
		twitter			=	new TwitterWrapper(this);
		tweets			=	new ArrayList<Status>();
		
		twitter.addListener( new TwitterEventListener() 
		{			
			@Override
			public void onEvent(TwitterEvent evt) 
			{
				//println( "Twitter updated! " + (Status) evt.getSource() );
				birds.addTweetToQueue( (Status) evt.getSource() );
			}
		} );
	}

	void setupOpenCV()
	{
		opencv       =  new OpenCV(this);  
		opencv.allocate( kinect.getDepthImage().width, kinect.getDepthImage().height );
	}

	void setupPhysics()
	{
		physics = new VerletPhysics2D();
		physics.setDrag(0.005f);
	  
		physics.setWorldBounds(new Rect(50, 50, width-100, height-100));  
		//physics.addBehavior(new GravityBehavior(new Vec2D(0, 0.05f)));
	  
		for( int i = 0; i < BirdsController.NUM_BIRDS; ++i )
		{
			VerletParticle2D p = new VerletParticle2D(new Vec2D(random(width-100)+50, random(height-100)+50));
			physics.addParticle(p);
		}		
	}

	void setupBirds()
	{
		birds			=	new BirdsController(this, physics);
	}

	// --------------------------------------------------------------------------------------------------------
	// DRAW FUNCTIONS
	// --------------------------------------------------------------------------------------------------------
	
	void drawOpenCV()
	{
		PImage rgb = kinect.getRGBImage();
		opencv.copy( kinect.getDepthImage() );  

		//arbitrary size numbers for finding blobs big enough to be people
		blobs = opencv.blobs( 75*75, 320*240, 10, false );

		if( blobs.length > 0 )
		{  
			blobCounter = 0;
			blob = blobs[0];

			if( DRAW_KINECT_BLOBS )
			{
				int nx, ny;

				//mask.beginDraw();				
				fill(200,50,50,40);
				noStroke();

				beginShape();
					for( int i = 0; i < blob.points.length; i += 3 ) 
					{
						nx = parseInt( map( blob.points[i].x, 0, 320, 0, width ) );
						ny = parseInt( map( blob.points[i].y, 0, 240, 0, height ) );
	
						vertex( nx, ny );
					}
				endShape(CLOSE);
				
				//mask.endDraw();
			}
		}
		else
		{
			blob = null;
			blobCounter++;

			if( blobCounter == COUNT_UNTIL_KILL_KINECT_USERS)
			{
				kinect.killUsers();
			}
		}
		
		//image( mask, 0,0 );
	}

	

	// --------------------------------------------------------------------------------------------------------
	// KINECT HELPER FUNCS
	// --------------------------------------------------------------------------------------------------------
	
	public void onNewUser(int userId)
	{
		kinect.onNewUser(userId);
	}
	
	public void onLostUser(int userId)
	{
		kinect.onLostUser(userId);
	}
	
	public void onStartCalibration(int userId)
	{
		kinect.onStartCalibration(userId);
	}
	
	public void onEndCalibration(int userId, boolean successfull)
	{
		kinect.onEndCalibration(userId, successfull);
		
		if( successfull ) 
		{
			birds.getFlock().addObj(kinect.leftHandVector);
			birds.getFlock().addObj(kinect.rightHandVector);
		}
	}
	
	public void onStartPose(String pose,int userId)
	{
		kinect.onStartPose(pose, userId);
	}
	
	public void onEndPose(String pose,int userId)
	{
		kinect.onEndPose(pose, userId);
	}

}
