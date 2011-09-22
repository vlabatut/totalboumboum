package org.totalboumboum.ai.v201011.ais.arikyaman.v1;

import java.util.Collection;
import java.util.Iterator;
import java.awt.Color;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

/**
 * @author Furkan Arık
 * @author Çağdaş Yaman
 */
public class ArikYaman extends ArtificialIntelligence
{ 	private final int BLANK = 0;
	private final int INDES = 1;
	private final int DES = 2;
	private final int BOMB=3;
	private final int BNSRANGE=4;
	private final int BNSBOMB=5;
	private final int HERO=6;
	private final int OWNHERO=7;
	private final int SPLASH=8;
	private AiZone gamemap;
	private AiOutput out;
	private int[][] matrixofcollect;	
	boolean onetime=true;
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		AiAction result = new AiAction(AiActionName.NONE);
		gamemap=getPercepts();
		out=getOutput();
		if(onetime){
		matrixofcollect=new int[gamemap.getHeight()][gamemap.getWidth()];
		this.initialiseMap(matrixofcollect,gamemap);
		this.fillwithInDes(matrixofcollect,gamemap);
		onetime=false;
		}
		else{this.refreshMatrix(matrixofcollect);}
		this.fillwithBombs(matrixofcollect, gamemap);
		this.fillwithBns(matrixofcollect, gamemap);
		this.fillwithDes(matrixofcollect, gamemap);
		this.takeHeroes(matrixofcollect, gamemap);
		this.takeFireHoles(matrixofcollect, gamemap);
		printMatrixtoConsole(matrixofcollect);
		printOnScreen(matrixofcollect,out);
		return result;
	}
	
	private void initialiseMap(int[][] matrixofcollect, AiZone gamemap) {
		try{
		checkInterruption();
		for(int i=0; i<gamemap.getHeight(); i++){
			checkInterruption();
			for(int j=0; j<gamemap.getWidth(); j++){
				checkInterruption();
				matrixofcollect[i][j] = BLANK;
			}
		}
		}
		catch(StopRequestException e){}
	}
	
	private void fillwithInDes(int[][] matrixofcollect, AiZone gamemap){
		try{
			checkInterruption();
			Collection<AiBlock> allblocks = gamemap.getBlocks();
			Iterator<AiBlock> itallblocks = allblocks.iterator();
			while(itallblocks.hasNext()){
				checkInterruption();
				AiBlock block = itallblocks.next();
				if(!(block.isDestructible())){
				matrixofcollect[block.getLine()][block.getCol()] = INDES;
				}
			}
			}
			catch(StopRequestException e){}
	}
	
	private void fillwithDes(int[][] matrixofcollect, AiZone gamemap){
		try{
			checkInterruption();
			Collection<AiBlock> allblocks = gamemap.getBlocks();
			Iterator<AiBlock> itallblocks = allblocks.iterator();
			while(itallblocks.hasNext()){
				checkInterruption();
				AiBlock block = itallblocks.next();
				if(block.isDestructible()){
				matrixofcollect[block.getLine()][block.getCol()] = DES;
				}
			}
			}
			catch(StopRequestException e){}
	}
	
	private void fillwithBombs(int[][] matrixofcollect, AiZone gamemap){
		try{
			checkInterruption();
			Collection<AiBomb> bmbs = gamemap.getBombs();
			Iterator<AiBomb> itbmb = bmbs.iterator();
			while(itbmb.hasNext()){
				checkInterruption();
				AiBomb bmb = itbmb.next();
				matrixofcollect[bmb.getLine()][bmb.getCol()] = BOMB ;
			}
			}
			catch(StopRequestException e){}
	}
	
	private void fillwithBns(int[][] matrixofcollect, AiZone gamemap){
		try{
			checkInterruption();
			Collection<AiItem> allbns = gamemap.getItems();
			Iterator<AiItem> itbns = allbns.iterator();
			while(itbns.hasNext()){
				checkInterruption();
				AiItem bns = itbns.next();
				if(bns.getType().toString()=="EXTRA_BOMB"){
				matrixofcollect[bns.getLine()][bns.getCol()] = BNSBOMB;
				}
				else{
				matrixofcollect[bns.getLine()][bns.getCol()] = BNSRANGE;	
				}
			 }
		}
			catch(StopRequestException e){}
	}
	private void takeHeroes(int [][] matrixofcollect,AiZone gamemap){
		try{
			checkInterruption();
			Collection<AiHero> allheroes = gamemap.getHeroes();
			Iterator<AiHero> itheroes = allheroes.iterator();
			while(itheroes.hasNext()){
				checkInterruption();
				AiHero hero = itheroes.next();
				if(hero!=gamemap.getOwnHero())
					matrixofcollect[hero.getLine()][hero.getCol()] = HERO;	
				else
					matrixofcollect[hero.getLine()][hero.getCol()] = OWNHERO;
			 }
		}
			catch(StopRequestException e){}
	}
	private void takeFireHoles(int [][] matrixofcollect,AiZone gamemap){
		try{
			checkInterruption();
			Collection<AiFire> splashes = gamemap.getFires();
			Iterator<AiFire> itsplash = splashes.iterator();
			while(itsplash.hasNext()){
				checkInterruption();
				AiFire splash = itsplash.next();
				matrixofcollect[splash.getLine()][splash.getCol()] = SPLASH;	
			 }
		}
			catch(StopRequestException e){}
	}
	private void refreshMatrix(int[][] matrixofcollect){
		try{
			checkInterruption();
			for(int i=0; i<matrixofcollect.length; i++){
				checkInterruption();
				for(int j=0; j<matrixofcollect[i].length; j++){
					checkInterruption();
					if(matrixofcollect[i][j]>1)
					{matrixofcollect[i][j] = BLANK;}
				}
			}
			}
			catch(StopRequestException e){}
	}
	
	private void printMatrixtoConsole(int[][] matrixofcollect)
	{ int i=0;
	  while(i<matrixofcollect.length)
	  {	  int j=0;
		  while(j<matrixofcollect[i].length)
		  {	 
			  System.out.print("["+matrixofcollect[i][j]+"]");
			  j=j+1;
		  }
		  System.out.print("\n");
		  i=i+1;
	  }
	  System.out.print("-----\n");
	}
	
	private void printOnScreen(int[][] matrixofcollect,AiOutput out)
	{  try{
		checkInterruption();
		for(int i=0; i<matrixofcollect.length; i++){
		  checkInterruption();
		  for(int j=0; j<matrixofcollect[i].length; j++){
			  checkInterruption();
			  if(matrixofcollect[i][j]==1){
					checkInterruption();
				  	out.setTileColor(i, j, Color.BLACK);//I didn't set any text because of the mess
				  }
			  if(matrixofcollect[i][j]==2){
				checkInterruption();
			  	out.setTileColor(i, j, Color.WHITE);
			  	out.setTileText(i, j,"WALL");
			  }
			  if(matrixofcollect[i][j]==3){
					checkInterruption();
				  	out.setTileColor(i, j, Color.RED);
				  	out.setTileText(i, j,"BOMB");
			  }
			  if(matrixofcollect[i][j]==4){
					checkInterruption();
				  	out.setTileColor(i, j, Color.BLUE);
				  	out.setTileText(i, j,"BONUSRANGE");
			  }
			  if(matrixofcollect[i][j]==5){
					checkInterruption();
				  	out.setTileColor(i, j, Color.GREEN);
				  	out.setTileText(i, j,"BONUSBOMB");
			  }
			  if(matrixofcollect[i][j]==6){
					checkInterruption();
				  	out.setTileColor(i, j, Color.ORANGE);
				  	out.setTileText(i, j,"HERO");
			  }
			  if(matrixofcollect[i][j]==7){
					checkInterruption();
				  	out.setTileColor(i, j, Color.PINK);
				  	out.setTileText(i, j,"OWNHERO");
			  }
			  if(matrixofcollect[i][j]==8){
					checkInterruption();
				  	out.setTileColor(i, j, Color.YELLOW);
				  	out.setTileText(i, j,"SPLASH");
			  }
		  }
		}	
		}
		catch(StopRequestException e){}
	}
}
