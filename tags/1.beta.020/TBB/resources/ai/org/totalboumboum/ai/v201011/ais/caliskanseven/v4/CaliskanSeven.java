package org.totalboumboum.ai.v201011.ais.caliskanseven.v4;

import java.awt.Color;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;

public class CaliskanSeven extends ArtificialIntelligence
{
	AiTile oldtarget =null;
	AiPath oldpath = null;
	@Override
	public AiAction processAction() throws StopRequestException
	{	
		checkInterruption();
		
		
		Matrix mtx = new Matrix();
		PathFinding pfind = new PathFinding();
		
		// Calculation of Collect Matrix and Attack Matrix
		double[][] mtx_collecte = mtx.calculate(this, -50, -50,
				-50, 5, -5, -1, 2);
		double[][] mtx_attaque = mtx.calculate(this, -50, -50,
				-50, 1, -1, 5, 0);
		
		double[][] mtx_final;
		
		// Choosing the matrix to use
		try {
			if(pfind.canReachHeros(this)){
				mtx_final = mtx_attaque;
			}else mtx_final = mtx_collecte;
		} catch (LimitReachedException e) {
			// 
			e.printStackTrace();
			mtx_final = mtx_collecte;
		}
        //Showing the values of the matrix in the gameplay
		for(int l = 0; l<this.getPercepts().getHeight(); ++l){
			for (int c= 0; c<this.getPercepts().getWidth(); ++c){
				this.getOutput().setTileText(l, c, String.valueOf(mtx_final[l][c]));
			}
		}
		
		// Movement & Placing the bombs
		AiTile target = pfind.findTarget(this, mtx_final);
		
		try {
			//Stabilizing the path
			AiPath path = pfind.findPath(this, target, mtx_final);
			
			if(oldtarget == null){
				oldtarget = target;
			}
			if(oldpath == null){
				oldpath = path;
			}
			
			if(target == oldtarget && path.compareTo(oldpath)==0){
				target = oldtarget;
				path = oldpath;
			}
			
			for(AiTile t:path.getTiles()){
				this.getOutput().setTileColor(t, Color.GREEN);
			}
			// Place the bomb when reached,if not continue anyway
			if(path.isEmpty() || path.getLength()==1){
				AiAction result = new AiAction(AiActionName.DROP_BOMB);
				return result;
			}else{
				AiAction result = new AiAction(AiActionName.MOVE,
						this.getPercepts().getDirection(path.getTile(0), path.getTile(1)));
				
				oldpath = path;
				oldtarget = target;
				
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
