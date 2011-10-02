package org.totalboumboum.ai.v201011.ais.caliskanseven.v5;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;

/**
 * 
 * @author Mustafa Çalışkan
 * @author Cihan Seven
 *
 */
@SuppressWarnings("deprecation")
public class Util {
	static List<AiTile> getBlastList(CaliskanSeven ai,boolean acs) throws StopRequestException{
		ai.checkInterruption();
		List<AiTile> blast = new ArrayList<AiTile> ();
		for(AiBomb b:ai.getPercepts().getBombs()){
			ai.checkInterruption();
			if(!acs || !b.getBlast().contains(ai.getPercepts().getOwnHero().getTile()))
				blast.addAll(b.getBlast());
		}
		return blast;
	}
}
