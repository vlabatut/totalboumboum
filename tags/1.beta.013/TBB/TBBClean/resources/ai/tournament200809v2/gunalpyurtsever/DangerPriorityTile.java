package tournament200809v2.gunalpyurtsever;

import fr.free.totalboumboum.ai.adapter200809.AiTile;

public class DangerPriorityTile{

	AiTile tile;
	int dangerpriority;
	public DangerPriorityTile(AiTile tile, int dangerpriority) {
		
		this.tile = tile;
		this.dangerpriority = dangerpriority;
	}
	public AiTile getTile() {
		return tile;
	}


	public int getDangerpriority() {
		return dangerpriority;
	}
	public void setDangerpriority(int dangerpriority) {
		this.dangerpriority = dangerpriority;
	}
	
}
