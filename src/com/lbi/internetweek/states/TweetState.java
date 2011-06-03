package com.lbi.internetweek.states;

import processing.core.PApplet;
import processing.core.PVector;
import processing.core.PImage;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.model.AppProxy;
import com.lbi.internetweek.view.components.Bird;

import de.looksgood.ani.Ani;

public class TweetState extends BirdState 
{	
	private int				FRAME_UPDATE_FREQ	=	6;
	private int				NUM_FRAMES			=	2;
	private float			MAX_SCALE			=	0.85f;
	private int				MIN_X				=	100;
	private int				MIN_Y				=	180;
	private int				MAX_X				=	ApplicationFacade.app.width - 325;
	
	private int				frame_counter		=	0;
	private int 			flying_frame 		=	0;
	
	private boolean			_isAnimatingLarger	=	false;	
	public float			_scale;
	
	public PVector			startingPosition;
	public PVector			startingVelocity;
	
	public TweetState( Bird b, PImage[] frames )
	{
		super(b, frames);
		
		_scale = bird.scale;
	}

	@Override
	public void setState(IBirdState state)
	{
		super.setState(state);
		
		if( state instanceof HurtState )
		{
			bird.isHurt = true;
			bird.state = bird.hurtState;
			//break tweet bubble
		}
		else if( state instanceof FlyingState )
		{
			bird.getBoid().pos = startingPosition;
			bird.getBoid().vel = startingVelocity;
			
			bird.state = bird.flyingState;
		}		
	}
	
	public void draw() 
	{
		//calculate flying frame
		if( (bird.getParent().frameCount + bird.SEED) % FRAME_UPDATE_FREQ == 0 )
		{
			bird.birdTexture = _birdFrames[ (frame_counter++) % NUM_FRAMES ];
		}
		
		/*
		if( (bird.getParent().frameCount + bird.SEED) % FRAME_UPDATE_FREQ == 0 )
		{
			bird.birdTexture = bird.flyingFrames[ (frame_counter++) % NUM_FRAMES ];
		}
		*/
		
		bird.scale = _scale;
		
		if( !_isAnimatingLarger )
		{
			if( bird.scale < MAX_SCALE )
			{
				_isAnimatingLarger = true;
				Ani.to(this, 0.6f, "_scale", MAX_SCALE, Ani.CUBIC_OUT, "onEnd:onAnimateLargerComplete" );
								
				if( bird.x < MIN_X )
				{
					startingPosition.x 	= 	MIN_X;
					bird._velocity.x 	=	-5;
					Ani.to(bird, 0.6f, "x", MIN_X, Ani.CUBIC_OUT );
				}
				else if ( bird.x > MAX_X )
				{
					startingPosition.x 	= MAX_X;
					bird._velocity.x 	=	5;
					Ani.to(bird, 0.6f, "x", MAX_X, Ani.CUBIC_OUT );
				}
				
				if( bird.y < MIN_Y )
				{
					startingPosition.y = MIN_Y;
					Ani.to(bird, 0.6f, "y", MIN_Y, Ani.CUBIC_OUT );
				}
			}
		}	
	}
	
	public void onAnimateLargerComplete(Ani theAni)
	{
		//bird.sendTweetCallback();
		_isAnimatingLarger = false;
	}

}
