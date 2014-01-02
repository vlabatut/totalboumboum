package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v3.criterion;


import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v3.Agent;

/**
 * DestructableWall
 * On regarde si il y a des mur destructible a cote de nos murs. 
 * 
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
public class DestructableWall extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "DestructableWall";
	
	/**
	 * Domaine de definition = { true , false }
	 * Si il y a au moins un mur destructible il prend la valeur faux
	 * sinon il prend la valeur true
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public DestructableWall (Agent ai)
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
				for(AiTile Tile: tile.getNeighbors()){
					ai.checkInterruption();
					if(!Tile.getBlocks().isEmpty()&& Tile.getBlocks().get(0).isDestructible()){		
						result=false;
			}
		}		
		return result;
	}
}
