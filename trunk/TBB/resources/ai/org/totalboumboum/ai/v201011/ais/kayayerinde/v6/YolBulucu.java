package org.totalboumboum.ai.v201011.ais.kayayerinde.v6;

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

import java.awt.Color;
import java.util.Iterator;

import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;



public class YolBulucu
{
	private boolean verbose = false;
	
	public YolBulucu(KayaYerinde onder) throws StopRequestException
	{
		onder.checkInterruption();
		init(onder);
	}

	public YolBulucu(KayaYerinde onder, double x, double y) throws StopRequestException, LimitReachedException
	{	onder.checkInterruption(); //APPEL OBLIGATOIRE
	
		init(onder);
		setDestination(x,y);
	}
	
	public YolBulucu(KayaYerinde onder, AiTile destination) throws StopRequestException, LimitReachedException
	{	onder.checkInterruption(); //APPEL OBLIGATOIRE
	
		init(onder);
		setDestination(destination);
	}
	
	private void init(KayaYerinde onder) throws StopRequestException
	{	onder.checkInterruption(); //APPEL OBLIGATOIRE
		
		this.onder = onder;
		zone = onder.getPercepts();
		costCalculator = new BasicCostCalculator();
		heuristicCalculator = new BasicHeuristicCalculator();
		astar =new Astar(onder, zone.getOwnHero(), costCalculator, heuristicCalculator);
		updatePrev();
	}
	
	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** l'IA concernée par ce gestionnonderre de chemin */
	private KayaYerinde onder;
	/** zone de jeu */
	private AiZone zone;
	
	/////////////////////////////////////////////////////////////////
	// DESTINATION	/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si le personnage est arrivé à destination */
	private boolean arrived;
	/** la case de destination sélectionn�e */
	private AiTile tileDest;
	/** l'abscisse de destination */
	private double xDest;
	/** l'ordonnée de destination */
	private double yDest;
	
	/**
	 * modifie la case de destination du personnage,
	 * place les coordonnées de destination au centre de cette case,
	 * et recalcule le chemin.
	 * @throws LimitReachedException 
	 */
	public void setDestination(AiTile destination) throws StopRequestException, LimitReachedException
	{	onder.checkInterruption(); //APPEL OBLIGATOIRE
		
		arrived = false;
		tileDest = destination;
		xDest = tileDest.getPosX();
		yDest = tileDest.getPosY();
		path = astar.processShortestPath(zone.getOwnHero().getTile(),destination);
	}

	/**
	 * modifie les coordonnées de destination,
	 * met à jour automatiquement la case correspondante,
	 * et recalcule le chemin.
	 * @throws LimitReachedException 
	 */
	public void setDestination(double x, double y) throws StopRequestException, LimitReachedException
	{	onder.checkInterruption(); //APPEL OBLIGATOIRE
		
		arrived = false;
		double normalized[] = zone.normalizePosition(x, y);
		xDest = normalized[0];
		yDest = normalized[1];
		tileDest = zone.getTile(xDest,yDest);
		path = astar.processShortestPath(zone.getOwnHero().getTile(),tileDest);
	}

	/*	public boolean hasArrived() throws StopRequestException
	{	onder.checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result = true;
		result = result && destination.getPosX()==onder.getCurrentX();
		result = result && destination.getPosY()==onder.getCurrentY();
		return result;
	}
*/	

	/**
	 * détermine si le personnage est arrivé au centre de la case
	 * passée en paramètre
	 */
/*	private boolean hasArrived(AiTile tile) throws StopRequestException
	{	onder.checkInterruption(); //APPEL OBLIGATOIRE
		
		AiHero ownHero = onder.getOwnHero();
		boolean result = onder.getZone().hasSamePixelPosition(ownHero,tile);
		return result;
	}
*/
	/**
	 * détermine si le personnage est arrivé aux coordonnées de destination
	 */
	public boolean hasArrived() throws StopRequestException
	{	onder.checkInterruption(); //APPEL OBLIGATOIRE
		
//		if(!arrived)
		{	// on teste si le personnage est à peu près situé à la position de destination 
			AiHero ownHero = zone.getOwnHero();
			double xCurrent = ownHero.getPosX();
			double yCurrent = ownHero.getPosY();
			arrived = zone.hasSamePixelPosition(xCurrent,yCurrent,xDest,yDest);
			// cas particulier : oscillation autour du point d'arrivée
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
	/** abscisse précédente */
	private double xPrev;
	/** ordonnée précédente */
	private double yPrev;	
	
	/**
	 * met à jour la position précédente du personnage,
	 * exprimée en pixels
	 */
	private void updatePrev() throws StopRequestException
	{	onder.checkInterruption(); //APPEL OBLIGATOIRE
	
		AiHero hero = zone.getOwnHero();
		xPrev = hero.getPosX();
		yPrev = hero.getPosY();		
	}

	/////////////////////////////////////////////////////////////////
	// PATH			/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le chemin à suivre */
	private AiPath path;
	
	/**
	 * vérifie que le personnage est bien sur le chemin pr�-calculé,
	 * en supprimant si besoin les cases inutiles.
	 * Si le personnage n'est plus sur le chemin, alors le chemin
	 * est vide après l'exécution de cette méthode.
	 */
	private void checkIsOnPath() throws StopRequestException
	{	onder.checkInterruption(); //APPEL OBLIGATOIRE
	
		AiTile currentTile = zone.getOwnHero().getTile();
		while(!path.isEmpty() && path.getTile(0)!=currentTile)
		{	onder.checkInterruption(); //APPEL OBLIGATOIRE
			
			path.removeTile(0);
		}
	}
	
	/**
	 * détermine si le personnage a d�passé la première case du chemin
	 * en direction de la seconde case
	 */
/*	private boolean hasCrossed(AiTile tile) throws StopRequestException
	{	onder.checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result = false;
		if(path.getLength()>1)
		{	AiHero hero = onder.getOwnHero();
			AiTile source = path.getTile(0);
			AiTile target = path.getTile(1);
			Direction direction = onder.getZone().getDirection(source,target);
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
	{	onder.checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result = true;
		Iterator<AiTile> it = path.getTiles().iterator();
		while(it.hasNext() && result)
		{	onder.checkInterruption(); //APPEL OBLIGATOIRE
			
			AiTile tile = it.next();
			result = tile.isCrossableBy(zone.getOwnHero());
//			if(!result)
//				System.out.println();
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// A STAR					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** classe implémentant l'algorithme A* */
	private org.totalboumboum.ai.v201011.adapter.path.astar.Astar astar;
	/** classe implémentant la fonction heuristique */
	private HeuristicCalculator heuristicCalculator;
	/** classe implémentant la fonction de coût */
	private CostCalculator costCalculator;

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/** 
	 * calcule la prochonderne direction pour aller vers la destination 
	 *(ou renvoie Direction.NONE si aucun déplacement n'est n�cessonderre)
	 * @throws LimitReachedException 
	 * */
	public Direction update() throws StopRequestException, LimitReachedException
	{	onder.checkInterruption(); //APPEL OBLIGATOIRE
		
		Direction result = Direction.NONE;
		if(!hasArrived())
		{	// on vérifie que le joueur est toujours sur le chemin
			checkIsOnPath();
			// si le chemin est vide ou invalide, on le recalcule
			if(path.isEmpty() || !checkPathValidity())
				path = astar.processShortestPath(zone.getOwnHero().getTile(),tileDest);
			if(checkPathValidity())
			{	// s'il reste deux cases au moins dans le chemin, on se dirige vers la suivante
				if(path.getLength()>1)
				{	AiTile tile = path.getTile(1);
					result = zone.getDirection(zone.getOwnHero(),tile);	
				}
				// sinon, s'il ne reste qu'une seule case, on va au centre
				else if(path.getLength()>0)
				{	AiHero ownHero = zone.getOwnHero();
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
	private void updateOutput() throws StopRequestException
	{	onder.checkInterruption(); //APPEL OBLIGATOIRE
		
		if(path!=null && !path.isEmpty())	
		{	AiOutput output = onder.getOutput();
			Color color = Color.BLACK;
			output.addPath(path, color);
		}
	}
}
