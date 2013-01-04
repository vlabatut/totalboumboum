package org.totalboumboum.ai.v201213.ais.erdemtayyar.v4.criterion;




import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v4.ErdemTayyar;


/**
 * Cette classe est pour l'adversaire indirecte.
 * Cette classe est un simple exemple de critère binaire, il renvois 1 s'il existe un mur destructible
 * a coté d'un adversaire indirecte, s'İl n'existe pas, il renvois 0.
 * 
 * 
 * @author Banu Erdem
 * @author Zübeyir Tayyar
 */

public class EnnemieDistance extends AiUtilityCriterionBoolean<ErdemTayyar> {

	
	/**
	 * We affect the name of out criteria
	 */
	public static final String	NAME	= "EnnemieDistance";

	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	
	public EnnemieDistance( ErdemTayyar ai ) throws StopRequestException
	{
		super(ai, NAME);
		ai.checkInterruption();
	}

	


	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue( AiTile tile ) throws StopRequestException
	{   
		ai.checkInterruption();
		int i;
		boolean result = false;
	    try
		{
	    	for(i=0;i<this.ai.getTs().getClosestAccDesWalltoEnemy().size();i++)
	    		{
	    		ai.checkInterruption();
	    		if (this.ai.getTs().getClosestAccDesWalltoEnemy().get(i).getNeighbors().contains(tile))
		          result = true;
	    		}
    		
	    	
		}
		catch ( NullPointerException e )
		{
			result = false;
		}
		return result;
	}
}
