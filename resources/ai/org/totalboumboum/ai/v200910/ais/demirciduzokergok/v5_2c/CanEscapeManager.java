package org.totalboumboum.ai.v200910.ais.demirciduzokergok.v5_2c;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;

/**
 * 
 * @author Osman Demirci
 * @author Mustafa Göktuğ Düzok
 * @author Hatice Esra Ergök
 * 
 */
@SuppressWarnings("deprecation")
public class CanEscapeManager {

	/**
	 * 
	 * @param ai
	 *            Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public CanEscapeManager(DemirciDuzokErgok ai) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		this.ai = ai;
		zone = ai.getPercepts();
		// safe_map=new Safety_Map(zone);

		// init destinations
		arrivedB = false;
		// safe_map=new Safety_Map(zone);
		AiHero our_bomberman = zone.getOwnHero();
		possibleDestB = destinations_possibles_b(our_bomberman.getTile());
		updatePath_b();
	}

	/**
	 * 
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public int getPathLength() throws StopRequestException {
		ai.checkInterruption();
		return pathB.getLength();
	}

	/**
	 * 
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public void updatePath_b() throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE

		pathB = ai.aStar.processShortestPath(ai.getPercepts().getOwnHero()
				.getTile(), possibleDestB);
		// System.out.println(path_b.getLength());
		ai.updateAstarQueueSize();
		arrivedTileB = pathB.getLastTile();

	}

	/**
	 * 
	 * @param tile
	 *            Description manquante !
	 * @return Description manquante !
	 * @throws StopRequestException
	 *             Description manquante !
	 */
	public List<AiTile> destinations_possibles_b(AiTile tile)
			throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE
		safeMap = new CanEscape(zone, ai);
		AiTile tile_dest_b;
		List<AiTile> result_b = new ArrayList<AiTile>();

		for (int pos_y = 0; pos_y < zone.getHeight(); pos_y++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE

			for (int pos_x = 0; pos_x < zone.getWidth(); pos_x++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE

				if (safeMap.returnMatrix()[pos_y][pos_x] == safeMap.SAFE_CASE
						|| safeMap.returnMatrix()[pos_y][pos_x] == safeMap.BONUS) {
					tile_dest_b = zone.getTile(pos_y, pos_x);
					result_b.add(tile_dest_b);
				}

			}

		}

		return result_b;

	}

	/** */
	private DemirciDuzokErgok ai;
	/** */
	private AiZone zone;
	/** */
	private CanEscape safeMap;
	/** */
	@SuppressWarnings("unused")
	private AiTile arrivedTileB;
	/** */
	private List<AiTile> possibleDestB;
	/** */
	@SuppressWarnings("unused")
	private boolean arrivedB;
	/** */
	private AiPath pathB;
}
