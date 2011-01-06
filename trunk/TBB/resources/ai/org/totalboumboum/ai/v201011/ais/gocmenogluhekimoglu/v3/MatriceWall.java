package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v3;

import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiFloor;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;

public class MatriceWall extends MatriceCalc {

	public MatriceWall(GocmenogluHekimoglu monIa) throws StopRequestException {
		super(monIa);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Pour la destruction des murs dans la "mode collecte". nous donnons cas près des murs destructibles 1, les autres 0.
	 */
	public void calculate() throws StopRequestException {
		monIa.checkInterruption();
		
		List<AiFloor> floors = monIa.getPercepts().getFloors();
		
		for(AiFloor floor:floors){
			List<AiBlock> blocks = floor.getTile().getBlocks();
			for(AiBlock block:blocks){
				if(block.isDestructible()){
					List<AiTile> tiles = block.getTile().getNeighbors();
					for(AiTile tile:tiles){
						this.matrix[tile.getLine()][tile.getCol()] = 1;
					}
				}
			}
		}
	}
}
