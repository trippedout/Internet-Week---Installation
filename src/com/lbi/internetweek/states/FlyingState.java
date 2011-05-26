package com.lbi.internetweek.states;

import com.lbi.internetweek.views.Bird;
import com.lbi.internetweek.views.boids.Boid;

public class FlyingState extends BirdState
{
	int				FRAME_UPDATE_FREQ	=	5;
	int				NUM_FRAMES			=	2;
	
	int				frame_counter		=	0;
	int 			flying_frame 		=	0;
	
	public FlyingState( Bird b )
	{
		super(b);
	}

	@Override
	public void draw() 
	{
		//calculate flying frame
		if( (bird.getParent().frameCount + bird.SEED) % FRAME_UPDATE_FREQ == 0 )
		{
			bird.birdTexture = bird.flyingFrames[ (frame_counter++) % NUM_FRAMES ];
		}
		
		Boid b = bird.getBoid();
		
		bird.x = b.pos.x;
		bird.y = b.pos.y;		
		bird.velocity = b.vel;
	}
	
}
