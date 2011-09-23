package org.totalboumboum.ai.v201112.adapter.agent;

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

import org.totalboumboum.ai.v201112.adapter.communication.AiAction;
import org.totalboumboum.ai.v201112.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201112.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * classe dont chaque agent doit hériter. La méthode processAction est la méthode 
 * appelée par le gestionnaire d'agents pour l'interroger que la prochaine action 
 * à effectuer.<br/>
 * <b>ATTENTION :</b> remarque très importante.
 * A la fin de la partie, le jeu demande à l'agent de s'arrêter. Dans certaines
 * conditions, l'agent ne voudra pas s'arrêter (par exemple si elle est dans une
 * boucle infinie, ou bloquée dans un traitement récursif). Pour éviter ce 
 * genre de problème, CHAQUE méthode définie dans l'agent doit :
 * <ul>	<li>CONTENIR A SON TOUT DEBUT un appel à la méthode {@link #checkInterruption}</li>
 *  	<li>faire suivre (mot-clé throw) les interruptions StopRequestException, 
 *  		et ne SURTOUT PAS les traiter (pas de try/catch)</li>
 * </ul>
 * De plus, cette fonction doit également apparaître au début de chaque boucle
 * définie dans l'agent, qu'il s'agisse d'un {@code for}, d'un {@code while} 
 * ou d'un {@code do/while}.
 *  
 * @author Vincent Labatut
 *
 */
public abstract class ArtificialIntelligence implements Callable<AiAction>
{	
	/////////////////////////////////////////////////////////////////
	// THREAD			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicateur de demande de terminaison de l'agent (activé par le jeu à la fin de la partie) */
	private boolean stopRequest = false;

	/**
	 * Méthode appelée par le jeu pour demander la fin de l'agent.
	 * Elle modifie la valeur de l'indicateur stopRequest, ce qui permettra
	 * de lever une {@link StopRequestException} au prochain appel 
	 * de la méthode {@link #checkInterruption}.
	 */
	public synchronized final void stopRequest()
	{	stopRequest = true;		
	}
	
	/**
	 * Méthode testant si le jeu a demandé la terminaison de l'agent.
	 * Si c'est le cas, une exception est levée, qui sera propagée jusqu'à call
	 * et forcera la terminaison de l'agent. Cette exception ne doit surtout pas être
	 * interceptée localement par un {@code try/catch}. 
	 */
	public synchronized final void checkInterruption() throws StopRequestException
	{	Thread.yield();
		if(stopRequest)
			throw new StopRequestException();
	}
	
	@Override
	public final AiAction call()
	{	AiAction result;
		// on réinitialise la sortie de l'agent
		reinitOutput();
		try
		{	// on initialise l'agent si besoin
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
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** La zone de jeu à laquelle l'agent a accès */
	private AiZone zone;
	
	/**
	 * Renvoie la zone à laquelle l'agent a accès.
	 * @return	
	 * 		une {@link AiZone} représentant tous les percepts utilisables par l'agent.
	 */
	public final AiZone getZone()
	{	return zone;
	}
	/**
	 * méthode utilisée par le moteur du jeu 
	 * pour initialiser la zone de l'agent. 
	 * 
	 * @param zone	
	 * 		l'objet représentant la zone à laquelle l'agent aura accès
	 */
	public final void setZone(AiZone zone)
	{	this.zone = zone;
		output = new AiOutput(zone);
	}
	
	/**
	 * Méthode permettant de mettre à jour
	 * les percepts de l'agent, c'est-à-dire
	 * les différents objets stockés en interne
	 * dans ses classes.<br/>
	 * <b>Attention :</b> si cette méthode n'est pas redéfinie,
	 * alors rien ne se passe.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public void updatePercepts() throws StopRequestException
	{	
		// méthode à surcharger
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Sortie graphique de l'agent */
	private AiOutput output;
	
	/** 
	 * Renvoie la sortie graphique de l'agent, 
	 * afin d'afficher des informations par dessus la zone de jeu
	 * (utile lors du débogage). Le programme peut modifier cet objet
	 * pour colorer des cases et afficher des chemins ou du texte
	 * 
	 * @return	
	 * 		la sortie de l'agent
	 */
	public final AiOutput getOutput()
	{	return output;
	}

	/**
	 * réinitialise la sortie graphique de l'agent.
	 * méthode appelée automatiquement avant chaque itération de l'agent.
	 */
	private final void reinitOutput()
	{	output.reinit();
	}

	/**
	 * Méthode permettant de mettre à jour
	 * les sorties graphiques de l'agent.<br/>
	 * <b>Attention :</b> si cette méthode n'est pas redéfinie,
	 * alors aucune sortie graphique n'est définie.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public void updateOutput() throws StopRequestException
	{	
		// méthode à surcharger
	}

	/////////////////////////////////////////////////////////////////
	// INITIALIZATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean initialized = false;
	/**
	 * méthode à surcharger s'il est nécessaire que l'agent soit
	 * initagentlisé. Toute opération définie dans cette fonction
	 * sera réalisée une fois, juste avant le début de la partie.
	 * A noter que les percepts ont néanmoins déjà été mis à jour.
	 */
	public void init() throws StopRequestException
	{	
	}

	/////////////////////////////////////////////////////////////////
	// MODE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le mode courant de l'agent */
	protected AiMode mode = AiMode.COLLECTING;
	
	/**
	 * Renvoie le mode courant de cet agent,
	 * i.e. soit {@link AiMode#ATTACKING}, soit
	 * {@link AiMode#COLLECTING}.
	 * 
	 * @return
	 * 		Le mode courant de cet agent.
	 */
	public final AiMode getMode()
	{	return mode;
	}

	/**
	 * Modifie le mode courant de cet agent,
	 * i.e. soit {@link AiMode#ATTACKING}, soit
	 * {@link AiMode#COLLECTING}.
	 * 
	 * @param mode
	 * 		Le nouveau mode de cet agent.
	 */
	public final void setMode(AiMode mode)
	{	this.mode = mode;
	}

	/**
	 * Méthode permettant de mettre à jour
	 * le mode de l'agent : {@link AiMode#ATTACKING}
	 * ou {@link AiMode#COLLECTING}.<br/>
	 * <b>Attention :</b> si cette méthode n'est pas redéfinie,
	 * alors la valeur {@link AiMode#COLLECTING} est
	 * systématiquement utilisée.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public void updateMode() throws StopRequestException
	{	mode = AiMode.COLLECTING;
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Méthode implémentant le traitement effectué par l'agent sur la zone,
	 * et renvoyant une action en réaction. Cette méthode implémente
	 * l'algorithme général mis au point lors du projet 2010-11.
	 * 
	 * @return	
	 * 		Action que l'agent a décidé d'effectuer.
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public final AiAction processAction() throws StopRequestException
	{	checkInterruption();
		
		// mises à jour
		{	// mise à jour des percepts et données communes
			updatePercepts();
			
			// mise à jour du mode de l'agent : ATTACKING ou COLLECTING
			updateMode();
			
			// mise à jour des valeurs d'utilité
			updateUtility();
			
			// mise à jour des sorties
			updateOutput();
		}
		
		// action 
		// (note : les actions sont mutuellement exclusives, c'est soit l'une soit l'autre)
		AiAction result = null;
		{	// on essaie de poser une bombe
			if(considerBombing())
			{	result = new AiAction(AiActionName.DROP_BOMB);
			}
			
			// on essaie de se déplaccer
			else
			{	// on récupère la direction
				Direction direction = considerMoving();
				// si pas de direction : on suppose que c'est NONE
				if(direction==null)	
					direction = Direction.NONE;
				// on construit l'action en fonction de la direction
				if(direction==Direction.NONE)
					result = new AiAction(AiActionName.NONE);
				else
					result = new AiAction(AiActionName.MOVE,direction);
			}
		}
		
		// on renvoie l'action sélectionnée
		return result;
	}
	
	/**
	 * Méthode permettant de déterminer si l'agent
	 * doit poser une bombe ou pas. Cette décision
	 * dépend des valeurs d'utilité courantes.<br/>
	 * La méthode renvoie un booléen {@code true}
	 * si l'agent doit poser une bombe, et
	 * {@code false} sinon.<br/>
	 * <b>Attention :</b> si cette méthode n'est pas redéfinie,
	 * alors la valeur {@code false} est systématiquement
	 * renvoyée (i.e. : pas de bombe posée).
	 * 
	 * @return
	 * 		Renvoie {@code true} ssi l'agent doit poser une bombe.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public boolean considerBombing() throws StopRequestException
	{	return false;
	}

	/**
	 * Méthode permettant de déterminer si l'agent
	 * doit se déplacer, et dans quelle direction.
	 * Cette décision dépend des valeurs d'utilité courantes.<br/>
	 * La méthode renvoie une {@link Direction} indiquant le
	 * sens du déplacement, ou bien {@code null} ou {@link Direction#NONE}
	 * si aucun déplacement ne doit être effectué.<br/>
	 * <b>Attention :</b> si cette méthode n'est pas redéfinie,
	 * alors la valeur {@link Direction#NONE} est systématiquement
	 * renvoyée (i.e. : pas de déplacement).
	 * 
	 * @return
	 * 		Renvoie une direction indiquant le sens (ou l'absence) de déplacement de l'agent.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Direction considerMoving() throws StopRequestException
	{	return Direction.NONE;
	}

	/////////////////////////////////////////////////////////////////
	// UTILITY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Méthode permettant de mettre à jour
	 * les valeurs d'utiltié de l'agent. Il s'agit
	 * généralement d'une matrice numérique, mais
	 * rien n'est obligatoire. Le calcul de ces valeurs
	 * est fonction de la zone, mais aussi du mode
	 * courant de l'agent.<br/>
	 * <b>Attention :</b> si cette méthode n'est pas redéfinie,
	 * alors rien ne se passe.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public void updateUtility() throws StopRequestException
	{	
		// méthode à surcharger
	}
	
	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Termine proprement l'agent afin de libérer les ressources qu'il occupait.
	 */
	final void finish()
	{	zone = null;
	}
}
