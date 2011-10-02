package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v3;

import java.awt.Color;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.PixelHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.MatrixCostCalculator;

/**
 * @author Can Göçmenoğlu
 * @author Irfan Hekimoğlu
 */
@SuppressWarnings("deprecation")
public class PathCalculator {
	private GocmenogluHekimoglu monIa;
	private PixelHeuristicCalculator heurcalc;
	private MatrixCostCalculator costcalc;
	private Astar astar;
	private AiTile target;
	private AiPath path;
	private MatriceCalc matrix;
	
	/**
	 * 
	 * @param ia
	 * @param matrix
	 * @throws StopRequestException
	 */
	PathCalculator(GocmenogluHekimoglu ia,MatriceCalc matrix) throws StopRequestException {
		ia.checkInterruption();
		
		this.monIa = ia;
		this.heurcalc = new PixelHeuristicCalculator();
		this.costcalc = new MatrixCostCalculator(matrix.getMatrix());
		this.matrix = matrix;
		this.astar = new Astar(this.monIa, 
				this.monIa.getPercepts().getOwnHero(),
				this.costcalc,
				this.heurcalc);
		this.path = null;
		this.findTarget();
	}
	
	AiTile getTarget(){
		return this.target;
	}
	
	AiPath getPath(){
		return this.path;
	}
	
	static void showPath(GocmenogluHekimoglu monIa,AiPath path) throws StopRequestException{
		monIa.checkInterruption();
		
		
		if(path != null){
			AiTile target = path.getLastTile();
			for(AiTile tile: path.getTiles()){				
				if(tile == path.getLastTile()){
					monIa.getOutput().setTileColor(target.getLine(),target.getCol(), new Color(255,0,255));
				}else{
					monIa.getOutput().setTileColor(tile.getLine(),tile.getCol(), new Color(0,0,255));
				}
			}
		}
	}
	 
	
	/**
	 * ce fait une recherche A*, de trouver le chemin vers le cas le plus apprécié dans la matrice donnée.
	 * @throws StopRequestException
	 */
	void findTarget() throws StopRequestException{
		monIa.checkInterruption();
		
		double mtxcpy[][] = matrix.getMatrix();
		int maxi,maxj;
		int counter = 0;
		maxi = 0;
		maxj = 0;
		
		int map[][] = MatriceCalc.reachMatrix(monIa);
		AiTile mtile = monIa.getPercepts().getOwnHero().getTile();
		map = MatriceCalc.freeWalk(monIa, map, mtile.getLine(), mtile.getCol(), matrix.zoneh, matrix.zonew);
		/*
		for(int i=0;i<matrix.zoneh;i++){
			for(int j=0;j< matrix.zonew;j++){
				if(map[i][j]==2){
					monIa.getOutput().setTileColor(i,j, new Color(0,255,0));
				}
			}
		}
		*/
		while(true){
			if(counter++ >= 1){
				path = null;
				break;
			}
			
			double maxval = mtxcpy[mtile.getLine()][mtile.getCol()];
			maxi = mtile.getLine();
			maxj = mtile.getCol();
			for(int i = 0;i<matrix.zoneh;i++){
				for(int j = 0;j<matrix.zonew;j++){
					monIa.checkInterruption();
					
					if(map[i][j] == 2){
						if(mtxcpy[i][j]>maxval){
							maxval = mtxcpy[i][j];
							maxi = i;
							maxj = j;
						}
					}
					
				}
			}
		
			try {
				path = astar.processShortestPath(monIa.getPercepts().getOwnHero().getTile(),
						monIa.getPercepts().getTile(maxi, maxj));
				
				
				if(path.getTiles().size()>0){
					break;
				}else{
					path = null;
					System.out.printf("ASTAR COUNTER:%d UNREACHABLE L:%d C:%d \n", counter,maxi,maxj);
					mtxcpy[maxi][maxj] = -1000;
				}
			} catch (LimitReachedException e) {
				// 				e.printStackTrace();
			}
		}
		
		target = monIa.getPercepts().getTile(maxi, maxj);
	}
	
}
