package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v1;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

public class AkbulutKupelioglu extends ArtificialIntelligence
{	
	private AiZone zone;
	private AiOutput output;
	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		
		zone = getPercepts();
		output = getOutput();
		
		Mode mode = ModeCollecte.getInstance(zone,this);
		Matrix interet = mode.calculateMatrix();
		
		int i,j;
		for(i = 0; i<interet.getHeight(); i++)
		{
			checkInterruption();
			for(j=0; j<interet.getWidth(); j++)
			{
				checkInterruption();
				output.setTileText(i, j, Integer.toString(interet.getElement(i, j)));
			}
		}
		
		AiAction result = new AiAction(AiActionName.NONE);
		
		interet.resetMatrix();
		return result;
	}
}
