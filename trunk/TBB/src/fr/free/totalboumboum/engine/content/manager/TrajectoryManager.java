package fr.free.totalboumboum.engine.content.manager;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.GestureConstants;
import fr.free.totalboumboum.engine.content.feature.action.AbstractAction;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.anime.AnimeGesture;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.feature.permission.ThirdPermission;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryDirection;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryPack;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryStep;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.CalculusTools;



public class TrajectoryManager
{	// divers
	/** sprite dirig� par ce TrajectoryManager */
	private Sprite sprite;
	/** ensemble de toutes les trajectoires disponibles */
	private TrajectoryPack trajectoryPack;
	/** trajectoire courante */
	private TrajectoryDirection currentTrajectory;
	/** indique que la trajectoire est termin�e (plus de d�placement) */
	private boolean isTerminated;
	/** temps normalis� �coul� de puis le d�but de la trajectoire */
	private double currentTime;
	/** dur�e totale de la trajectoire (soit l'originale, soit la forc�e) */
	private double totalDuration;
	/** coefficient de mofication du temps d� au d�lai impos� */
	private double forcedDurationCoeff;
	/** nom du geste courant */
	private String currentGestureName;
	/** modification de position X (utilis�e lors de la mise � jour de la position) */
	double shiftX;
	/** modification de position Y (utilis�e lors de la mise � jour de la position) */
	double shiftY;
	/** modification de position Z (utilis�e lors de la mise � jour de la position) */
	double shiftZ;

	// position
	/** position X relativement � la trajectoire courante */
	private double relativePosX;
	/** position Y relativement � la trajectoire courante */
	private double relativePosY;
	/** position Z relativement � la trajectoire courante */
	private double relativePosZ;
	/** position X absolue (en fait : soit par rapport au niveau, soit par rapport au boundToSprite) */
	private double currentPosX;
	/** position Y absolue (en fait : soit par rapport au niveau, soit par rapport au boundToSprite) */
	private double currentPosY;
	/** position Z absolue (en fait : soit par rapport au niveau, soit par rapport au boundToSprite) */
	private double currentPosZ;
	/** position X pr�c�dente (absolue) */
	private double previousPosX;
	/** position Y pr�c�dente (absolue) */
	private double previousPosY;
	/** position Z pr�c�dente (absolue) */
	private double previousPosZ;
	/** direction de d�placement courante */
	private Direction currentDirection;
	/** niveau d'interaction X avec les commandes */
	private double xMove;
	/** niveau d'interaction Y avec les commandes */
	private double yMove;
	
	// position impos�e
	/** deplacement X forc� total */
	private double forcedTotalXShift;
	/** deplacement Y forc� total */
	private double forcedTotalYShift;
	/** deplacement Z forc� total */
	private double forcedTotalZShift;
	/** temps imparti pour atteindre la position forc�e (tient compte du d�lai impos� � la trajectoire) */
	private double forcedPositionTime;
	/** position X exprim�e relativement au centre de la case courante */
	private double relativeForcedPosX;
	/** position Y exprim�e relativement au centre de la case courante */
	private double relativeForcedPosY;
	/** position Z exprim�e relativement au centre de la case courante */
	private double relativeForcedPosZ;
	
	// collisions
	private ArrayList<Sprite> intersectedSprites;
	private Sprite collidedSprites[];
	
	
	

	/** indique si la trajectoire a impliqu� (pour le moment) que le sprite ait d�coll� du sol */ 
	private boolean hasFlied;
	
/* ********************************
 * INIT
 * ********************************
 */	
	public TrajectoryManager(Sprite sprite)
	{	this.currentPosX = 0;
		this.currentPosY = 0;
		this.currentPosZ = 0;
		xMove = 0;
		yMove = 0;
		this.sprite = sprite;
		configuration = sprite.getConfiguration();
		forcedDurationCoeff = 1;
		totalDuration = 0;
		forcedTotalXShift = 0;
		forcedTotalYShift = 0;
		forcedTotalZShift = 0;
		forcedPositionTime = 0;
		relativeForcedPosX = 0;
		relativeForcedPosY = 0;
		relativeForcedPosZ = 0;
		currentGestureName = GestureConstants.NONE;
		currentDirection = Direction.NONE;
		shiftX = 0;
		shiftY = 0;
		shiftZ = 0;
		intersectedSprites = new ArrayList<Sprite>();
		collidedSprites = new Sprite[2];
	}

	public void setTrajectoryPack(TrajectoryPack trajectoryPack)
	{	this.trajectoryPack = trajectoryPack;	
	}
	
/* ********************************
 * PROCESS
 * ********************************
 */	
	/**
	 * change l'animation en cours d'affichage
	 */
	public void setGesture(String gesture, Direction spriteDirection, Direction controlDirection, boolean reinit, double forcedDuration)
	{	currentGestureName = gesture;
		hasFlied = getCurrentPosZ()>0;
		Direction previousDirection = currentDirection;
		currentDirection = controlDirection;
		setInteractiveMove(controlDirection);
		currentTrajectory = trajectoryPack.getTrajectoryDirection(gesture, spriteDirection);
		// reseting the gesture
		if(reinit)
		{	totalDuration = currentTrajectory.getTotalDuration();
			currentTime = 0;
			// isTerminated ?
			if(totalDuration == 0)
			{	isTerminated = true;
				forcedDurationCoeff = 1;
				sprite.processEvent(new EngineEvent(EngineEvent.TRAJECTORY_OVER)); //NOTE pertinent ?
			}
			else
			{	isTerminated = false;
				// forcedDuration
				if(forcedDuration<0)
				{	if(isBoundToSprite())
						forcedDuration = getBoundToSprite().getTrajectoryTotalDuration();
					else
						forcedDuration = 0;
				}	
				// forcedDurationCoeff
				if(forcedDuration == 0)
					forcedDurationCoeff = 1;
				else if(currentTrajectory.getProportional())
				{	forcedDurationCoeff = forcedDuration/totalDuration;
					totalDuration = forcedDuration;
				}
			}
			// relative position
			relativePosX = 0;
			relativePosY = 0;
			relativePosZ = 0;
			// forced position
			relativeForcedPosX = 0;
			relativeForcedPosY = 0;
			relativeForcedPosZ = 0;
			forcedPositionTime = currentTrajectory.getForcedPositionTime()*forcedDurationCoeff;
			processForcedShifts(currentPosX, currentPosY, currentPosZ);
		}
		// no reset : the same gesture (or an equivalent one) goes on, but in a different direction
		else
		{	// relative position is updated
			if(totalDuration!=0)
				updateRelativePos();
			// update forced shifts
			if(forcedPositionTime>0)
				correctForcedShifts();
			/* NOTE en cas de trajectoire repeat : 
			 * ne faut-il pas r�initialiser la position forc�e � chaque r�p�tition ?
			 */
		}
	}

	/**
	 * si on a d�pass� le forcedPositionDuration, il faut :  
	 * 		- calculer la position virtuelle � ce temps l� (ce qui implique de calculer le point de d�part en r�f�rence)
	 * 		- v�rifier si elle colle � la position forc�e
	 * 		- si elle ne colle pas : la corriger
	 * si on ne l'a pas encore d�pass� : � peu pr�s pareil
	 * 		- calculer la position initiale
	 * 		- calculer la position forc�e correspondante
	 * 		- corriger  
	 */
	private void correctForcedShifts()
	{	// on calcule la position initiale virtuelle pour la nouvelle trajectoire
		double initX = currentPosX - relativePosX;
		double initY = currentPosY - relativePosY;
		double initZ = currentPosZ - relativePosZ;
		// calcul des nouvelles positions forc�es
		processForcedShifts(initX,initY,initZ);
		// calcul des positions th�oriques � l'instant pr�sent
		Sprite boundToSprite = getBoundToSprite();
		// process the step
		Iterator<TrajectoryStep> i = currentTrajectory.getIterator();
		TrajectoryStep nextStep=null;
		double nextTime=0;
		double nextX=0, nextY=0, nextZ=0;
		do
		{	TrajectoryStep trajectoryStep = i.next();
			nextTime = nextTime + trajectoryStep.getDuration()*forcedDurationCoeff;
			nextStep = trajectoryStep;
			nextX = nextX+nextStep.getXShift();
			nextY = nextY+nextStep.getYShift();
			nextZ = nextZ+nextStep.getZShift(boundToSprite);
		}
		while(nextTime<currentTime);
		// process the theoretical position
		double previousX = nextX - nextStep.getXShift();
		double previousY = nextY - nextStep.getYShift();
		double previousZ = nextZ - nextStep.getZShift(boundToSprite);
		double previousTime = nextTime - nextStep.getDuration()*forcedDurationCoeff;
		double stepElapsedTime = forcedPositionTime - previousTime;
		double coef = stepElapsedTime / nextStep.getDuration();
		double theoRelX = previousX + coef*nextStep.getXShift(); 
		double theoRelY = previousY + coef*nextStep.getYShift();
		double theoRelZ = previousZ + coef*nextStep.getZShift(boundToSprite);
		double theoreticalX = theoRelX + initX;
		double theoreticalY = theoRelY + initY;
		double theoreticalZ = theoRelZ + initZ;
		// add the forced shift
		double forcedCoef = 1;
		if(currentTime<=forcedPositionTime)
			forcedCoef = currentTime / forcedPositionTime;
		if(currentTrajectory.isForceXPosition())
			theoreticalX = theoreticalX + forcedCoef*forcedTotalXShift;
		if(currentTrajectory.isForceYPosition())
			theoreticalY = theoreticalY + forcedCoef*forcedTotalYShift;
		if(currentTrajectory.isForceZPosition())
			theoreticalZ = theoreticalZ + forcedCoef*forcedTotalZShift;
		// process the gaps
		double gapX = theoreticalX - currentPosX;
		double gapY = theoreticalY - currentPosY;
		double gapZ = theoreticalZ - currentPosZ;
		/* TODO on pourrait �chelonner la correction, mais on choisit l'approche brutale
		 * quitte � affiner par la suite si n�cessaire
		 */
		currentPosX = currentPosX + gapX;
		currentPosY = currentPosY + gapY;
		currentPosZ = currentPosZ + gapZ;
	}
	
	/**
	 * 1) on calcule la position relative originale (XML) � t=forcedTime
	 * 2) on l'utilise pour calculer la position absolue originale � t=forcedTime
	 * 3) on relativise cette position par rapport � la r�f�rence courante : tile ou boundToSprite
	 * 4) on calcule l'�cart avec la forcedPosition
	 * cet �cart sera r�pandu sur le d�but de la trajectoire correspondant � forcedTime 
	 */
	private void processForcedShifts(double initX, double initY, double initZ)
	{	forcedTotalXShift = 0;
		forcedTotalYShift = 0;
		forcedTotalZShift = 0;
		if(forcedPositionTime>0)
		{	Sprite boundToSprite = getBoundToSprite();
			// process the step
			Iterator<TrajectoryStep> i = currentTrajectory.getIterator();
			TrajectoryStep nextStep=null;
			double nextTime=0;
			double nextX=0, nextY=0, nextZ=0;
			do
			{	TrajectoryStep trajectoryStep = i.next();
				nextTime = nextTime + trajectoryStep.getDuration()*forcedDurationCoeff;
				nextStep = trajectoryStep;
				nextX = nextX+nextStep.getXShift();
				nextY = nextY+nextStep.getYShift();
				nextZ = nextZ+nextStep.getZShift(boundToSprite);
			}
			while(nextTime<forcedPositionTime);
			// process the original position
			double previousX = nextX - nextStep.getXShift();
			double previousY = nextY - nextStep.getYShift();
			double previousZ = nextZ - nextStep.getZShift(boundToSprite);
			double previousTime = nextTime - nextStep.getDuration()*forcedDurationCoeff;
			double stepElapsedTime = forcedPositionTime - previousTime;
			double coef = stepElapsedTime / nextStep.getDuration();
			double originalX = previousX + coef*nextStep.getXShift(); 
			double originalY = previousY + coef*nextStep.getYShift();
			double originalZ = previousZ + coef*nextStep.getZShift(boundToSprite);
			// process the relative position
			double relativeX,relativeY,relativeZ;
			if(isBoundToSprite())
			{	// relative to boundToSprite ?
				relativeX = initX + originalX;
				relativeY = initY + originalY;
				relativeZ = initZ + originalZ;
			}
			else
			{	// or relative to a tile ?
				Tile tempTile = sprite.getLevel().getTile(initX, initY);
				double futureX = initX + originalX;
				double centerX = tempTile.getPosX();
				relativeX = futureX - centerX;
				double futureY = initY + originalY;
				double centerY = tempTile.getPosY();
				relativeY = futureY - centerY;
				double futureZ = initZ + originalZ;
				double centerZ = 0;
				relativeZ = futureZ - centerZ;
			}
			// process the gap with the forced position
			if(currentTrajectory.isForceXPosition())
				forcedTotalXShift = currentTrajectory.getForcedXPosition() - relativeX;
			if(currentTrajectory.isForceYPosition())
				forcedTotalYShift = currentTrajectory.getForcedYPosition() - relativeY;
			if(currentTrajectory.isForceZPosition())
				forcedTotalZShift = currentTrajectory.getForcedZPosition() - relativeZ;
		}
	}
	
	/**
	 * modifie la position absolue courante en fonction du boundToSprite.
	 * Cette m�thode doit imp�rativement �tre appel�e juste avant un changement de gesture.
	 * @param newSprite
	 */
	public void setBoundToSprite(Sprite newSprite)
	{	Sprite oldSprite = sprite.getBoundToSprite();
		if(oldSprite==null)
		{	// avant:non - maintenant:rien
			if(newSprite==null)
			{	// rien en fait
			}
			// avant:non - maintenant:oui
			else
			{	currentPosX = 0;
				currentPosY = 0;
				currentPosZ = 0;
			}
		}
		else
		{	// avant:oui - maintenant:non
			if(newSprite==null)
			{	currentPosX = currentPosX+oldSprite.getCurrentPosX();
				currentPosY = currentPosY+oldSprite.getCurrentPosY();
				currentPosZ = currentPosZ+oldSprite.getCurrentPosZ();
			}
			// avant:oui - maintenant:oui (mais pas le m�me)
			else if(newSprite!=oldSprite)
			{	currentPosX = 0;
				currentPosY = 0;
				currentPosZ = 0;
			}
		}
	}
	
	/**
	 * Modifie le niveau d'interaction des controles du sprite
	 * @param controlDirection
	 */
	private void setInteractiveMove(Direction controlDirection)
	{	int p[] = controlDirection.getIntFromDirection();
		xMove = p[0];
		yMove = p[1];
	}
	
	/**
	 * m�thode appel�e � chaque it�ration
	 * met � jour le d�placement et la position relative.
	 * attention : dans le cas d'une r�p�tition, le dernier point
	 * de la trajectoire sert de premier point � la r�p�tition suivante.
	 * donc si on veut un cycle parfait, il faut rajouter un dernier point ramenant ou premier 
	 */
	public void update()
	{	
/*	
if(sprite instanceof Bomb)
if(previousPosX != currentPosX || previousPosY != currentPosY || previousPosZ != currentPosZ)
{	System.out.println("temps:"+currentTime+" ("+currentGestureName+")");
	System.out.println("tile:"+sprite.getTile().getPosX()+";"+sprite.getTile().getPosY());
	System.out.println("bombe:"+currentPosX+";"+currentPosY+";"+currentPosZ);
	System.out.println();
}
*/
		// keep the previous position
		previousPosX = currentPosX;
		previousPosY = currentPosY;
		previousPosZ = currentPosZ;
		// process the interactive (control) shift
		long milliPeriod = getConfiguration().getMilliPeriod();
		double speedCoeff = sprite.getSpeedCoeff();
		shiftX = xMove*currentTrajectory.getXInteraction()*milliPeriod*speedCoeff/1000;
		shiftY = yMove*currentTrajectory.getYInteraction()*milliPeriod*speedCoeff/1000;
		shiftZ = 0;
		// is the trajectory terminated ?
		Iterator<TrajectoryStep> i = currentTrajectory.getIterator();
		if (!isTerminated && i.hasNext())
		{	// update current time
			updateTime();
			if(!isTerminated)
			{	// update the position relatively to the trajectory
				updateRelativePos();				
				// manage the forced position
				updateRelativeForcedPos();
			}
		}
		
		// applying the shifts
		currentPosX = currentPosX + shiftX;
		currentPosY = currentPosY + shiftY;
		currentPosZ = currentPosZ + shiftZ;
		
		// collisions management
		if(!isBoundToSprite())
		{	
			// calcul de la direction de d�placement effective du sprite par rapport � la position pr�c�dente
			double dx = currentPosX-previousPosX;
			double dy = currentPosY-previousPosY;
			Direction moveDir = Direction.getCompositeFromDouble(dx, dy);
if(moveDir!=Direction.NONE)			
	{	
	

			
			// r�cup�ration des cases voisines, y compris la case courante
			Tile tile = sprite.getTile();
			ArrayList<Tile> neighbourTiles = sprite.getLevel().getNeighbourTiles(tile.getLine(),tile.getCol());
			neighbourTiles.add(tile);
			// r�cup�ration des sprites situ�s dans ces cases
			ArrayList<Sprite> neighbourSprites = getNeighbourSprites(neighbourTiles);
			// filtrage des sprites : distance critique, situ� sur le chemin et refusant la p�n�tration
			ArrayList<Sprite> obstacleSprites = getObstacleSprites(neighbourSprites);
			// modification de la position de ce sprite
			Sprite newCollidedSprites[] = new Sprite[2];
			if(obstacleSprites.size()>0)
			{	if(moveDir.isPrimary())
					testCollisionsSimple(obstacleSprites,moveDir,newCollidedSprites,true);
				else if(moveDir.isComposite())
					testCollisionsComposite(obstacleSprites,moveDir,newCollidedSprites);
			}
			updateCollidedSprites(newCollidedSprites);
			// on d�termine quels sprites sont intersected
			ArrayList<Sprite> newIntersectedSprites = getIntersectedSprites(neighbourSprites);
			// mise � jour des sprites intersected
			updateIntersectedSprites(newIntersectedSprites);
	
			
	}
			
			
			
			
			
			
			
/*			
			
			// sortie de tile ?
			Direction moveXDir = Direction.getHorizontalFromDouble(currentPosX-previousPosX);
			Direction moveYDir = Direction.getVerticalFromDouble(currentPosY-previousPosY);
			Tile tile = sprite.getTile();
			// horizontal
			{	double dx = Math.round(currentPosX-tile.getPosX());
				Direction dir = Direction.getHorizontalFromDouble(dx);
				if(dir!=Direction.NONE)
				{	Tile neighbour = tile.getNeighbour(dir);
					Request req;
					if(currentPosZ==0)
						req = new Request(Request.RQST_LET_WALK_IN,dir);
					else
						req = new Request(Request.RQST_LET_FLY_IN,dir);
					req.setActor(sprite);
					boolean autorization = neighbour.answerAndRequest(req);
					if(!autorization && moveXDir==dir)
					{	//currentPosX = tile.getPosX();
						currentPosX = previousPosX; //NOTE cf la note plus bas
						sprite.putEvent(new Event(Event.ENV_BLOCKED_PATH));
					}
				}
			}
			// vertical
			{	double dy = Math.round(currentPosY-tile.getPosY());
				Direction dir = Direction.getVerticalFromDouble(dy);
				if(dir!=Direction.NONE)
				{	Tile neighbour = tile.getNeighbour(dir);
					Request req;
					if(currentPosZ==0)
						req = new Request(Request.RQST_LET_WALK_IN,dir);
					else
						req = new Request(Request.RQST_LET_FLY_IN,dir);
					req.setActor(sprite);
					boolean autorization = neighbour.answerAndRequest(req);
					if(!autorization && moveYDir==dir)
					{	//currentPosY = tile.getPosY();
						// NOTE pour �viter les pb avec les bombes : 
						// quand on sort et qu'on revient en arri�re, �a t�l�porte � la bonne position... 
						// on suppose maintenant qu'on ne fera pas de d�placement trop important, 
						// sinon le bonhomme se retrouvera bloqu� loin de l'obstacle, en fait. 
						// et faudrait recalculer sa position
						//
						currentPosY = previousPosY;
						sprite.putEvent(new Event(Event.ENV_BLOCKED_PATH));
					}
				}
			}
			// diagonal
			{	double dx = Math.round(currentPosX-tile.getPosX());
				double dy = Math.round(currentPosY-tile.getPosY());
				Direction dir = Direction.getCompositeFromDouble(dx, dy);
				if(dir.isComposite())
				{	Tile neighbour = tile.getNeighbour(dir);
					Request req;
					if(currentPosZ==0)
						req = new Request(Request.RQST_LET_WALK_IN,dir);
					else
						req = new Request(Request.RQST_LET_FLY_IN,dir);
					req.setActor(sprite);
					boolean autorization = neighbour.answerAndRequest(req);
					if(!autorization && (moveXDir==dir.getStrongPrimary() || moveYDir==dir.getWeakPrimary()))
					{	int intDir[] = dir.getIntFromDirection();
						// calcul de la droite d�limitant la zone interdite
						double cdTest = 0; 	
						if(intDir[0]==intDir[1])
							cdTest = -1;
						else
							cdTest = +1;
						double oaTest = tile.getPosY() - cdTest*(tile.getPosX()+intDir[0]*sprite.getTile().getDimension()/2);
						// est-on du mauvais c�t� ?
						if((tile.getPosY()>(cdTest*tile.getPosX()+oaTest))
								!= (currentPosY>(cdTest*currentPosX+oaTest)))
						{	// calcul de la direction principale
							Direction mainDir;
							{	double dx2 = currentPosX-previousPosX;
								double dy2 = currentPosY-previousPosY;
								if(Math.abs(dx2)>Math.abs(dy2))
									mainDir = Direction.getHorizontalFromDouble(dx2);
								else if(Math.abs(dx2)<Math.abs(dy2))
									mainDir = Direction.getVerticalFromDouble(dy2);
								else
								{	if(Math.abs(dx)>Math.abs(dy))
										mainDir = Direction.getHorizontalFromDouble(dx);
									else //if(Math.abs(dx)<Math.abs(dy))
										mainDir = Direction.getVerticalFromDouble(dy);
								}
							}
							// on limite la direction de plus petit d�placement
							if(mainDir.isHorizontal())
								currentPosY = cdTest*currentPosX + oaTest;
							else //if(mainDir.isVertical())
								currentPosX = (currentPosY - oaTest)/cdTest;
						}
						sprite.putEvent(new Event(Event.ENV_BLOCKED_PATH));
					}					
						
				}
			}
*/
		}
		
		
		// normalizing an absolute position (if not bound)
			//NOTE � placer avant, non ?
		if(!isBoundToSprite())
		{	double temp[] = sprite.getLevel().normalizePosition(currentPosX, currentPosY);
			currentPosX = temp[0];
			currentPosY = temp[1];
		}
		
		// updating the flight flag
		if(!hasFlied && currentPosZ>0)
			hasFlied = true;
		
		// normalizing height at the end of an air move
		if(isTerminated)
		{	/* FIXME grosssssse bidouille:
			 * avec les calculs il arrive que le z ne revienne pas � z�ro apr�s un saut.
			 */
			if(currentPosZ>-1 && currentPosZ<1) //NOTE utiliser CalculusTools
				currentPosZ = 0;
			//
			if(hasFlied && currentPosZ==0)
			{	EngineEvent e = new EngineEvent(EngineEvent.TOUCH_GROUND,sprite,null,getActualDirection());
				sprite.getTile().spreadEvent(e);
			}
		}
	}
	
	private void updateTime()
	{	double milliPeriod = getConfiguration().getMilliPeriod();
		double delta = milliPeriod*forcedDurationCoeff*sprite.getSpeedCoeff();	
		currentTime = currentTime + delta;
		if(currentTime > totalDuration)
		{	// looping the trajectory
			if (currentTrajectory.getRepeat())
			{	while(currentTime>totalDuration)
					currentTime = currentTime - totalDuration;
				relativePosX = currentTrajectory.getTotalXShift()-relativePosX;
				relativePosY = currentTrajectory.getTotalYShift()-relativePosY;
				relativePosZ = currentTrajectory.getTotalZShift(getBoundToSprite())-relativePosZ;
			}
			// or terminating the trajectory
			else
			{	currentTime = totalDuration;
				double currentX = currentTrajectory.getTotalXShift(); 
				double currentY = currentTrajectory.getTotalYShift();
				double currentZ = currentTrajectory.getTotalZShift(getBoundToSprite());
				shiftX = shiftX + (currentX - relativePosX);
				shiftY = shiftY + (currentY - relativePosY);
				shiftZ = shiftZ + (currentZ - relativePosZ);
				relativePosX = currentX;
				relativePosY = currentY;
				relativePosZ = currentZ;
				isTerminated = true;
				sprite.processEvent(new EngineEvent(EngineEvent.TRAJECTORY_OVER));
			}
		}	
	}
	
	private void updateRelativePos()
	{	Iterator<TrajectoryStep> i = currentTrajectory.getIterator();
		TrajectoryStep nextStep=null;
		double nextTime=0;
		double nextX=0, nextY=0, nextZ=0;
		do
		{	TrajectoryStep trajectoryStep = i.next();
			nextTime = nextTime + trajectoryStep.getDuration()*forcedDurationCoeff;
			nextStep = trajectoryStep;
			nextX = nextX+nextStep.getXShift();
			nextY = nextY+nextStep.getYShift();				
			nextZ = nextZ+nextStep.getZShift(getBoundToSprite());				
		}
		while(nextTime<currentTime && i.hasNext());
		// round
		if(nextTime<currentTime)
			nextTime = totalDuration;
		
		// process the intermediate position (between two steps)
		double previousX = nextX - nextStep.getXShift();
		double previousY = nextY - nextStep.getYShift();
		double previousZ = nextZ - nextStep.getZShift(getBoundToSprite());
		double previousTime = nextTime - nextStep.getDuration()*forcedDurationCoeff;
		double stepElapsedTime = currentTime - previousTime;
		double coef = stepElapsedTime / (nextStep.getDuration()*forcedDurationCoeff);
		double currentX = previousX + coef*nextStep.getXShift(); 
		double currentY = previousY + coef*nextStep.getYShift();
		double currentZ = previousZ + coef*nextStep.getZShift(getBoundToSprite());
		shiftX = shiftX + (currentX - relativePosX);
		shiftY = shiftY + (currentY - relativePosY);
		shiftZ = shiftZ + (currentZ - relativePosZ);
		relativePosX = currentX;
		relativePosY = currentY;
		relativePosZ = currentZ;		
	}
	
	private void updateRelativeForcedPos()
	{	if(currentTime<=forcedPositionTime)
		{	double forcedCoef = currentTime / forcedPositionTime;
			if(currentTrajectory.isForceXPosition())
			{	double forcedCurrentX = forcedCoef*forcedTotalXShift; 
				shiftX = shiftX + (forcedCurrentX - relativeForcedPosX);
				relativeForcedPosX = forcedCurrentX;
			}
			if(currentTrajectory.isForceYPosition())
			{	double forcedCurrentY = forcedCoef*forcedTotalYShift; 
				shiftY = shiftY + (forcedCurrentY - relativeForcedPosY);
				relativeForcedPosY = forcedCurrentY;
			}
			if(currentTrajectory.isForceZPosition())
			{	double forcedCurrentZ = forcedCoef*forcedTotalZShift; 
				shiftZ = shiftZ + (forcedCurrentZ - relativeForcedPosZ);
				relativeForcedPosZ = forcedCurrentZ;
			}
		}		
	}
	
/* ********************************
 * TIME
 * ********************************
 */	

	/**
	 * renvoie la dur�e totale pr�vue pour la trajectoire.
	 * @return
	 */
	public double getTotalDuration()
	{	return totalDuration;
	}
	
/* ********************************
 * POSITION
 * ********************************
 */	
	public double getCurrentPosX()
	{	double result = currentPosX;
		if(isBoundToSprite())
			result = result + getBoundToSprite().getCurrentPosX();
		return result;
	}
	public void setCurrentPosX(double posX)
	{	currentPosX = posX;
	}
	
	public double getCurrentPosY()
	{	double result = currentPosY;
		if(isBoundToSprite())
			result = result + getBoundToSprite().getCurrentPosY();
		return result;
	}
	public void setCurrentPosY(double posY)
	{	currentPosY = posY;
	}
	
	public double getCurrentPosZ()
	{	double result = currentPosZ;
		if(isBoundToSprite())
			result = result + getBoundToSprite().getCurrentPosZ();
		return result;
	}
	public void setCurrentPosZ(double posZ)
	{	currentPosZ = posZ;
	}
	
/* ********************************
 * MISC
 * ********************************
 */	
	public Direction getActualDirection()
	{	Direction result = currentDirection;
		if(currentDirection==Direction.NONE)
		{	double dx = currentPosX-previousPosX;
			double dy = currentPosY-previousPosY;
			result = Direction.getCompositeFromDouble(dx, dy);
		}
		return result;
	}

    private Configuration configuration;
	public Configuration getConfiguration()
	{	return configuration;	
	}
	
/* ********************************
 * BOUND
 * ********************************
 */	
	private Sprite getBoundToSprite()
	{	return sprite.getBoundToSprite();
	}
	
	private boolean isBoundToSprite()
	{	return sprite.isBoundToSprite();
	}
	
	
	
	
	
	
	
	
	
	/**
	 * dresse la liste de tous les sprites pr�sents dans les tuiles pass�es en param�tres
	 */
	public ArrayList<Sprite> getNeighbourSprites(ArrayList<Tile> neighbourTiles)
	{	ArrayList<Sprite> result = new ArrayList<Sprite>();
		Iterator<Tile> iter = neighbourTiles.iterator();
		while(iter.hasNext())
		{	Tile temp = iter.next();
			result.addAll(temp.getSprites());
		}
		return result;
	}
	/**
	 * parmi les sprites de la liste pass�e en param�tre, 
	 * ne garde que les sprites � la fois : 
	 * 		- critiquement proches
	 * 		- sur le chemin de d�placement
	 * 		- qui constituent des obstacles (pas traversables)
	 */
	private ArrayList<Sprite>  getObstacleSprites(ArrayList<Sprite> sprites)
	{	ArrayList<Sprite> result = new ArrayList<Sprite>();
		Iterator<Sprite> iter = sprites.iterator();
//if(previousPosX>=514f && previousPosX<515f && previousPosY>=384f && previousPosY<385f)
//System.out.println();
		while(iter.hasNext())
		{	Sprite tempSprite = iter.next();
			if(tempSprite!=sprite 
					&& isClose(tempSprite, true) //isInCloseArea(tempSprite) 
					&& isOnTheWay(tempSprite)
					&& isObstacle(tempSprite))
			{	result.add(tempSprite);
			}
		}
		// on trie la liste pass�e en param�tre (ou ce qu'il en reste) par distance
		Collections.sort(result,new Comparator<Sprite>()
		{	@Override
			public int compare(Sprite arg0, Sprite arg1)
			{	int resultat;
				double dist0 = computeDistance(arg0);
				double dist1 = computeDistance(arg1);
				if(dist0>dist1)
					resultat = +1;
				else if(dist0<dist1)
					resultat = -1;
				else
					resultat = 0;
				return resultat;
			}
		});
		return result;
	}
	
	private ArrayList<Sprite> getIntersectedSprites(ArrayList<Sprite> sprites)
	{	ArrayList<Sprite> result = new ArrayList<Sprite>();
		Iterator<Sprite> iter = sprites.iterator();
		while(iter.hasNext())
		{	Sprite tempSprite = iter.next();
			if(tempSprite!=sprite && isClose(tempSprite,true))
				result.add(tempSprite);
		}
		return result;
	}
	
	/**
	 * d�termine si le sprite pass� en param�tre est critiquement proche
	 * strict : true=in�galit� stricte, false=in�galit� large (loose)
	 */
	private boolean isClose(Sprite tempSprite, boolean strict)
	{	boolean result;
		// calcul des distances
		double distX = Math.abs(tempSprite.getCurrentPosX()-currentPosX);
		double distY = Math.abs(tempSprite.getCurrentPosY()-currentPosY);
		// calcul du r�sultat
		result = isClose(distX,strict) && isClose(distY,strict);
		return result;
	}
	private boolean isClose(double distance, boolean strict)
	{	boolean result;
		if(strict)
			result = CalculusTools.isRelativelySmallerThan(distance,getLoop().getScaledTileDimension(),getLoop());
		else
			result = CalculusTools.isRelativelySmallerOrEqualThan(distance,getLoop().getScaledTileDimension(),getLoop());
		return result;
	}
	private double computeDistance(Sprite tempSprite)
	{	double result;
		double distX = Math.abs(tempSprite.getCurrentPosX()-currentPosX);
		double distY = Math.abs(tempSprite.getCurrentPosY()-currentPosY);
		result = distX+distY;
		return result;
	}
	/**
	 * d�termine si le sprite pass� en param�tre est proche d'un des points du segment
	 * d�limit� par la position courante et l'ancienne position de ce sprite.
	 * 
	 * @param tempSprite	le sprite � tester
	 * @return
	 */
	private boolean isInCloseArea(Sprite tempSprite)
	{	boolean result;
		double interX=0;
		double interY=0;
		// d�placement horizontal
		if(currentPosY==previousPosY)
		{	interX = tempSprite.getCurrentPosX();
			interY = currentPosY;
		}
		// d�placement vertical
		else if(currentPosX==previousPosX)
		{	interX = currentPosX;
			interY = tempSprite.getCurrentPosY();
		}
		else
		// cas g�n�ral
		{	// droite entre currentPos et previousPos
			double a = (currentPosY-previousPosY)/(currentPosX-previousPosX);
			double b = currentPosY - a*currentPosX;
			// perpendiculaire passant par tempSprite
			double ap = -1/a;
			double bp = tempSprite.getCurrentPosY() - ap*tempSprite.getCurrentPosX();
			// point d'intersection
			interX = (bp-b)/(a-ap);
			interY = a*interX + b;
		}
		// distances entre tempSprite et le point d'intersection
		double distX = Math.abs(tempSprite.getCurrentPosX()-interX);
		double distY = Math.abs(tempSprite.getCurrentPosY()-interY);
		//
		result = isClose(distX,true) && isClose(distY,true);
		return result;
	}
		
	/**
	 * d�termine si le sprite pass� en param�tre est sur le chemin de d�placement
	 */
	private boolean isOnTheWay(Sprite tempSprite)
	{	boolean result;
		// sprite de r�f�rence
		Direction hMoveDir = getActualDirection().getHorizontalPrimary();
		Direction vMoveDir = getActualDirection().getVerticalPrimary();
		// sprite test�
		double tdx = tempSprite.getCurrentPosX()-previousPosX;
		double tdy = tempSprite.getCurrentPosY()-previousPosY;
		Direction hObstacleDir = Direction.getHorizontalFromDouble(tdx);
		Direction vObstacleDir = Direction.getVerticalFromDouble(tdy);
		// calcul du r�sultat
		result = (vObstacleDir==vMoveDir && vObstacleDir!=Direction.NONE)
			|| (hObstacleDir==hMoveDir && hObstacleDir!=Direction.NONE);
		return result;
	}
	/**
	 * d�termine si le sprite pass� en param�tre refuse ou accepte de laisser passer ce sprite
	 */
	private boolean isObstacle(Sprite tempSprite)
	{	boolean result;
/*	
		SpecificAction specificAction;
		if(isOnGround())
			specificAction = new SpecificAction(AbstractAction.MOVELOW,getConfiguration());
		else
			specificAction = new SpecificAction(AbstractAction.MOVEHIGH,getConfiguration());
		specificAction.setActor(sprite);
		specificAction.setDirection(currentDirection);
		AbstractAbility ability = sprite.computeAbility(specificAction);
		result = !tempSprite.answerRequest(req);
 */
	
SpecificAction specificAction;
if(isOnGround())
	specificAction = new SpecificAction(AbstractAction.MOVELOW,sprite,null,currentDirection);
else
	specificAction = new SpecificAction(AbstractAction.MOVEHIGH,sprite,null,currentDirection);
ThirdPermission permission = tempSprite.getThirdPermission(specificAction);
result = permission==null;

/* FIXME grosse bidouille pour �viter qu'une bombe qui vient d'�tre pos�e ne bloque un joueur.
 * � rectifier !
 */ 
if(sprite instanceof Hero && tempSprite instanceof Bomb && sprite.getTile()==tempSprite.getTile())
	result=false;


		return result;
	}
	/**
	 * en supposant que ce sprite et le param�tre sont en contact (pas de recouvrement toutefois)
	 * cette fonction d�termine si leurs d�placements �ventuels vont les faire se rencontrer (et vraisemblablement provoquer une collision)
	 */
	private boolean areConverging(Sprite tempSprite)
	{	boolean result;
		boolean test1=false,test2=false;
		boolean res1=true,res2=true;
		// position relative du param�tre
		double tdx = tempSprite.getCurrentPosX() - previousPosX;
		double tdy = tempSprite.getCurrentPosY() - previousPosY;
		// test horizontal
		if(CalculusTools.isRelativelyEqualTo(Math.abs(tdx),getLoop().getScaledTileDimension(),getLoop()))
		{	// d�placement du sprite de r�f�rence
			Direction spriteHDir = getActualDirection().getHorizontalPrimary();
			// d�placement du param�tre
			Direction tempHDir = tempSprite.getActualDirection().getHorizontalPrimary();
			// direction du param�tre relativement au sprite
			Direction hPos = Direction.getHorizontalFromDouble(tdx);
			// result
			res1 = spriteHDir==hPos || tempHDir==hPos.getOpposite();
			test1 = true;
		}
		if(CalculusTools.isRelativelyEqualTo(Math.abs(tdy),getLoop().getScaledTileDimension(),getLoop()))
		{	// d�placement du sprite de r�f�rence
			Direction spriteVDir = getActualDirection().getVerticalPrimary();
			// d�placement du param�tre
			Direction tempVDir = tempSprite.getActualDirection().getVerticalPrimary();
			// direction du param�tre relativement au sprite
			Direction vPos = Direction.getVerticalFromDouble(tdy);
			// result
			res2 = spriteVDir==vPos || tempVDir==vPos.getOpposite();
			test2 = true;
		}
		//
		result = (test1 || test2) && (res1 && res2);
		return result;
	}	
	
	/**
	 * passe tous les sprites en revue, en modificant la direction de d�placement
	 * en fonction des diff�rentes positions.
	 * la nouvelle position courante est ensuite �ventuellement modifi�e en fonction des
	 * blocages d�tect�s.
	 * la fonction renvoie le(s) obstacle(s) rencontr�(s).
	 */
	private void testCollisionsComposite(ArrayList<Sprite> sprites, Direction moveDir, Sprite newCollidedSprites[])
	{	
		
/*
 * TODO very ugly workaround 
 * problem description : 
 * 		in some fancy graphical definitions and/or speeds, 
 * 		player gets blocked at crossroads when moving in a composite direction
 * cause :
 * 		the sprite doesn't pass by central the location of the tile
 * workaround :
 * 		if this center is on the sprite trajectory, its location is rounded to this center 		
 */
double tileX = sprite.getTile().getPosX();
double tileY = sprite.getTile().getPosY();
if((moveDir.getHorizontalPrimary()==Direction.RIGHT && tileX>previousPosX && tileX<currentPosX)
		|| (moveDir.getHorizontalPrimary()==Direction.LEFT && tileX<previousPosX && tileX>currentPosX))
	currentPosX = tileX;
if((moveDir.getVerticalPrimary()==Direction.DOWN && tileY>previousPosY && tileY<currentPosY)
		|| (moveDir.getVerticalPrimary()==Direction.UP && tileY<previousPosY && tileY>currentPosY))
	currentPosY = tileY;
		
		
		boolean debug = true;
		Direction hDir = moveDir.getHorizontalPrimary();
		Direction vDir = moveDir.getVerticalPrimary();
		double newPosX = previousPosX;
		double newPosY = previousPosY;
		if(debug)
		{	System.out.println("previousPos:"+previousPosX+";"+previousPosY);		
			System.out.println("currentPos:"+currentPosX+";"+currentPosY);		
			System.out.println("nbre sprites:"+sprites.size());		
			System.out.println("moveDir:"+moveDir);
		}
		Iterator<Sprite> iter = sprites.iterator();
		// tant que tous les d�placements n'ont pas �t� interdits, on passe les obstacles en revue
		while(iter.hasNext() && (hDir!=Direction.NONE || vDir!=Direction.NONE))
		{	Sprite temp = iter.next();
			// on calcule les distances entre l'obstacle et la position pr�c�dente du sprite
			double distXP = Math.abs(temp.getCurrentPosX()-previousPosX);
			double distYP = Math.abs(temp.getCurrentPosY()-previousPosY);
			// on calcule les distances entre l'obstacle et la position potentielle du sprite
			double distXC = Math.abs(temp.getCurrentPosX()-currentPosX);
			double distYC = Math.abs(temp.getCurrentPosY()-currentPosY);
			if(debug)
			{	System.out.println("obstacle:"+temp.getCurrentPosX()+","+temp.getCurrentPosY());			
				System.out.println("dist obstacle/previous:"+distXP+","+distYP);			
				System.out.println("dist obstacle/potential:"+distXC+","+distYC);			
			}
			// pour chacune des deux directions : le fait de ne pas la d�sactiver provoque-t-il la collision ?
			// pour chacune des deux directions : le fait de ne pas la d�sactiver fait-il diminuer la distance ?
//if(vDir!=Direction.NONE && isClose(distXP,true) && isClose(distYC,true) && distYC<distYP)
			if(vDir!=Direction.NONE && isClose(distXP,true) && distYC<distYP)
			{	// si oui on d�sactive la direction
				int dir = vDir.getIntFromDirection()[1];
				vDir = Direction.NONE;
				// on calcule le d�placement maximal
				double tempPosY = temp.getCurrentPosY()-dir*getLoop().getScaledTileDimension();
				if(debug)
				{	System.out.println("tempPosY:"+tempPosY);			
				}
//				if(isClose(distXP,true) && isClose(distYP,true))
//					tempPosY = previousPosY;
				if(debug)
				{	System.out.println("tempPosY:"+tempPosY);			
				}
				// on met � jour la nouvelle position seulement si cet obstacle est plus contraignant 
//				if(Math.abs(tempPosY-currentPosY)<Math.abs(newPosY-currentPosY))
				{	newPosY = tempPosY;
					newCollidedSprites[1] = temp;
				}
			}
			//NOTE faudrait p-� un else ici ? ou pas, pr le cas o� on est dans un obstacle
			if(hDir!=Direction.NONE && isClose(distYP,true) && distXC<distXP)
			{	int dir = hDir.getIntFromDirection()[0];
				hDir = Direction.NONE;
				double tempPosX = temp.getCurrentPosX()-dir*getLoop().getScaledTileDimension();
				if(debug)
				{	System.out.println("tempPosX:"+tempPosX);			
				}
//				if(isClose(distXP,true) && isClose(distYP,true))
//					tempPosX = previousPosX;
				if(debug)
				{	System.out.println("tempPosX:"+tempPosX);			
				}
//				if(Math.abs(tempPosX-currentPosY)<Math.abs(newPosY-currentPosY))
				{	newPosX = tempPosX;
					newCollidedSprites[0] = temp;
				}
			}
		}
		if(hDir == Direction.NONE)
			currentPosX = newPosX;
		if(vDir == Direction.NONE)
			currentPosY = newPosY;
		if(debug)
		{	System.out.println("currentPos:"+currentPosX+","+currentPosY);			
			System.out.println();
		}
	}

	private void testCollisionsSimple(ArrayList<Sprite> sprites, Direction moveDir, Sprite[] newCollidedSprites, boolean help)
	{	boolean debug = false;
		Direction oldDir = moveDir.getHorizontalPrimary();
		if(oldDir == Direction.NONE)
			oldDir = moveDir.getVerticalPrimary();
		Direction newDir = null; 
		boolean goOn = true;
		double newPosX=previousPosX, newPosY=previousPosY;
		if(debug)
		{	System.out.println("previousPos:"+previousPosX+";"+previousPosY);		
			System.out.println("currentPos:"+currentPosX+";"+currentPosY);		
			System.out.println("nbre sprites:"+sprites.size());		
			System.out.println("oldDir:"+oldDir);
		}
		Iterator<Sprite> iter = sprites.iterator();
		double scaledTileDim = getLoop().getScaledTileDimension();
		// tant que tous les d�placements n'ont pas �t� interdits, on passe les obstacles en revue
		while(iter.hasNext() && goOn)
		{	Sprite temp = iter.next();
			if(debug)
			{	System.out.println("newDir:"+newDir);			
				System.out.println(temp.getClass()+":"+temp.getCurrentPosX()+";"+temp.getCurrentPosY());
			}
			// si on n'a pas encore chang� la direction, il p-e est temps de le faire
			if(newDir == null)
			{	// diff�rentiel entre l'ancienne position et l'obstacle trait�
				// et direction de l'obstacle relativement � l'ancienne position
				double delta1, delta2;
				Direction tempDir;
				if(oldDir.isVertical())
				{	delta1 = previousPosX-temp.getCurrentPosX();
					tempDir = Direction.getHorizontalFromDouble(delta1);
					delta2 = previousPosY-temp.getCurrentPosY();
					newCollidedSprites[1] = temp;
				}
				else
				{	delta1 = previousPosY-temp.getCurrentPosY();
					tempDir = Direction.getVerticalFromDouble(delta1);
					delta2 = previousPosX-temp.getCurrentPosX();
					newCollidedSprites[0] = temp;
				}
				// si possible, on bouge au maximum le sprite dans l'ancienne direction
				if(Math.abs(delta2)>scaledTileDim)
				{	double dir = delta2/Math.abs(delta2);
					double d = dir*scaledTileDim;
					if(oldDir.isVertical())
						newPosY = temp.getCurrentPosY()+d;
					else 
						newPosX = temp.getCurrentPosX()+d;
				}
				// on ne modifie l'ancienne direction que si le sprite est suffisament loin du centre de l'obstacle
				/* NOTE l'aide � la navigation est ici. � ne r�server qu'aux sprites heros ? (pas bombes et autres)... 
				 * de toute fa�on, ils ne devraient jamais en avoir besoin, car ils sont cens�s �tre parfaitement centr�s 
				 */
				if(Math.abs(delta1)>scaledTileDim/4 && help)
				{	//de plus, il faut que le passage dans la nouvelle direction soit d�gag�, sinon c'est pas la peine de bouger
					double dir = delta1/Math.abs(delta1);
					// on v�rifie donc qu'aucun des autres sprites obstacles ne sont situ�s dans l'alignement de l'obstacle courant
					Iterator<Sprite> i = sprites.iterator();
					boolean found = false;
					while(i.hasNext() && !found)
					{	Sprite tp = i.next();
						if(tp!=temp)
						{	double d;
							boolean test;
//							int indx;
							if(oldDir.isVertical())
							{	d = dir*(tp.getCurrentPosX()-temp.getCurrentPosX());
								test = tp.getCurrentPosY()==temp.getCurrentPosY();
//								indx = 0;
							}
							else
							{	d = dir*(tp.getCurrentPosY()-temp.getCurrentPosY());
								test = tp.getCurrentPosX()==temp.getCurrentPosX();
//								indx = 1;
							}
							if(d>=0 && d<=scaledTileDim && test)
							{	found = true;
//								result[indx] = tp;
							}
						}
					}
					//
					if(!found)
					{	if(oldDir.isVertical())
						{	double d = Math.abs(currentPosY - previousPosY)*dir;
							newPosX = previousPosX + d;
							if(Math.abs(newPosX-temp.getCurrentPosX())>scaledTileDim) 
								newPosX = temp.getCurrentPosX() + dir*scaledTileDim;
						}
						else
						{	double d = Math.abs(currentPosX - previousPosX)*dir;
							newPosY = previousPosY + d;
							if(Math.abs(newPosY-temp.getCurrentPosY())>scaledTileDim) 
								newPosY = temp.getCurrentPosY() + dir*scaledTileDim;
						}
						newDir = tempDir;							
					}
					else
						newDir = Direction.NONE;
				}
				else
					newDir = Direction.NONE;
			}
			// si on a d�j� chang� la direction, il faut juste v�rifier la coh�rence avec les obstacles restants
			else if(newDir!=Direction.NONE)
			{	double dist=-1;
				int indx=-1;
				if(newDir.isVertical())
				{	dist = Math.abs(newPosY-temp.getCurrentPosY());
					indx = 1;
				}
				else if(newDir.isHorizontal())
				{	dist = Math.abs(newPosX-temp.getCurrentPosX());
					indx = 0;
				}
				if(isClose(dist,true))
				{	goOn = false;
					newDir = Direction.NONE;
					newCollidedSprites[indx] = temp;
				}			
			}
		}
		if(debug)
			System.out.println("newDir:"+newDir);					
		//NOTE if & else sont pareils ?
		if(newDir == Direction.NONE)
		{	currentPosX = newPosX;
			currentPosY = newPosY;
		}
		else
		{	currentPosX = newPosX;
			currentPosY = newPosY;
		}
		if(debug)
			System.out.println();	
	}
	
	private void updateIntersectedSprites(ArrayList<Sprite> newIntersectedSprites)
	{	Iterator<Sprite> i;
/*	
if(newCollidedSprites.size()>0)
	System.out.println("->"+newCollidedSprites.size());
if(collidedSprites.size()>0)
	System.out.println("->"+newCollidedSprites.size());
if(sprite instanceof Hero)	
{	System.out.print("{");
	if(newCollidedSprites.size()>0)
	{	for(int j=0;j<newCollidedSprites.size();j++)
			System.out.print(" "+newCollidedSprites.get(j));
	}
	System.out.println(" }");
	//
	System.out.print("{");
	if(collidedSprites.size()>0)
	{	for(int j=0;j<collidedSprites.size();j++)
			System.out.print(" "+collidedSprites.get(j));
	}
	System.out.println(" }");
	System.out.println();
}
*/

		// sprites no longer intersected
		i = intersectedSprites.iterator();
		while(i.hasNext())
		{	Sprite tempSprite = i.next();
			if(newIntersectedSprites.contains(tempSprite))
				newIntersectedSprites.remove(tempSprite);
			else
			{	i.remove();
				EngineEvent event = new EngineEvent(EngineEvent.INTERSECTION_OFF,sprite,tempSprite,getActualDirection());
				tempSprite.processEvent(event);
				tempSprite.removeIntersectedSprite(sprite);
				event = new EngineEvent(EngineEvent.INTERSECTION_OFF,tempSprite,sprite,getActualDirection().getOpposite());
				sprite.processEvent(event);				
			}
		}
		// new intersected sprites
		i = newIntersectedSprites.iterator();
		while(i.hasNext())
		{	Sprite tempSprite = i.next();
			intersectedSprites.add(tempSprite);
			EngineEvent event = new EngineEvent(EngineEvent.INTERSECTION_ON,sprite,tempSprite,getActualDirection());
			tempSprite.processEvent(event);
			tempSprite.addIntersectedSprite(sprite);
			event = new EngineEvent(EngineEvent.INTERSECTION_ON,tempSprite,sprite,getActualDirection().getOpposite());
			sprite.processEvent(event);
		}
	}
	
	private void updateCollidedSprites(Sprite newCollidedSprites[])
	{	//NOTE faut-il distinquer les changements de direction ?

		
/*		
//		if(newCollidedSprites.size()>0)
//			System.out.println("->"+newCollidedSprites.size());
//		if(collidedSprites.size()>0)
//			System.out.println("->"+newCollidedSprites.size());
		if(sprite instanceof Hero)	
		{	System.out.println(getActualDirection());
			System.out.print("{");
			if(newCollidedSprites.size()>0)
			{	for(int j=0;j<newCollidedSprites.size();j++)
					System.out.print(" "+newCollidedSprites.get(j));
			}
			System.out.println(" }");
			//
			System.out.print("{");qqqqq
			if(collidedSprites.size()>0)
			{	for(int j=0;j<collidedSprites.size();j++)
					System.out.print(" "+collidedSprites.get(j));
			}
			System.out.println(" }");
			System.out.println();
		}
*/		
		
		
/*		
if(sprite instanceof Hero)
	if(newCollidedSprites[0]!=null || newCollidedSprites[1]!=null)
		System.out.print("");
*/




		// sprites no longer collided
		for(int i=0;i<2;i++)
		{	if(collidedSprites[i]!=null &&
				collidedSprites[i]!=newCollidedSprites[0] && collidedSprites[i]!=newCollidedSprites[1])
			{	EngineEvent event = new EngineEvent(EngineEvent.COLLIDED_OFF,sprite,collidedSprites[i],getActualDirection());
				collidedSprites[i].processEvent(event);
				event = new EngineEvent(EngineEvent.COLLIDING_OFF,sprite,collidedSprites[i],getActualDirection());
				sprite.processEvent(event);
			}
		}
		// new collided sprites
		for(int i=0;i<2;i++)
		{	if(newCollidedSprites[i]!= null && 
				newCollidedSprites[i]!=collidedSprites[0] && newCollidedSprites[i]!=collidedSprites[1])
			{	EngineEvent event = new EngineEvent(EngineEvent.COLLIDED_ON,sprite,newCollidedSprites[i],getActualDirection());
				newCollidedSprites[i].processEvent(event);
				event = new EngineEvent(EngineEvent.COLLIDING_ON,sprite,newCollidedSprites[i],getActualDirection());
				sprite.processEvent(event);
			}
		}
		// update collidedSPrites
		for(int i=0;i<2;i++)
			collidedSprites[i] = newCollidedSprites[i];
	}
	
	public void addIntersectedSprite(Sprite intersectedSprite)
	{	intersectedSprites.add(intersectedSprite);
	}
	public void removeIntersectedSprite(Sprite intersectedSprite)
	{	intersectedSprites.remove(intersectedSprite);
	}
/*	
	public void addCollidedSprite(AbstractSprite collidedSprite)
	{	collidedSprites.add(collidedSprite);
	}
	public void removeCollidedSprite(AbstractSprite collidedSprite)
	{	collidedSprites.remove(collidedSprite);
	}
*/	
/*
	public Direction getHorizontalMoveDirection()
	{	double dx = currentPosX-previousPosX;
		Direction result = Direction.getHorizontalFromDouble(dx);
		return result;
	}
	public Direction getVerticalMoveDirection()
	{	double dy = currentPosY-previousPosY;
		Direction result = Direction.getVerticalFromDouble(dy);
		return result;
	}
	public Direction getMoveDirection()
	{	Direction h = getHorizontalMoveDirection();
		Direction v = getVerticalMoveDirection();
		Direction result = Direction.getComposite(h, v);
		return result;
	}
*/
	public boolean isColliding()
	{	return collidedSprites[0]!=null || collidedSprites[1]!=null; 
	}
	public boolean isCollidingSprite(Sprite s)
	{	return collidedSprites[0]==s || collidedSprites[1]==s;		
	}

	public boolean isIntersectingSprite(Sprite s)
	{	return intersectedSprites.contains(s);		
	}

	public boolean isOnGround()
	{	return CalculusTools.isRelativelyEqualTo(currentPosZ,0,getLoop());
	}
	
	
	
	public Loop getLoop()
	{	return sprite.getLoop();
	}
	
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// intersected sprites
			{	Iterator<Sprite> it = intersectedSprites.iterator();
				while(it.hasNext())
				{	Sprite temp = it.next();
					//temp.finish();
					it.remove();
				}
			}
			// trajectory pack
			trajectoryPack.finish();
			trajectoryPack = null;
			// misc
			collidedSprites = null;
			currentDirection = null;
			currentTrajectory = null;
			sprite = null;
		}
	}
}
