package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v3.criterion;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v3.Agent;

/**
 * Cette classe est un critère menace
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class Menace extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "Menace";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public Menace(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Boolean processValue(AiTile tile){	
		ai.checkInterruption();

		for (AiItem item: tile.getItems()) {
			ai.checkInterruption();		
			if(ai.isMalus(item))
				return false;
		}
		ArrayList<AiTile> tiles =new ArrayList<AiTile>();
		tiles.addAll(this.ai.dangerousTiles());
		if(!(tiles.isEmpty()) && (tiles.contains(tile))) {
			return false;
		}		
		return true;		
	}
}
