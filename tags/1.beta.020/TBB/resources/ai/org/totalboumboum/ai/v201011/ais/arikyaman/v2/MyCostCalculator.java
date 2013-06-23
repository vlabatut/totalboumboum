package org.totalboumboum.ai.v201011.ais.arikyaman.v2;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;


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
		if((ProcessMatrix[ay.getPercepts().getOwnHero().getLine()][ay.getPercepts().getOwnHero().getCol()]>0||ProcessMatrix[end.getLine()][end.getCol()]<0)&&ProcessMatrix[end.getLine()][end.getCol()]<ProcessMatrix[ay.getPercepts().getOwnHero().getLine()][ay.getPercepts().getOwnHero().getCol()]){
			return 100000;
		}
		return (end.getCol()*end.getCol())+(end.getLine()*end.getLine());
	}

}