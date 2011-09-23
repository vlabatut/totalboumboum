package org.totalboumboum.ai.v201011.ais.kayayerinde.v1;

import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

/**
 * @author Önder Kaya
 * @author Nezaket Yerinde
 */
@SuppressWarnings("unused")
public class KayaYerinde extends ArtificialIntelligence {
	private final int CASE_SUR = 0;
	private final int CASE_INACCESSIBLE = 1;
	private final int CASE_SCOPE = 2;
	private org.totalboumboum.ai.v201011.adapter.data.AiHero ourHero;

	@Override
	public AiAction processAction() throws StopRequestException {
		// 
		// avant tout: test d'interruption
		checkInterruption();
		//La perception instantanement de l'environnement
		org.totalboumboum.ai.v201011.adapter.data.AiZone gameZone = getPercepts();
		//Le resultat du traitement indiquant l'action suivante
		AiAction result = null;
		//Notre hero dans cette zone
		this.ourHero = gameZone.getOwnHero();
		
		return result;
		
	}
	private void initialiseMatriceCollect(int[][] matriceCollect, AiZone gameZone) throws StopRequestException{
		checkInterruption();
		int height=gameZone.getHeight();
		int width=gameZone.getWidth();
		for(int i=0; i<height; i++){
			checkInterruption();
			for(int j=0; j<width; j++){
				checkInterruption();
				matriceCollect[i][j] = CASE_SUR;
			}
		}
	}
	private void fillBombsMatriceCollect(int[][] matriceCollect, AiZone gameZone) throws StopRequestException{
		checkInterruption();
		Collection<AiBomb> bombs = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		while (iteratorBombs.hasNext()) {
			checkInterruption();
			AiBomb bomb = iteratorBombs.next();
			matriceCollect[bomb.getLine()][bomb.getCol()] = CASE_INACCESSIBLE;
			Collection<AiTile> inScopeTiles = bomb.getBlast();
			Iterator<AiTile> iteratorScope = inScopeTiles.iterator();
			while (iteratorScope.hasNext()) {
				checkInterruption();
				matriceCollect[iteratorScope.next().getLine()][iteratorScope
						.next().getCol()] = CASE_SCOPE;
			}
		}
	}
}
