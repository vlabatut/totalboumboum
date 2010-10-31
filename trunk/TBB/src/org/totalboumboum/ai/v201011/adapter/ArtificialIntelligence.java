package org.totalboumboum.ai.v201011.adapter;

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

import java.util.concurrent.Callable;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.representation.AiDataZone;

/**
 * classe dont chaque IA doit h�riter. La m�thode processAction est la m�thode 
 * appel�e par le gestionnaire d'IA pour l'interroger que la prochaine action 
 * � effectuer.
 * <p>
 * <b>ATTENTION :</b> remarque tr�s importante.
 * A la fin de la partie, le jeu demande � l'IA de s'arr�ter. Dans certaines
 * conditions, l'IA ne voudra pas s'arr�ter (par exemple si elle est dans une
 * boucle infinie, ou bloqu�e dans un traitement r�cursif). Pour �viter ce 
 * genre de probl�me, CHAQUE m�thode d�finie dans l'IA doit :
 * 	- CONTENIR A SON TOUT DEBUT un appel � la m�thode checkInterruption()
 *  - faire suivre (mot-cl� throw) les interruptions StopRequestException, et ne SURTOUT PAS les traiter (pas de try/catch)
 * De plus, cette fonction doit �galement appara�tre au d�but de chaque boucle
 * d�finie dans l'IA, qu'il s'agisse d'un for, d'un while ou d'un do/while.
 *  
 * @author Vincent Labatut
 *
 */
public abstract class ArtificialIntelligence implements Callable<AiAction>
{	
	/////////////////////////////////////////////////////////////////
	// THREAD			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indicateur de demande de terminaison de l'IA (activ� par le jeu � la fin de la partie) */
	private boolean stopRequest = false;

	/**
	 * m�thode appel�e par le jeu pour demander la fin de l'IA.
	 * Elle modifie la valeur de l'indcateur stopRequest, ce qui permettra
	 * de lever une StopRequestException au prochain appel de la m�thode checkInterruption.
	 */
	public synchronized final void stopRequest()
	{	stopRequest = true;		
	}
	
	/**
	 * m�thode testant si le jeu a demand� la terminaison de l'IA.
	 * Si c'est le cas, une exception est lev�e, qui sera propag�e jusqu'� call
	 * et forcera la terminaison de l'IA. Cette exception ne doit surtout pas �tre
	 * intercept�e localement par un try/catch. 
	 */
	public synchronized final void checkInterruption() throws StopRequestException
	{	Thread.yield();
		if(stopRequest)
			throw new StopRequestException();
	}
	
	@Override
	public final AiAction call()
	{	AiAction result;
		// on r�initialise la sortie de l'IA
		reinitOutput();
		try
		{	// on calcule la prochaine action � effectuer
			result = processAction();		
		}
		catch (StopRequestException e)
		{	result = new AiAction(AiActionName.NONE);
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** les percepts auxquels l'IA a acc�s */
	private AiDataZone percepts;
	
	/**
	 * m�thode impl�mentant le traitement effectu� par l'IA sur les percepts,
	 * et renvoyant une action en r�action.
	 * 
	 * @return	action que l'IA a d�cider d'effectuer
	 * @throws StopRequestException	au cas o� le moteur demande la terminaison de l'IA
	 */
	public abstract AiAction processAction() throws StopRequestException;

	/**
	 * renvoie les percepts auxquels l'IA a acc�s
	 * @return	une AiZone repr�sentant tous les percepts utilisables par l'IA
	 */
	public final AiDataZone getPercepts()
	{	return percepts;
	}
	/**
	 * m�thode utilis�e par le moteur du jeu pour initialiser les percepts de l'IA. 
	 * 
	 * @param percepts	l'objet repr�sentant les percepts auxquels l'IA aura acc�s
	 */
	public final void setPercepts(AiDataZone percepts)
	{	this.percepts = percepts;
		output = new AiOutput(percepts);
	}

	/**
	 * termine proprement l'IA afin de lib�rer les ressources qu'elle occupait.
	 */
	final void finish()
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
	public final AiOutput getOutput()
	{	return output;
	}

	/**
	 * r�initialise la sortie graphique de l'IA.
	 * M�thode appel�e automatiquement avant chaque it�ration de l'IA.
	 */
	private final void reinitOutput()
	{	output.reinit();
	}
}
