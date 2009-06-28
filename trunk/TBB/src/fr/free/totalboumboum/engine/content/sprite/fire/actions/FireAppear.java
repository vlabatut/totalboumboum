package fr.free.totalboumboum.engine.content.sprite.fire.actions;

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

import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.feature.gesture.action.ActionName;
import fr.free.totalboumboum.engine.content.feature.gesture.action.SpecificAction;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;

/** 
 * appearing in a tile, coming from nowhere
 * (after a teleport, a dropped bomb, player at the begining of a round, etc.)
 * 
 * 	<br>actor: 			any
 * 	<br>target: 		any (probably a floor, but not necessarily)
 * 	<br>direction:		any or none
 * 	<br>contact:		any or none
 * 	<br>tilePosition:	any or undefined
 * 	<br>orientation:	any or undefined
 *  
 */
public class FireAppear extends SpecificAction
{
	public FireAppear(Fire actor, Tile tile,Direction direction)
	{	super(ActionName.APPEAR,actor,tile);
		setDirection(direction);
	}

	/////////////////////////////////////////////////////////////////
	// EXECUTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean execute()
	{	boolean result;
		Fire fire = (Fire)getActor();
		AbstractAbility ability = fire.modulateAction(this);
		result = ability.isActive();
		if(result)
		{	fire.initGesture();
			getTile().addSprite(fire);
			fire.setCurrentPosX(getTile().getPosX());
			fire.setCurrentPosY(getTile().getPosY());
			fire.setGesture(GestureName.BURNING,getDirection(),getDirection(), true);
		}
		else
			fire.consumeTile(getTile());
		return result;
	}

/*	
	/////////////////////////////////////////////////////////////////
	// GENERAL ACTION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GeneralAppear generalAction;

	@Override
	public GeneralAction getGeneralAction()
	{	return generalAction;
	}
	
	@Override
	protected void initGeneralAction() 
	{	generalAction = new GeneralAppear();
		super.initGeneralAction(generalAction);
	}
*/	
}
