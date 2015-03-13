package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4.criterion;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
//import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4.Agent;

/**
 * Dans cette critere, on voit des "blasttiles".
 * Avec "blasttiles", on peut prendre des cases qui est en menace par bombe qui va exploiser.
 * Quand on met un bombe, s'il y a blast notre agent cherche un securité zone qui n'est pas en menace.
 * S'il y a blast, la valeur de securitezone retourne false.
 * Sinon la valeur retourne true.
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
@SuppressWarnings("deprecation")
public class SecuriteZone extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "SecuriteZone";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public SecuriteZone(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Boolean processValue(AiTile tile){	
		ai.checkInterruption();
		
		ArrayList<AiTile> blastTiles = new ArrayList<AiTile>();
		ArrayList<AiTile> controlTiles = new ArrayList<AiTile>();
		ArrayList<AiBomb> bombs = new ArrayList<AiBomb>();
		for(AiBomb b : bombs){
			ai.checkInterruption();
			blastTiles.addAll(b.getBlast());
		}
		
		int tileColumn=tile.getCol();
		int tileRow=tile.getRow();
		
		boolean securiteZone=false;
		
		controlTiles.addAll(ai.accessibleTiles);
		controlTiles.removeAll(blastTiles);
		
		for(AiTile t: controlTiles){
			ai.checkInterruption();
			
			int tC=t.getCol();
			int tR=t.getRow();
			if(( tC==tileColumn || tR==tileRow)){
				;
			}
			else 
				securiteZone=true;
			
		}		
		return securiteZone;
		
	}
}
