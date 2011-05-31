package com.lbi.internetweek.view.components;

import hypermedia.video.Blob;
import hypermedia.video.OpenCV;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.Installation;
import com.lbi.internetweek.view.KinectMediator;

import processing.core.PImage;

public class KinectView
{
	//debug
	boolean DRAW_KINECT_BLOBS        	=	true;
	int COUNT_UNTIL_KILL_KINECT_USERS	=	120;
	
	private KinectMediator			_mediator;
	private Installation			_pa;
	private OpenCV					_opencv;

	private PImage          _depthImage, _rgbImage;
	
	private Blob[]          blobs;
	private Blob            blob;
	private int				blobCounter = 0;

	public KinectView(KinectMediator mediator)
	{
		_mediator		=	mediator;
		_pa				=	ApplicationFacade.app;

		_depthImage   		=	_pa.createImage( 320, 240, _pa.RGB );
		_rgbImage   		=	_pa.createImage( 320, 240, _pa.RGB );

		setupOpenCV();
	}

	private final void setupOpenCV()
	{
		_opencv       =  new OpenCV(_pa);  
		_opencv.allocate( _depthImage.width, _depthImage.height );
	}

	public void drawOutlines()
	{
		//PImage rgb = _mediator.getRGBImage();		
		_opencv.copy( _depthImage );  

		//arbitrary size numbers for finding blobs big enough to be people
		blobs = _opencv.blobs( 75*75, 320*240, 10, false );

		if( blobs.length > 0 )
		{  
			int numBlobs, nx, ny;
			
			blobCounter = 0;

			if( DRAW_KINECT_BLOBS )
			{
				if( blobs.length <= 3 ) 
					numBlobs = blobs.length;
				else
					numBlobs = 3;
				
				for( int j = 0; j < numBlobs; ++j )
				{
					blob 	=	blobs[j];
					
					_pa.fill(50,200,50,20);
					_pa.strokeWeight(3f);
					_pa.stroke(255,0,0,185);
	
					_pa.beginShape();
					
					for( int i = 0; i < blob.points.length; i += 3 ) 
					{
						nx = _pa.parseInt( _pa.map( blob.points[i].x, 0, 320, 0, _pa.width ) );
						ny = _pa.parseInt( _pa.map( blob.points[i].y, 0, 240, 0, _pa.height ) );
	
						_pa.vertex( nx, ny );
					}			
					
					_pa.endShape(_pa.CLOSE);
					_pa.noStroke();
				}
			}
		}
		else
		{
			blob = null;
			blobCounter++;

			if( blobCounter == COUNT_UNTIL_KILL_KINECT_USERS)
			{
				_mediator.killUsers();
			}
		}
	}

	public PImage getRGBImage()
	{
		return _rgbImage;
	}

	public PImage getDepthImage() 
	{
		return _depthImage;
	}
}
