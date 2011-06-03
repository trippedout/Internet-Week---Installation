package com.lbi.internetweek.states;

import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

import com.lbi.internetweek.model.AppProxy;
import com.lbi.internetweek.view.boids.Boid;
import com.lbi.internetweek.view.components.Bird;

import de.looksgood.ani.Ani;

public class FlyingState extends BirdState
{
	private int				FRAME_UPDATE_FREQ	=	5;
	private int				NUM_FRAMES			=	2;
	private float			MIN_SCALE			=	AppProxy.BIRD_MIN_SCALE;
	
	private int				frame_counter		=	0;
	private int 			flying_frame 		=	0;
	
	private boolean			_isAnimatingSmaller	=	false;
	private float			_scale;
	
	public FlyingState( Bird b, PImage[] frames )
	{
		super(b, frames);
		
		_scale = bird.scale;
	}

	@Override
	public void setState(IBirdState state)
	{
		super.setState(state);
		
		if( state instanceof TweetState )
		{
			//store vars for return
			bird.tweetState.startingPosition = new PVector( bird.getBoid().pos.x, bird.getBoid().pos.y );
			bird.tweetState.startingVelocity = new PVector( bird.getBoid().vel.x, bird.getBoid().vel.y );
			
			bird.isTweeting = true;
			bird.state = bird.tweetState;
		}
		else if( state instanceof HurtState )
		{
			bird.hurtState.startingPosition = bird.getBoid().pos;
			
			bird.isHurt = true;
			bird.state = bird.hurtState;
		}
	}
	
	@Override
	public void draw() 
	{
		//calculate flying frame
		if( (bird.getParent().frameCount + bird.SEED) % FRAME_UPDATE_FREQ == 0 )
		{
//			bird.birdTexture = bird.flyingFrames[ (frame_counter++) % NUM_FRAMES ];
			bird.birdTexture = _birdFrames[ (frame_counter++) % NUM_FRAMES ];
		}

		if( !_isAnimatingSmaller )
		{
			if( bird.scale > MIN_SCALE )
			{
				_scale = bird.scale;
				_isAnimatingSmaller = true;
				Ani.to(this, 1.2f, "_scale", MIN_SCALE, Ani.CUBIC_OUT, "onEnd:onAnimateSmallerComplete" );
			}
		}
		
		Boid b = bird.getBoid();
		bird.x 			= b.pos.x;
		bird.y 			= b.pos.y;		
		bird._velocity 	= b.vel;
		bird.scale 		= _scale;		
		//bird.rotation 	= b.vel.heading2D();
	}
	
	public void onAnimateSmallerComplete(Ani theAni)
	{
		_isAnimatingSmaller = false;
	}
	
	
}
