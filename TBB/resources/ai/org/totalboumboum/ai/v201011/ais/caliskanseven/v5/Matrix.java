package org.totalboumboum.ai.v201011.ais.caliskanseven.v5;

import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiFloor;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiItemType;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;

/**
 * @author Mustafa Çalışkan
 * @author Cihan Seven
 */
@SuppressWarnings("deprecation")
public class Matrix {
	public double[][] calculate(CaliskanSeven ai,int wfire,
			int wblast,int wbomb,int wbonus,int wmalus,
			int whero,int wwall,List<AiTile> closed,int wclosed) throws StopRequestException{
		ai.checkInterruption();
		
		int l,c;
		l = ai.getPercepts().getHeight();
		c = ai.getPercepts().getWidth();
		double mtx[][] = new double[l][c];
		
		// Closed Tiles
		for(AiTile t:closed){
			ai.checkInterruption();
			mtx[t.getLine()][t.getCol()] += wclosed;
		}
		
		// Bonus Malus
		List<AiItem> items = ai.getPercepts().getItems();
		for(AiItem item:items){
			ai.checkInterruption();
			
			if(item.getType().name() == AiItemType.MALUS.name()){
				mtx[item.getLine()][item.getCol()] += wmalus;
			} else mtx[item.getLine()][item.getCol()] += wbonus;
		}
		
		// Bomb
		List<AiBomb> bombs = ai.getPercepts().getBombs();
		for(AiBomb bomb:bombs){
			ai.checkInterruption();
			
			mtx[bomb.getLine()][bomb.getCol()] += wbomb;
			List<AiTile> blast = bomb.getBlast();
			for(AiTile b:blast){
				ai.checkInterruption();
				
				mtx[b.getLine()][b.getCol()] += wblast;
			}
		}
		
		List<AiFire> flames = ai.getPercepts().getFires();
		for(AiFire f:flames){
			ai.checkInterruption();
			
			mtx[f.getLine()][f.getCol()] += wfire;
		}
		
		// Heros
		List<AiHero> heros = ai.getPercepts().getHeroes();
		for(AiHero h:heros){
			ai.checkInterruption();
			
			if(h.equals(ai.getPercepts().getOwnHero()))
				continue;
			
			if(h.hasEnded())
				continue;
			
			List<AiTile> nheros = h.getTile().getNeighbors();
			for(AiTile t:nheros)
				mtx[t.getLine()][t.getCol()] += whero;
			mtx[h.getLine()][h.getCol()] -= whero;
		}
		
		// Destructible Walls
		List<AiBlock> blocks = ai.getPercepts().getDestructibleBlocks();
		for(AiBlock b:blocks){
			ai.checkInterruption();
			
			List<AiTile> nblocks = b.getTile().getNeighbors();
			for(AiTile t:nblocks){
				ai.checkInterruption();
				
				mtx[t.getLine()][t.getCol()] += wwall;
			}
		}
		
		// Distance
		List<AiFloor> tiles = ai.getPercepts().getFloors();
		
		double maxdist = ai.getPercepts().getHeight()+ai.getPercepts().getWidth();
		AiTile mytile = ai.getPercepts().getOwnHero().getTile();
		for(AiFloor f:tiles){
			ai.checkInterruption();
			double val = (maxdist-ai.getPercepts().getTileDistance(mytile, f.getTile()))/maxdist;
			mtx[f.getLine()][f.getCol()] += val*2;
		}
		
		
		return mtx;
		
	}
}
