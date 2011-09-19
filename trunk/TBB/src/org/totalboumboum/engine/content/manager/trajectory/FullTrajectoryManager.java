package org.totalboumboum.engine.content.manager.trajectory;

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

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.event.EngineEvent;
import org.totalboumboum.engine.content.feature.gesture.Gesture;
import org.totalboumboum.engine.content.feature.gesture.trajectory.direction.TrajectoryDirection;
import org.totalboumboum.engine.content.feature.gesture.trajectory.step.TrajectoryStep;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteChangePositionEvent;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.calculus.CombinatoricsTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class FullTrajectoryManager extends TrajectoryManager
{	
	public FullTrajectoryManager(Sprite sprite)
	{	super(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// GESTURE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** trajectoire courante */
	private TrajectoryDirection currentTrajectory;

	/**
	 * change l'animation en cours d'affichage
	 */
	@Override
	public void updateGesture(Gesture gesture, Direction spriteDirection, Direction controlDirection, boolean reinit, double forcedDuration)
	{	// init
		hasFlied = getCurrentPosZ()>0;
		@SuppressWarnings("unused")
		Direction previousDirection = currentDirection;
		currentDirection = controlDirection;
		setInteractiveMove(controlDirection);
		currentTrajectory = gesture.getTrajectoryDirection(spriteDirection);
		// reinit the gesture
		if(reinit)
		{	trajectoryDuration = currentTrajectory.getTotalDuration();
			currentTime = 0;
			// isTerminated ?
			if(trajectoryDuration == 0)
			{	isTerminated = true;
				forcedDurationCoeff = 1;
				sprite.processEvent(new EngineEvent(EngineEvent.TRAJECTORY_OVER));
			}
			else
			{	isTerminated = false;
				// forcedDuration defined (positive or null)
				if(forcedDuration>=0)
				{	currentTime = 0;
					currentStep = currentTrajectory.getIterator().next();
					trajectoryTime = 0;
				}
				// forcedDuration relative to bound sprite (negative)
				else if(isBoundToSprite())
				{	// init with the bound sprite values
					Sprite sprt = getBoundToSprite();
					forcedDuration = sprt.getTrajectoryTotalDuration();
					currentTime = sprt.getTrajectoryCurrentTime();
					trajectoryTime = currentTime;
					if(currentTrajectory.getRepeat())
					{	while(trajectoryTime>trajectoryDuration)
							trajectoryTime = trajectoryTime - trajectoryDuration;
					}
					updateStep();
				}
				// no bound sprite nor forcedDuration: act like forcedDuration=0
				else
				{	forcedDuration = 0;
					currentTime = 0;
					currentStep = currentTrajectory.getIterator().next();
					trajectoryTime = 0;
				}			
			
				// forcedDurationCoeff
				if(forcedDuration == 0)
				{	forcedDurationCoeff = 1;
					totalDuration = trajectoryDuration;
				}
				else 
				{	totalDuration = forcedDuration;
					// proportionnal
					if(currentTrajectory.getProportional())
						forcedDurationCoeff = trajectoryDuration/forcedDuration;
					// or not proportionnal
					else
						forcedDurationCoeff = 1;
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
			forcedPositionTime = currentTrajectory.getForcedPositionTime();
			processForcedShifts(currentPosX, currentPosY, currentPosZ);
		}
		// no reinit : the same gesture (or an equivalent one) goes on, but in a different direction
		else
		{	// relative position is updated
			if(trajectoryDuration!=0)
				updateRelativePos();
			// update forced shifts
			if(forcedPositionTime>0)
				correctForcedShifts();
			/* NOTE en cas de trajectoire repeat : 
			 * ne faut-il pas r�initialiser la position forc�e à chaque répétition ?
			 */
		}
	}

	/////////////////////////////////////////////////////////////////
	// FORCED SHIFTS		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** deplacement X forc� total */
	private double forcedTotalXShift = 0;
	/** deplacement Y forc� total */
	private double forcedTotalYShift = 0;
	/** deplacement Z forc� total */
	private double forcedTotalZShift = 0;

	/**
	 * si on a d�passé le forcedPositionDuration, il faut :  
	 * 		- calculer la position virtuelle à ce temps l� (ce qui implique de calculer le point de départ en r�f�rence)
	 * 		- v�rifier si elle colle à la position forc�e
	 * 		- si elle ne colle pas : la corriger
	 * si on ne l'a pas encore d�passée : à peu près pareil
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
		// calcul des positions th�oriques à l'instant prèsent
		Sprite boundToSprite = getBoundToSprite();
		// process the step
		Iterator<TrajectoryStep> i = currentTrajectory.getIterator();
		TrajectoryStep nextStep=null;
		double nextTime=0;
		double nextX=0, nextY=0, nextZ=0;
		do
		{	TrajectoryStep trajectoryStep = i.next();
			nextTime = nextTime + trajectoryStep.getDuration();
			nextStep = trajectoryStep;
			nextX = nextX+nextStep.getXShift();
			nextY = nextY+nextStep.getYShift();
			nextZ = nextZ+nextStep.getZShift(boundToSprite);
		}
		while(nextTime<trajectoryTime);
		// process the theoretical position
		double previousX = nextX - nextStep.getXShift();
		double previousY = nextY - nextStep.getYShift();
		double previousZ = nextZ - nextStep.getZShift(boundToSprite);
		double previousTime = nextTime - nextStep.getDuration();
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
		if(trajectoryTime<=forcedPositionTime)
			forcedCoef = trajectoryTime / forcedPositionTime;
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
		/* NOTE on pourrait �chelonner la correction, mais on choisit l'approche brutale
		 * quitte à affiner par la suite si nécessaire
		 */
		currentPosX = currentPosX + gapX;
		currentPosY = currentPosY + gapY;
		currentPosZ = currentPosZ + gapZ;
	}
	
	/**
	 * 1) on calcule la position relative originale (XML) à t=forcedTime
	 * 2) on l'utilise pour calculer la position absolue originale à t=forcedTime
	 * 3) on relativise cette position par rapport à la r�f�rence courante : tile ou boundToSprite
	 * 4) on calcule l'�cart avec la forcedPosition
	 * cet �cart sera r�pandu sur le d�but de la trajectoire correspondant à forcedTime 
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
				nextTime = nextTime + trajectoryStep.getDuration();
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
			double previousTime = nextTime - nextStep.getDuration();
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
				Tile tempTile = RoundVariables.level.getTile(initX, initY);
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
	
	/////////////////////////////////////////////////////////////////
	// BINDING				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * modifie la position absolue courante en fonction du boundToSprite.
	 * Cette méthode doit imp�rativement être appelée juste avant un changement de gesture.
	 * @param newSprite
	 */
	@Override
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
	
	/////////////////////////////////////////////////////////////////
	// CONTROLS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** niveau d'interaction X avec les commandes */
	private double xMove = 0;
	/** niveau d'interaction Y avec les commandes */
	private double yMove = 0;

	/**
	 * Modifie le niveau d'interaction des controles du sprite
	 * @param controlDirection
	 */
	private void setInteractiveMove(Direction controlDirection)
	{	int p[] = controlDirection.getIntFromDirection();
		xMove = p[0];
		yMove = p[1];
	}
	
	/////////////////////////////////////////////////////////////////
	// UPDATE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique que la trajectoire est termin�e (plus de déplacement) */
	private boolean isTerminated;
		/** modification de position X (utilisée lors de la mise à jour de la position) */
	double shiftX = 0;
	/** modification de position Y (utilisée lors de la mise à jour de la position) */
	double shiftY = 0;
	/** modification de position Z (utilisée lors de la mise à jour de la position) */
	double shiftZ = 0;

	/**
	 * méthode appelée à chaque itération
	 * met à jour le déplacement et la position relative.
	 * attention : dans le cas d'une répétition, le dernier point
	 * de la trajectoire sert de premier point à la répétition suivante.
	 * donc si on veut un cycle parfait, il faut rajouter un dernier point ramenant ou premier 
	 */
	@Override
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
		// keep trace of the previous position
		previousPosX = currentPosX;
		previousPosY = currentPosY;
		previousPosZ = currentPosZ;
		
		// process the interactive (control) shift
		long milliPeriod = Configuration.getEngineConfiguration().getMilliPeriod();
		double speedCoeff = sprite.getCurrentSpeedCoeff();
		shiftX = xMove*currentTrajectory.getXInteraction()*milliPeriod*speedCoeff/1000;
//if(sprite instanceof Hero)
//{	System.out.println("xMove: "+xMove);
//	System.out.println("currentTrajectory.getXInteraction(): "+currentTrajectory.getXInteraction());
//	System.out.println("milliPeriod: "+milliPeriod);
//	System.out.println("speedCoeff: "+speedCoeff);
//	System.out.println("----------------------------");
//}
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
		{	// calcul de la direction de déplacement effective du sprite par rapport à la position pr�c�dente
			double dx = currentPosX-previousPosX;
			double dy = currentPosY-previousPosY;
			Direction moveDir = Direction.getCompositeFromDouble(dx, dy);
			
			// normalizing an absolute position (if not bound)
			double temp[] = RoundVariables.level.normalizePosition(currentPosX, currentPosY);
			currentPosX = temp[0];
			currentPosY = temp[1];
			
			if(moveDir!=Direction.NONE)			
			{	
//if(sprite instanceof Hero)
//	System.out.println("position:"+previousPosX+","+previousPosY+" ("+sprite.getTile().getLine()+","+sprite.getTile().getCol()+") -> "+currentPosX+","+currentPosY+" ("+sprite.getLevel().getTile(currentPosX,currentPosY).getLine()+","+sprite.getLevel().getTile(currentPosX,currentPosY).getCol()+") ["+currentDirection+"]");	

				double dist = Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
//if(sprite instanceof Hero)
//	System.out.println("distance: "+dist);				
				MoveZone mz = new MoveZone(sprite,previousPosX,previousPosY,currentPosX,currentPosY,moveDir,moveDir,dist);
				mz.applyMove();
				//
				currentPosX = mz.getCurrentX();
				currentPosY = mz.getCurrentY();
				List<Sprite> newIntersectedSprites = mz.getIntersectedSprites();
				List<Sprite> newCollidedSprites = mz.getCollidedSprites();
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
		{	hasFlied = true;
			EngineEvent e = new EngineEvent(EngineEvent.LEAVE_GROUND,sprite,null,getActualDirection());
			sprite.getTile().spreadEvent(e);
		}
		
		// normalizing height at the end of an air move
		if(isTerminated)
		{	if(CombinatoricsTools.isRelativelyEqualTo(currentPosZ,0))
				currentPosZ = 0;
			//
			sprite.processEvent(new EngineEvent(EngineEvent.TRAJECTORY_OVER));
			if(hasFlied && currentPosZ==0)
			{	EngineEvent e = new EngineEvent(EngineEvent.TOUCH_GROUND,sprite,null,getActualDirection());
				sprite.getTile().spreadEvent(e);
			}				
		}
if(Double.isNaN(currentPosX) || Double.isNaN(currentPosY))
	System.out.println("AZDSOIP�PMJP");
	
		// updating the tile
		updateTile();
		
		// record event
		recordEvent();
	}
	
	/////////////////////////////////////////////////////////////////
	// RELATIVE POSITIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** position X relativement à la trajectoire courante */
	private double relativePosX;
	/** position Y relativement à la trajectoire courante */
	private double relativePosY;
	/** position Z relativement à la trajectoire courante */
	private double relativePosZ;

	/**
	 * met à jour la position relative à la trajectoire ou au sprite liant
	 */
	private void updateRelativePos()
	{	Iterator<TrajectoryStep> i = currentTrajectory.getIterator();
		TrajectoryStep nextStep=null;
		double nextTime=0;
		double nextX=0, nextY=0, nextZ=0;
		do
		{	TrajectoryStep trajectoryStep = i.next();
			nextTime = nextTime + trajectoryStep.getDuration();
			nextStep = trajectoryStep;
			nextX = nextX+nextStep.getXShift();
			nextY = nextY+nextStep.getYShift();				
			nextZ = nextZ+nextStep.getZShift(getBoundToSprite());				
		}
		while(nextTime<trajectoryTime && i.hasNext());
		// round
		if(nextTime<trajectoryTime)
			nextTime = trajectoryDuration;
		
		// process the intermediate position (between two steps)
		double previousX = nextX - nextStep.getXShift();
		double previousY = nextY - nextStep.getYShift();
		double previousZ = nextZ - nextStep.getZShift(getBoundToSprite());
		double previousTime = nextTime - nextStep.getDuration();
		double stepElapsedTime = trajectoryTime - previousTime;
		double coef = stepElapsedTime / (nextStep.getDuration());
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
	
	/////////////////////////////////////////////////////////////////
	// RELATIVE FORCED POSITIONS	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** position X exprimée relativement au centre de la case courante */
	private double relativeForcedPosX = 0;
	/** position Y exprimée relativement au centre de la case courante */
	private double relativeForcedPosY = 0;
	/** position Z exprimée relativement au centre de la case courante */
	private double relativeForcedPosZ = 0;

	private void updateRelativeForcedPos()
	{	if(trajectoryTime<=forcedPositionTime)
		{	double forcedCoef = trajectoryTime / forcedPositionTime;
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

	/////////////////////////////////////////////////////////////////
	// STEP					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** pas courrant */
	private TrajectoryStep currentStep;
	
	private void updateStep()
	{	// process current displayable image
		double nextTime = 0;
		Iterator<TrajectoryStep> i = currentTrajectory.getIterator();
		do
		{	currentStep = i.next(); 
			nextTime = nextTime + currentStep.getDuration()/**forcedDurationCoeff*/;
		}
		while(nextTime<trajectoryTime && i.hasNext());
	}
	
	/////////////////////////////////////////////////////////////////
	// TILE					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void updateTile()
	{	// get current and previous tiles
		Tile previousTile = sprite.getTile();
		Tile currentTile = RoundVariables.level.getTile(currentPosX,currentPosY);
		// compare them
		if(previousTile!=currentTile)
			sprite.changeTile(currentTile);
	}

	/////////////////////////////////////////////////////////////////
	// TIME					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** temps normalis� �coul� de puis le d�but de la trajectoire */
	private double trajectoryTime;
	/** dur�e totale originale de la trajectoire */
	private double trajectoryDuration = 0;
	/** coefficient de mofication du temps d� au d�lai impos� */
	private double forcedDurationCoeff = 1;
	/** temps imparti pour atteindre la position forc�e (tient compte du d�lai impos� à la trajectoire) */
	private double forcedPositionTime = 0;

	private void updateTime()
	{	double milliPeriod = Configuration.getEngineConfiguration().getMilliPeriod();
		double delta = milliPeriod*sprite.getCurrentSpeedCoeff();	
		currentTime = currentTime + delta;
		trajectoryTime = trajectoryTime + delta*forcedDurationCoeff;
		if(trajectoryTime > trajectoryDuration)
		{	// looping the trajectory
			if(currentTrajectory.getRepeat())
			{	while(trajectoryTime>trajectoryDuration)
					trajectoryTime = trajectoryTime - trajectoryDuration;
				relativePosX = currentTrajectory.getTotalXShift()-relativePosX;
				relativePosY = currentTrajectory.getTotalYShift()-relativePosY;
				relativePosZ = currentTrajectory.getTotalZShift(getBoundToSprite())-relativePosZ;
			}
			// or terminating the trajectory
			else
			{	trajectoryTime = trajectoryDuration;
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
	
	/////////////////////////////////////////////////////////////////
	// SPEED				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public double getCurrentSpeed()
	{	// get basic speed
		double basicSpeed = 0;
		if(currentDirection.isHorizontal())
			basicSpeed = basicSpeed + currentTrajectory.getXInteraction();
		if(currentDirection.isVertical())
			basicSpeed = basicSpeed + currentTrajectory.getYInteraction();
			else
				basicSpeed = currentTrajectory.getXInteraction()+currentTrajectory.getYInteraction();
		
		// apply speed coefficient
		double result = sprite.getCurrentSpeedCoeff()*basicSpeed;
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// EVENTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void recordEvent()
	{	SpriteChangePositionEvent event = new SpriteChangePositionEvent(sprite);
		if(currentPosX!=previousPosX)
			event.setChange(SpriteChangePositionEvent.SPRITE_EVENT_POSITION_X,currentPosX);
		if(currentPosY!=previousPosY)
			event.setChange(SpriteChangePositionEvent.SPRITE_EVENT_POSITION_Y,currentPosY);
		if(currentPosZ!=previousPosZ)
			event.setChange(SpriteChangePositionEvent.SPRITE_EVENT_POSITION_Z,currentPosZ);
		RoundVariables.writeEvent(event);
	}
	
	/////////////////////////////////////////////////////////////////
	// COLLISIONS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void updateCollidedSprites(List<Sprite> newCollidedSprites)
	{	//NOTE faut-il distinguer les changements de direction ?
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
			{	// remove the sprites
				i.remove();
				tempSprite.removeCollidedSprite(sprite);
				// send events
				EngineEvent event = new EngineEvent(EngineEvent.COLLIDED_OFF,sprite,tempSprite,getActualDirection());
				sprite.processEvent(event);				
				tempSprite.processEvent(event);
			}
		}
		// new collided sprites
		i = newCollidedSprites.iterator();
		while(i.hasNext())
		{	Sprite tempSprite = i.next();
			// add the sprites
			collidedSprites.add(tempSprite);
			tempSprite.addCollidedSprite(sprite);
			// send events
			EngineEvent event = new EngineEvent(EngineEvent.COLLIDED_ON,sprite,tempSprite,getActualDirection());
			sprite.processEvent(event);
			tempSprite.processEvent(event);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// INTERSECTIONS		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void updateIntersectedSprites(List<Sprite> newIntersectedSprites)
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
			{	// remove the sprites
				i.remove();
				tempSprite.removeIntersectedSprite(sprite);
				// send events
				EngineEvent event = new EngineEvent(EngineEvent.INTERSECTION_OFF,sprite,tempSprite,getActualDirection());
				sprite.processEvent(event);				
				tempSprite.processEvent(event);
			}
		}
		// new intersected sprites
		i = newIntersectedSprites.iterator();
		while(i.hasNext())
		{	Sprite tempSprite = i.next();
			// add sprites
			intersectedSprites.add(tempSprite);
			tempSprite.addIntersectedSprite(sprite);
			// send events
			EngineEvent event = new EngineEvent(EngineEvent.INTERSECTION_ON,sprite,tempSprite,getActualDirection());
			sprite.processEvent(event);
			tempSprite.processEvent(event);
		}
	}

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public TrajectoryManager copy(Sprite sprite)
	{	TrajectoryManager result = new FullTrajectoryManager(sprite);
		return result;
	}
}
