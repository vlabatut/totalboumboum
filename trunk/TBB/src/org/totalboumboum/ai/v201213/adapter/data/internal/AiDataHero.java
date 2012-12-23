package org.totalboumboum.ai.v201213.adapter.data.internal;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiSprite;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.gesture.Gesture;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Représente un personnage du jeu, ie un sprite 
 * contrôlé par un joueur humain ou un agent.
 * 
 * @author Vincent Labatut
 */
final class AiDataHero extends AiDataSprite<Hero> implements AiHero
{
	/**
	 * Crée une représentation du joueur passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param tile
	 * 		Case contenant le sprite.
	 * @param sprite
	 * 		Sprite à représenter.
	 */
	protected AiDataHero(AiDataTile tile, Hero sprite)
	{	super(tile,sprite);
		
		initColor();
		initSpeed();
		updateBombParam();
		updateSpeed();
		updateCollisions();
		updateContagious(0);
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void update(AiDataTile tile, long elapsedTime)
	{	super.update(tile,elapsedTime);
		
		updateBombParam();
		updateSpeed();
		updateCollisions();
		updateContagious(elapsedTime);
	}

	/////////////////////////////////////////////////////////////////
	// BOMB PARAMETERS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Exemple de bombe que le personnage peut poser */
	private AiDataBomb bombPrototype;
	/** Nombre de bombes que le personnage peut poser simultanément (en général) */
	private int bombNumberMax;
	/** Nombre de bombes que le personnage a actuellement posées */
	private int bombNumberCurrent;
	/** Nombre maximal de bombes que le personnage peut poser simultanément (dans l'absolu) */
	private int bombNumberLimit;
	/** Portée maximale de la bombe, dans l'absolu */
	private int bombRangeLimit;
	
	@Override
	public AiBomb getBombPrototype()
	{	return bombPrototype;
	}
	
	@Override
	public int getBombRange()
	{	return bombPrototype.getRange();
	}
	
	@Override
	public int getBombRangeLimit()
	{	return bombRangeLimit;
	}
	
	@Override
	public long getBombDuration()
	{	return bombPrototype.getNormalDuration();
	}
	
	@Override
	public long getExplosionDuration()
	{	return bombPrototype.getExplosionDuration();
	}
	
	@Override
	public int getBombNumberMax()
	{	return bombNumberMax;
	}
	
	@Override
	public int getBombNumberCurrent()
	{	return bombNumberCurrent;
	}
	
	@Override
	public int getBombNumberLimit()
	{	return bombNumberLimit;
	}

	/**
	 * Met à jour les paramètres décrivant les 
	 * bombes que ce personnage peut poser.
	 */
	private void updateBombParam()
	{	Hero sprite = getSprite();
		
		// prototype bomb
		{	Bomb bomb = sprite.makeBomb();
			bombPrototype = new AiDataBomb(tile,bomb);
			bombPrototype.tile = null;
			bombPrototype.posX = 0;
			bombPrototype.posY = 0;
			bombPrototype.posZ = 0;
		}
		
		// max number of simultaneous bombs
		{	StateAbility ab = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_NUMBER);
	    	bombNumberMax = (int)ab.getStrength();
	        ab = sprite.modulateStateAbility(StateAbilityName.HERO_BOMB_NUMBER_MAX);
			if(ab.isActive())
			{	int limit = (int)ab.getStrength();
				if(bombNumberMax>limit)
					bombNumberMax = limit;
			}
		}
		
    	// limit for this value
		{	StateAbility ab = sprite.getAbility(StateAbilityName.HERO_BOMB_NUMBER_MAX);
			bombNumberLimit = (int)ab.getStrength();
		}
		
		// number of bombs currently dropped
    	bombNumberCurrent = sprite.getDroppedBombs().size();
    	
    	// limit for the bomb range
		{	StateAbility ab = sprite.getAbility(StateAbilityName.HERO_BOMB_RANGE_MAX);
			bombRangeLimit = (int)ab.getStrength();
		}
    	
    	//System.out.println(getSprite().getColor()+": bombRange="+bombRange+" bombNumber="+bombNumber+" bombCount="+bombCount);
		//System.out.println("bombNumberLimit: "+bombNumberLimit+ " rangeLimit:"+rangeLimit);		
	}
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Couleur du personnage (et de ses bombes) */
	private PredefinedColor color;
	
	@Override
	public PredefinedColor getColor()
	{	return color;	
	}
	
	/**
	 * Initialise la couleur du personnage
	 */
	private void initColor()
	{	Hero sprite = getSprite();
		color = sprite.getColor();	
	}

	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Index de vitesse de déplacement au sol du personnage */
	private int walkingSpeedIndex;
	/** Vitesses possibles de déplacement du personnage, exprimées en pixel/seconde */
	private final Map<Integer,Double> walkingSpeeds = new HashMap<Integer, Double>();
	/** Copie externe des vitesses */
	private final Map<Integer,Double> externalWalkingSpeeds = Collections.unmodifiableMap(walkingSpeeds);
	
	@Override
	public double getWalkingSpeed()
	{	Double result = getWalkingSpeed(walkingSpeedIndex);
		return result;
	}
	
	/**
	 * Calcule la vitesse de déplacement pour
	 * l'index spécifié.
	 * 
	 * @param index
	 * 		Index à considérer.
	 * @return
	 * 		Vitesse de déplacement en pixel/seconde.
	 */
	private double getWalkingSpeed(int index)
	{	Double result = null;
	
		// get the speed indices
		List<Integer> keys = new ArrayList<Integer>(walkingSpeeds.keySet());
		Collections.sort(keys);
		int zero = keys.indexOf(0);
		
		// increased speed
		if(index>0)
		{	int i = zero;
			while(i<keys.size() && keys.get(i)<=index)
				i++;
			i--;
			index = keys.get(i);
		}
		
		// decreased speed
		else if(index<0)
		{	int i = zero;
			while(i>=0 && keys.get(i)>=index)
				i--;
			i++;
			index = keys.get(i);
		}
		result = walkingSpeeds.get(index);
				
		return result;
	}
	
	@Override
	public int getWalkingSpeedIndex()
	{	return walkingSpeedIndex;
	}
	
	@Override
	public Map<Integer,Double> getWalkingSpeeds()
	{	return externalWalkingSpeeds;
	}
	
	/**
	 * Initialise les données relatives à la vitesse
	 * de déplacement de ce personnage.
	 */
	private void initSpeed()
	{	walkingSpeedIndex = 0;
		Sprite sprite = getSprite();
		Gesture walking = getSprite().getGesturePack().getGesture(GestureName.WALKING);
		double basicSpeed = walking.getTrajectoryDirection(Direction.RIGHT).getXInteraction();
		walkingSpeeds.put(walkingSpeedIndex, basicSpeed);
		List<Integer> indices = sprite.getGroundSpeedIndices();
		for(int speedIndex: indices)
		{	String name = StateAbilityName.getHeroWalkSpeed(speedIndex);
			if(name!=null)
			{	StateAbility ability = sprite.modulateStateAbility(name);
				if(ability.isActive())
				{	double speedCoeff = ability.getStrength();
					double walkingSpeed = speedCoeff*basicSpeed;
					walkingSpeeds.put(speedIndex, walkingSpeed);
				}
			}
		}
//		System.out.print("done");
	}
	
	@Override
	protected void updateSpeed()
	{	// update current speed
		super.updateSpeed();
		
		// update current walking speed index
		Sprite sprite = getSprite();
		walkingSpeedIndex = (int)sprite.getGroundSpeedCoeff()[1];
	}

	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indique si le personnage peut traverser les murs */
	private boolean throughBlocks;
	/** Indique si le personnage peut traverser les bombes */
	private boolean throughBombs;
	/** Indique si le personnage peut traverser le feu (sans brûler) */
	private boolean throughFires;
	
	@Override
	public boolean hasThroughBlocks()
	{	return throughBlocks;	
	}

	@Override
	public boolean hasThroughBombs()
	{	return throughBombs;	
	}

	@Override
	public boolean hasThroughFires()
	{	return throughFires;	
	}

	/**
	 * Met à jour les divers pouvoirs du personnage.
	 */
	private void updateCollisions()
	{	Sprite sprite = getSprite();
		StateAbility ability;
		// traverser les murs
		ability = sprite.modulateStateAbility(StateAbilityName.SPRITE_TRAVERSE_WALL);
		throughBlocks = ability.isActive();
		// traverser les bombes
		ability = sprite.modulateStateAbility(StateAbilityName.SPRITE_TRAVERSE_BOMB);
		throughBombs = ability.isActive();
		// traverser le feu
		ability = sprite.modulateStateAbility(StateAbilityName.HERO_FIRE_PROTECTION);
		throughFires = ability.isActive();
	}
	
	@Override
	public boolean isCrossableBy(AiSprite sprite)
	{	boolean result = false;
		if(sprite instanceof AiFire)
			result = true;
		else if(sprite instanceof AiHero)
			result = true;
		else
			result = sprite.isCrossableBy(this);		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public double getEffect(AiItem item)
	{	double result = -1;
	
		AiItemType type = item.getType();
		if(type==AiItemType.ANTI_BOMB || type==AiItemType.EXTRA_BOMB|| type==AiItemType.GOLDEN_BOMB || type==AiItemType.NO_BOMB)
		{	result = bombNumberMax + item.getStrength();
			result = Math.min(result, bombNumberLimit);
			result = Math.max(result, 0);
		}
		else if(type==AiItemType.ANTI_FLAME || type==AiItemType.EXTRA_FLAME || type==AiItemType.GOLDEN_FLAME || type==AiItemType.NO_FLAME)
		{	result = getBombRange()+ item.getStrength();
			result = Math.min(result, bombRangeLimit);
			result = Math.max(result, 0);
		}
		else if(type==AiItemType.ANTI_SPEED || type==AiItemType.EXTRA_SPEED || type==AiItemType.GOLDEN_SPEED || type==AiItemType.NO_SPEED)
		{	int index = walkingSpeedIndex + (int)item.getStrength();
			result = getWalkingSpeed(index);
		}
		else
		{	// rien à faire
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTAGION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste des items contagieux possédés */
	private final List<AiItem> contagiousItems = new ArrayList<AiItem>();
	/** Version immuable des liste des items contagieux possédés */
	private final List<AiItem> externalContagiousItems = Collections.unmodifiableList(contagiousItems);
	
	/**
	 * Met à jour la liste des items contagieux
	 * de ce joueur, ainsi que les items eux-mêmes.
	 * 
	 * @param elapsedTime 
	 * 		Temps écoulé depuis la dernière mise à jour.
	 */
	private void updateContagious(long elapsedTime)
	{	List<Item> items = sprite.getAllItems();
		AiDataZone zone = tile.getZone();
		contagiousItems.clear();
		
//System.out.println("-------------------------------------------");
//System.out.println(this);
//System.out.println("-------------------------------------------");
		// on traite chaque item de l'agent
		for(Item item: items)
		{	// on teste si l'item est contagieux
			StateAbility ability = item.getAbility(StateAbilityName.ITEM_CONTAGION_MODE);
			float mode = ability.getStrength();
			if(mode!=StateAbilityName.ITEM_CONTAGION_NONE)
			{	// on récupère éventuellement l'item à partir de la zone
				AiDataItem aiItem = zone.getItem(item);
				// et on le met à jour
				if(aiItem!=null)
					aiItem.update(null, elapsedTime);
				// ou sinon, on le crée et on l'ajoute à la zone
				else
				{	aiItem = new AiDataItem(null, item);
					zone.addItem(aiItem);
				}
				// dans les deux cas, on ajoute à la liste
				contagiousItems.add(aiItem);
//System.out.println(aiItem);
			}
		}
//System.out.println("contagious: "+contagious);
	}
	
	@Override
	public boolean isContagious()
	{	return !contagiousItems.isEmpty();
	}
	
	@Override
	public List<AiItem> getContagiousItems()
	{	return externalContagiousItems;
	}
	
	/////////////////////////////////////////////////////////////////
	// RANKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public int getRoundRank()
	{	AiDataZone zone = getTile().getZone();
		int result = zone.getRoundRank(this);
		return result;
	}
	
	@Override
	public int getMatchRank()
	{	AiDataZone zone = getTile().getZone();
		int result = zone.getMatchRank(this);
		return result;
	}
	
	@Override
	public int getStatsRank()
	{	AiDataZone zone = getTile().getZone();
		int result = zone.getStatsRank(this);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("Hero: [");
		result.append(super.toString());
		result.append(" - bmbCnt.: "+bombNumberCurrent);
		result.append(" - bmbNbr.: "+bombNumberMax);
		result.append(" - bmbRge.: "+getBombRange());
		result.append(" - color: "+color);
		result.append(" ]");
		return result.toString();
	}

	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void finish()
	{	super.finish();
	}
}
