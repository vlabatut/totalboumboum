package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v3;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiFloor;

/**
 * @author Can Göçmenoğlu
 * @author Irfan Hekimoğlu
 */
@SuppressWarnings("deprecation")
public class MatriceDist extends MatriceCalc  {

	public MatriceDist(GocmenogluHekimoglu monIa) throws StopRequestException {
		super(monIa);
		
	}
	
	/**
	 * Donne cas une valeur comprise entre 1 et 0 en fonction
	 *  de leurs distances à notre héros. 0 est le plus
	 *   éloigné, 1 est le plus proche
	 */
	public void calculate() throws StopRequestException {
		monIa.checkInterruption();
		int maxdist = monIa.getPercepts().getWidth()+monIa.getPercepts().getHeight();
		maxdist *= monIa.getPercepts().getTileDistance(monIa.getPercepts().getTile(0, 0),monIa.getPercepts().getTile(0, 1) );
		for(AiFloor floor:monIa.getPercepts().getFloors()){
			double val = 1-((double)monIa.getPercepts().getPixelDistance(floor, monIa.getPercepts().getOwnHero())/maxdist);
			this.matrix[floor.getLine()][floor.getCol()] = val;
		}
	}
}
