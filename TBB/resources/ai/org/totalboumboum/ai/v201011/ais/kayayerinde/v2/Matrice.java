package org.totalboumboum.ai.v201011.ais.kayayerinde.v2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

/**
 * @author Önder Kaya
 * @author Nezaket Yerinde
 */
public class Matrice {
	private double nHero=0;
	private double nFree=1;
	private double nDestructible=2;
	private double nItem=3;
	private double nRival=4;
	private double nBlock=5;
	private double nFeu=6;
	private double nBombe=7;
	private double nFire=8;
	@SuppressWarnings("unused")
	private List<AiItem> items;
	private AiZone zone;
	private List<AiBlock> blocks;
	@SuppressWarnings("unused")
	private List<AiHero> rivals;
	private List<AiBomb> bombes;
	@SuppressWarnings("unused")
	private List<AiFire> fires;
	private int width, height;
	private double[][] areaMatrix;
	private KayaYerinde ai;
	private AiHero ownHero;
	
	public Matrice(KayaYerinde ai1)throws StopRequestException
	{
		ai1.checkInterruption();
		this.zone=ai1.getZone();
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
		rivals=new ArrayList<AiHero>();
		rivals=zone.getHeroes();
		items=new ArrayList<AiItem>();
		items=zone.getItems();
		blocks=new ArrayList<AiBlock>();
		blocks=zone.getBlocks();
		bombes=new ArrayList<AiBomb>();
		bombes=zone.getBombs();
		fires=new ArrayList<AiFire>();
		fires=zone.getFires();
		
	}
	
	public void createMatrice() throws StopRequestException
	{
		ai.checkInterruption();
		List<AiItem> items1=zone.getItems();
		List<AiBlock> blocks1=zone.getBlocks();
		List<AiHero> rivals1=zone.getRemainingHeroes();
		rivals1.remove(ownHero);
		List<AiBomb> bombes1=zone.getBombs();
		List<AiFire> fires1=zone.getFires();
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
		
		Iterator<AiBlock> blockIt= blocks1.iterator();
		AiBlock b;
		while(blockIt.hasNext())
		{
			ai.checkInterruption();
			b=blockIt.next();
			if(!b.isCrossableBy(ownHero))
			{
				if(b.isDestructible())
					areaMatrix[b.getCol()][b.getLine()]=nDestructible;
				else
					areaMatrix[b.getCol()][b.getLine()]=nBlock;
			}
		}
		
		Iterator<AiItem> itemIt= items1.iterator();
		AiItem it;
		while(itemIt.hasNext())
		{
			ai.checkInterruption();
			it=itemIt.next();
			areaMatrix[it.getCol()][it.getLine()]=nItem;
		}
		
		Iterator<AiHero> rivalIt=rivals1.iterator();
		AiHero h;
		while(rivalIt.hasNext())
		{
			ai.checkInterruption();
			h=rivalIt.next();
			areaMatrix[h.getCol()][h.getLine()]=nRival;
		}
		
		areaMatrix[ownHero.getCol()][ownHero.getLine()]=nHero;
		
		Iterator<AiBomb> bombIt=bombes1.iterator();
		AiBomb bo;
		while(bombIt.hasNext())
		{
			ai.checkInterruption();
			bo=bombIt.next();
			areaMatrix[i=bo.getCol()][j=bo.getLine()]=nBombe;
			while(areaMatrix[i][bo.getLine()]<nBlock && i<height)
			{
				ai.checkInterruption();
				i++;
			}
			i--;
			while(areaMatrix[i][bo.getLine()]<nBlock && i>=0)
			{
				ai.checkInterruption();
				areaMatrix[i][bo.getLine()]=nFeu;
				i--;
			}
			while(areaMatrix[bo.getCol()][j]<nBlock && j<width)
			{
				ai.checkInterruption();
				j++;
			}
			j--;
			while(areaMatrix[bo.getCol()][j]<nBlock && j>=0)
			{
				ai.checkInterruption();
				areaMatrix[bo.getCol()][j]=nFeu;
				j--;
			}
		}
		
		Iterator<AiFire> fireIt=fires1.iterator();
		AiFire f;
		while(fireIt.hasNext())
		{
			ai.checkInterruption();
			f=fireIt.next();
			areaMatrix[f.getCol()][f.getLine()]=nFire;
		}
		
	}
	
	public double[][] getMatrice() throws StopRequestException
	{
		ai.checkInterruption();
		this.createMatrice();
		return areaMatrix;
	}
	
	public AiTile getBombTile(AiHero hero) throws StopRequestException
	{
		ai.checkInterruption();
		AiTile tile=null;
		this.createMatrice();
		if(areaMatrix[hero.getCol()][hero.getLine()]==nFeu)
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
