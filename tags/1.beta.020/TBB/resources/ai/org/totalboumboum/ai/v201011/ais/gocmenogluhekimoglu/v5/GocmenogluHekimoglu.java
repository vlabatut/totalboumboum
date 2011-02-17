package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v5;

import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;

public class GocmenogluHekimoglu extends ArtificialIntelligence
{
	boolean force_collect = false;
	@Override
	public AiAction processAction() throws StopRequestException
	{	
		this.checkInterruption();
		boolean attack = false;
		List<AiTile> tiles = Paths.accesibleTiles(this, this.getPercepts().getOwnHero(), false, true);
		
		if(Paths.areEnemiesAccesible(this,tiles) && !force_collect)
			attack=true;
		
		HashMap<AiTile,Double> matrix = MatrixCalc.calculateMatrix(this, tiles, attack);
		//MatrixCalc.textTileMap(this, matrix);
		//Paths.colorTiles(this, tiles, Color.GREEN);
		AiTile targetTile = Paths.kthHighestTile(this, matrix, 1);
		
		try {
			AiPath path = Paths.findPath(this, targetTile);
			
			if( path.isEmpty() || path.getLength()==1){
				AiAction result = null;
				List<AiTile> escapeTiles = Paths.accesibleTiles(this, this.getPercepts().getOwnHero(), true, true);
				if(escapeTiles.size()>0){
					result = new AiAction(AiActionName.DROP_BOMB);
					force_collect = false;
				}else {
					result = new AiAction(AiActionName.NONE);
					if(attack){
						force_collect = true;
					}
				}
				
				return result;
			}else{
				AiAction result = new AiAction(AiActionName.MOVE,
						this.getPercepts().getDirection(path.getTile(0), path.getTile(1)));
				force_collect = false;
				return result;
			}
			
		} catch (LimitReachedException e) {
			// 
			e.printStackTrace();
		}
		
		AiAction result = new AiAction(AiActionName.NONE);
		return result;
	}
	
}
