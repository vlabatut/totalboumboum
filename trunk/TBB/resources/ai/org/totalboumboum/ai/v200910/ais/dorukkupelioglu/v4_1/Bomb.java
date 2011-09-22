package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v4_1;

import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;



/**
 * amaç bombanın etki edeceği yerleri EXPLODED
 * olarak işaretlemek ve oralar safe listesinde ise
 * listeden çıkarmak ve sonrasında
 * kalan safelerden bi tanesine 
 * gidebiliyor muyuz bunu incelemek
 * @author Burcu Küpelioğlu
 * @author Oktay Doruk
 */
public class Bomb {
	
	private DorukKupelioglu dk;
	private AiZone zone;
	private Matrix matrix;
	private List<AiTile> safes;
	private AiPath path;
	private Astar astar;
	private boolean hasPathFound;
	private boolean dropped;
	private boolean bombEnded;
	private double BOMBWILLEXPLODE;
	
	public Bomb(DorukKupelioglu dk,AiTile tile) throws StopRequestException
	{
		dk.checkInterruption();
		this.dk=dk;
		zone=dk.getPercepts();
		path=new AiPath();
		matrix=dk.getMatrix();
		safes=matrix.getSafes();
		hasPathFound=false;
		dropped=false;
		bombEnded=true;
		path=findPath(tile);
	}
	
	private AiPath findPath(AiTile tile)throws StopRequestException
	{
		dk.checkInterruption();
		AiPath result=new AiPath();
		BOMBWILLEXPLODE=dk.getMatrix().ManhattanDistance(tile, dk.getHero().getTile())*tile.getSize()/dk.getHero().getWalkingSpeed()*1000+1500;
		if(safes.size()>0)//gidilecek başka safe yoksa zaten bomba bırakma
		{
			
			tempEXPLODED(tile);
			
			astar=new Astar(dk,true);
			astar.findPath(tile, safes);
			result=astar.getPath();
			//eğer yol bulduysak demek ki bomba bırakırsak kaçabileceğimiz yer var
			if(!(result.isEmpty()))
			{
				hasPathFound=true;
				bombEnded=false;
			}
			//System.out.println("bomba koyarsak kaçış yolumuz: "+result.toString());
		}
		return result;
	}
	

	
	//Bomba bırakacaksak etki alanını EXPLODED olarak matriste belirtmeliyiz
	public void tempEXPLODED(AiTile tile)throws StopRequestException
	{
		dk.checkInterruption();
		int line=tile.getLine();
		int col=tile.getCol();
		boolean right=true,left=true,up=true,down=true;
		int bombRange=zone.getOwnHero().getBombRange();
		for(int x=0;x<=bombRange;x++)
		{
			dk.checkInterruption();
			int cl=0;
			if(left)// sola giderken 
			{
				cl=col-x>=0?col-x:zone.getWidth()+col-x;
				if(matrix.getAreaMatrix()[line][cl]==State.INDESTRUCTIBLE)
					left=false;
				else if(matrix.getAreaMatrix()[line][cl]<State.BONUSDANGER)//doğru ise orda zaten tehlike yoktur süre -1 dir
				{
					matrix.changeState(line,cl, State.DANGER);
					safes.remove(zone.getTile(line,cl));
					matrix.changeTimeLeft(line,cl, BOMBWILLEXPLODE);
				}
				
			}
			if(right)//sağa giderken
			{
				cl=(col+x)%zone.getWidth();
				if(matrix.getAreaMatrix()[line][cl]==State.INDESTRUCTIBLE)
					right=false;
				else if(matrix.getAreaMatrix()[line][cl]<State.BONUSDANGER)
				{
					matrix.changeState(line, cl, State.DANGER);
					safes.remove(zone.getTile(line, cl));
					matrix.changeTimeLeft(line,cl, BOMBWILLEXPLODE);
				}
			}
			if(up)//yukarı giderken
			{
				cl=line-x>=0?line-x:zone.getHeight()+line-x;
				if(matrix.getAreaMatrix()[cl][col]==State.INDESTRUCTIBLE)
					up=false;
				else if(matrix.getAreaMatrix()[cl][col]<State.BONUSDANGER)
				{
					matrix.changeState(cl, col, State.DANGER);
					safes.remove(zone.getTile(cl, col));
					matrix.changeTimeLeft(cl,col, BOMBWILLEXPLODE);
				}
			}
			if(down) //aşağı giderken
			{
				cl=(line+x)%zone.getHeight();
				if(matrix.getAreaMatrix()[cl][col]==State.INDESTRUCTIBLE)
					down=false;
				else if(matrix.getAreaMatrix()[cl][col]<State.BONUSDANGER)
				{
					matrix.changeState(cl, col, State.DANGER);
					safes.remove(zone.getTile(cl, col));
					matrix.changeTimeLeft(cl,col, BOMBWILLEXPLODE);
				}
			}
		}
	}
	
	
	public Direction moveTo() throws StopRequestException
	{
		dk.checkInterruption();
		dropped=true;
		Direction moveDir=Direction.NONE;
		moveDir=zone.getDirection(zone.getOwnHero().getTile(), path.getTile(0));
		path.removeTile(0);
		return moveDir;
	}

	
	public void changeDropped(boolean dropped)throws StopRequestException
	{
		dk.checkInterruption();
		this.dropped=dropped;
	}

	public boolean Dropped()throws StopRequestException
	{
		dk.checkInterruption();
		return dropped;
	}
	public boolean pathAvailable()throws StopRequestException
	{
		dk.checkInterruption();
		if(path.getLength()==0)
			bombEnded=true;
		return hasPathFound && !bombEnded;
	}	
	
	public boolean hasEnded()throws StopRequestException
	{
		dk.checkInterruption();
		return bombEnded;
	}

	
	public boolean isDroppable()throws StopRequestException
	{
		dk.checkInterruption();
		return hasPathFound;
	}
}
