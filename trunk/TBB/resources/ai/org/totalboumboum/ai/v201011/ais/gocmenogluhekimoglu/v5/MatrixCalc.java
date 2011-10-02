package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v5;

import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiItemType;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;

/**
 * @author Can Göçmenoğlu
 * @author Irfan Hekimoğlu
 */
@SuppressWarnings("deprecation")
public class MatrixCalc {
	static void textTileMap(GocmenogluHekimoglu ai,HashMap<AiTile,Double> tiles) throws StopRequestException{
		ai.checkInterruption();
		for(AiTile t:tiles.keySet()){
			ai.getOutput().setTileText(t, String.valueOf(Math.round(tiles.get(t))));
		}
	}
	
	static HashMap<AiTile,Double> calculateMatrix(GocmenogluHekimoglu ai,List<AiTile> accesible,boolean attack) throws StopRequestException{
		ai.checkInterruption();
		
		HashMap<AiTile,Double> matrix = new HashMap<AiTile,Double>();
		
		//initial values are based on distance
		double distMax = ai.getPercepts().getHeight()+ai.getPercepts().getWidth();
		distMax *= ai.getPercepts().getPixelHeight()/ai.getPercepts().getHeight();
		AiHero h = ai.getPercepts().getOwnHero();
		for(AiTile t:accesible){
			ai.checkInterruption();
			double w = (distMax-ai.getPercepts().getPixelDistance(t.getPosX(), t.getPosY(), h.getPosX(), h.getPosY()))/distMax;
			w*=10;
			matrix.put(t, w);
		}
		
		//give negative values for bombs and flames
		for(AiBomb b:ai.getPercepts().getBombs()){
			for(AiTile t:b.getBlast()){
				ai.checkInterruption();
				if(matrix.containsKey(t))
					matrix.put(t, matrix.get(t)-50.0);
			}
		}
		
		for(AiFire f:ai.getPercepts().getFires()){
			ai.checkInterruption();
			if(matrix.containsKey(f.getTile()))
				matrix.put(f.getTile(), matrix.get(f.getTile())-50.0);
		}
		
		// choose mode
		if(attack){
			for(AiHero enemy:ai.getPercepts().getHeroes()){
				ai.checkInterruption();
				if(enemy.equals(ai.getPercepts().getOwnHero()) || enemy.hasEnded())
					continue;
				
				for(AiTile t:enemy.getTile().getNeighbors()){
					ai.checkInterruption();
					if(matrix.containsKey(t))
						matrix.put(t, matrix.get(t)+5.0);
				}
			}
		}else{
			//find n enemy
			int enemdist = 9999;
			AiHero nearestEnemy = null;
			for(AiHero enemy:ai.getPercepts().getHeroes()){
				ai.checkInterruption();
				if(enemy.equals(h) || enemy.hasEnded())
					continue;
				
				int tdist = ai.getPercepts().getTileDistance(enemy, h);
				if(tdist<enemdist){
					nearestEnemy = enemy;
					enemdist = tdist;
				}
			}
			
			double distMaxBlock = ai.getPercepts().getHeight()+ai.getPercepts().getWidth();
			// tiles that are neighbors to destructible walls are given  5  points
			for(AiBlock b:ai.getPercepts().getDestructibleBlocks()){
				for(AiTile t:b.getTile().getNeighbors()){
					ai.checkInterruption();
					if(matrix.containsKey(t)){
						matrix.put(t, matrix.get(t)+2.0);
						
						// add a bias based on their distance to the farthest enemy
						if(nearestEnemy != null){
							AiTile et = nearestEnemy.getTile();
							double w = (distMaxBlock-ai.getPercepts().getTileDistance(t,et))/distMaxBlock;
							w*=5;
							matrix.put(t, matrix.get(t)+w);
						}
					}
				}
			}
			
			// bonuses are given 10  points
			for(AiItem i:ai.getPercepts().getItems()){
				ai.checkInterruption();
				double w = 1;
				if(i.getType().name() == AiItemType.MALUS.name())
					w=-1;
				if(matrix.containsKey(i.getTile()))
					matrix.put(i.getTile(), matrix.get(i.getTile())+w);
			}
		}
		
		return matrix;
		
	}
}
