package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v3;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;

/**
 * Cette classe peut �tre utilisée pour calculer les effets des bombes sur la matrice nous calculons
 * @author Can G��meno�lu
 *
 */
public class MatriceBlast extends MatriceCalc {

	public MatriceBlast(GocmenogluHekimoglu monIa) throws StopRequestException {
		super(monIa);
	}

	/**
	 * Cette fonction donne 1 à la flamme et des bombes qui sont sur le point d'exploser, et il donne des valeurs inf�rieures à 1 à des cas qui sont en danger d'explosion. 0 est s�re.
	 */
	@Override
	public void calculate() throws StopRequestException {
		monIa.checkInterruption();
		// 
		List<AiBomb> bombs = monIa.getPercepts().getBombs();
		for(Iterator<AiBomb> b = bombs.iterator();b.hasNext();){
			monIa.checkInterruption();
			
			AiBomb bomb = (AiBomb)b.next();

			List<AiTile> tiles = bomb.getBlast();
				for(Iterator<AiTile> t = tiles.iterator();t.hasNext();){
					monIa.checkInterruption();
					
					AiTile tile = (AiTile)t.next();
					
					double val = (bomb.getNormalDuration()-bomb.getTime()-1000)/1000;
					double singletilewalkduration = tile.getSize() / monIa.getPercepts().getOwnHero().getWalkingSpeed();
	
					
					val = Math.max(1-(val/(singletilewalkduration*(bomb.getRange()+1))),matrix[tile.getLine()][tile.getCol()]);
					
					List<AiTile> ntiles = tile.getNeighbors();
					
					for(AiTile tn:ntiles){
						this.matrix[tn.getLine()][tn.getCol()] = 0.5;
					}
					
					
					this.matrix[tile.getLine()][tile.getCol()] = 1;
				}
			
			
		}
		
		List<AiFire> flames = monIa.getPercepts().getFires();
		for(AiFire fire:flames){
			this.matrix[fire.getLine()][fire.getCol()] = 1;
		}
	}

}
