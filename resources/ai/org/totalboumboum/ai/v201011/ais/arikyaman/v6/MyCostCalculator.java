package org.totalboumboum.ai.v201011.ais.arikyaman.v6;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;

/**
 * @author Furkan Arık
 * @author Çağdaş Yaman
 */
@SuppressWarnings("deprecation")
public class MyCostCalculator extends CostCalculator {

	/** */
	ArikYaman ay;
	/** */
    int[][]	ProcessMatrix;
	
    /**
     * 
     * @param ay
	 * 		description manquante !
     * @throws StopRequestException
	 * 		description manquante !
     */
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
	
	@Override
	public double processCost(AiPath path) throws StopRequestException
	{	ay.checkInterruption();
		
		double result = path.getTileDistance();
		return result;
	}
}