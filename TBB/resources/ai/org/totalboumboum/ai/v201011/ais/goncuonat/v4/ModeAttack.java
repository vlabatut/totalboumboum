package org.totalboumboum.ai.v201011.ais.goncuonat.v4;

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

public class ModeAttack 
{
	
	//public AiHero monia.ourHero;
	public GoncuOnat monia;
	
	// la case vide qui ne contient aucuns sprites
	// est represent�e dans la matrice da la zone.
	public final int CASE_EMPTY=0;
	// la case qui contient un mur indestructible pour le mode
	// attaque est represent�e dans la matrice da la zone.
	public final int ATTACK_HARDWALL =1 ;
	// la case qui contient le feu d'une bombe pour le mode
	// attaque est represent�e dans la matrice da la zone.
	public final int ATTACK_FIRE =-20 ;
	// la case qui contient un bonus pour le mode
	// attaque est represent�e dans la matrice da la zone.
	public final int ATTACK_BONUS= 3;
	// la case qui contient un héro pour le mode
	// attaque est represent�e dans la matrice da la zone.
	public final int ATTACK_RIVAL = 4;
	// la case qui contient une bombe pour le mode
	// attaque est represent�e dans la matrice da la zone.
	public final int ATTACK_BOMB = -20;
	
	//public AiPath nextMove=null;
	// chemin a suivre pour attaquer
	//public AiPath nextMoveAttack=null;
	public AiZone zone;
	
	public ModeAttack( GoncuOnat monia)
	{
		
		this.monia=monia;
	}
	/**
	 * Methode remplissant les cases de notre matrice de zone par la valeur 
	 * des case possedant du feu. 
	 * 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 * @throws StopRequestException
	 */
	public void valueFiresAttack(double[][] matrice, AiZone zone) throws StopRequestException
	{
		monia.checkInterruption(); // APPEL OBL�GATO�RE
		Collection<AiFire> fires = zone.getFires();
		Iterator<AiFire> iteratorFires = fires.iterator();
		
		
		while (iteratorFires.hasNext()) 
		{
			monia.checkInterruption(); // APPEL OBL�GATO�RE
			AiFire fire=iteratorFires.next();
			
			Collection<AiTile> fireNeighbors=fire.getTile().getNeighbors();
			Iterator<AiTile> iteratorFire = fireNeighbors.iterator();
			while(iteratorFire.hasNext())
			{
				AiTile tile=iteratorFire.next();
				monia.checkInterruption(); // APPEL OBL�GATO�RE
				if(tile.isCrossableBy(monia.ourHero))
					matrice[tile.getLine()][tile.getCol()] += ATTACK_FIRE;
			}
		}
		
		
	}
	
	/**
	 * Methode remplissant les cases de notre matrice de zone par la valeur 
	 * des cases possedant des bonus pour le mode attaque. 
	 * 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 * @throws StopRequestException
	 */
	public void valueBonusAttack(double[][] matrice, AiZone zone)throws StopRequestException 
	{
		monia.checkInterruption(); // APPEL OBL�GATO�RE
		Collection<AiItem> items = zone.getItems();
		Iterator<AiItem> iteratorItems = items.iterator();
		while (iteratorItems.hasNext()) 
		{
			monia.checkInterruption(); // APPEL OBL�GATO�RE
			AiItem item = iteratorItems.next();
			Collection<AiTile> bonusNeighbors=item.getTile().getNeighbors();
			Iterator<AiTile> iteratorFire = bonusNeighbors.iterator();
			while(iteratorFire.hasNext())
			{
				AiTile tile=iteratorFire.next();
				if(tile.isCrossableBy(monia.ourHero))
					matrice[tile.getLine()][tile.getCol()]+=ATTACK_BONUS;
			}
			
		}
	}
	
	/**
	 * Methode remplissant les cases de notre matrice de zone par la valeur 
	 * des cases possedant des bombes pour le mode attaque. 
	 * 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 * @throws StopRequestException
	 */
	public void valueBombsAttack(double[][] matrice, AiZone zone) throws StopRequestException{
		monia.checkInterruption(); // APPEL OBL�GATO�RE
		Collection<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		//Collection<AiTile> inScopeTiles=
		while (iteratorBombs.hasNext()) 
		{
			monia.checkInterruption(); // APPEL OBL�GATO�RE
			AiBomb bomb = iteratorBombs.next();
			
			Collection<AiTile> bombNeighbors=bomb.getTile().getNeighbors();
			Iterator<AiTile> iteratorBomb = bombNeighbors.iterator();
			while(iteratorBomb.hasNext())
			{
				AiTile tile=iteratorBomb.next();
				if(tile.isCrossableBy(monia.ourHero))
					matrice[bomb.getLine()][bomb.getCol()] += ATTACK_BOMB;
			}
			Collection<AiTile> inScopeTiles = bomb.getBlast();
			
			Iterator<AiTile> iteratorScope = inScopeTiles.iterator();
		
			
			while (iteratorScope.hasNext())
			{
				monia.checkInterruption(); // APPEL OBL�GATO�RE
				AiTile blastCase=iteratorScope.next();
				matrice[blastCase.getLine()][blastCase.getCol()] += ATTACK_FIRE;
				
			}
		}
	}
	
	/**
	 * Methode remplissant les cases de notre matrice de zone par la valeur 
	 * des cases possedant des heros pour le mode attaque. 
	 * 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 * @throws StopRequestException
	 */
	public void valueRivalAttack(double[][] matrice, AiZone zone)throws StopRequestException 
	{
		monia.checkInterruption(); // APPEL OBL�GATO�RE
		Collection<AiHero> items = zone.getHeroes();
		Iterator<AiHero> iteratorHeroes = items.iterator();
		while (iteratorHeroes.hasNext()) 
		{
			AiHero hero =iteratorHeroes.next();
			int x =hero.getLine();
			int y =hero.getCol();
			int blast=monia.ourHero.getBombRange();
			int i=x-blast;
			int j=y-blast;
			if(i<0)
				i=0;
			if(j<0)
				j=0;
			int rangex=x+blast+1;
			int rangey=y+blast+1;
			if(rangex>zone.getHeight())
				rangex=zone.getHeight();
			if(rangey>zone.getHeight())
				rangey=zone.getHeight();
			
			for(int a=i;a<rangex;a++)
			{
				monia.checkInterruption(); // APPEL OBL�GATO�RE
				for(int k=j;k<rangey;k++)
				{
					monia.checkInterruption(); // APPEL OBL�GATO�RE
					if(zone.getTile(a, k)!=zone.getTile(monia.ourHero.getLine(),monia.ourHero.getCol()))
					{
						if((Math.abs(a-x)==(monia.ourHero.getBombRange()-2) || Math.abs(k-y)==(monia.ourHero.getBombRange()-2)))
						{ 
							
							matrice[a][k]+=monia.ourHero.getBombRange()+ATTACK_RIVAL-2;
						}
						
						else if((Math.abs(a-x)==(monia.ourHero.getBombRange()-1) ||Math.abs(k-y)==(monia.ourHero.getBombRange()-1)))
						{ 
							matrice[a][k]+=monia.ourHero.getBombRange()+ATTACK_RIVAL-1;
						}
						
						else if((Math.abs(a-x)==monia.ourHero.getBombRange() || Math.abs(k-y)==monia.ourHero.getBombRange()))
						{
							matrice[a][k]+=monia.ourHero.getBombRange()+ATTACK_RIVAL;
						}
					}
				}
			}	
		}
	}
	
	/**
	 * Methode remplissant les cases de notre matrice de zone par la valeur 
	 * des cases possedant des murs destructibles et indestructibles pour 
	 * le mode attaque. 
	 * 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 * @throws StopRequestException
	 */
	public void valueBlocksAttack(double[][] matrice, AiZone zone) throws StopRequestException{
		monia.checkInterruption(); // APPEL OBL�GATO�RE
		Collection<AiBlock> blocks = zone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		while (iteratorBlocks.hasNext()) 
		{
			monia.checkInterruption(); // APPEL OBL�GATO�RE
			AiBlock block = iteratorBlocks.next();
		
			{
				Collection<AiTile> blockNeighbors= block.getTile().getNeighbors();
				Iterator<AiTile> iteratorBlock = blockNeighbors.iterator();
				
				while (iteratorBlock.hasNext()) 
				{
						AiTile tile=iteratorBlock.next();
						if(tile.isCrossableBy(monia.ourHero))
							matrice[tile.getLine()][tile.getCol()] += ATTACK_HARDWALL;
				}
			}
				
			
		}
	}
	
	/**
	 * 
	 * La methode pour remplir une liste avec les casses "crossableby" notre bonhomme
	 * 
	 * 
	 * @param matrice
	 * 				La Matrice de Zone
	 * @param zone
	 * 				la zone du jeu
	 *			 
	 * @throws StopRequestException
	 * 
	 * @returns  endpoint
	 * 			 La liste des cases que notre ia peut passer
	 */
	public List<AiTile> endPoint(double[][] matrice,AiZone zone)throws StopRequestException 
	{
		
		monia.checkInterruption(); // APPEL OBL�GATO�RE
		List<AiTile> endpoint = new ArrayList<AiTile>();
		//AiTile tile=new AiTile();
		for(int i=0;i<zone.getHeight();i++)
		{
			monia.checkInterruption(); // APPEL OBL�GATO�RE
			for(int j=0;j<zone.getWidth();j++)
			{
				monia.checkInterruption(); // APPEL OBL�GATO�RE
				if(zone.getTile(i, j).isCrossableBy(monia.ourHero))
					endpoint.add(zone.getTile(i,j));
					
			}
		
			
		}
			
		return endpoint;
		}
	
	public List<Double> endpointValue(List<AiTile> endPoint, double[][] matrice)throws StopRequestException 
	{
		monia.checkInterruption(); // APPEL OBL�GATO�RE
		double temp;
		List<Double> result = new ArrayList<Double>();
		for(int i=0;i<endPoint.size();i++)
		{
			temp=matrice[endPoint.get(i).getLine()][endPoint.get(i).getCol()];
			result.add(temp);
		}
			
		return result;
	
	}
	/**
	 * 
	 *  
	 * La methode pour remplis une liste des distances.
	 * 
	 *
	 * 				 
	 * @param  shortestPathAttack
	 * 				 
	 *			 
	 * @throws StopRequestException
	 * 
	 * @returns  result
	 * 			       La liste de distance des casses du shortestPathAttack
	 */
	public List<Double> endpointDistance(List<AiPath> shortestPathAttack)throws StopRequestException 
	{
		monia.checkInterruption(); // APPEL OBL�GATO�RE
		List<Double> result= new ArrayList<Double>();
		double temp;
		
		for(int i=0;i<shortestPathAttack.size();i++)
		{
			temp=shortestPathAttack.get(i).getTileDistance();
			result.add(temp);
		}
		return result;
		
	}
	
	/**
	 * 
	 *  
	 * La methode qui returne la plus logique selon notre algo.
	 * 
	 * @param  endpointValue
	 * 				 		La Liste pour tenir compte des valeur de dernier case d'un chemin.
	 * @param  endpointDistance
	 * 				 		les Distance des chemins de "shortestPathAttack"
	 *	@param	 shortestPathAttack
	 *						La liste des chemins pour l'attaquer.
	 * @throws StopRequestException
	 * 
	 * @returns   shortestPathAttack.get(index)
	 * 			  				Le chemin plus logique selon notre algorithm.
	 */
	public AiPath objectifPath(List<Double> endpointValue, List<Double> endpointDistance, List<AiPath> shortestPathAttack)throws StopRequestException 
	{
		List<Double> result= new ArrayList<Double>();
		double temp;
		monia.checkInterruption(); // APPEL OBL�GATO�RE
		for(int i=0;i<endpointValue.size();i++)
		{
			if(endpointDistance.get(i)!=null)
				if( endpointDistance.get(i)!=0)
				{
					temp=endpointValue.get(i)/endpointDistance.get(i);
					result.add(temp);
				}
			else if(endpointDistance.get(i)==null ||endpointDistance.get(i)==0)
			{
				temp=0;
				result.add(temp);
			}
			
		}
		double tempmax=result.get(0);
		double temp2=tempmax;
		int index=0;
		for(int j=0;j<result.size();j++)
		{
			temp2=result.get(j);
			if(temp2>tempmax)
			{
				tempmax=temp2;
				index=j;
			}
		}
		//if(shortestPathAttack.get(index)!=null)
			return shortestPathAttack.get(index);
		//else
			//return 
		 
	}
	
	/**
	 * 
	 *  La methode pour remplir une liste des chemins possible pour avoir une occasion d'attaquer.
	 * 
	 * 
	 * @param  startPoint
	 * 				 
	 * @param  ownHero
	 * 					notre ia
	 * @param  endPoints
	 * 					La liste des dernier points des chemins possible.
	 * @param   zone		 
	 *			 La zone du jeu
	 * @throws StopRequestException
	 * 
	 * @returns   result
	 * 			  La liste des chemins pour un movement attaque
	 */
	public List<AiPath> shortestPathAttack(AiHero ownHero, AiTile startPoint,List<AiTile> endPoints, AiZone zone) throws StopRequestException
	{
		monia.checkInterruption(); // APPEL OBL�GATO�RE
		// le chemin le plus court possible
		AiPath shortestPath=null;
		// L'objet pour implementer l'algo A*
		Astar astar;
		// Calcul du cout par la classe de l'API
		CostCalculator cost = new BasicCostCalculator();
		// Calcul de l'heuristic par la classe de l'API
		List<AiPath> result = new ArrayList<AiPath>();
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		astar = new Astar(monia, ownHero, cost, heuristic);
		try
		{
			for(int i=0;i<endPoints.size();i++)
			{
				
				shortestPath = astar.processShortestPath(startPoint,endPoints.get(i));
				if(shortestPath!=null)
					result.add(shortestPath);
			}
		} 
		catch (LimitReachedException e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 
	 *  La methode pour appliquer l'algorithm d'attaque en utilisant les fonctions precedents.
	 * 
	 * 
	 * @param  zone
	 * 				La zone du jeu	 
	 * @param  matrice
	 * 				 La matrice de la zone
	 *			 
	 * @throws StopRequestException  
	 * 			  
	 */
	
	public void matriceAttack(double[][] matrice,AiZone zone)throws StopRequestException 
	{
		monia.checkInterruption(); // APPEL OBL�GATO�RE
		if (monia.nextMoveAttack == null) 
		{
			
			List<AiTile> endPoint=this.endPoint(matrice, zone);
			List<Double> endPointValue=this.endpointValue(endPoint,matrice);
			List<AiPath> shortestPathAtt=this.shortestPathAttack(monia.ourHero, monia.ourHero.getTile(), endPoint, zone);
			List<Double> endPointDistance=this.endpointDistance(shortestPathAtt);
			monia.nextMoveAttack = this.objectifPath(endPointValue, endPointDistance,shortestPathAtt);
				
		} 
		
		else 
		{
			if (monia.nextMoveAttack.getLength() == 0)
				monia.nextMoveAttack = null;
			else 
			{
				boolean adapt = false;
				List<AiTile> nextTiles = monia.nextMoveAttack.getTiles();
				for (int i = 0; i < nextTiles.size(); i++) 
				{
					if (!nextTiles.get(i).isCrossableBy(monia.ourHero)||matrice[nextTiles.get(i).getLine()][nextTiles.get(i).getCol()] == ATTACK_FIRE)
							adapt = true;
				}
			
				if (adapt)
					monia.nextMoveAttack = null;
				
				else 
				{
					if ((monia.ourHero.getLine() == monia.nextMoveAttack.getTile(0).getLine())&& (monia.ourHero.getCol() == monia.nextMoveAttack.getTile(0).getCol())) 
					{
						monia.nextMoveAttack.getTiles().remove(0);
						if (monia.nextMoveAttack.getTiles().isEmpty()) 
						{
							monia.nextMoveAttack = null;
		
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
	 *            les cases cibles ou le hero peut aller
	 * @return le chemin le plus court a parcourir
	 * @throws StopRequestException
	 */
	public AiPath shortestPath(AiHero ownHero, AiTile startPoint,List<AiTile> endPoints) throws StopRequestException
	{
		monia.checkInterruption(); // APPEL OBL�GATO�RE
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
	 * 
	 *  
	 * La methode pour la decision de mettre une bombe.
	 * 
	 * @param  matrice
	 * 				 La matrice de la zone
	 * @param  zone
	 * 				La zone du jeu 
	 *			 
	 * @throws StopRequestException
	 * 
	 * @returns   
	 * 			  
	 */
	public boolean dropBombCheckAttack(AiZone zone, double[][]matrice)throws StopRequestException
	{
		boolean result=false;
		AiPath path =null;
		List<AiTile> check= new ArrayList<AiTile>();
		for(int i=0; i<zone.getHeight(); i++)
		{
			monia.checkInterruption(); // APPEL OBL�GATO�RE
			for(int j=0; j<zone.getWidth(); j++)
			{
				monia.checkInterruption(); // APPEL OBL�GATO�RE
				if(matrice[i][j]!=ATTACK_FIRE ||matrice[i][j]!=ATTACK_BOMB || zone.getTile(i,j).isCrossableBy(monia.ourHero))
				{
					check.add(zone.getTile(i,j));
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
				monia.checkInterruption(); // APPEL OBL�GATO�RE
				if(zone.getTile(i, y).isCrossableBy(monia.ourHero))
					bombBlast.add(zone.getTile(i,y));
		}
		
		if(b<=0)
			b=0;
		if(y+bombRange>zone.getWidth())
			d=zone.getWidth();
		for(int j=b;j<d;j++)
			{
				monia.checkInterruption(); // APPEL OBL�GATO�RE
				if(zone.getTile(x, j).isCrossableBy(monia.ourHero))
					bombBlast.add(zone.getTile(x,j));
			}
		for(int k=0;k<bombBlast.size();k++)
		{
			monia.checkInterruption(); // APPEL OBL�GATO�RE
			for(int l=0;l<check.size();l++)
			{
				monia.checkInterruption(); // APPEL OBL�GATO�RE
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
			if(path.getPixelDistance()/monia.ourHero.getWalkingSpeed()>monia.ourHero.getExplosionDuration())
				result=true;
		}
		
		
		return result; 
	}
	/**
	 * 
	 * La methode pour trouver le plus court chemin d'attaquer.
	 * 
	 * 
	 * @param startPoint
	 * 				Le point de depart pour la recherche du chemin
	 * @param ownHero
	 * 				notre hero
	 * @param endPoint
	 * 				le dernier case de la chemin
	 * @param zone
	 * 				La zone du jeu
	 * @throws StopRequestException
	 * 
	 * @returns result
	 * 			Le chemin qu'on va suivre
	 */
	public AiPath shortestPathAttackTile(AiHero ownHero, AiTile startPoint,AiTile endPoint, AiZone zone) throws StopRequestException
	{
		monia.checkInterruption(); // APPEL OBL�GATO�RE
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
	 * 
	 *  La methode pour  que notre bonhomme puisse fuir en cas de danger.
	 * 
	 * 
	 * @param  zone
	 * 				 La zone du jeu
	 * @param  matrice
	 * 				  La matrice de la zone
	 *			 
	 * @throws StopRequestException
	 * 			  
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
					if(matrice[line][col]==CASE_EMPTY) 
						
					{
						if(monia.ourHero.getLine()!=line && monia.ourHero.getCol()!=col)
							tileList.add(zone.getTile(line, col));
					}
					if(matrice[line][col]>=0)
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
				if (matrice[monia.nextMove.getLastTile().getLine()][monia.nextMove.getLastTile().getCol()] ==ATTACK_FIRE)
					monia.nextMove = null;
				else 
				{
					List<AiTile> nextTiles = monia.nextMove.getTiles();
					for (int i = 0; i < nextTiles.size(); i++) 
					{
						if (!nextTiles.get(i).isCrossableBy(monia.ourHero))
							adapt = true;
					}
					if (adapt)
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
	
	
	
}
