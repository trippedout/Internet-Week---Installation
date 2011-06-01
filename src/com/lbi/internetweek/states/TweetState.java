package com.lbi.internetweek.states;

import processing.core.PVector;

import com.lbi.internetweek.view.components.Bird;

import de.looksgood.ani.Ani;

public class TweetState extends BirdState 
{	
	private int				FRAME_UPDATE_FREQ	=	6;
	private int				NUM_FRAMES			=	2;
	private float			MAX_SCALE			=	0.85f;
	
	private int				frame_counter		=	0;
	private int 			flying_frame 		=	0;
	
	private boolean			_isAnimatingLarger	=	false;	
	public float			_scale;
	
	public PVector			startingPosition;
	public PVector			startingVelocity;
	
	public TweetState( Bird b )
	{
		super(b);
		
		_scale = bird.scale;
	}

	@Override
	public void setState(IBirdState state)
	{
		if( state instanceof TweetState )
		{
			//Do nothing because it exists
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
		if( (bird.getParent().frameCount + bird.SEED) % FRAME_UPDATE_FREQ == 0 )
		{
			bird.birdTexture = bird.flyingFrames[ (frame_counter++) % NUM_FRAMES ];
		}
		
		bird.scale = _scale;
		
		if( !_isAnimatingLarger )
		{
			if( bird.scale < MAX_SCALE )
			{
				_isAnimatingLarger = true;
				Ani.to(this, 1.2f, "_scale", MAX_SCALE, Ani.CUBIC_OUT, "onEnd:onAnimateLargerComplete" );
			}
		}	
	}
	
	public void onAnimateLargerComplete(Ani theAni)
	{
		
		_isAnimatingLarger = false;
	}
	
	@Override
	public boolean setTweetState()
	{
		return false;
	}

}
