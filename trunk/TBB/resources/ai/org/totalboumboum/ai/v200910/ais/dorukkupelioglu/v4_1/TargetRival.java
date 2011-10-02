package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v4_1;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * @author Burcu Küpelioğlu
 * @author Oktay Doruk
 */
@SuppressWarnings("deprecation")
public class TargetRival {

	private DorukKupelioglu dk;
	private List<AiTile> rivals;
	private Astar astar;
	private AiPath path;
	private boolean hasPathFound;
	private boolean targetRivalEnded;
	private boolean pathWorks;
	private List<Double> pathStates;
	private List<Double> pathStatesControl;
	
	public TargetRival(DorukKupelioglu dk) throws StopRequestException
	{
		dk.checkInterruption();
		this.dk=dk;
		dk.init();
		rivals=dk.getMatrix().getRivals();
		path=new AiPath();
		hasPathFound=false;
		targetRivalEnded=true;
		pathWorks=true;
		astar=new Astar(dk,false);
			
		if(!(rivals.isEmpty()))
		{
			astar.findPath(dk.getHero().getTile(), rivals);
			path=astar.getPath();
			//if(dk.getMatrix().getAreaMatrix()[path.getLastTile().getLine()][path.getLastTile().getCol()]>State.MALUS)
				//if(dk.getHero().getTile().getLine()==path.getLastTile().getLine()||dk.getHero().getTile().getCol()==path.getLastTile().getCol())
					
			if(!(path.isEmpty()))
			{
				targetRivalEnded=false;
				hasPathFound=true;
				pathStates=new ArrayList<Double>();
				for(int index=0;index<path.getLength()-1;index++)
				{
					pathStates.add(dk.getMatrix().getAreaMatrix()[path.getTile(index).getLine()][path.getTile(index).getCol()]);
				}
				System.out.println("rakip yol: "+path.toString());
				System.out.println("rakip için stateler : "+pathStates.toString());
			}
		}
	}
	
	public Direction moveTo()throws StopRequestException
	{
		dk.checkInterruption();
		Direction moveDir=Direction.NONE;
		moveDir=dk.getPercepts().getDirection(dk.getHero().getTile(), path.getTile(0));
		path.removeTile(0);
		pathStates.remove(0);
		return moveDir;
	}
	
	public boolean pathAvailable() throws StopRequestException
	{
		dk.checkInterruption();
		dk.init();
		pathStatesControl=new ArrayList<Double>();
		int control=0;
		
		if(path.getLength()==1|| path.getLength()==0)
			targetRivalEnded=true;
		else
		{
			while(control<path.getLength()-1)
			{
				pathStatesControl.add(dk.getMatrix().getAreaMatrix()[path.getTile(control).getLine()][path.getTile(control).getCol()]);
				control++;
			}
			if(!(pathStates.equals(pathStatesControl)))
			{
				targetRivalEnded=true;
				hasPathFound=false;
				pathWorks=false;
			}
		}
		return hasPathFound && !targetRivalEnded;
	}	
	
	public boolean pathWorks()
	{
		return pathWorks;
	}
	
	
	public boolean succeed() throws StopRequestException
	{
		dk.checkInterruption();
		return hasPathFound;
	}
}
