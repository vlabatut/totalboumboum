package org.totalboumboum.ai.v201011.ais.caliskanseven.v6;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
/**
 * This class has some static functions that can be helpful in other classes.
 * 
 * @author Mustafa Çalışkan
 * @author Cihan Seven
 *
 */
public class Util {
	
	/**
	 * This function gives a list of all the bomb blasts in the map.
	 * 
	 * @param ai
	 * @param acs attack mode
	 * @return
	 * @throws StopRequestException
	 */
	static List<AiTile> getBlastList(CaliskanSeven ai,boolean acs) throws StopRequestException{
		ai.checkInterruption();
		List<AiTile> blast = new ArrayList<AiTile> ();
		for(AiBomb b:ai.getPercepts().getBombs()){
			ai.checkInterruption();
			
			if(!acs || !b.getBlast().contains(ai.getPercepts().getOwnHero().getTile()))
				blast.addAll(b.getBlast());
		}
		
		if(!acs){
		List<AiHero> heros = ai.getPercepts().getHeroes();
		
		for(AiHero hero : heros){
			ai.checkInterruption();
			List<AiTile> tiles = hero.getTile().getNeighbors();
			tiles.add(hero.getTile());
			
			if(tiles.contains(ai.getPercepts().getOwnHero().getTile()))
				continue;
			
			blast.addAll(tiles);
		}
		}
		
		return blast;
	}
}
