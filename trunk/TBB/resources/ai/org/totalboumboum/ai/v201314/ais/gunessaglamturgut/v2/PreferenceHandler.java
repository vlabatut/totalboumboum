package org.totalboumboum.ai.v201314.ais.gunessaglamturgut.v2;

import java.util.*;

import org.totalboumboum.ai.v201314.adapter.agent.AiCategory;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * This class is responsible of the preferences.
 * 
 * @author Neşe Güneş
 * @author Esra Sağlam
 * @author Siyabend Turgut
 */
public class PreferenceHandler extends AiPreferenceHandler<Agent>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected PreferenceHandler(Agent ai)
    {	super(ai);
		ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void resetCustomData()
	{	ai.checkInterruption();
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Set<AiTile> selectTiles()
	{	ai.checkInterruption();

		Set<AiTile> result = new HashSet<AiTile>();

		if (ai.getModeHandler().getMode() == AiMode.ATTACKING) {
			Set<AiTile> tiles = ai.getTileUtil().getEnemyTilesInBombRange(ai.getZone().getOwnHero().getBombRange());
			result.addAll(tiles);
			result.addAll(ai.getTileUtil().getAccessibleTiles());
		} else {
			// TODO Mode collecte
		}

		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CATEGORY					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Name of the first category. */
	private static String VOISINAGE_UN_ENNEMI = "VOISINAGE_UN_ENNEMI";
	/** Name of the first category. */
	private static String NON_PROCHE_UN_ENNEMI = "NON_PROCHE_UN_ENNEMI";
	
	@Override
	protected AiCategory identifyCategory(AiTile tile)
	{	ai.checkInterruption();

		Set<AiTile> accessibles = ai.getTileUtil().getAccessibleTiles(tile);

		for (AiHero aiHero : ai.getZone().getRemainingOpponents()) {
			if (accessibles.contains(aiHero.getTile()))
				return getCategory(VOISINAGE_UN_ENNEMI);
		}
		return getCategory(NON_PROCHE_UN_ENNEMI);
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput()
	{	ai.checkInterruption();
		
		outputWorstPref = false;
		
		super.updateOutput();
	}
}
