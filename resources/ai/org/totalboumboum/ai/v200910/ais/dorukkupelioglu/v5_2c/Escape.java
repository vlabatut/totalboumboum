package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v5_2c;

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
public class Escape {
	
	/** */
	private DorukKupelioglu dk;
	/** */
	private Astar astar;
	/** */
	private AiPath path;
	/** */
	private boolean hasPathFound;
	/** */
	private boolean escapeEnded;
	/** */
	private boolean pathWorks;
	/** */
	private List<AiTile> safes;
	/** */
	private List<Double> pathStates;
	/** */
	private List<Double> pathStatesControl;
	
	/**
	 * 
	 * @param dk
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Escape(DorukKupelioglu dk)throws StopRequestException
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
				astar=new Astar(dk,true);//dabord useTime true
				astar.findPath(dk.getHero().getTile(),safes);
				path=astar.getPath();
			
				if(!(path.isEmpty()))
				{
					escapeEnded=false;
					hasPathFound=true;
					pathStates=new ArrayList<Double>();
					for(int index=0;index<path.getLength();index++)
					{	dk.checkInterruption();
						pathStates.add(dk.getMatrix().getAreaMatrix()[path.getTile(index).getLine()][path.getTile(index).getCol()]);
					}
				}
				else
				{
					astar=new Astar(dk,false);//et useTime false
					astar.findPath(dk.getHero().getTile(),safes);
					path=astar.getPath();
					if(!(path.isEmpty()))
					{
						escapeEnded=false;
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
		}
		else
			hasPathFound=true;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Direction moveTo()throws StopRequestException
	{
		dk.checkInterruption();
		Direction moveDir=Direction.NONE;
		if(path.getLength()!=0 && dk.getMatrix().getAreaMatrix()[path.getTile(0).getLine()][path.getTile(0).getCol()]!=State.FIRE && dk.getHero().getTile()!=path.getLastTile())
		{
			moveDir=dk.getPercepts().getDirection(dk.getHero().getTile(), path.getTile(0));
			path.removeTile(0);
			pathStates.remove(0);
		}
		return moveDir;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
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
				dk.checkInterruption();
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
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean pathWorks() throws StopRequestException
	{	dk.checkInterruption();
		return pathWorks;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean hasEnded()throws StopRequestException
	{
		dk.checkInterruption();
		return escapeEnded;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean succeed()throws StopRequestException
	{
		dk.checkInterruption();
		return hasPathFound;
	}
}
