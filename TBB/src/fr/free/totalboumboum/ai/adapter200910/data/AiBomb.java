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
import fr.free.totalboumboum.engine.content.feature.Contact;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.Orientation;
import fr.free.totalboumboum.engine.content.feature.Role;
import fr.free.totalboumboum.engine.content.feature.TilePosition;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbilityName;
import fr.free.totalboumboum.engine.content.feature.action.Circumstance;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.action.appear.GeneralAppear;
import fr.free.totalboumboum.engine.content.feature.action.movelow.GeneralMoveLow;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;

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
		initFuse();
		initRange();
		initColor();
		updateWorking();
		updateCollisions();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	void update(AiTile tile)
	{	super.update(tile);
		updateWorking();
		updateTime();
		updateCollisions();
	}

	/////////////////////////////////////////////////////////////////
	// FUSE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** d�clenchement par compte � rebours */
	private boolean countdownTrigger;
	/** d�clenchement par t�l�commande */
	private boolean remoteControlTrigger;
	/** d�clenchement par explosion */
	private boolean explosionTrigger;
	/** d�lai normal (ie hors-panne) avant l'explosion de la bombe */
	private long normalDuration;
	
	/**
	 * indique si l'explosion de la bombe d�pend d'un compte � rebours
	 * @return	vrai si la bombe d�pend d'un compte � rebours
	 */
	public boolean hasCountdownTrigger()
	{	return countdownTrigger;
	}
	
	/**
	 * indique si l'explosion de la bombe d�pend d'une t�l�commande
	 * @return	vrai si la bombe d�pend d'une t�l�commande
	 */
	public boolean hasRemoteControlTrigger()
	{	return remoteControlTrigger;
	}
	
	/**
	 * indique si l'explosion de la bombe d�pend d'un contact avec du feu
	 * @return	vrai si la bombe explose au contact du feu
	 */
	public boolean hasExplosionTrigger()
	{	return explosionTrigger;
	}
	
	/**
	 * initialisation des param�tres li�s � l'explosion de la bombe
	 */
	private void initFuse()
	{	Bomb bomb = getSprite();
	
		// theoretic delay before explosion 
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
			normalDuration = (long)ability.getStrength();		
		}	
		// compte � rebours
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_TIMER);
			countdownTrigger = ability.isActive();
		}
		// t�l�commande
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_CONTROL);
			remoteControlTrigger = ability.isActive();
		}
		// feu
		{	StateAbility ability = bomb.modulateStateAbility(StateAbilityName.BOMB_TRIGGER_COMBUSTION);
			explosionTrigger = ability.isActive();
		}
	}


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
	
	/**
	 * calcule une liste de cases correspondant au souffle de cette bombe,
	 * i.e. toutes les cases qui seront atteinte quand elle va exploser.
	 * Cette m�thode tient compte de murs, items, etc., c'est � dire qu'elle
	 * ne donne que les cases qui seront touch�es si la bombe devait exploser
	 * � l'instant o� cette m�thode est invoqu�e. Si un des obstacles � l'explosion
	 * disparait (par exemple si un joueur rammasse un item qui bloquait l'explosion),
	 * alors le souffle peut changer.
	 * 
	 * @return	une liste de cases correspondant aux cases qui seront touch�es par la flamme de cette bombe 
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
						blocked = block.is
//TODO inutile de faire des fonctions g�n�ralistes : on traine en interne au cas par cas						
						// si le bloc est destructible, la flamme ne sera pas bloqu�e uniquement en cas de p�n�tration
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
				// on rajoute �ventuellement la case dans blast
				if(blasted)
					result.add(tile);
				// on passe � la case suivante
				i++;
			}
		}
		
		return result;
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
	// LIFE TIME 		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** temps �coul� depuis que la bombe a �t� pos�e, exprim� en ms */
	private double time = 0;
	
	/**
	 * renvoie le temps �coul� depuis que la bombe a �t� pos�e,
	 * exprim� en millisecondes
	 * 
	 * @return	temps exprim� en ms
	 */
	public double getTime()
	{	return time;	
	}
	
	/**
	 * met � jour le temps �coul� depuis que la bombe a �t� pos�e
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
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si cette bombe laisse passer les joueurs */
	private StopType stopHeroes;
	/** indique si cette bombe laisse passer le feu */
	private StopType stopFires;
	/** indique si cette bombe peut traverser les items */
	private boolean throughItems;

	/**
	 * teste si cette bombe est capable de passer
	 * � travers les items
	 * 
	 * @return	vrai si la bombe traverse les items
	 */
	public boolean hasThroughItems()
	{	return throughItems;	
	}

	/** 
	 * met jour les diff�rentes caract�ristiques de cette bombe
	 * concernant la gestion des collisions avec les autres sprites
	 */
	private void updateCollisions()
	{	Bomb sprite = getSprite();
		
		// bloque les personnages
		{	GeneralAction generalAction = new GeneralMoveLow();
			generalAction.addActor(Role.HERO);
			generalAction.addDirection(Direction.RIGHT);
			Circumstance actorCircumstance = new Circumstance();
			actorCircumstance.addContact(Contact.COLLISION);
			actorCircumstance.addOrientation(Orientation.FACE);
			actorCircumstance.addTilePosition(TilePosition.NEIGHBOR);
			Circumstance targetCircumstance = new Circumstance();
			ArrayList<AbstractAbility> actorProperties = new ArrayList<AbstractAbility>();
			ArrayList<AbstractAbility> targetProperties = new ArrayList<AbstractAbility>();
			boolean temp = sprite.isThirdPreventing(generalAction,actorProperties,targetProperties,actorCircumstance,targetCircumstance);
			if(temp)
			{	StateAbility ability = new StateAbility(StateAbilityName.SPRITE_TRAVERSE_WALL);
				actorProperties.add(ability);
				temp = sprite.isThirdPreventing(generalAction,actorProperties,targetProperties,actorCircumstance,targetCircumstance);
				if(temp)
					stopHeroes = StopType.WEAK_STOP;
				else
					stopHeroes = StopType.STRONG_STOP;
			}
			else
				stopHeroes = StopType.NO_STOP;
		}

		// bloque le feu
		{	GeneralAction generalAction = new GeneralAppear();
			generalAction.addActor(Role.FIRE);
			generalAction.addDirection(Direction.NONE);
			Circumstance actorCircumstance = new Circumstance();
			actorCircumstance.addContact(Contact.INTERSECTION);
			actorCircumstance.addOrientation(Orientation.NEUTRAL);
			actorCircumstance.addTilePosition(TilePosition.SAME);
			Circumstance targetCircumstance = new Circumstance();
			ArrayList<AbstractAbility> actorProperties = new ArrayList<AbstractAbility>();
			ArrayList<AbstractAbility> targetProperties = new ArrayList<AbstractAbility>();
			boolean temp = sprite.isThirdPreventing(generalAction,actorProperties,targetProperties,actorCircumstance,targetCircumstance);
			if(temp)
			{	StateAbility ability = new StateAbility(StateAbilityName.SPRITE_TRAVERSE_WALL);
				actorProperties.add(ability);
				temp = sprite.isThirdPreventing(generalAction,actorProperties,targetProperties,actorCircumstance,targetCircumstance);
				if(temp)
					stopFires = StopType.WEAK_STOP;
				else
					stopFires = StopType.STRONG_STOP;
			}
			else
				stopFires = StopType.NO_STOP;
		}
	}	

	public boolean isCrossableBy(AiSprite<?> sprite)
	{	// par d�faut, on bloque
		boolean result = false;
		// si le sprite consid�r� est un personnage
		if(sprite instanceof AiHero)
		{	AiHero hero = (AiHero) sprite;
			if(hero.getTile()==getTile()) //simplification
				result = true;
			else if(stopHeroes==StopType.NO_STOP)
				result = true;
			else if(stopHeroes==StopType.WEAK_STOP)
				result = hero.hasThroughBombs();
			else if(stopHeroes==StopType.STRONG_STOP)
				result = false;
		}
		// si le sprite consid�r� est un feu
		else if(sprite instanceof AiFire)
		{	AiFire fire = (AiFire) sprite;
			if(stopFires==StopType.NO_STOP)
				result = true;
			else if(stopFires==StopType.WEAK_STOP)
				result = fire.hasThroughBombs();
			else if(stopFires==StopType.STRONG_STOP)
				result = false;
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("Bomb: [");
		result.append(super.toString());
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
