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

import org.totalboumboum.ai.v201112.adapter.agent.handler.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.handler.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.agent.handler.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.agent.handler.AiUtilityHandler;
import org.totalboumboum.ai.v201112.adapter.communication.AiAction;
import org.totalboumboum.ai.v201112.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201112.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Chaque agent doit hériter de cette classe. La méthode {@link #processAction} est la méthode 
 * appelée par le gestionnaire d'agents pour obtenir la prochaine action 
 * à effectuer. Cette méthode implémente l'algorithme général spécifié en cours
 * et ne doit être ni modifiée ni surchargée.<br/>
 * L'algorithme général se décompose en différentes parties :
 * <ul>
 * 		<li>initialisation</li>
 * 		<li>mise à jour des percepts</li>
 * 		<li>calcul du mode</li>
 * 		<li>mise à jour des valeurs d'utilité</li>
 * 		<li>décision de poser une bombe ou pas</li>
 * 		<li>choix d'une direction de déplacement</li>
 * 		<li>mise à jour de la sortie graphique</li>
 * </ul>
 * Certaines de ces parties sont implémentées directement dans cette classe.<br/>
 * <br/>
 * Ainsi, l'initialisation de l'agent est réalisée grâce à la méthode {@link #init}.
 * Cette méthode se charge d'initialiser les percepts avec {@link #initPercepts} et
 * les gestionnaires avec {@link #initHandlers}. Les trois méthodes peuvent être
 * surchargées. Au moins les deux dernières doivent l'être, car par défaut {@code #initPercepts}
 * ne fait rien du tout, et {@code #initHandlers} crée des gestionnaires qui ne 
 * font rien du tout. A noter que rien n'empêche de surcharger une de ces méthodes 
 * de manière à en appeler d'autres. Bien sûr, toutes ces méthodes sont appelées
 * seulement une fois, juste avant le début de la partie (notez cependant que la zone est 
 * déjà disponible).<br/>
 * La mise à jour des percepts est également réalisée localement, grâce à la méthode
 * {@link #updatePercepts()}. Par défaut, elle ne fait rien du tout, il est donc
 * nécessaire de la surcharger.<br/>
 * Enfin, la méthode {@link #updateOutput} se charge de mettre à jour la sortie
 * graphique de l'agent. Elle doit donc être surchargée si on veut afficher des 
 * informations en cours de jeu : cases colorées, texte, chemins, etc. Sinon,
 * par défaut, elle n'affiche rien du tout.<br/>
 * <br/>
 * Le reste du comportement de l'agent est implémenté dans des gestionnaires spécialisés,
 * qui prennent la forme de classes spécifiques. Les gestionnaires de mode, d'utilité,
 * de posage de bombe et de déplacement doivent respectivement hériter des classes 
 * {@link AiModeHandler}, {@link AiUtilityHandler}, {@link AiBombHandler} et {@link AiMoveHandler}. 
 * Cf. leur documentation pour plus d'information.<br/>
 * <br/>
 * <b>Attention :</b> remarque très importante.
 * A la fin de la partie, le jeu demande à l'agent de s'arrêter. Dans certaines
 * conditions anormales, l'agent ne voudra pas s'arrêter (par exemple s'il est dans une
 * boucle infinie, ou bloquée dans un traitement récursif). Pour éviter ce 
 * genre de problème, <b>chaque</b> méthode définie dans l'agent doit :
 * <ul>	<li><b>Contenir à son tout début</b> un appel à la méthode {@link #checkInterruption}</li>
 *  	<li>faire suivre (mot-clé throw) les interruptions StopRequestException, 
 *  		et ne <b>surtout pas</b> les traiter localement (pas de {@code try}/{@code catch})</li>
 * </ul>
 * De plus, cette fonction doit également apparaître au début de chaque boucle
 * définie dans l'agent, qu'il s'agisse d'un {@code for}, d'un {@code while} 
 * ou d'un {@code do/while}.
 *  
 * @author Vincent Labatut
 */
public abstract class ArtificialIntelligence implements Callable<AiAction>
{	private boolean verbose = true;
	
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
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
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
	/** La zone de jeu à laquelle l'agent a accès (privée pour ne pas que l'agent puisse la changer) */
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
	protected void updatePercepts() throws StopRequestException
	{	
		// méthode à surcharger
	}
	
	/**
	 * Méthode permettant d'initialiser
	 * les percepts de l'agent, c'est-à-dire
	 * les différents objets stockés en interne
	 * dans ses classes.<br/>
	 * <b>Attention :</b> si cette méthode n'est pas redéfinie,
	 * alors rien ne se passe.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void initPercepts() throws StopRequestException
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
	protected void updateOutput() throws StopRequestException
	{	
		// méthode à surcharger
	}

	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** gestionnaire chargé du mode de l'agent */
	protected AiModeHandler modeHandler;
	/** gestionnaire chargé de calculer les valeurs d'utilité */
	protected AiUtilityHandler utilityHandler;
	/** gestionnaire chargé de l'action de poser une bombe */
	protected AiBombHandler bombHandler;
	/** gestionnaire chargé des déplacements de l'agent */
	protected AiMoveHandler moveHandler;
	
	/**
	 * Cette méthode a pour but d'initialiser les gestionnaires.<br/>
	 * Par défaut, elle crée des versions minimales des gestionnaires
	 * standards, qui ne font absolument rien. Il est donc
	 * nécessaire de la surcharger de manière à utiliser
	 * vos propres classes de gestionnaires.<br/>
	 * De plus, si votre agent utilise d'autres gestionnaires que
	 * les gestionnaires standards, il faut également rajouter leur
	 * initialisation.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void initHandlers() throws StopRequestException
	{	modeHandler = new AiModeHandler(this){};
		utilityHandler = new AiUtilityHandler(this){};
		bombHandler = new AiBombHandler(this){};
		moveHandler = new AiMoveHandler(this){};
	}
	
	/////////////////////////////////////////////////////////////////
	// INITIALIZATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indique si l'agent a déjà été initialisé ou pas */
	private boolean initialized = false;
	
	/**
	 * Cette méthode initialise l'agent.
	 * Toute opération qui y est définie
	 * sera réalisée une fois, juste avant le début de la partie.
	 * A noter que la zone est néanmoins déjà à jour.<br/>
	 * Par défaut, l'initialisation porte sur les gestionnaires
	 * avec {@link #initHandlers()} et sur les percepts
	 * avec {@link #initPercepts()}. Si d'autres initialisations
	 * sont nécessaires, il est possible de surcharger cette méthode.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void init() throws StopRequestException
	{	// initialisation des gestionnaires
		initHandlers();
		
		// initialisation des percepts
		initPercepts();
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
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public final AiAction processAction() throws StopRequestException
	{	checkInterruption();
verbose = zone.getOwnHero().getColor()==PredefinedColor.YELLOW;
		if(verbose) System.out.println(System.currentTimeMillis()+">>>>>>>>>>>>>>>>");
		if(verbose) System.out.println(zone);
		
		// mises à jour
		{	// mise à jour des percepts et données communes
			{	long before = System.currentTimeMillis();
				updatePercepts();
				long after = System.currentTimeMillis();
				long elapsed = after - before;
				if(verbose) System.out.println(before+"::updatePercepts: "+elapsed+" ms");
			}
			
			// mise à jour du mode de l'agent : ATTACKING ou COLLECTING
			{	long before = System.currentTimeMillis();
				modeHandler.update();
				long after = System.currentTimeMillis();
				long elapsed = after - before;
				if(verbose) System.out.println(before+"::updateMode: "+elapsed+" ms ["+modeHandler.mode+"]");
			}
			
			// mise à jour des valeurs d'utilité
			{	long before = System.currentTimeMillis();
				utilityHandler.update();
				long after = System.currentTimeMillis();
				long elapsed = after - before;
				if(verbose) System.out.println(before+"::updateUtility: "+elapsed+" ms");
			}
		}
		
		// action 
		// (note : les actions sont mutuellement exclusives, c'est soit l'une soit l'autre)
		AiAction result = null;
		{	long before = System.currentTimeMillis();
			boolean cb = bombHandler.considerBombing();
			long after = System.currentTimeMillis();
			long elapsed = after - before;
			if(verbose) System.out.println(before+"::considerBombing: "+elapsed+" ms ["+cb+"]");
			
			// on essaie de poser une bombe
			if(cb)
			{	result = new AiAction(AiActionName.DROP_BOMB);
			}

			// on essaie de se déplaccer
			else
			{	// on récupère la direction
				before = System.currentTimeMillis();
				Direction direction = moveHandler.considerMoving();
				after = System.currentTimeMillis();
				elapsed = after - before;
				if(verbose) System.out.println(before+"::considerMoving: "+elapsed+" ms ["+direction+"]");
				
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
		
		// mise à jour des sorties
		long before = System.currentTimeMillis();
		updateOutput();
		long after = System.currentTimeMillis();
		long elapsed = after - before;
		if(verbose) System.out.println(before+"::updateOutput: "+elapsed+" ms");
		
		// on renvoie l'action sélectionnée
		if(verbose) System.out.println(after+"<<<<<<<<<<<<<<<<");
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Termine proprement l'agent afin de libérer les ressources qu'il occupait.
	 */
	final void finish()
	{	// percepts
		zone = null;
		
		// handlers
		bombHandler = null;
		modeHandler = null;
		moveHandler = null;
		utilityHandler = null;
	}
}
