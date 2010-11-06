package org.totalboumboum.ai.v201011.adapter.model;

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
final class AiSimTile implements AiTile
{	
	/**
	 * construit une simulation de case à partir des coordonnées passées en paramètres
	 * 
	 * @param zone	zone contenant la simulation
	 * @param line	ligne de la case
	 * @param col	colonne de la case
	 * @param posX	abscisse de la case
	 * @param posY	ordonnée de la case
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

	/**
	 * construit une simulation de la case passée en paramètre
	 * 
	 * @param tile	case simulée
	 * @param zone	zone contenant la simulation
	 */
/*	protected AiSimTile(AiTile tile, AiSimZone zone)
	{	this.zone = zone;

		size = RoundVariables.scaledTileDimension;

		// location
		line = tile.getLine();
		col = tile.getCol();
		posX = tile.getPosX();
		posY = tile.getPosY();
		
		// sprites
		for(AiBlock s1: tile.getBlocks())
		{	AiSimBlock s2 = new AiSimBlock(s1,this);
			internalBlocks.add(s2);
			externalBlocks.add(s2);
		}
		for(AiBomb s1: tile.getBombs())
		{	AiSimBomb s2 = new AiSimBomb(s1,this);
			internalBombs.add(s2);
			externalBombs.add(s2);
		}
		for(AiFire s1: tile.getFires())
		{	AiSimFire s2 = new AiSimFire(s1,this);
			internalFires.add(s2);
			externalFires.add(s2);
		}
		for(AiFloor s1: tile.getFloors())
		{	AiSimFloor s2 = new AiSimFloor(s1,this);
			internalFloors.add(s2);
			externalFloors.add(s2);
		}
		for(AiHero s1: tile.getHeroes())
		{	AiSimHero s2 = new AiSimHero(s1,this);
			internalHeroes.add(s2);
			externalHeroes.add(s2);
		}
		for(AiItem s1: tile.getItems())
		{	AiSimItem s2 = new AiSimItem(s1,this);
			internalItems.add(s2);
			externalItems.add(s2);
		}
	}
*/
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
	// TILE LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** ligne de la zone contenant cette case */
	private int line;
	/** colonne de la zone contenant cette case */
	private int col;
		
	@Override
	public int getLine()
	{	return line;	
	}

	@Override
	public int getCol()
	{	return col;	
	}

	/////////////////////////////////////////////////////////////////
	// PIXEL LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** position de la case en pixels */
	private double posX;
	/** position de la case en pixels */
	private double posY;
		
	@Override
	public double getPosX()
	{	return posX;	
	}
	
	@Override
	public double getPosY()
	{	return posY;	
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE SIZE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** longueur d'un côté de la case en pixels */
	private double size;
		
	@Override
	public double getSize()
	{	return size;	
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void addSprite(AiSimSprite sprite)
	{	// tile
		//AiSimTile tile = sprite.getTile();
		//tile.addSprite(sprite);
		
		// sprites
		if(sprite instanceof AiSimBlock)
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
	
	/////////////////////////////////////////////////////////////////
	// BLOCKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste à usage interne des blocks éventuellement contenus dans cette case */
	private final List<AiSimBlock> internalBlocks = new ArrayList<AiSimBlock>();
	/** liste à usage externe des blocks éventuellement contenus dans cette case */
	private final List<AiBlock> externalBlocks = new ArrayList<AiBlock>();
	
	@Override
	public List<AiBlock> getBlocks()
	{	return externalBlocks;	
	}
	
	/** 
	 * renvoie la liste des blocks contenus dans cette case 
	 * (la liste peut être vide)
	 * 
	 * @return	les blocks éventuellement contenus dans cette case
	 */
	protected List<AiSimBlock> getInternalBlocks()
	{	return internalBlocks;	
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste à usage interne des bombes éventuellement contenues dans cette case */
	private final List<AiSimBomb> internalBombs = new ArrayList<AiSimBomb>();
	/** liste à usage externe des bombes éventuellement contenues dans cette case */
	private final List<AiBomb> externalBombs = new ArrayList<AiBomb>();
	
	@Override
	public List<AiBomb> getBombs()
	{	return externalBombs;	
	}
	
	/** 
	 * renvoie la liste des bombes contenues dans cette case 
	 * (la liste peut être vide)
	 * 
	 * @return	les bombes éventuellement contenues dans cette case
	 */
	protected List<AiSimBomb> getInternalBombs()
	{	return internalBombs;	
	}
	
	/////////////////////////////////////////////////////////////////
	// FIRES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste à usage interne des feux éventuellement contenus dans cette case */
	private final List<AiSimFire> internalFires = new ArrayList<AiSimFire>();
	/** liste à usage externe des feux éventuellement contenus dans cette case */
	private final List<AiFire> externalFires = new ArrayList<AiFire>();
	
	@Override
	public List<AiFire> getFires()
	{	return externalFires;	
	}
	
	/** 
	 * renvoie la liste des feux contenus dans cette case 
	 * (la liste peut être vide)
	 * 
	 * @return	les feux éventuellement contenus dans cette case
	 */
	protected List<AiSimFire> getInternalFires()
	{	return internalFires;	
	}
	
	/////////////////////////////////////////////////////////////////
	// FLOORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste à usage interne des sols éventuellement contenus dans cette case */
	private final List<AiSimFloor> internalFloors = new ArrayList<AiSimFloor>();
	/** liste à usage externe des sols éventuellement contenus dans cette case */
	private final List<AiFloor> externalFloors = new ArrayList<AiFloor>();
	
	@Override
	public List<AiFloor> getFloors()
	{	return externalFloors;	
	}
	
	/** 
	 * renvoie les sols de cette case 
	 * (il y a forcément au moins un sol)
	 * 
	 * @return	les sols contenus dans cette case
	 */
	protected List<AiSimFloor> getInternalFloors()
	{	return internalFloors;	
	}
	
	/////////////////////////////////////////////////////////////////
	// HEROES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste à usage interne des personnages éventuellement contenus dans cette case */
	private final List<AiSimHero> internalHeroes = new ArrayList<AiSimHero>();
	/** liste à usage externe des personnages éventuellement contenus dans cette case */
	private final List<AiHero> externalHeroes = new ArrayList<AiHero>();
	
	@Override
	public List<AiHero> getHeroes()
	{	return externalHeroes;	
	}
	
	/** 
	 * renvoie la liste des personnages contenus dans cette case 
	 * (la liste peut être vide)
	 * 
	 * @return	les personnages éventuellement contenus dans cette case
	 */
	protected List<AiSimHero> getInternalHeroes()
	{	return internalHeroes;	
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste à usage interne des items éventuellement contenus dans cette case */
	private final List<AiSimItem> internalItems = new ArrayList<AiSimItem>();
	/** liste à usage externe des items éventuellement contenus dans cette case */
	private final List<AiItem> externalItems = new ArrayList<AiItem>();

	@Override
	public List<AiItem> getItems()
	{	return externalItems;	
	}

	/** 
	 * renvoie la liste des items contenus dans cette case 
	 * (la liste peut être vide)
	 * 
	 * @return	les items éventuellement contenus dans cette case
	 */
	protected List<AiSimItem> getInternalItems()
	{	return internalItems;	
	}

	/////////////////////////////////////////////////////////////////
	// ABILITIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public boolean isCrossableBy(AiSprite sprite)
	{	boolean result = true;
		// murs
		if(result)
			result = isCrossableBy(sprite,internalBlocks);
		// bombes
		if(result)
			result = isCrossableBy(sprite,internalBombs);
		// feu
		if(result)
			result = isCrossableBy(sprite,internalFires);
		// heroes
		if(result)
			result = isCrossableBy(sprite,internalHeroes);
		// item
		if(result)
			result = isCrossableBy(sprite,internalItems);
		//
		return result;
	}
	
	/**
	 * fonction auxiliaire utilisée pour déterminer si cette
	 * case est traversable par le sprite passé en paramètre.
	 * (cette fonction réalise le traitement relativement à 
	 * la liste de sprite passée en paramètre)
	 * 
	 * @param sprite	le sprite qui veut traverser cette case
	 * @param list	les sprites de cette case à tester
	 * @return	vrai si le sprite peut traverser tous les sprites de la liste
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
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiSimTile)
		{	
//			AiTile t = (AiTile)o;	
//			result = tile==t.tile && zone==t.zone;
			result = this==o;
		}
		return result;
	}
	
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		result.append("("+line+";"+col+")");
		return result.toString();
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
	 * termine les simulations de sprites passées en paramètres
	 * 
	 * @param <T>	type de simulation
	 * @param internalList	liste de simulations
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
