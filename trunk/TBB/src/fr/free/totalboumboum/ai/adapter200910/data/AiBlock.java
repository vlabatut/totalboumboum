package fr.free.totalboumboum.ai.adapter200910.data;

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
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.Orientation;
import fr.free.totalboumboum.engine.content.feature.Role;
import fr.free.totalboumboum.engine.content.feature.TilePosition;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.action.Circumstance;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.action.consume.SpecificConsume;
import fr.free.totalboumboum.engine.content.feature.action.movelow.GeneralMoveLow;
import fr.free.totalboumboum.engine.content.sprite.block.Block;

/**
 * Représente un bloc du jeu, c'est à dire généralement un mur
 * (pouvant être détruit ou pas). 
 * 
 * @author Vincent
 *
 */

public class AiBlock extends AiSprite<Block>
{
	/**
	 * crée une représentation du bloc passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite à représenter
	 */
	AiBlock(AiTile tile, Block sprite)
	{	super(tile,sprite);
		updateAbilities();
	}	
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	void update(AiTile tile)
	{	super.update(tile);
		updateAbilities();
	}

	/////////////////////////////////////////////////////////////////
	// ABILITIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si ce mur peut être détruit par une bombe */
	private boolean destructible;
	/** indique si ce mur laisse passer les joueurs ayant un item pour traverser les murs */
	private boolean blockHero;
	
	/** 
	 * met jour les différentes caractéristiques de ce bloc 
	 */
	private void updateAbilities()
	{	Block sprite = getSprite();
		
		// destructible
		SpecificAction specificAction = new SpecificConsume(sprite);
		destructible = sprite.isTargetPreventing(specificAction);

		// bloque les personnages
		GeneralAction generalAction = new GeneralMoveLow();
		generalAction.addActor(Role.HERO);
		generalAction.addDirection(Direction.RIGHT);
		Circumstance actorCircumstance = new Circumstance();
		actorCircumstance.addContact(Contact.INTERSECTION);
		actorCircumstance.addOrientation(Orientation.FACE);
		actorCircumstance.addTilePosition(TilePosition.SAME);
		Circumstance targetCircumstance = new Circumstance();
		ArrayList<AbstractAbility> actorProperties = new ArrayList<AbstractAbility>();
		ArrayList<AbstractAbility> targetProperties = new ArrayList<AbstractAbility>();
		blockHero = sprite.isThirdPreventing(generalAction,actorProperties,targetProperties,actorCircumstance,targetCircumstance);
	}	

	/**
	 * renvoie vrai si ce bloc peut être détruit par une bombe, et faux sinon
	 * 
	 * @return	l'indicateur de destructibilité du mur
	 */
	public boolean isDestructible()
	{	return destructible;		
	}

	/**
	 * renvoie vrai si ce bloc bloque le passage de tout personnage,
	 * y compris ceux qui ont le pouvoir de traverser les murs
	 * 
	 * @return	vrai ssi le bloc empêche tout personnage de passer
	 */
	public boolean hasBlockHero()
	{	return blockHero;		
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("Block: [");
		result.append(super.toString());
		result.append(" - destr.: "+destructible);
		result.append(" ]");
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	void finish()
	{	super.finish();
	}

}
