package com.lbi.internetweek.views;

import java.util.ArrayList;
import java.util.List;

import com.lbi.internetweek.events.TwitterEvent;
import com.lbi.internetweek.events.TwitterEventListener;
import com.lbi.internetweek.view.boids.Boid;
import com.lbi.internetweek.view.boids.Flock;
import com.lbi.internetweek.view.boids.Zone;
import com.lbi.internetweek.view.components.Bird;

import hypermedia.video.Blob;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.behaviors.AttractionBehavior;

import twitter4j.Status;

public class BirdsController 
{
	//birds
	public static int 	NUM_BIRDS 		=	30;
	Bird[]	        	_birds			=	new Bird[NUM_BIRDS];
	Flock				_flock;
	Perch				_perch;
	
	//main
	PApplet				_pa;
	
	//physics
	VerletPhysics2D			_physics;
	int                   	numAttractors = 20;
	AttractionBehavior[]  	attractionPool = new AttractionBehavior[numAttractors];
		
	//tweets
	List<Status>			_tweetsQueue		=	new ArrayList<Status>();
	boolean 				_tweetIsShowing		=	false;
	TweetView				_tweet;
	TwitterEventListener	_tweetListener;
	Bird					_tweetBird;
	
	
	public BirdsController( PApplet p, VerletPhysics2D physics )
	{
		_pa				=	p;
		_flock			=	new Flock(_pa);
		_physics		=	physics;
		
		//_birdImage      =	_pa.loadImage( "bird_flying_sprites.png" );
		_perch			=	new Perch( _pa, NUM_BIRDS );
		
		createBirds();
		createAttractors();
		createTweetListeners();
	}
	
	private void createBirds() 
	{
		/*
		for( int i = 0; i < NUM_BIRDS; ++i )
		{
			//*
			_flock.add();
			
			_birds[i] = new Bird( _pa, 
					_birdImage, 
					_flock.get(i),
					_physics.particles.get(i),
					_perch.getSpot(i)
			);
			_birds[i].setBirds(_birds);			
			
		}
		//*/
	}

	private void createAttractors() 
	{	
		//attraction pool for use in keeping birds on/away from person
		for( int i = 0; i < attractionPool.length; ++i )
		{
			Vec2D v = new Vec2D( -1000, -1000 );
			attractionPool[i] = new AttractionBehavior(v, 250.0f, 0.0f);
		}
	}

	private void createTweetListeners() 
	{
		_tweet					=	new TweetView(_pa);		
		
		_tweetListener			=	new TwitterEventListener()		
		{			
			@Override
			public void onEvent(TwitterEvent evt) 
			{
				_tweet.killTweet();
				
				_tweetBird.setState(_tweetBird.getLastState());
				_tweetBird 		=	null;
				_tweetIsShowing = 	false;
			}
		};

		_tweet.addListener(_tweetListener);
	}

	// --------------------------------------------------------------------------------------------------------
	// PUBLIC FUNCTIONS
	// --------------------------------------------------------------------------------------------------------

	public void draw()
	{
		_flock.update();
		checkQueue();
		
		for( int i = 0; i < NUM_BIRDS; ++i )
		{
			Bird b = _birds[i];
			if( b != _tweetBird ) b.draw();
		}	
		
		if(_tweetIsShowing)
		{
			_tweetBird.draw();
			_tweet.draw( _tweetBird.x, _tweetBird.y );
		}
	}
	
	// --------------------------------------------------------------------------------------------------------
	// TWITTER FUNCTIONS
	// --------------------------------------------------------------------------------------------------------
	
	public void addTweetToQueue(Status status) 
	{
		_tweetsQueue.add(status);
	}

	private void checkQueue() 
	{
		if( !_tweetIsShowing && _tweetsQueue.size() > 0 )
		{
			Bird b = getRandomBird();
			
			/*
			if( b.setTweetState() )
			{				
				_tweetIsShowing 	=	true;
				_tweetBird			=	b;
								
				Status s = _tweetsQueue.iterator().next();
				_tweetsQueue.remove(s);
				
				_tweet.showTweet( s );
			}
			else
				checkQueue();
				//*/
		}
	}

	// --------------------------------------------------------------------------------------------------------
	// TEST FUNCTIONS
	// --------------------------------------------------------------------------------------------------------
	
	public Bird getRandomBird()
	{
		return _birds[ _pa.floor( _pa.random(NUM_BIRDS) ) ];
	}
	
	public void testState() 
	{
		Bird b = getRandomBird();
		b.setState( b.getRandomState() );
	}	

	// --------------------------------------------------------------------------------------------------------
	// PHSYICS FUNCTIONS
	// --------------------------------------------------------------------------------------------------------
	
	public void updatePhysics(Blob blob)
	{
		if( blob != null )
		{
			for( int i = 0; i < attractionPool.length; ++i ) 
		    {
				if( !_physics.behaviors.contains(attractionPool[i]) )
					_physics.addBehavior(attractionPool[i]);
		    }
			
			int spacer = _pa.floor(blob.points.length / numAttractors);
		     
			if( spacer >= 1 )
			{
				int count = 0;
		         
		        for( int i = 0; i < blob.points.length; i += spacer ) 
		        {
		        	if( count < attractionPool.length - 1 ) count++;
		          
		        	AttractionBehavior b = attractionPool[count];
		        	b.setStrength( -0.2f );
		          
		        	Vec2D v = b.getAttractor();
		        	v.x = _pa.parseInt( _pa.map( blob.points[i].x, 0, 320, 0, _pa.width ) );
		        	v.y = _pa.parseInt( _pa.map( blob.points[i].y, 0, 240, 0, _pa.height ) );
		          
		        	/*
		        	_pa.fill(255,0,0,100);
		        	_pa.ellipse(v.x, v.y, 8, 8);
		        	_pa.noFill();
		        	//*/
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
		else //if blob is null get rid of attractors
		{
			for( int i = 0; i < attractionPool.length; ++i ) 
		    {
				if( _physics.behaviors.contains(attractionPool[i]) )
					_physics.removeBehavior(attractionPool[i]);
		    }
		}	  
	}

	// --------------------------------------------------------------------------------------------------------
	// GET SET FUNCTIONS
	// --------------------------------------------------------------------------------------------------------
	
	public Flock getFlock()
	{
		return _flock;
	}

}
