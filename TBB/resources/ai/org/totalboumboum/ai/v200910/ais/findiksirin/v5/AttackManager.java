package org.totalboumboum.ai.v200910.ais.findiksirin.v5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Ali Fındık
 * @author Göknur Şırın
 */
@SuppressWarnings("deprecation")
public class AttackManager
{
	public AttackManager(FindikSirin ai,Direction posTarget, Direction posWalkable) throws StopRequestException
	{	ai.checkInterruption();
	
		this.ai = ai;
		zone = ai.getZone();
			
		// initialisation de A*
		double costMatrix[][] = new double[zone.getHeight()][zone.getWidth()];
		costCalculator = new MatrixCostCalculator(costMatrix);
		heuristicCalculator = new BasicHeuristicCalculator();
		astar = new Astar(ai,ai.getOwnHero(),costCalculator,heuristicCalculator);
		
		// init destinations
		arrived = false;
			setBombingPath(posTarget,posWalkable);
	}
	
	
	private List<AiTile> heroDestinations() throws StopRequestException{
	ai.checkInterruption();
	List<AiTile> dest = new ArrayList<AiTile>();
	for(int line=0;line<zone.getHeight();line++)
	{	ai.checkInterruption();
		for(int col=0;col<zone.getWidth();col++)
		{	ai.checkInterruption();
			AiTile tile = zone.getTile(line,col);
			if(!tile.getHeroes().isEmpty())
			{
				dest.add(tile);
			}
		}
	}
	return dest;
	}
	
	
/////////////////////////////////////////////////////////////////////////////////////////
//L'ALGORITHME ASTAR POUR CALCULER LE COUTS DES CHEMINS
	/** classe implémentant l'algorithme A* */
	private Astar astar;
	/** classe implémentant la fonction heuristique */
	private HeuristicCalculator heuristicCalculator;
	/** classe implémentant la fonction de coût */
	private MatrixCostCalculator costCalculator;
	
	private void updateCostCalculator() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		// calcul de la matrice de coût : on prend l'opposé du niveau de sûreté
		// i.e. : plus le temps avant l'explosion est long, plus le coût est faible 
		double safetyMatrix[][] = ai.getSafetyManager().getMatrix();
		for(int line=0;line<zone.getHeight();line++)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			for(int col=0;col<zone.getWidth();col++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				double cost = -safetyMatrix[line][col];
				costCalculator.setCost(line,col,cost);
			}
		}
	}	
//////////////////////////////////////////////////////////////////////////////////////////
	//notre IA et la zone du jeu
	private FindikSirin ai;
	private AiZone zone;


	//si on est arrive a la destination
	private boolean arrived;

	private AiTile tileDest;
	// destinations potentielles
	//private List<AiTile> possibleDest;
	//le chemin qu'on va suivre
	private AiPath path;


	
	public boolean decidedToBomb() throws StopRequestException{
		ai.checkInterruption();
		boolean result=false;
		List <AiTile> heroPos= heroDestinations();
		Iterator <AiTile> h= heroPos.iterator();
		AiTile currentTile=zone.getOwnHero().getTile();
		while(h.hasNext()){
			ai.checkInterruption();
			if( isOnLine(currentTile,h.next()) ){
				result=true;
			}
		}
		
		return result;
	}
	
	public boolean isOnLine(AiTile tile1, AiTile tile2) throws StopRequestException{
		ai.checkInterruption();
		boolean result=false;
		if( (( tile1.getLine()==tile2.getLine() || tile1.getCol()==tile2.getCol()) && !( tile1.getLine()==tile2.getLine() && tile1.getCol()==tile2.getCol() )  )  ){
			Direction dir=zone.getDirection(tile1, tile2);
			
			AiTile temp=tile1.getNeighbor(dir);
			if(temp!=null){
				while(!(temp.getLine()==tile2.getLine() && temp.getCol()== tile2.getCol())){
					ai.checkInterruption();
					if( temp.getBlocks().isEmpty() && temp.getBombs().isEmpty() ){
						temp=temp.getNeighbor(dir);
					}
				}
				if(temp.getNeighbor(dir).getLine()== tile2.getLine() && temp.getCol()== tile2.getCol()){
					result=true;
				}
				
				else result=false;
			}
		}
		
		else if( tile1.getLine()==tile2.getLine() && tile1.getCol()==tile2.getCol() )
		{
			result= true;
		}

		return result;
	}
	
	
	// retourne si on est arrive ou pas
	public boolean hasArrived() throws StopRequestException
	{	ai.checkInterruption();
		if(!arrived)
		{	if(tileDest==null)
			{
				arrived = true;
			}
		
			else
			{	AiTile currentTile = ai.getCurrentTile();
				arrived = currentTile==tileDest;			
			}
		}
		return arrived;
	}/*
	//on met a jour le chemin qu'on a choisit
	private void updatePath() throws StopRequestException
	{	ai.checkInterruption();		
		path = astar.processShortestPath(ai.getCurrentTile(),possibleDest);
		tileDest = path.getLastTile();
	}
	*/
	//si on est sur le chemin calculé
	private void checkIsOnPath() throws StopRequestException
	{	ai.checkInterruption();
		
		AiTile currentTile = ai.getCurrentTile();
		while(!path.isEmpty() && path.getTile(0)!=currentTile)
		{	ai.checkInterruption();
			path.removeTile(0);
		}
	}
	
	/*
//si le chemin est encore sure
	private boolean checkPathValidity() throws StopRequestException
	{	ai.checkInterruption();	
		boolean result = true;
		Iterator<AiTile> it = path.getTiles().iterator();
		while(it.hasNext() && result)
		{	ai.checkInterruption();
			AiTile tile = it.next();
			//result = tile.isCrossableBy(ai.getOwnHero()) && (ai.getSafetyManager().isSafe(tile));			
			result = tile.isCrossableBy(ai.getOwnHero());
		}
		return result;
	}	*/
//le process de decision
	public Direction update() throws StopRequestException
	{	ai.checkInterruption();
		// on met a jour la matrice de cout
		updateCostCalculator();
		Direction result = Direction.NONE;
		if(!hasArrived())
		{	// on verifie si on est arrivé
			checkIsOnPath();
			// si le chemin n'est pas vide si il est valide on reprend
			//if(path.isEmpty() || !checkPathValidity())
				//updatePath();
			// s'il reste deux cases au moins dans le chemin, on se dirige vers la suivante
			AiTile tile = null;
			if(path.getLength()>1)
				tile = path.getTile(1);
			// sinon, s'il ne reste qu'une seule case, on y va
			else if(path.getLength()>0)
				tile = path.getTile(0);
			// on determine la direction prochaine
			if(tile!=null)
				result = zone.getDirection(ai.getOwnHero(),tile);			
		}	
		return result;
	}
	

	public AiTile bomb1Pos=null;
	public AiTile bomb2Pos=null;
	public AiTile bomb3Pos=null;

	private void setBombingPath(Direction posTarget, Direction posWalkable) throws StopRequestException{
		ai.checkInterruption();
		AiTile currentTile=ai.getCurrentTile();
		AiTile firstStop=currentTile.getNeighbor(posWalkable).getNeighbor(posWalkable);
		AiTile secondStop=firstStop.getNeighbor(posTarget).getNeighbor(posTarget);
		bomb1Pos=currentTile;		
		bomb2Pos=firstStop;
		bomb3Pos=secondStop;
		path = astar.processShortestPath(currentTile,bomb2Pos);
		path.addTile(firstStop.getNeighbor(posTarget));
		path.addTile(secondStop);		
	}
}
