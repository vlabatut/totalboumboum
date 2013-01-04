package org.totalboumboum.ai.v201213.ais.gerginozkanoglu.v2;

import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;

/**
 * 
 * This class contains operations about our agent.
 * @author Secil Ozkanoglu
 * @author Tugce Gergin
 *
 */
public class AgentOperation {
	
	/**
	 * agent.
	 */
	private GerginOzkanoglu ai;
	
	/**
	 * constructor
	 * @param ai
	 * @throws StopRequestException
	 */
	public AgentOperation(GerginOzkanoglu ai) throws StopRequestException
	{
		ai.checkInterruption();
		this.ai = ai;
		
	}
	
	/**
	 * this method checks if our agent is in danger or not.
	 * @return boolean
	 * @throws StopRequestException
	 */
	public boolean agentInDanger() throws StopRequestException
	{
		ai.checkInterruption();
		TileCalculation calculate = new TileCalculation(this.ai);
		if(calculate.getDangerousBoxes().contains(this.ai.getZone().getOwnHero().getTile()))
			return true;
		return false;
	}

}
