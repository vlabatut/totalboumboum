package org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.NoHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
import org.totalboumboum.ai.v201112.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 * @author Furkan Arık
 * @author Aksel Köseoğlu
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<ArikKoseoglu>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected MoveHandler(ArikKoseoglu ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		
	}

	/////////////////////////////////////////////////////////////////
		// PROCESSING				/////////////////////////////////////
		/////////////////////////////////////////////////////////////////
	@Override
	protected Direction considerMoving() throws StopRequestException
	{	ai.checkInterruption();
		
		// initialize elements
		AiHero ownHero = ai.getZone().getOwnHero();
		NoHeuristicCalculator heuristic = new NoHeuristicCalculator(ai);
		TileCostCalculator cost= new TileCostCalculator(ai);
		BasicSuccessorCalculator successor = new BasicSuccessorCalculator(ai);
		
		
		//on prend tile qui a valeur max
		Collection<List<AiTile>> asd= ai.getUtilityHandler().getUtilitiesByValue().values();
		Iterator<List<AiTile>> itr = asd.iterator();
		HashMap<AiTile, Float> liste = ai.getUtilityHandler().getUtilitiesByTile();
		AiTile hedef = null;
		
		while(itr.hasNext()){
			ai.checkInterruption();
			List<AiTile> hedefler = itr.next();
			int sizeList = hedefler.size();
			for(int i = 0 ; i<sizeList;i++ ){
				ai.checkInterruption();
				if((hedef==null || liste.get(hedef)> liste.get(hedefler.get(i))) && !ownHero.getTile().equals(hedefler.get(i))){
					hedef = hedefler.get(i);
				}
			}
			
		}
		
		if( liste.get(currentDestination) != null){
			if(liste.get(currentDestination).equals(liste.get(hedef))){
				hedef = currentDestination;
			}
		}
		
		if(hedef != null){
			if(hedef.equals(ownHero.getTile())){
				this.currentPath = null;
				this.currentDirection = Direction.NONE;
				
			}
			else{
				Astar astar =new Astar(ai, ownHero, cost, heuristic, successor);
				AiLocation heroLoc= new AiLocation(ownHero);
				try {
					this.currentPath = astar.processShortestPath(heroLoc,hedef);
					if(!possibleDePasser(ai.getZone().getOwnHero(), this.currentPath) && (this.inDangeraous(heroLoc,ai.getZone())==null)){
						this.currentDirection = Direction.NONE;
						return this.currentDirection;
					}
				} catch (LimitReachedException e) {
					//e.printStackTrace();
				}
				this.currentDestination = hedef;
				if(this.currentPath != null){
					if(this.currentPath.getLocations().size()>0){
						this.currentDirection = this.calculateDirection(this.currentPath.getLocation(1));
					}	
				}
				else{
					this.currentDirection = Direction.NONE;
				}
			}
		}
		else{
			this.currentDirection = Direction.NONE;
		}
		return this.currentDirection;
	}
	
	/**
	 *Calculate direciton 
	 * @param loc 
	 * 		description manquante !
	 * @return 
	 * 		description manquante !
	 * @throws StopRequestException 
	 * 		description manquante !
	 */
	private Direction calculateDirection(AiLocation loc) throws StopRequestException{
		ai.checkInterruption();
		AiTile ownHero = ai.getZone().getOwnHero().getTile();
		double ownPosX = ownHero.getPosX()
				, ownPosY = ownHero.getPosY()
				, nextPosX = loc.getPosX()
				, nextPosY = loc.getPosY();
		if(nextPosX == ownPosX && nextPosY > ownPosY){
			return Direction.DOWN;
		}
		else if(nextPosX == ownPosX && nextPosY < ownPosY){
			return Direction.UP;
		}
		else if(nextPosX > ownPosX && nextPosY > ownPosY){
			return Direction.DOWNRIGHT;
		}
		else if(nextPosX > ownPosX && nextPosY < ownPosY){
			return Direction.UPRIGHT;
		}
		else if(nextPosX > ownPosX && nextPosY == ownPosY){
			return Direction.RIGHT;
		}
		else if(nextPosX < ownPosX && nextPosY > ownPosY){
			return Direction.DOWNLEFT;
		}
		else if(nextPosX < ownPosX && nextPosY < ownPosY){
			return Direction.UPLEFT;
		}
		else if(nextPosX < ownPosX && nextPosY == ownPosY){
			return Direction.LEFT;
		}
		return Direction.NONE;
	}
	
	/**
	 * Calculate possibilite de passer tile precedent 
	 * @param hero 
	 * 		description manquante !
	 * @param path 
	 * 		description manquante !
	 * @return 
	 * 		description manquante !
	 * @throws StopRequestException 
	 * 		description manquante !
	 */
	private boolean possibleDePasser(AiHero hero,AiPath path) throws StopRequestException{
		ai.checkInterruption();
		
		List<AiLocation> locations = path.getLocations();
		if(locations != null){
			AiLocation heroLoc = new AiLocation(hero);
			int possitionHero = locations.indexOf(heroLoc);
			
			if(locations.size()> possitionHero){
				AiLocation nextLocation = locations.get(possitionHero+1);

				AiBomb bomb = inDangeraous(nextLocation, ai.getZone());

				if(bomb!=null){
					int timePass =(int)path.getDuration(hero),//(gameArea.getTileDistance(hero.getTile(), this.currentDestination) * (int)hero.getCurrentSpeed()),
							timeBomb = (int)(bomb.getNormalDuration() - bomb.getTime());
					if((timePass)*3 > timeBomb){ //pour consider time for calculation
						return false;
					}
				}
				else{
					return true;
				}
			}
		}
		
		return true;
	} 
	
	/**
	 * Calculate possibilite de passer tile precedent 
	 * @param next 
	 * 		description manquante !
	 * @param gameArea 
	 * 		description manquante !
	 * @return 
	 * 		description manquante !
	 * @throws StopRequestException 
	 * 		description manquante !
	 */
	private AiBomb inDangeraous(AiLocation next, AiZone gameArea) throws StopRequestException{
		ai.checkInterruption();
		AiTile ownTile = next.getTile();
		List<AiBomb> bombs = gameArea.getBombs();
		for(int i = 0 ; i< bombs.size(); i++){
			ai.checkInterruption();
			List<AiTile> scope = bombs.get(i).getBlast();
			if(scope.contains(ownTile)){
				return bombs.get(i);
			}
		}
		return null;
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
	}
}
