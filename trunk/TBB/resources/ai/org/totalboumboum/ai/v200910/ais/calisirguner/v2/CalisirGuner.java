package org.totalboumboum.ai.v200910.ais.calisirguner.v2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiItemType;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * on na pas pu atteindre nos objectifs pour les matrices et on na pas pu utiliser
 * les temps des bombes mais au moins on essaie de s'enfuir des bombes 
 * et tout va bien pour nos objectifs pour les bonus
 * et on sest profité des classes de groupe bleu de l'annee precedente
 * 
 * @version 2
 * 
 * @author Emre Calisir
 * @author Burak Ozgen Guner
 *
 */
public class CalisirGuner extends ArtificialIntelligence
{	private AiZone zone;
/** la case occupée actuellement par le personnage */
private AiTile caseactuelle;
/** la case sur laquelle on veut aller */
private AiTile pasprochain = null;
private boolean premiere=true; 
AiAction result = new AiAction(AiActionName.NONE);
private AiTile resultat;


/** larea du jeu */
private Map map;
private  AiHero bomberman;
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		zone=getPercepts();
		bomberman=zone.getOwnHero();
		caseactuelle=bomberman.getTile();
		Direction dir=Direction.NONE;
		map=new Map(zone);
		
		if(!test_sur(caseactuelle))
		fuite();
		
		else if(zone.getItems().size()>0)
		  collection();
		if (pasprochain!=null){
			
			
			dir= zone.getDirection(
					caseactuelle, pasprochain);}
	//pour les directions composites
		if (dir!=Direction.NONE
				&& dir != Direction.DOWNLEFT
				&& dir != Direction.DOWNRIGHT
				&& dir != Direction.UPLEFT
				&& dir != Direction.UPRIGHT) {
		
			

			
			
			result = new AiAction(AiActionName.MOVE, dir);
		}
		else
			result = new AiAction(AiActionName.NONE);
		
		return result;
	}
	//pour les bonus
	private void collection() throws StopRequestException {
		// avant tout : test d'interruption
		checkInterruption();
		List<AiItem> destination = new ArrayList<AiItem>();

		destination=zone.getItems();
		Iterator<AiItem> iterator = destination.iterator();
		AiTile option;
		//comm eon est ecrit celui ce qu'on a moins de 2 est comme but
		AiItemType type=null;
		if (bomberman.getBombNumber()<2)
			type=AiItemType.EXTRA_BOMB;
		else if (bomberman.getBombRange()<2)
			type=AiItemType.EXTRA_FLAME;
		
		
		
		if (!destination.isEmpty()) {
			while (iterator.hasNext()) {
				checkInterruption(); // APPEL OBLIGATOIRE
				
				option = iterator.next().getTile();
				
				if (premiere)
				{	resultat=option;
					premiere=false;}
				if (type!=null)
				{if (type==AiItemType.EXTRA_BOMB && option.getItems().iterator().next().getType()==AiItemType.EXTRA_BOMB)
					resultat=option;
				if (type==AiItemType.EXTRA_FLAME && option.getItems().iterator().next().getType()==AiItemType.EXTRA_FLAME)
					resultat=option;
				
				}
				//sinon a plus proche
				else if (longueur(bomberman.getCol(),bomberman.getLine(),resultat.getCol(),resultat.getLine())>
						longueur(bomberman.getCol(),bomberman.getLine(),option.getCol(),option.getLine()))
					resultat=option;
				
	}
			System.out.println(resultat);
			
			
		} chemin();
	}
	
		
	

	private void fuite()throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		Astar fuite;
		Collection<AiTile> destination = new ArrayList<AiTile>();
	
		for (int line = 0; line < zone.getHeight(); line++) {
			checkInterruption(); // APPEL OBLIGATOIRE
			for (int col = 0; col < zone.getWidth(); col++) {
				checkInterruption(); // APPEL OBLIGATOIRE
				if (bomberman.getCol() != col || bomberman.getLine() != line) {
					 fuite = new Astar(map, bomberman.getCol(), bomberman.getLine(), col, line);
					
					if (test_sur(zone.getTile(line,col)) && zone.getTile(line, col).getBlocks().size()==0) {
						//sil ya du chemin
						if (fuite.findPath())
						
							destination.add(zone.getTile(line,col));
					}
				}}}
		
		
		Iterator<AiTile> iterator = destination.iterator();
		AiTile option;
		
		if (!destination.isEmpty()) {
			while (iterator.hasNext()) {
				checkInterruption(); // APPEL OBLIGATOIRE
				//on va choisir la plus proche car on na pas pu encore realiser nos matriices de risque
				option = iterator.next();
				
				if (premiere)
				{	resultat=option;
					premiere=false;}
				else if (longueur(bomberman.getCol(),bomberman.getLine(),resultat.getCol(),resultat.getLine())>
						longueur(bomberman.getCol(),bomberman.getLine(),option.getCol(),option.getLine()))
					resultat=option;
				
	}
			//System.out.println(resultat);
			
			
		}
		chemin();
	}
	
	void chemin() throws StopRequestException {
		// avant tout : test d'interruption
		checkInterruption();
		Astar dest = new Astar(map, bomberman.getCol(), bomberman.getLine(), resultat.getCol(),resultat.getLine());
		premiere=true;
		AiTile prochaine = null;
		if ( dest.findPath()) {
			

			Deque<Integer> deque = dest.getPath();
			
			if(!deque.isEmpty()) {

				// avant tout : test d'interruption
				checkInterruption();
				
				int temp =deque.poll();
				
				prochaine = zone.getTile(deque.poll(), temp);}
				if (prochaine == caseactuelle) {
				
					if (!deque.isEmpty()) {
						int temp = deque.poll();
						

						prochaine = zone.getTile(deque.poll(), temp);
		
				
			}}
	}  pasprochain=prochaine;
	//System.out.println(pasprochain);
	}	
		
		//distance euclidien
	int longueur(int x,int y,int x1,int y2){
		
		return (int) Math.sqrt((y2-y)*(y2-y)+(x1-x)*(x1-x));
		
	}
//si on est en securite
	private boolean test_sur(AiTile tile)throws StopRequestException{
		checkInterruption(); // APPEL OBLIGATOIRE
		if (tile.getBombs().size()>0 || tile.getFires().size()>0 || tile.getBlocks().size()>0 || map.returnMatrix()[tile.getCol()][tile.getLine()]==Etat.FLAMMES)
			return false;
		
		
		else 
		return true;
		
		
		
		
		
	}
}
