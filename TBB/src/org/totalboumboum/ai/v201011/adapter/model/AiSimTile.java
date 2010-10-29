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
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.game.round.RoundVariables;

/**
 * simule une case du jeu, avec tous les sprites qu'elle contient.
 * 
 * @author Vincent Labatut
 *
 */
public class AiSimTile
{	
	/**
	 * construit une simulation de case � partir des coordonn�es pass�es en param�tres
	 * 
	 * @param zone	zone contenant la simulation
	 * @param line	ligne de la case
	 * @param col	colonne de la case
	 * @param posX	abscisse de la case
	 * @param posY	ordonn�e de la case
	 */
	public AiSimTile(AiSimZone zone, int line, int col, double posX, double posY)
	{	this.zone = zone;

		size = RoundVariables.scaledTileDimension;

		// location
		this.line = line;
		this.col = col;
		this.posX = posX;
		this.posY = posY;
	}

	/**
	 * construit une simulation de la case pass�e en param�tre
	 * 
	 * @param tile	case simul�e
	 * @param zone	zone contenant la simulation
	 */
	AiSimTile(AiTile tile, AiSimZone zone)
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
			blocks.add(s2);
		}
		for(AiBomb s1: tile.getBombs())
		{	AiSimBomb s2 = new AiSimBomb(s1,this);
			bombs.add(s2);
		}
		for(AiFire s1: tile.getFires())
		{	AiSimFire s2 = new AiSimFire(s1,this);
			fires.add(s2);
		}
		for(AiFloor s1: tile.getFloors())
		{	AiSimFloor s2 = new AiSimFloor(s1,this);
			floors.add(s2);
		}
		for(AiHero s1: tile.getHeroes())
		{	AiSimHero s2 = new AiSimHero(s1,this);
			heroes.add(s2);
		}
		for(AiItem s1: tile.getItems())
		{	AiSimItem s2 = new AiSimItem(s1,this);
			items.add(s2);
		}
	}
	
	/**
	 * construit une simulation de case pass�e en param�tre
	 * 
	 * @param tile	case � copier
	 * @param zone	zone contenant la simulation
	 */
	public AiSimTile(AiSimTile tile, AiSimZone zone)
	{	this.zone = zone;

		size = RoundVariables.scaledTileDimension;

		// location
		line = tile.line;
		col = tile.col;
		posX = tile.posX;
		posY = tile.posY;
		
		// sprites
		for(AiSimBlock s1: tile.getBlocks())
		{	AiSimBlock s2 = new AiSimBlock(s1,this);
			blocks.add(s2);
		}
		for(AiSimBomb s1: tile.getBombs())
		{	AiSimBomb s2 = new AiSimBomb(s1,this);
			bombs.add(s2);
		}
		for(AiSimFire s1: tile.getFires())
		{	AiSimFire s2 = new AiSimFire(s1,this);
			fires.add(s2);
		}
		for(AiSimFloor s1: tile.getFloors())
		{	AiSimFloor s2 = new AiSimFloor(s1,this);
			floors.add(s2);
		}
		for(AiSimHero s1: tile.getHeroes())
		{	AiSimHero s2 = new AiSimHero(s1,this);
			heroes.add(s2);
		}
		for(AiSimItem s1: tile.getItems())
		{	AiSimItem s2 = new AiSimItem(s1,this);
			items.add(s2);
		}
	}

	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** simulation de la zone � laquelle cette case appartient */
	private AiSimZone zone;
	
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
	private final List<AiSimBlock> blocks = new ArrayList<AiSimBlock>();
	/** liste des bombes �ventuellement contenues dans cette case */
	private final List<AiSimBomb> bombs = new ArrayList<AiSimBomb>();
	/** liste des feux �ventuellement contenus dans cette case */
	private final List<AiSimFire> fires = new ArrayList<AiSimFire>();
	/** liste des sols �ventuellement contenus dans cette case */
	private final List<AiSimFloor> floors = new ArrayList<AiSimFloor>();
	/** liste des personnages �ventuellement contenus dans cette case */
	private final List<AiSimHero> heroes = new ArrayList<AiSimHero>();
	/** liste des items �ventuellement contenus dans cette case */
	private final List<AiSimItem> items = new ArrayList<AiSimItem>();

	/** 
	 * renvoie la liste des blocks contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	les blocks �ventuellement contenus dans cette case
	 */
	public List<AiSimBlock> getBlocks()
	{	return blocks;	
	}
	/** 
	 * renvoie la liste des bombes contenues dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	les bombes �ventuellement contenues dans cette case
	 */
	public List<AiSimBomb> getBombs()
	{	return bombs;	
	}
	/** 
	 * renvoie la liste des feux contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	les feux �ventuellement contenus dans cette case
	 */
	public List<AiSimFire> getFires()
	{	return fires;	
	}
	/** 
	 * renvoie les sols de cette case 
	 * (il y a forc�ment au moins un sol)
	 * 
	 * @return	les sols contenus dans cette case
	 */
	public List<AiSimFloor> getFloors()
	{	return floors;	
	}
	/** 
	 * renvoie la liste des personnages contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	les personnages �ventuellement contenus dans cette case
	 */
	public List<AiSimHero> getHeroes()
	{	return heroes;	
	}
	/** 
	 * renvoie la liste des items contenus dans cette case 
	 * (la liste peut �tre vide)
	 * 
	 * @return	les items �ventuellement contenus dans cette case
	 */
	public List<AiSimItem> getItems()
	{	return items;	
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
	 * un obstacle, notamment pour les sprite de type Hero.</br>
	 * cf. les m�thodes de m�me nom dans les classes filles de AiSprite
	 * 
	 *  @param sprite	le sprite qui veut traverser cette case
	 *  @return	vrai ssi ce sprite , � cet instant, peut traverser cette case
	 */
	public boolean isCrossableBy(AiSimSprite sprite)
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
	private <T extends AiSimSprite> boolean isCrossableBy(AiSimSprite sprite, List<T> list)
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
	public AiSimTile getNeighbor(Direction direction)
	{	AiSimTile result = zone.getNeighborTile(line,col,direction);
		return result;		
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
	public List<AiSimTile> getNeighbors()
	{	List<AiSimTile> result = new ArrayList<AiSimTile>();
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
	 * termine les simulations de sprites pass�es en param�tres
	 * 
	 * @param <T>	type de simulation
	 * @param list	liste de simulations
	 */
	private <T extends AiSimSprite> void finishSprites(List<T> list)
	{	Iterator<T> it = list.iterator();
		while(it.hasNext())
		{	T temp = it.next();
			temp.finish();
		}
		list.clear();
	}
}
