package org.totalboumboum.ai.v200910.adapter.data;

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
 */

public class AiTile
{	/**
	 * construit une représentation de la case passée en paramètre
	 * @param tile	case représentée
	 * @param zone	zone contenant la représentation
	 */
	AiTile(Tile tile, AiZone zone)
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
	 * met à jour cette case et son contenu
	 */
	void update()
	{	updateSprites();		
	}
	
	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** représentation de la zone à laquelle cette case appartient */
	private AiZone zone;
	
	public AiZone getZone()
	{	return zone;	
	}
	
	/////////////////////////////////////////////////////////////////
	// ENGINE TILE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** case du jeu que cette classe représente */
	private Tile tile;
	
	Tile getTile()
	{	return tile;
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** ligne de la zone contenant cette case */
	private int line;
	/** colonne de la zone contenant cette case */
	private int col;
		
	/** 
	 * renvoie le numéro de la ligne contenant cette case
	 * 
	 * @return	la ligne de cette case
	 */
	public int getLine()
	{	return line;	
	}
	/** 
	 * renvoie le numéro de la colonne contenant cette case
	 *  
	 * @return	la colonne de cette case
	 */
	public int getCol()
	{	return col;	
	}
	
	/** 
	 * initialise les numéros de ligne et colonne de cette case 
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
		
	/** 
	 * renvoie l'abscisse de la case en pixels
	 * 
	 * @return	l'abscisse de cette case
	 */
	public double getPosX()
	{	return posX;	
	}
	
	/** 
	 * renvoie l'ordonnée de la case en pixels
	 * 
	 * @return	l'ordonnée de cette case
	 */
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
	/** longueur d'un côté de la case en pixels */
	private double size;
		
	/** 
	 * renvoie la taille de la case en pixels
	 * 
	 * @return	longueur d'un côté de la case en pixels
	 */
	public double getSize()
	{	return size;	
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste des blocks éventuellement contenus dans cette case */
	private final List<AiBlock> blocks = new ArrayList<AiBlock>();
	/** liste des bombes éventuellement contenues dans cette case */
	private final List<AiBomb> bombs = new ArrayList<AiBomb>();
	/** liste des feux éventuellement contenus dans cette case */
	private final List<AiFire> fires = new ArrayList<AiFire>();
	/** liste des sols éventuellement contenus dans cette case */
	private final List<AiFloor> floors = new ArrayList<AiFloor>();
	/** liste des personnages éventuellement contenus dans cette case */
	private final List<AiHero> heroes = new ArrayList<AiHero>();
	/** liste des items éventuellement contenus dans cette case */
	private final List<AiItem> items = new ArrayList<AiItem>();

	/** 
	 * renvoie la liste des blocks contenus dans cette case 
	 * (la liste peut être vide)
	 * 
	 * @return	les blocks éventuellement contenus dans cette case
	 */
	public List<AiBlock> getBlocks()
	{	return blocks;	
	}
	/** 
	 * renvoie la liste des bombes contenues dans cette case 
	 * (la liste peut être vide)
	 * 
	 * @return	les bombes éventuellement contenues dans cette case
	 */
	public List<AiBomb> getBombs()
	{	return bombs;	
	}
	/** 
	 * renvoie la liste des feux contenus dans cette case 
	 * (la liste peut être vide)
	 * 
	 * @return	les feux éventuellement contenus dans cette case
	 */
	public List<AiFire> getFires()
	{	return fires;	
	}
	/** 
	 * renvoie les sols de cette case 
	 * (il y a forcément au moins un sol)
	 * 
	 * @return	les sols contenus dans cette case
	 */
	public List<AiFloor> getFloors()
	{	return floors;	
	}
	/** 
	 * renvoie la liste des personnages contenus dans cette case 
	 * (la liste peut être vide)
	 * 
	 * @return	les personnages éventuellement contenus dans cette case
	 */
	public List<AiHero> getHeroes()
	{	return heroes;	
	}
	/** 
	 * renvoie la liste des items contenus dans cette case 
	 * (la liste peut être vide)
	 * 
	 * @return	les items éventuellement contenus dans cette case
	 */
	public List<AiItem> getItems()
	{	return items;	
	}
	
	/** 
	 * met à jour les représentations des sprites contenus dans cette case
	 */
	private void updateSprites()
	{	// block
		{	blocks.clear();
			Iterator<Block> it = tile.getBlocks().iterator();
			while(it.hasNext())
			{	Block b = it.next();
				GestureName gesture = b.getCurrentGesture().getName();
				if(!(gesture==GestureName.NONE
					|| gesture==GestureName.HIDING
					|| gesture==GestureName.ENDED))
				{	AiBlock block = zone.getBlock(b);
					if(block==null)
					{	block = new AiBlock(this,b);
						zone.addBlock(block);
					}
					block.update(this);
					blocks.add(block);
				}
			}
		}
		// bombs
		{	bombs.clear();
			Iterator<Bomb> it = tile.getBombs().iterator();
			while(it.hasNext())
			{	Bomb b = it.next();
				GestureName gesture = b.getCurrentGesture().getName();
				if(!(gesture==GestureName.NONE
					|| gesture==GestureName.HIDING
					|| gesture==GestureName.ENDED))
				{	AiBomb bomb = zone.getBomb(b);
					if(bomb==null)
					{	bomb = new AiBomb(this,b);
						zone.addBomb(bomb);
					}
					bomb.update(this);
					bombs.add(bomb);
				}
			}
		}
		// fires
		{	fires.clear();
			Iterator<Fire> it = tile.getFires().iterator();
			while(it.hasNext())
			{	Fire f = it.next();
				GestureName gesture = f.getCurrentGesture().getName();
				if(!(gesture==GestureName.NONE
					|| gesture==GestureName.HIDING
					|| gesture==GestureName.ENDED))
				{	AiFire fire = zone.getFire(f);
					if(fire==null)
					{	fire = new AiFire(this,f);
						zone.addFire(fire);
					}
					fire.update(this);
					fires.add(fire);
				}
			}
		}
		// floor
		{	floors.clear();
			Iterator<Floor> it = tile.getFloors().iterator();
			while(it.hasNext())
			{	Floor f = it.next();
				GestureName gesture = f.getCurrentGesture().getName();
				if(!(gesture==GestureName.HIDING 
					|| gesture==GestureName.ENDED))
				{	AiFloor floor = zone.getFloor(f);
					if(floor==null)
					{	floor = new AiFloor(this,f);
						zone.addFloor(floor);
					}
					floor.update(this);
					floors.add(floor);
				}
			}
		}
		// heroes
		{	heroes.clear();
			Iterator<Hero> it = tile.getHeroes().iterator();
			while(it.hasNext())
			{	Hero h = it.next();
				GestureName gesture = h.getCurrentGesture().getName();
				if(!(gesture==GestureName.HIDING 
					|| gesture==GestureName.ENDED))
				{	AiHero hero = zone.getHero(h);
					if(hero==null)
					{	hero = new AiHero(this,h);
						zone.addHero(hero);
					}
					hero.update(this);
					heroes.add(hero);
				}
			}
		}
		// item
		{	items.clear();
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
				{	AiItem item = zone.getItem(i);
					if(item==null)
					{	item = new AiItem(this,i);
						zone.addItem(item);
					}
					item.update(this);
					items.add(item);
				}
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// ABILITIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/**
	 * Teste si le sprite passé en paramètre est capable de traverser
	 * cette case. Sinon, cela signifie qu'elle contient au moins un
	 * obstacle que le personnage ne peut pas traverser. 
	 * Tous les sprites ne sont pas sensibles aux mêmes obstacles,
	 * cela dépend à la fois du type des sprites considérés (Hero,
	 * Bomb, Item, Block, etc) et des pouvoirs courants (passer à travers
	 * les murs, passer à travers les bombes, etc). Le feu peut constituer
	 * un obstacle, notamment pour les sprite de tyep Hero.</br>
	 * cf. les méthodes de même nom dans les classes filles de AiSprite
	 * 
	 *  @param sprite	le sprite qui veut traverser cette case
	 *  @return	vrai ssi ce sprite , à cet instant, peut traverser cette case
	 */
	public boolean isCrossableBy(AiSprite<?> sprite)
	{	boolean result = true;
		// murs
		if(result)
			result = isCrossableBy(sprite,blocks);
		// bombes
		if(result)
			result = isCrossableBy(sprite,bombs);
		// feu
		if(result)
			result = isCrossableBy(sprite,fires);
		// heroes
		if(result)
			result = isCrossableBy(sprite,heroes);
		// item
		if(result)
			result = isCrossableBy(sprite,items);
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
	private <T extends AiSprite<?>> boolean isCrossableBy(AiSprite<?> sprite, List<T> list)
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
	private final HashMap<Direction,AiTile> neighbors = new HashMap<Direction, AiTile>();
	
	/**
	 * initialise une fois pour toutes les voisins de la case,
	 * pour ne pas avoir à les recalculer à chaque appel de la méthode
	 * getNeighbors.
	 */
	void initNeighbors()
	{	List<Direction> directions = Direction.getPrimaryValues();
		for(Direction direction: directions)
		{	Tile neighbor = tile.getNeighbor(direction);
			int line = neighbor.getLine();
			int col = neighbor.getCol();
			AiTile aiNeighbor = getZone().getTile(line,col);
			neighbors.put(direction,aiNeighbor);
		}
	}
	
	/**
	 * renvoie le voisin de cette case passée en paramètre, situé dans la direction
	 * passée en paramètre. Attention, seulement les directions primaires sont
	 * utilisées (UP, RIGHT, DOWN, LEFT) : pas de direction composite (UPLEFT, etc.).
	 * Dans le cas contraire, la fonction renvoie null.</br>
	 * ATTENTION : les niveaux sont circulaires, ce qui signifie que le voisin
	 * d'une case située au bord du niveau est une case située sur l'autre bord.
	 * Par exemple, dans un niveau contenant width colonnes, pour une case située
	 * à la position (ligne,0), le voisin de gauche est la case située à la position
	 * (ligne,width-1). Même chose pour les bordures haut et bas.
	 * 
	 * @param direction	direction dans laquelle le voisin se trouve
	 * @return	le voisin de cette case, situé dans la direction indiquée (ou null si la direction n'est pas primaire)
	 */
	public AiTile getNeighbor(Direction direction)
	{	return neighbors.get(direction);		
	}
	
	/**
	 * renvoie la liste des voisins de cette case.
	 * Il s'agit des voisins directs situés en haut, à gauche, en bas et à droite.</br>
	 * 
	 * ATTENTION : les niveaux sont circulaires, ce qui signifie que le voisin
	 * d'une case située au bord du niveau est une case située sur l'autre bord.
	 * Par exemple, dans un niveau contenant width colonnes, pour une case située
	 * à la position (ligne,0), le voisin de gauche est la case située à la position
	 * (ligne,width-1). Même chose pour les bordures haut et bas.
	 * 
	 * @return	la liste des voisins situés en haut, à gauche, en bas et à droite de la case passée en paramètre
	 */
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
		if(o instanceof AiTile)
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
		finishSprites(blocks);
		// bombs
		finishSprites(bombs);
		// fires
		finishSprites(fires);
		// floor
		finishSprites(floors);
		// heroes
		finishSprites(heroes);
		// item
		finishSprites(items);
	}

	/**
	 * termine les représentations de sprites passées en paramètres
	 * 
	 * @param <T>	type de représentation
	 * @param list	liste de représentations
	 */
	private <T extends AiSprite<?>> void finishSprites(List<T> list)
	{	Iterator<T> it = list.iterator();
		while(it.hasNext())
		{	T temp = it.next();
			temp.finish();
		}
		list.clear();
	}
}
