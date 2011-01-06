package org.totalboumboum.ai.v201011.ais.hacibektasogluilgar.v1;


import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

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
	 int width;
	 int height;
	 Etats matriceCollecte[][];

	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		
		AiAction result= new AiAction(AiActionName.NONE);
		
		zone = getPercepts();
		height=zone.getHeight();
		width=zone.getWidth();
		matriceCollecte = new Etats[height][width];
		
		
		Maps map = new Maps(this, zone, matriceCollecte);
		
					
		matriceCollecte=map.misAJourEtatsMatrice(matriceCollecte);
		result=map.calculeTotalDeMatrice(matriceCollecte);
		map.afficherMatrice(map.matriceTotal);


		return result;
	}
	
	
	


	
}
