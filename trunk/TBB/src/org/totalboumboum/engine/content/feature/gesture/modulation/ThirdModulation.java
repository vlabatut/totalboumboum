package org.totalboumboum.engine.content.feature.gesture.modulation;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import java.util.List;

import org.totalboumboum.engine.content.feature.Contact;
import org.totalboumboum.engine.content.feature.Orientation;
import org.totalboumboum.engine.content.feature.TilePosition;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.action.Circumstance;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.action.SpecificAction;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ThirdModulation extends AbstractActionModulation
{	private static final long serialVersionUID = 1L;

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
	public boolean isConcerningAction(SpecificAction specificAction, Circumstance actorCircumstances, Circumstance targetCircumstances) 
//TODO vérififier dans le mod mgr que c bien cette méthode (et pas la parente) qui est appelée
	{	boolean result = super.isConcerningAction(specificAction);
		if(result)
			result = this.actorCircumstance.subsume(actorCircumstances);
		if(result)
			result = this.targetCircumstance.subsume(targetCircumstances);
/*		// tile positions
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
*/		//	
		return result;		
	}
	public boolean isConcerningAction(GeneralAction generalAction, List<AbstractAbility> actorProperties, List<AbstractAbility> targetProperties, Circumstance actorCircumstances, Circumstance targetCircumstances) 
	{	boolean result = super.isConcerningAction(generalAction,actorProperties,targetProperties);
		if(result)
			result = this.actorCircumstance.subsume(actorCircumstances);
		if(result)
			result = this.targetCircumstance.subsume(targetCircumstances);
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
/*	public ThirdModulation copy()
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
*/
/*	public ThirdModulation cacheCopy(double zoomFactor)
	{	GeneralAction actionCopy = action.cacheCopy(zoomFactor);
		ThirdModulation result = new ThirdModulation(actionCopy); //NOTE why copy the action?
		
		// actor modulations
		for(AbstractAbility ability: actorRestrictions)
			result.actorRestrictions.add(ability.cacheCopy(zoomFactor));

		// target modulations
		for(AbstractAbility ability: targetRestrictions)
			result.targetRestrictions.add(ability.cacheCopy(zoomFactor));
		
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
		result.frame = frame;
		result.gestureName = gestureName;
		result.strength = strength;
		
		return result;
	}*/
}
