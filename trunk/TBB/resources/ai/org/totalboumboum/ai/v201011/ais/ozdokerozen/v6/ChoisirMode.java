package org.totalboumboum.ai.v201011.ais.ozdokerozen.v6;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiItemType;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

/**
 * Class pour seulement choisir mode
 * Et faire les calculs pour cette selection
 * 
 * @author Gizem Lara Özdöker
 * @author Sercan Özen
 */
@SuppressWarnings("deprecation")
public class ChoisirMode{
	ArtificialIntelligence AI;
	AiZone gameZone;
	private int BESOINMIN;
	private int BESOINMAX;
	
	public ChoisirMode(OzdokerOzen ai) throws StopRequestException {
		ai.checkInterruption();
		AI=ai;
		gameZone=ai.getPercepts();
	}
		
	
	/**
	 * Methode donne nous combien de bonus dans le total
	 * @return total de bonus
	 * @throws StopRequestException 
	 */
	int combienDeBonus() throws StopRequestException{
		AI.checkInterruption();
		int result;
		try{
			List<AiItem> bonus=new ArrayList<AiItem>();
			bonus=gameZone.getItems();
			int hidden=gameZone.getHiddenItemsCount(AiItemType.EXTRA_BOMB);

			result=hidden+bonus.size();
		}catch(NullPointerException e){
			result=gameZone.getHiddenItemsCount();
		}		
		return result;
	}
	
	/**
	 * Methode calcule les valeurs BESOINMIN et BESOINMAX
	 * Ces valeur precise notre point de vue dans la matrice 
	 * @throws StopRequestException 
	 */
	void combienSuffisant() throws StopRequestException{
		AI.checkInterruption();
		int bonus=combienDeBonus();
		int hero=gameZone.getHeroes().size();
		this.BESOINMIN=(1+(bonus/hero));
		this.BESOINMAX=3;
	}
	
	/**
	 * Methode precise le decision pour le mode
	 * @return la decision de selection
	 * @throws StopRequestException 
	 */
	boolean modeChoisir() throws StopRequestException{
		AI.checkInterruption();
		int nosBomb=gameZone.getOwnHero().getBombNumberMax();
		combienSuffisant();
		if(nosBomb>=BESOINMIN || nosBomb>=BESOINMAX ||gameZone.getOwnHero().getBombRange()>=6){
			//attaque mode
			return true;}
		else{
			//collecte mode
			return false;
		}
	}
	
}
