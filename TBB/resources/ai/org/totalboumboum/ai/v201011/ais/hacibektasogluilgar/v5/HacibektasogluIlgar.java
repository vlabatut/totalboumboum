package org.totalboumboum.ai.v201011.ais.hacibektasogluilgar.v5;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;

public class HacibektasogluIlgar extends ArtificialIntelligence {
	
	 AiZone zone;
	 AiHero notreHero;
	
	public AiAction processAction() throws StopRequestException {
		// avant tout : test d'interruption
		checkInterruption();
		AiAction result= new AiAction(AiActionName.NONE);
		
		zone = getPercepts();
		notreHero=zone.getOwnHero();
		
		CalculeZone cz=new CalculeZone(this);
		Action act=new Action(this, cz.matriceZone, cz.matriceTotal, cz);
		cz.misAJourEtatsMatrice(cz.matriceZone);
		try {
			result=act.choisirLeMode(cz.matriceZone);
		} catch (LimitReachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cz.afficherMatrice(cz.matriceTotal);
		
		return result;
	}

}
