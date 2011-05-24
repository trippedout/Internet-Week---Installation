package com.lbi.internetweek;

import processing.core.*;
import processing.opengl.PGraphicsOpenGL;

import hypermedia.video.*;

import com.lbi.internetweek.utils.Stats;
import com.lbi.internetweek.views.Background;
import com.lbi.internetweek.views.Bird;

import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.behaviors.*;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;

import SimpleOpenNI.*;

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
	
	//boids
	//Flock flock;

	//kinect
	SimpleOpenNI    kinect;
	PImage          depthImage;
	int[]           rawDepth;
	int             kWidth, kHeight;
	int             nearThreshold, farThreshold;

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
		shapeMode(CENTER);
		  
		//vars
		//-------------------------------------
		nearThreshold      =  0;
		farThreshold       =  2500;
		  
		//image loading
		//-------------------------------------
		pi_Bg        =  loadImage( "bg.jpg" );
		pi_Bird      =  loadImage( "bird_sprites.png" );
		depthImage   =  createImage( 320, 240, RGB );
		  
		//instantiation
		//-------------------------------------
		bg			=	new Background(this);
		setupKinect();
		setupOpenCV();
		setupPhysics();
		setupFlock();
		  
		//extras
		//-------------------------------------
		    
		_stats      =  new Stats(this);  
	}
	
	public void draw()
	{
		
		//initial calls
		//-------------------------------------
		bg.draw();
		
		kinect.update();  
		drawKinect();
		 
		checkPhysics();
		physics.update();
		drawBirds();
		  
		//classes
		//-------------------------------------
		
		//flock.run();
		 
		//extras
		//-------------------------------------
		//if( mousePressed ) 
			_stats.draw(0,0);
	}
	
	// --------------------------------------------------------------------------------------------------------
	// SETUP FUNCTIONS
	// --------------------------------------------------------------------------------------------------------

	void setupKinect()
	{
		kinect = new SimpleOpenNI(this,SimpleOpenNI.RUN_MODE_MULTI_THREADED); 
		kinect.setMirror(true);
		kinect.enableDepth();
	}

	void setupOpenCV()
	{
	  opencv       =  new OpenCV(this);  
	  opencv.allocate( depthImage.width, depthImage.height );
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

	void setupFlock()
	{
	  //flock = new Flock();
	  
	  for (int i = 0; i < numBirds; ++i) 
	  {
		  birds[i]		=	new Bird(this, pi_Bird);
	    //flock.addBoid( new Boid( new Vec2D(width/2,height/2), 3.0, 0.05 ) );
	  }
	}

	// --------------------------------------------------------------------------------------------------------
	// DRAW FUNCTIONS
	// --------------------------------------------------------------------------------------------------------

	void drawKinect()
	{
	  int i, j, c, dpos, pos;
	  int w=640, h=480;
	  
	  if( frameCount % 4 == 0 )
	  {
	    dpos = 0;
	    pos = 0;
	    
	    rawDepth = kinect.depthMap();
	    
	    for( i = 0; i < h; i += 2 )
	    {
	      for( j = 0; j < w; j += 2 )
	      {
	        dpos   =  ( i * w ) + j;
	        
	        c      =  rawDepth[dpos];
	                
	        if( c > nearThreshold && c < farThreshold )
	          depthImage.pixels[pos] = 0xffffff;
	        else
	          depthImage.pixels[pos] = 0;
	        
	        pos++;
	      }
	    }
	    
	    depthImage.updatePixels();
	  }
	  
	  /*
	  //draw depthImage quickly on screen
	  beginShape(QUADS);
	  texture(depthImage);
	  vertex(0,0,0,0);
	  vertex(320,0,320,0);
	  vertex(320,240,320,240);
	  vertex(0,240,0,240);
	  endShape();
	  //*/
	  
	  //openCV blob analysis
	  opencv.copy( depthImage );  
	  blobs = opencv.blobs( 75*75, 320*240, 50, false );
	  
	  if( blobs.length > 0 )
	  {  
	      blob = blobs[0];
	      
	     if( DRAW_KINECT_BLOBS )
	     {
	        fill(0,255,0,100);
	        noStroke();
	        int nx, ny;
	        
	        beginShape();        
	        for( i = 0; i < blob.points.length; i += 2 ) 
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

}
