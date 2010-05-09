package fr.free.totalboumboum.data.profile;

import java.util.ArrayList;
import java.util.HashMap;

public class ControlSettings
{
	private HashMap<Integer,String> onKeys;
	private HashMap<Integer,String> offKeys;
	private ArrayList<String> autofires;
	
	public ControlSettings()
	{	onKeys = new HashMap<Integer,String>();
		offKeys = new HashMap<Integer,String>();
		autofires = new ArrayList<String>(); 
	}
	
	public boolean containsOnKey(int key)
	{	return onKeys.containsKey(key);
	}
	public void addOnKey(int key, String event)
	{	onKeys.put(key,event);		
	}
	public String getEventFromKey(int keyCode)
	{	String result = onKeys.get(keyCode);
		if(result==null)
			result = offKeys.get(keyCode);
		return result;
	}
	
	public boolean containsOffKey(int key)
	{	return offKeys.containsKey(key);
	}
	public void addOffKey(int key, String event)
	{	offKeys.put(key,event);		
	}
	
	public boolean isAutofire(String event)
	{	return autofires.contains(event);		
	}
	public void addAutofire(String event)
	{	autofires.add(event);		
	}
}
