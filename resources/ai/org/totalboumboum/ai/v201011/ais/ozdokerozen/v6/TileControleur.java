package org.totalboumboum.ai.v201011.ais.ozdokerozen.v6;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;

/**
 * Class qui determine de poser bombe
 * 
 * @author Gizem Lara Özdöker
 * @author Sercan Özen
 * 
 */
@SuppressWarnings("deprecation")
public class TileControleur {
	/** */
	AiZone gameZone;
	/** */
	ArtificialIntelligence IA;
	/** */
	int[][] matriceImaginaire;
	/** */
	int[][] matriceTiles;
	/** */
	public List<AiTile> cibleSecure = new ArrayList<AiTile>();

	/** */
	int MURDESTRUCTIBLE = 5;
	/** */
	int MURINDESTRUCTIBLE = 0;
	/** */
	int FIRE = -2;
	/** */
	int SECURE = 1;
	/** */
	int BONUS = 10;
	/** */
	int SCOPE = -3;

	/** variable temporiel */
	AiTile temp;
	/** */
	public AiTile tileFuir;

	/**
	 * 
	 * @param ai
	 *            description manquante !
	 * @param matrice
	 *            description manquante !
	 * @throws StopRequestException
	 *             description manquante !
	 */
	public TileControleur(OzdokerOzen ai, int matrice[][])
			throws StopRequestException {
		ai.checkInterruption();
		IA = ai;
		gameZone = ai.getPercepts();
		int height = gameZone.getHeight();
		int width = gameZone.getWidth();
		matriceImaginaire = new int[height][width];
		matriceTiles = matrice;
		for (int i = 0; i < height; i++) {
			IA.checkInterruption();
			for (int j = 0; j < width; j++) {
				IA.checkInterruption();
				matriceImaginaire[i][j] = matrice[i][j];
			}
		}

	}

	/**
	 * Methode qui calcule les valeurs dans la matrice apres posage le bomb
	 * 
	 * @param placeABombe
	 *            description manquante !
	 * @throws StopRequestException
	 *             description manquante !
	 */
	public void placerBombe(AiTile placeABombe) throws StopRequestException {
		IA.checkInterruption();
		if (placeABombe != null) {
			// on place le bombe où on pense.
			matriceImaginaire[placeABombe.getLine()][placeABombe.getCol()] = -1;

			// on prend le range de notre bombe
			int bombeFire = gameZone.getOwnHero().getBombRange();

			// les cordinant de bomb imaginaire
			int bombX = placeABombe.getCol();
			int bombY = placeABombe.getLine();
			// On precise le domaine de bombe avec 4 for loop
			// on a utilisé 4 loop parceque on ne veut pas de tomber dans une
			// exception (IndexBoundException par exemple.)

			for (int i = bombY + 1; i <= bombY + bombeFire; i++) {
				IA.checkInterruption();
				if (i > matriceImaginaire.length - 1)
					break;
				if (matriceImaginaire[i][bombX] == MURDESTRUCTIBLE
						| matriceImaginaire[i][bombX] == MURINDESTRUCTIBLE)
					break;
				else
					matriceImaginaire[i][bombX] = -3;
			}

			for (int i = bombY - 1; i >= bombY - bombeFire; i--) {
				IA.checkInterruption();
				if (i < 0)
					break;
				if (matriceImaginaire[i][bombX] == MURDESTRUCTIBLE
						| matriceImaginaire[i][bombX] == MURINDESTRUCTIBLE)
					break;
				else
					matriceImaginaire[i][bombX] = -3;
			}
			for (int i = bombX + 1; i <= bombX + bombeFire; i++) {
				IA.checkInterruption();
				if (i > matriceImaginaire.length - 1)
					break;
				if (matriceImaginaire[bombY][i] == MURDESTRUCTIBLE
						| matriceImaginaire[bombY][i] == MURINDESTRUCTIBLE)
					break;
				else
					matriceImaginaire[bombY][i] = -3;
			}

			for (int i = bombX - 1; i >= bombX - bombeFire; i--) {
				IA.checkInterruption();
				if (i < 0)
					break;
				if (matriceImaginaire[bombY][i] == MURDESTRUCTIBLE
						| matriceImaginaire[bombY][i] == MURINDESTRUCTIBLE)
					break;
				else
					matriceImaginaire[bombY][i] = -3;
			}
		}

	}

	/**
	 * Methode qui retourne les tiles qu'on peut arrive (sans case scope)
	 * 
	 * @param debut
	 *            description manquante !
	 * @param tilesPossible
	 *            description manquante !
	 * @param matrice
	 *            description manquante !
	 * @throws StopRequestException
	 *             description manquante !
	 */
	public void tilePossibleArrive(AiTile debut, List<AiTile> tilesPossible,
			int[][] matrice) throws StopRequestException {
		IA.checkInterruption();

		for (int i = 0; i < debut.getNeighbors().size(); i++) {
			IA.checkInterruption();
			// Y k labilen duvara kars l k geliyorsa Salla
			if (matrice[debut.getNeighbors().get(i).getLine()][debut
					.getNeighbors().get(i).getCol()] == SECURE) {
				if (!(tilesPossible.contains(debut.getNeighbors().get(i)))) {
					tilesPossible.add(debut.getNeighbors().get(i));
					tilePossibleArrive(debut.getNeighbors().get(i),
							tilesPossible, matrice);
				}
			}
			if (matrice[debut.getNeighbors().get(i).getLine()][debut
					.getNeighbors().get(i).getCol()] == BONUS) {
				if (!(tilesPossible.contains(debut.getNeighbors().get(i)))) {
					tilesPossible.add(debut.getNeighbors().get(i));
					tilePossibleArrive(debut.getNeighbors().get(i),
							tilesPossible, matrice);

				}
			}
		}
	}

	/**
	 * Methode qui retourne les tiles qu'on peut arrive
	 * 
	 * @param debut
	 *            description manquante !
	 * @param tilesPossible
	 *            description manquante !
	 * @param scopes
	 *            description manquante !
	 * @param matrice
	 *            description manquante !
	 * @throws StopRequestException
	 *             description manquante !
	 */
	public void tilePossibleArriveAvecRisk(AiTile debut,
			List<AiTile> tilesPossible, List<AiTile> scopes, int[][] matrice)
			throws StopRequestException {
		IA.checkInterruption();
		for (int i = 0; i < debut.getNeighbors().size(); i++) {
			IA.checkInterruption();
			// eklenen k s m
			if (matrice[debut.getNeighbors().get(i).getLine()][debut
					.getNeighbors().get(i).getCol()] == SCOPE) {
				if (!(scopes.contains(debut.getNeighbors().get(i)))) {
					scopes.add(debut.getNeighbors().get(i));
					tilePossibleArriveAvecRisk(debut.getNeighbors().get(i),
							tilesPossible, scopes, matrice);
				}
			}
			// Y k labilen duvara kars l k geliyorsa Salla
			if (matrice[debut.getNeighbors().get(i).getLine()][debut
					.getNeighbors().get(i).getCol()] == SECURE) {
				if (!(tilesPossible.contains(debut.getNeighbors().get(i)))) {
					tilesPossible.add(debut.getNeighbors().get(i));
					tilePossibleArriveAvecRisk(debut.getNeighbors().get(i),
							tilesPossible, scopes, matrice);
				}
			}
			if (matrice[debut.getNeighbors().get(i).getLine()][debut
					.getNeighbors().get(i).getCol()] == BONUS) {
				if (!(tilesPossible.contains(debut.getNeighbors().get(i)))) {
					tilesPossible.add(debut.getNeighbors().get(i));
					tilePossibleArrive(debut.getNeighbors().get(i),
							tilesPossible, matrice);

				}
			}

		}
	}

	/**
	 * Methode qui tie les path accecible
	 * 
	 * @param secureTiles
	 *            description manquante !
	 * @throws StopRequestException
	 *             description manquante !
	 */
	public void tierLesTilesParDistanceEtAccebilite(List<AiTile> secureTiles)
			throws StopRequestException {
		IA.checkInterruption();

		// //////////////////////////////////
		// NOT:burada yoldaki di er bombalar ate leri felan katmad n!
		// ////////////////////////////////////
		// premierement on remove les cible qu'on ne peut pas arriver
		// Alors on les tie par distance
		for (int i = secureTiles.size(); i > 2; i--) {
			IA.checkInterruption();
			for (int j = 0; j < i - 1; j++) {
				IA.checkInterruption();
				if (calculerDistance(secureTiles.get(j)) > calculerDistance(secureTiles
						.get(j + 1))) {
					temp = secureTiles.get(j + 1);
					secureTiles.remove(j + 1);
					secureTiles.add(j + 1, secureTiles.get(j));
					secureTiles.remove(j);
					secureTiles.add(j, temp);
				}
			}
		}

	}

	/**
	 * Methode qui tie les path accecible
	 * 
	 * @param secureTiles
	 *            description manquante !
	 * @param debut
	 *            description manquante !
	 * @throws StopRequestException
	 *             description manquante !
	 */
	public void tierLesTilesParDistanceEtAccebilite(List<AiTile> secureTiles,
			AiTile debut) throws StopRequestException {
		IA.checkInterruption();

		for (int i = secureTiles.size(); i > 2; i--) {
			IA.checkInterruption();
			for (int j = 0; j < i - 1; j++) {
				IA.checkInterruption();
				if (calculerDistance(secureTiles.get(j), debut) > calculerDistance(
						secureTiles.get(j + 1), debut)) {
					temp = secureTiles.get(j + 1);
					secureTiles.remove(j + 1);
					secureTiles.add(j + 1, secureTiles.get(j));
					secureTiles.remove(j);
					secureTiles.add(j, temp);
				}
			}
		}

	}

	/**
	 * Methode qui calcule le path plus court
	 * 
	 * @param notreHero
	 *            description manquante !
	 * @param startPoint
	 *            description manquante !
	 * @param endPoint
	 *            description manquante !
	 * @return les path plus court d'apres que A*
	 * @throws StopRequestException
	 *             description manquante !
	 */
	public AiPath calculateShortestPath(AiHero notreHero, AiTile startPoint,
			AiTile endPoint) throws StopRequestException {
		IA.checkInterruption();

		// le chemin le plus court possible
		AiPath shortestPath = null;
		// L'objet pour implementer l'algo A*
		Astar astar;
		// Calcul du cout et d'heuristic par la classe de l'API

		if (endPoint != null) {
			BasicCostCalculator cost = new BasicCostCalculator();
			HeuristicCalculator heuristic = new BasicHeuristicCalculator();
			astar = new Astar(IA, notreHero, cost, heuristic);
			try {
				shortestPath = astar.processShortestPath(startPoint, endPoint);
				return shortestPath;
			} catch (LimitReachedException e) {
				//
				//e.printStackTrace();
				return null;
			}
		} else
			return null;

	}

	/**
	 * Methode qui calcule un path d'apres algorithm DFS(depth-first-search)
	 * 
	 * @param debut
	 *            description manquante !
	 * @param fin
	 *            description manquante !
	 * @param onAVu
	 *            description manquante !
	 * @param path
	 *            description manquante !
	 * @param tilesPossibles
	 *            description manquante !
	 * @return ? description manquante !
	 * @throws StopRequestException
	 *             description manquante !
	 */
	boolean pathFinder(AiTile debut, AiTile fin, List<AiTile> onAVu,
			List<AiTile> path, List<AiTile> tilesPossibles)
			throws StopRequestException {
		IA.checkInterruption();

		boolean retour = false;
		for (int i = 0; i < debut.getNeighbors().size(); i++) {
			IA.checkInterruption();
			if (debut.getNeighbors().get(i).equals(fin)) {
				IA.checkInterruption();
				retour = true;
				if (!(path.contains(fin)) && !(path.contains(debut))) {
					path.add(fin);
					path.add(debut);
				}
				break;
			} else if (tilesPossibles.contains(debut.getNeighbors().get(i))
					&& !(onAVu.contains(debut.getNeighbors().get(i)))) {
				onAVu.add(debut.getNeighbors().get(i));
				retour = pathFinder(debut.getNeighbors().get(i), fin, onAVu,
						path, tilesPossibles);
				if (retour == true) {
					path.add(debut);
					break;
				}
			} else if (!(tilesPossibles.contains(debut.getNeighbors().get(i)))) {
				retour = false;
			}

		}
		return retour;
	}

	/**
	 * Mehotde qui cree une path meilleur selon les informations de list de tile
	 * de recherche DFS(depth first search)
	 * 
	 * @param path
	 *            description manquante !
	 * @param pathOptimise
	 *            description manquante !
	 * @param fin
	 *            description manquante !
	 * @throws StopRequestException
	 *             description manquante !
	 */
	void ameliorePath(List<AiTile> path, AiPath pathOptimise, AiTile fin)
			throws StopRequestException {
		IA.checkInterruption();

		int j = 0;
		List<AiTile> dejavu = new ArrayList<AiTile>();
		for (int i = (path.size() - 1); i >= 0; i--) {
			IA.checkInterruption();
			if (path.get(i).equals(gameZone.getOwnHero().getTile())
					&& !(dejavu.contains(path.get(i)))) {
				pathOptimise.addTile(path.get(i));
				dejavu.add(path.get(i));

				j++;
			} else if (calculerDistance(fin, path.get(i)) == 0) {
				pathOptimise.addTile(path.get(i));
				dejavu.add(path.get(i));
				break;
			} else {
				AiTile temp;
				List<AiTile> list = new ArrayList<AiTile>();
				list = pathOptimise.getTiles().get(j - 1).getNeighbors();
				for (int k = 0; k < list.size(); k++) {
					IA.checkInterruption();
					if (!(path.contains(list.get(k)))) {
						list.remove(k);
						k--;
					} else if (dejavu.contains(list.get(k))) {
						// ecran.setTileColor(list.get(k), Color.ORANGE);
						list.remove(k);
						k--;
					}
				}
				if (list.size() == 1) {
					pathOptimise.addTile(path.get(i));
					dejavu.add(path.get(i));
					j++;
				} else if (list.size() > 1) {
					temp = list.get(0);
					for (int k = 1; k < list.size(); k++) {
						IA.checkInterruption();
						if (calculerDistance(fin, list.get(k - 1)) > calculerDistance(
								fin, list.get(k)))
							temp = list.get(k);

					}
					pathOptimise.addTile(temp);
					i = path.indexOf(temp);
					dejavu.add(temp);
					j++;
				}

			}
		}
		// Si on passe le meme tile deux fois, on peux les effacer
		AiTile tileControl;

		for (int i = 0; i < pathOptimise.getTiles().size(); i++) {
			IA.checkInterruption();
			tileControl = pathOptimise.getTiles().get(i);
			for (j = i + 1; j < pathOptimise.getTiles().size(); j++) {
				IA.checkInterruption();
				if (tileControl.equals(pathOptimise.getTiles().get(j))) {
					for (int k = j; k > i; k--) {
						IA.checkInterruption();
						pathOptimise.removeTile(k);

					}
				}
			}

		}
	}

	/**
	 * Methode qui calculent entre deux tile simplement
	 * 
	 * @param tile
	 *            description manquante !
	 * @param debut
	 *            description manquante !
	 * @return description manquante !
	 * @throws StopRequestException
	 *             description manquante !
	 */
	public double calculerDistance(AiTile tile, AiTile debut)
			throws StopRequestException {
		IA.checkInterruption();

		// Basit versiyon
		double hX = debut.getPosX();
		double hY = debut.getPosY();
		// les coordinant de cible
		double cibleX = tile.getPosX();
		double cibleY = tile.getPosY();
		int result;
		result = (int) ((Math.abs(hX - cibleX)) + (Math.abs(hY - cibleY)));
		return result;
	}

	/**
	 * Methode calculant plus court distance entre hero et un cible
	 * 
	 * @param tile
	 *            description manquante !
	 * @return la distance description manquante !
	 * @throws StopRequestException
	 *             description manquante !
	 */
	public double calculerDistance(AiTile tile) throws StopRequestException {
		IA.checkInterruption();

		// Basit versiyon
		// les coordinant de hero
		double heroX = gameZone.getOwnHero().getPosX();
		double heroY = gameZone.getOwnHero().getPosY();

		// les coordinant de cible
		double cibleX = tile.getPosX();
		double cibleY = tile.getPosY();
		double result;
		result = (Math.abs(heroX - cibleX)) + (Math.abs(heroY - cibleY)) - 1;
		return result;
	}

	/**
	 * Methode qui calcule possiblite de fuir
	 * 
	 * @param espacePourFuir
	 *            description manquante !
	 * @param placeBombe
	 *            description manquante !
	 * @return si vrai retourne 0 sinon -1
	 * @throws StopRequestException
	 *             description manquante !
	 * @throws LimitReachedException
	 *             description manquante !
	 */
	public int possibleDeFuir(AiTile espacePourFuir, AiTile placeBombe)
			throws StopRequestException, LimitReachedException {
		IA.checkInterruption();

		AiPath temp;
		AiHero hero = gameZone.getOwnHero();

		if (espacePourFuir == null)
			return -1;
		else {
			temp = calculateShortestPath(hero, hero.getTile(), espacePourFuir);
			if(temp==null)
				return -1;
			else
			{// Bu de eri alamad g m z i in sabit
				double durationBombe = 2400;
				// System.out.println("Bomba zaman "+hero.getBombDuration());
				if (durationBombe > temp.getDuration(hero)) {
					return 0;
				} else {
					return -1;
				}
			}
		}
	}

	/**
	 * Methode qui nous dit la decision la posage bombe à un tile
	 * 
	 * @param placeABombe
	 *            description manquante !
	 * @return la decision
	 * @throws StopRequestException
	 *             description manquante !
	 */
	public boolean estQueOnPoseBombe(AiTile placeABombe)
			throws StopRequestException {
		IA.checkInterruption();

		placerBombe(placeABombe);
		boolean result = false;
		AiTile cible = null;
		List<AiTile> scopes = new ArrayList<AiTile>();
		tilePossibleArriveAvecRisk(placeABombe, cibleSecure, scopes,
				matriceImaginaire);
		cible = tileBFS(placeABombe);
		try {
			tierLesTilesParDistanceEtAccebilite(cibleSecure, placeABombe);
			if (possibleDeFuir(cible, placeABombe) >= 0) {
				// oui,secure à poser a bombe dans le tile "placeABombe"
				// System.out.println("bomba koyup ka abiliriz");
				result = true;
			} else {
				// System.out.println("bomba koymayal m cunku guvenli yer yok");
				// non,on doit faire une autre chose
				result = false;
			}
		} catch (LimitReachedException e) {
			//
			//e.printStackTrace();
		}
		return result;

	}

	/**
	 * Methode qui calcule possiblite de fuir
	 * 
	 * @param espacePourFuir
	 * @param placeBombe
	 * @return si vrai retourne 0 sinon -1
	 * @throws StopRequestException
	 * @throws LimitReachedException
	 */

	/**
	 * Retourne premier case sure d'apres algorith BFS
	 * 
	 * @param poseBombe
	 *            description manquante !
	 * @return description manquante !
	 * @throws StopRequestException
	 *             description manquante !
	 */
	public AiTile tileBFS(AiTile poseBombe) throws StopRequestException {
		IA.checkInterruption();

		AiTile cible = null;
		boolean flag = false;

		List<AiTile> scopes = new ArrayList<AiTile>();
		List<AiTile> voisins = new ArrayList<AiTile>();
		for (int i = 0; i < poseBombe.getNeighbors().size(); i++) {
			IA.checkInterruption();
			voisins.add(poseBombe.getNeighbors().get(i));
		}
		for (int i = 0; i < voisins.size(); i++) {
			IA.checkInterruption();
			if (matriceImaginaire[voisins.get(i).getLine()][voisins.get(i)
					.getCol()] == SCOPE) {
				scopes.add(voisins.get(i));
			}
		}
		for (int i = 0; i < scopes.size(); i++) {
			IA.checkInterruption();
			List<AiTile> tempVoision = scopes.get(i).getNeighbors();
			for (int j = 0; j < tempVoision.size(); j++) {
				IA.checkInterruption();
				if (matriceImaginaire[tempVoision.get(j).getLine()][tempVoision
						.get(j).getCol()] == SECURE) {
					cible = tempVoision.get(j);
					// System.out.println("fonksiyon icinden"+tempVoision.get(j));
					flag = true;
					break;
				} else if (matriceImaginaire[tempVoision.get(j).getLine()][tempVoision
						.get(j).getCol()] == SCOPE) {
					if (!scopes.contains(tempVoision.get(j))) {
						scopes.add(tempVoision.get(j));
					}
				}
			}
			if (flag)
				break;

		}
		tileFuir = cible;
		return cible;
	}
}
