package org.totalboumboum.ai.v201415.adapter.tools;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.totalboumboum.ai.v201415.adapter.data.AiBomb;
import org.totalboumboum.ai.v201415.adapter.data.AiFire;
import org.totalboumboum.ai.v201415.adapter.data.AiHero;
import org.totalboumboum.ai.v201415.adapter.data.AiTile;
import org.totalboumboum.ai.v201415.adapter.data.AiZone;
import org.totalboumboum.ai.v201415.adapter.data.internal.AiDataZone;
import org.totalboumboum.ai.v201415.adapter.model.full.AiSimBlock;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Ensemble de méthodes liées aux calculs portant sur les bombes et explosions.
 * <br/>
 * Cet objet est initialisé automatiquement par l'API et disponible
 * via l'objet zone concerné (que ce soir {@link AiDataZone} ou {@link AiSimBlock}).
 * <br/>
 * <b>Attention :</b> Cette classe ne doit pas être instanciée par le concepteur 
 * de l'agent : récupérez une instance existante, grâce à l'objet zone concerné.
 *  
 * @author Vincent Labatut
 */
public final class AiBombTools extends AiAbstractTools
{
	/**
	 * Constructeur à ne pas utiliser. Réservé à l'API.
	 * 
	 * @param zone 
	 * 		Zone associée à cet objet.
	 */
	public AiBombTools(AiZone zone)
	{	super(zone);
	
		this.bombs = zone.getBombs();
		
		this.colors = new ArrayList<PredefinedColor>();
		List<AiHero> heroes = zone.getHeroes();
		for(AiHero hero: heroes)
		{	PredefinedColor color = hero.getColor();
			colors.add(color);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste des bombes de la zone */
	private List<AiBomb> bombs;
	/** Liste des couleurs des personnages de la zone */
	private List<PredefinedColor> colors;
	
	/////////////////////////////////////////////////////////////////
	// THREATENED BOMBS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map associant à chaque bombe la liste des bombes qu'elle menace */
	private final Map<AiBomb,List<AiBomb>> threatenedBombs = new HashMap<AiBomb, List<AiBomb>>();
	/** Version immuable de la map associant à chaque bombe la liste des bombes qu'elle menace */
	private final Map<AiBomb,List<AiBomb>> externalThreatenedBombs = Collections.unmodifiableMap(threatenedBombs);

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
	{	if(!updated)
			updateFuseMaps();
		return externalThreatenedBombs;
	}
	
	/////////////////////////////////////////////////////////////////
	// DELAYS BY BOMB		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map associant à chaque bombe le temps avant son explosion */
	private final Map<AiBomb,Long> delaysByBombs = new HashMap<AiBomb, Long>();
	/** Version immuable de la map associant à chaque bombe le temps avant son explosion */
	private final Map<AiBomb,Long> externalDelaysByBombs = Collections.unmodifiableMap(delaysByBombs);

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
	{	if(!updated)
			updateFuseMaps();
		return externalDelaysByBombs;
	}

	/////////////////////////////////////////////////////////////////
	// BOMBS BY DELAY		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map associant à chaque temps avant explosion la liste des bombes concernées */
	private final Map<Long,List<AiBomb>> bombsByDelays = new HashMap<Long, List<AiBomb>>();
	/** Version immuable de la map associant à chaque temps avant explosion la liste des bombes concernées */
	private final Map<Long,List<AiBomb>> externalBombsByDelays = Collections.unmodifiableMap(bombsByDelays);

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
	{	if(!updated)
			updateFuseMaps();
		return externalBombsByDelays;
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBS BY COLOR		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map associant à chaque couleur la liste des bombes correspondant */
	private final Map<PredefinedColor,List<AiBomb>> bombsByColor = new HashMap<PredefinedColor,List<AiBomb>>();
	/** Version immuable de la map associant à chaque couleur la liste des bombes correspondant */
	private final Map<PredefinedColor,List<AiBomb>> externalBombsByColor = Collections.unmodifiableMap(bombsByColor);
	/** Indique si les données ont été mises à jour durant la dernière itération */
	protected boolean updatedBombsByColor;
	
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
	{	if(!updatedBombsByColor)
			updateBombsByColor();
		List<AiBomb> result = externalBombsByColor.get(color);
		
		return result;
	}
	
	/**
	 * Met à jour la map des bombes classées en fonction
	 * de leur couleur.
	 */
	private void updateBombsByColor()
	{	// possibly create lists
		if(bombsByColor.isEmpty())
		{	for(PredefinedColor color: colors)
			{	List<AiBomb> list = new ArrayList<AiBomb>();
				bombsByColor.put(color,list);
				List<AiBomb> uList = Collections.unmodifiableList(list);
				externalBombsByColor.put(color,uList);
			}
		}
	
		// reset lists
		for(PredefinedColor color: colors)
		{	List<AiBomb> list = bombsByColor.get(color);
			list.clear();
		}
		
		// add bombs
		for(AiBomb bomb: bombs)
		{	PredefinedColor color = bomb.getColor();
			List<AiBomb> list = bombsByColor.get(color);
			list.add(bomb);
		}
		
		updatedBombsByColor = true;
	}
	
	/////////////////////////////////////////////////////////////////
	// GENERAL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Initialise différentes structures 
	 * contenant des données sur les bombes
	 * et explosions de cette zone.
	 */
	private void updateFuseMaps()
	{	delaysByBombs.clear();
		bombsByDelays.clear();
		threatenedBombs.clear();
		
		// retrieve necessary info
		for(AiBomb bomb: bombs)
		{	AiTile tile = bomb.getTile();
			List<AiFire> fires = tile.getFires();
			
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
			for(AiTile blastTile: blast)
			{	List<AiBomb> tileBombs = blastTile.getBombs();
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
		
		updated = true;
	}

	/////////////////////////////////////////////////////////////////
	// UPDATE					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void reset()
	{	super.reset();
		updatedBombsByColor = false;
	}
}
