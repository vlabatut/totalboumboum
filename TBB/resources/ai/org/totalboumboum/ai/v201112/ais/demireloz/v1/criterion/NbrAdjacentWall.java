package org.totalboumboum.ai.v201112.ais.demireloz.v1.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBlock;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.ais.demireloz.v1.DemirelOz;

/**
 * Cette classe représente est un simple exemple de 
 * critère entier. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin. Notez que les bornes
 * du domaine de définition sont spécifiées dans
 * l'appel au constructeur ({@code super(nom,inf,sup)}).
 * 
 * @author Enis Demirel
 * @author Berke Öz
 */
@SuppressWarnings("deprecation")
public class NbrAdjacentWall extends AiUtilityCriterionInteger
{	/** Nom de ce critère */
	public static final String NAME = "NbrAdjacentWall";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public NbrAdjacentWall(DemirelOz ai) throws StopRequestException
	{	// init nom + bornes du domaine de définition
		super(NAME,0,3);
		
		// init agent
		this.ai = ai;
	}

    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected DemirelOz ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	
		int result = 0;
	
		
		for (AiTile currentNeighbors : tile.getNeighbors()) 
		{
			for(AiBlock currentBlock: currentNeighbors.getBlocks())
			{
				if(currentNeighbors.getBlocks().isEmpty()!=true && currentBlock.isDestructible())
				{
					result= result+1;
				}
	     	}
		}
		
		
		return result;
	}

}
