package org.totalboumboum.engine.content.feature.action.detonate;

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

import org.totalboumboum.engine.content.feature.Contact;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Orientation;
import org.totalboumboum.engine.content.feature.TilePosition;
import org.totalboumboum.engine.content.feature.action.ActionName;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.sprite.Sprite;

/** 
 * producing an explosion, with flames and everything. 
 * usually performed by a bomb (triggered bomb, etc)
 * 
 * 	<br>actor: 			any (probably a bomb)
 * 	<br>target: 		none
 * 	<br>direction:		any or none
 * 	<br>contact:		none
 * 	<br>tilePosition:	undefined
 * 	<br>orientation:	undefined
 * 
 * @author Vincent Labatut
 *
 */
public class SpecificDetonate extends SpecificAction
{
	public SpecificDetonate(Sprite actor)
	{	super(ActionName.DETONATE,actor);
	}
	
	public SpecificDetonate(Sprite actor, Direction direction)
	{	super(ActionName.DETONATE,actor,null,direction,Contact.NONE,TilePosition.NONE,Orientation.NONE);
		
	}
/*	
	/////////////////////////////////////////////////////////////////
	// GENERAL ACTION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GeneralDetonate generalAction;

	@Override
	public GeneralAction getGeneralAction()
	{	return generalAction;
	}
	
	@Override
	protected void initGeneralAction() 
	{	generalAction = new GeneralDetonate();
		super.initGeneralAction(generalAction);
	}
*/	
}
