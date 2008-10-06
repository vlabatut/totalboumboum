package fr.free.totalboumboum.ai.adapter200809;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.Orientation;
import fr.free.totalboumboum.engine.content.feature.TilePosition;
import fr.free.totalboumboum.engine.content.feature.action.AbstractAction;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.permission.TargetPermission;
import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;

/**
 * Représente un bloc du jeu, c'est à dire généralement : un mur. 
 * 
 * 
 * @author Vincent
 *
 */

public class AiBlock extends AiSprite<Block>
{
	AiBlock(AiTile tile, Block sprite)
	{	super(tile,sprite);
		updateDestructible();
	}	
	
	@Override
	void update(AiTile tile)
	{	super.update(tile);
		updateDestructible();
	}

	@Override
	void finish()
	{	super.finish();
	}

	/////////////////////////////////////////////////////////////////
	// DESTRUCTIBLE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si ce mur peut être détruit par une bombe */
	private boolean destructible;
	
	/** 
	 * initialise l'indicateur de destructibilité de ce bloc 
	 */
	private void updateDestructible()
	{	Block sprite = getSprite();
		SpecificAction action = new SpecificAction(AbstractAction.CONSUME,new Fire(sprite.getLevel()),sprite,Direction.NONE,Contact.COLLISION,TilePosition.SAME,Orientation.SAME);
		TargetPermission perm = sprite.getTargetPermission(action);
		destructible = perm!=null;
	}	
	/** 
	 * renvoie vrai si ce bloc peut être détruit par une bombe, et faux sinon 
	 */
	public boolean isDestructible()
	{	return destructible;		
	}
}
