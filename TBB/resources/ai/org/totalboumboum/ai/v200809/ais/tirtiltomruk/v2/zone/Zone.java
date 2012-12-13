package org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2.zone;

import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v200809.adapter.AiBlock;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiFire;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiItem;
import org.totalboumboum.ai.v200809.adapter.AiItemType;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2.TirtilTomruk;

/**
*
* @author Abdullah Tırtıl
* @author Mert Tomruk
*
*/
@SuppressWarnings("deprecation")
public class Zone {
	/** Une personnalisation generale du AiZone */
	private AiZone zone;
	/** */
	private Collection<AiHero> rivals;
	/** */
	private AiHero caractere;
	/** */
	private Collection<AiBomb> bombes;
	/** */
	private Collection<AiBlock> blocs;
	/** */
	private Collection<AiItem> objets;
	/** */
	private Collection<AiFire> feus;
	/** */
	private int xMax;
	/** */
	private int yMax;
	/** */
	private ZoneEnum[][] zoneArray;
	/** */
	private TirtilTomruk source;
	/** */
	private int lastSimulatedBombExplodes;

	/**
	 * 
	 * @param zone
	 * @param source
	 * @throws StopRequestException
	 */
	public Zone(AiZone zone, TirtilTomruk source) throws StopRequestException {
		source.checkInterruption(); // Appel Obligatoire
		this.source = source;
		this.zone = zone;
		this.caractere = zone.getOwnHero();
		rivals = zone.getHeroes();
		this.bombes = zone.getBombs();
		this.blocs = zone.getBlocks();
		this.objets = zone.getItems();
		this.feus = zone.getFires();
		this.xMax = zone.getWidth();
		this.yMax = zone.getHeight();
		init();
	}

	/**
	 * 
	 * @throws StopRequestException
	 */
	private void init() throws StopRequestException {
		source.checkInterruption(); // Appel Obligatoire
		zoneArray = new ZoneEnum[xMax][yMax];
		int i, j;
		// Initialisation
		for (i = 0; i < xMax; i++) {
			source.checkInterruption(); // Appel Obligatoire
			for (j = 0; j < yMax; j++) {
				source.checkInterruption(); // Appel Obligatoire
				zoneArray[i][j] = ZoneEnum.LIBRE;
			}
		}
		// Mettons notre caractere
		//zoneArray[caractere.getTile().getCol()][caractere.getTile().getLine()] = ZoneEnum.CARACTERE;

		// Mettons nos rivals
		Iterator<AiHero> itRivals = rivals.iterator();
		while (itRivals.hasNext()) {
			source.checkInterruption(); // Appel Obligatoire
			AiHero temp = itRivals.next();
			if (!temp.equals(caractere))
				zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.RIVAL;
		}

		// Mettons les blocs
		Iterator<AiBlock> itBlocs = blocs.iterator();
		while (itBlocs.hasNext()) {
			source.checkInterruption(); // Appel Obligatoire
			AiBlock temp = itBlocs.next();
			if (temp.isDestructible())
				zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.BLOCDEST;
			else
				zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.BLOCINDEST;
		}

		// Mettons les bombes et les feus possibles
		Iterator<AiBomb> itBombes = bombes.iterator();
		while (itBombes.hasNext()) {
			source.checkInterruption(); // Appel Obligatoire
			AiBomb temp = itBombes.next();

			/*
			 * for(int k = temp.getCol() - temp.getRange(); k < temp.getCol() +
			 * temp.getRange() && k < xMax && (zoneArray[k][temp.getLine()] !=
			 * ZoneEnum.BLOCDEST || zoneArray[k][temp.getLine()] !=
			 * ZoneEnum.BLOCINDEST); k++) zoneArray[k][temp.getLine()] =
			 * ZoneEnum.FEUPOSSIBLE; for(int k = temp.getLine() -
			 * temp.getRange(); k < temp.getLine() + temp.getRange() && k < yMax
			 * && (zoneArray[temp.getCol()][k] != ZoneEnum.BLOCDEST ||
			 * zoneArray[temp.getCol()][k] != ZoneEnum.BLOCINDEST); k++)
			 * zoneArray[temp.getCol()][k] = ZoneEnum.FEUPOSSIBLE;
			 */

			// Les tiles dangeureux
			int k = 1;
			// Est-ce qu'on continue sur ces directions?
			boolean up = true;
			boolean down = true;
			boolean left = true;
			boolean right = true;
			while (k <= temp.getRange() && (up || down || left || right)) {
				source.checkInterruption(); // Appel Obligatoire
				if (up) {
					if ((zoneArray[temp.getCol()][temp.getLine() - k] != ZoneEnum.BLOCDEST)
							&& (zoneArray[temp.getCol()][temp.getLine() - k] != ZoneEnum.BLOCINDEST)) {
						zoneArray[temp.getCol()][temp.getLine() - k] = ZoneEnum.FEUPOSSIBLE;
					} else {
						up = false;
					}
				}
				if (down) {
					if ((zoneArray[temp.getCol()][temp.getLine() + k] != ZoneEnum.BLOCDEST)
							&& (zoneArray[temp.getCol()][temp.getLine() + k] != ZoneEnum.BLOCINDEST)) {
						zoneArray[temp.getCol()][temp.getLine() + k] = ZoneEnum.FEUPOSSIBLE;
					} else {
						down = false;
					}
				}
				if (left) {
					if ((zoneArray[temp.getCol() - k][temp.getLine()] != ZoneEnum.BLOCDEST)
							&& (zoneArray[temp.getCol() - k][temp.getLine()] != ZoneEnum.BLOCINDEST)) {
						zoneArray[temp.getCol() - k][temp.getLine()] = ZoneEnum.FEUPOSSIBLE;
					} else {
						left = false;
					}
				}
				if (right) {
					if ((zoneArray[temp.getCol() + k][temp.getLine()] != ZoneEnum.BLOCDEST)
							&& (zoneArray[temp.getCol() + k][temp.getLine()] != ZoneEnum.BLOCINDEST)) {
						zoneArray[temp.getCol() + k][temp.getLine()] = ZoneEnum.FEUPOSSIBLE;
					} else {
						right = false;
					}
				}
				k++;
			}
		}

		// Mettons les bombes
		Iterator<AiBomb> itBombes2 = bombes.iterator();
		while (itBombes2.hasNext()) {
			source.checkInterruption(); // Appel Obligatoire
			AiBomb temp = itBombes2.next();
			zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.BOMBE;
		}

		// Mettons les feus
		Iterator<AiFire> itFeus = feus.iterator();
		while (itFeus.hasNext()) {
			source.checkInterruption(); // Appel Obligatoire
			AiFire temp = itFeus.next();
			zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.FEU;
		}

		// Mettons les bonus
		Iterator<AiItem> itObjets = objets.iterator();
		while (itObjets.hasNext()) {
			source.checkInterruption(); // Appel Obligatoire
			AiItem temp = itObjets.next();
			if (temp.getType() == AiItemType.EXTRA_BOMB)
				zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.BONUSBOMBE;
			if (temp.getType() == AiItemType.EXTRA_FLAME)
				zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.BONUSFEU;
		}
	}

	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public ZoneEnum[][] getZoneArray() throws StopRequestException {
		source.checkInterruption(); // Appel Obligatoire
		return zoneArray;
	}

	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < yMax; i++) {
			for (int j = 0; j < xMax; j++) {
				result = result + zone.getTile(i, j) + this.zoneArray[j][i]
						+ "    ";
			}
			result = result + "\n";
		}
		return result;
		/*
		 * String result = ""; result += "DENEME BIR KI UC"; for(int i = 0; i <
		 * this.zone.getHeigh() ; i++) { for(int j = 0; j <
		 * this.zone.getWidth(); j++) { if(zoneArray[j][i] == ZoneEnum.BLOCDEST)
		 * result += zone.getTile(i, j).toString()+ "BLOCDEST   "; else
		 * if(zoneArray[j][i] == ZoneEnum.BLOCINDEST) result += zone.getTile(i,
		 * j).toString() + "BLOCINDS   "; else result += zone.getTile(i,
		 * j).toString() + "LIBRE      "; } result += "\n"; } return result;
		 */
	}

	/**
	 * 
	 * @param bomb
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public ZoneEnum[][] simulateBomb(AiTile bomb) throws StopRequestException {
		source.checkInterruption(); // Appel Obligatoire
		ZoneEnum[][] result = new ZoneEnum[zone.getWidth()][zone.getHeight()];
		//Reproduisons le tableau:
		for(int k = 0; k <this.zone.getHeight(); k++)
		{
			for(int l = 0; l<this.zone.getWidth();l++)
			{
				result[l][k] = this.zoneArray[l][k];
			}
		}
		
		this.lastSimulatedBombExplodes = 0;
		// Les tiles dangeureux
		int k = 1;
		if(!bomb.getBombs().isEmpty())
		{
			this.lastSimulatedBombExplodes = -1;
			return result;
		}
		// Est-ce qu'on continue sur ces directions?
		boolean up = true;
		boolean down = true;
		boolean left = true;
		boolean right = true;
		result[bomb.getCol()][bomb.getLine()] = ZoneEnum.EXPLOSION_SIMULE;
		while (k <= this.zone.getOwnHero().getBombRange() && (up || down || left || right)) {
			if (up) {
				if ((result[bomb.getCol()][bomb.getLine() - k] != ZoneEnum.BLOCDEST)
						&& (result[bomb.getCol()][bomb.getLine() - k] != ZoneEnum.BLOCINDEST)) {
					result[bomb.getCol()][bomb.getLine() - k] = ZoneEnum.EXPLOSION_SIMULE;
				} else {
					up = false;
					if (result[bomb.getCol()][bomb.getLine() - k] == ZoneEnum.BLOCDEST) {
						result[bomb.getCol()][bomb.getLine() - k] = ZoneEnum.BLOC_EXPLOSE_SIMULE;
						this.lastSimulatedBombExplodes++;
					}
				}
			}
			if (down) {
				if ((result[bomb.getCol()][bomb.getLine() + k] != ZoneEnum.BLOCDEST)
						&& (result[bomb.getCol()][bomb.getLine() + k] != ZoneEnum.BLOCINDEST)) {
					result[bomb.getCol()][bomb.getLine() + k] = ZoneEnum.EXPLOSION_SIMULE;
				} else {
					down = false;
					if (result[bomb.getCol()][bomb.getLine() + k] == ZoneEnum.BLOCDEST) {
						result[bomb.getCol()][bomb.getLine() + k] = ZoneEnum.BLOC_EXPLOSE_SIMULE;
						this.lastSimulatedBombExplodes++;
					}
				}
			}
			if (left) {
				if ((result[bomb.getCol() - k][bomb.getLine()] != ZoneEnum.BLOCDEST)
						&& (result[bomb.getCol() - k][bomb.getLine()] != ZoneEnum.BLOCINDEST)) {
					result[bomb.getCol() - k][bomb.getLine()] = ZoneEnum.EXPLOSION_SIMULE;
				} else {
					left = false;
					if (result[bomb.getCol() - k][bomb.getLine()] == ZoneEnum.BLOCDEST) {
						result[bomb.getCol() - k][bomb.getLine()] = ZoneEnum.BLOC_EXPLOSE_SIMULE;
						this.lastSimulatedBombExplodes++;
					}
				}
			}
			if (right) {
				if ((result[bomb.getCol() + k][bomb.getLine()] != ZoneEnum.BLOCDEST)
						&& (result[bomb.getCol() + k][bomb.getLine()] != ZoneEnum.BLOCINDEST)) {
					result[bomb.getCol() + k][bomb.getLine()] = ZoneEnum.EXPLOSION_SIMULE;
				} else {
					right = false;
					if (result[bomb.getCol() + k][bomb.getLine()] == ZoneEnum.BLOCDEST) {
						result[bomb.getCol() + k][bomb.getLine()] = ZoneEnum.BLOC_EXPLOSE_SIMULE;
						this.lastSimulatedBombExplodes++;
					}
				}
			}
			k++;
		}
		return result;
	}

	/**
	 * 
	 * @return
	 * 		?
	 */
	public int getLastSimulatedBombExplodes() {
		return lastSimulatedBombExplodes;
	}

}
