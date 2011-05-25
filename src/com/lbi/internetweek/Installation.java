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
import com.lbi.internetweek.views.Flock;
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
	boolean DRAW_KINECT_BLOBS        =  true;

	//images
	PImage pi_LBiLogo;

	//background
	Background  bg;
	
	//kinect
	KinectWrapper	kinect;
	
	//Birds
	Flock			flock;
	
	//twitter
	TwitterWrapper		twitter;
	ArrayList<Status>	tweets;

	//openCV
	OpenCV          opencv;
	Blob[]          blobs;
	Blob            blob;

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
		  
		//image loading
		//-------------------------------------
		
		  
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
		
		flock.updatePhysics(blob);
		flock.draw();
		  
		//if( mousePressed ) 
			_stats.draw(0,0);
	}

	public void mousePressed()
	{
		flock.testState();
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
				flock.addTweetToQueue( (Status) evt.getSource() );
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
	  
		for( int i = 0; i < Flock.NUM_BIRDS; ++i )
		{
			VerletParticle2D p = new VerletParticle2D(new Vec2D(random(width-100)+50, random(height-100)+50));
			physics.addParticle(p);
		}		
	}

	void setupBirds()
	{
		flock			=	new Flock(this, physics);
	}

	// --------------------------------------------------------------------------------------------------------
	// DRAW FUNCTIONS
	// --------------------------------------------------------------------------------------------------------

	void drawOpenCV()
	{
		opencv.copy( kinect.getDepthImage() );  
		
		//arbitrary size numbers for finding blobs big enough to be people
		blobs = opencv.blobs( 75*75, 320*240, 10, false );
  
		if( blobs.length > 0 )
		{  
			blob = blobs[0];
      
			if( DRAW_KINECT_BLOBS )
			{
				int nx, ny;
				
				fill(0,255,0,100);
				noStroke();
				
				beginShape();
				//TODO: GET RGB IMAGE OF PERSON WORKING
				//texture( kinect.getRGBImage() );
				
				for( int i = 0; i < blob.points.length; i += 3 ) 
				{
					nx = parseInt( map( blob.points[i].x, 0, 320, 0, width ) );
					ny = parseInt( map( blob.points[i].y, 0, 240, 0, height ) );
          
					vertex( nx, ny );
				}
				endShape(CLOSE);
			}
			}
	  else
	  {
		  blob = null;
	  }
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
