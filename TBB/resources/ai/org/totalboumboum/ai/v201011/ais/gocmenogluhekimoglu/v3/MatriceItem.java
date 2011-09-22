package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v3;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiItemType;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;

/**
 * @author Can Göçmenoğlu
 * @author Irfan Hekimoğlu
 */
public class MatriceItem extends MatriceCalc {

	boolean malus;
	
	public MatriceItem(GocmenogluHekimoglu monIa,boolean malus) throws StopRequestException {
		super(monIa);
		
		this.malus = malus;
		// 
	}
	
	/**
	 * donner des cas avec des bonus 1, autres 0
	 */
	@Override
	public void calculate() throws StopRequestException {
		monIa.checkInterruption();
		// 
		
		List<AiItem> items = monIa.getPercepts().getItems();
		for(Iterator<AiItem> i = items.iterator();i.hasNext();){
			monIa.checkInterruption();
			
			AiItem item = (AiItem)i.next();
			
			if(malus){
				if(item.getType() != AiItemType.MALUS)
					break;
			}else{
				if(item.getType() != AiItemType.EXTRA_BOMB && item.getType() != AiItemType.EXTRA_FLAME)
					break;
			}
			
			AiTile tile = item.getTile();
			
			double maxdist = monIa.getPercepts().getWidth() +  monIa.getPercepts().getHeight();
			double dist = monIa.getPercepts().getTileDistance(monIa.getPercepts().getOwnHero().getTile(), item.getTile());
			dist = 1-dist/maxdist;
			
			this.matrix[tile.getLine()][tile.getCol()] = dist;
		}
		
	}
}
