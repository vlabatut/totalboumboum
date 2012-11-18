package org.totalboumboum.ai.v201213.adapter.model.full;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiFloor;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiSprite;
import org.totalboumboum.ai.v201213.adapter.data.AiState;
import org.totalboumboum.ai.v201213.adapter.data.AiStateName;
import org.totalboumboum.ai.v201213.adapter.data.AiStopType;
import org.totalboumboum.ai.v201213.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.calculus.LevelsTools;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * simule la zone de jeu et tous ces constituants : cases et sprites.
 * Il s'agit de la classe principale pour la simulation de l'évolution du jeu.</br>
 * 
 * L'ensemble des objets représente un état du jeu et ne peut 
 * être modifié que via la classe AiModel.
 * 
 * @author Vincent Labatut
 *
 */
public final class AiSimZone extends AiZone
{	
	/**
	 * contruit une zone qui est une copie de celle passée en paramètre.
	 * si la paramètre fullCopy est faux, les sprites ne sont pas copiés,
	 * sinon tout est copié.
	 * 
	 * @param zone
	 * 		La zone de référence
	 */
	public AiSimZone(AiZone zone)
	{	// matrix and tiles
		Thread.yield();
		AiTile[][] m = zone.getMatrix();
		height = zone.getHeight();
		width = zone.getWidth();
		pixelLeftX = zone.getPixelLeftX();
		pixelTopY = zone.getPixelTopY();
		pixelWidth = zone.getPixelWidth();
		pixelHeight = zone.getPixelHeight();
		matrix = new AiSimTile[height][width];
		for(int row=0;row<height;row++)
		{	for(int col=0;col<width;col++)
			{	AiTile tile = m[row][col];
				double posX = tile.getPosX();
				double posY = tile.getPosY();
				AiSimTile aiTile = new AiSimTile(this,row,col,posX,posY);
				matrix[row][col] = aiTile;
				tiles.add(aiTile);
			}
		}
		
		// sprites
		Thread.yield();
		for(int row=0;row<height;row++)
		{	for(int col=0;col<width;col++)
			{	AiTile tile = zone.getTile(row,col);
				AiSimTile simTile = matrix[row][col];
				// blocks
				List<AiBlock> blocks = tile.getBlocks();
				for(AiBlock block: blocks)
				{	AiSimBlock simBlock = new AiSimBlock(simTile, block);
					addSprite(simBlock);
				}
				// bombs
				List<AiBomb> bombs = tile.getBombs();
				for(AiBomb bomb: bombs)
				{	AiSimBomb simBomb = new AiSimBomb(simTile, bomb);
					addSprite(simBomb);
				}
				// fires
				List<AiFire> fires = tile.getFires();
				for(AiFire fire: fires)
				{	AiSimFire simFire = new AiSimFire(simTile, fire);
					addSprite(simFire);
				}
				// floors
// NOTE to gain a few ms...				
//				List<AiFloor> floors = tile.getFloors();
//				for(AiFloor floor: floors)
//				{	AiSimFloor simFloor = new AiSimFloor(floor,simTile);
//					addSprite(simFloor);
//				}
				// heroes
				List<AiHero> heroes = tile.getHeroes();
				for(AiHero hero: heroes)
				{	AiSimHero simHero = new AiSimHero(simTile, hero);
					addSprite(simHero);
				}
				// items
				List<AiItem> items = tile.getItems();
				for(AiItem item: items)
				{	AiSimItem simItem = new AiSimItem(simTile, item);
					addSprite(simItem);
				}
			}
		}
		
		// also copy the eliminated heroes
		Thread.yield();
		for(AiHero hero: zone.getHeroes())
		{	if(!externalHeroes.contains(hero))
			{	AiTile tile = hero.getTile();
				int row = tile.getRow();
				int col = tile.getCol();
				AiSimHero simHero = new AiSimHero(matrix[row][col], hero);
				internalHeroes.add(simHero);
				externalHeroes.add(simHero);
			}
		}
		
		// set own hero
		AiHero oh = zone.getOwnHero();
		PredefinedColor color = oh.getColor();
		ownHero = getInternalHeroByColor(color);
		
		// set meta-data
		Thread.yield();
		for(AiHero hero: zone.getHeroes())
		{	color = hero.getColor();
			AiSimHero aiHero = getInternalHeroByColor(color);
			int roundRank = zone.getRoundRank(hero);
			roundRanks.put(aiHero,roundRank);
			int matchRank = zone.getMatchRank(hero);
			matchRanks.put(aiHero,matchRank);
			int statsRank = zone.getStatsRank(hero);
			statsRanks.put(aiHero,statsRank);
		}
		
		// items
		Thread.yield();
		hiddenItemsCount = zone.getHiddenItemsCount();
		for(AiItemType type: AiItemType.values())
		{	int value = zone.getHiddenItemsCount(type);
			hiddenItemsCounts.put(type,value);
		}
		
		// sudden death events
		List<AiSuddenDeathEvent> sdEvents = zone.getAllSuddenDeathEvents();
		for(AiSuddenDeathEvent event: sdEvents)
		{	AiSimSuddenDeathEvent e = new AiSimSuddenDeathEvent(this, event);
			suddenDeathEvents.add(e);
			internalSuddenDeathEvents.add(e);
		}
		
		// time
		totalTime = zone.getTotalTime();
		limitTime = zone.getLimitTime();
		elapsedTime = 0;
	}

	/**
	 * Crée une zone vide dont les dimensions correspondent à celles
	 * passées en paramètres.
	 * <br/>
	 * Bien sûr, cette zone doit ensuite être
	 * complétée avec des sprites (personnages,
	 * blocks, bombes, items...) pour être
	 * utilisable (cf. et autres méthodes similaires).
	 * <br/>
	 * <b>Remarque</b> : cette façon de créer un modèle est réservée
	 * au débogage, elle n'est pas prévue pour une utilisation
	 * en cours de jeu.
	 * 
	 * @param height
	 * 		Hauteur de la zone à créer.
	 * @param width
	 * 		Largeur de la zone à créer.
	 */
	public AiSimZone(int height, int width)
	{	Thread.yield();
		this.height = height;
		this.width = width;
		pixelLeftX = 0;
		pixelTopY = 0;
		double dim = RoundVariables.scaledTileDimension;
		pixelWidth = dim*width;
		pixelHeight = dim*height;
		matrix = new AiSimTile[height][width];
		for(int row=0;row<height;row++)
		{	for(int col=0;col<width;col++)
			{	double posX = dim*col+dim/2;
				double posY = dim*row+dim/2;
				AiSimTile aiTile = new AiSimTile(this,row,col,posX,posY);
				matrix[row][col] = aiTile;
				tiles.add(aiTile);
			}
		}
		
		// items
		hiddenItemsCount = 0;
		
		// sudden death events
		suddenDeathEvents = new ArrayList<AiSuddenDeathEvent>();
		internalSuddenDeathEvents = new ArrayList<AiSimSuddenDeathEvent>();
		
		// time
		totalTime = 0;
		elapsedTime = 0;
		limitTime = 0;
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
	 * Renvoie la case voisine de la case passée en paramètre,
	 * dans la direction spécifiée (en considérant le fait que le niveau
	 * est fermé. 
	 * <br/>
	 * Si la direction est {@link Direction#NONE NONE}, alors c'est
	 * la case de coordonnées ({@code row},{@code col}) qui est
	 * renvoyée.
	 * <br/>
	 * Si la direction est composite, une {@link IllegalArgumentException}
	 * est levée.
	 *  
	 *  @param row
	 *  	ligne de la case à traite
	 *  @param col
	 *  	colonne de la case à traiter
	 *  @param direction
	 *  	direction de la case voisine relativement à la case de référence
	 *  @return	
	 *  	la case voisine dans la direction précisée
	 *  
	 *  @throws IllegalArgumentException
	 *  	Si la {@code direction} est composite.
	 */
	public AiSimTile getNeighborTile(int row, int col, Direction direction)
	{	if(direction.isComposite())
			throw new IllegalArgumentException("method getNeighborTile does not handle composite directions.");
	
		AiSimTile result;
		int c,l;
		Direction p[] = direction.getPrimaries(); 

		if(p[0]==Direction.LEFT)
			c = (col+width-1)%width;
		else if(p[0]==Direction.RIGHT)
			c = (col+1)%width;
		else
			c = col;

		if(p[1]==Direction.UP)
			l = (row+height-1)%height;
		else if(p[1]==Direction.DOWN)
			l = (row+1)%height;
		else
			l = row;

		result = matrix[l][c];
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * met à jour les variables temporelles de cette zone.
	 * (méthode utilisée seulement lors de la simulation)
	 * 
	 * @param duration
	 * 		délai depuis la dernière simulation
	 */
	protected void updateTime(long duration)
	{	totalTime = totalTime + duration;
		elapsedTime = duration;
	}
	
	/////////////////////////////////////////////////////////////////
	// TILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste de toutes les cases de cette zone */
	private final List<AiTile> tiles = new LinkedList<AiTile>(); 
	
	@Override
	public List<AiTile> getTiles()
	{	return tiles;
	}
	
	@Override
	public AiSimTile getTile(int row, int col)
	{	return matrix[row][col];
	}
	
	@Override
	public AiSimTile getTile(double x, double y)
	{	int[] coord = LevelsTools.getTile(x,y,pixelLeftX,pixelTopY,pixelHeight,pixelWidth,height,width);
		AiSimTile result = matrix[coord[0]][coord[1]];
		return result;
	}
		
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Compteur d'id pour les sprites */
	private int spriteId = 0; 

	/**
	 * Permet de générer des id pour les sprites créés lors des simulation
	 * 
	 * @return
	 * 		Une nouvelle id de sprite.
	 */
	private int createNewId()
	{	int result = spriteId;
		spriteId++;
		return result;
	}
	
	/**
	 * Permet de rajouter un sprite dans cette zone.
	 * <br/>
	 * <b>Attention :</b> le sprite a obligatoirement déjà été 
	 * affecté à une case lors de sa construction, donc il s'agit 
	 * ici simplement de mettre à jour les listes de sprites de la zone
	 * 
	 *  @param sprite
	 *  	Le sprite à rajouter à cette zone.
	 */
	protected void addSprite(AiSimSprite sprite)
	{	if(sprite instanceof AiSimBlock)
		{	AiSimBlock block = (AiSimBlock)sprite;
			addSprite(block);
		}
		else if(sprite instanceof AiSimBomb)
		{	AiSimBomb bomb = (AiSimBomb)sprite;
			addSprite(bomb);
		}
		else if(sprite instanceof AiSimFire)
		{	AiSimFire fire = (AiSimFire)sprite;
			addSprite(fire);
		}
		else if(sprite instanceof AiSimFloor)
		{	AiSimFloor floor = (AiSimFloor)sprite;
			addSprite(floor);
		}
		else if(sprite instanceof AiSimHero)
		{	AiSimHero hero = (AiSimHero)sprite;
			addSprite(hero);
		}
		else if(sprite instanceof AiSimItem)
		{	AiSimItem item = (AiSimItem)sprite;
			addSprite(item);
		}
	}

	/**
	 * Supprime un sprite de la zone et de la case correspondante.
	 * s'il s'agit d'un joueur, il est supprimé seulement de la case
	 * et de la liste des joueurs encore en jeu (mais la zone continue
	 * à le référencer dans la liste générale des joueurs, car le sprite
	 * peut encore etre utile, par exemple pour obtenir le classement
	 * de ce joueur).
	 * 
	 * @param sprite
	 * 		Le sprit à supprimer de la zone.
	 */
	protected void removeSprite(AiSimSprite sprite)
	{	if(sprite instanceof AiSimBlock)
		{	AiSimBlock block = (AiSimBlock)sprite;
			removeSprite(block);
		}
		else if(sprite instanceof AiSimBomb)
		{	AiSimBomb bomb = (AiSimBomb)sprite;
			removeSprite(bomb);
		}
		else if(sprite instanceof AiSimFire)
		{	AiSimFire fire = (AiSimFire)sprite;
			removeSprite(fire);
		}
		else if(sprite instanceof AiSimFloor)
		{	AiSimFloor floor = (AiSimFloor)sprite;
			removeSprite(floor);
		}
		else if(sprite instanceof AiSimHero)
		{	AiSimHero hero = (AiSimHero)sprite;
			removeSprite(hero);
		}
		else if(sprite instanceof AiSimItem)
		{	AiSimItem item = (AiSimItem)sprite;
			removeSprite(item);
		}
	}
	
	/**
	 * Renvoie la simulation de sprite de même numéro (id)
	 * que celui passé en paramètre. Cette méthode permet
	 * de suivre le même sprite à travers différents états
	 * de la simulation, dans lesquels il est représenté
	 * par des objets différents.
	 * 
	 * @param sprite
	 * 		Le sprite ciblé.
	 * @return	
	 * 		Sa représentation dans cette zone.
	 */
	public AiSimSprite getSpriteById(AiSprite sprite)
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
	 * @param sprite
	 * 		le sprite ciblé
	 * @return	
	 * 		sa représentation dans cette zone
	 */
	public AiSimBlock getSpriteById(AiBlock sprite)
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
	
	/**
	 * Permet de rajouter un bloc dans cette zone.
	 * <br/>
	 * <b>Attention :</b> le bloc a obligatoirement déjà été 
	 * affecté à une case lors de sa construction, donc il s'agit 
	 * ici simplement de mettre à jour les listes de sprites de la zone.
	 * 
	 *  @param block
	 *  	le bloc à rajouter à cette zone
	 */
	protected void addSprite(AiSimBlock block)
	{	AiSimTile tile = block.getTile();
		internalBlocks.add(block);
		externalBlocks.add(block);
		tile.addSprite(block);
	}
	
	/**
	 * supprime un sprite de la zone et de la case correspondante.
	 * 
	 * @param block
	 * 		le sprite à supprimer de la zone
	 */
	protected void removeSprite(AiSimBlock block)
	{	internalBlocks.remove(block);
		externalBlocks.remove(block);
		
		AiSimTile tile = block.getTile();
		tile.removeSprite(block);
	}

	/**
	 * Cette méthode permet de rajouter un mur dans une zone,
	 * afin de créer un cas de test par exemple. Aucun contrôle
	 * n'est effectué sur la possibilité de créer le sprite ou pas.
	 * Par exemple, la méthode ne teste pas si la case spécifiée 
	 * contient déjà un mur.
	 * <br/>
	 * <b>Attention</b> : cette méthode est prévue pour effectuer
	 * du débogage hors-ligne, c'est à dire en dehors du jeu. Si elle
	 * est utilisée pour modifier une zone initialisée par le moteur
	 * du jeu, alors il est probable que des erreurs apparaissent.
	 * 
	 * @param tile
	 * 		La case de la zone devant contenir le sprite.
	 * @param destructible
	 * 		Le type de mur à créer : {@code true} pour un mur destructible,
	 * 		{@code false} pour un mur indestructible.
	 * @return
	 * 		Le block créé. 
	 */
	public AiSimBlock createBlock(AiTile tile, boolean destructible)
	{	// location
		int row = 0;
		int col = 0;
		AiSimTile t = null;
		double posX = 0;
		double posY = 0;
		double posZ = 0;
		if(tile!=null)
		{	row = tile.getRow();
			col = tile.getCol();
			t = matrix[row][col];
			posX = t.getPosX();
			posY = t.getPosY();
			posZ = 0;
		}
		
		// create sprite
		long burningDuration;
		double currentSpeed = 0;
		AiStopType stopHeroes;
		AiStopType stopFires;
		if(destructible)
		{	stopHeroes = AiStopType.WEAK_STOP;
			stopFires = AiStopType.WEAK_STOP;
			burningDuration = 470;
		}
		else
		{	stopHeroes = AiStopType.STRONG_STOP;
			stopFires = AiStopType.STRONG_STOP;
			burningDuration = -1;
		}
		AiSimState state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
		int id = createNewId();
		AiSimBlock result = new AiSimBlock(id,t,posX,posY,posZ,state,burningDuration,currentSpeed,destructible,stopHeroes,stopFires);
		
		// update zone
		if(tile!=null)
			addSprite(result);
		
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
	 * @param sprite
	 * 		le sprite ciblé
	 * @return	
	 * 		sa représentation dans cette zone
	 */
	public AiSimBomb getSpriteById(AiBomb sprite)
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

	/**
	 * Permet de rajouter une bombe dans cette zone.
	 * <br/>
	 * <b>Attention :</b> la bombe a obligatoirement déjà été 
	 * affecté à une case lors de sa construction, donc il s'agit 
	 * ici simplement de mettre à jour les listes de sprites de la zone.
	 * 
	 *  @param bomb
	 *  	La bombe à rajouter à cette zone.
	 */
	protected void addSprite(AiSimBomb bomb)
	{	AiSimTile tile = bomb.getTile();
		internalBombs.add(bomb);
		externalBombs.add(bomb);
		tile.addSprite(bomb);
	}
	
	/**
	 * supprime un sprite de la zone et de la case correspondante.
	 * 
	 * @param bomb
	 * 		le sprite à supprimer de la zone
	 */
	protected void removeSprite(AiSimBomb bomb)
	{	internalBombs.remove(bomb);
		externalBombs.remove(bomb);
		
		AiSimTile tile = bomb.getTile();
		tile.removeSprite(bomb);
	}

	/**
	 * Cette méthode permet de rajouter une bombe dans une zone,
	 * afin de créer un cas de test par exemple. Aucun contrôle
	 * n'est effectué sur la possibilité de créer le sprite ou pas.
	 * Par exemple, la méthode ne teste pas si la case spécifiée 
	 * contient déjà un mur.
	 * <br/>
	 * La bombe crée ici n'appartient à aucun joueur, mais au niveau.
	 * <br/>
	 * <b>Attention</b> : cette méthode est prévue pour effectuer
	 * du débogage hors-ligne, c'est à dire en dehors du jeu. Si elle
	 * est utilisée pour modifier une zone initialisée par le moteur
	 * du jeu, alors il est probable que des erreurs apparaissent.
	 * 
	 * @param tile
	 * 		La case de la zone devant contenir le sprite.
	 * @param range
	 * 		La portée de la bombe à créer.
	 * @param elapsedTime
	 * 		Le temps écoulé depuis que la bombe a été posée.
	 * @return
	 * 		La bombe créée. 
	 */
	public AiSimBomb createBomb(AiTile tile, int range, long elapsedTime)
	{	// location
		int row = 0;
		int col = 0;
		AiSimTile t = null;
		double posX = 0;
		double posY = 0;
		double posZ = 0;
		if(tile!=null)
		{	row = tile.getRow();
			col = tile.getCol();
			t = matrix[row][col];
			posX = t.getPosX();
			posY = t.getPosY();
			posZ = 0;
		}

		// create sprite
		AiSimFire firePrototype = createFire(null);
		boolean countdownTrigger = true;
		boolean remoteControlTrigger = false;
		boolean explosionTrigger = true;
		boolean throughItems = false;
		float failureProbability = 0.03f;
		long burningDuration = 100;
		long latencyDuration = 140;
		long normalDuration = 2400;
		long time = elapsedTime;
		boolean penetrating = false;
		boolean working = true;
		double currentSpeed = 0;
		double slidingSpeed = (140*RoundVariables.zoomFactor)/16;
		AiStopType stopHeroes = AiStopType.WEAK_STOP;
		AiStopType stopFires = AiStopType.WEAK_STOP;
		AiSimState state = new AiSimState(AiStateName.STANDING,Direction.NONE,elapsedTime);
		PredefinedColor color = null;
		int id = createNewId();
		AiSimBomb bomb = new AiSimBomb(id,t,posX,posY,posZ,
				state,burningDuration,currentSpeed,slidingSpeed,
				countdownTrigger,remoteControlTrigger,explosionTrigger,
				normalDuration,latencyDuration,failureProbability,firePrototype,
				stopHeroes,stopFires,throughItems,range,penetrating,
				color,working,time);
		
		// update zone
		if(tile!=null)
			addSprite(bomb);
		
		return bomb;
	}

	/**
	 * Cette méthode permet de rajouter une bombe dans une zone,
	 * afin de créer un cas de test par exemple. Aucun contrôle
	 * n'est effectué sur la possibilité de créer le sprite ou pas.
	 * Par exemple, la méthode ne teste pas si la case spécifiée 
	 * contient déjà un mur.
	 * <br/>
	 * La bombe crée ici appartient au joueur passé en paramètre.
	 * Si le paramètre {@code tile} est {@code null}, la bombe
	 * est placée dans la case du joueur.
	 * <br/>
	 * <b>Attention</b> : cette méthode est prévue pour effectuer
	 * du débogage hors-ligne, c'est à dire en dehors du jeu. Si elle
	 * est utilisée pour modifier une zone initialisée par le moteur
	 * du jeu, alors il est probable que des erreurs apparaissent.
	 * 
	 * @param tile
	 * 		La case de la zone devant contenir le sprite. Si ce paramètre
	 * 		vaut {@code null}, alors la bombe est posée dans la case du
	 * 		joueur spécifié.
	 * @param hero
	 * 		Le personnage devant poser la bombe.
	 * @return 
	 * 		La bombe créée.
	 */
	public AiSimBomb createBomb(AiTile tile, AiSimHero hero)
	{	// location
		int row = 0;
		int col = 0;
		AiSimTile t = null;
		if(tile==null)
			tile = hero.getTile();
		if(tile!=null)
		{	row = tile.getRow();
			col = tile.getCol();
			t = matrix[row][col];
		}
		
		// create sprite
		AiBomb bombPrototype = hero.getBombPrototype();
		AiSimBomb result = new AiSimBomb(t, bombPrototype);

		// update
		addSprite(result);
		hero.updateBombNumberCurrent(+1);
		
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
	 * @param sprite
	 * 		le sprite ciblé
	 * @return	
	 * 		sa représentation dans cette zone
	 */
	public AiSimFire getSpriteById(AiFire sprite)
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
	
	/**
	 * Permet de rajouter un feu dans cette zone.
	 * <br/>
	 * <b>Attention :</b> le feu a obligatoirement déjà été 
	 * affecté à une case lors de sa construction, donc il s'agit 
	 * ici simplement de mettre à jour les listes de sprites de la zone.
	 * 
	 *  @param fire
	 *  	Le feu à rajouter à cette zone.
	 */
	protected void addSprite(AiSimFire fire)
	{	AiSimTile tile = fire.getTile();
		internalFires.add(fire);
		externalFires.add(fire);
		tile.addSprite(fire);
	}
	
	/**
	 * supprime un sprite de la zone et de la case correspondante.
	 * 
	 * @param fire
	 * 		le sprite à supprimer de la zone
	 */
	protected void removeSprite(AiSimFire fire)
	{	internalFires.remove(fire);
		externalFires.remove(fire);
		
		AiSimTile tile = fire.getTile();
		tile.removeSprite(fire);
	}

	/**
	 * Cette méthode permet de rajouter un fire dans une zone,
	 * afin de créer un cas de test par exemple. Aucun contrôle
	 * n'est effectué sur la possibilité de créer le sprite ou pas.
	 * Par exemple, la méthode ne teste pas si la case spécifiée 
	 * contient déjà un mur.
	 * <br/>
	 * <b>Attention</b> : cette méthode est prévue pour effectuer
	 * du débogage hors-ligne, c'est à dire en dehors du jeu. Si elle
	 * est utilisée pour modifier une zone initialisée par le moteur
	 * du jeu, alors il est probable que des erreurs apparaissent.
	 * 
	 * @param tile
	 * 		La case de la zone devant contenir le sprite.
	 * @return 
	 * 		Le feu créé.
	 */
	public AiSimFire createFire(AiTile tile)
	{	// location
		int row = 0;
		int col = 0;
		AiSimTile t = null;
		double posX = 0;
		double posY = 0;
		double posZ = 0;
		if(tile!=null)
		{	row = tile.getRow();
			col = tile.getCol();
			t = matrix[row][col];
			posX = t.getPosX();
			posY = t.getPosY();
			posZ = 0;
		}
		
		// create sprite
		long burningDuration = 640;
		double currentSpeed = 0;
		boolean throughBlocks = false;
		boolean throughBombs = true;
		boolean throughItems = false;
		AiSimState state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
		int id = createNewId();
		AiSimFire result = new AiSimFire(id,t,posX,posY,posZ,state,burningDuration,currentSpeed,throughBlocks,throughBombs,throughItems);
		
		// update zone
		if(tile!=null)
			addSprite(result);
		
		return result;
	}

	/**
	 * Cette méthode permet de rajouter un fire dans une zone,
	 * afin de créer un cas de test par exemple. Aucun contrôle
	 * n'est effectué sur la possibilité de créer le sprite ou pas.
	 * Par exemple, la méthode ne teste pas si la case spécifiée 
	 * contient déjà un mur.
	 * <br/>
	 * Cette méthode permet de créer un nouveau sprite de feu
	 * en recopiant celui passé en paramètre.
	 * <br/>
	 * <b>Attention</b> : cette méthode est prévue pour effectuer
	 * du débogage hors-ligne, c'est à dire en dehors du jeu. Si elle
	 * est utilisée pour modifier une zone initialisée par le moteur
	 * du jeu, alors il est probable que des erreurs apparaissent.
	 * 
	 * @param original
	 * 		Le sprite original, à recopier.
	 * @param tile
	 * 		La case de la zone devant contenir le sprite.
	 * @return
	 * 		Le feu créé. 
	 */
	protected AiSimFire createFire(AiFire original, AiTile tile)
	{	// location
		int row = 0;
		int col = 0;
		AiSimTile t = null;
		double posX = 0;
		double posY = 0;
		double posZ = 0;
		if(tile!=null)
		{	row = tile.getRow();
			col = tile.getCol();
			t = matrix[row][col];
			posX = t.getPosX();
			posY = t.getPosY();
			posZ = 0;
		}
		
		// create sprite
		long burningDuration = original.getBurningDuration();
		double currentSpeed = original.getCurrentSpeed();
		boolean throughBlocks = original.hasThroughBlocks();
		boolean throughBombs = original.hasThroughBombs();
		boolean throughItems = original.hasThroughItems();
		AiState s = original.getState();
		AiSimState state = new AiSimState(s.getName(),s.getDirection(),0);
		int id = createNewId();
		AiSimFire result = new AiSimFire(id,t,posX,posY,posZ,state,burningDuration,currentSpeed,throughBlocks,throughBombs,throughItems);
		
		// update zone
		if(tile!=null)
			addSprite(result);
		
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
	 * @param sprite
	 * 		le sprite ciblé
	 * @return	
	 * 		sa représentation dans cette zone
	 */
	public AiSimFloor getSpriteById(AiFloor sprite)
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
	
	/**
	 * Permet de rajouter un sol dans cette zone.
	 * <br/>
	 * <b>Attention :</b> le sol a obligatoirement déjà été 
	 * affecté à une case lors de sa construction, donc il s'agit 
	 * ici simplement de mettre à jour les listes de sprites de la zone.
	 * 
	 *  @param floor
	 *  	le sol à rajouter à cette zone.
	 */
	protected void addSprite(AiSimFloor floor)
	{	AiSimTile tile = floor.getTile();
		internalFloors.add(floor);
		externalFloors.add(floor);
		tile.addSprite(floor);
	}
	
	/**
	 * supprime un sprite de la zone et de la case correspondante.
	 * 
	 * @param floor
	 * 		le sprite à supprimer de la zone
	 */
	protected void removeSprite(AiSimFloor floor)
	{	internalFloors.remove(floor);
		externalFloors.remove(floor);
		
		AiSimTile tile = floor.getTile();
		tile.removeSprite(floor);
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
	 * @param color
	 * 		couleur du personnage à retrouver
	 * @return	
	 * 		le personnage correspondant à la couleur spécifiée
	 */
	private AiSimHero getInternalHeroByColor(PredefinedColor color)
	{	AiSimHero result = null;
		Iterator<AiSimHero> it = internalHeroes.iterator();
		while(result==null && it.hasNext())
		{	AiSimHero htemp = it.next();
			if(htemp.getColor()==color)
				result = htemp;
		}
		return result;
	}
	
	@Override
	public AiSimHero getHeroByColor(PredefinedColor color)
	{	AiSimHero result = null;
		Iterator<AiSimHero> it = internalHeroes.iterator();
		while(result==null && it.hasNext())
		{	AiSimHero hero = it.next();
			if(hero.getColor()==color)
				result = hero;
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
	 * @param sprite
	 * 		le sprite ciblé
	 * @return	
	 * 		sa représentation dans cette zone
	 */
	public AiSimHero getSpriteById(AiHero sprite)
	{	AiSimHero result = null;
		int id = sprite.getId();
		Iterator<AiSimHero> it = internalHeroes.iterator();
		while(result==null && it.hasNext())
		{	AiSimHero temp = it.next();
			if(temp.getId()==id)
				result = temp;
		}
		return result;
	}
	
	/**
	 * Permet de rajouter un personnage dans cette zone.
	 * <br/>
	 * <b>Attention :</b> le personnage a obligatoirement déjà été 
	 * affecté à une case lors de sa construction, donc il s'agit 
	 * ici simplement de mettre à jour les listes de sprites de la zone.
	 * 
	 *  @param hero
	 *  	Le personnage à rajouter à cette zone.
	 */
	protected void addSprite(AiSimHero hero)
	{	AiSimTile tile = hero.getTile();
		internalHeroes.add(hero);
		externalHeroes.add(hero);
		remainingHeroList.add(hero);
		tile.addSprite(hero);
	}
	
	/**
	 * supprime un sprite de la zone et de la case correspondante.
	 * comme il s'agit d'un joueur, il est supprimé seulement de la case
	 * et de la liste des joueurs encore en jeu (mais la zone continue
	 * à le référencer dans la liste générale des joueurs, car le sprite
	 * peut encore etre utile, par exemple pour obtenir le classement
	 * de ce joueur)
	 * 
	 * @param hero
	 * 		le sprite à supprimer de la zone
	 */
	protected void removeSprite(AiSimHero hero)
	{	// only remove from this list, because the others must stay complete
		remainingHeroList.remove(hero);
		
		AiSimTile tile = hero.getTile();
		tile.removeSprite(hero);
	}

	/**
	 * insûre un nouveau personnage dans la zone
	 * (méthode utilisée lors de la simulation)
	 * 
	 * @param hero
	 * 		le personnage à insérer
	 * @param 
	 * 		isOwnHero	indique si le personnage à insérer est celui contrôlé par l'agent
	 */
	protected void addHero(AiSimHero hero, boolean isOwnHero)
	{	// sprites
		addSprite(hero);
		if(isOwnHero)
			ownHero = hero;
		
		// ranks
		roundRanks.put(hero,0);
		matchRanks.put(hero,0);
		statsRanks.put(hero,0);
	}

	/**
	 * Cette méthode permet de rajouter un personnage dans une zone,
	 * afin de créer un cas de test par exemple. Aucun contrôle
	 * n'est effectué sur la possibilité de créer le sprite ou pas.
	 * Par exemple, la méthode ne teste pas si la case spécifiée 
	 * contient déjà un mur.
	 * <br/>
	 * <b>Attention</b> : cette méthode est prévue pour effectuer
	 * du débogage hors-ligne, c'est à dire en dehors du jeu. Si elle
	 * est utilisée pour modifier une zone initialisée par le moteur
	 * du jeu, alors il est probable que des erreurs apparaissent.
	 * 
	 * @param tile
	 * 		La case de la zone devant contenir le sprite.
	 * @param color
	 * 		La couleur du joueur.
	 * @param bombNumber
	 * 		Le nombre de bombes que le joueur peut poser.
	 * @param range
	 * 		La portée des bombes du joueur.
	 * @param ownHero
	 * 		Indique si ce personnage est celui contrôlé par l'agent.
	 * @return
	 * 		Le personnage créé. 
	 */
	public AiSimHero createHero(AiTile tile, PredefinedColor color, int bombNumber, int range, boolean ownHero)
	{	// bomb prototype
		AiSimBomb bombPrototype;
		{	AiSimTile t = null;
			double posX = 0;
			double posY = 0;
			double posZ = 0;
	
			AiSimFire firePrototype = createFire(null);
			boolean countdownTrigger = true;
			boolean remoteControlTrigger = false;
			boolean explosionTrigger = true;
			boolean throughItems = false;
			float failureProbability = 0.03f;
			long burningDuration = 100;
			long latencyDuration = 140;
			long normalDuration = 2400;
			long time = 0;
			boolean penetrating = false;
			boolean working = true;
			double currentSpeed = 0;
			double slidingSpeed = (140*RoundVariables.zoomFactor)/16 * 1000;
			AiStopType stopHeroes = AiStopType.WEAK_STOP;
			AiStopType stopFires = AiStopType.WEAK_STOP;
			AiSimState state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
			int id = createNewId();
			bombPrototype = new AiSimBomb(id,t,posX,posY,posZ,
					state,burningDuration,currentSpeed,slidingSpeed,
					countdownTrigger,remoteControlTrigger,explosionTrigger,
					normalDuration,latencyDuration,failureProbability,firePrototype,
					stopHeroes,stopFires,throughItems,range,penetrating,
					color,working,time);
		}
		
		// location
		int row = 0;
		int col = 0;
		AiSimTile t = null;
		double posX = 0;
		double posY = 0;
		double posZ = 0;
		if(tile!=null)
		{	row = tile.getRow();
			col = tile.getCol();
			t = matrix[row][col];
			posX = t.getPosX();
			posY = t.getPosY();
			posZ = 0;
		}
		
		// create sprite
		long burningDuration = 810; // dépend du sprite...
		int bombCount = 0;
		double currentSpeed = 0;
		double walkingSpeed = 50*RoundVariables.zoomFactor;
		boolean throughBlocks = false;
		boolean throughBombs = false;
		boolean throughFires = false;
		AiSimState state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
		int id = createNewId();
		AiSimHero result = new AiSimHero(id,t,posX,posY,posZ, 
				state,burningDuration,currentSpeed,
				bombPrototype,bombNumber,bombCount, 
				throughBlocks,throughBombs,throughFires, 
				color,walkingSpeed);
		
		// update zone
		if(tile!=null)
			addHero(result,ownHero);
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** liste interne des items contenus dans cette zone */
	private final List<AiSimItem> internalItems = new ArrayList<AiSimItem>();
	/** liste externe des items contenus dans cette zone */
	private final List<AiItem> externalItems = new ArrayList<AiItem>();
	
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
	
	/**
	 * Permet de diminuer le nombre d'items cachés,
	 * lors de la simulation.
	 * 
	 * @param type
	 * 		Le type d'item dont le nombre doit être changer.
	 * @param delta
	 * 		La modification à apporter au nombre d'items.
	 */
	protected void updateHiddenItemsCount(AiItemType type, int delta)
	{	Integer value = hiddenItemsCounts.get(type);
		hiddenItemsCounts.put(type,value + delta);
		hiddenItemsCount = hiddenItemsCount + delta;
	}
	
	/**
	 * renvoie la simulation de sprite de même numéro (id)
	 * que celui passé en paramètre. Cette méthode permet
	 * de suivre le même sprite à travers différents états
	 * de la simulation, dans lesquels il est représenté
	 * par des objets différents.
	 * 
	 * @param sprite
	 * 		le sprite ciblé
	 * @return	
	 * 		sa représentation dans cette zone
	 */
	public AiSimItem getSpriteById(AiItem sprite)
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
	
	/**
	 * Permet de rajouter un item dans cette zone.
	 * <br/>
	 * <b>Attention :</b> l'item a obligatoirement déjà été 
	 * affecté à une case lors de sa construction, donc il s'agit 
	 * ici simplement de mettre à jour les listes de sprites de la zone.
	 * 
	 *  @param item
	 *  	L'item sprite à rajouter à cette zone.
	 */
	protected void addSprite(AiSimItem item)
	{	AiSimTile tile = item.getTile();
		internalItems.add(item);
		externalItems.add(item);
		tile.addSprite(item);
	}
	
	/**
	 * Supprime un sprite de la zone et de la case correspondante.
	 * 
	 * @param item
	 * 		Le sprite à supprimer de la zone.
	 */
	protected void removeSprite(AiSimItem item)
	{	internalItems.remove(item);
		externalItems.remove(item);
		
		AiSimTile tile = item.getTile();
		tile.removeSprite(item);
	}

	/**
	 * Méthode utilisée en interne
	 * pour initialiser une zone de test.
	 * 
	 * @param hiddenItemsCount
	 * 		Nombre total d'items cachés.
	 * @param hiddenItemsCounts
	 * 		Nombres d'items cachés par type d'item.
	 */
	public void setHidenItemCount(int hiddenItemsCount, HashMap<AiItemType,Integer> hiddenItemsCounts)
	{	this.hiddenItemsCount = hiddenItemsCount;
		this.hiddenItemsCounts.putAll(hiddenItemsCounts);
	}
	
	/**
	 * Cette méthode permet de rajouter un item dans une zone,
	 * afin de créer un cas de test par exemple. Aucun contrôle
	 * n'est effectué sur la possibilité de créer le sprite ou pas.
	 * Par exemple, la méthode ne teste pas si la case spécifiée 
	 * contient déjà un mur.
	 * <br/>
	 * <b>Attention</b> : cette méthode est prévue pour effectuer
	 * du débogage hors-ligne, c'est à dire en dehors du jeu. Si elle
	 * est utilisée pour modifier une zone initialisée par le moteur
	 * du jeu, alors il est probable que des erreurs apparaissent.
	 * 
	 * @param tile
	 * 		La case de la zone devant contenir le sprite.
	 * @param itemType
	 * 		Le type d'item à créer.
	 * @return
	 * 		L'item créé. 
	 */
	public AiSimItem createItem(AiTile tile, AiItemType itemType)
	{	// location
		int row = 0;
		int col = 0;
		AiSimTile t = null;
		double posX = 0;
		double posY = 0;
		double posZ = 0;
		if(tile!=null)
		{	row = tile.getRow();
			col = tile.getCol();
			t = matrix[row][col];
			posX = t.getPosX();
			posY = t.getPosY();
			posZ = 0;
		}
		
		// create sprite
		long burningDuration = 900;
		double currentSpeed = 0;
		AiStopType stopBombs = AiStopType.WEAK_STOP;
		AiStopType stopFires = AiStopType.WEAK_STOP;
		AiSimState state = new AiSimState(AiStateName.STANDING,Direction.NONE,0);
		int id = createNewId();
		AiSimItem result = new AiSimItem(id,t,posX,posY,posZ,state,burningDuration,currentSpeed,itemType,stopBombs,stopFires);
		
		// update zone
		if(tile!=null)
		{	addSprite(result);
			updateHiddenItemsCount(itemType,1);
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// OWN HERO			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le personnage contrôlé par l'agent */
	private AiSimHero ownHero;

	@Override
	public AiSimHero getOwnHero()
	{	return ownHero;	
	}
	
	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
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
	// SUDDEN DEATH				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste interne d'évènements de mort subite */
	private List<AiSimSuddenDeathEvent> internalSuddenDeathEvents = new ArrayList<AiSimSuddenDeathEvent>();

	/**
	 * Renvoie la représentation interne des évènements
	 * de mort subite.
	 * 
	 * @return
	 * 		Renvoie une liste d'évènements de mort subite.
	 */
	protected List<AiSimSuddenDeathEvent> getInternalSuddenDeathEvents()
	{	return internalSuddenDeathEvents;
	}
	
	/**
	 * Crée un évènement de mort subite. Il se produira à l'instant spécifié
	 * en ms, et verra apparaître les sprites indiqués dans la map passée en
	 * paramètre. La clé de cette map est une case, dans laquelle les sprites
	 * composant la valeur (une liste) apparaîtront lors de l'évènement.
	 * <p/>
	 * Les sprites doivent avoir été créés avec l'une des méthodes {@code createXxxx} 
	 * de cette classe, et surtout sans sans préciser leur case à la création (i.e.
	 * en utilisant la valeur {@code null} à la place de la case). En effet, si vous précisez
	 * une case, le sprite est directement inséré dans la zone de jeu, ce qui est en
	 * conflit avec la définition d'un évènement. Pour compenser, la case doit
	 * être indiquée lors de la création del'évènement, en tant que clé de la map passée
	 * en paramètre.
	 * <p/>
	 * Ce sont bien les sprites passés en paramètre, et pas des copies, qui vont
	 * être utilisés pour créer l'évènement.
	 * 
	 * @param time
	 * 		Instant de l'évènement à créer.
	 * @param sprites
	 * 		Map contenant les sprites devant apparaître, et la case d'apparition.
	 * @return
	 * 		L'évènement correspondant (qui est aussi enregistré dans cette zone).
	 */
	public AiSuddenDeathEvent createSuddenDeathEvent(long time, HashMap<AiTile, List<AiSprite>> sprites)
	{	List<AiSimSprite> s = new ArrayList<AiSimSprite>();
		
		for(Entry<AiTile,List<AiSprite>> entry: sprites.entrySet())
		{	AiTile tile = entry.getKey();
			AiSimTile t = matrix[tile.getRow()][tile.getCol()];
			double posX = t.getPosX();
			double posY = t.getPosY();
			double posZ = 0;
			List<AiSprite> list = entry.getValue();
			for(AiSprite sprite: list)
			{	AiSimSprite sprt = (AiSimSprite)sprite;
				sprt.setTile(t);
				sprt.setPos(posX, posY, posZ);
				s.add(sprt);
			}
		}
		
		AiSimSuddenDeathEvent result = new AiSimSuddenDeathEvent(time, s);
		suddenDeathEvents.add(result);
		internalSuddenDeathEvents.add(result);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * termine proprement cette simulation (une fois que l'agent n'en a plus besoin).
	 */
	public void finish()
	{	// matrix
		for(int row=0;row<height;row++)
			for(int col=0;col<width;col++)
				matrix[row][col].finish();
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
		
		// sudden death events
		suddenDeathEvents.clear();
		internalSuddenDeathEvents.clear();
	}
}
