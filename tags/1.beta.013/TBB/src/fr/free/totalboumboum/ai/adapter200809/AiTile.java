package fr.free.totalboumboum.ai.adapter200809;

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
 * repr�sente une case du jeu, avec tous les sprites qu'elle contient.
 * 
 * @author Vincent
 *
 */

public class AiTile
{	/** repr�sentation de la zone � laquelle cette case appartient */
	private AiZone zone;
	/** case du jeu que cette classe repr�sente */
	private Tile tile;
	
	/**
	 * construit une repr�sentation de la case pass�e en param�tre
	 * @param tile	case repr�sent�e
	 * @param zone	zone contenant la repr�sentation
	 */
	AiTile(Tile tile, AiZone zone)
	{	this.zone = zone;
		this.tile = tile;
		initLocation();
		updateSprites();
	}
	
	/**
	 * met � jour cette case et son contenu
	 */
	void update()
	{	updateSprites();		
	}
	
	/**
	 * termine proprement cette case
	 */
	void finish()
	{	// block
		if(block!=null)
		{	block.finish();	
			block = null;
		}
		// bombs
		finishSprites(bombs);
		// fires
		finishSprites(fires);
		// floor
		if(floor!=null)
		{	floor.finish();	
			floor = null;
		}
		// heroes
		finishSprites(heroes);
		// item
		if(item!=null)
		{	item.finish();	
			item = null;
		}	
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
	private void initLocation()
	{	this.line = tile.getLine();
		this.col = tile.getCol();
	}

	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** block �ventuellement contenu dans cette case */
	private AiBlock block = null;
	/** liste des bombes �ventuellement contenues dans cette case */
	private final ArrayList<AiBomb> bombs = new ArrayList<AiBomb>();
	/** liste des feux �ventuellement contenus dans cette case */
	private final ArrayList<AiFire> fires = new ArrayList<AiFire>();
	/** sol de cette case */
	private AiFloor floor = null;
	/** liste des personnages �ventuellement contenus dans cette case */
	private final ArrayList<AiHero> heroes = new ArrayList<AiHero>();
	/** item �ventuellement contenu dans cette case */
	private AiItem item = null;

	/** 
	 * renvoie le block contenu dans cette case 
	 * ou null s'il n'y a pas de block dans cette case
	 * 
	 * @return	le bloc �ventuellement contenu dans cette case
	 */
	public AiBlock getBlock()
	{	return block;	
	}
	/** 
	 * renvoie la liste des bombes contenues dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	les bombes �ventuellement contenues dans cette case
	 */
	public Collection<AiBomb> getBombs()
	{	Collection<AiBomb> result = Collections.unmodifiableCollection(bombs);
		return result;	
	}
	/** 
	 * renvoie la liste des feux contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	les feux �ventuellement contenus dans cette case
	 */
	public Collection<AiFire> getFires()
	{	Collection<AiFire> result = Collections.unmodifiableCollection(fires);
		return result;	
	}
	/** 
	 * renvoie le sol de cette case 
	 * (il y a forc�ment un sol)
	 * 
	 * @return	le sol contenu dans cette case
	 */
	public AiFloor getFloor()
	{	return floor;	
	}
	/** 
	 * renvoie la liste des personnages contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	les personnages �ventuellement contenus dans cette case
	 */
	public Collection<AiHero> getHeroes()
	{	Collection<AiHero> result = Collections.unmodifiableCollection(heroes);
		return result;	
	}
	/** 
	 * renvoie l'item (apparent) contenu dans cette case 
	 * ou null s'il n'y a pas d'item apparent dans cette case
	 * 
	 * @return	l'item �ventuellement contenu dans cette case
	 */
	public AiItem getItem()
	{	return item;	
	}
	
	/** 
	 * met � jour les repr�sentations des sprites contenus dans cette case
	 */
	private void updateSprites()
	{	// block
		{	Block b = tile.getBlock();
			if(b!=null)
			{	GestureName gesture = b.getCurrentGesture().getName();
				if(!(gesture==GestureName.NONE 
					|| gesture==GestureName.HIDING
					|| gesture==GestureName.ENDED))
				{	block = zone.getBlock(b);
					if(block==null)
					{	block = new AiBlock(this,b);
						zone.addBlock(block);
					}
					block.update(this);
				}
			}
			else
				block = null;
		}
		// bombs
		{	bombs.clear();
			Iterator<Bomb> i = tile.getBombs().iterator();
			while(i.hasNext())
			{	Bomb b = i.next();
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
			Iterator<Fire> i = tile.getFires().iterator();
			while(i.hasNext())
			{	Fire f = i.next();
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
		{	Floor f = tile.getFloor();
			if(f!=null)
			{	GestureName gesture = f.getCurrentGesture().getName();
				if(!(gesture==GestureName.NONE
					|| gesture==GestureName.HIDING
					|| gesture==GestureName.ENDED))
				{	floor = zone.getFloor(f);
					if(floor==null)
					{	floor = new AiFloor(this,f);
						zone.addFloor(floor);
					}
					floor.update(this);
				}
			}
			else
				floor = null;
		}
		// heroes
		{	heroes.clear();
			Iterator<Hero> i = tile.getHeroes().iterator();
			while(i.hasNext())
			{	Hero h = i.next();
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
		{	Item i = tile.getItem();
			if(i!=null)
			{	GestureName gesture = i.getCurrentGesture().getName();
				if(!(gesture==GestureName.NONE
					|| gesture==GestureName.HIDING
					|| gesture==GestureName.ENDED))
				{	item = zone.getItem(i);
					if(item==null)
					{	item = new AiItem(this,i);
						zone.addItem(item);
					}
					item.update(this);
				}
			}
			else
				item = null;
		}
	}
	/**
	 * termine les repr�sentations de sprites pass�es en param�tre
	 * @param <T>	type de repr�sentation
	 * @param list	liste de repr�sentations
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
	 * renvoie le voisin de cette case pass�e en param�tre, situ� dans la direction
	 * pass�e en param�tre.
	 * ATTENTION : les niveaux sont circulaires, ce qui signifie que le voisin
	 * d'une case situ�e au bord du niveau est une case situ�e sur l'autre bord.
	 * Par exemple, dans un niveau contenant width colonnes, pour une case situ�e
	 * � la position (ligne,0), le voisin de gauche est la case situ�e � la position
	 * (ligne,width-1). M�me chose pour les bordures haut et bas.
	 * 
	 * @param direction	direction dans laquelle le voisin se trouve
	 * @return	le voisin de cette case, situ� dans la direction indiqu�e
	 */
	public AiTile getNeighbor(Direction direction)
	{	AiTile result;
		result = zone.getNeighborTile(this,direction);
		return result;
	}
	
}
