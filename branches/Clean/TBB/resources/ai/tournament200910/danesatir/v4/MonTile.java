package tournament200910.danesatir.v4;

import fr.free.totalboumboum.ai.adapter200910.data.AiTile;

public class MonTile {
	private AiTile tile;
	private int iter;
	MonTile(AiTile tile, int iter) {
		this.setTile(tile);
		this.setIter(iter);
	}
	private void setTile(AiTile tile) {
		this.tile = tile;
	}
	public AiTile getTile() {
		return tile;
	}
	private void setIter(int iter) {
		this.iter = iter;
	}
	public int getIter() {
		return iter;
	}
	
}
