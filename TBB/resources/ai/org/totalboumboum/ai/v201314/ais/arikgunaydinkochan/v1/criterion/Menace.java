package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v1.criterion;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v1.Agent;

/**
 * Cette classe est un critère menace
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class Menace extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "Menace";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public Menace(Agent ai)
	{	super(ai,NAME,0,2);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile){	
		ai.checkInterruption();
		
		AiZone zone = ai.getZone();
		List<AiBomb> bombs = new ArrayList<AiBomb>();
		List<AiFire> fires = new ArrayList<AiFire>();
		
		List<AiTile> blasttiles = new ArrayList<AiTile>();
		List<AiTile> flametiles = new ArrayList<AiTile>();
	
		//
			
		fires = zone.getFires();
		
		for(AiFire item : fires){
			ai.checkInterruption();
			flametiles.add(item.getTile());
		}
			
			if(flametiles.contains(tile)){
				return 2;
				}		
		//		
		bombs = zone.getBombs();
		
		for( AiBomb item : bombs  ){
			ai.checkInterruption();
			blasttiles.addAll(item.getBlast()) ;	
		}
				
			if(blasttiles.contains(tile)){
				return 1;
				}		
		return 0;
	}
}
