package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v2;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;

/**
 * @author Can Göçmenoğlu
 * @author Irfan Hekimoğlu
 */
@SuppressWarnings("deprecation")
public class GocmenogluHekimoglu extends ArtificialIntelligence
{	
	AiPath oldpath;
	AiTile oldtarget;
	AiTile lasttile;
	long lastcall;
	public long lastbomb;
	double[][] oldmtx;
	public GocmenogluHekimoglu(){
		lastcall = 0;
		oldpath = null;
		oldtarget = null;
		lastbomb = 0;
		oldmtx=null;
		lasttile=null;
	}
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		
		
		MatriceCalc collecte = new MatriceCalc(this); 
		MatriceCalc attaque = new MatriceCalc(this);
		MatriceCalc choix = null;
		
		MatriceBlast mblast = new MatriceBlast(this);
		MatriceItem mbon = new MatriceItem(this,false);
		MatriceItem mmal = new MatriceItem(this,true);
		MatriceHero mhero = new MatriceHero(this);
		MatriceDist mdist = new MatriceDist(this);
		MatriceWall mwall = new MatriceWall(this);
		
		ActionManager actnmgr = new ActionManager(this);
		
		mblast.calculate();
		mbon.calculate();
		mmal.calculate();
		mhero.calculate();
		mdist.calculate();
		mwall.calculate();
		
		@SuppressWarnings("unused")
		int mybombnumber = this.getPercepts().getOwnHero().getBombNumberMax();
		float bombratio = (float) (this.getPercepts().getHiddenItemsCount()/Math.max(0.1,this.getPercepts().getOwnHero().getBombNumberMax()));
		
		if(bombratio > 1){
			//System.out.printf("mode=collecte\r" );
			//COLLECT
			choix = collecte;
			choix.addWithWeight(mblast, -10);
			choix.addWithWeight(mbon, 5);
			choix.addWithWeight(mmal, -1);
			choix.addWithWeight(mhero, -1);
			choix.addWithWeight(mdist, 0.05);
			choix.addWithWeight(mwall, 2);
		}else{
			//System.out.printf("mode=attaque\r" );
			//ATTACK
			choix = attaque;
			choix.addWithWeight(mblast, -10);
			choix.addWithWeight(mbon, 1);
			choix.addWithWeight(mmal, -1);
			
				choix.addWithWeight(mhero, 5);
			//choix.addWithWeight(mdist, 0.05);
			choix.addWithWeight(mwall, 0);
		}
		
		 
		choix.afficheText();
		
		actnmgr.loadMatrix(choix,mblast);
		
		long time = System.currentTimeMillis()-lastcall;
		
		
		
		
		
		if(time > 10 || (oldpath == null && oldtarget == null)){
			actnmgr.findTarget();
			/*
			if(oldmtx != null ){
				double newt = choix.matrix[actnmgr.target.getLine()][actnmgr.target.getCol()];
				double oldt = choix.matrix[oldtarget.getLine()][oldtarget.getCol()];
				double oldt_o = oldmtx[oldtarget.getLine()][oldtarget.getCol()];
				
				double gain = oldt_o-oldt  ;
				
				if(gain>0.5){
					actnmgr.target = oldtarget;
					actnmgr.path = oldpath;
				}
			}*/
			/*
			if(actnmgr.target == oldtarget && actnmgr.path.getLength() <= oldpath.getLength()){
				actnmgr.path = oldpath;
			}*/ 
			
			if(lasttile  == null || actnmgr.path.getLength() > 1 && lasttile != actnmgr.path.getTile(1)){
				lasttile = actnmgr.path.getTile(1);
				oldtarget = actnmgr.target;
				oldpath = actnmgr.path;
				lastcall = System.currentTimeMillis();
			}
			
			
			
		}else{
			actnmgr.target = oldtarget;
			actnmgr.path = oldpath;
			
		}
		
		oldmtx = choix.getMatrix();
		return actnmgr.followPath();
	}
	
}
