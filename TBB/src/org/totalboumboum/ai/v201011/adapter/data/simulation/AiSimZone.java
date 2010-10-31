package org.totalboumboum.ai.v201011.adapter.data.simulation;

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
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * simule la zone de jeu et tous ces constituants : cases et sprites.
 * Il s'agit de la classe principale pour la simulation de l'évolution du jeu.</br>
 * 
 * L'ensemble des objets représente un état du jeu et ne peut (volontairement) pas être modifié.
 * 
 * @author Vincent Labatut
 *
 */
public final class AiSimZone extends AiZone
{	
	/**
	 * construit une simulation du niveau passé en paramètre,
	 * du point de vue du joueur passé en paramètre.
	 * 
	 * @param zone	zone d'origine de la simulation
	 */
	public AiSimZone(AiZone zone)
	{	// init matrix, tiles and lists
		AiTile[][] m = zone.getMatrix();
		height = zone.getHeight();
		width = zone.getWidth();
		matrix = new AiSimTile[height][width];
		for(int lineIndex=0;lineIndex<height;lineIndex++)
		{	for(int colIndex=0;colIndex<width;colIndex++)
			{	// tile
				AiTile tile = m[lineIndex][colIndex];
				AiSimTile aiTile = new AiSimTile(tile,this);
				matrix[lineIndex][colIndex] = aiTile;
				// blocks
				List<AiSimBlock> blocks = aiTile.getInternalBlocks();
				internalBlocks.addAll(blocks);
				externalBlocks.addAll(blocks);
				// bombs
				List<AiSimBomb> bombs = aiTile.getInternalBombs();
				internalBombs.addAll(bombs);
				externalBombs.addAll(bombs);
				// fires
				List<AiSimFire> fires = aiTile.getInternalFires();
				internalFires.addAll(fires);
				externalFires.addAll(fires);
				// floors
				List<AiSimFloor> floors = aiTile.getInternalFloors();
				internalFloors.addAll(floors);
				externalFloors.addAll(floors);
				// heroes
				List<AiSimHero> heroes = aiTile.getInternalHeroes();
				internalHeroes.addAll(heroes);
				externalHeroes.addAll(heroes);
				// items
				List<AiSimItem> items = aiTile.getInternalItems();
				internalItems.addAll(items);
				externalItems.addAll(items);
			}
		}
		
		// set own hero
		AiHero oh = zone.getOwnHero();
		PredefinedColor color = oh.getColor();
		ownHero = getHeroByColor(color);
		
		// set time
		totalTime = zone.getTotalTime();	//TODO should be updated (?)
		elapsedTime = zone.getElapsedTime();
		limitTime = zone.getLimitTime();
		
		// set meta-data
		for(AiHero hero: zone.getHeroes())
		{	color = hero.getColor();
			AiSimHero aiHero = getHeroByColor(color);
			int roundRank = zone.getRoundRank(hero);
			roundRanks.put(aiHero,roundRank);
			int matchRank = zone.getMatchRank(hero);
			matchRanks.put(aiHero,matchRank);
			int statsRank = zone.getStatsRank(hero);
			statsRanks.put(aiHero,statsRank);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// MATRIX			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** matrice représentant la zone et tous les sprites qu'elle contient */
	private AiSimTile[][] matrix;
	
	@Override
	public AiSimTile[][] getMatrix()
	{	return matrix;
	}
	
	/** 
	 * renvoie la case voisine de la case passée en paramètre,
	 * dans la direction spécifiée (en considérant le fait que le niveau
	 * est fermé.
	 *  
	 *  @param line	ligne de la case à traite
	 *  @param col	colonne de la case à traiter
	 *  @param direction	direction de la case voisine relativement à la case de référence
	 *  @return	la case voisine dans la direction précisée
	 */
	protected AiSimTile getNeighborTile(int line, int col, Direction direction)
	{	AiSimTile result;
		int c,l;
		Direction p[] = direction.getPrimaries(); 

		if(p[0]==Direction.LEFT)
			c = (col+width-1)%width;
		else if(p[0]==Direction.RIGHT)
			c = (col+1)%width;
		else
			c = col;

		if(p[1]==Direction.UP)
			l = (line+height-1)%height;
		else if(p[1]==Direction.DOWN)
			l = (line+1)%height;
		else
			l = line;

		result = matrix[l][c];
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public AiSimTile getTile(int line, int col)
	{	return matrix[line][col];
	}
	
	public AiSimTile getTile(double x, double y)
	{	Tile tile = RoundVariables.level.getTile(x,y);
		int line = tile.getLine();
		int col = tile.getCol();
		AiSimTile result = matrix[line][col];
		return result;
	}
		
	/////////////////////////////////////////////////////////////////
	// BLOCKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des blocks contenus dans cette zone */
	private final List<AiSimBlock> internalBlocks = new ArrayList<AiSimBlock>();
	/** liste externe des blocks contenus dans cette zone */
	private final List<AiBlock> externalBlocks = new ArrayList<AiBlock>();
	
	public List<AiBlock> getBlocks()
	{	return externalBlocks;	
	}
	
	public List<AiBlock> getDestructibleBlocks()
	{	List<AiBlock> result = new ArrayList<AiBlock>();

		for(AiBlock block: internalBlocks)
		{	if(block.isDestructible())
				result.add(block);
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des bombes contenues dans cette zone */
	private final List<AiSimBomb> internalBombs = new ArrayList<AiSimBomb>();
	/** liste externe des bombes contenues dans cette zone */
	private final List<AiBomb> externalBombs = new ArrayList<AiBomb>();
	
	public List<AiBomb> getBombs()
	{	return externalBombs;	
	}
		
	/////////////////////////////////////////////////////////////////
	// FIRES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des feux contenus dans cette zone */
	private final List<AiSimFire> internalFires = new ArrayList<AiSimFire>();
	/** liste externe des feux contenus dans cette zone */
	private final List<AiFire> externalFires = new ArrayList<AiFire>();
	
	public List<AiFire> getFires()
	{	return externalFires;	
	}
		
	/////////////////////////////////////////////////////////////////
	// FLOORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des sols contenus dans cette zone */
	private final List<AiSimFloor> internalFloors = new ArrayList<AiSimFloor>();
	/** liste externe des sols contenus dans cette zone */
	private final List<AiFloor> externalFloors = new ArrayList<AiFloor>();

	public List<AiFloor> getFloors()
	{	return externalFloors;	
	}
	
	/////////////////////////////////////////////////////////////////
	// HEROES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne de tous les personnages contenus dans cette zone */
	private final List<AiSimHero> internalHeroes = new ArrayList<AiSimHero>();
	/** liste externe de tous les personnages contenus dans cette zone */
	private final List<AiHero> externalHeroes = new ArrayList<AiHero>();
	/** liste externe des personnages restant encore dans cette zone */
	private final List<AiHero> remainingHeroList = new ArrayList<AiHero>();
	
	public List<AiHero> getHeroes()
	{	return externalHeroes;	
	}
	
	public List<AiHero> getRemainingHeroes()
	{	return remainingHeroList;	
	}
	
	public List<AiHero> getRemainingOpponents()
	{	List<AiHero> result = new ArrayList<AiHero>(remainingHeroList);
		result.remove(ownHero);
		return result;	
	}
	
	/**
	 * retrouve l'objet simulant un personnage grâce à sa couleur
	 * 
	 * @param color	couleur du personnage à retrouver
	 * @return	le personnage correspondant à la couleur spécifiée
	 */
	private AiSimHero getHeroByColor(PredefinedColor color)
	{	AiSimHero result = null;
		Iterator<AiSimHero> it = internalHeroes.iterator();
		while(result==null && it.hasNext())
		{	AiSimHero htemp = it.next();
			if(htemp.getColor()==color)
				result = htemp;
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des items contenus dans cette zone */
	private final List<AiSimItem> internalItems = new ArrayList<AiSimItem>();
	/** liste externe des items contenus dans cette zone */
	private final List<AiItem> externalItems = new ArrayList<AiItem>();
	/** nombre d'items cachés, i.e. pas encore ramassés */
	private int hiddenItemsCount;
	
	public List<AiItem> getItems()
	{	return externalItems;	
	}
	
	public int getHiddenItemsCount()
	{	return hiddenItemsCount;
		//TODO must be updated manually
	}
	
	/////////////////////////////////////////////////////////////////
	// OWN HERO			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le personnage contrôlé par l'IA */
	private AiSimHero ownHero;

	public AiSimHero getOwnHero()
	{	return ownHero;	
	}

	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(Object o)
	{	boolean result = false;
		if(o instanceof AiSimZone)
		{	
//			AiZone zone = (AiZone)o;	
//			result = level==zone.level && player==zone.player;
			result = this==o;
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine proprement cette simulation (une fois que l'IA n'en a plus besoin).
	 */
	public void finish()
	{	// matrix
		for(int line=0;line<height;line++)
			for(int col=0;col<width;col++)
				matrix[line][col].finish();
		matrix = null;
		
		// blocks
		internalBlocks.clear();
		externalBlocks.clear();

		// bombs
		internalBombs.clear();
		externalBombs.clear();
		
		// fires
		internalFires.clear();
		externalFires.clear();
		
		// floors
		internalFloors.clear();
		externalFloors.clear();
		
		// heroes
		internalHeroes.clear();
		externalHeroes.clear();
		ownHero = null;
		
		// items
		internalItems.clear();
		externalItems.clear();
	}
}
