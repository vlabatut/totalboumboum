package org.totalboumboum.ai.v200910.ais.bektasmazilyah.v3;

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
 * @version 3
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
				int a =0;
				source.checkInterruption(); //Appel Obligatoire
				
					AiTile temp1 = temp.getTile();
					while(up && a<k)
					{
						
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
		
		
	}
	
	
	public EtatEnum[][] getOurZone() throws StopRequestException {
		source.checkInterruption();
		return ourZone;
	}

	public EtatEnum getValeur(int x2, int y2) {
		
		return ourZone[x2][y2];
	}

	public List<AiTile> findSafeTiles(AiTile origin) throws StopRequestException
	{	source.checkInterruption(); //APPEL OBLIGATOIRE
	
		List<AiTile> result = new ArrayList<AiTile>();
		for(int line=0;line<y;line++)
		{	source.checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<x;col++)
			{	source.checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile;
				if(source.isClear(x,y))
					{
						tile=map.getTile(x, y);
						result.add(tile);
					}
			}
		}
		
		return result;
	}
	
	public boolean isWalkable(int x1, int y1) {

		boolean resultat = false;

		if (ourZone[x1][y1] == EtatEnum.LIBRE || ourZone[x1][y1] == EtatEnum.RIVAL)
			resultat = true;

		return resultat;
	}

	/** nous allons lutiliser pour senfuire car on peut passer par les flmmes */
	public boolean isRunnable(int x1, int y1) {

		boolean resultat = false;

		if (ourZone[x1][y1] != EtatEnum.BOMBE
				&& ourZone[x1][y1] != EtatEnum.DANGER
				&& ourZone[x1][y1] != EtatEnum.BLOCDEST
				&& ourZone[x1][y1] != EtatEnum.FEU
				&& ourZone[x1][y1] != EtatEnum.BLOCINDEST
		)

			resultat = true;
		return resultat;
	}

	/**
	 * on va utiliser cette methode pour voir sil ya qqch quon peut acceder en
	 * laiissant des bombes car elle peut avoir des murs dest
	 */
	public boolean isReachable(int x1, int y1) {

		boolean resultat = false;

		if (ourZone[x1][y1] != EtatEnum.BOMBE 
				&& ourZone[x1][y1] != EtatEnum.DANGER 
				&& ourZone[x1][y1] != EtatEnum.FEU
				&& ourZone[x1][y1] != EtatEnum.BLOCINDEST)
				
				resultat = true;
		return resultat;
	}

	/**
	 * on peut passer par des danger et des flammes on la cree car qd qqn met
	 * deux bombes en meme temps on //ne bouge pas car on voit comme on na pas
	 * de lieu pour se cacher
	 */
	public boolean isNoWhereElse(int x1, int y1) {

		boolean resultat = false;

		if (ourZone[x1][y1] != EtatEnum.BOMBE
				&& ourZone[x1][y1] != EtatEnum.FEU
				&& ourZone[x1][y1] != EtatEnum.BLOCDEST
				&& ourZone[x1][y1] != EtatEnum.BLOCINDEST
		)

			resultat = true;
		return resultat;
	}
	
	
}
