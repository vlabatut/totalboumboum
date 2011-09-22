package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v4_1;


/**
 * @author Burcu Küpelioğlu
 * @author Oktay Doruk
 */
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

public class Target 
{
	private DorukKupelioglu dk;
	
	private TargetBonus bonus;
	private TargetDestructible destructible;
	private TargetRival rival;
	private Escape escape;
	private Astar astar;
	private AiPath path;
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
		dropBomb=false;
		hasPathFound=false;
		targetEnded=true;
		pathWorks=true;
		danger=false;
	}
	
	public void init()throws StopRequestException
	{
		dk.checkInterruption();
		
		//önce yakınımda rival var mı gidebilio muyum ya da aynı hizada isek bomba bırakabilio muyum 
		astar=new Astar(dk,true);
		astar.findPath(dk.getHero().getTile(),dk.getMatrix().getRivals());
		path=new AiPath();
		path=astar.getPath();
		if(path.getLength()>0 && path.getLength()<10)//rakibe yol var ve uzaklığım 10 den küçükse
		{
			System.err.println("yol var ve uzunluk 10 dan küçük");
			int line1,line2,col1,col2;
			line1=dk.getHero().getTile().getLine();
			line2=path.getLastTile().getLine();
			
			col1=dk.getHero().getTile().getCol();
			col2=path.getLastTile().getCol();
			int dist=Integer.MAX_VALUE;
			boolean empty=false;
			if(line1>line2)
			{
				int temp=line2;
				line2=line1;
				line1=temp;
			}
			if(col1>col2)
			{
				int temp=col2;
				col2=col1;
				col1=temp;
			}
			if(line1==line2)
			{
				empty=true;
				for(int in=col1+1;in<col2;in++)
					if(dk.getMatrix().getAreaMatrix()[line1][in]!=State.FREE)
					{
						empty=false;
						break;
					}
				dist=col2-col1;
			}
			if(col1==col2)
			{
				empty=true;
				for(int in=line1+1;in<line2;in++)
					if(dk.getMatrix().getAreaMatrix()[in][col1]!=State.FREE)
					{
						empty=false;
						break;
					}
				dist=line2-line1;
			}
			if(empty && dist<=dk.getHero().getBombRange())
			{
				bomb=new Bomb(dk,dk.getHero().getTile());
				if( bomb.pathAvailable())
				{
					dropBomb=true;
					targetEnded=true;
					hasPathFound=true;
					currTarget=noTarget;
				}
			}
			else
			{
				rival=new TargetRival(dk);
				if(rival.pathAvailable())
				{
					currTarget=targetRival;
					hasPathFound=true;
					targetEnded=false;
				}
				else if(rival.succeed())
				{
					dropBomb=true;
					hasPathFound=true;
					targetEnded=true;
					currTarget=noTarget;
				}
			}
		}
		else if(dk.getHero().getTile().getHeroes().size()>1)
		{
			bomb=new Bomb(dk,dk.getHero().getTile());
			if( bomb.pathAvailable())
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
			if(bonus.pathAvailable())
			{
				//System.out.println("duvara gidioduk yolda bonus bulduk");
				currTarget=targetBonus;
				destructible=null;
			}
			else //bonus 
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
		
	
		
		
		else//asıl initialisation burada
		{
			if(dk.getMatrix().getAreaMatrix()[dk.getHero().getTile().getLine()][dk.getHero().getTile().getCol()]>State.MALUS)
				danger=true;
			else
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
				
				else//bonusa gidemiosam
				{
					bonus=null;
					destructible=new TargetDestructible(dk);//duvara bakarım
					if(destructible.pathAvailable())
					{
						hasPathFound=true;
						targetEnded=false;
						currTarget=targetDest;
					}
					else if(destructible.succeed())
					{
						hasPathFound=true;
						dropBomb=true;
						currTarget=noTarget;
					}
					else
					{
						destructible=null;
						rival=new TargetRival(dk);
						if(rival.pathAvailable())
						{
							currTarget=targetRival;
							hasPathFound=true;
							targetEnded=false;
						}
						else if(rival.succeed())
						{
							dropBomb=true;
							hasPathFound=true;
							targetEnded=true;
							currTarget=noTarget;
						}
						
					}
				}
			}
		}
		if(!pathWorks || danger)
		{
			if(dk.getMatrix().getAreaMatrix()[dk.getHero().getTile().getLine()][dk.getHero().getTile().getCol()]>State.MALUS)
			{
				escape=new Escape(dk);
				currTarget=targetEscape;
				pathWorks=true;
			}
			
		}
	}
	
	public Direction moveTo() throws StopRequestException
	{
		dk.checkInterruption();
		Direction moveDir=Direction.NONE;
		/*
		 * bi hal yemiosam ya da destructible e gidiosam 
		 * ve giderken bi bonus alabileceğimi görüosam gider bonusu alırım 
		 * ama rivak kovalıosam bonusu 2. plana atarım 
		 */
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
