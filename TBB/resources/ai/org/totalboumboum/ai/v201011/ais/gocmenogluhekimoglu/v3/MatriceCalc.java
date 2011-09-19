package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v3;


import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;

/**
 * Classe pour les calculs matriciels
 * @author Can G��meno�lu
 *
 */
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
	
	/**
	 * renvoie une matrice nulle
	 * @return
	 * @throws StopRequestException
	 */
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
	
	/**
	 * fonction qui calcule la matrice en utilisant la zone de jeu
	 */
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
	
	/**
	 * vérifie si nous pouvons atteindre nos ennemis sans détruire les murs
	 */
	static boolean canReachHeros(GocmenogluHekimoglu monIa)throws StopRequestException{
		BasicHeuristicCalculator heurcalc = new BasicHeuristicCalculator();
		BasicCostCalculator costcalc = new BasicCostCalculator();
		 
		Astar astar = new Astar(monIa, 
				monIa.getPercepts().getOwnHero(),
				costcalc,
				heurcalc);
		
		List<AiTile> herotiles = new ArrayList<AiTile>();
		
		for(AiHero h:monIa.getPercepts().getHeroes()){
			if(h.equals(monIa.getPercepts().getOwnHero()))
				continue;
			
			herotiles.add(h.getTile());
		}
		
		try {
			if(!astar.processShortestPath(monIa.getPercepts().getOwnHero().getTile(), herotiles).isEmpty()){
				return true;
			}
		} catch (LimitReachedException e) {
			// 
			return false;
		}
		
		
		return false;
		
	}
	
	/**
	 * renvoie une matrice qui a 1 pour les cas passable
	 */
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
				
				double val = (bomb.getNormalDuration()-bomb.getTime())/1000;
				double singletilewalkduration = tile.getSize() / monIa.getPercepts().getOwnHero().getWalkingSpeed();
				double timetowalkrange = singletilewalkduration*bomb.getRange();
				/*
				if(val>0.9){
					mtx[tile.getLine()][tile.getCol()] = 0;
				}else {
					mtx[tile.getLine()][tile.getCol()] = 1;
				}
				*/
				
				if(val<timetowalkrange || mtx[tile.getLine()][tile.getCol()] == 0){
					mtx[tile.getLine()][tile.getCol()] = 0;

				}else{
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
	
	/**
	 * même que ReachMatrix mais cette fois toutes les bombes sont compt�s comme explosé.
	 * @param monIa
	 * @return
	 * @throws StopRequestException
	 */
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
		
		List<AiHero> heros = monIa.getPercepts().getHeroes();
		for ( AiHero hero:heros){
			if(hero.equals(monIa.getPercepts().getOwnHero()))
				continue;
			AiTile tile = hero.getTile();
			mtx[tile.getLine()][tile.getCol()] = 0;
			mtx[Math.max(tile.getLine()+1,h-1)][tile.getCol()] = 0;
			mtx[Math.min(tile.getLine()-1,0)][tile.getCol()] = 0;
			mtx[tile.getLine()][Math.max(tile.getCol()+1,w-1)] = 0;
			mtx[tile.getLine()][Math.min(tile.getCol()-1,0)] = 0;
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
	
	/**
	 * vérifier si nous pouvons fuir si on met une bombe.
	 * @param monIa
	 * @param reach
	 * @param bline
	 * @param bcol
	 * @param lim
	 * @return
	 * @throws StopRequestException
	 */
	static public boolean canFreeWalkBomb(GocmenogluHekimoglu monIa,int[][] reach,int bline,int bcol,int lim)throws StopRequestException{
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
					monIa.getOutput().setTileColor(i, j,new Color(0 ,255,0));
				}
			}
		}
		
		if(pcnt >= lim){
			return true;
		}
		
		return false;
		
		
	}
	
	/**
	 * trouver tous les cas, nous pouvons aller
	 * @param monIa
	 * @param map
	 * @param l
	 * @param c
	 * @param h
	 * @param w
	 * @return
	 * @throws StopRequestException
	 */
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
	
	
	/**
	 * ajouter la matrice donnée, multiplié par une constante, à cette matrice
	 * @param mtx
	 * @param weight
	 * @throws StopRequestException
	 */
	public void addWithWeight(MatriceCalc mtx,double weight) throws StopRequestException {
		monIa.checkInterruption();
		
		for(int i=0;i<zoneh;i++){
			for(int j=0;j<zonew;j++){
				matrix[i][j] += mtx.getMatrix()[i][j]*weight;
			}
		}
	}
	
	/**
	 * Imprimer la matrice sous forme de texte.
	 * @throws StopRequestException
	 */
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
