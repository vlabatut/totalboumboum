package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v2;

import java.util.ArrayList;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMode;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. 
 * Cf. la documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class BombHandler extends AiBombHandler<Agent>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected BombHandler(Agent ai)
    {	super(ai);
    	ai.checkInterruption();
   	
		
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing()
	{	ai.checkInterruption();
		boolean result = false;
		
		AiZone zone=  ai.getZone();
		AiHero ownHero=zone.getOwnHero();
		AiTile ownHeroTile=ownHero.getTile();
		
		AiModeHandler<Agent> modeHandler = ai.getModeHandler(); 
		AiMode mode = modeHandler.getMode();
		
		Map<AiTile,Integer> zonePreference=ai.getPreferenceHandler().getPreferencesByTile();
		int value=zonePreference.get(ownHeroTile);
	
		ArrayList<AiBomb> mybombs = new ArrayList<AiBomb>();
		mybombs.addAll(zone.getBombsByColor(PredefinedColor.GREY));
		
		int bombsValue = ownHero.getBombNumberMax()-ownHero.getBombNumberCurrent();
		int bombsValuePoser=ownHero.getBombNumberCurrent();
		
		if((!mybombs.isEmpty())&&ownHeroTile.getBombs().isEmpty()&&bombsValue>0 &&bombsValuePoser<2&&(mode==AiMode.ATTACKING)){
			result=true;
		}
		else if(value==0&&bombsValuePoser<2&&(mode==AiMode.ATTACKING)){
			result=true;
		}
		else result=false;
			
		return result; 
	}
	


	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 */
	protected void updateOutput()
	{	ai.checkInterruption();
		
		
	}
}
