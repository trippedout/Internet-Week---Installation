package com.lbi.internetweek.controller.game;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import com.lbi.internetweek.view.GUIMediator;

public class ShowCalibrationCommand extends SimpleCommand
{
	public void execute(INotification note)
	{
		( (GUIMediator) this.facade.retrieveMediator(GUIMediator.NAME ) )
			.showCalibration( (Integer) note.getBody() );
	}
}
