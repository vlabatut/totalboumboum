package fr.free.totalboumboum.engine.control;

import java.util.Iterator;

import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;

public class ControlCode
{	private int keyCode;
	private boolean mode;
	
	public ControlCode(int keyCode, boolean mode)
	{	this.keyCode = keyCode;
		this.mode = mode;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public boolean getMode() {
		return mode;
	}

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
		}
	}
}
