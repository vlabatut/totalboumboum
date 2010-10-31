package org.totalboumboum.ai.v201011.adapter.data.representation;

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
import java.util.HashMap;
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
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.sprite.block.Block;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.fire.Fire;
import org.totalboumboum.engine.content.sprite.floor.Floor;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.game.round.RoundVariables;

/**
 * repr�sente une case du jeu, avec tous les sprites qu'elle contient.
 * 
 * @author Vincent Labatut
 *
 */
final class AiDataTile implements AiTile
{	
	/**
	 * construit une repr�sentation de la case pass�e en param�tre
	 * 
	 * @param tile	case repr�sent�e
	 * @param zone	zone contenant la repr�sentation
	 */
	protected AiDataTile(Tile tile, AiDataZone zone)
	{	this.zone = zone;
		this.tile = tile;
		size = RoundVariables.scaledTileDimension;
		initTileLocation();
		initPixelLocation();
		updateSprites();
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * met � jour cette case et son contenu
	 */
	protected void update()
	{	updateSprites();		
	}
	
	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** repr�sentation de la zone � laquelle cette case appartient */
	private AiDataZone zone;
	
	@Override
	public AiDataZone getZone()
	{	return zone;	
	}
	
	/////////////////////////////////////////////////////////////////
	// ENGINE TILE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** case du jeu que cette classe repr�sente */
	private Tile tile;
	
	/**
	 * renvoie la case r�elle repr�sente par cet objet
	 * @return
	 */
	protected Tile getTile()
	{	return tile;
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
	
	/** 
	 * initialise les num�ros de ligne et colonne de cette case 
	 */
	private void initTileLocation()
	{	line = tile.getLine();
		col = tile.getCol();
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
	
	/** 
	 * initialise la position de cette case en pixels 
	 */
	private void initPixelLocation()
	{	posX = tile.getPosX();
		posY = tile.getPosY();
	}

	/////////////////////////////////////////////////////////////////
	// TILE SIZE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** longueur d'un c�t� de la case en pixels */
	private double size;
		
	@Override
	public double getSize()
	{	return size;	
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste � usage interne des blocks �ventuellement contenus dans cette case */
	private final List<AiDataBlock> internalBlocks = new ArrayList<AiDataBlock>();
	/** liste � usage externe des blocks �ventuellement contenus dans cette case */
	private final List<AiBlock> externalBlocks = new ArrayList<AiBlock>();
	/** liste � usage interne des bombes �ventuellement contenues dans cette case */
	private final List<AiDataBomb> internalBombs = new ArrayList<AiDataBomb>();
	/** liste � usage externe des bombes �ventuellement contenues dans cette case */
	private final List<AiBomb> externalBombs = new ArrayList<AiBomb>();
	/** liste � usage interne des feux �ventuellement contenus dans cette case */
	private final List<AiDataFire> internalFires = new ArrayList<AiDataFire>();
	/** liste � usage externe des feux �ventuellement contenus dans cette case */
	private final List<AiFire> externalFires = new ArrayList<AiFire>();
	/** liste � usage interne des sols �ventuellement contenus dans cette case */
	private final List<AiDataFloor> internalFloors = new ArrayList<AiDataFloor>();
	/** liste � usage externe des sols �ventuellement contenus dans cette case */
	private final List<AiFloor> externalFloors = new ArrayList<AiFloor>();
	/** liste � usage interne des personnages �ventuellement contenus dans cette case */
	private final List<AiDataHero> internalHeroes = new ArrayList<AiDataHero>();
	/** liste � usage externe des personnages �ventuellement contenus dans cette case */
	private final List<AiHero> externalHeroes = new ArrayList<AiHero>();
	/** liste � usage interne des items �ventuellement contenus dans cette case */
	private final List<AiDataItem> internalItems = new ArrayList<AiDataItem>();
	/** liste � usage externe des items �ventuellement contenus dans cette case */
	private final List<AiItem> externalItems = new ArrayList<AiItem>();

	@Override
	public List<AiBlock> getBlocks()
	{	return externalBlocks;	
	}
	
	@Override
	public List<AiBomb> getBombs()
	{	return externalBombs;	
	}
	
	@Override
	public List<AiFire> getFires()
	{	return externalFires;	
	}
	
	@Override
	public List<AiFloor> getFloors()
	{	return externalFloors;	
	}
	
	@Override
	public List<AiHero> getHeroes()
	{	return externalHeroes;	
	}
	
	@Override
	public List<AiItem> getItems()
	{	return externalItems;	
	}
	
	/** 
	 * met � jour les repr�sentations des sprites contenus dans cette case
	 */
	private void updateSprites()
	{	// block
		{	internalBlocks.clear();
			externalBlocks.clear();
			Iterator<Block> it = tile.getBlocks().iterator();
			while(it.hasNext())
			{	Block b = it.next();
				GestureName gesture = b.getCurrentGesture().getName();
				if(!(gesture==GestureName.NONE
					|| gesture==GestureName.HIDING
					|| gesture==GestureName.ENDED))
				{	AiDataBlock block = zone.getBlock(b);
					if(block==null)
					{	block = new AiDataBlock(this,b);
						zone.addBlock(block);
					}
					block.update(this);
					internalBlocks.add(block);
				}
			}
		}
		// bombs
		{	internalBombs.clear();
			externalBombs.clear();
			Iterator<Bomb> it = tile.getBombs().iterator();
			while(it.hasNext())
			{	Bomb b = it.next();
				GestureName gesture = b.getCurrentGesture().getName();
				if(!(gesture==GestureName.NONE
					|| gesture==GestureName.HIDING
					|| gesture==GestureName.ENDED))
				{	AiDataBomb bomb = zone.getBomb(b);
					if(bomb==null)
					{	bomb = new AiDataBomb(this,b);
						zone.addBomb(bomb);
					}
					bomb.update(this);
					internalBombs.add(bomb);
				}
			}
		}
		// fires
		{	internalFires.clear();
			externalFires.clear();
			Iterator<Fire> it = tile.getFires().iterator();
			while(it.hasNext())
			{	Fire f = it.next();
				GestureName gesture = f.getCurrentGesture().getName();
				if(!(gesture==GestureName.NONE
					|| gesture==GestureName.HIDING
					|| gesture==GestureName.ENDED))
				{	AiDataFire fire = zone.getFire(f);
					if(fire==null)
					{	fire = new AiDataFire(this,f);
						zone.addFire(fire);
					}
					fire.update(this);
					internalFires.add(fire);
					externalFires.add(fire);
				}
			}
		}
		// floor
		{	internalFloors.clear();
			externalFloors.clear();
			Iterator<Floor> it = tile.getFloors().iterator();
			while(it.hasNext())
			{	Floor f = it.next();
				GestureName gesture = f.getCurrentGesture().getName();
				if(!(gesture==GestureName.HIDING 
					|| gesture==GestureName.ENDED))
				{	AiDataFloor floor = zone.getFloor(f);
					if(floor==null)
					{	floor = new AiDataFloor(this,f);
						zone.addFloor(floor);
					}
					floor.update(this);
					internalFloors.add(floor);
					externalFloors.add(floor);
				}
			}
		}
		// heroes
		{	internalHeroes.clear();
			externalHeroes.clear();
			Iterator<Hero> it = tile.getHeroes().iterator();
			while(it.hasNext())
			{	Hero h = it.next();
				GestureName gesture = h.getCurrentGesture().getName();
				if(!(gesture==GestureName.HIDING 
					|| gesture==GestureName.ENDED))
				{	AiDataHero hero = zone.getHero(h);
					if(hero==null)
					{	hero = new AiDataHero(this,h);
						zone.addHero(hero);
					}
					hero.update(this);
					internalHeroes.add(hero);
					externalHeroes.add(hero);
				}
			}
		}
		// item
		{	internalItems.clear();
			externalItems.clear();
			Iterator<Item> it = tile.getItems().iterator();
			while(it.hasNext())
			{	Item i = it.next();
				GestureName gesture = i.getCurrentGesture().getName();
				if(gesture==GestureName.NONE)
				{	int hiddenItemsCount = zone.getHiddenItemsCount();
					hiddenItemsCount++;
					zone.setHiddenItemsCount(hiddenItemsCount);
				}
				else if(!(gesture==GestureName.HIDING
					|| gesture==GestureName.ENDED))
				{	AiDataItem item = zone.getItem(i);
					if(item==null)
					{	item = new AiDataItem(this,i);
						zone.addItem(item);
					}
					item.update(this);
					internalItems.add(item);
					externalItems.add(item);
				}
			}
		}
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
	 * fonction auxiliaire utilis�e pour d�terminer si cette
	 * case est traversable par le sprite pass� en param�tre.
	 * (cette fonction r�alise le traitement relativement � 
	 * la liste de sprite pass�e en param�tre)
	 * 
	 * @param sprite	le sprite qui veut traverser cette case
	 * @param list	les sprites de cette case � tester
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
	/** voisins de cette case dans les quatre directions principales */
	private final HashMap<Direction,AiDataTile> neighbors = new HashMap<Direction, AiDataTile>();
	
	/**
	 * initialise une fois pour toutes les voisins de la case,
	 * pour ne pas avoir � les recalculer � chaque appel de la m�thode
	 * getNeighbors.
	 */
	protected void initNeighbors()
	{	List<Direction> directions = Direction.getPrimaryValues();
		for(Direction direction: directions)
		{	Tile neighbor = tile.getNeighbor(direction);
			int line = neighbor.getLine();
			int col = neighbor.getCol();
			AiDataTile aiNeighbor = getZone().getTile(line,col);
			neighbors.put(direction,aiNeighbor);
		}
	}
	
	@Override
	public AiDataTile getNeighbor(Direction direction)
	{	return neighbors.get(direction);		
	}
	
	@Override
	public List<AiTile> getNeighbors()
	{	List<AiTile> result = new ArrayList<AiTile>();
		for(Direction direction: Direction.getPrimaryValues())
			result.add(neighbors.get(direction));
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiDataTile)
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
	void finish()
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
	 * @param <T>	type de simulation
	 * @param internalList	liste de simulations
	 */
	private <T extends AiDataSprite<?>, U extends AiSprite> void finishSprites(List<T> internalList, List<U> externalList)
	{	Iterator<T> it = internalList.iterator();
		while(it.hasNext())
		{	T temp = it.next();
			temp.finish();
		}
		internalList.clear();
		externalList.clear();
	}
}
