package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.util;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v2.AkbulutKupelioglu;

/**
 * @author Yasa Akbulut
 * @author Burcu Küpelioğlu
 */
public class PathUtility
{

	public static AiTile getFirstDestructibleOnPath(AiPath path, AkbulutKupelioglu monIa) throws StopRequestException
	{
		monIa.checkInterruption();
		for(AiTile tile : path.getTiles())
		{
			if(!tile.getBlocks().isEmpty())
				if(tile.getBlocks().get(0).isDestructible())
					return tile;
		}
		return null;
	}
}
