package org.totalboumboum.ai.v200910.ais.enhoskarapazar.v5c;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.HeuristicCalculator;

/**
 * @author Sadettin Enhoş
 * @author Ali Can Karapazar
 */
public class PathManagement {

	
	/**
	 * crée un PathManager chargé d'amener le personnage à la position (x,y)
	 * exprimée en pixels
	 */
	public PathManagement(EnhosKarapazar ai, double x, double y) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		init(ai);
		setDestination(x,y);
	}
	
	/**
	 * crée un PathManager chargé d'amener le personnage au centre de la case
	 * passée en paramètre
	 */
	public PathManagement(EnhosKarapazar ai, AiTile destination) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		init(ai);
		setDestination(destination);
	}
	
	/**
	 * initialise ce PathManager
	 */
	private void init(EnhosKarapazar ai) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		this.ai = ai;
		zone = ai.getZone();
		costCalculator = new BasicCostCalculator();
		heuristicCalculator = new BasicHeuristicCalculator();
		astar = new Astar(ai,ai.getOwnHero(),costCalculator,heuristicCalculator);
		//updatePrev();
	}
	
	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** l'IA concernée par ce gestionnaire de chemin */
	private EnhosKarapazar ai;
	/** zone de jeu */
	private AiZone zone;
	
	/////////////////////////////////////////////////////////////////
	// DESTINATION	/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** la case de destination sélectionnée */
	private AiTile tileDest;
	/** l'abscisse de destination */
	private double xDest;
	/** l'ordonnée de destination */
	private double yDest;
	
	/**
	 * modifie la case de destination du personnage,
	 * place les coordonnées de destination au centre de cette case,
	 * et recalcule le chemin.
	 */
	public void setDestination(AiTile destination) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		tileDest = destination;
		xDest = tileDest.getPosX();
		yDest = tileDest.getPosY();
		path = astar.processShortestPath(ai.getCurrentTile(),destination);
		if(path.getLength()>0)
			path.removeTile(0);
	}

	/**
	 * modifie les coordonnées de destination,
	 * met à jour automatiquement la case correspondante,
	 * et recalcule le chemin.
	 */
	public void setDestination(double x, double y) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		double normalized[] = zone.normalizePosition(x, y);
		xDest = normalized[0];
		yDest = normalized[1];
		tileDest = zone.getTile(xDest,yDest);
		path = astar.processShortestPath(ai.getCurrentTile(),tileDest);
		if(path.getLength()>0)	
		path.removeTile(0);
	}


	/////////////////////////////////////////////////////////////////
	// PATH			/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le chemin à suivre */
	private AiPath path;
	
	
	/** 
	 * teste si le chemin est toujours valide, i.e. s'il
	 * est toujours sûr et si aucun obstacle n'est apparu
	 * depuis la dernière itération
	 */
	public boolean checkPathValidity() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result = true;
		Iterator<AiTile> it = path.getTiles().iterator();
		while(it.hasNext() && result)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			AiTile tile = it.next();
			if (tile.isCrossableBy(ai.getOwnHero()) && estCaseSure(tile.getLine(), tile.getCol())) 
					result=true;
			else
				result=false;
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// A STAR					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** classe implémentant l'algorithme A* */
	private Astar astar;
	/** classe implémentant la fonction heuristique */
	private HeuristicCalculator heuristicCalculator;
	/** classe implémentant la fonction de coût */
	private CostCalculator costCalculator;

	

	
	
	public int getLength() throws StopRequestException
	{	
		ai.checkInterruption();
		return path.getLength();
	}
	public  List<AiTile> getPathList() throws StopRequestException
	{
		ai.checkInterruption();
		return path.getTiles();
	}
	
	public boolean isWalkable() throws StopRequestException
	{ 
		ai.checkInterruption();
		if(path.getLength()>0)
			return true;
		else
			return false;
	}
	public void printPath() throws StopRequestException
	{
		ai.checkInterruption();
		System.out.println(path.getTiles().toString());
	}
	
	private boolean estCaseSure(int line, int col) throws StopRequestException
	{
		
		ai.checkInterruption();
		boolean tempresult =true;
		DangerZone dZone=new DangerZone(zone, ai);
		if( dZone.getEnum(line, col)!=ZoneEnum.FEU && dZone.getEnum(line, col)!=ZoneEnum.FLAMMES && dZone.getEnum(line, col)!=ZoneEnum.BOMBE)
			tempresult=false;
		dZone=null;
		return tempresult;
	}
	
	
}
