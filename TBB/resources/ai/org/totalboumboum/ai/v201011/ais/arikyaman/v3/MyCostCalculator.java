package org.totalboumboum.ai.v201011.ais.arikyaman.v3;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;

/**
 * @author Furkan Arık
 * @author Çağdaş Yaman
 */
public class MyCostCalculator extends CostCalculator {

	ArikYaman ay;
    int[][]	ProcessMatrix;
	
	public MyCostCalculator(ArikYaman ay) throws StopRequestException
	{	ay.checkInterruption();
		this.ay = ay;
		this.ProcessMatrix=ay.getProcessMatrix();
	}
	
	@Override
	public double processCost(AiTile start, AiTile end) throws StopRequestException {
		ay.checkInterruption();
		if((ProcessMatrix[end.getLine()][end.getCol()]<0)){
			return 100000;
		}
		double x=(ay.getPercepts().getOwnHero().getPosX()-end.getPosX());
		double y=(ay.getPercepts().getOwnHero().getPosY()-end.getPosY());
		return Math.sqrt(x*x+y*y)/10;
	}
	public double processCost(AiPath path) throws StopRequestException
	{	double result = path.getTileDistance();
		return result;
	}

}