package org.totalboumboum.ai.v201011.ais.arikyaman.v6;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Furkan Arık
 * @author Çağdaş Yaman
 */
@SuppressWarnings("deprecation")
public class ArikYaman extends ArtificialIntelligence
{	/** */
	public String mode;
	/** */
	private AiZone GameMap;
	/** */
	private AiHero AY;
	/** */
	private int[][] ProcessMatrix;	
	/** */
	private boolean onetime=true;
	/** */
	private CostCalculator clc;
	/** */
	private HeuristicCalculator hrc=new BasicHeuristicCalculator();
	/** */
	private Astar astar;
	/** */
	AiPath path1 = null;
	/** */
	AiPath path2 = null;
	/** */
	int pathrecord=1;
	/** */
	int nl;
	/** */
	int nr;
	/** */
	Collection<AiTile> tiles=new ArrayList<AiTile>();
	/** */
	List<AiTile> AccesiblePositives=new ArrayList<AiTile>();
	/** */
	List<AiTile> Safes=new ArrayList<AiTile>();
	/** */
	boolean canbomb=false;
	/** */
	boolean escape=true;
	/** */
	boolean closed=false;
	/** */
	boolean countcontrol=true;
	/** */
	int coef=1;
	
	
	public AiAction processAction() throws StopRequestException
	{	checkInterruption();
		AiAction result = new AiAction(AiActionName.NONE);
		GameMap=getPercepts();
		AY=GameMap.getOwnHero();
		List<AiHero> enemies;
		enemies=GameMap.getRemainingHeroes();
		enemies.remove(AY);
		AiTile target=AY.getTile();
		if(!enemies.isEmpty()){
		target=enemies.get(0).getTile();}
		if(onetime){
		ProcessMatrix = new int[GameMap.getHeight()][GameMap.getWidth()];
		this.initialiseMap(ProcessMatrix, GameMap);
		this.fillwithInDes(ProcessMatrix, GameMap);
		onetime=false;
		}
		else{this.refreshMatrix(ProcessMatrix);}
		int n=0;
		if((AY.getBombNumberMax()+AY.getBombRange())/GameMap.getHiddenItemsCount()<1/2){
			n=100;
		}
		else{n=1;
		}		
		this.fillwithDes(ProcessMatrix);
		this.fillDestructionAreas(ProcessMatrix,target);
		this.fillwithBombs(ProcessMatrix, GameMap);
		this.fillwithBns(ProcessMatrix, GameMap,n);
		this.takeFireHoles(ProcessMatrix, GameMap);
		this.fillwithAtackPlaces(ProcessMatrix, GameMap,coef);
		
		clc=new MyCostCalculator(this);
		astar= new Astar(this,AY,clc,hrc);
		Safes.clear();
		AccesiblePositives.clear();
		tiles.clear();
		try{
		AiTile opt=this.FindClosestOptimum(ProcessMatrix, AY.getTile(), 0);
		AccesiblePositives.remove(opt);
		if(ProcessMatrix[AY.getLine()][AY.getCol()]<0){
		if(escape&&!Safes.isEmpty()){
			if(path2!=null){
			  int count=0;
			  List<AiTile> tiles = path2.getTiles();
			  Iterator<AiTile> it = tiles.iterator();
			  while(it.hasNext()){
			   checkInterruption();
			   AiTile ntile=it.next();
			   if(ProcessMatrix[ntile.getLine()][ntile.getCol()]<0){
				   count=count+1;
			   }
			  }
			  if(count>2||ProcessMatrix[path2.getLastTile().getLine()][path2.getLastTile().getCol()]<=0||this.checkPathCorruption(path2, ProcessMatrix)){
				  path2=this.calculeShortestPath(Safes, GameMap);
			  }
 			}
			else{
			path2=this.calculeShortestPath(Safes, GameMap);}
			pathrecord=1;
			escape=false;
		}}
		else{
		if(path2==null||this.checkPathCorruption(path2, ProcessMatrix)){
		if(!AccesiblePositives.isEmpty()){
		if((opt==AY.getTile()&&canbomb)){
		path2=this.calculeShortestPath(AccesiblePositives, GameMap);canbomb=false;
		pathrecord=1;}
		else{path2=this.calculeShortestPath(opt, GameMap);
		if(path2.getDuration(AY)>3200){
			path2=this.calculeShortestPath(AccesiblePositives, GameMap);
		}
		pathrecord=1;}
		}
		else{escape=true; return new AiAction(AiActionName.NONE);}
		escape=true;
	    }
		else{
			try{
			if((ProcessMatrix[path2.getLastTile().getLine()][path2.getLastTile().getCol()]<=0&&escape!=false)||closed){
				path2=this.calculeShortestPath(opt, GameMap);
				pathrecord=1;
			}
			}
			catch(LimitReachedException e){path2=this.calculeShortestPath(opt, GameMap);
			pathrecord=1;}
		}}
		if(path2!=null){
        if(path2.getLastTile()==AY.getTile()){
			int s=ProcessMatrix[AY.getLine()][AY.getCol()];
			if(s>0 && s<72&&this.CanDropBomb(ProcessMatrix, AY, GameMap)){
			 result=new AiAction(AiActionName.DROP_BOMB);
			 coef=coef*(-1);
			 path2=null;
			 escape=true;
			}
			else{
			 path2=null;
			 escape=true;
			 canbomb=true;
			}
		}
		else{
		if(AY.getTile()==path2.getTile(pathrecord)){
			pathrecord=pathrecord+1;}
		Direction direction=this.DirectionChooser(path2,AY,pathrecord);
		if(direction==Direction.NONE){closed=true;}
		else{closed=false;}
		result=new AiAction(AiActionName.MOVE,direction);
		}}
		}
	    catch(LimitReachedException e){e.printStackTrace();}	
		return result;
	}

	/**
	 * 
	 * @param path
	 * @param hero
	 * @param pathrecord
	 * @return ?
	 * @throws StopRequestException
	 */
	private Direction DirectionChooser(AiPath path,AiHero hero,int pathrecord) throws StopRequestException
	{	checkInterruption();
	
		Direction direction= Direction.NONE;
		double down;
		double right;
		down=hero.getPosY()-path.getTile(pathrecord).getPosY();
		right=hero.getPosX()-path.getTile(pathrecord).getPosX();
		if(ProcessMatrix[path.getTile(pathrecord).getLine()][path.getTile(pathrecord).getCol()]<-165&&ProcessMatrix[hero.getLine()][hero.getCol()]>=0){
			return Direction.NONE;
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
	
	/**
	 * 
	 * @param ProcessMatrix
	 * @param GameMap
	 * @throws StopRequestException
	 */
	private void initialiseMap(int[][] ProcessMatrix, AiZone GameMap) throws StopRequestException {
		checkInterruption();
		for(int i=0; i<GameMap.getHeight(); i++){
			checkInterruption();
			for(int j=0; j<GameMap.getWidth(); j++){
				checkInterruption();
				ProcessMatrix[i][j] = 0;
			}
		}
	}
	
	/**
	 * 
	 * @param ProcessMatrix
	 * @param AY
	 * @param GameMap
	 * @return ?
	 * @throws StopRequestException
	 * @throws LimitReachedException
	 */
	private boolean CanDropBomb(int[][] ProcessMatrix, AiHero AY, AiZone GameMap) throws StopRequestException, LimitReachedException{
		checkInterruption();
		int[][] DuplicateMatrix=ProcessMatrix.clone();
		int line=AY.getLine();
		int col=AY.getCol();
		int maxline=DuplicateMatrix.length;
		int maxcol=DuplicateMatrix[0].length;
		int range=AY.getBombRange();
		double duration= AY.getBombDuration();
		double speed=AY.getWalkingSpeed();
		boolean dp=true,dm=true,lp=true,lm=true;
		DuplicateMatrix[line][col]=-100;
		for(int i=1;i<=range;i++){
			checkInterruption();
			if(DuplicateMatrix[(line+i+maxline)%maxline][col]>-100&&dp){
				DuplicateMatrix[(line+i+maxline)%maxline][col]=-100;}
			else{if(DuplicateMatrix[(line+i+maxline)%maxline][col]<=-900){dp=false;}}
			if(DuplicateMatrix[(line-i+maxline)%maxline][col]>-100&&dm){
				DuplicateMatrix[(line-i+maxline)%maxline][col]=-100;}
			else{if(DuplicateMatrix[(line-i+maxline)%maxline][col]<=-900){dm=false;}}
			if(DuplicateMatrix[line][(col+i+maxcol)%maxcol]>-100&&lp){
				DuplicateMatrix[line][(col+i+maxcol)%maxcol]=-100;}
			else{if(DuplicateMatrix[line][(col+i+maxcol)%maxcol]<=-900){lp=false;}}
			if(DuplicateMatrix[line][(col-i+maxcol)%maxcol]>-100&&lm){
				DuplicateMatrix[line][(col-i+maxcol)%maxcol]=-100;}
			else{if(DuplicateMatrix[line][(col-i+maxcol)%maxcol]<=-900){lm=false;}}
		}
		Safes.clear();
		AccesiblePositives.clear();
		tiles.clear();
		@SuppressWarnings("unused")
		AiTile opt=this.FindClosestOptimum(DuplicateMatrix, AY.getTile(),0);
		if(Safes.isEmpty()){
			return false;		
		}
		else{
			AiPath isescape=calculeShortestPath(Safes,GameMap);
			if((duration-500)/1000<(isescape.getPixelDistance()/speed)){
				return false;
			}
			else{ List<AiTile> list=isescape.getTiles();
				  Iterator<AiTile> itlist=list.iterator();
				  while(itlist.hasNext()){
			      checkInterruption();
				  AiTile tile=itlist.next();
				  if(DuplicateMatrix[tile.getLine()][tile.getCol()]<-115){
				  return false;
				  }
				  }
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param ProcessMatrix
	 * @param GameMap
	 * @throws StopRequestException
	 */
	private void fillwithInDes(int[][] ProcessMatrix, AiZone GameMap) throws StopRequestException{
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
	
	/**
	 * 
	 * @param ProcessMatrix
	 * @param GameMap
	 * @param coef
	 * @throws StopRequestException
	 */
	private void fillwithAtackPlaces(int[][] ProcessMatrix, AiZone GameMap,int coef) throws StopRequestException{
			checkInterruption();
			Collection<AiHero> allheroes = GameMap.getHeroes();
			Iterator<AiHero> itallheroes = allheroes.iterator();
			while(itallheroes.hasNext()){
				checkInterruption();
			    AiHero hero = itallheroes.next();
				if(hero!=AY){
				  boolean r=true,l=true,u=true,d=true;
				  for(int i=1;i<=AY.getBombRange();i++){
					  checkInterruption();
					  if(r&&ProcessMatrix[hero.getLine()][hero.getCol()+i]>=0){
						  ProcessMatrix[hero.getLine()][hero.getCol()+i]=32-this.distanceCoefCalc(hero.getLine(),hero.getCol()+i, hero.getTile(), ProcessMatrix,32)+coef*this.distanceCoefCalc(hero.getLine(),hero.getCol()+i, AY.getTile(), ProcessMatrix,16);
					  }
					  else{if(r&&ProcessMatrix[hero.getLine()][hero.getCol()+i]<=-900){r=false;}}
					  if(l&&ProcessMatrix[hero.getLine()][hero.getCol()-i]>=0){
						  ProcessMatrix[hero.getLine()][hero.getCol()-i]=32-this.distanceCoefCalc(hero.getLine(),hero.getCol()-i, hero.getTile(), ProcessMatrix,32)+coef*this.distanceCoefCalc(hero.getLine(),hero.getCol()-i,AY.getTile(), ProcessMatrix,16);
					  }
					  else{if(l&&ProcessMatrix[hero.getLine()][hero.getCol()-i]<=-900){l=false;}}
					  if(d&&ProcessMatrix[hero.getLine()+i][hero.getCol()]>=0){
						  ProcessMatrix[hero.getLine()+i][hero.getCol()]=32-this.distanceCoefCalc(hero.getLine()+i,hero.getCol(),hero.getTile(), ProcessMatrix,32)+coef*this.distanceCoefCalc(hero.getLine()+i,hero.getCol(),AY.getTile(), ProcessMatrix,16);
					  }
					  else{if(d&&ProcessMatrix[hero.getLine()+i][hero.getCol()]<=-900){d=false;}}
					  if(u&&ProcessMatrix[hero.getLine()-i][hero.getCol()]>=0){
						  ProcessMatrix[hero.getLine()-i][hero.getCol()]=32-this.distanceCoefCalc(hero.getLine()-i,hero.getCol(),hero.getTile(), ProcessMatrix,32)+coef*this.distanceCoefCalc(hero.getLine()-i,hero.getCol(),AY.getTile(), ProcessMatrix,16);
					  }
					  else{if(u&&ProcessMatrix[hero.getLine()-i][hero.getCol()]<=-900){u=false;}}
				  }
				}
			}
	}
	
	/**
	 * 
	 * @param ProcessMatrix
	 * @throws StopRequestException
	 */
	private void refreshMatrix(int[][] ProcessMatrix) throws StopRequestException{
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
	
	/**
	 * 
	 * @param ProcessMatrix
	 * @throws StopRequestException
	 */
	private void fillwithDes(int[][] ProcessMatrix) throws StopRequestException{
			checkInterruption();
			Collection<AiBlock> allblocks = GameMap.getDestructibleBlocks();
			Iterator<AiBlock> itallblocks = allblocks.iterator();
			while(itallblocks.hasNext()){
				checkInterruption();
				AiBlock block = itallblocks.next();
				ProcessMatrix[block.getLine()][block.getCol()]= -900;
			}
	}
	
	/**
	 * 
	 * @param ProcessMatrix
	 * @param tile
	 * @throws StopRequestException
	 */
	private void fillDestructionAreas(int[][] ProcessMatrix,AiTile tile) throws StopRequestException{
		checkInterruption();
		for(int i=0;i<ProcessMatrix.length;i++){
			checkInterruption();
			for(int j=0;j<ProcessMatrix[i].length;j++){
				checkInterruption();
				if(ProcessMatrix[i][j]==-900){
					int ip1=((i+1)%ProcessMatrix.length),im1=(i-1+ProcessMatrix.length)%ProcessMatrix.length;
					int jp1=((j+1)%ProcessMatrix[i].length),jm1=(j-1+ProcessMatrix[i].length)%ProcessMatrix[0].length;
					if(ProcessMatrix[ip1][j]==0){ProcessMatrix[ip1][j] +=20-this.distanceCoefCalc(ip1,j,tile,ProcessMatrix,20);}
					if(ProcessMatrix[im1][j]==0){ProcessMatrix[im1][j] +=20-this.distanceCoefCalc(im1,j,tile,ProcessMatrix,20);}
					if(ProcessMatrix[i][jp1]==0){ProcessMatrix[i][jp1] +=20-this.distanceCoefCalc(i,jp1,tile,ProcessMatrix,20);}
					if(ProcessMatrix[i][jm1]==0){ProcessMatrix[i][jm1] +=20-this.distanceCoefCalc(i,jm1,tile,ProcessMatrix,20);}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param ProcessMatrix
	 * @param GameMap
	 * @throws StopRequestException
	 */
	private void fillwithBombs(int[][] ProcessMatrix, AiZone GameMap) throws StopRequestException{
			checkInterruption();	
			Object[] bmbs= GameMap.getBombs().toArray();
			bmbs=this.sortBombs(bmbs);
			for(int i=0;i<bmbs.length;i++){
				checkInterruption();
				AiBomb bmb = (AiBomb)bmbs[i];
			    Collection<AiTile> blasts = bmb.getBlast();
			    Iterator<AiTile> itblasts = blasts.iterator();
			    double timecoef;
			    timecoef =(double)(bmb.getNormalDuration()-bmb.getTime())/bmb.getNormalDuration();
			    if(ProcessMatrix[bmb.getLine()][bmb.getCol()] > -200 + 100*timecoef){
			    ProcessMatrix[bmb.getLine()][bmb.getCol()]  = (int) (-200 + 100*timecoef);
			    }
			    while(itblasts.hasNext()){
			     checkInterruption();
			     AiTile blstzn=itblasts.next();
			     if((blstzn.getLine()!= bmb.getLine() || blstzn.getCol() != bmb.getCol())&&  ProcessMatrix[blstzn.getLine()][blstzn.getCol()]>-900 ){
			    	 if(ProcessMatrix[blstzn.getLine()][blstzn.getCol()]>ProcessMatrix[bmb.getLine()][bmb.getCol()]+1){
			    	 ProcessMatrix[blstzn.getLine()][blstzn.getCol()] = ProcessMatrix[bmb.getLine()][bmb.getCol()]+1;}
			     }
			     }	
			    ProcessMatrix[bmb.getLine()][bmb.getCol()]=-200;
			}
	}
	
	/**
	 * 
	 * @param ProcessMatrix
	 * @param GameMap
	 * @param n
	 * @throws StopRequestException
	 */
	private void fillwithBns(int[][] ProcessMatrix, AiZone GameMap,int n) throws StopRequestException{
			checkInterruption();
			Collection<AiItem> allbns = GameMap.getItems();
			Iterator<AiItem> itbns = allbns.iterator();
			while(itbns.hasNext()){
				checkInterruption();
				AiItem bns = itbns.next();
				ProcessMatrix[bns.getLine()][bns.getCol()] += n-this.distanceCoefCalc(bns.getTile(),this.AY.getTile(),ProcessMatrix, n);
			 }
	}
	
	/**
	 * 
	 * @param tile1
	 * @param tile2
	 * @param matrix
	 * @param n
	 * @return ?
	 * @throws StopRequestException
	 */
	private int distanceCoefCalc(AiTile tile1, AiTile tile2,int[][] matrix,int n) throws StopRequestException{
		checkInterruption();
		double a=Math.sqrt(matrix.length*matrix.length+matrix[0].length*matrix[0].length);
		double b=Math.sqrt((tile1.getLine()-tile2.getLine())*(tile1.getLine()-tile2.getLine())+(tile1.getCol()-tile2.getCol())*(tile1.getCol()-tile2.getCol()));
		int result=(int)((b/a)*n);
		return result;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param tile2
	 * @param matrix
	 * @param n
	 * @return ?
	 * @throws StopRequestException
	 */
	private int distanceCoefCalc(int x,int y, AiTile tile2,int[][] matrix,int n) throws StopRequestException{
		checkInterruption();
		double a=Math.sqrt(matrix.length*matrix.length+matrix[0].length*matrix[0].length);
		double b=Math.sqrt((x-tile2.getLine())*(x-tile2.getLine())+(y-tile2.getCol())*(y-tile2.getCol()));
		int result=(int)((b/a)*n);
		return result;
	}
	
	/**
	 * 
	 * @param ProcessMatrix
	 * @param GameMap
	 * @throws StopRequestException
	 */
	private void takeFireHoles(int [][] ProcessMatrix,AiZone GameMap) throws StopRequestException{
			checkInterruption();
			Collection<AiFire> splashes = GameMap.getFires();
			Iterator<AiFire> itsplash = splashes.iterator();
			while(itsplash.hasNext()){
				checkInterruption();
				AiFire splash = itsplash.next();
				ProcessMatrix[splash.getLine()][splash.getCol()] = -200;	
			 }
	}
	
	/**
	 * 
	 * @param bmbs
	 * @return ?
	 * @throws StopRequestException
	 */
	private Object[] sortBombs(Object[] bmbs) throws StopRequestException{
		checkInterruption();
		Object temp;
		int lowest=0;
		for(int i=0;i<bmbs.length;i++){
			checkInterruption();
			double lower=10000000;
			for(int j=i;j<bmbs.length;j++)
			{ checkInterruption();
			
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
	
	/**
	 * 
	 * @param ProcessMatrix
	 * @param Tile
	 * @param param
	 * @return ?
	 * @throws StopRequestException
	 */
	private AiTile FindClosestOptimum(int[][] ProcessMatrix,AiTile Tile,int param) throws StopRequestException{
		checkInterruption();
		AiTile[] tile=new AiTile[4];
		AiTile result=Tile;
		tiles.add(Tile);
		int line = Tile.getLine();
		int col = Tile.getCol();
		int linep1=line+1,linem1=line-1,colp1=col+1,colm1=col-1;
		int optimum = ProcessMatrix[line][col];
		if(line==ProcessMatrix.length-1){
			linep1=0;
		}
		if(line==0){
			linem1=ProcessMatrix.length-1;
		}
		if(col==ProcessMatrix[0].length-1){
			colp1=0;
		}
		if(col==0){
			colm1=ProcessMatrix[0].length-1;
		}
		if(ProcessMatrix[linep1][col]>-200&&param!=2)
		{	if(tiles.contains(GameMap.getTile(linep1, col))){
				tile[0]=GameMap.getTile(linep1, col);
			}
			else{
		    if(ProcessMatrix[linep1][col]>0){
			if(!AccesiblePositives.contains(GameMap.getTile(linep1, col))){
			AccesiblePositives.add(GameMap.getTile(linep1, col));}}
		    if(ProcessMatrix[linep1][col]>=0){
		    	Safes.add(GameMap.getTile(linep1, col));
		    }
			tiles.add(GameMap.getTile(linep1, col));
			tile[0]=this.FindClosestOptimum(ProcessMatrix, GameMap.getTile(linep1, col),1);}
			
		}
		if(ProcessMatrix[linem1][col]>-200&&param!=1)
		{	if(tiles.contains(GameMap.getTile(linem1, col))){
				tile[1]=GameMap.getTile(linem1, col);}
			else{
			if(ProcessMatrix[linem1][col]>0){
			if(!AccesiblePositives.contains(GameMap.getTile(linem1, col))){
			AccesiblePositives.add(GameMap.getTile(linem1, col));}}
			 if(ProcessMatrix[linem1][col]>=0){
			    Safes.add(GameMap.getTile(linem1, col));
			   }
			tiles.add(GameMap.getTile(linem1, col));
			tile[1]=this.FindClosestOptimum(ProcessMatrix, GameMap.getTile(linem1, col),2);}
		
		}
		if(ProcessMatrix[line][colp1]>-200&&param!=4)
		{	if(tiles.contains(GameMap.getTile(line, colp1))){
				tile[2]=GameMap.getTile(line, colp1);}
			else{
			if(ProcessMatrix[line][colp1]>0){
			if(!AccesiblePositives.contains(GameMap.getTile(line, colp1))){
			AccesiblePositives.add( GameMap.getTile(line, colp1));}}
			if(ProcessMatrix[line][colp1]>=0){
			    Safes.add(GameMap.getTile(line, colp1));
			}
			tiles.add(GameMap.getTile(line, colp1));
			tile[2]=this.FindClosestOptimum(ProcessMatrix, GameMap.getTile(line, colp1),3);}
	
		}
		if(ProcessMatrix[line][colm1]>-200&&param!=3)
		{	if(tiles.contains(GameMap.getTile(line, colm1))){
			tile[3]=GameMap.getTile(line, colm1);}
			else{
			if(ProcessMatrix[line][colm1]>0){
			if(!AccesiblePositives.contains(GameMap.getTile(line, colm1))){
			AccesiblePositives.add(GameMap.getTile(line, colm1));}}
			if(ProcessMatrix[line][colm1]>=0){
			    Safes.add(GameMap.getTile(line, colm1));
			}
			tiles.add(GameMap.getTile(line, colm1));
			tile[3]=this.FindClosestOptimum(ProcessMatrix, GameMap.getTile(line, colm1),4);}
		}

		Iterator<AiTile> it=AccesiblePositives.iterator();
		while(it.hasNext()){
			checkInterruption();
			AiTile opt=it.next();
			if(ProcessMatrix[opt.getLine()][opt.getCol()]>optimum){
				optimum=ProcessMatrix[opt.getLine()][opt.getCol()];
				result=opt;
			}
		}
		return result;
	}
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public int[][] getProcessMatrix() throws StopRequestException{
		checkInterruption();
		return this.ProcessMatrix;
	}
	
	/**
	 * 
	 * @param Tile
	 * @param GameMap
	 * @return ?
	 * @throws StopRequestException
	 * @throws LimitReachedException
	 */
	private AiPath calculeShortestPath(AiTile Tile,AiZone GameMap) throws StopRequestException, LimitReachedException
	{	checkInterruption();
	    AiPath path=astar.processShortestPath(AY.getTile(),Tile);
		path.setStart(AY.getPosX(), AY.getPosY());
		return path;
	}
	
	/**
	 * 
	 * @param Tiles
	 * @param GameMap
	 * @return ?
	 * @throws StopRequestException
	 * @throws LimitReachedException
	 */
	private AiPath calculeShortestPath(List<AiTile> Tiles,AiZone GameMap) throws StopRequestException, LimitReachedException
	{	checkInterruption();
		AiPath path=astar.processShortestPath(AY.getTile(),Tiles);
		path.setStart(AY.getPosX(), AY.getPosY());
		return path;
	}
	
	/**
	 * 
	 * @param path
	 * @param ProcessMatrix
	 * @return ?
	 * @throws StopRequestException
	 */
	private boolean checkPathCorruption(AiPath path,int[][] ProcessMatrix) throws StopRequestException{
		checkInterruption();
		List<AiTile> tiles = path.getTiles();
		Iterator<AiTile> it = tiles.iterator();
		while(it.hasNext()){
			checkInterruption();
			AiTile tile=it.next();
			if(ProcessMatrix[tile.getLine()][tile.getCol()]<=-200){
				return true;
			}
		}
		return false;
	}
}
