package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v2.Agent;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Classe principale de notre critère Countdown
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
public class Countdown extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "TEMPSRESTENT";
	
	/**
	 * 		Création d'un nouveau critère entier qui calcule le temps restant de 
	 * 		l'explosion dans la case donnée en paramètre et attribue a un valeur 
	 * 		entier compris entre [1,3] 
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public Countdown(Agent ai)
	{	
		super(ai,NAME,1,3);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Calcule et renvoie la valeur de critère "Countdown" pour la case passée en paramètre.
	 * 
	 * @param tile
	 * 		La case à traiter.
	 * 
	 * @return
	 * 		La valeur entier de ce critère pour la case spécifiée.
	 * 		Retourne 1 si la case va avoir une explosion dans 1 secondes,
	 * 		retourne 2 si la case va avoir une explosion dans 3 secondes
	 * 		et 3 quand la case ne serait pas affecté par une explosion.
	 *  		
	 */
	@Override
	public Integer processValue(AiTile tile)
	{	
		ai.checkInterruption();
		int result;
		
		if(tile.isCrossableBy(this.ai.getZone().getOwnHero()))
		{
			long duration = this.ai.getTimeLeft(tile);			
			
			if(duration > 1000 && duration <= 2000)
			{
				result = 2;
			}
			else if(duration <= 1000)
			{
				result = 1;
			}			
			else
			{
				result = 3;
			}						
		}
		else
		{
			result = 1;
		}
		
		return result;		
	}
}
