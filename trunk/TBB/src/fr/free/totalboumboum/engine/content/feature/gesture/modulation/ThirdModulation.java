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

import java.util.ArrayList;

import fr.free.totalboumboum.engine.content.feature.Contact;
import fr.free.totalboumboum.engine.content.feature.TilePosition;
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
	// CONTACTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** contacts between the actor and the modulating sprite */
	protected final ArrayList<Contact> actorContacts = new ArrayList<Contact>();
	/** contacts between the target and the modulating sprite */
	protected final ArrayList<Contact> targetContacts = new ArrayList<Contact>();
/*
	protected ArrayList<Contact> getContacts()
	{	return contacts;
	}
*/
	public void addActorContact(Contact contact)
	{	actorContacts.add(contact);		
	}

	public void addTargetContact(Contact contact)
	{	targetContacts.add(contact);		
	}

	/////////////////////////////////////////////////////////////////
	// TILE POSITIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** positions of the actor in terms of tiles */
	protected final ArrayList<TilePosition> actorTilePositions =  new ArrayList<TilePosition>();
	/** positions of the target in terms of tiles */
	protected final ArrayList<TilePosition> targetTilePositions =  new ArrayList<TilePosition>();
	
/*	protected ArrayList<TilePosition> getTilePositions()
	{	return tilePositions;
	}
*/	
	public void addActorTilePosition(TilePosition tilePosition)
	{	actorTilePositions.add(tilePosition);
	}

	public void addTargetTilePosition(TilePosition tilePosition)
	{	targetTilePositions.add(tilePosition);
	}

	/////////////////////////////////////////////////////////////////
	// MODULATE					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * tests if this modulation is related to the specified action
	 */
	public boolean isConcerningAction(SpecificAction specificAction, Sprite modulator) 
//TODO vérififier dans le mod mgr que c bien cette méthode (et pas la parente) qui est appelée
//TODO dans le XML, mettre undefined par défaut partout
	{	boolean result = super.isConcerningAction(specificAction);
		Sprite actor = specificAction.getActor();
		Sprite target = specificAction.getTarget();
		// tile positions
		if(result)
		{	TilePosition actorTilePosition = TilePosition.getTilePosition(modulator,actor);
			result = actorTilePositions.contains(actorTilePosition);
			if(result)
			{	TilePosition targetTilePosition = TilePosition.getTilePosition(modulator,target);
				result = targetTilePositions.contains(targetTilePosition);
			}
		}
		// contacts
		if(result)
		{	Contact actorContact = Contact.getContact(modulator,actor);
			result = actorContacts.contains(actorContact);
			if(result)
			{	Contact targetContact = Contact.getContact(modulator,target);
				result = targetContacts.contains(targetContact);
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
		result.actorContacts.addAll(actorContacts);
		result.targetContacts.addAll(targetContacts);
		// tile positions
		result.actorTilePositions.addAll(actorTilePositions);
		result.targetTilePositions.addAll(targetTilePositions);
		// misc
		result.finished = finished;
		result.frame = frame;
		result.gestureName = gestureName;
		result.strength = strength;
		return result;
	}
}
