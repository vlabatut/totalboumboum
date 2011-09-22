package org.totalboumboum.ai.v201011.ais.avcigungor.v5;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

/**
 * @author Ibrahim Avcı
 * @author Burak Güngör
 */
public class FillingClass {
	private final double FOSSE = 0;
	private final double MUR_INDESTRUCTIBLE = 1;
	private final double MUR_DESTRUCTIBLE = 2;
	private final double BOMBE = 3;
	private final double BLAST = 4;
	private final double BONUS_EXTRA_BOMBE = 5;
	private final double BONUS_EXTRA_BLAST = 6;
	private final double CASE_SUR = 7;
	private final double ADVERSAIRE = 8;
	private final double NOUS = 9;
	private final double FLAMME = 10;

	public double matrice[][];

	AvciGungor ai;

	AiZone gameZone;
	private AiHero ourHero;

	public FillingClass(AvciGungor ai) throws StopRequestException {
		this.ai = ai;
		gameZone = ai.getPercepts();
		ourHero = gameZone.getOwnHero();
		matrice=new double[gameZone.getHeight()][gameZone.getWidth()];
		this.initialiseMatrice(matrice, gameZone);
		this.emplirBlock(matrice, gameZone);
		this.emplirBombe(matrice, gameZone);
		this.emplirBonus(matrice, gameZone);
		this.emplirHero(matrice, gameZone);
		this.emplirSur(matrice, gameZone);
		this.fillFiresMatrice(matrice, gameZone);
	}

	private void initialiseMatrice(double[][] matrice, AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption();
		int height = gameZone.getHeight();
		int width = gameZone.getWidth();
		for (int i = 0; i < height; i++) {
			ai.checkInterruption();
			for (int j = 0; j < width; j++) {
				ai.checkInterruption();
				matrice[i][j] = FOSSE;

			}
		}

	}

	private void emplirBonus(double[][] matrice, AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption();
		Collection<AiItem> items = gameZone.getItems();
		Iterator<AiItem> iteratorItems = items.iterator();
		while (iteratorItems.hasNext()) {
			ai.checkInterruption();
			AiItem item = iteratorItems.next();
			if (item.getType().toString() == "EXTRA_BOMB") {
				ai.checkInterruption();
				matrice[item.getLine()][item.getCol()] = BONUS_EXTRA_BOMBE;

			} else if (item.getType().toString() == "EXTRA_FLAME") {
				ai.checkInterruption();
				matrice[item.getLine()][item.getCol()] = BONUS_EXTRA_BLAST;
			}

		}
	}

	private void emplirHero(double[][] matrice, AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption();
		Collection<AiHero> allheroes = gameZone.getHeroes();
		Iterator<AiHero> itheroes = allheroes.iterator();
		while (itheroes.hasNext()) {
			ai.checkInterruption();
			ourHero = itheroes.next();
			if (ourHero != gameZone.getOwnHero())
				matrice[ourHero.getLine()][ourHero.getCol()] = ADVERSAIRE;
			else
				matrice[ourHero.getLine()][ourHero.getCol()] = NOUS;
		}

	}

	private void emplirBombe(double[][] matrice, AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption();
		Collection<AiBomb> bombs = gameZone.getBombs();

		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		while (iteratorBombs.hasNext()) {
			ai.checkInterruption();
			AiBomb bomb = iteratorBombs.next();
			matrice[bomb.getLine()][bomb.getCol()] = BOMBE;
			List<AiTile> inScopeTiles = bomb.getBlast();

			for (int i = 0; i < inScopeTiles.size(); i++) {
				ai.checkInterruption();
				matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i)
						.getCol()] = BLAST;
			}

		}

	}

	private void emplirBlock(double[][] matrice, AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption();
		Collection<AiBlock> blocks = gameZone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		while (iteratorBlocks.hasNext()) {
			ai.checkInterruption();
			AiBlock block = iteratorBlocks.next();
			if (block.isDestructible()) {
				ai.checkInterruption();
				matrice[block.getLine()][block.getCol()] = MUR_DESTRUCTIBLE;
			} else {
				ai.checkInterruption();
				matrice[block.getLine()][block.getCol()] = MUR_INDESTRUCTIBLE;
			}
		}
	}

	private void emplirSur(double[][] matrice, AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption();
		for (int i = 0; i < gameZone.getHeight(); i++) {
			ai.checkInterruption();
			for (int j = 0; j < gameZone.getWidth(); j++) {
				ai.checkInterruption();
				if (matrice[i][j] == FOSSE) {
					ai.checkInterruption();
					matrice[i][j] = CASE_SUR;
				}

			}
		}

	}

	private void fillFiresMatrice(double[][] matrice, AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption();
		List<org.totalboumboum.ai.v201011.adapter.data.AiFire> fires = gameZone
				.getFires();
		Iterator<org.totalboumboum.ai.v201011.adapter.data.AiFire> iteratorFires = fires
				.iterator();
		while (iteratorFires.hasNext()) {
			ai.checkInterruption();
			org.totalboumboum.ai.v201011.adapter.data.AiFire fire = iteratorFires
					.next();
			matrice[fire.getLine()][fire.getCol()] = FLAMME;
		}
	}

	public double[][] getMatrice() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		return matrice;
	}
	public boolean isBonus(int i, int j) throws StopRequestException {
		ai.checkInterruption();
		boolean resultat = false;
		
		if (matrice[j][i] == 5 && matrice[j][i] == 6)
			resultat = true;
		
		return resultat;
	}
	public boolean isSafe(int i, int j) throws StopRequestException {
		ai.checkInterruption();
		boolean resultat = false;

		if (matrice[i][j] == 7 && isBonus(i, j))
			resultat = true;
		return resultat;
	}
	public boolean iswall(int x1, int y1) throws StopRequestException {
		ai.checkInterruption();
		boolean resultat = false;
		AiTile tile = gameZone.getTile(y1, x1);
		Collection<AiBlock> blocks = tile.getBlocks();
		if(!blocks.isEmpty()){
			AiBlock block = blocks.iterator().next();
			if (matrice[y1][x1] == 2 && block.isDestructible())
				resultat = true;
		}
		return resultat;

	}

	
}
