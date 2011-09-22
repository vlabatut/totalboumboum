package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v1;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

/**
 * @author Can Göçmenoğlu
 * @author Irfan Hekimoğlu
 */
public class GocmenogluHekimoglu extends ArtificialIntelligence
{	
	
	
	public GocmenogluHekimoglu(){
		
	}
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		
		
		MatriceCalc collecte = new MatriceCalc(this); 
		
		MatriceBlast mblast = new MatriceBlast(this);
		MatriceItem mbon = new MatriceItem(this,false);
		MatriceItem mmal = new MatriceItem(this,true);
		MatriceHero mhero = new MatriceHero(this);
		
		mblast.calculate();
		mbon.calculate();
		mmal.calculate();
		mhero.calculate();

		collecte.addWithWeight(mblast, -10);
		collecte.addWithWeight(mbon, 2);
		collecte.addWithWeight(mmal, -5);
		collecte.addWithWeight(mhero, -5);
		
		collecte.afficheColor();
		collecte.afficheText();
		
		return new AiAction(AiActionName.NONE);
	}
	
}
