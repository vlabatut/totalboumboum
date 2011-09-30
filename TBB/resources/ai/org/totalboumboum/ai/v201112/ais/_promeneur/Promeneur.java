package org.totalboumboum.ai.v201112.ais._promeneur;

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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe implémente un agent très faible,
 * qui se contente de se promener aléatoirement dans la zone
 * de jeu. Il ne se déplace pas complètement au hasard,
 * car il n'entre pas sur une case qui contient du feu.
 * Par contre, il n'anticipe pas du tout les explosions
 * à venir.
 * 
 * @author Vincent Labatut
 * @version 2 - version adaptée à l'API 2011-2012
 */
public class Promeneur extends ArtificialIntelligence 
{
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** la zone de jeu */ 
	private AiZone zone = null;
	/** le personnage contrôlé par l'agent*/
	private AiHero ownHero = null;
	
	@Override
	public void init() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		zone = getZone();
		ownHero = zone.getOwnHero();
		
		currentTile = ownHero.getTile();
		nextTile = currentTile;		
		previousTile = currentTile;		
	}
	
	@Override
	public void updatePercepts() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE

		// on met à jour la position de l'ia dans la zone
		currentTile = ownHero.getTile();
	}
	
	/////////////////////////////////////////////////////////////////
	// MOVE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** la case occupée actuellement par le personnage*/
	private AiTile currentTile;
	/** la case sur laquelle on veut aller */
	private AiTile nextTile = null;
	/** la dernière case par laquelle on est passé */ 
	private AiTile previousTile = null;

	@Override
	public Direction considerMoving() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		// arrivé à destination : on choisit une nouvelle destination
		if(currentTile==nextTile)
			pickNextTile();
		
		// au cas ou quelqu'un prendrait le Contrôle manuel du personnage
		else if(previousTile!=currentTile)
		{	previousTile = currentTile;
			pickNextTile();			
		}
		
		// sinon (on garde la même direction) on vérifie qu'un obstacle (ex: bombe) n'est pas apparu dans la case
		else
			checkNextTile();
		
		// on détermine la direction de déplacement
		Direction result = zone.getDirection(currentTile,nextTile);
		
		return result;
	}
	
	/**
	 * Choisit comme destination une case voisine de la case actuellement occupée par l'IA.
	 * Cette case doit être accessible (pas de mur ou de bombe ou autre obstacle) et doit
	 * être différente de la case précédemment occupée
	 * 
	 * @throws StopRequestException 
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	private void pickNextTile() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		// liste des cases voisines accessibles	
		List<AiTile> tiles = getClearNeighbors(currentTile);
		// on sort de la liste la case d'où l'on vient (pour éviter de repasser au même endroit)
		boolean canGoBack = false;
		if(tiles.contains(previousTile))
		{	tiles.remove(previousTile);
			canGoBack = true;
		}
		// s'il reste des cases dans la liste
		if(tiles.size()>0)
		{	// si la liste contient la case située dans la direction déplacement précedente,
			// on évite de l'utiliser (je veux avancer en zig-zag et non pas en ligne droite)
			AiTile tempTile = null;
			Direction dir = getZone().getDirection(previousTile,currentTile);
			if(dir!=Direction.NONE)
			{	tempTile =  currentTile.getNeighbor(dir);
				if(tiles.contains(tempTile))
					tiles.remove(tempTile);
			}
			// s'il reste des cases dans la liste
			if(tiles.size()>0)
			{	// on en tire une au hasard
				double p = Math.random()*tiles.size();
				int index = (int)p;
				nextTile = tiles.get(index);
				previousTile = currentTile;
			}
			// sinon (pas le choix) on continue dans la même direction
			else
			{	nextTile = tempTile;
				previousTile = currentTile;
			}
		}
		// sinon (pas le choix) on tente de revenir en arrière
		else
		{	if(canGoBack)
			{	nextTile = previousTile;
				previousTile = currentTile;
			}
			// et sinon on ne peut pas bouger, donc on ne fait rien du tout
		}
	}
	
	/**
	 * Détermine la liste des cases voisines celle passée en paramètre
	 * et dans lesquelles l'agent a la possibilité de se déplacer.
	 * 
	 * @param tile
	 * 		La case dont on veut tester les voisines.
	 * @return
	 * 		La liste de cases voisines dans lesquelles l'agent peut se déplacer.
	 * 
	 * @throws StopRequestException
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	private List<AiTile> getClearNeighbors(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// liste des cases autour de la case de référence
		Collection<AiTile> neighbors = tile.getNeighbors();
		// on garde les cases sans bloc ni bombe ni feu
		List<AiTile> result = new ArrayList<AiTile>();
		Iterator<AiTile> it = neighbors.iterator();
		while(it.hasNext())
		{	checkInterruption(); //APPEL OBLIGATOIRE
		
			AiTile t = it.next();
			if(isClear(t))
				result.add(t);
		}
		return result;
	}
	
	/**
	 * Cette méthode teste si l'agent peut se déplacer
	 * dans la case passée en paramètre.
	 * 
	 * @param tile
	 * 		La case à vérifier.
	 * @return
	 * 		{@code true} ssi l'agent peut pénétrer dans la case.
	 * 
	 * @throws StopRequestException
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	private boolean isClear(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result;
		List<AiBlock> blocks = tile.getBlocks();
		Collection<AiBomb> bombs = tile.getBombs();
		Collection<AiFire> fires = tile.getFires();
		result = blocks.isEmpty() && bombs.size()==0 && fires.size()==0;
		return result;
	}
	
	/**
	 * Détermine si la case dans laquelle on veut aller est toujours
	 * propre à être parcourue, ou bien si elle est devenue impraticable
	 * (par exemple du feu est apparu à l'intérieur). Dans ce dernier
	 * cas, il est nécessaire de se trouver une nouvelle case de destination.
	 * 
	 * @throws StopRequestException
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	private void checkNextTile() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// si un obstacle est apparu sur la case destination, on change de destination
		if(!isClear(nextTile))
		{	// liste des cases voisines accessibles	
			List<AiTile> tiles = getClearNeighbors(currentTile);
			// on sort l'ancienne destination (qui est maintenant bloquée) de la liste
			if(tiles.contains(nextTile))
				tiles.remove(nextTile);
			// s'il reste des cases dans la liste : on en tire une au hasard
			if(tiles.size()>0)
			{	double p = Math.random()*tiles.size();
				int index = (int)p;
				nextTile = tiles.get(index);
				previousTile = currentTile;
			}
		}
	}
}
