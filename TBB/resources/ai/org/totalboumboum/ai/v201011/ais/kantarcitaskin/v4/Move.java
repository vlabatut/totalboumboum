package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v4;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Burcu Kantarcı
 * @author Ayça Taşkın
 */
@SuppressWarnings({ "unused", "deprecation" })
public class Move 
{
	/**
	 * Classe qui calcule la direction de movement en trouvant le chemin le plus court
	 * */
	AiZone zone;
	int mode;
	ArtificialIntelligence ai;
	double[][] matrix;
	
	
	/**
	 * Constructeur
	 * 
	 * @param zone
	 * 		la zone du jeu
	 * @param matrix
	 * 		la zone numerique du jeu
	 * @param ai
	 * 		AI
	 * 
	 * */
	public Move(AiZone zone, int mode, ArtificialIntelligence ai)
	{
		this.ai = ai;
		this.zone=zone;
		this.mode=mode;
	}
	
	
	/**
	 *trouve la direction de l'action se deplacer. Pour cela elle utilise l'algo A* 
	 *et puis la fonction de cout Cost et la matrice numerique qui represent la zone du jeu
	 *S'il n'y a pas de direction elle renvoi Direction.NONE, si n'y a pas de path 
	 *elle renvoi Direction.NONE aussi.
	 *
	 *
	 * @return resultat
	 * 		la direction de l'action se deplacer
	 * */
	public Direction getDirection() throws StopRequestException
	{	
		matrix = new double[zone.getHeight()][zone.getWidth()];
		MatrixCalculator collect = new MatrixCalculator(zone,mode,ai);
		matrix = collect.matrixConstruction();
		
		Cost  cost = new Cost(matrix);
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		Astar astar = new Astar(ai, zone.getOwnHero(), cost, heuristic);
		AiPath path = null;
		Direction resultat= Direction.NONE;
		AiTile target = targetTile();

		try 
		{
			path = astar.processShortestPath(zone.getOwnHero().getTile(), target);
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		} catch (LimitReachedException e) {
			// 
			e.printStackTrace();
		}
		if(path.getTiles().size()==0 )
		{
			List<AiTile> neig = calculeArea(1);
			Iterator<AiTile> neiglist = neig.iterator();
			AiTile temp, temp2 = zone.getOwnHero().getTile(), target2 = null;
			int line=0, col =0;
			
			
			while(neiglist.hasNext())
			{
				temp = neiglist.next();
				
					if(matrix[temp.getLine()][temp.getCol()]>=matrix[line][col])
					{
						if(zone.getOwnHero().getTile().getLine()-line == 1 && zone.getOwnHero().getTile().getCol() - col ==1)
						{
							boolean check1 = false, check2 = false;
							if(zone.getTile(zone.getOwnHero().getLine(), temp.getCol()).isCrossableBy(zone.getOwnHero()))
								check1 = true;
							if(zone.getTile(temp.getLine(),zone.getOwnHero().getCol()).isCrossableBy(zone.getOwnHero()))
								check2 = true;
							if(check1 || check2)
							{
								line= temp.getLine();
								col=temp.getCol();
								temp = zone.getTile(line, col);
							}	
							else
								matrix[temp.getLine()][temp.getCol()]=-100;
						}	
						else
						{
							line= temp.getLine();
							col=temp.getCol();
							temp = zone.getTile(line, col);
						}
					
					}
			}
			
			resultat = zone.getDirection(zone.getOwnHero().getTile(), zone.getTile(line, col));
		
		}
		else
		{
			path.removeTile(0);
			if(path.getTiles().size()==0)
			{
					resultat = Direction.NONE;
			}
			else
			{
				//if(isSafe(path.getTile(0)))
					resultat = zone.getDirection(zone.getOwnHero().getTile(), path.getFirstTile());
			
			}
		}
		
		for(int i=0; i<zone.getHeight(); i++)
		{
			for(int j=0; j<zone.getWidth(); j++)
			{
				ai.checkInterruption();
				org.totalboumboum.ai.v201011.adapter.communication.AiOutput output = ai.getOutput();
				output.setTileText(i, j, ""+matrix[i][j]);			
			}
		}	
		
			
		return resultat;
	}

	/*
	 * Cherche la case dont la valeur est plus eleve.S'elle sont tout les memes alors 
	 * il renvoi la case de l'hero correspondant
	 * 
	 * @return resultat
	 * 		la case dont la valeur est plus eleve ou bien la case de l'hero
	 * 
	 * */
	
	
	public AiTile targetTile()
	{
		int max=0, maxi=0, maxj=0;
		AiTile resultat;
		for (int i=0;i<zone.getHeight();i++)
		{
			for(int j=0;j<zone.getWidth();j++)
			{
				if(matrix[i][j]>=max)
				{
					maxi=i;
					maxj=j;
					max = (int) matrix[i][j];
				}
			
			}
			if(maxi==0 && maxj==0)
			{
				resultat = zone.getOwnHero().getTile();
			}
		}
		
		resultat= zone.getTile(maxi, maxj);
		return resultat;
	}
	public AiTile targetTile1()
	{
		AiTile result = zone.getOwnHero().getTile();
		double temp = 0;
		int x = result.getLine();
		int y = result.getCol();
		int xstart = result.getLine() - zone.getOwnHero().getBombRange();
		int xend = result.getLine() + zone.getOwnHero().getBombRange();
		int ystart = result.getCol() - zone.getOwnHero().getCol();
		int yend = result.getCol() + zone.getOwnHero().getCol();
		for (int i=xstart; i<xend; i++)
		{
			for (int j=ystart; j<yend; j++)
			{
				temp = temp + matrix[x+i][y+j];
				if(temp >= 100)
				{
					result = zone.getTile(i, j);
					break;
				}				
			}
			
		}
		
		
		return result;
	}
	
	
	
	public boolean isSafe(AiTile tile)
	{
		boolean result = true;
		List<AiBomb> bombs = this.zone.getBombs();
		Iterator<AiBomb> bombList = bombs.iterator();
		AiBomb bomb;
		while (bombList.hasNext())
		{
			bomb = bombList.next();
			List<AiTile> tempBlast = bomb.getBlast();
			Iterator<AiTile> blasts = tempBlast.iterator();
			AiTile blast;
			while(blasts.hasNext())
			{
				blast=blasts.next();
				if(blast.getLine() == tile.getLine() && blast.getCol() == tile.getCol())
					result = false;		
			}	
		}	
		return result;
	}
	
	
	
	public List<AiTile> calculeArea(int range)
	{
		List<AiTile> result = new ArrayList<AiTile>();
		result.add(zone.getOwnHero().getTile());
		result.add(zone.getOwnHero().getTile().getNeighbor(Direction.DOWN));
		result.add(zone.getOwnHero().getTile().getNeighbor(Direction.UP));
		result.add(zone.getOwnHero().getTile().getNeighbor(Direction.LEFT));
		result.add(zone.getOwnHero().getTile().getNeighbor(Direction.RIGHT));
		result.add(zone.getOwnHero().getTile().getNeighbor(Direction.DOWN).getNeighbor(Direction.LEFT));
		result.add(zone.getOwnHero().getTile().getNeighbor(Direction.DOWN).getNeighbor(Direction.RIGHT));
		result.add(zone.getOwnHero().getTile().getNeighbor(Direction.UP).getNeighbor(Direction.LEFT));
		result.add(zone.getOwnHero().getTile().getNeighbor(Direction.UP).getNeighbor(Direction.RIGHT));

		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
