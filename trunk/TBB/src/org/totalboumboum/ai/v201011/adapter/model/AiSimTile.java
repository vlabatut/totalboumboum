package org.totalboumboum.ai.v201011.adapter.model;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiFloor;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.game.round.RoundVariables;

/**
 * simule une case du jeu, avec tous les sprites qu'elle contient.
 * 
 * @author Vincent Labatut
 *
 */
final class AiSimTile extends AiTile
{	
	/**
	 * construit une simulation de case à partir des coordonnées pass�es en param�tres
	 * 
	 * @param zone
	 * 		zone contenant la simulation
	 * @param line
	 * 		ligne de la case
	 * @param col
	 * 		colonne de la case
	 * @param posX
	 * 		abscisse de la case
	 * @param posY
	 * 		ordonn�e de la case
	 */
	protected AiSimTile(AiSimZone zone, int line, int col, double posX, double posY)
	{	this.zone = zone;

		size = RoundVariables.scaledTileDimension;

		// location
		this.line = line;
		this.col = col;
		this.posX = posX;
		this.posY = posY;
	}

	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** simulation de la zone à laquelle cette case appartient */
	private AiSimZone zone;
	
	@Override
	public AiSimZone getZone()
	{	return zone;	
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// TODO comment
	protected void addSprite(AiSimSprite sprite)
	{	if(sprite instanceof AiSimBlock)
		{	AiSimBlock block = (AiSimBlock)sprite;
			internalBlocks.add(block);
			externalBlocks.add(block);
		}
		else if(sprite instanceof AiSimBomb)
		{	AiSimBomb bomb = (AiSimBomb)sprite;
			internalBombs.add(bomb);
			externalBombs.add(bomb);
		}
		else if(sprite instanceof AiSimFire)
		{	AiSimFire fire = (AiSimFire)sprite;
			internalFires.add(fire);
			externalFires.add(fire);
		}
		else if(sprite instanceof AiSimFloor)
		{	AiSimFloor floor = (AiSimFloor)sprite;
			internalFloors.add(floor);
			externalFloors.add(floor);
		}
		else if(sprite instanceof AiSimHero)
		{	AiSimHero hero = (AiSimHero)sprite;
			internalHeroes.add(hero);
			externalHeroes.add(hero);
		}
		else if(sprite instanceof AiSimItem)
		{	AiSimItem item = (AiSimItem)sprite;
			internalItems.add(item);
			externalItems.add(item);
		}
	}
	
	// TODO comment
	protected void removeSprite(AiSimSprite sprite)
	{	if(sprite instanceof AiSimBlock)
		{	AiSimBlock block = (AiSimBlock)sprite;
			internalBlocks.remove(block);
			externalBlocks.remove(block);
		}
		else if(sprite instanceof AiSimBomb)
		{	AiSimBomb bomb = (AiSimBomb)sprite;
			internalBombs.remove(bomb);
			externalBombs.remove(bomb);
		}
		else if(sprite instanceof AiSimFire)
		{	AiSimFire fire = (AiSimFire)sprite;
			internalFires.remove(fire);
			externalFires.remove(fire);
		}
		else if(sprite instanceof AiSimFloor)
		{	AiSimFloor floor = (AiSimFloor)sprite;
			internalFloors.remove(floor);
			externalFloors.remove(floor);
		}
		else if(sprite instanceof AiSimHero)
		{	AiSimHero hero = (AiSimHero)sprite;
			internalHeroes.remove(hero);
			externalHeroes.remove(hero);
		}
		else if(sprite instanceof AiSimItem)
		{	AiSimItem item = (AiSimItem)sprite;
			internalItems.remove(item);
			externalItems.remove(item);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// BLOCKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste à usage interne des blocks �ventuellement contenus dans cette case */
	private final List<AiSimBlock> internalBlocks = new ArrayList<AiSimBlock>();
	/** liste à usage externe des blocks �ventuellement contenus dans cette case */
	private final List<AiBlock> externalBlocks = new ArrayList<AiBlock>();
	
	@Override
	public List<AiBlock> getBlocks()
	{	return externalBlocks;	
	}
	
	/** 
	 * renvoie la liste des blocks contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	
	 * 		les blocks �ventuellement contenus dans cette case
	 */
	protected List<AiSimBlock> getInternalBlocks()
	{	return internalBlocks;	
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste à usage interne des bombes �ventuellement contenues dans cette case */
	private final List<AiSimBomb> internalBombs = new ArrayList<AiSimBomb>();
	/** liste à usage externe des bombes �ventuellement contenues dans cette case */
	private final List<AiBomb> externalBombs = new ArrayList<AiBomb>();
	
	@Override
	public List<AiBomb> getBombs()
	{	return externalBombs;	
	}
	
	/** 
	 * renvoie la liste des bombes contenues dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	
	 * 		les bombes �ventuellement contenues dans cette case
	 */
	protected List<AiSimBomb> getInternalBombs()
	{	return internalBombs;	
	}
	
	/////////////////////////////////////////////////////////////////
	// FIRES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste à usage interne des feux �ventuellement contenus dans cette case */
	private final List<AiSimFire> internalFires = new ArrayList<AiSimFire>();
	/** liste à usage externe des feux �ventuellement contenus dans cette case */
	private final List<AiFire> externalFires = new ArrayList<AiFire>();
	
	@Override
	public List<AiFire> getFires()
	{	return externalFires;	
	}
	
	/** 
	 * renvoie la liste des feux contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	
	 * 		les feux �ventuellement contenus dans cette case
	 */
	protected List<AiSimFire> getInternalFires()
	{	return internalFires;	
	}
	
	/////////////////////////////////////////////////////////////////
	// FLOORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste à usage interne des sols �ventuellement contenus dans cette case */
	private final List<AiSimFloor> internalFloors = new ArrayList<AiSimFloor>();
	/** liste à usage externe des sols �ventuellement contenus dans cette case */
	private final List<AiFloor> externalFloors = new ArrayList<AiFloor>();
	
	@Override
	public List<AiFloor> getFloors()
	{	return externalFloors;	
	}
	
	/** 
	 * renvoie les sols de cette case 
	 * (il y a forc�ment au moins un sol)
	 * 
	 * @return	
	 * 		les sols contenus dans cette case
	 */
	protected List<AiSimFloor> getInternalFloors()
	{	return internalFloors;	
	}
	
	/////////////////////////////////////////////////////////////////
	// HEROES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste à usage interne des personnages �ventuellement contenus dans cette case */
	private final List<AiSimHero> internalHeroes = new ArrayList<AiSimHero>();
	/** liste à usage externe des personnages �ventuellement contenus dans cette case */
	private final List<AiHero> externalHeroes = new ArrayList<AiHero>();
	
	@Override
	public List<AiHero> getHeroes()
	{	return externalHeroes;	
	}
	
	/** 
	 * renvoie la liste des personnages contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	
	 * 		les personnages �ventuellement contenus dans cette case
	 */
	protected List<AiSimHero> getInternalHeroes()
	{	return internalHeroes;	
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste à usage interne des items �ventuellement contenus dans cette case */
	private final List<AiSimItem> internalItems = new ArrayList<AiSimItem>();
	/** liste à usage externe des items �ventuellement contenus dans cette case */
	private final List<AiItem> externalItems = new ArrayList<AiItem>();

	@Override
	public List<AiItem> getItems()
	{	return externalItems;	
	}

	/** 
	 * renvoie la liste des items contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	
	 * 		les items �ventuellement contenus dans cette case
	 */
	protected List<AiSimItem> getInternalItems()
	{	return internalItems;	
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
			result = isCrossableBy(sprite,internalBlocks);
		// bombes
		if(result && !ignoreBombs)
			result = isCrossableBy(sprite,internalBombs);
		// feu
		if(result && !ignoreFires)
			result = isCrossableBy(sprite,internalFires);
		// sol
	//	if(result && !ignoreFloors)
	//		result = isCrossableBy(sprite,internalFloors);
		// heroes
		if(result && !ignoreHeroes)
			result = isCrossableBy(sprite,internalHeroes);
		// item
		if(result && !ignoreItems)
			result = isCrossableBy(sprite,internalItems);
		//
		return result;
	}
	
	@Override
	public boolean isCrossableBy(AiSprite sprite)
	{	return isCrossableBy(sprite,false,false,false,false,false,false);
	}

	/**
	 * fonction auxiliaire utilisée pour d�terminer si cette
	 * case est traversable par le sprite pass� en param�tre.
	 * (cette fonction r�alise le traitement relativement à 
	 * la liste de sprite pass�e en param�tre)
	 * 
	 * @param sprite
	 * 		le sprite qui veut traverser cette case
	 * @param list
	 * 		les sprites de cette case à tester
	 * @return	
	 * 		vrai si le sprite peut traverser tous les sprites de la liste
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
	@Override
	public AiSimTile getNeighbor(Direction direction)
	{	AiSimTile result = zone.getNeighborTile(line,col,direction);
		return result;		
	}
	
	@Override
	public List<AiTile> getNeighbors()
	{	List<AiTile> result = new ArrayList<AiTile>();
		for(Direction direction: Direction.getPrimaryValues())
			result.add(getNeighbor(direction));
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine proprement cette case
	 */
	protected void finish()
	{	// block
		finishSprites(internalBlocks,externalBlocks);
		// bombs
		finishSprites(internalBombs,externalBombs);
		// fires
		finishSprites(internalFires,externalFires);
		// floor
		finishSprites(internalFloors,externalFloors);
		// heroes
		finishSprites(internalHeroes,externalHeroes);
		// item
		finishSprites(internalItems,externalItems);
	}

	/**
	 * termine les simulations de sprites pass�es en param�tres
	 * 
	 * @param <T>
	 * 		type de simulation
	 * @param internalList
	 * 		liste de simulations
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
