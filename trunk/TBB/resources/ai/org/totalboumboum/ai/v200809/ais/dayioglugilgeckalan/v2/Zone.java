package org.totalboumboum.ai.v200809.ais.dayioglugilgeckalan.v2;

import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v200809.adapter.AiBlock;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiFire;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiItem;
import org.totalboumboum.ai.v200809.adapter.AiItemType;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

/**
 * 
 * @author Ali Batuhan Dayioğlugil
 * @author Gökhan Geçkalan
 *
 */
@SuppressWarnings("deprecation")
public class Zone{
	//Une personnalisation du AiZone qui se specifie sur les dangers de la zone
	//private AiZone zone;
	private Collection <AiHero> rivals;
	private AiHero caractere;
	private Collection <AiBomb> bombes;
	private Collection <AiBlock> blocs;
	private Collection <AiItem> objets;
	private Collection <AiFire> feus; 
	private int xMax;
	private int yMax;
	private ZoneEnum [][] zoneArray;
	private DayioglugilGeckalan source;
	
	/**
	 * 
	 * @param zone
	 * @param source
	 * @throws StopRequestException
	 */
	public Zone(AiZone zone, DayioglugilGeckalan source) throws StopRequestException
	{
		source.checkInterruption(); //Appel Obligatoire
		this.source = source;
		//this.zone = zone;
		this.caractere = zone.getOwnHero();
		rivals = zone.getHeroes();
		this.bombes = zone.getBombs();
		this.blocs = zone.getBlocks();
		this.objets = zone.getItems();
		this.feus = zone.getFires();
		this.xMax = zone.getWidth(); 
		this.yMax = zone.getHeight();
		init();
	}
	
	private void init() throws StopRequestException
	{
		source.checkInterruption(); //Appel Obligatoire
		zoneArray = new ZoneEnum [xMax][yMax];
		int i,j;
		//Initialisation
		for(i = 0; i < xMax; i++)
		{	
			source.checkInterruption(); //Appel Obligatoire
			for(j = 0; j < yMax; j++)
			{
				source.checkInterruption(); //Appel Obligatoire
				zoneArray[i][j] = ZoneEnum.LIBRE;
			}			
		}
		//Mettons notre caractere
		zoneArray[caractere.getTile().getCol()][caractere.getTile().getLine()] = ZoneEnum.CARACTERE;
		
		//Mettons nos rivals
		Iterator <AiHero> itRivals = rivals.iterator();
		while(itRivals.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiHero temp = itRivals.next();
			if(!temp.equals(caractere))
				zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.RIVAL;
		}
		
		//Mettons les blocs
		Iterator <AiBlock> itBlocs = blocs.iterator();
		while(itBlocs.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiBlock temp = itBlocs.next();
			if(temp.isDestructible())
				zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.BLOCDEST;
			else
				zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.BLOCINDEST;
		}
		
		//Mettons les bombes et les feus possibles
		Iterator <AiBomb> itBombes = bombes.iterator();
		while(itBombes.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiBomb temp = itBombes.next();
			
			/*for(int k = temp.getCol() - temp.getRange(); k < temp.getCol() + temp.getRange() && k < xMax && (zoneArray[k][temp.getLine()] != ZoneEnum.BLOCDEST || zoneArray[k][temp.getLine()] != ZoneEnum.BLOCINDEST); k++)
				zoneArray[k][temp.getLine()] = ZoneEnum.FEUPOSSIBLE;
			for(int k = temp.getLine() - temp.getRange(); k < temp.getLine() + temp.getRange() && k < yMax && (zoneArray[temp.getCol()][k] != ZoneEnum.BLOCDEST || zoneArray[temp.getCol()][k] != ZoneEnum.BLOCINDEST); k++)
				zoneArray[temp.getCol()][k] = ZoneEnum.FEUPOSSIBLE;
			*/
			
			//Les tiles dangeureux
			int k = 1;
			//Est-ce qu'on continue sur ces directions?
			boolean up = true;
			boolean down = true;
			boolean left = true;
			boolean right = true;
			while(k <= temp.getRange() && (up || down || left || right))
			{
				source.checkInterruption(); //Appel Obligatoire
				if(up)
				{
					if((zoneArray[temp.getCol()][temp.getLine() - k] != ZoneEnum.BLOCDEST) && (zoneArray[temp.getCol()][temp.getLine() - k] != ZoneEnum.BLOCINDEST))
					{
						zoneArray[temp.getCol()][temp.getLine() - k] = ZoneEnum.FEUPOSSIBLE;
					}
					else
					{
						up = false;
					}
				}
				if(down)
				{
					if((zoneArray[temp.getCol()][temp.getLine() + k] != ZoneEnum.BLOCDEST) && (zoneArray[temp.getCol()][temp.getLine() + k] != ZoneEnum.BLOCINDEST))
					{
						zoneArray[temp.getCol()][temp.getLine() + k] = ZoneEnum.FEUPOSSIBLE;
					}
					else
					{
						down = false;
					}
				}
				if(left)
				{
					if((zoneArray[temp.getCol() - k][temp.getLine()] != ZoneEnum.BLOCDEST) && (zoneArray[temp.getCol() - k][temp.getLine()] != ZoneEnum.BLOCINDEST))
					{
						zoneArray[temp.getCol() - k][temp.getLine()] = ZoneEnum.FEUPOSSIBLE;
					}
					else
					{
						left = false;
					}
				}
				if(right)
				{
					if((zoneArray[temp.getCol() + k][temp.getLine()] != ZoneEnum.BLOCDEST) && (zoneArray[temp.getCol() + k][temp.getLine()] != ZoneEnum.BLOCINDEST))
					{
						zoneArray[temp.getCol() + k][temp.getLine()] = ZoneEnum.FEUPOSSIBLE;
					}
					else
					{
						right = false;
					}
				}
				k++;
			}			
		}
		
		//Mettons les bombes
		Iterator <AiBomb> itBombes2 = bombes.iterator();
		while(itBombes2.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiBomb temp = itBombes2.next();
			zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.BOMBE;		
		}
		
		//Mettons les feus
		Iterator <AiFire> itFeus = feus.iterator();
		while(itFeus.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiFire temp = itFeus.next();
			zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.FEU;		
		}
		
		
				
		//Mettons les bonus
		Iterator <AiItem> itObjets = objets.iterator();
		while(itObjets.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiItem temp = itObjets.next();
			if(temp.getType() == AiItemType.EXTRA_BOMB)
				zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.BONUSBOMBE;
			if(temp.getType() == AiItemType.EXTRA_FLAME)
				zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.BONUSFEU;
		}
	}

	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public ZoneEnum[][] getZoneArray() throws StopRequestException {
		source.checkInterruption(); //Appel Obligatoire
		return zoneArray;
	}

	public String toString()
	{	
		String result = "";
		for(int i = 0; i < yMax;i++)
		{	
			for(int j = 0; j < xMax; j++)
				result += "(" + i + "," + j + ")" + zoneArray[j][i] + "   ";
			result += "\n";
		}
		
		return result;
	}
	
}
