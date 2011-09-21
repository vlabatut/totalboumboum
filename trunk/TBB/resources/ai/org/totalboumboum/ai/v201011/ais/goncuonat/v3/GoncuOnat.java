package org.totalboumboum.ai.v201011.ais.goncuonat.v3;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
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
import org.totalboumboum.engine.content.feature.Direction;

public class GoncuOnat extends ArtificialIntelligence
{


	public AiHero ourHero;
	
	private final int CASE_EMPTY=0;
	private final int COLLECT_FIRE =-20 ;
	private final int COLLECT_SOFTWALL = 2;
	private final int COLLECT_BONUS= 10;
	private final int COLLECT_RIVAL = -10;
	private final int COLLECT_BOMB = -20;
	private final int ATTACK_HARDWALL =1 ;
	private final int ATTACK_FIRE =-20 ;
	
	private final int ATTACK_BONUS= 3;
	private final int ATTACK_RIVAL = 4;
	private final int ATTACK_BOMB = -20;
	public AiPath nextMove=null;
	public AiPath nextMoveBonus=null;
	public AiPath nextMoveAttack=null;
	private boolean searchBonus = true;
	
	
	
	
	private void valueFiresCollecte(double[][] matrice, AiZone zone) throws StopRequestException
	{
		checkInterruption();
		Collection<AiFire> fires = zone.getFires();
		Iterator<AiFire> iteratorFires = fires.iterator();
		while (iteratorFires.hasNext()) {
			checkInterruption();
			AiFire fire = iteratorFires.next();
			matrice[fire.getLine()][fire.getCol()] = COLLECT_FIRE;
		}
	}
	private void valueBonusCollecte(double[][] matrice, AiZone zone)throws StopRequestException 
	{
		checkInterruption();
		Collection<AiItem> items = zone.getItems();
		Iterator<AiItem> iteratorItems = items.iterator();
		while (iteratorItems.hasNext()) 
		{
			checkInterruption();
			AiItem item = iteratorItems.next();
			matrice[item.getLine()][item.getCol()] =COLLECT_BONUS;
		}
}
	private void valueRivalCollecte(double[][] matrice, AiZone zone)throws StopRequestException 
	{
		checkInterruption();
		Collection<AiHero> items = zone.getHeroes();
		Iterator<AiHero> iteratorHeroes = items.iterator();
		while (iteratorHeroes.hasNext()) 
		{
			checkInterruption();
			AiHero hero = iteratorHeroes.next();
			if(hero.getLine()!=ourHero.getLine()&&hero.getCol()!=ourHero.getCol())
			
				matrice[hero.getLine()][hero.getCol()] =COLLECT_RIVAL;
		}
	
		
	}
	


	private void valueBlocksCollecte(double[][] matrice, AiZone zone) throws StopRequestException{
		checkInterruption();
		Collection<AiBlock> blocks = zone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		while (iteratorBlocks.hasNext()) 
		{
			checkInterruption();
			AiBlock block = iteratorBlocks.next();
			if(block.isDestructible())
			{
				Collection<AiTile> blockNeighbors= block.getTile().getNeighbors();
				Iterator<AiTile> iteratorBlock = blockNeighbors.iterator();
				
				while (iteratorBlock.hasNext()) 
				{
						AiTile tile=iteratorBlock.next();
						if(tile.getBombs().isEmpty()&&tile.getFires().isEmpty()&&tile.getItems().isEmpty()&&tile.isCrossableBy(ourHero))
							matrice[tile.getLine()][tile.getCol()] = COLLECT_SOFTWALL;
				}
					
			}
				
		
			
		}
	}
	private void valueBombsCollecte(double[][] matrice, AiZone zone) throws StopRequestException{
		checkInterruption();
		Collection<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		//Collection<AiTile> inScopeTiles=
		while (iteratorBombs.hasNext()) 
		{
			checkInterruption();
			AiBomb bomb = iteratorBombs.next();
			matrice[bomb.getLine()][bomb.getCol()] = COLLECT_BOMB;
			
			Collection<AiTile> inScopeTiles = bomb.getBlast();
			
			Iterator<AiTile> iteratorScope = inScopeTiles.iterator();
		
			
			while (iteratorScope.hasNext())
			{
				checkInterruption();
				AiTile blastCase=iteratorScope.next();
				matrice[blastCase.getLine()][blastCase.getCol()] = COLLECT_FIRE;
				
			}
		}
	}
	/*
	 * public boolean dropBombCheckAttack(AiZone zone, double[][]matrice)throws StopRequestException
	{
			boolean result=false;
			List<AiTile> check= new ArrayList<AiTile>();
			for(int i=0; i<zone.getHeight(); i++)
			{
				checkInterruption();
				for(int j=0; j<zone.getWidth(); j++)
				{
					checkInterruption();
					if(matrice[i][j]!=ATTACK_FIRE ||matrice[i][j]!=ATTACK_BOMB ||!zone.getTile(i,j).isCrossableBy(ourHero))
					{
						check.add(zone.getTile(i,j));
					}
				}
			}
			List <AiTile> bombBlast =new ArrayList<AiTile>();
			
			int bombRange=ourHero.getBombRange();
			int x=ourHero.getTile().getLine();
			int y=ourHero.getTile().getCol();
			int a=x-bombRange;
			int b=y-bombRange;
			int c=x+bombRange;
			int d=y+bombRange;
			
			if(a<=0)
					a=0;
			if(c>zone.getHeight());
					c=zone.getHeight();
			for(int i=a;i<x+c;i++)
			{
					checkInterruption();
					bombBlast.add(zone.getTile(i,y));
			}
			if(b<=0)
				b=0;
			if(y+bombRange>zone.getWidth())
				d=zone.getWidth();
			for(int j=y-bombRange;j<d;j++)
			{
				checkInterruption();
				bombBlast.add(zone.getTile(x,j));
			}
			for(int k=0;k<bombBlast.size();k++)
			{
			checkInterruption();
			for(int l=0;l<check.size();l++)
			{
			checkInterruption();
			if(bombBlast.get(k).getLine()==check.get(l).getLine()&&bombBlast.get(k).getCol()==check.get(l).getCol())
			check.remove(l);
			}
			}
			AiPath path=this.shortestPath(ourHero, ourHero.getTile(), check);
			if(path!=null)
			{
			if(path.getPixelDistance()/ourHero.getWalkingSpeed()>ourHero.getBurningDuration())
			result=true;
			}
			return result;
			 
	}
*/
	
	
	public void initialiseMatrice(double [][]matrice, AiZone zone)throws StopRequestException
	{
		checkInterruption();
		int height=zone.getHeight();
		int width=zone.getWidth();
		for(int i=0; i<height; i++)
		{
			checkInterruption();
			for(int j=0; j<width; j++)
			{
				checkInterruption();
				matrice[i][j] = CASE_EMPTY;
				
			}
		}
	}
	private void valueFiresAttack(double[][] matrice, AiZone zone) throws StopRequestException
	{
		checkInterruption();
		Collection<AiFire> fires = zone.getFires();
		Iterator<AiFire> iteratorFires = fires.iterator();
		
		
		while (iteratorFires.hasNext()) 
		{
			checkInterruption();
			AiFire fire=iteratorFires.next();
			
			Collection<AiTile> fireNeighbors=fire.getTile().getNeighbors();
			Iterator<AiTile> iteratorFire = fireNeighbors.iterator();
			while(iteratorFire.hasNext())
			{
				AiTile tile=iteratorFire.next();
				checkInterruption();
				if(tile.isCrossableBy(ourHero))
					matrice[tile.getLine()][tile.getCol()] += ATTACK_FIRE;
			}
		}
		
		
	}
	private void valueBonusAttack(double[][] matrice, AiZone zone)throws StopRequestException 
	{
		checkInterruption();
		Collection<AiItem> items = zone.getItems();
		Iterator<AiItem> iteratorItems = items.iterator();
		while (iteratorItems.hasNext()) 
		{
			checkInterruption();
			AiItem item = iteratorItems.next();
			Collection<AiTile> bonusNeighbors=item.getTile().getNeighbors();
			Iterator<AiTile> iteratorFire = bonusNeighbors.iterator();
			while(iteratorFire.hasNext())
			{
				AiTile tile=iteratorFire.next();
				if(tile.isCrossableBy(ourHero))
					matrice[tile.getLine()][tile.getCol()]+=ATTACK_BONUS;
			}
			
		}
	}
	private void valueBombsAttack(double[][] matrice, AiZone zone) throws StopRequestException{
		checkInterruption();
		Collection<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		//Collection<AiTile> inScopeTiles=
		while (iteratorBombs.hasNext()) 
		{
			checkInterruption();
			AiBomb bomb = iteratorBombs.next();
			
			Collection<AiTile> bombNeighbors=bomb.getTile().getNeighbors();
			Iterator<AiTile> iteratorBomb = bombNeighbors.iterator();
			while(iteratorBomb.hasNext())
			{
				AiTile tile=iteratorBomb.next();
				if(tile.isCrossableBy(ourHero))
					matrice[bomb.getLine()][bomb.getCol()] += ATTACK_BOMB;
			}
			Collection<AiTile> inScopeTiles = bomb.getBlast();
			
			Iterator<AiTile> iteratorScope = inScopeTiles.iterator();
		
			
			while (iteratorScope.hasNext())
			{
				checkInterruption();
				AiTile blastCase=iteratorScope.next();
				matrice[blastCase.getLine()][blastCase.getCol()] += ATTACK_FIRE;
				
			}
		}
	}
	
	public void valueRivalAttack(double[][] matrice, AiZone zone)throws StopRequestException 
	{
		checkInterruption();
		Collection<AiHero> items = zone.getHeroes();
		Iterator<AiHero> iteratorHeroes = items.iterator();
		while (iteratorHeroes.hasNext()) 
		{
			AiHero hero =iteratorHeroes.next();
			int x =hero.getLine();
			int y =hero.getCol();
			int blast=ourHero.getBombRange();
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
				checkInterruption();
				for(int k=j;k<rangey;k++)
				{
					checkInterruption();
					if(zone.getTile(a, k)!=zone.getTile(ourHero.getLine(),ourHero.getCol()))
					{
						if((Math.abs(a-x)==(ourHero.getBombRange()-2) || Math.abs(k-y)==(ourHero.getBombRange()-2)))
						{ 
							
							matrice[a][k]+=ourHero.getBombRange()+ATTACK_RIVAL-2;
						}
						
						else if((Math.abs(a-x)==(ourHero.getBombRange()-1) ||Math.abs(k-y)==(ourHero.getBombRange()-1)))
						{ 
							matrice[a][k]+=ourHero.getBombRange()+ATTACK_RIVAL-1;
						}
						
						else if((Math.abs(a-x)==ourHero.getBombRange() || Math.abs(k-y)==ourHero.getBombRange()))
						{
							matrice[a][k]+=ourHero.getBombRange()+ATTACK_RIVAL;
						}
					}
				}
			}	
		}
	}
	
	private void valueBlocksAttack(double[][] matrice, AiZone zone) throws StopRequestException{
		checkInterruption();
		Collection<AiBlock> blocks = zone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		while (iteratorBlocks.hasNext()) 
		{
			checkInterruption();
			AiBlock block = iteratorBlocks.next();
		
			{
				Collection<AiTile> blockNeighbors= block.getTile().getNeighbors();
				Iterator<AiTile> iteratorBlock = blockNeighbors.iterator();
				
				while (iteratorBlock.hasNext()) 
				{
						AiTile tile=iteratorBlock.next();
						if(tile.isCrossableBy(ourHero))
							matrice[tile.getLine()][tile.getCol()] += ATTACK_HARDWALL;
				}
			}
				
			
		}
	}
	private void updateOutputAttack(double [][]matrice, AiZone zone) throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
	
		AiOutput output = getOutput();

		
		for(int line=0;line<zone.getHeight();line++)
		{	checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	checkInterruption(); //APPEL OBLIGATOIRE
				
				double a=(matrice[line][col]);
				String text=Double.toString(a);
				
				output.setTileText(line,col,text);
			}
		}
	}
	
	
	//public void
	
	public AiAction action(AiPath nextMove) throws StopRequestException
	{
		checkInterruption();
		
		List<AiTile> tiles= new ArrayList<AiTile>();
		tiles = nextMove.getTiles();
	
		double dx;
		
		double dy;
		{
		dx = (tiles.get(0).getLine()) - (this.ourHero.getLine());
	
		dy = (tiles.get(0).getCol()) - (this.ourHero.getCol());

		if (dx < 0 && dy == 0) 
		{
			return new AiAction(AiActionName.MOVE, Direction.UP);
		}
		else if (dx < 0 && dy < 0) 
		{
			return new AiAction(AiActionName.MOVE, Direction.UPLEFT);
		} 
		else if (dx == 0 && dy < 0)
		{
			return new AiAction(AiActionName.MOVE, Direction.LEFT);
		} 
		else if (dx > 0 && dy == 0) 
		{
			return new AiAction(AiActionName.MOVE, Direction.DOWN);
		} 
		else if (dx > 0 && dy > 0)
		{
			return new AiAction(AiActionName.MOVE, Direction.DOWNRIGHT);
		} 
		else if (dx == 0 && dy > 0) 
		{
			return new AiAction(AiActionName.MOVE, Direction.RIGHT);
		} 
		else if (dx > 0 && dy < 0) 
		{
			return new AiAction(AiActionName.MOVE, Direction.DOWNLEFT);
		}
		else if (dx < 0 && dy > 0)
		{
			return new AiAction(AiActionName.MOVE, Direction.UPRIGHT);
		}
		else {
			return new AiAction(AiActionName.NONE);
		}
		}

	}
	
	private void updateOutputCollecte(double [][]matrice, AiZone zone) throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
	
		AiOutput output = getOutput();

		
		for(int line=0;line<zone.getHeight();line++)
		{	
			checkInterruption(); //APPEL OBLIGATOIRE
			
			for(int col=0;col<zone.getWidth();col++)
			{	
				checkInterruption(); //APPEL OBLIGATOIRE
				
			double a=(matrice[line][col]);
			String text=Double.toString(a);
			
			output.setTileText(line,col,text);
				 
			}
		}
	}
	
	
	@Override
	public AiAction processAction() throws StopRequestException 
	{
		checkInterruption();
		AiZone zone=getPercepts();
		AiAction result=new AiAction(AiActionName.NONE);
		int width = zone.getWidth();
		int height = zone.getHeight();
		double[][] matrice= new double[height][width];
		this.ourHero=zone.getOwnHero();
		boolean dropBomb = false;
		this.initialiseMatrice(matrice, zone);
		
		if(ourHero.getBombNumberMax()>=3 )
		{
			
			this.valueBonusAttack(matrice, zone);
			//this.valueRivalAttack(matrice, zone);
			this.valueBlocksAttack(matrice,zone);
			this.valueBombsAttack(matrice, zone);
			this.valueFiresAttack(matrice, zone);
			this.valueRivalAttack(matrice, zone);
			this.updateOutputAttack(matrice, zone);
			System.out.println("matrice attack day z");
			
			dropBomb = false;
			
			if ((matrice[ourHero.getLine()][ourHero.getCol()] == ATTACK_FIRE)
				||(matrice[ourHero.getLine()][ourHero.getCol()] == ATTACK_BOMB)) 
			{
				this.nextMoveAttack=null;
				this.runAwayAlgo(matrice,zone);
				if (nextMove != null)
					result = action(nextMove);
				
			}
			
			else
			{	
				matriceAttack( matrice,zone);
				
					dropBomb = true;
				
			
				if (dropBomb) 
				{
					
					result = new AiAction(AiActionName.DROP_BOMB);
				}
		
				else
				{
					if (nextMoveAttack != null)
					{
						result = this.action(nextMoveAttack);
					}
				}
		
			}
		}
		
		else
		{

			this.valueBonusCollecte(matrice, zone);
			this.valueRivalCollecte(matrice, zone);
			this.valueBlocksCollecte(matrice,zone);
			this.valueBombsCollecte(matrice, zone);
			this.valueFiresCollecte(matrice, zone);
		
			this.updateOutputCollecte(matrice,zone);
			
			
			dropBomb = false;
			
			if ((matrice[ourHero.getLine()][ourHero.getCol()] == COLLECT_FIRE)
				||(matrice[ourHero.getLine()][ourHero.getCol()] == COLLECT_BOMB)) 
			{
				this.nextMoveBonus=null;
				this.runAwayAlgo(matrice,zone);
				if (nextMove != null)
					result = action(nextMove);
				
			}
			
			else
			{
				if(searchBonus)
				{	matriceCollecte( matrice,zone);
					if (matrice[ourHero.getTile().getLine()][ourHero.getTile().getCol()] == COLLECT_SOFTWALL)
					{
						//if8kontrol)
						dropBomb = true;
						
					}
						
					else 
					{
						if (matrice[ourHero.getTile().getLine()][ourHero.getTile().getCol()]  == COLLECT_FIRE)
						{
							dropBomb = false;
						}
					}
					if (dropBomb) 
					{
						
						result = new AiAction(AiActionName.DROP_BOMB);
					}
			
					else
					{
						if (nextMoveBonus != null)
						{
							result = this.action(nextMoveBonus);
						}
					}
				}
				
			}
		}
		
		return result;
		 
	}
	public double shortestPathCost(double[][]matrice, AiPath nextMove)throws StopRequestException
	{
		checkInterruption();
		double result=0;
		if(nextMove.getLength()!=0)
		{
			
			for(int i=0;i<nextMove.getLength();i++)
			{
				AiTile tempPath=nextMove.getTile(i);
				result= matrice[tempPath.getLine()][tempPath.getCol()];
			
			}
		}
		return result+10;	
	}
	public double bestPathCost (double[][] matrice,AiPath nextMove) throws StopRequestException
	{
		double result=(this.shortestPathCost(matrice, nextMove)/(this.getPathDistance(nextMove)*ourHero.getWalkingSpeed()));
			
		return result;
	}
	
	public int getPathDistance(AiPath nextMove)throws StopRequestException
	{
		checkInterruption();
		if(nextMove.getLength()!=0)
			return nextMove.getLength();
		else
			return 1;
	}
	
	
	private void runAwayAlgo(double[][] matrice,AiZone zone)throws StopRequestException 
	{
		checkInterruption();
		
		
		if (this.nextMove == null) 
		{
			List<AiTile> tileList=new ArrayList<AiTile>();
			
			for(int line=0;line<zone.getHeight();line++)
			{
				checkInterruption();
				for(int col=0;col<zone.getWidth();col++)
				{
					checkInterruption();
					if(matrice[line][col]==CASE_EMPTY) 
						
					{
						if(ourHero.getLine()!=line && ourHero.getCol()!=col)
							tileList.add(zone.getTile(line, col));
					}
					if(matrice[line][col]==COLLECT_SOFTWALL)
					{
						if(ourHero.getLine()!=line && ourHero.getCol()!=col)
							tileList.add(zone.getTile(line, col));
					}
				}
			}
			if(tileList.size()!=0)
				this.nextMove=shortestPath(ourHero,ourHero.getTile(),tileList);
		}
		
		else 
		{
			if (nextMove.getLength() == 0)
				nextMove = null;
			else 
			{
				boolean adapt = false;
				if (matrice[nextMove.getLastTile().getLine()][nextMove.getLastTile().getCol()] == COLLECT_FIRE)
						nextMove = null;
				else 
				{
					List<AiTile> nextTiles = nextMove.getTiles();
					for (int i = 0; i < nextTiles.size(); i++) 
					{
						if (!nextTiles.get(i).isCrossableBy(ourHero))
							adapt = true;
					}
					if (adapt)
						nextMove = null;
					else 
					{
						if ((this.ourHero.getLine() == this.nextMove.getTile(0).getLine())&& (this.ourHero.getCol() == this.nextMove.getTile(0).getCol()))
						{
							this.nextMove.getTiles().remove(0);
							if (this.nextMove.getTiles().isEmpty())
							{
								this.nextMove = null;
							}
						}
					}
				}
			}
		}

	}
	
	private List<AiTile> endPoint(double[][] matrice,AiZone zone)throws StopRequestException 
	{
		
		checkInterruption();
		List<AiTile> endpoint = new ArrayList<AiTile>();
		//AiTile tile=new AiTile();
		for(int i=0;i<zone.getHeight();i++)
		{
			checkInterruption();
			for(int j=0;j<zone.getWidth();j++)
			{
				checkInterruption();
				if(zone.getTile(i, j).isCrossableBy(ourHero))
					endpoint.add(zone.getTile(i,j));
					
			}
		
			
		}
			
		return endpoint;
		}
	
	private List<Double> endpointValue(List<AiTile> endPoint, double[][] matrice)throws StopRequestException 
	{
		checkInterruption();
		double temp;
		List<Double> result = new ArrayList<Double>();
		for(int i=0;i<endPoint.size();i++)
		{
			temp=matrice[endPoint.get(i).getLine()][endPoint.get(i).getCol()];
			result.add(temp);
		}
			
		return result;
	
	}
	
	private List<Double> endpointDistance(List<AiPath> shortestPathAttack)throws StopRequestException 
	{
		checkInterruption();
		List<Double> result= new ArrayList<Double>();
		double temp;
		
		for(int i=0;i<shortestPathAttack.size();i++)
		{
			temp=shortestPathAttack.get(i).getTileDistance();
			result.add(temp);
		}
		return result;
		
	}
	
	private AiPath objectifPath(List<Double> endpointValue, List<Double> endpointDistance, List<AiPath> shortestPathAttack)throws StopRequestException 
	{
		List<Double> result= new ArrayList<Double>();
		double temp;
		checkInterruption();
		for(int i=0;i<endpointValue.size();i++)
		{
			if(endpointDistance.get(i)!=null ||endpointDistance.get(i)!=0)
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
		
		return shortestPathAttack.get(index);
		
	}
	
	private List<AiPath> shortestPathAttack(AiHero ownHero, AiTile startPoint,List<AiTile> endPoints, AiZone zone) throws StopRequestException
	{
		checkInterruption();
		// le chemin le plus court possible
		AiPath shortestPath=null;
		// L'objet pour implementer l'algo A*
		Astar astar;
		// Calcul du cout par la classe de l'API
		CostCalculator cost = new BasicCostCalculator();
		// Calcul de l'heuristic par la classe de l'API
		List<AiPath> result = new ArrayList<AiPath>();
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		astar = new Astar(this, ownHero, cost, heuristic);
		try
		{
			for(int i=0;i<endPoints.size();i++)
			{
				
				shortestPath = astar.processShortestPath(startPoint,endPoints.get(i));
				result.add(shortestPath);
			}
		} 
		catch (LimitReachedException e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	private AiPath shortestPathAttackTile(AiHero ownHero, AiTile startPoint,AiTile endPoint, AiZone zone) throws StopRequestException
	{
		checkInterruption();
		// le chemin le plus court possible
		AiPath shortestPath=null;
		// L'objet pour implementer l'algo A*
		Astar astar;
		// Calcul du cout par la classe de l'API
		CostCalculator cost = new BasicCostCalculator();
		// Calcul de l'heuristic par la classe de l'API
		AiPath result = new AiPath();
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		astar = new Astar(this, ownHero, cost, heuristic);
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
	
	
	
	
	private void matriceAttack(double[][] matrice,AiZone zone)throws StopRequestException 
	{
		checkInterruption();
		if (nextMoveAttack == null)
		{
			
			List<AiTile> endPoint=this.endPoint(matrice, zone);
			List<Double> endPointValue=this.endpointValue(endPoint,matrice);
			List<AiPath> shortestPathAtt=this.shortestPathAttack(ourHero, ourHero.getTile(), endPoint, zone);
			List<Double> endPointDistance=this.endpointDistance(shortestPathAtt);
			nextMoveAttack = this.objectifPath(endPointValue, endPointDistance,shortestPathAtt);
			
				
		} 
		
		else 
		{
			if (nextMoveAttack.getLength() == 0)
				nextMoveAttack = null;
			else 
			{
				
				boolean adapt = false;
				List<AiTile> nextTiles = nextMoveAttack.getTiles();
				for (int i = 0; i < nextTiles.size(); i++) 
				{
					if (!nextTiles.get(i).isCrossableBy(ourHero)||matrice[nextTiles.get(i).getLine()][nextTiles.get(i).getCol()] == ATTACK_FIRE)
							adapt = true;
				}
			
				if (adapt)
					nextMoveAttack = null;
				
				else 
				{
					if ((this.ourHero.getLine() == this.nextMoveAttack.getTile(0).getLine())&& (this.ourHero.getCol() == this.nextMoveBonus.getTile(0).getCol())) 
					{
						this.nextMoveAttack.getTiles().remove(0);
						if (this.nextMoveAttack.getTiles().isEmpty()) 
						{
							this.nextMoveAttack = null;
		
						}
					}
				}
			}
		}
		
	}
	
	private void matriceCollecte(double[][] matrice,AiZone zone)throws StopRequestException 
	{
		checkInterruption();
		
		if (nextMoveBonus == null) 
		{
			List<AiItem> itemList = zone.getItems();
			if (itemList.size() != 0) 
			{
				List<AiTile> itemTiles = new ArrayList<AiTile>();
				for (int i = 0; i < itemList.size(); i++) 
				{
					checkInterruption();
					if (matrice[itemList.get(i).getLine()][itemList.get(i).getCol()] != COLLECT_FIRE )
						itemTiles.add(itemList.get(i).getTile());
				}
					
					
					List<Double>  totalOfTiles = new ArrayList<Double>();
		
					double temp;
					AiPath tempPath = new  AiPath();
					double max1=0,max2=0;
					int index=0;
	
					for(int i=0;i<itemTiles.size();i++)
					{
						
						tempPath= shortestPathAttackTile(ourHero, ourHero.getTile(), itemTiles.get(i),zone);
						if( tempPath != null )
							temp= shortestPathCost(matrice, tempPath) /(getPathDistance(tempPath)*ourHero.getWalkingSpeed());
						else
							temp=0;
						
						totalOfTiles.add(temp);
					}
					max1=totalOfTiles.get(0);
					max2=max1;
					
					for (int j=0;j<totalOfTiles.size();j++)
					{
							max2=totalOfTiles.get(j);
							if(max2>max1)
							{
								max1=max2;
								index=j;
							}
					}
					
					nextMoveBonus = shortestPathAttackTile(ourHero,ourHero.getTile(),itemTiles.get(index),zone);
					
					
					
					//nextMoveBonus = shortestPath(ourHero,ourHero.getTile(),itemTiles);
				
			}
			else 
			{
					
				Collection<AiBlock> blocks = zone.getBlocks();
				Iterator<AiBlock> iteratorBlocks = blocks.iterator();
				List<AiBlock> destructibleBlocks = new ArrayList<AiBlock>();
				List<AiTile> endPoint=new ArrayList<AiTile>();
				while (iteratorBlocks.hasNext()) 
				{
					checkInterruption();
					AiBlock block = iteratorBlocks.next();
					if(block.isDestructible())
					{
						destructibleBlocks.add(block);
						List<AiTile> blockneighbor = block.getTile().getNeighbors();
						
						for(int i=0;i<blockneighbor.size();i++)
						{	
							if(matrice[blockneighbor.get(i).getLine()][blockneighbor.get(i).getCol()]==COLLECT_SOFTWALL)
								{
								endPoint.add(blockneighbor.get(i));
								System.out.println("bomba konacak yer:"+blockneighbor.get(i).getLine()+","+blockneighbor.get(i).getCol());
								}
						}
						
					}
					
					
				}
				
				if(endPoint.size()!=0)
				{
					
					
					{
						nextMoveBonus = shortestPath(ourHero,ourHero.getTile(),endPoint);
						
						
					//	int yol=this.getPathDistance(nextMove);
						
						/*double ret= this.shortestPathCost(matrice, nextMove);
						
						//double best=this.bestPathCost(matrice, nextMove);
						*/
					}
					
					
				} 
		
				
				
				else 
				{
					searchBonus = false;
				}
		
			nextMove = null;
			}
			
		} 
		
		else 
		{
			if (nextMoveBonus.getLength() == 0)
				nextMoveBonus = null;
			else 
			{
				boolean adapt = false;
				List<AiTile> nextTiles = nextMoveBonus.getTiles();
				for (int i = 0; i < nextTiles.size(); i++) 
				{
					if (!nextTiles.get(i).isCrossableBy(ourHero)||matrice[nextTiles.get(i).getLine()][nextTiles.get(i).getCol()] == COLLECT_FIRE)
							adapt = true;
				}
			
				if (adapt)
					nextMoveBonus = null;
				
				else 
				{
					if ((this.ourHero.getLine() == this.nextMoveBonus.getTile(0).getLine())&& (this.ourHero.getCol() == this.nextMoveBonus.getTile(0).getCol())) 
					{
						this.nextMoveBonus.getTiles().remove(0);
						if (this.nextMoveBonus.getTiles().isEmpty()) 
						{
							this.nextMoveBonus = null;
		
						}
					}
				}
			}
		}
	}
	
	
	private AiPath shortestPath(AiHero ownHero, AiTile startPoint,List<AiTile> endPoints) throws StopRequestException
	{
		checkInterruption();
		// le chemin le plus court possible
		AiPath shortestPath=null;
		// L'objet pour implementer l'algo A*
		Astar astar;
		// Calcul du cout par la classe de l'API
		CostCalculator cost = new BasicCostCalculator();
		// Calcul de l'heuristic par la classe de l'API
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		astar = new Astar(this, ownHero, cost, heuristic);
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
	
	/*public AiPath runAwayPath(AiHero ourHero, double [][]matrice, AiPath nextmove,AiZone zone)throws StopRequestException
	{
		checkInterruption();
		AiPath path=nextMove;
		if(matrice[ourHero.getLine()][ourHero.getCol()]== COLLECT_FIRE)
		{
			List<AiTile> tempTile = new ArrayList<AiTile>();
			for(int i=0; i<zone.getHeight(); i++)
			{
				checkInterruption();
				for(int j=0; j<zone.getWidth(); j++)
				{
					checkInterruption();
					if(matrice[i][j]==CASE_EMPTY)
						tempTile.add(zone.getTile(i,j));
				}
			path=this.shortestPath(ourHero, ourHero.getTile(),tempTile);
			
			
			}
		}
		
		return path;
		
		
	}*/
	
	
}

