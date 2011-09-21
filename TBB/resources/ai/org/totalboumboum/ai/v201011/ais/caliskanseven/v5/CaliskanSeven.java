package org.totalboumboum.ai.v201011.ais.caliskanseven.v5;

import java.awt.Color;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;

@SuppressWarnings("unused")
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
		
		List<AiTile> closed = pfind.listEscapeBomb(this);
		
		// Calculation of Collect Matrix and Attack Matrix
		double[][] mtx_collecte = mtx.calculate(this, -50, -50,
				-50, 1, -5, -1, 5,closed,0);
		double[][] mtx_attaque = mtx.calculate(this, -50, -50,
				-50, 0, -1, 5, 0,closed,0);
		
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
				checkInterruption();
				this.getOutput().setTileText(l, c, String.valueOf(Math.round(mtx_final[l][c])));
			}
		}
		
		// Movement & Placing the bombs
		AiTile target = pfind.findTarget(this, mtx_final);
		//BUNU S L
		
		try {
			//Stabilizing the path
			AiPath path = pfind.findPath(this, target, mtx_final);
			
			if(oldtarget == null){
				oldtarget = target;
			}
			if(oldpath == null){
				oldpath = path;
			}
			
			double t_v = mtx_final[target.getLine()][target.getCol()];
			double ot_v = mtx_final[oldtarget.getLine()][oldtarget.getCol()];
			
			/*
			if(Math.abs(t_v-ot_v)<0.1){
				if(target.getCol()<oldtarget.getCol()){
					target = oldtarget;
					path = oldpath;
				}else if(target.getLine()<oldtarget.getLine()){
					target = oldtarget;
					path = oldpath;
				}
			}*/
			
			if(target == oldtarget && (path.compareTo(oldpath)==0)){
				target = oldtarget;
				path = oldpath;
			}
			
			for(AiTile t:closed){
				checkInterruption();
				this.getOutput().setTileColor(t, Color.GREEN);
			}
			/*
			for(AiTile t:path.getTiles()){
				this.getOutput().setTileColor(t, Color.GREEN);
			}*/
			// Place the bomb when reached,if not continue anyway
			if( path.isEmpty() || path.getLength()==1){
				AiAction result = null;
				if(pfind.canEscapeBomb(closed))
					result = new AiAction(AiActionName.DROP_BOMB);
				else 
					result = new AiAction(AiActionName.NONE);
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
