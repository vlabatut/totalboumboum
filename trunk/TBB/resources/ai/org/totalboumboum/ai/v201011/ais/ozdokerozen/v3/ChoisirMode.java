package org.totalboumboum.ai.v201011.ais.ozdokerozen.v3;

import java.util.List;

import org.totalboumboum.ai.v201011.adapter.data.AiItem;
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
	AiZone gameZone;
	
	private int BESOINMIN;
	private int BESOINMAX;
	
	public ChoisirMode(OzdokerOzen ai) {
		gameZone=ai.getPercepts();
	}
		
	
	/**
	 * Methode donne nous combien de bonus dans le total
	 * @return total de bonus
	 */
	int combienDeBonus(){
		int result;
		try{
			List<AiItem> bonus=gameZone.getItems();
			result=gameZone.getHiddenItemsCount()+bonus.size();
		}catch(NullPointerException e){
			result=gameZone.getHiddenItemsCount();
		}		
		return result;
	}
	
	/**
	 * Methode calcule les valeurs BESOINMIN et BESOINMAX
	 * Ces valeur precise notre point de vue dans la matrice 
	 */
	void combienSuffisant(){
		int bonus=combienDeBonus();
		int hero=gameZone.getHeroes().size();
		this.BESOINMIN=(1+(bonus/hero));
		this.BESOINMAX=3;
	}
	
	/**
	 * Methode precise le decision pour le mode
	 * @return la decision de selection
	 */
	boolean modeChoisir(){
		int nosBomb=gameZone.getOwnHero().getBombNumberMax();
		//System.out.println("Bomba Say m z :"+nosBomb);
		combienSuffisant();
		//System.out.println("Combien suffisant Min:"+BESOINMIN+", Max: "+BESOINMAX);
		if(nosBomb>=BESOINMIN | nosBomb>=BESOINMAX){
			System.out.println("--------ATAK------");
			//attaque mode
			return true;}
		else{
			System.out.println("--------COLLECTE------");
			//collecte mode
			return false;
		}
	}
	
}
