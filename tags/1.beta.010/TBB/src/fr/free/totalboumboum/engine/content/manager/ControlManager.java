package fr.free.totalboumboum.engine.content.manager;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import fr.free.totalboumboum.configuration.controls.ControlSettings;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.control.ControlCode;
import fr.free.totalboumboum.engine.loop.Loop;

public class ControlManager
{	/** managed sprite  */
	protected Sprite sprite;
	/** controls waiting to be processed */
	protected LinkedList<ControlCode> controlCodes;
	/** events waiting to be transmitted */
	protected LinkedList<ControlEvent> controlEvents;
	/** list of current control */
	protected LinkedList<Integer> currentControls;
	
	protected ControlSettings controlSettings;
	
	
	protected int compte=0;
	
	public ControlManager(Sprite sprite)
	{	this.sprite = sprite;
		controlCodes = new LinkedList<ControlCode>();
		controlEvents = new LinkedList<ControlEvent>();
		currentControls = new LinkedList<Integer>();
	}	

	public void setControlSettings(ControlSettings controlSettings)
	{	this.controlSettings = controlSettings;
	}
	
	public Sprite getSprite()
	{	return sprite;
	}
	public void setSprite(Sprite sprite)
	{	this.sprite = sprite;
	}

	public Loop getLoop()
	{	return sprite.getLoop();
	}
	
	public synchronized void putControlCode(ControlCode controlCode)
	{	controlCodes.offer(controlCode);	
	}

	public synchronized void putControlEvent(ControlEvent controlEvent)
	{	controlEvents.offer(controlEvent);	
	}

	public synchronized void update()
	{	if(currentControls.size()>0 || controlCodes.size()>0)
			updateCodes();
		else if(controlEvents.size()>0)
			updateEvents();
	}
	
	private void updateEvents()
	{	// transmiting the events to the event manager
		Iterator<ControlEvent> i = controlEvents.iterator();
		while(i.hasNext())
		{	ControlEvent temp = i.next();
			i.remove();
			sprite.processEvent(temp);
//System.out.println("reçu:"+temp.getName()+","+temp.getMode());			
		}
	}
	
	private void updateCodes()
	{	boolean debug = false;
//		boolean test = controlCodes.size()>0; 
		if(debug /*&& test*/)		
		{	System.out.println("--"+compte+"--");
			compte++;
			System.out.print("controlCodes: [ ");
			for(int i=0;i<controlCodes.size();i++)
				System.out.print("("+controlCodes.get(i).getKeyCode()+","+controlCodes.get(i).getMode()+") ");
			System.out.println(" ]");
			//
			System.out.print("currentControls: [ ");
			for(int i=0;i<currentControls.size();i++)
				System.out.print(currentControls.get(i)+" ");
			System.out.println(" ]");
		}
	
		// initializing the events list with the existing autofire codes
		LinkedList<ControlEvent> eventsList = new LinkedList<ControlEvent>();
		for(int j=currentControls.size()-1;j>=0;j--)
		{	int keyCode = currentControls.get(j);
			ArrayList<String> eventNames = controlSettings.getEventsFromOnKey(keyCode);
			Iterator<String> it = eventNames.iterator();
			while(it.hasNext())
			{	String eventName = it.next();
				if(controlSettings.isAutofire(eventName))
				{	ControlEvent event = new ControlEvent(eventName,true);
					eventsList.offer(event);
				}
			}
		}
		
		// parsing the new control codes
		while(controlCodes.size()>0)
		{	ControlCode cc = controlCodes.poll();
			int keyCode = cc.getKeyCode();
			// if a key is pressed : new control in currentControls & new event in eventsList
			if(cc.getMode())
			{	ArrayList<String> eventNames = controlSettings.getEventsFromOnKey(keyCode);
				if(!currentControls.contains(keyCode))
					currentControls.offer(keyCode);
				Iterator<String> it = eventNames.iterator();
				while(it.hasNext())
				{	String eventName = it.next();
					ControlEvent event = new ControlEvent(eventName,true);
					eventsList.offer(event);
				}
			}
			// if a key is released : control removed from currentControls & new event in eventsList
			else
			{	ArrayList<String> eventNames = controlSettings.getEventsFromOffKey(keyCode);
				if(currentControls.contains(keyCode))
					currentControls.remove(new Integer(keyCode));
				Iterator<String> it = eventNames.iterator();
				while(it.hasNext())
				{	String eventName = it.next();
					ControlEvent event = new ControlEvent(eventName,true);
					removeEvent(eventsList,event);
					event = new ControlEvent(eventName,false);
					eventsList.offer(event);
				}
			}		
		}
		
		// transmiting the events to the event manager
		Iterator<ControlEvent> i = eventsList.iterator();
		while(i.hasNext())
		{	ControlEvent temp = i.next();
			sprite.processEvent(temp);
		}

		if(debug /*&& test*/)		
		{	System.out.print("eventsList: [ ");
			i = eventsList.iterator();
			while(i.hasNext())
			{	ControlEvent temp = i.next();
				System.out.print(temp.getName()+" ");
			}		
			System.out.println(" ]");
		}		
	}
	
//	protected abstract void processEvent();
	
	private void removeEvent(LinkedList<ControlEvent> eventsList, ControlEvent event)
	{	boolean found = false;
		int i = eventsList.size()-1;
		while(!found && i>=0)
		{	ControlEvent temp = eventsList.get(i);
			if(temp.getName().equals(event.getName()))
			{	found = true;
				eventsList.remove(i);
			}
			else
				i--;
		}		
	}

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// control codes
			{	Iterator<ControlCode> it = controlCodes.iterator();
				while(it.hasNext())
				{	ControlCode temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// control events
			{	Iterator<ControlEvent> it = controlEvents.iterator();
				while(it.hasNext())
				{	ControlEvent temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// misc
			currentControls.clear();
			controlSettings = null;
			sprite = null;
		}
	}
}
