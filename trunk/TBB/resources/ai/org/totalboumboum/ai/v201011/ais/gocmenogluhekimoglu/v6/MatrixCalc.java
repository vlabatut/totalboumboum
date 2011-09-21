package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v6;

import java.util.ArrayList;
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
 * This class represents our matrix and matrix calculation functions
 * @author Can G  meno lu
 *
 */
public class MatrixCalc {
	GocmenogluHekimoglu ai;
	Paths paths;
	
	public MatrixCalc(GocmenogluHekimoglu ai,Paths paths) throws StopRequestException{
		ai.checkInterruption();
		this.ai = ai;
		this.paths = paths;
	}
	
	/**
	 * Debug function
	 * @param tiles
	 * @throws StopRequestException
	 */
	void textTileMap(HashMap<AiTile,Double> tiles) throws StopRequestException{
		ai.checkInterruption();
		for(AiTile t:tiles.keySet()){
			ai.checkInterruption();
			ai.getOutput().setTileText(t, String.valueOf(roundTwoDecimals(tiles.get(t),2)));
			ai.getOutput().setBold(true);
		}
	}
	
	/**
	 * Round a double value to c decimal places
	 * @param d
	 * @param c
	 * @return
	 * @throws StopRequestException
	 */
	double roundTwoDecimals(double d, int c) throws StopRequestException {
		ai.checkInterruption();
		int temp=(int)((d*Math.pow(10,c)));
		return (((double)temp)/Math.pow(10,c));
		}

	/**
	 * This function calculates a matrix based on the selected mode (collect or attack),
	 * We used a hashmap for the matrix, so that we don't have to calculate ,
	 * unnecessary tiles in each step (by checking if a tile is,
	 * accesible in the first place), saving lots of CPU time.
	 * @param accesible
	 * @param attack
	 * @return
	 * @throws StopRequestException
	 */
	HashMap<AiTile,Double> calculateMatrix(List<AiTile> accesible,boolean attack) throws StopRequestException{
		ai.checkInterruption();

		HashMap<AiTile,Double> matrix = new HashMap<AiTile,Double>();
		
		//initial values are based on distance
		double distMax = ai.getPercepts().getHeight()+ai.getPercepts().getWidth();
		distMax *= ai.getPercepts().getPixelHeight()/ai.getPercepts().getHeight();
		AiHero h = ai.getPercepts().getOwnHero();
		for(AiTile t:accesible){
			ai.checkInterruption();
			double w = (distMax-ai.getPercepts().getPixelDistance(t.getPosX(), t.getPosY(), h.getPosX(), h.getPosY()))/distMax;
			w*=1;
			matrix.put(t, w);
		}
		
		//give negative values for bombs and flames
		for(AiBomb b:ai.getPercepts().getBombs()){
			ai.checkInterruption();
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
			int enemdist = 10000;
			AiHero nearestEnemy = null;
			for(AiHero enemy:ai.getPercepts().getHeroes()){
				ai.checkInterruption();
				if(enemy.equals(h) || enemy.hasEnded())
					continue;
				
				int tdist = paths.tileDistNonCyc(enemy.getTile(), h.getTile());
				if(tdist<enemdist){
					nearestEnemy = enemy;
					enemdist = tdist;
				}
			}
			
			// find his accesible tiles
			
			
			double distMaxBlock = ai.getPercepts().getHeight()+ai.getPercepts().getWidth();
			// tiles that are neighbors to destructible walls are given  5  points
			List<AiTile> blocktiles = new ArrayList<AiTile>();
			for(AiBlock b:ai.getPercepts().getDestructibleBlocks()){
				ai.checkInterruption();
				for(AiTile t:b.getTile().getNeighbors()){
					ai.checkInterruption();
					if(matrix.containsKey(t)){
						if(!blocktiles.contains(t)){
							matrix.put(t, matrix.get(t)+0.1);
							blocktiles.add(t);
						}
						
					}
				}
			}
			
			if(nearestEnemy != null){
				List<AiTile> enemEscapeTiles = paths.accesibleTiles( nearestEnemy, false, false);
				AiTile targetTile = enemEscapeTiles.get(enemEscapeTiles.size()/2);
			
				for(AiTile t:matrix.keySet()){
					ai.checkInterruption();
					// add a bias based on their distance to the farthest enemy
					if(nearestEnemy != null){
						
						AiTile et = targetTile;
						double w = (distMaxBlock-paths.tileDistNonCyc(t,et));
						w*=1;
						matrix.put(t, matrix.get(t)+w);
					}
				}
			}
			
			
			
			// bonuses are given 10  points
			for(AiItem i:ai.getPercepts().getItems()){
				ai.checkInterruption();
				double w = 0.1;
				if(i.getType().name() == AiItemType.MALUS.name())
					w=-1;
				if(matrix.containsKey(i.getTile()))
					matrix.put(i.getTile(), matrix.get(i.getTile())+w);
			}
		}
		
		return matrix;
		
	}
}
