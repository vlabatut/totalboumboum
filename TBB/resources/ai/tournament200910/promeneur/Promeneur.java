package tournament200910.promeneur;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import fr.free.totalboumboum.ai.adapter200910.ArtificialIntelligence;
import fr.free.totalboumboum.ai.adapter200910.communication.AiAction;
import fr.free.totalboumboum.ai.adapter200910.communication.AiActionName;
import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiBlock;
import fr.free.totalboumboum.ai.adapter200910.data.AiBomb;
import fr.free.totalboumboum.ai.adapter200910.data.AiFire;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiState;
import fr.free.totalboumboum.ai.adapter200910.data.AiStateName;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.data.AiZone;
import fr.free.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe impl�mente une IA relativement stupide, qui se contente
 * de se promener al�atoirement un peu partout dans le niveau,
 * en essayant d'�viter les explosions.
 * 
 * Attention, il s'agit juste d'un programme exemple, les m�thodes ne
 * sont certainement pas les meilleures !
 */
public class Promeneur extends ArtificialIntelligence 
{
	/**
	 * m�thode appel�e par le moteur du jeu 
	 * pour obtenir une action de l'IA
	 * 
	 * @throws StopRequestException
	 */
	public AiAction processAction() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		AiAction result = new AiAction(AiActionName.NONE);
		
		// premier appel : on initialise l'IA
		if(ownHero == null)
			init();		
		
		// si le personnage control� a �t� �limin�, inutile de continuer
		if(!ownHero.hasEnded())
		{	// on met � jour la position de l'ia dans la zone
			currentTile = ownHero.getTile();
			
			// arriv� � destination : on choisit une nouvelle destination
			if(currentTile==nextTile)
				pickNextTile();
			// au cas ou quelqu'un prendrait le contr�le manuel du personnage
			else if(previousTile!=currentTile)
			{	previousTile = currentTile;
				pickNextTile();			
			}
			// sinon (on garde la m�me direction) on v�rifie qu'un obstacle (ex: bombe) n'est pas apparu dans la case
			else
				checkNextTile();
			
			// on calcule la direction � prendre
			Direction direction = getPercepts().getDirection(currentTile,nextTile);
	
			// on calcule l'action
			if(direction!=Direction.NONE)
				result = new AiAction(AiActionName.MOVE,direction);
			
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// INITIALISATION			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * initialisation de l'IA lors du premier appel
	 * 
	 * @throws StopRequestException
	 */
	private void init() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		zone = getPercepts();
		ownHero = zone.getOwnHero();
	
		currentTile = ownHero.getTile();
		nextTile = currentTile;		
		previousTile = currentTile;		
	}
	
	/** la case occup�e actuellement par le personnage*/
	private AiTile currentTile;
	/** la case sur laquelle on veut aller */
	private AiTile nextTile = null;
	/** la derni�re case par laquelle on est pass� */ 
	private AiTile previousTile = null;

	/////////////////////////////////////////////////////////////////
	// PERCEPTS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** les percepts */
	private AiZone zone;
	/** le personnage command� par cette IA */
	private AiHero ownHero;
	
	/////////////////////////////////////////////////////////////////
	// CASES					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Choisit comme destination une case voisine de la case actuellement occup�e par l'IA.
	 * Cette case doit �tre accessible (pas de mur ou de bombe ou autre obstacle) et doit
	 * �tre diff�rente de la case pr�c�demment occup�e
	 * 
	 * @throws StopRequestException 
	 */
	private void pickNextTile() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		// liste des cases voisines accessibles	
		ArrayList<AiTile> tiles = getClearNeighbors(currentTile);
		// on sort de la liste la case d'o� l'on vient (pour �viter de repasser au m�me endroit)
		boolean canGoBack = false;
		if(tiles.contains(previousTile))
		{	tiles.remove(previousTile);
			canGoBack = true;
		}
		// s'il reste des cases dans la liste
		if(tiles.size()>0)
		{	// si la liste contient la case situ�e dans la direction d�placement pr�cedente,
			// on �vite de l'utiliser (je veux avancer en zig-zag et non pas en ligne droite)
			AiTile tempTile = null;
			Direction dir = getPercepts().getDirection(previousTile,currentTile);
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
			// sinon (pas le choix) on continue dans la m�me direction
			else
			{	nextTile = tempTile;
				previousTile = currentTile;
			}
		}
		// sinon (pas le choix) on tente de revenir en arri�re
		else
		{	if(canGoBack)
			{	nextTile = previousTile;
				previousTile = currentTile;
			}
			// et sinon on ne peut pas bouger, donc on ne fait rien du tout
		}
	}
	
	/**
	 * d�termine quelles sont les cases voisines sans danger
	 * 
	 * @param tile	case actuelle
	 * @return	la liste des cases voisines sans danger
	 * @throws StopRequestException
	 */
	private ArrayList<AiTile> getClearNeighbors(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// liste des cases autour de la case de r�f�rence
		List<AiTile> neighbors = tile.getNeighbors();
		// on garde les cases sans bloc ni bombe ni feu
		ArrayList<AiTile> result = new ArrayList<AiTile>();
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
	 * d�termine si une case est sans danger
	 * 
	 * @param tile	la case � tester
	 * @return	vrai ssi la case ne contient ni bombe, ni feu, ni bloc
	 * @throws StopRequestException
	 */
	private boolean isClear(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result;
		Collection<AiBlock> blocks = tile.getBlocks();
		Collection<AiBomb> bombs = tile.getBombs();
		Collection<AiFire> fires = tile.getFires();
		result = blocks.isEmpty()&& bombs.isEmpty() && fires.isEmpty();
		return result;
	}
	
	/**
	 * d�termine la case suivant o� se d�placer
	 * 
	 * @throws StopRequestException
	 */
	private void checkNextTile() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// si un obstacle est apparu sur la case destination, on change de destination
		if(!isClear(nextTile))
		{	// liste des cases voisines accessibles	
			ArrayList<AiTile> tiles = getClearNeighbors(currentTile);
			// on sort l'ancienne destination (qui est maintenant bloqu�e) de la liste
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
