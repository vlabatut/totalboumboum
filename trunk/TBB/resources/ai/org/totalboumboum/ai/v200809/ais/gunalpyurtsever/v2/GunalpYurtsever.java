package org.totalboumboum.ai.v200809.ais.gunalpyurtsever.v2;

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
import org.totalboumboum.ai.v200809.adapter.AiItem;
import org.totalboumboum.ai.v200809.adapter.AiStateName;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Ozan Gunalp
 * @author Sinan Yurtsever
 *
 */
public class GunalpYurtsever extends ArtificialIntelligence
{

	private int strategy;
	// liste des bombes
	private List<AiBomb> bombList;
	// Definit pour garder la cost pour acceder a cible( modifié a chaque invocation de Astar 
	private int cost;
	
	
	/**
	 * Mise a jour la liste des bombes
	 * @throws StopRequestException
	 */
	private void updateBombList()  throws StopRequestException{
		checkInterruption(); //APPEL OBLIGATOIRE
		//Iterator sur la liste des bombes
		Iterator<AiBomb> it=bombList.iterator();
		while(it.hasNext()){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiBomb bomb=it.next();
			//Supprime de la liste la bomb qui est null ou deja brulé
			if(bomb != null){
				if(bomb.getState().getName() == AiStateName.BURNING)
					it.remove();
			}
			else
				it.remove();
		}
		// Ajoute les bombes qui ont juste apparus
		for(AiBomb bomb:getPercepts().getBombs()){
			checkInterruption(); //APPEL OBLIGATOIRE
			if(!bombList.contains(bomb))
				bombList.add(bomb);
		}
	}
	
	/**
	 * Prend les neighbors de Tile qui sont traversable
	 * Avec un option qui rende le feu comme traversable ou contraire
	 * @param head
	 * @param fire
	 * @return
	 * @throws StopRequestException
	 */
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
	
	/**
	 * Utilise dans la fonction getClearNeighbors
	 * Pour prendre pas le feu comme traversable
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
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
	
	/**
	 * 
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
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
	
	/**
	 * Prend comme parametre la Tile et trace la route entre cette destination 
	 * et la Tile de OwnHero
	 * 
	 * Return la prochaine action MOVE avec la direction et on modifie le champs "cost"
	 * Return action NONE si OwnHero est sur la Tile cible 
	 * ou bien OwnHero ne peut pas approcher la cible
	 * 
	 * @param tileGone
	 * @return
	 * @throws StopRequestException
	 */
	private AiAction AstarAlgorithm(AiTile tileGone) throws StopRequestException{
		checkInterruption(); //APPEL OBLIGATOIRE
		
		AiAction currentAction;
		
		AiZone zone = getPercepts();
		
		AiHero ownHero = zone.getOwnHero();
		
		AstarComparator comparator = new AstarComparator();
		
		PriorityQueue<CostTile> queue = new PriorityQueue<CostTile>(1,comparator);
		
		Collection<AiTile> dangerousTiles = markDangerTiles();
		
		
		//si la tile cible est la meme avec l'un de ownhero
		if(tileGone.equals(ownHero.getTile())){
			
			cost = 0;
			return new AiAction(AiActionName.NONE);
			
		}
		
		//on initialise avec la tile qu'on est sur
		CostTile head =new CostTile(ownHero.getTile(),0,tileGone,Direction.NONE);
		
		head.setmarkVisited(true);
		
		List<AiTile> tiles = null;
		
		tiles = getClearNeighbors(head.getAiTile(),true);
		
		// on commence en developpant le quatre direction
		while(!tiles.isEmpty()){
			
			checkInterruption(); //APPEL OBLIGATOIRE
			
			if(!dangerousTiles.contains(tiles.get(0))){
				
				queue.offer(new CostTile(tiles.get(0),1,tileGone,zone.getDirection(head.getAiTile(),tiles.get(0))));
				
			}
			tiles.remove(0);
			
		}
		
		int iter = 2;
		//cette iteration est pour le developpement
		//si on trouve un rue ou bien si on ne trouve rien ou si on developpe innecessairement on arrete le loop
		while(!queue.isEmpty() && !queue.peek().getAiTile().equals(tileGone) && iter<200)
		{
			checkInterruption(); //APPEL OBLIGATOIRE
			
			//tile developpe
			CostTile costTile = queue.poll();
			
			if(!costTile.getmarkVisited()){
				
				costTile.setmarkVisited(true);
				
				//on developpe les tiles encadrant
				//on cherche meme en traversant dans le tile brulent parce qu'il va s'eteindre jusqu'on traverse
				if(costTile.getCost()>2)
					tiles = getClearNeighbors(costTile.getAiTile(),false);
				else
					tiles = getClearNeighbors(costTile.getAiTile(),true);
				
				
				while(!tiles.isEmpty()){
					
					checkInterruption(); //APPEL OBLIGATOIRE
				
					if(!dangerousTiles.contains(tiles.get(0))){
						
						//on cree des costtiles pour les developper
						CostTile myTile = new CostTile(tiles.get(0),costTile.getCost()+1,tileGone,costTile.getFirstDirection());
						
						boolean a = false;
						
						Iterator<CostTile> ab = queue.iterator();
						
						//on cherche si mytile est deja enfilés
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
					//la selection pour developpement est terminé
					tiles.remove(0);
				
				}
				iter++;
			}
		}
		//si on itere excesivement
		if(queue.isEmpty() || iter>199){
			currentAction = new AiAction(AiActionName.NONE);
		}
		else{
			//on prend la direction
			currentAction = new AiAction(AiActionName.MOVE,queue.peek().getFirstDirection());
			cost = queue.peek().getCost();
		}
		return currentAction;
	}
	
	/**
	 * Meme avec AstarAlgorithm mais pour avancer dans les tiles en danger pour s'enfuir
	 * @param tileGone
	 * @return
	 * @throws StopRequestException
	 */
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
			
		
		return currentAction;
	}
	
	
	/**
	 * Fonction obsolete pour approcher au hero cible
	 * @param heroTile
	 * @return
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private AiTile TargetTileCalculator(AiTile heroTile) throws StopRequestException
	{
		checkInterruption(); // APPEL OBLIGATOIRE
		AiTile myNearestTile = null;
		int col = heroTile.getLine();
		int line = heroTile.getCol();
		boolean up=true;
		boolean right=false;
		if(getPercepts().getHeight()/2<col)
			up = false;
		if(getPercepts().getWidth()/2<line)	
		 right = true;
		int verDif = getPercepts().getWidth()-col;
		int horDif = getPercepts().getHeight()-line;
		if(up && right)
			if(line<verDif)
				if(this.isClear(heroTile.getNeighbor(Direction.DOWN)))
					myNearestTile = heroTile.getNeighbor(Direction.DOWN);
				else
					if(this.isClear(heroTile.getNeighbor(Direction.LEFT)))
						myNearestTile = heroTile.getNeighbor(Direction.LEFT);
					else
						myNearestTile = heroTile.getNeighbor(Direction.DOWNLEFT);
			else
				if(this.isClear(heroTile.getNeighbor(Direction.LEFT)))
					myNearestTile = heroTile.getNeighbor(Direction.LEFT);
				else
					if(this.isClear(heroTile.getNeighbor(Direction.DOWN)))
						myNearestTile = heroTile.getNeighbor(Direction.DOWN);
					else
						myNearestTile = heroTile.getNeighbor(Direction.DOWNLEFT);
		
		if(up && !right)
			if(line>col)
				if(this.isClear(heroTile.getNeighbor(Direction.DOWN)))
					myNearestTile = heroTile.getNeighbor(Direction.DOWN);
				else
					if(this.isClear(heroTile.getNeighbor(Direction.RIGHT)))
						myNearestTile = heroTile.getNeighbor(Direction.RIGHT);
					else
						myNearestTile = heroTile.getNeighbor(Direction.DOWNRIGHT);
			else
				if(this.isClear(heroTile.getNeighbor(Direction.RIGHT)))
					myNearestTile = heroTile.getNeighbor(Direction.RIGHT);
				else
					if(this.isClear(heroTile.getNeighbor(Direction.DOWN)))
						myNearestTile = heroTile.getNeighbor(Direction.DOWN);
					else
						myNearestTile = heroTile.getNeighbor(Direction.DOWNRIGHT);
			
		if(!up && right)
			if(horDif>verDif)
				if(this.isClear(heroTile.getNeighbor(Direction.UP)))
					myNearestTile = heroTile.getNeighbor(Direction.UP);
				else
					if(this.isClear(heroTile.getNeighbor(Direction.LEFT)))
						myNearestTile = heroTile.getNeighbor(Direction.LEFT);
					else
						myNearestTile = heroTile.getNeighbor(Direction.UPLEFT);
			else
				if(this.isClear(heroTile.getNeighbor(Direction.LEFT)))
					myNearestTile = heroTile.getNeighbor(Direction.LEFT);
				else
					if(this.isClear(heroTile.getNeighbor(Direction.UP)))
						myNearestTile = heroTile.getNeighbor(Direction.UP);
					else
						myNearestTile = heroTile.getNeighbor(Direction.UPLEFT);
		
		if(!up && !right)
			if(line>verDif)
				if(this.isClear(heroTile.getNeighbor(Direction.UP)))
					myNearestTile = heroTile.getNeighbor(Direction.UP);
				else
					if(this.isClear(heroTile.getNeighbor(Direction.RIGHT)))
						myNearestTile = heroTile.getNeighbor(Direction.RIGHT);
					else
						myNearestTile = heroTile.getNeighbor(Direction.UPRIGHT);
			else
				if(this.isClear(heroTile.getNeighbor(Direction.RIGHT)))
					myNearestTile = heroTile.getNeighbor(Direction.RIGHT);
				else
					if(this.isClear(heroTile.getNeighbor(Direction.UP)))
						myNearestTile = heroTile.getNeighbor(Direction.UP);
					else
						myNearestTile = heroTile.getNeighbor(Direction.UPRIGHT);
		
		
		return myNearestTile;
		
	}
	
	/**
	 * fonction qui supersede TargetTileCalculator pour approcher au hero cible
	 * 
	 * @param heroTile
	 * @return
	 * @throws StopRequestException
	 */
	private AiTile TargetTileCalculator2(AiTile heroTile) throws StopRequestException
	{
		checkInterruption();//APPEL OBLIGATOIRE;
		AiZone zone = getPercepts();
		AiTile returningTile = heroTile;
		Collection<AiTile> tiles = getClearNeighbors(heroTile, true);
		//on prend un check plus que possible pour comparer
		int checkcost = 1000;
		//on choisit un des tiles adjacentes pour approcher
		for(AiTile t : tiles){
			checkInterruption();//APPEL OBLIGATOIRE;
			AiAction res = AstarAlgorithm(t);
			// si la tile nest pas inapprochable ou bien nest pas la tile qu'on trouve sur on developpe le tile pour choisir
			if(res.getName() != AiActionName.NONE || zone.getOwnHero().getTile().equals(t)){
				if(cost<checkcost){
					checkcost = cost;
					returningTile = t;
					}
				}
			}
		
		return returningTile;
			
	}	

	/**
	 * Fonction pour garder les tiles en danger
	 * @return dangertiles
	 * @throws StopRequestException
	 */
	private List<AiTile> markDangerTiles() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		
		List<AiTile> dangerTiles = new ArrayList<AiTile>();
		Collection<AiBomb> bombs = getPercepts().getBombs();
		//s'il y a des bombs
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
					//on cherche pour chaque direction du bomb jusqu'a la portée
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
	
	
	/**
	 * pour trouver les tiles qui sont plus ou moins en danger a cause des bombs
	 * 
	 * @return
	 * @throws StopRequestException
	 */
	private List<DangerPriorityTile> markDangerTileswithPriority() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		
		List<DangerPriorityTile> dangerTiles = new ArrayList<DangerPriorityTile>();
		Collection<AiBomb> bombs = getPercepts().getBombs();
		if (bombs.size() > 0) {
			int range;
			Iterator<AiBomb> it=bombList.iterator();
			while(it.hasNext()){
				checkInterruption(); // APPEL OBLIGATOIRE
				
				AiBomb b=it.next();
				range = b.getRange();
				dangerTiles.add(new DangerPriorityTile(b.getTile(),bombList.indexOf(b)));
				Collection<AiTile> bombNeighbors = getPercepts().getNeighborTiles(b.getTile());
				for (AiTile t : bombNeighbors) {
					checkInterruption(); // APPEL OBLIGATOIRE
					
					int i = 0;
					Direction d = getPercepts().getDirection(b.getTile(),t);
					DangerPriorityTile theTile;
					while ((t.getBlock()==null)&& i < range) {
						checkInterruption(); // APPEL OBLIGATOIRE
					
						theTile = null;
						for(DangerPriorityTile dangerTile : dangerTiles){
							checkInterruption(); // APPEL OBLIGATOIRE
							if(dangerTile.getTile().equals(t))
								theTile = dangerTile;
						}
						if(!dangerTiles.contains(theTile))
							dangerTiles.add(new DangerPriorityTile(t,bombList.indexOf(b)));
						else
							if(theTile.getDangerpriority()>bombList.indexOf(b))
								theTile.setDangerpriority(bombList.indexOf(b));
								
						t = getPercepts().getNeighborTile(t, d);
						i++;
					}
				}
			}
		}
		return dangerTiles;
	}
	
	
	/**
	 * fonction obsolete pour s'enfuir de danger
	 * 
	 * @param targetedHero
	 * @return
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private AiTile ultimateRunaway(AiHero targetedHero) throws StopRequestException{
		
		checkInterruption(); // APPEL OBLIGATOIRE

		Collection<AiTile> newones = markDangerTiles();


		Iterator<AiTile> newIterator = newones.iterator();
		AstarComparator myComparator= new AstarComparator();

		
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
					isAlsoFireClear(controllingTile))
					{
					//
					CostTile myTile = new CostTile(controllingTile,(int)(Math.abs(controllingTile.getCol()-getPercepts().getOwnHero().getCol())+Math.abs(controllingTile.getLine()-getPercepts().getOwnHero().getLine())),getPercepts().getOwnHero().getTile(),Direction.NONE);
					nearestPriority.add(myTile);
					
				}
			}
		
		}
		return nearestPriority.poll().getAiTile();
	}

	
	/**
	 * fonction pour prendre la prorite dans un tile qui est danger
	 * 
	 * @param dangerTiles
	 * @param reqTile
	 * @return
	 */
	private int getpriorityValue(Collection<DangerPriorityTile> dangerTiles,AiTile reqTile){
		
	int priorityValue = -1;
	
	for(DangerPriorityTile dpTile: dangerTiles){
		
		
		if(reqTile.equals(dpTile.getTile())){
			
			priorityValue = dpTile.getDangerpriority();
		}
	}
			
	return priorityValue;
	}
	
	
	/**
	 * fonction qui supersede ultimateRunaway le hero s'enfuit regardant la place du hero cible
	 * 
	 * @param chosentokillTile
	 * @return
	 * @throws StopRequestException
	 */
	private AiTile ultimateRunaway2(AiTile chosentokillTile) throws StopRequestException{
		
		checkInterruption(); //APPEL OBLIGATOIRE
		
		//tile qu'on veut s'enfuir
		AiTile salvation = null;
		
		Collection<DangerPriorityTile> dangerTiles = markDangerTileswithPriority();
		
		DangerPriorityCostTileComparator myComparator= new DangerPriorityCostTileComparator(getPercepts().getWidth(),getPercepts().getHeight());

		PriorityQueue<DangerPriorityCostTile> developperList = new PriorityQueue<DangerPriorityCostTile>(1,myComparator);
		
        AiZone zone = getPercepts();
        
        // la tile qu'on trouve sur
        DangerPriorityCostTile headTile = new DangerPriorityCostTile(new CostTile(zone.getOwnHero().getTile(),0,chosentokillTile,Direction.NONE),getpriorityValue(dangerTiles,zone.getOwnHero().getTile()));
        
        developperList.add(headTile);
        
        //pour empecher les developpements en exces
        int dontgettoofar = 0;
        
        DangerPriorityCostTile currentTile;
        
        //pour les tiles qu'on a juste developpé
        AiTile cacheTile = null;
        
        boolean found = false;
        
        while(!found && dontgettoofar <100 && !developperList.isEmpty()){
        	
        	checkInterruption(); //APPEL OBLIGATOIRE
     
        	//tile qu'on developpe
        	currentTile = developperList.poll();
        	
        	
        	Collection<AiTile> neighTiles = getClearNeighbors(currentTile.getCostTile().getAiTile(),true);
        	
        	
        	if(cacheTile!= null)
        		neighTiles.remove(cacheTile);
        	
        	//choisir les nouvelles tiles pour developper
        	for(AiTile a: neighTiles){
        		checkInterruption(); //APPEL OBLIGATOIRE
        		
        		DangerPriorityCostTile newcomer = new DangerPriorityCostTile(new CostTile(a,currentTile.getCostTile().getCost()+1,chosentokillTile,Direction.NONE),getpriorityValue(dangerTiles,a));
        		
        		boolean listCheck = true;
        		
        		for(DangerPriorityCostTile t: developperList){
        			checkInterruption(); //APPEL OBLIGATOIRE
        			//verifie si la tile n'est pas deha dans la liste
        			if(t.getCostTile().getAiTile().equals(newcomer.getCostTile().getAiTile()))
        					listCheck = false;
        					
        		}
        		
        		if(listCheck){
        			
        			developperList.add(newcomer);
        		
        		}
        	}           	
        	
        	cacheTile = currentTile.getCostTile().getAiTile();
        	
        	dontgettoofar++;

    			
        	
        	if(!developperList.isEmpty())
        		if(developperList.peek().priority == -1 )// <headTile.getPriority()
        			found = true;
        	
        	
        	
        }
		
        if(found){
        	salvation = developperList.poll().getCostTile().getAiTile();
        }
        else
        	salvation = zone.getOwnHero().getTile();
        
		return salvation;
		
	}
	
	
	/**
	 * Pour trouver le tile d'un Hero qu'on peut approcher.
	 * 
	 * en cas qu'on ne peut pas trouver un hero la fonction retourne null
	 * 
	 * @return returningTile
	 * @throws StopRequestException
	 */
	private AiTile nearestHero() throws StopRequestException{
		checkInterruption(); //APPEL OBLIGATOIRE
		AiTile returningTile = null;
		AiZone zone=getPercepts();
		int checkcost = 1000;
		Collection<AiHero> heroes = zone.getHeroes();
		
		for(AiHero rival: heroes){
			checkInterruption();//APPEL OBLIGATOIRE;
			if(!rival.equals(zone.getOwnHero())){
				AiAction res = AstarAlgorithm(TargetTileCalculator2(rival.getTile()));
				if(res.getName() != AiActionName.NONE || zone.getOwnHero().getTile().equals(TargetTileCalculator2(rival.getTile()))){
					if(cost<checkcost){
						checkcost = cost;
						returningTile = rival.getTile();
					}
				}
			}
		}
		return returningTile;
	}
	
	/**
	 * Pour decider la strategie qu'on n'est pas en danger
	 * modifie int strategie
	 * 	
	 *		-1 si on ne fait rien
	 *		 0 si on doit attaquer un hero
	 *		 1 si on cherche un item
	 *		 2 si on detruit des murs
	 * 
	 * @param zone
	 * @return
	 * @throws StopRequestException
	 */
	private AiTile decideStrategy() throws StopRequestException{
		
		checkInterruption(); //APPEL OBLIGATOIRE
		cost = 0;
		AiZone zone = getPercepts();
		strategy = -1;
		
		int checkcost = 1000;
		AiTile returningTile = nearestHero();
		if(returningTile!=null)
			strategy = 0;
		else
			returningTile = zone.getOwnHero().getTile();
		
		if(strategy == -1){
			Collection<AiItem> itemssss = zone.getItems();
			for(AiItem item: itemssss){
				
				checkInterruption();//APPEL OBLIGATOIRE;
				AiAction res = AstarAlgorithm(item.getTile());
				if(res.getName() != AiActionName.NONE)
					if(cost<checkcost){
						checkcost = cost;
						returningTile = item.getTile();
						strategy = 1;
					}
			}
		}
		
		checkcost = 1000;
		
		if(strategy == -1){
			
			List<AiTile> blockSearcher = new ArrayList<AiTile>();
			Collection<AiTile> possibleDestructibles = new ArrayList<AiTile>();
			Collection<AiTile> oldClears = new ArrayList<AiTile>();
			blockSearcher.add(zone.getOwnHero().getTile());
			int itera = 0;
			while((!blockSearcher.isEmpty()) && itera<80 ){
				
				checkInterruption();//APPEL OBLIGATOIRE;
				
				AiTile currentTile = blockSearcher.get(0);
				blockSearcher.remove(0);
				oldClears.add(currentTile);
				Collection<AiTile> neighs = getClearNeighbors(currentTile, true);
				neighs.removeAll(oldClears);
				blockSearcher.addAll(neighs);
				neighs = zone.getNeighborTiles(currentTile);
				Iterator<AiTile> it = neighs.iterator();
				while(it.hasNext())
				{
					checkInterruption();//APPEL OBLIGATOIRE;
					
					AiTile neighbor = it.next();
					if(neighbor.getBlock()!= null)
						if(neighbor.getBlock().isDestructible())
							if(!possibleDestructibles.contains(currentTile))
								possibleDestructibles.add(currentTile);
						
				}
				
				if(!possibleDestructibles.isEmpty() && possibleDestructibles.size()<50){
					for(AiTile block: possibleDestructibles){
						itera++;
						checkInterruption();//APPEL OBLIGATOIRE;
						AiAction res = AstarAlgorithm(block);
						if(res.getName() != AiActionName.NONE || block.equals(zone.getOwnHero().getTile()))
							if(cost<checkcost){
								checkcost = cost;
								returningTile = block;
								strategy = 2;
							}
					}					
				}				
			}			
		}	
		return returningTile;
	}
	
	
	/**
	 * cette fonction verifie la cas de suicider en placant un bombe que laisse aucun tile a s'enfuir
	 * 
	 * @param zone
	 * @return
	 * @throws StopRequestException
	 */
	private boolean bombCheck(AiZone zone) throws StopRequestException{
		
		checkInterruption(); //APPEL OBLIGATOIRE
		List<AiTile> blockSearcher = new ArrayList<AiTile>();
		Collection<AiTile> oldClears = new ArrayList<AiTile>();
		blockSearcher.add(zone.getOwnHero().getTile());
		while(!blockSearcher.isEmpty()){
			
			checkInterruption();//APPEL OBLIGATOIRE;
			
	
			AiTile currentTile = blockSearcher.get(0);
			blockSearcher.remove(0);
			oldClears.add(currentTile);
			Collection<AiTile> neighs = getClearNeighbors(currentTile, true);
			neighs.removeAll(oldClears);
			blockSearcher.addAll(neighs);					
			}
		oldClears.removeAll(markDangerTiles());
		AiTile tott = zone.getOwnHero().getTile();
		Collection<AiTile> neighbors = getClearNeighbors(tott,true);
		for (AiTile t : neighbors) {
			checkInterruption(); // APPEL OBLIGATOIRE
			
			int i = 0;
			Direction d = getPercepts().getDirection(tott,t);
			while (t.getBlock()==null && i < zone.getOwnHero().getBombRange()) {
				checkInterruption(); // APPEL OBLIGATOIRE
			
				oldClears.remove(t);
				t = getPercepts().getNeighborTile(t, d);
				i++;
			}
		
		}
		oldClears.remove(zone.getOwnHero().getTile());
		if(oldClears.isEmpty())
			return false;
		else
			return true;
	}
	
	
	/* (non-Javadoc)
	 * @see org.totalboumboum.ai.adapter200809.ArtificialIntelligence#processAction()
	 */
	public AiAction processAction() throws StopRequestException
	{	
		checkInterruption(); // APPEL OBLIGATOIRE
		
		// reinitalize le strategie
		strategy = -1;
		
		//creation et mis a jour de bombList
		if(bombList==null)
			bombList =new ArrayList<AiBomb>();
		else
			updateBombList();
		
		
		AiTile runawayPlace;
		
		AiAction resultat = new AiAction(AiActionName.NONE);

		AiZone zone = getPercepts();
		
		//verifie s'il y a des autres
		if(zone.getHeroes().size()>1){
			
			Collection<AiTile> tilesinDanger = markDangerTiles();
			
			// si on est en danger
			if(tilesinDanger.contains(zone.getOwnHero().getTile())){
				//cherche un hero
				AiTile nearHero=nearestHero();
				
				if(nearHero==null){
					Iterator<AiHero> it=zone.getHeroes().iterator();
					while(it.hasNext()){
						AiHero h=it.next();
						if(!h.equals(zone.getOwnHero()))
							nearHero=h.getTile();
					}
				}
				checkInterruption(); // APPEL OBLIGATOIRE
				// on s'enfuit
				AiTile runTile = ultimateRunaway2(nearHero);
				runawayPlace = runTile;
				// verifie s'il y a un place pour s'enfuir
				if (runawayPlace != null)
					resultat = simpleAstarAlgorithm(runawayPlace);
				
				// c'est un controle specifique en cas des doubles angles de bombe
				//hero attend jusqu'a un bombe explose
				if (resultat.getName() == AiActionName.MOVE) {

					Collection<AiTile> sonkontrolTile = getClearNeighbors(zone
							.getOwnHero().getTile().getNeighbor(
									resultat.getDirection()), true);
					if (tilesinDanger.containsAll(sonkontrolTile)
							&& zone.getOwnHero().getTile().getBombs().size() == 0
							&& sonkontrolTile.size() > 2)
						resultat = new AiAction(AiActionName.NONE);
				}
				
			
			}
		
			else{
				
				AiTile theTile = decideStrategy();
				if(strategy == 0)
				{
					
					AiTile myTargetTile = TargetTileCalculator2(theTile);
					if(
						((theTile.getCol() == zone.getOwnHero().getCol() &&
						zone.getOwnHero().getTile().getNeighbor(Direction.DOWN).getBlock()==null  &&
						zone.getOwnHero().getTile().getNeighbor(Direction.UP).getBlock()==null	   &&
						(Math.abs(theTile.getLine() - zone.getOwnHero().getLine())<=zone.getOwnHero().getBombRange())
						) 
						|| 
						(theTile.getLine() == zone.getOwnHero().getLine() && 
						zone.getOwnHero().getTile().getNeighbor(Direction.LEFT).getBlock()==null &&
						zone.getOwnHero().getTile().getNeighbor(Direction.RIGHT).getBlock()==null &&
						Math.abs(theTile.getCol() - zone.getOwnHero().getCol())<=zone.getOwnHero().getBombRange())
						)
						 && bombCheck(zone)
						)
					
						resultat = new AiAction(AiActionName.DROP_BOMB);
					
					else	
						if(!(myTargetTile==null))
							if(zone.getOwnHero().getTile().equals(myTargetTile))								
								resultat = new AiAction(AiActionName.NONE);
							else
								resultat = AstarAlgorithm(myTargetTile);
						else
							if(zone.getOwnHero().getTile().getBombs().size()!=0)
								resultat = AstarAlgorithm(theTile);
							else	
								resultat = new AiAction(AiActionName.NONE);	
				}
				if(strategy == 1){
					
					resultat = AstarAlgorithm(theTile);
					
				}
				
				if(strategy == 2){
					if(zone.getOwnHero().getTile() == theTile){
						if(bombCheck(zone))
							if(zone.getOwnHero().getBombCount()<1)
								resultat = new AiAction(AiActionName.DROP_BOMB);
							else
								resultat = new AiAction(AiActionName.NONE);
						else
							resultat = new AiAction(AiActionName.NONE);
					}else
						resultat = AstarAlgorithm(theTile);
				}
					
			}
		}

		return resultat;
		
	}

}

