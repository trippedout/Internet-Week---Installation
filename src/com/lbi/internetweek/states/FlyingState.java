package com.lbi.internetweek.states;

import com.lbi.internetweek.view.boids.Boid;
import com.lbi.internetweek.view.components.Bird;

public class FlyingState extends BirdState
{
	private int				FRAME_UPDATE_FREQ	=	5;
	private int				NUM_FRAMES			=	2;
	private float			MIN_SCALE			=	0.35f;
	
	private int				frame_counter		=	0;
	private int 			flying_frame 		=	0;
	
	private boolean			_isAnimatingSmaller	=	false;
	private float			_scale;
	
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
		bird._velocity = b.vel;		
	}
	
}
