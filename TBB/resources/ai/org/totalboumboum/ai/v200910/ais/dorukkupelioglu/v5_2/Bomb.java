package org.totalboumboum.ai.v200910.ais.dorukkupelioglu.v5_2;

import java.util.List;


import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * amaç bombanın etki edeceği yerleri FIRE
 * olarak işaretlemek ve oralar safe listesinde ise
 * listeden çıkarmak ve sonrasında
 * kalan safelerden bi tanesine 
 * gidebiliyor muyuz bunu incelemek
 * @author user
 *
 */
public class Bomb {
	
	private DorukKupelioglu dk;
	private Matrix matrix;
	private Astar astar;
	private AiPath path;
	private AiZone zone;
	private List<AiTile> safes;
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
		if(safes.size()>0 && dk.getMatrix().getTimeLeft()[tile.getLine()][tile.getCol()]!=-3)//gidilecek başka safe yoksa zaten bomba bırakma
		{			
			tempFIRE(tile);
			astar=new Astar(dk,true);
			astar.findPath(tile, safes);
			result=astar.getPath();
			if(!(result.isEmpty()))
			{
				hasPathFound=true;
				bombEnded=false;
			}
		}
		return result;
	}

	public void tempFIRE(AiTile tile)throws StopRequestException
	{
		dk.checkInterruption();
		int line=tile.getLine();
		int col=tile.getCol();
		boolean right=true,left=true,up=true,down=true;
		int bombRange=zone.getOwnHero().getBombRange();
		/*
		 * bombamın patlamasına yukarda 1500 ms varmış gibi gösterdim ama
		 * bombayı yerleştireceğim yer bi bombanın etkisi altında ise 
		 * yerleştireceğim bomba o bomba patlayacağı zaman patlar
		 */
		if(dk.getMatrix().getTimeLeft()[line][col]>-1)
			BOMBWILLEXPLODE=dk.getMatrix().ManhattanDistance(tile, dk.getHero().getTile())*tile.getSize()/dk.getHero().getWalkingSpeed()*1000+dk.getMatrix().getTimeLeft()[line][col];
		//ilk olarak bombayı yerleştireceğim yerin özelliklerini değiştiriyorum
		matrix.changeState(line,col, State.BOMB);
		safes.remove(zone.getTile(line,col));
		matrix.changeTimeLeft(line,col, BOMBWILLEXPLODE);
		//şimdi de bombanın etki edeceği yerlerin özelliklerini değiştiriyorum 
		for(int x=1;x<=bombRange;x++)
		{
			dk.checkInterruption();
			int cl=0;
			if(left)//line constante col variable
			{
				cl=col-x>=0?col-x:zone.getWidth()+col-x;
				if(matrix.getAreaMatrix()[line][cl]==State.INDESTRUCTIBLE)
					left=false;
				else if(matrix.getAreaMatrix()[line][cl]<State.BONUSDANGER)//doğru ise orda zaten tehlike yoktur süre -1 dir
				{
					/*
					 * astar kullanıp yol ararken rakiple aynı yönü seçerse 
					 * ve o esnada rakip bomba bırakırsa bizim mal iki bomba arasında kalabilir
					 * bunu engellemek için burayı duvar gibi gösteriyorum ki rakiple aynı yeri seçmesin
					 * ya da arada başka kaçabileceği bi yer varsa seçsin 
					 */
					if(dk.getMatrix().getAreaMatrix()[line][cl]==State.RIVAL)
						matrix.changeState(line,cl, State.BOMB);
					else
						matrix.changeState(line,cl, State.DANGER);
					if(dk.getMatrix().getTimeLeft()[line][cl]==-1)
						matrix.changeTimeLeft(line,cl, BOMBWILLEXPLODE);
					safes.remove(zone.getTile(line,cl));
				}
			}
			if(right)//line costante col variable
			{
				cl=(col+x)%zone.getWidth();
				if(matrix.getAreaMatrix()[line][cl]==State.INDESTRUCTIBLE)
					right=false;
				else if(matrix.getAreaMatrix()[line][cl]<State.BONUSDANGER)
				{
					if(dk.getMatrix().getAreaMatrix()[line][cl]==State.RIVAL)
						matrix.changeState(line,cl, State.BOMB);
					else
						matrix.changeState(line, cl, State.DANGER);
					if(dk.getMatrix().getTimeLeft()[line][cl]==-1)
						matrix.changeTimeLeft(line,cl, BOMBWILLEXPLODE);
					safes.remove(zone.getTile(line, cl));
				}
			}
			if(up)//col constante line variable
			{
				cl=line-x>=0?line-x:zone.getHeight()+line-x;
				if(matrix.getAreaMatrix()[cl][col]==State.INDESTRUCTIBLE)
					up=false;
				else if(matrix.getAreaMatrix()[cl][col]<State.BONUSDANGER)
				{
					if(dk.getMatrix().getAreaMatrix()[cl][col]==State.RIVAL)
						matrix.changeState(cl,col, State.BOMB);
					else
						matrix.changeState(cl, col, State.DANGER);
					if(dk.getMatrix().getTimeLeft()[cl][col]==-1)
						matrix.changeTimeLeft(cl,col, BOMBWILLEXPLODE);
					safes.remove(zone.getTile(cl, col));
				}
			}
			if(down) //col constante line variable
			{
				cl=(line+x)%zone.getHeight();
				if(matrix.getAreaMatrix()[cl][col]==State.INDESTRUCTIBLE)
					down=false;
				else if(matrix.getAreaMatrix()[cl][col]<State.BONUSDANGER)
				{
					if(dk.getMatrix().getAreaMatrix()[cl][col]==State.RIVAL)
						matrix.changeState(cl,col, State.BOMB);
					else
						matrix.changeState(cl, col, State.DANGER);
					if(dk.getMatrix().getTimeLeft()[cl][col]==-1)
						matrix.changeTimeLeft(cl,col, BOMBWILLEXPLODE);
					safes.remove(zone.getTile(cl, col));
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
