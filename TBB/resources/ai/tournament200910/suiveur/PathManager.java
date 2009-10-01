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
import fr.free.totalboumboum.ai.adapter200910.data.AiZone;
import fr.free.totalboumboum.ai.adapter200910.path.AiPath;
import fr.free.totalboumboum.ai.adapter200910.path.astar.Astar;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.BasicCostCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.CostCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.BasicHeuristicCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.HeuristicCalculator;
import fr.free.totalboumboum.engine.content.feature.Direction;

/**
 * classe charg�e d'impl�menter un d�placement, 
 * en respectant un chemin donn�
 */
public class PathManager
{
	/** interrupteur permettant d'afficher la trace du traitement */
	private boolean verbose = true;

	public PathManager(Suiveur ai, AiTile destination) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		init(ai);
		setDestination(destination);
	}
	
	public PathManager(Suiveur ai, double x, double y) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		init(ai);
		setDestination(x,y);
	}
	
	/**
	 * initialise ce PathManager
	 */
	private void init(Suiveur ai) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		this.ai = ai;
		costCalculator = new BasicCostCalculator();
		heuristicCalculator = new BasicHeuristicCalculator();
		astar = new Astar(ai.getOwnHero(),costCalculator,heuristicCalculator);
		updatePrev();
	}
	
	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** l'IA concern�e par ce gestionnaire de chemin */
	private Suiveur ai;

	/////////////////////////////////////////////////////////////////
	// DESTINATION	/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si le personnage est arriv� � destination */
	private boolean arrived;
	/** la case de destination */
	private AiTile tileDest;
	/** l'abscisse de destination */
	private double xDest;
	/** l'ordonn�e de destination */
	private double yDest;
	
	/**
	 * modifie la case de destination du personnage,
	 * place les coordonn�es de destination au centre de cette case,
	 * et recalcule le chemin.
	 */
	public void setDestination(AiTile destination) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		arrived = false;
		tileDest = destination;
		xDest = tileDest.getPosX();
		yDest = tileDest.getPosY();
		path = astar.processShortestPath(ai.getCurrentTile(),destination);
	}

	/**
	 * modifie les coordonn�es de destination,
	 * met � jour automatiquement la case correspondante,
	 * et recalcule le chemin.
	 */
	public void setDestination(double x, double y) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		arrived = false;
		AiZone zone = ai.getZone();
		double normalized[] = zone.normalizePosition(x, y);
		xDest = normalized[0];
		yDest = normalized[1];
		tileDest = zone.getTile(xDest,yDest);
		path = astar.processShortestPath(ai.getCurrentTile(),tileDest);
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
	 * d�termine si le personnage est arriv� au centre de la case
	 * pass�e en param�tre
	 */
/*	private boolean hasArrived(AiTile tile) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		AiHero ownHero = ai.getOwnHero();
		boolean result = ai.getZone().hasSamePixelPosition(ownHero,tile);
		return result;
	}
*/
	/**
	 * d�termine si le personnage est arriv� aux coordonn�es de destination
	 */
	public boolean hasArrived() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		if(!arrived)
		{	// on teste si le personnage est � peu pr�s situ� � la position de destination 
			AiZone zone = ai.getZone();
			AiHero ownHero = ai.getOwnHero();
			double xCurrent = ownHero.getPosX();
			double yCurrent = ownHero.getPosY();
			arrived = zone.hasSamePixelPosition(xCurrent,yCurrent,xDest,yDest);
			// cas particulier : oscillation autour du point d'arriv�e
			if(!arrived)
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
	/** abscisse pr�c�dente */
	private double xPrev;
	/** ordonn�e pr�c�dente */
	private double yPrev;	
	
	/**
	 * met � jour la position pr�c�dente du personnage,
	 * exprim�e en pixels
	 */
	private void updatePrev() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		AiHero hero = ai.getOwnHero();
		xPrev = hero.getPosX();
		yPrev = hero.getPosY();		
	}

	/////////////////////////////////////////////////////////////////
	// PATH			/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le chemin � suivre */
	private AiPath path;
	
	/**
	 * v�rifie que le personnage est bien sur le chemin pr�-calcul�,
	 * en supprimant si besoin les cases inutiles.
	 * Si le personnage n'est plus sur le chemin, alors le chemin
	 * est vide apr�s l'ex�cution de cette m�thode.
	 */
	private void checkIsOnPath() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		AiTile currentTile = ai.getCurrentTile();
		while(!path.isEmpty() && path.getTile(0)!=currentTile)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			path.removeTile(0);
		}
	}
	
	/**
	 * d�termine si le personnage a d�pass� la premi�re case du chemin
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
//TODO pb ici : l'encadrement est toujours vrai puisque le niveau est circulaire !
			//p-� carr�ment laisser tomber la contrainte de passer par le centre des cases ?
			//et donner comme objectif une position en pixel (pvt �tre un centre)
*/
	
	/** 
	 * teste si le chemin est toujours valide, i.e. s'il
	 * est toujours s�r et si aucun obstacle n'est apparu
	 * depuis la derni�re it�ration
	 */
	private boolean checkPathValidity() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result = true;
		Iterator<AiTile> it = path.getTiles().iterator();
		while(it.hasNext() && result)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tile = it.next();
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
	 *(ou Direction.NONE si aucun d�placement n'est n�cessaire)
	 * */
	public Direction update() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		Direction result = Direction.NONE;
		if(!hasArrived())
		{	// on v�rifie que le joueur est toujours sur le chemin
			checkIsOnPath();
			// si le chemin est vide ou invalide, on le recalcule
			if(path.isEmpty() || !checkPathValidity())
				path = astar.processShortestPath(ai.getCurrentTile(),tileDest);
			// s'il reste deux cases au moins dans le chemin, on se dirige vers la suivante
			AiTile tile = null;
			if(path.getLength()>1)
				tile = path.getTile(1);
			// sinon, s'il ne reste qu'une seule case, on va au centre
			else if(path.getLength()>0)
				tile = path.getTile(0);
			// on d�termine la direction du prochain d�placement
			if(tile!=null)
				result = ai.getZone().getDirection(ai.getOwnHero(),tile);			
		}
		
		// mise � jour de la position pr�c�dente
		updatePrev();
		
		if(verbose)
		{	System.out.println(">>>>>>>>>> PATH MANAGER <<<<<<<<<<");
			System.out.println("path: "+path);
			System.out.println("direction: "+result);
		}
		return result;
	}
}
