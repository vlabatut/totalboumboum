package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v5_2;

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
public class TargetBonus {
	
	private DorukKupelioglu dk;
	private Astar astar;//yol bulma fonksiyonu kullanacağım
	private AiPath path;
	private boolean hasPathFound;
	private boolean targetBonusEnded;
	private boolean pathWorks;
	private List<AiTile> bonuses; // matrixte tutualan haritadaki bonusları tutacak (en yakından en uzağa)
	private List<Double> pathStates;
	private List<Double> pathStatesControl;
	
	
	public TargetBonus(DorukKupelioglu dk)throws StopRequestException
	{
		dk.checkInterruption();
		this.dk=dk;
		bonuses=dk.getMatrix().getBonus();
		path=new AiPath();
		hasPathFound=false;
		targetBonusEnded=true;
		pathWorks=true;		
		astar=new Astar(dk,true);
		if(!(bonuses.isEmpty()))
		{
			astar.findPath(dk.getHero().getTile(), bonuses);
			path=astar.getPath();
			if(!(path.isEmpty()))
			{
				targetBonusEnded=false;
				hasPathFound=true;
				pathStates=new ArrayList<Double>();
				for(int index=0;index<path.getLength();index++)
				{
					dk.checkInterruption();
					pathStates.add(dk.getMatrix().getAreaMatrix()[path.getTile(index).getLine()][path.getTile(index).getCol()]);
				}
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
		int control=0;
		if(path.isEmpty())
			targetBonusEnded=true;
		else
		{
			pathStatesControl=new ArrayList<Double>();
			while(control<path.getLength())
			{
				dk.checkInterruption();
				pathStatesControl.add(dk.getMatrix().getAreaMatrix()[path.getTile(control).getLine()][path.getTile(control).getCol()]);
				control++;
			}
			if(!(pathStates.equals(pathStatesControl)))
			{
				targetBonusEnded=true;
				hasPathFound=false;
				pathWorks=false;
			}
		}
		return hasPathFound && !targetBonusEnded;
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
	
	public int PathLength()
	{
		return path.getLength();
	}
}
