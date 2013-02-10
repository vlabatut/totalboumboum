package org.totalboumboum.ai;

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

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.ai.AisConfiguration;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.event.ControlEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.AiPlayer;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.RoundVariables;
import org.xml.sax.SAXException;


/**
 * 
 * Classe servant d'interface entre le jeu et un agent.
 * Elle doit être surclassée de manière à obtenir un adaptateur pour une famille
 * d'agent donnée. Puis, chaque agent doit lui-même surclasser la classe résultante
 * (tout ça dans le but de faciliter le chargement de la classe implémentant l'agent).
 * 
 * @author Vincent Labatut
 *
 * @param <V>	
 * 		le type de donnée renvoyée par l'agent (et devant être traduite par l'adaptateur en un évènement compatible avec le moteur du jeu)
 */

public abstract class AiAbstractManager<V>
{	/**
     * contruit un nouveau manager pour l'agent passé en paramètre.
     * Ce constructeur est destiné à être appelé par le constructeur situé dans 
     * la classe héritant de celle-ci et située dans le dossier de l'agent
     *    
     * @param 
     * 		ai
     */
	public AiAbstractManager(Callable<V> ai)
    {	this.ai = ai;
//TODO ai.loadUtilities(); ?    	
	}

    /**
     * Initialise ce gestionnaire d'agent.
     * 
     * @param instance	
     * 		Instance utilisée dans ce round.
     * @param player	
     * 		Joueur contrôlé par l'agent.
     * 
     * @throws NoSuchMethodException
     * 		Problème lors du chargement des préférences de l'agent. 
     * @throws ClassNotFoundException 
     * 		Problème lors du chargement des préférences de l'agent. 
     * @throws IllegalAccessException 
     * 		Problème lors du chargement des préférences de l'agent. 
     * @throws InstantiationException 
     * 		Problème lors du chargement des préférences de l'agent. 
     * @throws InvocationTargetException 
     * 		Problème lors du chargement des préférences de l'agent. 
     * @throws ParserConfigurationException 
     * 		Problème lors du chargement des préférences de l'agent. 
     * @throws SAXException 
     * 		Problème lors du chargement des préférences de l'agent. 
     * @throws IOException 
     * 		Problème lors du chargement des préférences de l'agent. 
     */
    @SuppressWarnings("unchecked")
	public void init(String instance, AiPlayer player) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, ParserConfigurationException, SAXException, IOException
	{	// input
    	this.player = player;
		
    	// output
		Level level = RoundVariables.level;
		int height = level.getGlobalHeight();
		int width = level.getGlobalWidth();
		tileColors = new List[height][width];
		tileTexts = new List[height][width];
		for(int row=0;row<height;row++)
		{	for(int col=0;col<width;col++)
			{	tileColors[row][col] = new ArrayList<Color>();
				tileTexts[row][col] = new ArrayList<String>();
			}
		}
		initSteps();
	}
		
	/////////////////////////////////////////////////////////////////
	// EXCEPTIONS LOG	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Objet permettant d'accéder à la configuration des agents dans le jeu */
    private AisConfiguration aisConfiguration = Configuration.getAisConfiguration();
	
	/////////////////////////////////////////////////////////////////
	// THREAD			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /** Objet implementant le comportement de l'agent */
	private Callable<V> ai;
    /** Gestionnaire de threads pour exécuter l'agent */
    private ExecutorService executorAi = null;
    /** Future utilisé pour récupérer le résultat de l'agent */
    private Future<V> futureAi = null;
    /** Indique si cet agent était en pause */
    private boolean paused = false;
    
    /**
     * Renvoie l'agent géré par cette classe.
     * 
     * @return	
     * 		L'agent géré par cette classe sous la forme d'un Callable
     */
    public final Callable<V> getAi()
    {	return ai;    	
    }
    
    /**
     * Initialise le thread de l'agent.
     */
    public void initAgent()
    {	// on initialise le thread
    	Profile profile = player.getProfile();
    	String name = player.getColor()+":"+profile.getAiPackname()+"/"+profile.getAiName()+":"+player.getName();
    	ThreadFactory fact = new AiThreadFactory(name);
    	executorAi = Executors.newSingleThreadExecutor(fact);
    	
    	// on lance un calcul bidon pour initialiser le thread
    	makeCall();
    }
    
    /**
     * Utilise la classe d'agent associé à ce personnage pour mettre à jour les variables
     * qui permettront au moteur du jeu de déplacer le personnage.
     * 
     * @param aisPause
     * 		indique si l'agent doit passer en pause
     * @return
     * 		vrai si l'agent a effectué une action
     */
    public final boolean update(boolean aisPause)
    {	boolean result = false;
    	
    	// on lance le calcul pour le premier coup
//    	if(futureAi==null)
//       	makeCall();
    	
//    	else
    	{	// si l'agent était en pause
	    	if(paused)
	    	{	// sortie de pause ?
	    		if(!aisPause)
		    	{	// basculement de la pause
	    			paused = false;
	   				// on lance le calcul pour le prochain coup
					makeCall();	    		
		    	}    		
	    	}
	    	
	    	// sinon : un appel avait déjà été effectué
	    	else
	    	{	// passage en pause ?
	    		if(aisPause)
	    		{	// basculement de la pause
	    			paused = true;
	    			// on arrête le joueur
	    			ControlEvent[] events =
	    			{	new ControlEvent(ControlEvent.UP,false),
						new ControlEvent(ControlEvent.RIGHT,false),
						new ControlEvent(ControlEvent.DOWN,false),
						new ControlEvent(ControlEvent.LEFT,false),
						new ControlEvent(ControlEvent.DROPBOMB,false),
						new ControlEvent(ControlEvent.JUMP,false),
						new ControlEvent(ControlEvent.PUNCHBOMB,false),
						new ControlEvent(ControlEvent.STOPBOMB,false),
						new ControlEvent(ControlEvent.THROWBOMB,false),
						new ControlEvent(ControlEvent.TRIGGERBOMB,false)
	    			};
	    			for(ControlEvent event: events)
						player.getSprite().putControlEvent(event);
	    		}
	    		// pas de passage en pause : cet appel est-il fini ?
	    		else if(futureAi.isDone())
	    		{	try
	    			{	// on récupére les réactions de l'agent
	    				V value = futureAi.get();
	    				// on les convertit et les envoie au moteur
	    				List<ControlEvent> events = convertReaction(value);
	    				Iterator<ControlEvent> it = events.iterator();
	    				while(it.hasNext())
	    				{	ControlEvent event = it.next();
	    					player.getSprite().putControlEvent(event);
	    				}
	    				result = !events.isEmpty();
	    				// on met à jour les sorties de l'agent
	    				updateOutput();
	    				// on met à jour les temps de l'agent
	    				updateDurations();
					}
	    			catch (InterruptedException e)
	    			{	if(aisConfiguration.getLogExceptions())
	    				{	OutputStream out = aisConfiguration.getExceptionsLogOutput();
		    				PrintWriter printWriter = new PrintWriter(out,true);
		    				e.printStackTrace(printWriter);
	    				}
	    				if(Configuration.getAisConfiguration().getDisplayExceptions())
	    					e.printStackTrace();
					}
	    			catch (ExecutionException e)
	    			{	if(aisConfiguration.getLogExceptions())
						{	OutputStream out = aisConfiguration.getExceptionsLogOutput();
							PrintWriter printWriter = new PrintWriter(out,true);
							e.printStackTrace(printWriter);
						}
						if(Configuration.getAisConfiguration().getDisplayExceptions())
	    					e.printStackTrace();
					}    			
	    			// on lance le calcul pour le prochain coup
	    			makeCall();
	    		}
	    		// sinon on ne fait rien
	    		else
	    		{	//
	    		}
	    	}
    	}
    	
    	return result;
    }
    
    /**
     * Réalise l'appel à la classe qui implémente l'agent,
     * afin que celui-ci calcule la prochaine action à effectuer.
     */
    private final void makeCall()
    {	
//    	System.out.println(player.getColor()+":"+GameVariables.loop.getTotalTime());    	
    	updatePercepts();
     	futureAi = executorAi.submit(ai);
    }
    
    /**
     * terminer ce gestionnaire, et en particulier le thread exécutant l'agent.
     * Ou plutôt tente de le terminer, car le résultat ne peut être forcé.
     */
    public final void finish()
    {	finishAi();
		
    	boolean result = futureAi.isDone(); 
    	if(!result) 
    		result = futureAi.cancel(true);
		/*List<Runnable> list = */executorAi.shutdownNow();
    	
		finishPercepts();
    	
		ai = null;
    	executorAi = null;
    	futureAi = null;
    	player = null;
    }
    
    /**
     * termine cet agent, et en particulier le processus qui l'exécute.
     * Pour cette raison, l'agent doit implémenter une méthode forçant 
     * sa terminaison.
     */
    public abstract void finishAi();

    /////////////////////////////////////////////////////////////////
	// PLAYER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /** le joueur contrôlé par l'agent */
    private AiPlayer player;
   
	/**
	 * renvoie le joueur contrôlé par l'agent géré
	 * 
	 * @return	
	 * 		un objet représentant le joueur contrôlé par l'agent
	 */
    public AbstractPlayer getPlayer()
	{	return player;		
	}
	
    /////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * méthode utilisée pour mettre à jour les percepts de l'agent avant 
	 * que cette dernière ne calcule la prochaine action à effectuer.
	 * Cette méthode doit être surchargée de manière à adapter la structure
	 * des données à l'agent qui va les traiter
	 */
	public abstract void updatePercepts();
	
	/**
	 * méthode utilisée pour convertir la valeur renvoyée par l'agent 
	 * en un évènement standard traitable par le moteur du jeu.
	 * 
	 * @param value	
	 * 		la valeur renvoyée par l'agent, qui est à convertir
	 * @return	
	 * 		le résultat de la conversion sous la forme d'un évènement à envoyer au sprite contrôlé par l'agent
	 */
	public abstract List<ControlEvent> convertReaction(V value);
   
	/**
	 * termine proprement les percepts, de manière à libérer les ressources occupées.
	 * Cette méthode est appelée lorsque la partie est terminée et que les
	 * percepts deviennent inutiles.
	 */
	public abstract void finishPercepts();
	
    /////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Noms des différentes étapes implémentées par l'agent */
	private final List<String> stepNames = new LinkedList<String>();
	/** Couleurs associées aux différentes étapes implémentées par l'agent, pour l'affichage */
	private final HashMap<String,Color> stepColors = new HashMap<String, Color>();
	/** Durées instantannées associées aux différentes étapes implémentées par l'agent, pour le moteur */
	private final HashMap<String,LinkedList<Long>> stepInstantDurations = new HashMap<String, LinkedList<Long>>();
	/** Durées moyennes associées aux différentes étapes implémentées par l'agent, pour le moteur */
	private final HashMap<String,Float> stepAverageDurations = new HashMap<String, Float>();
	/** Name of the fake step corresponding to the total duration */
	public final String TOTAL_DURATION = "Total";
	/** Nombre de valeurs considérées pour la moyenne flottante */
	protected final int AVERAGE_SCOPE = 10;
	
	/** 
	 * initialise la liste des noms des étapes 
	 * implémentées par l'agent 
	 */
	protected abstract void initSteps();
	
	/**
	 * Met à jour la map contenant
	 * les temps destinés au moteur.
	 */
	public abstract void updateDurations();
	
	/**
	 * Renvoie les noms des différentes
	 * étapes implémentées par l'agent.
	 * 
	 * @return
	 * 		Une liste de noms d'étapes.
	 */
	public List<String> getStepNames()
	{	return stepNames;
	}
	
	/**
	 * Renvoie les dernières durées instantannées associées
	 * aux étapes implémentées par l'agent.
	 * 
	 * @return
	 * 		Une map associant une durée à chaque nom d'étape.
	 */
	public HashMap<String,LinkedList<Long>> getInstantDurations()
	{	return stepInstantDurations;
	}
	
	/**
	 * Renvoie les dernières durées moyennes associées
	 * aux étapes implémentées par l'agent.
	 * 
	 * @return
	 * 		Une map associant une durée à chaque nom d'étape.
	 */
	public HashMap<String,Float> getAverageDurations()
	{	return stepAverageDurations;
	}
	
	/**
	 * Renvoie les couleurs associées
	 * aux étapes implémentées par l'agent.
	 * 
	 * @return
	 * 		Une map associant une couleur à chaque nom d'étape.
	 */
	public HashMap<String,Color> getStepColors()
	{	return stepColors;
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** couleurs associées aux cases (ou null pour aucune couleur */
	private List<Color>[][] tileColors;
	/** textes à afficher sur les cases (liste vide pour aucun texte) */
	private List<String>[][] tileTexts;
	/** mode d'affichage du texte : gras ou pas */
	private boolean bold = false;
	/** chemins à afficher */
	private final List<List<Tile>> paths = new ArrayList<List<Tile>>();
	/** couleurs des chemins à afficher */
	private final List<Color> pathColors = new ArrayList<Color>();
	
	/**
	 * Met à jour la représentation des sorties de l'agent
	 * qui est destinée au moteur.
	 */
	protected abstract void updateOutput();
	
	/**
	 * renvoie les couleurs des cases
	 * 
	 * @return	
	 * 		matrice de couleurs
	 */
	public List<Color>[][] getTileColors()
	{	return tileColors;
	}

	/**
	 * renvoie les textes à afficher sur les cases
	 * 
	 * @return	
	 * 		matrice de textes
	 */
	public List<String>[][] getTileTexts()
	{	return tileTexts;
	}

	/**
	 * renvoie le mode d'affichage du texte (gras ou pas)
	 * 
	 * @return	
	 * 		vrai si le mode d'affichage est gras
	 */
	public boolean isBold()
	{	return bold;
	}
	
	/**
	 * renvoie la liste de chemins à afficher
	 * 
	 * @return	
	 * 		liste de vecteurs de cases contigües représentant des chemins
	 */
	public List<List<Tile>> getPaths()
	{	return paths;
	}

	/**
	 * renvoie les couleurs des chemins à afficher
	 * 
	 * @return	
	 * 		liste de couleurs
	 */
	public List<Color> getPathColors()
	{	return pathColors;
	}
}
