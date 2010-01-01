package org.totalboumboum.ai.adapter200809;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;

/**
 * repr�sente une bombe du jeu, ie un objet que les joueurs peuvent d�poser
 * pour d�truire les murs et �liminer les autre joueurs.
 * 
 * @author Vincent
 *
 */

public class AiBomb extends AiSprite<Bomb>
{
	/**
	 * cr�e une repr�sentation de la bombe pass�e en param�tre, et contenue dans 
	 * la case pass�e en param�tre.
	 * 
	 * @param tile	case contenant le sprite
	 * @param sprite	sprite � repr�senter
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
	 * @return	une valeur de type AiBombType repr�sentant le type de bombe
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
	/** port�e de la bombe, ie. : nombre de cases occup�es par sa flamme */
	private int range;
	
	/**
	 * renvoie la port�e de la bombe
	 * (ie. le nombre de cases occup�es par sa flamme)
	 * @return	port�e de la bombe
	 */
	public int getRange()
	{	return range;	
	}
	
	/**
	 * initialise la port�e de la bombe
	 */
	private void initRange()
	{	Bomb bomb = getSprite();
		range = bomb.getFlameRange();
	}
	
	/////////////////////////////////////////////////////////////////
	// WORKING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** inidique si cette bombe fonctionne normalement (ie si elle n'est pas tomb�e en panne) */
	private boolean working;
	
	/**
	 * indique si cette bombe fonctionne normalement (ie si elle n'est pas tomb�e en panne)
	 * 
	 * @return	vrai si cette bombe marche, faux si elle est en panne
	 */
	public boolean isWorking()
	{	return working;	
	}
	
	/**
	 * met � jour l'indicateur de panne de cette bombe
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
	/** couleur de la bombe (correspondant � celle du personnage qui l'a pos�e) */
	private PredefinedColor color;
	
	/**
	 * renvoie la couleur de cette bombe.
	 * Cette couleur est null si aucun joueur n'a pos� la bombe 
	 * (pour certains niveaux sp�ciaux o� les blocs peuvent g�n�rer des bombes)  
	 * 
	 * @return un symbole de type PredefinedColor repr�sentant une couleur
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
	/** d�lai normal (ie hors-panne) avant l'explosion de la bombe */
	private long normalDuration;

	/**
	 * renvoie le d�lai normal avant l'explosion de la bombe.
	 * Ce d�lai ne tient pas compte des pannes �ventuelles.
	 * Ce d�lai n'est pas d�fini pour tous les types de bombes
	 * 
	 * @return	le d�lai normal avant explosion exprim� en millisecondes
	 */
	public long getNormalDuration()
	{	return normalDuration;
	}

	/**
	 * initialisation des param�tres li�s � l'explosion de la bombe
	 */
	private void initFuse()
	{	// theoretic delay before explosion 
		{	StateAbility ability = getSprite().modulateStateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
			normalDuration = (long)ability.getStrength();		
		}
	}
}
