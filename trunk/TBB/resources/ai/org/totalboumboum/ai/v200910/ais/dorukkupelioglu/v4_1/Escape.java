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
public class Escape {
	
	private DorukKupelioglu dk;
	private List<AiTile> safes;
	private Astar astar;
	private AiPath path;
	private boolean hasPathFound;
	private boolean escapeEnded;
	private boolean pathWorks;
	private List<Double> pathStates;
	private List<Double> pathStatesControl;
	
	public Escape(DorukKupelioglu dk)throws StopRequestException//safe yer yoksa ya da safe yer var ama oraya giden yol yoksa escape biter
	{
		dk.checkInterruption();
		this.dk=dk;
		safes=dk.getMatrix().getSafes();
		escapeEnded = true;
		hasPathFound=false;
		pathWorks=true;

		if(!safes.isEmpty())
		{
			astar=new Astar(dk,false);
			astar.findPath(dk.getHero().getTile(),safes);
			path=astar.getPath();
		
			if(!(path.isEmpty()))
			{
				escapeEnded=false;
				hasPathFound=true;
				pathStates=new ArrayList<Double>();
				for(int index=0;index<path.getLength();index++)
					pathStates.add(dk.getMatrix().getAreaMatrix()[path.getTile(index).getLine()][path.getTile(index).getCol()]);
			}
		}
		System.err.println(path.toString());
	}
	
	public Direction moveTo()throws StopRequestException//bu fonksiyondan önce pathAvailable fonk çağır 
	{
		dk.checkInterruption();
		Direction moveDir=Direction.NONE;
		moveDir=dk.getPercepts().getDirection(dk.getHero().getTile(), path.getTile(0));
		path.removeTile(0);
		pathStates.remove(0);
		return moveDir;
	}
	
	public boolean pathAvailable()throws StopRequestException
	{
		dk.checkInterruption();
		int control=0;
		pathStatesControl=new ArrayList<Double>();
		if(path.getLength()==0)
			escapeEnded=true;
		else
		{
			while(control<path.getLength())
			{
				pathStatesControl.add(dk.getMatrix().getAreaMatrix()[path.getTile(control).getLine()][path.getTile(control).getCol()]);
				control++;
			}
			if(!(pathStates.equals(pathStatesControl)))
			{
				escapeEnded=true;
				hasPathFound=false;
				pathWorks=false;
				System.err.println("kaçış yolu bozuldu");
			}
		}
		return hasPathFound && !escapeEnded;
	}
	
	public boolean pathWorks()
	{
		return pathWorks;
	}
	
	public boolean hasEnded()throws StopRequestException
	{
		dk.checkInterruption();
		return escapeEnded;
	}
	
	public boolean succeed()throws StopRequestException
	{
		dk.checkInterruption();
		return hasPathFound;
	}
}
