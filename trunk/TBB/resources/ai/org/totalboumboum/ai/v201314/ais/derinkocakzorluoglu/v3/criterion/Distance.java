package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v3.criterion;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v3.Agent;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v3.CalculationHelper;

/**
 * Distance
 * The criteria for distance  Il va retourner false si la case est
 * proche a nous sinon il va retourner true.
 * 
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
public class Distance extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "Distance";
	
	/** Domaine de définition  {true,false} 
	 * Si la methode simpleTileDistance dont les parametres sont (nous et la tile on regarde)
	 * renvoit une valeur plus petit ou egale a 4 il va retourner faux. Donc on comprend qu'il est proche
	 * Sinon il va retourner true.
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public Distance(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Boolean processValue(AiTile tile)
	{	ai.checkInterruption();
	
		boolean result = true;
		int a;
		CalculationHelper objet=new CalculationHelper(ai);
		a=objet.simpleTileDistance(ai.myHero.getTile(),tile);
		if(a<4)
			result=false;
		
		return result;
	}
}
