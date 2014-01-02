package org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.enginserhantipici.v3.Agent;

/**
 * for having different preference values
 * 
 * @author Gözde Engin
 * @author Barış Serhan
 * @author Garip Tipici
 */
public class PreferenceSeparator extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "PREFERENCE_SEPARATOR";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	
	public PreferenceSeparator(Agent ai)
	{	super(ai,NAME,0,25);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile)
	{	ai.checkInterruption();
	
		int result;
		int tId = ai.tileIdentification(tile);
		
		
		
		
		switch (tId){
		case 0: result = 0; break;
		case 1: result = 1; break;
		case 2: result = 2; break;
		case 3: result = 3; break;
		case 4: result = 4; break;
		case 5: result = 5; break;
		case 6: result = 6; break;
		case 7: result = 7; break;
		case 8: result = 8; break;
		case 9: result = 9; break;
		case 10: result = 10; break;
		case 11: result = 11; break;
		case 12: result = 12; break;
		case 13: result = 13; break;
		case 14: result = 14; break;
		case 15: result = 15; break;
		case 16: result = 16; break;
		case 17: result = 17; break;
		case 18: result = 18; break;
		case 19: result = 19; break;
		case 20: result = 20; break;
		case 21: result = 21; break;
		case 22: result = 22; break;
		case 23: result = 23; break;
		case 24: result = 24; break;
		default: result = 25; break;
		}
		
		return result;
	}
}
