package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4.util;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v4.AkbulutKupelioglu;

/**
 * Contains tools regarding paths.
 * @author yasa
 *
 */
public class PathUtility
{

	/**
	 * Gets the first destructible on a given path. If there is none, returns null.
	 * @param path The path to be considered.
	 * @param monIa The AkbulutKupelioglu using this.
	 * @return The first destructible tile.
	 * @throws StopRequestException
	 */
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
	/**
	 * Gets all the destructibles on a given path. Returns null if there isn't any.
	 * @param path The path to be considered.
	 * @param monIa The AkbulutKupelioglu using this.
	 * @return A list of destructibles on the path.
	 * @throws StopRequestException
	 */
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
	/**
	 * Gets the tiles who are just before each destructible on a given path.
	 * @param path The path to be considered.
	 * @param monIa The AkbulutKupelioglu using this.
	 * @return A list of tiles before the destructibles.
	 * @throws StopRequestException
	 */
	public static List<AiTile> getDestructibleNeighboursOnPath(AiPath path, AkbulutKupelioglu monIa) throws StopRequestException
	{
		monIa.checkInterruption();
		List<AiTile> result = new ArrayList<AiTile>();
		for(int i=1; i<path.getLength(); i++)
		{
			monIa.checkInterruption();
			if(!path.getTile(i).getBlocks().isEmpty())
				if(path.getTile(i).getBlocks().get(0).isDestructible())
					result.add(path.getTile(i-1));
		}
		return result;
	}
}
