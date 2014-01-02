package org.totalboumboum.ai.v201314.ais.saylamsonmez.v3;

import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;


/**
 * Classe gérant le choi de l'ennemi.
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class Enemy
{	
	/** our hero */
	AiHero ourHero;
	/** zone */
	AiZone zone;
	/** pour acceder à un agent*/
	private Agent ai;
	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	public Enemy(Agent ai)
    {	
    	ai.checkInterruption();
    	this.ai=ai;
		zone=ai.getZone();
		ourHero=zone.getOwnHero();	
		
	}


    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

    /**
     * Methode pour envoyer l'ennemie qui est choisi.
     *
     * @return
     * 			L'ennemie ce qui est choisi.
     * 
     * @throws StopRequestException
     * 			  Au cas où le moteur demande la terminaison de l'agent.
     */
	public AiHero selectEnemy() throws StopRequestException
	{
		ai.checkInterruption();
		AiHero enemy=null;
		enemy=highPointEnemy();
		if(enemy==null)
			enemy=nearestEnemy();
        return enemy;
	}
	
	/**
	 * Cette méthode nous permet de calculer le plus proche ennemie.
	 *  
	 * @return
	 * 		Le plus proche l'ennemie.
	 * 
	 * @throws StopRequestException
	 * 			Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AiHero nearestEnemy() throws StopRequestException
	{
		ai.checkInterruption();
		AiHero enemy=null;
		int distance=1000;
		int distanceBetweEnenemy=0;
		for( AiHero hero : this.ai.getZone().getRemainingOpponents())
		{
			ai.checkInterruption();
			distanceBetweEnenemy=zone.getTileDistance(ourHero.getTile(), hero.getTile());
			if(distance>distanceBetweEnenemy)
			{
				distance = distanceBetweEnenemy;
				enemy = hero;
			}
		}
        return enemy;
	}
	/**
	 * Methode pour decider à quel ennemie on va choisir.
	 * Si dans la zone il y a un ennemie qui a max point, alors on le choisit.
	 * 
	 * @return
	 * 				Retourne comme l'ennemie qui a le plus point.
	 * 
	 * @throws StopRequestException
	 * 				Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AiHero highPointEnemy() throws StopRequestException
	{
		ai.checkInterruption();
		AiHero enemy = null;
		int maxPoint = Integer.MIN_VALUE;
		for(AiHero hero: zone.getRemainingOpponents())
		{
			ai.checkInterruption();
			if(hero.getMatchRank()!=0)
			{
				if(maxPoint<hero.getMatchRank())
				{
					maxPoint=hero.getMatchRank();
					enemy=hero;
				}
			}
		}
		return enemy;
		
	}


	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 */
	protected void updateOutput()
	{	ai.checkInterruption();
		

	}
}
