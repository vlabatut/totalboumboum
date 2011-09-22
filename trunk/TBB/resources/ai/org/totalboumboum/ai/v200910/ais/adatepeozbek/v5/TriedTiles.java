package org.totalboumboum.ai.v200910.ais.adatepeozbek.v5;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;

/**
 * 
 * @version 5
 * 
 * @author Can Adatape
 * @author Sena Özbek
 *
 */
public class TriedTiles
{
	public List<AiTile> visited = null;
	private AdatepeOzbek ownAi;
	public TriedTiles(AdatepeOzbek ai)throws StopRequestException
	{
		ownAi = ai;
		ownAi.checkInterruption();
		visited = new ArrayList<AiTile>();
	}
	
	public void add(AiTile tile) throws StopRequestException
	{
		ownAi.checkInterruption();
		if(!visited.contains(tile))
			visited.add(tile);
	}
	
	public void reset() throws StopRequestException
	{
		ownAi.checkInterruption();
		visited.clear();
	}
		
}