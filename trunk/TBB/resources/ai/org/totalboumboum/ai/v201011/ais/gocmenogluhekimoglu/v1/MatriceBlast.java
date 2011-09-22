package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v1;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;

/**
 * @author Can Göçmenoğlu
 * @author Irfan Hekimoğlu
 */
public class MatriceBlast extends MatriceCalc {

	public MatriceBlast(GocmenogluHekimoglu monIa) throws StopRequestException {
		super(monIa);
		// 
	}

	@Override
	public void calculate() throws StopRequestException {
		monIa.checkInterruption();
		// 
		List<AiBomb> bombs = monIa.getPercepts().getBombs();
		for(Iterator<AiBomb> b = bombs.iterator();b.hasNext();){
			monIa.checkInterruption();
			
			AiBomb bomb = (AiBomb)b.next();

			List<AiTile> tiles = bomb.getBlast();
			for(Iterator<AiTile> t = tiles.iterator();t.hasNext();){
				monIa.checkInterruption();
				
				AiTile tile = (AiTile)t.next();
				
				double val = (bomb.getNormalDuration()-bomb.getTime())/1000;
				double singletilewalkduration = tile.getSize() / monIa.getPercepts().getOwnHero().getWalkingSpeed();

				
				val = Math.max(1-(val/(singletilewalkduration*(bomb.getRange()+1))),matrix[tile.getLine()][tile.getCol()]);
				
				
				this.matrix[tile.getLine()][tile.getCol()] = val;
			}
		}
	}

}
