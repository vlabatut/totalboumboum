package tournament200809.demiragsagar.v2;

import fr.free.totalboumboum.ai.adapter200809.AiAction;
import fr.free.totalboumboum.ai.adapter200809.AiActionName;
import fr.free.totalboumboum.ai.adapter200809.ArtificialIntelligence;
import fr.free.totalboumboum.ai.adapter200809.StopRequestException;

public class DemiragSagar extends ArtificialIntelligence
{
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		
		AiAction result = new AiAction(AiActionName.NONE);
		return result;
	}
}