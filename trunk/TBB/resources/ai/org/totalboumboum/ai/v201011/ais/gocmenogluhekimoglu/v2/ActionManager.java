package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v2;

import java.awt.Color;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * @author Can Göçmenoğlu
 * @author Irfan Hekimoğlu
 */
@SuppressWarnings({ "unused", "deprecation" })
public class ActionManager {
	protected GocmenogluHekimoglu monIa;
	MatriceCalc matrix;
	Astar astar;
	MyHeuristicCalculator heurcalc;
	BasicCostCalculator costcalc;
	AiTile target;
	AiPath path;
	
	ActionManager(GocmenogluHekimoglu ia) throws StopRequestException {
		this.monIa = ia;
	}
	
	void loadMatrix(MatriceCalc matrix,MatriceCalc blastmtx) throws StopRequestException{
		monIa.checkInterruption();
		this.matrix = matrix;
		this.heurcalc = new MyHeuristicCalculator();//MyHeuristicCalculator();
		this.heurcalc.blastmatrix = blastmtx.getMatrix();
		this.costcalc = new BasicCostCalculator();//MatrixCostCalculator(matrix.getMatrix());
		this.astar = new Astar(this.monIa, 
				this.monIa.getPercepts().getOwnHero(),
				this.costcalc,
				this.heurcalc);
	}
	
	AiTile findTarget() throws StopRequestException{
		double mtxcpy[][] = matrix.getMatrix();
		int maxi,maxj;
		int counter = 0;
		maxi = 0;
		maxj = 0;
		
		int map[][] = MatriceCalc.reachMatrix(monIa);
		AiTile mtile = monIa.getPercepts().getOwnHero().getTile();
		map = MatriceCalc.freeWalk(monIa, map, mtile.getLine(), mtile.getCol(), matrix.zoneh, matrix.zonew);
		
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
					System.out.printf("ASTAR COUNTER:%d UNREACHABLE L:%d C:%d \n", counter,maxi,maxj);
					mtxcpy[maxi][maxj] = -1000;
				}
			} catch (LimitReachedException e) {
				// 
				e.printStackTrace();
			}
		}
		
		return target = monIa.getPercepts().getTile(maxi, maxj);
	}
	
	Direction getTileDirection(AiTile t1,AiTile t2){
		int t1x = t1.getCol();
		int t1y = t1.getLine();
		int t2x = t2.getCol();
		int t2y = t2.getLine();
		
		if(t2x > t1x){
			return Direction.RIGHT;
		}else if(t2x < t1x){
			return Direction.LEFT;
		}else if(t1y > t2y){
			return Direction.UP;
		}else if(t1y < t2y){
			return Direction.DOWN;
		}else return Direction.NONE;
		
	}
	
	AiAction followPath() throws StopRequestException{
		monIa.checkInterruption();
		/*
		for(AiTile tile: path.getTiles()){				
			if(tile == path.getLastTile()){
				monIa.getOutput().setTileColor(target.getLine(),target.getCol(), new Color(255,0,255));
			}else{
				monIa.getOutput().setTileColor(tile.getLine(),tile.getCol(), new Color(0,0,255));
			}
		}*/
		
		int map[][] = MatriceCalc.reachMatrix(monIa);
		AiTile mtile = monIa.getPercepts().getOwnHero().getTile();
		map = MatriceCalc.freeWalk(monIa, map, mtile.getLine(), mtile.getCol(), matrix.zoneh, matrix.zonew);
		
		
		for(int i=0;i<matrix.zoneh;i++){
			for(int j=0;j< matrix.zonew;j++){
				if(map[i][j]==2){
					monIa.getOutput().setTileColor(i,j, new Color(0,255,0));
				}
			}
		}
		
		if(path==null){
			return new AiAction(AiActionName.NONE);
		}
		
		
		for(AiTile tile: path.getTiles()){				
			if(tile == path.getLastTile()){
				monIa.getOutput().setTileColor(target.getLine(),target.getCol(), new Color(255,0,0));
			}else{
				monIa.getOutput().setTileColor(tile.getLine(),tile.getCol(), new Color(0,0,255));
			}
		}
		
		map = MatriceCalc.fakeReach(monIa);
		mtile = monIa.getPercepts().getOwnHero().getTile();
		map = MatriceCalc.freeWalk(monIa, map, mtile.getLine(), mtile.getCol(), matrix.zoneh, matrix.zonew);
		boolean bombsafe = MatriceCalc.canFreeWalkBomb(monIa, map, mtile.getLine(), mtile.getCol());
			if(path!=null && path.getTiles().size() > 1){
				while(path.getTile(0)!=mtile){
					path.removeTile(0);
				}
				/*
				if(MatriceHero.heroesOnSight(monIa)){
					monIa.lastbomb = System.currentTimeMillis();
					return new AiAction(AiActionName.DROP_BOMB);
				}*/
				return new AiAction(AiActionName.MOVE,getTileDirection(path.getTile(0),path.getTile(1)));
			}else {
				
				if(bombsafe){
					if(Math.random() > 0.95){
						//if(MatriceHero.heroesOnSight(monIa)){
							monIa.lastbomb = System.currentTimeMillis();
							return new AiAction(AiActionName.DROP_BOMB);
						//}else{
						//	return new AiAction(AiActionName.NONE);
						//}
					}else{
						return new AiAction(AiActionName.NONE);
					}
				}else{
					return new AiAction(AiActionName.NONE);
				}
				 
			} 
			
	}
}
