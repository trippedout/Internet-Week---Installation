package com.lbi.internetweek.controller;

import org.puremvc.java.patterns.command.MacroCommand;

import com.lbi.internetweek.ApplicationFacade;

public class StartupCommand extends MacroCommand
{	
	@Override
	protected void initializeMacroCommand()
	{
		addSubCommand(new PrepModelCommand());
		addSubCommand(new PrepViewCommand());
		
		this.facade.removeCommand(ApplicationFacade.STARTUP);
	}
}
