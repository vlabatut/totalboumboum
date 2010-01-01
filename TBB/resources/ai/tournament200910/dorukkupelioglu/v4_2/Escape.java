package tournament200910.dorukkupelioglu.v4_2;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.adapter200910.communication.StopRequestException;
import org.totalboumboum.ai.adapter200910.data.AiTile;
import org.totalboumboum.ai.adapter200910.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;


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
		path=new AiPath();
		if(dk.getMatrix().getAreaMatrix()[dk.getHero().getTile().getLine()][dk.getHero().getTile().getCol()]>State.MALUS)
		{	if(!safes.isEmpty())
			{
				astar=new Astar(dk,true);
				astar.findPath(dk.getHero().getTile(),safes);
				path=astar.getPath();
			
				if(!(path.isEmpty()))//zamanlı astarda yol bulamazsa zamansız deneyeceğim
				{
					escapeEnded=false;
					hasPathFound=true;
					pathStates=new ArrayList<Double>();
					for(int index=0;index<path.getLength();index++)
						pathStates.add(dk.getMatrix().getAreaMatrix()[path.getTile(index).getLine()][path.getTile(index).getCol()]);
				}
				else
				{
					astar=new Astar(dk,false);//burda da zamansız deniyorum
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
			}
		}
		else
			hasPathFound=true;
	}
	
	
	
	public Direction moveTo()throws StopRequestException//bu fonksiyondan önce pathAvailable fonk çağır 
	{
		dk.checkInterruption();
		Direction moveDir=Direction.NONE;
		if(path.getLength()!=0)
		if(dk.getMatrix().getAreaMatrix()[path.getTile(0).getLine()][path.getTile(0).getCol()]!=State.FIRE)
		{
			moveDir=dk.getPercepts().getDirection(dk.getHero().getTile(), path.getTile(0));
			path.removeTile(0);
			pathStates.remove(0);
		}
		return moveDir;
	}
	
	public boolean pathAvailable()throws StopRequestException
	{
		dk.checkInterruption();
		int control=0;
		pathStatesControl=new ArrayList<Double>();
		if(path.getLength()==0)//yol kalmadıysa olay bitmiştir
			escapeEnded=true;
		else// yol varsa bozulup bozulmadığına bakarım
		{
			while(control<path.getLength())
			{
				pathStatesControl.add(dk.getMatrix().getAreaMatrix()[path.getTile(control).getLine()][path.getTile(control).getCol()]);
				control++;
			}
			if(!(pathStates.equals(pathStatesControl)))// eşit değilse yol bozulmuştur bu escape biter
			{
				escapeEnded=true;
				hasPathFound=false;
				pathWorks=false;
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
