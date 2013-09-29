package org.totalboumboum.ai.v201314.adapter.model.partial;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiSprite;
import org.totalboumboum.ai.v201314.adapter.data.AiStateName;
import org.totalboumboum.ai.v201314.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.model.full.AiFullModel;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.test.AstarUse;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Cette classe est chargée de simuler l'évolution d'une zone.
 * Pour cela, une modèle doit d'abord être initialisé avec une zone de départ,
 * obtenue simplement à partir des percepts de l'agent.
 * <br/>
 * Par rapport à {@link AiFullModel}, il s'agit ici d'une représentation
 * simplifiée, définie spécifiquement pour A*. Au contraire, les
 * objets de classe {@link AiFullModel}, qui contiennent plus
 * d'information (la zone complète, en fait) sont plus appropriés
 * à des calculs liés à la prise de décision et à la stratégie de l'agent.
 * <br/>
 * Pour préserver la cohérence de la zone, l'utilisateur ne peut 
 * pas la modifier directement, mais seulement à travers les 
 * méthodes proposées dans cette classe. Il peut :
 * <ul>
 * 		<li>simuler un déplacement du joueur.</li>
 * 		<li>simuler une attente du joueur</li>
 * </ul>
 * Au cours de la simulation, le modèle est modifié et son ancien
 * état est donc perdu. Si vous voulez conserver l'ancien état,
 * il faut en faire une copie en re-créant un nouveau modèle à
 * partir de l'existant, avant d'y effectuer une simulation.
 * <br/>
 * L'utilisateur peut également récupérer le temps écoulé entre deux simulations.
 * <br/>
 * A noter que la mort subite est prise en compte seulement partiellement, en
 * raison de sa nature partiellement aléatoire : quand un sprite tombe du ciel,
 * il est possible qu'il rebondisse au hasard si sa case d'atterrissage est déjà
 * occupée. La case dans laquelle ce sprite finira est complètement imprévisible,
 * et ne peut donc être simulée.
 * <br/>
 * Il faut souligner que les pas de simulation sont déterminés de façon évènementielle.
 * En d'autres termes, un pas se termine quand un évènement se produit. Les 
 * évènements considérés par cette classe sont :
 * <ul>
 * 		<li>la fin du déplacement demandé</li>
 * 		<li>la fin de l'attente demandée</li>
 * 		<li>l'élimination du joueur</li>
 * </ul>
 * Dès qu'un de ces évènements se produit, le pas de simulation se termine.
 * Si une attente est demandée alors qu'il ne reste pas d'explosion
 * à venir dans la zone, alors l'attente n'est pas réalisée (car la
 * zone ne peut plus évoluer).
 * <br/>
 * Vous pouvez observer une illustration du fonctionnement de ce modèle en exécutant
 * la classe {@link AstarUse}. Notez toute fois que cette classe de test crée la zone
 * en partant de rien, alors que les agents disposent de leurs percepts.
 * Pour cette raison, elle utilise pour initialiser la zone des méthodes 
 * auxquelles les agents n'ont pas accès.
 * 
 * @author Vincent Labatut
 */
public class AiPartialModel
{	
	/**
	 * Initialise le modèle avec la zone passée en paramètre.
	 * Le personnage de référence est automatiquement celui
	 * contrôlé par l'agent.
	 * 
	 * @param zone
	 * 		La zone courante, qui servira de point de départ à la simulation.
	 */
	public AiPartialModel(AiZone zone)
	{	this(zone,zone.getOwnHero());
	}	
	
	/**
	 * Initialise le modèle avec la zone passée en paramètre.
	 * Le personnage de référence est celui passé en paramètre.
	 * 
	 * @param zone
	 * 		La zone courante, qui servira de point de départ à la simulation.
	 * @param hero
	 * 		Le personnage de référence.
	 */
	public AiPartialModel(AiZone zone, AiHero hero)
	{	// zone
		this.zone = zone;
		this.width = zone.getWidth();
		this.height = zone.getHeight();
		
		// hero
		PredefinedColor color = hero.getColor();
		currentHero = zone.getHeroByColor(color);
		currentLocation = new AiLocation(currentHero);
		
		// durations
		totalDuration = zone.getTotalTime();
		
		// sudden death events
		initSuddenDeathEvents(zone);

		// obstacles
		obstacles = new boolean[height][width];
		initObstacles(zone);
		
		// explosions
		explosions = new AiExplosionList[height][width];
		initExplosions(zone);
	}	

	/**
	 * Initialise le modèle en effectuant une copie
	 * de celui passé en paramètre.
	 * 
	 * @param model
	 * 		Le modèle à copier.
	 */
	public AiPartialModel(AiPartialModel model)
	{	// dimensions
		this.zone = model.zone;
		this.width = model.width;
		this.height = model.height;
		
		// hero
		currentHero = model.currentHero;
		currentLocation = model.currentLocation;
		
		// durations
		totalDuration = model.totalDuration;
		
		// sudden death events
		suddenDeathEvents.addAll(model.suddenDeathEvents);
		
		// matrices
		obstacles = new boolean[height][width];
		explosions = new AiExplosionList[height][width];
		for(int row=0;row<height;row++)
		{	for(int col=0;col<width;col++)
			{	obstacles[row][col] = model.obstacles[row][col];
				if(model.explosions[row][col]!=null)
				{	AiExplosionList list1 = model.explosions[row][col];
					AiExplosionList list2 = list1.copy();
					explosions[row][col] = list2;
				}
			}
		}
		
		// init map
		initMap();
	}
	
	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Zone de jeu. */
	private final AiZone zone;
	/** Largeur de la zone de jeu. */
	private int width;
	/** Hauteur de la zone de jeu. */
	private int height;
	
	/**
	 * Renvoie la zone ayant servi à initialiser
	 * ce modèle partiel.
	 * 
	 * @return
	 * 		La {@link AiZone} ayant servi à initialiser ce modèle partiel.
	 */
	public AiZone getOriginalZone()
	{	return zone;
	}
	
	/** 
	 * Renvoie la hauteur de la zone de jeu
	 * (en cases).
	 *  
	 *  @return	
	 *  	Hauteur de la zone de jeu.
	 */
	public int getHeight()
	{	return height;	
	}
	
	/** 
	 * Renvoie la largeur de la zone de jeu
	 * (en cases).
	 *  
	 *  @return	
	 *  	Largeur de la zone de jeu.
	 */
	public int getWidth()
	{	return width;	
	}
	
	/////////////////////////////////////////////////////////////////
	// HERO				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Personnage de référence */
	private final AiHero currentHero;
	/** Emplacement virtuel de ce personnage */
	private AiLocation currentLocation;
	
	/** 
	 * Renvoie le personnage de référence.
	 * 
	 * @return
	 * 		Le personnage de référence.
	 */
	public AiHero getCurrentHero()
	{	return currentHero;
	}
	
	/**
	 * Renvoie la position virtuelle
	 * du personnage de référence.
	 * 
	 * @return
	 * 		La position du personnage de référence.
	 */
	public AiLocation getCurrentLocation()
	{	return currentLocation;
	}

	/////////////////////////////////////////////////////////////////
	// OBSTACLES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Matrice contenant tous les obstacles pour l'agent */
	private boolean[][] obstacles;
	
	/**
	 * Analyse la zone passée en paramètre et
	 * en déduit le contenu de la matrice des
	 * obstacles. On y distingue seulement deux
	 * types de cases : avec ou sans obstacle.
	 * On ignore ici l'aspect destructible ou
	 * indestructible des murs.
	 * 
	 * @param zone
	 * 		La zone de référence.
	 */
	private void initObstacles(AiZone zone)
	{	List<AiTile> tiles = zone.getTiles();
		for(AiTile tile: tiles)
		{	boolean crossable = tile.isCrossableBy(currentHero);
				// pour éviter de considérer une bombe comme un obstacle
//				&& !tile.getBlocks().isEmpty(); // inutile, en fait : une bombe est un obstacle qui va disparaitre
			// cas particulier : bombe posée par l'agent
			if(crossable)
			{	List<AiBomb> bombs = tile.getBombs();
				if(!bombs.isEmpty())
				{	AiBomb bomb = bombs.get(0);
					AiHero owner = bomb.getOwner();
					if(owner==null || owner.equals(currentHero))
						crossable = false;
				}
			}
			// cas particulier : bloc en train d'atterrir >> considéré comme un obstacle (même si pas obstacle)
			if(crossable)
			{	List<AiBlock> blocks = tile.getBlocks();
				if(!blocks.isEmpty())
				{	AiBlock block = blocks.get(0);
					if(block.getState().getName()==AiStateName.FLYING)
						crossable = false;
				}
			}
			
			// on met à jour la matrice
			int col = tile.getCol();
			int row = tile.getRow();
			obstacles[row][col] = !crossable;
		}
	}
	
	/**
	 * Permet de savoir si une case contient
	 * un obstacle pour le joueur, ou pas.
	 * 
	 * @param tile
	 * 		La case à tester.
	 * @return
	 * 		{@code true} ssi la case est un obstacle pour le joueur de référence.
	 */
	public boolean isObstacle(AiTile tile)
	{	int row = tile.getRow();
		int col = tile.getCol();
		boolean result = obstacles[row][col];
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Évènements de mort subite */
	private final List<AiSuddenDeathEvent> suddenDeathEvents = new ArrayList<AiSuddenDeathEvent>();

	/**
	 * Intialise la liste d'évènements de mort
	 * subite. on ne garde que ceux qui ne se sont
	 * pas encore réalisés.
	 * 
	 * @param zone
	 * 		La zone utilisée pour initialiser ce modèle.
	 */
	private void initSuddenDeathEvents(AiZone zone)
	{	List<AiSuddenDeathEvent> events = zone.getAllSuddenDeathEvents();

		// detect the next event which hasn't happened yet
		int i = 0;
		long time = 0;
		while(i<events.size() && time<totalDuration)
		{	AiSuddenDeathEvent event = events.get(i);
			time = event.getTime();
			if(time<totalDuration)
				i++;
		}
		
		// keep only the following ones
		for(int j=i;j<events.size();j++)
			suddenDeathEvents.add(events.get(j));
	}
	
	/////////////////////////////////////////////////////////////////
	// EXPLOSIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Matrice contenant toutes les explosions prévues */
	private AiExplosionList[][] explosions;
	/** Map contenant toutes les explosions classées par instant de départ */
	private final Map<Long,List<AiExplosion>> explosionMap = new HashMap<Long, List<AiExplosion>>();
	
	/**
	 * Renvoie la première explosion disponible
	 * pour la case passée en paramètre, ou
	 * {@code null} si aucune explosion n'existe
	 * pour cette case.
	 * 
	 * @param tile
	 * 		La case à traiter.
	 * @return
	 * 		La prochaine explosion de la case spécifiée.
	 */
	public AiExplosion getExplosion(AiTile tile)
	{	AiExplosion result = null;
		int row = tile.getRow();
		int col = tile.getCol();
		AiExplosionList list = explosions[row][col];
		if(list!=null)
			result = list.first();
		return result;
	}
	
	/**
	 * Renvoie la liste des explosions disponibles
	 * pour la case passée en paramètre, ou
	 * {@code null} si aucune liste n'existe
	 * pour cette case.
	 * 
	 * @param tile
	 * 		La case à traiter.
	 * @return
	 * 		La liste des explosions pour la case spécifiée.
	 */
	public AiExplosionList getExplosionList(AiTile tile)
	{	int row = tile.getRow();
		int col = tile.getCol();
		AiExplosionList result = explosions[row][col];
		return result;
	}
	
	/**
	 * Permet de savoir si une case contient
	 * une explosion ou pas.
	 * 
	 * @param tile
	 * 		La case à tester.
	 * @return
	 * 		{@code true} ssi la case contient au moins une explosion.
	 */
	public boolean isThreatened(AiTile tile)
	{	int row = tile.getRow();
		int col = tile.getCol();
		AiExplosionList explosionList = explosions[row][col];
		boolean result = explosionList!=null && !explosionList.isEmpty();
		return result;
	}
	
	/**
	 * Analyse la zone passée en paramètre et
	 * en déduit le contenu de la matrice des
	 * explosions.
	 * 
	 * @param zone
	 * 		La zone de référence.
	 */
	private void initExplosions(AiZone zone)
	{	// on màj la matrice en fonction des feux existants
		initFires();

		// on màj la matrice en fonction des bombes existantes et à venir
		initBombs();
		
		// on remplit la map contenant les explosions
		initMap();
	}
	
	/**
	 * Intègre les feux dans la matrice
	 * d'explosions.
	 */
	private void initFires()
	{	List<AiFire> fires = zone.getFires();
		for(AiFire fire: fires)
		{	AiTile tile = fire.getTile();
			int col = fire.getCol();
			int row = fire.getRow();
			long time = fire.getElapsedTime();
			long duration = fire.getBurningDuration();
			long endTime = duration - time;
			AiExplosionList list = explosions[row][col];
			if(list==null)
			{	list = new AiExplosionList(tile);
				explosions[row][col] = list;
			}
			AiExplosion explosion = new AiExplosion(0,endTime,tile);
			list.add(explosion);
		}
	}
	
	/**
	 * Intègre les bombes dans la matrice d'explosions.
	 * Toutes les cases appartenant au blast des bombes sont
	 * ajoutées dans la matrice. Les explosions précédentes
	 * et les évènements de mort subite sont pris en compte
	 * (approximativement, pour ces derniers). 
	 */
	private void initBombs()
	{	//Map<AiBomb,List<AiBomb>> threatenedBombs = zone.getThreatenedBombs();
		//Map<AiBomb,Long> delaysByBombs = zone.getDelaysByBombs();
		Map<Long,List<AiBomb>> bombsByDelays = zone.getBombsByDelays();
		
		// process bombs by order of explosion
		TreeSet<Long> orderedDelays = new TreeSet<Long>(bombsByDelays.keySet());
		while(!orderedDelays.isEmpty())
		{	// get the delay
			long delay = orderedDelays.first();
			orderedDelays.remove(delay);
			// get the bombs associated to this delay
			List<AiBomb> bombList = bombsByDelays.get(delay);
			
			// process each bomb for the current delay
			for(AiBomb bomb: bombList)
			{	// add explosion to the matrix
				long endTime = delay + bomb.getExplosionDuration();
				// get the bomb blast
				List<AiTile> blast = getBlast(delay,bomb);
				// add each tile to the explosion matrix
				for(AiTile tile: blast)
				{	int col = tile.getCol();
					int row = tile.getRow();
					AiExplosionList list = explosions[row][col];
					if(list==null)
					{	list = new AiExplosionList(tile);
						explosions[row][col] = list;
					}
					AiExplosion explosion = new AiExplosion(delay,endTime,tile);
					list.add(explosion);
				}
			}
		}
		
		// process sudden death event by order of occurrence
		for(AiSuddenDeathEvent event: suddenDeathEvents)
		{	long time = event.getTime() - totalDuration;
			List<AiTile> tiles = event.getTiles();
			for(AiTile tile: tiles)
			{	List<AiSprite> sprites = event.getSpritesForTile(tile);
				// check if a bomb is going to fall
				AiBomb bomb = null;
				boolean noBomb = false;
				for(AiSprite sprite: sprites)
				{	if(sprite instanceof AiBomb)
						bomb = (AiBomb) sprite;
					if(sprite instanceof AiBlock)
						noBomb = true;
				}
				if(bomb!=null && !noBomb)
				{	// add explosion to the matrix
					long startTime;
					// start time depends on whether the tile is already in an explosion
					{	int col = tile.getCol();
						int row = tile.getRow();
						AiExplosionList list = explosions[row][col];
						if(list!=null && list.getIntersection(time,time+1)!=null)
							startTime = time + bomb.getLatencyDuration();
						else
							startTime = time + bomb.getNormalDuration() - bomb.getElapsedTime();
					}
					long endTime = startTime + bomb.getExplosionDuration();
					// get the bomb blast
					List<AiTile> blast = getBlast(time,bomb);
					// add each tile to the explosion matrix
					for(AiTile t: blast)
					{	int col = t.getCol();
						int row = t.getRow();
						AiExplosionList list = explosions[row][col];
						if(list==null)
						{	list = new AiExplosionList(t);
							explosions[row][col] = list;
						}
						AiExplosion explosion = new AiExplosion(startTime,endTime,t);
						list.add(explosion);
					}
				}
			}
		}
	}
	
	/**
	 * Cette méthode identifie les cases qui seront touchées
	 * par l'explosion de la bombe passée en paramètre.
	 * <br/>
	 * La méthode {@link AiBomb#getBlast} n'est pas utilisable ici,
	 * car elle ne tient pas compte de l'évolution précedente de
	 * la zone. Il faut donc explicitement faire ce calcul ici.
	 * C'est aussi dans cette méthode qu'on tiendra compte de la mort
	 * subite (partiellement).
	 * <br/>
	 * <b>Note :</b> la liste renvoyée est générée à la demande.
	 * Elle peut donc être modifiée sans problème par l'agent.
	 *  
	 * @param start
	 * 		Instant d'explosion de la bombe.
	 * @param bomb
	 * 		La bombe qu'on veut traiter.
	 * @return
	 *		Une liste de cases (peut être vide, en fonction de la bombe).
	 */
	private List<AiTile> getBlast(long start, AiBomb bomb)
	{	List<AiTile> result = new ArrayList<AiTile>();
		int range = bomb.getRange();
		
		// center
		AiTile tile = bomb.getTile();
		result.add(tile);
		
		// branches
		boolean blocked[] = {false,false,false,false};
		AiTile tiles[] = {tile,tile,tile,tile};
		Direction directions[] = {Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.UP};
		List<AiTile> processed = new ArrayList<AiTile>();
		processed.add(tile);
		boolean goOn = true;
		int length = 1;
		while(goOn && length<=range)
		{	goOn = false;
			// increase the explosion
			for(int i=0;i<directions.length;i++)
			{	if(!blocked[i])
				{	// get the tile
					Direction direction = directions[i];
					AiTile tempTile = tiles[i].getNeighbor(direction);
					tiles[i] = tempTile;
					if(!processed.contains(tempTile))
					{	processed.add(tempTile);
						boolean inRange = true;
					
						// get the tile content at this time
						AiSprite sprite = getTileContentAtTime(tempTile,start);
						if(sprite!=null)
						{	blocked[i] = true;
							if(sprite instanceof AiBlock)
								inRange = ((AiBlock)sprite).isDestructible();
						}
						
						// finishing the tile process
						goOn = goOn || !blocked[i];
						if(inRange)
							result.add(tempTile);
					}
				}
			}
			length++;
		}
	
		return result;
	}
	
	/**
	 * Méthode utilisée pour calculer le blast des bombes.
	 * Elle renvoie le contenu d'une case à l'instant spécifié.
	 * Les explosions et évènements de mort subite sont pris
	 * en compte (de façon approximative, pour ces derniers).
	 * 
	 * @param tile
	 * 		La case dont on veut le contenu.
	 * @param time
	 * 		L'instant ciblé.
	 * @return
	 * 		Un sprite représentant le contenu de la case, ou {@code null}
	 * 		si elle ne contient pas d'obstacle.
	 */
	private AiSprite getTileContentAtTime(AiTile tile, long time)
	{	AiSprite result = null;
		AiSprite temp = null;
		int row = tile.getRow();
		int col = tile.getCol();
		
		// init
		List<AiBlock> blocks = tile.getBlocks();
		List<AiItem> items = tile.getItems();
		List<AiBomb> bombs = tile.getBombs();
		if(!blocks.isEmpty())
		{	temp = blocks.get(0);
			if(!((AiBlock)temp).isDestructible())
				result = temp;
		}
		else if(!items.isEmpty())
		{	temp = items.get(0);
		}
		else if(!bombs.isEmpty())
		{	temp = bombs.get(0);
		}
		
		if(result == null)
		{	// retrieve all related sudden death events
			List<AiSuddenDeathEvent> relatedSde = new ArrayList<AiSuddenDeathEvent>();
			Iterator<AiSuddenDeathEvent> it = suddenDeathEvents.iterator();
			if(it.hasNext())
			{	AiSuddenDeathEvent s;
				long sdTime; 
				do
				{	s = it.next();
					sdTime = s.getTime() - totalDuration;
					if(sdTime<=time)
						relatedSde.add(s);
				}
				while(it.hasNext() && sdTime<=time);
			}
			
			// (approximate) simulation
			it = relatedSde.iterator();
			Iterator<AiExplosion> it2;
			if(explosions[row][col] == null)
				it2 = new AiExplosionList().iterator();
			else
				it2 = explosions[row][col].iterator();
			long time1 = Long.MAX_VALUE;
			long time2 = Long.MAX_VALUE;
			AiSuddenDeathEvent evt = null;
			while(result==null && (it.hasNext() || it2.hasNext()))
			{	// get the respective times
				if(time1==Long.MAX_VALUE && it.hasNext())
				{	evt = it.next();
					time1 = evt.getTime() - totalDuration;
				}
				if(time2==Long.MAX_VALUE && it2.hasNext())
				{	AiExplosion e = it2.next();
					time2 = e.getStart();
				}
				
				// a sudden death event occurs
				if(time1<time2)
				{	List<AiSprite> list = evt.getSpritesForTile(tile);
					for(AiSprite s: list)
					{	if(s instanceof AiBlock)
						{	temp = s;	
							if(!((AiBlock)s).isDestructible())
								result = temp;
						}
						else if(s instanceof AiItem || s instanceof AiBomb)
						{	if(temp == null)
								temp = s;
						}
					}
					time1 = Long.MAX_VALUE;
				}
				// an explosion occurs 
				else if(time2<time1 || (time1==time2 && time2!=Long.MAX_VALUE))
				{	temp = null;
					time2 = Long.MAX_VALUE;
				}
			}
			
			if(result==null)
				result = temp;
		}
		
		return result;
	}
	
	/**
	 * Initialise la map d'explosions à partir de la matrice.
	 */
	private void initMap()
	{	explosionMap.clear();
		for(int row=0;row<height;row++)
		{	for(int col=0;col<width;col++)
			{	AiExplosionList expls = explosions[row][col];
				if(expls!=null)
				{	for(AiExplosion explosion: expls)
					{	long startTime = explosion.getStart();
						List<AiExplosion> list = explosionMap.get(startTime);
						if(list==null)
						{	list = new ArrayList<AiExplosion>();
							explosionMap.put(startTime,list);
						}
						list.add(explosion);
					}
				}
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Durée totale depuis le début de la simulation */
	private long totalDuration;
	/** Durée de la dernière simulation */
	private long duration = 0;

	/**
	 * Renvoie la durée totale depuis
	 * le début de la simulation.
	 * 
	 * @return
	 * 		La durée totale depuis le début de la simulation (en ms).
	 */
	public long getTotalDuration()
	{	return totalDuration;
	}
	
	/**
	 * Renvoie la durée de la
	 * dernière simulation.
	 * 
	 * @return
	 * 		La durée de la dernière simulation (en ms).
	 */
	public long getDuration()
	{	return duration;
	}
	
	/////////////////////////////////////////////////////////////////
	// SIMULATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Effectue une simulation jusqu'à ce que le
	 * personnage de référence ait changé de case
	 * dans la direction demandée, ou bien qu'il
	 * rencontre un obstacle, ou bien qu'il soit éliminé.
	 * <br/>
	 * En cas d'élimination, la fonction renvoie {@code false}
	 * (et si tout va bien, elle renvoie {@code true}. Attention
	 * de bien tenir compte de ce résultat, car si, après une 
	 * élimination, vous demandez à nouveau une simulation sur ce modèle, 
	 * cette simulation sera effectuée comme si l'élimination n'avait pas
	 * eu lieu.
	 * 
	 * @param direction
	 * 		La direction du déplacement.
	 * @return
	 * 		{@code true} ssi le personnage n'a pas été éliminé 
	 * 		au cours de la simulation.
	 */
	public boolean simulateMove(Direction direction)
	{	// init
		boolean result = true;
		double speed = currentHero.getWalkingSpeed();
		
		// on récupère la case de destination
		double ownX = currentLocation.getPosX();
		double ownY = currentLocation.getPosY();
		AiTile sourceTile = currentLocation.getTile();
		double sourceX = sourceTile.getPosX();
		double sourceY = sourceTile.getPosY();
		AiTile destinationTile = sourceTile.getNeighbor(direction);
		int destinationRow = destinationTile.getRow();
		int destinationCol = destinationTile.getCol();
		double destinationX = ownX;
		double destinationY = ownY;
		
		// distance à parcourir pour changer de case
		double distance = 0;
		if(obstacles[destinationRow][destinationCol])
		{	// si la case d'arrivée contient un obstacle infranchissable, on avance jusqu'à lui seulement
			Direction effectiveDirection = zone.getDirection(ownX,ownY,sourceX,sourceY); // on teste si on est du bon côté de la case
			if(direction==effectiveDirection)
			{	distance = zone.getPixelDistance(ownX,ownY,sourceX,sourceY);
				destinationX = sourceX;
				destinationY = sourceY;
			}
		}
		else
		{	// sinon on considère le point le plus proche dans cette case
			distance = zone.getPixelDistance(currentLocation,destinationTile,direction);
			double cp[] = zone.getContactPoint(currentLocation,destinationTile,true);
			destinationX = cp[0];
			destinationY = cp[1];
		}
		// temps nécessaire pour parcourir cette distance
		long timeNeeded = (long)Math.ceil(distance/speed * 1000);
	
		// on applique la simulation
		result = simulateTime(timeNeeded,direction);
		currentLocation = new AiLocation(destinationX,destinationY,zone);
		if(duration==0)
		{	duration = timeNeeded;
			totalDuration = totalDuration + duration;
		}
		
		return result;
	}
	
	/**
	 * Effectue une simulation d'attente pour la durée
	 * spécifiée. Si le personnage est éliminé avant
	 * la fin de l'attente, la fonction renvoie {@code false},
	 * sinon elle renvoie {@code true}. La variable
	 * {@code duration} est mise à jour avec la durée
	 * attendue (qui peut être inférieure à {@code limit}
	 * si le personnage a été éliminé avant la fin de l'attente).
	 * <br/>
	 * Attention de bien tenir compte du résultat de la méthode.
	 * En effet, après une élimination, si vous demandez à nouveau 
	 * une simulation sur ce modèle, cette simulation sera effectuée 
	 * comme si l'élimination n'avait pas eu lieu.
	 * 
	 * @param limit
	 * 		La durée d'attente souhaitée.
	 * @return
	 * 		{@code true} ssi le personnage a survécu à l'attente.
	 */
	public boolean simulateWait(long limit)
	{	boolean result = simulateTime(limit,Direction.NONE);
		return result;
	}

	/**
	 * Effectue une simulation générale du passage du temps. La 
	 * matrice et la map d'explosion sont mises à jour. La matrice
	 * des obstacles aussi, en fonction des évènements de mort
	 * subite.
	 * <br/>
	 * La variable {@code duration} est également mise à jour 
	 * pour une durée pouvant être inférieure à {@code limit} en
	 * cas d'élimination du personnage. Le paramètre {@code direction}
	 * correspond à la direction de déplacement du personnage
	 * de référence en cas de déplacement, et à {@link Direction#NONE}
	 * en cas d'attente.
	 * <br/>
	 * En cas d'élimination, la fonction renvoie {@code false}
	 * (et si tout va bien, elle renvoie {@code true}. Attention
	 * de bien tenir compte de ce résultat, car si, après une 
	 * élimination, vous demandez à nouveau une simulation sur ce modèle, 
	 * cette simulation sera effectuée comme si l'élimination n'avait pas
	 * eu lieu.
	 * 
	 * @param limit
	 * 		La durée d'attente souhaitée.
	 * @param direction
	 * 		La direction du déplacement éventuel du personnage.
	 * @return
	 * 		{@code true} ssi le personnage a survécu à la simulation.
	 */
	public boolean simulateTime(long limit, Direction direction)
	{	// init
		boolean result = true;
		duration = 0;
		AiTile sourceTile = currentLocation.getTile();
		int sourceRow = sourceTile.getRow();
		int sourceCol = sourceTile.getCol();
		AiTile destinationTile = sourceTile.getNeighbor(direction);
		int destinationRow = destinationTile.getRow();
		int destinationCol = destinationTile.getCol();
		
		// on calcule quand les cases source et destination vont exploser
		{	AiExplosionList sourceExplList = explosions[sourceRow][sourceCol];
			if(sourceExplList!=null)
			{	long sourceExplTime = sourceExplList.first().getStart();
				// si ça se produit avant la limite, on la met à jour
				if(sourceExplTime<=limit)
				{	limit = sourceExplTime;
					result = false;
				}
			}
			if(!sourceTile.equals(destinationTile))
			{	AiExplosionList destinationExplList = explosions[destinationRow][destinationCol];
				if(destinationExplList!=null)
				{	long destinationExplTime = destinationExplList.first().getStart();
					// si ça se produit avant la limite, on la met à jour
					if(destinationExplTime<=limit)
					{	limit = destinationExplTime;
						result = false;
					}
				}
			}
		}
		
		// on calcule quand les cases source et destination vont être écrasées lors d'un évènement de mort subite
		{	long time = 0;
			// on teste chaque évènement
			Iterator<AiSuddenDeathEvent> it = suddenDeathEvents.iterator();
			while(time<limit && it.hasNext())
			{	// on récupère le temps
				AiSuddenDeathEvent event = it.next();
				time = event.getTime() - totalDuration;
				if(time<=limit)
				{	// on teste chacune des deux cases de déplacement
					List<AiTile> tiles = Arrays.asList(sourceTile,destinationTile);
					Iterator<AiTile> it2 = tiles.iterator();
					boolean found = false;
					while(it2.hasNext() && !found)
					{	AiTile tile = it2.next();
						// on teste chaque sprite de l'évènement
						List<AiSprite> sprites = event.getSpritesForTile(tile);
						Iterator<AiSprite> it3 = sprites.iterator();
						while(it3.hasNext() && !found)
						{	AiSprite sprite = it3.next();
							found = sprite instanceof AiBlock;
						}
					}
					if(found)
					{	limit = time;
						result = false;
					}
				}
			}
		}
		
		// on fait les mises à jour pour la limite de temps fixée
		updateExplosions(limit);
		updateSuddenDeath(limit);
		
		// mise à jour du temps
		totalDuration = totalDuration + duration;
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// UPDATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Complète la simulation en réalisant les explosions
	 * devant avoir lieu avant la limite de temps spécifiée
	 * en paramètre.
	 * 
	 * @param limit
	 * 		Limite de temps de la mise à jour.
	 */
	private void updateExplosions(long limit)
	{	// on considère chaque explosion restant
		HashMap<Long,List<AiExplosion>> newMap = new HashMap<Long, List<AiExplosion>>();
		Iterator<Entry<Long,List<AiExplosion>>> itMap = explosionMap.entrySet().iterator();
		while(itMap.hasNext())
		{	// on récupère la liste d'explosions
			Entry<Long,List<AiExplosion>> entry = itMap.next();
			List<AiExplosion> list = entry.getValue();
			
			// on calcule le nouveau temps de démarrage pour toutes les explosions de la liste
			long startTime = entry.getKey();
			long newStartTime = Math.max(0,startTime-limit);
			
			// on traite séparément chaque explosion de la liste
			Iterator<AiExplosion> itExp = list.iterator();
			while(itExp.hasNext())
			{	// init
				AiExplosion explosion = itExp.next();
 				long endTime = explosion.getEnd();
				AiTile tile = explosion.getTile();
				int col = tile.getCol();
				int row = tile.getRow();
				
				// si l'explosion a commencé
				if(startTime==0)
				{	// on màj la matrice d'obstacles, car le contenu éventuel de la case disparait
					obstacles[row][col] = false;
				}
				
				// si l'explosion s'achève avant la limite
				if(endTime<=limit)
				{	// on màj la matrice d'obstacles, car le contenu éventuel de la case disparait
					obstacles[row][col] = false;
					// elle est carrément supprimée de la liste de la map
					itExp.remove();
					// et aussi de la liste de la matrice
					explosions[row][col].remove(explosion);
					if(explosions[row][col].isEmpty())
						explosions[row][col] = null;
					// on màj la durée simulée
					duration = Math.max(duration,endTime);
				}
				// sinon on met juste à jour les temps de démarrage/fin
				else
				{	// modifs valables à la fois pour les listes de la map et de la matrice
					// màj du temps de départ
					explosion.setStart(newStartTime);
					// màj du temps de fin
					long newEndTime = endTime - limit;
					explosion.setEnd(newEndTime);
					
					// màj de la durée simulée
					duration = Math.max(duration,limit);
				}
				
				// si la case concernée est source ou destination du personnage, il est éliminé
				//result = result && !tile.equals(sourceTile) && tile.equals(destinationTile);
				// en fait, déjà décidé avant la boucle
			}
			
			// on sort la liste de la map
			itMap.remove();
			// si elle n'est pas vide, on l'ajoute dans la nouvelle map avec le nouveau temps de départ
			if(!list.isEmpty())
			{	List<AiExplosion> temp = newMap.get(newStartTime);
				if(temp==null)
					newMap.put(newStartTime,list);
				else
					temp.addAll(list);
			}
		}
		
		// on met à jour la map
		explosionMap.clear();
		explosionMap.putAll(newMap);
	}

	/**
	 * Complète la simulation en réalisant les explosions
	 * devant avoir lieu avant la limite de temps spécifiée
	 * en paramètre.
	 * 
	 * @param limit
	 * 		Limite de temps de la mise à jour.
	 */
	private void updateSuddenDeath(long limit)
	{	long time = 0;
		// on teste chaque évènement
		Iterator<AiSuddenDeathEvent> it = suddenDeathEvents.iterator();
		while(time<limit && it.hasNext())
		{	// on récupère le temps
			AiSuddenDeathEvent event = it.next();
			time = event.getTime() - totalDuration;
			if(time<=limit)
			{	// on teste chaque sprite de l'évènement
				List<AiSprite> sprites = event.getSprites();
				for(AiSprite sprite: sprites)
				{	if(sprite instanceof AiBlock
						|| sprite instanceof AiBomb)
					{	AiTile tile = sprite.getTile();
						int row = tile.getRow();
						int col = tile.getCol();
						// on vérifie si la case n'est pas déjà occupée par une explosion
						AiExplosionList list = explosions[row][col];
						obstacles[row][col] = (sprite instanceof AiBlock && !((AiBlock)sprite).isDestructible())
							|| list==null || list.getIntersection(time,time+1)==null;
					}
				}
				// on supprime de la liste
				it.remove();
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Cette fonction permet d'afficher la zone représentée dans ce modèle
	 * sous forme d'ASCII art, ce qui est beaucoup plus lisible que du texte classique.
	 * <br/>
	 * <b>Attention :</b> pour avoir un affichage correct avec la console Eclipse, il faut
	 * aller dans la configuration de démarrage du programme, aller
	 * dans l'onglet "Commnon" puis dans la partie "Console Encoding" et
	 * sélectionner UTF8 ou unicode.
	 * <br/>
	 * Voici un exemple d'affichage obtenu :<
	 * <pre>
	 *   0 1 2 3 4 5 6
	 *  ╔═╦═╦═╦═╦═╦═╦═╗
	 * 0║█║█║█║█║█║█║█║	Légende:
	 *  ╠═╬═╬═╬═╬═╬═╬═╣	╔═╗
	 * 1║█║☺║ ║ ║ ║◊║█║	║ ║	case vide non-menacée
	 *  ╠═╬═╬═╬═╬═╬═╬═╣	╚═╝
	 * 2║█║ ║█║ ║█║▲║█║	 █ 	mur destructible non-menacé ou mur indestructible
	 *  ╠═╬═╬═╬═╬═╬═╬═╣	 ◊ 	obstacle menacé (bombe ou mur destructible)
	 * 3║█║░║ ║☻║▲║◊║█║	 ☺ 	joueur non-menacé
	 *  ╠═╬═╬═╬═╬═╬═╬═╣	 ☻ 	joueur menacé par une explosion à venir
	 * 4║█║░║█║ ║█║▲║█║	 ░	feu présent
	 *  ╠═╬═╬═╬═╬═╬═╬═╣  ▲	case vide pour l'instant, mais menacée
	 * 5║█║░║░║░║ ║▲║█║
	 *  ╠═╬═╬═╬═╬═╬═╬═╣
	 * 6║█║█║█║█║█║█║█║
	 *  ╚═╩═╩═╩═╩═╩═╩═╝
	 * </pre>
	 * 
	 * @return
	 * 		Une représentation de la zone de type ASCII art.
	 */
	@Override
	public String toString()
	{	StringBuffer result = new StringBuffer();
		AiTile ownTile = currentLocation.getTile();
		int ownRow = ownTile.getRow();
		int ownCol = ownTile.getCol();
	
		// col numbers
		if(width>10)
		{	result.append("  ");
			for(int i=0;i<10;i++)
				result.append("  ");
			for(int i=10;i<width;i++)
			{	result.append(" ");
				result.append(i/10);
			}
			result.append("\n");
		}
		result.append("  ");
		for(int i=0;i<width;i++)
		{	result.append(" ");
			result.append(i%10);
		}
		result.append("\n");
		
		// top row
		result.append("  ╔");
		for(int col=0;col<width-1;col++)
			result.append("═╦");
		result.append("═╗\n");
		
		// content
		for(int row=0;row<height;row++)
		{	// row number
			if(row<10)
				result.append(" ");
			result.append(row);
			// actual content
			for(int col=0;col<width;col++)
			{	result.append("║");
				if(obstacles[row][col])
				{	// regular obstacle (hardwall or non-threatened softwall)
					if(explosions[row][col]==null)
						result.append("█");
					// threatened softwall or bomb
					else
						result.append("◊");
				}
				else if(row==ownRow && col==ownCol)
				{	// non-threatened player
					if(explosions[row][col]==null)
						result.append("☺");
					// threatened or dead player
					else
						result.append("☻");
				}
				else
				{	// non-threatened empty tile
					if(explosions[row][col]==null)
						result.append(" ");
					// fire
					else if(explosions[row][col].first().getStart()==0)
						result.append("░");
					// threatened empty tile
					else
						result.append("▲");
				}
			}
			result.append("║\n");
			if(row<height-1)
			{	result.append("  ╠");
				for(int col=0;col<width-1;col++)
					result.append("═╬");
				result.append("═╣\n");
			}
		}
		
		// bottom row
		result.append("  ╚");
		for(int col=0;col<width-1;col++)
			result.append("═╩");
		result.append("═╝\n");
		
		return result.toString();
	}
	
	/**
	 * Renvoie une chaîne de caractères contenant
	 * le temps de la première explosion
	 * contenue dans chaque case.
	 * 
	 * @param start
	 * 		Si {@code true}, alors les temps de début d'explosions seront affichés ;
	 * 		si {@code false}, alors ce seront ceux de fin.
	 * @return
	 * 		Une représentation textuelle des temps d'explosion.
	 */
	public String toStringDelays(boolean start)
	{	StringBuffer result = new StringBuffer();
		for(int row=0;row<height;row++)
		{	for(int col=0;col<width;col++)
			{	result.append("\t");
				AiExplosionList list = explosions[row][col];
				if(list==null)
					result.append("+∞");
				else
				{	if(start)
						result.append(list.first().getStart());
					else
						result.append(list.first().getEnd());
				}
			}
			result.append("\n");
		}
		return result.toString();
	}
}
