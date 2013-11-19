package org.totalboumboum.ai.v201213.adapter.model.full;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiFloor;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiSprite;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Simule une case du jeu, avec tous les sprites qu'elle contient.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public final class AiSimTile extends AiTile
{	/** Id de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Construit une simulation de case à partir 
	 * des coordonnées passées en paramètres.
	 * 
	 * @param zone
	 * 		Zone contenant la simulation.
	 * @param row
	 * 		Ligne de la case.
	 * @param col
	 * 		Colonne de la case.
	 * @param posX
	 * 		Abscisse de la case.
	 * @param posY
	 * 		Ordonnée de la case.
	 */
	protected AiSimTile(AiSimZone zone, int row, int col, double posX, double posY)
	{	this.zone = zone;

		size = RoundVariables.scaledTileDimension;

		// location
		this.row = row;
		this.col = col;
		this.posX = posX;
		this.posY = posY;
	}

	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Simulation de la zone à laquelle cette case appartient */
	private AiSimZone zone;
	
	@Override
	public AiSimZone getZone()
	{	return zone;	
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Rajoute à cette case le sprite passé
	 * en paramètre.
	 * 
	 * @param sprite 
	 * 		Le sprite à rajouter à cette case.
	 */
	protected void addSprite(AiSimSprite sprite)
	{	if(sprite instanceof AiSimBlock)
		{	AiSimBlock block = (AiSimBlock)sprite;
			blocks.add(block);
			neutralBlocks.add(block);
		}
		else if(sprite instanceof AiSimBomb)
		{	AiSimBomb bomb = (AiSimBomb)sprite;
			bombs.add(bomb);
			neutralBombs.add(bomb);
		}
		else if(sprite instanceof AiSimFire)
		{	AiSimFire fire = (AiSimFire)sprite;
			fires.add(fire);
			neutralFires.add(fire);
		}
		else if(sprite instanceof AiSimFloor)
		{	AiSimFloor floor = (AiSimFloor)sprite;
			floors.add(floor);
			neutralFloors.add(floor);
		}
		else if(sprite instanceof AiSimHero)
		{	AiSimHero hero = (AiSimHero)sprite;
			heroes.add(hero);
			neutralHeroes.add(hero);
		}
		else if(sprite instanceof AiSimItem)
		{	AiSimItem item = (AiSimItem)sprite;
			items.add(item);
			neutralItems.add(item);
		}
	}
	
	/**
	 * Supprime de cette case le sprite passé
	 * en paramètre.
	 * 
	 * @param sprite 
	 * 		Le sprite à supprimer de cette case.
	 */
	protected void removeSprite(AiSimSprite sprite)
	{	if(sprite instanceof AiSimBlock)
		{	AiSimBlock block = (AiSimBlock)sprite;
			blocks.remove(block);
			neutralBlocks.remove(block);
		}
		else if(sprite instanceof AiSimBomb)
		{	AiSimBomb bomb = (AiSimBomb)sprite;
			bombs.remove(bomb);
			neutralBombs.remove(bomb);
		}
		else if(sprite instanceof AiSimFire)
		{	AiSimFire fire = (AiSimFire)sprite;
			fires.remove(fire);
			neutralFires.remove(fire);
		}
		else if(sprite instanceof AiSimFloor)
		{	AiSimFloor floor = (AiSimFloor)sprite;
			floors.remove(floor);
			neutralFloors.remove(floor);
		}
		else if(sprite instanceof AiSimHero)
		{	AiSimHero hero = (AiSimHero)sprite;
			heroes.remove(hero);
			neutralHeroes.remove(hero);
		}
		else if(sprite instanceof AiSimItem)
		{	AiSimItem item = (AiSimItem)sprite;
			items.remove(item);
			neutralItems.remove(item);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// BLOCKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste à usage interne des blocs éventuellement contenus dans cette case */
	private final List<AiSimBlock> blocks = new ArrayList<AiSimBlock>();
	/** Liste à usage externe des blocs éventuellement contenus dans cette case */
	private final List<AiBlock> neutralBlocks = new ArrayList<AiBlock>();
	/** Version immuable de la liste neutre des blocs */
	private final List<AiBlock> externalNeutralBlocks = Collections.unmodifiableList(neutralBlocks);
	
	@Override
	public List<AiBlock> getBlocks()
	{	return externalNeutralBlocks;	
	}
	
	/** 
	 * Renvoie la liste des blocks contenus dans cette case 
	 * (la liste peut être vide).
	 * 
	 * @return	
	 * 		Les blocks éventuellement contenus dans cette case.
	 */
	protected List<AiSimBlock> getInternalBlocks()
	{	return blocks;	
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste à usage interne des bombes éventuellement contenues dans cette case */
	private final List<AiSimBomb> bombs = new ArrayList<AiSimBomb>();
	/** Liste à usage externe des bombes éventuellement contenues dans cette case */
	private final List<AiBomb> neutralBombs = new ArrayList<AiBomb>();
	/** Version immuable del la liste neutre de bombes */
	private final List<AiBomb> externalNeutralBombs = Collections.unmodifiableList(neutralBombs);
	
	@Override
	public List<AiBomb> getBombs()
	{	return externalNeutralBombs;	
	}
	
	/** 
	 * Renvoie la liste des bombes contenues dans cette case 
	 * (la liste peut être vide).
	 * 
	 * @return	
	 * 		Les bombes éventuellement contenues dans cette case.
	 */
	protected List<AiSimBomb> getInternalBombs()
	{	return bombs;	
	}
	
	/////////////////////////////////////////////////////////////////
	// FIRES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste à usage interne des feux éventuellement contenus dans cette case */
	private final List<AiSimFire> fires = new ArrayList<AiSimFire>();
	/** Liste à usage externe des feux éventuellement contenus dans cette case */
	private final List<AiFire> neutralFires = new ArrayList<AiFire>();
	/** Version immuable de la liste neutre de feux */
	private final List<AiFire> externalNeutralFires = Collections.unmodifiableList(neutralFires);
	
	@Override
	public List<AiFire> getFires()
	{	return externalNeutralFires;	
	}
	
	/** 
	 * Renvoie la liste des feux contenus dans cette case 
	 * (la liste peut être vide).
	 * 
	 * @return	
	 * 		Les feux éventuellement contenus dans cette case.
	 */
	protected List<AiSimFire> getInternalFires()
	{	return fires;	
	}
	
	/////////////////////////////////////////////////////////////////
	// FLOORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste à usage interne des sols éventuellement contenus dans cette case */
	private final List<AiSimFloor> floors = new ArrayList<AiSimFloor>();
	/** Liste à usage externe des sols éventuellement contenus dans cette case */
	private final List<AiFloor> neutralFloors = new ArrayList<AiFloor>();
	/** Version immuable de la liste neutre des sols */
	private final List<AiFloor> externalNeutralFloors = Collections.unmodifiableList(neutralFloors);
	
	@Override
	public List<AiFloor> getFloors()
	{	return externalNeutralFloors;	
	}
	
	/** 
	 * Renvoie les sols de cette case 
	 * (il y a forcément au moins un sol).
	 * 
	 * @return	
	 * 		Les sols contenus dans cette case.
	 */
	protected List<AiSimFloor> getInternalFloors()
	{	return floors;	
	}
	
	/////////////////////////////////////////////////////////////////
	// HEROES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste à usage interne des personnages éventuellement contenus dans cette case */
	private final List<AiSimHero> heroes = new ArrayList<AiSimHero>();
	/** Liste à usage externe des personnages éventuellement contenus dans cette case */
	private final List<AiHero> neutralHeroes = new ArrayList<AiHero>();
	/** Version immuable de la liste des personnages */
	private final List<AiHero> externalNeutralHeroes = Collections.unmodifiableList(neutralHeroes);
	
	@Override
	public List<AiHero> getHeroes()
	{	return externalNeutralHeroes;	
	}
	
	/** 
	 * Renvoie la liste des personnages contenus dans cette case 
	 * (la liste peut être vide).
	 * 
	 * @return	
	 * 		Les personnages éventuellement contenus dans cette case.
	 */
	protected List<AiSimHero> getInternalHeroes()
	{	return heroes;	
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste à usage interne des items éventuellement contenus dans cette case */
	private final List<AiSimItem> items = new ArrayList<AiSimItem>();
	/** Liste à usage externe des items éventuellement contenus dans cette case */
	private final List<AiItem> neutralItems = new ArrayList<AiItem>();
	/** Version immuable de la liste neutre des items */
	private final List<AiItem> externalNeutralItems = Collections.unmodifiableList(neutralItems);

	@Override
	public List<AiItem> getItems()
	{	return externalNeutralItems;	
	}

	/** 
	 * Renvoie la liste des items contenus dans cette case 
	 * (la liste peut être vide).
	 * 
	 * @return	
	 * 		Les items éventuellement contenus dans cette case.
	 */
	protected List<AiSimItem> getInternalItems()
	{	return items;	
	}

	/////////////////////////////////////////////////////////////////
	// ABILITIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public boolean isCrossableBy(AiSprite sprite, 
			boolean ignoreBlocks, boolean ignoreBombs, boolean ignoreFires, boolean ignoreFloors, boolean ignoreHeroes, boolean ignoreItems)
	{	boolean result = true;
		// murs
		if(result && !ignoreBlocks)
			result = isCrossableBy(sprite,blocks);
		// bombes
		if(result && !ignoreBombs)
			result = isCrossableBy(sprite,bombs);
		// feu
		if(result && !ignoreFires)
			result = isCrossableBy(sprite,fires);
		// sol
	//	if(result && !ignoreFloors)
	//		result = isCrossableBy(sprite,internalFloors);
		// heroes
		if(result && !ignoreHeroes)
			result = isCrossableBy(sprite,heroes);
		// item
		if(result && !ignoreItems)
			result = isCrossableBy(sprite,items);
		//
		return result;
	}
	
	@Override
	public boolean isCrossableBy(AiSprite sprite)
	{	return isCrossableBy(sprite,false,false,false,false,false,false);
	}

	/**
	 * Fonction auxiliaire utilisée pour déterminer si cette
	 * case est traversable par le sprite passé en paramètre.
	 * (cette fonction réalise le traitement relativement à 
	 * la liste de sprite passée en paramètre).
	 * 
	 * @param <T> 
	 * 		Type de sprite à traiter.
	 * 
	 * @param sprite
	 * 		Le sprite qui veut traverser cette case.
	 * @param list
	 * 		Les sprites de cette case à tester.
	 * @return	
	 * 		{@code true} ssi le sprite peut traverser tous les sprites de la liste.
	 */
	private <T extends AiSprite> boolean isCrossableBy(AiSprite sprite, List<T> list)
	{	boolean result = true;
		if(!list.isEmpty())
		{	Iterator<T> it = list.iterator();
			while(it.hasNext() && result)
			{	T s = it.next();
				result = s.isCrossableBy(sprite);				
			}
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// NEIGHBORS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map des voisins de cette case dans les quatre directions principales */
	private final Map<Direction,AiSimTile> neighborMap = new HashMap<Direction, AiSimTile>();
	/** Liste des voisins de cette case dans les quatre directions principales */
	private final List<AiTile> neighborList = new ArrayList<AiTile>();
	/** Version immuable de la liste des voisins */
	private final List<AiTile> externalNeighborList = Collections.unmodifiableList(neighborList);
	
	/**
	 * Initialise une fois pour toutes les voisins de la case,
	 * pour ne pas avoir à les recalculer à chaque appel de la méthode
	 * {@link #getNeighbors}.
	 */
	protected void initNeighbors()
	{	List<Direction> directions = Direction.getPrimaryValues();
		for(Direction direction: directions)
		{	AiSimTile neighbor = zone.getNeighborTile(row,col,direction);
			neighborMap.put(direction,neighbor);
		}
		neighborMap.put(Direction.NONE,this);
		
		for(Direction direction: Direction.getPrimaryValues())
			neighborList.add(neighborMap.get(direction));
	}
	
	@Override
	public AiSimTile getNeighbor(Direction direction)
	{	if(direction.isComposite())
		{	PredefinedColor color = getZone().getOwnHero().getColor();
			throw new IllegalArgumentException("method getNeighbor does not handle composite directions ("+color+" player).");
		}
		AiSimTile result = neighborMap.get(direction);
if(result==null)
	System.out.print("");
		return result;		
	}
	
	@Override
	public List<AiTile> getNeighbors()
	{	return externalNeighborList;
	}

	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Termine proprement cette case.
	 */
	protected void finish()
	{	// block
		finishSprites(blocks,neutralBlocks);
		// bombs
		finishSprites(bombs,neutralBombs);
		// fires
		finishSprites(fires,neutralFires);
		// floor
		finishSprites(floors,neutralFloors);
		// heroes
		finishSprites(heroes,neutralHeroes);
		// item
		finishSprites(items,neutralItems);
	}

	/**
	 * Termine les simulations de sprites passées en paramètres.
	 * 
	 * @param <T>
	 * 		Type de simulation.
	 * @param <U> 
	 * 		Type de sprite à traiter.
	 * 
	 * @param internalList
	 * 		Liste de simulations.
	 * @param externalList 
	 * 		Liste de sprites.
	 */
	private <T extends AiSimSprite, U extends AiSprite> void finishSprites(List<T> internalList, List<U> externalList)
	{	Iterator<T> it = internalList.iterator();
		while(it.hasNext())
		{	T temp = it.next();
			temp.finish();
		}
		internalList.clear();
		externalList.clear();
	}
}
