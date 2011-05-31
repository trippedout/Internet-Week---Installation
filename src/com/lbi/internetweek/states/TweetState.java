package com.lbi.internetweek.states;

import twitter4j.Status;

import com.lbi.internetweek.view.components.Bird;

import de.looksgood.ani.Ani;

public class TweetState extends BirdState 
{	
	private int				FRAME_UPDATE_FREQ	=	6;
	private int				NUM_FRAMES			=	2;
	private float			MAX_SCALE			=	0.8f;
	
	private int				frame_counter		=	0;
	private int 			flying_frame 		=	0;
	
	private boolean			_isAnimatingLarger	=	false;	
	public float			_scale;
	
	public TweetState( Bird b )
	{
		super(b);
		
		_scale = bird.scale;		
	}

	public void draw() 
	{
		if( (bird.getParent().frameCount + bird.SEED) % FRAME_UPDATE_FREQ == 0 )
		{
			bird.birdTexture = bird.flyingFrames[ (frame_counter++) % NUM_FRAMES ];
		}
		
		bird.scale = _scale;
		
		if( !isAnimatingLarger )
		{
			if( bird.scale < MAX_SCALE )
			{
				isAnimatingLarger = true;
				Ani.to(this, 1.2f, "_scale", 1, Ani.CUBIC_OUT, "onEnd:onAnimateLargerComplete" );
			}
		}	
	}
	
	public void onAnimateLargerComplete(Ani theAni)
	{
		isAnimatingLarger = false;
	}
	
	@Override
	public boolean setTweetState()
	{
		return false;
	}

}
