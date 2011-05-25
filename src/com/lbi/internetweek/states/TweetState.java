package com.lbi.internetweek.states;

import twitter4j.Status;

import com.lbi.internetweek.views.Bird;

public class TweetState extends BirdState 
{	
	int				FRAME_UPDATE_FREQ	=	6;
	int				NUM_FRAMES			=	2;
	
	int				frame_counter		=	0;
	int 			flying_frame 		=	0;
	
	public TweetState( Bird b )
	{
		super(b);
	}

	public void draw() 
	{
		if( (bird.getParent().frameCount + bird.SEED) % FRAME_UPDATE_FREQ == 0 )
		{
			bird.birdTexture = bird.flyingFrames[ (frame_counter++) % NUM_FRAMES ];
		}
	}
	
	@Override
	public boolean setTweetState()
	{
		return false;
	}

}
