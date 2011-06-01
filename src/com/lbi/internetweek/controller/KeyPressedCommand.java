package com.lbi.internetweek.controller;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import processing.core.PApplet;
import processing.core.PConstants;

public class KeyPressedCommand extends SimpleCommand
{
	public void execute(INotification note)
	{	
		switch( (Integer) note.getBody() )
		{
		case PConstants.UP:
			PApplet.println("UP KEY PRESSED");
			break;
			
		case PConstants.DOWN:
			PApplet.println("DOWN KEY PRESSED");
			break;
		}
	}
}
