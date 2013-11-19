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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.engine.content.feature.event.ControlEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.AiPlayer;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.images.PredefinedColor;
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
 * 		Le type de donnée renvoyée par l'agent (et devant être traduite par 
 * 		l'adaptateur en un évènement compatible avec le moteur du jeu)
 * @param <T>
 * 		Représentation des percepts.	
 */
public abstract class AiAbstractManager<V, T extends Serializable>
{	
	/**
     * Contruit un nouveau manager. L'agent concerné
     * est récupé en utilisant la méthode {@link #instantiateAgent()},
     * qui doit être surchargée dans le manager que le concepteur
     * définit pour son agent. Ce système le laisse libre de choisir
     * quelle version de son agent il veut utiliser (s'il y en a plusieurs).
     */
	public AiAbstractManager()
    {	this.ai = instantiateAgent();
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
     * @throws URISyntaxException 
	 * 		Problème lors de la localisation du fichier de préférences.
     * @throws IllegalArgumentException 
	 * 		Problème lors du chargement des critères, catégories ou combinaisons.
     */
    @SuppressWarnings("unchecked")
	public void init(String instance, AiPlayer player) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, ParserConfigurationException, SAXException, IOException, IllegalArgumentException, URISyntaxException
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
     * 		L'agent géré par cette classe sous la forme d'un Callable.
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
	 * Cette méthode doit renvoyer une instance de l'agent.
	 * A implémenter par le concepteur de l'agent, car cela
	 * lui permet de choisir parmi différentes versions de son
	 * agent.
	 * 
	 * @return 
	 * 		Renvoie une instance de l'agent.
	 */
    protected abstract Callable<V> instantiateAgent();
    
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
     * Termine ce gestionnaire, et en particulier le thread exécutant l'agent.
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
     * Termine cet agent, et en particulier le processus qui l'exécute.
     * Pour cette raison, l'agent doit implémenter une méthode forçant 
     * sa terminaison.
     */
    public abstract void finishAi();

    /////////////////////////////////////////////////////////////////
	// PLAYER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /** Le joueur contrôlé par l'agent */
    private AiPlayer player;
   
	/**
	 * Tenvoie le joueur contrôlé par l'agent géré
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
     * Renvoie les derniers percepts calculés pour l'agent.
     * 
     * @return
     * 		Un objet représentant les derniers percepts calculés.
     */
    public abstract T getCurrentPercepts();
    
	/**
	 * Méthode utilisée pour mettre à jour les percepts de l'agent avant 
	 * que ce dernier ne calcule la prochaine action à effectuer.
	 * <br/>
	 * Cette méthode doit être surchargée de manière à adapter la structure
	 * des données à l'agent qui va les traiter
	 */
	public abstract void updatePercepts();
	
	/**
	 * Méthode utilisée pour convertir la valeur renvoyée par l'agent 
	 * en un évènement standard traitable par le moteur du jeu.
	 * 
	 * @param value	
	 * 		la valeur renvoyée par l'agent, qui est à convertir
	 * @return	
	 * 		le résultat de la conversion sous la forme d'un évènement à envoyer au sprite contrôlé par l'agent
	 */
	public abstract List<ControlEvent> convertReaction(V value);
   
	/**
	 * Termine proprement les percepts, de manière à libérer les ressources occupées.
	 * Cette méthode est appelée lorsque la partie est terminée et que les
	 * percepts deviennent inutiles.
	 */
	public abstract void finishPercepts();
	
	/**
	 * Enregistre les percepts (ceux définis par l'API IA)
	 * dans un fichier, afin de pouvoir les utiliser plus tard, de
	 * façon hors-ligne.
	 * 
	 * @throws IOException
	 * 		Problème lors de l'enregistrement des percepts. 
	 */
	public final void writePercepts() throws IOException
	{	T percepts = getCurrentPercepts();
		if(percepts!=null)
		{	// get current date and time
			Calendar cal = new GregorianCalendar();
			Date currentTime = cal.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
			String currentStr = sdf.format(currentTime);
			
			// set up file name
			String path = FilePaths.getCapturePerceptsPath() + File.separator + currentStr + FileNames.EXTENSION_DATA;
			File file = new File(path);
	
			// open stream
			OutputStream os = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(os);
			
			// record meta data
			oos.writeObject(currentTime);
			LevelInfo info = RoundVariables.loop.getRound().getHollowLevel().getLevelInfo();
			oos.writeObject(info.getTitle());
			oos.writeObject(info.getGlobalHeight());
			oos.writeObject(info.getGlobalWidth());
			oos.writeObject(info.getPackName());
			oos.writeObject(info.getInstanceName());
			oos.writeObject(info.getThemeName());
			oos.writeObject(player.getColor());
			
			// record percepts
			oos.writeObject(percepts);
			
			// close stream
			oos.close();
		}
	}
	
	/**
	 * Lit les percepts (ceux définis par l'API IA)
	 * à partir d'un fichier, afin de pouvoir les utiliser de
	 * façon hors-ligne.
	 * 
	 * @param fileName
	 * 		Nom du fichier de percepts à lire. 
	 * @return 
	 * 		Les percepts lus dans le fichier spécifié.
	 * 
	 * @throws IOException
	 * 		Problème lors de la lecture des percepts. 
	 * @throws ClassNotFoundException 
	 * 		Problème lors de la lecture des percepts. 
	 */
	public final T readPercepts(String fileName) throws IOException, ClassNotFoundException
	{	// set up file name
		String path = FilePaths.getCapturePerceptsPath() + File.separator + fileName;
		System.out.println("Loading percepts from file \""+path+"\"");
		File file = new File(path);

		// open stream
		InputStream is = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(is);
		
		// record meta data
		Date time = (Date)ois.readObject();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		String timeStr = sdf.format(time);
		System.out.println("..Percepts recorded on "+timeStr);
		
		String title = (String)ois.readObject();
		int height = (Integer)ois.readObject();
		int width = (Integer)ois.readObject();
		String packName = (String)ois.readObject();
		String instanceName = (String)ois.readObject();
		String themeName = (String)ois.readObject();
		PredefinedColor color = (PredefinedColor)ois.readObject();
		System.out.println("..Controled player color: "+color);
		System.out.println("..Zone name: "+packName+"/"+title);
		System.out.println("..Zone dimensions: "+height+"×"+width);
		System.out.println("..Zone theme: "+instanceName+"/"+themeName);
		
		// record percepts
		@SuppressWarnings("unchecked")
		T result = (T)ois.readObject();
		System.out.println("..Zone content: ");
		System.out.println(result.toString());
		System.out.println("All percepts loaded");
		
		// close stream
		ois.close();

		return result;
	}
	
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
	 * Initialise la liste des noms des étapes 
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
	/** Couleurs associées aux cases (ou null pour aucune couleur */
	private List<Color>[][] tileColors;
	/** Textes à afficher sur les cases (liste vide pour aucun texte) */
	private List<String>[][] tileTexts;
	/** Mode d'affichage du texte : gras ou pas */
	private boolean bold = false;
	/** Taille du texte affiché */
	private float textSize = 1;
	/** Chemins à afficher */
	private final List<List<double[]>> paths = new ArrayList<List<double[]>>();
	/** Couleurs des chemins à afficher */
	private final List<Color> pathColors = new ArrayList<Color>();
	/** Temps d'attente associés aux chemins à afficher */
	private final List<List<Long>> pathWaits = new ArrayList<List<Long>>();
	
	/**
	 * Met à jour la représentation des sorties de l'agent
	 * qui est destinée au moteur.
	 */
	protected abstract void updateOutput();
	
	/**
	 * Renvoie les couleurs des cases
	 * 
	 * @return	
	 * 		matrice de couleurs
	 */
	public List<Color>[][] getTileColors()
	{	return tileColors;
	}

	/**
	 * Renvoie les textes à afficher sur les cases
	 * 
	 * @return	
	 * 		matrice de textes
	 */
	public List<String>[][] getTileTexts()
	{	return tileTexts;
	}

	/**
	 * Renvoie le mode d'affichage du texte (gras ou pas)
	 * 
	 * @return	
	 * 		vrai si le mode d'affichage est gras
	 */
	public boolean isBold()
	{	return bold;
	}
	
	/**
	 * Change le mode d'affichage du texte : gras ou pas.
	 * 
	 * @param bold
	 * 		La valeur {@code true} indique que l'affichage sera effectué en gras.
	 */
	public void setBold(boolean bold)
	{	this.bold = bold;
	}
	
	/**
	 * Renvoie la taille du texte, exprimé en
	 * proportion de la taille normale.
	 * 
	 * @return
	 * 		Taille courante du texte.
	 */
	public float getTextSize()
	{	return textSize;
	}

	/**
	 * Change la taille du texte, exprimé en proportion de
	 * la taille normale. Donc : 2=deux fois plus grand,
	 * 0.5=deux fois plus petit, etc.
	 * 
	 * @param textSize
	 * 		Nouvelle taille du texte, en proportion du texte normal.
	 */
	public void setTextSize(float textSize)
	{	this.textSize = textSize;
	}

	/**
	 * Renvoie la liste de chemins à afficher.
	 * 
	 * @return	
	 * 		Liste de vecteurs de cases contigües représentant des chemins.
	 */
	public List<List<double[]>> getPaths()
	{	return paths;
	}

	/**
	 * Renvoie les couleurs des chemins à afficher.
	 * 
	 * @return	
	 * 		Liste de couleurs.
	 */
	public List<Color> getPathColors()
	{	return pathColors;
	}

	/**
	 * Renvoie les temps d'attente parfois associés
	 * aux différentes étapes des chemins.
	 * 
	 * @return	
	 * 		Liste de couleurs de durées exprimées en ms.
	 */
	public List<List<Long>> getPathWaits()
	{	return pathWaits;
	}
}
