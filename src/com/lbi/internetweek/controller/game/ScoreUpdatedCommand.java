package com.lbi.internetweek.controller.game;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import com.lbi.internetweek.view.GUIMediator;

public class ScoreUpdatedCommand extends SimpleCommand
{
	public void execute(INotification note)
	{
		( (GUIMediator) this.facade.retrieveMediator(GUIMediator.NAME) )
			.updateScore( (Integer) note.getBody() );
	}
}
