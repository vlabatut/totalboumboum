package org.totalboumboum.ai.v200910.ais.aldanmazyenigun.v5;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @version 5
 * 
 * @author Cansin Aldanmaz
 * @author Yalcin Yenigun
 *
 */
public class AldanmazYenigun extends ArtificialIntelligence {

	private AiHero ownHero = null; //notre joueur

	private AiZone zone = null;

	private AiTile caseActuelle = null; //la case actuelle du joueur

	private AvoidController escapeManager = null; //la classe qui controlle la case sur.

	private ItemController safeManager = null; //pour controller les items

	private ItemController abstractSafeManager = null;

	private WallController putBombController = null;

	private AbstractBombController abstractBombController = null;

	private HeroController abstractHeroController = null;

	private AiTile targetPreviousTile;


	private AiHero targetHero = null;

	/** classe chargée de déterminer quelles cases sont s�res */
	private SafetyZone safetyZone = null;

	private boolean thereIsSafeTile = true;

	private boolean bonusAccessible = true;

	private boolean heroAccessible = true;

	private boolean assezArmee = false;

	// private boolean isThereAnyHeroInBombeRange = false;

	private boolean hasArrivedButDanger = false;

	/** les coordonnées de notre hero */
	@SuppressWarnings("unused")
	private double x;

	@SuppressWarnings( { "unused" })
	private double y;

	private PathController targetManager = null;

	@Override
	public AiAction processAction() throws StopRequestException {

		checkInterruption(); // APPEL OBLIGATOIRE

		if (ownHero == null)
			init(); //on initialise les matrices.

		AiAction result = new AiAction(AiActionName.NONE);

		updateMatrix(); //on met a jour les matrices.

		boolean start = false;
		if (!ownHero.hasEnded()) { 

			updateLocation(); // on met à jour la position de l'ia dans la zone
			Direction moveDir = Direction.NONE;
			findIsBonusAccessible(); //on trouve si il y a un bonus accessible
			
			if (ownHero.getBombNumber() >= 3) //si on a 3 bombes, on est assez armee.
				assezArmee = true;

			// si on est en train de fuir : on continue
			if (escapeManager != null) {
				if (escapeManager.hasArrived()) {
					escapeManager = null;
				} else
					moveDir = escapeManager.update();
				result = new AiAction(AiActionName.MOVE, moveDir);
			}
			// sinon si on est en danger : on commence à fuir
			else if (!isSafe(caseActuelle)) {
				escapeManager = new AvoidController(this);
				moveDir = escapeManager.update();
				result = new AiAction(AiActionName.MOVE, moveDir);
			} 
			// si on est en train de collecter les bonus : on continue 
			 else if (safeManager != null) {
				if (safeManager.hasArrived()) {
					safeManager = null;
				} else {
					moveDir = safeManager.update();

					if (ownHero.getTile().getNeighbor(moveDir) != null)
						if (!isSafe(ownHero.getTile().getNeighbor(moveDir)))
							moveDir = Direction.NONE;
				}
				result = new AiAction(AiActionName.MOVE, moveDir);

			}
			// sinon si on n'est pas sur le bonus et s'il y a les bonus accessibles et 
			// si on n'est pas assez armee: on commence à collecter les bonus
			else if (!isBonus(caseActuelle)
					&& !findItemsTiles(caseActuelle).isEmpty()
					&& bonusAccessible && !assezArmee) {
				safeManager = new ItemController(this);
				moveDir = safeManager.update();
				result = new AiAction(AiActionName.MOVE, moveDir);
			}
			//si le rival est accessible et on est assez arm�� on l'attaque
			else if (heroAccessible) {
				updateTarget();

				List<AiHero> heroess = new ArrayList<AiHero>(zone
						.getRemainingHeroes());
				heroess.remove(ownHero);

				for (AiHero h : heroess) {
					checkInterruption();
					if (h.getCol() != ownHero.getCol()) {
						result = new AiAction(AiActionName.MOVE, moveDir);
					} else {

						if (Math.abs(h.getLine() - ownHero.getLine()) <= 2)
							start = true;
						else
							result = new AiAction(AiActionName.MOVE, moveDir);
					}

					if (h.getLine() != ownHero.getLine()) {
						result = new AiAction(AiActionName.MOVE, moveDir);
					} else {

						if (Math.abs(h.getCol() - ownHero.getCol()) <= 2)
							start = true;
						else
							result = new AiAction(AiActionName.MOVE, moveDir);
					}

				}

				if (targetHero != null){
					moveDir = targetManager.update();
				}

				if (targetManager.hasArrived())
					start = true;
				else {
					if (targetHero != null) {
						if (targetHero.getCol() != ownHero.getCol()) {
							result = new AiAction(AiActionName.MOVE, moveDir);
						} else {
							if (Math.abs(targetHero.getLine()
									- ownHero.getLine()) <= 2)
								start = true;
							else
								result = new AiAction(AiActionName.MOVE,
										moveDir);
						}

						if (targetHero.getLine() != ownHero.getLine()) {
							result = new AiAction(AiActionName.MOVE, moveDir);
						} else {

							if (Math.abs(targetHero.getCol()
									- ownHero.getCol()) <= 2)
								start = true;
							else
								result = new AiAction(AiActionName.MOVE,
										moveDir);
						}
						if ((ownHero.getLine() == targetHero.getLine())
								&& (ownHero.getCol() == targetHero.getCol())) {
							start = true;
						}
					}
				}
				result = new AiAction(AiActionName.MOVE, moveDir);

				if (targetHero != null) {
					if (targetHero.hasEnded())
						chooseTarget();
				}
			}		
			else if (putBombController != null) {
				chooseTarget();

				if (putBombController.hasArrived()) {
					putBombController = null;
					hasArrivedButDanger = false;
					start = true;
				} else {
					moveDir = putBombController.update();
					if (ownHero.getTile().getNeighbor(moveDir) != null)
						if (!isSafe(ownHero.getTile().getNeighbor(moveDir)))
							moveDir = Direction.NONE;
				}
				result = new AiAction(AiActionName.MOVE, moveDir);
				if (targetHero != null) {
					if (targetHero.getCol() != ownHero.getCol()) {
						result = new AiAction(AiActionName.MOVE, moveDir);
					} else {
						if (Math.abs(targetHero.getLine() - ownHero.getLine()) <= 2)
							start = true;
						else
							result = new AiAction(AiActionName.MOVE, moveDir);
					}

					if (targetHero.getLine() != ownHero.getLine()) {
						result = new AiAction(AiActionName.MOVE, moveDir);
					} else {
						if (Math.abs(targetHero.getCol() - ownHero.getCol()) <= 2)
							start = true;
						else
							result = new AiAction(AiActionName.MOVE, moveDir);
					}
					if ((ownHero.getLine() == targetHero.getLine())
							&& (ownHero.getCol() == targetHero.getCol())) {
						start = true;
					}
				}
			}else if (!findWallTiles(caseActuelle).isEmpty()) { //si on est en train de trouver les murs, on continue
				chooseTarget();
				putBombController = new WallController(this);
				moveDir = putBombController.update();
				result = new AiAction(AiActionName.MOVE, moveDir);
			}


			if (start) { //start signifie que on met une bombe en controllant
				if (abstractBombController != null) {
					if (abstractBombController.hasArrived()) {
						abstractBombController = null;
					} else {
						if (abstractBombController.isThereSafeTiles()) {
							if (abstractBombController.getPath().getDuration(
									ownHero) <= 2300
									&& abstractBombController.getPath()
											.getDuration(ownHero) != 0) {
								if (heroAccessible) {
									if (ownHero.getBombCount() < 3)
										result = new AiAction(
												AiActionName.DROP_BOMB);
								} else {
									if (ownHero.getBombCount() < 2)
										result = new AiAction(
												AiActionName.DROP_BOMB);
								}
							} else {
								hasArrivedButDanger = true;
							}
						}
					}
					start = false;
				} else if (!findAbstractSafeTiles(caseActuelle).isEmpty()) {
					abstractBombController = new AbstractBombController(this);
					start = false;
				}
			}

		}
		return result;
	}

	//initialisation
	private void init() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		zone = getPercepts();
		ownHero = zone.getOwnHero();

		updateLocation();

		targetManager = new PathController(this, caseActuelle);
	}
	//on met a jour la matrice
	private void updateMatrix() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		zone = getPercepts();
		safetyZone = new SafetyZone(this);
	}

	private void updateLocation() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		caseActuelle = ownHero.getTile();
		x = ownHero.getPosX();
		y = ownHero.getPosY();
	}

	/**
	 * renvoi le niveau de danger de la case
	 */
	public double getDangerLevel(AiTile tile) throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		return safetyZone.getDangerLevel(tile);
	}

	/**
	 * trouve les cases surs
	 */
	public List<AiTile> findSafeTiles(AiTile origin)
			throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		List<AiTile> result = new ArrayList<AiTile>();
		for (int line = 0; line < zone.getHeight(); line++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int col = 0; col < zone.getWidth(); col++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(line, col);
				int x = tile.getCol();
				int y = tile.getLine();

				if (safetyZone.isSafe(x, y))
					result.add(tile);

			}
		}
		return result;

	}

	/**
	 * trouve les cases abstraites surs si on est en train de poser une bombe
	 */
	public List<AiTile> findAbstractSafeTiles(AiTile origin)
			throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		List<AiTile> result = new ArrayList<AiTile>();
		for (int line = 0; line < zone.getHeight(); line++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int col = 0; col < zone.getWidth(); col++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(line, col);
				int x = tile.getCol();
				int y = tile.getLine();

				if (safetyZone.abstractIsSafe(x, y))
					result.add(tile);
			}
		}
		return result;
	}

	/**
	 * s'il y a une case sur, c'est true.
	 */
	public boolean getThereIsSafeTile() throws StopRequestException {
		checkInterruption();
		return this.thereIsSafeTile;
	}

	/**
	 * trouve les lieux des items
	 */
	public List<AiTile> findItemsTiles(AiTile origin)
			throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		List<AiTile> result = new ArrayList<AiTile>();
		for (int line = 0; line < zone.getHeight(); line++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int col = 0; col < zone.getWidth(); col++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(line, col);
				int x = tile.getCol();
				int y = tile.getLine();

				if (safetyZone.isBonus(x, y))
					result.add(tile);
			}
		}
		return result;
	}
	

	/**
	 * trouve les lieux des heros
	 */
	public List<AiTile> findHerosTiles(AiTile origin)
			throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		List<AiTile> result = new ArrayList<AiTile>();
		for (int line = 1; line < zone.getHeight() - 1; line++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int col = 1; col < zone.getWidth() - 1; col++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(line, col);
				int x = tile.getCol();
				int y = tile.getLine();

				if (safetyZone.isHero(x - 1, y))
					result.add(tile);

				if (safetyZone.isHero(x + 1, y))
					result.add(tile);

				if (safetyZone.isHero(x, y - 1))
					result.add(tile);

				if (safetyZone.isHero(x, y + 1))
					result.add(tile);

			}
		}

		return result;
	}

	/**
	 * trouve les lieux des murs
	 */
	public List<AiTile> findWallTiles(AiTile origin)
			throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		List<AiTile> result = new ArrayList<AiTile>();
		for (int line = 1; line < zone.getHeight() - 1; line++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int col = 1; col < zone.getWidth() - 1; col++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				AiTile tile = zone.getTile(line, col);
				int x = tile.getCol();
				int y = tile.getLine();

				if (safetyZone.iswall(x - 1, y))
					result.add(tile);

				else if (safetyZone.iswall(x + 1, y))
					result.add(tile);

				else if (safetyZone.iswall(x, y - 1))
					result.add(tile);

				else if (safetyZone.iswall(x, y + 1))
					result.add(tile);

				if (hasArrivedButDanger && (tile == caseActuelle))
					result.remove(tile);
			}
		}

		return result;

	}


	public AiHero getOwnHero() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		return ownHero;
	}

	public AiZone getZone() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		return zone;
	}

	public AiTile getActualTile() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		return caseActuelle;
	}

	public SafetyZone getZoneFormee() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		return safetyZone;
	}

	private boolean isSafe(AiTile tile) throws StopRequestException {
		checkInterruption();
		boolean result = false;

		try {
			result = safetyZone.isSafe(tile.getCol(), tile.getLine());
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}
		return result;
	}

	private boolean isBonus(AiTile tile) throws StopRequestException {
		checkInterruption();
		boolean result = false;

		try {
			result = safetyZone.isBonus(tile.getCol(), tile.getLine());
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("unused")
	private boolean isWall(AiTile tile) throws StopRequestException {
		checkInterruption();
		boolean result = false;

		try {
			result = safetyZone.iswall(tile.getCol(), tile.getLine());
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}
		return result;
	}

	private boolean isHero(AiTile tile) throws StopRequestException {
		checkInterruption();
		boolean result = false;

		try {
			result = safetyZone.isHero(tile.getCol(), tile.getLine());
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}
		return result;
	}

	private void findIsBonusAccessible() throws StopRequestException {
		checkInterruption();

		Direction dir = Direction.NONE;

		if (abstractSafeManager != null) {
			if (abstractSafeManager.hasArrived()) {
				abstractSafeManager = null;
			} else {
				dir = abstractSafeManager.update();
				if (ownHero.getTile().getNeighbor(dir) != null)
					if (!isSafe(ownHero.getTile().getNeighbor(dir)))
						dir = Direction.NONE;
			}
		} else if (!isBonus(caseActuelle)
				&& !findItemsTiles(caseActuelle).isEmpty()) {
			abstractSafeManager = new ItemController(this);
			dir = abstractSafeManager.update();
		}
	}

	private void findIsHeroAccessible(AiHero hero) throws StopRequestException {
		checkInterruption();

		Direction dir = Direction.NONE;

		if (abstractHeroController != null) {
			if (abstractHeroController.hasArrived()) {
				abstractHeroController = null;
			} else {
				dir = abstractHeroController.update();
				if (ownHero.getTile().getNeighbor(dir) != null)
					if (!isHero(ownHero.getTile().getNeighbor(dir)))
						dir = Direction.NONE;
			}
		} else if (!findHerosTiles(caseActuelle).isEmpty()) {
			abstractHeroController = new HeroController(this, hero);
			dir = abstractHeroController.update();
		}
	}





	public void setAccessible(boolean accessible) {
		this.bonusAccessible = accessible;
	}

	public boolean isBonusAccessible() {
		return bonusAccessible;
	}

	public void setHeroAccessible(boolean accessible) {
		this.heroAccessible = accessible;
	}

	public boolean isHeroAccessible() {
		return heroAccessible;
	}

	/**
	 * choisit al�atoirement un joueur comme cible à suivre
	 * 
	 * @throws StopRequestException
	 */

	private void chooseTarget() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		targetHero = null;
		ArrayList<AiHero> heros = new ArrayList<AiHero>(zone
				.getRemainingHeroes());
		heros.remove(ownHero);

		if (!heros.isEmpty()) {
			targetHero = heros.get(0);
			findIsHeroAccessible(targetHero);
		}
		if (!heroAccessible) {
			if (heros.size() > 1) {
				targetHero = heros.get(1);
				findIsHeroAccessible(targetHero);
			}
		}
		if (!heroAccessible) {
			if (heros.size() > 2) {
				targetHero = heros.get(2);
				findIsHeroAccessible(targetHero);
			}
		}
		if (!heroAccessible) {
			if (heros.size() > 3) {
				targetHero = heros.get(3);
				findIsHeroAccessible(targetHero);
			}
		}

	}

	/**
	 * met à jour la cible, et éventuellement le chemin jusqu'� elle
	 */
	private void updateTarget() throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		if (targetHero == null || targetHero.hasEnded()) {
			chooseTarget();
			if (targetHero != null) {
				AiTile targetCurrentTile = targetHero.getTile();
				targetManager.setDestination(targetCurrentTile);
				targetPreviousTile = targetCurrentTile;
			}
		} else {
			AiTile targetCurrentTile = targetHero.getTile();
			if (targetCurrentTile == caseActuelle) {
				double targetX = targetHero.getPosX();
				double targetY = targetHero.getPosY();
				targetManager.setDestination(targetX, targetY);
			} else if (targetCurrentTile != targetPreviousTile) {
				targetManager.setDestination(targetCurrentTile);
				targetPreviousTile = targetCurrentTile;
			}

		}
	}

	public AiHero getTargetHero() throws StopRequestException {
		checkInterruption();
		return targetHero;
	}


}
