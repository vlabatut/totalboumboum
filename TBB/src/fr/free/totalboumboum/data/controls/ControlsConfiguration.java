package fr.free.totalboumboum.data.controls;

import java.util.HashMap;

public class ControlsConfiguration
{
	
	/////////////////////////////////////////////////////////////////
	// CONTROLS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<Integer,ControlSettings> controlSettings = new HashMap<Integer,ControlSettings>();
	
	public HashMap<Integer,ControlSettings> getControlSettings()
	{	return controlSettings;	
	}
	
	public void putControlSettings(int index, ControlSettings controlSetting)
	{	controlSettings.put(index,controlSetting);
	}
}
