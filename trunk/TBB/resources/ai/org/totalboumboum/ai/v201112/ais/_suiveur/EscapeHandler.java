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
import java.util.Iterator;
import java.util.Set;

import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201112.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.astar.successor.BasicSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * classe chargée d'implémenter un déplacement de fuite,
 * (personnage menacé par une ou plusieurs bombes) 
 * 
 * @author Vincent Labatut
 */
public class EscapeHandler extends AiMoveHandler<Suiveur>
{	/** interrupteur permettant d'afficher la trace du traitement */
	private boolean verbose = false;

	/**
	 * crée un EscapeManager chargé d'amener le personnage 
	 * au centre d'une case sûre
	 */
	public EscapeHandler(Suiveur ai) throws StopRequestException
	{	super(ai);
		ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		// init A*
		AiZone zone = ai.getZone();
		double costMatrix[][] = new double[zone.getHeight()][zone.getWidth()];
		costCalculator = new MatrixCostCalculator(ai,costMatrix);
		heuristicCalculator = new BasicHeuristicCalculator(ai);
		successorCalculator = new BasicSuccessorCalculator(ai);
		astar = new Astar(ai,ai.ownHero,costCalculator,heuristicCalculator,successorCalculator);
		
		// init destinations
		arrived = false;
		AiHero ownHero = ai.ownHero;
		possibleDest = ai.safetyHandler.findSafeTiles(ownHero.getTile());
		updatePath();
	}
	
	/////////////////////////////////////////////////////////////////
	// DESTINATION	/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si le personnage est arrivé à destination */
	private boolean arrived;
	/** destinations potentielles */
	private Set<AiTile> possibleDest;

	/**
	 * détermine si le personnage est arrivé dans la case de destination.
	 * S'il n'y a pas de case de destination, on considère que le personnage
	 * est arrivé.
	 */
	public boolean hasArrived() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		if(!arrived)
		{	if(currentDestination==null)
				arrived = true;
			else
			{	AiTile currentTile = ai.currentTile;
				arrived = currentTile==currentDestination;			
			}
		}
		
		return arrived;
	}

	/////////////////////////////////////////////////////////////////
	// PATH			/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le chemin à suivre */
	private AiPath path;
	
	private void updatePath() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		AiLocation location = new AiLocation(ai.currentX,ai.currentY,ai.getZone());
		try
		{	path = astar.processShortestPath(location,possibleDest);
		}
		catch (LimitReachedException e)
		{	//e.printStackTrace();
			path = new AiPath();
		}
		
		if(path==null || path.isEmpty())
			currentDestination = ai.currentTile;
		else
			currentDestination = path.getLastLocation().getTile();
	}
	
	/**
	 * vérifie que le personnage est bien sur le chemin pré-calculé,
	 * en supprimant si besoin les cases inutiles (car précedent la case courante).
	 * Si le personnage n'est plus sur le chemin, alors le chemin
	 * est vide après l'exécution de cette méthode.
	 */
	private void checkIsOnPath() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		AiTile currentTile = ai.currentTile;
		while(!path.isEmpty() && !path.getLocation(0).getTile().equals(currentTile))
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			path.removeLocation(0);
		}
	}
	
	/** 
	 * teste si le chemin est toujours valide, i.e. si
	 * aucun obstacle n'est apparu depuis la dernière itération.
	 * Contrairement au PathManager, ici pour simplifier on ne teste
	 * que l'apparition de nouveaux obstacles (feu, bombes, murs), et non pas 
	 * les changement concernant la sûreté des cases. En d'autres termes,
	 * si une bombe apparait avant que le personnage d'ait atteint une
	 * case sure, elle ne sera pas prise en compte dans la trajectoire.
	 * 
	 */
	private boolean checkPathValidity() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result = true;
		Iterator<AiLocation> it = path.getLocations().iterator();
		while(it.hasNext() && result)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			AiTile tile = it.next().getTile();
			result = tile.isCrossableBy(ai.ownHero);			
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
	private MatrixCostCalculator costCalculator;
	/** classe implémentant la fonction successeur */
	private BasicSuccessorCalculator successorCalculator;

	private void updateCostCalculator() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		AiZone zone = ai.getZone();
		// calcul de la matrice de coût : on prend l'opposé du niveau de sûreté
		// i.e. : plus le temps avant l'explosion est long, plus le coût est faible 
		double safetyMatrix[][] = ai.safetyHandler.getMatrix();
		for(int line=0;line<zone.getHeight();line++)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				
				double cost = -safetyMatrix[line][col];
				costCalculator.setCost(line,col,cost);
			}
		}
	}
	
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
		// on met d'abord à jour la matrice de cout
		updateCostCalculator();
	
		Direction result = Direction.NONE;
		if(!hasArrived())
		{	// on vérifie que le joueur est toujours sur le chemin
			checkIsOnPath();
			// si le chemin est vide ou invalide, on le recalcule.
			if(path.isEmpty() || !checkPathValidity())
				updatePath();
			// s'il reste deux cases au moins dans le chemin, on se dirige vers la suivante
			AiTile tile = null;
			if(path.getLength()>1)
				tile = path.getLocation(1).getTile();
			// sinon, s'il ne reste qu'une seule case, on va au centre
			else if(path.getLength()>0)
				tile = path.getLocation(0).getTile();
			// on détermine la direction du prochain déplacement
			if(tile!=null)
				result = zone.getDirection(ai.ownHero,tile);			
		}
		
		// mise à jour de la sortie
		updateOutput();

		if(verbose)
		{	System.out.println(">>>>>>>>>> ESCAPE MANAGER <<<<<<<<<<");
			System.out.println("path: "+path);
			System.out.println("direction: "+result);
		}
		return result;
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
		
		if(path!=null && !path.isEmpty())	
		{	AiOutput output = ai.getOutput();
			Color color = Color.RED;
			output.addPath(path, color);
		}
	}
}
