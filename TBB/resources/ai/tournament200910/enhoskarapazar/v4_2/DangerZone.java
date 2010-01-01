package tournament200910.enhoskarapazar.v4_2;

import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.adapter200910.communication.StopRequestException;
import org.totalboumboum.ai.adapter200910.data.AiBlock;
import org.totalboumboum.ai.adapter200910.data.AiBomb;
import org.totalboumboum.ai.adapter200910.data.AiFire;
import org.totalboumboum.ai.adapter200910.data.AiHero;
import org.totalboumboum.ai.adapter200910.data.AiItem;
import org.totalboumboum.ai.adapter200910.data.AiItemType;
import org.totalboumboum.ai.adapter200910.data.AiTile;
import org.totalboumboum.ai.adapter200910.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;


public class DangerZone {

	private AiZone zone;
	private Collection<AiHero> rivals;
	private AiHero caractere;
	private Collection<AiBomb> bombes;
	private Collection<AiBlock> blocs;
	private Collection<AiItem> objets;
	private Collection<AiFire> feus;
	private int xMax;
	private int yMax;
	private ZoneEnum[][] zoneArray;
	private EnhosKarapazar source;

	public DangerZone(AiZone zone, EnhosKarapazar source)
			throws StopRequestException {
		source.checkInterruption(); // Appel Obligatoire
		this.source = source;
		this.bombes = zone.getBombs();
		this.zone = zone;
		this.caractere = zone.getOwnHero();
		rivals = zone.getHeroes();
		this.blocs = zone.getBlocks();
		this.objets = zone.getItems();
		this.feus = zone.getFires();
		this.xMax = zone.getWidth();
		this.yMax = zone.getHeigh();
		init();
	}

	private void init() throws StopRequestException {
		source.checkInterruption(); // Appel Obligatoire
		zoneArray = new ZoneEnum[yMax][xMax];
		int i, j;
		// Initialisation
		for (i = 0; i < xMax; i++) {
			source.checkInterruption(); // Appel Obligatoire
			for (j = 0; j < yMax; j++) {
				source.checkInterruption(); // Appel Obligatoire
				zoneArray[j][i] = ZoneEnum.LIBRE;
			}
		}
		// Mettons notre caractere

		// Mettons nos rivals
		Iterator<AiHero> itRivals = rivals.iterator();
		while (itRivals.hasNext()) {
			source.checkInterruption(); // Appel Obligatoire
			AiHero temp = itRivals.next();

			zoneArray[temp.getLine()][temp.getCol()] = ZoneEnum.ADVERSAIRE;
		}

		zoneArray[caractere.getTile().getLine()][caractere.getTile().getCol()] = ZoneEnum.CARACTERE;

		// Mettons les blocs
		Iterator<AiBlock> itBlocs = blocs.iterator();
		while (itBlocs.hasNext()) {
			source.checkInterruption(); // Appel Obligatoire
			AiBlock temp = itBlocs.next();
			if (temp.isDestructible())
				zoneArray[temp.getLine()][temp.getCol()] = ZoneEnum.DESTRUCTIBLES;
			else
				zoneArray[temp.getLine()][temp.getCol()] = ZoneEnum.INDESTRUCTIBLES;
		}

		// Mettons les bombes et les feus possibles
		Iterator<AiBomb> itBombes = bombes.iterator();
		while (itBombes.hasNext()) {
			source.checkInterruption(); // Appel Obligatoire
			AiBomb temp = itBombes.next();

			// Les tiles dangeureux
			int k = 0;
			// Est-ce qu'on continue sur ces directions?
			boolean up = true, down = true, left = true, right = true;
			while (k < temp.getRange() + 1 && (up || down || left || right)) {
				int a = 0;
				source.checkInterruption(); // Appel Obligatoire

				AiTile temp1 = temp.getTile();
				while (up && a < k) {
					source.checkInterruption(); // Appel Obligatoire
					AiTile temp2 = temp1.getNeighbor(Direction.UP);
					temp1 = temp2;
					int y = temp2.getCol();
					int x = temp2.getLine();
					if ((zoneArray[x][y] != ZoneEnum.DESTRUCTIBLES)
							&& (zoneArray[x][y] != ZoneEnum.INDESTRUCTIBLES)) {
						zoneArray[x][y] = ZoneEnum.FLAMMES;
						a++;
					} else
						up = false;
				}
				a = 0;
				temp1 = temp.getTile();

				while (down && a < k) {
					source.checkInterruption(); // Appel Obligatoire
					AiTile temp2 = temp1.getNeighbor(Direction.DOWN);
					temp1 = temp2;
					int y = temp2.getCol();
					int x = temp2.getLine();
					if ((zoneArray[x][y] != ZoneEnum.DESTRUCTIBLES)
							&& (zoneArray[x][y] != ZoneEnum.INDESTRUCTIBLES)) {
						zoneArray[x][y] = ZoneEnum.FLAMMES;
						a++;
					} else
						down = false;
				}
				a = 0;
				temp1 = temp.getTile();
				while (left && a < k) {
					source.checkInterruption(); // Appel Obligatoire
					AiTile temp2 = temp1.getNeighbor(Direction.LEFT);
					temp1 = temp2;
					int y = temp2.getCol();
					int x = temp2.getLine();
					if ((zoneArray[x][y] != ZoneEnum.DESTRUCTIBLES)
							&& (zoneArray[x][y] != ZoneEnum.INDESTRUCTIBLES)) {
						zoneArray[x][y] = ZoneEnum.FLAMMES;
						a++;
					} else
						left = false;
				}
				a = 0;
				temp1 = temp.getTile();
				while (right && a < k) {
					source.checkInterruption(); // Appel Obligatoire
					AiTile temp2 = temp1.getNeighbor(Direction.RIGHT);
					temp1 = temp2;
					int y = temp2.getCol();
					int x = temp2.getLine();
					if ((zoneArray[x][y] != ZoneEnum.DESTRUCTIBLES)
							&& (zoneArray[x][y] != ZoneEnum.INDESTRUCTIBLES)) {
						zoneArray[x][y] = ZoneEnum.FLAMMES;
						a++;
					} else
						right = false;
				}
				a = 0;
				temp1 = temp.getTile();
				k++;
			}
		}

		// Mettons les bombes
		Iterator<AiBomb> itBombes2 = bombes.iterator();
		while (itBombes2.hasNext()) {
			source.checkInterruption(); // Appel Obligatoire
			AiBomb temp = itBombes2.next();
			zoneArray[temp.getLine()][temp.getCol()] = ZoneEnum.BOMBE;
		}

		// Mettons les feus
		Iterator<AiFire> itFeus = feus.iterator();
		while (itFeus.hasNext()) {
			source.checkInterruption(); // Appel Obligatoire
			AiFire temp = itFeus.next();
			zoneArray[temp.getLine()][temp.getCol()] = ZoneEnum.FEU;
		}

		// Mettons les bonus
		Iterator<AiItem> itObjets = objets.iterator();
		while (itObjets.hasNext()) {
			source.checkInterruption(); // Appel Obligatoire
			AiItem temp = itObjets.next();
			if (temp.getType() == AiItemType.EXTRA_BOMB)
				zoneArray[temp.getLine()][temp.getCol()] = ZoneEnum.BONUSBOMBE;
			if (temp.getType() == AiItemType.EXTRA_FLAME)
				zoneArray[temp.getLine()][temp.getCol()] = ZoneEnum.BONUSRANGE;
		}
	}

	public void PrintDangerZone() throws StopRequestException {
		source.checkInterruption();
		for (int i = 0; i < zone.getWidth(); i++) {
			source.checkInterruption();
			for (int j = 0; j < zone.getHeigh(); j++) {
				source.checkInterruption();
				System.out.print(j + ":" + i + ":" + zoneArray[j][i]);
			}
			System.out.println();
		}
	}

	public ZoneEnum getEnum(int line, int col) throws StopRequestException {
		source.checkInterruption(); // Appel Obligatoire
		return zoneArray[line][col];
	}

	public Direction crossControl(int range, int line, int col)
			throws StopRequestException {
		source.checkInterruption();
		Direction result = Direction.NONE;
		Boolean upLine = false, downLine = false, rightCol = false, leftCol = false;
		Boolean upLine2 = false, downLine2 = false, rightCol2 = false, leftCol2 = false;
		ZoneEnum zone = null;
		for (int i = 1; i <= range; i++) {
			source.checkInterruption();
			zone = null;
			if (!upLine) {
				if (line + 1 < yMax && col + i < xMax) {
					zone = getEnum(line + 1, col + i);
					if (canGo(line +1, col + i)
							&&(zone == ZoneEnum.BONUSBOMBE
							|| zone == ZoneEnum.BONUSRANGE
							|| zone == ZoneEnum.LIBRE
							||zone==ZoneEnum.ADVERSAIRE))
						upLine = true;
				}
			}
			if (!downLine) {
				if (line - 1 > 0 && col + i < xMax) {
					zone = getEnum(line - 1, col + i);
					if (canGo(line - 1, col + i)
							&&(zone == ZoneEnum.BONUSBOMBE
									|| zone == ZoneEnum.BONUSRANGE
									|| zone == ZoneEnum.LIBRE
									||zone==ZoneEnum.ADVERSAIRE))
						downLine = true;
				}
			}

			if (!rightCol) {
				if (line + i < yMax && col + 1 < xMax) {
					zone = getEnum(line + i, col + 1);
					if (canGo(line + i, col + 1)
							&&(zone == ZoneEnum.BONUSBOMBE
									|| zone == ZoneEnum.BONUSRANGE
									|| zone == ZoneEnum.LIBRE
									||zone==ZoneEnum.ADVERSAIRE))
						rightCol = true;
				}
			}
			if (!leftCol) {
				if (line + i < yMax && col - 1 > 0) {
					zone = getEnum(line + i, col - 1);
					if (canGo(line + i, col - 1)
							&&(zone == ZoneEnum.BONUSBOMBE
									|| zone == ZoneEnum.BONUSRANGE
									|| zone == ZoneEnum.LIBRE
									||zone==ZoneEnum.ADVERSAIRE))
						leftCol = true;
				}
			}

			if (!upLine2) {
				if (line + 1 < yMax && 0 < col - i) {
					zone = getEnum(line + 1, col - i);
					if (canGo(line + 1, col - i)
							&&(zone == ZoneEnum.BONUSBOMBE
									|| zone == ZoneEnum.BONUSRANGE
									|| zone == ZoneEnum.LIBRE
									||zone==ZoneEnum.ADVERSAIRE))
						upLine2 = true;
				}
			}
			if (!downLine2) {
				if (line - 1 > 0 && 0 < col - i) {
					zone = getEnum(line - 1, col - i);
					if (canGo(line - 1, col - i)
							&&(zone == ZoneEnum.BONUSBOMBE
									|| zone == ZoneEnum.BONUSRANGE
									|| zone == ZoneEnum.LIBRE
									||zone==ZoneEnum.ADVERSAIRE))
						downLine2 = true;
				}
			}

			if (!rightCol2) {
				if (line - i > 0 && col + 1 < xMax) {
					zone = getEnum(line - i, col + 1);
					if (canGo(line - i, col + 1)
							&&(zone == ZoneEnum.BONUSBOMBE
									|| zone == ZoneEnum.BONUSRANGE
									|| zone == ZoneEnum.LIBRE
									||zone==ZoneEnum.ADVERSAIRE))
						rightCol2 = true;
				}
			}
			if (!leftCol2) {
				if (line - i > 0 && col - 1 > 0) {
					zone = getEnum(line - i, col - 1);
					if (canGo(line - i, col - 1)
							&&(zone == ZoneEnum.BONUSBOMBE
									|| zone == ZoneEnum.BONUSRANGE
									|| zone == ZoneEnum.LIBRE
									||zone==ZoneEnum.ADVERSAIRE))
						leftCol2 = true;
				}
			}

		}

		if (upLine || downLine || leftCol || rightCol || upLine2 || downLine2
				|| leftCol2 || rightCol2) {
			if (upLine || downLine)
				result = Direction.RIGHT;
			else if (upLine2 || downLine2)
				result = Direction.LEFT;
			else if (leftCol2 || rightCol2)
				result = Direction.UP;
			else if (leftCol || rightCol)
				result = Direction.DOWN;
		}
		zone = null;
		upLine = downLine = leftCol = rightCol = null;
		return result;
	}

	public Direction rangeControl(int range, int line, int col)
			throws StopRequestException {
		source.checkInterruption();
		Direction result = Direction.NONE;
		Boolean RIGHT = true, LEFT = true, DOWN = true, UP = true;
		ZoneEnum zone = null;
		for (int i = 1; i <= range; i++) {
			source.checkInterruption();
			zone = null;
			if (RIGHT) {
				zone = getEnum(line, col + i);
				if (zone != ZoneEnum.LIBRE && zone != ZoneEnum.BONUSBOMBE
						&& zone != ZoneEnum.BONUSRANGE
						//&& zone != ZoneEnum.ADVERSAIRE
						&& zone != ZoneEnum.FLAMMES)
					RIGHT = false;
			}
			if (LEFT) {
				zone = getEnum(line, col - i);
				if (zone != ZoneEnum.LIBRE && zone != ZoneEnum.BONUSBOMBE
						&& zone != ZoneEnum.BONUSRANGE
					//	&& zone != ZoneEnum.ADVERSAIRE
						&& zone != ZoneEnum.FLAMMES)
					LEFT = false;
			}
			if (UP) {
				zone = getEnum(line - i, col);
				if (zone != ZoneEnum.LIBRE && zone != ZoneEnum.BONUSBOMBE
						&& zone != ZoneEnum.BONUSRANGE
					//	&& zone != ZoneEnum.ADVERSAIRE
						&& zone != ZoneEnum.FLAMMES)
					UP = false;
			}
			if (DOWN) {
				zone = getEnum(line + i, col);
				if (zone != ZoneEnum.LIBRE && zone != ZoneEnum.BONUSBOMBE
						&& zone != ZoneEnum.BONUSRANGE
					//	&& zone != ZoneEnum.ADVERSAIRE
						&& zone != ZoneEnum.FLAMMES)
					DOWN = false;
			}
		}
		if (RIGHT || LEFT || UP || DOWN)
			if (RIGHT)
				result = Direction.RIGHT;
			else if (LEFT)
				result = Direction.LEFT;
			else if (UP)
				result = Direction.UP;
			else if (DOWN)
				result = Direction.DOWN;
		zone = null;
		LEFT = RIGHT = UP = DOWN = null;
		if (result == Direction.NONE)
			result = crossControl(range, line, col);
		return result;
	}

	private boolean canGo(int line, int col) throws StopRequestException {
		source.checkInterruption();
		boolean ret = false;
		PathManagement pMan = new PathManagement(source, zone
				.getTile(line, col));
		if (pMan.getPathList() != null && pMan.isWalkable())
			ret = true;
		pMan = null;
		return ret;
	}

}
