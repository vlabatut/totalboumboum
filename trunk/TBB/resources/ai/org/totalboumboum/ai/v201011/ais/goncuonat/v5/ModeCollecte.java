package org.totalboumboum.ai.v201011.ais.goncuonat.v5;

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
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;

/**
 * @author Elif Göncü
 * @author Yağız Onat
 */
@SuppressWarnings("deprecation")
public class ModeCollecte 
{

	//notre héro
	
	private GoncuOnat monia;
	
	// la case vide qui ne contient aucuns sprites
	// est representée dans la matrice da la zone.
	public final int CASE_EMPTY=0;
	// la case qui contient le feu de la bombe pour le mode
	// collecte est representée dans la matrice da la zone.
	public final int COLLECT_FIRE =-20 ;
	// la case qui contient un mur destructible pour le mode
	// collecte est representée dans la matrice da la zone.
	public final int COLLECT_SOFTWALL = 2;
	// la case qui contient un bonus pour le mode
	// collecte est representée dans la matrice da la zone.
	public final int COLLECT_BONUS= 10;
	// la case qui contient un héro pour le mode
	// collecte est representée dans la matrice da la zone.
	private final int COLLECT_RIVAL = -10;
	// la case qui contient une bombe pour le mode
	// collecte est representée dans la matrice da la zone.
	public final int COLLECT_BOMB = -20;
	// chemin asuivre pour s'enfuir du danger
	// AiPath nextMove=null;
	// chemin a suivre pour ramasser des bonus
	//public AiPath nextMoveBonus=null;
	public boolean searchBonus = true;
	
	

	public ModeCollecte(GoncuOnat ia)throws StopRequestException
	{
		ia.checkInterruption();
		this.monia=ia;
		//zone=ia.getZone();
		//AiHero monia.ourHero=ia.getOwnHero();
	}
	
	public boolean isInList (List<AiTile> list , AiTile tile) throws StopRequestException
	{
		monia.checkInterruption();
		boolean result=false;
		for(int i=0;i<list.size();i++)
		{
			monia.checkInterruption();
			if(list.get(i)==tile)
				result=true;
		}
		return result;
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
	 * @returns result
	 * 				le cout de la case
	 */
	public double bestPathCost (double[][] matrice,AiPath nextMove) throws StopRequestException
	{
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
	 * @returns result
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
	 * Methode permettant de calculer la somme des valeurs des cases du plus court chemin 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param nextMove
	 * 				le chemin a suivre
	 * @throws StopRequestException
	 * @returns result
	 * 				la somme des cases du chemin
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
	 * 
	 * Methode calculant le chemin le plus court que l'hero peut suivre.
	 * 
	 * @param ownHero
	 *            l'hero sollicite par notre AI
	 * @param startPoint
	 *            la position de notre hero
	 * @param endPoints
	 *            les cases cibles ou le hero peut aller
	 * @return le chemin le plus court a parcourir
	 * @throws StopRequestException
	 */
	
	public AiPath shortestPath(AiHero ownHero, AiTile startPoint,List<AiTile> endPoints) throws StopRequestException
	{
		monia.checkInterruption();
		// le chemin le plus court possible
		AiPath shortestPath=null;
		// L'objet pour implementer l'algo A*
		Astar astar;
		// Calcul du cout par la classe de l'API
		CostCalculator cost = new BasicCostCalculator();
		// Calcul de l'heuristic par la classe de l'API
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		astar = new Astar(monia, ownHero, cost, heuristic);
		try
		{
			shortestPath = astar.processShortestPath(startPoint,endPoints);
		} 
		catch (LimitReachedException e) 
		{
			e.printStackTrace();
		}
		return shortestPath;
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
		AiPath tempPath = new  AiPath();
		if (monia.nextMoveBonus == null) 
		{
			List<AiItem> itemList = zone.getItems();
			if (itemList.size() != 0) 
			{
				List<AiTile> itemTiles = new ArrayList<AiTile>();
				for (int i = 0; i < itemList.size(); i++) 
				{
					monia.checkInterruption();
					if (matrice[itemList.get(i).getLine()][itemList.get(i).getCol()] != COLLECT_FIRE)
						itemTiles.add(itemList.get(i).getTile());
				}
				
				//if(itemTiles.size()!=0)
				//	if(!itemTiles.isEmpty())
				//			monia.nextMoveBonus = shortestPath(monia.ourHero,monia.ourHero.getTile(),itemTiles);
			
				List<Double>  totalOfTiles = new ArrayList<Double>();
				
				double temp;
				
				double max1=0,max2=0;
				int index=0;

				for(int i=0;i<itemTiles.size();i++)
				{
					monia.checkInterruption(); 
					tempPath= shortestPathEachTile(monia.ourHero, monia.ourHero.getTile(), itemTiles.get(i));
					if( (shortestPathCost(matrice, tempPath)!= 0 ) &&  getPathDistance(tempPath)!= 0 )
						temp= shortestPathCost(matrice, tempPath) /(getPathDistance(tempPath)*monia.ourHero.getWalkingSpeed());
					else
						temp=0;
					
					totalOfTiles.add(temp);
				}
				if(totalOfTiles!=null)
					if(totalOfTiles.size()!=0)
						{max1=totalOfTiles.get(0);
						max2=max1;}
				
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
				
				//if(itemTiles!=null)
				monia.nextMoveBonus = shortestPathEachTile(monia.ourHero,monia.ourHero.getTile(),itemTiles.get(index));
				
				
				
				//nextMoveBonus = shortestPath(monia.ourHero,monia.ourHero.getTile(),itemTiles);
			
			} 
				
			
			if(tempPath.getLength()==0)
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
						if(shortestPath(monia.ourHero,monia.ourHero.getTile(),endPoint)!=null)
							
						{
							if(monia.verbose)
								System.out.println("nextMoveBonusBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
							if(shortestPath(monia.ourHero,monia.ourHero.getTile(),endPoint).getLength()!=0)
							{	if(monia.verbose)
									System.out.println("nextMoveBonusBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
								monia.nextMoveBonus = shortestPath(monia.ourHero,monia.ourHero.getTile(),endPoint);
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
	/**
	 * 
	 * Methode calculant le chemin le plus court que l'hero peut suivre.
	 * 
	 * @param ownHero
	 *            l'hero sollicite par notre AI
	 * @param startPoint
	 *            la position de notre hero
	 * @param endPoints
	 *            la case cible ou le hero peut aller
	 * @return le chemin le plus court a parcourir
	 * @throws StopRequestException
	 */
	public AiPath shortestPathEachTile(AiHero ownHero, AiTile startPoint,AiTile endPoint) throws StopRequestException
	{
		monia.checkInterruption();
		// le chemin le plus court possible
		AiPath shortestPath=null;
		// L'objet pour implementer l'algo A*
		Astar astar;
		// Calcul du cout par la classe de l'API
		CostCalculator cost = new BasicCostCalculator();
		// Calcul de l'heuristic par la classe de l'API
		AiPath result = new AiPath();
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		astar = new Astar(monia, ownHero, cost, heuristic);
		try
		{		
				shortestPath = astar.processShortestPath(startPoint,endPoint);
				result=shortestPath;
		} 
		catch (LimitReachedException e) 
		{
			e.printStackTrace();
		}
		return result;
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
	public void runAwayAlgo(double[][] matrice,AiZone zone)throws StopRequestException 
	{
		monia.checkInterruption();//APPEL OBLIGATOIRE
		
	
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
			if(tileList.size()!=0)
				{
				if(shortestPath(monia.ourHero,monia.ourHero.getTile(),tileList)!=null)
					
					monia.nextMove=shortestPath(monia.ourHero,monia.ourHero.getTile(),tileList);
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
	/**
	 * Methode implementant l'algorithme de posage de bombe
	 * 
	 * @param zone
	 *            La zone du jeu
	 * @param matrice
	 *            La matrice de zone
	 * @throws StopRequestException
	 */
	
	public boolean dropBombCheck(AiZone zone /*double[][]matrice*/)throws StopRequestException
	{
		monia.checkInterruption();
		boolean result=false;
		AiPath path =null;
		List<AiTile> check= new ArrayList<AiTile>();
		
		Collection<AiBomb> bombs = zone.getBombs();
		List<AiTile> blast = new ArrayList<AiTile>();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		List<AiTile> blastTemp= new ArrayList<AiTile>();
		if(bombs!=null && bombs.size()!=0 )
		{
			
			while(iteratorBombs.hasNext())
			{
				
				monia.checkInterruption();
				blast =iteratorBombs.next().getBlast();
			
				for(int k=0;k<blast.size();k++)
					{
						monia.checkInterruption();
						blastTemp.add(blast.get(k));
						
					}
				
			}
			
			for(int i=0; i<zone.getHeight(); i++)
			{
				monia.checkInterruption(); // APPEL OBLIGATOIRE
				for(int j=0; j<zone.getWidth(); j++)
				{
					monia.checkInterruption();
					
					if(zone.getTile(i,j).getFires().isEmpty()&& zone.getTile(i, j).getBombs().isEmpty() 
							&& zone.getTile(i,j).isCrossableBy(monia.ourHero)&& !isInList(blastTemp,zone.getTile(i, j)))	
					{	
						check.add(zone.getTile(i,j));
					}
					
				}
			}
		
		
		}
		else
		{
			
			for(int i=0; i<zone.getHeight(); i++)
		
			{
				monia.checkInterruption(); // APPEL OBLIGATOIRE
				for(int j=0; j<zone.getWidth(); j++)
				{
					monia.checkInterruption(); // APPEL OBLIGATOIRE
					
						if(zone.getTile(i, j).getFires().isEmpty()&&zone.getTile(i, j).getBombs().isEmpty() 
								&& zone.getTile(i,j).isCrossableBy(monia.ourHero))
						{
							check.add(zone.getTile(i,j));
							
						}
				}
				
			}
		}
		List <AiTile> bombBlast =new ArrayList<AiTile>();
		int bombRange=monia.ourHero.getBombRange();
		int x=monia.ourHero.getTile().getLine();
		int y=monia.ourHero.getTile().getCol();
		int a=x-bombRange;
		int b=y-bombRange;
		int c=x+bombRange;
		int d=y+bombRange;
		if(a<=0)
			a=0;
		if(c>zone.getHeight());
			c=zone.getHeight();
		for(int i=a;i<c;i++)
		{
				monia.checkInterruption(); // APPEL OBLIGATOIRE
				if(zone.getTile(i, y).isCrossableBy(monia.ourHero))
					bombBlast.add(zone.getTile(i,y));
		}
		
		if(b<=0)
			b=0;
		if(y+bombRange>zone.getWidth())
			d=zone.getWidth();
		for(int j=b;j<d;j++)
			{
				monia.checkInterruption(); // APPEL OBLIGATOIRE
				if(zone.getTile(x, j).isCrossableBy(monia.ourHero))
					bombBlast.add(zone.getTile(x,j));
			}
		for(int k=0;k<bombBlast.size();k++)
		{
			monia.checkInterruption(); // APPEL OBLIGATOIRE
			for(int l=0;l<check.size();l++)
			{
				monia.checkInterruption(); // APPEL OBLIGATOIRE
				if(bombBlast.get(k).getLine()==check.get(l).getLine()&&bombBlast.get(k).getCol()==check.get(l).getCol())
					check.remove(l);
			}
		}
		if(check!=null)
			if(check.size()!=0)
				 path=this.shortestPath(monia.ourHero, monia.ourHero.getTile(), check);
		if(path!=null)
			if(path.getLength()!=0)
		{
			if((path.getDuration(monia.ourHero)/1000)<monia.ourHero.getExplosionDuration() && this.checkSecure(path,monia.zone))
			{
				
				result=true;
			}
			
		}
		
		return result; 
	}
	
	public boolean checkSecure(AiPath path, AiZone zone)throws StopRequestException
	{
		monia.checkInterruption();
		boolean result=true;
		Collection<AiBomb> bombs = zone.getBombs();
		List<AiTile> blast = new ArrayList<AiTile>();
		List<AiTile> temp= new ArrayList<AiTile>();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		List<AiTile> blastTemp= new ArrayList<AiTile>();
		if(bombs.size()!=0 && bombs!=null)
		{while(iteratorBombs.hasNext())
		{
			monia.checkInterruption();
			blast =iteratorBombs.next().getBlast();
			for(int k=0;k<blast.size();k++)
			{
				monia.checkInterruption();
				blastTemp.add(blast.get(k));
			}
				
		}}
		for(int i=0;i<path.getLength();i++)
		{	
			monia.checkInterruption();
			if(blastTemp!=null && blastTemp.size()!=0)
			{	
				for(int j=0;j<blastTemp.size();j++)
				{
					monia.checkInterruption();
					if(path.getTile(i).getLine()==blastTemp.get(j).getLine() && path.getTile(i).getCol()==blastTemp.get(j).getCol());
						temp.add(path.getTile(i));
				}
			}
		}
		if(temp.size()!=0 && temp!=null)
			result=false;
		else
			result=true;
		
		return result;
	}
}