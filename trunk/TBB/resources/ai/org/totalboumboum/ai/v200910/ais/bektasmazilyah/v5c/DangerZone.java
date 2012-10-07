package org.totalboumboum.ai.v200910.ais.bektasmazilyah.v5c;

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
 * @version 5.c
 * 
 * @author Erdem Bektaş
 * @author Nedim Mazilyah
 *
 */
@SuppressWarnings("deprecation")
public class DangerZone {
	
	/** code source */
	private BektasMazilyah source;
	/** zone du jeu */
	private AiZone map;
	/** notre own hero */
	private AiHero hero;
	/** les adversaires */
	private Collection<AiHero> rivals;
	/** les murs */
	private Collection<AiBlock> blocs;
	/** les bombes */
 	private Collection<AiBomb> bombes;
 	/** les feus */
 	private Collection <AiFire> feus;
 	/** les items */
	private Collection<AiItem> objets;
	/** dangerZone */
	private EtatEnum [][] ourZone;
	/** width de la zone */ 
	int x;
	/** height de la zone */
	int y;
	
	/**
	 * 
	 * @param zone
	 * @param source
	 * @throws StopRequestException
	 */
	public DangerZone(AiZone zone, BektasMazilyah source) throws StopRequestException
	{
		source.checkInterruption(); // appel obligatoire
		this.map=zone;
		this.source=source;
		this.hero=zone.getOwnHero();
		this.rivals=zone.getRemainingHeroes();
		this.blocs=zone.getBlocks();
		this.bombes=zone.getBombs();
		this.feus=zone.getFires();
		this.objets=zone.getItems();
		this.x=zone.getWidth();
		this.y=zone.getHeight();
		//initialiser
		init();

	}

	/**
	 * 
	 * @throws StopRequestException
	 */
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
						if((ourZone[x][y] != EtatEnum.BLOCDEST) && (ourZone[x][y] != EtatEnum.BLOCINDEST) && (ourZone[x][y] != EtatEnum.BONUSBOMBE) && (ourZone[x][y] != EtatEnum.BONUSFEU))
						{
							ourZone[x][y] = EtatEnum.DANGER;
							a++;
						}
						else up = false;
						if((ourZone[x][y] == EtatEnum.BONUSBOMBE) || (ourZone[x][y] == EtatEnum.BONUSFEU))
							ourZone[x][y] = EtatEnum.DANGER;
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
						if((ourZone[x][y] != EtatEnum.BLOCDEST) && (ourZone[x][y] != EtatEnum.BLOCINDEST) && (ourZone[x][y] != EtatEnum.BONUSBOMBE) && (ourZone[x][y] != EtatEnum.BONUSFEU))
						{
							ourZone[x][y] = EtatEnum.DANGER;
							a++;
						}
						else down = false;
						if((ourZone[x][y] == EtatEnum.BONUSBOMBE) || (ourZone[x][y] == EtatEnum.BONUSFEU))
							ourZone[x][y] = EtatEnum.DANGER;
							
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
						if((ourZone[x][y] != EtatEnum.BLOCDEST) && (ourZone[x][y] != EtatEnum.BLOCINDEST) && (ourZone[x][y] != EtatEnum.BONUSBOMBE) && (ourZone[x][y] != EtatEnum.BONUSFEU))
						{
							ourZone[x][y] = EtatEnum.DANGER;
							a++;
						}
						else left = false;
						if((ourZone[x][y] == EtatEnum.BONUSBOMBE) || (ourZone[x][y] == EtatEnum.BONUSFEU))
							ourZone[x][y] = EtatEnum.DANGER;
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
						if((ourZone[x][y] != EtatEnum.BLOCDEST) && (ourZone[x][y] != EtatEnum.BLOCINDEST) && (ourZone[x][y] != EtatEnum.BONUSBOMBE) && (ourZone[x][y] != EtatEnum.BONUSFEU))
						{
							ourZone[x][y] = EtatEnum.DANGER;
							a++;
						}
						else right = false;
						if((ourZone[x][y] == EtatEnum.BONUSBOMBE) || (ourZone[x][y] == EtatEnum.BONUSFEU))
							ourZone[x][y] = EtatEnum.DANGER;
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
		
	/*******************************
	 * La methode renvoie dangerZone
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public EtatEnum[][] getOurZone() throws StopRequestException {
		source.checkInterruption();
		return ourZone;
	}

	/***************************************************
	 * La methode renvoie la valeur enumarate d'une tile
	 * @param x
	 * @param y
	 * @return etatEnum
	 * @throws StopRequestException
	 */
	public EtatEnum getValeur(int x, int y) throws StopRequestException {
		source.checkInterruption(); //Appel Obligatoire
		return ourZone[x][y];
	}

	/***
	 * La methode sert a trouver les tiles claires
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
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
						Astar a=new Astar (this,hero.getCol(),hero.getLine(),col,line,source);
							if ((hero.getCol()!= col || hero.getLine() != line) && a.findPath())
								result.add(tile);
					}
			}
		}
		
		return result;
	}
	
	/*************************************
	 * La methode sert a trouver les bonus
	 * @return list
	 * @throws StopRequestException
	 */
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
					Astar a=new Astar (this,hero.getCol(),hero.getLine(),col,line,source);
					if ((hero.getCol()!= col || hero.getLine() != line) && a.findSecurePath())
						result.add(tile);
				}
			}
		}
		return result;
	}
	
	/************************************************
	 * La methode sert a trouver les tiles dangereux
	 * @return list
	 * @throws StopRequestException
	 */
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
						
						Astar a=new Astar (this,hero.getCol(),hero.getLine(),col,line,source);
							if ((hero.getCol()!= col || hero.getLine() != line) && a.findPath())
								result.add(tile);
					}
			}
		}
		
		return result;
	}
	
	/******************************************************
	 * La methode sert a preciser les tiles des adversaires
	 * @return list
	 * @throws StopRequestException
	 */
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
						Astar a=new Astar (this,hero.getCol(),hero.getLine(),col,line,source);
						if ((hero.getCol()!= col || hero.getLine() != line) && a.findSecurePath())
							result.add(tile);
					}
			}
		}
		
		return result;
	}

	/********************************************************************************
	 * La methode sert a trouver les tiles a deposer des bombe pour detruir des murs
	 * @param neighbors
	 * @return list
	 * @throws StopRequestException
	 */
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
					Astar a=new Astar (this,hero.getCol(),hero.getLine(),col,line,source);
					if ((hero.getCol()!= col || hero.getLine() != line) && a.findSecurePath())
						result.add(tile);
				}	
			}
		}
		return result;
	}
		
	/**********************************************
	 * La methode renvoie les tiles desctructibles
	 * @return list
	 * @throws StopRequestException
	 */
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
	//	result=temizle(result);
		return result;
	}

	/*********************************************
	 * pour preciser si on peut aller sur ce chemin
	 * @param x2
	 * @param y2
	 * @return boolean
	 * @throws StopRequestException
	 */
	public boolean isWalkable(int x2, int y2) throws StopRequestException {
		source.checkInterruption();
		if (ourZone[x2][y2]==EtatEnum.FEU || ourZone[x2][y2]==EtatEnum.BOMBE || ourZone[x2][y2]==EtatEnum.BLOCINDEST || ourZone[x2][y2]==EtatEnum.BLOCDEST)
			return false;
		else
			return true;
	}
	
	/**************************************************************
	 * pour preciser si on peut aller a bonus sur une chemin secure
	 * @param x2
	 * @param y2
	 * @return boolean
	 * @throws StopRequestException
	 */
	public boolean canGoToBonus(int x2,int y2) throws StopRequestException
	{
		source.checkInterruption();
		if (ourZone[x2][y2]==EtatEnum.DANGER || ourZone[x2][y2]==EtatEnum.FEU || ourZone[x2][y2]==EtatEnum.BOMBE || ourZone[x2][y2]==EtatEnum.BLOCINDEST || ourZone[x2][y2]==EtatEnum.BLOCDEST)
			return false;
		else
			return true;
	}
	
	/**********************************************************************************
	 * La methode qu'on va utiliser pour connaitre si on peut aller vers une adversaire
	 * @param x2
	 * @param y2
	 * @return boolean
	 * @throws StopRequestException
	 */
	public boolean canGoRival(int x2, int y2) throws StopRequestException
	{
		source.checkInterruption();
		if (ourZone[x2][y2]==EtatEnum.DANGER || ourZone[x2][y2]==EtatEnum.FEU || ourZone[x2][y2]==EtatEnum.BOMBE || ourZone[x2][y2]==EtatEnum.BLOCINDEST)
			return false;
		else
			return true;
	}
	
}
