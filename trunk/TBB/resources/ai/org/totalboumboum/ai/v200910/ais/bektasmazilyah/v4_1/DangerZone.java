package org.totalboumboum.ai.v200910.ais.bektasmazilyah.v4_1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiItemType;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @version 4.1
 * 
 * @author Erdem Bektaş
 * @author Nedim Mazilyah
 *
 */
public class DangerZone {
	
	private BektasMazilyah source;
	private AiZone map;
	private AiHero hero;
	private Collection<AiHero> rivals;
	private Collection<AiBlock> blocs;
 	private Collection<AiBomb> bombes;
 	private Collection <AiFire> feus;
	private Collection<AiItem> objets;
	private EtatEnum [][] ourZone;
	int x;
	int y;
	
	public DangerZone(AiZone zone, BektasMazilyah source) throws StopRequestException
	{
		source.checkInterruption();
		this.map=zone;
		this.source=source;
		this.hero=zone.getOwnHero();
		this.rivals=zone.getHeroes();
		this.blocs=zone.getBlocks();
		this.bombes=zone.getBombs();
		this.feus=zone.getFires();
		this.objets=zone.getItems();
		this.x=zone.getWidth();
		this.y=zone.getHeight();
		
		init();

	}

	private void init() throws StopRequestException {
		
		source.checkInterruption();
		
		ourZone = new EtatEnum [x][y];
		int i,j;
		
		//Initialisation
		for(i = 0; i < x; i++)
		{	
			source.checkInterruption(); //Appel Obligatoire
			for(j = 0; j < y; j++)
			{
				source.checkInterruption(); //Appel Obligatoire
				ourZone[i][j] = EtatEnum.LIBRE;
			}
		}
		
		//Mettons notre hero
		ourZone[hero.getTile().getCol()][hero.getTile().getLine()] = EtatEnum.HERO;
		
		//Mettons nos rivals
		Iterator <AiHero> itRivals = rivals.iterator();
		while(itRivals.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiHero temp = itRivals.next();
			if(!temp.equals(hero))
				ourZone[temp.getCol()][temp.getLine()] = EtatEnum.RIVAL;
		}
		
		//Mettons les blocs
		Iterator <AiBlock> itBlocs = blocs.iterator();
		while(itBlocs.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiBlock temp = itBlocs.next();
			if(temp.isDestructible())
				ourZone[temp.getCol()][temp.getLine()] = EtatEnum.BLOCDEST;
			else
				ourZone[temp.getCol()][temp.getLine()] = EtatEnum.BLOCINDEST;
		}
		
		//Mettons les bonus
		Iterator <AiItem> itObjets = objets.iterator();
		while(itObjets.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiItem temp = itObjets.next();
			if(temp.getType() == AiItemType.EXTRA_BOMB);
				ourZone[temp.getCol()][temp.getLine()] = EtatEnum.BONUSBOMBE;
			if(temp.getType() == AiItemType.EXTRA_FLAME)
				ourZone[temp.getCol()][temp.getLine()] = EtatEnum.BONUSFEU;
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
			while(k < temp.getRange()+1 && (up || down || left || right))
			{
				
				source.checkInterruption(); //Appel Obligatoire
				int a =0;
					AiTile temp1 = temp.getTile();
					while(up && a<k)
					{
						source.checkInterruption(); //Appel Obligatoire
						AiTile temp2 = temp1.getNeighbor(Direction.UP);
						temp1 = temp2; 
						int x = temp2.getCol();
						int y = temp2.getLine();
						if((ourZone[x][y] != EtatEnum.BLOCDEST) && (ourZone[x][y] != EtatEnum.BLOCINDEST))
						{
							ourZone[x][y] = EtatEnum.DANGER;
							a++;
						}
						else up = false;
					}
					a = 0;
					temp1 = temp.getTile();
					
				
					while(down && a<k)
					{
						source.checkInterruption(); //Appel Obligatoire
						AiTile temp2 = temp1.getNeighbor(Direction.DOWN);
						temp1 = temp2; 
						int x = temp2.getCol();
						int y = temp2.getLine();
						if((ourZone[x][y] != EtatEnum.BLOCDEST) && (ourZone[x][y] != EtatEnum.BLOCINDEST))
						{
							ourZone[x][y] = EtatEnum.DANGER;
							a++;
						}
						else down = false;
					}
					a = 0;
					temp1 = temp.getTile();
					while(left && a<k)
					{
						source.checkInterruption(); //Appel Obligatoire
						AiTile temp2 = temp1.getNeighbor(Direction.LEFT);
						temp1 = temp2; 
						int x = temp2.getCol();
						int y = temp2.getLine();
						if((ourZone[x][y] != EtatEnum.BLOCDEST) && (ourZone[x][y] != EtatEnum.BLOCINDEST))
						{
							ourZone[x][y] = EtatEnum.DANGER;
							a++;
						}
						else left = false;
					}
					a = 0;
					temp1 = temp.getTile();
					while(right && a<k)
					{
						source.checkInterruption(); //Appel Obligatoire
						AiTile temp2 = temp1.getNeighbor(Direction.RIGHT);
						temp1 = temp2; 
						int x = temp2.getCol();
						int y = temp2.getLine();
						if((ourZone[x][y] != EtatEnum.BLOCDEST) && (ourZone[x][y] != EtatEnum.BLOCINDEST))
						{
							ourZone[x][y] = EtatEnum.DANGER;
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
			ourZone[temp.getCol()][temp.getLine()] = EtatEnum.BOMBE;		
		}
		
		//Mettons les feus
		Iterator <AiFire> itFeus = feus.iterator();
		while(itFeus.hasNext())
		{
			source.checkInterruption(); //Appel Obligatoire
			AiFire temp = itFeus.next();
			ourZone[temp.getCol()][temp.getLine()] = EtatEnum.FEU;		
		}
				
	}
		
	public EtatEnum[][] getOurZone() throws StopRequestException {
		source.checkInterruption();
		return ourZone;
	}

	public EtatEnum getValeur(int x, int y) throws StopRequestException {
		source.checkInterruption(); //Appel Obligatoire
		return ourZone[x][y];
	}

	public List<AiTile> findSafeTiles() throws StopRequestException
	{	source.checkInterruption(); //APPEL OBLIGATOIRE
	
		List<AiTile> result = new ArrayList<AiTile>();
		for(int line=0;line<y;line++)
		{	source.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<x;col++)
			{	source.checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile;
				if(source.isClear(col,line))
					{
					  
						tile=map.getTile(line, col);
						Astar a=new Astar (this,hero.getCol(),hero.getLine(),col,line);
							if ((hero.getCol()!= col || hero.getLine() != line) && a.findPath())
								result.add(tile);
					}
			}
		}
//	System.out.println(result.toString());
		
		return result;
	}
		
	public List<AiTile> findBonusTiles() throws StopRequestException
	{	source.checkInterruption(); //APPEL OBLIGATOIRE
	
	List<AiTile> result = new ArrayList<AiTile>();
		for(int line=0;line<y;line++)
		{	source.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<x;col++)
			{	source.checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile;
				if(source.isBonus(col, line) && !(source.isDanger(col,line)))
				{
					tile=map.getTile(line, col);
					Astar a=new Astar (this,hero.getCol(),hero.getLine(),col,line);
					if ((hero.getCol()!= col || hero.getLine() != line) && a.findSecurePath())
						result.add(tile);
				}
			}
		}
	//	System.out.println(result.toString());
		return result;
	}
	
	public List<AiTile> findDangerTiles() throws StopRequestException
	{	source.checkInterruption(); //APPEL OBLIGATOIRE
	
	List<AiTile> result = new ArrayList<AiTile>();
		for(int line=0;line<y;line++)
		{	source.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<x;col++)
			{	source.checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile;
				if(source.isDanger(col,line))
					{
					  
						tile=map.getTile(line, col);
						
						Astar a=new Astar (this,hero.getCol(),hero.getLine(),col,line);
							if ((hero.getCol()!= col || hero.getLine() != line) && a.findPath())
								result.add(tile);
					}
			}
		}
//	System.out.println(result.toString());
		
		return result;
	}
	
	public List<AiTile> findDesctructibleTiles() throws StopRequestException
	{	source.checkInterruption(); //APPEL OBLIGATOIRE
	
		List<AiTile> result = new ArrayList<AiTile>();
		for(int line=0;line<y;line++)
		{	source.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<x;col++)
			{	source.checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile;
				if(source.isDesctructible(col, line))
					{
						tile=map.getTile(line, col);
						result.add(tile);
					}
			}
		}
//	System.out.println(result.toString());
		
		return result;
	}
	
	public List<AiTile> findRivalsTiles() throws StopRequestException
	{	source.checkInterruption(); //APPEL OBLIGATOIRE
	
		List<AiTile> result = new ArrayList<AiTile>();
		for(int line=0;line<y;line++)
		{	source.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<x;col++)
			{	source.checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile;
				if(source.isRival(col, line))
					{
						tile=map.getTile(line, col);
						Astar a=new Astar (this,hero.getCol(),hero.getLine(),col,line);
						if ((hero.getCol()!= col || hero.getLine() != line) && a.findSecurePath())
							result.add(tile);
					}
			}
		}
//	System.out.println(result.toString());
		
		return result;
	}

	public List<AiTile> findTilesForDestruct(List<AiTile> neighbors) throws StopRequestException
	{	source.checkInterruption(); //APPEL OBLIGATOIRE
	
		List<AiTile> result = new ArrayList<AiTile>();
		for(int line =0; line <y; line++)
		{	source.checkInterruption(); //APPEL OBLIGATOIR
			for(int col = 0; col < x; col++)
			{	
				source.checkInterruption(); //APPEL OBLIGATOIRE	
				AiTile tile;
				tile=map.getTile(line, col);
				if(neighbors.contains(tile))
				{
					Astar a=new Astar (this,hero.getCol(),hero.getLine(),col,line);
					if ((hero.getCol()!= col || hero.getLine() != line) && a.findSecurePath())
						result.add(tile);
				}	
			}
		}
		
		
//	System.out.println(result.toString());
		return result;
	}
	
	public void afficheDestructibles(List<AiTile> destructibles) throws StopRequestException
	{
		source.checkInterruption();
		System.out.println(destructibles);
	}
	
	public boolean isWalkable(int x2, int y2) throws StopRequestException {
		source.checkInterruption();
		if (ourZone[x2][y2]==EtatEnum.FEU || ourZone[x2][y2]==EtatEnum.BOMBE || ourZone[x2][y2]==EtatEnum.BLOCINDEST || ourZone[x2][y2]==EtatEnum.BLOCDEST)
			return false;
		else
			return true;
	}
	
	public boolean canGoToBonus(int x2,int y2) throws StopRequestException
	{
		source.checkInterruption();
		if (ourZone[x2][y2]==EtatEnum.DANGER || ourZone[x2][y2]==EtatEnum.FEU || ourZone[x2][y2]==EtatEnum.BOMBE || ourZone[x2][y2]==EtatEnum.BLOCINDEST || ourZone[x2][y2]==EtatEnum.BLOCDEST)
			return false;
		else
			return true;
	}
	
	public boolean canGoRival(int x2, int y2) throws StopRequestException
	{
		source.checkInterruption();
		if (ourZone[x2][y2]==EtatEnum.DANGER || ourZone[x2][y2]==EtatEnum.FEU || ourZone[x2][y2]==EtatEnum.BOMBE || ourZone[x2][y2]==EtatEnum.BLOCINDEST)
			return false;
		else
			return true;
	}
	
	public void afficheDangerZone() throws StopRequestException
	{
		source.checkInterruption();
		for(int i = 0; i<map.getWidth() ; i++)
		{
			source.checkInterruption();
			for(int j = 0; j < map.getHeight(); j++)
			{
				source.checkInterruption();
				System.out.print(i+":"+j+":"+ ourZone[i][j]);
			}
			System.out.println();
		}
	}
	
}
