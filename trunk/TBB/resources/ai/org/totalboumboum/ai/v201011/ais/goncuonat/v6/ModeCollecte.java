package org.totalboumboum.ai.v201011.ais.goncuonat.v6;


import java.util.ArrayList;
import java.util.Collection;
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
import org.totalboumboum.ai.v201011.adapter.path.AiPath;

/**
 * @author Elif Göncü
 * @author Yağız Onat
 */
@SuppressWarnings("deprecation")
public class ModeCollecte 
{

	/** notre héro */
	private GoncuOnat monia;
	
	/** la case vide qui ne contient aucuns sprites
	// est representée dans la matrice da la zone.
	 * 
	 */
	public final int CASE_EMPTY=0;
	/** la case qui contient le feu de la bombe pour le mode
	// collecte est representée dans la matrice da la zone.
	 * 
	 */
	public final int COLLECT_FIRE =-20 ;
	/** la case qui contient un mur destructible pour le mode
	// collecte est representée dans la matrice da la zone.
	 * 
	 */
	public final int COLLECT_SOFTWALL = 2;
	/** la case qui contient un bonus pour le mode
	// collecte est representée dans la matrice da la zone.
	 * 
	 */
	public final int COLLECT_BONUS= 10;
	/** la case qui contient un héro pour le mode
	// collecte est representée dans la matrice da la zone.
	 * 
	 */
	private final int COLLECT_RIVAL = -10;
	/** la case qui contient une bombe pour le mode
	// collecte est representée dans la matrice da la zone.
	 * 
	 */
	public final int COLLECT_BOMB = -20;
	/** chemin asuivre pour s'enfuir du danger
	// AiPath nextMove=null;
	// chemin a suivre pour ramasser des bonus
	 * 
	 */
	//public AiPath nextMoveBonus=null;
	public boolean searchBonus = true;
	
	
	/**
	 * 
	 * @param ia
	 * @throws StopRequestException
	 */
	public ModeCollecte(GoncuOnat ia)throws StopRequestException
	{
		ia.checkInterruption();
		this.monia=ia;
	}
	
	
	
	/**
	 * Methode remplissant les cases de notre matrice de zone par la valeur 
	 * des cases possedant des bombes pour le mode collecte. 
	 * 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 * @throws StopRequestException
	 */
	public void valueBonusCollecte(double[][] matrice, AiZone zone)throws StopRequestException 
	{
		monia.checkInterruption();
		Collection<AiItem> items = zone.getItems();
		Iterator<AiItem> iteratorItems = items.iterator();
		if(!items.isEmpty())
		{
			while (iteratorItems.hasNext()) 
			{
				monia.checkInterruption();
				AiItem item = iteratorItems.next();
				matrice[item.getLine()][item.getCol()]=COLLECT_BONUS;
			}
		}
	}
	
	/**
	 * Methode remplissant les cases de notre matrice de zone en mode collecte par la valeur 
	 * des cases possedant des feux  
	 * 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 * @throws StopRequestException
	 */
	
	public void valueFiresCollecte(double[][] matrice, AiZone zone) throws StopRequestException
	{
		monia.checkInterruption();
		Collection<AiFire> fires = zone.getFires();
		Iterator<AiFire> iteratorFires = fires.iterator();
		while (iteratorFires.hasNext()) 
		{
			monia.checkInterruption();
			AiFire fire = iteratorFires.next();
			matrice[fire.getLine()][fire.getCol()] = COLLECT_FIRE;
		}
	}
	
	/**
	 * Methode remplissant les cases de notre matrice de zone en mode collecte par la valeur 
	 * des cases possedant les héros  
	 * 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 * @throws StopRequestException
	 */
	
	public void valueRivalCollecte(double[][] matrice, AiZone zone)throws StopRequestException 
	{
		
		monia.checkInterruption();
		Collection<AiHero> heros = zone.getHeroes();
		Iterator<AiHero> iteratorHeroes = heros.iterator();
		if(!heros.isEmpty())
		{
			while (iteratorHeroes.hasNext()) 
			{
				monia.checkInterruption();
				AiHero hero = iteratorHeroes.next();
				if(hero.getLine()!=monia.ourHero.getLine()&&hero.getCol()!=monia.ourHero.getCol())
				
					matrice[hero.getLine()][hero.getCol()] =COLLECT_RIVAL;
			}
		}
	
		
	}
	
	

	/**
	 * Methode remplissant les cases de notre matrice de zone en mode collecte par la valeur 
	 * des cases possedant des murs  
	 * 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 * @throws StopRequestException
	 */
	public void valueBlocksCollecte(double[][] matrice, AiZone zone) throws StopRequestException
	{
		monia.checkInterruption();
		Collection<AiBlock> blocks = zone.getDestructibleBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		while (iteratorBlocks.hasNext()) 
		{
			monia.checkInterruption();
			AiBlock block = iteratorBlocks.next();
			Collection<AiTile> blockNeighbors= block.getTile().getNeighbors();
			Iterator<AiTile> iteratorBlock = blockNeighbors.iterator();
			while (iteratorBlock.hasNext()) 
			{
				monia.checkInterruption(); 
				AiTile tile=iteratorBlock.next();
				if(tile.getBombs().isEmpty()&&tile.getFires().isEmpty()&&tile.getItems().isEmpty()&&tile.isCrossableBy(monia.ourHero))
					matrice[tile.getLine()][tile.getCol()] = COLLECT_SOFTWALL;
			}
					
		}
	}
	/**
	 * Methode remplissant les cases de notre matrice de zone en mode collecte par la valeur 
	 * des cases possedant des bombes  
	 * 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 * @throws StopRequestException
	 */
	public void valueBombsCollecte(double[][] matrice, AiZone zone) throws StopRequestException
	{
		monia.checkInterruption();
		Collection<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		
		while (iteratorBombs.hasNext()) 
		{
			monia.checkInterruption();
			AiBomb bomb = iteratorBombs.next();
			matrice[bomb.getLine()][bomb.getCol()] = COLLECT_BOMB;
			
			Collection<AiTile> inScopeTiles = bomb.getBlast();
			
			Iterator<AiTile> iteratorScope = inScopeTiles.iterator();
			
			
			while (iteratorScope.hasNext())
			{
				monia.checkInterruption();
				AiTile blastCase=iteratorScope.next();
				matrice[blastCase.getLine()][blastCase.getCol()] = COLLECT_FIRE;
				
			}
		}
	}
	
	/**
	 * Methode permettant de calculer le cout de la case a se deplacer en mode collecte
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param nextMove
	 * 				le chemin a suivre
	 * @throws StopRequestException
	 * @return result
	 * 				le cout de la case
	 */
	public double bestPathCost (double[][] matrice,AiPath nextMove) throws StopRequestException
	{
		monia.checkInterruption();
		double result=(this.shortestPathCost(matrice, nextMove)/(this.getPathDistance(nextMove)*monia.ourHero.getWalkingSpeed()));
			
		return result;
	}
	

	/**
	 * Methode permettant de calculer le cout de la case a se deplacer en mode collecte
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param nextMove
	 * 				le chemin a suivre
	 * @throws StopRequestException
	 * @return result
	 * 				le cout de la case
	 */
	public double shortestPathCost(double[][]matrice, AiPath nextMove)throws StopRequestException
	{
		monia.checkInterruption();
		double result=0;
		if(nextMove.getLength()!=0)
		{
			
			for(int i=0;i<nextMove.getLength();i++)
			{
				monia.checkInterruption();
				AiTile tempPath=nextMove.getTile(i);
				result+= matrice[tempPath.getLine()][tempPath.getCol()];
			
			}
		}
		return result+10;	
	}

	/**
	 * Methode permettant de calculer la longueur du chemin.
	 * 
	 * @param nextMove
	 * 				le chemin a suivre
	 * @throws StopRequestException
	 * 
	 * @return result
	 * 				la distance du chemin en cases.
	 */
	public int getPathDistance(AiPath nextMove)throws StopRequestException
	{
		monia.checkInterruption();
		if(nextMove.getLength()!=0)
			return nextMove.getLength();
		else
			return 1;
	}

	/**
	 * Methode implementant l'algo. de bonus.
	 * 
	 * @param zone
	 *            La zone du jeu
	 * @param matrice
	 *            La matrice de zone
	 * @throws StopRequestException
	 */
	public void matriceCollecte(double[][] matrice,AiZone zone)throws StopRequestException 
	{
		monia.checkInterruption();
		ShortestPath spath= new ShortestPath(monia);
		AiPath tempPath = new  AiPath();
		List<AiBlock> blockss=zone.getDestructibleBlocks();
		List<AiTile> tiless=new ArrayList<AiTile>();
		for(int i=0; i<blockss.size();i++)
		{
			monia.checkInterruption();
			tiless.add(blockss.get(i).getTile());
		}
		if (monia.nextMoveBonus == null) 
		{
			List<AiItem> itemList = zone.getItems();
			if (spath.shortestPath(monia.ourHero, monia.ourHero.getTile(), tiless)==null) 
			{
				if(itemList.size()!=0)
				{
					List<AiTile> itemTiles = new ArrayList<AiTile>();
					for (int i = 0; i < itemList.size(); i++) 
					{
						monia.checkInterruption();
						if (matrice[itemList.get(i).getLine()][itemList.get(i).getCol()] != COLLECT_FIRE)
							itemTiles.add(itemList.get(i).getTile());
					}
					
					List<Double>  totalOfTiles = new ArrayList<Double>();
					
					double temp;
					
					double max1=0,max2=0;
					int index=0;
					for(int i=0;i<itemTiles.size();i++)
					{
						monia.checkInterruption(); 
						tempPath= spath.shortestPathEachTile(monia.ourHero, monia.ourHero.getTile(), itemTiles.get(i));
						if( (shortestPathCost(matrice, tempPath)!= 0 ) &&  getPathDistance(tempPath)!= 0 )
							temp= shortestPathCost(matrice, tempPath) /(getPathDistance(tempPath)*monia.ourHero.getWalkingSpeed());
						else
							temp=0;
						
						totalOfTiles.add(temp);
					}
					if(totalOfTiles!=null)
						if(totalOfTiles.size()!=0)
							{
							max1=totalOfTiles.get(0);
							max2=max1;
							}
					
					for (int j=0;j<totalOfTiles.size();j++)
					{
						monia.checkInterruption(); 
							max2=totalOfTiles.get(j);
							if(max2>max1)
							{
								max1=max2;
								index=j;
							}
					}
				
					if(itemTiles!=null && itemTiles.size()!=0)
						monia.nextMoveBonus = spath.shortestPathEachTile(monia.ourHero,monia.ourHero.getTile(),itemTiles.get(index));
				
			
			}
			}
			else
			
	
			{
					
				Collection<AiBlock> blocks = zone.getBlocks();
				Iterator<AiBlock> iteratorBlocks = blocks.iterator();
				List<AiBlock> destructibleBlocks = new ArrayList<AiBlock>();
				List<AiTile> endPoint=new ArrayList<AiTile>();
				while (iteratorBlocks.hasNext()) 
				{
					monia.checkInterruption();
					AiBlock block = iteratorBlocks.next();
					if(block.isDestructible())
					{
						destructibleBlocks.add(block);
						List<AiTile> blockneighbor = block.getTile().getNeighbors();
						
						for(int i=0;i<blockneighbor.size();i++)
						{	
							monia.checkInterruption(); 
							if(matrice[blockneighbor.get(i).getLine()][blockneighbor.get(i).getCol()]==COLLECT_SOFTWALL)
								{
								endPoint.add(blockneighbor.get(i));
								if(monia.verbose)
									System.out.println("bomba konacak yer:"+blockneighbor.get(i).getLine()+","+blockneighbor.get(i).getCol());
								}
						}
						
					}
					
					
				}
				
				if(endPoint.size()!=0)
				{
					{
						if(monia.verbose)
							System.out.println("nextMoveBonusAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
						if(spath.shortestPath(monia.ourHero,monia.ourHero.getTile(),endPoint)!=null)
							
						{
							if(monia.verbose)
								System.out.println("nextMoveBonusBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
							if(spath.shortestPath(monia.ourHero,monia.ourHero.getTile(),endPoint).getLength()!=0)
							{	if(monia.verbose)
									System.out.println("nextMoveBonusBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
								monia.nextMoveBonus = spath. shortestPath(monia.ourHero,monia.ourHero.getTile(),endPoint);
								if(monia.verbose)
									System.out.println("nextMoveBonus="+ monia.nextMoveBonus.getLength());}
						
					}}
					
					
				} 
		
				
				
				else 
				{
					searchBonus = false;
				}
		
			monia.nextMove = null;
			}
			
		
		}
	
		else 
		{
			if (monia.nextMoveBonus.getLength() == 0)
				monia.nextMoveBonus = null;
			else 
			{
				boolean adapt = false;
				List<AiTile> nextTiles = monia.nextMoveBonus.getTiles();
				for (int i = 0; i < nextTiles.size(); i++) 
				{
					monia.checkInterruption(); 
					if (!nextTiles.get(i).isCrossableBy(monia.ourHero)||matrice[nextTiles.get(i).getLine()][nextTiles.get(i).getCol()] == COLLECT_FIRE)
							adapt = true;
				}
			
				if (adapt)
					monia.nextMoveBonus = null;
				
				else 
				{
					if ((this.monia.ourHero.getLine() == monia.nextMoveBonus.getTile(0).getLine())&& (this.monia.ourHero.getCol() == monia.nextMoveBonus.getTile(0).getCol())) 
					{
						monia.nextMoveBonus.getTiles().remove(0);
						if (monia.nextMoveBonus.getTiles().isEmpty()) 
						{
							monia.nextMoveBonus = null;
		
						}
					}
				}
			}
		}
	}

	
	
	
	


	
}