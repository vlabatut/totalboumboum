package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v5_2;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Burcu Küpelioğlu
 * @author Oktay Doruk
 */
@SuppressWarnings("deprecation")
public class Target 
{
	private DorukKupelioglu dk;
	
	private TargetBonus bonus;
	private TargetDestructible destructible;
	private TargetRival rival;
	
	private boolean hasPathFound;
	private boolean targetEnded;
	private boolean pathWorks;

	private static int noTarget=0;
	private static int targetDest=1;
	private static int targetBonus=2;
	private static int targetRival=3;

	private int currTarget;
	private boolean dropBomb;
	private int counter;
	
	
	public Target(DorukKupelioglu dk) throws StopRequestException
	{
		dk.checkInterruption();
		this.dk=dk;
		currTarget=noTarget;
		hasPathFound=false;
		targetEnded=true;
		pathWorks=true;
		counter=0;
	}
	
	public void init()throws StopRequestException
	{
		dk.checkInterruption();
		dropBomb=false;
		rival=new TargetRival(dk);
		if(rival.pathAvailable())//yani haspathfound true ve targetrivalended false
		{
			currTarget=targetRival;
			hasPathFound=true;
			targetEnded=false;
		}
		else if(rival.succeed() || dk.getHero().getTile().getHeroes().size()>1)//bomba bıraktığımda rakibe etki edecek herhangi bir yere vardıysam
		{
			//aynı casede isek problem yok
			//yanyana caselerde ise aradaki mesafenin 
			if(counter==1)
				pathWorks=false;
			dropBomb=true;
			hasPathFound=true;
			targetEnded=true;
			currTarget=noTarget;
			rival=null;
			counter++;
		}
		
		else if(currTarget==targetDest)//yakınımda rakip yoksa
		{//önce arada bonus alabilo muyum ona bakarım
			bonus=new TargetBonus(dk);
			if(bonus.pathAvailable() && bonus.PathLength()<destructible.PathLength())
			{
				currTarget=targetBonus;
				destructible=null;
			}
			else //
			{
				bonus=null;
				if(!(destructible.pathAvailable()))
				{
					if(!(destructible.pathWorks()))
						pathWorks=false;
					else if(destructible.succeed())
						dropBomb=true;
					
					targetEnded=true;						
					destructible=null;
					currTarget=noTarget;
				}
			}
		}
		
		
		else if(currTarget==targetBonus)
		{
			if(!(bonus.pathAvailable()))
			{
				if(!(bonus.pathWorks()))
					pathWorks=false;
				dropBomb=false;
				targetEnded=true;
				bonus=null;
				currTarget=noTarget;
			}
		}
		
		else//buraya geldiysem hiçbir hedefim yok demektir
		{//tehlikede miyim değil miyim ona bakıyorum
			if(dk.getMatrix().getAreaMatrix()[dk.getHero().getTile().getLine()][dk.getHero().getTile().getCol()]>State.MALUS)
				pathWorks=false;
			else//tehlikede değilsem hedef seçerim
			{
				hasPathFound=false;
				targetEnded=true;
				pathWorks=true;
				
				destructible=new TargetDestructible(dk);//duvara bakarım
				if(destructible.pathAvailable())//duvara gidebiliyorsam
				{
					hasPathFound=true;
					targetEnded=false;
					currTarget=targetDest;
				}
				else if(destructible.succeed())//zaten duvarın yanındaysam
				{
					hasPathFound=true;
					dropBomb=true;
					currTarget=noTarget;
				}
				else//duvara yol yoksa
				{
					destructible=null;
					bonus=new TargetBonus(dk);
					if(bonus.pathAvailable())
					{
						hasPathFound=true;
						targetEnded=false;
						currTarget=targetBonus;
					}
					else
					{
						bonus=null;
						rival=null;
						pathWorks=false;
					}
				}
			}
		}
	}
	
	public Direction moveTo() throws StopRequestException
	{
		dk.checkInterruption();
		Direction moveDir=Direction.NONE;
		if(currTarget==targetBonus)
			moveDir=bonus.moveTo();
		else if(currTarget==targetDest)
			moveDir=destructible.moveTo();
		else if(currTarget==targetRival)
			moveDir=rival.moveTo();
		return moveDir;
	}
	
	public boolean pathAvailable()throws StopRequestException
	{
		dk.checkInterruption();
		init();
		return hasPathFound && !targetEnded;
	}
	
	public boolean pathWorks()
	{
		return pathWorks;
	}
	
	public boolean targetEnded() throws StopRequestException
	{
		dk.checkInterruption();
		return targetEnded;
	}
	
	public boolean succeed() throws StopRequestException
	{
		dk.checkInterruption();
		return hasPathFound;
	}
	
	public boolean dropBomb() throws StopRequestException
	{
		dk.checkInterruption();
		return dropBomb;
	}
}
