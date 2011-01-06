package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v1;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

public class MatriceHero extends MatriceCalc {

	public MatriceHero(GocmenogluHekimoglu monIa) throws StopRequestException {
		super(monIa);
		// TODO Auto-generated constructor stub
	}
	
	private int maxmin(int val,int min,int max){
		return Math.min(max,Math.max(val,min));
	}

	@Override
	public void calculate() throws StopRequestException {
		monIa.checkInterruption();
		// TODO Auto-generated method stub
		
		@SuppressWarnings("unused")
		int tilesizepx = (int) monIa.getPercepts().getTile(0,0).getSize();
		
		int mybombrange = monIa.getPercepts().getOwnHero().getBombRange();
		List<AiHero> heros = monIa.getPercepts().getHeroes();
		for(Iterator<AiHero> h = heros.iterator();h.hasNext();){
			monIa.checkInterruption();
			
			AiHero hero = (AiHero)h.next();
			
			if(hero.equals(monIa.getPercepts().getOwnHero()))
				break;
			
			int direction[];
			
			Direction hdir = hero.getState().getDirection();
			direction = hdir.getIntFromDirection();
			
			AiTile tile = hero.getTile();
			
			double maxdist = monIa.getPercepts().getWidth() +  monIa.getPercepts().getHeight();
			
			
			for(int l=0;Math.abs(l)<=mybombrange+1;l += 1){
				for(int c=0;Math.abs(c)<=mybombrange+1;c += 1){
					monIa.checkInterruption();
					for(int i = -1;i<=1;i++){
						for(int j = -1;j<=1;j++){
							double dist = monIa.getPercepts().getTileDistance(monIa.getPercepts().getOwnHero().getTile(), monIa.getPercepts().getTile(maxmin(tile.getLine()+i+l*direction[1],0,14), maxmin(tile.getCol()+j+c* direction[0],0,14)));
							dist = 1-dist/maxdist;
							this.matrix[maxmin(tile.getLine()+i+l*direction[1],0,14)][maxmin(tile.getCol()+j+c* direction[0],0,14)] = dist;
						}
					}
					
				}
			}
			
		}
		
	}
}
