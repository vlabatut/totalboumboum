package org.totalboumboum.ai.v201112.adapter.model.partial;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import java.util.Map.Entry;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.model.full.AiFullModel;
import org.totalboumboum.ai.v201112.adapter.model.full.AiFullModelTest;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe est chargée de simuler l'évolution d'une zone.
 * Pour cela, une modèle doit d'abord être initialisé avec une zone de départ,
 * obtenue simplement à partir des percepts de l'agent.<br/>
 * Par rapport à {@link AiFullModel}, il s'agit ici d'une représentation
 * simplifiée, définie spécifiquement pour A*. Au contraire, les
 * objets de classe {@link AiFullModel}, qui contiennent plus
 * d'information (la zone complète, en fait) sont plus appropriés
 * à des calculs liés à la précise de décision et à la stratégie de l'agent.<br/>
 * Pour prèserver la cohérence de la zone, l'utilisateur ne peut 
 * pas la modifier directement, mais seulement à travers les 
 * méthodes proposées dans cette classe. Il peut :
 * <ul>
 * 		<li>simuler un déplacement du joueur.</li>
 * 		<li>simuler une attente du jouru</li>
 * </ul>
 * Au cours de la simulation, le modèle est modifié et son ancien
 * état est donc perdu. Si vous voulez conserver l'ancien état,
 * il faut en faire une copie en re-créant un nouveau modèle à
 * partir de l'existant, avant d'y effectuer une simulation.<br/>
 * L'utilisateur peut également récupérer le temps écoulé entre deux simulations.<br/>
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
 * zone ne peut plus évoluer).<br/>
 * Vous pouvez observer une illustration du fonctionnement de ce modèle en exécutant
 * la classe {@link AiFullModelTest}. Notez toute fois que cette classe de test crée la zone
 * en partant de rien, alors que les agents disposent de leurs percepts.
 * Pour cette raison, elle utilise pour initialiser la zone des méthodes 
 * auxquelles les agents n'ont pas accès.
 * 
 * @author Vincent Labatut
 */
public class AiPartialModel
{	
	/**
	 * initialise le modèle avec la zone passée en paramètre.
	 * 
	 * @param zone
	 * 		la zone courante, qui servira de point de départ à la simulation.
	 */
	public AiPartialModel(AiZone zone)
	{	// zone
		this.zone = zone;
		this.width = zone.getWidth();
		this.height = zone.getHeight();
		
		// hero
		ownHero = zone.getOwnHero();
		ownLocation = new AiLocation(ownHero);
		
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
		ownHero = model.ownHero;
		ownLocation = model.ownLocation;
		
		// matrices
		obstacles = new boolean[height][width];
		explosions = new AiExplosionList[height][width];
		for(int row=0;row<height;row++)
		{	for(int col=0;col<width;col++)
			{	obstacles[row][col] = model.obstacles[row][col];
				if(model.explosions[row][col]!=null)
					explosions[row][col] = model.explosions[row][col].copy();
			}
		}
		
		// init map
		initMap();
	}
	
	/////////////////////////////////////////////////////////////////
	// ZONE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Zone de jeu. */
	private AiZone zone;
	/** Largeur de la zone de jeu. */
	private int width;
	/** Hauteur de la zone de jeu. */
	private int height;
	
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
	private AiHero ownHero;
	/** Emplacement virtuel de ce personnage */
	private AiLocation ownLocation;
	
	/** 
	 * Renvoie le personnage de référence.
	 * 
	 * @return
	 * 		Le personnage de référence.
	 */
	public AiHero getOwnHero()
	{	return ownHero;
	}
	
	/**
	 * Renvoie la position virtuelle
	 * du personnage de référence.
	 * 
	 * @return
	 * 		La position du personnage de référence.
	 */
	public AiLocation getOwnLocation()
	{	return ownLocation;
	}

	/////////////////////////////////////////////////////////////////
	// OBSTACLES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Matrice contenant tous les obstacles pour l'agent */
	private boolean[][] obstacles;
	
	/**
	 * Analyse la zone passée en paramètre et
	 * en déduit le contenu de la matrice des
	 * obstacles. On y distingue seulement trois
	 * types de cases : sans obstacles, avec
	 * des obstacles destructibles, ou avec
	 * des obstacles indestructibles.
	 * 
	 * @param zone
	 * 		La zone de référence.
	 */
	private void initObstacles(AiZone zone)
	{	List<AiTile> tiles = zone.getTiles();
		for(AiTile tile: tiles)
		{	boolean crossable = tile.isCrossableBy(ownHero);
				// pour éviter de considérer une bombe comme un obstacle
//				&& !tile.getBlocks().isEmpty(); // inutile, en fait : une bombe est un obstacle qui va disparaitre
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
	// EXPLOSIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Matrice contenant toutes les explosions prévues */
	private AiExplosionList[][] explosions;
	/** Map contenant toutes les explosions classées par instant de départ */
	private HashMap<Long,List<AiExplosion>> explosionMap;
	
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
		boolean result = explosions[row][col]==null || explosions[row][col].isEmpty();
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

		// on màj la matrice en fonction des bombes existantes
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
			long time = fire.getTime();
			long duration = fire.getBurningDuration();
			long endTime = duration - time;
			AiExplosionList list = explosions[row][col];
			if(list==null)
			{	list = new AiExplosionList();
				explosions[row][col] = list;
			}
			AiExplosion explosion = new AiExplosion(0,endTime,tile);
			list.add(explosion);
		}
	}
	
	/**
	 * Intègre les bombes dans la matrice
	 * d'explosions.
	 */
	private void initBombs()
	{	//HashMap<AiBomb,List<AiBomb>> threatenedBombs = zone.getThreatenedBombs();
		//HashMap<AiBomb,Long> delaysByBombs = zone.getDelaysByBombs();
		HashMap<Long,List<AiBomb>> bombsByDelays = zone.getBombsByDelays();
		
		// on traite les bombes par ordre d'explosion effectif
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
				List<AiTile> blast = bomb.getBlast();
				for(AiTile tile: blast)
				{	// check if the tile contains a hardwall
					List<AiBlock> blocks = tile.getBlocks();
					boolean hardwall = false;
					Iterator<AiBlock> it = blocks.iterator();
					while(it.hasNext() && !hardwall)
					{	AiBlock block = it.next();
						hardwall = !block.isDestructible();
					}
					
					// only ass tiles without any hardwall (these will always be obstacles anyway)
					if(!hardwall)
					{	int col = tile.getCol();
						int row = tile.getRow();
						AiExplosionList list = explosions[row][col];
						if(list==null)
						{	list = new AiExplosionList();
							explosions[row][col] = list;
						}
						AiExplosion explosion = new AiExplosion(delay,endTime,tile);
						list.add(explosion);
					}
				}
			}
		}
	}
	
	/**
	 * Initialise la map d'explosions à 
	 * partir de la matrice.
	 */
	private void initMap()
	{	explosionMap = new HashMap<Long, List<AiExplosion>>();
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
	// SIMULATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Durée de la dernière simulation */
	private long duration = 0;

	/**
	 * Renvoie la durée de la
	 * dernière simulation
	 * 
	 * @return
	 * 		La durée de la dernière simulation (en ms).
	 */
	public long getDuration()
	{	return duration;
	}
	
	/**
	 * Effectue une simulation jusqu'à ce que le
	 * personnage de référence ait changé de case
	 * dans la direction demandée, ou bien qu'il
	 * rencontre un obstacle, ou bien qu'il soit éliminé.
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
		double speed = ownHero.getWalkingSpeed();
		
		// on récupère la case de destination
		double ownX = ownLocation.getPosX();
		double ownY = ownLocation.getPosY();
		AiTile sourceTile = ownLocation.getTile();
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
			distance = zone.getPixelDistance(ownLocation,destinationTile,direction) + 1;
			double cp[] = zone.getContactPoint(ownLocation,destinationTile);
			destinationX = cp[0];
			destinationY = cp[1];
		}
		// temps nécessaire pour parcourir cette distance
		long timeNeeded = (long)Math.ceil(distance/speed * 1000);
	
		// on applique la simulation
		result = simulateExplosions(timeNeeded,direction);
		ownLocation = new AiLocation(destinationX,destinationY,zone);
		if(duration==0)
			duration = timeNeeded;
		
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
	 * 
	 * @param limit
	 * 		La durée d'attente souhaitée.
	 * @return
	 * 		{@code true} ssi le personnage a survécu à l'attente.
	 */
	public boolean simulateWait(long limit)
	{	boolean result = simulateExplosions(limit,Direction.NONE);
		return result;
	}

	/**
	 * Effectue une simulation d'explosion. La matrice et
	 * la map d'explosion sont mises à jour. La variable
	 * {@code duration} est également mise à jour pour une 
	 * durée pouvant être inférieure à {@code limit} en
	 * cas d'élimination du personnage. Le paramètre {@code direction}
	 * correspond à la direction de déplacement du personnage
	 * de référence en cas de déplacement, et à {@link Direction#NONE}
	 * en cas d'attente.
	 * 
	 * @param duration
	 * 		La durée d'attente souhaitée.
	 * @return
	 * 		{@code true} ssi le personnage a survécu à la simulation.
	 */
	public boolean simulateExplosions(long limit, Direction direction)
	{	// init
		boolean result = true;
		duration = 0;
		AiTile sourceTile = ownLocation.getTile();
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
		
		// on considère chaque explosion restant
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
				
				// si l'explosion s'achève avant la limite
				if(endTime<=limit)
				{	// elle est carrément supprimée de la liste de la map
					itExp.remove();
					// et aussi de la liste de la matrice
					explosions[row][col].remove(explosion);
					if(explosions[row][col].isEmpty())
						explosions[row][col] = null;
					// on màj la durée simulée
					duration = Math.max(duration,endTime);
					// on màj la matrice d'obstacles, car le contenu éventuel de la case disparait
					obstacles[row][col] = false;
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
				newMap.put(newStartTime,list);
		}
		
		// on met à jour la map
		explosionMap.clear();
		explosionMap.putAll(newMap);
		
		return result;
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
	 *  ┌─┬─┬─┬─┬─┬─┬─┐
	 * 0│█│█│█│█│█│█│█│	Légende:
	 *  ├─┼─┼─┼─┼─┼─┼─┤	┌─┐
	 * 1│█│☺│ │□│ │ │█│	│ │	case vide
	 *  ├─┼─┼─┼─┼─┼─┼─┤	└─┘
	 * 2│█│ │█│ │█│ │█│	 █ 	mur destructible non-menacé ou mur indestructible
	 *  ├─┼─┼─┼─┼─┼─┼─┤	 ▒ 	obstacle menacé (bombe ou mur destructible)
	 * 3│█│░│☻│ │ │▒│█│	 ☺ 	joueur non-menacé
	 *  ├─┼─┼─┼─┼─┼─┼─┤	 ☻ 	joueur menacé
	 * 4│█│░│█│ │█│ │█│	 ░	feu ou case vide menacée
	 *  ├─┼─┼─┼─┼─┼─┼─┤	  	case vide non-menacée
	 * 5│█│░│░│░│ │●│█│	 
	 *  ├─┼─┼─┼─┼─┼─┼─┤
	 * 6│█│█│█│█│█│█│█│
	 *  └─┴─┴─┴─┴─┴─┴─┘
	 * </pre>
	 * 
	 * @return
	 * 		Une représentation de la zone de type ASCII art.
	 */
	@Override
	public String toString()
	{	String result = "  ";
		AiTile ownTile = ownLocation.getTile();
		int ownRow = ownTile.getRow();
		int ownCol = ownTile.getCol();
	
		// col numbers
		if(width>10)
		{	for(int i=0;i<10;i++)
				result = result + "  ";
			for(int i=10;i<width;i++)
				result = result + " " + (i/10);
			result = result + "\n";
		}
		result = result + "  ";
		for(int i=0;i<width;i++)
			result = result + " " + (i%10);
		result = result + "\n";
		
		// top row
		result = result + "  ┌";
		for(int col=0;col<width-1;col++)
			result = result + "─┬";
		result = result + "─┐\n";
		
		// content
		for(int row=0;row<height;row++)
		{	// row number
			if(row<10)
				result = result + " ";
			result = result + row;
			// actual content
			for(int col=0;col<width;col++)
			{	result = result + "│";
				if(obstacles[row][col])
				{	if(explosions[row][col]==null)
						result = result + "█";
					else
						result = result + "▒";
				}
				else if(row==ownRow && col==ownCol)
				{	if(explosions[row][col]==null)
						result = result + "☺";
					else
						result = result + "☻";
				}
				else
				{	if(explosions[row][col]==null)
						result = result + " ";
					else
						result = result + "░";
				}
			}
			result = result + "│\n";
			if(row<height-1)
			{	result = result + "  ├";
				for(int col=0;col<width-1;col++)
					result = result + "─┼";
				result = result + "─┤\n";
			}
		}
		
		// bottom row
		result = result + "  └";
		for(int col=0;col<width-1;col++)
			result = result + "─┴";
		result = result + "─┘\n";
		
		return result;
	}
}
