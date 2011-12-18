package fr.free.totalboumboum.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



import fr.free.totalboumboum.engine.content.feature.event.ControlEvent;
import fr.free.totalboumboum.engine.player.Player;

public abstract class AbstractAiManager<V>
{
    /**
     * contruit un nouveau manager pour l'IA pass� en param�tre.
     * Ce constructeur est destin� � �tre appel� par le constructeur situ� dans 
     * la classe h�ritant de celle-ci et situ�e dans le dossier de l'IA   
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
    /** gestionnaire de threads pour ex�cuter l'IA */
    private ExecutorService executorAi = null;
    /** future utilis� pour r�cup�rer le r�sultat de l'IA */
    private Future<V> futureAi;

    public Callable<V> getAi()
    {	return ai;    	
    }
    
    /**
     * Utilise la classe d'IA associ�e � ce personnage pour mettre � jour les variables
     * qui permettront au moteur du jeu de d�placer le personnage.
     */
    public final void update()
    {	// s'il s'agit du premier appel
    	if(executorAi == null)
    	{	executorAi = Executors.newSingleThreadExecutor();
    		makeCall();
    	}
    	// sinon : un appel avait d�j� �t� effectu�
    	else
    	{	// si cet appel est fini :  
    		if(futureAi.isDone())
    		{	// on met � jour le joueur
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
     * R�alise l'appel � la classe qui impl�mente l'IA
     */
    private final void makeCall()
    {	updatePercepts();
    	futureAi = executorAi.submit(ai);
    }
    
    /**
     * tente de terminer le thread ex�cutant l'IA
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
    
    public abstract void finishAi();

    /////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    private Player player;
   
	public void init(String instance, Player player)
	{	this.player = player;
	}
		
	public Player getPlayer()
	{	return player;		
	}
	
	/**
	 * m�thode utilis�e pour initialiser les percepts de l'ia avant 
	 * que cette derni�re ne calcule la prochaine action � effectuer.
	 * Cette m�thode doit �tre surcharg�e de mani�re � adapter la structure
	 * des donn�es � l'IA qui va les traiter
	 */
	public abstract void updatePercepts();
	
	/**
	 * m�thode utilis�e pour convertir la valeur renvoy�e par l'ia 
	 * en un �v�nement standard traitable par le moteur du jeu.
	 * @param value	la valeur renvoy�e par l'ia, qui est � convertir
	 * @return	le r�sultat de la conversion sous la forme d'un �v�nement � envoyer au sprite contr�l� par l'IA
	 */
	public abstract ArrayList<ControlEvent> convertReaction(V value);
   
	public abstract void finishPercepts();
}