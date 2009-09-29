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
import java.util.Collection;
import java.util.List;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbilityName;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;

/**
 * représente une bombe du jeu, ie un objet que les joueurs peuvent déposer
 * pour détruire les murs et éliminer les autre joueurs.
 * 
 * @author Vincent
 *
 */

public class AiBomb extends AiSprite<Bomb>
{
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

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	void update(AiTile tile)
	{	super.update(tile);
		updateWorking();
		updateTime();
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
	
	/**
	 * calcule une liste de cases correspondant au souffle de cette bombe,
	 * i.e. toutes les cases qui seront atteinte quand elle va exploser.
	 * Cette méthode tient compte de murs, items, etc., c'est à dire qu'elle
	 * ne donne que les cases qui seront touchées si la bombe devait exploser
	 * à l'instant où cette méthode est invoquée. Si un des obstacles à l'explosion
	 * disparait (par exemple si un joueur rammasse un item qui bloquait l'explosion),
	 * alors le souffle peut changer.
	 * 
	 * @return	une liste de cases correspondant aux cases qui seront touchées par la flamme de cette bombe 
	 */
	public List<AiTile> getBlast()
	{	// init
		ArrayList<AiTile> result = new ArrayList<AiTile>();
		AiTile center = getTile();
		result.add(center);
		
		// calcul du souffle
		for(Direction direction: Direction.getPrimaryValues())
		{	AiTile tile = center;
			boolean blocked = false;
			int i=0;
			while(i<range && !blocked)
			{	boolean blasted = true;
				tile = tile.getNeighbor(direction);
				// blocs
				if(!blocked)
				{	Collection<AiBlock> blocks = tile.getBlocks();
					if(!blocks.isEmpty())
					{	AiBlock block = blocks.iterator().next();
						// si le bloc est destructible, la flamme ne sera pas bloquée uniquement en cas de pénétration
						if(block.isDestructible())
						{	if(type!=AiBombType.PENETRATION && type!=AiBombType.REMOTE_PENETRATION)
								blocked = true;
						}
						else
						{	blocked = true;
							blasted = false;						
						}
					}
				}
				// items
				if(!blocked)
				{	Collection<AiItem> items = tile.getItems();
					if(!items.isEmpty())
					{	blocked = true;
						blasted = false;
					}
				}
				// bombes
				if(!blocked)
				{	Collection<AiBomb> bombs = tile.getBombs();
					if(!bombs.isEmpty())
						blocked = true;
				}
				// on rajoute éventuellement la case dans blast
				if(blasted)
					result.add(tile);
				// on passe à la case suivante
				i++;
			}
		}
		
		return result;
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

	/////////////////////////////////////////////////////////////////
	// LIFE TIME 		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** temps écoulé depuis que la bombe a été posée, exprimé en ms */
	private double time = 0;
	
	/**
	 * renvoie le temps écoulé depuis que la bombe a été posée,
	 * exprimé en millisecondes
	 * 
	 * @return	temps exprimé en ms
	 */
	public double getTime()
	{	return time;	
	}
	
	/**
	 * met à jour le temps écoulé depuis que la bombe a été posée
	 */
	private void updateTime()
	{	Bomb sprite = getSprite();
		GestureName gesture = sprite.getCurrentGesture().getName();
		
		if(gesture==GestureName.STANDING || gesture==GestureName.STANDING_FAILING
			|| gesture==GestureName.OSCILLATING || gesture==GestureName.OSCILLATING_FAILING
			|| gesture==GestureName.SLIDING || gesture==GestureName.SLIDING)
		{	double elapsedTime = getTile().getZone().getElapsedTime();
			time = time + elapsedTime;
		}
		else if(gesture==GestureName.APPEARING
				|| gesture==GestureName.BOUNCING
				|| gesture==GestureName.DISAPPEARING
				|| gesture==GestureName.ENTERING
				|| gesture==GestureName.HIDING
				|| gesture==GestureName.LANDING
				|| gesture==GestureName.NONE
				|| gesture==GestureName.PREPARED)
		{	time = 0;		
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	void finish()
	{	super.finish();
	}
}
