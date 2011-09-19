package org.totalboumboum.ai.v200910.adapter;

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

import java.util.concurrent.Callable;

import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.AiOutput;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.configuration.Configuration;

/**
 * classe dont chaque IA doit h�riter. La méthode processAction est la méthode 
 * appelée par le gestionnaire d'IA pour l'interroger que la prochaine action 
 * à effectuer.
 * <p>
 * ATTENTION : remarque tr�s importante.
 * A la fin de la partie, le jeu demande à l'IA de s'arrêter. Dans certaines
 * conditions, l'IA ne voudra pas s'arrêter (par exemple si elle est dans une
 * boucle infinie, ou bloquée dans un traitement r�cursif). Pour �viter ce 
 * genre de probl�me, CHAQUE méthode définie dans l'IA doit :
 * 	- CONTENIR A SON TOUT DEBUT un appel à la méthode checkInterruption()
 *  - faire suivre (mot-cl� throw) les interruptions StopRequestException, et ne SURTOUT PAS les traiter (pas de try/catch)
 * De plus, cette fonction doit également appara�tre au d�but de chaque boucle
 * définie dans l'IA, qu'il s'agisse d'un for, d'un while ou d'un do/while.
 *  
 * @author Vincent Labatut
 *
 */
public abstract class ArtificialIntelligence implements Callable<AiAction>
{	
	/////////////////////////////////////////////////////////////////
	// THREAD			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indicateur de première invocation (pour la compatibilité */
	private boolean firstTime = true;
	/** indicateur de demande de terminaison de l'IA (activ� par le jeu à la fin de la partie) */
	private boolean stopRequest = false;
	/** compteur temporel pour �viter que le thread rende la main trop souvent */
	private Long lastYield = null;
	/** compte les appels à checkInterruption() entre deux yields */
	private int callCount = 0;

	/**
	 * méthode appelée par le jeu pour demander la fin de l'IA.
	 * Elle modifie la valeur de l'indcateur stopRequest, ce qui permettra
	 * de lever une StopRequestException au prochain appel de la méthode checkInterruption.
	 */
	public synchronized void stopRequest()
	{	stopRequest = true;		
	}
	
	/**
	 * méthode testant si le jeu a demandé la terminaison de l'IA.
	 * Si c'est le cas, une exception est lev�e, qui sera propag�e jusqu'à call
	 * et forcera la terminaison de l'IA. Cette exception ne doit surtout pas être
	 * intercept�e localement par un try/catch. 
	 */
	public synchronized void checkInterruption() throws StopRequestException
	{	if(lastYield==null)
		{	lastYield = System.currentTimeMillis();
			Thread.yield();
		}
		else
		{	callCount++;
			long newTime = System.currentTimeMillis();
			long diff = newTime - lastYield;
			long limit = Configuration.getAisConfiguration().getAiYieldPeriod();
			if(diff>limit)
			{	lastYield = newTime;
//if(this instanceof AdatepeOzbek)		
//	System.out.println(this.getClass()+">"+diff+"("+callCount+")");
				callCount = 0;
				Thread.yield();
			}			
		}
		
		//Thread.yield();
		if(stopRequest)
			throw new StopRequestException();
	}
	
	@Override
	public final AiAction call()
	{	AiAction result = new AiAction(AiActionName.NONE);
	
		if(firstTime)
			firstTime = false;
		else
		{	// on r�initialise la sortie de l'IA
			reinitOutput();
			try
			{	// on calcule la prochaine action à effectuer
				result = processAction();		
			}
			catch (StopRequestException e)
			{	result = new AiAction(AiActionName.NONE);
			}
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** les percepts auxquels l'IA a accès */
	private AiZone percepts;
	
	/**
	 * méthode implémentant le traitement effectué par l'IA sur les percepts,
	 * et renvoyant une action en r�action.
	 * 
	 * @return	action que l'IA a d�cider d'effectuer
	 * @throws StopRequestException	au cas où le moteur demande la terminaison de l'IA
	 */
	public abstract AiAction processAction() throws StopRequestException;

	/**
	 * renvoie les percepts auxquels l'IA a accès
	 * @return	une AiZone représentant tous les percepts utilisables par l'IA
	 */
	public AiZone getPercepts()
	{	return percepts;
	}
	/**
	 * méthode utilisée par le moteur du jeu pour initialiser les percepts de l'IA. 
	 * 
	 * @param percepts	l'objet représentant les percepts auxquels l'IA aura accès
	 */
	public void setPercepts(AiZone percepts)
	{	this.percepts = percepts;
		output = new AiOutput(percepts);
	}

	/**
	 * termine proprement l'IA afin de lib�rer les ressources qu'elle occupait.
	 */
	void finish()
	{	percepts = null;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** sortie graphique de l'IA */
	private AiOutput output;
	
	/** 
	 * renvoie la sortie graphique de l'IA, 
	 * afin d'afficher des informations par dessus la zone de jeu
	 * (utile lors du d�bogage). Le programme peut modifier cet objet
	 * pour colorer des cases et afficher des chemins ou du texte
	 * 
	 * @return	la sortie de l'IA
	 */
	public AiOutput getOutput()
	{	return output;
	}

	/**
	 * r�initialise la sortie graphique de l'IA.
	 * méthode appelée automatiquement avant chaque itération de l'IA.
	 */
	private void reinitOutput()
	{	output.reinit();
	}
}
