package org.totalboumboum.ai.v201011.ais.avcigungor.v5;

import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
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
public class AttackClass {

	AiZone gameZone;
	FillingClass fillingClass ;
	double matriceAttaque[][];
	private AiOutput aio;
	 AiHero ourHero;
	 private AvciGungor ai;
	public AttackClass(AvciGungor ai)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		this.ai = ai;
		gameZone = ai.getPercepts();
		aio=ai.getOutput();
		ourHero = gameZone.getOwnHero();

		// init A*
		//double costMatrix[][] = new double[gameZone.getHeight()][gameZone.getWidth()];
	//	
		
		
		updateMatrix();
		matriceAttaque = fillingClass.getMatrice();
		this.fillAttackMatrix();
/*		List<AiTile> heroTiles = new ArrayList<AiTile>();
		heroTiles.add(ourHero.getTile());

		costCalculator = new MatrixCostCalculator(costMatrix);
		heuristicCalculator = new BasicHeuristicCalculator();
		astar = new Astar(ai, ourHero, costCalculator,
				heuristicCalculator);

		// init destinations
		arrived = false;
		possibleDest = heroTiles;*/
		//updatePath();
		
		
		
	}

	
	/** zone de jeu */
	

	// ///////////////////////////////////////////////////////////////
	// DESTINATION /////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** indique si le personnage est arrivé à destination */
/*	private boolean arrived;
	*//** la case de destination sélectionnée pour la fuite *//*
	private AiTile tileDest;
	*//** destinations potentielles *//*
	private List<AiTile> possibleDest;
	private Astar astar;
	*//** classe implémentant la fonction heuristique *//*
	private HeuristicCalculator heuristicCalculator;
	*//** classe implémentant la fonction de coût *//*
	private MatrixCostCalculator costCalculator;*/

	private void fillAttackMatrix() throws StopRequestException {
		ai.checkInterruption();

		for (int i = 0; i < gameZone.getHeight(); i++) {
			ai.checkInterruption();
			for (int j = 0; j < gameZone.getWidth(); j++) {
				ai.checkInterruption();
				if (matriceAttaque[i][j] == 1) {
					ai.checkInterruption();
					
				}
				if (matriceAttaque[i][j] == 2) {
					ai.checkInterruption();
					if (matriceAttaque[i - 1][j] == 7) {
						ai.checkInterruption();
						matriceAttaque[i - 1][j] = 0;
						aio.setTileColor(i - 1, j, Color.GREEN);
						aio.setTileText(i - 1, j, "0");
						
					}
					if (matriceAttaque[i + 1][j] == 7) {
						ai.checkInterruption();
						matriceAttaque[i + 1][j] = 0;
						aio.setTileColor(i + 1, j, Color.GREEN);
						aio.setTileText(i + 1, j, "0");
					
					}
					if (matriceAttaque[i][j - 1] == 7) {
						ai.checkInterruption();
						matriceAttaque[i][j - 1] = 0;
						aio.setTileColor(i, j - 1, Color.GREEN);
						aio.setTileText(i, j - 1, "0");
						
					}
					if (matriceAttaque[i][j + 1] == 7) {
						ai.checkInterruption();
						matriceAttaque[i][j + 1] = 0;
						aio.setTileColor(i, j + 1, Color.GREEN);
						aio.setTileText(i, j + 1, "0");
					
					}
				}
				if (matriceAttaque[i][j] == 4) {
					ai.checkInterruption();

					Collection<AiBomb> bombs = gameZone.getBombs();
					Iterator<AiBomb> iteratorBombs = bombs.iterator();
					int bombaki = -240;
					while (iteratorBombs.hasNext()) {
						ai.checkInterruption();
						AiBomb bomb = iteratorBombs.next();
						matriceAttaque[bomb.getLine()][bomb.getCol()] = 3;

						if (bomb.getTime() < ((bomb.getNormalDuration() - bomb
								.getTime()) / 3)) {
							ai.checkInterruption();
							bombaki = -240;
						} else if (bomb.getTime() < ((2 * bomb
								.getNormalDuration() - bomb.getTime()) / 3)
								&& bomb.getTime() > ((bomb
										.getExplosionDuration() - bomb
										.getTime()) / 3)) {
							ai.checkInterruption();
							bombaki = -480;
						} else {
							ai.checkInterruption();
							bombaki = -720;
						}
						if (bomb.getLine() == i) {
							ai.checkInterruption();
							matriceAttaque[i][j] = (bombaki)
									/ (1 + Math.abs((bomb.getCol() - j)));
							
							String k = Integer.toString((bombaki)
									/ (1 + Math.abs((bomb.getCol() - j))));

							aio.setTileColor(i, j, Color.GREEN);
							aio.setTileText(i, j, k);
							
						}
						if (bomb.getCol() == j) {
							ai.checkInterruption();
						
							matriceAttaque[i][j] = (bombaki)
									/ (1 + Math.abs((bomb.getLine() - i)));
							String m = Integer.toString((bombaki)
									/ (1 + Math.abs((bomb.getLine() - i))));
							aio.setTileColor(i, j, Color.GREEN);
							aio.setTileText(i, j, m);
							
						}

					}
					//bombak = bombaki;
				}
				if (matriceAttaque[i][j] == 3) {
					ai.checkInterruption();
					matriceAttaque[i][j] = -1000;
					
					aio.setTileColor(i, j, Color.GREEN);
					aio.setTileText(i, j, "-1000");
					
				}
				if (matriceAttaque[i][j] == 5) {
					ai.checkInterruption();
					matriceAttaque[i][j] = 60;
					aio.setTileColor(i, j, Color.GREEN);
					aio.setTileText(i, j, "60");
					
				}
				if (matriceAttaque[i][j] == 6) {
					ai.checkInterruption();
					matriceAttaque[i][j] = 60;
					aio.setTileColor(i, j, Color.GREEN);
					aio.setTileText(i, j, "60");
			

				}
				if (matriceAttaque[i][j] == 7) {
					ai.checkInterruption();
				
				}
				if (matriceAttaque[i][j] == 8) {
					ai.checkInterruption();
					matriceAttaque[i][j] = 240;
					aio.setTileColor(i, j, Color.GREEN);
					aio.setTileText(i, j, "240");
					
				}
				if (matriceAttaque[i][j] == 9) {
					ai.checkInterruption();
				
				}
				if (matriceAttaque[i][j] == 10) {
					ai.checkInterruption();
					matriceAttaque[i][j] = -1000;
				
				}
			}
		}
	}

	private void updateMatrix() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		gameZone = ai.getPercepts();

		fillingClass = new FillingClass(this.ai);
		
	}
	public double[][] getMatrice() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		return matriceAttaque;
	}
	
	
}
