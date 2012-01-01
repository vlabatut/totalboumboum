package org.totalboumboum.engine.content.feature.action.consume;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import org.totalboumboum.engine.content.feature.Contact;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Orientation;
import org.totalboumboum.engine.content.feature.TilePosition;
import org.totalboumboum.engine.content.feature.action.ActionName;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.fire.Fire;

/** 
 * putting fire to another object, usually performed by fire (but not necessarily).
 * different from detonating, which is performed by bombs and results in the production of fire.
 * 
 * 	<br>actor: 			any (probably fire, but not necessarily)
 * 	<br>target: 		any
 * 	<br>direction:		any or none
 * 	<br>contact:		any or none
 * 	<br>tilePosition:	any or undefined
 * 	<br>orientation:	any or undefined
 * 
 * @author Vincent Labatut
 *
 */
public class SpecificConsume extends SpecificAction
{
	public SpecificConsume(Sprite actor, Sprite target)
	{	super(ActionName.CONSUME,actor,target);
	}

	public SpecificConsume(Sprite actor, Sprite target, Direction direction, Contact contact, TilePosition tilePosition, Orientation orientation)
	{	super(ActionName.CONSUME,actor,target,direction,contact,tilePosition,orientation);		
	}
	
	/**
	 * anonym action, just for test purposes
	 */
	public SpecificConsume(Sprite target)
	{	super(ActionName.CONSUME,new Fire(),target,Direction.NONE,Contact.COLLISION,TilePosition.SAME,Orientation.FACE);
	}

/*	
	/////////////////////////////////////////////////////////////////
	// GENERAL ACTION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GeneralConsume generalAction;

	@Override
	public GeneralAction getGeneralAction()
	{	return generalAction;
	}
	
	@Override
	protected void initGeneralAction() 
	{	generalAction = new GeneralConsume();
		super.initGeneralAction(generalAction);
	}
*/	
}
