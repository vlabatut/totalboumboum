package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v3.criterion;

//import java.util.ArrayList;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
//import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v3.Agent;

/**
 * Cette classe est un critère securitezone
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class SecuriteZone extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "SecuriteZone";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public SecuriteZone(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Boolean processValue(AiTile tile){	
		ai.checkInterruption();
		
		ArrayList<AiTile> blastTiles = new ArrayList<AiTile>();
		ArrayList<AiTile> controlTiles = new ArrayList<AiTile>();
		ArrayList<AiBomb> bombs = new ArrayList<AiBomb>();
		for(AiBomb b : bombs){
			ai.checkInterruption();
			blastTiles.addAll(b.getBlast());
		}
		
		int tileColumn=tile.getCol();
		int tileRow=tile.getRow();
		
		boolean securiteZone=false;
		
		controlTiles.addAll(ai.accessibleTiles);
		controlTiles.removeAll(blastTiles);
		
		for(AiTile t: controlTiles){
			ai.checkInterruption();
			
			int tC=t.getCol();
			int tR=t.getRow();
			if(( tC==tileColumn || tR==tileRow)){
				;
			}
			else 
				securiteZone=true;
			
		}		
		return securiteZone;
		
	}
}
