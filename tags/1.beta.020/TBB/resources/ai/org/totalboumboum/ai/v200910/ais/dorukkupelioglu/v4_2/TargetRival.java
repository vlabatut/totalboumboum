package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v4_2;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;



public class TargetRival {

	private DorukKupelioglu dk;
	private List<AiTile> rivals;
	private List<AiTile> available;
	private Astar astar;
	private AiPath path;
	private boolean hasPathFound;
	private boolean targetRivalEnded;
	private boolean pathWorks;
	
	public TargetRival(DorukKupelioglu dk) throws StopRequestException
	{
		dk.checkInterruption();
		this.dk=dk;
		path=new AiPath();
		available=new ArrayList<AiTile>();
		hasPathFound=false;
		targetRivalEnded=true;		
		rivals=dk.getMatrix().getRivals();
		available=findAvailable();//bomba bırakınca rakibi öldürebileceğim yerler
		available=dk.getMatrix().regulateList(dk.getHero().getTile(), available);//yakınlığına göre sıraladım
		astar=new Astar(dk,true);//zamanlı astar oluşturdum
			
		if(!(available.isEmpty()))//rakibe etki edebilecek yer varsa
		{
			if(available.contains(dk.getHero().getTile()))//bunlardan biri, zaten bulunduğum yerse
			{
				targetRivalEnded=true;
				hasPathFound=true;
			}
			else//değilse yol bulurum
			{
				astar.findPath(dk.getHero().getTile(), available);
				path=astar.getPath();
				
				if(!(path.isEmpty()))//eğer bu yol boşsa başarısız oldum
				{
					targetRivalEnded=false;
					hasPathFound=true;
				}
			}
		}
	}
	
	
	/**
	 * bomba bıraktığımda rakibe etki edecek yerleri bulur
	 * @return
	 * @throws StopRequestException
	 */
	public List<AiTile> findAvailable()throws StopRequestException
	{
		dk.checkInterruption();
		List<AiTile> result=new ArrayList<AiTile>();
		for(int index=0;index<rivals.size();index++)//rivalların yanı kendisi değil
		{
			dk.checkInterruption();
			boolean right=true,left=true,up=true,down=true;
			int line=rivals.get(index).getLine();
			int col=rivals.get(index).getCol();
			for(int pow=1;pow<=dk.getHero().getBombRange();pow++)
			{
				dk.checkInterruption();
				int cl=0;
				if(left)
				{
					cl=col-pow>=0?col-pow:dk.getPercepts().getWidth()+col-pow;
					if(dk.getMatrix().getAreaMatrix()[line][cl]!=State.FREE
					&&dk.getMatrix().getAreaMatrix()[line][cl]!=State.FIRE
					&&dk.getMatrix().getAreaMatrix()[line][cl]!=State.DANGER
					&&dk.getMatrix().getAreaMatrix()[line][cl]!=State.RIVAL)
						left=false;
					else 
						result.add(dk.getPercepts().getTile(line, cl));
				}
				if(right)
				{
					cl=(col+pow)%dk.getPercepts().getWidth();
					if(dk.getMatrix().getAreaMatrix()[line][cl]!=State.FREE
							&&dk.getMatrix().getAreaMatrix()[line][cl]!=State.FIRE
							&&dk.getMatrix().getAreaMatrix()[line][cl]!=State.DANGER
							&&dk.getMatrix().getAreaMatrix()[line][cl]!=State.RIVAL)
						right=false;
					else 
						result.add(dk.getPercepts().getTile(line, cl));
				}
				if(up)
				{
					cl=line-pow>=0?line-pow:dk.getPercepts().getHeight()+line-pow;
					if(dk.getMatrix().getAreaMatrix()[cl][col]!=State.FREE
							&&dk.getMatrix().getAreaMatrix()[cl][col]!=State.FIRE
							&&dk.getMatrix().getAreaMatrix()[cl][col]!=State.DANGER
							&&dk.getMatrix().getAreaMatrix()[cl][col]!=State.RIVAL)
						up=false;
					else 
						result.add(dk.getPercepts().getTile(cl, col));
				}	
				if(down)
				{
					cl=(line+pow)%dk.getPercepts().getHeight();
					if(dk.getMatrix().getAreaMatrix()[cl][col]!=State.FREE
							&&dk.getMatrix().getAreaMatrix()[cl][col]!=State.FIRE
							&&dk.getMatrix().getAreaMatrix()[cl][col]!=State.DANGER
							&&dk.getMatrix().getAreaMatrix()[cl][col]!=State.RIVAL)
						down=false;
					else 
						result.add(dk.getPercepts().getTile(cl, col));
				}	
			}
		}
		return result;
	}
	
	public Direction moveTo()throws StopRequestException
	{
		dk.checkInterruption();
		Direction moveDir=Direction.NONE;
		if(!path.isEmpty())
		moveDir=dk.getPercepts().getDirection(dk.getHero().getTile(), path.getTile(0));
		path.removeTile(0);
		return moveDir;
	}
	
	public boolean pathAvailable() throws StopRequestException
	{
		dk.checkInterruption();
		if(!(path.isEmpty()))
			targetRivalEnded=true;
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
