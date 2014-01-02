package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v1.criterion;

//import java.util.Arrays;
//import java.util.List;
//import java.util.Set;
//import java.util.TreeSet;
import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
//import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionString;
//import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v1.Agent;

/**
 * The criteria for distance . It will turn the value 0 if the case is near to us and
 * 1 if not .
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
public class Distance extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "Distance";
	/** Domaine de définition  {0 et 1} */
	/**
	 * Si la distance entre notre agent et cette case est plus petit ou égale a 3
     * il prend 0 comme valeur. Sinon il va être 1.
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
	public Boolean processValue(AiTile tile)
	{	ai.checkInterruption();

	boolean result = false;
		
		
		int a;
		
		a=ai.getZone().getTileDistance(ai.getZone().getOwnHero().getTile(),tile);
		if(a<=3)
			
			result=true;
		return result;
	}
}
