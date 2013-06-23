package org.totalboumboum.engine.content.manager.anime;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.event.EngineEvent;
import org.totalboumboum.engine.content.feature.gesture.Gesture;
import org.totalboumboum.engine.content.feature.gesture.anime.direction.AnimeDirection;
import org.totalboumboum.engine.content.feature.gesture.anime.step.AnimeStep;
import org.totalboumboum.engine.content.feature.gesture.anime.stepimage.StepImage;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.loop.LocalLoop;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.images.ImageTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class AnimeManager
{	
	public AnimeManager(Sprite sprite)
	{	this.sprite = sprite;
	} 
	
	/////////////////////////////////////////////////////////////////
	// SPRITE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** sprite possédant ce manager */
	protected Sprite sprite;
	
	/////////////////////////////////////////////////////////////////
	// GESTURE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** animation courante */
	protected AnimeDirection currentAnime;
	
	/**
	 * Change l'animation en cours pour le sprite considéré.
	 * paramètres :
	 * 	- reinit : 
	 * 		- remet currentTime (le temps écoulé) à 0 et recalcule le durationCoeff.
	 * 		- ceci permet de passer d'une anime à une autre sans repartir à zéro, par exemple pour un changement de direction pendant walking.
	 * 		- on suppose alors que les deux animes ont exactement les mêmes caractéristiques (même durée, proportion, etc), car elles ne sont pas réinitialisées.
	 * 	- forcedDuration : 
	 * 		- si >0 : force l'animation à durer le temps passé en paramètre :
	 * 			- si l'animation est définie comme proportionnelle, la durée de chaque pas est modifiée (linéairement) pour coller à forcedDuration
	 * 			- sinon, l'animation est jouée normalement, mais :
	 * 				- interrompue si elle est plus longue que forcedDuration
	 * 				- reste bloquée sur la dernière image si elle est plus courte que forcedDuration  
	 * 		- si =0 : la durée de l'animation n'est pas forcée, on utilise celle définie dans le fichier xml
	 * 		- si <0 : force  l'animation à durer le même temps que le sprite lié (s'il existe, sinon c'est comme si forcedDuration==0)
	 * 
	 * Remarques : 
	 * 	- une 'animation fixe' (une seule image ou pas d'image) sera en général associée à totalDuration=0 et repeat=true.
	 * 	- s'il n'y a pas reinit, forcedDuration n'est pas pris en compte...  
	 */
	public void updateGesture(Gesture gesture, Direction direction, boolean reinit, double forcedDuration)
	{	currentDirection = direction;
		twinkleChange = false;
	
//if(currentDirection==null)
//	while(true)System.out.println("null direction");
//if(sprite instanceof Floor && gesture.getName()==GestureName.ENTERING)
//	System.out.println("F>>totalTime="+GameVariables.loop.getTotalTime()+" forcedDuration="+forcedDuration);
//if(sprite instanceof Block && gesture.getName()==GestureName.ENTERING)
//	System.out.println("B>>totalTime="+GameVariables.loop.getTotalTime()+" forcedDuration="+forcedDuration);
//if(sprite instanceof Hero && gesture.getName()==GestureName.EXULTING)
//	System.out.println("H>>totalTime="+GameVariables.loop.getTotalTime()+" forcedDuration="+forcedDuration);
//if(sprite instanceof Hero)
//	System.out.println("H>>"+gesture.getName());

		currentAnime = gesture.getAnimeDirection(currentDirection);
		if(currentAnime==null)
		{	currentAnime = new AnimeDirection();
			currentAnime.setDirection(direction);
			currentAnime.setGestureName(gesture.getName());
			AnimeStep as = new AnimeStep();
			currentAnime.add(as);
			//TODO à supprimer si le chargement d'animations est finalisé de manière à éviter cette situation (= anime vide)
		}
		if(reinit)
		{	isTerminated = false;
			animeDuration = currentAnime.getTotalDuration();
			// forcedDuration defined (positive or null)
			if(forcedDuration>=0)
			{	currentTime = 0;
				currentStep = currentAnime.getIterator().next();
				animeTime = 0;
			}
			// forcedDuration relative to bound sprite (negative)
			else if(isBoundToSprite())
			{	// init with the bound sprite values
				Sprite sprt = getBoundToSprite();
				forcedDuration = sprt.getAnimeTotalDuration();
				currentTime = sprt.getAnimeCurrentTime();
				animeTime = currentTime;
				if(currentAnime.getRepeat())
				{	while(animeTime>animeDuration)
						animeTime = animeTime - animeDuration;
				}
				updateStep();
			}
			// no bound sprite nor forcedDuration: act like forcedDuration=0
			else
			{	forcedDuration = 0;
				currentTime = 0;
				currentStep = currentAnime.getIterator().next();
				animeTime = 0;
			}
			
			// steady image
			if(animeDuration == 0)
			{	forcedDurationCoeff = 1;
				totalDuration = forcedDuration;
				isTerminated = false; //NOTE was true, but should not : it's supposed to be over only when the forcedDuration is over
			}			
			// actual anime
			else
			{	// without forced duration 
				if(forcedDuration == 0)
				{	forcedDurationCoeff = 1;
					totalDuration = animeDuration;
				}
				// with forced duration
				else 
				{	totalDuration = forcedDuration;
					// proportionnal
					if(currentAnime.getProportional())
						forcedDurationCoeff = animeDuration/forcedDuration;
					// or not proportionnal
					else 
						forcedDurationCoeff = 1;
				}
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// UPDATE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * méthode appelée à chaque itération : 
	 * met à jour l'image à afficher
	 */
	public void update()
	{	updateTime();
//if(sprite instanceof Floor)
//	System.out.println("F  totalTime="+GameVariables.loop.getTotalTime()+" currentTime="+currentTime);
//if(sprite instanceof Block)
//	System.out.println("B  totalTime="+GameVariables.loop.getTotalTime()+" currentTime="+currentTime);
//if(sprite instanceof Hero)
//	System.out.println("H  totalTime="+GameVariables.loop.getTotalTime()+" currentTime="+currentTime+" animeTime="+animeTime);
		updateStep();
		// check if the animations is over
		if(currentTime>totalDuration)
		{	isTerminated = true;
			// process the anime end only if the loop is regular/server
			if(RoundVariables.loop instanceof LocalLoop)
				sprite.processEvent(new EngineEvent(EngineEvent.ANIME_OVER));
//if(sprite instanceof Floor)
//	System.out.println("F<<totalTime="+GameVariables.loop.getTotalTime()+" currentTime="+currentTime);
//if(sprite instanceof Block)
//	System.out.println("B<<totalTime="+GameVariables.loop.getTotalTime()+" currentTime="+currentTime);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// STEP					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** pas courrant */
	protected AnimeStep currentStep;

	/**
	 * calcule l'étape courante de l'animation, en fonction du temps courant
	 */
	private void updateStep()
	{	// process current displayable image
		double nextTime = 0;
		Iterator<AnimeStep> i = currentAnime.getIterator();
		do
		{	currentStep = i.next(); 
			nextTime = nextTime + currentStep.getDuration()/**forcedDurationCoeff*/;
		}
		while(nextTime<animeTime && i.hasNext());
	}

	/////////////////////////////////////////////////////////////////
	// TIME					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** temps total écoulé de puis le début de l'animation */
	protected double currentTime;
	/** temps normalisé écoulé de puis le début de l'animation (réinitialisé par un repeat) */
	protected double animeTime;
	/** durée totale originale de l'animation */
	protected double animeDuration;
	/** durée totale effective de l'animation */
	protected double totalDuration = 0;
	/** coefficient de mofication du temps dû au délai imposé */
	protected double forcedDurationCoeff = 1;
	
	/**
	 * met à jour les différentes variables gérant le temps
	 */
	private void updateTime()
	{	// update current time
//if(sprite instanceof Hero && currentAnime.getName().startsWith(GestureName.EXULTING.toString()))
//		System.out.println();
		double milliPeriod = Configuration.getEngineConfiguration().getMilliPeriod();
		double delta = milliPeriod*sprite.getCurrentSpeedCoeff();	
		currentTime = currentTime + delta;
		
		// anime time
		animeTime = animeTime + delta*forcedDurationCoeff;
		if(currentAnime.getRepeat() && animeDuration>0)
		{	while(animeTime>animeDuration)
				animeTime = animeTime - animeDuration;
		}
		
		// invisible?
		StateAbility ability = sprite.modulateStateAbility(StateAbilityName.SPRITE_INVISIBLE);
		if(ability.isActive())
		{	double proba = Math.random();
			invisible = proba>=visibleProbability;
		}
		else
		{	invisible = false;
			// twinkle?
			ability = sprite.modulateStateAbility(StateAbilityName.SPRITE_TWINKLE);
			int colorInt = (int)ability.getStrength();
			if(colorInt!=0)
			{	twinkleTime = twinkleTime + milliPeriod;
				// get color
				if(colorInt<0)
					twinkleColor = null;
				else
					twinkleColor = colorInt;
				// get time values
				ability = sprite.modulateStateAbility(StateAbilityName.SPRITE_TWINKLE_NORMAL);
				double showDuration = ability.getStrength();
				ability = sprite.modulateStateAbility(StateAbilityName.SPRITE_TWINKLE_COLOR);
				double hideDuration = ability.getStrength();
				if(showDuration<=0)
				{	if(hideDuration<=0)
						hideDuration = 500; // NOTE arbitrary value
					showDuration = hideDuration;
				}
				else
				{	if(hideDuration<=0)
						hideDuration = showDuration;
				}
				double twinkleDuration = hideDuration + showDuration;
				// NOTE all the previous processing stuff could be done once and for all, if we suppose these abilities don't change, and the same holds for blinking
				// process current twinkle state
				long mod = ((long)twinkleTime) % ((long)twinkleDuration);
				twinkleChange = mod>showDuration;
			}
			else
			{	twinkleChange = false;
				twinkleTime = 0;
			}
		}
	}
	
	/**
	 * renvoie la durée totale prévue pour l'animation.
	 * @return
	 */
	public double getTotalDuration()
	{	return totalDuration;
	}
	
	public double getCurrentTime()
	{	return currentTime;
	}
	
	
	/////////////////////////////////////////////////////////////////
	// SHADOW				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean hasShadow()
	{	return currentStep.hasShadow();	
	}
	
	public StepImage getShadow()
	{	StepImage result = null;
		if(!invisible)
		{	result = currentStep.getShadow();
			if(result!=null && twinkleChange)
			{	if(twinkleColor==null)
					result = null;
				else
				{	result = result.copy();
					BufferedImage image = result.getImage();
					image = ImageTools.getFilledImage(image,twinkleColor);
					result.setImage(image);
				
				}
			}
		}
		//result = ImageTools.getDarkenedImage(result,blinkParam);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TWINKLE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indicates if the sprite should be colored because of some twinkle going on */
	protected boolean twinkleChange;
	/** color (or absence of color) used for twinkling */
	protected Integer twinkleColor;
	/** indicates how long the sprite has been twinkling */
	protected double twinkleTime;
	/** constant used to modify the blinking image */
//	private float blinkParam = 2f;

	/////////////////////////////////////////////////////////////////
	// INVISIBLE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** sprite temporarily invisible */
	protected boolean invisible;
	/** chance for the sprite not to be invisible when he's supposed to be */
	protected float visibleProbability = 0.02f;

	/////////////////////////////////////////////////////////////////
	// MISC					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * renvoie l'image à afficher 
	 * @return
	 */
	public List<StepImage> getCurrentImages()
	{	List<StepImage> result = new ArrayList<StepImage>();
		if(!invisible)
		{	result = currentStep.getImages();
			if(!result.isEmpty() && twinkleChange)
			{	if(twinkleColor==null)
					result = new ArrayList<StepImage>();
				else
				{	List<StepImage> temp = new ArrayList<StepImage>();
					for(StepImage stepImage: result)
					{	BufferedImage image = stepImage.getImage();
						double xSft = stepImage.getXShift();
						double ySft = stepImage.getYShift();
						BufferedImage imageCopy = ImageTools.getFilledImage(image,twinkleColor);
						StepImage stepImageCopy = new StepImage(imageCopy,xSft,ySft);
						temp.add(stepImageCopy);
					}
					result = temp;
				}
			}
		}
		//result = ImageTools.getDarkenedImage(result,blinkParam);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TERMINATED			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique que l'animation est finie (on reste sur la dernière image) */
	protected boolean isTerminated;
	
	public boolean isTerminated()
	{	return isTerminated;	
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECTION			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** current facing direction */
	protected Direction currentDirection = Direction.NONE;
	
	public Direction getCurrentDirection()
	{	return currentDirection;
	}
	
	/////////////////////////////////////////////////////////////////
	// BOUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public double getBoundHeight()
	{	return currentAnime.getBoundHeight();
	}

	private Sprite getBoundToSprite()
	{	return sprite.getBoundToSprite();
	}
	
	private boolean isBoundToSprite()
	{	return sprite.isBoundToSprite();
	}

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public AnimeManager copy(Sprite sprite)
	{	AnimeManager result = new AnimeManager(sprite);
		return result;
	}
}