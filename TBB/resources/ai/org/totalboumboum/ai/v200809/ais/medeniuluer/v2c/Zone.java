package org.totalboumboum.ai.v200809.ais.medeniuluer.v2c;

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

/**
*
* @author Ekin Medeni
* @author Pinar Uluer
*
*/
public class Zone {
	// Une personnalisation generale du AiZone
	private AiZone zone;
	private Collection<AiHero> rivals;
	private AiHero hero;
	private Collection<AiBomb> bombs;
	private Collection<AiBlock> blocs;
	private Collection<AiItem> objects;
	private Collection<AiFire> fires;
	private int xMax;
	private int yMax;
	private ZoneEnum[][] zoneArray;
	private MedeniUluer mu;
	private int lastSimulatedBombExplodes;

	public Zone(AiZone zone, MedeniUluer mu) throws StopRequestException {
		mu.checkInterruption(); // Appel Obligatoire
		this.mu = mu;
		this.zone = zone;
		this.hero = zone.getOwnHero();
		rivals = zone.getHeroes();
		this.bombs = zone.getBombs();
		this.blocs = zone.getBlocks();
		this.objects = zone.getItems();
		this.fires = zone.getFires();
		this.xMax = zone.getWidth();
		this.yMax = zone.getHeight();
		init();
	}

	private void init() throws StopRequestException {
		mu.checkInterruption(); // Appel Obligatoire
		zoneArray = new ZoneEnum[xMax][yMax];
		int i, j;
		// Initialisation
		for (i = 0; i < xMax; i++) {
			mu.checkInterruption(); // Appel Obligatoire
			for (j = 0; j < yMax; j++) {
				mu.checkInterruption(); // Appel Obligatoire
				zoneArray[i][j] = ZoneEnum.LIBRE;
			}
		}
		
		zoneArray[hero.getTile().getCol()][hero.getTile().getLine()] = ZoneEnum.CARACTERE;

		Iterator<AiHero> itRivals = rivals.iterator();
		while (itRivals.hasNext()) {
			mu.checkInterruption(); // Appel Obligatoire
			AiHero temp = itRivals.next();
			if (!temp.equals(hero))
				zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.RIVAL;
		}

		Iterator<AiBlock> itBlocs = blocs.iterator();
		while (itBlocs.hasNext()) {
			mu.checkInterruption(); // Appel Obligatoire
			AiBlock temp = itBlocs.next();
			if (temp.isDestructible())
				zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.BLOCDESTRUCTIBLE;
			else
				zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.BLOCINDESTRUCTIBLE;
		}

		// Mettons les bombes et les feus possibles
		Iterator<AiBomb> itBombes = bombs.iterator();
		while (itBombes.hasNext()) {
			mu.checkInterruption(); // Appel Obligatoire
			AiBomb temp = itBombes.next();

			// Les tiles dangeureux
			int k = 1;
			// Est-ce qu'on continue sur ces directions?
			boolean up = true;
			boolean down = true;
			boolean left = true;
			boolean right = true;
			while (k <= temp.getRange() && (up || down || left || right)) {
				mu.checkInterruption(); // Appel Obligatoire
				if (up) {
					if ((zoneArray[temp.getCol()][temp.getLine() - k] != ZoneEnum.BLOCDESTRUCTIBLE)
							&& (zoneArray[temp.getCol()][temp.getLine() - k] != ZoneEnum.BLOCINDESTRUCTIBLE)) {
						zoneArray[temp.getCol()][temp.getLine() - k] = ZoneEnum.FEUPOSSIBLE;
					} else {
						up = false;
					}
				}
				if (down) {
					if ((zoneArray[temp.getCol()][temp.getLine() + k] != ZoneEnum.BLOCDESTRUCTIBLE)
							&& (zoneArray[temp.getCol()][temp.getLine() + k] != ZoneEnum.BLOCINDESTRUCTIBLE)) {
						zoneArray[temp.getCol()][temp.getLine() + k] = ZoneEnum.FEUPOSSIBLE;
					} else {
						down = false;
					}
				}
				if (left) {
					if ((zoneArray[temp.getCol() - k][temp.getLine()] != ZoneEnum.BLOCDESTRUCTIBLE)
							&& (zoneArray[temp.getCol() - k][temp.getLine()] != ZoneEnum.BLOCINDESTRUCTIBLE)) {
						zoneArray[temp.getCol() - k][temp.getLine()] = ZoneEnum.FEUPOSSIBLE;
					} else {
						left = false;
					}
				}
				if (right) {
					if ((zoneArray[temp.getCol() + k][temp.getLine()] != ZoneEnum.BLOCDESTRUCTIBLE)
							&& (zoneArray[temp.getCol() + k][temp.getLine()] != ZoneEnum.BLOCINDESTRUCTIBLE)) {
						zoneArray[temp.getCol() + k][temp.getLine()] = ZoneEnum.FEUPOSSIBLE;
					} else {
						right = false;
					}
				}
				k++;
			}
		}

		// Mettons les bombes
		Iterator<AiBomb> itBombes2 = bombs.iterator();
		while (itBombes2.hasNext()) {
			mu.checkInterruption(); // Appel Obligatoire
			AiBomb temp = itBombes2.next();
			zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.BOMBE;
		}

		// Mettons les feus
		Iterator<AiFire> itFeus = fires.iterator();
		while (itFeus.hasNext()) {
			mu.checkInterruption(); // Appel Obligatoire
			AiFire temp = itFeus.next();
			zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.FEU;
		}

		// Mettons les bonus
		Iterator<AiItem> itObjets = objects.iterator();
		while (itObjets.hasNext()) {
			mu.checkInterruption(); // Appel Obligatoire
			AiItem temp = itObjets.next();
			if (temp.getType() == AiItemType.EXTRA_BOMB)
				zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.BONUSBOMBE;
			if (temp.getType() == AiItemType.EXTRA_FLAME)
				zoneArray[temp.getCol()][temp.getLine()] = ZoneEnum.BONUSFEU;
		}
	}

	public ZoneEnum[][] getZoneArray() throws StopRequestException {
		mu.checkInterruption(); // Appel Obligatoire
		return zoneArray;
	}

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
		
	}

	public ZoneEnum[][] simulateBomb(AiTile bomb) throws StopRequestException {
		mu.checkInterruption(); // Appel Obligatoire
		ZoneEnum[][] result = new ZoneEnum[zone.getWidth()][zone.getHeight()];
		//Reproduisons le tableau:
		for(int k = 0; k <this.zone.getHeight(); k++)
		{	mu.checkInterruption(); // Appel Obligatoire
			for(int l = 0; l<this.zone.getWidth();l++)
			{	mu.checkInterruption(); // Appel Obligatoire
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
			mu.checkInterruption(); // Appel Obligatoire
			if (up) {
				if ((result[bomb.getCol()][bomb.getLine() - k] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (result[bomb.getCol()][bomb.getLine() - k] != ZoneEnum.BLOCINDESTRUCTIBLE)) 
				{
					result[bomb.getCol()][bomb.getLine() - k] = ZoneEnum.EXPLOSION_SIMULE;
				} 
				else 
				{
					up = false;
					if (result[bomb.getCol()][bomb.getLine() - k] == ZoneEnum.BLOCDESTRUCTIBLE) 
					{
						result[bomb.getCol()][bomb.getLine() - k] = ZoneEnum.BLOC_EXPLOSE_SIMULE;
						this.lastSimulatedBombExplodes++;
					}
				}
			}
			if (down) 
			{
				if ((result[bomb.getCol()][bomb.getLine() + k] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (result[bomb.getCol()][bomb.getLine() + k] != ZoneEnum.BLOCINDESTRUCTIBLE)) 
				{
					result[bomb.getCol()][bomb.getLine() + k] = ZoneEnum.EXPLOSION_SIMULE;
				} 
				else 
				{
					down = false;
					if (result[bomb.getCol()][bomb.getLine() + k] == ZoneEnum.BLOCDESTRUCTIBLE) {
						result[bomb.getCol()][bomb.getLine() + k] = ZoneEnum.BLOC_EXPLOSE_SIMULE;
						this.lastSimulatedBombExplodes++;
					}
				}
			}
			if (left) {
				if ((result[bomb.getCol() - k][bomb.getLine()] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (result[bomb.getCol() - k][bomb.getLine()] != ZoneEnum.BLOCINDESTRUCTIBLE)) 
				{
					result[bomb.getCol() - k][bomb.getLine()] = ZoneEnum.EXPLOSION_SIMULE;
				} 
				else 
				{
					left = false;
					if (result[bomb.getCol() - k][bomb.getLine()] == ZoneEnum.BLOCDESTRUCTIBLE) {
						result[bomb.getCol() - k][bomb.getLine()] = ZoneEnum.BLOC_EXPLOSE_SIMULE;
						this.lastSimulatedBombExplodes++;
					}
				}
			}
			if (right) {
				if ((result[bomb.getCol() + k][bomb.getLine()] != ZoneEnum.BLOCDESTRUCTIBLE)
						&& (result[bomb.getCol() + k][bomb.getLine()] != ZoneEnum.BLOCINDESTRUCTIBLE))
				{
					result[bomb.getCol() + k][bomb.getLine()] = ZoneEnum.EXPLOSION_SIMULE;
				} 
				else 
				{
					right = false;
					if (result[bomb.getCol() + k][bomb.getLine()] == ZoneEnum.BLOCDESTRUCTIBLE) 
					{
						result[bomb.getCol() + k][bomb.getLine()] = ZoneEnum.BLOC_EXPLOSE_SIMULE;
						this.lastSimulatedBombExplodes++;
					}
				}
			}
			k++;
		}
		return result;
	}

	public int getLastSimulatedBombExplodes() throws StopRequestException {
		mu.checkInterruption(); // Appel Obligatoire
		return lastSimulatedBombExplodes;
	}

}
