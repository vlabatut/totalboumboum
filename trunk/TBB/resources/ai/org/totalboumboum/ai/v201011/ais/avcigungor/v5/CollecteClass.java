package org.totalboumboum.ai.v201011.ais.avcigungor.v5;

import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ibrahim Avcı
 * @author Burak Güngör
 */
@SuppressWarnings({ "unused", "deprecation" })
public class CollecteClass {

	/** */
	private AiZone gameZone;
	/** */
	private FillingClass fillingClass;
	/** */
	public double matriceCollecte[][];
	/** */
	private boolean hasArrivedButDanger = false;
	// public double matrice[][];
	/** */
	private AiHero ourHero;
	/** */
	private AiOutput aio;
	/** */
	private AvciGungor ai;

	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public CollecteClass(AvciGungor ai) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		this.ai = ai;
		aio = ai.getOutput();
		gameZone = ai.getPercepts();

		ourHero = gameZone.getOwnHero();
		this.updateMatrix();
		matriceCollecte = fillingClass.getMatrice();

		this.fillCollecteMatrix();

	}

	/**
	 * 
	 * @param origin
	 * @return 
	 * 		?
	 * @throws StopRequestException
	 */
	public List<AiTile> findItemsTiles(AiTile origin)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		List<AiTile> result = new ArrayList<AiTile>();
		for (int line = 0; line < gameZone.getHeight(); line++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			for (int col = 0; col < gameZone.getWidth(); col++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tile = gameZone.getTile(line, col);
				int x = tile.getCol();
				int y = tile.getLine();

				if (fillingClass.isBonus(x, y))
					result.add(tile);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param origin
	 * @return 
	 * 		?
	 * @throws StopRequestException
	 */
	public List<AiTile> findSafeTiles(AiTile origin)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		List<AiTile> result = new ArrayList<AiTile>();
		for (int line = 0; line < gameZone.getHeight(); line++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			for (int col = 0; col < gameZone.getWidth(); col++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tile = gameZone.getTile(line, col);
				int x = tile.getCol();
				int y = tile.getLine();

				if (fillingClass.isSafe(x, y))
					result.add(tile);

			}
		}
		return result;

	}

	/**
	 * 
	 * @param origin
	 * @return 
	 * 		?
	 * @throws StopRequestException
	 */
	public List<AiTile> findWallTiles(AiTile origin)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		List<AiTile> result = new ArrayList<AiTile>();
		for (int line = 1; line < gameZone.getHeight() - 1; line++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			for (int col = 1; col < gameZone.getWidth() - 1; col++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tile = gameZone.getTile(line, col);
				int x = tile.getCol();
				int y = tile.getLine();

				if (fillingClass.iswall(x - 1, y))
					result.add(tile);

				else if (fillingClass.iswall(x + 1, y))
					result.add(tile);

				else if (fillingClass.iswall(x, y - 1))
					result.add(tile);

				else if (fillingClass.iswall(x, y + 1))
					result.add(tile);

				if (hasArrivedButDanger && (tile == ourHero.getTile()))
					result.remove(tile);
			}
		}

		return result;

	}

	/**
	 * 
	 * @throws StopRequestException
	 */
	private void fillCollecteMatrix() throws StopRequestException {
		ai.checkInterruption();

		for (int i = 0; i < gameZone.getHeight(); i++) {
			ai.checkInterruption();
			for (int j = 0; j < gameZone.getWidth(); j++) {
				ai.checkInterruption();

				if (matriceCollecte[i][j] == 1) {
					ai.checkInterruption();

				}
				if (matriceCollecte[i][j] == 2) {
					ai.checkInterruption();
					if (matriceCollecte[i - 1][j] == 7) {
						ai.checkInterruption();
						matriceCollecte[i - 1][j] = 120;
						aio.setTileColor(i - 1, j, Color.GREEN);
						aio.setTileText(i - 1, j, "120");

					}
					if (matriceCollecte[i + 1][j] == 7) {
						ai.checkInterruption();
						matriceCollecte[i + 1][j] = 120;
						aio.setTileColor(i + 1, j, Color.GREEN);
						aio.setTileText(i + 1, j, "120");

					}
					if (matriceCollecte[i][j - 1] == 7) {
						ai.checkInterruption();
						matriceCollecte[i][j - 1] = 120;
						aio.setTileColor(i, j - 1, Color.GREEN);
						aio.setTileText(i, j - 1, "120");

					}
					if (matriceCollecte[i][j + 1] == 7) {
						ai.checkInterruption();
						matriceCollecte[i][j + 1] = 120;
						aio.setTileColor(i, j + 1, Color.GREEN);
						aio.setTileText(i, j + 1, "120");

					}
				}

				if (matriceCollecte[i][j] == 3) {

					ai.checkInterruption();
					matriceCollecte[i][j] = -1000;
					aio.setTileColor(i, j, Color.GREEN);
					aio.setTileText(i, j, "-1000");

				}
				if (matriceCollecte[i][j] == 4) {
					ai.checkInterruption();

					Collection<AiBomb> bombs = gameZone.getBombs();
					Iterator<AiBomb> iteratorBombs = bombs.iterator();

					while (iteratorBombs.hasNext()) {
						ai.checkInterruption();
						AiBomb bomb = iteratorBombs.next();
						matriceCollecte[bomb.getLine()][bomb.getCol()] = 3;

						int bombak = -240;

						if (bomb.getTime() < ((bomb.getNormalDuration()
								+ bomb.getExplosionDuration() - bomb.getTime()) / 3)) {
							ai.checkInterruption();
							bombak = -240;
						} else if (bomb.getTime() < ((2 * bomb
								.getNormalDuration() - bomb.getTime()) / 3)
								&& bomb.getTime() > (((bomb
										.getExplosionDuration()) - bomb
										.getTime()) / 3)) {
							ai.checkInterruption();
							bombak = -480;
						} else {
							ai.checkInterruption();
							bombak = -720;

						}

						if (bomb.getLine() == i) {
							ai.checkInterruption();
							matriceCollecte[i][j] = bombak
									/ (1 + Math.abs((bomb.getCol() - j)));
							String k = Integer.toString(bombak
									/ (1 + Math.abs((bomb.getCol() - j))));

							aio.setTileColor(i, j, Color.GREEN);
							aio.setTileText(i, j, k);

						}
						if (bomb.getCol() == j) {
							ai.checkInterruption();

							matriceCollecte[i][j] = bombak
									/ (1 + Math.abs((bomb.getLine() - i)));
							String m = Integer.toString(bombak
									/ (1 + Math.abs((bomb.getLine() - i))));
							aio.setTileColor(i, j, Color.GREEN);
							aio.setTileText(i, j, m);

						}
						if (matriceCollecte[i][j] == 3) {
							matriceCollecte[i][j] = bombak;

						}

					}
				}

				if (matriceCollecte[i][j] == 5) {
					ai.checkInterruption();

					int max = ourHero.getBombNumberMax();
					int a = 360;

					List<AiHero> heros = gameZone.getRemainingHeroes();
					for (int k = 0; k < heros.size(); k++) {
						ai.checkInterruption();
						if ((heros.get(k).getBombNumberMax() > ourHero
								.getBombNumberMax())
								& (heros.get(k).getBombNumberMax() > max)) {
							ai.checkInterruption();
							max = heros.get(k).getBombNumberMax();
						}
					}
					if (max >= ourHero.getBombNumberMax()) {
						ai.checkInterruption();
						a = (1 + (max - ourHero.getBombNumberMax())) * 360;
					}
					matriceCollecte[i][j] = a;
					String m = Integer.toString(a);
					aio.setTileColor(i, j, Color.GREEN);
					aio.setTileText(i, j, m);

				}
				if (matriceCollecte[i][j] == 6) {

					ai.checkInterruption();

					int max = ourHero.getBombRange();
					int a = 360;

					List<AiHero> heros = gameZone.getRemainingHeroes();
					for (int k = 0; k < heros.size(); k++) {
						ai.checkInterruption();
						if ((heros.get(k).getBombRange() > ourHero
								.getBombRange())
								& (heros.get(k).getBombRange() > max)) {
							ai.checkInterruption();
							max = heros.get(k).getBombRange();
						}
					}
					if (max >= ourHero.getBombRange()) {
						ai.checkInterruption();

						a = (1 + (max - ourHero.getBombRange())) * 360;
					}
					matriceCollecte[i][j] = a;

					String m = Integer.toString(a);
					aio.setTileColor(i, j, Color.GREEN);
					aio.setTileText(i, j, m);

				}
				if (matriceCollecte[i][j] == 7) {
					ai.checkInterruption();

				}
				if (matriceCollecte[i][j] == 8) {
					ai.checkInterruption();

				}
				if (matriceCollecte[i][j] == 9) {
					ai.checkInterruption();

				}
				if (matriceCollecte[i][j] == 10) {
					ai.checkInterruption();
					matriceCollecte[i][j] = -1000;
					aio.setTileColor(i, j, Color.RED);
					aio.setTileText(i, j, "-1000");

				}
			}
		}
	}

	/**
	 * 
	 * @throws StopRequestException
	 */
	private void updateMatrix() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		gameZone = ai.getPercepts();

		fillingClass = new FillingClass(this.ai);

	}

	/**
	 * 
	 * @return 
	 * 		?
	 * @throws StopRequestException
	 */
	public double[][] getMatrice() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		return matriceCollecte;
	}

}
