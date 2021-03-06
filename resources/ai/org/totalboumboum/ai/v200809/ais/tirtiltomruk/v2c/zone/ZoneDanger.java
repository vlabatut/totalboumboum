package org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2c.zone;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.totalboumboum.ai.v200809.adapter.AiBlock;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiFire;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiItem;
import org.totalboumboum.ai.v200809.adapter.AiItemType;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2c.TirtilTomruk;

/**
*
* @author Abdullah Tırtıl
* @author Mert Tomruk
*
*/
@SuppressWarnings("deprecation")
public class ZoneDanger {
	/** Une personnalisation du AiZone qui se specifie sur les dangers de la zone */
	private AiZone zone;
	/** */
	private Collection <AiHero> rivals;
	/** */
	private AiHero caractere;
	/** */
	private Collection <AiBomb> bombes;
	/** */
	private Vector <TimedBomb> timedBombes;
	/** */
	private Collection <AiBlock> blocs;
	/** */
	private Collection <AiItem> objets;
	/** */
	private Collection <AiFire> feus; 
	/** */
	private int xMax;
	/** */
	private int yMax;
	/** */
	private ZoneEnum [][] zoneArray;
	/** */
	private TirtilTomruk source;
	
	/**
	 * 
	 * @param zone
	 * 		Description manquante !
	 * @param source
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	@SuppressWarnings("unchecked")
	public ZoneDanger(AiZone zone, TirtilTomruk source) throws StopRequestException
	{
		source.checkInterruption(); //Appel Obligatoire
		this.source = source;
		this.bombes = zone.getBombs();
		this.timedBombes = (Vector<TimedBomb>) source.getTimedBombes().clone();
		this.zone = zone;
		this.caractere = zone.getOwnHero();
		rivals = zone.getHeroes();
		this.blocs = zone.getBlocks();
		this.objets = zone.getItems();
		this.feus = zone.getFires();
		this.xMax = zone.getWidth(); 
		this.yMax = zone.getHeight();
		init();
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
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
		Iterator<TimedBomb> itBombes = this.timedBombes.iterator();
		
			
		
		while(itBombes.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			TimedBomb temp = itBombes.next();
			
			//Les tiles dangeureux
			int k = 1;
			//Est-ce qu'on continue sur ces directions?
			boolean up = true;
			boolean down = true;
			boolean left = true;
			boolean right = true;
			while(k <= temp.getBomb().getRange() && (up || down || left || right))
			{
				source.checkInterruption(); //Appel Obligatoire
				if(up)
				{
					if((zoneArray[temp.getBomb().getCol()][temp.getBomb().getLine() - k] != ZoneEnum.BLOCDEST) && (zoneArray[temp.getBomb().getCol()][temp.getBomb().getLine() - k] != ZoneEnum.BLOCINDEST))
					{
						if(!this.zone.getTile(temp.getBomb().getLine() - k, temp.getBomb().getCol()).getBombs().isEmpty())
						{
							Iterator<AiBomb> itAiBomb = this.zone.getTile(temp.getBomb().getLine() - k, temp.getBomb().getCol()).getBombs().iterator();
							if(itAiBomb.hasNext())
							{
								TimedBomb tempTimedBomb = new TimedBomb(this.zone,itAiBomb.next(),0,source.getTime(),source);
								this.timedBombes.get(this.timedBombes.indexOf(tempTimedBomb)).setDanger(temp.getDanger());
							}
						}
						else
							zoneArray[temp.getBomb().getCol()][temp.getBomb().getLine() - k] = temp.getDanger();
					}
					else
					{
						up = false;
					}
				}
				if(down)
				{
					if((zoneArray[temp.getBomb().getCol()][temp.getBomb().getLine() + k] != ZoneEnum.BLOCDEST) && (zoneArray[temp.getBomb().getCol()][temp.getBomb().getLine() + k] != ZoneEnum.BLOCINDEST))
					{
						if(!this.zone.getTile(temp.getBomb().getLine() + k, temp.getBomb().getCol()).getBombs().isEmpty())
						{
							Iterator<AiBomb> itAiBomb = this.zone.getTile(temp.getBomb().getLine() + k, temp.getBomb().getCol()).getBombs().iterator();
							if(itAiBomb.hasNext())
							{
								TimedBomb tempTimedBomb = new TimedBomb(this.zone,itAiBomb.next(),0,source.getTime(),source);
								this.timedBombes.get(this.timedBombes.indexOf(tempTimedBomb)).setDanger(temp.getDanger());
							}
						}
						else
							zoneArray[temp.getBomb().getCol()][temp.getBomb().getLine() + k] = temp.getDanger();
					}
					else
					{
						down = false;
					}
				}
				if(left)
				{
					if((zoneArray[temp.getBomb().getCol() - k][temp.getBomb().getLine()] != ZoneEnum.BLOCDEST) && (zoneArray[temp.getBomb().getCol() - k][temp.getBomb().getLine()] != ZoneEnum.BLOCINDEST))
					{
						if(!this.zone.getTile(temp.getBomb().getLine(), temp.getBomb().getCol() - k).getBombs().isEmpty())
						{
							Iterator<AiBomb> itAiBomb = this.zone.getTile(temp.getBomb().getLine(), temp.getBomb().getCol() - k).getBombs().iterator();
							if(itAiBomb.hasNext())
							{
								TimedBomb tempTimedBomb = new TimedBomb(this.zone,itAiBomb.next(),0,source.getTime(),source);
								this.timedBombes.get(this.timedBombes.indexOf(tempTimedBomb)).setDanger(temp.getDanger());
							}
						}
						else
							zoneArray[temp.getBomb().getCol() - k][temp.getBomb().getLine()] = temp.getDanger();
					}
					else
					{
						left = false;
					}
				}
				if(right)
				{
					if((zoneArray[temp.getBomb().getCol() + k][temp.getBomb().getLine()] != ZoneEnum.BLOCDEST) && (zoneArray[temp.getBomb().getCol() + k][temp.getBomb().getLine()] != ZoneEnum.BLOCINDEST))
					{
						if(!this.zone.getTile(temp.getBomb().getLine(), temp.getBomb().getCol() + k).getBombs().isEmpty())
						{
							Iterator<AiBomb> itAiBomb = this.zone.getTile(temp.getBomb().getLine(), temp.getBomb().getCol() + k).getBombs().iterator();
							if(itAiBomb.hasNext())
							{
								TimedBomb tempTimedBomb = new TimedBomb(this.zone,itAiBomb.next(),0,source.getTime(),source);
								this.timedBombes.get(this.timedBombes.indexOf(tempTimedBomb)).setDanger(temp.getDanger());
							}
						}
						else
							zoneArray[temp.getBomb().getCol() + k][temp.getBomb().getLine()] = temp.getDanger();
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
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public ZoneEnum[][] getZoneArray() throws StopRequestException {
		source.checkInterruption(); //Appel Obligatoire
		return zoneArray;
	}

	@Override
	public String toString()
	{	
		String result = "";
		for(int i = 0; i < yMax; i++)
		{
			for(int j = 0; j < xMax; j++)
			{
				result = result + zone.getTile(i,j) + this.zoneArray[j][i] + "    ";
			}
			result = result + "\n";
		}
		return result;
	}

}
