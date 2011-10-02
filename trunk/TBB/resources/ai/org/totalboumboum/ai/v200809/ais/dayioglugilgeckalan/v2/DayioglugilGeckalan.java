package org.totalboumboum.ai.v200809.ais.dayioglugilgeckalan.v2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import java.util.Vector;

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v200809.adapter.AiActionName;
import org.totalboumboum.ai.v200809.adapter.AiBlock;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiFire;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiItem;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Ali Batuhan Dayioğlugil
 * @author Gökhan Geçkalan
 *
 */
@SuppressWarnings("deprecation")
public class DayioglugilGeckalan extends ArtificialIntelligence {
	//private AiZone zone1;
	private AiTile currentTile=null;
	private AiTile nextTile=null;
	//private AiTile previousTile=null;
	private AiTile targetTile=null;
	private boolean danger;
	private LinkedList<AiTile> path;	
	int width = 0;
	int heigh = 0;
	int x;
	LinkedList<AiTile> choix;
	int y;
	AiZone zone;
	AiHero ownHero;
	AiTile mur;
	Direction direction = Direction.NONE;
	Noeud goal;
	boolean arret=false;
	AiAction result = new AiAction(AiActionName.NONE);

	
	
	public AiAction processAction() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		
		 zone = getPercepts();
		width = zone.getWidth();
		heigh = zone.getHeight();
		 ownHero = zone.getOwnHero();
		 x=getPercepts().getOwnHero().getCol();
		 y=getPercepts().getOwnHero().getLine();
		 currentTile = ownHero.getTile();
			

			targetTile = currentTile;
			danger = false;
		 if (nextTile==null)
			 init();
		
		
		if (ownHero != null) {
			boolean existe=false;
			//bonus();
			Iterator <AiBlock>ip=getPercepts().getBlocks().iterator();
			 while(ip.hasNext() && !existe)
			 {if(( ip.next()).isDestructible())
				 existe=true;
				 
			 }
			//if (!arret){
			
				//arret=true;

				 
			if(existe)
			mur=getClosestDestructibleBlock();
			//}
			
			
			//isDanger();
			LinkedList<AiTile> danger = dangerZone();
//			LinkedList<AiTile> safe = safeZone();
			if (danger.contains(currentTile) || danger.contains(nextTile)) {
				//kacma fonksiyonu
				
				pickNextTile(findPath());
				
				
			} 
			else if(existe) {				
				pickNextTile(mur);
				
				// ulasabildigin bonus varsa bonus topla
				
				boolean bonusFound = false;
				AiTile bonusTile = null;
				if(isThereBonusAccessible())
				{
					bonusTile = getClosestBonus();
					if(bonusTile != null)
					{
						
						PathFinder pathFind = new PathFinder(zone,bonusTile,this);
						if(!pathFind.getPath().isEmpty() && pathFind.getPath()!=null && pathFind.getPath().size() <= 6)
						{
							
							bonusFound = true;
						}
					}
				}
				
				if(bonusFound && bonusTile!=null)
				{
					pickNextTile(bonusTile);
					
				}
				else{
				
				// bonus yoksa duvar patlat
				
				//ya da attack yap
				if (currentTile==mur){
					
					if (isSafe(mur) && ownHero.getBombCount() ==0)
				{
						result=new AiAction(AiActionName.DROP_BOMB);
				direction=Direction.NONE;}
				arret=false;
				}
				}}
			if (nextTile!=null){
			direction = zone.getDirection(
					currentTile, nextTile);
			
			}
			if ( direction!=Direction.NONE &&direction != Direction.DOWNLEFT
					&& direction != Direction.DOWNRIGHT
					&& direction != Direction.UPLEFT
					&& direction != Direction.UPRIGHT) {

				
				

				result = new AiAction(AiActionName.MOVE, direction);
			}
		}
		if(dangerZone().contains(nextTile) && !dangerZone().contains(currentTile))
			result = new AiAction(AiActionName.NONE);
		
		return result;
	}
	public void init(){
		
		nextTile=currentTile;
		//previousTile=currentTile;
	}

	/*private LinkedList<AiTile> pathDestructible() throws StopRequestException 
	{
		checkInterruption();
		
		LinkedList<AiTile> result = new LinkedList<AiTile>();
		LinkedList<AiTile> temp = new LinkedList<AiTile>();
		int min = 1000;
		AiBlock isDestructible;
		Collection<AiBlock> tempDestructible = new ArrayList<AiBlock>();
		
		tempDestructible = zone1.getBlocks();
		Iterator<AiBlock> it1 = tempDestructible.iterator();
		while(it1.hasNext())
		{
			isDestructible = it1.next();
			if(isDestructible.isDestructible())
			{
				result.add(isDestructible.getTile()) ;
			}
			PathFinder pathFind = new PathFinder(this.zone1, isDestructible.getTile(),this);
			temp = (LinkedList<AiTile>) pathFind.getPath();
			if (min > temp.size() && !temp.isEmpty()) {
				min = temp.size();
				result = temp;
				targetTile = isDestructible.getTile();
			
		}
		
		}
		return result;	
	}
	private LinkedList<AiTile> safeWay()
	{
		LinkedList<AiTile>  dangerWay = new LinkedList<AiTile>();
		LinkedList<AiTile>  safeWay = new LinkedList<AiTile>();
		Collection<AiBlock> blocks = new ArrayList<AiBlock>();
		Collection<AiFire> fires = new LinkedList<AiFire>();
		Collection<AiBomb> bombs = new LinkedList<AiBomb>();
		blocks=zone.getBlocks();
		fires=zone.getFires();
		bombs= zone.getBombs();
		
		Iterator<AiBlock> it1= blocks.iterator();
		
		
		while(it1.hasNext())
		{
			AiTile temp1= it1.next().getTile();
			dangerWay.add(temp1);
			
		}
		Iterator<AiFire> it2= fires.iterator();
		while(it1.hasNext())
		{
			AiTile temp1= it2.next().getTile();
			dangerWay.add(temp1);
			
		}
		Iterator<AiBomb> it3= bombs.iterator();
		while(it1.hasNext())
		{
			AiTile temp1= it3.next().getTile();
			dangerWay.add(temp1);
			
		}
		
		for (int i = 0 ; i < width ; i++) {
			for (int j = 0 ; j < heigh ; j++) {
				AiTile safe = zone.getTile(j,i);
				if (!dangerWay.contains(safe))
					safeWay.add(safe);
			}
		}
		
		return safeWay;
	}
*/
	private LinkedList<AiTile> dangerZone() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		LinkedList<AiTile> dangerZone = new LinkedList<AiTile>();
		Collection<AiBomb> bombs = new ArrayList<AiBomb>();
		Collection<AiTile> bombDanger = new ArrayList<AiTile>();
		AiZone zone = getPercepts();
		bombs = zone.getBombs();
		
		Iterator<AiBomb> itBomb = bombs.iterator();
	
		while (itBomb.hasNext()) {
			checkInterruption();
			AiBomb temp = itBomb.next();
			
			AiTile temp2 =temp.getTile();
			bombDanger.add(temp2); 								
			
			int bnx = temp.getCol();
			int bny = temp.getLine();			
			
			AiBlock right = temp2.getNeighbor(Direction.RIGHT).getBlock();
			AiBlock left = temp2.getNeighbor(Direction.LEFT).getBlock();
			AiBlock up = temp2.getNeighbor(Direction.UP).getBlock();
			AiBlock down = temp2.getNeighbor(Direction.DOWN).getBlock();			
		
			if (left == null) {
				for (int i = 1 ; (i <= temp.getRange()) && (bnx-i > 0) ; i++ ) {	
					checkInterruption(); // APPEL OBLIGATOIRE				
					if (zone.getTile(bny,bnx-i).getBlock() == null && !bombDanger.contains(zone.getTile(bny,bnx-i))) 
						bombDanger.add(zone.getTile(bny,bnx-i));				
				}			
			}
			if (right == null) {
				for (int i = 1 ; (i <= temp.getRange()) && (bnx+i < width); i++ ) { 
					checkInterruption(); // APPEL OBLIGATOIRE					
					if (zone.getTile(bny,bnx+i).getBlock() == null && !bombDanger.contains(zone.getTile(bny,bnx+i))) 
						bombDanger.add(zone.getTile(bny,bnx+i));											
				}		
			}
			if (up == null) {
				for (int i = 1 ; (i <= temp.getRange()) && (bny -i> 0); i++ ) {	
					checkInterruption(); // APPEL OBLIGATOIRE
						if (zone.getTile(bny-i,bnx).getBlock() == null && !bombDanger.contains(zone.getTile(bny-i,bnx))) 
							bombDanger.add(zone.getTile(bny-i,bnx));					
				}				
			}
			if (down == null)	{
				for (int i = 1 ; (i <= temp.getRange()) && (bny+i < heigh); i++ ) {  
					checkInterruption(); // APPEL OBLIGATOIRE
					if (zone.getTile(bny+i,bnx).getBlock() == null && !bombDanger.contains(zone.getTile(bny+i,bnx))) 
						bombDanger.add(zone.getTile(bny+i,bnx));														
				}				
			}		
		}	
			

		Collection<AiFire> fires = new ArrayList<AiFire>();
		LinkedList<AiTile> fireDanger = new LinkedList<AiTile>();
		fires = zone.getFires();
		Iterator<AiFire> itFire = fires.iterator();

		while (itFire.hasNext())
		{
			checkInterruption(); // APPEL OBLIGATOIRE
			fireDanger.add(itFire.next().getTile());
		}
		Iterator<AiTile> itBD = bombDanger.iterator();
		Iterator<AiTile> itFD = fireDanger.iterator();

		while (itBD.hasNext()) {
			checkInterruption(); // APPEL OBLIGATOIRE
			dangerZone.add(itBD.next());
		}
		while (itFD.hasNext())
		{
			checkInterruption(); // APPEL OBLIGATOIRE
			dangerZone.add(itFD.next());
		}
		
		return dangerZone;	
	}
	
	private LinkedList<AiTile> safeZone() throws StopRequestException
	{	checkInterruption(); // APPEL OBLIGATOIRE
		LinkedList<AiTile> safeZone = new LinkedList<AiTile>();
		LinkedList<AiTile> dangerZone = new LinkedList<AiTile>();
		Collection<AiBlock> blocks = new ArrayList<AiBlock>();
		
		
		
		dangerZone = dangerZone();
		//System.out.println("danger"+dangerZone);
		blocks = zone.getBlocks();
		
		for (int i = 0 ; i < width ; i++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int j = 0 ; j < heigh ; j++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(j,i);
				if ( !dangerZone.contains(tile) && !blocks.contains(tile.getBlock()))
					safeZone.add(tile);
			}
		}
		//System.out.println(safeZone);
		return safeZone;
	}
	/*private void bonus() throws StopRequestException{
		checkInterruption();
	 choix = new LinkedList<AiTile>();
		LinkedList<Astar> choixas = new LinkedList<Astar>();
		for (int i = 0; i <width; i++) {
			checkInterruption();
			for (int j = 0; j < heigh; j++) {
				checkInterruption();
				// i j hata veriyo eger destination ayn bulundugu yerse
				if ((getPercepts().getTile(j, i).getItem() != null )) {

					PathFinder pathfinder = new PathFinder(getPercepts(),getPercepts().getTile(j, i),this);
					if(pathfinder.getPath()!= null)
					{
					

						choix.add(getPercepts().getTile(j, i));
						

					}
				}
			}
		}
		//System.out.println(choix);
		 result = new AiAction(AiActionName.MOVE, Direction.NONE);
		
		
		
	}*/
	private void pickNextTile(AiTile targettile) throws StopRequestException {
		checkInterruption();
		AiZone zone = getPercepts();
		//AiHero ownHero = zone.getOwnHero();
		
			
			//previousTile = currentTile;
			if (targettile != currentTile) {
				PathFinder tempPath = new PathFinder(zone, targettile,this);
				
		
				this.path = tempPath.getPath();
				if(path!=null && !path.isEmpty()){
					nextTile=path.poll();
					if(nextTile==currentTile)
					{
						if(path!=null)
							nextTile=path.poll();
					}
				}
			}
			else
			{
				nextTile = currentTile;
			}
			/*} else {
				
				previousTile = currentTile;
				this.path = findPath();
				if (targetTile != ownHero.getTile()) {
					if (!path.isEmpty()) {
						nextTile = path.poll();
						if (nextTile.equals(ownHero.getTile()))
							nextTile = path.poll();
					}
				}
			}*/
		
	}

	private AiTile findPath() throws StopRequestException {
		checkInterruption();
		LinkedList<AiTile> escapeTiles = new LinkedList<AiTile>();
		
		
	//	Zone tempZone = new Zone(this.zone,this);
		
		
		LinkedList<AiTile> temp = new LinkedList<AiTile>();
		int min = 1000;
		AiTile escapeTileTemp;
		escapeTiles = safeZone();
		
		Iterator<AiTile> itEscape = escapeTiles.iterator();
		
		while (itEscape.hasNext()) {
			checkInterruption(); // APPEL OBLIGATOIRE
			escapeTileTemp = itEscape.next();
			
			PathFinder pathFind = new PathFinder(this.getPercepts(), escapeTileTemp,this);
			//System.out.println("sakfj");
			
			temp = (LinkedList<AiTile>) pathFind.getPath();
			if ((min > temp.size() && !temp.isEmpty()) || escapeTileTemp.equals(ownHero.getTile())) {
				min = temp.size();
				
				
				targetTile = escapeTileTemp;
			}
		}
		
		return targetTile;
	}
	private AiTile getClosestDestructibleBlock() throws StopRequestException 
	{	checkInterruption();
		
		
		//AiTile res = null;
		//LinkedList<AiTile> result = new LinkedList<AiTile>();
		LinkedList<AiTile> temp = new LinkedList<AiTile>();
		LinkedList<AiTile> nextPossibleTiles = new LinkedList<AiTile>();
		LinkedList<AiTile> possibleTargets = new LinkedList<AiTile>();
		//AiHero ownHero = zone.getOwnHero();
		
		int min = 1000;
		//int min2 = 1000;
		//AiBlock isDestructible;
		Collection<AiBlock> blocks = new ArrayList<AiBlock>();
		Collection<AiBlock> tempDestructible = new ArrayList<AiBlock>();
		
		//tempDestructible = zone1.getBlocks();
		blocks = zone.getBlocks();
		Iterator<AiBlock> itBl = blocks.iterator();
			while (itBl.hasNext()) {
				checkInterruption(); // APPEL OBLIGATOIRE
				AiBlock temp2 = itBl.next();
				if (temp2.isDestructible())
					tempDestructible.add(temp2);
			}
		
		Iterator<AiBlock> itBl2 = tempDestructible.iterator();
		//LinkedList<AiTile> possibleTargetPath = new LinkedList<AiTile>();
			while (itBl2.hasNext()) {
				checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tempDest = itBl2.next().getTile();
				AiTile git1 = zone.getTile(tempDest.getLine()+1,tempDest.getCol());
				if (safeZone().contains(git1))
				possibleTargets.add(git1);
				AiTile git2 = zone.getTile(tempDest.getLine()-1,tempDest.getCol());
				if (safeZone().contains(git2))
				possibleTargets.add(git2);
				AiTile git3 = zone.getTile(tempDest.getLine(),tempDest.getCol()+1);
				if (safeZone().contains(git3))
				possibleTargets.add(git3);
				AiTile git4 = zone.getTile(tempDest.getLine(),tempDest.getCol()-1);
				if (safeZone().contains(git4))
				possibleTargets.add(git4);
				//System.out.println(tempDest.toString());
				//System.out.println("while 1");
			}	
				
			Iterator<AiTile> itTile = possibleTargets.iterator();
				
			while (itTile.hasNext()){
				checkInterruption(); // APPEL OBLIGATOIRE
				//System.out.println("while 2");
				AiTile tempTarget = itTile.next(); 
				PathFinder pathFind = new PathFinder(this.zone,tempTarget,this);
				temp = pathFind.getPath();
				if(( min > temp.size() || min == temp.size() ) && !temp.isEmpty())
				{			
					min = temp.size();				
					nextPossibleTiles.add(tempTarget);
				}
				//System.out.println("while 3");
			}
			
			Iterator<AiTile> itTargets = nextPossibleTiles.iterator();
 			AiTile target = null;
			if(itTargets.hasNext())
 				target = itTargets.next();
			
	/*	boolean found = false;
		LinkedList<AiTile> nextPossibleTargets = new LinkedList<AiTile>();
		
		Iterator<AiTile> itTarget = nextPossibleTiles.iterator();
			while (itTarget.hasNext() && !found) {
					AiTile tempTarget = itTarget.next();
					
					
					Iterator<AiTile> itPossible = possibleTargets.iterator();
					while (itPossible.hasNext()) {
						AiTile targetDest = itPossible.next();
						PathFinder pathFinder = new PathFinder(this.zone,targetDest);
						possibleTargetPath = pathFinder.getPath();
						if ((min > possibleTargetPath.size() || min == possibleTargetPath.size()) && !possibleTargetPath.isEmpty())
						{
							min = possibleTargetPath.size();
							nextPossibleTargets.add(targetDest);						
						}									
					}						
					if ( !nextPossibleTargets.isEmpty())
						found = true;	
			}		
			System.out.println("while dan ciktim -----------------------");
		Iterator<AiTile> targets = nextPossibleTargets.iterator();	
		AiTile target  = targets.next();*/
		/*System.out.println("##########################################");
		System.out.println(target);
		System.out.println("##########################################");*/
			
		if(target==null){
			
			target = ownHero.getTile();
			
		}
		return target;	
	}


	/*private AiAction move() throws StopRequestException {
		checkInterruption();
		AiZone zone1 = getPercepts();
		AiHero ownHero = zone1.getOwnHero();
		if (ownHero != null) {
			currentTile = ownHero.getTile();

			if (nextTile == null) {
				nextTile = currentTile;
				previousTile = currentTile;
			}
			
			if (currentTile == nextTile)
				pickNextTile();		
			else if (previousTile != currentTile) {
				previousTile = currentTile;
				pickNextTile();
			}
			Direction direction = zone1.getDirection(currentTile, nextTile);

			AiAction result = new AiAction(AiActionName.NONE);

			if (direction == Direction.NONE)
				result = new AiAction(AiActionName.MOVE, direction);
			return result;
		} else
			return new AiAction(AiActionName.NONE);
	}*/

	/*private void isDanger() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		if (!isClear(currentTile) || !isClear(nextTile)) {
			danger = true;
		} else
			danger = false;
	}*/

	/*private boolean isClear(AiTile tile) {
		boolean resultat = true;


		if (tile == null)
			resultat = true;
		else if (tile.getBombs().size() != 0 || (tile.getFires().size() != 0)) 
			resultat = false;
		else resultat=true;
		
		return resultat;
	}*/
	private boolean isSafe(AiTile tile) throws StopRequestException 
	{
		
		checkInterruption(); // APPEL OBLIGATOIRE
		int range = ownHero.getBombRange();
		Vector <AiTile> explosion = new Vector<AiTile>();
		
		
		for (int i = x -range ; i <= x + range ; i++  ) {
			checkInterruption(); // APPEL OBLIGATOIRE
			if ( i > 0 && (i) < width) 
				explosion.add(zone.getTile(tile.getLine(),i));				
		}
		
		for ( int j = y - range ; j <= y + range ; j++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			if ((j) > 0 && (j) < heigh ) 
				explosion.add(zone.getTile(tile.getLine(),j));
		}

		
		
		for (int i = x -range ; i <= x + range ; i++  ) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for ( int j = y - range ; j <= y + range ; j++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				if ( (i > 0 && (i) < width && (j) > 0 && (j) < heigh )) {
					
					if (safeZone().contains(zone.getTile(j,i)) && !explosion.contains(zone.getTile(j,i)))
						danger = true;
					
				}
			}
		}
		
		return danger;
		
	}

	public boolean isThereBonusAccessible() throws StopRequestException
	{
		checkInterruption(); // APPEL OBLIGATOIRE
		Iterator<AiItem> itBonus = zone.getItems().iterator();
		while(itBonus.hasNext())
		{
			checkInterruption(); // APPEL OBLIGATOIRE
			PathFinder pathFind = new PathFinder(zone,itBonus.next().getTile(),this);
			if(!pathFind.getPath().isEmpty() || pathFind.getPath() != null)
				return true;
		}
		return false;
	}
	
	public AiTile getClosestBonus() throws StopRequestException
	{
		checkInterruption(); // APPEL OBLIGATOIRE
		AiTile minTile = null;
		int minDist = Integer.MAX_VALUE;
		Iterator<AiItem> itBonus = zone.getItems().iterator();
		while(itBonus.hasNext())
		{
			checkInterruption(); // APPEL OBLIGATOIRE
			AiTile temp = itBonus.next().getTile();
			PathFinder pathFind = new PathFinder(zone,temp,this);
			if(!pathFind.getPath().isEmpty() && pathFind.getPath() != null)
				if(minDist > pathFind.getPath().size())
				{
					minDist = pathFind.getPath().size();
					minTile = temp;
				}
		}
		return minTile;		
	}
	
	
}