package org.totalboumboum.ai.v200910.ais.adatepeozbek.v1;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import org.totalboumboum.ai.v200910.adapter.communication.AiOutput;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * Classe charg�e d'impl�menter un d�placement,
 * en respectant un chemin donn�
 */
public class PathManager
{
	/** Interrupteur permettant d'afficher la trace du traitement */
	private boolean verbose = false;

	/**
	 * Cr�e un PathManager charg� d'amener le personnage � la position (x,y)
	 * exprim�e en pixels
	 */
	public PathManager(AdatepeOzbek ai, double x, double y) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		init(ai);
		setDestination(x,y);
	}
	
	/**
	 * Cr�e un PathManager charg� d'amener le personnage au centre de la case
	 * pass�e en param�tre
	 */
	public PathManager(AdatepeOzbek ai, AiTile destination) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		init(ai);
		setDestination(destination);
	}
	
	/**
	 * Initialise ce PathManager
	 */
	private void init(AdatepeOzbek ai) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		this.ai = ai;
		zone = ai.getZone();
		costCalculator = new BasicCostCalculator();
		heuristicCalculator = new BasicHeuristicCalculator();
		astar = new Astar(ai,ai.getOwnHero(),costCalculator,heuristicCalculator);
		updatePrev();
	}
	
	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** l'IA concern�e par ce gestionnaire de chemin */
	private AdatepeOzbek ai;
	/** zone de jeu */
	private AiZone zone;
	
	/////////////////////////////////////////////////////////////////
	// DESTINATION	/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si le personnage est arriv� � destination */
	private boolean arrived;
	/** la case de destination s�lectionn�e */
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
		
//		if(!arrived)
		{	// on teste si le personnage est � peu pr�s situ� � la position de destination 
			AiHero ownHero = ai.getOwnHero();
			double xCurrent = ownHero.getPosX();
			double yCurrent = ownHero.getPosY();
			arrived = zone.hasSamePixelPosition(xCurrent,yCurrent,xDest,yDest);
			// cas particulier : oscillation autour du point d'arriv�e
			if(!arrived && path.getLength()==1)
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
//			if(!result)
//				System.out.println();
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// A STAR					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** classe impl�mentant l'algorithme A* */
	private Astar astar;
	/** classe impl�mentant la fonction heuristique */
	private HeuristicCalculator heuristicCalculator;
	/** classe impl�mentant la fonction de co�t */
	private CostCalculator costCalculator;

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/** 
	 * calcule la prochaine direction pour aller vers la destination 
	 *(ou renvoie Direction.NONE si aucun d�placement n'est n�cessaire)
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
			if(checkPathValidity())
			{	// s'il reste deux cases au moins dans le chemin, on se dirige vers la suivante
				if(path.getLength()>1)
				{	AiTile tile = path.getTile(1);
					result = zone.getDirection(ai.getOwnHero(),tile);	
				}
				// sinon, s'il ne reste qu'une seule case, on va au centre
				else if(path.getLength()>0)
				{	AiHero ownHero = ai.getOwnHero();
					double x1 = ownHero.getPosX();
					double y1 = ownHero.getPosY();
					result = zone.getDirection(x1,y1,xDest,yDest);
				}
			}
		}
		
		// mise � jour de la position pr�c�dente
		updatePrev();
		// mise � jour de la sortie
		updateOutput();
		
		if(verbose)
		{	System.out.println(">>>>>>>>>> PATH MANAGER <<<<<<<<<<");
			System.out.println("path: "+path);
			System.out.println("direction: "+result);
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * met � jour la sortie graphique de l'IA en fonction du
	 * chemin courant
	 */
	private void updateOutput() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		if(path!=null && !path.isEmpty())	
		{	AiOutput output = ai.getOutput();
			Color color = Color.BLACK;
			output.addPath(path, color);
		}
	}
}
