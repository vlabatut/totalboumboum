package org.totalboumboum.ai.v201011.ais.hacibektasogluilgar.v2;

import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;



/**
 * 
 * on calcule la matrice collecte
 * 
 * @author Elif Nurdan Ýlgar && Engin Hacýbektaþoðlu
 *
 */
public class HacibektasogluIlgar extends ArtificialIntelligence
{	
	 AiZone zone;
	 AiHero monAi;
	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA 
	 * @throws LimitReachedException */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		
		AiAction result= new AiAction(AiActionName.NONE);
		
		zone = getPercepts();		
		
		Maps map = new Maps(this);
		map.misAJourEtatsMatrice(map.matriceCollecte);
		

		try {
			result=map.choisirLeMode(map.matriceCollecte);
		} catch (LimitReachedException e) {
			// 
			e.printStackTrace();
		}
		
		map.afficherMatrice(map.matriceTotal);
		
		return result;
	}
	
}
