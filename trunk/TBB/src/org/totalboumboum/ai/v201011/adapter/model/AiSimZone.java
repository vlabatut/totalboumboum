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
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.calculus.LevelsTools;
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
	 * construit une simulation de la zone passée en paramètre.
	 * 
	 * @param zone	zone originale de la simulation
	 */
/*	public AiSimZone(AiZone zone)
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
*/
	/**
	 * contruit une zone vide de mêmes dimensions que celle passée en paramètre
	 * 
	 * @param zone	la zone de référence
	 * @return	une nouvelle zone vide de mêmes dimensions
	 */
	protected AiSimZone(AiZone zone)
	{	// matrix and tiles
		AiTile[][] m = zone.getMatrix();
		height = zone.getHeight();
		width = zone.getWidth();
		pixelLeftX = zone.getPixelLeftX();
		pixelTopY = zone.getPixelTopY();
		pixelWidth = zone.getPixelWidth();
		pixelHeight = zone.getPixelHeight();
		matrix = new AiSimTile[height][width];
		for(int line=0;line<height;line++)
		{	for(int col=0;col<width;col++)
			{	AiTile tile = m[line][col];
				double posX = tile.getPosX();
				double posY = tile.getPosY();
				AiSimTile aiTile = new AiSimTile(this,line,col,posX,posY);
				matrix[line][col] = aiTile;
			}
		}
		
		// process heroes
		for(AiHero hero: zone.getHeroes())
		{	AiSimTile tile = null;
			AiSimState state = new AiSimState(AiStateName.ENDED,Direction.NONE,0);
			double posX = hero.getPosX();
			double posY = hero.getPosY();
			double posZ = hero.getPosZ();
			long burningDuration = hero.getBurningDuration();
			double currentSpeed = hero.getCurrentSpeed();
			int bombRange = hero.getBombRange();
			int bombNumber = hero.getBombNumber();
			int bombCount = hero.getBombCount();
			boolean throughBlocks = hero.hasThroughBlocks();
			boolean throughBombs = hero.hasThroughBombs();
			boolean throughFires = hero.hasThroughFires();
			PredefinedColor color = hero.getColor();
			double walkingSpeed = hero.getWalkingSpeed();
			AiSimHero h = new AiSimHero(tile,posX, posY, posZ, state,burningDuration,currentSpeed,
					bombRange,bombNumber,bombCount,
					throughBlocks,throughBombs,throughFires,color,walkingSpeed);
			internalHeroes.add(h);
			externalHeroes.add(h);
		}
		
		// set own hero
		AiHero oh = zone.getOwnHero();
		PredefinedColor color = oh.getColor();
		ownHero = getHeroByColor(color);
		
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
		
		// misc
		hiddenItemsCount = zone.getHiddenItemsCount();
		limitTime = zone.getLimitTime();
	}

	//NOTE temp constructor
	protected AiSimZone(int height, int width, AiSimHero hero)
	{	this.height = height;
		this.width = width;
		pixelLeftX = 0;
		pixelTopY = 0;
		pixelWidth = 100*width;
		pixelHeight = 100*height;
		matrix = new AiSimTile[height][width];
		for(int line=0;line<height;line++)
		{	for(int col=0;col<width;col++)
			{	double posX = 100*col+50;
				double posY = 100*line+50;
				AiSimTile aiTile = new AiSimTile(this,line,col,posX,posY);
				matrix[line][col] = aiTile;
			}
		}
		
		internalHeroes.add(hero);
		externalHeroes.add(hero);
		ownHero = hero;
		roundRanks.put(hero,1);
		matchRanks.put(hero,1);
		statsRanks.put(hero,1);
		
		totalTime = 0;
		elapsedTime = 0;
		limitTime = 0;

	}

	protected void update(AiZone zone, long duration)
	{	totalTime = zone.getTotalTime() + duration;
		elapsedTime = duration;
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
	@Override
	public AiSimTile getTile(int line, int col)
	{	return matrix[line][col];
	}
	
	@Override
	public AiSimTile getTile(double x, double y)
	{	int[] coord = LevelsTools.getTile(x,y,pixelLeftX,pixelTopY,height,width);
		AiSimTile result = matrix[coord[0]][coord[1]];
		return result;
	}
		
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void addSprite(AiSimSprite sprite)
	{	AiSimTile tile = sprite.getTile();

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
			PredefinedColor color = hero.getColor();
			AiSimHero hero0 = getHeroByColor(color);
			internalHeroes.remove(hero0);
			internalHeroes.add(hero);
			externalHeroes.remove(hero0);
			externalHeroes.add(hero);
			remainingHeroList.add(hero);
			if(ownHero==hero0)
				ownHero = hero;
			int roundRank = roundRanks.get(hero0);
			roundRanks.remove(hero0);
			roundRanks.put(hero,roundRank);
			int matchRank = matchRanks.get(hero0);
			matchRanks.remove(hero0);
			matchRanks.put(hero,matchRank);
			int statsRank = statsRanks.get(hero0);
			statsRanks.remove(hero0);
			statsRanks.put(hero,statsRank);
			sprite = hero;
		}
		else if(sprite instanceof AiSimItem)
		{	AiSimItem item = (AiSimItem)sprite;
			internalItems.add(item);
			externalItems.add(item);
		}
		
		tile.addSprite(sprite);
	}
	
	/////////////////////////////////////////////////////////////////
	// BLOCKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des blocks contenus dans cette zone */
	private final List<AiSimBlock> internalBlocks = new ArrayList<AiSimBlock>();
	/** liste externe des blocks contenus dans cette zone */
	private final List<AiBlock> externalBlocks = new ArrayList<AiBlock>();
	
	@Override
	public List<AiBlock> getBlocks()
	{	return externalBlocks;	
	}
	
	@Override
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
	
	@Override
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
	
	@Override
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

	@Override
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
	
	@Override
	public List<AiHero> getHeroes()
	{	return externalHeroes;	
	}
	
	@Override
	public List<AiHero> getRemainingHeroes()
	{	return remainingHeroList;	
	}
	
	@Override
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
	
	@Override
	public List<AiItem> getItems()
	{	return externalItems;	
	}
	
	@Override
	public int getHiddenItemsCount()
	{	return hiddenItemsCount;
		//TODO must be updated manually
	}
	
	/////////////////////////////////////////////////////////////////
	// OWN HERO			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le personnage contrôlé par l'IA */
	private AiSimHero ownHero;

	@Override
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
