package org.totalboumboum.ai.v201011.ais.avcigungor.v2;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb; //import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Ibrahim Avcı
 * @author Burak Güngör
 * 
 */

public class AvciGungor extends ArtificialIntelligence {

	private final int FOSSE = 0;
	private final int MUR_INDESTRUCTIBLE = 1;
	private final int MUR_DESTRUCTIBLE = 2;
	private final int BOMBE = 3;
	private final int BLAST = 4;
	private final int BONUS_EXTRA_BOMBE = 5;
	private final int BONUS_EXTRA_BLAST = 6;
	private final int CASE_SUR = 7;
	private final int ADVERSAIRE = 8;
	private final int NOUS = 9;
	private final int FLAMME = 10;
	private AiHero ourHero;
	private AiOutput aio;
	private boolean modeCollecte = true;

	private int matriceCollecte[][];
	private int matriceAttaque[][];
	private AiZone gameZone;

	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException {
		checkInterruption();
		gameZone = getPercepts();
		aio = getOutput();
		ourHero = gameZone.getOwnHero();
		AiAction result = new AiAction(AiActionName.NONE);

		matriceCollecte = new int[gameZone.getHeight()][gameZone.getWidth()];
		matriceAttaque = new int[gameZone.getHeight()][gameZone.getWidth()];
		modeCollecte = true;
		
		  if ((gameZone.getHiddenItemsCount() == 0 && gameZone.getItems()
		  .size() == 0)) modeCollecte = false;
		 

		if (modeCollecte) {
			this.initialiseMatriceCollecte(matriceCollecte, gameZone);
			this.emplirBlock(matriceCollecte, gameZone);
			this.emplirBombe(matriceCollecte, gameZone);
			this.emplirBonus(matriceCollecte, gameZone);
			this.emplirHero(matriceCollecte, gameZone);
			this.emplirSur(matriceCollecte, gameZone);
			this.fillFiresMatrice(matriceCollecte, gameZone);
			this.montrerConsole(matriceCollecte);
			result = new AiAction(AiActionName.MOVE, Direction.UP);
			
			// this.montrerLecran(matriceCollecte, aio);
		} else {

			this.initialiseMatriceCollecte(matriceAttaque, gameZone);
			this.emplirBlock(matriceAttaque, gameZone);
			this.emplirBombe(matriceAttaque, gameZone);
			this.emplirBonus(matriceAttaque, gameZone);
			this.emplirHero(matriceAttaque, gameZone);
			this.emplirSur(matriceAttaque, gameZone);
			this.fillFiresMatrice(matriceCollecte, gameZone);
			this.montrerConsole(matriceAttaque);
			// this.montrerLecran(matriceAttaque, aio);
		}
		return result;

	}

	private void initialiseMatriceCollecte(int[][] matriceCollecte,
			AiZone gameZone) throws StopRequestException {
		checkInterruption();
		int height = gameZone.getHeight();
		int width = gameZone.getWidth();
		for (int i = 0; i < height; i++) {
			checkInterruption();
			for (int j = 0; j < width; j++) {
				checkInterruption();
				matriceCollecte[i][j] = FOSSE;

			}
		}

	}

	private void emplirBonus(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		Collection<AiItem> items = gameZone.getItems();
		Iterator<AiItem> iteratorItems = items.iterator();
		while (iteratorItems.hasNext()) {
			checkInterruption();
			AiItem item = iteratorItems.next();
			if (item.getType().toString() == "EXTRA_BOMB") {
				checkInterruption();
				matrice[item.getLine()][item.getCol()] = BONUS_EXTRA_BOMBE;

			} else if (item.getType().toString() == "EXTRA_FLAME") {
				checkInterruption();
				matrice[item.getLine()][item.getCol()] = BONUS_EXTRA_BLAST;
			}

		}
	}

	/*
	 * private void emplirFire(int[][] matrice, AiZone gameZone) throws
	 * StopRequestException { checkInterruption(); Collection<AiFire> fires =
	 * gameZone.getFires(); Iterator<AiFire> iteratorFires = fires.iterator();
	 * while (iteratorFires.hasNext()) { checkInterruption(); AiFire fire =
	 * iteratorFires.next(); matrice[fire.getLine()][fire.getCol()] = OBSTACLE;
	 * } }
	 */

	private void emplirHero(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		Collection<AiHero> allheroes = gameZone.getHeroes();
		Iterator<AiHero> itheroes = allheroes.iterator();
		while (itheroes.hasNext()) {
			checkInterruption();
			ourHero = itheroes.next();
			if (ourHero != gameZone.getOwnHero())
				matrice[ourHero.getLine()][ourHero.getCol()] = ADVERSAIRE;
			else
				matrice[ourHero.getLine()][ourHero.getCol()] = NOUS;
		}

	}

	private void emplirBombe(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		Collection<AiBomb> bombs = gameZone.getBombs();

		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		while (iteratorBombs.hasNext()) {
			checkInterruption();
			AiBomb bomb = iteratorBombs.next();
			matrice[bomb.getLine()][bomb.getCol()] = BOMBE;
			List<AiTile> inScopeTiles = bomb.getBlast();

			for (int i = 0; i < inScopeTiles.size(); i++) {
				checkInterruption();
				matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i)
						.getCol()] = BLAST;
			}

		}

	}

	private void emplirBlock(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		Collection<AiBlock> blocks = gameZone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		while (iteratorBlocks.hasNext()) {
			checkInterruption();
			AiBlock block = iteratorBlocks.next();
			if (block.isDestructible()) {
				checkInterruption();
				matrice[block.getLine()][block.getCol()] = MUR_DESTRUCTIBLE;
			} else {
				checkInterruption();
				matrice[block.getLine()][block.getCol()] = MUR_INDESTRUCTIBLE;
			}
		}
	}

	private void emplirSur(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		for (int i = 0; i < gameZone.getHeight(); i++) {
			checkInterruption();
			for (int j = 0; j < gameZone.getWidth(); j++) {
				checkInterruption();
				if (matrice[i][j] == FOSSE) {
					checkInterruption();
					matrice[i][j] = CASE_SUR;
				}

			}
		}

	}

	private void fillFiresMatrice(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		List<org.totalboumboum.ai.v201011.adapter.data.AiFire> fires = gameZone
				.getFires();
		Iterator<org.totalboumboum.ai.v201011.adapter.data.AiFire> iteratorFires = fires
				.iterator();
		while (iteratorFires.hasNext()) {
			checkInterruption();
			org.totalboumboum.ai.v201011.adapter.data.AiFire fire = iteratorFires
					.next();
			matrice[fire.getLine()][fire.getCol()] = FLAMME;
		}
	}

	private void montrerConsole(int[][] matrice) throws StopRequestException {
		/*
		 * for(int i=0; i<gameZone.getHeigh(); i++){ checkInterruption();
		 * for(int j=0; j<gameZone.getWidth(); j++){ checkInterruption();
		 * System.out.print("["+matrice[i][j]+"]");
		 * 
		 * } System.out.print("\n");
		 * 
		 * } System.out.print("-----\n");
		 */
		if (modeCollecte) {
			for (int i = 0; i < gameZone.getHeight(); i++) {
				checkInterruption();
				for (int j = 0; j < gameZone.getWidth(); j++) {
					checkInterruption();

					if (matrice[i][j] == 1) {
						checkInterruption();
						aio.setTileColor(i, j, Color.RED);
						aio.setTileText(i, j, "IND");
					}
					if (matrice[i][j] == 2) {
						checkInterruption();
						if (matrice[i - 1][j] == 7) {
							checkInterruption();
							matrice[i - 1][j] = 120;
							aio.setTileColor(i - 1, j, Color.GREEN);
							aio.setTileText(i - 1, j, "120");
						}
						if (matrice[i + 1][j] == 7) {
							checkInterruption();
							matrice[i + 1][j] = 120;
							aio.setTileColor(i + 1, j, Color.GREEN);
							aio.setTileText(i + 1, j, "120");
						}
						if (matrice[i][j - 1] == 7) {
							checkInterruption();
							matrice[i][j - 1] = 120;
							aio.setTileColor(i, j - 1, Color.GREEN);
							aio.setTileText(i, j - 1, "120");
						}
						if (matrice[i][j + 1] == 7) {
							checkInterruption();
							matrice[i][j + 1] = 120;
							aio.setTileColor(i, j + 1, Color.GREEN);
							aio.setTileText(i, j + 1, "120");
						}
					}

					if (matrice[i][j] == 3) {

						checkInterruption();
						matrice[i][j] = -1000;

						aio.setTileColor(i, j, Color.GREEN);
						aio.setTileText(i, j, "-1000");
					}
					if (matrice[i][j] == 4) {
						checkInterruption();

						Collection<AiBomb> bombs = gameZone.getBombs();
						Iterator<AiBomb> iteratorBombs = bombs.iterator();

						while (iteratorBombs.hasNext()) {
							checkInterruption();
							AiBomb bomb = iteratorBombs.next();
							matrice[bomb.getLine()][bomb.getCol()] = BOMBE;

							int bombak = -240;

							if (bomb.getTime() < ((bomb.getNormalDuration()
									+ bomb.getExplosionDuration() - bomb
									.getTime()) / 3)) {
								checkInterruption();
								bombak = -240;
							} else if (bomb.getTime() < ((2 * bomb
									.getNormalDuration() - bomb.getTime()) / 3)
									&& bomb.getTime() > (((bomb
											.getExplosionDuration()) - bomb
											.getTime()) / 3)) {
								checkInterruption();
								bombak = -480;
							} else {
								checkInterruption();
								bombak = -720;

							}

							if (bomb.getLine() == i) {
								checkInterruption();
								matrice[i][j] = bombak
										/ (1 + Math.abs((bomb.getCol() - j)));
								String k = Integer.toString(bombak
										/ (1 + Math.abs((bomb.getCol() - j))));

								aio.setTileColor(i, j, Color.GREEN);
								aio.setTileText(i, j, k);
							}
							if (bomb.getCol() == j) {
								checkInterruption();
								String m = Integer.toString(bombak
										/ (1 + Math.abs((bomb.getLine() - i))));
								matrice[i][j] = bombak
										/ (1 + Math.abs((bomb.getLine() - i)));
								aio.setTileColor(i, j, Color.GREEN);
								aio.setTileText(i, j, m);
							}
							if (matrice[i][j]==3) {
								matrice[i][j] = bombak;

								String k = Integer.toString(bombak);

								aio.setTileColor(i, j, Color.GREEN);
								aio.setTileText(i, j, k);
							}

						}
					}

					if (matrice[i][j] == 5) {
						checkInterruption();

						int max = ourHero.getBombNumberMax();
						int a = 360;

						List<AiHero> heros = gameZone.getRemainingHeroes();
						for (int k = 0; k < heros.size(); k++) {
							checkInterruption();
							if ((heros.get(k).getBombNumberMax() > ourHero
									.getBombNumberMax())
									& (heros.get(k).getBombNumberMax() > max)) {
								checkInterruption();
								max = heros.get(k).getBombNumberMax();
							}
						}
						if (max >= ourHero.getBombNumberMax()) {
							checkInterruption();
							a = (1 + (max - ourHero.getBombNumberMax())) * 360;
						}
						matrice[i][j] = a;
						String m = Integer.toString(a);
						aio.setTileColor(i, j, Color.GREEN);
						aio.setTileText(i, j, m);
					}
					if (matrice[i][j] == 6) {

						checkInterruption();

						int max = ourHero.getBombRange();
						int a = 360;

						List<AiHero> heros = gameZone.getRemainingHeroes();
						for (int k = 0; k < heros.size(); k++) {
							checkInterruption();
							if ((heros.get(k).getBombRange() > ourHero
									.getBombRange())
									& (heros.get(k).getBombRange() > max)) {
								checkInterruption();
								max = heros.get(k).getBombRange();
							}
						}
						if (max >= ourHero.getBombRange()) {
							checkInterruption();

							a = (1 + (max - ourHero.getBombRange())) * 360;
						}
						matrice[i][j] = a;
						String m = Integer.toString(a);
						aio.setTileColor(i, j, Color.GREEN);
						aio.setTileText(i, j, m);
					}
					if (matrice[i][j] == 7) {
						checkInterruption();
						aio.setTileColor(i, j, Color.ORANGE);
						aio.setTileText(i, j, "CS");
					}
					if (matrice[i][j] == 8) {
						checkInterruption();
						aio.setTileColor(i, j, Color.GREEN);
						aio.setTileText(i, j, "-60");
					}
					if (matrice[i][j] == 9) {
						checkInterruption();
						aio.setTileColor(i, j, Color.WHITE);
						aio.setTileText(i, j, "NOUS");
					}
					if (matrice[i][j] == 10) {
						checkInterruption();
						matrice[i][j] = -1000;
						aio.setTileColor(i, j, Color.WHITE);
						aio.setTileText(i, j, "-1000");
					}
					System.out.print("[" + matrice[i][j] + "]");
				}
				System.out.print("\n");

			}
		} else {
			for (int i = 0; i < gameZone.getHeight(); i++) {
				checkInterruption();
				for (int j = 0; j < gameZone.getWidth(); j++) {
					checkInterruption();
					if (matrice[i][j] == 1) {
						checkInterruption();
						aio.setTileColor(i, j, Color.RED);
						aio.setTileText(i, j, "IND");
					}
					if (matrice[i][j] == 2) {
						checkInterruption();
						if (matrice[i - 1][j] == 7) {
							checkInterruption();
							matrice[i - 1][j] = 0;
							aio.setTileColor(i - 1, j, Color.GREEN);
							aio.setTileText(i - 1, j, "0");
						}
						if (matrice[i + 1][j] == 7) {
							checkInterruption();
							matrice[i + 1][j] = 0;
							aio.setTileColor(i + 1, j, Color.GREEN);
							aio.setTileText(i + 1, j, "0");
						}
						if (matrice[i][j - 1] == 7) {
							checkInterruption();
							matrice[i][j - 1] = 0;
							aio.setTileColor(i, j - 1, Color.GREEN);
							aio.setTileText(i, j - 1, "0");
						}
						if (matrice[i][j + 1] == 7) {
							checkInterruption();
							matrice[i][j + 1] = 120;
							aio.setTileColor(i, j + 1, Color.GREEN);
							aio.setTileText(i, j + 1, "0");
						}
					}
					int bombak = -240;
					if (matrice[i][j] == 4) {
						checkInterruption();

						Collection<AiBomb> bombs = gameZone.getBombs();
						Iterator<AiBomb> iteratorBombs = bombs.iterator();
						int bombaki = -240;
						while (iteratorBombs.hasNext()) {
							checkInterruption();
							AiBomb bomb = iteratorBombs.next();
							matrice[bomb.getLine()][bomb.getCol()] = BOMBE;

							if (bomb.getTime() < ((bomb.getNormalDuration() - bomb
									.getTime()) / 3)) {
								checkInterruption();
								bombaki = -240;
							} else if (bomb.getTime() < ((2 * bomb
									.getNormalDuration() - bomb.getTime()) / 3)
									&& bomb.getTime() > ((bomb
											.getExplosionDuration() - bomb
											.getTime()) / 3)) {
								checkInterruption();
								bombaki = -480;
							} else {
								checkInterruption();
								bombaki = -720;
							}
							if (bomb.getLine() == i) {
								checkInterruption();
								matrice[i][j] = (bombaki)
										/ (1 + Math.abs((bomb.getCol() - j)));
								String k = Integer.toString((bombaki)
										/ (1 + Math.abs((bomb.getCol() - j))));

								aio.setTileColor(i, j, Color.GREEN);
								aio.setTileText(i, j, k);
							}
							if (bomb.getCol() == j) {
								checkInterruption();
								String m = Integer.toString((bombaki)
										/ (1 + Math.abs((bomb.getLine() - i))));
								matrice[i][j] = (bombaki)
										/ (1 + Math.abs((bomb.getLine() - i)));
								aio.setTileColor(i, j, Color.GREEN);
								aio.setTileText(i, j, m);
							}

						}
						bombak = bombaki;
					}
					if (matrice[i][j] == 3) {
						checkInterruption();
						matrice[i][j] = bombak;
						String m = Integer.toString((bombak));
						aio.setTileColor(i, j, Color.GREEN);
						aio.setTileText(i, j, m);
					}
					if (matrice[i][j] == 5) {
						checkInterruption();
						matrice[i][j] = 60;
						aio.setTileColor(i, j, Color.GREEN);
						aio.setTileText(i, j, "60");
					}
					if (matrice[i][j] == 6) {
						checkInterruption();
						matrice[i][j] = 60;
						aio.setTileColor(i, j, Color.GREEN);
						aio.setTileText(i, j, "60");

					}
					if (matrice[i][j] == 7) {
						checkInterruption();
						aio.setTileColor(i, j, Color.ORANGE);
						aio.setTileText(i, j, "CS");
					}
					if (matrice[i][j] == 8) {
						checkInterruption();
						matrice[i][j] = 240;
						aio.setTileColor(i, j, Color.GREEN);
						aio.setTileText(i, j, "240");
					}
					if (matrice[i][j] == 9) {
						checkInterruption();
						aio.setTileColor(i, j, Color.WHITE);
						aio.setTileText(i, j, "NOUS");
					}
					if (matrice[i][j] == 10) {
						checkInterruption();
						matrice[i][j] = -1000;
						aio.setTileColor(i, j, Color.WHITE);
						aio.setTileText(i, j, "-1000");
					}
					System.out.print("[" + matrice[i][j] + "]");
				}
				System.out.print("\n");

			}

		}
		System.out.print("------\n");
	}

	/*
	 * private void montrerLecran(int[][] matrice, AiOutput aio) throws
	 * StopRequestException { checkInterruption(); for (int i = 0; i <
	 * gameZone.getHeigh(); i++) { checkInterruption(); for (int j = 0; j <
	 * gameZone.getWidth(); j++) { checkInterruption(); if (matrice[i][j] == 1)
	 * { checkInterruption(); aio.setTileColor(i, j, Color.RED);
	 * aio.setTileText(i, j, "IND"); } if (matrice[i][j] == 2) {
	 * checkInterruption(); aio.setTileColor(i, j, Color.YELLOW);
	 * aio.setTileText(i, j, "DES"); } if (matrice[i][j] == 3) {
	 * checkInterruption(); aio.setTileColor(i, j, Color.DARK_GRAY);
	 * aio.setTileText(i, j, "BOMBE"); } if (matrice[i][j] == 4) {
	 * checkInterruption(); aio.setTileColor(i, j, Color.BLUE);
	 * aio.setTileText(i, j, "FLM"); } if (matrice[i][j] == 5) {
	 * checkInterruption(); aio.setTileColor(i, j, Color.GREEN);
	 * aio.setTileText(i, j, "BEB"); } if (matrice[i][j] == 6) {
	 * checkInterruption(); aio.setTileColor(i, j, Color.MAGENTA);
	 * aio.setTileText(i, j, "BEF"); } if (matrice[i][j] == 7) {
	 * checkInterruption(); aio.setTileColor(i, j, Color.ORANGE);
	 * aio.setTileText(i, j, "CS"); } if (matrice[i][j] == 8) {
	 * checkInterruption(); aio.setTileColor(i, j, Color.BLACK);
	 * aio.setTileText(i, j, "ADV"); } if (matrice[i][j] == 9) {
	 * checkInterruption(); aio.setTileColor(i, j, Color.WHITE);
	 * aio.setTileText(i, j, "NOUS"); } } }
	 * 
	 * }
	 */
}
