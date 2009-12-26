package tournament200910.dereligeckalan.v4_2;

import java.util.Collection;
import java.util.Iterator;




import fr.free.totalboumboum.ai.adapter200910.data.AiItemType;

import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiBlock;
import fr.free.totalboumboum.ai.adapter200910.data.AiBomb;
import fr.free.totalboumboum.ai.adapter200910.data.AiFire;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiItem;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;

import fr.free.totalboumboum.ai.adapter200910.data.AiZone;
import fr.free.totalboumboum.engine.content.feature.Direction;


public class Zone {
	private Collection <AiHero> rivals;
	private AiHero caractere;
	private Collection <AiBomb> bombes;
	private Collection <AiBlock> blocs;
	private Collection <AiItem> objets;
	private Collection <AiFire> feus; 
	private int xMax;
	private int yMax;
	private ZoneEnum [][] zoneArray;
	private DereliGeckalan source;
	public Zone(AiZone zone, DereliGeckalan source) throws StopRequestException
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
		this.yMax = zone.getHeigh();
		init();
		//String s = toString();
		//System.out.println(s);
		
	}
	private void init() throws StopRequestException
	{
		source.checkInterruption(); //Appel Obligatoire
		zoneArray = new ZoneEnum [yMax][xMax];
		int i,j;
		//Initialisation
		for(i = 0; i < xMax; i++)
		{	
			source.checkInterruption(); //Appel Obligatoire
			for(j = 0; j < yMax; j++)
			{
				source.checkInterruption(); //Appel Obligatoire
				zoneArray[j][i] = ZoneEnum.LIBRE;
			}			
		}
		//Mettons notre caractere
		zoneArray[caractere.getTile().getLine()][caractere.getTile().getCol()] = ZoneEnum.CARACTERE;
		
		//Mettons nos rivals
		Iterator <AiHero> itRivals = rivals.iterator();
		while(itRivals.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiHero temp = itRivals.next();
			if(!temp.equals(caractere))
				zoneArray[temp.getLine()][temp.getCol()] = ZoneEnum.RIVAL;
		}
		
		//Mettons les blocs
		Iterator <AiBlock> itBlocs = blocs.iterator();
		while(itBlocs.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiBlock temp = itBlocs.next();
			if(temp.isDestructible())
				zoneArray[temp.getLine()][temp.getCol()] = ZoneEnum.BLOCDEST;
			else
				zoneArray[temp.getLine()][temp.getCol()] = ZoneEnum.BLOCINDEST;
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
			int k = 0;
			//Est-ce qu'on continue sur ces directions?
			boolean up = true;
			boolean down = true;
			boolean left = true;
			boolean right = true;
			while(k < temp.getRange() && (up || down || left || right))
			{
				int a =0;
				source.checkInterruption(); //Appel Obligatoire
				
					AiTile temp1 = temp.getTile();
					while(up && a<k)
					{
						
						AiTile temp2 = temp1.getNeighbor(Direction.UP);
						temp1 = temp2; 
						int x = temp2.getCol();
						int y = temp2.getLine();
						if((zoneArray[y][x] != ZoneEnum.BLOCDEST) && (zoneArray[y][x] != ZoneEnum.BLOCINDEST))
						{
							zoneArray[y][x] = ZoneEnum.FEUPOSSIBLE;
							a++;
						}
						else up = false;
					}
					a = 0;
					temp1 = temp.getTile();
					
				
					while(down && a<k)
					{
						
						AiTile temp2 = temp1.getNeighbor(Direction.DOWN);
						temp1 = temp2; 
						int x = temp2.getCol();
						int y = temp2.getLine();
						if((zoneArray[y][x] != ZoneEnum.BLOCDEST) && (zoneArray[y][x] != ZoneEnum.BLOCINDEST))
						{
							zoneArray[y][x] = ZoneEnum.FEUPOSSIBLE;
							a++;
						}
						else down = false;
					}
					a = 0;
					temp1 = temp.getTile();
					while(left && a<k)
					{
						
						AiTile temp2 = temp1.getNeighbor(Direction.LEFT);
						temp1 = temp2; 
						int x = temp2.getCol();
						int y = temp2.getLine();
						if((zoneArray[y][x] != ZoneEnum.BLOCDEST) && (zoneArray[y][x] != ZoneEnum.BLOCINDEST))
						{
							zoneArray[y][x] = ZoneEnum.FEUPOSSIBLE;
							a++;
						}
						else left = false;
					}
					a = 0;
					temp1 = temp.getTile();
					while(right && a<k)
					{
						
						AiTile temp2 = temp1.getNeighbor(Direction.RIGHT);
						temp1 = temp2; 
						int x = temp2.getCol();
						int y = temp2.getLine();
						if((zoneArray[y][x] != ZoneEnum.BLOCDEST) && (zoneArray[y][x] != ZoneEnum.BLOCINDEST))
						{
							zoneArray[y][x] = ZoneEnum.FEUPOSSIBLE;
							a++;
						}
						else right = false;
					}
					a = 0;
					temp1 = temp.getTile();
				k++;
			}			
		}
		
		//Mettons les bombes
		Iterator <AiBomb> itBombes2 = bombes.iterator();
		while(itBombes2.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiBomb temp = itBombes2.next();
			zoneArray[temp.getLine()][temp.getCol()] = ZoneEnum.BOMBE;		
		}
		
		//Mettons les feus
		Iterator <AiFire> itFeus = feus.iterator();
		while(itFeus.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiFire temp = itFeus.next();
			zoneArray[temp.getLine()][temp.getCol()] = ZoneEnum.FEU;		
		}
		
		
				
		//Mettons les bonus
		Iterator <AiItem> itObjets = objets.iterator();
		while(itObjets.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiItem temp = itObjets.next();
			
			if(temp.getType() == AiItemType.EXTRA_BOMB)
				zoneArray[temp.getLine()][temp.getCol()] = ZoneEnum.BONUSBOMBE;
			if(temp.getType() == AiItemType.EXTRA_FLAME)
				zoneArray[temp.getLine()][temp.getCol()] = ZoneEnum.BONUSFEU;
		}
	}

	
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
				result += "(" + i + "," + j + ")" + zoneArray[i][j] + "   ";
			result += "\n";
		}
		
		return result;
	}
}
