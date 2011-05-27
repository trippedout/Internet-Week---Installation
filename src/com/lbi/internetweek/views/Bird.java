package com.lbi.internetweek.views;

import com.lbi.internetweek.states.FlyingState;
import com.lbi.internetweek.states.IBirdState;
import com.lbi.internetweek.states.PerchState;
import com.lbi.internetweek.states.TweetState;
import com.lbi.internetweek.views.boids.Boid;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import twitter4j.Status;

public class Bird 
{
	public int 			SEED;
	
	public PImage[]		flyingFrames		=	new PImage[2];
	public PImage[]		perchedFrames		=	new PImage[1];
	
	public PImage		birdTexture;
	public Vec2D		_perch;

	public VerletParticle2D		_vp;
	public Boid					_boid;
	public PVector				velocity;
	
	public int			mirrored = 1;
	public float 		scale = .3f;
	public float		x, y, lx, ly;
	
	public Bird[]		birds;
	
	PApplet 			_pa;
	PImage 				sprites;	
	
	IBirdState		state;
	IBirdState		lastState;
	public PerchState		perchState;
	public TweetState		tweetState;
	public FlyingState		flyingState;
	
	int bw, bh, bhw, bhh;
	
	public Bird( PApplet parent, PImage birdImage, Boid boid, VerletParticle2D verletParticle2D, Vec2D p )
	{
		SEED			=	PApplet.floor(parent.random(100));		
		
		_pa				=	parent;
		sprites		 	=	birdImage;
		_vp				=	verletParticle2D;
		_boid			=	boid;
		_perch			=	p;
		
		velocity		=	new PVector();		
		
		bw				=	192;
		bh				=	192;
		bhw				=	bw/2;
		bhh				=	bh/2;
		
		setFrames();
		setStates();
		
		state = flyingState;
		setState( flyingState );
	}

	// --------------------------------------------------------------------------------------------------------
	// SETUP FUNCTIONS
	// --------------------------------------------------------------------------------------------------------

	private void setFrames() 
	{
		//birdTexture		=	_pa.createImage(bw, bh, _pa.RGB);
		
		int rows = 2, cols = 1;
		
		//flying frames
		for (int i = 0; i < rows; i++)
		{
		    for (int j = 0; j < cols; j++)
		    {
		    	flyingFrames[(i * cols) + j] = sprites.get(
		            i * bw,
		            j * bh,
		            bw,
		            bh
		        );
		    }
		}	
		
		rows = 1;
		
		//_perched frames
		perchedFrames[0] = sprites.get(0,64,bw,bh);
		/*
		for (int i = 0; i < rows; i++)
		{
		    for (int j = 1; j < cols; j++)
		    {
		    	_perchedFrames[(i * cols) + j-1] = sprites.get(
		            i * bw,
		            j * bh,
		            bw,
		            bh
	            );
		    }
		}
		//*/
	}

	private void setStates() 
	{
		perchState		=	new PerchState(this);
		tweetState		=	new TweetState(this);
		flyingState		=	new FlyingState(this);
	}
	

	// --------------------------------------------------------------------------------------------------------
	// STATE CHANGING FUNCTIONS
	// --------------------------------------------------------------------------------------------------------

	public IBirdState getState() 
	{
		return state;
	}

	public IBirdState getLastState() {
		return lastState;
	}

	public void setState(IBirdState state) 
	{
		this.lastState = this.state;
		this.state = state;
		this.state.draw();
	}

	public boolean setTweetState()
	{
		return state.setTweetState();
	}

	// --------------------------------------------------------------------------------------------------------
	// PUBLIC FUNCTIONS
	// --------------------------------------------------------------------------------------------------------

	public void draw()
	{	
		//draw state to update values
		state.draw();
		
		mirrored = velocity.x >= 0 ? -1 : 1;
		
		//draw bird
		if( birdTexture != null )
		{			
			_pa.pushMatrix();
		    	_pa.translate( x, y );
		    	_pa.scale(mirrored * scale, scale);
		    	
		    	_pa.noStroke();
		    	_pa.beginShape();
		    	_pa.texture(birdTexture);
		    	_pa.vertex( -bhw, -bh, 0, 0 );
		    	_pa.vertex( bhw, -bh, bw, 0 );
		    	_pa.vertex( bhw, 0, bw, bh );
		    	_pa.vertex( -bhw, 0, 0, bh );
		    	_pa.endShape();	    	
		    _pa.popMatrix();
		}
	    
	    lx = x;
	    ly = y;
	}

	public PApplet getParent()
	{
		return _pa;
	}

	public IBirdState getRandomState() 
	{
		//for testing
		return flyingState;
	}

	public Bird[] getBirds() {
		return birds;
	}

	public void setBirds(Bird[] birds) {
		this.birds = birds;
	}
	
	public VerletParticle2D getParticle() {
		return _vp;
	}

	public Boid getBoid() {
		return _boid;
	}


}
