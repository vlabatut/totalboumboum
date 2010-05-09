package fr.free.totalboumboum.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



import fr.free.totalboumboum.ai.InterfaceAi;
import fr.free.totalboumboum.ai.adapter200708.ArtificialIntelligence;
import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Contact;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.Orientation;
import fr.free.totalboumboum.engine.content.feature.TilePosition;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.action.AbstractAction;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.content.feature.permission.TargetPermission;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.player.Player;

public abstract class AbstractAiManager<V>
{
    /**
     * contruit un nouveau manager pour l'IA passé en paramètre.
     * Ce constructeur est destiné à être appelé par le constructeur situé dans 
     * la classe héritant de celle-ci et située dans le dossier de l'IA   
     * @param ai
     */
	public AbstractAiManager(Callable<V> ai)
    {	this.ai = ai;
	}

    /////////////////////////////////////////////////////////////////
	// THREAD			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /** object implementing the IA behaviour */
	private Callable<V> ai;
    /** gestionnaire de threads pour exécuter l'IA */
    private ExecutorService executorAi = null;
    /** future utilisé pour récupérer le résultat de l'IA */
    private Future<V> futureAi;

    public Callable<V> getAi()
    {	return ai;    	
    }
    
    /**
     * Utilise la classe d'IA associée à ce personnage pour mettre à jour les variables
     * qui permettront au moteur du jeu de déplacer le personnage.
     */
    public void update()
    {	// s'il s'agit du premier appel
    	if(executorAi == null)
    	{	executorAi = Executors.newSingleThreadExecutor();
    		makeCall();
    	}
    	// sinon : un appel avait déjà été effectué
    	else
    	{	// si cet appel est fini :  
    		if(futureAi.isDone())
    		{	// on met à jour le joueur
    			try
    			{	V value = futureAi.get();
    				ArrayList<ControlEvent> events = convertReaction(value);
    				Iterator<ControlEvent> it = events.iterator();
    				while(it.hasNext())
    				{	ControlEvent event = it.next();
    					player.getSprite().putControlEvent(event);
    				}
				}
    			catch (InterruptedException e)
    			{	e.printStackTrace();
				}
    			catch (ExecutionException e)
    			{	e.printStackTrace();
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
     * Réalise l'appel à la classe qui implémente l'IA
     */
    private void makeCall()
    {	setPercepts();
    	futureAi = executorAi.submit(ai);
    }
    
    /**
     * tente de terminer le thread exécutant l'IA
     */
    public void finish()
    {	boolean result = futureAi.isDone(); 
    	if(!result) 
    		result = futureAi.cancel(true);
    	/*List<Runnable> list = */executorAi.shutdownNow();
    	
    	//NOTE finaliser les objects inutilisés
    }

    /////////////////////////////////////////////////////////////////
	// DATA				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    private Player player;
   
	public void init(String instance, Player player)
	{	this.player = player;
	}
		
	public Player getPlayer()
	{	return player;		
	}
	
	/**
	 * méthode utilisée pour initialiser les percepts de l'ia avant 
	 * que cette dernière ne calcule la prochaine action à effectuer.
	 * Cette méthode doit être surchargée de manière à adapter la structure
	 * des données à l'IA qui va les traiter
	 */
	public abstract void setPercepts();
	
	/**
	 * méthode utilisée pour convertir la valeur renvoyée par l'ia 
	 * en un évènement standard traitable par le moteur du jeu.
	 * @param value	la valeur renvoyée par l'ia, qui est à convertir
	 * @return	le résultat de la conversion sous la forme d'un évènement à envoyer au sprite contrôlé par l'IA
	 */
	public abstract ArrayList<ControlEvent> convertReaction(V value);
   
}
