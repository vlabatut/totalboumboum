package fr.free.totalboumboum.engine.content.feature.gesture.modulation;

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

import fr.free.totalboumboum.engine.content.feature.Contact;
import fr.free.totalboumboum.engine.content.feature.Orientation;
import fr.free.totalboumboum.engine.content.feature.TilePosition;
import fr.free.totalboumboum.engine.content.feature.gesture.Circumstance;
import fr.free.totalboumboum.engine.content.feature.gesture.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.gesture.action.SpecificAction;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class ThirdModulation extends AbstractActionModulation
{
	public ThirdModulation(GeneralAction action)
	{	super(action);
	}
	
	public ThirdModulation(SpecificAction action)
	{	super(action);
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTOR CIRCUMSTANCE	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** circumstances between the actor and the modulating sprite */
	private final Circumstance actorCircumstance = new Circumstance();

	public Circumstance getActorCircumstance()
	{	return actorCircumstance;	
	}
	
	public void addActorContact(Contact contact)
	{	actorCircumstance.addContact(contact);		
	}

	public void addActorTilePosition(TilePosition tilePosition)
	{	actorCircumstance.addTilePosition(tilePosition);
	}

	public void addActorOrientation(Orientation orientation)
	{	actorCircumstance.addOrientation(orientation);
	}

	/////////////////////////////////////////////////////////////////
	// TARGET CIRCUMSTANCE	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** circumstances between the actor and the modulating sprite */
	private final Circumstance targetCircumstance = new Circumstance();

	public Circumstance getTargetCircumstance()
	{	return targetCircumstance;	
	}
	
	public void addTargetContact(Contact contact)
	{	targetCircumstance.addContact(contact);		
	}

	public void addTargetTilePosition(TilePosition tilePosition)
	{	targetCircumstance.addTilePosition(tilePosition);
	}

	public void addTargetOrientation(Orientation orientation)
	{	targetCircumstance.addOrientation(orientation);
	}

	/////////////////////////////////////////////////////////////////
	// MODULATE					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * tests if this modulation is related to the specified action
	 */
	public boolean isConcerningAction(SpecificAction specificAction, Sprite modulator) 
//TODO v�rififier dans le mod mgr que c bien cette m�thode (et pas la parente) qui est appel�e
	{	boolean result = super.isConcerningAction(specificAction);
		Sprite actor = specificAction.getActor();
		Sprite target = specificAction.getTarget();
		// tile positions
		if(result)
		{	TilePosition actorTilePosition = TilePosition.getTilePosition(modulator,actor);
			result = actorCircumstance.containsTilePosition(actorTilePosition);
			if(result)
			{	TilePosition targetTilePosition = TilePosition.getTilePosition(modulator,target);
				result = targetCircumstance.containsTilePosition(targetTilePosition);
			}
		}
		// contacts
		if(result)
		{	Contact actorContact = Contact.getContact(modulator,actor);
			result = actorCircumstance.containsContact(actorContact);
			if(result)
			{	Contact targetContact = Contact.getContact(modulator,target);
				result = targetCircumstance.containsContact(targetContact);
			}
		}
		// orientations
		if(result)
		{	Orientation actorOrientation = Orientation.getOrientation(modulator,actor);
			result = actorCircumstance.containsOrientation(actorOrientation);
			if(result)
			{	Orientation targetOrientation = Orientation.getOrientation(modulator,target);
				result = targetCircumstance.containsOrientation(targetOrientation);
			}
		}
		//	
		return result;		
	}

	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof ThirdModulation)
		{	ThirdModulation perm = (ThirdModulation) o;
			result = action.equals(perm.getAction());
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public ThirdModulation copy()
	{	GeneralAction actionCopy = action;
		ThirdModulation result = new ThirdModulation(actionCopy); //NOTE why copy the action?
		// restrictions
		result.actorRestrictions.addAll(actorRestrictions);
		result.targetRestrictions.addAll(targetRestrictions);
		// contacts
		for(Contact c: actorCircumstance.getContacts())
			result.addActorContact(c);
		for(Contact c: targetCircumstance.getContacts())
			result.addTargetContact(c);
		// tile positions
		for(TilePosition tp: actorCircumstance.getTilePositions())
			result.addActorTilePosition(tp);
		for(TilePosition tp: targetCircumstance.getTilePositions())
			result.addTargetTilePosition(tp);
		// orientations
		for(Orientation o: actorCircumstance.getOrientations())
			result.addActorOrientation(o);
		for(Orientation o: targetCircumstance.getOrientations())
			result.addTargetOrientation(o);
		// misc
		result.finished = finished;
		result.frame = frame;
		result.gestureName = gestureName;
		result.strength = strength;
		return result;
	}
}
