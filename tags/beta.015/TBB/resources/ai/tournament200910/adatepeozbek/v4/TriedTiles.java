package tournament200910.adatepeozbek.v4;

import java.util.ArrayList;
import java.util.List;
import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;

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