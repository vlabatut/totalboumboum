package org.totalboumboum.ai.v201011.ais.caliskanseven.v6;

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
 * This is the matrix class of our AI.
 * @author Mustafa Çalışkan
 * @author Cihan Seven
 */
@SuppressWarnings("deprecation")
public class Matrix {
	
	/**
	 * This class calculates the matrix according to the given values.
	 * 
	 * @param ai 
	 * @param wfire this is the value of fire explosion
	 * @param wblast this is the value of bomb blasts
	 * @param wbomb this is the value of bomb numbers in game play
	 * @param wbonus this is the value of bonuses
	 * @param wmalus this is the value of maluses
	 * @param whero this is the value of other ais
	 * @param wwall this is the value of walls
	 * @param closed unused
	 * @param wclosed unused
	 * @return 
	 * 		?
	 * @throws StopRequestException
	 */
	public double[][] calculate(CaliskanSeven ai, int wfire, int wblast,
			int wbomb, int wbonus, int wmalus, int whero, int wwall,
			List<AiTile> closed, int wclosed) throws StopRequestException {
		ai.checkInterruption();

		int l, c;
		l = ai.getPercepts().getHeight();
		c = ai.getPercepts().getWidth();
		double mtx[][] = new double[l][c];

		// Adding Values of Destructible Walls to the matrix 
		List<AiBlock> blocks = ai.getPercepts().getDestructibleBlocks();
		for (AiBlock b : blocks) {
			ai.checkInterruption();

			List<AiTile> nblocks = b.getTile().getNeighbors();
			
			for (AiTile t : nblocks) {
				ai.checkInterruption();
				
				mtx[t.getLine()][t.getCol()] = wwall;
			}
		}
		
		// Adding Values of Closed Tiles to the matrix
		for (AiTile t : closed) {
			ai.checkInterruption();
			mtx[t.getLine()][t.getCol()] += wclosed;
		}

		// Adding Values of Bonus Malus to the matrix
		List<AiItem> items = ai.getPercepts().getItems();
		for (AiItem item : items) {
			ai.checkInterruption();

			if (item.getType().name() == AiItemType.MALUS.name()) {
				mtx[item.getLine()][item.getCol()] += wmalus;
			} else
				mtx[item.getLine()][item.getCol()] += wbonus;
		}
		// Adding Values of Bombs to the matrix
		long lastbombdur = 99999999;
		List<AiBomb> bombs = ai.getPercepts().getBombs();
		for (AiBomb bomb : bombs) {
			ai.checkInterruption();
			
			//checking the bomb if it's ours
			if(bomb.getOwner().equals(ai.getPercepts().getOwnHero())){
				if(bomb.getTime()<lastbombdur)
					lastbombdur = bomb.getTime();
			}
			
			mtx[bomb.getLine()][bomb.getCol()] += wbomb;
			List<AiTile> blast = bomb.getBlast();
			for (AiTile b : blast) {
				ai.checkInterruption();

				mtx[b.getLine()][b.getCol()] += wblast;
			}
		}

		List<AiFire> flames = ai.getPercepts().getFires();
		for (AiFire f : flames) {
			ai.checkInterruption();

			mtx[f.getLine()][f.getCol()] += wfire;
		}

		// Adding Values of Heroes to the matrix
		List<AiHero> heros = ai.getPercepts().getHeroes();
		AiHero closesthero = null;
		int closestd = 9999;
		for (AiHero h : heros) {
			ai.checkInterruption();

			if (h.equals(ai.getPercepts().getOwnHero()))
				continue;

			if (h.hasEnded())
				continue;

			int dist = PathFinding.tiledist(ai,h.getTile(), ai.getPercepts()
					.getOwnHero().getTile());
			if (dist < closestd) {
				closestd = dist;
				closesthero = h;
			}

			List<AiTile> nheros = h.getTile().getNeighbors();
			for (AiTile t : nheros){
				ai.checkInterruption();
				mtx[t.getLine()][t.getCol()] += whero;
			}
			mtx[h.getLine()][h.getCol()] -= whero;
		}

		int zoneh = ai.getPercepts().getHeight();
		int zonew = ai.getPercepts().getWidth();
		int max = zoneh + zonew;
		if (closesthero != null) {
			for (int i = 0; i < zonew; i++) {
				ai.checkInterruption();
				for (int j = 0; j < zoneh; j++) {
					ai.checkInterruption();
					int hdist = PathFinding.tiledist(ai,closesthero.getTile(), ai
							.getPercepts().getTile(j, i));
					
					if(lastbombdur > 100){
						mtx[j][i] += max - hdist;
					}else mtx[j][i] -= max - hdist;
					
				}
			}
		}

		// Adding Values of Distance to the matrix
		List<AiFloor> tiles = ai.getPercepts().getFloors();

		double maxdist = ai.getPercepts().getHeight()
				+ ai.getPercepts().getWidth();
		AiTile mytile = ai.getPercepts().getOwnHero().getTile();
		for (AiFloor f : tiles) {
			ai.checkInterruption();
			double val = (maxdist - ai.getPercepts().getTileDistance(mytile,
					f.getTile()))
					/ maxdist;
			mtx[f.getLine()][f.getCol()] += val * 2;
		}

		return mtx;

	}
}
