package org.totalboumboum.ai.v201112.ais._suiveur;

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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.astar.cost.TileCostCalculator;
import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201112.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.heuristic.TileHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * classe chargée d'implémenter un déplacement, 
 * en respectant un chemin donné
 * 
 * @author Vincent Labatut
 */
public class TargetHandler extends AiMoveHandler<Suiveur>
{	/** interrupteur permettant d'afficher la trace du traitement */
	private boolean verbose = false;

	/**
	 * crée un PathManager chargé d'amener le personnage à la position (x,y)
	 * exprimée en pixels
	 */
	public TargetHandler(Suiveur ai) throws StopRequestException
	{	super(ai);
		ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		costCalculator = new TileCostCalculator(ai);
		heuristicCalculator = new TileHeuristicCalculator(ai);
		successorCalculator = new BasicSuccessorCalculator(ai);
		astar = new Astar(ai,ai.ownHero,costCalculator,heuristicCalculator,successorCalculator);
		updatePrev();
	}
	
	/////////////////////////////////////////////////////////////////
	// DESTINATION	/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si le personnage est arrivé à destination */
	private boolean arrived;
	/** l'abscisse de destination */
	private double xDest;
	/** l'ordonnée de destination */
	private double yDest;
	
	/**
	 * modifie la case de destination du personnage,
	 * place les coordonnées de destination au centre de cette case,
	 * et recalcule le chemin.
	 */
	public void setDestination(AiTile destination) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		arrived = false;
		currentDestination = destination;
		xDest = currentDestination.getPosX();
		yDest = currentDestination.getPosY();
		AiLocation location = new AiLocation(ai.currentX,ai.currentY,ai.getZone());
		try
		{	currentPath = astar.processShortestPath(location,destination);
		}
		catch (LimitReachedException e)
		{	//e.printStackTrace();
			currentPath = new AiPath();
		}
	}

	/**
	 * modifie les coordonnées de destination,
	 * met à jour automatiquement la case correspondante,
	 * et recalcule le chemin.
	 */
	public void setDestination(double x, double y) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		AiZone zone = ai.getZone();
		arrived = false;
		double normalized[] = zone.normalizePosition(x, y);
		xDest = normalized[0];
		yDest = normalized[1];
//		tileDest = zone.getTile(xDest,yDest);
		AiLocation location = new AiLocation(ai.currentX,ai.currentY,zone);
		try
		{	currentPath = astar.processShortestPath(location,currentDestination);
		}
		catch (LimitReachedException e)
		{	//e.printStackTrace();
			currentPath = new AiPath();
		}
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
/*	private boolean hasArrived(AiTile tile) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		AiHero ownHero = ai.getOwnHero();
		boolean result = ai.getZone().hasSamePixelPosition(ownHero,tile);
		return result;
	}
*/
	/**
	 * détermine si le personnage est arrivé aux coordonnées de destination
	 */
	public boolean hasArrived() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		AiZone zone = ai.getZone();
		if(currentPath==null)
			arrived = false;
		else
		{	// on teste si le personnage est à peu près situé à la position de destination 
			AiHero ownHero = ai.ownHero;
			double xCurrent = ownHero.getPosX();
			double yCurrent = ownHero.getPosY();
			arrived = zone.hasSamePixelPosition(xCurrent,yCurrent,xDest,yDest);
			// cas particulier : oscillation autour du point d'arrivée
			if(!arrived && currentPath.getLength()==1)
			{	Direction prevDir = zone.getDirection(xPrev,yPrev,xDest,yDest);
				Direction currentDir = zone.getDirection(xCurrent,yCurrent,xDest,yDest);
				arrived = prevDir.getOpposite()==currentDir;
			}
		}
		
		return arrived;
	}

	/////////////////////////////////////////////////////////////////
	// PREVIOUS LOCATION	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abscisse précédente */
	private double xPrev;
	/** ordonnée précédente */
	private double yPrev;	
	
	/**
	 * met à jour la position précédente du personnage,
	 * exprimée en pixels
	 */
	private void updatePrev() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		AiHero hero = ai.ownHero;
		xPrev = hero.getPosX();
		yPrev = hero.getPosY();		
	}

	/////////////////////////////////////////////////////////////////
	// PATH			/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * vérifie que le personnage est bien sur le chemin pré-calculé,
	 * en supprimant si besoin les cases inutiles.
	 * Si le personnage n'est plus sur le chemin, alors le chemin
	 * est vide après l'exécution de cette méthode.
	 */
	private void checkIsOnPath() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		if(currentPath==null)
			currentPath = new AiPath();
		else
		{	AiTile currentTile = ai.currentTile;
			while(!currentPath.isEmpty() && !currentPath.getLocation(0).getTile().equals(currentTile))
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				
				currentPath.removeLocation(0);
			}
		}
	}
	
	/**
	 * détermine si le personnage a dépassé la première case du chemin
	 * en direction de la seconde case
	 */
/*	private boolean hasCrossed(AiTile tile) throws StopRequestException
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
		}
		return result;
	}
*/
	
	/** 
	 * teste si le chemin est toujours valide, i.e. s'il
	 * est toujours sûr et si aucun obstacle n'est apparu
	 * depuis la dernière itération
	 */
	private boolean checkPathValidity() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result = true;
		Iterator<AiLocation> it = currentPath.getLocations().iterator();
		while(it.hasNext() && result)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			AiLocation location = it.next();
			AiTile tile = location.getTile();
			result = tile.isCrossableBy(ai.ownHero) && ai.safetyHandler.isSafe(tile);
//			if(!result)
//				System.out.println();
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// A STAR					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** classe implémentant l'algorithme A* */
	private Astar astar;
	/** classe implémentant la fonction heuristique */
	private HeuristicCalculator heuristicCalculator;
	/** classe implémentant la fonction de coût */
	private CostCalculator costCalculator;
	/** classe implémentant la fonction successeur */
	private SuccessorCalculator successorCalculator;

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/** 
	 * calcule la prochaine direction pour aller vers la destination 
	 *(ou renvoie Direction.NONE si aucun déplacement n'est nécessaire)
	 * */
	@Override
	protected Direction considerMoving() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		AiZone zone = ai.getZone();
		Direction result = Direction.NONE;

		updateTarget();
		if(target!=null)
		{	if(!hasArrived())
			{	// on vérifie que le joueur est toujours sur le chemin
				checkIsOnPath();
				// si le chemin est vide ou invalide, on le recalcule
				if(currentPath==null || currentPath.isEmpty() || !checkPathValidity())
				{	AiLocation location = new AiLocation(ai.currentTile);
					try
					{	currentPath = astar.processShortestPath(location,currentDestination);
					}
					catch (LimitReachedException e)
					{	//e.printStackTrace();
						currentPath = new AiPath();
					}
				}
				if(currentPath!=null && checkPathValidity())
				{	// s'il reste deux cases au moins dans le chemin, on se dirige vers la suivante
					if(currentPath.getLength()>1)
					{	AiTile tile = currentPath.getLocation(1).getTile();
						result = zone.getDirection(ai.ownHero,tile);	
					}
					// sinon, s'il ne reste qu'une seule case, on va au centre
					else if(currentPath.getLength()>0)
					{	AiHero ownHero = ai.ownHero;
						double x1 = ownHero.getPosX();
						double y1 = ownHero.getPosY();
						result = zone.getDirection(x1,y1,xDest,yDest);
					}
				}
			}
			
			// mise à jour de la position précédente
			updatePrev();
			// mise à jour de la sortie
			updateOutput();
			
			if(verbose)
			{	System.out.println(">>>>>>>>>> PATH MANAGER <<<<<<<<<<");
				System.out.println("path: "+currentPath);
				System.out.println("direction: "+result);
			}
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TARGET					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** la cible à suivre (ou null si aucune cible n'existe) */
	private AiHero target;
	/** case précédente de la cible */
	private AiTile targetPreviousTile;
	
	/**
	 * choisit aléatoirement un joueur comme cible à suivre
	 */
	private void chooseTarget() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		target = null;
		List<AiHero> heroes = new ArrayList<AiHero>(ai.getZone().getRemainingHeroes());
		heroes.remove(ai.ownHero);
		if(!heroes.isEmpty())
		{	int index = (int)Math.random()*heroes.size();
			target = heroes.get(index);
		}
	}

	/**
	 * met à jour la cible, et éventuellement le chemin jusqu'à elle
	 */
	private void updateTarget() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		if(target==null || target.hasEnded())
		{	chooseTarget();
			if(target!=null)
			{	AiTile targetCurrentTile = target.getTile();
				setDestination(targetCurrentTile);
				targetPreviousTile = targetCurrentTile; 
			}
		}
		else
		{	AiTile targetCurrentTile = target.getTile();
			if(targetCurrentTile==ai.currentTile)
			{	double targetX = target.getPosX();
				double targetY = target.getPosY();
				setDestination(targetX,targetY);				
			}
			else if(targetCurrentTile!=targetPreviousTile)
			{	setDestination(targetCurrentTile);
				targetPreviousTile = targetCurrentTile;				
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * met à jour la sortie graphique de l'IA en fonction du
	 * chemin courant
	 */
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		if(currentPath!=null && !currentPath.isEmpty())	
		{	AiOutput output = ai.getOutput();
			Color color = Color.BLACK;
			output.addPath(currentPath, color);
		}
	}
}
