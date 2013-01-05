package org.totalboumboum.ai.v201112.adapter.data.internal;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiFloor;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiItemType;
import org.totalboumboum.ai.v201112.adapter.data.AiSprite;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
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
 * représente une case du jeu, avec tous les sprites qu'elle contient.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
final class AiDataTile extends AiTile
{	
	/**
	 * construit une représentation de la case passée en paramètre
	 * 
	 * @param tile
	 * 		case représentée
	 * @param zone
	 * 		zone contenant la représentation
	 */
	protected AiDataTile(Tile tile, AiDataZone zone)
	{	this.zone = zone;
		this.tile = tile;
		size = RoundVariables.scaledTileDimension;
		initTileLocation();
		initPixelLocation();
		updateSprites(0);
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * met à jour cette case et son contenu
	 * 
	 * @param elapsedTime 
	 */
	protected void update(long elapsedTime)
	{	updateSprites(elapsedTime);		
	}
	
	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** représentation de la zone à laquelle cette case appartient */
	private AiDataZone zone;
	
	@Override
	public AiDataZone getZone()
	{	return zone;	
	}
	
	/////////////////////////////////////////////////////////////////
	// ENGINE TILE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** case du jeu que cette classe représente */
	private Tile tile;
	
	/**
	 * renvoie la case réelle représentée par cet objet
	 * 
	 * @return
	 * 		la case réelle représentée par cet objet
	 */
	protected Tile getTile()
	{	return tile;
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * initialise les numéros de ligne et colonne de cette case 
	 */
	private void initTileLocation()
	{	row = tile.getRow();
		col = tile.getCol();
	}

	/////////////////////////////////////////////////////////////////
	// PIXEL LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * initialise la position de cette case en pixels 
	 */
	private void initPixelLocation()
	{	posX = tile.getPosX();
		posY = tile.getPosY();
	}

	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste à usage interne des blocks éventuellement contenus dans cette case */
	private final List<AiDataBlock> internalBlocks = new ArrayList<AiDataBlock>();
	/** liste à usage externe des blocks éventuellement contenus dans cette case */
	private final List<AiBlock> externalBlocks = new ArrayList<AiBlock>();
	/** liste à usage interne des bombes éventuellement contenues dans cette case */
	private final List<AiDataBomb> internalBombs = new ArrayList<AiDataBomb>();
	/** liste à usage externe des bombes éventuellement contenues dans cette case */
	private final List<AiBomb> externalBombs = new ArrayList<AiBomb>();
	/** liste à usage interne des feux éventuellement contenus dans cette case */
	private final List<AiDataFire> internalFires = new ArrayList<AiDataFire>();
	/** liste à usage externe des feux éventuellement contenus dans cette case */
	private final List<AiFire> externalFires = new ArrayList<AiFire>();
	/** liste à usage interne des sols éventuellement contenus dans cette case */
	private final List<AiDataFloor> internalFloors = new ArrayList<AiDataFloor>();
	/** liste à usage externe des sols éventuellement contenus dans cette case */
	private final List<AiFloor> externalFloors = new ArrayList<AiFloor>();
	/** liste à usage interne des personnages éventuellement contenus dans cette case */
	private final List<AiDataHero> internalHeroes = new ArrayList<AiDataHero>();
	/** liste à usage externe des personnages éventuellement contenus dans cette case */
	private final List<AiHero> externalHeroes = new ArrayList<AiHero>();
	/** liste à usage interne des items éventuellement contenus dans cette case */
	private final List<AiDataItem> internalItems = new ArrayList<AiDataItem>();
	/** liste à usage externe des items éventuellement contenus dans cette case */
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
	 * met à jour les représentations des sprites contenus dans cette case
	 * 
	 * @param elapsedTime
	 * 		temps écoulé depuis la dernière mise à jour
	 */
	private void updateSprites(long elapsedTime)
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
					block.update(this,elapsedTime);
					internalBlocks.add(block);
					externalBlocks.add(block);
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
					bomb.update(this,elapsedTime);
					internalBombs.add(bomb);
					externalBombs.add(bomb);
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
					fire.update(this,elapsedTime);
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
					floor.update(this,elapsedTime);
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
					hero.update(this,elapsedTime);
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
				{	AiItemType type = AiItemType.makeItemType(i.getItemName());	
					zone.signalHiddenItem(type);
				}
				else if(!(gesture==GestureName.HIDING
					|| gesture==GestureName.ENDED))
				{	AiDataItem item = zone.getItem(i);
					if(item==null)
					{	item = new AiDataItem(this,i);
						zone.addItem(item);
					}
					item.update(this,elapsedTime);
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
//		if(result && !ignoreFloors)
//			result = isCrossableBy(sprite,internalFloors);
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
	 * fonction auxiliaire utilisée pour déterminer si cette
	 * case est traversable par le sprite passé en paramètre.
	 * (cette fonction réalise le traitement relativement à 
	 * la liste de sprite passée en paramètre)
	 * 
	 * @param <T> 
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
	/** voisins de cette case dans les quatre directions principales */
	private final HashMap<Direction,AiDataTile> neighbors = new HashMap<Direction, AiDataTile>();
	
	/**
	 * Initialise une fois pour toutes les voisins de la case,
	 * pour ne pas avoir à les recalculer à chaque appel de la méthode
	 * {@link #getNeighbors}.
	 */
	protected void initNeighbors()
	{	List<Direction> directions = Direction.getPrimaryValues();
		for(Direction direction: directions)
		{	Tile neighbor = tile.getNeighbor(direction);
			int row = neighbor.getRow();
			int col = neighbor.getCol();
			AiDataTile aiNeighbor = getZone().getTile(row,col);
			neighbors.put(direction,aiNeighbor);
		}
		neighbors.put(Direction.NONE,this);
	}
	
	@Override
	public AiDataTile getNeighbor(Direction direction)
	{	if(direction.isComposite())
			throw new IllegalArgumentException("method getNeighbor does not handle composite directions.");
		AiDataTile result = neighbors.get(direction);
		return result;
	}
	
	@Override
	public List<AiTile> getNeighbors()
	{	List<AiTile> result = new ArrayList<AiTile>();
		for(Direction direction: Direction.getPrimaryValues())
			result.add(neighbors.get(direction));
		return result;
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
	 * termine les simulations de sprites passées en paramètres
	 * 
	 * @param <T>
	 * 		type de simulation
	 * @param <U> 
	 * 
	 * @param internalList
	 * 		liste de simulations
	 * @param externalList 
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
