package com.lbi.internetweek.views;

import com.lbi.internetweek.states.IBirdState;

import processing.core.PApplet;
import processing.core.PImage;
import toxi.physics2d.VerletParticle2D;

public class Bird 
{
	int 			SEED;
	int				FRAME_UPDATE_FREQ	=	6;
	
	PApplet 		p;
	PImage 			bird_sprites;
	PImage			bird;
	
	IBirdState		state;
	FlyingState		flyingState;

	int				mirrored = 1;
	
	int				NUM_FRAMES		=	2;
	PImage[]		frames			=	new PImage[NUM_FRAMES];	
	int				frame_counter	=	0;
	int 			flying_frame 	=	0;
	int bw, bh, bhw, bhh;
	
	public Bird( PApplet parent, PImage birdImage )
	{
		SEED			=	PApplet.floor(parent.random(100));
		
		p 				=	parent;
		bird_sprites 	=	birdImage;
		
		bw				=	128;
		bh				=	64;
		bhw				=	bw/2;
		bhh				=	64/2;
		
		setFrames();
		setNextFrame();
	}
	
	private void setFrames() 
	{
		int rows = 2, cols = 1;
		
		for (int i = 0; i < rows; i++)
		{
		    for (int j = 0; j < cols; j++)
		    {
		    	frames[(i * cols) + j] = bird_sprites.get(
		            i * bw,
		            j * bh,
		            bw,
		            bh
		        );
		    }
		}		
	}

	public void draw(VerletParticle2D vp)
	{
		//set vars
		mirrored = vp.getVelocity().x >= 0 ? -1 : 1;
		
		//check props
		checkWings();
		
		//draw bird
		p.pushMatrix();
	    	p.translate( vp.x, vp.y );
	    	p.scale(mirrored, 1);
	    	
	    	p.noStroke();
	    	p.beginShape();
	    	p.texture(bird);
	    	p.vertex( -bhw, -bh, 0, 0 );
	    	p.vertex( bhw, -bh, bw, 0 );
	    	p.vertex( bhw, 0, bw, bh );
	    	p.vertex( -bhw, 0, 0, bh );
	    	p.endShape();
	    	
	    p.popMatrix();
	}

	private void checkWings() 
	{
		if( (p.frameCount+SEED) % FRAME_UPDATE_FREQ == 0 )
		{
			flying_frame = (frame_counter++) % NUM_FRAMES;			
			setNextFrame();
		}
	}

	private void setNextFrame() 
	{
		//p.println(flying_frame);
		bird = frames[flying_frame];
	}

	public IBirdState getState() {
		return state;
	}

	public void setState(IBirdState state) {
		this.state = state;
	}
}
