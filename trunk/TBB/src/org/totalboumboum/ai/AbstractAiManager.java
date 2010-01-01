package org.totalboumboum.ai;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
import org.totalboumboum.engine.player.Player;
import org.totalboumboum.game.round.RoundVariables;


/**
 * 
 * Classe servant d'interface entre le jeu et une IA.
 * Elle doit �tre surclass�e de mani�re � obtenir un adaptateur pour une famille
 * d'IA donn�e. Puis, chaque IA doit elle m�me surclasser la classe r�sultante
 * (tout �a dans le but de faciliter le chargement de la classe impl�mentant l'IA).
 * 
 * @author Vincent
 *
 * @param <V>	le type de donn�e renvoy�e par l'IA (et devant �tre traduite par l'adaptateur en un �v�nement compatible avec le moteur du jeu)
 */

public abstract class AbstractAiManager<V>
{	/**
     * contruit un nouveau manager pour l'IA pass� en param�tre.
     * Ce constructeur est destin� � �tre appel� par le constructeur situ� dans 
     * la classe h�ritant de celle-ci et situ�e dans le dossier de l'IA   
     * @param ai
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
    /** gestionnaire de threads pour ex�cuter l'IA */
    private ExecutorService executorAi = null;
    /** future utilis� pour r�cup�rer le r�sultat de l'IA */
    private Future<V> futureAi;
    /** indique si cette IA �tait en pause */
    private boolean paused = false;
    
    /**
     * renvoie l'IA g�r�e par cette classe.
     * 
     * @return	l'IA g�r�e par cette classe sous la forme d'un Callable
     */
    public Callable<V> getAi()
    {	return ai;    	
    }
    
    /**
     * Utilise la classe d'IA associ�e � ce personnage pour mettre � jour les variables
     * qui permettront au moteur du jeu de d�placer le personnage.
     */
    public final void update(boolean aisPause)
    {	// s'il s'agit du premier appel
    	if(executorAi == null)
    	{	ThreadFactory fact = new ThreadFactory()
    		{	public Thread newThread(Runnable r)
    			{	Thread result = new Thread(r);
    				result.setPriority(Thread.MIN_PRIORITY);
    				return result;
    			}   		
    		};
    		executorAi = Executors.newSingleThreadExecutor(fact);
    		// on lance le calcul pour le prochain coup
    		makeCall();
    	}
    	// si l'IA �tait en pause
    	else if(paused)
    	{	// sortie de pause ?
    		if(!aisPause)
	    	{	// basculement de la pause
    			paused = false;
   				// on lance le calcul pour le prochain coup
				makeCall();	    		
	    	}    		
    	}
    	// sinon : un appel avait d�j� �t� effectu�
    	else
    	{	// passage en pause ?
    		if(aisPause)
    		{	// basculement de la pause
    			paused = true;
    			// on arr�te le joueur
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
    			{	// on r�cup�re les r�actions de l'IA
    				V value = futureAi.get();
    				// on les convertit et les envoie au moteur
    				ArrayList<ControlEvent> events = convertReaction(value);
    				Iterator<ControlEvent> it = events.iterator();
    				while(it.hasNext())
    				{	ControlEvent event = it.next();
    					player.getSprite().putControlEvent(event);
    				}
    				// on met � jour les sorties de l'IA
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
    }
    
    /**
     * R�alise l'appel � la classe qui impl�mente l'IA,
     * afin que celle ci calcule la prochaine action � effectuer.
     */
    private final void makeCall()
    {	
//    	System.out.println(player.getColor()+":"+GameVariables.loop.getTotalTime());    	
    	updatePercepts();
     	futureAi = executorAi.submit(ai);
    }
    
    /**
     * terminer ce gestionnaire, et en particulier le thread ex�cutant l'IA.
     * Ou plut�t tente de le terminer, car le r�sultat ne peut �tre forc�.
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
     * termine cette IA, et en particulier le processus qui l'ex�cute.
     * Pour cette raison, l'IA doit impl�menter une m�thode for�ant 
     * sa terminaison.
     */
    public abstract void finishAi();

    /////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /** le joueur contr�l� par l'IA */
    private Player player;
   
    /**
     * initialise le gestionnaire d'IA
     * 
     * @param instance	instance utilis�e dans ce round
     * @param player	joueur contr�l� par l'IA
     */
    public void init(String instance, Player player)
	{	// input
    	this.player = player;
		// output
		Level level = RoundVariables.level;
		int height = level.getGlobalHeight();
		int width = level.getGlobalWidth();
		tileColors = new Color[height][width];
		tileTexts = new String[height][width];
	}
		
	/**
	 * renvoie le joueur contr�l� par l'IA g�r�e
	 * 
	 * @return	un objet repr�sentant le joueur contr�l� par l'IA
	 */
    public Player getPlayer()
	{	return player;		
	}
	
	/**
	 * m�thode utilis�e pour mettre � jour les percepts de l'ia avant 
	 * que cette derni�re ne calcule la prochaine action � effectuer.
	 * Cette m�thode doit �tre surcharg�e de mani�re � adapter la structure
	 * des donn�es � l'IA qui va les traiter
	 */
	public abstract void updatePercepts();
	
	/**
	 * m�thode utilis�e pour convertir la valeur renvoy�e par l'ia 
	 * en un �v�nement standard traitable par le moteur du jeu.
	 * 
	 * @param value	la valeur renvoy�e par l'ia, qui est � convertir
	 * @return	le r�sultat de la conversion sous la forme d'un �v�nement � envoyer au sprite contr�l� par l'IA
	 */
	public abstract ArrayList<ControlEvent> convertReaction(V value);
   
	/**
	 * termine proprement les percepts, de mani�re � lib�rer les ressources occup�es.
	 * Cette m�thode est appel�e lorsque la partie est termin�e et que les
	 * percepts deviennent inutiles.
	 */
	public abstract void finishPercepts();
	
    /////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** couleurs associ�es aux cases (ou null pour aucune couleur */
	private Color[][] tileColors;
	/** texte � afficher sur les cases (ou null pour aucun texte) */
	private String[][] tileTexts;
	/** chemins � afficher */
	private final List<List<Tile>> paths = new ArrayList<List<Tile>>();
	/** couleurs des chemins � afficher */
	private final List<Color> pathColors = new ArrayList<Color>();
	
	/**
	 * met � jour la repr�sentation des sorties de l'IA
	 * qui est destin�e au moteur
	 */
	protected abstract void updateOutput();
	
	/**
	 * renvoie les couleurs des cases
	 * 
	 * @return	matrice de couleurs
	 */
	public Color[][] getTileColors()
	{	return tileColors;
	}

	/**
	 * renvoie les textes � afficher sur les cases
	 * 
	 * @return	matrice de textes
	 */
	public String[][] getTileTexts()
	{	return tileTexts;
	}

	/**
	 * renvoie la liste de chemins � afficher
	 * 
	 * @return	liste de vecteurs de cases contig�es repr�sentant des chemins
	 */
	public List<List<Tile>> getPaths()
	{	return paths;
	}

	/**
	 * renvoie les couleurs des chemins � afficher
	 * 
	 * @return	liste de couleurs
	 */
	public List<Color> getPathColors()
	{	return pathColors;
	}
}
