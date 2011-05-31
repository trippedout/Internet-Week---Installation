package com.lbi.internetweek.model;

import java.util.Hashtable;

import org.puremvc.java.patterns.observer.Notification;
import org.puremvc.java.patterns.proxy.Proxy;

import processing.core.PImage;

public class AppProxy extends Proxy
{
	final public static String NAME = "AppProxy";
	
	final public static String MODE_CHANGE		=	"mode_change";
	
	final public static int 	MODE_NORMAL		=	0;
	final public static int 	MODE_GAME		=	1;
	final public static int 	MODE_DANCE		=	2;
	
	public static final int 	NUM_BIRDS		=	20;	
	public static PImage 		_birdImage;

	private int	_mode			=	MODE_NORMAL;
	
	public AppProxy()
	{
		super(NAME);
	}
		
	public Hashtable getItems()
    {
        return (Hashtable) super.getData();
    }

    public void addItem(Object item)
    { 
        Hashtable temp = getItems();
        temp.put(item.toString(), item);
    }
    
    public static void setBirdImage( PImage bi )
    {
    	_birdImage = bi;
    }
    
    public static PImage getBirdImage()
    {
    	return _birdImage;
    }
    
    public void setMode(int mode)
    {
    	_mode = mode;
    	this.facade.notifyObservers( new Notification(MODE_CHANGE, _mode) );
    }
}
