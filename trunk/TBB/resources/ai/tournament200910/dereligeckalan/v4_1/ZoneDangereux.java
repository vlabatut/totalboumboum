package tournament200910.dereligeckalan.v4_1;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;





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


public class ZoneDangereux {
	
	private Collection <AiHero> rivals;
	private AiHero caractere;
	private Collection <AiBomb> bombes;
	private Collection <AiBlock> blocs;
	private Collection <AiItem> objets;
	private Collection <AiFire> feus; 
	private int xMax;
	private int yMax;
	private double matris[][];
	private DereliGeckalan source;
	public ZoneDangereux(AiZone zone, DereliGeckalan source) throws StopRequestException {
		source.checkInterruption(); //Appel Obligatoire
		this.source = source;
		//this.zone = zone;
		this.caractere = zone.getOwnHero();
		this.rivals = zone.getRemainingHeroes();
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
		matris = new double [yMax][xMax];
		int i,j;
		//Initialisation
		for(i = 0; i < xMax; i++)
		{	
			source.checkInterruption(); //Appel Obligatoire
			for(j = 0; j < yMax; j++)
			{
				source.checkInterruption(); //Appel Obligatoire
				matris[j][i] = 0 - nombreMur(source.getPercepts().getTile(j,i));
			}			
		}
		//Mettons notre caractere
		//matris[caractere.getTile().getLine()][caractere.getTile().getCol()] = 1;
		
		//Mettons nos rivals
		Iterator <AiHero> itRivals = rivals.iterator();
		while(itRivals.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiHero temp = itRivals.next();
			if(!temp.equals(caractere) && temp !=null)
				matris[temp.getLine()][temp.getCol()] = -11;
		}
		
		//Mettons les blocs
		Iterator <AiBlock> itBlocs = blocs.iterator();
		while(itBlocs.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiBlock temp = itBlocs.next();
			if(temp.isDestructible())
			{
				matris[temp.getLine()][temp.getCol()] = -2;
				
			}
			else
				matris[temp.getLine()][temp.getCol()] = 0;
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
				
				source.checkInterruption(); //Appel Obligatoire
				int a =0;
					AiTile temp1 = temp.getTile();
					while(up && a<k)
					{
						source.checkInterruption();
						AiTile temp2 = temp1.getNeighbor(Direction.UP);
						temp1 = temp2; 
						int x = temp2.getCol();
						int y = temp2.getLine();
						if((matris[y][x] != 0) && (matris[y][x] != -2) )
						{
							matris[y][x] = (temp.getNormalDuration() - temp.getTime());
							a++;
						}
						else up = false;
					}
					a = 0;
					temp1 = temp.getTile();
					
				
					while(down && a<k)
					{
						source.checkInterruption();
						AiTile temp2 = temp1.getNeighbor(Direction.DOWN);
						temp1 = temp2; 
						int x = temp2.getCol();
						int y = temp2.getLine();
						if((matris[y][x] != 0) && (matris[y][x] != -2))
						{
							matris[y][x] = (temp.getNormalDuration() - temp.getTime());
							a++;
						}
						else down = false;
					}
					a = 0;
					temp1 = temp.getTile();
					while(left && a<k)
					{
						source.checkInterruption();
						AiTile temp2 = temp1.getNeighbor(Direction.LEFT);
						temp1 = temp2; 
						int x = temp2.getCol();
						int y = temp2.getLine();
						if((matris[y][x] != 0) && (matris[y][x] != -2))
						{
							matris[y][x] = (temp.getNormalDuration() - temp.getTime());
							a++;
						}
						else left = false;
					}
					a = 0;
					temp1 = temp.getTile();
					while(right && a<k)
					{
						source.checkInterruption();
						AiTile temp2 = temp1.getNeighbor(Direction.RIGHT);
						temp1 = temp2; 
						int x = temp2.getCol();
						int y = temp2.getLine();
						if((matris[y][x] != 0) && (matris[y][x] != -2))
						{
							matris[y][x] = (temp.getNormalDuration() - temp.getTime());
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
			matris[temp.getLine()][temp.getCol()] = temp.getNormalDuration();		
		}
		
		//Mettons les feus
		Iterator <AiFire> itFeus = feus.iterator();
		while(itFeus.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiFire temp = itFeus.next();
			matris[temp.getLine()][temp.getCol()] = 10;		
		}
		
		
				
		//Mettons les bonus
		Iterator <AiItem> itObjets = objets.iterator();
		while(itObjets.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiItem temp = itObjets.next();
			
			if(temp.getType() == AiItemType.EXTRA_BOMB)
				matris[temp.getLine()][temp.getCol()] = -1;
			if(temp.getType() == AiItemType.EXTRA_FLAME)
				matris[temp.getLine()][temp.getCol()] = -1;
		}
	}
	private int nombreMur(AiTile target) throws StopRequestException {
		source.checkInterruption();
		
		int res = 1;
		Collection<AiBlock> bloks = source.getPercepts().getBlocks();
		LinkedList<AiTile> blokZone = new LinkedList<AiTile>();
		Iterator<AiBlock> it1 = bloks.iterator();
		while(it1.hasNext())
		{
			source.checkInterruption();
			AiBlock blok = it1.next();
			AiTile temp = blok.getTile();
			if(blok.isDestructible())
			{
				blokZone.add(temp);
			}
		}
 		AiTile up = target.getNeighbor(Direction.UP);
		AiTile down = target.getNeighbor(Direction.DOWN);
		AiTile right = target.getNeighbor(Direction.RIGHT);
		AiTile left = target.getNeighbor(Direction.LEFT);
		if(blokZone.contains(up))
			res ++;
		if(blokZone.contains(down))
			res++;
		if(blokZone.contains(left))
			res++;
		if(blokZone.contains(right))
			res++;
		return res;
	}
	public double[][] getZoneArray() throws StopRequestException {
		source.checkInterruption(); //Appel Obligatoire
		return matris;
	}
	public String toString()
	{	
		String result = "";
		for(int i = 0; i < yMax;i++)
		{	
			for(int j = 0; j < xMax; j++)
				result += "(" + i + "," + j + ")" + matris[i][j] + "   ";
			result += "\n";
		}
		
		return result;
	}
}
