package org.totalboumboum.ai.v200910.ais.findiksirin.v5c;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Ali Fındık
 * @author Göknur Şırın
 */
@SuppressWarnings("deprecation")
public class BonusManager
{
	/**
	 * 
	 * @param ai
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public BonusManager(FindikSirin ai) throws StopRequestException
	{	ai.checkInterruption();
	
		this.ai = ai;
		zone = ai.getZone();
			
		// init destinations
		arrived = false;
		if(noBonusReachable())
		{
			possibleDest=bonusDestructions();
			OnBonusDestruction=true;
		}
		else
		{
			OnBonusDestruction=false;
			possibleDest=bonusDestinations();
		}
			updatePath();
	}
	
	
	/** creation de la liste des bonus
	 *  
	 * @return ?
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private List<AiTile> bonusDestinations() throws StopRequestException{
	ai.checkInterruption();
	List<AiTile> dest = new ArrayList<AiTile>();
	for(int line=0;line<zone.getHeight();line++)
	{	ai.checkInterruption();
		for(int col=0;col<zone.getWidth();col++)
		{	ai.checkInterruption();
			AiTile tile = zone.getTile(line,col);
			if(!tile.getItems().isEmpty() && ai.isSafe(tile))
			{
				dest.add(tile);
			}
		}
	}
	return dest;
	}
	
	/** si il n'y a pas un bonus Reachable sur la zone du jeu
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean noBonusReachable() throws StopRequestException{
		ai.checkInterruption();
		if(bonusDestinations().isEmpty())
			return true;
		else
		{
		AiPath shortestBonusPath = ai.astar.processShortestPath(zone.getOwnHero().getTile(), bonusDestinations());		
		return shortestBonusPath.isEmpty();
		}
	}
	
	/** si il n'y a pas un bonus sur la zone du jeu
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean noBonus() throws StopRequestException{
		ai.checkInterruption();
		return bonusDestinations().isEmpty();
	}
	/**
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean isBonusOver() throws StopRequestException{
		ai.checkInterruption();
		return bonusDestructions().isEmpty();
	}
	
	/** si la liste des bombes est vide, on cree une liste des murs pour destruire
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public List<AiTile> bonusDestructions() throws StopRequestException{
		ai.checkInterruption();
		List<AiTile> destructibles = new ArrayList<AiTile>();
		boolean isDest=false;
		AiTile tempTile=null;
			for(int line=0;line<zone.getHeight();line++)
			{	ai.checkInterruption();
				for(int col=0;col<zone.getWidth();col++)
				{	ai.checkInterruption();
					AiTile tile = zone.getTile(line,col);
					List <AiBlock> blocks = tile.getBlocks();
					Iterator <AiBlock> b= blocks.iterator();
					while(b.hasNext()){
						ai.checkInterruption();
						if(b.next().isDestructible()) isDest=true;
					}
					if(isDest)
					{
						List <AiTile> neighbors = tile.getNeighbors();
						Iterator <AiTile> i= neighbors.iterator();
						while(i.hasNext()){
							ai.checkInterruption();
							tempTile=i.next();
							if(tempTile.isCrossableBy(zone.getOwnHero()) && ai.isSafe(tempTile) ){
								ai.checkInterruption();
								destructibles.add(tempTile);
							}
						}
					}
					isDest=false;
				}
			}
		return destructibles;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//L'ALGORITHME ASTAR POUR CALCULER LE COUTS DES CHEMINS
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
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
				ai.costCalculator.setCost(line,col,cost);
			}
		}
	}	
//////////////////////////////////////////////////////////////////////////////////////////
	/** notre IA et la zone du jeu */
	private FindikSirin ai;
	/** */
	private AiZone zone;
	
	/** */
	private boolean OnBonusDestruction;

	/** si on est arrive a la destination */
	private boolean arrived;
	/** la case de destination choisit pour la fuite */
	private AiTile tileDest;
	/** destinations potentielles */
	private List<AiTile> possibleDest;
	/** le chemin qu'on va suivre */
	private AiPath path;

	/**
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean isOnBonusDestruction() throws StopRequestException{
		ai.checkInterruption();
		return OnBonusDestruction;
	}
	
	
	/** retourne si on est arrive ou pas
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
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
	}
	/** on met a jour le chemin qu'on a choisit 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void updatePath() throws StopRequestException
	{	ai.checkInterruption();		
		path = ai.astar.processShortestPath(ai.getCurrentTile(),possibleDest);
		tileDest = path.getLastTile();
	}
	
	/** si on est sur le chemin calculé 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void checkIsOnPath() throws StopRequestException
	{	ai.checkInterruption();
		
		AiTile currentTile = ai.getCurrentTile();
		while(!path.isEmpty() && path.getTile(0)!=currentTile)
		{	ai.checkInterruption();
			path.removeTile(0);
		}
	}
	
	/**
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean isPathSecure() throws StopRequestException{
		ai.checkInterruption();
		AiTile temp=null;
		boolean isSecure=true;
		if(path!=null){
			List <AiTile> l = path.getTiles();
			Iterator <AiTile> i = l.iterator();
			while(i.hasNext()){
				ai.checkInterruption();
				temp=i.next();
				if (!ai.isSafe(temp)) isSecure=false;
			}
		}
		return isSecure;
	}
	
	
	/** si le chemin est encore sure
	 * 
	 * @return ?
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
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
	/** 
	 * le process de decision
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Direction update() throws StopRequestException
	{	ai.checkInterruption();
		// on met a jour la matrice de cout
		updateCostCalculator();
		Direction result = Direction.NONE;
		if(!hasArrived())
		{	// on verifie si on est arrivé
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
}
