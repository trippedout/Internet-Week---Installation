package com.lbi.internetweek.view;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

import com.lbi.internetweek.ApplicationFacade;
import com.lbi.internetweek.view.components.BackgroundView;

public class BackgroundMediator extends Mediator
{
	final public static String NAME = "BackgroundMediator";
	
	private BackgroundView background;
	
	public BackgroundMediator()
	{
		super( NAME, null );
		
		getBackground().loadImages();
	}
	
	private BackgroundView getBackground()
	{
		if( background == null )
		{
			background = new BackgroundView(this);			
		}
		
		return background;
	}

	public void handleNotification(INotification note)
	{
		if( note.getName().equals(ApplicationFacade.DRAW) )
		{
			System.out.println("BG NOTE DRAW");
		}
	}
	
	public void draw()
	{
		getBackground().draw();
	}

}
