package org.totalboumboum.ai.v201011.ais.goncuonat.v6;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
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
public class ShortestPath 
{

	private GoncuOnat monia;
	
	/**
	 * 
	 * @param monia
	 * @throws StopRequestException
	 */
	public ShortestPath(GoncuOnat monia) throws StopRequestException
	{
		monia.checkInterruption();
		this.monia = monia;
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
		monia.checkInterruption(); // APPEL OBLIGATOIRE
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
	 *  La methode pour remplir une liste des chemins possible pour avoir une occasion d'attaquer.
	 * 
	 * 
	 * @param  endPoints
	 * 				 les cases cibles ou le hero peut aller
	 * @param  ownHero
	 * 					notre ia
	 * @param  startPoint
	 * 					la case ou notre hero se trouve.
	 * @throws StopRequestException
	 * 
	 * @return   result
	 * 			  La liste des chemins pour un movement attaque
	 */
	public List<AiPath> shortestPathAttack(AiHero ownHero, AiTile startPoint,List<AiTile> endPoints) throws StopRequestException
	{
		monia.checkInterruption(); // APPEL OBLIGATOIRE
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
				monia.checkInterruption();
				shortestPath = astar.processShortestPath(startPoint,endPoints.get(i));
				if(shortestPath!=null)
					result.add(shortestPath);
				else
					result.add(null);
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
	 * 
	 * La methode qui calcul un chemin vers le startPoint a le endPoint
	 * @param  ownHero
	 * 					notre ia
	 * @param  endPoint
	 * 				 les cases cibles ou le hero peut alle
	 * @param  startPoint
	 * 					la case ou notre hero se trouve.
	 *
	 * @throws StopRequestException
	 * 
	 * @return AiPath
	 * 			le chemin entre 2 points donnees
	 * 
	 */
	
	public AiPath shortestPathEachTile(AiHero ownHero, AiTile startPoint,AiTile endPoint) throws StopRequestException
	{
		monia.checkInterruption(); // APPEL OBLIGATOIRE
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
	 * La methode qui renvoye le case du rival plus proche
	 *@param   zone		 
	 *			 La zone du jeu
	 *
	 * @throws StopRequestException
	 * 
	 * @return le case de rival plus proche
	 * 
	 */
	
	
public AiTile closestEnemy (AiZone zone) throws StopRequestException
{	
	monia.checkInterruption();
	AiTile result;
	List<AiHero> allHeros =zone.getHeroes();
	List<AiTile> allHerosTiless = new ArrayList<AiTile>();
	for(int i=0;i<allHeros.size();i++)
	{
		monia.checkInterruption();
		if(!allHeros.get(i).equals(monia.ourHero))
			allHerosTiless.add(allHeros.get(i).getTile());
	}
	List<Double> distance = new ArrayList<Double>();
	double dis;
	for(int i=0;i<allHerosTiless.size();i++)
	{
		monia.checkInterruption();
		dis=distance(monia.ourHero.getTile(),allHerosTiless.get(i));
		distance.add(dis);
	}
	
	double min1;
	double index=0;
	if(distance!=null && distance.size()!=0)
		min1=distance.get(0);
	else
		min1=0;
	double min2=0;
	for (int u=1;u<distance.size();u++)
	{
		monia.checkInterruption();
		min2=distance.get(u);
		if(min1>min2)
		{
			min1=min2;
			index=u;
		}
	}
	result=allHerosTiless.get((int) index);
	return result;
}


/**
 * La methode qui renvoie la distance manhattan entre 2 cases quelconque.
 * 
 * @param tile1
 * 			un case quelconque
 * @param tile2
 * 			un case quelconque
 *
 * @throws StopRequestException
 * 
 * @return la distance manhattan entre 2 cases quelconque
 * 
 */


public double distance(AiTile tile1, AiTile tile2)throws StopRequestException
{
	monia.checkInterruption();
	double x= tile1.getPosX();
	double y= tile1.getPosY();
	double xx= tile2.getPosX();
	double yy= tile2.getPosY();
	
	return (Math.abs(x-xx)+Math.abs(y-yy));
	
}

/**
 * La methode qui renvoi la premiere hero dans la zone
 * 
 * @param hero
 * 			un hero quelconque
 *@param   zone		 
 *			 La zone du jeu
 * @throws StopRequestException
 * 
 * @return AiHero
 * 
 */


public AiHero getHero(AiTile hero, AiZone zone) throws StopRequestException
{
	monia.checkInterruption();
	List<AiHero> heroes= new ArrayList<AiHero>();
	heroes=zone.getHeroes();
	AiHero result=heroes.get(0);
	if(heroes.size()!=0 && heroes!=null)
	{	
		for(int i=0;i<heroes.size();i++)
		{
			monia.checkInterruption();
			if(heroes.get(i).getLine()== hero.getLine() && heroes.get(i).getCol()== hero.getCol() )
				result=heroes.get(i);
		}	
	}	
	return result;
}


/**
 * La methode qui permet de calculer liste des distance entre les endPoints et La case du rival plus proche.
 * 
 * @param closestEnemy	
 * 			La case du rival plus proche
 * 
 * @param endPoints
 * 			La liste des points que notre bonhomme puisse voiloir aller.
 * @param   zone		 
 *			 La zone du jeu
 *
 * @throws StopRequestException
 * 
 * @return liste des distance entre les endPoints et La case du rival plus proche
 * 
 */

	public List<Double> tileToClosestEnemy(AiTile closestEnemy,List<AiTile> endPoints, AiZone zone) throws StopRequestException 	
	{
		monia.checkInterruption();
		List<Double> result=new ArrayList<Double>();
		double temp;
		for(int h=0;h<endPoints.size();h++)
		{
			monia.checkInterruption();
			temp=0;
			temp=distance(closestEnemy, endPoints.get(h));
			result.add(temp);
		}
		return result;
		
	
	}
}
