package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v4_2;



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
	private Escape escape;
	private Bomb bomb;
	
	private boolean hasPathFound;
	private boolean targetEnded;
	private boolean pathWorks;
	private boolean danger;

	private static int noTarget=0;
	private static int targetDest=1;
	private static int targetBonus=2;
	private static int targetRival=3;
	private static int targetEscape=4;

	private int currTarget;
	private boolean dropBomb;
	
	
	public Target(DorukKupelioglu dk) throws StopRequestException
	{
		dk.checkInterruption();
		this.dk=dk;
		currTarget=noTarget;
		hasPathFound=false;
		targetEnded=true;
		pathWorks=true;
		danger=false;
	}
	
	public void init()throws StopRequestException
	{
		dk.checkInterruption();
		if(!danger)
		{
				dropBomb=false;
				rival=new TargetRival(dk);
				if(rival.pathAvailable())
				{
					currTarget=targetRival;
					hasPathFound=true;
					targetEnded=false;
				}
				else if(rival.succeed())//bomba bıraktığımda rakibe etki edecek herhangi bir yere vardıysam
				{
					dropBomb=true;
					hasPathFound=true;
					targetEnded=true;
					currTarget=noTarget;
				}
				else if(dk.getHero().getTile().getHeroes().size()>1)//rakiple aynı yerdeysem
				{
					bomb=new Bomb(dk,dk.getHero().getTile());
					if( bomb.pathAvailable())//buraya bomba bırakırsam kaçacak yerim varsa
					{
						dropBomb=true;
						targetEnded=true;
						hasPathFound=true;
						currTarget=noTarget;
					}
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
						danger=true;
					else//tehlikede değilsem hedef seçerim
					{
						hasPathFound=false;
						targetEnded=true;
						pathWorks=true;
						
						bonus=new TargetBonus(dk);//önce bonus yarattım
						if(bonus.pathAvailable())//bonusa gidebiliosam
						{
							hasPathFound=true;
							targetEnded=false;
							currTarget=targetBonus;
						}
						
						else//bonusa yoksa
						{
							bonus=null;
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
							else//duvara da yol yoksa
							{
								destructible=null;
								rival=null;
								danger=true;
							}
						}
					}
				}
		}
		if(!pathWorks || danger)//yol bozulmuş ya da tehlike alarmı varsa
		{
			danger=true;
			destructible=null;
			rival=null;
			bonus=null;
			dk.getMatrix().createAreaMatrix();
			escape=new Escape(dk);
			if(escape.pathAvailable())
			{
				currTarget=targetEscape;
				hasPathFound=true;
				targetEnded=false;
				pathWorks=true;
			}
			else if(escape.succeed())
			{
				danger=false;
				currTarget=noTarget;
				pathWorks=true;
				targetEnded=true;
			}
			else
				escape=new Escape(dk);
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
		else if(currTarget==targetEscape)
			moveDir=escape.moveTo();
		return moveDir;
	}
	
	public boolean pathAvailable()throws StopRequestException
	{
		dk.checkInterruption();
		init();
		return hasPathFound && !targetEnded;
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
