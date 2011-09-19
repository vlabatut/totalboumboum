package org.totalboumboum.ai.v200910.ais.dereligeckalan.v3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiItemType;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;



/**
 * >> ce texte est à remplacer par votre propre description de votre IA
 * 
 * classe principale de l'IA, qui définit son comportement. n'hésitez pas �
 * décomposer le traitement en plusieurs classes, plus votre programme est
 * modulaire et plus il sera facile à débugger, modifier, relire, comprendre,
 * etc.
 */
public class DereliGeckalan extends ArtificialIntelligence {

	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */

	private int matris[][] = null;
	private AiTile nextTile;
	private AiTile currentTile, targetTile;

	private LinkedList<AiTile> path;
	private boolean mur = false;
	private boolean bonus = false;
	private AiTile murDestructible;
	Direction direction = Direction.NONE;

	public AiAction processAction() throws StopRequestException { // avant tout
		// : test
		// d'interruption

		checkInterruption();
		matris = new int[getPercepts().getHeight()][getPercepts().getWidth()];
		AiZone zone = getPercepts();

		//AiHero ownHero = zone.getOwnHero();
		currentTile = zone.getOwnHero().getTile();
		//int line = currentTile.getLine();
		//int col = currentTile.getCol();

		targetTile = currentTile;
		murDestructible = currentTile;
		AiAction result = new AiAction(AiActionName.NONE);
		initMatrice();
		// String s = toString();
		// System.out.println(s);
		if (dangerZone().contains(currentTile)) {
			// System.out.println("1");
			pickNextTile(findNext());
			if (danger() == false) {
				/*
				 * String s = toTileString(nextTile); String s1 =
				 * toTileString(currentTile); String s2 =
				 * toTileString(targetTile); System.out.println("next" + s1);
				 * System.out.println("current" + s);
				 * System.out.println("target" + s2);
				 */
				mur = false;
				direction = zone.getDirection(currentTile, nextTile);
				result = new AiAction(AiActionName.MOVE, direction);
			} else {
				result = new AiAction(AiActionName.NONE);
			}

		}

		else if (bonusZone() != null){
			//bonus = true;
			 pickNextTile(findNextbonus());
			 direction = zone.getDirection(currentTile, nextTile);
			 result = new AiAction(AiActionName.MOVE,direction);
			System.out.println("1");
		}
		if(!bonus && zone.getOwnHero().getBombCount() == 0){
			
			pickNextTile(findNextmur());
			
			direction = zone.getDirection(currentTile, nextTile);
			if(currentTile == murDestructible && mur)
			{
				if(!isDangerBomb())
				result = new AiAction(AiActionName.DROP_BOMB);
				
			}
			else {
			result = new AiAction(AiActionName.MOVE,direction);
			}
			//System.out.println(nextTile.toString());
		}
		// AiAction result = new AiAction(AiActionName.MOVE);
		
		/*else if(mur == true && murDestructible == currentTile){
			result = new AiAction(AiActionName.DROP_BOMB,Direction.NONE);
			mur = false;
		}*/
		mur = false;
		bonus = false;
		
		return result;
	}

	/**
	 * une méthode bidon pour l'exemple
	 * 
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private String toTileString(AiTile tile) {
		String res = "";
		int x = tile.getLine();
		int y = tile.getCol();
		res = res + String.valueOf(x) + "," + String.valueOf(y);
		return res;
	}


	private void initMatrice() throws StopRequestException {
		// LinkedList<AiTile> danger = dangerZone();
		AiZone zone = getPercepts();

		for (int i = 0; i < zone.getHeight(); i++) {
			checkInterruption();
			for (int j = 0; j < zone.getWidth(); j++) {
				checkInterruption();
				matris[i][j] = 0;
			}
		}
		Collection<AiBlock> blocks = zone.getBlocks();
		LinkedList<AiTile> blokTile = new LinkedList<AiTile>();
		Iterator<AiBlock> it_blocks = blocks.iterator();

		while (it_blocks.hasNext()) {
			checkInterruption();
			AiBlock blok = it_blocks.next();
			AiTile tile = blok.getTile();
			blokTile.add(tile);
		}
		LinkedList<AiTile> dangerZone = new LinkedList<AiTile>();
		LinkedList<AiTile> safeZone = safeZone();
		Collection<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> itBomb = bombs.iterator();
		while (itBomb.hasNext()) {
			AiBomb bomb = itBomb.next();
			double temp = bomb.getNormalDuration() - bomb.getTime();
			AiTile tile2 = bomb.getTile();
			int x = tile2.getCol();
			int y = tile2.getLine();
			// System.out.println("eeeee");
			matris[y][x] = (int) (bomb.getNormalDuration());

			int k = bomb.getRange();
			int i = 0;
			AiTile tile1 = tile2;

			while (i < k && !blocks.contains(tile2)) {
				checkInterruption();
				AiTile tile = tile2.getNeighbor(Direction.DOWN);
				// System.out.println("Asagi");
				tile2 = tile;
				if (!blokTile.contains(tile)) {
					// System.out.println("-1");
					dangerZone.add(tile);
					int a = tile.getLine();
					int b = tile.getCol();
					matris[a][b] = (int) (temp);
				} else
					break;
				i++;
			}
			i = 0;
			tile2 = tile1;

			while (i < k && !blocks.contains(tile2)) {
				checkInterruption();
				AiTile tile = tile2.getNeighbor(Direction.UP);
				// System.out.println("yukar�");
				tile2 = tile;
				if (!blokTile.contains(tile)) {
					// System.out.println("-2");
					dangerZone.add(tile);
					int a = tile.getLine();
					int b = tile.getCol();
					matris[a][b] = (int) (temp);
				} else
					break;
				i++;
			}
			i = 0;
			tile2 = tile1;
			while (i < k && !blocks.contains(tile2)) {
				checkInterruption();
				AiTile tile = tile2.getNeighbor(Direction.RIGHT);
				// System.out.println("sagai");
				tile2 = tile;
				if (!blokTile.contains(tile)) {
					// System.out.println("-3");
					dangerZone.add(tile);
					int a = tile.getLine();
					int b = tile.getCol();
					matris[a][b] = (int) (temp);
				} else
					break;
				i++;
			}
			i = 0;
			tile2 = tile1;
			while (i < k && !blocks.contains(tile2)) {
				checkInterruption();
				AiTile tile = tile2.getNeighbor(Direction.LEFT);
				// System.out.println("sola");
				tile2 = tile;
				if (!blokTile.contains(tile)) {
					// System.out.println("-4");
					dangerZone.add(tile);
					int a = tile.getLine();
					int b = tile.getCol();
					matris[a][b] = (int) (temp);
				} else
					break;
				i++;
			}
			i = 0;
			tile2 = tile1;
		}

		Iterator<AiTile> it2 = safeZone.iterator();
		while (it2.hasNext()) {
			AiTile temp = it2.next();
			int x = temp.getLine();
			int y = temp.getCol();
			matris[x][y] = -1;
		}

	}

	public String toString() {
		String result = "";
		for (int i = 0; i < getPercepts().getHeight(); i++) {
			for (int j = 0; j < getPercepts().getWidth(); j++)
				result += "(" + i + "," + j + ")" + matris[i][j] + "   ";
			result += "\n";
		}

		return result;
	}
	private boolean surPath(LinkedList<AiTile> tile) throws StopRequestException {
		checkInterruption();
		boolean res = true;
		Iterator<AiTile> it1 = tile.iterator();
		while(it1.hasNext())
		{
			checkInterruption();
			AiTile temp = it1.next();
			if(isSafe(temp) == true)
				res = true;
			else {
				res = false;
				break;
			}
		}
		return res;
	}
	private AiTile findNextbonus() throws StopRequestException {
		checkInterruption();
		LinkedList<AiTile> escapeTiles = new LinkedList<AiTile>();
		LinkedList<AiTile> temp = new LinkedList<AiTile>();
		int min = 1000;
		AiTile escapeTileTemp;
		escapeTiles = bonusZone();

		Iterator<AiTile> itEscape = escapeTiles.iterator();

		while (itEscape.hasNext()) {
			checkInterruption(); // APPEL OBLIGATOIRE
			escapeTileTemp = itEscape.next();

			PathFinder pathFind = new PathFinder(this.getPercepts(),
					escapeTileTemp, this);
			// System.out.println("sakfj");

			temp = (LinkedList<AiTile>) pathFind.getPath();
			
			if ((min > temp.size() && min > 0 && !temp.isEmpty() && surPath(temp) == true)
					|| escapeTileTemp.equals(getPercepts().getOwnHero()
							.getTile())) {
				min = temp.size();

				targetTile = escapeTileTemp;
				bonus = true;
			}
		}
		
		return targetTile;
	}
	@SuppressWarnings("unused")
	private int nombreMur(AiTile tile) throws StopRequestException {
		checkInterruption();
		int res = 0;
		AiZone zone = getPercepts();
		AiHero ownHero = zone.getOwnHero();
		
		int x = ownHero.getBombRange();
		
		int i = 0;
		AiTile right = tile.getNeighbor(Direction.RIGHT);
		AiTile left = tile.getNeighbor(Direction.LEFT);
		AiTile down = tile.getNeighbor(Direction.DOWN);
		AiTile up = tile.getNeighbor(Direction.UP);
		boolean mur = false;
		while(i<x)
		{
			checkInterruption();

			while(!blokZone().contains(right) )
			{
				checkInterruption();
				i++;
				right = right.getNeighbor(Direction.RIGHT);
				System.out.println("3");
			}
			if(i != x)
			{
				res++;
			}
			i = 0;
			while(!blokZone().contains(left) )
			{
				checkInterruption();
				i++;
				left = left.getNeighbor(Direction.LEFT);
				System.out.println("4");
			}
			if(i != x)
			{
				res++;
			}
			i = 0;
			while(!blokZone().contains(down))
			{
				checkInterruption();
				i++;
				down = down.getNeighbor(Direction.DOWN);
				System.out.println("5");
			}
			if(i != x)
			{
				res++;
			}
			i = 0;
			while(!blokZone().contains(up))
			{
				checkInterruption();
				i++;
				up = up.getNeighbor(Direction.UP);
				System.out.println("6");
			}
			if(i != x)
			{
				res++;
			}
			i = 0;
		}
		i=0;
		return res;
	}
	
	private LinkedList<AiTile> blokZone() throws StopRequestException {
		checkInterruption();
		Collection<AiBlock> blokZone1 = getPercepts().getBlocks();
		LinkedList<AiTile> res = new LinkedList<AiTile>();
		Iterator<AiBlock> it1 = blokZone1.iterator();
		while(it1.hasNext())
		{
			checkInterruption();
			AiBlock temp = it1.next();
			AiTile tempTile = temp.getTile();
			res.add(tempTile);
		}
		return res;
	}
	/*private AiTile explodeMur() throws StopRequestException {
		AiZone zone = getPercepts();
		AiTile tile = zone.getOwnHero().getTile();
		AiTile res = tile;
		Collection<AiBlock> bloks = zone.getBlocks();
		Iterator<AiBlock> it1 = bloks.iterator();
		while(it1.hasNext())
		{
			AiTile temp =it1.next().getTile();
			
		}
		
	}*/
	private AiTile findNext() throws StopRequestException {
		checkInterruption();
		LinkedList<AiTile> escapeTiles = new LinkedList<AiTile>();
		LinkedList<AiTile> temp = new LinkedList<AiTile>();
		int min = Integer.MAX_VALUE;
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
			//String ss = String.valueOf(temp.size());
			//System.out.println(ss);
			if ((min > temp.size() && !temp.isEmpty()) || escapeTileTemp.equals(getPercepts().getOwnHero().getTile())) {
				min = temp.size();
				// System.out.printf(String.valueOf(min));
				// System.out.println(s);
				targetTile = escapeTileTemp;
			}
		}

		return targetTile;
	}

	@SuppressWarnings("unused")
	private AiTile escapeTile() throws StopRequestException {
		checkInterruption();
		AiTile resultat = currentTile;
		List<Integer> times = new ArrayList<Integer>();
		Collection<AiBomb> bombs = getPercepts().getBombs();
		LinkedList<AiTile> bombTiles = new LinkedList<AiTile>();
		Iterator<AiBomb> it1 = bombs.iterator();
		while (it1.hasNext()) {

			if (it1.next().getBlast().contains(currentTile)
					|| it1.next().getTile() == currentTile) {
				AiTile temp = it1.next().getTile();
				bombTiles.add(temp);
				int time = (int) (it1.next().getNormalDuration() - it1.next()
						.getTime());
				times.add(time);
			}
		}

		return resultat;
	}

	@SuppressWarnings("unused")
	private LinkedList<AiTile> bombRange(AiBomb bomb)
			throws StopRequestException {
		LinkedList<AiTile> resultat = new LinkedList<AiTile>();

		return resultat;
	}

	private boolean danger() throws StopRequestException {
		checkInterruption();
		boolean danger = false;
		LinkedList<AiTile> dangerTiles = dangerZone();
		if (dangerTiles.contains(currentTile)
				&& dangerTiles.contains(targetTile)) {
			danger = true;
		}
		return danger;
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

	@SuppressWarnings("unused")
	private List<Double> bombTimes() throws StopRequestException {
		checkInterruption();
		List<Double> bombTimes = new ArrayList<Double>();
		Collection<AiBomb> bombs = getPercepts().getBombs();
		Iterator<AiBomb> it1 = bombs.iterator();
		while (it1.hasNext()) {
			double temp = it1.next().getNormalDuration();
			bombTimes.add(temp);
		}
		return bombTimes;

	}

	
	private LinkedList<AiTile> safeZone1() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		LinkedList<AiTile> safeZone = new LinkedList<AiTile>();
		LinkedList<AiTile> dangerZone = new LinkedList<AiTile>();
		Collection<AiBlock> blocks = new ArrayList<AiBlock>();

		dangerZone = dangerZone1(getPercepts().getOwnHero().getTile());
		// System.out.println("danger"+dangerZone);
		blocks = getPercepts().getBlocks();
		Iterator<AiBlock> it1 = blocks.iterator();
		LinkedList<AiTile> blok = new LinkedList<AiTile>();
		while (it1.hasNext()) {
			AiTile temp = it1.next().getTile();
			blok.add(temp);
		}
		for (int i = 0; i < getPercepts().getWidth(); i++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int j = 0; j < getPercepts().getHeight(); j++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tile = getPercepts().getTile(j, i);
				if (!dangerZone.contains(tile) && !blok.contains(tile))
					safeZone.add(tile);
			}
		}
		// System.out.println(safeZone);
		return safeZone;

	}

	private LinkedList<AiTile> safeZone() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		LinkedList<AiTile> safeZone = new LinkedList<AiTile>();
		LinkedList<AiTile> dangerZone = new LinkedList<AiTile>();
		Collection<AiBlock> blocks = new ArrayList<AiBlock>();

		dangerZone = dangerZone();
		// System.out.println("danger"+dangerZone);
		blocks = getPercepts().getBlocks();
		Iterator<AiBlock> it1 = blocks.iterator();
		LinkedList<AiTile> blok = new LinkedList<AiTile>();
		while (it1.hasNext()) {
			AiTile temp = it1.next().getTile();
			blok.add(temp);
		}
		for (int i = 0; i < getPercepts().getWidth(); i++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int j = 0; j < getPercepts().getHeight(); j++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tile = getPercepts().getTile(j, i);
				if (!dangerZone.contains(tile) && !blok.contains(tile))
					safeZone.add(tile);
			}
		}
		// System.out.println(safeZone);
		return safeZone;

	}



	@SuppressWarnings("unused")
	private boolean isSur(AiTile tile) throws StopRequestException {
		checkInterruption();
		boolean res = true;
		AiZone zone = getPercepts();
		Collection<AiBlock> blok = zone.getBlocks();

		Iterator<AiBlock> blokTile = blok.iterator();
		LinkedList<AiTile> tiles = new LinkedList<AiTile>();
		while (blokTile.hasNext()) {
			AiTile temp = blokTile.next().getTile();
			tiles.add(temp);
		}
		int count = 0;
		AiTile up = tile.getNeighbor(Direction.UP);
		AiTile down = tile.getNeighbor(Direction.DOWN);
		AiTile right = tile.getNeighbor(Direction.RIGHT);
		AiTile left = tile.getNeighbor(Direction.LEFT);
		if (tiles.contains(down))
			count++;
		if (tiles.contains(up))
			count++;
		if (tiles.contains(left))
			count++;
		if (tiles.contains(right))
			count++;
		if (count < 5)
			res = false;
		return res;
	}
	private LinkedList<AiTile> murZone() throws StopRequestException {
		checkInterruption();
		LinkedList<AiTile> res = new LinkedList<AiTile>();
		AiZone zone = getPercepts();
		Collection<AiBlock> blok = zone.getBlocks();
		Iterator<AiBlock> it1 = blok.iterator();
		while(it1.hasNext())
		{
			checkInterruption();
			AiBlock temp1 = it1.next();
			AiTile temp2 = temp1.getTile();
			if(temp1.isDestructible())
			{
				
				
				//System.out.println(temp1.toString());
				res.add(temp2);
			}
		}
		
		return res;
	}
	private AiTile findNextmur() throws StopRequestException {
		checkInterruption();
		LinkedList<AiTile> escapeTiles = new LinkedList<AiTile>();
		LinkedList<AiTile> temp = new LinkedList<AiTile>();
		int min = Integer.MAX_VALUE;
		AiTile escapeTileTemp;
		escapeTiles = murZone();

		Iterator<AiTile> itEscape = escapeTiles.iterator();

		while (itEscape.hasNext()) {
			checkInterruption(); // APPEL OBLIGATOIRE
			escapeTileTemp = itEscape.next();
			AiTile up = escapeTileTemp.getNeighbor(Direction.UP);
			AiTile down = escapeTileTemp.getNeighbor(Direction.DOWN);
			AiTile left = escapeTileTemp.getNeighbor(Direction.LEFT);
			AiTile right = escapeTileTemp.getNeighbor(Direction.RIGHT);
			if(!dangerZone().contains(up) && !blokZone().contains(up))
			{
			PathFinder pathFind = new PathFinder(this.getPercepts(),
					up, this);
			// System.out.println("sakfj");

			temp = (LinkedList<AiTile>) pathFind.getPath();
			//String ss = String.valueOf(temp.size());
			
			
			//System.out.println(ss);
			if ((min > temp.size() && !temp.isEmpty()) && surPath(temp) == true || up.equals(getPercepts().getOwnHero().getTile())) {
				min = temp.size();
				// System.out.printf(String.valueOf(min));
				// System.out.println(s);
				targetTile = up;
				mur = true;
			}
			}
			if(!dangerZone().contains(down) && !blokZone().contains(down))
			{
			PathFinder pathFind = new PathFinder(this.getPercepts(),
					down, this);
			// System.out.println("sakfj");

			temp = (LinkedList<AiTile>) pathFind.getPath();
			//String ss = String.valueOf(temp.size());
			
			
			//System.out.println(ss);
			if ((min > temp.size() && !temp.isEmpty()) && surPath(temp) == true || down.equals(getPercepts().getOwnHero().getTile())) {
				min = temp.size();
				// System.out.printf(String.valueOf(min));
				// System.out.println(s);
				targetTile = down;
				mur = true;
			}
			}
			if(!dangerZone().contains(left) && !blokZone().contains(left))
			{
			PathFinder pathFind = new PathFinder(this.getPercepts(),
					left, this);
			// System.out.println("sakfj");

			temp = (LinkedList<AiTile>) pathFind.getPath();
			//String ss = String.valueOf(temp.size());
			
			
			//System.out.println(ss);
			if ((min > temp.size() && !temp.isEmpty()) && surPath(temp) == true|| left.equals(getPercepts().getOwnHero().getTile())) {
				min = temp.size();
				// System.out.printf(String.valueOf(min));
				// System.out.println(s);
				targetTile = left;
				mur = true;
			}
			}
			if(!dangerZone().contains(right) && !blokZone().contains(right))
			{
			PathFinder pathFind = new PathFinder(this.getPercepts(),
					right, this);
			// System.out.println("sakfj");

			temp = (LinkedList<AiTile>) pathFind.getPath();
			//String ss = String.valueOf(temp.size());
			
			
			//System.out.println(ss);
			if ((min > temp.size() && !temp.isEmpty()) && surPath(temp) == true|| right.equals(getPercepts().getOwnHero().getTile())) {
				min = temp.size();
				// System.out.printf(String.valueOf(min));
				// System.out.println(s);
				targetTile = right;
				mur = true;
			}
			}
		}
		System.out.println(targetTile.toString());
		
		murDestructible = targetTile;
		return targetTile;
	}

	private boolean isSafe(AiTile tile) throws StopRequestException {
		checkInterruption();

		boolean x = true;
		//AiZone zone = getPercepts();
		// AiHero ownHero = zone.getOwnHero();

		if (dangerZone().contains(tile))
			
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
		if (fires.size() > 0) {
			while (itfires.hasNext()) {
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
	private LinkedList<AiTile> dangerZone1(AiTile tile) throws StopRequestException {
		LinkedList<AiTile> res = dangerZone();
		int x = getPercepts().getOwnHero().getBombRange();
		int i = 0;
		AiTile up = tile.getNeighbor(Direction.UP);
		AiTile down = tile.getNeighbor(Direction.DOWN);
		AiTile left = tile.getNeighbor(Direction.LEFT);
		AiTile right = tile.getNeighbor(Direction.RIGHT);
		if(!blokZone().contains(up))
			res.add(up);
		if(!blokZone().contains(down))
			res.add(down);
		if(!blokZone().contains(left))
			res.add(left);
		if(!blokZone().contains(right))
			res.add(right);
		while (i<x && !blokZone().contains(right))
		{
			checkInterruption();
			right = right.getNeighbor(Direction.RIGHT);
			// System.out.println("sola");
			
			if (!blokZone().contains(right)) {
				// System.out.println("-4");
				res.add(right);
			} else
				break;
			i++;
		}
		i = 0;
		while (i<x && !blokZone().contains(left))
		{
			checkInterruption();
			
			left = left.getNeighbor(Direction.LEFT);
			// System.out.println("sola");
			
			if (!blokZone().contains(left)) {
				// System.out.println("-4");
				res.add(left);
			} else
				break;
			i++;
		}
		i = 0;
		while (i<x && !blokZone().contains(down))
		{
			checkInterruption();
			down = down.getNeighbor(Direction.DOWN);
			// System.out.println("sola");
			
			if (!blokZone().contains(down)) {
				// System.out.println("-4");
				res.add(down);
			} else
				break;
			i++;
		}
		i = 0;
		while (i<x && !blokZone().contains(up))
		{
			checkInterruption();
			right = right.getNeighbor(Direction.UP);
			// System.out.println("sola");
			
			if (!blokZone().contains(up)) {
				// System.out.println("-4");
				res.add(up);
			} else
				break;
			i++;
		}
		i = 0;
		return res;
	}
	
	private boolean isDangerBomb() throws StopRequestException {
		boolean res = false;
		
			checkInterruption();
			LinkedList<AiTile> path1 = new LinkedList<AiTile>();
			LinkedList<AiTile> escapeTiles = new LinkedList<AiTile>();
			LinkedList<AiTile> temp = new LinkedList<AiTile>();
			int min = Integer.MAX_VALUE;
			AiTile escapeTileTemp;
			escapeTiles = safeZone1();
			AiTile tile1 = currentTile;
			Iterator<AiTile> itEscape = escapeTiles.iterator();

			while (itEscape.hasNext()) {
				checkInterruption(); // APPEL OBLIGATOIRE
				escapeTileTemp = itEscape.next();

				PathFinder pathFind = new PathFinder(this.getPercepts(),
						escapeTileTemp, this);
				// System.out.println("sakfj");

				temp = (LinkedList<AiTile>) pathFind.getPath();
				//String ss = String.valueOf(temp.size());
				//System.out.println(ss);
				if ((min > temp.size() && !temp.isEmpty()) || escapeTileTemp.equals(getPercepts().getOwnHero().getTile())) {
					min = temp.size();
					// System.out.printf(String.valueOf(min));
					// System.out.println(s);
					tile1 = escapeTileTemp;
				}
				if(tile1 != currentTile)
				{
					PathFinder tempPath = new PathFinder(getPercepts(), tile1, this);

					path1 = tempPath.getPath();
					if (path1 == null) {
						res = true;
					}
					
			}
			
			}
		
		return res;
	}
	private LinkedList<AiTile> bonusZone() throws StopRequestException {
		checkInterruption();
		LinkedList<AiTile> bonusZone = new LinkedList<AiTile>();
		Collection<AiItem> bonus = getPercepts().getItems();
		Iterator<AiItem> it1 = bonus.iterator();
		while (it1.hasNext()) {
			AiTile temp = it1.next().getTile();
			bonusZone.add(temp);
		}
		return bonusZone;

	}

	@SuppressWarnings("unused")
	private AiTile findNextbon() throws StopRequestException {
		checkInterruption();
		LinkedList<AiTile> bonusTile = new LinkedList<AiTile>();
		Collection<AiItem> items = getPercepts().getItems();
		Iterator<AiItem> it1 = items.iterator();
		boolean number = false;
		boolean range = false;
		if (getPercepts().getOwnHero().getBombNumber() < 5) {
			while (it1.hasNext()) {
				checkInterruption();
				if (it1.next().getType() == AiItemType.EXTRA_BOMB) {
					AiTile temp = it1.next().getTile();
					bonusTile.add(temp);
				}
			}
		}
		LinkedList<AiTile> bonuslar = bonusTile;
		LinkedList<AiTile> temp = new LinkedList<AiTile>();

		int min = 1000;
		Iterator<AiTile> goTile = bonuslar.iterator();
		while (goTile.hasNext()) {
			AiTile escapeTileTemp = goTile.next();
			PathFinder pathFind = new PathFinder(this.getPercepts(),
					escapeTileTemp, this);
			// System.out.println("sakfj");

			temp = (LinkedList<AiTile>) pathFind.getPath();
			if ((min > temp.size() && min > 0 && !temp.isEmpty())
					|| escapeTileTemp.equals(getPercepts().getOwnHero()
							.getTile())) {
				min = temp.size();

				targetTile = escapeTileTemp;
			}
		}
		return targetTile;
	}

}
