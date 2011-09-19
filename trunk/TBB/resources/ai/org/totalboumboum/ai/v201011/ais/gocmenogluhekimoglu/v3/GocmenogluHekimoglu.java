package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v3;

import java.awt.Color;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;

@SuppressWarnings("unused")
public class GocmenogluHekimoglu extends ArtificialIntelligence
{	
	AiPath oldpath;
	// millisecondes écoulées depuis le dernier calcul de chemin
	long msec = 0;
	
	
	long msec_bomb = 0;
	
	public GocmenogluHekimoglu(){
		this.oldpath = null;
	}

	public AiAction processAction() throws StopRequestException
	{	
		checkInterruption();
		
		// initialiser les matrices
		MatriceCalc collecte = new MatriceCalc(this); 
		MatriceCalc attaque = new MatriceCalc(this);
		MatriceCalc choix = null;
		
		MatriceBlast mblast = new MatriceBlast(this);
		MatriceItem mbon = new MatriceItem(this,false);
		MatriceItem mmal = new MatriceItem(this,true);
		MatriceHero mhero = new MatriceHero(this);
		MatriceDist mdist = new MatriceDist(this);
		MatriceWall mwall = new MatriceWall(this);
		
		// calculer les matrices
		mblast.calculate();
		mbon.calculate();
		mmal.calculate();
		mhero.calculate();
		mdist.calculate();
		mwall.calculate();
		
		// calculer si nous avons assez de bonus
		float bombratio = Math.min(this.getPercepts().getHiddenItemsCount(),3)/(this.getPercepts().getOwnHero().getBombNumberMax()+this.getPercepts().getOwnHero().getBombRange()-2.01f);
		boolean attaquemode = false;
		
		// sélectionnez le mode
		if(bombratio > 1 || !MatriceCalc.canReachHeros(this)){
			//COLLECTE
			choix = collecte;
			
			//ajouter d'autres matrices
			choix.addWithWeight(mblast, -50);
			choix.addWithWeight(mbon, 5);
			choix.addWithWeight(mmal, -1);
			choix.addWithWeight(mhero, -1);
			choix.addWithWeight(mdist, 0.05);
			choix.addWithWeight(mwall, 2);
		}else{
			//ATTAQUE
			attaquemode = true;
			choix = attaque;
			choix.addWithWeight(mblast, -50);
			choix.addWithWeight(mbon, 5);
			choix.addWithWeight(mmal, -5);
			
			//nous devrions fuir l'ennemi après avoir mis une bombe pendant quelques secondes.
			if(System.currentTimeMillis()-msec_bomb > 2000){
				choix.addWithWeight(mhero, 10);
			}else choix.addWithWeight(mhero, -10);
			
			choix.addWithWeight(mwall, 0);
		}
		if(attaquemode){
			System.out.print("attack\n");
		}else System.out.print("collect\n");
		
		 
		choix.afficheText();
		
		// utiliser le vieux chemin si elle est plus longue, ou qu'il n'�tait pas plus de 25 millisecondes
		if(oldpath == null || System.currentTimeMillis()-msec >= 25){
			PathCalculator pcalc = new PathCalculator(this,choix);
			if(oldpath == null || !oldpath.getLastTile().equals(pcalc.getPath().getLastTile()) || oldpath.compareTo(pcalc.getPath())<0){
				oldpath = pcalc.getPath();
				msec = System.currentTimeMillis();
			}
		}
		
		//PathCalculator.showPath(this,oldpath);
		
		// BOMBE
		int[][] map = MatriceCalc.fakeReach(this);
		AiTile mtile = this.getPercepts().getOwnHero().getTile();
		boolean inbombrange = (map[mtile.getLine()][mtile.getCol()]==0)?true:false;
		
		map = MatriceCalc.freeWalk(this, map, mtile.getLine(), mtile.getCol(), choix.zoneh, choix.zonew);
		
		boolean bombsafe = false;
		if(attaquemode){
			bombsafe = MatriceCalc.canFreeWalkBomb(this, map, mtile.getLine(), mtile.getCol(),5);
		}else{
			bombsafe = MatriceCalc.canFreeWalkBomb(this, map, mtile.getLine(), mtile.getCol(),1);
		}
		
		
		if(oldpath!=null && !oldpath.isEmpty()){
			if(!attaquemode && this.getPercepts().getTileDistance(mtile, oldpath.getLastTile()) < 1){
				if(Math.random()>0.2 && bombsafe && !inbombrange){
					return new AiAction(AiActionName.DROP_BOMB);
				}
			}else if(Math.random()>0.2 && attaquemode){
				List<AiHero> heros = this.getPercepts().getHeroes();
				for(AiHero h:heros){
					if(h.equals(this.getPercepts().getOwnHero())){
						continue;
					}else if(bombsafe && this.getPercepts().getTileDistance(this.getPercepts().getOwnHero(), h)<=this.getPercepts().getOwnHero().getBombRange()/2){
						msec_bomb = System.currentTimeMillis();
						return new AiAction(AiActionName.DROP_BOMB);
					}
				}
			}
			
		}
		 
		return ActionManager.move(this, oldpath);
	}
	
}
