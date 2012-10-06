package org.totalboumboum.ai.v201011.ais.goncuonat.v6;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;

/**
 * @author Elif Göncü
 * @author Yağız Onat
 */
@SuppressWarnings("deprecation")
public class Runaway 
{
	private GoncuOnat monia;
	/** */
	public final int CASE_EMPTY=0;
	/** */
	public final int COLLECT_SOFTWALL = 2;
	/** */
	public final int COLLECT_BONUS= 10;
	/** */
	public final int COLLECT_FIRE =-20 ;
	
	/**
	 * 
	 * @param ia
	 * @throws StopRequestException
	 */
	public Runaway(GoncuOnat ia) throws StopRequestException 
	{
		ia.checkInterruption();
		this.monia = ia;
		//monia.checkInterruption();
	
	}
	
	
	/**
	 * 
	 * La methode pour calculer un chemin de fuir en mode attaque
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 *
	 * @throws StopRequestException
	 * 
	 * 
	 */
	
	public void runAwayAlgoAttack(double[][] matrice,AiZone zone)throws StopRequestException 
	{
		
		monia.checkInterruption();//APPEL OBLIGATOIRE
		ShortestPath spath= new ShortestPath(monia);
		List<AiBlock> toRemoveTiles=new ArrayList<AiBlock>();
		List<AiTile> tileList=new ArrayList<AiTile>();
		toRemoveTiles=zone.getBlocks();
		
		if (monia.nextMove == null) 
		{
			ModeAttack mattack= new ModeAttack(monia);
			
			tileList=mattack.endPoint(matrice, zone);
			for(int i=0;i<toRemoveTiles.size();i++)
			{
				monia.checkInterruption();
				
				for(int j=0;j<tileList.size();j++)
				{
					monia.checkInterruption();
					if(toRemoveTiles.get(i).equals(tileList.get(j)))
						tileList.remove(j);
				}
						
			}
			
			
			if(tileList.size()!=0)
				{
					List<Double> endPointValues=mattack.endpointValue(tileList,matrice);
					List<AiPath> shortestPathAtts=spath.shortestPathAttack(monia.ourHero, monia.ourHero.getTile(),tileList);
					monia.nextMove=mattack.objectifPath(endPointValues, shortestPathAtts,matrice);
					
				}
		}
		
		else 
		{
			if (monia.nextMove.getLength() == 0)
				monia.nextMove = null;
			else 
			{
				boolean danger = false;
				if (matrice[monia.nextMove.getLastTile().getLine()][monia.nextMove.getLastTile().getCol()]<0)
					monia.nextMove = null;
				else 
				{
					List<AiTile> nextTiles = monia.nextMove.getTiles();
					for (int i = 0; i < nextTiles.size(); i++) 
					{
						monia.checkInterruption();
						if (!nextTiles.get(i).isCrossableBy(monia.ourHero))
							danger = true;
					}
					if (danger)
						monia.nextMove = null;
					else 
					{
						if ((monia.ourHero.getLine() == monia.nextMove.getTile(0).getLine())&& (monia.ourHero.getCol() == monia.nextMove.getTile(0).getCol()))
						{
							monia.nextMove.getTiles().remove(0);
							if (monia.nextMove.getTiles().isEmpty())
							{
								monia.nextMove = null;
							}
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * La methode pour calculer un chemin de fuir en mode attaque
	 * 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 *
	 * @param list
	 *			la liste des cases on puisse vouloir aller
	 * @throws StopRequestException
	 * 
	 *
	 * 
	 */
	
	
	
	public void runAwayAlgoAttackCheck(double[][] matrice,AiZone zone, List<AiTile> list)throws StopRequestException 
	{
		monia.checkInterruption();//APPEL OBLIGATOIRE
		ShortestPath spath= new ShortestPath(monia);
		ModeAttack mattack = new ModeAttack(monia);
		if (monia.nextMoveCheck == null) 
		{
			if(list.size()!=0)
				{
					List<Double> endPointValues=mattack.endpointValue(list,matrice);
					List<AiPath> shortestPathAtts=spath.shortestPathAttack(monia.ourHero, monia.ourHero.getTile(),list);
					monia.nextMoveCheck=mattack.objectifPathCheck(endPointValues,list, shortestPathAtts,matrice);
				}
		}
		
		else 
		{
			if (monia.nextMoveCheck.getLength() == 0)
				monia.nextMoveCheck = null;
			else 
			{
				boolean danger = false;
				if (matrice[monia.nextMoveCheck.getLastTile().getLine()][monia.nextMoveCheck.getLastTile().getCol()]<0)
					monia.nextMoveCheck = null;
				else 
				{
					List<AiTile> nextTiles = monia.nextMoveCheck.getTiles();
					for (int i = 0; i < nextTiles.size(); i++) 
					{
						monia.checkInterruption();
						if (!nextTiles.get(i).isCrossableBy(monia.ourHero))
							danger = true;
					}
					if (danger)
						monia.nextMoveCheck = null;
					else 
					{
						if ((monia.ourHero.getLine() == monia.nextMoveCheck.getTile(0).getLine())&& (monia.ourHero.getCol() == monia.nextMoveCheck.getTile(0).getCol()))
						{
							monia.nextMoveCheck.getTiles().remove(0);
							if (monia.nextMoveCheck.getTiles().isEmpty())
							{
								monia.nextMoveCheck = null;
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Methode implementant l'algorithme de defense
	 * 
	 * @param zone
	 *            La zone du jeu
	 * @param matrice
	 *            La matrice de zone
	 * @throws StopRequestException
	 */
	public void runAwayAlgoCollecte(double[][] matrice,AiZone zone)throws StopRequestException 
	{
		monia.checkInterruption();//APPEL OBLIGATOIRE
		ShortestPath spath = new ShortestPath(monia); 
	
		if (monia.nextMove == null) 
		{
			List<AiTile> tileList=new ArrayList<AiTile>();
			
			for(int line=0;line<zone.getHeight();line++)
			{
				monia.checkInterruption();//APPEL OBLIGATOIRE
				for(int col=0;col<zone.getWidth();col++)
				{
					monia.checkInterruption();//APPEL OBLIGATOIRE
					if(matrice[line][col]==CASE_EMPTY ||matrice[line][col]==COLLECT_SOFTWALL||matrice[line][col]==COLLECT_BONUS) 
						
					{
						if(monia.ourHero.getLine()!=line && monia.ourHero.getCol()!=col)
							tileList.add(zone.getTile(line, col));
					}
					
				}
			}
			if(tileList.size()!=0 && tileList!=null)
				{
					monia.nextMove=spath.shortestPath(monia.ourHero,monia.ourHero.getTile(),tileList);
				}
		}
		
		else 
		{
			if (monia.nextMove.getLength() == 0)
				monia.nextMove = null;
			else 
			{
				boolean adapt = false;
				if (matrice[monia.nextMove.getLastTile().getLine()][monia.nextMove.getLastTile().getCol()] == COLLECT_FIRE)
						monia.nextMove = null;
				else 
				{
					List<AiTile> nextTiles = monia.nextMove.getTiles();
					for (int i = 0; i < nextTiles.size(); i++) 
					{
						monia.checkInterruption(); 
						if (!nextTiles.get(i).isCrossableBy(monia.ourHero))
							adapt = true;
					}
					if (adapt)
						monia.nextMove = null;
					else 
					{
						if ((monia.ourHero.getLine() == monia.nextMove.getTile(0).getLine())&& (monia.ourHero.getCol() ==monia.nextMove.getTile(0).getCol()))
						{
							monia.nextMove.getTiles().remove(0);
							if (monia.nextMove.getTiles().isEmpty())
							{
								monia.nextMove = null;
							}
						}
					}
				}
			}
		}

	}

	
	

}
