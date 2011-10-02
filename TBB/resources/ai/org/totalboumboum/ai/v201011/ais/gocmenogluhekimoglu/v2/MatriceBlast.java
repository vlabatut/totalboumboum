package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v2;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiSprite;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.engine.content.manager.explosion.FullExplosionManager;

/**
 * @author Can Göçmenoğlu
 * @author Irfan Hekimoğlu
 */
@SuppressWarnings({ "unused", "deprecation" })
public class MatriceBlast extends MatriceCalc {

	public MatriceBlast(GocmenogluHekimoglu monIa) throws StopRequestException {
		super(monIa);
		// 
	}
	
	static public List<AiTile> fakeBlast(GocmenogluHekimoglu monIa,int line,int col) throws StopRequestException{
		monIa.checkInterruption();  
		//AiBomb b = new AiBomb(monIa.getPercepts().getTile(line, col), null);
		return null;
		
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
					
					double val = (bomb.getNormalDuration()-bomb.getTime()-1000)/1000;
					double singletilewalkduration = tile.getSize() / monIa.getPercepts().getOwnHero().getWalkingSpeed();
	
					
					val = Math.max(1-(val/(singletilewalkduration*(bomb.getRange()+1))),matrix[tile.getLine()][tile.getCol()]);
					
					
					this.matrix[tile.getLine()][tile.getCol()] = 1;
				}
			
			
		}
		
		List<AiFire> flames = monIa.getPercepts().getFires();
		for(AiFire fire:flames){
			this.matrix[fire.getLine()][fire.getCol()] = 1;
		}
	}

}
