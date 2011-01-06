package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v1;

import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

@SuppressWarnings("unused")
public class Walls 
{
	private AiZone zone; // La zone de jeu de cette instance
	private final int WALL = 40;
	
	public void fillMatrix(int [][] matrix) throws StopRequestException
	{
		
		Collection<AiBlock> block = zone.getBlocks();
		Iterator<AiBlock> iteratorBlock = block.iterator();
		while (iteratorBlock.hasNext()) 
		{
			AiBlock blocks = iteratorBlock.next();
			matrix[blocks.getLine()][blocks.getCol()] = WALL;
			
		}
	}

}
