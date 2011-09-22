package org.totalboumboum.ai.v200809.ais.gunalpyurtsever.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v200809.adapter.AiActionName;
import org.totalboumboum.ai.v200809.adapter.AiBlock;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiFire;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Ozan Günalp
 * @author Sinan Yürtsever
 *
 */
public class GunalpYurtsever extends ArtificialIntelligence
{
	
	private List<AiTile> getClearNeighbors(AiTile head, boolean fire) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// liste des cases autour de la case de référence
		Collection<AiTile> neighbors = getPercepts().getNeighborTiles(head);
		// on garde les cases sans bloc ni bombe ni feu
		List<AiTile> result = new ArrayList<AiTile>();
		Iterator<AiTile> it = neighbors.iterator();
		while(it.hasNext())
		{	checkInterruption(); //APPEL OBLIGATOIRE
		
			AiTile t = it.next();
			if(fire){
				if(isAlsoFireClear(t))
					result.add(t);
			}
			else
				if(isClear(t))
					result.add(t);
		}
		return result;
	}
	
	private boolean isAlsoFireClear(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result;
		AiBlock block = tile.getBlock();
		Collection<AiBomb> bombs = tile.getBombs();
		Collection<AiFire> fires = tile.getFires();
		Collection<AiHero> heroes = tile.getHeroes();
		if(heroes.contains(getPercepts().getOwnHero()))
			result = (block==null && bombs.size()==0 && fires.size()==0 && heroes.size()==1);
		else
			result = (block==null && bombs.size()==0 && fires.size()==0 && heroes.size()==0);
		return result;
	}
	
	private boolean isClear(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result;
		AiBlock block = tile.getBlock();
		Collection<AiBomb> bombs = tile.getBombs();
		Collection<AiHero> heroes = tile.getHeroes();
		if(heroes.contains(getPercepts().getOwnHero()))
			result = (block==null && bombs.size()==0 && heroes.size()==1);
		else
			result = (block==null && bombs.size()==0  && heroes.size()==0);
		return result;
	}
	
	private AiAction AstarAlgorithm(AiTile tileGone) throws StopRequestException{
		checkInterruption(); //APPEL OBLIGATOIRE
		
		AiAction currentAction;
		AiZone zone = getPercepts();
		AiHero ownHero = zone.getOwnHero();
		AstarComparator comparator = new AstarComparator();
		PriorityQueue<CostTile> queue = new PriorityQueue<CostTile>(1,comparator);
		Collection<AiTile> dangerousTiles = markDangerTiles();
		
		CostTile head =new CostTile(ownHero.getTile(),0,tileGone,Direction.NONE);
		head.setmarkVisited(true);
		List<AiTile> tiles = null;
		
		try {
			tiles = getClearNeighbors(head.getAiTile(),true);
		} catch (StopRequestException e) {
			
			e.printStackTrace();
			
		}

		while(!tiles.isEmpty()){
			
			checkInterruption(); //APPEL OBLIGATOIRE
			
			if(!dangerousTiles.contains(tiles.get(0))){
				
				queue.offer(new CostTile(tiles.get(0),1,tileGone,zone.getDirection(head.getAiTile(),tiles.get(0))));
				
			}
			tiles.remove(0);
			
		}
		checkInterruption(); //APPEL OBLIGATOIRE
		int iter = 2;
		while(!queue.isEmpty() && !queue.peek().getAiTile().equals(tileGone) && iter<200){
			checkInterruption(); //APPEL OBLIGATOIRE
			
			CostTile costTile = queue.poll();
			if(!costTile.getmarkVisited()){
				costTile.setmarkVisited(true);
				try {
				if(costTile.getCost()>2)
					tiles = getClearNeighbors(costTile.getAiTile(),false);
				else
					tiles = getClearNeighbors(costTile.getAiTile(),true);
				
				} catch (StopRequestException e) {
				
					e.printStackTrace();
				
				}
				while(!tiles.isEmpty()){
					checkInterruption(); //APPEL OBLIGATOIRE
					if(!dangerousTiles.contains(tiles.get(0))){
						
						CostTile myTile = new CostTile(tiles.get(0),costTile.getCost()+1,tileGone,costTile.getFirstDirection());
						boolean a = false;
						Iterator<CostTile> ab = queue.iterator();
						while(ab.hasNext()){
							checkInterruption(); // APPEL OBLIGATOIRE
							AiTile cTile = ab.next().getAiTile();
							if(cTile.equals(myTile.getAiTile()))
								a= true;
						}
						if(!a){
						queue.offer(myTile);
						}
					}
					tiles.remove(0);
				
				}
				iter++;
			}
		}
		
		if(queue.isEmpty() || iter>199){
			currentAction = new AiAction(AiActionName.NONE);
		}
		else
			currentAction = new AiAction(AiActionName.MOVE,queue.peek().getFirstDirection());
		
		
		//System.out.println("gidilen yn:"+currentAction.getDirection().toString());
		return currentAction;
	}
	
	private AiAction simpleAstarAlgorithm(AiTile tileGone) throws StopRequestException{
		checkInterruption(); //APPEL OBLIGATOIRE
		
		AiAction currentAction;
		AiZone zone = getPercepts();
		AiHero ownHero = zone.getOwnHero();
		AstarComparator comparator = new AstarComparator();
		PriorityQueue<CostTile> queue = new PriorityQueue<CostTile>(1,comparator);
		
		CostTile head =new CostTile(ownHero.getTile(),0,tileGone,Direction.NONE);
		head.setmarkVisited(true);
		List<AiTile> tiles = null;
		try {
			tiles = getClearNeighbors(head.getAiTile(),true);
		} catch (StopRequestException e) {
			
			e.printStackTrace();
			
		}

		while(!tiles.isEmpty()){
			checkInterruption(); //APPEL OBLIGATOIRE
			
			queue.offer(new CostTile(tiles.get(0),1,tileGone,zone.getDirection(head.getAiTile(),tiles.get(0))));
			tiles.remove(0);
		}
		checkInterruption(); //APPEL OBLIGATOIRE
		int iter = 2;
		while(!queue.isEmpty() && !queue.peek().getAiTile().equals(tileGone) && iter<500){
			checkInterruption(); //APPEL OBLIGATOIRE
			
			CostTile costTile = queue.poll();
			if(!costTile.getmarkVisited()){
				costTile.setmarkVisited(true);
				try {
					if(costTile.getCost()>1)
						tiles = getClearNeighbors(costTile.getAiTile(),false);
					else
						tiles = getClearNeighbors(costTile.getAiTile(),true);
				
				} catch (StopRequestException e) {
				
					e.printStackTrace();
				
				}
				while(!tiles.isEmpty()){
					checkInterruption(); //APPEL OBLIGATOIRE
					CostTile myTile = new CostTile(tiles.get(0),costTile.getCost()+1,tileGone,costTile.getFirstDirection());
					boolean a = false;
					Iterator<CostTile> ab = queue.iterator();
					while(ab.hasNext()){
						checkInterruption(); // APPEL OBLIGATOIRE
						AiTile cTile = ab.next().getAiTile();
						if(cTile.equals(myTile.getAiTile()))
							a= true;
					}
					if(!a){
						queue.offer(myTile);
					}
					tiles.remove(0);
				
				}
				iter++;
			}
		}
		
		if(queue.isEmpty() || iter>499)
			currentAction = new AiAction(AiActionName.NONE);
	
		else
			currentAction = new AiAction(AiActionName.MOVE,queue.peek().getFirstDirection());
		
			
		
		//System.out.println("gidilen yn:"+currentAction.getDirection().toString());
		return currentAction;
	}
	
	
	public AiTile TargetTileCalculator(AiHero targetHero) throws StopRequestException
	{
		checkInterruption(); // APPEL OBLIGATOIRE
		AiTile myNearestTile = null;
		int col = targetHero.getLine();
		int line = targetHero.getCol();
		boolean up=true;
		boolean right=false;
		if(getPercepts().getHeight()/2<col)
			up = false;
		if(getPercepts().getWidth()/2<line)	
		 right = true;
		int verDif = getPercepts().getWidth()-col-3;
		int horDif = getPercepts().getHeight()-line;
		if(up && right)
			if(line<verDif)
				if(this.isClear(targetHero.getTile().getNeighbor(Direction.DOWN)))
					myNearestTile = targetHero.getTile().getNeighbor(Direction.DOWN);
				else
					myNearestTile = targetHero.getTile().getNeighbor(Direction.DOWNLEFT);
			else
				if(this.isClear(targetHero.getTile().getNeighbor(Direction.LEFT)))
					myNearestTile = targetHero.getTile().getNeighbor(Direction.LEFT);
				else
					myNearestTile = targetHero.getTile().getNeighbor(Direction.DOWNLEFT);
		if(up && !right)
			if(line>col)
				if(this.isClear(targetHero.getTile().getNeighbor(Direction.DOWN)))
					myNearestTile = targetHero.getTile().getNeighbor(Direction.DOWN);
				else
					myNearestTile = targetHero.getTile().getNeighbor(Direction.DOWNRIGHT);
			else
				if(this.isClear(targetHero.getTile().getNeighbor(Direction.RIGHT)))
					myNearestTile = targetHero.getTile().getNeighbor(Direction.RIGHT);
				else
					myNearestTile = targetHero.getTile().getNeighbor(Direction.UPRIGHT);
			
		if(!up && right)
			if(horDif>verDif)
				if(this.isClear(targetHero.getTile().getNeighbor(Direction.UP)))
					myNearestTile = targetHero.getTile().getNeighbor(Direction.UP);
				else
					myNearestTile = targetHero.getTile().getNeighbor(Direction.UPLEFT);
			else
				if(this.isClear(targetHero.getTile().getNeighbor(Direction.LEFT)))
					myNearestTile = targetHero.getTile().getNeighbor(Direction.LEFT);
				else
					myNearestTile = targetHero.getTile().getNeighbor(Direction.UPLEFT);
		
		if(!up && !right)
			if(line>verDif)
				if(this.isClear(targetHero.getTile().getNeighbor(Direction.UP)))
					myNearestTile = targetHero.getTile().getNeighbor(Direction.UP);
				else
					myNearestTile = targetHero.getTile().getNeighbor(Direction.UPRIGHT);
			else
				if(this.isClear(targetHero.getTile().getNeighbor(Direction.RIGHT)))
					myNearestTile = targetHero.getTile().getNeighbor(Direction.RIGHT);
				else
					myNearestTile = targetHero.getTile().getNeighbor(Direction.UPRIGHT);
		
		//System.out.println("neresi:"+getPercepts().getDirection(targetHero.getTile(),myNearestTile));
		return myNearestTile;
		
	}
	
	
	public List<AiTile> markDangerTiles() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		
		List<AiTile> dangerTiles = new ArrayList<AiTile>();
		Collection<AiBomb> bombs = getPercepts().getBombs();
		if (bombs.size() > 0) {
			int range;
			Iterator<AiBomb> it=bombs.iterator();
			while(it.hasNext()){
				checkInterruption(); // APPEL OBLIGATOIRE
				
				AiBomb b=it.next();
				range = b.getRange();
				dangerTiles.add(b.getTile());
				Collection<AiTile> bombNeighbors = getPercepts().getNeighborTiles(b.getTile());
				for (AiTile t : bombNeighbors) {
					checkInterruption(); // APPEL OBLIGATOIRE
					
					int i = 0;
					Direction d = getPercepts().getDirection(b.getTile(),t);
					while (t.getBlock()==null && i < range) {
						checkInterruption(); // APPEL OBLIGATOIRE
						if(!dangerTiles.contains(t))
							dangerTiles.add(t);
						t = getPercepts().getNeighborTile(t, d);
						i++;
					}
				}
			}
		}
		return dangerTiles;
	}
	
	
	
	public AiTile ultimateRunaway(AiHero targetedHero) throws StopRequestException{
		
		checkInterruption(); // APPEL OBLIGATOIRE

		Collection<AiTile> newones = markDangerTiles();


		Iterator<AiTile> newIterator = newones.iterator();
		NearestSafePlaceComparator myComparator= new NearestSafePlaceComparator();

		
		PriorityQueue<CostTile> nearestPriority;
		

			nearestPriority = new PriorityQueue<CostTile>(1,myComparator);

		while(newIterator.hasNext()){
			
			checkInterruption(); // APPEL OBLIGATOIRE
			
			AiTile checkingTile = newIterator.next();
			
			Collection<AiTile> surroundedTiles = getClearNeighbors(checkingTile,true);
			
			Iterator<AiTile> surroundedIterator = surroundedTiles.iterator();
			
			while(surroundedIterator.hasNext())
			{
				checkInterruption(); // APPEL OBLIGATOIRE
				
				AiTile controllingTile = surroundedIterator.next();
				
				if(!newones.contains(controllingTile) &&
					controllingTile.getBombs().size()== 0 &&
					!markDangerTiles().contains(controllingTile)&&
					isAlsoFireClear(controllingTile)){	
					CostTile myTile = new CostTile(controllingTile,(int)(Math.abs(controllingTile.getCol()-getPercepts().getOwnHero().getCol())+Math.abs(controllingTile.getLine()-getPercepts().getOwnHero().getLine())),targetedHero.getTile(),Direction.NONE);
					nearestPriority.add(myTile);
					//System.out.println(myTile.toString());
				}
			}
		
		}
		return nearestPriority.poll().getAiTile();
	}
	
	AiTile runawayPlace;
	
	public AiAction processAction() throws StopRequestException
	{	
		checkInterruption(); // APPEL OBLIGATOIRE
		//System.out.println("-----");
		//System.out.println("bomba sayisi:"+getPercepts().getBombs().size());
		AiAction resultat=new AiAction(AiActionName.NONE);
		AiZone zone = getPercepts();
		
		//System.out.println("h"+zone.getOwnHero().getTile().toString());
		
		Collection<AiTile> tilesinDanger = markDangerTiles();
		//System.out.println("tehlikeliler:"+tilesinDanger.toString());
		//System.out.println("tehlikede mi?:"+tilesinDanger.contains(zone.getOwnHero().getTile()));
		
		
		if(tilesinDanger.contains(zone.getOwnHero().getTile())){
			
			//System.out.println("aaa");
			Iterator<AiHero> it = zone.getHeroes().iterator();
			while(it.hasNext()){
				checkInterruption(); // APPEL OBLIGATOIRE
				AiHero t=it.next();
				if(!t.equals(zone.getOwnHero())){
						AiTile runTile = ultimateRunaway(t);
						//System.out.println("th:"+t.getTile().toString());
						runawayPlace = runTile;
				
						//System.out.println(runawayPlace.toString());
						resultat = simpleAstarAlgorithm(runawayPlace);
							
				}
			}
			if(resultat.getName() == AiActionName.MOVE){
				
				Collection<AiTile> sonkontrolTile = getClearNeighbors(zone.getOwnHero().getTile().getNeighbor(resultat.getDirection()),true);
				if(tilesinDanger.containsAll(sonkontrolTile) && zone.getOwnHero().getTile().getBombs().size() == 0 && sonkontrolTile.size() > 2)
						resultat = new AiAction(AiActionName.NONE);
				}
			return resultat;
			
		}
		
		else{
			Iterator<AiHero> it = zone.getHeroes().iterator();
			AiTile myTargetTile = null;
			AiTile targetHeroesTile = null;
			while(it.hasNext()){
				checkInterruption(); //APPEL OBLIGATOIRE
				AiHero t=it.next();
				if(!t.equals(zone.getOwnHero()))
					if(t.getTile().getBombs().size()==0){
						myTargetTile = this.TargetTileCalculator(t);
						targetHeroesTile = t.getTile();
					}
			}
			
			
			if(
					((targetHeroesTile.getCol() == zone.getOwnHero().getCol() &&
					zone.getOwnHero().getTile().getNeighbor(Direction.DOWN).getBlock()==null  &&
					zone.getOwnHero().getTile().getNeighbor(Direction.UP).getBlock()==null	
					) 
					|| 
					(targetHeroesTile.getLine() == zone.getOwnHero().getLine() && 
					zone.getOwnHero().getTile().getNeighbor(Direction.LEFT).getBlock()==null &&
					zone.getOwnHero().getTile().getNeighbor(Direction.RIGHT).getBlock()==null 
					))
					
				){
				
				//System.out.println("bomba");
				resultat = new AiAction(AiActionName.DROP_BOMB);
			}
			else{	
			
				if(!(myTargetTile==null))
					if(zone.getOwnHero().getTile().equals(myTargetTile)){
						resultat = new AiAction(AiActionName.NONE);
						//System.out.println("bomba");
					}
					else{
						resultat = AstarAlgorithm(myTargetTile);
					}
				else{
					if(zone.getOwnHero().getTile().getBombs().size()!=0){
						resultat = AstarAlgorithm(targetHeroesTile);
					}
				}
			}

			
			return resultat;
			
		}
	}
}

