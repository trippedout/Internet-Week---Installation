package com.lbi.internetweek.view.components;

import hypermedia.video.Blob;
import processing.core.PApplet;
import processing.core.PImage;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.behaviors.AttractionBehavior;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.events.TwitterEvent;
import com.lbi.internetweek.events.TwitterEventListener;
import com.lbi.internetweek.model.AppProxy;
import com.lbi.internetweek.model.PhysicsProxy;
import com.lbi.internetweek.view.BirdsMediator;
import com.lbi.internetweek.view.boids.Flock;
import com.lbi.internetweek.views.TweetView;

public class BirdsView 
{
	private BirdsMediator 		_mediator;	
	private PApplet 			_pa;
	private PImage        		_birdImage;
	private Flock				_flock;
	private VerletPhysics2D		_physics;

	private Bird[]	        			_birds				=	new Bird[AppProxy.NUM_BIRDS];
	private int                   		_numAttractors 		=	20;
	private AttractionBehavior[]  		_attractionPool 	= 	new AttractionBehavior[_numAttractors];
	
	private Bird				_tweetingBird;

	public BirdsView(BirdsMediator mediator)
	{
		_pa 			=	(PApplet) ApplicationFacade.app;
		_mediator 		=	mediator;		
		_physics		=	PhysicsProxy.getPhysics();

		_flock			=	new Flock(_pa);

		_birdImage      =	_pa.loadImage( "bird_flying_sprites.png" );		
		AppProxy.setBirdImage(_birdImage);

		createBirds();
		createAttractors();
	}

	private void createBirds()
	{
		for( int i = 0; i < AppProxy.NUM_BIRDS; ++i )
		{
			_flock.add();

			_birds[i] = new Bird( _flock.get(i), _physics.particles.get(i) );			
			_birds[i].setBirds(_birds);
		}
	}

	private void createAttractors() 
	{	
		//attraction pool for use in keeping birds on/away from person
		for( int i = 0; i < _attractionPool.length; ++i )
		{
			Vec2D v = new Vec2D( -1000, -1000 );
			_attractionPool[i] = new AttractionBehavior(v, 250.0f, 0.0f);
		}
	}

	// --------------------------------------------------------------------------------------------------------
	// PUBLIC FUNCTIONS
	// --------------------------------------------------------------------------------------------------------

	public void update()
	{
		_flock.update();
	}

	public void draw()
	{
		for( int i = 0; i < AppProxy.NUM_BIRDS; ++i )
		{
			Bird b = _birds[i];
			b.draw();
		}
	}
	
	public Bird setTwitterBird()
	{
		Bird b = getRandomBird();
		
		if( b.isHurt || b.isTweeting )
			return setTwitterBird();
		else
		{
			b.setState(b.tweetState);
			b.isTweeting = true;			
			_tweetingBird = b;
			return b;
		}
	}

	public Bird getTweetingBird()
	{
		return _tweetingBird;
	}

	
	// --------------------------------------------------------------------------------------------------------
	// TEST FUNCTIONS
	// --------------------------------------------------------------------------------------------------------

	public Bird getRandomBird()
	{
		return _birds[ _pa.floor( _pa.random(AppProxy.NUM_BIRDS) ) ];
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
			for( int i = 0; i < _attractionPool.length; ++i ) 
			{
				if( !_physics.behaviors.contains(_attractionPool[i]) )
					_physics.addBehavior(_attractionPool[i]);
			}

			int spacer = _pa.floor(blob.points.length / _numAttractors);

			if( spacer >= 1 )
			{
				int count = 0;

				for( int i = 0; i < blob.points.length; i += spacer ) 
				{
					if( count < _attractionPool.length - 1 ) count++;

					AttractionBehavior b = _attractionPool[count];
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
				for( int i = 0; i < _attractionPool.length; ++i ) 
				{
					AttractionBehavior b = _attractionPool[i];
					b.setStrength( 0.0f );

					Vec2D v = b.getAttractor();
					v.x = -1000;
					v.y = -1000;
				}
			}
		}
		else //if blob is null get rid of attractors
		{
			for( int i = 0; i < _attractionPool.length; ++i ) 
			{
				if( _physics.behaviors.contains(_attractionPool[i]) )
					_physics.removeBehavior(_attractionPool[i]);
			}
		}	  
	}

	public Flock getFlock()
	{
		return _flock;
	}

	public void setGameMode()
	{
		// TODO Auto-generated method stub
		
	}

	public void setDanceMode()
	{
		// TODO Auto-generated method stub
		
	}

	public void setNormalMode()
	{
		// TODO Auto-generated method stub
		
	}

}
