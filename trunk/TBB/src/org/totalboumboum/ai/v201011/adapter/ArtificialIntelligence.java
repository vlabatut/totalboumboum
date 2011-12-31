package org.totalboumboum.ai.v201011.adapter;

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

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

/**
 * classe dont chaque IA doit hériter. La méthode processAction est la méthode 
 * appelée par le gestionnaire d'IA pour l'interroger que la prochaine action 
 * à effectuer.
 * <p>
 * <b>ATTENTION :</b> remarque très importante.
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
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public abstract class ArtificialIntelligence implements Callable<AiAction>
{	
	/////////////////////////////////////////////////////////////////
	// THREAD			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicateur de demande de terminaison de l'IA (activé par le jeu à la fin de la partie) */
	private boolean stopRequest = false;
	/** temps total écoulé */
	protected long totalDuration = 0;
	/** Indique s'il s'agit du tout premier appel du thread (avant le début du jeu) */
	private boolean blank = true;

	/**
	 * méthode appelée par le jeu pour demander la fin de l'IA.
	 * Elle modifie la valeur de l'indcateur stopRequest, ce qui permettra
	 * de lever une StopRequestException au prochain appel de la méthode checkInterruption.
	 */
	public synchronized final void stopRequest()
	{	stopRequest = true;		
	}
	
	/**
	 * méthode testant si le jeu a demandé la terminaison de l'IA.
	 * Si c'est le cas, une exception est levée, qui sera propagée jusqu'à call
	 * et forcera la terminaison de l'IA. Cette exception ne doit surtout pas être
	 * interceptée localement par un try/catch. 
	 */
	public synchronized final void checkInterruption() throws StopRequestException
	{	Thread.yield();
		if(stopRequest)
			throw new StopRequestException();
	}
	
	@Override
	public final AiAction call()
	{	long before = System.currentTimeMillis();
		
		AiAction result;
		// on réinitialise la sortie de l'IA
		reinitOutput();
		
		// tout premier appel (avant le début de la partie)
		if(blank)
		{	blank = false;
			result = new AiAction(AiActionName.NONE);
		}
		
		// cas général (appel en cours de jeu)
		else
		{	try
			{	// on initialise l'IA si besoin
				if(!initialized)
				{	initialized = true;
					init();
					result = new AiAction(AiActionName.NONE);
				}
				
				// on calcule la prochaine action à effectuer
				else
					result = processAction();		
			}
			catch (StopRequestException e)
			{	result = new AiAction(AiActionName.NONE);
			}
		}
		
		long after = System.currentTimeMillis();
		totalDuration = after - before;
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Les percepts auxquels l'IA a accès */
	private AiZone percepts;
	
	/**
	 * méthode implémentant le traitement effectué par l'IA sur les percepts,
	 * et renvoyant une action en réaction.
	 * 
	 * @return	
	 * 		action que l'IA a décider d'effectuer
	 * @throws StopRequestException	
	 * 		au cas où le moteur demande la terminaison de l'IA
	 */
	public abstract AiAction processAction() throws StopRequestException;

	/**
	 * Renvoie les percepts auxquels l'IA a accès
	 * @return	
	 * 		une AiZone représentant tous les percepts utilisables par l'IA
	 */
	public final AiZone getPercepts()
	{	return percepts;
	}
	/**
	 * méthode utilisée par le moteur du jeu pour initialiser les percepts de l'IA. 
	 * 
	 * @param percepts	
	 * 		l'objet représentant les percepts auxquels l'IA aura accès
	 */
	public final void setPercepts(AiZone percepts)
	{	this.percepts = percepts;
		output = new AiOutput(percepts);
	}

	/**
	 * Termine proprement l'IA afin de libérer les ressources qu'elle occupait.
	 */
	final void finish()
	{	percepts = null;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Sortie graphique de l'IA */
	private AiOutput output;
	
	/** 
	 * Renvoie la sortie graphique de l'IA, 
	 * afin d'afficher des informations par dessus la zone de jeu
	 * (utile lors du débogage). Le programme peut modifier cet objet
	 * pour colorer des cases et afficher des chemins ou du texte
	 * 
	 * @return	
	 * 		la sortie de l'IA
	 */
	public final AiOutput getOutput()
	{	return output;
	}

	/**
	 * réinitialise la sortie graphique de l'IA.
	 * méthode appelée automatiquement avant chaque itération de l'IA.
	 */
	private final void reinitOutput()
	{	output.reinit();
	}

	/////////////////////////////////////////////////////////////////
	// INITIALIZATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean initialized = false;
	/**
	 * méthode à surcharger s'il est nécessaire que l'agent soit
	 * initialisé. Toute opération définie dans cette fonction
	 * sera réalisée une fois, juste avant le début de la partie.
	 * A noter que les percepts ont néanmoins déjà été mis à jour.
	 * Si aucune méthode init n'est définie dans l'agent, alors celle
	 * ci sera exécutée (qui ne fait rien du tout).
	 */
	public void init() throws StopRequestException
	{	
	}
}
