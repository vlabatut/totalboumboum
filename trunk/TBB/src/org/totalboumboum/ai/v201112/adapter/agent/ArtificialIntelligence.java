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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import org.totalboumboum.ai.v201112.adapter.communication.AiAction;
import org.totalboumboum.ai.v201112.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201112.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Chaque agent doit hériter de cette classe. La méthode {@link #processAction} est la méthode 
 * appelée par le gestionnaire d'agents pour obtenir la prochaine action 
 * à effectuer. Cette méthode implémente l'algorithme général spécifié en cours
 * et ne doit être ni modifiée ni surchargée.
 * <br/>
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
 * Certaines de ces parties sont implémentées directement dans cette classe.
 * <br/>
 * <br/>
 * Ainsi, l'initialisation de l'agent est réalisée grâce à la méthode {@link #init}.
 * Cette méthode se charge d'initialiser les percepts avec {@link #initPercepts} et
 * les gestionnaires avec {@link #initHandlers}. Les trois méthodes peuvent être
 * surchargées. Au moins les deux dernières doivent l'être, car par défaut {@code #initPercepts}
 * ne fait rien du tout, et {@code #initHandlers} crée des gestionnaires qui ne 
 * font rien du tout. A noter que rien n'empêche de surcharger une de ces méthodes 
 * de manière à en appeler d'autres. Bien sûr, toutes ces méthodes sont appelées
 * seulement une fois, juste avant le début de la partie (notez cependant que la zone est 
 * déjà disponible).
 * <br/>
 * La mise à jour des percepts est également réalisée localement, grâce à la méthode
 * {@link #updatePercepts()}. Par défaut, elle ne fait rien du tout, il est donc
 * nécessaire de la surcharger.
 * <br/>
 * Enfin, la méthode {@link #updateOutput} se charge de mettre à jour la sortie
 * graphique de l'agent. Elle doit donc être surchargée si on veut afficher des 
 * informations en cours de jeu : cases colorées, texte, chemins, etc. Sinon,
 * par défaut, elle n'affiche rien du tout.
 * <br/>
 * <br/>
 * Le reste du comportement de l'agent est implémenté dans des gestionnaires spécialisés,
 * qui prennent la forme de classes spécifiques. Les gestionnaires de mode, d'utilité,
 * de posage de bombe et de déplacement doivent respectivement hériter des classes 
 * {@link AiModeHandler}, {@link AiUtilityHandler}, {@link AiBombHandler} et {@link AiMoveHandler}. 
 * Cf. leur documentation pour plus d'information.
 * <br/>
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
	 * <br/>
	 * Cette méthode est réservée au moteur du jeu.
	 */
	public synchronized final void stopRequest()
	{	stopRequest = true;		
	}
	
	/**
	 * Méthode testant si le jeu a demandé la terminaison de l'agent.
	 * Si c'est le cas, une exception est levée, qui sera propagée jusqu'à {@link #call()}
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
		resetOutput();
		try
		{	// on initialise l'agent si besoin
			if(!initialized)
			{	initialized = true;
				colorStr = zone.getOwnHero().getColor().toString();
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
	 * Méthode utilisée par le moteur du jeu 
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
	 * dans ses classes.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected abstract void updatePercepts() throws StopRequestException;
	
	/**
	 * Méthode permettant d'initialiser
	 * les percepts de l'agent, c'est-à-dire
	 * les différents objets stockés en interne
	 * dans ses classes.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected abstract void initPercepts() throws StopRequestException;

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
	private final void resetOutput()
	{	output.reinit();
	}

	/**
	 * Méthode permettant de mettre à jour
	 * les sorties graphiques de l'agent.
	 * <br/>
	 * <b>Attention :</b> si cette méthode n'est pas redéfinie,
	 * alors la sortie graphique par défaut consiste à 
	 * afficher le chemin et la destination courants,
	 * ainsi que les valeurs d'utilité courantes.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void updateOutput() throws StopRequestException
	{	// affiche les chemins et destinations courants
		getMoveHandler().updateOutput();
		
		// affiche les utilités courantes
		getUtilityHandler().updateOutput();
	}

	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Cette méthode a pour but d'initialiser les gestionnaires.
	 * Elle doit obligatoirement être surchargée.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected abstract void initHandlers() throws StopRequestException;

	/**
	 * Renvoie le gestionnaire de mode de cet agent.
	 * Il doit avoir d'abord été créé dans la méthode
	 * {@link #initHandlers()}.
	 * 
	 * @return
	 * 		Le gestionnaire de mode de cet agent.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected abstract AiModeHandler<?> getModeHandler() throws StopRequestException;
	
	/**
	 * Renvoie le gestionnaire d'utilité de cet agent.
	 * Il doit avoir d'abord été créé dans la méthode
	 * {@link #initHandlers()}.
	 * 
	 * @return
	 * 		Le gestionnaire d'utilité de cet agent.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected abstract AiUtilityHandler<?> getUtilityHandler() throws StopRequestException;
	
	/**
	 * Renvoie le gestionnaire de posage de bombe de cet agent.
	 * Il doit avoir d'abord été créé dans la méthode
	 * {@link #initHandlers()}.
	 * 
	 * @return
	 * 		Le gestionnaire de posage de bombe de cet agent.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected abstract AiBombHandler<?> getBombHandler() throws StopRequestException;
	
	/**
	 * Renvoie le gestionnaire de déplacement de cet agent.
	 * Il doit avoir d'abord été créé dans la méthode
	 * {@link #initHandlers()}.
	 * 
	 * @return
	 * 		Le gestionnaire de déplacement de cet agent.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected abstract AiMoveHandler<?> getMoveHandler() throws StopRequestException;
	
	/////////////////////////////////////////////////////////////////
	// INITIALIZATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indique si l'agent a déjà été initialisé ou pas */
	private boolean initialized = false;
	
	/**
	 * Cette méthode initialise l'agent.
	 * Toute opération qui y est définie
	 * sera réalisée une fois, juste avant le début de la partie.
	 * A noter que la zone est néanmoins déjà à jour.
	 * <br/>
	 * Par défaut, l'initialisation porte sur les percepts
	 * avec {@link #initPercepts()} et sur les gestionnaires
	 * avec {@link #initHandlers()}. Si d'autres initialisations
	 * sont nécessaires, il est possible de surcharger cette méthode.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void init() throws StopRequestException
	{	// initialisation des percepts
		initPercepts();
		
		// initialisation des gestionnaires
		initHandlers();
    	// on affiche éventuellement les utilités
		getUtilityHandler().displayUtilities();
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
		resetDurations();
		long before0 = print(">> Entering processAction ----------------------");
		print("\n"+zone);
		
		// mises à jour
		{	// mise à jour des percepts et données communes
			{	long before = print("  >> Entering updatePercepts");
				updatePercepts();
				long after = System.currentTimeMillis();
				long elapsed = after - before;
				print("  << Exiting updatePercepts: duration="+elapsed+" ms");
				stepDurations.put(PERCEPTS,elapsed);
			}
			
			// mise à jour du mode de l'agent : ATTACKING ou COLLECTING
			{	long before = print("  >> Entering updateMode");
				getModeHandler().update();
				long after = System.currentTimeMillis();
				long elapsed = after - before;
				print("  << Exiting updateMode: duration="+elapsed+" ms result="+getModeHandler().mode);
				stepDurations.put(MODE,elapsed);
			}
			
			// mise à jour des valeurs d'utilité
			{	long before = print("  >> Entering updateUtility");
				getUtilityHandler().update();
				long after = System.currentTimeMillis();
				long elapsed = after - before;
				print("  << Exiting updateUtility: duration="+elapsed+" ms");
				stepDurations.put(UTILITY,elapsed);
			}
		}
		
		// action 
		// (note : les actions sont mutuellement exclusives, c'est soit l'une soit l'autre)
		AiAction result = null;
		{	long before = print("  >> Entering considerBombing");
			boolean cb = getBombHandler().considerBombing();
			long after = System.currentTimeMillis();
			long elapsed = after - before;
			print("  << Exiting considerBombing: duration="+elapsed+" ms result="+cb);
			stepDurations.put(BOMB,elapsed);
			
			// on essaie de poser une bombe
			if(cb)
			{	result = new AiAction(AiActionName.DROP_BOMB);
			}

			// on essaie de se déplaccer
			else
			{	// on récupère la direction
				before = print("  >> Entering considerMoving");
				Direction direction = getMoveHandler().considerMoving();
				after = System.currentTimeMillis();
				elapsed = after - before;
				print("  << Exiting considerMoving: duration="+elapsed+" ms result="+direction);
				stepDurations.put(MOVE,elapsed);
				
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
		{	long before = print("  >> Entering updateOutput");
			updateOutput();
			long after = System.currentTimeMillis();
			long elapsed = after - before;
			print("  << Exiting updateOutput: duration="+elapsed+" ms");
			stepDurations.put(OUTPUT,elapsed);
		}
		
		// on renvoie l'action sélectionnée
		long after0 = System.currentTimeMillis();
		long elapsed0 = after0 - before0;
		print("<< Exiting processAction duration="+elapsed0+" ----------------------");
		totalDuration = elapsed0;
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Champ utilisé par le moteur : étape de mise à jour des percepts */
	protected final String PERCEPTS = "Percepts";
	/** Champ utilisé par le moteur : étape de mise à jour des percepts */
	protected final String MODE = "Mode";
	/** Champ utilisé par le moteur : étape de mise à jour des percepts */
	protected final String UTILITY = "Utility";
	/** Champ utilisé par le moteur : étape de mise à jour des percepts */
	protected final String BOMB = "Bomb";
	/** Champ utilisé par le moteur : étape de mise à jour des percepts */
	protected final String MOVE = "Move";
	/** Champ utilisé par le moteur : étape de mise à jour des percepts */
	protected final String OUTPUT = "Output";
	/** Champ utilisé par le moteur : les différentes étapes du traitement */
	protected final List<String> stepNames = new ArrayList<String>(Arrays.asList(PERCEPTS,MODE,UTILITY,BOMB,MOVE,OUTPUT));
	/** Champ utilisé par le moteur : temps réel total utilisé lors du dernier appel (en ms) */ 
	private long totalDuration;
	/** Champ utilisé par le moteur : temps réel de chaque étape (en ms) */
	private final HashMap<String,Long> stepDurations = new HashMap<String,Long>();
	
	/**
	 * Réinitialise tous les temps réels
	 * avant de démarrer le traitement.
	 */
	private final void resetDurations()
	{	totalDuration = 0;
		stepDurations.clear();
		for(String step: stepNames)
			stepDurations.put(step,0l);
	}
	
	/**
	 * Renvoie le temps réel écoulé lors
	 * du dernier appel de cet agent,
	 * exprimé en ms.
	 * <br/>
	 * Cette méthode est destinée au moteur
	 * du jeu. Pour l'agent, il vaut mieux
	 * utiliser la méthode {@link AiZone#getTotalTime()}.
	 * 
	 * @return
	 * 		Temps réel total en ms.
	 */
	public final long getTotalDuration()
	{	return totalDuration;
	}

	/**
	 * Renvoie une map contenant 
	 * le temps réel écoulé pour
	 * chacune des étapes de cet
	 * agent, lors du dernier appel,
	 * exprimé en ms.
	 * <br/>
	 * Cette méthode est destinée au moteur
	 * du jeu. Pour l'agent, il vaut mieux
	 * utiliser la méthode {@link AiZone#getTotalTime()}.
	 * 
	 * @return
	 * 		Temps réel de chaque étape, en ms.
	 */
	public final HashMap<String,Long> getStepDurations()
	{	return stepDurations;
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Préfixe affiché avant chaque message */
	protected String colorStr;
	/** Indique si l'agent doit utiliser la sortie texte (pour le débogage) */
	protected boolean verbose = false;
	
	/**
	 * Cette méthode affiche à l'écran le message passé en paramètre,
	 * à condition que {@link #verbose} soit {@code true}. Elle
	 * préfixe automatiquement la couleur de l'agent et le moment
	 * de l'affichage. Utilisez-la pour tout affichage de message,
	 * car elle vous permet de désactiver tous vos messages simplement
	 * en faisant {@code verbose = false;}.
	 * 
	 * @param msg
	 * 		Le message à afficher dans la console.
	 * @return
	 * 		Le temps à l'instant de l'affichage.
	 */
	protected final long print(String msg)
	{	long time = System.currentTimeMillis();
		if(verbose)
		{	String prefix = "[" + time + ":" + colorStr + "]";
			String message = prefix + " " + msg;
			System.out.println(message);
		}
		return time;
	}
	
	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Termine proprement l'agent afin de libérer les ressources 
	 * qu'il occupait. Cette mméthode destinée au moteur.
	 */
	final void finish()
	{	zone = null;
	}
}
