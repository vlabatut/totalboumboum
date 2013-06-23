package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.comparator;

import java.util.Comparator;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v3.AkbulutKupelioglu;

public class TileComparator implements Comparator<AiTile>
{

	AiTile ownTile = null;
	AkbulutKupelioglu monIa;
	public TileComparator(AiTile ownTile, AkbulutKupelioglu ia) throws StopRequestException
	{
		ia.checkInterruption();
		this.ownTile = ownTile;
	}
	@Override
	public int compare(AiTile arg0, AiTile arg1)
	{
		int d0 = Math.abs(arg0.getCol() - ownTile.getCol()) + Math.abs(arg0.getLine() - ownTile.getLine());
		int d1 = Math.abs(arg1.getCol() - ownTile.getCol()) + Math.abs(arg1.getLine() - ownTile.getLine());
		return d0-d1;
	}
	

}
