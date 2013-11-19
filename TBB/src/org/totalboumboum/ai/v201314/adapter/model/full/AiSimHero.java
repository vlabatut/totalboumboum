package org.totalboumboum.ai.v201314.adapter.model.full;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiSprite;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Simule un personnage du jeu, ie un sprite contrôlé par un joueur
 * humain ou un agent.
 * 
 * @author Vincent Labatut
 */
public final class AiSimHero extends AiSimSprite implements AiHero
{	/** Id de la classe */
	private static final long serialVersionUID = 1L;
	
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
	 * @param contagiousItems 
	 * 		Liste des items contagieux.
	 * @param uuid 
	 * 		Identifiant unique du joueur contrôlant ce personnage.
	 */
	protected AiSimHero(int id, AiSimTile tile, double posX, double posY, double posZ,
			AiSimState state, long burningDuration, double currentSpeed,
			AiSimBomb bombPrototype, int bombNumberMax, int bombNumberCurrent, int bombNumberLimit, int rangeLimit,
			boolean throughBlocks, boolean throughBombs, boolean throughFires,
			PredefinedColor color, int walkingSpeedIndex, Map<Integer,Double> walkingSpeeds,
			List<AiItem> contagiousItems,
			String uuid)
	{	super(id,tile,posX,posY,posZ,state,burningDuration,currentSpeed);
		AiSimZone zone = tile.getZone();
		
		// bombs
		this.bombPrototype = bombPrototype;
		this.bombNumberMax = bombNumberMax;
		this.bombNumberCurrent = bombNumberCurrent;
		this.bombNumberLimit = bombNumberLimit;
		this.bombRangeLimit = rangeLimit;
		
		// collisions
		this.throughBlocks = throughBlocks;
		this.throughBombs = throughBombs;
		this.throughFires = throughFires;

		// speeds
		this.walkingSpeedIndex = walkingSpeedIndex;
		this.walkingSpeeds.putAll(walkingSpeeds);
		
		// contagion
		for(AiItem item: contagiousItems)
		{	AiSimItem simItem = zone.getItem(item);
			if(simItem==null)
			{	simItem = new AiSimItem(null,item);
				zone.addSprite(simItem);
			}
			this.contagiousItems.add(simItem);
			this.neutralContagiousItems.add(simItem);
		}
		
		// misc
		this.color = color;
		this.uuid = uuid;
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
		bombPrototype = new AiSimBomb(null,hero.getBombPrototype());
		bombNumberMax = hero.getBombNumberMax();
		bombNumberCurrent = hero.getBombNumberCurrent();
		bombNumberLimit = hero.getBombNumberLimit();
		bombRangeLimit = hero.getBombRangeLimit();
		
		// collisions
		throughBlocks = hero.hasThroughBlocks();
		throughBombs = hero.hasThroughBombs();
		throughFires = hero.hasThroughFires();
		
		// speed
		walkingSpeedIndex = hero.getWalkingSpeedIndex();
		walkingSpeeds.putAll(hero.getWalkingSpeeds());
		
		// contagion
		for(AiItem item: hero.getContagiousItems())
		{	AiSimItem simItem = zone.getItem(item);
			if(simItem==null)
			{	simItem = new AiSimItem(null,item);
				zone.addSprite(simItem);
			}
			this.contagiousItems.add(simItem);
			this.neutralContagiousItems.add(simItem);
		}

		// misc
		color = hero.getColor();
		uuid = hero.getUuid();
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMB PARAMETERS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Exemple de bombe que le personnage peut poser */
	private AiSimBomb bombPrototype;
	/** Nombre de bombes que le personnage peut poser simultanément (en général) */
	private int bombNumberMax;
	/** Nombre de bombes maximal avant de prendre un malus genre no-bomb */
	protected int savedBombNumberMax = -1;
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
	
	/**
	 * Renvoie la portée enregistrée avant de prendre
	 * un malus, genre no-flame. Utilisée lors de la
	 * simulation.
	 * 
	 * @return
	 * 		La portée avant malus.
	 */
	protected int getSavedRange()
	{	return bombPrototype.getSavedRange();	
	}
	
	/**
	 * Enregistre la portée, avant de la modifier
	 * en raison d'un malus genre no-flame. Utilisée 
	 * lors de la simulation, pour restaurer la portée
	 * originale.
	 * <br/>
	 * Si la portée a déjà été enregistrée (i.e. plusieurs
	 * malus ramassés successivement), alors elle n'est
	 * pas ré-enregistrée, pour ne pas perdre la valeur pertinente.
	 */
	protected void recordRange()
	{	bombPrototype.recordRange();
	}
	
	/**
	 * Restaure la portée, après qu'un malus genre 
	 * no-flame ait arrêté de l'affecter.
	 * <br/>
	 * L'enregistrement est ré-initialisé à -1, afin
	 * de permettre des sauvegardes ultérieures.
	 */
	protected void restoreRange()
	{	bombPrototype.restoreRange();
	}
	
	@Override
	public int getBombRangeLimit()
	{	return bombRangeLimit;
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
	 * Renvoie le nombre de bombes enregistré avant de prendre
	 * un malus, genre no-bomb. Utilisée lors de la
	 * simulation.
	 * 
	 * @return
	 * 		Le nombnre de bombes avant malus.
	 */
	protected int getSavedBombNumberMax()
	{	return savedBombNumberMax;	
	}
	
	/**
	 * Enregistre le nombre de bombes maximal, avant de le modifier
	 * en raison d'un malus genre no-bomb. Utilisée 
	 * lors de la simulation, pour restaurer le nombre
	 * original.
	 * <br/>
	 * Si le nombre a déjà été enregistré (i.e. plusieurs
	 * malus ramassés successivement), alors il n'est
	 * pas ré-enregistré, pour ne pas perdre la valeur pertinente.
	 */
	protected void recordBombNumberMax()
	{	if(savedBombNumberMax == -1)
			savedBombNumberMax = bombNumberMax;
	}
	
	/**
	 * Restaure le nombre de bombes maximal, après qu'un malus genre 
	 * no-bomb ait arrêté de l'affecter.
	 * <br/>
	 * L'enregistrement est ré-initialisé à -1, afin
	 * de permettre des sauvegardes ultérieures.
	 */
	protected void restoreBombNumberMax()
	{	if(savedBombNumberMax == -1)
		{	bombNumberMax = 1;
			savedBombNumberMax = -1;
		}
		else
		{	bombNumberMax = savedBombNumberMax;
			savedBombNumberMax = -1;
		}
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
	// UUID				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Identifiant unique du joueur controlant ce personnage */
	private String uuid;
	
	@Override
	public String getUuid()
	{	return uuid;	
	}
	
	/////////////////////////////////////////////////////////////////
	// SPEED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Index de vitesse de déplacement au sol du personnage */
	private int walkingSpeedIndex;
	/** Portée de la bombe avant de prendre un malus genre no-flame */
	protected int savedWalkingSpeedIndex = -1;
	/** Vitesses possibles de déplacement du personnage, exprimées en pixel/seconde */
	private final Map<Integer,Double> walkingSpeeds = new HashMap<Integer, Double>();
	/** Version immuable de la map des vitesses */
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
	 * Met à jour la vitesse de déplacement de ce joueur.
	 * 
	 * @param delta
	 * 		La modification à apporter à l'index de vitesse du joueur.
	 */
	protected void updateWalkingSpeedIndex(int delta)
	{	walkingSpeedIndex = walkingSpeedIndex + delta;
	}
	
	/**
	 * Renvoie la vitesse enregistrée avant de prendre
	 * un malus, genre no-speed. Utilisée lors de la
	 * simulation.
	 * 
	 * @return
	 * 		La vitesse avant malus.
	 */
	protected int getSavedWalkingSpeed()
	{	return savedWalkingSpeedIndex;	
	}
	
	/**
	 * Enregistre la vitesse, avant de la modifier
	 * en raison d'un malus genre no-speed. Utilisée 
	 * lors de la simulation, pour restaurer la vitesse
	 * originale.
	 * <br/>
	 * Si la vitesse a déjà été enregistrée (i.e. plusieurs
	 * malus ramassés successivement), alors elle n'est
	 * pas ré-enregistrée, pour ne pas perdre la valeur pertinente.
	 */
	protected void recordWalkingSpeedIndex()
	{	if(savedWalkingSpeedIndex == -1)
			savedWalkingSpeedIndex = walkingSpeedIndex;
	}
	
	/**
	 * Restaure la vitesse, après qu'un malus genre 
	 * no-speed ait arrêté de l'affecter.
	 * <br/>
	 * L'enregistrement est ré-initialisé à -1, afin
	 * de permettre des sauvegardes ultérieures.
	 */
	protected void restoreWalkingSpeedIndex()
	{	if(savedWalkingSpeedIndex == -1)
		{	walkingSpeedIndex = 1;
			savedWalkingSpeedIndex = -1;
		}
		else
		{	walkingSpeedIndex = savedWalkingSpeedIndex;
			savedWalkingSpeedIndex = -1;
		}
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
			result = Math.min(result, bombRangeLimit);
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
	/** Liste des items contagieux possédés */
	private final List<AiSimItem> contagiousItems = new ArrayList<AiSimItem>();
	/** Liste neutre des items contagieux possédés */
	private final List<AiItem> neutralContagiousItems = new ArrayList<AiItem>();
	/** Version immuable de la liste neutre des items contagieux possédés */
	private final List<AiItem> externalNeutralContagiousItems = Collections.unmodifiableList(neutralContagiousItems);
	
	@Override
	public boolean isContagious()
	{	return !contagiousItems.isEmpty();
	}
	
	@Override
	public List<AiItem> getContagiousItems()
	{	return externalNeutralContagiousItems;
	}
	
	/**
	 * Rajoute un item à la liste des items contagieux de ce personnage.
	 * <br/>
	 * Cette fonction est à usage interne et vous (le concepteur de l'agent)
	 * ne devez pas l'utiliser.
	 * 
	 * @param item
	 * 		L'item à rajouter à la liste des items contagieux de ce personnage.
	 */
	public void addContagiousItem(AiSimItem item)
	{	contagiousItems.add(item);
		neutralContagiousItems.add(item);
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
	
	/**
	 * Supprime un item contagieux de la liste.
	 * 
	 * @param item
	 * 		L'item à supprimer.
	 */
	public void removeContagiousItem(AiItem item)
	{	contagiousItems.remove(item);
		neutralContagiousItems.remove(item);
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
