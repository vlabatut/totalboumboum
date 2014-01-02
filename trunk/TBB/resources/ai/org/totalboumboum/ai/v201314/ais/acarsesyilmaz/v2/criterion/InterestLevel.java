package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v2.Agent;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Classe principale de notre critère InterestLevel
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
public class InterestLevel extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "INTERESTLEVEL";
	
	/**
	 * 
	 *  Cette méthode fait des calcule par les types de bonus : bombe, flame et speed.
	 *  Il donne la valeur obtenu avec la soustraction de la moyenne d'un type de bonus
	 *  possédé par les ennemies avec le nombre de bonus de ce type qu'on possede en utilisant 
	 *  la méthode "calculateInterest". "calculateInterest" renvoie donc une double array 
	 *  de longueur 3 et notre méthode fait un classement par rapport au type qu'on est le
	 *  plus intéressé avec un int array.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public InterestLevel(Agent ai)
	{	
		super(ai,NAME,0,4);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Calcule et renvoie la valeur de critère "Interest Level" pour la case passée en paramètre.
	 * 
	 * @param tile
	 * 		La case à traiter.
	 * 
	 * @return
	 * 		La valeur de ce critère pour la case spécifiée.
	 * 		si la case correspond au type qu'on veut le plus renvoie 3
	 * 		si la case correspond au type type qu'on veut le moin renvoie 1
	 * 		si la case correspond au type type qu'on veut en moyenne renvoie 2
	 * 		sinon renvoie 4
	 * 		
	 * 		
	 */
	@Override
	public Integer processValue(AiTile tile)
	{
		
		/*
		 *  result[0] : Bomb
		 * 	result[1] : Flame
		 * 	result[2] : Speed		  
		 */	
		
		ai.checkInterruption();	
		int result;
		
		if(tile.isCrossableBy(this.ai.getZone().getOwnHero()) && !tile.getItems().isEmpty())
		{
			AiItemType type = tile.getItems().get(0).getType();
			if(type.isBonus())
			{
				int[] interest = ai.interestOrder();
				
				if(type == AiItemType.EXTRA_FLAME || type == AiItemType.GOLDEN_FLAME)
				{
					result = 3-interest[1];
				}
				else if(type == AiItemType.EXTRA_SPEED || type == AiItemType.GOLDEN_SPEED)
				{
					result = 3-interest[2];					
				}
				else if(type == AiItemType.EXTRA_BOMB || type == AiItemType.GOLDEN_BOMB)
				{
					result = 3-interest[0];					
				}
				else if(type == AiItemType.RANDOM_EXTRA)
				{
					result = 1;
				}
				else
				{
					result = 4;
				}
			}
			else
			{
				result = 4;
			}
		}
		else
		{
			result = 4;
		}				
		return result;
	}
}
