package org.totalboumboum.ai.v201415.adapter.data;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201415.adapter.data.AiBlock;
import org.totalboumboum.ai.v201415.adapter.data.AiBomb;
import org.totalboumboum.ai.v201415.adapter.data.AiFire;
import org.totalboumboum.ai.v201415.adapter.data.AiFloor;
import org.totalboumboum.ai.v201415.adapter.data.AiHero;
import org.totalboumboum.ai.v201415.adapter.data.AiItem;
import org.totalboumboum.ai.v201415.adapter.data.AiItemType;
import org.totalboumboum.ai.v201415.adapter.data.AiTile;
import org.totalboumboum.ai.v201415.adapter.path.AiLocation;
import org.totalboumboum.ai.v201415.adapter.tools.AiAbstractTools;
import org.totalboumboum.ai.v201415.adapter.tools.AiBombTools;
import org.totalboumboum.ai.v201415.adapter.tools.AiContactPointTools;
import org.totalboumboum.ai.v201415.adapter.tools.AiDirectionTools;
import org.totalboumboum.ai.v201415.adapter.tools.AiPixelDistanceTools;
import org.totalboumboum.ai.v201415.adapter.tools.AiPixelPositionTools;
import org.totalboumboum.ai.v201415.adapter.tools.AiTileDistanceTools;
import org.totalboumboum.ai.v201415.adapter.tools.AiTilePositionTools;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.images.PredefinedColor;

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
	// TOOLS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste de tous les outils de cette zone */
	protected final List<AiAbstractTools> tools = new ArrayList<AiAbstractTools>();
	/** Outils pemettant les calculs relatifs aux bombes et explosions */
	protected AiBombTools bombTools;
	/** Outils pemettant le calcul des points de contact */
	protected AiContactPointTools contactPointTools;
	/** Outils permettant le calcul des directions */
	protected AiDirectionTools directionTools;
	/** Outils permettant le calcul des distances en pixels */
	protected AiPixelDistanceTools pixelDistanceTools;
	/** Outils permettant le calcul des positions en pixels */
	protected AiPixelPositionTools pixelPositionTools;
	/** Outils permettant le calcul des distances en cases */
	protected AiTileDistanceTools tileDistanceTools;
	/** Outils permettant le calcul des positions en cases */
	protected AiTilePositionTools tilePositionTools;
	
	/**
	 * Renvoie les outils pemettant les calculs relatifs aux bombes et explosions.
	 * 
	 * @return
	 * 		Outils pemettant les calculs relatifs aux bombes et explosions.
	 */
	public AiBombTools getBombTools()
	{	return bombTools;
	}

	/**
	 * Renvoie les outils pemettant le calcul des points de contact.
	 * 
	 * @return
	 * 		Outils pemettant le calcul des points de contact.
	 */
	public AiContactPointTools getContactPointTools()
	{	return contactPointTools;
	}

	/**
	 * Renvoie les outils pemettant le calcul des directions.
	 * 
	 * @return
	 * 		Outils pemettant le calcul des directions.
	 */
	public AiDirectionTools getDirectionTools()
	{	return directionTools;
	}

	/**
	 * Renvoie les outils pemettant le calcul des distances en pixels.
	 * 
	 * @return
	 * 		Outils pemettant le calcul des distances en pixels.
	 */
	public AiPixelDistanceTools getPixelDistanceTools()
	{	return pixelDistanceTools;
	}

	/**
	 * Renvoie les outils pemettant le calcul des positions en pixels.
	 * 
	 * @return
	 * 		Outils pemettant le calcul des positions en pixels.
	 */
	public AiPixelPositionTools getPixelPositionTools()
	{	return pixelPositionTools;
	}

	/**
	 * Renvoie les outils pemettant le calcul des distances en cases.
	 * 
	 * @return
	 * 		Outils pemettant le calcul des positions en cases.
	 */
	public AiTileDistanceTools getTileDistanceTools()
	{	return tileDistanceTools;
	}

	/**
	 * Renvoie les outils pemettant le calcul des points de contact.
	 * 
	 * @return
	 * 		Outils pemettant le calcul des positions en cases.
	 */
	public AiTilePositionTools getTilePositionTools()
	{	return tilePositionTools;
	}
	
	/**
	 * Creates all the tools object required by this zone.
	 */
	private void initTools()
	{	
		
	}
	
	/**
	 * Avertit chaque outil qu'une nouvelle itération commence,
	 * et que les résultats de l'itération précédente doivent
	 * être invalidés.
	 */
	private void resetTools()
	{	for(AiAbstractTools t: tools)
			t.reset();
	}
	
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
	// RANKS			/////////////////////////////////////////////
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
	 * Initialise la taille des cases.
	 * 
	 * @param value 
	 * 		Taille d'une case.
	 */
	protected void initTileSize(double value)
	{	AiTile.size = value;
	}
	
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
		{	Integer value = hiddenItemsCounts.get(key);
			if(value==null)
				value = 0;
			double proba = value/total;
			result.put(key,proba);
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
	// PIXEL DIMENSIONS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Abscisse des pixels les plus à gauche */
	protected double pixelLeftX;
	/** Ordonnée des pixels les plus hauts */
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
	 * Renvoie la largeur du niveau en pixels.
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
	 * Renvoie la hauteur du niveau en pixels.
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
	// STRING		/////////////////////////////////////////////////
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
	{	boolean details = true && !GameData.PRODUCTION;
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
			if(details)
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
		if(details)
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
		if(details)
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
			if(details)
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
				if(details)
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
		if(details)
		{	result.append("\t└");
			for(int col=0;col<width-1;col++)
				result.append("─────┴");
			result.append("─────┘");
		}
		result.append("\n");
		
		return result.toString();
	}
}
