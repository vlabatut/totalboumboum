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
import java.util.Iterator;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.anime.AnimeDirection;
import fr.free.totalboumboum.engine.content.feature.anime.AnimePack;
import fr.free.totalboumboum.engine.content.feature.anime.AnimeStep;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.loop.Loop;

public class AnimeManager
{	/** sprite poss�dant ce manager */
	private Sprite sprite;
	/** ensemble de toutes les animations disponibles */
	private AnimePack animePack;
	/** animation courante */
	private AnimeDirection currentAnime;
	/** pas courrant */
	private AnimeStep currentStep;
	/** indique que l'animation est finie (on reste sur la derni�re image) */
	private boolean isTerminated;
	/** temps total �coul� de puis le d�but de l'animation */
	private double currentTime;
	/** temps normalis� �coul� de puis le d�but de l'animation (r�initialis� par un repeat) */
	private double animeTime;
	/** dur�e totale originale de l'animation */
	private double animeDuration;
	/** dur�e totale effective de l'animation */
	private double totalDuration;
	/** coefficient de mofication du temps d� au d�lai impos� */
	private double forcedDurationCoeff;
	
/* ********************************
 * INIT
 * ********************************
 */	
	public AnimeManager(Sprite sprite)
	{	this.sprite = sprite;
		forcedDurationCoeff = 1;
		totalDuration = 0;
	} 

	public void setAnimePack(AnimePack animePack)
	{	this.animePack = animePack;	
	}
	
/* ********************************
 * PROCESS
 * ********************************
 */	
	/**
	 * Change l'animation en cours pour le sprite consid�r�.
	 * Param�tres :
	 * 	- reinit : 
	 * 		- remet currentTime (le temps �coul�) � 0 et recalcule le durationCoeff.
	 * 		- ceci permet de passer d'une anime � une autre sans repartir � z�ro, par exemple pour un changement de direction pendant walking.
	 * 		- on suppose alors que les deux animes ont exactement les m�mes caract�ristiques (m�me dur�e, proportion, etc), car elles ne sont pas r�initialis�es.
	 * 	- forcedDuration : 
	 * 		- si >0 : force l'animation � durer le temps pass� en param�tre :
	 * 			- si l'animation est d�finie comme proportionnelle, la dur�e de chaque pas est modifi�e (lin�airement) pour coller � forcedDuration
	 * 			- sinon, l'animation est jou�e normalement, mais :
	 * 				- interrompue si elle est plus longue que forcedDuration
	 * 				- reste bloqu�e sur la derni�re image si elle est plus courte que forcedDuration  
	 * 		- si =0 : la dur�e de l'animation n'est pas forc�e, on utilise celle d�finie dans le fichier xml
	 * 		- si <0 : force  l'animation � durer le m�me temps que le sprite li� (s'il existe, sinon c'est comme si forcedDuration==0)
	 * 
	 * Remarques : 
	 * 	- une 'animation fixe' (une seule image ou pas d'image) sera en g�n�ral associ�e � totalDuration=0 et repeat=true.
	 * 	- s'il n'y a pas reinit, forcedDuration n'est pas pris en compte...  
	 */
	public void setGesture(String gesture, Direction direction, boolean reinit, double forcedDuration)
	{	currentAnime = animePack.getAnimeDirection(gesture, direction);
		if(reinit)
		{	isTerminated = false;
			animeDuration = currentAnime.getTotalDuration();
			// independent sprite
			if(forcedDuration>=0)
			{	currentTime = 0;
				currentStep = currentAnime.getIterator().next();
				animeTime = 0;
			}
			// bound sprite
			else
			{	// init with the bound sprite values
				if(isBoundToSprite())
				{	Sprite sprt = getBoundToSprite();
					forcedDuration = sprt.getAnimeTotalDuration();
					currentTime = sprt.getAnimeCurrentTime();
					animeTime = currentTime;
					if(currentAnime.getRepeat())
					{	while(animeTime>animeDuration)
							animeTime = animeTime - animeDuration;
					}
					updateImage();
				}
				// if no bound sprite : then supposing no forcedDuration (default case)
				else
				{	forcedDuration = 0;
					currentTime = 0;
					currentStep = currentAnime.getIterator().next();
					animeTime = 0;
				}
			}							
			
			// steady image
			if(animeDuration == 0)
			{	forcedDurationCoeff = 1;
				totalDuration = forcedDuration;
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
						forcedDurationCoeff = forcedDuration/totalDuration;
					// or not proportionnal
					else 
						forcedDurationCoeff = 1;
				}
			}
		}
	}

	/**
	 * m�thode appel�e � chaque it�ration : 
	 * met � jour l'image � afficher
	 */
	public void update()
	{	updateTime();
		updateImage();
		// check if the animations is over
		if(currentTime>totalDuration)
		{	isTerminated = true;
			sprite.processEvent(new EngineEvent(EngineEvent.ANIME_OVER));
		}
	}
	
	public void updateTime()
	{	// update current time
		double milliPeriod = sprite.getConfiguration().getMilliPeriod();
		double delta = milliPeriod*forcedDurationCoeff*sprite.getSpeedCoeff();	
		currentTime = currentTime + delta;
		animeTime = animeTime + delta;
		if(currentAnime.getRepeat() && animeDuration>0)
		{	while(animeTime>animeDuration)
				animeTime = animeTime - animeDuration;
		}
	}
	
	public void updateImage()
	{	// process current displayable image
		double nextTime = 0;
		Iterator<AnimeStep> i = currentAnime.getIterator();
		do
		{	currentStep = i.next(); 
			nextTime = nextTime + currentStep.getDuration()*forcedDurationCoeff;
		}
		while(nextTime<animeTime && i.hasNext());
	}

/* ********************************
 * TIME
 * ********************************
 */	

	/**
	 * renvoie la dur�e totale pr�vue pour l'animation.
	 * @return
	 */
	public double getTotalDuration()
	{	return totalDuration;
	}
	public double getCurrentTime()
	{	return currentTime;
	}
	
	
/* ********************************
 * SHADOW
 * ********************************
 */	
	public boolean hasShadow()
	{	return currentStep.hasShadow();	
	}
	public BufferedImage getShadow()
	{	return currentStep.getShadow();	
	}
	
	public double getShadowXShift()
	{	return currentStep.getShadowXShift();
	}
	public double getShadowYShift()
	{	return currentStep.getShadowYShift();
	}
	
/* ********************************
 * MISC
 * ********************************
 */	
	/**
	 * renvoie l'image � afficher 
	 * @return
	 */
	public BufferedImage getCurrentImage()
	{	return currentStep.getImage();
	}

	public Loop getLoop()
	{	return sprite.getLoop();
	}
	
	public PredefinedColor getColor()
	{	return animePack.getColor();		
	}
	
/* ********************************
 * POSITION
 * ********************************
 */	
	public double getXShift()
	{	return currentStep.getXShift();
	}
	public double getYShift()
	{	double result = currentStep.getYShift();
		if(isBoundToSprite())
			result = result + currentStep.getBoundYShift().getValue(getBoundToSprite());
		return result;
	}
	
/* ********************************
 * BOUND
 * ********************************
 */	
	public double getBoundHeight()
	{	return currentAnime.getBoundHeight();
	}

	private Sprite getBoundToSprite()
	{	return sprite.getBoundToSprite();
	}
	
	private boolean isBoundToSprite()
	{	return sprite.isBoundToSprite();
	}



	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// anime pack
			animePack.finish();
			animePack = null;
			// misc
			currentAnime = null;
			currentStep = null;
			sprite = null;
		}
	}
}
