package org.totalboumboum.ai.v200809.ais.coremenkucukkarakurt.v2c;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
 * @author Selim Çöremen
 * @author Umut Küçükkarakürt
 *
 */
@SuppressWarnings("deprecation")
public class CoremenKucukkarakurt extends ArtificialIntelligence
{
	
	/** la case occupée actuellement par le personnage*/
	private AiTile currentTile;
	
	/** la case sur laquelle on veut aller */
	private AiTile nextTile = null;
	
	/** la dernière case par laquelle on est passé */ 
	private AiTile previousTile = null;
	
	// la methode principale
	public AiAction processAction() throws StopRequestException
	{	
		
		checkInterruption(); //APPEL OBLIGATOIRE
	
		
		
		AiZone zone = getPercepts();
		AiHero ownHero = zone.getOwnHero();
		
		
		AiAction result = new AiAction(AiActionName.NONE);
		
		// si ownHero est null, c'est que l'IA est morte : inutile de continuer
		if(ownHero!=null)
		{	
			
			// on met à jour la position de l'ia dans la zone
			currentTile = ownHero.getTile();
			
			// premier appel : on initialise l'IA	
			if(nextTile == null)
				init();

			
			
			/*
			 * si mon currenttile est en securité et si ma region est en securité et si je suis à coté
			 * d'un mur et si il y a pas une bombe proche
			 */
			if(isCaseSafe(currentTile) && isRegionSafe(currentTile) && canIDestroyAWall(currentTile)&& getCloseBombs().size()<1){
				
				// si il y a une direction n'est pas dangerous
				if(isAnySafeDirection(currentTile)){
					
					// on mets une bombe
					return result= new AiAction(AiActionName.DROP_BOMB);
					
				}
				
				// si tous les directions possibles sont dangereux
				else{
					// on fait un mouvement par hasard
					return result= comportementHasard(zone, ownHero);
				}
			}
		
			// si il y a plus de 1 bombes
			if(getCloseBombs().size()>=1){	
				// si mon case est en securité
				if(isCaseSafe(currentTile)){
					
					// on fait rien
					result=new AiAction(AiActionName.NONE);
					
				}
				// si mon case n'est pas en secutité
				else{
					
					// on fait un mouvement par hasard
					
					result=comportementHasard(zone, ownHero);
					
				}
			}
			
			// si y'en a pas plus de deux bombes et si je  suis  en meme ligne avec un autre hero 
			// et si je ne suis pas en meme ligne ou en meme colonne avec au autre bombe
			else if((amIOnTheSameLine(currentTile)  ) && zone.getBombs().size()<2 && getClearNeighbors(currentTile).size()>2){
				 
				// on verifie s'il y a une seule bombes
				 if(getPercepts().getBombs().size()>0){
					 // je ne mets pas une bombe si je suis en meme ligne ou colonne avec cette bombe
					 if(getBombPos().get(0).getCol()==currentTile.getCol() || getBombPos().get(0).getLine()==currentTile.getLine()){
						
						 result=comportementHasard(zone, ownHero);
					 }
						// on fait un mouvement par hasard
						 
					 else
						// sinon on mets une bombe
						
						 result= new AiAction(AiActionName.DROP_BOMB);
					
				 }
				 // sinon on mets une bombe
				 else{
					 
					 result=new AiAction(AiActionName.DROP_BOMB);
				 }
					 
				
			}
			
			// on mets des bombes si il ya plus de 3 bombes et 
			else if(zone.getBombs().size()>0 ){
				AiTile tempTile;
				
				// on prends le position de la bombe la plus dengereux 
				AiTile dangBombPos=getDeepistBombPos();	
				
				// si la bombe la plus dengereux existe
				if(dangBombPos!=null)
					tempTile= getDirection(dangBombPos);
				else
					// sinon on s'eloigne de notre propre position
					tempTile=currentTile;
				
				// on prend la direction qui fait notre hero plus loin de temptile
				Direction direction = getPercepts().getDirection(currentTile,tempTile);
				
				// on applique le mouvement de cette direction
				
				result=new AiAction(AiActionName.MOVE,direction);
				
				
			}
			// si il y a aucune bombe 
			else {
				 // on fait un mouvement par hasard
				
				 result=comportementHasard(zone, ownHero);
			}
				
			
				
		}	
						
		
		return result;
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 */
	private void init() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		nextTile = currentTile;		
		previousTile = currentTile;		
	}
	
	/**
	 * Choisit comme destination une case voisine de la case actuellement occupée par l'IA.
	 * Cette case doit être accessible (pas de mur ou de bombe ou autre obstacle) et doit
	 * être différente de la case précédemment occupée
	 * 
	 * @param tile 
	 * @return ?
	 * 
	 * @throws StopRequestException 
	 */
	private List<AiTile> getClearNeighbors(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// liste des cases autour de la case de référence
		Collection<AiTile> neighbors = getPercepts().getNeighborTiles(tile);
		// on garde les cases sans bloc ni bombe ni feu
		List<AiTile> result = new ArrayList<AiTile>();
		Iterator<AiTile> it = neighbors.iterator();
		while(it.hasNext())
		{	checkInterruption(); //APPEL OBLIGATOIRE
		
			AiTile t = it.next();
			if(isClear(t))
				result.add(t);
		}
		return result;
	}
	
	/**
	 * 
	 * @param tile
	 * @return ?
	 * @throws StopRequestException
	 */
	private boolean isClear(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result;
		AiBlock block = tile.getBlock();
		Collection<AiBomb> bombs = tile.getBombs();
		Collection<AiFire> fires = tile.getFires();
		result = block==null && bombs.size()==0 && fires.size()==0;
		return result;
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 */
	private void checkNextTile() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// si un obstacle est apparu sur la case destination, on change de destination
		if(!isClear(nextTile))
		{	// liste des cases voisines accessibles	
			List<AiTile> tiles = getClearNeighbors(currentTile);
			// on sort l'ancienne destination (qui est maintenant bloquée) de la liste
			if(tiles.contains(nextTile))
				tiles.remove(nextTile);
			// s'il reste des cases dans la liste : on en tire une au hasard
			if(tiles.size()>0)
			{	double p = Math.random()*tiles.size();
				int index = (int)p;
				nextTile = tiles.get(index);
				previousTile = currentTile;
			}
		}
	}
	
	/** la methode qui verifie si la case est en securité ou non
	 * 
	 * @param tile
	 * @return ?
	 * @throws StopRequestException
	 */
	private boolean isCaseSafe (AiTile tile) throws StopRequestException{
		checkInterruption(); //APPEL OBLIGATOIRE
		tile=currentTile;
		for (int i=0;i<5;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.UP);
			if((tempTile.getBombs().size()!=0 && tempTile.getBlock()==null) || tempTile.getFires().size()>0)
				return false;
			tile=tempTile;
		}
		tile=currentTile;
		for (int i=0;i<5;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.DOWN);
			if((tempTile.getBombs().size()!=0 && tempTile.getBlock()==null) || tempTile.getFires().size()>0)
				return false;
			tile=tempTile;
		}
		tile=currentTile;
		for (int i=0;i<5;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.RIGHT);
			if((tempTile.getBombs().size()!=0 && tempTile.getBlock()==null) || tempTile.getFires().size()>0)
				return false;
			tile=tempTile;
		}
		tile=currentTile;
		for (int i=0;i<5;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.LEFT);
			if((tempTile.getBombs().size()!=0 && tempTile.getBlock()==null) || tempTile.getFires().size()>0)
				return false;
			tile=tempTile;
		}
		if(currentTile.getBombs().size()!=0)
			return false;
		
		return true;
	}
	
	
	/** la methode qui verifie si je suis en meme ligne ou en meme colonne avec un case choisi
	 * 
	 * @param tile
	 * @return ?
	 * @throws StopRequestException
	 */
	private boolean amIOnTheSameLine(AiTile tile) throws StopRequestException{
		
		checkInterruption(); //APPEL OBLIGATOIRE
		tile=currentTile;
		for (int i=0;i<4;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.UP);
			if(tempTile.getHeroes().size()!=0 && tile.getBlock()==null)
				return true;
			tile=tempTile;
		}
		tile=currentTile;
		for (int i=0;i<4;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.DOWN);
			if(tempTile.getHeroes().size()!=0 && tile.getBlock()==null)
				return true;
			tile=tempTile;
		}
		tile=currentTile;
		for (int i=0;i<4;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.RIGHT);
			if(tempTile.getHeroes().size()!=0 && tile.getBlock()==null)
				return true;
			tile=tempTile;
		}
		tile=currentTile;
		for (int i=0;i<4;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.LEFT);
			if(tempTile.getHeroes().size()!=0 && tile.getBlock()==null)
				return true;
			tile=tempTile;
		}	
		return false;	
	}
	
	
	/** comportement par hasard
	 *  
	 * @param alan
	 * @param ownHero
	 * @return ?
	 * @throws StopRequestException
	 */
	private AiAction comportementHasard (AiZone alan, AiHero ownHero) throws StopRequestException{
		
		checkInterruption(); //APPEL OBLIGATOIRE
		
		AiAction result = new AiAction(AiActionName.NONE);
		
		// si ownHero est null, c'est que l'IA est morte : inutile de continuer
		
		// on met à jour la position de l'ia dans la zone
			
		currentTile = ownHero.getTile();

		// premier appel : on initialise l'IA
			
				
		if(nextTile == null)
			init();
		if(currentTile.getBombs().size()>0){
			
			if(isAnySafeDirection(currentTile)){
				
				return result= new AiAction(AiActionName.MOVE,getTheSafeDir(currentTile));
				
			}
			
			
		}
		
		
		
		// arrivé à destination : on choisit une nouvelle destination
		if(currentTile==nextTile)
			pickNextTile();
		
		// au cas ou quelqu'un prendrait le Contrôle manuel du personnage
		
		else if(previousTile!=currentTile)
		{	previousTile = currentTile;
			pickNextTile();			
		}
		
		// sinon (on garde la même direction) on vérifie qu'un obstacle (ex: bombe) n'est pas apparu dans la case
		else
			checkNextTile();
				
		// on calcule la direction à prendre
		
		Direction direction = getPercepts().getDirection(currentTile,nextTile);

		// on calcule l'action
		if(direction!=Direction.NONE)
			result = new AiAction(AiActionName.MOVE,direction);
		return result;
	}
	
	
	/** la methode qui renvoie la distance entre deux cases choisits
	 * 
	 * @param tile1
	 * @param tile2
	 * @return ?
	 * @throws StopRequestException
	 */
	private double getDistance(AiTile tile1,AiTile tile2) throws StopRequestException{
		
		checkInterruption(); //APPEL OBLIGATOIRE
		
		int x1=tile1.getCol();
		int y1=tile1.getLine();
		
		int x2=tile2.getCol();
		int y2=tile2.getLine();
		
		return Math.sqrt(Math.abs(x2-x1)*Math.abs(x2-x1)+Math.abs(y2-y1)*Math.abs(y2-y1));
		
	}
	
	/** la methode qui renvoi la liste des positions des bombes presentes
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	private List<AiTile> getBombPos() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		
		// liste des cases autour de la case de référence
		Collection<AiBomb> bombs = getPercepts().getBombs();
		// on garde les cases sans bloc ni bombe ni feu
		List<AiTile> result = new ArrayList<AiTile>();
	
		Iterator<AiBomb> it = bombs.iterator();
	
		while(it.hasNext())
		{	checkInterruption(); //APPEL OBLIGATOIRE
	
			AiBomb t = it.next();
			result.add(t.getTile());
		
		}
		return result;
		
		
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 */
	private void pickNextTile() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
	// liste des cases voisines accessibles	
	List<AiTile> tiles = getClearNeighbors(currentTile);
	// on sort de la liste la case d'où l'on vient (pour éviter de repasser au même endroit)
	boolean canGoBack = false;
	if(tiles.contains(previousTile))
	{	tiles.remove(previousTile);
		canGoBack = true;
	}
	// s'il reste des cases dans la liste
	if(tiles.size()>0)
	{	// si la liste contient la case située dans la direction déplacement précedente,
		// on évite de l'utiliser (je veux avancer en zig-zag et non pas en ligne droite)
		AiTile tempTile = null;
		Direction dir = getPercepts().getDirection(previousTile,currentTile);
		if(dir!=Direction.NONE)
		{	tempTile =  getPercepts().getNeighborTile(currentTile, dir);
			if(tiles.contains(tempTile))
				tiles.remove(tempTile);
		}
		// s'il reste des cases dans la liste
		if(tiles.size()>0)
		{	// on en tire une au hasard
			double p = Math.random()*tiles.size();
			int index = (int)p;
			nextTile = tiles.get(index);
			previousTile = currentTile;
		}
		// sinon (pas le choix) on continue dans la même direction
		else
		{	nextTile = tempTile;
			previousTile = currentTile;
		}
	}
	// sinon (pas le choix) on tente de revenir en arrière
	else
	{	if(canGoBack)
		{	nextTile = previousTile;
			previousTile = currentTile;
		}
		// et sinon on ne peut pas bouger, donc on ne fait rien du tout
	}
	}
	
	/** la methode qui renvoie la direction qui n'est pas dangereuse
	 *  
	 * @param refTile
	 * @return ?
	 * @throws StopRequestException
	 */
	private AiTile getDirection(AiTile refTile) throws StopRequestException{
		checkInterruption();
		List<AiTile> tiles = getClearNeighbors(currentTile);
		
		double min=10000000;
		int k=0;
		double dist= getDistance(refTile, currentTile);
		
		for (int i=0;i<tiles.size();i++){
			checkInterruption();
			if(getDistance(refTile, tiles.get(i))>dist && getDistance(refTile, tiles.get(i))<min ){
				min=getDistance(refTile, tiles.get(i));
				k=i;
			}
				
		}
		if(!tiles.isEmpty())
			return tiles.get(k);
		else
			return currentTile;
	}
	
	
	/** la methode qui renvoie la position de la bombe la proche a son explosion
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	private AiTile getDeepistBombPos() throws StopRequestException{
		
		checkInterruption(); //APPEL OBLIGATOIRE
		
		AiTile tile=null;
		double min=10000000;
		
		List<AiBomb> bombs = getCloseBombs();	
		
		
		Iterator<AiBomb> it = bombs.iterator();
	
		
		
		while(it.hasNext())
		{	checkInterruption(); //APPEL OBLIGATOIRE
	
			AiBomb t = it.next();
			if(t.getNormalDuration()<min){
				min=t.getNormalDuration();
				tile=t.getTile();
				
			}
		
		}
		return tile;
		
	}
	
	/** la methode qui renvoie la liste des bombes proche a notre hero
	 * 
	 * @return ?
	 * @throws StopRequestException
	 */
	private List<AiBomb> getCloseBombs() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		
		// liste des cases autour de la case de référence
		Collection<AiBomb> bombs = getPercepts().getBombs();
		// on garde les cases sans bloc ni bombe ni feu
		List<AiBomb> result = new ArrayList<AiBomb>();
	
		Iterator<AiBomb> it = bombs.iterator();
	
		while(it.hasNext())
		{	checkInterruption(); //APPEL OBLIGATOIRE
			
			AiBomb t = it.next();
			if(getDistance(currentTile, t.getTile())<5)
				result.add(t);
		
		}
		return result;
	}	
	
	
	
	/** la methode qui renvoie si notre hero est à coté d'un mur
	 * 
	 * @param tile
	 * @return ?
	 * @throws StopRequestException
	 */
	private boolean canIDestroyAWall (AiTile tile) throws StopRequestException{
		checkInterruption(); //APPEL OBLIGATOIRE
		tile=currentTile;
		
		AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.UP);
		if(tempTile.getBlock()!=null){
			if(tempTile.getBlock().isDestructible())
				return true;
		}
			
		tempTile =  getPercepts().getNeighborTile(tile, Direction.DOWN);
		if(tempTile.getBlock()!=null){
			if(tempTile.getBlock().isDestructible())
				return true;
		}
			
		tempTile =  getPercepts().getNeighborTile(tile, Direction.RIGHT);
		if(tempTile.getBlock()!=null){
			if(tempTile.getBlock().isDestructible())
				return true;
		}
		
		tempTile =  getPercepts().getNeighborTile(tile, Direction.LEFT);
		if(tempTile.getBlock()!=null){
			if(tempTile.getBlock().isDestructible())
				return true;
		}
		
			
		return false;
	}

	
	
	/** la methode qui renvoie si notre hero a une possible direction qui n'est pas dangereuse
	 * 
	 * @param tile
	 * @return ?
	 * @throws StopRequestException
	 */
	private boolean isAnySafeDirection(AiTile tile) throws StopRequestException{
		checkInterruption(); //APPEL OBLIGATOIRE
		
		tile=currentTile;
		
		for (int i=0;i<10;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.UP);
			
			if(tempTile.getBlock()!=null ){
				
				break;
			}
			//else if(getTheNeigborgsNumber(tempTile)==1)	
			else if(isClear(getPercepts().getNeighborTile(tempTile, Direction.RIGHT)) || isClear(getPercepts().getNeighborTile(tempTile, Direction.LEFT)))
				return true;
			
			tile=tempTile;
			
		}
		tile=currentTile;
		for (int i=0;i<10;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.DOWN);
			
			if(tempTile.getBlock()!=null ){
				
				break;
			}
			//else if(getTheNeigborgsNumber(tempTile)==1)
			else if(isClear(getPercepts().getNeighborTile(tempTile, Direction.RIGHT)) || isClear(getPercepts().getNeighborTile(tempTile, Direction.LEFT)))
				return true;
			
			tile=tempTile;
			
		}
		tile=currentTile;
		for (int i=0;i<10;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.RIGHT);
			
			if(tempTile.getBlock()!=null ){
				
				break;
			}
			//else if(getTheNeigborgsNumber(tempTile)==1)
			else if(isClear(getPercepts().getNeighborTile(tempTile, Direction.DOWN)) || isClear(getPercepts().getNeighborTile(tempTile, Direction.UP)))
				return true;
			 
			tile=tempTile;
			
		}
		tile=currentTile;
		for (int i=0;i<10;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.LEFT);
			
			if(tempTile.getBlock()!=null ){
				
				break;
			}
			
			//else if(getTheNeigborgsNumber(tempTile)==1)
			else if(isClear(getPercepts().getNeighborTile(tempTile, Direction.UP)) || isClear(getPercepts().getNeighborTile(tempTile, Direction.DOWN)))
				return true;
			tile=tempTile;
			
		}
		
		return false;
		
	}
	
	
	/** la methode qui renvoie la direction ce qu'on doit faire pour n'est pas se suicide
	 * 
	 * @param tile
	 * @return ?
	 * @throws StopRequestException
	 */
	private Direction getTheSafeDir(AiTile tile) throws StopRequestException{
		checkInterruption(); //APPEL OBLIGATOIRE
		
		
		List<Direction> dirs = new ArrayList<Direction>();
		
		
		tile=currentTile;
		
		for (int i=0;i<10;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.UP);
			
			if(tempTile.getBlock()!=null ){
				
				break;
			}
			//else if(getTheNeigborgsNumber(tempTile)==1)	
			else if(isClear(getPercepts().getNeighborTile(tempTile, Direction.RIGHT)) || isClear(getPercepts().getNeighborTile(tempTile, Direction.LEFT)))
				dirs.add(Direction.UP);
			
			tile=tempTile;
			
		}
		tile=currentTile;
		for (int i=0;i<10;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.DOWN);
			
			if(tempTile.getBlock()!=null ){
				
				break;
			}
			//else if(getTheNeigborgsNumber(tempTile)==1)
			else if(isClear(getPercepts().getNeighborTile(tempTile, Direction.RIGHT)) || isClear(getPercepts().getNeighborTile(tempTile, Direction.LEFT)))
				dirs.add(Direction.DOWN);
			
			tile=tempTile;
			
		}
		tile=currentTile;
		for (int i=0;i<10;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.RIGHT);
			
			if(tempTile.getBlock()!=null ){
				
				break;
			}
			//else if(getTheNeigborgsNumber(tempTile)==1)
			else if(isClear(getPercepts().getNeighborTile(tempTile, Direction.DOWN)) || isClear(getPercepts().getNeighborTile(tempTile, Direction.UP)))
				dirs.add(Direction.RIGHT);
			 
			tile=tempTile;
			
		}
		tile=currentTile;
		for (int i=0;i<10;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.LEFT);
			
			if(tempTile.getBlock()!=null ){
				
				break;
			}
			
			//else if(getTheNeigborgsNumber(tempTile)==1)
			else if(isClear(getPercepts().getNeighborTile(tempTile, Direction.UP)) || isClear(getPercepts().getNeighborTile(tempTile, Direction.DOWN)))
				dirs.add(Direction.LEFT);
			tile=tempTile;
			
		}
		
		if(dirs.size()>0){
			int rand = (int) Math.random()*dirs.size();
			return dirs.get(rand);
		}
		else return
			null;
		
	}
	
	/** la methode qui renvoie si il y a au moins un voisin tile qui est en securité
	 * 
	 * @param tile
	 * @return ?
	 * @throws StopRequestException
	 */
	private boolean isRegionSafe(AiTile tile) throws StopRequestException{
		
		checkInterruption(); //APPEL OBLIGATOIRE
		
		
		
		
		// liste des cases autour de la case de référence
		Collection<AiTile> neighborgs = getClearNeighborTiles(currentTile);
		// on garde les cases sans bloc ni bombe ni feu
		
	
		Iterator<AiTile> it = neighborgs.iterator();
	
		while(it.hasNext())
		{	checkInterruption(); //APPEL OBLIGATOIRE
			
			if(isCaseSafe(it.next()))
					return true;
		
		}
		return false;
	}
	
	/** la methode qui renvoie la liste des voisins
	 *  
	 * @param tile
	 * @return ?
	 * @throws StopRequestException
	 */
	private Collection<AiTile> getClearNeighborTiles(AiTile tile) throws StopRequestException
	{	checkInterruption();
		Collection<AiTile> result = new ArrayList<AiTile>();
		List<Direction> directions = Direction.getPrimaryValues();
		Iterator<Direction> d = directions.iterator();
		while(d.hasNext())
		{	checkInterruption();
			Direction dir = d.next();
			AiTile neighbor = getPercepts().getNeighborTile(tile, dir);
			if(isClear(neighbor))
				result.add(neighbor);
		}
		result = Collections.unmodifiableCollection(result);
		return result;
	}
}
