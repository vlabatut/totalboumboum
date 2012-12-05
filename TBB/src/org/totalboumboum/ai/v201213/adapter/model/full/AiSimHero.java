package org.totalboumboum.ai.v201213.adapter.model.full;

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
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiSprite;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * simule un personnage du jeu, ie un sprite contrôlé par un joueur
 * humain ou une IA.
 * 
 * @author Vincent Labatut
 *
 */
public final class AiSimHero extends AiSimSprite implements AiHero
{
	/**
	 * Crée une simulation du personnage passé en paramètre,
	 * avec les propriétés passées en paramètres.
	 * 
	 * @param id
	 * 		Numéro d'identification du personnage.
	 * @param tile
	 * 		Case contenant le personnage.
	 * @param posX
	 * 		Abscisse du personnage.
	 * @param posY
	 * 		Ordonnée du personnage.
	 * @param posZ
	 * 		Hauteur du personnage.
	 * @param state
	 * 		État du personnage.
	 * @param burningDuration
	 * 		Durée de combustion du personnage.
	 * @param currentSpeed
	 * 		Vitesse courante de déplacement du personnage.
	 * @param bombPrototype
	 * 		Exemple de bombe que le personnage peut poser.
	 * @param bombNumberMax
	 * 		Nombre de bombes que le personnage peut poser.
	 * @param bombNumberCurrent
	 * 		Nombre de bombes actuellement posées.
	 * @param bombNumberLimit
	 * 		Nombre maximal de bombes que le personnage peut poser simultanément (dans l'absolu). 
	 * @param rangeLimit 
	 * 		Portée maximale de la bombe, dans l'absolu.
	 * @param throughBlocks
	 * 		Capacité du personnage à traverser les blocs.
	 * @param throughBombs
	 * 		Capacité du personnage à traverser les bombes.
	 * @param throughFires
	 * 		Capacité du personnage à traverser le feu (sans mourir !).
	 * @param color
	 * 		Couleur du personnage.
	 * @param walkingSpeedIndex
	 * 		Index de vitesse de déplacement au sol.
	 * @param walkingSpeeds
	 * 		Vitesses de déplacement au sol du personnage.
	 * @param contagious
	 * 		Indique si le personnage possède des items contagieux. 
	 * @param contagiousItems 
	 * 		Liste des items contagieux.
	 */
	protected AiSimHero(int id, AiSimTile tile, double posX, double posY, double posZ,
			AiSimState state, long burningDuration, double currentSpeed,
			AiBomb bombPrototype, int bombNumberMax, int bombNumberCurrent, int bombNumberLimit, int rangeLimit,
			boolean throughBlocks, boolean throughBombs, boolean throughFires,
			PredefinedColor color, int walkingSpeedIndex, HashMap<Integer,Double> walkingSpeeds,
			boolean contagious, List<AiItem> contagiousItems)
	{	super(id,tile,posX,posY,posZ,state,burningDuration,currentSpeed);
		AiSimZone zone = tile.getZone();
		
		// bombs
		this.bombPrototype = bombPrototype;
		this.bombNumberMax = bombNumberMax;
		this.bombNumberCurrent = bombNumberCurrent;
		this.bombNumberLimit = bombNumberLimit;
		this.rangeLimit = rangeLimit;
		
		// collisions
		this.throughBlocks = throughBlocks;
		this.throughBombs = throughBombs;
		this.throughFires = throughFires;

		// speeds
		this.walkingSpeedIndex = walkingSpeedIndex;
		this.walkingSpeeds = new HashMap<Integer,Double>(walkingSpeeds);
		
		// contagion
		this.contagious = contagious;
		this.contagiousItems = new ArrayList<AiSimItem>();
		for(AiItem item: contagiousItems)
		{	AiSimItem simItem = zone.getItem(item);
			if(simItem==null)
			{	simItem = new AiSimItem(null,item);
				zone.addSprite(simItem);
			}
			this.contagiousItems.add(simItem);
		}
		
		// misc
		this.color = color;
	}	

	/**
	 * Crée une simulation du joueur passé en paramètre, et contenue dans 
	 * la case passée en paramètre.
	 * 
	 * @param hero	
	 * 		Sprite à simuler.
	 * @param tile	
	 * 		Case contenant le sprite.
	 */
	protected AiSimHero(AiSimTile tile, AiHero hero)
	{	super(tile,hero);
		AiSimZone zone = tile.getZone();
	
		// bombs
		bombPrototype = hero.getBombPrototype();
		bombNumberMax = hero.getBombNumberMax();
		bombNumberCurrent = hero.getBombNumberCurrent();
		bombNumberLimit = hero.getBombNumberLimit();
		rangeLimit = hero.getBombRangeLimit();
		
		// collisions
		throughBlocks = hero.hasThroughBlocks();
		throughBombs = hero.hasThroughBombs();
		throughFires = hero.hasThroughFires();
		
		// speed
		walkingSpeedIndex = hero.getWalkingSpeedIndex();
		walkingSpeeds = new HashMap<Integer, Double>(hero.getWalkingSpeeds());
		
		// contagion
		contagious = hero.isContagious();
		contagiousItems = new ArrayList<AiSimItem>();
		for(AiItem item: hero.getContagiousItems())
		{	AiSimItem simItem = zone.getItem(item);
			if(simItem==null)
			{	simItem = new AiSimItem(null,item);
				zone.addSprite(simItem);
			}
			this.contagiousItems.add(simItem);
		}

		// misc
		color = hero.getColor();
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMB PARAMETERS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** exemple de bombe que le personnage peut poser */
	private AiBomb bombPrototype;
	/** nombre de bombes que le personnage peut poser simultanément (en général) */
	private int bombNumberMax;
	/** nombre de bombes que le personnage a actuellement posées */
	private int bombNumberCurrent;
	/** Nombre maximal de bombes que le personnage peut poser simultanément (dans l'absolu) */
	private int bombNumberLimit;
	/** Portée maximale de la bombe, dans l'absolu */
	private int rangeLimit;
	
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
	{	return rangeLimit;
	}
	
	/**
	 * Met à jour la portée des bombes posées par ce joueur.
	 * 
	 * @param delta
	 * 		La modification à apporter à la portée des bombes.
	 */
	protected void updateBombRange(int delta)
	{	AiSimBomb proto = new AiSimBomb(null,bombPrototype);
		proto.updateRange(delta);
		bombPrototype = proto;
	}

	@Override
	public long getBombDuration()
	{	return bombPrototype.getNormalDuration();
	}

	@Override
	public long getExplosionDuration()
	{	return bombPrototype.getExplosionDuration();
	}
	
	/**
	 * met à jour le nombre de bombes posables simultanément par ce joueur
	 * 
	 * @param delta
	 * 		la modification à apporter au nombre de bombes
	 */
	protected void updateBombNumberMax(int delta)
	{	bombNumberMax = bombNumberMax + delta;
	}
	
	@Override
	public int getBombNumberMax()
	{	return bombNumberMax;
	}
	
	/**
	 * met à jour le nombre de bombes actuellement en jeu et appartenant
	 * à ce joueur
	 * 
	 * @param delta
	 * 		la modification à apporter au nombre de bombes
	 */
	protected void updateBombNumberCurrent(int delta)
	{	bombNumberCurrent = bombNumberCurrent + delta;
	}
	
	@Override
	public int getBombNumberCurrent()
	{	return bombNumberCurrent;
	}
	
	@Override
	public int getBombNumberLimit()
	{	return bombNumberLimit;
	}
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** couleur du personnage (et de ses bombes) */
	private PredefinedColor color;
	
	@Override
	public PredefinedColor getColor()
	{	return color;	
	}
	
	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Index de vitesse de déplacement au sol du personnage */
	private int walkingSpeedIndex;
	/** Vitesses possibles de déplacement du personnage, exprimées en pixel/seconde */
	private HashMap<Integer,Double> walkingSpeeds;
	
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
		
		int delta = -(int)Math.signum(index);
		while(index!=0 && result==null)
		{	result = walkingSpeeds.get(index);
			if(result==null)
				index = index + delta;
		}
		
		if(index==0)
			result = walkingSpeeds.get(index);
		
		return result;
	}
	
	@Override
	public int getWalkingSpeedIndex()
	{	return walkingSpeedIndex;
	}
	
	@Override
	public HashMap<Integer,Double> getWalkingSpeeds()
	{	return walkingSpeeds;
	}

	/**
	 * Met à jour la vitesse de déplacement de ce joueur.
	 * 
	 * @param delta
	 * 		La modification à apporter à l'index de vitesse du joueur.
	 */
	protected void updateWalkingSpeedIndex(int delta)
	{	walkingSpeedIndex = walkingSpeedIndex + delta;
	}
	
	/////////////////////////////////////////////////////////////////
	// COLLISIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si le personnage peut traverser les murs */
	private boolean throughBlocks;
	/** indique si le personnage peut traverser les bombes */
	private boolean throughBombs;
	/** indique si le personnage peut traverser le feu (sans brûler) */
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

	@Override
	public boolean isCrossableBy(AiSprite sprite)
	{	boolean result = false;
		
		// feu : jamais arrété par un personnage
		if(sprite instanceof AiFire)
			result = true;
		
		// personnages : pareil
		else if(sprite instanceof AiHero)
			result = true;
		
		// pour tout le reste, on renverse la relation 
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
		if(type==AiItemType.ANTI_BOMB || type==AiItemType.EXTRA_BOMB || type==AiItemType.NO_BOMB)
		{	result = bombNumberMax + item.getStrength();
			result = Math.min(result, bombNumberLimit);
		}
		else if(type==AiItemType.ANTI_FLAME || type==AiItemType.EXTRA_FLAME || type==AiItemType.NO_FLAME)
		{	result = getBombRange()+ item.getStrength();
			result = Math.min(result, rangeLimit);
		}
		else if(type==AiItemType.ANTI_SPEED || type==AiItemType.EXTRA_SPEED || type==AiItemType.NO_SPEED)
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
	/** Indique si ce joueur a un item contagieux */
	private boolean contagious;
	/** Liste des items contagieux possédés */
	private List<AiSimItem> contagiousItems;
	
	@Override
	public boolean isContagious()
	{	return contagious;
	}
	
	@Override
	public List<AiItem> getContagiousItems()
	{	List<AiItem> result = new ArrayList<AiItem>(contagiousItems);
		return result;
	}
	
	/**
	 * Renvoie la liste interne contenant les items
	 * contagieux.
	 * 
	 * @return
	 * 		Une liste d'items.
	 */
	public List<AiSimItem> getInternalContagiousItems()
	{	return contagiousItems;
	}
	
	/////////////////////////////////////////////////////////////////
	// RANKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public int getRoundRank()
	{	AiSimZone zone = getTile().getZone();
		int result = zone.getRoundRank(this);
		return result;
	}
	
	@Override
	public int getMatchRank()
	{	AiSimZone zone = getTile().getZone();
		int result = zone.getMatchRank(this);
		return result;
	}
	
	@Override
	public int getStatsRank()
	{	AiSimZone zone = getTile().getZone();
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
