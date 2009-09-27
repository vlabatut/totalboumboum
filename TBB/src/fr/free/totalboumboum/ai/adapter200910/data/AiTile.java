package fr.free.totalboumboum.ai.adapter200910.data;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;
import fr.free.totalboumboum.engine.content.sprite.floor.Floor;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;
import fr.free.totalboumboum.engine.content.sprite.item.Item;

/**
 * représente une case du jeu, avec tous les sprites qu'elle contient.
 * 
 * @author Vincent
 *
 */

public class AiTile
{	/** représentation de la zone à laquelle cette case appartient */
	private AiZone zone;
	/** case du jeu que cette classe représente */
	private Tile tile;
	
	/**
	 * construit une représentation de la case passée en paramètre
	 * @param tile	case représentée
	 * @param zone	zone contenant la représentation
	 */
	AiTile(Tile tile, AiZone zone)
	{	this.zone = zone;
		this.tile = tile;
		initLocation();
		updateSprites();
	}
	
	/**
	 * met à jour cette case et son contenu
	 */
	void update()
	{	updateSprites();		
	}
	
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
	// LOCATION			/////////////////////////////////////////////
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
	private void initLocation()
	{	this.line = tile.getLine();
		this.col = tile.getCol();
	}

	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste des blocks éventuellement contenus dans cette case */
	private final ArrayList<AiBlock> blocks = new ArrayList<AiBlock>();
	/** liste des bombes éventuellement contenues dans cette case */
	private final ArrayList<AiBomb> bombs = new ArrayList<AiBomb>();
	/** liste des feux éventuellement contenus dans cette case */
	private final ArrayList<AiFire> fires = new ArrayList<AiFire>();
	/** liste des sols éventuellement contenus dans cette case */
	private final ArrayList<AiFloor> floors = new ArrayList<AiFloor>();
	/** liste des personnages éventuellement contenus dans cette case */
	private final ArrayList<AiHero> heroes = new ArrayList<AiHero>();
	/** liste des items éventuellement contenus dans cette case */
	private final ArrayList<AiItem> items = new ArrayList<AiItem>();

	/** 
	 * renvoie la liste des blocks contenus dans cette case 
	 * (la liste peut être vide)
	 * 
	 * @return	les blocks éventuellement contenus dans cette case
	 */
	public Collection<AiBlock> getBlocks()
	{	Collection<AiBlock> result = Collections.unmodifiableCollection(blocks);
		return result;	
	}
	/** 
	 * renvoie la liste des bombes contenues dans cette case 
	 * (la liste peut être vide)
	 * 
	 * @return	les bombes éventuellement contenues dans cette case
	 */
	public Collection<AiBomb> getBombs()
	{	Collection<AiBomb> result = Collections.unmodifiableCollection(bombs);
		return result;	
	}
	/** 
	 * renvoie la liste des feux contenus dans cette case 
	 * (la liste peut être vide)
	 * 
	 * @return	les feux éventuellement contenus dans cette case
	 */
	public Collection<AiFire> getFires()
	{	Collection<AiFire> result = Collections.unmodifiableCollection(fires);
		return result;	
	}
	/** 
	 * renvoie les sols de cette case 
	 * (il y a forcément au moins un sol)
	 * 
	 * @return	les sols contenus dans cette case
	 */
	public Collection<AiFloor> getFloors()
	{	Collection<AiFloor> result = Collections.unmodifiableCollection(floors);
		return result;	
	}
	/** 
	 * renvoie la liste des personnages contenus dans cette case 
	 * (la liste peut être vide)
	 * 
	 * @return	les personnages éventuellement contenus dans cette case
	 */
	public Collection<AiHero> getHeroes()
	{	Collection<AiHero> result = Collections.unmodifiableCollection(heroes);
		return result;	
	}
	/** 
	 * renvoie la liste des items contenus dans cette case 
	 * (la liste peut être vide)
	 * 
	 * @return	les items éventuellement contenus dans cette case
	 */
	public Collection<AiItem> getItems()
	{	Collection<AiItem> result = Collections.unmodifiableCollection(items);
		return result;	
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
				if(!(gesture==GestureName.NONE
					|| gesture==GestureName.HIDING
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
				if(!(gesture==GestureName.NONE
					|| gesture==GestureName.HIDING
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
				if(!(gesture==GestureName.NONE
					|| gesture==GestureName.HIDING
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
	/**
	 * termine les représentations de sprites passées en paramètre
	 * @param <T>	type de représentation
	 * @param list	liste de représentations
	 */
	private <T extends AiSprite<?>> void finishSprites(ArrayList<T> list)
	{	Iterator<T> it = list.iterator();
		while(it.hasNext())
		{	T temp = it.next();
			temp.finish();
		}
		list.clear();
	}

	/////////////////////////////////////////////////////////////////
	// TILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/**
	 * renvoie le voisin de cette case passée en paramètre, situé dans la direction
	 * passée en paramètre.
	 * ATTENTION : les niveaux sont circulaires, ce qui signifie que le voisin
	 * d'une case située au bord du niveau est une case située sur l'autre bord.
	 * Par exemple, dans un niveau contenant width colonnes, pour une case située
	 * à la position (ligne,0), le voisin de gauche est la case située à la position
	 * (ligne,width-1). Même chose pour les bordures haut et bas.
	 * 
	 * @param direction	direction dans laquelle le voisin se trouve
	 * @return	le voisin de cette case, situé dans la direction indiquée
	 */
	public AiTile getNeighbor(Direction direction)
	{	AiTile result;
		result = zone.getNeighborTile(this,direction);
		return result;
	}
	
}
