package fr.free.totalboumboum.engine.content.feature.action.drop;

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

import fr.free.totalboumboum.engine.content.feature.GestureConstants;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.action.AbstractAction;
import fr.free.totalboumboum.engine.content.feature.action.ActionName;
import fr.free.totalboumboum.engine.content.feature.action.Contact;
import fr.free.totalboumboum.engine.content.feature.action.IncompatibleParameterException;
import fr.free.totalboumboum.engine.content.feature.action.Orientation;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.action.TilePosition;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;

/** 
 * putting an object on the ground.
 * usually a player dropping a bomb
 * 
 * 	<br>actor: 			any (probably a hero)
 * 	<br>target: 		any (probably a bomb)
 * 	<br>direction:		any or none
 * 	<br>contact:		none (the target is not supposed to be ongame)
 * 	<br>tilePosition:	undefined
 * 	<br>orientation:	undefined
 *  
 */
public class SpecificDrop extends SpecificAction<GeneralDrop>
{
	public SpecificDrop(Sprite actor) throws IncompatibleParameterException
	{	super(ActionName.DROP,actor,target);
	}

	@Override
	public boolean execute()
	{
		
		
		if((gesture.equals(GestureConstants.PUSHING) || gesture.equals(GestureConstants.STANDING)
				|| gesture.equals(GestureConstants.WAITING) || gesture.equals(GestureConstants.WALKING))
				&& event.getMode())
			{	Bomb bomb = sprite.makeBomb();
				SpecificAction action = new SpecificAction(AbstractAction.DROP,sprite,bomb,spriteDirection,Contact.INTERSECTION,TilePosition.SAME,Orientation.SAME);
				ActionAbility ability = sprite.computeAbility(action);
				if(ability.isActive())
				{	sprite.dropBomb(bomb);
					if(gesture.equals(GestureConstants.WAITING))
					{	setWaitDelay();
						gesture = GestureConstants.STANDING;
						sprite.setGesture(gesture,spriteDirection,controlDirection,true);					
					}
					else if(gesture.equals(GestureConstants.STANDING))
						setWaitDelay();
				}
			}
		
		
		// TODO Auto-generated method stub
		return false;
	}
}
