package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v3;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 *	Cette classe définit les fonctions nécessaires pour les mouvements du joueur.
 * @author Can Göçmenoğlu
 * @author Irfan Hekimoğlu
 */
public class ActionManager {
	
	/**
	 * Cette fonction trouve la prochaine action que nous devons faire pour suivre le chemin donné.
	 * @return AiAction
	 */
	static AiAction move(GocmenogluHekimoglu monIa,AiPath fpath) throws StopRequestException{
		monIa.checkInterruption();
		
		if(fpath==null || fpath.getLength() < 2){
			return new AiAction(AiActionName.NONE);
		}
		
		//where are we on the path?
		AiTile mytile = monIa.getPercepts().getOwnHero().getTile();
		int mytileindex = 0;
		for(AiTile t:fpath.getTiles()){
			if(t.equals(mytile))
				break;
			mytileindex++;
		}
		
		if(mytileindex>=fpath.getLength()-1)
			return new AiAction(AiActionName.NONE);
		
		AiTile t1 = fpath.getTile(mytileindex);
		AiTile t2 = fpath.getTile(mytileindex+1);
		
		int t1x = t1.getCol();
		int t1y = t1.getLine();
		int t2x = t2.getCol();
		int t2y = t2.getLine();
		Direction d;
		
		if(t2x > t1x){
			d = Direction.RIGHT;
		}else if(t2x < t1x){
			d = Direction.LEFT;
		}else if(t1y > t2y){
			d = Direction.UP;
		}else if(t1y < t2y){
			d = Direction.DOWN;
		}else d = Direction.NONE;
		
		
		return new AiAction(AiActionName.MOVE,d);
		
		
	}
}
