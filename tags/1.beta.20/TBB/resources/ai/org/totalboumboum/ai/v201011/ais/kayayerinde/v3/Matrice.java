package org.totalboumboum.ai.v201011.ais.kayayerinde.v3;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.*;

@SuppressWarnings("unused")

public class Matrice {
	private double nHero=0;
	private double nFree=0;
	private double nDestructible=50;
	private double nItem=3;
	private double nRival=4;
	private double nBlock=100;
	private double nFeu=200;
	private double nBombe=250;
	private double nFire=500;
	private AiZone zone;
	private List<AiBlock> blocks;
	private List<AiBomb> bombes;
	private int width, height;
	private double[][] areaMatrix;
	private KayaYerinde ai;
	private AiHero ownHero;
	
	public Matrice(KayaYerinde ai1)throws StopRequestException
	{
		ai1.checkInterruption();
		this.zone=ai1.getPercepts();
		this.ai=ai1;
		init();
		
	}
	
	public void init() throws StopRequestException
	{
		ai.checkInterruption();
		width=zone.getWidth();
		height=zone.getHeight();
		ownHero=zone.getOwnHero();
		areaMatrix=new double[height][width];
		this.createMatrice();
	}
	
	public void createMatrice() throws StopRequestException
	{
		ai.checkInterruption();
		int i, j;
		for(i=0;i<height;i++)
		{
			ai.checkInterruption();
			for(j=0;j<width;j++)
			{
				ai.checkInterruption();
				areaMatrix[i][j]=nFree;
			}
		}
		
		ai.checkInterruption();
		List<AiBlock> blocks1=zone.getBlocks();
		Iterator<AiBlock> blockIt= blocks1.iterator();
		AiBlock b;
		while(blockIt.hasNext())
		{
			ai.checkInterruption();
			b=blockIt.next();
			if(!b.isCrossableBy(ownHero))
			{
				if(b.isDestructible())
					areaMatrix[b.getLine()][b.getCol()]=nDestructible;
				else
					areaMatrix[b.getLine()][b.getCol()]=nBlock;
			}
		}
		
		/*ai.checkInterruption();
		List<AiItem> items1=zone.getItems();
		Iterator<AiItem> itemIt= items1.iterator();
		AiItem it;
		while(itemIt.hasNext())
		{
			ai.checkInterruption();
			it=itemIt.next();
			areaMatrix[it.getLine()][it.getCol()]=nItem;
		}*/
		
		/*ai.checkInterruption();
		List<AiHero> rivals1=zone.getRemainingHeroes();
		rivals1.remove(ownHero);
		Iterator<AiHero> rivalIt=rivals1.iterator();
		AiHero h;
		while(rivalIt.hasNext())
		{
			ai.checkInterruption();
			h=rivalIt.next();
			areaMatrix[h.getLine()][h.getCol()]=nRival;
		}
		
		ai.checkInterruption();
		areaMatrix[ownHero.getLine()][ownHero.getCol()]=nHero;*/
		
		ai.checkInterruption();
		List<AiBomb> bombes1=zone.getBombs();
		Iterator<AiBomb> bombIt=bombes1.iterator();
		AiBomb bo;
		while(bombIt.hasNext())
		{
			ai.checkInterruption();
			bo=bombIt.next();
			areaMatrix[i=bo.getLine()][j=bo.getCol()]=nBombe;
			while(areaMatrix[i+1][j]!=nBlock && areaMatrix[i+1][j]!=nDestructible && i<height-1)
			{
				ai.checkInterruption();
				areaMatrix[i+1][j]=areaMatrix[i+1][j]+nFeu;
				i++;
			}
			i=bo.getLine();
			while(areaMatrix[i-1][j]!=nBlock && areaMatrix[i-1][j]!=nDestructible && i>0)
			{
				ai.checkInterruption();
				areaMatrix[i-1][j]=areaMatrix[i-1][j]+nFeu;
				i--;
			}
			i=bo.getLine();
			while(areaMatrix[i][j+1]!=nBlock && areaMatrix[i][j+1]!=nDestructible && j<width-1)
			{
				ai.checkInterruption();
				areaMatrix[i][j+1]=areaMatrix[i][j+1]+nFeu;
				j++;
			}
			j=bo.getCol();
			while(areaMatrix[i][j-1]!=nBlock && areaMatrix[i][j-1]!=nDestructible && j>0)
			{
				ai.checkInterruption();
				areaMatrix[i][j-1]=areaMatrix[i][j-1]+nFeu;
				j--;
			}
		}
		
		ai.checkInterruption();
		List<AiFire> fires1=zone.getFires();
		Iterator<AiFire> fireIt=fires1.iterator();
		AiFire f;
		while(fireIt.hasNext())
		{
			ai.checkInterruption();
			f=fireIt.next();
			areaMatrix[f.getLine()][f.getCol()]=nFire;
		}
		
	}
	
	public double[][] getMatrice() throws StopRequestException
	{
		ai.checkInterruption();
		return areaMatrix;
	}
	
	public AiTile getBombTile(AiHero hero) throws StopRequestException
	{
		ai.checkInterruption();
		AiTile tile=null;
		if(areaMatrix[hero.getLine()][hero.getCol()]!=nFree)
		{
			Iterator<AiBomb> bombIt=bombes.iterator();
			AiBomb bo;
			while(bombIt.hasNext())
			{
				ai.checkInterruption();
				bo=bombIt.next();
				if(bo.getCol()==hero.getCol() || bo.getLine()==hero.getLine())
					tile=bo.getTile();
			}
		}
		return tile;
	}
	public List<AiBlock> getBlocks()
	{
		return blocks;
	}

}
