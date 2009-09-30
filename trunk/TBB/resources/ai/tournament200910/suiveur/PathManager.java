package tournament200910.suiveur;

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

import java.util.Iterator;

import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.path.AiPath;
import fr.free.totalboumboum.ai.adapter200910.path.astar.Astar;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.BasicCostCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.CostCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.BasicHeuristicCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.HeuristicCalculator;
import fr.free.totalboumboum.engine.content.feature.Direction;

/**
 * classe chargée d'implémenter un déplacement, 
 * en respectant un chemin donné
 */
public class PathManager
{
	/** interrupteur permettant d'afficher la trace du traitement */
	private boolean verbose = true;

	public PathManager(Suiveur ai, AiTile destination) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		this.ai = ai;
		costCalculator = new BasicCostCalculator();
		heuristicCalculator = new BasicHeuristicCalculator();
		astar = new Astar(ai.getOwnHero(),costCalculator,heuristicCalculator);
		setDestination(destination);
	}
	
	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** l'IA concernée par ce gestionnaire de chemin */
	private Suiveur ai;

	/////////////////////////////////////////////////////////////////
	// DESTINATION	/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** la case de destination */
	private AiTile destination;
	
	/**
	 * modifie la case de destination du personnage
	 * et recalcule le chemin.
	 */
	public void setDestination(AiTile destination) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		this.destination = destination;
		path = astar.processShortestPath(ai.getCurrentTile(),destination);
	}

	/*	public boolean hasArrived() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result = true;
		result = result && destination.getPosX()==ai.getCurrentX();
		result = result && destination.getPosY()==ai.getCurrentY();
		return result;
	}
*/	

	/**
	 * détermine si le personnage est arrivé au centre de la case
	 * passée en paramètre
	 */
	private boolean hasArrived(AiTile tile) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		AiHero ownHero = ai.getOwnHero();
		boolean result = ai.getZone().hasSamePixelPosition(ownHero,tile);
		return result;
	}

	/**
	 * détermine si le personnage est arrivé au centre de la case de destination
	 */
	public boolean hasArrived() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		boolean result = hasArrived(destination);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PATH			/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le chemin à suivre */
	private AiPath path;
	
	/**
	 * vérifie que le personnage est bien sur le chemin pré-calculé,
	 * en supprimant si besoin les cases inutiles.
	 * Si le personnage n'est plus sur le chemin, alors le chemin
	 * est vide après l'exécution de cette méthode.
	 */
	private void checkIsOnPath() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		AiTile currentTile = ai.getCurrentTile();
		while(!path.isEmpty() && path.getTile(0)!=currentTile)
			path.removeTile(0);		
	}
	
	/**
	 * détermine si le personnage a dépassé la première case du chemin
	 * en direction de la seconde case
	 */
	private boolean hasCrossed(AiTile tile) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result = false;
		if(path.getLength()>1)
		{	AiHero hero = ai.getOwnHero();
			AiTile source = path.getTile(0);
			AiTile target = path.getTile(1);
			Direction direction = ai.getZone().getDirection(source,target);
			double pos0;
			double pos1;
			double pos2;
			if(direction.isHorizontal())
			{	pos0 = source.getPosX();
				pos1 = hero.getPosX();
				pos2 = target.getPosX();
			}
			else
			{	pos0 = source.getPosY();
				pos1 = hero.getPosY();
				pos2 = target.getPosY();
			}
			result = pos0<=pos1 && pos1<=pos2 || pos0>=pos1 && pos1>=pos2;
//TODO pb ici : l'encadrement est toujours vrai puisque le niveau est circulaire !
			//p-ê carrément laisser tomber la contrainte de passer par le centre des cases ?
			//et donner comme objectif une position en pixel (pvt être un centre)
		}
		return result;
	}
	
	/** 
	 * teste si le chemin est toujours valide, i.e. s'il
	 * est toujours sûr et si aucun obstacle n'est apparu
	 * depuis la dernière itération
	 */
	private boolean checkPathValidity() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result = true;
		Iterator<AiTile> it = path.getTiles().iterator();
		while(it.hasNext() && result)
		{	AiTile tile = it.next();
			result = tile.isCrossableBy(ai.getOwnHero()) && ai.isSafe(tile);
			
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// A STAR					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Astar astar;
	private HeuristicCalculator heuristicCalculator;
	private CostCalculator costCalculator;

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/** 
	 * calcule la prochaine direction pour aller vers la destination 
	 *(ou Direction.NONE si aucun déplacement n'est nécessaire)
	 * */
	public Direction update() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		Direction result = Direction.NONE;
		if(!hasArrived())
		{	// on vérifie que le joueur est toujours sur le chemin
			checkIsOnPath();
			// si le chemin est vide ou invalide, on le recalcule
			if(path.isEmpty() || !checkPathValidity())
				path = astar.processShortestPath(ai.getCurrentTile(),destination);
			// si le chemin courant est non vide, on poursuit la route
			if(!path.isEmpty())
			{	AiTile tile = path.getTile(0);
				// on teste si on est arrivé ou si on a dépassé la case suivante
				if(hasArrived(tile) || hasCrossed(tile))
				{	// si oui, on passe à la prochaine case
					if(path.getLength()>1)
						tile = path.getTile(1);
					else
						tile = null;					
				}
				// on détermine la direction vers la prochaine case
				if(tile!=null)
					result = ai.getZone().getDirection(ai.getOwnHero(),tile);
			}
		}
		
		if(verbose)
		{	System.out.println(">>>>>>>>>> PATH MANAGER <<<<<<<<<<");
			System.out.println("path: "+path);
			System.out.println("direction: "+result);
		}
		return result;
	}
}
