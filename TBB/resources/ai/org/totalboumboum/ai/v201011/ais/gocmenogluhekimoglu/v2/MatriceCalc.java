package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v2;


import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;

/**
 * @author Can Göçmenoğlu
 * @author Irfan Hekimoğlu
 */
@SuppressWarnings({ "unused", "deprecation" })
public class MatriceCalc {
	protected GocmenogluHekimoglu monIa;
	protected int zoneh,zonew;
	protected double matrix[][];
	
	
	public MatriceCalc(GocmenogluHekimoglu monIa) throws StopRequestException
	{	// avant tout : test d'interruption
		monIa.checkInterruption();
		 
		this.monIa = monIa;	
		this.zoneh = monIa.getPercepts().getHeight();
		this.zonew = monIa.getPercepts().getWidth();
		this.setMatrix(this.getNulMatrix());
	}
	
	public double[][] getNulMatrix() throws StopRequestException{
		monIa.checkInterruption();
		
		double[][] mtx = new double[zoneh][zonew];
		
		for(int i=0;i<zoneh;i++){
			for(int j=0;j<zonew;j++){
				mtx[i][j] = 0;
			}
		}

		return mtx;
	}
	
	public void calculate() throws StopRequestException {
	}

	public void setMatrix(double matrix[][]) throws StopRequestException {
		monIa.checkInterruption();
		
		this.matrix = matrix;
	}

	public double[][] getMatrix() throws StopRequestException {
		monIa.checkInterruption();
		
		return matrix;
	}
	
	
	static public int[][] reachMatrix(GocmenogluHekimoglu monIa)throws StopRequestException{
		monIa.checkInterruption();
		
		int h = monIa.getPercepts().getHeight();
		int w = monIa.getPercepts().getWidth();
		
		int[][] mtx = new int[h][w];
		
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++){
				if(monIa.getPercepts().getTile(i, j).isCrossableBy(monIa.getPercepts().getOwnHero())){
					mtx[i][j] = 1;
				}else mtx[i][j] = 0;
			}
		}
		
		List<AiBomb> bombs = monIa.getPercepts().getBombs();
		for(Iterator<AiBomb> b = bombs.iterator();b.hasNext();){
			monIa.checkInterruption();
			
			AiBomb bomb = (AiBomb)b.next();

			List<AiTile> tiles = bomb.getBlast();

			if(!tiles.contains(monIa.getPercepts().getOwnHero().getTile())){
			for(Iterator<AiTile> t = tiles.iterator();t.hasNext();){
				monIa.checkInterruption();
				
				AiTile tile = (AiTile)t.next();
				
				double val = (bomb.getNormalDuration()-bomb.getTime()-1000)/1000;
				double singletilewalkduration = tile.getSize() / monIa.getPercepts().getOwnHero().getWalkingSpeed();

				
				val = 1-(val/(singletilewalkduration*(bomb.getRange()+1)));
				
				if(val>0){
					mtx[tile.getLine()][tile.getCol()] = 0;
				}else {
					mtx[tile.getLine()][tile.getCol()] = 1;
				}
				
			}
			}
			
		}
		
		List<AiFire> flames = monIa.getPercepts().getFires();
		for(AiFire fire:flames){
			mtx[fire.getLine()][fire.getCol()] = 0;
		}
		
		return mtx;
	}
	
	static public int[][] fakeReach(GocmenogluHekimoglu monIa)throws StopRequestException{
		monIa.checkInterruption();
		 
		int h = monIa.getPercepts().getHeight();
		int w = monIa.getPercepts().getWidth();
		
		int[][] mtx = new int[h][w];
		
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++){
				if(monIa.getPercepts().getTile(i, j).isCrossableBy(monIa.getPercepts().getOwnHero())){
					mtx[i][j] = 1;
				}else mtx[i][j] = 0;
			}
		}
		
		List<AiBomb> bombs = monIa.getPercepts().getBombs();
		for(Iterator<AiBomb> b = bombs.iterator();b.hasNext();){
			monIa.checkInterruption();
			
			AiBomb bomb = (AiBomb)b.next();

			List<AiTile> tiles = bomb.getBlast();

			for(Iterator<AiTile> t = tiles.iterator();t.hasNext();){
				monIa.checkInterruption();
				
				AiTile tile = (AiTile)t.next();
				
				mtx[tile.getLine()][tile.getCol()] = 0;
				
			}
			
		}
		
		List<AiFire> flames = monIa.getPercepts().getFires();
		for(AiFire fire:flames){
			mtx[fire.getLine()][fire.getCol()] = 0;
		}
		
		// NOW WE HAVE A LIST OF ALL THE CROSSABLE TILES MARKED AS 
		
		return mtx;
		
	}
	
	static public boolean canFreeWalkBomb(GocmenogluHekimoglu monIa,int[][] reach,int bline,int bcol)throws StopRequestException{
		monIa.checkInterruption();
		
		// remove a fake bomb's reach from freeWalk map, see if any reachable colums left
		int range = monIa.getPercepts().getOwnHero().getBombRange();
		int h = monIa.getPercepts().getHeight();
		int w = monIa.getPercepts().getWidth();
		
		for(int i = 0;i<range;i++){
			//left
			if(bcol-i>=0){
				reach[bline][bcol-i] = 0;
			}
			//right
			if(bcol+i<w){
				reach[bline][bcol+i] = 0;
			}
			//up
			if(bline-i>=0){
				reach[bline-i][bcol] = 0;
			}
			//down
			if(bline+i<h){
				reach[bline+i][bcol] = 0;
			}
		}
		
		int pcnt = 0;
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++){
				if(reach[i][j]==2){ 
					pcnt++;
				}
			}
		}
		
		if(pcnt >= 3){
			return true;
		}
		
		return false;
		
		
	}
	
	static public int[][] freeWalk(GocmenogluHekimoglu monIa,int[][] map,int l,int c,int h, int w) throws StopRequestException{
		monIa.checkInterruption();
		
		map[l][c] = 2;
		// left neighbor
		if(c-1 >= 0){
			if(map[l][c-1] == 1)
				map = freeWalk(monIa,map,l,c-1,h,w);
		}
		// right neighbor
		if(c+1 < w){
			if(map[l][c+1] == 1)
				map = freeWalk(monIa,map,l,c+1,h,w);
		}
		// up neighbor
		if(l-1 >= 0){
			if(map[l-1][c] == 1)
				map = freeWalk(monIa,map,l-1,c,h,w);
		}
		// down neighbor
		if(l+1 < h){
			if(map[l+1][c] == 1)
				map = freeWalk(monIa,map,l+1,c,h,w);
		}
		return map;
	}
	
	
	
	public void addWithWeight(MatriceCalc mtx,double weight) throws StopRequestException {
		monIa.checkInterruption();
		
		for(int i=0;i<zoneh;i++){
			for(int j=0;j<zonew;j++){
				matrix[i][j] += mtx.getMatrix()[i][j]*weight;
			}
		}
	}
	
	public void afficheText() throws StopRequestException {
		monIa.checkInterruption();
		
		DecimalFormat df = new DecimalFormat("0.00");
		
		for(int i=0;i<zoneh;i++){
			for(int j=0;j<zonew;j++){
				
				monIa.getOutput().setTileText(i,j, df.format(matrix[i][j]));
			}
		}
	}
	
	public void afficheColor() throws StopRequestException {
		monIa.checkInterruption();
		
		for(int i=0;i<zoneh;i++){
			for(int j=0;j<zonew;j++){
				double val = matrix[i][j];
				if(val <=0){
					monIa.getOutput().setTileColor(i, j, new Color( Math.min((int) (val*-255), 255) ,0,0));
				}else{
					monIa.getOutput().setTileColor(i, j, new Color(0, Math.min((int) (val*255), 255) ,0));
				}
			}
		}
	}
}
