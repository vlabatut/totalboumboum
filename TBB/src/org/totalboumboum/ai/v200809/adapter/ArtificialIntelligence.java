package org.totalboumboum.ai.v200809.adapter;

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

/**
 * classe dont chaque IA doit hériter. La méthode processAction est la méthode 
 * appelée par le gestionnaire d'IA pour l'interroger que la prochaine action 
 * à effectuer.
 * <p>
 * ATTENTION : remarque très importante.
 * A la fin de la partie, le jeu demande à l'IA de s'arrêter. Dans certaines
 * conditions, l'IA ne voudra pas s'arrêter (par exemple si elle est dans une
 * boucle infinie, ou bloquée dans un traitement récursif). Pour éviter ce 
 * genre de problème, CHAQUE méthode définie dans l'IA doit :
 * 	- CONTENIR A SON TOUT DEBUT un appel à la méthode checkInterruption()
 *  - faire suivre (mot-clé throw) les interruptions StopRequestException, et ne SURTOUT PAS les traiter (pas de try/catch)
 * De plus, cette fonction doit également apparaître au début de chaque boucle
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
	/** indicateur de demande de terminaison de l'IA (activé par le jeu à la fin de la partie) */
	private boolean stopRequest = false;

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
	 * Si c'est le cas, une exception est levée, qui sera propagée jusqu'à call
	 * et forcera la terminaison de l'IA. Cette exception ne doit surtout pas être
	 * interceptée localement par un try/catch. 
	 */
	public synchronized void checkInterruption() throws StopRequestException
	{	if(stopRequest)
			throw new StopRequestException();
	}
	
	@Override
	public final AiAction call()
	{	AiAction result = new AiAction(AiActionName.NONE);
		
		if(firstTime)
			firstTime = false;
		else
		{	try
			{	result = processAction();		
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
	 * et renvoyant une action en réaction.
	 * 
	 * @return	action que l'IA a décider d'effectuer
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
	}

	/**
	 * termine proprement l'IA afin de libérer les ressources qu'elle occupait.
	 */
	void finish()
	{	percepts = null;
	}
}
