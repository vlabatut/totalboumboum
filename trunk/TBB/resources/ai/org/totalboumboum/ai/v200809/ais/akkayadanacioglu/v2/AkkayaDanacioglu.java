package org.totalboumboum.ai.v200809.ais.akkayadanacioglu.v2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.Iterator;

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
 * @author Huseyin Akkaya
 * @author Hayko Danacioğlu
 *
 */
@SuppressWarnings("deprecation")
public class AkkayaDanacioglu extends ArtificialIntelligence
{
	
	/** la case occupée actuellement par le personnage*/
	private AiTile currentTile;
	
	/** la case sur laquelle on veut aller */
	private AiTile nextTile = null;
	
	/** la dernière case par laquelle on est passé */ 
	private AiTile previousTile = null;
	
	/*Methode Main de jeu
	 * 
	 * (non-Javadoc)
	 * @see fr.free.totalboumboum.ai.adapter200809.ArtificialIntelligence#processAction()
	 */
	@Override
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
			 * si le caractere est dans la meme tile avec une bombe
			 */
			if(currentTile.getBombs().size()>0){
				
				//on prends les directions possibles
				List<Direction> dirs= getPossibleDirs(currentTile);
				
				for(int i=0;i<dirs.size();i++){
					
					//si les directions ce qu'on ai obtenu n'est pas une direction dangeraux 
					if(!isItDangerousDir(dirs.get(i))){
						
						//move a la direction safe
						return result= new AiAction(AiActionName.MOVE,dirs.get(i));
						
					}
					
				}
				
			}
	
			//si la tile que je soit sur n'est pas en danger et s'il n'y a pas des bombes dangereaux
			if(!isInDanger(currentTile)&& dangerousBombs().size()<1){
				
				//les directions possibles
				List<Direction> dirs= getPossibleDirs(currentTile);
				
				for(int i=0;i<dirs.size();i++){
					
					//s'il y a une direction que je pousse echapper
					if(!isItDangerousDir(dirs.get(i))){
						//drop bomb
						return result= new AiAction(AiActionName.DROP_BOMB);
						
					}
					
				}
				
				
				
			}
			

			// s'il y a au minimum un bombe
			if(dangerousBombs().size()>=1){	
				
				// si mon case est en securité
				if(!isInDanger(currentTile)){
					// on fait rien
					result=new AiAction(AiActionName.NONE);	
				}
				// si mon case n'est pas en secutité
				else{
					// random action
					
					result=getNextTile();	
				}
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
	 * @param tile 
	 * @return
	 * 		?
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
	 * @return
	 * 		?
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
	
	// la methode qui verifie si je suis en meme ligne ou en meme colonne avec un case choisi
	
	// comportement par hasard 
	
	
	/**
	 * @throws StopRequestException 
	 * 
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
	
	// la methode qui renvoie la liste des bombes proche a notre hero
	
	
	/**
	 * renvoi la liste des bombes qui me menacent
	 * @return
	 * 		?
	 * @throws StopRequestException 
	 */
	private List<AiBomb> dangerousBombs() throws StopRequestException{
		
		checkInterruption(); //APPEL OBLIGATOIRE
		
		// liste des bombes dans la zone
		Collection<AiBomb> bombs = getPercepts().getBombs();
		
		List<AiBomb> result = new ArrayList<AiBomb>();
	
		Iterator<AiBomb> it = bombs.iterator();
		
		while(it.hasNext()){
			
			AiBomb tempbomb= it.next();
			int difx= Math.abs(currentTile.getLine()-tempbomb.getTile().getLine());
			int dify= Math.abs(currentTile.getCol()-tempbomb.getTile().getCol());
			if(difx<5 || dify<5)
				result.add(tempbomb);
			
			
		}
		
		return result;
	}
	
	

	
	/**
	 *  isInDanger regarde a tile s'il y a danger
	 * @param tile 
	 * @return
	 * 		?
	 * @throws StopRequestException 
	 */
	private boolean isInDanger(AiTile tile) throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		boolean result = false;
		int currentCol = tile.getCol();
		int currentLine = tile.getLine();
		AiZone zone = getPercepts();

		Collection<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> it = bombs.iterator();
		while (it.hasNext() && !result) {
			checkInterruption(); // APPEL OBLIGATOIRE
			AiBomb bomb = it.next();
			int boundryColMin = bomb.getCol() - bomb.getRange();
			int boundryColMax = bomb.getCol() + bomb.getRange();
			int boundryLineMin = bomb.getLine() - bomb.getRange();
			int boundryLineMax = bomb.getLine() + bomb.getRange();

			if (((currentCol <= boundryColMax && currentCol >= boundryColMin && currentLine == bomb
					.getLine()) || (currentLine <= boundryLineMax
					&& currentLine >= boundryLineMin && currentCol == bomb
					.getCol()))
					&& !isThereBlockBetween(bomb.getTile(), tile, zone)) {
				result = true;
			}
			
			
		}
		
		
		
		// liste des cases autour de la case de référence
		Collection<AiTile> neighborgs = getPercepts().getNeighborTiles(currentTile);
		// on garde les cases sans bloc ni bombe ni feu
		
		Iterator<AiTile> n = neighborgs.iterator();
	
		while(n.hasNext() && !result)
		{	checkInterruption(); //APPEL OBLIGATOIRE
			
			if(n.next().getFires().size()>0)
					result=true;
		
		}
		
		if(currentTile.getBombs().size()>0)
			result=true;
		

		return result;
	}
	
	
	/**
	 *  est qu'il y a un block entre les deux line
	 * @param tile1 
	 * @param tile2 
	 * @param zone 
	 * @return
	 * 		?
	 * @throws StopRequestException 
	 */
	private boolean isThereBlockBetween(AiTile tile1, AiTile tile2, AiZone zone)throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		int col1 = tile1.getCol();
		int line1 = tile1.getLine();
		int col2 = tile2.getCol();
		int line2 = tile2.getLine();

		if (line1 == line2) {
			if (col1 < col2) {
				for (int i = col2 - 1; i >= col1 + 1; i--) {
					checkInterruption(); // APPEL OBLIGATOIRE
					if (zone.getTile(line1, i).getBlock() != null) {
						return true;
					}
				}
			} else {
					for (int i = col2 + 1; i <= col1 - 1; i++) {
						checkInterruption(); // APPEL OBLIGATOIRE
						if (zone.getTile(line1, i).getBlock() != null) {
							return true;
						}
					}
			}
		} else if (col1 == col2) {
					if (line1 < line2) {
						for (int i = line2 - 1; i >= line1 + 1; i--) {
							checkInterruption(); // APPEL OBLIGATOIRE
							if (zone.getTile(i, col1).getBlock() != null) {
								return true;
							}
						}
					} else {
							for (int i = line2 + 1; i <= line1 - 1; i++) {
								checkInterruption(); // APPEL OBLIGATOIRE
								if (zone.getTile(i, col1).getBlock() != null) {
									return true;
								}
							}
					}
		}

		return false;
	}
		
	
	/**
	 * regarde a direction entree comme un parametre (danger ou safe)
	 * @param dir 
	 * @return
	 * 		?
	 * @throws StopRequestException 
	 */
	private boolean isItDangerousDir(Direction dir) throws StopRequestException{

			AiTile tile=currentTile;
			for(int i=0;i<10;i++){
				AiTile temptTile= tile.getNeighbor(dir);
				if(temptTile.getBlock()!=null)
					break;
				
				//est qu'on peut bouger vertical quand on est sur la direction horizontal
				if(dir.isHorizontal()){
					if(getPossibleDirs(temptTile).contains(Direction.DOWN) || getPossibleDirs(temptTile).contains(Direction.UP))
						return false;
				}
				//est qu'on peut bouger horizontal quand on est sur la direction vertical 
				else if(dir.isVertical()){
					if(getPossibleDirs(temptTile).contains(Direction.RIGHT) || getPossibleDirs(temptTile).contains(Direction.LEFT))
						return false;
				}

				tile=temptTile;
			}
			
			return true;
	}
	
	
	
	/**
	 * renvoi la liste des directions possibles
	 * @param tile 
	 * @return
	 * 		?
	 * @throws StopRequestException 
	 */
	private List<Direction> getPossibleDirs(AiTile tile) throws StopRequestException{
		
		checkInterruption(); //APPEL OBLIGATOIRE
		
		// liste des cases autour de la case de référence
		Collection<AiTile> tiles = getClearNeighbors(tile);
		// on garde les cases sans bloc ni bombe ni feu
		List<Direction> result = new ArrayList<Direction>();
	
		Iterator<AiTile> it = tiles.iterator();
		
		while(it.hasNext()){
			
			result.add(getPercepts().getDirection(tile, it.next()));
			
		}
		
		return result;
	}
	
	
	
	/**
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	// le comportement de promeneur
	private AiAction getNextTile() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
	AiAction result = new AiAction(AiActionName.NONE);
	
	
			
	if(nextTile == null)
		init();
	if(currentTile.getBombs().size()>0){
		
		List<Direction> dirs= getPossibleDirs(currentTile);
		
		for(int i=0;i<dirs.size();i++){
			
			if(!isItDangerousDir(dirs.get(i))){
				
				return result= new AiAction(AiActionName.MOVE,dirs.get(i));
				
			}
			
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
	
	
}
