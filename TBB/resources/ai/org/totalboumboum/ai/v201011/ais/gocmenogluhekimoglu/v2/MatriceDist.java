package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v2;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiFloor;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;

public class MatriceDist extends MatriceCalc  {

	public MatriceDist(GocmenogluHekimoglu monIa) throws StopRequestException {
		super(monIa);
		
		// TODO Auto-generated constructor stub
	}
	
	public void calculate() throws StopRequestException {
		monIa.checkInterruption();
		@SuppressWarnings("unused")
		AiTile mytile = monIa.getPercepts().getOwnHero().getTile();
		int maxdist = monIa.getPercepts().getWidth()+monIa.getPercepts().getHeight();
		maxdist *= monIa.getPercepts().getTileDistance(monIa.getPercepts().getTile(0, 0),monIa.getPercepts().getTile(0, 1) );
		for(AiFloor floor:monIa.getPercepts().getFloors()){
			double val = 1-((double)monIa.getPercepts().getPixelDistance(floor, monIa.getPercepts().getOwnHero())/maxdist);
			this.matrix[floor.getLine()][floor.getCol()] = val;
		}
	}
}
