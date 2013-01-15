package org.totalboumboum.ai.v201314.adapter.data.internal;

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

import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiFloor;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiSprite;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
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
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Représente une case du jeu, avec tous les sprites qu'elle contient.
 * 
 * @author Vincent Labatut
 */
final class AiDataTile extends AiTile
{	
	/**
	 * Construit une représentation de la case passée en paramètre.
	 * 
	 * @param tile
	 * 		Case représentée.
	 * @param zone
	 * 		Zone contenant la représentation.
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
	 * Met à jour cette case et son contenu.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 */
	protected void update(long elapsedTime)
	{	updateSprites(elapsedTime);		
	}
	
	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Représentation de la zone à laquelle cette case appartient */
	private AiDataZone zone;
	
	@Override
	public AiDataZone getZone()
	{	return zone;	
	}
	
	/////////////////////////////////////////////////////////////////
	// ENGINE TILE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Case du jeu que cette classe représente */
	private Tile tile;
	
	/**
	 * Renvoie la case réelle représentée par cet objet.
	 * 
	 * @return
	 * 		La case réelle représentée par cet objet.
	 */
	protected Tile getTile()
	{	return tile;
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Initialise les numéros de ligne et colonne de cette case. 
	 */
	private void initTileLocation()
	{	row = tile.getRow();
		col = tile.getCol();
	}

	/////////////////////////////////////////////////////////////////
	// PIXEL LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Initialise la position de cette case en pixels. 
	 */
	private void initPixelLocation()
	{	posX = tile.getPosX();
		posY = tile.getPosY();
	}

	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Met à jour les représentations des sprites contenus dans cette case.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 */
	private void updateSprites(long elapsedTime)
	{	updateBlocks(elapsedTime);
		updateBombs(elapsedTime);
		updateFires(elapsedTime);
		updateFloors(elapsedTime);
		updateHeroes(elapsedTime);
		updateItems(elapsedTime);
	}
	
	/////////////////////////////////////////////////////////////////
	// BLOCKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste à usage interne des blocs éventuellement contenus dans cette case */
	private final List<AiDataBlock> blocks = new ArrayList<AiDataBlock>();
	/** Liste à usage externe des blocs éventuellement contenus dans cette case */
	private final List<AiBlock> neutralBlocks = new ArrayList<AiBlock>();
	/** Version immuable de la liste externe de blocs */
	private final List<AiBlock> externalNeutralBlocks = Collections.unmodifiableList(neutralBlocks);
	
	@Override
	public List<AiBlock> getBlocks()
	{	return externalNeutralBlocks;	
	}
	
	/** 
	 * Met à jour les représentations des blocs contenus dans cette case.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 */
	private void updateBlocks(long elapsedTime)
	{	blocks.clear();
		neutralBlocks.clear();
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
				blocks.add(block);
				neutralBlocks.add(block);
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste à usage interne des bombes éventuellement contenues dans cette case */
	private final List<AiDataBomb> bombs = new ArrayList<AiDataBomb>();
	/** Liste à usage externe des bombes éventuellement contenues dans cette case */
	private final List<AiBomb> neutralBombs = new ArrayList<AiBomb>();
	/** Version immuable de la liste externe de bombes */
	private final List<AiBomb> externalNeutralBombs = Collections.unmodifiableList(neutralBombs);
	
	@Override
	public List<AiBomb> getBombs()
	{	return externalNeutralBombs;	
	}
	
	/** 
	 * Met à jour les représentations des bombes contenus dans cette case.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 */
	private void updateBombs(long elapsedTime)
	{	bombs.clear();
		neutralBombs.clear();
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
				bombs.add(bomb);
				neutralBombs.add(bomb);
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// FIRES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste à usage interne des feux éventuellement contenus dans cette case */
	private final List<AiDataFire> fires = new ArrayList<AiDataFire>();
	/** Liste à usage externe des feux éventuellement contenus dans cette case */
	private final List<AiFire> neutralFires = new ArrayList<AiFire>();
	/** Version immuable de la liste externe de feux */
	private final List<AiFire> externalNeutralFires = Collections.unmodifiableList(neutralFires);
	
	@Override
	public List<AiFire> getFires()
	{	return externalNeutralFires;	
	}
	
	/** 
	 * Met à jour les représentations des feux contenus dans cette case.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 */
	private void updateFires(long elapsedTime)
	{	fires.clear();
		neutralFires.clear();
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
				fires.add(fire);
				neutralFires.add(fire);
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// FLOORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste à usage interne des sols éventuellement contenus dans cette case */
	private final List<AiDataFloor> floors = new ArrayList<AiDataFloor>();
	/** Liste à usage externe des sols éventuellement contenus dans cette case */
	private final List<AiFloor> neutralFloors = new ArrayList<AiFloor>();
	/** Version immuable de la liste externe de sols */
	private final List<AiFloor> externalNeutralFloors = Collections.unmodifiableList(neutralFloors);
	
	@Override
	public List<AiFloor> getFloors()
	{	return externalNeutralFloors;	
	}
	
	/** 
	 * Met à jour les représentations des sols contenus dans cette case.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 */
	private void updateFloors(long elapsedTime)
	{	floors.clear();
		neutralFloors.clear();
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
				floors.add(floor);
				neutralFloors.add(floor);
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// HEROES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste à usage interne des personnages éventuellement contenus dans cette case */
	private final List<AiDataHero> heroes = new ArrayList<AiDataHero>();
	/** Liste à usage externe des personnages éventuellement contenus dans cette case */
	private final List<AiHero> neutralHeroes = new ArrayList<AiHero>();
	/** Version immuable de la liste externe de personnages */
	private final List<AiHero> externalNeutralHeroes = Collections.unmodifiableList(neutralHeroes);
	
	@Override
	public List<AiHero> getHeroes()
	{	return externalNeutralHeroes;	
	}
	
	/** 
	 * Met à jour les représentations des personnages contenus dans cette case.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 */
	private void updateHeroes(long elapsedTime)
	{	heroes.clear();
		neutralHeroes.clear();
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
				heroes.add(hero);
				neutralHeroes.add(hero);
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste à usage interne des items éventuellement contenus dans cette case */
	private final List<AiDataItem> items = new ArrayList<AiDataItem>();
	/** Liste à usage externe des items éventuellement contenus dans cette case */
	private final List<AiItem> neutralItems = new ArrayList<AiItem>();
	/** Version immuable de la liste externe de items */
	private final List<AiItem> externalNeutralItems = Collections.unmodifiableList(neutralItems);

	@Override
	public List<AiItem> getItems()
	{	return externalNeutralItems;	
	}

	/** 
	 * Met à jour les représentations des items contenus dans cette case.
	 * 
	 * @param elapsedTime
	 * 		Temps écoulé depuis la dernière mise à jour.
	 */
	private void updateItems(long elapsedTime)
	{	items.clear();
		neutralItems.clear();
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
				items.add(item);
				neutralItems.add(item);
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
			result = isCrossableBy(sprite,blocks);
		// bombes
		if(result && !ignoreBombs)
			result = isCrossableBy(sprite,bombs);
		// feu
		if(result && !ignoreFires)
			result = isCrossableBy(sprite,fires);
		// sol
//		if(result && !ignoreFloors)
//			result = isCrossableBy(sprite,internalFloors);
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
	 * Méthode auxiliaire utilisée pour déterminer si cette
	 * case est traversable par le sprite passé en paramètre.
	 * (cette fonction réalise le traitement relativement à 
	 * la liste de sprite passée en paramètre).
	 * 
	 * @param <T> 
	 * 		Type de sprite à traiter
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
	private final Map<Direction,AiDataTile> neighborMap = new HashMap<Direction, AiDataTile>();
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
		{	Tile neighbor = tile.getNeighbor(direction);
			int row = neighbor.getRow();
			int col = neighbor.getCol();
			AiDataTile aiNeighbor = getZone().getTile(row,col);
			neighborMap.put(direction,aiNeighbor);
		}
		neighborMap.put(Direction.NONE,this);
		
		for(Direction direction: Direction.getPrimaryValues())
			neighborList.add(neighborMap.get(direction));
	}
	
	@Override
	public AiDataTile getNeighbor(Direction direction)
	{	if(direction.isComposite())
		{	PredefinedColor color = getZone().getOwnHero().getColor();
			throw new IllegalArgumentException("method getNeighbor does not handle composite directions ("+color+" player).");
		}
		AiDataTile result = neighborMap.get(direction);
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
	void finish()
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
	 * 		type de simulation.
	 * @param <U> 
	 * 		Type de sprite à traiter.
	 * @param internalList
	 * 		liste de simulations.
	 * @param externalList 
	 * 		Liste de simulations.
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
