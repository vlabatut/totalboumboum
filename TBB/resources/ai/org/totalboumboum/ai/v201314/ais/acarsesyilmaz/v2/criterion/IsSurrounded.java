package org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v2.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.ais.acarsesyilmaz.v2.Agent;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Classe principale de notre critère Is Surrounded
 * 
 * @author Emre Acar
 * @author Yankı Sesyılmaz
 */
public class IsSurrounded extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "ISSURROUNDED";
	
	/**
	 * 		Création d'un nouveau critère entier qui calcule le niveau de disponibilité d'une 
	 * 		case donnée en paramètre. Il regarde pour chaque case voisin, s'il y a un mur, une 
	 * 		bombe, une explosion ou bien une future explosion, respectivement par les méthodes 
	 * 		"getBlocks", "getBombs","getFires" et "WillBurn". D'où le niveau de disponibilité 
	 * 		diminue ou agmente au fur et à mesure. Une case qui est totalement disponible va 
	 * 		renvoyer la valeur 0. Une case qui est totalement indisponible va renvoyer la valeur 4.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public IsSurrounded(Agent ai)
	{	
		super(ai,NAME,0,4);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Calcule et renvoie la valeur de critère "Is Surrounded" pour la case passée en paramètre.
	 * 
	 * @param tile
	 * 		La case à traiter.
	 * 
	 * @return
	 * 		La valeur de ce critère pour la case spécifiée.
	 * 		Une case qui est totalement disponible va renvoyer la valeur 0 et
	 * 		une case qui est totalement indisponible va renvoyer la valeur 4.
	 */
	@Override
	public Integer processValue(AiTile tile)
	{
		ai.checkInterruption();	
		int result;
		
		if(tile.isCrossableBy(this.ai.getZone().getOwnHero()))
		{
			result = this.ai.checkIfSurrounded(tile);		
		}
		else
		{
			result = 4;
		}				
		return result;
	}
}
