package org.totalboumboum.ai.v200910.ais.adatepeozbek.v5c;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;

/**
 * 
 * @version 5.c
 * 
 * @author Can Adatape
 * @author Sena Özbek
 *
 */
@SuppressWarnings("deprecation")
public class TriedTiles
{	/** */
	public List<AiTile> visited = null;
	/** */
	private AdatepeOzbek ownAi;
	
	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public TriedTiles(AdatepeOzbek ai)throws StopRequestException
	{	ai.checkInterruption();
		ownAi = ai;
		ownAi.checkInterruption();
		visited = new ArrayList<AiTile>();
	}
	
	/**
	 * 
	 * @param tile
	 * @throws StopRequestException
	 */
	public void add(AiTile tile) throws StopRequestException
	{
		ownAi.checkInterruption();
		if(!visited.contains(tile))
			visited.add(tile);
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 */
	public void reset() throws StopRequestException
	{
		ownAi.checkInterruption();
		visited.clear();
	}
		
}