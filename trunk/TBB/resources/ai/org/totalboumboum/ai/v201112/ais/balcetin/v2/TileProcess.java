package org.totalboumboum.ai.v201112.ais.balcetin.v2;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiFire;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;

/**
 * Tile process class to get tile processes.
 * 
 * @author Adnan Bal
 */
@SuppressWarnings("deprecation")
public class TileProcess {
	BalCetin ai;

	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public TileProcess(BalCetin ai) throws StopRequestException{
		ai.checkInterruption();
		this.ai = ai;
	}

	
	/**Gets dangerous tiles.
	 * @return dangerous tiles list which are in danger by fires,bombs and bombs' blasts.
	 * @throws StopRequestException
	 */
	public List<AiTile> getDangerousTiles() throws StopRequestException {
		ai.checkInterruption();
		ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();
		for (AiBomb Bombs : ai.getZone().getBombs()) {
			ai.checkInterruption();
			dangerousTiles.add(Bombs.getTile());
			for (AiTile BombTile : Bombs.getBlast()) {
				ai.checkInterruption();
				dangerousTiles.add(BombTile);
			}

		}
		for (AiFire Fires : ai.getZone().getFires()) {
			ai.checkInterruption();
			dangerousTiles.add(Fires.getTile());
		}
		return dangerousTiles;

	}

	/**
	 * Gets safe tiles
	 * @return safe tiles list which are cleaned from dangerous ones.
	 * @throws StopRequestException
	 */
	public List<AiTile> getsafeTiles() throws StopRequestException {
		ai.checkInterruption();
		List<AiTile> safeTiles = this.ai.getZone().getTiles();
		safeTiles.removeAll(getDangerousTiles());
		return safeTiles;

	}

		
	/**Gets reacheable tiles by our hero.Attention, this list has the tiles even they are surrounded by 4 walls. 
	 * @return reacheable tiles list
	 * @throws StopRequestException
	 */
	public List<AiTile> getcanReachTiles() throws StopRequestException {
		ai.checkInterruption();
		List<AiTile> canReachTilesList = new ArrayList<AiTile>();
		for (AiTile canReachTiles : getsafeTiles()) {
			ai.checkInterruption();
			if (canReachTiles.isCrossableBy(ai.ownHero))
				canReachTilesList.add(canReachTiles);

		}
		return canReachTilesList;
	}

	/**
	 * Determines all walkable tiles.
	 * 
	 * @return A list of walkable tiles by our hero.
	 * @throws StopRequestException
	 */
	public List<AiTile> getwalkableTiles() throws StopRequestException {
		ai.checkInterruption();
		List<AiTile> result = new ArrayList<AiTile>();
		AiTile currentTile = this.ai.getZone().getOwnHero().getTile();
		result.add(currentTile);
		int oldCount = 0;
		int newCount = Integer.MAX_VALUE;
		int i = 0;
		
		while ( oldCount != newCount ) 
		{
			ai.checkInterruption();
			i++;System.out.println("Dongu no : " + i);
			oldCount = result.size();
			List<AiTile> list = new ArrayList<AiTile>(result);
			
			for (AiTile aiTile : list) 
			{
				ai.checkInterruption();
				for (AiTile neighbor : aiTile.getNeighbors()) 
				{
					ai.checkInterruption();
					if (!result.contains(neighbor) && neighbor.isCrossableBy(this.ai.getZone().getOwnHero())) 
					{
						result.add(neighbor);
					}
				}
			}
			newCount = result.size();
		}
		return result;
//
//		while (newCount > oldCount) {
//
//			if (this.ai.getZone().getOwnHero().getTile()
//					.getNeighbor(Direction.UP)
//					.isCrossableBy(this.ai.getZone().getOwnHero())) {
//				result.add(this.ai.getZone().getOwnHero().getTile()
//						.getNeighbor(Direction.UP));
//			}
//			if (this.ai.getZone().getOwnHero().getTile()
//					.getNeighbor(Direction.DOWN)
//					.isCrossableBy(this.ai.getZone().getOwnHero())) {
//				result.add(this.ai.getZone().getOwnHero().getTile()
//						.getNeighbor(Direction.DOWN));
//			}
//			if (this.ai.getZone().getOwnHero().getTile()
//					.getNeighbor(Direction.LEFT)
//					.isCrossableBy(this.ai.getZone().getOwnHero())) {
//				result.add(this.ai.getZone().getOwnHero().getTile()
//						.getNeighbor(Direction.LEFT));
//			}
//			if (this.ai.getZone().getOwnHero().getTile()
//					.getNeighbor(Direction.RIGHT)
//					.isCrossableBy(this.ai.getZone().getOwnHero())) {
//				result.add(this.ai.getZone().getOwnHero().getTile()
//						.getNeighbor(Direction.RIGHT));
//			}
//		}
	

	}
}
