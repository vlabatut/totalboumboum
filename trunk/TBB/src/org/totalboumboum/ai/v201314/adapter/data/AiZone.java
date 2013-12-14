package org.totalboumboum.ai.v201314.adapter.data;

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

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiFloor;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiSprite;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.computing.ApproximationTools;
import org.totalboumboum.tools.images.PredefinedColor;
import org.totalboumboum.tools.levels.DeltaTools;
import org.totalboumboum.tools.levels.DistancePixelTools;
import org.totalboumboum.tools.levels.DistanceTileTools;
import org.totalboumboum.tools.levels.PositionTools;

/**
 * Représente la zone de jeu et tous ces constituants : cases et sprites.
 * Il s'agit de la classe principale des percepts auxquels l'agent a accès.
 * <br/>
 * A chaque fois que l'agent est sollicité par le jeu pour connaître l'action
 * qu'il veut effectuer, cette représentation est mise à jour. L'agent ne reçoit
 * pas une nouvelle AiZone : l'AiZone existante est modifiée en fonction de l'évolution
 * du jeu. De la même façon, les cases ({@link AiTile}) restent les mêmes, ainsi que les sprites et
 * les autres objets. Si l'agent a besoin d'une trace des états précédents du jeu, son
 * concepteur doit se charger de l'implémenter lui-même.
 * 
 * @author Vincent Labatut
 */
public abstract class AiZone implements Serializable
{	/** Id de la classe */
	private static final long serialVersionUID = 1L;
	
	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Temps écoulé depuis le début du jeu */
	protected long totalTime = 0;
	/** Temps écoulé depuis la mise à jour précédente de l'IA considérée */
	protected long elapsedTime = 0;
	/** Durée maximale de la partie */
	protected long limitTime = 0;
	
	/**
	 * Renvoie le temps total écoulé depuis le début du jeu.
	 * 
	 * @return	
	 * 		Le temps total écoulé exprimé en millisecondes.
	 */
	public long getTotalTime()
	{	return totalTime;		
	}
	
	/**
	 * Renvoie le temps écoulé depuis la mise à jour précédente
	 * de l'IA considérée.
	 * 
	 * @return	
	 * 		Le temps écoulé exprimé en millisecondes.
	 */
	public long getElapsedTime()
	{	return elapsedTime;		
	}
	
	/**
	 * Renvoie la durée maximale de la partie
	 * (elle peut éventuellement durer moins longtemps).
	 * 
	 * @return	
	 * 		La durée maximale de la partie.
	 */
	public long getLimitTime()
	{	return limitTime;		
	}
	
	/////////////////////////////////////////////////////////////////
	// META DATA		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Rangs des joueurs pour la manche en cours (ces rangs peuvent évoluer) */
	protected final Map<AiHero,Integer> roundRanks = new HashMap<AiHero, Integer>();
	/** Rangs des joueurs pour la rencontre en cours (ces rangs n'évoluent pas pendant la manche) */
	protected final Map<AiHero,Integer> matchRanks = new HashMap<AiHero, Integer>();
	/** Rangs des joueurs au classement global du jeu (ces rangs n'évoluent pas pendant la manche) */
	protected final Map<AiHero,Integer> statsRanks = new HashMap<AiHero, Integer>();

	/**
	 * Renvoie le classement du personnage passé en paramètre, pour la manche en cours.
	 * Ce classement est susceptible d'évoluer d'ici la fin de la manche actuellement jouée, 
	 * par exemple si ce joueur est éliminé.
	 * 
	 * @param hero
	 * 		Le personnage considéré.
	 * @return	
	 * 		Son classement dans la manche en cours.
	 */
	public int getRoundRank(AiHero hero)
	{	return roundRanks.get(hero);
	}
	
	/**
	 * Renvoie le classement du personnage passé en paramètre, pour la rencontre en cours.
	 * Ce classement n'évolue pas pendant la manche actuellement jouée.
	 * 
	 * @param hero
	 * 		Le personnage considéré.
	 * @return	
	 * 		Son classement dans la rencontre en cours.
	 */
	public int getMatchRank(AiHero hero)
	{	return matchRanks.get(hero);
	}
	
	/**
	 * Renvoie le classement du personnage passé en paramètre, dans le classement général du jeu (Glicko-2)
	 * Ce classement n'évolue pas pendant la manche actuellement jouée.
	 * 
	 * @param hero
	 * 		Le personnage considéré.
	 * @return	
	 * 		Son classement général (Glicko-2).
	 */
	public int getStatsRank(AiHero hero)
	{	return statsRanks.get(hero);
	}
	
	/////////////////////////////////////////////////////////////////
	// MATRIX			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Hauteur totale de la zone de jeu exprimée en cases (ie: nombre de lignes) */
	protected int height;
	/** Largeur totale de la zone de jeu exprimée en cases (ie: nombre de colonnes) */
	protected int width;

	/**
	 * Renvoie la matrice de cases représentant la zone de jeu.
	 * <br/>
	 * <b>Attention :</b> cette matrice ne doit surtout pas être modifiée, sinon
	 * les percepts reçus par l'agent ultérieurement seront
	 * probablement faux. De plus, cela peut provoquer certaines
	 * exceptions.
	 * 
	 * @return	
	 * 		La matrice correspondant à la zone de jeu.
	 */
	public abstract AiTile[][] getMatrix();

	/** 
	 * Renvoie la hauteur totale (y compris les éventuelles cases situées hors de l'écran)
	 * de la zone de jeu exprimée en cases (i.e. : nombre de lignes).
	 *  
	 *  @return	
	 *  	Hauteur de la zone.
	 */
	public int getHeight()
	{	return height;	
	}
	
	/** 
	 * Renvoie la largeur totale (y compris les éventuelles cases situées hors de l'écran)
	 * de la zone de jeu exprimée en cases (i.e. : nombre de colonnes).
	 *  
	 *  @return	
	 *  	Largeur de la zone.
	 */
	public int getWidth()
	{	return width;	
	}
	
	/////////////////////////////////////////////////////////////////
	// TILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie toutes les cases de cette zone
	 * sous forme d'une liste. Les cases y sont
	 * placées ligne à ligne.
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return
	 * 		La liste de toutes les cases de cette zone.
	 */
	public abstract List<AiTile> getTiles();
	
	/**
	 * Renvoie la case de mêmes coordonnées que celle
	 * passée en paramètre. Pratique pour obtenir la
	 * case équivalant à une autre case, mais dans une
	 * autre zone (par exemple deux zones représentant
	 * le même environnement à deux instants différents).
	 * 
	 * @param tile
	 * 		Case de référence contenue dans une autre zone.
	 * @return
	 * 		Case située au même endroit dans cette zone.
	 */
	public AiTile getTile(AiTile tile)
	{	int col = tile.getCol();
		int row = tile.getRow();
		AiTile result = getTile(row,col);
		return result;
	}

	/**
	 * Renvoie la case située dans la zone à la position passée en paramètre.
	 *   
	 *  @param row
	 *  	Numéro de la ligne contenant la case à renvoyer.
	 *  @param col
	 *  	Numéro de la colonne contenant la case à renvoyer.
	 *  @return	
	 *  	Case située aux coordonnées spécifiées en paramètres.
	 */
	public abstract AiTile getTile(int row, int col);
	
	/**
	 * Renvoie la case qui contient le pixel passé en paramètre.
	 *   
	 *  @param x
	 *  	Abscisse du pixel concerné.
	 *  @param y
	 *  	Ordonnée du pixel concerné.
	 *  @return	
	 *  	Case contenant le pixel situé aux coordonnées spécifiées en paramètres.
	 */
	public abstract AiTile getTile(double x, double y);
		
	/**
	 * Renvoie la case qui contient l'emplacement passé en paramètre.
	 *   
	 *  @param location
	 *  	L'emplacement concerné.
	 *  @return	
	 *  	Case contenant l'emplacement spécifié en paramètres.
	 */
	public AiTile getTile(AiLocation location)
	{	double x = location.getPosX();
		double y = location.getPosY();
		AiTile result = getTile(x,y);
		return result;
	}
		
	/////////////////////////////////////////////////////////////////
	// BLOCKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Renvoie la liste des blocks contenus dans cette zone
	 * (la liste peut être vide). 
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return	
	 * 		Liste de tous les blocs contenus dans cette zone.
	 */
	public abstract List<AiBlock> getBlocks();
	
	/** 
	 * Renvoie la liste des blocks destructibles contenus dans cette zone
	 * (la liste peut être vide). 
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return	
	 * 		Liste de tous les blocs destructibles contenus dans cette zone.
	 */
	public abstract List<AiBlock> getDestructibleBlocks();
	
	/////////////////////////////////////////////////////////////////
	// BOMBS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indique si la mise à jour des maps de bombes a été faite à cette itéeation */
	protected boolean bombmapsUpdated = false;
	/** Map associant à chaque bombe la liste des bombes qu'elle menace */
	protected final Map<AiBomb,List<AiBomb>> threatenedBombs = new HashMap<AiBomb, List<AiBomb>>();
	/** Version immuable de la map associant à chaque bombe la liste des bombes qu'elle menace */
	protected final Map<AiBomb,List<AiBomb>> externalThreatenedBombs = Collections.unmodifiableMap(threatenedBombs);
	/** Map associant à chaque bombe le temps avant son explosion */
	protected final Map<AiBomb,Long> delaysByBombs = new HashMap<AiBomb, Long>();
	/** Version immuable de la map associant à chaque bombe le temps avant son explosion */
	protected final Map<AiBomb,Long> externalDelaysByBombs = Collections.unmodifiableMap(delaysByBombs);
	/** Map associant à chaque temps avant explosion la liste des bombes concernées */
	protected final Map<Long,List<AiBomb>> bombsByDelays = new HashMap<Long, List<AiBomb>>();
	/** Version immuable de la map associant à chaque temps avant explosion la liste des bombes concernées */
	protected final Map<Long,List<AiBomb>> externalBombsByDelays = Collections.unmodifiableMap(bombsByDelays);
	
	/** 
	 * Renvoie la liste des bombes contenues dans cette zone 
	 * (la liste peut être vide).
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return	
	 * 		Liste de toutes les bombes contenues dans cette zone.
	 */
	public abstract List<AiBomb> getBombs();
	
	/** 
	 * Renvoie la liste de bombes de la couleur passée en paramètre.
	 * la liste est vide si aucune bombe de cette couleur n'existe ou si 
	 * cette couleur est {@code null}.
	 * <br/>
	 * <b>Note :</b> la liste renvoyée est générée à la demande.
	 * Elle peut être modifiée par l'agent sans problème.
	 * 
	 * @param color 
	 * 		La couleur recherchée.
	 * @return	
	 * 		Une liste de bombe de la couleur passée en paramètre.
	 */
	public List<AiBomb> getBombsByColor(PredefinedColor color)
	{	List<AiBomb> result = new LinkedList<AiBomb>();
		
		for(AiBomb bomb: getBombs())
		{	if(bomb.getColor()==color)
				result.add(bomb);
		}
		
		return result;
	}
	
	/**
	 * Renvoie les temps d'explosion de chaque bombe
	 * présente dans la zone, en tenant compte des
	 * réactions en chaîne. Le résultat prend la forme
	 * d'une map dont la clé est la bombe et la valeur
	 * le temps restant avant son explosion.
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return
	 * 		Une map décrivant les temps d'explosion des bombes.
	 */
	public Map<AiBomb,Long> getDelaysByBombs()
	{	if(!bombmapsUpdated)
			initBombData();
		return externalDelaysByBombs;
	}
	
	/**
	 * Renvoie les temps d'explosion en ms de chaque bombe
	 * présente dans la zone, en tenant compte des
	 * réactions en chaîne. Le résultat prend la forme
	 * d'une map dont la clé est le temps restant avant l'explosion
	 * et la valeur une liste de bombes associées à ce temps.
	 * 
	 * @return
	 * 		Une map décrivant les temps d'explosion des bombes.
	 */
	public Map<Long,List<AiBomb>> getBombsByDelays()
	{	if(!bombmapsUpdated)
			initBombData();
		return externalBombsByDelays;
	}
	
	/**
	 * Renvoie une map décrivant les bombes menacées
	 * par d'autres bombe. La clé de cette map est une
	 * bombe menaçante et la valeur une liste de bombes
	 * menacées par cette bombe.
	 * 
	 * @return
	 * 		Une map décrivant les bombes menacées.
	 */
	public Map<AiBomb,List<AiBomb>> getThreatenedBombs()
	{	if(!bombmapsUpdated)
			initBombData();
		return externalThreatenedBombs;
	}
	
	/**
	 * Initialise différentes structures 
	 * contenant des données sur les bombes
	 * et explosions de cette zone.
	 */
	private void initBombData()
	{	delaysByBombs.clear();
		bombsByDelays.clear();
		threatenedBombs.clear();
		
		// retrieve necessary info
		for(AiBomb bomb: getBombs())
		{	List<AiFire> fires = bomb.getTile().getFires();
			// delay map & bomb map
			long delay = Long.MAX_VALUE; //TODO non-time bombs are considered to have an infinite delay, which should be corrected
			// fire-sensitive bomb currently caught in an explosion
			if(bomb.hasExplosionTrigger() && !fires.isEmpty())
			{	long fireDuration = 0;
				for(AiFire fire: fires)
				{	long time = fire.getElapsedTime();
					if(time>fireDuration)
						fireDuration = time;
				}
				delay = Math.max(bomb.getLatencyDuration()-fireDuration,0);
			}
			// time bomb
			if(bomb.hasCountdownTrigger())
			{	long temp = Math.max(bomb.getNormalDuration()-bomb.getElapsedTime(),0);
				delay = Math.min(delay,temp);
			}
			delaysByBombs.put(bomb,delay);
			List<AiBomb> tempList = bombsByDelays.get(delay);
			if(tempList==null)
			{	tempList = new ArrayList<AiBomb>();
				bombsByDelays.put(delay,tempList);
			}
			tempList.add(bomb);
			
			// threatened bombs list
			List<AiBomb> tempTarget = new ArrayList<AiBomb>();
			List<AiTile> blast = bomb.getBlast();
			for(AiTile tile: blast)
			{	List<AiBomb> tileBombs = tile.getBombs();
				// we only consider the bombs sensitive to explosions
				for(AiBomb b: tileBombs)
				{	if(b.hasExplosionTrigger())
						tempTarget.addAll(tileBombs);
				}
			}
			if(!tempTarget.isEmpty())
				threatenedBombs.put(bomb,tempTarget);
		}
		
		// get temporal explosion order
		TreeSet<Long> orderedDelays = new TreeSet<Long>(bombsByDelays.keySet());
		while(!orderedDelays.isEmpty())
		{	// get the delay
			long delay = orderedDelays.first();
			orderedDelays.remove(delay);
			if(delay<Integer.MAX_VALUE)	// we ignore non-time bombs
			{	// get the bombs associated to this delay
				List<AiBomb> bombList = bombsByDelays.get(delay);
	
				// update threatened bomb delays while considering a bomb can detonate 
				// another one before the regular time 
				for(AiBomb bomb1: bombList)
				{	// get the threatened bombs
					List<AiBomb> bList = threatenedBombs.get(bomb1);
					// update their delays
					for(AiBomb bomb2: bList)
					{	// get the delay 
						long delay2 = delaysByBombs.get(bomb2);
						// add latency time
						long newDelay = delay + bomb2.getLatencyDuration();
						
						// if this makes the delay shorter, we update eveywhere needed
						if(bomb2.hasExplosionTrigger() && newDelay<delay2)
						{	// in the result map
							delaysByBombs.put(bomb2,newDelay);
							
							// we remove the bomb from the other (inverse) map
							{	List<AiBomb> tempList = bombsByDelays.get(delay2);
								tempList.remove(bomb2);
								// and possibly the delay itself, if no other bomb uses it anymore
								if(tempList.isEmpty())
								{	bombsByDelays.remove(delay2);
									orderedDelays.remove(delay2);
								}
							}
							
							// and put it again, but at the appropriate place this time
							{	List<AiBomb> tempList = bombsByDelays.get(newDelay);
								// on crée éventuellement la liste nécessaire
								if(tempList==null)
								{	tempList = new ArrayList<AiBomb>();
									bombsByDelays.put(newDelay,tempList);
									orderedDelays.add(newDelay);
								}
								tempList.add(bomb2);
							}
						}
					}
				}
			}
		}
		
		bombmapsUpdated = true;
	}
	
	/////////////////////////////////////////////////////////////////
	// FIRES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Renvoie la liste des feux contenus dans cette zone 
	 * (la liste peut être vide).
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return	
	 * 		Liste de tous les feux contenus dans cette zone.
	 */
	public abstract List<AiFire> getFires();

	/////////////////////////////////////////////////////////////////
	// FLOORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Renvoie la liste des sols contenus dans cette zone. 
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return	
	 * 		Liste de tous les sols contenus dans cette zone.
	 */
	public abstract List<AiFloor> getFloors();
	
	/////////////////////////////////////////////////////////////////
	// HEROES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Renvoie la liste des personnages contenus dans cette zone,
	 * y compris ceux qui ont été éliminés. 
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return	
	 * 		Liste de tous les joueurs contenus dans cette zone.
	 */
	public abstract List<AiHero> getHeroes();
	
	/** 
	 * Renvoie la liste des personnages contenus dans cette zone, 
	 * sauf ceux qui ont été éliminés ou qui ne sont pas actuellement
	 * en jeu.
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return	
	 * 		Liste de tous les joueurs encore contenus dans cette zone.
	 */
	public abstract List<AiHero> getRemainingHeroes();
	
	/** 
	 * Renvoie la liste des personnages contenus dans cette zone, 
	 * sauf ceux qui ont été éliminés ou qui ne sont pas actuellement
	 * en jeu, et sauf le personnage contrôlé par l'agent.
	 * <br/>
	 * <b>Note :</b> cette liste est générée à la demande, elle
	 * peut être modifiée par l'agent sans problème.
	 * 
	 * @return	
	 * 		Liste de tous les joueurs encore contenus dans cette zone, sauf celui de l'agent.
	 */
	public abstract List<AiHero> getRemainingOpponents();

	/** 
	 * Renvoie le personnage de la couleur passée en paramètre,
	 * ou null si aucun personnage de cette couleur existe ou si 
	 * cette couleur est {@code null}.
	 * <br/>
	 * <b>Attention :</b> Les personnages déjà éliminés sont aussi considérés.
	 *  
	 * @param color 
	 * 		La couleur recherchée.
	 * @return	
	 * 		Le personnage dont la couleur est celle passée en paramètre
	 */
	public abstract AiHero getHeroByColor(PredefinedColor color);

	/////////////////////////////////////////////////////////////////
	// ITEMS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Nombre d'items cachés, i.e. pas encore ramassés */
	protected int hiddenItemsCount;
	/** Nombre d'items cachés, par type*/
	protected final Map<AiItemType,Integer> hiddenItemsCounts = new HashMap<AiItemType, Integer>();
	
	/** 
	 * Renvoie la liste des items apparents contenus dans cette zone.
	 * La liste peut être vide.
	 * <br/>
	 * Les items apparaissant lors de la mort subite ne sont pas comptés.
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return	
	 * 		Liste de tous les items contenus dans cette zone.
	 */
	public abstract List<AiItem> getItems();
	
	/**
	 * Renvoie le nombre d'items cachés restant dans le niveau.
	 * Il s'agit des items qui sont encore cachés dans des blocs, 
	 * et qui n'ont pas été ramassés. Cette information permet de
	 * savoir s'il est encore nécessaire de faire exploser des blocs 
	 * pour trouver des items, ou pas.
	 * <br/>
	 * Les items apparaissant lors de la mort subite ne sont pas comptés.
	 * 
	 * @return	
	 * 		Le nombre d'items restant à découvrir.
	 */
	public int getHiddenItemsCount()
	{	return hiddenItemsCount;		
	}
	
	/**
	 * Renvoie le nombre d'items cachés restant dans le niveau, pour un type donné.
	 * Il s'agit des items qui sont encore cachés dans des blocs, 
	 * et qui n'ont pas été ramassés. Cette information permet de
	 * savoir s'il est encore nécessaire de faire exploser des blocs 
	 * pour trouver des items, ou pas.
	 * <br/>
	 * Les items apparaissant lors de la mort subite ne sont pas comptés.
	 * 
	 * @param type
	 * 		Le type d'items à considérer.
	 * @return	
	 * 		Le nombre d'items de ce type restant à découvrir.
	 */
	public int getHiddenItemsCount(AiItemType type)
	{	Integer result = hiddenItemsCounts.get(type);
		if(result==null)
			result = 0;
		return result;
	}
	
	/**
	 * Renvoie une map contenant la proportion d'items restant
	 * cachés dans le niveau, par type d'item. Cette proportion peut
	 * être assimilée à une probabilité : celle de découvrir un item
	 * de ce type quand le mur qu'on fait exploser contient un item.
	 * <br/>
	 * Les items apparaissant lors de la mort subite ne sont pas comptés.
	 * <br/>
	 * <b>Note :</b> cette map est générée à la demande, elle peut
	 * être modifiée sans problème par l'agent.
	 * 
	 * @return
	 * 		Une map contenant les probabilités associées à chaque type d'item.
	 */
	public Map<AiItemType,Double> getHiddenItemsProbas()
	{	HashMap<AiItemType,Double> result = new HashMap<AiItemType, Double>();
		double total = hiddenItemsCount;
		if(total==0)
			total = 1; // avoid division by zero
		for(AiItemType key: AiItemType.values())
		{	Double value = hiddenItemsCounts.get(key)/total;
			result.put(key,value);
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OWN HERO			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Renvoie le personnage qui est contrôlé par l'agent.
	 * 
	 * @return
	 * 		Le personnage contrôlé par l'agent.
	 */
	public abstract AiHero getOwnHero();
	
	/////////////////////////////////////////////////////////////////
	// DIRECTIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie la direction de la case target relativement à la case source.
	 * Par exemple, la case target de coordonnées (5,5) est à droite de
	 * la case source de coordonnées (5,6).
	 * <br/>
	 * Cette fonction peut être utile quand on veut savoir dans quelle direction
	 * il faut se déplacer pour aller de la case source à la case target.
	 * <br/>
	 * <b>ATTENTION 1 :</b> si les deux cases ne sont pas des voisines directes (ie. ayant un coté commun),
	 * il est possible que cette méthode renvoie une direction composite,
	 * c'est à dire : DOWNLEFT, DOWNRIGHT, UPLEFT ou UPRIGHT. Référez-vous à 
	 * la classe {@link Direction} pour plus d'informations sur ces valeurs.
	 * <br/>
	 * <b>ATTENTION 2 :</b> comme les niveaux sont circulaires, il y a toujours deux directions possibles.
	 * Cette méthode renvoie la direction du plus court chemin (sans considérer les éventuels obstacles).
	 * Par exemple, pour les cases (2,0) et (2,11) d'un niveau de 12 cases de largeur, le résultat sera
	 * {@link Direction#RIGHT RIGHT}, car {@link Direction#LEFT LEFT} permet également d'atteindre la case, 
	 * mais en parcourant un chemin plus long.
	 * <pre>
	 * <t> S>>>>>>>>>>T  distance=11
	 * <t>>S..........T> distance=1
	 * </pre>
	 * 
	 * @param source
	 * 		Case de référence.
	 * @param target
	 * 		Case dont on veut connaitre la direction.
	 * @return	
	 * 		La direction de {@code target} par rapport à {@code source}.
	 */
	public Direction getDirection(AiTile source, AiTile target)
	{	// differences
		int dx = target.getCol()-source.getCol();
		int dy = target.getRow()-source.getRow();
		
		// direction
		Direction temp = Direction.getCompositeFromDouble(dx,dy);
		Direction tempX = temp.getHorizontalPrimary();
		Direction tempY = temp.getVerticalPrimary();
		
		// distances
		int distDirX = Math.abs(dx);
		int distIndirX = getWidth()-distDirX;
		if(distDirX>distIndirX)
			tempX = tempX.getOpposite();
		int distDirY = Math.abs(dy);
		int distIndirY = getHeight()-distDirY;
		if(distDirY>distIndirY)
			tempY = tempY.getOpposite();
		
		// result
		Direction result = Direction.getComposite(tempX,tempY);
		return result;
	}
	
	/**
	 * Pareil que {@link #getDirection(double, double, double, double)}, mais 
	 * la méthode prend des emplacements en paramètres
	 * plutôt que des coordonnées.
	 * 
	 * @param source
	 * 		Emplacement de référence
	 * @param  target
	 * 		Emplacement dont on veut connaitre la direction
	 * @return	
	 * 		La direction de {@code target} par rapport à {@code source}.
	 */
	public Direction getDirection(AiLocation source, AiLocation target)
	{	double x1 = source.getPosX();
		double y1 = source.getPosY();
		double x2 = target.getPosX();
		double y2 = target.getPosY();
		Direction result = getDirection(x1,y1,x2,y2);
		return result;
	}
	
	/**
	 * Calcule la direction pour aller du sprite source au sprite target.
	 * Le niveau est considéré comme cyclique, i.e. le bord de droite est 
	 * relié au bord de gauche, et le bord du haut est relié au bord du bas.
	 * Cette méthode considère la direction correspondant à la distance la plus
	 * courte (qui peut correspondre à un chemin passant par les bords du niveau)
	 * La direction peut être {@code NONE} si jamais les deux sprites sont au même endroit.
	 * 
	 * @param source
	 * 		Sprite de départ.
	 * @param target
	 * 		Sprite de destination.
	 * @return	
	 * 		La direction pour aller de source vers target.
	 */
	public Direction getDirection(AiSprite source, AiSprite target)
	{	double x1 = source.getPosX();
		double y1 = source.getPosY();
		double x2 = target.getPosX();
		double y2 = target.getPosY();
		Direction result = getDirection(x1,y1,x2,y2);
		return result;		
	}
	
	/**
	 * Calcule la direction pour aller du sprite à la case passés en paramètres.
	 * Le niveau est considéré comme cyclique, i.e. le bord de droite est 
	 * relié au bord de gauche, et le bord du haut est relié au bord du bas.
	 * Cette méthode considère la direction correspondant à la distance la plus
	 * courte (qui peut correspondre à un chemin passant par les bords du niveau)
	 * La direction peut être {@code NONE} si jamais les deux sprites sont au même endroit
	 * 
	 * @param sprite
	 * 		Sprite en déplacement.
	 * @param tile
	 * 		Case de destination.
	 * @return	
	 * 		La direction pour aller du sprite vers la case.
	 */
	public Direction getDirection(AiSprite sprite, AiTile tile)
	{	double x1 = sprite.getPosX();
		double y1 = sprite.getPosY();
		double x2 = tile.getPosX();
		double y2 = tile.getPosY();
		Direction result = getDirection(x1,y1,x2,y2);
		return result;
	}
	
	/**
	 * Calcule la direction pour aller de la position (x1,y1) à la position (x2,y2)
	 * Le niveau est considéré comme cyclique, i.e. le bord de droite est 
	 * relié au bord de gauche, et le bord du haut est relié au bord du bas.
	 * Cette méthode considère la direction correspondant à la distance la plus
	 * courte (qui peut correspondre à un chemin passant par les bords du niveau).
	 * La direction peut être {@code NONE} si jamais les deux positions 
	 * sont équivalentes.
	 * 
	 * @param x1
	 * 		Première position horizontale en pixels.
	 * @param y1
	 * 		Première position verticale en pixels.
	 * @param x2
	 * 		Seconde position horizontale en pixels.
	 * @param y2
	 * 		Seconde position verticale en pixels.
	 * @return	
	 * 		La direction correspondant au chemin le plus court.
	 */
	public Direction getDirection(double x1, double y1, double x2, double y2)
	{	double dx = DeltaTools.getDeltaX(x1,x2,pixelLeftX,pixelWidth);
		if(ApproximationTools.isRelativelyEqualTo(dx,0))
			dx = 0;
		double dy = DeltaTools.getDeltaY(y1,y2,pixelTopY,pixelHeight);
		if(ApproximationTools.isRelativelyEqualTo(dy,0))
			dy = 0;
		Direction result = Direction.getCompositeFromRelativeDouble(dx,dy);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TILE DISTANCES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie la distance de Manhattan entre les cases de coordonnées
	 * (row1,col1) et (row2,col2), exprimée en cases.
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param  row1
	 * 		Ligne de la première case.
	 * @param col1
	 * 		Colonne de la première case.
	 * @param  row2
	 * 		Ligne de la seconde case.
	 * @param  col2
	 * 		Colonne de la seconde case.
	 * @param  direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public int getTileDistance(int row1, int col1, int row2, int col2, Direction direction)
	{	int result = DistanceTileTools.getTileDistance(row1,col1,row2,col2,direction,height,width);
		return result;
	}

	/**
	 * Renvoie la distance de Manhattan entre les cases de coordonnées
	 * (row1,col1) et (row2,col2), exprimée en cases.
	 * <br/> 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau)
	 * 
	 * @param row1
	 * 		Ligne de la première case.
	 * @param  col1
	 * 		Colonne de la première case.
	 * @param  row2
	 * 		Ligne de la seconde case.
	 * @param col2
	 * 		Colonne de la seconde case.
	 * @return
	 * 		La distance calculée. 
	 */
	public int getTileDistance(int row1, int col1, int row2, int col2)
	{	int result = DistanceTileTools.getTileDistance(row1,col1,row2,col2,Direction.NONE,height,width);
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan entre les deux cases passées en paramètres,
	 * exprimée en cases.
	 * <br/> 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, i.e. le bord de droite 
	 * est relié au bord de gauche, et le bord du haut est relié au bord du bas. 
	 * Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau)
	 * 
	 * @param tile1
	 * 		Première case.
	 * @param tile2
	 * 		Seconde case.
	 * @return
	 * 		La distance calculée. 
	 */
	public int getTileDistance(AiTile tile1, AiTile tile2)
	{	int result = getTileDistance(tile1,tile2,Direction.NONE);
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan entre les deux cases passées en paramètres,
	 * exprimée en cases.
	 * <br/> 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, i.e. le bord de droite 
	 * est relié au bord de gauche, et le bord du haut est relié au bord du bas. 
	 * Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param tile1
	 * 		Première case.
	 * @param tile2
	 * 		Seconde case.
	 * @param direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public int getTileDistance(AiTile tile1, AiTile tile2, Direction direction)
	{	int row1 = tile1.getRow();
		int col1 = tile1.getCol();
		int row2 = tile2.getRow();
		int col2 = tile2.getCol();
		int result = DistanceTileTools.getTileDistance(row1,col1,row2,col2,height,width);
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan entre les deux sprites passés en paramètres,
	 * exprimée en cases.
	 * <br/> 
   	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, i.e. le bord de droite 
	 * est relié au bord de gauche, et le bord du haut est relié au bord du bas. 
	 * Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau)
	 * 
	 * @param sprite1
	 * 		Premier sprite.
	 * @param sprite2
	 * 		Second sprite.
	 * @return
	 * 		La distance calculée. 
	 */
	public int getTileDistance(AiSprite sprite1, AiSprite sprite2)
	{	int result = getTileDistance(sprite1,sprite2,Direction.NONE);
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan entre les deux sprites passés en paramètres,
	 * exprimée en cases.
	 * <br/> 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, i.e. le bord de droite 
	 * est relié au bord de gauche, et le bord du haut est relié au bord du bas. 
	 * Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param sprite1
	 * 		Premier sprite.
	 * @param sprite2
	 * 		Second sprite.
	 * @param direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public int getTileDistance(AiSprite sprite1, AiSprite sprite2, Direction direction)
	{	AiTile tile1 = sprite1.getTile();
		AiTile tile2 = sprite2.getTile();
		int result = getTileDistance(tile1,tile2);
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan, exprimée en cases,
	 *  entre les cases contenant
	 * les emplacements passés en paramètres.
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param location1
	 * 		Emplacement de la première case.
	 * @param location2
	 * 		Emplacement de la seconde case.
	 * @param  direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public int getTileDistance(AiLocation location1, AiLocation location2, Direction direction)
	{	AiTile tile1 = location1.getTile();
		AiTile tile2 = location2.getTile();
		int result = getTileDistance(tile1.getRow(),tile1.getCol(),tile2.getRow(),tile2.getCol(),direction);
		return result;
	}

	/**
	 * Renvoie la distance de Manhattan, exprimées en cases,
	 * entre les cases contenant les emplacements passés en paramètres.
	 * <br/> 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau)
	 * 
	 * @param location1
	 * 		Emplacement de la première case.
	 * @param location2
	 * 		Emplacement de la seconde case.
	 * @return
	 * 		La distance calculée. 
	 */
	public int getTileDistance(AiLocation location1, AiLocation location2)
	{	AiTile tile1 = location1.getTile();
		AiTile tile2 = location2.getTile();
		int result = getTileDistance(tile1.getRow(),tile1.getCol(),tile2.getRow(),tile2.getCol());
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PIXEL DIMENSIONS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Coordonnée du pixel le plus a gauche */
	protected double pixelLeftX;
	/** Coordonnée du pixel le plus haut */
	protected double pixelTopY;
	/** Largeur du niveau en pixels */
	protected double pixelWidth;
	/** Hauteur du niveau en pixels */
	protected double pixelHeight;
	
	/**
	 * Renvoie la coordonnée du pixel le plus à gauche du niveau.
	 * <br/>
	 * <b>Note :</b> cette méthode n'est pas utile pour la programmation d'un agent.
	 * 
	 * @return	
	 * 		L'abscisse du pixel le plus à gauche du niveau.
	 */
	public double getPixelLeftX()
	{	return pixelLeftX;
	}

	/**
	 * Renvoie la coordonnée du pixel le plus haut du niveau.
	 * <br/>
	 * <b>Note :</b> cette méthode n'est pas utile pour la programmation d'un agent.
	 * 
	 * @return	
	 * 		L'ordonnée du pixel le plus haut du niveau.
	 */
	public double getPixelTopY()
	{	return pixelTopY;
	}

	/**
	 * Renvoie largeur du niveau en pixels.
	 * <br/>
	 * <b>Note :</b> cette méthode n'est pas utile pour la programmation d'un agent.
	 * 
	 * @return	
	 * 		La largeur du niveau en pixels.
	 */
	public double getPixelWidth()
	{	return pixelWidth;
	}

	/**
	 * Renvoie hauteur du niveau en pixels.
	 * <br/>
	 * <b>Note :</b> cette méthode n'est pas utile pour la programmation d'un agent.
	 * 
	 * @return	
	 * 		La hauteur du niveau en pixels.
	 */
	public double getPixelHeight()
	{	return pixelHeight;
	}
	
	/////////////////////////////////////////////////////////////////
	// PIXEL DISTANCES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie la distance de Manhattan entre les points de coordonnées
	 * (x1,y1) et (x2,y2), exprimée en pixels.
	 * <br/> 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau).
	 * 
	 * @param x1
	 * 		Abscisse du premier point.
	 * @param y1
	 * 		Ordonnée du premier point.
	 * @param x2
	 * 		Abscisse du second point.
	 * @param y2
	 * 		Ordonnée du second point.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getPixelDistance(double x1, double y1, double x2, double y2)
	{	double result = DistancePixelTools.getPixelDistance(x1,y1,x2,y2,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
		if(ApproximationTools.isRelativelyEqualTo(result,0))
			result = 0;
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan entre les points de coordonnées
	 * (x1,y1) et (x2,y2), exprimée en pixels.
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param x1
	 * 		Abscisse du premier point.
	 * @param y1
	 * 		Ordonnée du premier point.
	 * @param x2
	 * 		Abscisse du second point.
	 * @param y2
	 * 		Ordonnée du second point.
	 * @param direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getPixelDistance(double x1, double y1, double x2, double y2, Direction direction)
	{	double result = DistancePixelTools.getPixelDistance(x1,y1,x2,y2,direction,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
		if(ApproximationTools.isRelativelyEqualTo(result,0))
			result = 0;
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan entre les deux sprites passés en paramètres,
	 * exprimée en pixels.
	 * <br/> 
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, i.e. le bord de droite 
	 * est relié au bord de gauche, et le bord du haut est relié au bord du bas. 
	 * Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau)
	 * 
	 * @param sprite1
	 * 		Premier sprite.
	 * @param sprite2
	 * 		Second sprite.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getPixelDistance(AiSprite sprite1, AiSprite sprite2)
	{	double result = getPixelDistance(sprite1, sprite2,Direction.NONE);
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan entre les deux sprites passés en paramètres, exprimée en pixels.
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, i.e. le bord de droite 
	 * est relié au bord de gauche, et le bord du haut est relié au bord du bas. 
	 * Cette méthode considère la distance dans la direction indiquée par le 
	 * paramètre direction, qui peut correspondre à un chemin passant par 
	 * les bords du niveau.
	 * 
	 * @param sprite1
	 * 		Premier sprite.
	 * @param sprite2
	 * 		Second sprite.
	 * @param direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getPixelDistance(AiSprite sprite1, AiSprite sprite2, Direction direction)
	{	double x1 = sprite1.getPosX();
		double y1 = sprite1.getPosY();
		double x2 = sprite2.getPosX();
		double y2 = sprite2.getPosY();
		double result = DistancePixelTools.getPixelDistance(x1,y1,x2,y2,direction,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
		if(ApproximationTools.isRelativelyEqualTo(result,0))
			result = 0;
		return result;
	}

	/**
	 * Renvoie la distance de Manhattan entre les emplacements passés
	 * en paramètres, exprimée en pixels.
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param location1
	 * 		Emplacement du premier point.
	 * @param location2
	 * 		Emplacement du second point.
	 * @param direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getPixelDistance(AiLocation location1, AiLocation location2, Direction direction)
	{	double posX1 = location1.getPosX();
		double posY1 = location1.getPosY();
		double posX2 = location2.getPosX();
		double posY2 = location2.getPosY();
		double result = getPixelDistance(posX1,posY1,posX2,posY2,direction);
		return result;
	}

	/**
	 * Renvoie la distance de Manhattan entre les emplacements passés
	 * en paramètres, exprimée en pixels.
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau).
	 * 
	 * @param location1
	 * 		Emplacement du premier point.
	 * @param location2
	 * 		Emplacement du second point.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getPixelDistance(AiLocation location1, AiLocation location2)
	{	double posX1 = location1.getPosX();
		double posY1 = location1.getPosY();
		double posX2 = location2.getPosX();
		double posY2 = location2.getPosY();
		double result = getPixelDistance(posX1,posY1,posX2,posY2);
		return result;
	}

	/**
	 * Renvoie la distance de Manhattan, exprimée en pixels, 
	 * entre les coordonnées et la case passées en paramètre.
	 * On considère le point de la case le plus proche du
	 * point de départ (et non pas son centre !).
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param x1
	 * 		Abscisse du point de départ.
	 * @param y1
	 * 		Ordonnée du point de départ.
	 * @param tile
	 * 		Case d'arrivée.
	 * @param direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getPixelDistance(double x1, double y1, AiTile tile, Direction direction)
	{	double result = 0;
		AiTile tile0 = getTile(x1,y1);
		if(!tile0.equals(tile))
		{	result = result + getHorizontalPixelDistance(x1, tile, direction);
			result = result + getVerticalPixelDistance(y1, tile, direction);
		}
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan, exprimée en pixels, 
	 * et seulement pour l'axe horizontal, entre la coordonnée
	 * spécifiée et la case passées en paramètre.
	 * On considère le point de la case le plus proche du
	 * point de départ (et non pas son centre !).
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param x1
	 * 		Abscisse du point de départ.
	 * @param tile
	 * 		Case d'arrivée.
	 * @param direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getHorizontalPixelDistance(double x1, AiTile tile, Direction direction)
	{	double result = 0;
		double yCenter = tile.getPosY();
		AiTile t = getTile(x1, yCenter);
		if(!tile.equals(t))
		{	List<Direction> directions = new ArrayList<Direction>();
			if(direction==Direction.NONE)
			{	directions.add(Direction.LEFT);
				directions.add(Direction.RIGHT);
			}
			else
				directions.add(direction);
			
			double xCenter = tile.getPosX();
			double dim = tile.getSize();
			result = Double.MAX_VALUE;
			for(Direction dir: directions)
			{	double d = - dir.getIntFromDirection()[0];
				double x2 = xCenter + d*dim/2;
				if(d>0)	
					x2--;
				double temp = getPixelDistance(x1, yCenter, x2, yCenter, dir);
				if(temp<result)
					result = temp;
			}
			
//			double x2L = xCenter - dim/2 - 1;
//			double x2R = xCenter + dim/2;
//			double dxL = x2L - x1;
//			double dxR = x2R - x1;
//			double absDxL = Math.abs(dxL);
//			double absDxR = Math.abs(dxR);
//			double dx;
//			double direct;
//			if(absDxL<absDxR)
//			{	dx = dxL;
//				direct = absDxL;
//			}
//			else
//			{	dx = dxR;
//				direct = absDxR;
//			}
//			double indirect = pixelWidth - direct - dim;
//			Direction dir = direction.getHorizontalPrimary();
//			if(dir==Direction.NONE)
//				result = Math.min(direct,indirect);
//			else
//			{	Direction d = Direction.getHorizontalFromDouble(dx);
//				if(dir==d)
//					result = direct;
//				else
//					result = indirect;
//			}		
		}
		
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan, exprimée en pixels, 
	 * et seulement pour l'axe vertical, entre la coordonnée
	 * spécifiée et la case passées en paramètre.
	 * On considère le point de la case le plus proche du
	 * point de départ (et non pas son centre !).
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param y1
	 * 		Ordonnée du point de départ.
	 * @param tile
	 * 		Case d'arrivée.
	 * @param direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getVerticalPixelDistance(double y1, AiTile tile, Direction direction)
	{	double result = 0;
		double xCenter = tile.getPosX();
		AiTile t = getTile(xCenter, y1);
		if(!tile.equals(t))
		{	List<Direction> directions = new ArrayList<Direction>();
			if(direction==Direction.NONE)
			{	directions.add(Direction.DOWN);
				directions.add(Direction.UP);
			}
			else
				directions.add(direction);
			
			double yCenter = tile.getPosY();
			double dim = tile.getSize();
			result = Double.MAX_VALUE;
			for(Direction dir: directions)
			{	double d = - dir.getIntFromDirection()[1];
				double y2 = yCenter + d*dim/2;
				if(d>0)	
					y2--;
				double temp = getPixelDistance(xCenter, y1, xCenter, y2, dir);
				if(temp<result)
					result = temp;
			}
			
//			double y2U = yCenter - dim/2 - 1;
//			double y2D = yCenter + dim/2;
//			double dyU = y2U - y1;
//			double dyD = y2D - y1;
//			double absDyU = Math.abs(dyU);
//			double absDyD = Math.abs(dyD);
//			double dy;
//			double direct;
//			if(absDyU<absDyD)
//			{	dy = dyU;
//				direct = absDyU;
//			}
//			else
//			{	dy = dyD;
//				direct = absDyD;
//			}
//			double indirect = pixelWidth - direct - dim;
//			Direction dir = direction.getVerticalPrimary();
//			if(dir==Direction.NONE)
//				result = Math.min(direct,indirect);
//			else
//			{	Direction d = Direction.getVerticalFromDouble(dy);
//				if(dir==d)
//					result = direct;
//				else
//					result = indirect;
//			}
		}
		
		return result;
	}
	
	/**
	 * Renvoie la distance de Manhattan, exprimée en pixels, 
	 * entre les coordonnées et la case passées en paramètre.
	 * On considère le point de la case le plus proche du
	 * point de départ (et non pas son centre !).
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau).
	 * 
	 * @param x1
	 * 		Abscisse du point de départ.
	 * @param y1
	 * 		Ordonnée du point de départ.
	 * @param tile
	 * 		Case d'arrivée.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getPixelDistance(double x1, double y1, AiTile tile)
	{	double result = getPixelDistance(x1,y1,tile,Direction.NONE);
		return result;
	}

	/**
	 * Renvoie la distance de Manhattan, exprimée en pixels, 
	 * entre l'emplacement et la case passés en paramètre.
	 * On considère le point de la case le plus proche du
	 * point de départ (et non pas son centre !).
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance dans la direction
	 * indiquée par le paramètre direction, qui peut correspondre à un chemin 
	 * passant par les bords du niveau.
	 * 
	 * @param location
	 * 		Emplacement du point de départ.
	 * @param tile
	 * 		Case d'arrivée.
	 * @param direction
	 * 		Direction à considérer.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getPixelDistance(AiLocation location, AiTile tile, Direction direction)
	{	double x = location.getPosX();
		double y = location.getPosY();
		double result = getPixelDistance(x,y,tile,direction);
		return result;
	}

	/**
	 * Renvoie la distance de Manhattan, exprimée en pixels, 
	 * entre l'emplacement et la case passés en paramètre.
	 * On considère le point de la case le plus proche du
	 * point de départ (et non pas son centre !).
	 * <br/>
	 * <b>ATTENTION :</b> le niveau est considéré comme cyclique, 
	 * i.e. le bord de droite est relié au bord de gauche, et le bord du haut 
	 * est relié au bord du bas. Cette méthode considère la distance la plus courte
	 * (qui peut correspondre à un chemin passant par les bords du niveau).
	 * 
	 * @param location
	 * 		Emplacement du point de départ.
	 * @param tile
	 * 		Case d'arrivée.
	 * @return
	 * 		La distance calculée. 
	 */
	public double getPixelDistance(AiLocation location, AiTile tile)
	{	double x = location.getPosX();
		double y = location.getPosY();
		double result = getPixelDistance(x,y,tile);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TILE DELTAS				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////
	// PIXEL DELTAS				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Calcule la différence entre les coordonnées de deux points,
	 * exprimée en pixels. Le résultat est un tableau contenant
	 * deux valeurs : la première pour l'axe des abscisse et la seconde
	 * pour celui des ordonnées. Les valeurs correspondent à des
	 * distanes signées, le signe dépendant de la position relative
	 * des deux points. On a x1+deltax = x2 et y1+deltay = y2.
	 * 
	 * @param x1
	 * 		Abscisse du premier point.
	 * @param y1
	 * 		Ordonnée du premier point.
	 * @param x2
	 * 		Abscisse du second point.
	 * @param y2
	 * 		Ordonnée du second point.
	 * @param direction
	 * 		La direction dans laquelle calculer la difference.
	 * @return
	 * 		Un tableau de deux réels.
	 */
	public double[] getPixelDeltas(double x1, double y1, double x2, double y2, Direction direction)
	{	double dx = DeltaTools.getDeltaX(x1,x2,direction.getHorizontalPrimary(),pixelLeftX,pixelWidth);
		if(ApproximationTools.isRelativelyEqualTo(dx,0))
			dx = 0;
		double dy = DeltaTools.getDeltaY(y1,y2,direction.getVerticalPrimary(),pixelTopY,pixelHeight);
		if(ApproximationTools.isRelativelyEqualTo(dy,0))
			dy = 0;
		double result[] = {dx,dy};
		return result;
	}
	
	/**
	 * Comme {@link #getPixelDeltas(double, double, double, double, Direction)},
	 * mais on considère automatiquement la direction correspondant à la plus
	 * petite distance.
	 * 
	 * @param x1
	 * 		Abscisse du premier point.
	 * @param y1
	 * 		Ordonnée du premier point.
	 * @param x2
	 * 		Abscisse du second point.
	 * @param y2
	 * 		Ordonnée du second point.
	 * @return
	 * 		Un tableau de deux réels.
	 */
	public double[] getPixelDeltas(double x1, double y1, double x2, double y2)
	{	double result[] = getPixelDeltas(x1,y1,x2,y2,Direction.NONE);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CONTACT POINTS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Renvoie le point de contact entre
	 * les centres des deux cases passées en paramètres. 
	 * Les coordonnées du point de contact sont renvoyées 
	 * sous forme de tableau contenant deux doubles x et y.
	 * <br/>
	 * Attention, le point de contact est <i>à l'intérieur</i> 
	 * de la case de destination.
	 * <br/>
	 * <b>Attention :</b> Ces deux cases doivent être 
	 * des voisines directes.
	 * 
	 * @param tile1
	 * 		La première case.
	 * @param tile2
	 * 		La seconde case.
	 * @return
	 * 		La position du point de contact sous forme de couple (x,y)
	 * 
	 * @throws IllegalArgumentException
	 * 		Si les deux cases ne sont pas des voisines directes.
	 */
	public double[] getContactPoint(AiTile tile1, AiTile tile2)
	{	double x = 0;
		double y = 0;
		
		// on récupère les coordonnées des cases
		double tile1x = tile1.getPosX();
		double tile1y = tile1.getPosY();
		
		// direction entre les points
		Direction direction = getDirection(tile1,tile2);
		if(direction.isComposite())
		{	PredefinedColor color = getOwnHero().getColor();
			throw new IllegalArgumentException("The tiles must be direct neighbors ("+color+" player)");
		}
		int dir[] = direction.getIntFromDirection();
		
		// calcul des coordonnées
		if(direction.isHorizontal())
		{	x = tile1x + dir[0]*tile1.size/2;
			if(dir[0]<0)
				x--;
			y = tile1y;
		}
		else if(direction.isVertical())
		{	x = tile1x;
			y = tile1y + dir[1]*tile1.size/2;
			if(dir[1]<0)
				y--;
		}
		
		double result[] = normalizePosition(x,y);
		return result;
	}
	
	/**
	 * Renvoie le point de contact entre
	 * les deux points dont les coordonnées sont
	 * passées en paramètres. 
	 * Les coordonnées du point de contact sont renvoyées 
	 * sous forme de tableau contenant deux doubles x et y.
	 * <br/>
	 * Attention, le point de contact est <i>à l'intérieur</i> 
	 * de la case de destination.
	 * <br/>
	 * <b>Attention :</b> Ces deux points doivent appartenir 
	 * à des des cases voisines directes.
	 * 
	 * @param x1
	 * 		L'abscisse du premier point.
	 * @param y1
	 * 		L'ordonnée du premier point.
	 * @param x2
	 * 		L'abscisse du second point.
	 * @param y2
	 * 		L'ordonnée du second point.
	 * @return
	 * 		La position du point de contact sous forme de couple (x,y)
	 * 
	 * @throws IllegalArgumentException
	 * 		Si les deux points n'appartiennent pas à des cases voisines.
	 */
	public double[] getContactPoint(double x1, double y1, double x2, double y2)
	{	double x = 0;
		double y = 0;
	
		// on récupère les cases
		AiTile tile1 = getTile(x1,y1);
		double tile1x = tile1.getPosX();
		double tile1y = tile1.getPosY();
		AiTile tile2 = getTile(x2,y2);
		
		// direction entre les points
		Direction direction = getDirection(tile1,tile2);
		if(direction.isComposite())
		{	PredefinedColor color = getOwnHero().getColor();
			throw new IllegalArgumentException("Points must be in direct neighbor tiles ("+color+" player)");
		}
		int dir[] = direction.getIntFromDirection();
		
		// calcul des coordonnées
		if(direction.isHorizontal())
		{	x = tile1x + dir[0]*tile1.size/2;
			if(dir[0]<0)
				x--;
			y = (y1 + y2) / 2;
		}
		else if(direction.isVertical())
		{	x = (x1 + x2) / 2;
			y = tile1y + dir[1]*tile1.size/2;
			if(dir[1]<0)
				y--;
		}
		
		double result[] = normalizePosition(x,y);
		return result;
	}

	/**
	 * Renvoie le point de contact entre
	 * le point dont les coordonnées sont
	 * passées en paramètres et la case également
	 * passée en paramètre. 
	 * Les coordonnées du point de contact sont renvoyées 
	 * sous forme de tableau contenant deux doubles x et y.
	 * <br/>
	 * Attention, le point de contact est <i>à l'intérieur</i> 
	 * de la case de destination.
	 * <br/>
	 * <b>Attention :</b> Le point et la case passés
	 *  en paramètres doivent appartenir à des des 
	 *  cases voisines directes.
	 * 
	 * @param x1
	 * 		L'abscisse du point.
	 * @param y1
	 * 		L'ordonnée du point.
	 * @param tile
	 * 		La case.
	 * @param manhattan
	 * 		Indique si la trajectoire doit être directe ({@code false}) ou si seuls
	 * 		les déplacements horizontaux et verticaux sont tolérés ({@code true}).
	 * @return
	 * 		La position du point de contact sous forme de couple (x,y)
	 * 
	 * @throws IllegalArgumentException
	 * 		Si le point et la case ne sont pas voisins.
	 */
	public double[] getContactPoint(double x1, double y1, AiTile tile, boolean manhattan)
	{	double x = 0;
		double y = 0;
		
		// on récupère les cases
		AiTile tile1 = getTile(x1,y1);
		double tile1x = tile1.getPosX();
		double tile1y = tile1.getPosY();
		double tile2x = tile.getPosX();
		double tile2y = tile.getPosY();
		
		// direction entre les points
		Direction direction = getDirection(tile1,tile);
		if(direction.isComposite())
		{	PredefinedColor color = getOwnHero().getColor();
			throw new IllegalArgumentException("Points must be in direct neighbor tiles ("+color+" player)");
		}
		int dir[] = direction.getIntFromDirection();
		
		// calcul des coordonnées
		if(direction.isHorizontal())
		{	x = tile1x + dir[0]*tile1.size/2;
			if(dir[0]<0)
				x--;
			if(manhattan)
				y = tile2y;
			else
				y = (y1 + tile2y) / 2;
		}
		else if(direction.isVertical())
		{	if(manhattan)
				x = tile2x;
			else
				x = (x1 + tile2x) / 2;
			y = tile1y + dir[1]*tile1.size/2;
			if(dir[1]<0)
				y--;
		}
		
		double result[] = normalizePosition(x,y);
		return result;
	}

	/**
	 * Renvoie le point de contact entre
	 * l'emplacement et la case passés en paramètres. 
	 * Les coordonnées du point de contact sont renvoyées 
	 * sous forme de tableau contenant deux doubles x et y.
	 * <br/>
	 * Attention, le point de contact est <i>à l'intérieur</i> 
	 * de la case de destination.
	 * <br/>
	 * <b>Attention :</b> L'emplacement et la case passés
	 *  en paramètres doivent appartenir à des des 
	 *  cases voisines directes.
	 * 
	 * @param location
	 * 		L'emplacement.
	 * @param tile
	 * 		La case.
	 * @param manhattan
	 * 		Indique si la trajectoire doit être directe ({@code false}) ou si seuls
	 * 		les déplacements horizontaux et verticaux sont tolérés ({@code true}).
	 * @return
	 * 		La position du point de contact sous forme de couple (x,y)
	 * 
	 * @throws IllegalArgumentException
	 * 		Si l'emplacement et la case ne sont pas voisins.
	 */
	public double[] getContactPoint(AiLocation location, AiTile tile, boolean manhattan)
	{	double x = 0;
		double y = 0;
		
		// on récupère les cases
		double x1 = location.getPosX();
		double y1 = location.getPosY();
		AiTile tile1 = location.getTile();
		double tile1x = tile1.getPosX();
		double tile1y = tile1.getPosY();
		double tile2x = tile.getPosX();
		double tile2y = tile.getPosY();
		
		// direction entre les points
		Direction direction = getDirection(tile1,tile);
		if(direction.isComposite())
		{	PredefinedColor color = getOwnHero().getColor();
			throw new IllegalArgumentException("Both location and tile must be direct neighbors ("+color+" player)");
		}
		int dir[] = direction.getIntFromDirection();
		
		// calcul des coordonnées
		if(direction.isHorizontal())
		{	x = tile1x + dir[0]*tile1.size/2;
			if(dir[0]<0)
				x--;
			if(manhattan)
				y = tile2y;
			else
				y = (y1 + tile2y) / 2;
		}
		else if(direction.isVertical())
		{	if(manhattan)
				x = tile2x;
			else
				x = (x1 + tile2x) / 2;
			y = tile1y + dir[1]*tile1.size/2;
			if(dir[1]<0)
				y--;
		}
		
		double result[] = normalizePosition(x,y);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TRANSLATIONS				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Calcule le résultat de la translation de coordonnées
	 * (drow,dcol) appliquée à la case (row,col). Les coordonnées
	 * résultantes sont normalisées pour être dans la zone de jeu.
	 * <br/>
	 * Il est recommandé d'utiliser cette méthode pour effectuer
	 * des soustractions/additions sur les coordonnées des cases, 
	 * plutôt que directement utiliser les opérateurs + et -, afin
	 * d'éviter de sortir de la zone.
	 * 
	 * @param row
	 * 		Abscisse du point original.
	 * @param col
	 * 		Ordonnée du point original.
	 * @param drow
	 * 		Translation sur l'axe des abscisses.
	 * @param dcol
	 * 		Translation sur l'axe des ordonnées.
	 * @return
	 * 		Un tableau contenant les coordonnées du résultat de la translation.
	 */
	public int[] getTranslatedTile(int row, int col, int drow, int dcol)
	{	row = row + drow;
		col = col + dcol;
		int result[] = PositionTools.normalizePosition(row,col,height,width);
		return result;
	}

	/**
	 * Calcule le résultat de la translation de coordonnées
	 * (dx,dy) appliquée au point (x,y). Les coordonnées
	 * résultantes sont normalisées pour être dans la zone de jeu.
	 * <br/>
	 * Il est recommandé d'utiliser cette méthode pour effectuer
	 * des soustractions/additions sur les coordonnées, plutot
	 * que directement utiliser les opérateurs + et -, afin
	 * d'éviter de sortir de la zone.
	 * 
	 * @param x
	 * 		Abscisse du point original.
	 * @param y
	 * 		Ordonnée du point original.
	 * @param dx
	 * 		Translation sur l'axe des abscisses.
	 * @param dy
	 * 		Translation sur l'axe des ordonnées.
	 * @return
	 * 		Un tableau contenant les coordonnées du résultat de la translation.
	 */
	public double[] getTranslatedPoint(double x, double y, double dx, double dy)
	{	x = x + dx;
		y = y + dy;
		double result[] = PositionTools.normalizePosition(x,y,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COORDINATE NORMALIZING	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Prend n'importe quelles coordonnées exprimées en pixels et les normalise
	 * de manière à ce qu'elles appartiennent à la zone de jeu. Si les coordonnées
	 * désignent une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param x
	 * 		Abscisse.
	 * @param y
	 * 		Ordonnée.
	 * @return	
	 * 		Un tableau contenant les versions normalisées de x et y.
	 */
	public double[] normalizePosition(double x, double y)
	{	return PositionTools.normalizePosition(x,y,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
	}

	/**
	 * Prend n'importe quelle abscisse exprimée en pixels et la normalise
	 * de manière à ce qu'elle appartienne à la zone de jeu. Si la coordonnée
	 * désigne une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau (i.e. le côté gauche et le
	 * côté droit sont reliés) pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param x
	 * 		Abscisse.
	 * @return	
	 * 		La version normalisée de x.
	 */
	public double normalizePositionX(double x)
	{	return PositionTools.normalizePositionX(x,pixelLeftX,pixelWidth);
	}
	
	/**
	 * Prend n'importe quelle ordonnée exprimée en pixels et la normalise
	 * de manière à ce qu'elle appartienne à la zone de jeu. Si la coordonnée
	 * désigne une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau (i.e. le côté haut et le
	 * côté bas sont reliés) pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param y
	 * 		Ordonnée.
	 * @return	
	 * 		La version normalisée de y.
	 */
	public double normalizePositionY(double y)
	{	return PositionTools.normalizePositionY(y,pixelTopY,pixelHeight);
	}
	
	/**
	 * Prend n'importe quelles coordonnées exprimées en cases et les normalise
	 * de manière à ce qu'elles appartiennent à la zone de jeu. Si les coordonnées
	 * désignent une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param row
	 * 		Ligne de la case.
	 * @param col
	 * 		Colonne de la case.
	 * @return	
	 * 		Un tableau contenant les versions normalisées de row et col.
	 */
	public int[] normalizePosition(int row, int col)
	{	return PositionTools.normalizePosition(row,col,height,width);
	}

	/**
	 * Prend n'importe quelle abscisse exprimée en cases et la normalise
	 * de manière à ce qu'elle appartienne à la zone de jeu. Si la coordonnée
	 * désigne une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau (i.e. le côté gauche et le
	 * côté droit sont reliés) pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param col
	 * 		Colonne de la case.
	 * @return	
	 * 		La version normalisée de col.
	 */
	public int normalizePositionCol(int col)
	{	return PositionTools.normalizePositionCol(col,width);
	}

	/**
	 * Prend n'importe quelle ordonnée exprimée en cases et la normalise
	 * de manière à ce qu'elle appartienne à la zone de jeu. Si la coordonnée
	 * désigne une position située en dehors de la zone de jeu, cette méthode
	 * utilise la propriété cyclique du niveau (i.e. le côté haut et le
	 * côté bas sont reliés) pour déterminer une position
	 * équivalente située dans le niveau.
	 * 
	 * @param row
	 * 		Ligne de la case.
	 * @return	
	 * 		La version normalisée de row.
	 */
	public int normalizePositionRow(int row)
	{	return PositionTools.normalizePositionRow(row,height);
	}
	
	/////////////////////////////////////////////////////////////////
	// SAME PIXEL POSITION		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Teste si les deux sprites passés en paramètres occupent la
	 * même position au pixel près.
	 * 
	 * @param sprite1
	 * 		Le premier sprite.
	 * @param sprite2
	 * 		Le second sprite.
	 * @return	
	 * 		{@code true} ssi les deux sprites sont au même endroit.
	 */
	public boolean hasSamePixelPosition(AiSprite sprite1, AiSprite sprite2)
	{	boolean result;
		double x1 = sprite1.getPosX();
		double y1 = sprite1.getPosY();
		double x2 = sprite2.getPosX();
		double y2 = sprite2.getPosY();
		result = hasSamePixelPosition(x1,y1,x2,y2);
		return result;
	}
	
	/**
	 * Teste si le sprite passé en paramètre occupent le
	 * centre de la case passée en paramètre, au pixel près.
	 * 
	 * @param sprite
	 * 		Le sprite concerné.
	 * @param tile
	 * 		La case à traiter.
	 * @return	
	 * 		{@code true} ssi le sprite est au centre de la case.
	 */
	public boolean hasSamePixelPosition(AiSprite sprite, AiTile tile)
	{	boolean result;	
		double x1 = sprite.getPosX();
		double y1 = sprite.getPosY();
		double x2 = tile.getPosX();
		double y2 = tile.getPosY();
		result = hasSamePixelPosition(x1,y1,x2,y2);
		return result;
	}

	/**
	 * Teste si les deux points passés en paramètres occupent la
	 * même position au pixel près.
	 * 
	 * @param x1
	 * 		L'abscisse de la première position.
	 * @param y1
	 * 		L'ordonnée de la première position.
	 * @param x2
	 * 		L'abscisse de la seconde position.
	 * @param y2
	 * 		L'ordonnée de la seconde position.
	 * @return	
	 * 		{@code true} ssi les deux positions sont équivalentes au pixel près.
	 */
	public boolean hasSamePixelPosition(double x1, double y1, double x2, double y2)
	{	boolean result = true;	
		result = result && ApproximationTools.isRelativelyEqualTo(x1,x2);
		result = result && ApproximationTools.isRelativelyEqualTo(y1,y2);
		return result;
	}

	/**
	 * Teste si les deux emplacements passés en paramètres occupent la
	 * même position au pixel près.
	 * 
	 * @param location1
	 * 		Le premier emplacement.
	 * @param location2
	 * 		Le second emplacement.
	 * @return	
	 * 		Renvoie {@code true} ssi les deux emplacement sont équivalents au pixel près.
	 */
	public boolean hasSamePixelPosition(AiLocation location1, AiLocation location2)
	{	double x1 = location1.getPosX();
		double y1 = location1.getPosY();
		double x2 = location2.getPosX();
		double y2 = location2.getPosY();
		boolean result = hasSamePixelPosition(x1,y1,x2,y2);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// SUDDEN DEATH				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste associant à chaque temps une liste de sprites apparaissant lors de la mort subite */
	protected final List<AiSuddenDeathEvent> suddenDeathEvents = new ArrayList<AiSuddenDeathEvent>();
	/** Version immuable de la Liste des évènements de mort subite */
	protected final List<AiSuddenDeathEvent> externalSuddenDeathEvents = Collections.unmodifiableList(suddenDeathEvents);

	/**
	 * Renvoie le prochain des évènements constituant la mort subite,
	 * ou {null} si aucun évènement suivant n'est prévu.
	 * <br/>
	 * Les items apparaissant éventuellement dans l'évènement renvoyé ne sont
	 * pas comptés comme des items présents dans la zone de jeu. Ils le seront
	 * uniquement après leur apparition.
	 * 
	 * @return
	 * 		Un évènement de la mort subite, ou {@code null} s'il n'y en a pas/plus.
	 */
	public AiSuddenDeathEvent getNextSuddenDeathEvent()
	{	AiSuddenDeathEvent result = null;
		if(!suddenDeathEvents.isEmpty())
			result = suddenDeathEvents.get(0);
		return result;
	}
	
	/**
	 * Renvoie la liste de tous les évènements constituant la mort subite.
	 * Elle est vide si aucun évènement ne doit se produire (en particulier 
	 * si aucune mort subite n'est prévue).
	 * <br/>
	 * Les items apparaissant éventuellement dans la liste renvoyée ne sont
	 * pas comptés comme des items présents dans la zone de jeu. Ils le seront
	 * uniquement après leur apparition.
	 * <br/>
	 * <b>Attention :</b> la liste renvoyée par cette méthode 
	 * ne doit pas être modifiée par l'agent. Toute tentative
	 * de modification provoquera une {@link UnsupportedOperationException}.
	 * 
	 * @return
	 * 		Une liste d'évènements de mort subite.
	 */
	public List<AiSuddenDeathEvent> getAllSuddenDeathEvents()
	{
//System.out.println("--------------------------------------------");		
//for(AiSuddenDeathEvent e: externalSuddenDeathEvents)
//	System.out.println(e);		
		return externalSuddenDeathEvents;
	}
	
	/////////////////////////////////////////////////////////////////
	// MISC						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Cette fonction permet d'afficher la zone sous forme d'ASCII art,
	 * ce qui est beaucoup plus lisible que du texte classique.
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
	 * 2│█│ │█│ │█│ │█│	 █ 	mur indestructible
	 *  ├─┼─┼─┼─┼─┼─┼─┤	 ▒ 	mur destructible
	 * 3│█│░│☻│ │ │▒│█│	 □ 	item seul
	 *  ├─┼─┼─┼─┼─┼─┼─┤	 ● 	bombe seule
	 * 4│█│░│█│ │█│ │█│	 ☺ 	joueur seul
	 *  ├─┼─┼─┼─┼─┼─┼─┤	 ☻ 	joueur et bombe présents dans la même case
	 * 5│█│░│░│░│ │●│█│	 ░	feu (d'autres sprites peuvent être contenus dans la même case) 
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
	{	boolean displayBombs = false;	// TODO permet d'activer/désactiver l'affichage des temps des bombes
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(4);
		nf.setMaximumFractionDigits(0);
		nf.setGroupingUsed(false);
		StringBuffer result = new StringBuffer();
	
		// col numbers
		if(width>10)
		{	result.append("  ");
			for(int i=0;i<10;i++)
				result.append("  ");
			for(int i=10;i<width;i++)
			{	result.append(" ");
				result.append(i/10);
			}
			if(displayBombs)
			{	result.append("\t");
				for(int i=0;i<10;i++)
					result.append("      ");
				for(int i=10;i<width;i++)
				{	result.append("     ");
					result.append(i/10);
				}
			}
			result.append("\n");
		}
		result.append("  ");
		for(int i=0;i<width;i++)
		{	result.append(" ");
			result.append(i%10);
		}
		if(displayBombs)
		{	result.append("\t");
			for(int i=0;i<width;i++)
			{	result.append("     ");
				result.append(i%10);
			}
		}
		result.append("\n");
		
		// top row
		result.append("  ┌");
		for(int col=0;col<width-1;col++)
			result.append("─┬");
		result.append("─┐");
		if(displayBombs)
		{	result.append("\t┌");
			for(int col=0;col<width-1;col++)
				result.append("─────┬");
			result.append("─────┐");
		}
		result.append("\n");
		
		// content
		for(int row=0;row<height;row++)
		{	// row number
			if(row<10)
				result.append(" ");
			result.append(row);
			// actual content
			for(int col=0;col<width;col++)
			{	AiTile tile = getTile(row,col);
				List<AiBlock> blocks = tile.getBlocks();
				List<AiHero> heroes = tile.getHeroes();
				List<AiItem> items = tile.getItems();
				List<AiBomb> bombs = tile.getBombs();
				List<AiFire> fires = tile.getFires();
			
				result.append("│");
				if(blocks.size()>0)
				{	AiBlock block = blocks.get(0);
					if(block.isDestructible())
						result.append("▒");
					else
						result.append("█");
				}
				else if(heroes.size()>0)
				{	if(bombs.size()>0)
						result.append("☻");
					else
						result.append("☺");
				}
				else if(items.size()>0)
					result.append("□");
				else if(bombs.size()>0)
					result.append("●");
				else if(fires.size()>0)
					result.append("░");
				else
					result.append(" ");
			}
			result.append("│");
			if(displayBombs)
			{	result.append("\t");
				for(int col=0;col<width;col++)
				{	AiTile tile = getTile(row,col);
					List<AiBomb> bombs = tile.getBombs();
					
					result.append("│");
					if(bombs.size()>0)
					{	AiBomb bomb = bombs.get(0);
						long time = bomb.getNormalDuration() - bomb.getElapsedTime();
						if(time>=0)
							result.append(" ");
						result.append(nf.format(time));
					}
					else
						result.append("     ");
				}
				result.append("│");
			}
			result.append("\n");

			if(row<height-1)
			{	result.append("  ├");
				for(int col=0;col<width-1;col++)
					result.append("─┼");
				result.append("─┤");
				if(displayBombs)
				{	result.append("\t├");
					for(int col=0;col<width-1;col++)
						result.append("─────┼");
					result.append("─────┤");
				}
				result.append("\n");
			}
		}
		
		// bottom row
		result.append("  └");
		for(int col=0;col<width-1;col++)
			result.append("─┴");
		result.append("─┘");
		if(displayBombs)
		{	result.append("\t└");
			for(int col=0;col<width-1;col++)
				result.append("─────┴");
			result.append("─────┘");
		}
		result.append("\n");
		
		return result.toString();
	}
}
