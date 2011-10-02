package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v3;


import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * @author Can Göçmenoğlu
 * @author Irfan Hekimoğlu
 */
@SuppressWarnings("deprecation")
public class MatriceHero extends MatriceCalc {

	public MatriceHero(GocmenogluHekimoglu monIa) throws StopRequestException {
		super(monIa);
		// 
	}
	
	@SuppressWarnings("unused")
	private int maxmin(int val,int min,int max){
		return Math.min(max,Math.max(val,min));
	}
	
	/**
	 * avons-nous des ennemis dans la même ligne ou une colonne dans notre gamme de bombe?
	 */
	static public boolean heroesOnSight(GocmenogluHekimoglu monIa) throws StopRequestException{
		List<AiHero> heros = monIa.getPercepts().getHeroes();
		AiTile mytile = monIa.getPercepts().getOwnHero().getTile();
		
		for(AiHero h:heros){
			if(h.getColor().equals(PredefinedColor.RUST))
				continue;
			
			if(h.getCol() != mytile.getCol() && h.getLine()!=mytile.getLine()){
				continue;
			}else if (h.getCol() == mytile.getCol()){
				if(Math.abs(h.getLine()-mytile.getLine()) <= monIa.getPercepts().getOwnHero().getBombRange()){
					return true;
				}
			}else {
				if(Math.abs(h.getCol()-mytile.getCol()) <= monIa.getPercepts().getOwnHero().getBombRange()){
					return true;
				}
			}
		}
		
		return false;
		
	}

	/**
	 * donne 1 à des cas qui se trouvent dans un rayon de 5 cas pour nos ennemis.
	 */
	@Override
	public void calculate() throws StopRequestException {
		monIa.checkInterruption();
		// 
		
		@SuppressWarnings("unused")
		int tilesizepx = (int) monIa.getPercepts().getTile(0,0).getSize();
		
		int mybombrange = monIa.getPercepts().getOwnHero().getBombRange();
		List<AiHero> heros = monIa.getPercepts().getHeroes();
		
		for(int l=0;l<this.zoneh;l++){
			for(int c=0;c<this.zonew;c++){ 
				for(AiHero hero:heros){
					monIa.checkInterruption();
					
					if(hero.getColor().equals(PredefinedColor.RUST))
						continue;
					
					mybombrange = Math.min(mybombrange, 5);
					int dif = (mybombrange - monIa.getPercepts().getTileDistance(l, c, hero.getLine(), hero.getCol()));
					if( dif <= mybombrange && dif >= 0){
						this.matrix[l][c] = 1;
					}
				}
			}
		}
		
	}
}
