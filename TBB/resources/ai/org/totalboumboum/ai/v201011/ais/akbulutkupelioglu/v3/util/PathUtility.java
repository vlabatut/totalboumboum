package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.util;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.AkbulutKupelioglu;

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
			monIa.checkInterruption();
			if(!tile.getBlocks().isEmpty())
				if(tile.getBlocks().get(0).isDestructible())
					return tile;
		}
		return null;
	}
	public static List<AiTile> getDestructiblesOnPath(AiPath path, AkbulutKupelioglu monIa) throws StopRequestException
	{
		monIa.checkInterruption();
		List<AiTile> result = new ArrayList<AiTile>();
		for(AiTile tile : path.getTiles())
		{
			monIa.checkInterruption();
			if(!tile.getBlocks().isEmpty())
				if(tile.getBlocks().get(0).isDestructible())
					result.add(tile);
		}
		return result;
	}
	public static List<AiTile> getDestructibleNeighboursOnPath(AiPath path, AkbulutKupelioglu monIa) throws StopRequestException
	{
		monIa.checkInterruption();
		List<AiTile> result = new ArrayList<AiTile>();
		for(int i=1; i<path.getLength(); i++)
		{
			if(!path.getTile(i).getBlocks().isEmpty())
				if(path.getTile(i).getBlocks().get(0).isDestructible())
					result.add(path.getTile(i-1));
		}
		return result;
	}
}
