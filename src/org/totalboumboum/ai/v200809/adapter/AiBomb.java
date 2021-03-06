package org.totalboumboum.ai.v200809.adapter;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * représente une bombe du jeu, ie un objet que les joueurs peuvent déposer
 * pour détruire les murs et éliminer les autre joueurs.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public class AiBomb extends AiSprite<Bomb>
{	/** Id de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * crée une représentation de la bombe passée en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite à représenter
	 */
	AiBomb(AiTile tile, Bomb sprite)
	{	super(tile,sprite);
		initType();
		initRange();
		initFuse();
		updateWorking();
		initColor();
	}

	@Override
	void update(AiTile tile)
	{	super.update(tile);
		updateWorking();
	}

	@Override
	void finish()
	{	super.finish();
	}

	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("Bomb: [");
		result.append(super.toString());
		result.append(" - type: "+type);
		result.append(" - clr: "+color);
		result.append(" - wrk: "+working);
		result.append(" - dur.: "+normalDuration);
		result.append(" - rge: "+range);
		result.append(" ]");
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// TYPE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** type de la bombe */
	private AiBombType type;
	
	/**
	 * renvoie le type de la bombe
	 * @return	une valeur de type AiBombType représentant le type de bombe
	 */
	public AiBombType getType()
	{	return type;	
	}
	
	/**
	 * initialise le type de la bombe
	 */
	private void initType()
	{	Bomb bomb = getSprite();
		type = AiBombType.makeBombType(bomb.getBombName());		
	}
	
	/////////////////////////////////////////////////////////////////
	// RANGE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** portée de la bombe, ie. : nombre de cases occupées par sa flamme */
	private int range;
	
	/**
	 * renvoie la portée de la bombe
	 * (ie. le nombre de cases occupées par sa flamme)
	 * @return	portée de la bombe
	 */
	public int getRange()
	{	return range;	
	}
	
	/**
	 * initialise la portée de la bombe
	 */
	private void initRange()
	{	Bomb bomb = getSprite();
		range = bomb.getFlameRange();
	}
	
	/////////////////////////////////////////////////////////////////
	// WORKING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** inidique si cette bombe fonctionne normalement (ie si elle n'est pas tombée en panne) */
	private boolean working;
	
	/**
	 * indique si cette bombe fonctionne normalement (ie si elle n'est pas tombée en panne)
	 * 
	 * @return	vrai si cette bombe marche, faux si elle est en panne
	 */
	public boolean isWorking()
	{	return working;	
	}
	
	/**
	 * met à jour l'indicateur de panne de cette bombe
	 */
	private void updateWorking()
	{	Bomb sprite = getSprite();
		GestureName gesture = sprite.getCurrentGesture().getName();
		if(gesture==GestureName.OSCILLATING_FAILING
			|| gesture==GestureName.STANDING_FAILING)
			working = false;
		else
			working = true;
		
	}

	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** couleur de la bombe (correspondant à celle du personnage qui l'a posée) */
	private PredefinedColor color;
	
	/**
	 * renvoie la couleur de cette bombe.
	 * Cette couleur est null si aucun joueur n'a posé la bombe 
	 * (pour certains niveaux spéciaux où les blocs peuvent générer des bombes)  
	 * 
	 * @return un symbole de type PredefinedColor représentant une couleur
	 */
	public PredefinedColor getColor()
	{	return color;	
	}
	
	/**
	 * initialise la couleur de la bombe
	 */
	private void initColor()
	{	Bomb sprite = getSprite();
		color = sprite.getColor();	
	}

	/////////////////////////////////////////////////////////////////
	// FUSE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** délai normal (ie hors-panne) avant l'explosion de la bombe */
	private long normalDuration;

	/**
	 * renvoie le délai normal avant l'explosion de la bombe.
	 * Ce délai ne tient pas compte des pannes éventuelles.
	 * Ce délai n'est pas défini pour tous les types de bombes
	 * 
	 * @return	le délai normal avant explosion exprimé en millisecondes
	 */
	public long getNormalDuration()
	{	return normalDuration;
	}

	/**
	 * initialisation des paramètres liés à l'explosion de la bombe
	 */
	private void initFuse()
	{	// theoretic delay before explosion 
		{	StateAbility ability = getSprite().modulateStateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
			normalDuration = (long)ability.getStrength();		
		}
	}
}
