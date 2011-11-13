package org.totalboumboum.ai.v201112.adapter.model;

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
import java.util.List;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.model.partial.AiExplosion;
import org.totalboumboum.ai.v201112.adapter.model.partial.AiExplosionList;

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
public class AiAbstractModel
{	
	/////////////////////////////////////////////////////////////////
	// EXPLOSIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map associant à chaque bombe la liste des bombes qu'elle menace */
	private HashMap<AiBomb,List<AiBomb>> threatenedBombs = new HashMap<AiBomb,List<AiBomb>>();
	/** Map associant à chaque bombe le temps avant son explosion */
	private HashMap<AiBomb,Long> delaysByBombs;
	/** Map associant à chaque temps avant explosion la liste des bombes concernées */
	private HashMap<Long,List<AiBomb>> bombsByDelays = new HashMap<Long,List<AiBomb>>();

	/**
	 * Analyse la zone passée en paramètre et
	 * en déduit le contenu de la matrice des
	 * explosions.
	 * 
	 * @param zone
	 * 		La zone de référence.
	 */
	private void initExplosions(AiZone zone)
	{	List<AiBomb> bombs = zone.getBombs();
		delaysByBombs = new HashMap<AiBomb,Long>();
		bombsByDelays = new HashMap<Long,List<AiBomb>>();
		threatenedBombs = new HashMap<AiBomb,List<AiBomb>>();
		
		// retrieve necessary info
		for(AiBomb bomb: bombs)
		{	List<AiFire> fires = bomb.getTile().getFires();
			// delay map & bomb map
			long delay = Long.MAX_VALUE; //TODO remote bombs are considered to have an infinite delay, which should be corrected
			// fire-sensitive bomb currently caught in an explosion
			if(bomb.hasExplosionTrigger() && !fires.isEmpty())
			{	long fireDuration = 0;
				for(AiFire fire: fires)
				{	long time = fire.getTime();
					if(time>fireDuration)
						fireDuration = time;
				}
				delay = Math.max(bomb.getLatencyDuration()-fireDuration,0);
			}
			// time bomb
			if(bomb.hasCountdownTrigger())
			{	long temp = Math.max(bomb.getNormalDuration()-bomb.getTime(),0);
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
			threatenedBombs.put(bomb,tempTarget);
		}
		
		// include existing fires in the matrix
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
		
		// get temporal explosion order
		TreeSet<Long> orderedDelays = new TreeSet<Long>(bombsByDelays.keySet());
		while(!orderedDelays.isEmpty())
		{	// get the delay
			long delay = orderedDelays.first();
			orderedDelays.remove(delay);
			// get the bombs associated to this delay
			List<AiBomb> bombList = bombsByDelays.get(delay);
			
			// process each bomb for the current delay
			for(AiBomb bomb1: bombList)
			{	// add explosion to the matrix
				{	long endTime = delay + bomb1.getExplosionDuration();
					// get the bomb blast
					List<AiTile> blast = bomb1.getBlast();
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
				
				// update threatened bombs delays while considering a bomb 
				// can detonate another one before the regular time 
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
	}
}
