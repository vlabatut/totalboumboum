package org.totalboumboum.ai.v201011.ais.hacibektasogluilgar.v6;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;

/**
 * @author Engin Hacıbektaşoğlu
 * @author Elif Nurdan İlgar
 */
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
			// 
			e.printStackTrace();
		}
		
		cz.afficherMatrice(cz.matriceTotal, cz.matriceZone);
		
		return result;
	}

}
