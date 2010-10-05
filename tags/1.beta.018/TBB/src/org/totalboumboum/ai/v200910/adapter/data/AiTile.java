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
 * repr�sente une case du jeu, avec tous les sprites qu'elle contient.
 * 
 * @author Vincent Labatut
 *
 */

public class AiTile
{	/**
	 * construit une repr�sentation de la case pass�e en param�tre
	 * @param tile	case repr�sent�e
	 * @param zone	zone contenant la repr�sentation
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
	 * met � jour cette case et son contenu
	 */
	void update()
	{	updateSprites();		
	}
	
	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** repr�sentation de la zone � laquelle cette case appartient */
	private AiZone zone;
	
	public AiZone getZone()
	{	return zone;	
	}
	
	/////////////////////////////////////////////////////////////////
	// ENGINE TILE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** case du jeu que cette classe repr�sente */
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
	 * renvoie le num�ro de la ligne contenant cette case
	 * 
	 * @return	la ligne de cette case
	 */
	public int getLine()
	{	return line;	
	}
	/** 
	 * renvoie le num�ro de la colonne contenant cette case
	 *  
	 * @return	la colonne de cette case
	 */
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
		
	/** 
	 * renvoie l'abscisse de la case en pixels
	 * 
	 * @return	l'abscisse de cette case
	 */
	public double getPosX()
	{	return posX;	
	}
	
	/** 
	 * renvoie l'ordonn�e de la case en pixels
	 * 
	 * @return	l'ordonn�e de cette case
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
	/** longueur d'un c�t� de la case en pixels */
	private double size;
		
	/** 
	 * renvoie la taille de la case en pixels
	 * 
	 * @return	longueur d'un c�t� de la case en pixels
	 */
	public double getSize()
	{	return size;	
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste des blocks �ventuellement contenus dans cette case */
	private final List<AiBlock> blocks = new ArrayList<AiBlock>();
	/** liste des bombes �ventuellement contenues dans cette case */
	private final List<AiBomb> bombs = new ArrayList<AiBomb>();
	/** liste des feux �ventuellement contenus dans cette case */
	private final List<AiFire> fires = new ArrayList<AiFire>();
	/** liste des sols �ventuellement contenus dans cette case */
	private final List<AiFloor> floors = new ArrayList<AiFloor>();
	/** liste des personnages �ventuellement contenus dans cette case */
	private final List<AiHero> heroes = new ArrayList<AiHero>();
	/** liste des items �ventuellement contenus dans cette case */
	private final List<AiItem> items = new ArrayList<AiItem>();

	/** 
	 * renvoie la liste des blocks contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	les blocks �ventuellement contenus dans cette case
	 */
	public List<AiBlock> getBlocks()
	{	return blocks;	
	}
	/** 
	 * renvoie la liste des bombes contenues dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	les bombes �ventuellement contenues dans cette case
	 */
	public List<AiBomb> getBombs()
	{	return bombs;	
	}
	/** 
	 * renvoie la liste des feux contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	les feux �ventuellement contenus dans cette case
	 */
	public List<AiFire> getFires()
	{	return fires;	
	}
	/** 
	 * renvoie les sols de cette case 
	 * (il y a forc�ment au moins un sol)
	 * 
	 * @return	les sols contenus dans cette case
	 */
	public List<AiFloor> getFloors()
	{	return floors;	
	}
	/** 
	 * renvoie la liste des personnages contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	les personnages �ventuellement contenus dans cette case
	 */
	public List<AiHero> getHeroes()
	{	return heroes;	
	}
	/** 
	 * renvoie la liste des items contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	les items �ventuellement contenus dans cette case
	 */
	public List<AiItem> getItems()
	{	return items;	
	}
	
	/** 
	 * met � jour les repr�sentations des sprites contenus dans cette case
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
	 * Teste si le sprite pass� en param�tre est capable de traverser
	 * cette case. Sinon, cela signifie qu'elle contient au moins un
	 * obstacle que le personnage ne peut pas traverser. 
	 * Tous les sprites ne sont pas sensibles aux m�mes obstacles,
	 * cela d�pend � la fois du type des sprites consid�r�s (Hero,
	 * Bomb, Item, Block, etc) et des pouvoirs courants (passer � travers
	 * les murs, passer � travers les bombes, etc). Le feu peut constituer
	 * un obstacle, notamment pour les sprite de tyep Hero.</br>
	 * cf. les m�thodes de m�me nom dans les classes filles de AiSprite
	 * 
	 *  @param sprite	le sprite qui veut traverser cette case
	 *  @return	vrai ssi ce sprite , � cet instant, peut traverser cette case
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
	 * fonction auxiliaire utilis�e pour d�terminer si cette
	 * case est traversable par le sprite pass� en param�tre.
	 * (cette fonction r�alise le traitement relativement � 
	 * la liste de sprite pass�e en param�tre)
	 * 
	 * @param sprite	le sprite qui veut traverser cette case
	 * @param list	les sprites de cette case � tester
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
	 * pour ne pas avoir � les recalculer � chaque appel de la m�thode
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
	 * renvoie le voisin de cette case pass�e en param�tre, situ� dans la direction
	 * pass�e en param�tre. Attention, seulement les directions primaires sont
	 * utilis�es (UP, RIGHT, DOWN, LEFT) : pas de direction composite (UPLEFT, etc.).
	 * Dans le cas contraire, la fonction renvoie null.</br>
	 * ATTENTION : les niveaux sont circulaires, ce qui signifie que le voisin
	 * d'une case situ�e au bord du niveau est une case situ�e sur l'autre bord.
	 * Par exemple, dans un niveau contenant width colonnes, pour une case situ�e
	 * � la position (ligne,0), le voisin de gauche est la case situ�e � la position
	 * (ligne,width-1). M�me chose pour les bordures haut et bas.
	 * 
	 * @param direction	direction dans laquelle le voisin se trouve
	 * @return	le voisin de cette case, situ� dans la direction indiqu�e (ou null si la direction n'est pas primaire)
	 */
	public AiTile getNeighbor(Direction direction)
	{	return neighbors.get(direction);		
	}
	
	/**
	 * renvoie la liste des voisins de cette case.
	 * Il s'agit des voisins directs situ�s en haut, � gauche, en bas et � droite.</br>
	 * 
	 * ATTENTION : les niveaux sont circulaires, ce qui signifie que le voisin
	 * d'une case situ�e au bord du niveau est une case situ�e sur l'autre bord.
	 * Par exemple, dans un niveau contenant width colonnes, pour une case situ�e
	 * � la position (ligne,0), le voisin de gauche est la case situ�e � la position
	 * (ligne,width-1). M�me chose pour les bordures haut et bas.
	 * 
	 * @return	la liste des voisins situ�s en haut, � gauche, en bas et � droite de la case pass�e en param�tre
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
	 * termine les repr�sentations de sprites pass�es en param�tres
	 * 
	 * @param <T>	type de repr�sentation
	 * @param list	liste de repr�sentations
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
