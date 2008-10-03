package tournament200809.test;

import fr.free.totalboumboum.ai.adapter200809.AiAction;
import fr.free.totalboumboum.ai.adapter200809.AiActionName;
import fr.free.totalboumboum.ai.adapter200809.ArtificialIntelligence;
import fr.free.totalboumboum.engine.content.feature.Direction;

public class Test extends ArtificialIntelligence
{
	@Override
	public AiAction call() throws Exception
	{	AiActionName name = AiActionName.MOVE;
		Direction direction = Direction.UP;
		AiAction result = new AiAction(name,direction);
		return result;
	}
}
