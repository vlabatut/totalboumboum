package org.totalboumboum.ai.v201011.ais.arikyaman.v2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.awt.Color;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * >> ce texte est à remplacer par votre propre description de votre IA
 * >> remplacez aussi le nom de l'auteur.
 * 
 * classe principale de l'IA, qui définit son comportement.
 * n'hésitez pas à décomposer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile à
 * débugger, modifier, relire, comprendre, etc.
 * 
 * @author Vincent Labatut
 *
 */
public class ArikYaman extends ArtificialIntelligence
{	public String mode;
	private AiZone GameMap;
	private AiOutput out;
	private int[][] ProcessMatrix;	
	private boolean onetime=true;
	private CostCalculator clc;
	private HeuristicCalculator hrc=new BasicHeuristicCalculator();
	private Astar astar;
	AiPath path1 = null;
	AiPath path2 = null;
	Collection<AiTile> tiles=new ArrayList<AiTile>();
	List<AiTile> AccesiblePositives=new ArrayList<AiTile>();
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		AiAction result = new AiAction(AiActionName.NONE);
		GameMap=getPercepts();
		out=getOutput();
		if(onetime){
		ProcessMatrix = new int[GameMap.getHeight()][GameMap.getWidth()];
		this.initialiseMap(ProcessMatrix, GameMap);
		this.fillwithInDes(ProcessMatrix, GameMap);
		onetime=false;
		}
		else{this.refreshMatrix(ProcessMatrix);}
		this.fillwithDes(ProcessMatrix);
		this.fillDestructionAreas(ProcessMatrix);
		this.fillwithBombs(ProcessMatrix, GameMap);
		this.fillwithBns(ProcessMatrix, GameMap);
		this.takeFireHoles(ProcessMatrix, GameMap);
		this.printMatrix(ProcessMatrix);
		clc=new MyCostCalculator(this);
		astar= new Astar(this,GameMap.getOwnHero(),clc,hrc);
		AccesiblePositives.clear();
		tiles.clear();
		path1=null;
		try{
		AiTile opt=this.FindClosestOptimum(ProcessMatrix, GameMap.getOwnHero().getTile(), 0);
		System.out.println(AccesiblePositives.size());
		if(path2==null){
		path2=this.calculeShortestPath(opt, GameMap);}
		else{
		if(path2.getTiles().size()>0){
		path2=this.calculeShortestPath(path2.getLastTile(), GameMap);}
		else{path2=this.calculeShortestPath(opt, GameMap);}
		}
		out.addPath(path2, Color.RED);
		AccesiblePositives.remove(opt);
		if(AccesiblePositives.contains(GameMap.getOwnHero().getTile()))
		{AccesiblePositives.remove(GameMap.getOwnHero().getTile());
		}
		Iterator<AiTile> it=AccesiblePositives.iterator();
		while(it.hasNext()){
			out.setTileColor(it.next(), Color.CYAN);
		}
		if(!AccesiblePositives.isEmpty()){
        path1=this.calculeShortestPath(AccesiblePositives,GameMap);
		}
        out.addPath(path1, Color.BLUE);
        if(path2.getLastTile()==GameMap.getOwnHero().getTile()){
			int s=ProcessMatrix[GameMap.getOwnHero().getLine()][GameMap.getOwnHero().getCol()];
			if(s>0 && s<50&&this.CanDropBomb(AccesiblePositives)){
			 result=new AiAction(AiActionName.DROP_BOMB);
			 path2=null;
			}
			else{
			 path2=null;
			}
		}
		else{
        out.addPath(path2, Color.BLACK);
		result=new AiAction(AiActionName.MOVE,this.DirectionChooser(path2,GameMap.getOwnHero()));
		}
		}
	    catch(Exception e){e.printStackTrace();}	
		return result;
	}

	private Direction DirectionChooser(AiPath path,AiHero hero)
	{	Direction direction= Direction.NONE;
		double down;
		double right;
		if(path.getTiles().size()>1){
		down=hero.getPosY()-path.getTile(1).getPosY();
		right=hero.getPosX()-path.getTile(1).getPosX();
		if(this.ProcessMatrix[path.getTile(1).getLine()][path.getTile(1).getCol()]<-175)
		{return Direction.NONE;}
		}
		else{
		down=hero.getPosY()-path.getTile(0).getPosY();
		right=hero.getPosX()-path.getTile(0).getPosX();
		if(this.ProcessMatrix[path.getTile(0).getLine()][path.getTile(0).getCol()]<-175)
		{return Direction.NONE;}
		}
		
		if(down>0){
		if(right>0){direction=Direction.UPLEFT;}
		if(right==0){direction=Direction.UP;}
		if(right<0){direction=Direction.UPRIGHT;}}
		if(down<0){
		if(right>0){direction=Direction.DOWNLEFT;}
		if(right==0){direction=Direction.DOWN;}
		if(right<0){direction=Direction.DOWNRIGHT;}
		}
		if(down==0){
		if(right>0){direction=Direction.LEFT;}
		if(right==0){direction=Direction.NONE;}
		if(right<0){direction=Direction.RIGHT;}
		}
		return direction;
	}
	
	private void initialiseMap(int[][] ProcessMatrix, AiZone GameMap) {
		try{
		checkInterruption();
		for(int i=0; i<GameMap.getHeight(); i++){
			checkInterruption();
			for(int j=0; j<GameMap.getWidth(); j++){
				checkInterruption();
				ProcessMatrix[i][j] = 0;
			}
		}
		}
		catch(StopRequestException e){}
	}
	private boolean CanDropBomb(List<AiTile> AccesiblePositives){
		Iterator<AiTile> it=AccesiblePositives.iterator();
		AiTile temp;
		int col;
		int line;
		int herocol=GameMap.getOwnHero().getCol();
		int heroline=GameMap.getOwnHero().getLine();
		int range=GameMap.getOwnHero().getBombRange();
		while(it.hasNext()){
			temp=it.next();
			col=temp.getCol();
			line=temp.getLine();
			if(!((line==heroline&&Math.abs(col-herocol)<range)||(col==herocol&&Math.abs(line-heroline)<range))){
				return true;
			}
		}
		return false;
	}
	private void fillwithInDes(int[][] ProcessMatrix, AiZone GameMap){
		try{
			checkInterruption();
			Collection<AiBlock> allblocks = GameMap.getBlocks();
			Iterator<AiBlock> itallblocks = allblocks.iterator();
			while(itallblocks.hasNext()){
				checkInterruption();
				AiBlock block = itallblocks.next();
				if(!(block.isDestructible())){
				ProcessMatrix[block.getLine()][block.getCol()] = -1000;
				}
			}
			}
			catch(StopRequestException e){}
	}
	
	private void refreshMatrix(int[][] ProcessMatrix){
		try{
			checkInterruption();
			for(int i=0; i<ProcessMatrix.length; i++){
				checkInterruption();
				for(int j=0; j<ProcessMatrix[i].length; j++){
					checkInterruption();
					if(ProcessMatrix[i][j]>-1000)
					{ProcessMatrix[i][j] = 0;}
				}
			}
			}
			catch(StopRequestException e){}
	}
	

	
	private void fillwithDes(int[][] ProcessMatrix){
		try{
			checkInterruption();
			Collection<AiBlock> allblocks = GameMap.getBlocks();
			Iterator<AiBlock> itallblocks = allblocks.iterator();
			while(itallblocks.hasNext()){
				checkInterruption();
				AiBlock block = itallblocks.next();
				if(block.isDestructible()){
				ProcessMatrix[block.getLine()][block.getCol()]= -900;
				}
			}
			}
			catch(StopRequestException e){}
	}
	
	private void fillDestructionAreas(int[][] ProcessMatrix){
		for(int i=0;i<ProcessMatrix.length;i++)
			for(int j=0;j<ProcessMatrix[i].length;j++){
				if(ProcessMatrix[i][j]==-900){
				if(ProcessMatrix[i+1][j]>=0){ProcessMatrix[i+1][j] +=10;}
				if(ProcessMatrix[i-1][j]>=0){ProcessMatrix[i-1][j] +=10;}
				if(ProcessMatrix[i][j+1]>=0){ProcessMatrix[i][j+1] +=10;}
				if(ProcessMatrix[i][j-1]>=0){ProcessMatrix[i][j-1] +=10;}
				}
			}
	}
	private void fillwithBombs(int[][] ProcessMatrix, AiZone GameMap){
		try{
			checkInterruption();	
			Object[] bmbs= GameMap.getBombs().toArray();
			bmbs=this.sortBombs(bmbs);
			for(int i=0;i<bmbs.length;i++){
				checkInterruption();
				AiBomb bmb = (AiBomb)bmbs[i];
			    Collection<AiTile> blasts = bmb.getBlast();
			    Iterator<AiTile> itblasts = blasts.iterator();
			    double timecoef;
			    timecoef = (bmb.getNormalDuration()-bmb.getTime())/bmb.getNormalDuration();
			    if(ProcessMatrix[bmb.getLine()][bmb.getCol()] > -200 + 100*timecoef){
			    ProcessMatrix[bmb.getLine()][bmb.getCol()]  = (int) (-200 + 100*timecoef);
			    }
			    while(itblasts.hasNext()){
			     checkInterruption();
			     AiTile blstzn=itblasts.next();
			     double splashcoef=(bmb.getCol()-blstzn.getCol())*(bmb.getCol()-blstzn.getCol())+(bmb.getLine()-blstzn.getLine())*(bmb.getLine()-blstzn.getLine());
			     if((blstzn.getLine()!= bmb.getLine() || blstzn.getCol() != bmb.getCol())&&  ProcessMatrix[blstzn.getLine()][blstzn.getCol()]>-900 ){
			     ProcessMatrix[blstzn.getLine()][blstzn.getCol()] = ProcessMatrix[bmb.getLine()][bmb.getCol()]+(int)(splashcoef*4);
			     }
			     }	
			    ProcessMatrix[bmb.getLine()][bmb.getCol()]=-200;
			}
			}
			catch(StopRequestException e){}
	}
	
	private void fillwithBns(int[][] ProcessMatrix, AiZone GameMap){
		try{
			checkInterruption();
			Collection<AiItem> allbns = GameMap.getItems();
			Iterator<AiItem> itbns = allbns.iterator();
			while(itbns.hasNext()){
				checkInterruption();
				AiItem bns = itbns.next();
				if(bns.getType().toString()=="EXTRA_BOMB"){
				ProcessMatrix[bns.getLine()][bns.getCol()] += 55;
				}
				else{
				ProcessMatrix[bns.getLine()][bns.getCol()] += 60;	
				}
			 }
		}
			catch(Exception e){}
	}

	private void takeFireHoles(int [][] ProcessMatrix,AiZone GameMap){
		try{
			checkInterruption();
			Collection<AiFire> splashes = GameMap.getFires();
			Iterator<AiFire> itsplash = splashes.iterator();
			while(itsplash.hasNext()){
				checkInterruption();
				AiFire splash = itsplash.next();
				ProcessMatrix[splash.getLine()][splash.getCol()] = -200;	
			 }
		}
			catch(StopRequestException e){}
	}
	
	
	private void printMatrix(int[][] ProcessMatrix) throws StopRequestException
	{ checkInterruption();
		int i=0;
	  while(i<ProcessMatrix.length)
	  {	 checkInterruption();
		  int j=0;
		  while(j<ProcessMatrix[i].length)
		  {	  checkInterruption();
			  System.out.print("["+ProcessMatrix[i][j]+"]");
			  out.setTileText(i, j,Integer.toString(ProcessMatrix[i][j]));
			  j=j+1;
		  }
		  System.out.print("\n");
		  i=i+1;
	  }
	  System.out.print("-----\n");
	}
	
	private Object[] sortBombs(Object[] bmbs){
		Object temp;
		int lowest=0;
		for(int i=0;i<bmbs.length;i++){
			double lower=10000000;
			for(int j=i;j<bmbs.length;j++)
			{ 
			 if((((AiBomb) bmbs[j]).getNormalDuration()-((AiBomb) bmbs[j]).getTime())<lower){
				lower =((AiBomb) bmbs[j]).getNormalDuration()-((AiBomb) bmbs[j]).getTime();
				lowest=j;
			 }
			}
			temp= bmbs[i];
			bmbs[i] = bmbs[lowest];
			bmbs[lowest] = temp;
		}
		return bmbs;
	}
	private AiTile FindClosestOptimum(int[][] ProcessMatrix,AiTile Tile,int param) throws StopRequestException{
		checkInterruption();
		AiTile[] tile=new AiTile[4];
		AiTile result=Tile;
		tiles.add(Tile);
		int line = Tile.getLine();
		int col = Tile.getCol();
		int optimum = ProcessMatrix[line][col];
		if(ProcessMatrix[line+1][col]>-200&&param!=2)
		{	if(tiles.contains(GameMap.getTile(line+1, col))){
				tile[0]=GameMap.getTile(line+1, col);
			}
			else{
		    if(ProcessMatrix[line+1][col]>0){
			if(!AccesiblePositives.contains(GameMap.getTile(line+1, col))){
			AccesiblePositives.add(GameMap.getTile(line+1, col));}}
			tiles.add(GameMap.getTile(line+1, col));
			tile[0]=this.FindClosestOptimum(ProcessMatrix, GameMap.getTile(line+1, col),1);}
			
		}
		if(ProcessMatrix[line-1][col]>-200&&param!=1)
		{	if(tiles.contains(GameMap.getTile(line-1, col))){
				tile[1]=GameMap.getTile(line-1, col);}
			else{
			if(ProcessMatrix[line-1][col]>0){
			if(!AccesiblePositives.contains(GameMap.getTile(line-1, col))){
			AccesiblePositives.add(GameMap.getTile(line-1, col));}}
			tiles.add(GameMap.getTile(line-1, col));
			tile[1]=this.FindClosestOptimum(ProcessMatrix, GameMap.getTile(line-1, col),2);}
		
		}
		if(ProcessMatrix[line][col+1]>-200&&param!=4)
		{	if(tiles.contains(GameMap.getTile(line, col+1))){
				tile[2]=GameMap.getTile(line, col+1);}
			else{
			if(ProcessMatrix[line][col+1]>0){
			if(!AccesiblePositives.contains(GameMap.getTile(line, col+1))){
			AccesiblePositives.add(GameMap.getTile(line, col+1));}}
			tiles.add(GameMap.getTile(line, col+1));
			tile[2]=this.FindClosestOptimum(ProcessMatrix, GameMap.getTile(line, col+1),3);}
	
		}
		if(ProcessMatrix[line][col-1]>-200&&param!=3)
		{	if(tiles.contains(GameMap.getTile(line, col-1))){
			tile[3]=GameMap.getTile(line, col-1);}
			else{
			if(ProcessMatrix[line][col-1]>0){
			if(!AccesiblePositives.contains(GameMap.getTile(line, col-1))){
			AccesiblePositives.add(GameMap.getTile(line, col-1));}}
			tiles.add(GameMap.getTile(line, col-1));
			tile[3]=this.FindClosestOptimum(ProcessMatrix, GameMap.getTile(line, col-1),4);}
		}
		Iterator<AiTile> it=AccesiblePositives.iterator();
		while(it.hasNext()){
			AiTile opt=it.next();
			if(ProcessMatrix[opt.getLine()][opt.getCol()]>optimum){
				optimum=ProcessMatrix[opt.getLine()][opt.getCol()];
				result=opt;
			}
		}
		return result;
	}
	public int[][] getProcessMatrix(){
		return this.ProcessMatrix;
	}
	private AiPath calculeShortestPath(AiTile Tile,AiZone GameMap) throws StopRequestException, LimitReachedException
	{	checkInterruption();
		return astar.processShortestPath(GameMap.getOwnHero().getTile(),Tile);
	}
	private AiPath calculeShortestPath(List<AiTile> Tiles,AiZone GameMap) throws StopRequestException, LimitReachedException
	{	checkInterruption();
		return astar.processShortestPath(GameMap.getOwnHero().getTile(),Tiles);
	}
	//	private void takeHeroes(int [][] matrixofcollect,AiZone gamemap){
	//		try{
	//			checkInterruption();
	//			Collection<AiHero> allheroes = gamemap.getHeroes();
	//			Iterator<AiHero> itheroes = allheroes.iterator();
	//			while(itheroes.hasNext()){
	//				checkInterruption();
	//				AiHero hero = itheroes.next();
	//				if(hero!=gamemap.getOwnHero())
	//					matrixofcollect[hero.getLine()][hero.getCol()] = 1;	
	//				else
	//					matrixofcollect[hero.getLine()][hero.getCol()] = 1;
	//			 }
	//		}
	//			catch(StopRequestException e){}
	//	}
}
