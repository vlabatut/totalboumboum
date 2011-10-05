package tournament200910.dereligeckalan.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import tournament200910.dereligeckalan.v1.PathFinder;

import fr.free.totalboumboum.ai.adapter200910.ArtificialIntelligence;
import fr.free.totalboumboum.ai.adapter200910.communication.AiAction;
import fr.free.totalboumboum.ai.adapter200910.communication.AiActionName;
import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiBlock;
import fr.free.totalboumboum.ai.adapter200910.data.AiBomb;
import fr.free.totalboumboum.ai.adapter200910.data.AiFire;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;

import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.data.AiZone;
import fr.free.totalboumboum.engine.content.feature.Direction;

/**
 * >> ce texte est � remplacer par votre propre description de votre IA
 * 
 * classe principale de l'IA, qui d�finit son comportement. n'h�sitez pas �
 * d�composer le traitement en plusieurs classes, plus votre programme est
 * modulaire et plus il sera facile � d�bugger, modifier, relire, comprendre,
 * etc.
 */
public class DereliGeckalan extends ArtificialIntelligence {

	/** m�thode appel�e par le moteur du jeu pour obtenir une action de votre IA */

	private int matris[][] = null;
	private AiTile nextTile;
	private AiTile currentTile, targetTile;
	
	private LinkedList<AiTile> path;
	Direction direction = Direction.NONE;

	public AiAction processAction() throws StopRequestException { // avant tout
		// : test
		// d'interruption

		checkInterruption();
		matris = new int[getPercepts().getHeigh()][getPercepts().getWidth()];
		AiZone zone = getPercepts();

		AiHero ownHero = zone.getOwnHero();
		currentTile = zone.getOwnHero().getTile();
		targetTile = currentTile;
		AiAction result = new AiAction(AiActionName.NONE);
		
		if (ownHero != null) {
			// if(isSafe(currentTile)== false)
			initMatrice();
			if(nextTile == null)
				nextTile = currentTile;
			if (dangerZone().contains(currentTile)) {
				
				pickNextTile(findNext());
				if (nextTile!=null){
					direction = zone.getDirection(currentTile, nextTile);
					
					}
					if ( direction!=Direction.NONE &&direction != Direction.DOWNLEFT
							&& direction != Direction.DOWNRIGHT
							&& direction != Direction.UPLEFT
							&& direction != Direction.UPRIGHT) {

						
						
						//System.out.println("1");
						result = new AiAction(AiActionName.MOVE, direction);
					}

			} else 
				
				result = new AiAction(AiActionName.NONE);
			
		

		// AiAction result = new AiAction(AiActionName.MOVE);
		}
		return result;
	}

	/**
	 * une m�thode bidon pour l'exemple
	 * 
	 * @throws StopRequestException
	 */
	private void initMatrice() throws StopRequestException {
		//LinkedList<AiTile> danger = dangerZone();
		AiZone zone = getPercepts();

		for (int i = 0; i < zone.getHeigh(); i++) {
			checkInterruption();
			for (int j = 0; j < zone.getWidth(); j++) {
				checkInterruption();
				matris[i][j] = 0;
			}
		}

	}
	
	private AiTile findNext() throws StopRequestException {
		checkInterruption();
		LinkedList<AiTile> escapeTiles = new LinkedList<AiTile>();
		LinkedList<AiTile> temp = new LinkedList<AiTile>();
		int min = 1000;
		AiTile escapeTileTemp;
		escapeTiles = safeZone();

		Iterator<AiTile> itEscape = escapeTiles.iterator();

		while (itEscape.hasNext()) {
			checkInterruption(); // APPEL OBLIGATOIRE
			escapeTileTemp = itEscape.next();

			PathFinder pathFind = new PathFinder(this.getPercepts(),
					escapeTileTemp, this);
			// System.out.println("sakfj");

			temp = (LinkedList<AiTile>) pathFind.getPath();
			if ((min > temp.size() && !temp.isEmpty())
					|| escapeTileTemp.equals(getPercepts().getOwnHero()
							.getTile())) {
				min = temp.size();

				targetTile = escapeTileTemp;
			}
		}

		return targetTile;
	}

	private void pickNextTile(AiTile targettile) throws StopRequestException {
		checkInterruption();
		AiZone zone = getPercepts();
		// AiHero ownHero = zone.getOwnHero();

		// previousTile = currentTile;
		if (targettile != currentTile) {
			PathFinder tempPath = new PathFinder(zone, targettile, this);

			this.path = tempPath.getPath();
			if (path != null && !path.isEmpty()) {
				nextTile = path.poll();
				if (nextTile == currentTile) {
					if (path != null)
						nextTile = path.poll();
				}
			}
		} else {
			nextTile = currentTile;
		}

	}

	private LinkedList<AiTile> safeZone() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		LinkedList<AiTile> safeZone = new LinkedList<AiTile>();
		LinkedList<AiTile> dangerZone = new LinkedList<AiTile>();
		Collection<AiBlock> blocks = new ArrayList<AiBlock>();
		
		
		
		dangerZone = dangerZone();
		//System.out.println("danger"+dangerZone);
		blocks = getPercepts().getBlocks();
		Iterator<AiBlock> it1 = blocks.iterator();
		LinkedList<AiTile> blok = new LinkedList<AiTile>();
		while(it1.hasNext())
		{
			AiTile temp = it1.next().getTile();
			blok.add(temp);
		}
		for (int i = 0 ; i < getPercepts().getWidth() ; i++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int j = 0 ; j < getPercepts().getHeigh() ; j++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tile = getPercepts().getTile(j,i);
				if ( !dangerZone.contains(tile) && !blok.contains(tile))
					safeZone.add(tile);
			}
		}
		//System.out.println(safeZone);
		return safeZone;

	}

	@SuppressWarnings("unused")
	private boolean isSur(AiTile tile) throws StopRequestException {
		boolean res = true;
		AiZone zone = getPercepts();
		Collection<AiBlock> blok = zone.getBlocks();
		 
		Iterator<AiBlock> blokTile = blok.iterator();
		LinkedList<AiTile> tiles = new LinkedList<AiTile>();
		while(blokTile.hasNext())
		{
			AiTile temp = blokTile.next().getTile();
			tiles.add(temp);
		}
		int count = 0;
		AiTile up = tile.getNeighbor(Direction.UP);
		AiTile down = tile.getNeighbor(Direction.DOWN);
		AiTile right = tile.getNeighbor(Direction.RIGHT);
		AiTile left = tile.getNeighbor(Direction.LEFT);
		if(tiles.contains(down))
			count ++;
		if(tiles.contains(up))
			count ++;
		if(tiles.contains(left))
			count++; 
		if(tiles.contains(right))
			count ++;
		if(count <5)
			res = false;
		return res;
	}
	@SuppressWarnings("unused")
	private boolean isSafe(AiTile tile) throws StopRequestException {
		checkInterruption();

		boolean x = true;
		AiZone zone = getPercepts();
		// AiHero ownHero = zone.getOwnHero();

		if (dangerZone().contains(tile))
			;
		x = false;
		return x;
	}

	private LinkedList<AiTile> dangerZone() throws StopRequestException {

		checkInterruption();
		AiZone zone = getPercepts();

		LinkedList<AiTile> dangerZone = new LinkedList<AiTile>();
		Collection<AiBomb> bombs = zone.getBombs();
		Collection<AiFire> fires = zone.getFires();
		Collection<AiBlock> blocks = zone.getBlocks();
		LinkedList<AiTile> blokTile = new LinkedList<AiTile>();
		Iterator<AiBlock> it_blocks = blocks.iterator();
		Iterator<AiFire> itfires = fires.iterator();
		while (it_blocks.hasNext()) {
			checkInterruption();
			AiBlock blok = it_blocks.next();
			AiTile tile = blok.getTile();
			blokTile.add(tile);
		}
		if(fires.size()>0)
		{
			while(itfires.hasNext())
			{
				AiFire fire = itfires.next();
				AiTile temp = fire.getTile();
				dangerZone.add(temp);
			}
		}
		Iterator<AiBomb> it1 = bombs.iterator();
		if (bombs.size() > 0) {

			while (it1.hasNext()) {
				checkInterruption();

				AiBomb bomb = it1.next();

				int k = bomb.getRange();
				int x = bomb.getCol();
				int y = bomb.getLine();
				// System.out.printf(Integer.toString(x), Integer.toString(y));
				AiTile tempTile = zone.getTile(y, x);
				dangerZone.add(tempTile);
				AiTile tile1 = tempTile;
				int i = 0;
				while (i < k && !blocks.contains(tempTile)) {
					checkInterruption();
					AiTile tile = tempTile.getNeighbor(Direction.DOWN);
					// System.out.println("Asagi");
					tempTile = tile;
					if (!blokTile.contains(tile)) {
						// System.out.println("-1");
						dangerZone.add(tile);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tile1;

				while (i < k && !blocks.contains(tempTile)) {
					checkInterruption();
					AiTile tile = tempTile.getNeighbor(Direction.UP);
					// System.out.println("yukar�");
					tempTile = tile;
					if (!blokTile.contains(tile)) {
						// System.out.println("-2");
						dangerZone.add(tile);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tile1;
				while (i < k && !blocks.contains(tempTile)) {
					checkInterruption();
					AiTile tile = tempTile.getNeighbor(Direction.RIGHT);
					// System.out.println("sagai");
					tempTile = tile;
					if (!blokTile.contains(tile)) {
						// System.out.println("-3");
						dangerZone.add(tile);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tile1;
				while (i < k && !blocks.contains(tempTile)) {
					checkInterruption();
					AiTile tile = tempTile.getNeighbor(Direction.LEFT);
					// System.out.println("sola");
					tempTile = tile;
					if (!blokTile.contains(tile)) {
						// System.out.println("-4");
						dangerZone.add(tile);
					} else
						break;
					i++;
				}
				i = 0;
				tempTile = tile1;
			}

		}
		return dangerZone;
	}
}