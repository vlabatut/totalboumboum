package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v2;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * @author Can Göçmenoğlu
 * @author Irfan Hekimoğlu
 */
@SuppressWarnings("unused")
public class MatriceHero extends MatriceCalc {

	public MatriceHero(GocmenogluHekimoglu monIa) throws StopRequestException {
		super(monIa);
		// 
	}
	
	private int maxmin(int val,int min,int max){
		return Math.min(max,Math.max(val,min));
	}
	
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

	@Override
	public void calculate() throws StopRequestException {
		monIa.checkInterruption();
		
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
		
		
			/*
			int direction[];
			
			Direction hdir = hero.getState().getDirection();
			direction = hdir.getIntFromDirection();
			
			AiTile tile = hero.getTile();
			
			double maxdist = monIa.getPercepts().getWidth() +  monIa.getPercepts().getHeigh();
			
			
			for(int l=0;Math.abs(l)<=mybombrange+4;l += 1){
				for(int c=0;Math.abs(c)<=mybombrange+4;c += 1){
					monIa.checkInterruption();
					
					for(int i = -1;i<=1;i++){
						for(int j = -1;j<=1;j++){
							double dist = monIa.getPercepts().getTileDistance(monIa.getPercepts().getOwnHero().getTile(), monIa.getPercepts().getTile(maxmin(tile.getLine()+i+l*direction[1],0,14), maxmin(tile.getCol()+j+c* direction[0],0,14)));
							dist = 1-dist/maxdist;
							this.matrix[maxmin(tile.getLine()+i+l*direction[1],0,14)][maxmin(tile.getCol()+j+c* direction[0],0,14)] = dist;
						}
					}
					
					
				}
			}*/
			
		
		
	}
}
