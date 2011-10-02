package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v3;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
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
	/*
	 * Classe qui calcule la direction de movement en trouvant le chemin le plus court
	 * */
	AiZone zone;
	double[][] matrix;
	ArtificialIntelligence ai;
	/*
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
	public Move(AiZone zone, double[][] matrix, ArtificialIntelligence ai)
	{
		this.ai = ai;
		this.zone=zone;
		this.matrix=matrix;
	}
	
	
	/*
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
	{	Cost  cost = new Cost(matrix);
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
			List<AiTile> neig = zone.getOwnHero().getTile().getNeighbors();
			Iterator<AiTile> neiglist = neig.iterator();
			AiTile temp, temp2 = zone.getOwnHero().getTile(), target2 = null;
			int line=0, col =0;
			while(neiglist.hasNext())
			{
				temp = neiglist.next();
				
					if(matrix[temp.getLine()][temp.getCol()]>=matrix[line][col])
					{
						line= temp.getLine();
						col=temp.getCol();
					}
			}
			resultat = zone.getDirection(zone.getOwnHero().getTile(), zone.getTile(line, col));
			resultat = Direction.DOWN;
		}
		else
		{
			path.removeTile(0);
			if(path.getTiles().size()==0)
				resultat = Direction.NONE;
			else
				resultat = zone.getDirection(zone.getOwnHero().getTile(), path.getFirstTile());
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
	public AiTile targetTile ()
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
}
