package com.lbi.internetweek;

import java.util.Timer;

import processing.core.*;
import processing.opengl.PGraphicsOpenGL;

import hypermedia.video.*;

import com.lbi.internetweek.utils.Stats;
import com.lbi.internetweek.utils.TwitterWrapper;
import com.lbi.internetweek.views.Background;
import com.lbi.internetweek.views.Bird;
import com.lbi.internetweek.views.KinectWrapper;

import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.behaviors.*;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;

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
	PImage pi_Bg;
	PImage pi_LBiLogo;

	//background
	Background  bg;
	PImage pi_Grass;
	
	//kinect
	KinectWrapper	kinect;
	
	//twitter
	TwitterWrapper	twitter;
	Timer			twitter_timer;

	//openCV
	OpenCV          opencv;
	Blob[]          blobs;
	Blob            blob;

	//phsyics
	VerletPhysics2D       physics;
	VerletParticle2D[]    particles;
	int                   numAttractors = 20;
	AttractionBehavior[]  attractionPool = new AttractionBehavior[numAttractors];

	//BIRDSSSS
	int 			numBirds 	=	50;
	Bird[]        	birds		=	new Bird[numBirds];
	PShape        	ps_Bird;
	PImage        	pi_Bird;
	
	// --------------------------------------------------------------------------------------------------------
	// PROCESSING
	
	public void setup()
	{
		//processing calls
		//-------------------------------------
		size( 1280, 960, OPENGL );		
		  
		//image loading
		//-------------------------------------
		pi_Bird      =  loadImage( "bird_sprites.png" );
		  
		//instantiation
		//-------------------------------------
		bg				=	new Background(this);
		kinect			=	new KinectWrapper(this);		
		twitter			=	new TwitterWrapper(this);
		twitter_timer	=	new Timer();
		
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
		 
		checkPhysics();
		physics.update();
		drawBirds();
		  
		//if( mousePressed ) 
			_stats.draw(0,0);
	}

	// --------------------------------------------------------------------------------------------------------
	// SETUP FUNCTIONS
	// --------------------------------------------------------------------------------------------------------

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
	  
		for( int i = 0; i < numBirds; ++i )
		{
			VerletParticle2D p = new VerletParticle2D(new Vec2D(random(width-100)+50, random(height-100)+50));
			physics.addParticle(p);
		}
	  
		for( int i = 0; i < attractionPool.length; ++i )
		{
			Vec2D v = new Vec2D( -1000, -1000 );
			attractionPool[i] = new AttractionBehavior(v, 250.0f, 0.0f);
			physics.addBehavior(attractionPool[i]);
		}
	}

	void setupBirds()
	{
		for (int i = 0; i < numBirds; ++i) 
		{
			birds[i]		=	new Bird(this, pi_Bird);
		}
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
				fill(0,255,0,100);
				noStroke();
				int nx, ny;
        
				beginShape();        
				for( int i = 0; i < blob.points.length; i += 2 ) 
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

	void checkPhysics()
	{
		if( blob != null )
		{
			for( int i = 0; i < attractionPool.length; ++i ) 
		    {
				if( !physics.behaviors.contains(attractionPool[i]) )
					physics.addBehavior(attractionPool[i]);
		    }
			
			int spacer = floor(blob.points.length / numAttractors);
		     
			if( spacer >= 1 )
			{
				int count = 0;
		         
		        for( int i = 0; i < blob.points.length; i += spacer ) 
		        {
		        	if( count < attractionPool.length - 1 ) count++;
		          
		        	AttractionBehavior b = attractionPool[count];
		        	b.setStrength( 0.2f );
		          
		        	Vec2D v = b.getAttractor();
		        	v.x = parseInt( map( blob.points[i].x, 0, 320, 0, width ) );
		        	v.y = parseInt( map( blob.points[i].y, 0, 240, 0, height ) );
		          
		        	fill(255,0,0,100);
		        	ellipse(v.x, v.y, 8, 8);
		        	noFill();
		        }
			}	
		    else
		    {
			    for( int i = 0; i < attractionPool.length; ++i ) 
			    {
			    	AttractionBehavior b = attractionPool[i];
			    	b.setStrength( 0.0f );
		         
			    	Vec2D v = b.getAttractor();
			    	v.x = -1000;
			    	v.y = -1000;
			    }
		    }
		}
		else
		{
			for( int i = 0; i < attractionPool.length; ++i ) 
		    {
				if( physics.behaviors.contains(attractionPool[i]) )
					physics.removeBehavior(attractionPool[i]);
		    }
		}
	  
	}

	void drawBirds()
	{
		for( int i = 0; i < numBirds; ++i )
		{
			VerletParticle2D p = physics.particles.get(i);
			birds[i].draw(p);
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
