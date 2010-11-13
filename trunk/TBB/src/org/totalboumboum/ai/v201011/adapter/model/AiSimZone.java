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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiFloor;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiItemType;
import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
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
final class AiSimZone extends AiZone
{	
	/**
	 * contruit une zone qui est une copie de celle passée en paramètre.
	 * si la paramètre fullCopy est faux, les sprites ne sont pas copiés,
	 * sinon tout est copié.
	 * 
	 * @param 
	 * 		zone	la zone de référence
	 * @param 
	 * 		fullCopy	indique si les sprites doivent aussi être copiés ou pas
	 * @return	
	 * 		une nouvelle zone vide de mêmes dimensions
	 */
	protected AiSimZone(AiZone zone, boolean fullCopy)
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
		
		if(fullCopy)
		{	for(int line=0;line<height;line++)
			{	for(int col=0;col<width;col++)
				{	AiTile tile = zone.getTile(line,col);
					AiSimTile simTile = matrix[line][col];
					// blocks
					List<AiBlock> blocks = tile.getBlocks();
					for(AiBlock block: blocks)
					{	AiSimBlock simBlock = new AiSimBlock(block,simTile);
						addSprite(simBlock);
					}
					// bombs
					List<AiBomb> bombs = tile.getBombs();
					for(AiBomb bomb: bombs)
					{	AiSimBomb simBomb = new AiSimBomb(bomb,simTile);
						addSprite(simBomb);
					}
					// fires
					List<AiFire> fires = tile.getFires();
					for(AiFire fire: fires)
					{	AiSimFire simFire = new AiSimFire(fire,simTile);
						addSprite(simFire);
					}
					// floors
					List<AiFloor> floors = tile.getFloors();
					for(AiFloor floor: floors)
					{	AiSimFloor simFloor = new AiSimFloor(floor,simTile);
						addSprite(simFloor);
					}
					// heroes
					List<AiHero> heroes = tile.getHeroes();
					for(AiHero hero: heroes)
					{	AiSimHero simHero = new AiSimHero(hero,simTile);
						addSprite(simHero);
					}
					// items
					List<AiItem> items = tile.getItems();
					for(AiItem item: items)
					{	AiSimItem simItem = new AiSimItem(item,simTile);
						addSprite(simItem);
					}
				}
			}
		}
		else
		{	// process heroes
			for(AiHero hero: zone.getHeroes())
			{	AiSimTile tile = null;
				AiSimState state = new AiSimState(AiStateName.ENDED,Direction.NONE,0);
				double posX = hero.getPosX();
				double posY = hero.getPosY();
				double posZ = hero.getPosZ();
				long burningDuration = hero.getBurningDuration();
				double currentSpeed = hero.getCurrentSpeed();
				AiBomb bombPrototype = hero.getBombPrototype();
				int bombNumber = hero.getBombNumber();
				int bombCount = hero.getBombCount();
				boolean throughBlocks = hero.hasThroughBlocks();
				boolean throughBombs = hero.hasThroughBombs();
				boolean throughFires = hero.hasThroughFires();
				PredefinedColor color = hero.getColor();
				double walkingSpeed = hero.getWalkingSpeed();
				int id = hero.getId();
				AiSimHero h = new AiSimHero(id,tile,posX, posY, posZ, state,burningDuration,currentSpeed,
						bombPrototype,bombNumber,bombCount,
						throughBlocks,throughBombs,throughFires,color,walkingSpeed);
				internalHeroes.add(h);
				externalHeroes.add(h);
			}
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
		for(AiItemType type: AiItemType.values())
		{	int value = zone.getHiddenItemsCount(type);
			hiddenItemsCounts.put(type,value);
		}
		totalTime = zone.getTotalTime();
		limitTime = zone.getLimitTime();
		elapsedTime = zone.getElapsedTime();
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
	 *  @param 
	 *  	line	ligne de la case à traite
	 *  @param 
	 *  	col	colonne de la case à traiter
	 *  @param 
	 *  	direction	direction de la case voisine relativement à la case de référence
	 *  @return	
	 *  	la case voisine dans la direction précisée
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
	/**
	 * permet de rajouter un sprite dans cette zone
	 * remarque : le sprite a obligatoirement déjà été affecté à une case 
	 * lors de sa construction, donc il s'agit ici simplement de mettre
	 * à jour les listes de sprites de la zone
	 * 
	 *  @param
	 *  	sprite	le sprite à rajouter à cette zone
	 */
	protected void addSprite(AiSimSprite sprite)
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
	
	/**
	 * renvoie la simulation de sprite de même numéro (id)
	 * que celui passé en paramètre. Cette méthode permet
	 * de suivre le même sprite à travers différents états
	 * de la simulation, dans lesquels il est représenté
	 * par des objets différents.
	 * 
	 * @param 
	 * 		sprite	le sprite ciblé
	 * @return	
	 * 		sa représentation dans cette zone
	 */
	protected AiSimSprite getSpriteById(AiSprite sprite)
	{	AiSimSprite result = null;
		if(sprite instanceof AiBlock)
		{	AiBlock temp = (AiBlock)sprite;
			result = getSpriteById(temp);
		}
		else if(sprite instanceof AiBomb)
		{	AiBomb temp = (AiBomb)sprite;
			result = getSpriteById(temp);
		}
		else if(sprite instanceof AiFire)
		{	AiFire temp = (AiFire)sprite;
			result = getSpriteById(temp);
		}
		else if(sprite instanceof AiFloor)
		{	AiFloor temp = (AiFloor)sprite;
			result = getSpriteById(temp);
		}
		else if(sprite instanceof AiHero)
		{	AiHero temp = (AiHero)sprite;
			result = getSpriteById(temp);
		}
		else if(sprite instanceof AiItem)
		{	AiItem temp = (AiItem)sprite;
			result = getSpriteById(temp);
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// BLOCKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des blocks contenus dans cette zone */
	private final List<AiSimBlock> internalBlocks = new ArrayList<AiSimBlock>();
	/** liste externe des blocks contenus dans cette zone */
	private final List<AiBlock> externalBlocks = new ArrayList<AiBlock>();
	
	/**
	 * renvoie la liste interne de blocs
	 * 
	 * @return	
	 * 		liste interne de blocs
	 */
	protected List<AiSimBlock> getInternalBlocks()
	{	return internalBlocks;	
	}
		
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
	
	/**
	 * renvoie la simulation de sprite de même numéro (id)
	 * que celui passé en paramètre. Cette méthode permet
	 * de suivre le même sprite à travers différents états
	 * de la simulation, dans lesquels il est représenté
	 * par des objets différents.
	 * 
	 * @param 
	 * 		sprite	le sprite ciblé
	 * @return	
	 * 		sa représentation dans cette zone
	 */
	protected AiSimBlock getSpriteById(AiBlock sprite)
	{	AiSimBlock result = null;
		int id = sprite.getId();
		Iterator<AiSimBlock> it = internalBlocks.iterator();
		while(result==null)
		{	AiSimBlock temp = it.next();
			if(temp.getId()==id)
				result = temp;
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

	/**
	 * renvoie la liste interne de bombes
	 * 
	 * @return	
	 * 		liste interne de bombes
	 */
	protected List<AiSimBomb> getInternalBombs()
	{	return internalBombs;	
	}
		
	@Override
	public List<AiBomb> getBombs()
	{	return externalBombs;	
	}
	
	/**
	 * renvoie la simulation de sprite de même numéro (id)
	 * que celui passé en paramètre. Cette méthode permet
	 * de suivre le même sprite à travers différents états
	 * de la simulation, dans lesquels il est représenté
	 * par des objets différents.
	 * 
	 * @param 
	 * 		sprite	le sprite ciblé
	 * @return	
	 * 		sa représentation dans cette zone
	 */
	protected AiSimBomb getSpriteById(AiBomb sprite)
	{	AiSimBomb result = null;
		int id = sprite.getId();
		Iterator<AiSimBomb> it = internalBombs.iterator();
		while(result==null)
		{	AiSimBomb temp = it.next();
			if(temp.getId()==id)
				result = temp;
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FIRES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des feux contenus dans cette zone */
	private final List<AiSimFire> internalFires = new ArrayList<AiSimFire>();
	/** liste externe des feux contenus dans cette zone */
	private final List<AiFire> externalFires = new ArrayList<AiFire>();
	
	/**
	 * renvoie la liste interne de feux
	 * 
	 * @return	
	 * 		liste interne de feux
	 */
	protected List<AiSimFire> getInternalFires()
	{	return internalFires;	
	}
		
	@Override
	public List<AiFire> getFires()
	{	return externalFires;	
	}
		
	/**
	 * renvoie la simulation de sprite de même numéro (id)
	 * que celui passé en paramètre. Cette méthode permet
	 * de suivre le même sprite à travers différents états
	 * de la simulation, dans lesquels il est représenté
	 * par des objets différents.
	 * 
	 * @param 
	 * 		sprite	le sprite ciblé
	 * @return	
	 * 		sa représentation dans cette zone
	 */
	protected AiSimFire getSpriteById(AiFire sprite)
	{	AiSimFire result = null;
		int id = sprite.getId();
		Iterator<AiSimFire> it = internalFires.iterator();
		while(result==null)
		{	AiSimFire temp = it.next();
			if(temp.getId()==id)
				result = temp;
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FLOORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des sols contenus dans cette zone */
	private final List<AiSimFloor> internalFloors = new ArrayList<AiSimFloor>();
	/** liste externe des sols contenus dans cette zone */
	private final List<AiFloor> externalFloors = new ArrayList<AiFloor>();

	/**
	 * renvoie la liste interne de sols
	 * 
	 * @return	
	 * 		liste interne de sols
	 */
	protected List<AiSimFloor> getInternalFloors()
	{	return internalFloors;	
	}
		
	@Override
	public List<AiFloor> getFloors()
	{	return externalFloors;	
	}
	
	/**
	 * renvoie la simulation de sprite de même numéro (id)
	 * que celui passé en paramètre. Cette méthode permet
	 * de suivre le même sprite à travers différents états
	 * de la simulation, dans lesquels il est représenté
	 * par des objets différents.
	 * 
	 * @param 
	 * 		sprite	le sprite ciblé
	 * @return	
	 * 		sa représentation dans cette zone
	 */
	protected AiSimFloor getSpriteById(AiFloor sprite)
	{	AiSimFloor result = null;
		int id = sprite.getId();
		Iterator<AiSimFloor> it = internalFloors.iterator();
		while(result==null)
		{	AiSimFloor temp = it.next();
			if(temp.getId()==id)
				result = temp;
		}
		return result;
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
	
	/**
	 * renvoie la liste interne de personnages
	 * 
	 * @return	
	 * 		liste interne de personnages
	 */
	protected List<AiSimHero> getInternalHeroes()
	{	return internalHeroes;	
	}
		
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
	 * @param 
	 * 		color	couleur du personnage à retrouver
	 * @return	
	 * 		le personnage correspondant à la couleur spécifiée
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
	
	/**
	 * renvoie la simulation de sprite de même numéro (id)
	 * que celui passé en paramètre. Cette méthode permet
	 * de suivre le même sprite à travers différents états
	 * de la simulation, dans lesquels il est représenté
	 * par des objets différents.
	 * 
	 * @param 
	 * 		sprite	le sprite ciblé
	 * @return	
	 * 		sa représentation dans cette zone
	 */
	protected AiSimHero getSpriteById(AiHero sprite)
	{	AiSimHero result = null;
		int id = sprite.getId();
		Iterator<AiSimHero> it = internalHeroes.iterator();
		while(result==null)
		{	AiSimHero temp = it.next();
			if(temp.getId()==id)
				result = temp;
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
	/** nombre d'items cachés, par type*/
	private final HashMap<AiItemType,Integer> hiddenItemsCounts = new HashMap<AiItemType, Integer>();
	
	/**
	 * renvoie la liste interne d'items
	 * 
	 * @return	
	 * 		liste interne d'items
	 */
	protected List<AiSimItem> getInternalItems()
	{	return internalItems;	
	}
		
	@Override
	public List<AiItem> getItems()
	{	return externalItems;	
	}
	
	@Override
	public int getHiddenItemsCount()
	{	return hiddenItemsCount;
		//TODO must be updated manually
	}
	
	@Override
	public int getHiddenItemsCount(AiItemType type)
	{	Integer result = hiddenItemsCounts.get(type);
		if(result==null)
			result = 0;
		//TODO must be updated manually
		return result;
	}

	/**
	 * renvoie la simulation de sprite de même numéro (id)
	 * que celui passé en paramètre. Cette méthode permet
	 * de suivre le même sprite à travers différents états
	 * de la simulation, dans lesquels il est représenté
	 * par des objets différents.
	 * 
	 * @param 
	 * 		sprite	le sprite ciblé
	 * @return	
	 * 		sa représentation dans cette zone
	 */
	protected AiSimItem getSpriteById(AiItem sprite)
	{	AiSimItem result = null;
		int id = sprite.getId();
		Iterator<AiSimItem> it = internalItems.iterator();
		while(result==null)
		{	AiSimItem temp = it.next();
			if(temp.getId()==id)
				result = temp;
		}
		return result;
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
