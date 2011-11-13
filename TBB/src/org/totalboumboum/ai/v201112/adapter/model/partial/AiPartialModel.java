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

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;

/**
 * TODO à corriger
 * Cette classe est chargée de simuler l'évolution d'une zone.
 * Pour cela, une modèle doit d'abord être initialisé avec une zone de départ,
 * obtenue simplement à partir des percepts de l'agent.<br/>
 * Pour prèserver la cohérence de la zone, l'utilisateur ne peut 
 * pas la modifier directement, mais seulement à travers les 
 * méthodes proposées dans cette classe. Il peut :<ul>
 * 		<li> réaliser un ou plusieurs pas de simulation et obtenir la zone résultante.</li>
 * 		<li> demander à un des joueurs de poser une bombe</li>
 * 		<li> modifier la direction de déplacement d'un joueur, ou arrêter son déplacement</li>
 * 		<li> demander à une bombe d'exploser</li></ul>
 * Au cours de la simulation, une nouvelle zone est calculée et stockée
 * en interne : l'utilisateur peut alors y accéder et l'utiliser. Si
 * de nouveaux pas de simulation sont effectués, cette zone interne est 
 * remplacée par le résultats de ces simulations.<br/>
 * L'utilisateur peut également récupérer le temps écoulé entre deux simulations.<br/>
 * Il faut souligner que les pas de simulation sont déterminés de façon évènementielle.
 * En d'autres termes, un pas se termine quand un évènement se produit. Les 
 * évènements considérés par cette classe sont :<ul>
 * 		<li> la disparition ou l'apparition d'un sprite (ex : une bombe qui a explosé, un item qui apparait)
 * 		<li> un changement d'état (ex : un mur qui commence à brûler)
 * 		<li> un changement de case (ex : un joueur se déplaçant d'une case à une autre)
 * 		<li> la fin d'un déplacement (ex : un joueur qui se retrouve bloqué par un mur)</ul>
 * Dès qu'un de ces évènements se produit, le pas de simulation se termine.
 * Le modèle donne accès à la liste des sprites qui ont été impliqués dans un des évènements
 * causant la fin du pas de simulation.<br/>
 * Vous pouvez observer une illustration du fonctionnement de ce modèle en exécutant
 * la classe AiModelTest. Notez toute fois que cette classe de test crée la zone
 * en partant de rien, alors que les agents disposent de leurs percepts.
 * Pour cette raison, elle utilise pour initialiser la zone des méthodes 
 * auxquelles les agents n'ont pas accès.
 * 
 * @author Vincent Labatut
 *
 */
public class AiPartialModel
{	
	/**
	 * initialise le modèle avec la zone passée en paramètre.
	 * 
	 * @param currentZone
	 * 		la zone courante, qui servira de point de départ à la simulation
	 */
	public AiPartialModel(AiZone zone)
	{	// dimensions
		this.width = zone.getWidth();
		this.height = zone.getHeight();
		
		// hero
		ownHero = zone.getOwnHero();
		
		// obstacles
		obstacles = new boolean[height][width];
		initObstacles(zone);
		
		// explosions
		explosions = new AiExplosionList[height][width];
		initExplosions(zone);
	}	
	
	/////////////////////////////////////////////////////////////////
	// DIMENSIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int width;
	private int height;
	
	/////////////////////////////////////////////////////////////////
	// HERO				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AiHero ownHero;
	
	/////////////////////////////////////////////////////////////////
	// OBSTACLES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** matrice contenant tous les obstacles pour l'agent */
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
			int col = tile.getCol();
			int row = tile.getRow();
			obstacles[row][col] = crossable;
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// EXPLOSIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** matrice contenant toutes les explosions prévues */
	private AiExplosionList[][] explosions;
	/** Map associant à chaque bombe la liste des bombes qu'elle menace */
	private HashMap<AiBomb,List<AiBomb>> threatenedBombs;
	/** Map associant à chaque bombe le temps avant son explosion */
	private HashMap<AiBomb,Long> delaysByBombs;
	/** Map associant à chaque temps avant explosion la liste des bombes concernées */
	private HashMap<Long,List<AiBomb>> bombsByDelays;
	
	
	/**
	 * NOTE
	 * a-t-on vraiment besoin d'obstacles ?
	 * >> si la case est touchée par une explosion, elle sera vide à la fin de l'explosion,
	 * 	  de toute façon
	 */
	
	/**
	 * Analyse la zone passée en paramètre et
	 * en déduit le contenu de la matrice des
	 * explosions.
	 * 
	 * @param zone
	 * 		La zone de référence.
	 */
	private void initExplosions(AiZone zone)
	{	delaysByBombs = zone.getDelaysByBombs();
		bombsByDelays = zone.getBombsByDelays();
		threatenedBombs = zone.getThreatenedBombs();
		
		// on rajoute dans les matrice les feux existant
		{	List<AiFire> fires = zone.getFires();
			for(AiFire fire: fires)
			{	int col = fire.getCol();
				int row = fire.getRow();
				long time = fire.getTime();
				long duration = fire.getBurningDuration();
				long endTime = duration - time;
				AiExplosionList list = explosions[row][col];
				if(list==null)
				{	list = new AiExplosionList();
					explosions[row][col] = list;
				}
				AiExplosion explosion = new AiExplosion(0,endTime);
				list.add(explosion);
			}
		}
		
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
				{	int col = tile.getCol();
					int row = tile.getRow();
					AiExplosionList list = explosions[row][col];
					if(list==null)
					{	list = new AiExplosionList();
						explosions[row][col] = list;
					}
					AiExplosion explosion = new AiExplosion(delay,endTime);
					list.add(explosion);
				}
			}
		}
	}
}
