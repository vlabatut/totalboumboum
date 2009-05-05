package fr.free.totalboumboum.engine.content.manager.trajectory;

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
import java.util.Iterator;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.GestureConstants;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryDirection;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryPack;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryStep;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.CalculusTools;

public class TrajectoryManager
{	// divers
	/** sprite dirigé par ce TrajectoryManager */
	private Sprite sprite;
	/** ensemble de toutes les trajectoires disponibles */
	private TrajectoryPack trajectoryPack;
	/** trajectoire courante */
	private TrajectoryDirection currentTrajectory;
	/** indique que la trajectoire est terminée (plus de déplacement) */
	private boolean isTerminated;
	/** temps normalisé écoulé de puis le début de la trajectoire */
	private double currentTime;
	/** durée totale de la trajectoire (soit l'originale, soit la forcée) */
	private double totalDuration = 0;
	/** coefficient de mofication du temps dû au délai imposé */
	private double forcedDurationCoeff = 1;
	/** nom du geste courant */
	private String currentGestureName = GestureConstants.NONE;
	/** modification de position X (utilisée lors de la mise à jour de la position) */
	double shiftX = 0;
	/** modification de position Y (utilisée lors de la mise à jour de la position) */
	double shiftY = 0;
	/** modification de position Z (utilisée lors de la mise à jour de la position) */
	double shiftZ = 0;

	// position
	/** position X relativement à la trajectoire courante */
	private double relativePosX;
	/** position Y relativement à la trajectoire courante */
	private double relativePosY;
	/** position Z relativement à la trajectoire courante */
	private double relativePosZ;
	/** position X absolue (en fait : soit par rapport au niveau, soit par rapport au boundToSprite) */
	private double currentPosX = 0;
	/** position Y absolue (en fait : soit par rapport au niveau, soit par rapport au boundToSprite) */
	private double currentPosY = 0;
	/** position Z absolue (en fait : soit par rapport au niveau, soit par rapport au boundToSprite) */
	private double currentPosZ = 0;
	/** position X précédente (absolue) */
	private double previousPosX;
	/** position Y précédente (absolue) */
	private double previousPosY;
	/** position Z précédente (absolue) */
	@SuppressWarnings("unused")
	private double previousPosZ;
	/** direction de déplacement courante */
	private Direction currentDirection = Direction.NONE;
	/** niveau d'interaction X avec les commandes */
	private double xMove = 0;
	/** niveau d'interaction Y avec les commandes */
	private double yMove = 0;
	
	// position imposée
	/** deplacement X forcé total */
	private double forcedTotalXShift = 0;
	/** deplacement Y forcé total */
	private double forcedTotalYShift = 0;
	/** deplacement Z forcé total */
	private double forcedTotalZShift = 0;
	/** temps imparti pour atteindre la position forcée (tient compte du délai imposé à la trajectoire) */
	private double forcedPositionTime = 0;
	/** position X exprimée relativement au centre de la case courante */
	private double relativeForcedPosX = 0;
	/** position Y exprimée relativement au centre de la case courante */
	private double relativeForcedPosY = 0;
	/** position Z exprimée relativement au centre de la case courante */
	private double relativeForcedPosZ = 0;
	
	// collisions
	private final ArrayList<Sprite> intersectedSprites = new ArrayList<Sprite>();;
	private final ArrayList<Sprite> collidedSprites = new ArrayList<Sprite>();;
	
	
	

	/** indique si la trajectoire a impliqué (pour le moment) que le sprite ait décollé du sol */ 
	private boolean hasFlied;
	
/* ********************************
 * INIT
 * ********************************
 */	
	public TrajectoryManager(Sprite sprite)
	{	this.sprite = sprite;
	}

	public void setTrajectoryPack(TrajectoryPack trajectoryPack)
	{	this.trajectoryPack = trajectoryPack;	
	}
	
	/**
	 * change l'animation en cours d'affichage
	 */
	public void setGesture(String gesture, Direction spriteDirection, Direction controlDirection, boolean reinit, double forcedDuration)
	{	currentGestureName = gesture;
		hasFlied = getCurrentPosZ()>0;
		@SuppressWarnings("unused")
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
				sprite.processEvent(new EngineEvent(EngineEvent.TRAJECTORY_OVER));
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
		// no reinit : the same gesture (or an equivalent one) goes on, but in a different direction
		else
		{	// relative position is updated
			if(totalDuration!=0)
				updateRelativePos();
			// update forced shifts
			if(forcedPositionTime>0)
				correctForcedShifts();
			/* NOTE en cas de trajectoire repeat : 
			 * ne faut-il pas réinitialiser la position forcée à chaque répétition ?
			 */
		}
	}

	/**
	 * si on a dépassé le forcedPositionDuration, il faut :  
	 * 		- calculer la position virtuelle à ce temps là (ce qui implique de calculer le point de départ en référence)
	 * 		- vérifier si elle colle à la position forcée
	 * 		- si elle ne colle pas : la corriger
	 * si on ne l'a pas encore dépassée : à peu près pareil
	 * 		- calculer la position initiale
	 * 		- calculer la position forcée correspondante
	 * 		- corriger  
	 */
	private void correctForcedShifts()
	{	// on calcule la position initiale virtuelle pour la nouvelle trajectoire
		double initX = currentPosX - relativePosX;
		double initY = currentPosY - relativePosY;
		double initZ = currentPosZ - relativePosZ;
		// calcul des nouvelles positions forcées
		processForcedShifts(initX,initY,initZ);
		// calcul des positions théoriques à l'instant présent
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
		/* TODO on pourrait échelonner la correction, mais on choisit l'approche brutale
		 * quitte à affiner par la suite si nécessaire
		 */
		currentPosX = currentPosX + gapX;
		currentPosY = currentPosY + gapY;
		currentPosZ = currentPosZ + gapZ;
	}
	
	/**
	 * 1) on calcule la position relative originale (XML) à t=forcedTime
	 * 2) on l'utilise pour calculer la position absolue originale à t=forcedTime
	 * 3) on relativise cette position par rapport à la référence courante : tile ou boundToSprite
	 * 4) on calcule l'écart avec la forcedPosition
	 * cet écart sera répandu sur le début de la trajectoire correspondant à forcedTime 
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
	 * Cette méthode doit impérativement être appelée juste avant un changement de gesture.
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
			// avant:oui - maintenant:oui (mais pas le même sprite)
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
	
/* ********************************
 * UPDATE
 * ********************************
 */	
	/**
	 * méthode appelée à chaque itération
	 * met à jour le déplacement et la position relative.
	 * attention : dans le cas d'une répétition, le dernier point
	 * de la trajectoire sert de premier point à la répétition suivante.
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
		long milliPeriod = Configuration.getEngineConfiguration().getMilliPeriod();
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
		{	// calcul de la direction de déplacement effective du sprite par rapport à la position précédente
			double dx = currentPosX-previousPosX;
			double dy = currentPosY-previousPosY;
			Direction moveDir = Direction.getCompositeFromDouble(dx, dy);
			
			// normalizing an absolute position (if not bound)
			double temp[] = sprite.getLevel().normalizePosition(currentPosX, currentPosY);
			currentPosX = temp[0];
			currentPosY = temp[1];
			
			if(moveDir!=Direction.NONE)			
			{	
//if(sprite instanceof Hero)
//	System.out.println("position:"+previousPosX+","+previousPosY+" ("+sprite.getTile().getLine()+","+sprite.getTile().getCol()+") -> "+currentPosX+","+currentPosY+" ("+sprite.getLevel().getTile(currentPosX,currentPosY).getLine()+","+sprite.getLevel().getTile(currentPosX,currentPosY).getCol()+") ["+currentDirection+"]");	

				double dist = Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
				MoveZone mz = new MoveZone(sprite,previousPosX,previousPosY,currentPosX,currentPosY,sprite.getLevel(),moveDir,moveDir,dist);
				mz.applyMove();
				//
				currentPosX = mz.getCurrentX();
				currentPosY = mz.getCurrentY();
				ArrayList<Sprite> newIntersectedSprites = mz.getIntersectedSprites();
				ArrayList<Sprite> newCollidedSprites = mz.getCollidedSprites();
				//
				updateCollidedSprites(newCollidedSprites);
/*				
System.out.print("{");
if(collidedSprites.size()>0)
{	for(int j=0;j<collidedSprites.size();j++)
		System.out.print(" "+collidedSprites.get(j));
}
System.out.println(" }");
//System.out.println();
*/
				updateIntersectedSprites(newIntersectedSprites);
/*				
System.out.print("[");
if(intersectedSprites.size()>0)
{	for(int j=0;j<intersectedSprites.size();j++)
		System.out.print(" "+intersectedSprites.get(j));
}
System.out.println(" ]");
System.out.println();
*/
			}
		}
		
		// updating the flight flag
		if(!hasFlied && currentPosZ>0)
			hasFlied = true;
		
		// normalizing height at the end of an air move
		if(isTerminated)
		{	if(CalculusTools.isRelativelyEqualTo(currentPosZ,0,sprite.getLoop()))
				currentPosZ = 0;
			//
			if(hasFlied && currentPosZ==0)
			{	sprite.processEvent(new EngineEvent(EngineEvent.TRAJECTORY_OVER));
				EngineEvent e = new EngineEvent(EngineEvent.TOUCH_GROUND,sprite,null,getActualDirection());
				sprite.getTile().spreadEvent(e);
			}
			else
				sprite.processEvent(new EngineEvent(EngineEvent.TRAJECTORY_OVER));
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

	private void updateTime()
	{	double milliPeriod = Configuration.getEngineConfiguration().getMilliPeriod();
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
			}
		}	
	}

/* ********************************
 * TIME
 * ********************************
 */	
	/**
	 * renvoie la durée totale prévue pour la trajectoire.
	 * @return
	 */
	public double getTotalDuration()
	{	return totalDuration;
	}
	
	public double getCurrentTime()
	{	return currentTime;
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
 * DIRECTION
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

/* ********************************
 * COLLISIONS
 * ********************************
 */		
	private void updateCollidedSprites(ArrayList<Sprite> newCollidedSprites)
	{	//NOTE faut-il distinquer les changements de direction ?
		Iterator<Sprite> i;
		
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
			System.out.print("{");
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
		i = collidedSprites.iterator();
		while(i.hasNext())
		{	Sprite tempSprite = i.next();
			if(newCollidedSprites.contains(tempSprite))
				newCollidedSprites.remove(tempSprite);
			else
			{	i.remove();
				EngineEvent event = new EngineEvent(EngineEvent.COLLIDED_OFF,sprite,tempSprite,getActualDirection());
				tempSprite.processEvent(event);
				tempSprite.removeCollidedSprite(sprite);
				event = new EngineEvent(EngineEvent.COLLIDED_OFF,tempSprite,sprite,getActualDirection().getOpposite());
				sprite.processEvent(event);				
			}
		}
		// new collided sprites
		i = newCollidedSprites.iterator();
		while(i.hasNext())
		{	Sprite tempSprite = i.next();
			collidedSprites.add(tempSprite);
			EngineEvent event = new EngineEvent(EngineEvent.COLLIDED_ON,sprite,tempSprite,getActualDirection());
			tempSprite.processEvent(event);
			tempSprite.addCollidedSprite(sprite);
			event = new EngineEvent(EngineEvent.COLLIDED_ON,tempSprite,sprite,getActualDirection().getOpposite());
			sprite.processEvent(event);
		}
	}
	
	public void addCollidedSprite(Sprite collidedSprite)
	{	collidedSprites.add(collidedSprite);
	}
	public void removeCollidedSprite(Sprite collidedSprite)
	{	collidedSprites.remove(collidedSprite);
	}	

	public boolean isColliding()
	{	return !collidedSprites.isEmpty();
	}
	public boolean isCollidingSprite(Sprite s)
	{	return collidedSprites.contains(s);
	}

/* ********************************
 * INTERSECTIONS
 * ********************************
 */	
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
	
	public void addIntersectedSprite(Sprite intersectedSprite)
	{	intersectedSprites.add(intersectedSprite);
	}
	public void removeIntersectedSprite(Sprite intersectedSprite)
	{	intersectedSprites.remove(intersectedSprite);
	}
	
	public boolean isIntersectingSprite(Sprite s)
	{	return intersectedSprites.contains(s);
	}

	public boolean isOnGround()
	{	return CalculusTools.isRelativelyEqualTo(currentPosZ,0,getLoop());
	}
	
/* ********************************
 * FINISHED
 * ********************************
 */	
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// trajectory pack
			trajectoryPack.finish();
			trajectoryPack = null;
			// misc
			collidedSprites.clear();
			intersectedSprites.clear();
			currentDirection = null;
			currentTrajectory = null;
			sprite = null;
		}
	}	

/* ********************************
 * MISC
 * ********************************
 */	
	public Loop getLoop()
	{	return sprite.getLoop();
	}
	
	public String getCurrentGestureName()
	{	return currentGestureName;		
	}
}
