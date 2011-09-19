package org.totalboumboum.ai.v200809.ais.coremenkucukkarakurt.v1;

import java.util.ArrayList;
import java.util.Collection;
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
 * @author Selim Coremen
 * @author Umut Kucukkarak�rt
 *
 */
public class CoremenKucukkarakurt extends ArtificialIntelligence
{
	
	/** la case occup�e actuellement par le personnage*/
	private AiTile currentTile;
	
	/** la case sur laquelle on veut aller */
	private AiTile nextTile = null;
	
	/** la dernière case par laquelle on est pass� */ 
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
			
			// si il y a plus de 3 bombes
			if(getPercepts().getBombs().size()>=3){
				
				// si mon case est en securité
				if(isCaseSafe(currentTile)){
					
					// on fait rien
					result=new AiAction(AiActionName.NONE);
					
				}
				// si mon case n'est pas en secutit�
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
					 if(getBombPos().get(0).getCol()==currentTile.getCol() || getBombPos().get(0).getLine()==currentTile.getLine())
						// on fait un mouvement par hasard
						 result=comportementHasard(zone, ownHero);
					 else
						// sinon on mets une bombe
						 result= new AiAction(AiActionName.DROP_BOMB);
					
				 }
				 // sinon on mets une bombe
				 else
					 result=new AiAction(AiActionName.DROP_BOMB);
				
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
	
	private List<AiTile> getClearNeighbors(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// liste des cases autour de la case de r�f�rence
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
			List<AiTile> tiles = getClearNeighbors(currentTile);
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
	
	// la methode qui verifie si la case est en securité ou non
	private boolean isCaseSafe (AiTile tile) throws StopRequestException{
		checkInterruption(); //APPEL OBLIGATOIRE
		tile=currentTile;
		for (int i=0;i<5;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.UP);
			if(tempTile.getBombs().size()!=0 && tempTile.getBlock()==null)
				return false;
			tile=tempTile;
		}
		tile=currentTile;
		for (int i=0;i<5;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.DOWN);
			if(tempTile.getBombs().size()!=0 && tempTile.getBlock()==null)
				return false;
			tile=tempTile;
		}
		tile=currentTile;
		for (int i=0;i<5;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.RIGHT);
			if(tempTile.getBombs().size()!=0 && tempTile.getBlock()==null)
				return false;
			tile=tempTile;
		}
		tile=currentTile;
		for (int i=0;i<5;i++){
			checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tempTile =  getPercepts().getNeighborTile(tile, Direction.LEFT);
			if(tempTile.getBombs().size()!=0 && tempTile.getBlock()==null)
				return false;
			tile=tempTile;
		}
		if(currentTile.getBombs().size()!=0)
			return false;
		
		return true;
	}
	
	
	
	
	// la methode qui verifie si je suis en meme ligne ou en meme colonne avec un case choisi
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
	
	
	// comportement par hasard 
	private AiAction comportementHasard (AiZone alan, AiHero ownHero) throws StopRequestException{
		
		checkInterruption(); //APPEL OBLIGATOIRE
		
		AiAction result = new AiAction(AiActionName.NONE);
		
		// si ownHero est null, c'est que l'IA est morte : inutile de continuer
		
		// on met à jour la position de l'ia dans la zone
			
		currentTile = ownHero.getTile();

		// premier appel : on initialise l'IA
			
				
		if(nextTile == null)
			init();
				
		// arriv� à destination : on choisit une nouvelle destination
		if(currentTile==nextTile)
			pickNextTile();
		
		// au cas ou quelqu'un prendrait le contr�le manuel du personnage
		
		else if(previousTile!=currentTile)
		{	previousTile = currentTile;
			pickNextTile();			
		}
		
		// sinon (on garde la même direction) on v�rifie qu'un obstacle (ex: bombe) n'est pas apparu dans la case
		else
			checkNextTile();
				
		// on calcule la direction à prendre
		
		Direction direction = getPercepts().getDirection(currentTile,nextTile);

		// on calcule l'action
		if(direction!=Direction.NONE)
			result = new AiAction(AiActionName.MOVE,direction);
		return result;
	}
	
	
	// la methode qui renvoie la distance entre deux cases choisits
	private double getDistance(AiTile tile1,AiTile tile2) throws StopRequestException{
		
		checkInterruption(); //APPEL OBLIGATOIRE
		
		int x1=tile1.getCol();
		int y1=tile1.getLine();
		
		int x2=tile2.getCol();
		int y2=tile2.getLine();
		
		return Math.sqrt(Math.abs(x2-x1)*Math.abs(x2-x1)+Math.abs(y2-y1)*Math.abs(y2-y1));
		
	}
	
	
	// la methode qui verifie si je m'eloigne de la position d'une bombe
	@SuppressWarnings("unused")
	private boolean doesItMakesMeFar(AiTile tilebomb, AiTile tileNext) throws StopRequestException{
		
		checkInterruption(); //APPEL OBLIGATOIRE
		
		double dist1=getDistance(tilebomb,currentTile);
		double dist2=getDistance(tilebomb, tileNext);
		
		if(dist2>dist1)
			return true;
		else
			return false;
		
	}
	
	// la methode qui renvoi la liste des positions des bombes presentes
	private List<AiTile> getBombPos() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		
		// liste des cases autour de la case de r�f�rence
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
	
	
	private void pickNextTile() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
	// liste des cases voisines accessibles	
	List<AiTile> tiles = getClearNeighbors(currentTile);
	// on sort de la liste la case d'o� l'on vient (pour �viter de repasser au même endroit)
	boolean canGoBack = false;
	if(tiles.contains(previousTile))
	{	tiles.remove(previousTile);
		canGoBack = true;
	}
	// s'il reste des cases dans la liste
	if(tiles.size()>0)
	{	// si la liste contient la case situ�e dans la direction déplacement pr�cedente,
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
		// sinon (pas le choix) on continue dans la même direction
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
	// la methode pour s'enfuire avec un mouvement zigzag
	private AiTile getDirection(AiTile refTile) throws StopRequestException{
		
		List<AiTile> tiles = getClearNeighbors(currentTile);
		
		double min=10000000;
		int k=0;
		double dist= getDistance(refTile, currentTile);
		
		for (int i=0;i<tiles.size();i++){
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
	
	
	// la methode qui renvoie la position de la bombe la proche a son explosion
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
	
	// la methode qui renvoie la liste des bombes proche a notre hero
	private List<AiBomb> getCloseBombs() throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		
		// liste des cases autour de la case de r�f�rence
		Collection<AiBomb> bombs = getPercepts().getBombs();
		// on garde les cases sans bloc ni bombe ni feu
		List<AiBomb> result = new ArrayList<AiBomb>();
	
		Iterator<AiBomb> it = bombs.iterator();
	
		while(it.hasNext())
		{	checkInterruption(); //APPEL OBLIGATOIRE
			
			AiBomb t = it.next();
			if(getDistance(currentTile, t.getTile())<10)
				result.add(t);
		
		}
		return result;
	}	

}
