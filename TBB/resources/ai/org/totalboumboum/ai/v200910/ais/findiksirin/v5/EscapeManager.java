package org.totalboumboum.ai.v200910.ais.findiksirin.v5;

import java.util.Iterator;
import java.util.List;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

public class EscapeManager
{
	public EscapeManager(FindikSirin ai) throws StopRequestException
	{	ai.checkInterruption();
	
		this.ai = ai;
		zone = ai.getZone();
			
		// init A*
		double costMatrix[][] = new double[zone.getHeight()][zone.getWidth()];
		costCalculator = new MatrixCostCalculator(costMatrix);
		heuristicCalculator = new BasicHeuristicCalculator();
		astar = new Astar(ai,ai.getOwnHero(),costCalculator,heuristicCalculator);
		
		// init destinations
		arrived = false;
		AiHero ownHero = ai.getOwnHero();
		possibleDest = ai.getSafetyManager().findSafeTiles(ownHero.getTile());
		updatePath();
	}
	
	
/////////////////////////////////////////////////////////////////////////////////////////
//L'ALGORITHME ASTAR POUR CALCULER LE COUTS DES CHEMINS
	/** classe implémentant l'algorithme A* */
	private Astar astar;
	/** classe implémentant la fonction heuristique */
	private HeuristicCalculator heuristicCalculator;
	/** classe implémentant la fonction de co�t */
	private MatrixCostCalculator costCalculator;
	
	private void updateCostCalculator() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		// calcul de la matrice de co�t : on prend l'oppos� du niveau de s�ret�
		// i.e. : plus le temps avant l'explosion est long, plus le co�t est faible 
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
	// la case de destination choisit pour la fuite
	private AiTile tileDest;
	// destinations potentielles
	private List<AiTile> possibleDest;
	//le chemin qu'on va suivre
	private AiPath path;

	
	// retourne si on est arrive ou pas
	public boolean hasArrived() throws StopRequestException
	{	ai.checkInterruption();
		if(!arrived)
		{	if(tileDest==null)
				arrived = true;
			else
			{	AiTile currentTile = ai.getCurrentTile();
				arrived = currentTile==tileDest;			
			}
		}
		return arrived;
	}
	//on met a jour le chemin qu'on a choisit
	private void updatePath() throws StopRequestException
	{	ai.checkInterruption();		
		path = astar.processShortestPath(ai.getCurrentTile(),possibleDest);
		tileDest = path.getLastTile();
	}
	
	//si on est sur le chemin calculé
	private void checkIsOnPath() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		AiTile currentTile = ai.getCurrentTile();
		while(!path.isEmpty() && path.getTile(0)!=currentTile)
		{	ai.checkInterruption();
			path.removeTile(0);
		}
	}
	
//si le chemin est encore sure
	private boolean checkPathValidity() throws StopRequestException
	{	ai.checkInterruption();	
		boolean result = true;
		Iterator<AiTile> it = path.getTiles().iterator();
		while(it.hasNext() && result)
		{	ai.checkInterruption();
			AiTile tile = it.next();
			result = tile.isCrossableBy(ai.getOwnHero());			
		}
		return result;
	}	
//le process de decision
	public Direction update() throws StopRequestException
	{	ai.checkInterruption();
		// on met a jour la matrice de cout
		updateCostCalculator();
		Direction result = Direction.NONE;
		if(!hasArrived())
		{	// on verifie si on est arriv�
			checkIsOnPath();
			// si le chemin n'est pas vide si il est valide on reprend
			if(path.isEmpty() || !checkPathValidity())
				updatePath();
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
	/*
	private List <AiTile> GetDangerousZone(AiTile tile) throws StopRequestException{
		ai.checkInterruption();
		AiTile temp=tile;
		
		List <AiTile> zoneDangerous = new ArrayList<AiTile>();
		int bombRange=zone.getOwnHero().getBombRange();
		
		for(int i=0;i<bombRange;i++){
			ai.checkInterruption();
			temp=temp.getNeighbor(Direction.LEFT);
			zoneDangerous.add(temp);
		}
		temp=tile;
		for(int i=0;i<bombRange;i++){
			ai.checkInterruption();
			temp=temp.getNeighbor(Direction.RIGHT);
			zoneDangerous.add(temp);
		}
		temp=tile;
		for(int i=0;i<bombRange;i++){
			ai.checkInterruption();
			temp=temp.getNeighbor(Direction.UP);
			zoneDangerous.add(temp);
		}
		temp=tile;
		for(int i=0;i<bombRange;i++){
			ai.checkInterruption();
			temp=temp.getNeighbor(Direction.DOWN);
			zoneDangerous.add(temp);
		}
		return zoneDangerous;
	}
	*/
	/*
	public boolean hasEnoughSpace() throws StopRequestException{
		ai.checkInterruption();
		boolean result=true;
		AiTile currentTile=zone.getOwnHero().getTile();
		List<AiTile> dangerousZone=GetDangerousZone(currentTile);
		List <AiTile> calculatedPath=path.getTiles();
		Iterator <AiTile> i = dangerousZone.iterator();	
		while(i.hasNext()){
			if( calculatedPath.contains(i.next()) ){
				result =false;
			}
		}
	return result;
	}
	*/
}
