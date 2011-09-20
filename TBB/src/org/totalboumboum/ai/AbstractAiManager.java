package org.totalboumboum.ai;

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

import java.awt.Color;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.ai.AisConfiguration;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.event.ControlEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.AiPlayer;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.RoundVariables;


/**
 * 
 * Classe servant d'interface entre le jeu et une IA.
 * Elle doit être surclass�e de manière à obtenir un adaptateur pour une famille
 * d'IA donnée. Puis, chaque IA doit elle même surclasser la classe résultante
 * (tout ça dans le but de faciliter le chargement de la classe implémentant l'IA).
 * 
 * @author Vincent Labatut
 *
 * @param <V>	
 * 		le type de donnée renvoyée par l'IA (et devant être traduite par l'adaptateur en un évènement compatible avec le moteur du jeu)
 */

public abstract class AbstractAiManager<V>
{	/**
     * contruit un nouveau manager pour l'IA passé en paramètre.
     * Ce constructeur est destin� à être appel� par le constructeur situé dans 
     * la classe h�ritant de celle-ci et située dans le dossier de l'IA
     *    
     * @param 
     * 		ai
     */
	public AbstractAiManager(Callable<V> ai)
    {	this.ai = ai;
    	
	}

    /////////////////////////////////////////////////////////////////
	// EXCEPTIONS LOG	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AisConfiguration aisConfiguration = Configuration.getAisConfiguration();
	
	
	/////////////////////////////////////////////////////////////////
	// THREAD			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /** objet implementant le comportement de l'IA */
	private Callable<V> ai;
    /** gestionnaire de threads pour exécuter l'IA */
    private ExecutorService executorAi = null;
    /** future utilisé pour récupérer le résultat de l'IA */
    private Future<V> futureAi;
    /** indique si cette IA �tait en pause */
    private boolean paused = false;
    
    /**
     * renvoie l'IA gérée par cette classe.
     * 
     * @return	
     * 		l'IA gérée par cette classe sous la forme d'un Callable
     */
    public final Callable<V> getAi()
    {	return ai;    	
    }
    
    public void initAgent()
    {	// on initialise le thread
    	Profile profile = player.getProfile();
    	String name = player.getColor()+":"+profile.getAiPackname()+"/"+profile.getAiName()+":"+player.getName();
    	ThreadFactory fact = new AiThreadFactory(name);
    	executorAi = Executors.newSingleThreadExecutor(fact);
    	
    	// on lance le calcul pour le premier coup
    	makeCall();
    }
    
    /**
     * Utilise la classe d'IA associée à ce personnage pour mettre à jour les variables
     * qui permettront au moteur du jeu de déplacer le personnage.
     * 
     * @param aisPause
     * 		indique si l'agent doit passer en pause
     * @return
     * 		vrai si l'agent a effectué une action
     */
    public final boolean update(boolean aisPause)
    {	boolean result = false;
    	
    	// si l'IA �tait en pause
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
    			{	// on récupére les r�actions de l'IA
    				V value = futureAi.get();
    				// on les convertit et les envoie au moteur
    				List<ControlEvent> events = convertReaction(value);
    				Iterator<ControlEvent> it = events.iterator();
    				while(it.hasNext())
    				{	ControlEvent event = it.next();
    					player.getSprite().putControlEvent(event);
    				}
    				result = !events.isEmpty();
    				// on met à jour les sorties de l'IA
    				updateOutput();
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
    	
    	return result;
    }
    
    /**
     * réalise l'appel à la classe qui implémente l'IA,
     * afin que celle ci calcule la prochaine action à effectuer.
     * 
     * @param firstTime
     * 		la valeur vrai indique qu'il s'agit du premier appel
     */
    private final void makeCall()
    {	
//    	System.out.println(player.getColor()+":"+GameVariables.loop.getTotalTime());    	
    	updatePercepts();
     	futureAi = executorAi.submit(ai);
    }
    
    /**
     * terminer ce gestionnaire, et en particulier le thread exécutant l'IA.
     * Ou plut�t tente de le terminer, car le résultat ne peut être forc�.
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
     * termine cette IA, et en particulier le processus qui l'exécute.
     * Pour cette raison, l'IA doit implémenter une méthode for�ant 
     * sa terminaison.
     */
    public abstract void finishAi();

    /////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /** le joueur contr�l� par l'IA */
    private AiPlayer player;
   
    /**
     * initialise le gestionnaire d'IA
     * 
     * @param instance	
     * 		instance utilisée dans ce round
     * @param player	
     * 		joueur contr�l� par l'IA
     */
    @SuppressWarnings("unchecked")
	public void init(String instance, AiPlayer player)
	{	// input
    	this.player = player;
		
    	// output
		Level level = RoundVariables.level;
		int height = level.getGlobalHeight();
		int width = level.getGlobalWidth();
		tileColors = new Color[height][width];
		tileTexts = new List[height][width];
	}
		
	/**
	 * renvoie le joueur contr�l� par l'IA gérée
	 * 
	 * @return	
	 * 		un objet représentant le joueur contr�l� par l'IA
	 */
    public AbstractPlayer getPlayer()
	{	return player;		
	}
	
	/**
	 * méthode utilisée pour mettre à jour les percepts de l'ia avant 
	 * que cette dernière ne calcule la prochaine action à effectuer.
	 * Cette méthode doit être surchargée de manière à adapter la structure
	 * des données à l'IA qui va les traiter
	 */
	public abstract void updatePercepts();
	
	/**
	 * méthode utilisée pour convertir la valeur renvoyée par l'ia 
	 * en un évènement standard traitable par le moteur du jeu.
	 * 
	 * @param value	
	 * 		la valeur renvoyée par l'ia, qui est à convertir
	 * @return	
	 * 		le résultat de la conversion sous la forme d'un évènement à envoyer au sprite contr�l� par l'IA
	 */
	public abstract List<ControlEvent> convertReaction(V value);
   
	/**
	 * termine proprement les percepts, de manière à lib�rer les ressources occupées.
	 * Cette méthode est appelée lorsque la partie est terminée et que les
	 * percepts deviennent inutiles.
	 */
	public abstract void finishPercepts();
	
    /////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** couleurs associées aux cases (ou null pour aucune couleur */
	private Color[][] tileColors;
	/** textes à afficher sur les cases (liste vide pour aucun texte) */
	private List<String>[][] tileTexts;
	/** mode d'affichage du texte : gras ou pas */
	private boolean bold = false;
	/** chemins à afficher */
	private final List<List<Tile>> paths = new ArrayList<List<Tile>>();
	/** couleurs des chemins à afficher */
	private final List<Color> pathColors = new ArrayList<Color>();
	
	/**
	 * met à jour la représentation des sorties de l'IA
	 * qui est destin�e au moteur
	 */
	protected abstract void updateOutput();
	
	/**
	 * renvoie les couleurs des cases
	 * 
	 * @return	
	 * 		matrice de couleurs
	 */
	public Color[][] getTileColors()
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
	 * 		liste de vecteurs de cases contig�es représentant des chemins
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
