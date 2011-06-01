package com.lbi.internetweek.view.components;

import java.lang.reflect.Method;
import java.util.ArrayList;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.model.AppProxy;
import com.lbi.internetweek.states.FlyingState;
import com.lbi.internetweek.states.HurtState;
import com.lbi.internetweek.states.IBirdState;
import com.lbi.internetweek.states.PerchState;
import com.lbi.internetweek.states.TweetState;
import com.lbi.internetweek.view.boids.Boid;

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
	public PVector				_velocity	=	new PVector();

	public float 			scale			=	AppProxy.BIRD_MIN_SCALE;
	public float			rotation		=	0;
	public int				mirrored 		=	1;
	public float			x, y, lx, ly;
	
	public Method			callback;
	public Tweet 			tweetRef;
	
	public ArrayList<Bird>			birds;
	
	public boolean			isTweeting		=	false;
	public boolean			isHurt			=	false;
	
	private PApplet 		_pa;
	private PImage 			_birdImage;	
	
	//states
	public IBirdState		state;
	public IBirdState		lastState;
	public PerchState		perchState;
	public TweetState		tweetState;
	public FlyingState		flyingState;
	public HurtState		hurtState;
	
	int bw, bh, bhw, bhh;
	
	
	public Bird( Boid boid, VerletParticle2D verletParticle2D )//, Vec2D p )
	{
		_pa				=	ApplicationFacade.app;
		_birdImage		=	AppProxy.getBirdImage();		
		
		//state helpers
		_vp				=	verletParticle2D;
		_boid			=	boid;
		
		//animation seed
		SEED			=	_pa.floor(_pa.random(100));
		
		//height and width of bird images
		bw				=	192;
		bh				=	192;
		bhw				=	bw/2;
		bhh				=	bh/2;
		
		setFrames();
		setStates();
	}

	// --------------------------------------------------------------------------------------------------------
	// SETUP FUNCTIONS
	// --------------------------------------------------------------------------------------------------------

	private void setFrames() 
	{
		int rows = 2, cols = 1;
		
		//flying frames
		for (int i = 0; i < rows; i++)
		{
		    for (int j = 0; j < cols; j++)
		    {
		    	flyingFrames[(i * cols) + j] = _birdImage.get(
		            i * bw,
		            j * bh,
		            bw,
		            bh
		        );
		    }
		}	
		
		rows = 1;
		
		//_perched frames
		perchedFrames[0] = _birdImage.get(0,64,bw,bh);
	}

	private void setStates() 
	{
		perchState		=	new PerchState(this);
		tweetState		=	new TweetState(this);
		flyingState		=	new FlyingState(this);
		hurtState		=	new HurtState(this);

		state = flyingState;
		setState( flyingState );
	}

	// --------------------------------------------------------------------------------------------------------
	// DRAW FUNCTIONS
	// --------------------------------------------------------------------------------------------------------

	public void draw()
	{	
		//draw state to update values
		state.draw();
		
		mirrored = _velocity.x >= 0 ? -1 : 1;
		
		//draw bird
		if( birdTexture != null )
		{
			_pa.pushMatrix();
		    	_pa.translate( x, y );
		    	_pa.scale(mirrored * scale, scale);
		    	//_pa.rotate( rotation );		    	
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

	// --------------------------------------------------------------------------------------------------------
	// STATE CHANGING FUNCTIONS
	// --------------------------------------------------------------------------------------------------------
		
	public IBirdState getState() 
	{
		return state;
	}

	public IBirdState getLastState()
	{
		return lastState;
	}

	public void setState(IBirdState state) 
	{
		this.lastState = this.state;		
		this.state.setState( state );
		this.state.draw();
	}

	// --------------------------------------------------------------------------------------------------------
	// PUBLIC FUNCTIONS
	// --------------------------------------------------------------------------------------------------------

	public PApplet getParent()
	{
		return _pa;
	}

	public IBirdState getRandomState() 
	{
		//for testing
		return flyingState;
	}

	public ArrayList<Bird> getBirds() {
		return birds;
	}

	public void setBirds(ArrayList<Bird> _birds) {
		this.birds = _birds;
	}
	
	public VerletParticle2D getParticle() {
		return _vp;
	}

	public Boid getBoid() {
		return _boid;
	}
	
	public PVector getVec()
	{
		return new PVector(x,y);
	}


}
