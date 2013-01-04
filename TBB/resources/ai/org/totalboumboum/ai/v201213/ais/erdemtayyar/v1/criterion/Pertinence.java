package org.totalboumboum.ai.v201213.ais.erdemtayyar.v1.criterion;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.ais.erdemtayyar.v1.ErdemTayyar;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;

/**
 * C'est un critere binaire.
 * Cette classe est pour la critere pertinence, pour montrer si on a besoin d'un item ou pas.
 * 
 * @author Banu Erdem
 * @author Zübeyir Tayyar
 */
@SuppressWarnings("rawtypes")
public class Pertinence extends AiUtilityCriterionBoolean
{	/**
	 * We affect the name of out criteria
	 */
	public static final String	NAME	= "Pertinence";

	//CONSTRUCTOR
	/**
	 * On initialise la valeur dont la domaine de définition est  TRUE et FALSE.
	 * Si on a besoin d'un item sa valeur est TRUE sinon FALSE.
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unchecked")
	public Pertinence( ErdemTayyar ai ) throws StopRequestException
	{
		super(ai,NAME);
		ai.checkInterruption();
		
	}

	

	// ///////////////////////////////////////////////////////////////
	// PROCESS /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue( AiTile tile ) throws StopRequestException
	{
		ai.checkInterruption();
		boolean result = false;
		int bombNumber = ((ErdemTayyar) this.ai).getHero().getBombNumberMax();
		int fireRange = ((ErdemTayyar) this.ai).getHero().getBombRange();
		double Speed = ((ErdemTayyar) this.ai).getHero().getWalkingSpeed();

		if ( tile.getItems().contains( AiItemType.EXTRA_BOMB ) )
		{
			if ( bombNumber < fireRange )
			{
				result = true;
			}
			else
				result = false;
		}

		if ( tile.getItems().contains( AiItemType.EXTRA_FLAME ) )
		{
			if ( bombNumber > fireRange )
			{
				result = true;
			}
			else
				result = false;
		}
		if ( tile.getItems().contains( AiItemType.EXTRA_SPEED ) )
		{
			if ( Speed < 2 )
			{
				result = true;
			}
			else
				result = false;
		}
		

		return result;
	}
}

