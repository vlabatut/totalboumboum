

package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v1.criterion;

//import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
//import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
//import org.totalboumboum.ai.v201314.adapter.data.AiFire;
//import org.totalboumboum.ai.v201314.adapter.data.AiHero;
//import org.totalboumboum.ai.v201314.adapter.data.AiHero;
//import org.totalboumboum.ai.v201314.adapter.data.AiSprite;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
//import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v1.Agent;
/**
 * Criteria for danger . When there is a flame or a bombe it will turn the value 0
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
public class Danger extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "Danger";
	/** Domaine de définition  {0 et 1} */
	/**
	 * S’il y a au moins un bombe ou un flamme qui menace la case considéré,
	 *  la valeur de cette critère devient 1. Si non, c’est-à-dire si la case 
	 *  n’est pas en danger, il devient 0.
	 *  
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public Danger(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile)
	{	ai.checkInterruption();

           return tile.getFires().isEmpty();
           }
	}
