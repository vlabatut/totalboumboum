package tournament200809.akkayadanacioglu.v2;

import java.util.ArrayList;
import java.util.Collection;

import java.util.Iterator;

import org.totalboumboum.ai.adapter200809.AiAction;
import org.totalboumboum.ai.adapter200809.AiActionName;
import org.totalboumboum.ai.adapter200809.AiBlock;
import org.totalboumboum.ai.adapter200809.AiBomb;
import org.totalboumboum.ai.adapter200809.AiFire;
import org.totalboumboum.ai.adapter200809.AiHero;
import org.totalboumboum.ai.adapter200809.AiTile;
import org.totalboumboum.ai.adapter200809.AiZone;
import org.totalboumboum.ai.adapter200809.ArtificialIntelligence;
import org.totalboumboum.ai.adapter200809.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;









public class AkkayaDanacioglu extends ArtificialIntelligence
{
	
	/** la case occup�e actuellement par le personnage*/
	private AiTile currentTile;
	
	/** la case sur laquelle on veut aller */
	private AiTile nextTile = null;
	
	/** la derni�re case par laquelle on est pass� */ 
	private AiTile previousTile = null;
	
	/*Methode Main de jeu
	 * 
	 * (non-Javadoc)
	 * @see fr.free.totalboumboum.ai.adapter200809.ArtificialIntelligence#processAction()
	 */
	
	public AiAction processAction() throws StopRequestException
	{	
		
		checkInterruption(); //APPEL OBLIGATOIRE
		
		AiZone zone = getPercepts();
		AiHero ownHero = zone.getOwnHero();
		AiAction result = new AiAction(AiActionName.NONE);
		
		// si ownHero est null, c'est que l'IA est morte : inutile de continuer
		if(ownHero!=null)
		{	
			// on met � jour la position de l'ia dans la zone
			currentTile = ownHero.getTile();
			
			// premier appel : on initialise l'IA	
			if(nextTile == null)
				init();	
			
			/*
			 * si le caractere est dans la meme tile avec une bombe
			 */
			if(currentTile.getBombs().size()>0){
				
				//on prends les directions possibles
				ArrayList<Direction> dirs= getPossibleDirs(currentTile);
				
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
				ArrayList<Direction> dirs= getPossibleDirs(currentTile);
				
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
				
				// si mon case est en securit�
				if(!isInDanger(currentTile)){
					// on fait rien
					result=new AiAction(AiActionName.NONE);	
				}
				// si mon case n'est pas en secutit�
				else{
					// random action
					
					result=getNextTile();	
				}
			}
		}	
							
		return result;
	}
	
	private void init() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		nextTile = currentTile;		
		previousTile = currentTile;		
	}
	
	/**
	 * Choisit comme destination une case voisine de la case actuellement occup�e par l'IA.
	 * Cette case doit �tre accessible (pas de mur ou de bombe ou autre obstacle) et doit
	 * �tre diff�rente de la case pr�c�demment occup�e
	 * @throws StopRequestException 
	 */
	
	private ArrayList<AiTile> getClearNeighbors(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// liste des cases autour de la case de r�f�rence
		Collection<AiTile> neighbors = getPercepts().getNeighborTiles(tile);
		// on garde les cases sans bloc ni bombe ni feu
		ArrayList<AiTile> result = new ArrayList<AiTile>();
		Iterator<AiTile> it = neighbors.iterator();
		while(it.hasNext())
		{	checkInterruption(); //APPEL OBLIGATOIRE
		
			AiTile t = it.next();
			if(isClear(t))
				result.add(t);
		}
		return result;
	}
	
	private boolean isClear(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result;
		AiBlock block = tile.getBlock();
		Collection<AiBomb> bombs = tile.getBombs();
		Collection<AiFire> fires = tile.getFires();
		result = block==null && bombs.size()==0 && fires.size()==0;
		return result;
	}
	
	private void checkNextTile() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// si un obstacle est apparu sur la case destination, on change de destination
		if(!isClear(nextTile))
		{	// liste des cases voisines accessibles	
			ArrayList<AiTile> tiles = getClearNeighbors(currentTile);
			// on sort l'ancienne destination (qui est maintenant bloqu�e) de la liste
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
	
	
	
	private void pickNextTile() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
	// liste des cases voisines accessibles	
	ArrayList<AiTile> tiles = getClearNeighbors(currentTile);
	// on sort de la liste la case d'o� l'on vient (pour �viter de repasser au m�me endroit)
	boolean canGoBack = false;
	if(tiles.contains(previousTile))
	{	tiles.remove(previousTile);
		canGoBack = true;
	}
	// s'il reste des cases dans la liste
	if(tiles.size()>0)
	{	// si la liste contient la case situ�e dans la direction d�placement pr�cedente,
		// on �vite de l'utiliser (je veux avancer en zig-zag et non pas en ligne droite)
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
		// sinon (pas le choix) on continue dans la m�me direction
		else
		{	nextTile = tempTile;
			previousTile = currentTile;
		}
	}
	// sinon (pas le choix) on tente de revenir en arri�re
	else
	{	if(canGoBack)
		{	nextTile = previousTile;
			previousTile = currentTile;
		}
		// et sinon on ne peut pas bouger, donc on ne fait rien du tout
	}
	}
	
	// la methode qui renvoie la liste des bombes proche a notre hero
	
	
	/*
	 * renvoi la liste des bombes qui me menacent
	 */
	private ArrayList<AiBomb> dangerousBombs() throws StopRequestException{
		
		checkInterruption(); //APPEL OBLIGATOIRE
		
		// liste des bombes dans la zone
		Collection<AiBomb> bombs = getPercepts().getBombs();
		
		ArrayList<AiBomb> result = new ArrayList<AiBomb>();
	
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
	
	

	
	/*
	 *  isInDanger regarde a tile s'il y a danger
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
		
		
		
		// liste des cases autour de la case de r�f�rence
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
	
	
	/*
	 *  est qu'il y a un block entre les deux line
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
		
	
	/*
	 * regarde a direction entree comme un parametre (danger ou safe)
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
	
	
	
	/*
	 * renvoi la liste des directions possibles
	 */
	private ArrayList<Direction> getPossibleDirs(AiTile tile) throws StopRequestException{
		
		checkInterruption(); //APPEL OBLIGATOIRE
		
		// liste des cases autour de la case de r�f�rence
		Collection<AiTile> tiles = getClearNeighbors(tile);
		// on garde les cases sans bloc ni bombe ni feu
		ArrayList<Direction> result = new ArrayList<Direction>();
	
		Iterator<AiTile> it = tiles.iterator();
		
		while(it.hasNext()){
			
			result.add(getPercepts().getDirection(tile, it.next()));
			
		}
		
		return result;
	}
	
	
	
	// le comportement de promeneur
	private AiAction getNextTile() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
	AiAction result = new AiAction(AiActionName.NONE);
	
	
			
	if(nextTile == null)
		init();
	if(currentTile.getBombs().size()>0){
		
		ArrayList<Direction> dirs= getPossibleDirs(currentTile);
		
		for(int i=0;i<dirs.size();i++){
			
			if(!isItDangerousDir(dirs.get(i))){
				
				return result= new AiAction(AiActionName.MOVE,dirs.get(i));
				
			}
			
		}
		
	}
	// arriv� � destination : on choisit une nouvelle destination
	if(currentTile==nextTile)
		pickNextTile();
	
	// au cas ou quelqu'un prendrait le contr�le manuel du personnage
	
	else if(previousTile!=currentTile)
	{	previousTile = currentTile;
		pickNextTile();			
	}
	
	// sinon (on garde la m�me direction) on v�rifie qu'un obstacle (ex: bombe) n'est pas apparu dans la case
	else
		checkNextTile();
			
	// on calcule la direction � prendre
	
	Direction direction = getPercepts().getDirection(currentTile,nextTile);

	// on calcule l'action
	if(direction!=Direction.NONE)
		result = new AiAction(AiActionName.MOVE,direction);
	return result;
	}
	
	
}
