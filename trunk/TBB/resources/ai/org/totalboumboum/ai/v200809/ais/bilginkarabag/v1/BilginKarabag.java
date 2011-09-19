package org.totalboumboum.ai.v200809.ais.bilginkarabag.v1;

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
 * @author Ali Bilgin
 * @author Cagdas Emrah Karabag
 *
 */
public class BilginKarabag extends ArtificialIntelligence 
{
	/** la case occup�e actuellement par le personnage*/
	private AiTile currentTile;
	/** la case sur laquelle on veut aller */
	private AiTile nextTile = null;
	/** la dernière case par laquelle on est passé */ 
	private AiTile previousTile = null;
	
	public AiAction processAction() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		AiZone zone = getPercepts();
		AiHero ownHero = zone.getOwnHero();
		AiAction result = new AiAction(AiActionName.NONE);
		
		// si ownHero est null, c'est que l'IA est morte : inutile de continuer
		if(ownHero!=null)
		{	// on met à jour la position de l'ia dans la zone
			currentTile = ownHero.getTile();
			
			// premier appel : on initialise l'IA
			if(nextTile == null)
				init();
			
			// arriv� à destination : on choisit une nouvelle destination
			if(currentTile==nextTile)
				pickNextTile();
			// au cas ou quelqu'un prendrait le contr�le manuel du personnage
			else if(previousTile!=currentTile)
			{	
				previousTile = currentTile;
				pickNextTile();			
			}
			// sinon (on garde la même direction) on v�rifie qu'un obstacle (ex: bombe) n'est pas apparu dans la case
			else
				checkNextTile();
			
			// on calcule la direction à prendre
			Direction direction = getPercepts().getDirection(currentTile,nextTile);
	
			// on calcule l'action
			if(direction!=Direction.NONE) {
				if(nextTile.getHeroes().size() > 0) {
					nextTile = previousTile;
					direction = getPercepts().getDirection(currentTile, nextTile);
					result = new AiAction(AiActionName.DROP_BOMB,direction);
				} else {
					result = new AiAction(AiActionName.MOVE,direction);
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
	 * Cette case doit être accessible (pas de mur ou de bombe ou autre obstacle) et doit
	 * être différente de la case pr�c�demment occup�e
	 * @throws StopRequestException 
	 */
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
				boolean found = false;
				for (AiTile tile : tiles) {
					if (!dangerous(tile)) {
						nextTile = tile;
						found = true;
						break;
					}
				}
				if (!found) {
					nextTile = getSafeTile(currentTile);
				}
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
	
	private boolean dangerous(AiTile tile) throws StopRequestException {
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
			int minCol = bomb.getCol() - bomb.getRange();
			int maxCol = bomb.getCol() + bomb.getRange();
			int minLine = bomb.getLine() - bomb.getRange();
			int maxLine = bomb.getLine() + bomb.getRange();

			if (((currentCol <= maxCol && currentCol >= minCol 
					&& currentLine == bomb.getLine()) || (currentLine <= maxLine
					&& currentLine >= minLine && currentCol == bomb.getCol()))
					&& !foundBlock(bomb.getTile(), tile)) {
				result = true;
			}
		}

		return result;
	}

	private boolean foundBlock(AiTile tileFrom, AiTile tileTo)
			throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		AiZone zone = getPercepts();
		int col1 = tileFrom.getCol();
		int line1 = tileFrom.getLine();
		int col2 = tileTo.getCol();
		int line2 = tileTo.getLine();

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

	private AiTile getSafeTile(AiTile currentTile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		// once komsulardan guvenli olan ilkini secmeye calisacagiz.
		Collection<AiTile> neighbors = getClearNeighbors(currentTile);
		Iterator<AiTile> it = neighbors.iterator();
		while (it.hasNext()) {
			checkInterruption(); //APPEL OBLIGATOIRE
			
			AiTile neighborTile = it.next();
			if (isClear(neighborTile) && !dangerous(neighborTile)) {
				return neighborTile;
			}
		}
		// komsulardan hayir yok, oldugumuz yer guvende ise hareket etmeye gerek yok.
		if (!dangerous(currentTile) && isClear(currentTile)) {
			return currentTile;
		}
		// bomba var, mecburen bombadan uzaklasacagiz. dogru yonu secmemiz gerek.
		AiZone zone = getPercepts();
		Collection<AiBomb> bombs = zone.getBombs();
		int zone1, zone2, zone3, zone4;
		zone1 = zone2 = zone3 = zone4 = 0;
		Iterator<AiBomb> itBomb = bombs.iterator();
		while (itBomb.hasNext()) {
			checkInterruption(); //APPEL OBLIGATOIRE
			
			AiBomb bomb = itBomb.next();
			// hangi zone'da ise, o zone'un puanini arttir
			if (bomb.getCol() < currentTile.getCol() && bomb.getLine() <= currentTile.getLine()) {
				zone1++;
			} else if (bomb.getCol() >= currentTile.getCol() && bomb.getLine() < currentTile.getLine()) {
				zone2++;
			} else if (bomb.getCol() <= currentTile.getCol() && bomb.getLine() > currentTile.getLine()) {
				zone3++;
			} else {
				zone4++;
			}
		}
		// en dusuk puanli zone'a kacmaliyiz.
		int targetZone = zone1;
		
		if (zone2 < targetZone) {
			targetZone = zone2;
		} else if (zone3 < targetZone) {
				targetZone = zone3;
		} else if (zone4 < targetZone) {
				targetZone = zone4;
		}
		// yonu belirlemeliyiz.
		Direction dir = Direction.NONE;
		if (targetZone == zone1) {
			dir = Direction.LEFT;
		} else if (targetZone == zone2) {
				dir = Direction.UP;
		} else if (targetZone == zone3) {
				dir = Direction.DOWN;
		} else {
				dir = Direction.RIGHT;
		}
		AiTile targetTile = zone.getNeighborTile(currentTile, dir);
		if (dangerous(targetTile) && isClear(targetTile) 
				&& !dangerous(currentTile) && isClear(currentTile)) {
			targetTile = currentTile;
		}
		
		return targetTile;
	}
}
