package org.totalboumboum.ai.v201011.ais.ozdokerozen.v3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200809.ais.akpolatsener.v1.Neighbors;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;

/**
 * @author Gizem Lara Özdöker
 * @author Sercan Özen
 */
@SuppressWarnings({ "unused", "deprecation" })
public class OutilesCollecte {

	TileControleur tileControleur;
	List<AiItem> lesBonus;
	ArtificialIntelligence abc;
	 //pour les sortie dans API
	private AiOutput ecran;
	
	public OutilesCollecte(OzdokerOzen ai,AiZone gameZone,int[][] matrice) {
		tileControleur=new TileControleur(ai, matrice);
		abc=ai;
		}
	
	
	public List<AiTile> trouveBonusPath (int hedef,int[][] matrice,AiTile hero,AiZone gameZone,AiTile notre,AiTile previous){
		List<AiTile> tile = null;
		List<AiTile> prochepath=notre.getNeighbors();
			for(int i=0;i<prochepath.size();i++){
				if(prochepath.get(i)==previous){
					System.out.println("buraya bakm yoruz.");
				}else{
					if(matrice[prochepath.get(i).getLine()][prochepath.get(i).getCol()]==hedef){
						System.out.println(" HEDEF bulundu ve ekleniyor: "+prochepath.get(i).getLine()+","+prochepath.get(i).getCol());
						tile.add(prochepath.get(i));
						System.out.println("////////////////////");
						System.out.println("Bonusu bulduk be abicim, imdi geri d n yoruz");
						System.out.println("////////////////////");
						break;
					}else if(matrice[prochepath.get(i).getLine()][prochepath.get(i).getCol()]==0 || matrice[prochepath.get(i).getLine()][prochepath.get(i).getCol()]==5){
						System.out.println("Duvar var buralarda bu yol yalan oldu");
						tile = null;
					}else if(matrice[prochepath.get(i).getLine()][prochepath.get(i).getCol()]<0){
						System.out.println("Bu k s mlarda bomba var,ate i var yalan olur bu yolda");
						tile = null;
					}else if(matrice[prochepath.get(i).getLine()][prochepath.get(i).getCol()]==1){
						System.out.println("Bu yol a  k devam edelim");
						previous=notre;
						System.out.println(" Boù yol: "+prochepath.get(i).getLine()+","+prochepath.get(i).getCol());
						tile=add(prochepath.get(i));
						if(trouveBonusPath(10, matrice, hero, gameZone,prochepath.get(i),previous)!=null)
							tile.addAll(trouveBonusPath(10, matrice, hero, gameZone,prochepath.get(i),previous));
						
					}
				}
			}
		
		return tile;
	}
	
	private List<AiTile> add(AiTile aiTile) {
		// 
		return null;
	}


	/**
	 * Methode analyse qu'il ya de bonus ou pas dans l'aire de jeu
	 * @param gameZone
	 * @return existance de bombe
	 */
	public boolean existeBonus(AiZone gameZone){
		if(gameZone.getItems().isEmpty()) return false;
		else{ 
			lesBonus=gameZone.getItems();
			return true;
		}
	}
	
	/**
	 * Methode nous dit qu'il y a un path possible ou pas
	 * @param gameZone
	 * @param matrice
	 * @param cible
	 * @return true s'il y a une path pour acceser à bonus
	 * @throws StopRequestException
	 * @throws LimitReachedException
	 */
	public boolean existePath(AiZone gameZone,int[][] matrice,AiTile cible) throws StopRequestException, LimitReachedException{
		AiHero hero=gameZone.getOwnHero();
		AiPath path=tileControleur.calculateShortestPath(hero, hero.getTile(), cible);
		if(path.isEmpty()) return false;
		else return true;
	}

	/**
	 *Verilen hedef do rultusunda hangi duvar  k raca  m z  veren fonsiyon olacak
	 */
	public AiTile murQuOnVaDetruitre(AiTile bonus,int[][] matrice,AiZone gameZone) {
		
		boolean trouverMur=false;
		AiTile cible=null;
		
		List<AiTile> tilesPossible=new ArrayList<AiTile>();
		tileControleur.tilePossibleArrive(gameZone.getOwnHero().getTile(), tilesPossible, matrice);
		
		//on commence à scanner de ces valeur
		int i,j,plusX,plusY;
				
		//on les initialise en faire attention matrice length pour ne pas tomber à l'exception
		if(bonus.getCol()!=0){
			if(bonus.getLine()!=0){
					i=bonus.getCol()-1;
					j=bonus.getLine()-1;
			}else{
				i=bonus.getCol()-1;
				j=bonus.getLine();
			}	
		}else{
			i=bonus.getCol();
			if(bonus.getLine()!=0)
				j=bonus.getLine()-1;
			else
				j=bonus.getLine();	
		}
		if(bonus.getCol()<matrice.length)
			plusX=1;
		else
			plusX=0;
		if(bonus.getLine()<matrice.length)
			plusY=1;
		else
			plusY=0;
		
		
		//SCANNER
		while(!trouverMur){
			int jY=j;
			while(jY<bonus.getLine()+plusY){
				int iX=i;
				while(iX<bonus.getCol()+plusX){
					if(matrice[jY][iX]==tileControleur.MURDESTRUCTIBLE){
						AiTile mur=gameZone.getTile(jY, iX);
						for(int k=0;k<mur.getNeighbors().size();k++){
							if(tilesPossible.contains(mur.getNeighbors().get(k))){
								trouverMur=true;
								cible=mur.getNeighbors().get(k);
								break;
							}
						}
					}
					if(trouverMur==true) break;
					iX++;
				}
				if(trouverMur==true) break;
				jY++;
			}
			if(trouverMur==true) break;
			
			//on donne les nouveaux valeurs aux i,j,plusX,plusY
			//mais on fait l'attetion aux exceptitons
			//pour i
			if(i-1<0) i=0;
			else i=i-1;
			//pour j
			if(j-1<0) j=0;
			else j=j-1;
			//pour plusX
			if(bonus.getCol()+plusX+1<=matrice.length) plusX++;
			if(bonus.getLine()+plusY+1<=matrice.length) plusY++;
		}
		System.out.println("Hedef i in k racag m z duvar: "+cible);
		return cible;
	}
	
	/**
	 * Methode donne le tile de plus court path.
	 * @param matrice
	 * @param gameZone
	 * @return tile de cible
	 * @throws StopRequestException
	 * @throws LimitReachedException
	 */
	public AiTile murPlusProche(int[][] matrice,AiZone gameZone) throws StopRequestException, LimitReachedException{
		abc.checkInterruption();
		List<AiTile> onPeutAller=null;
		AiBlock temp;
		int s=0;
		List<AiBlock> murs=new ArrayList<AiBlock>();
		murs=gameZone.getBlocks();
		List<AiBlock> mursDesturictibles = new ArrayList<AiBlock>();
		
		List<AiTile> tilesPossible=new ArrayList<AiTile>();
		tileControleur.tilePossibleArrive(gameZone.getOwnHero().getTile(), tilesPossible, matrice);
		//on remove les mur indestructible
		for(int i=0;i<murs.size();i++){
			if(murs.get(i).isDestructible()){
					mursDesturictibles.add(s,murs.get(i));
					s++;
			}
		}
		
		
		//on efface les murs pas d'accesible
		for(int i=0;i<mursDesturictibles.size();i++){
			boolean effacer=true;
			List<AiTile> voisin=mursDesturictibles.get(i).getTile().getNeighbors();
			for(int j=0;j<voisin.size();j++){
				if(tilesPossible.contains(voisin.get(j))){
					effacer=false;
					break;
				}
			}
			if(effacer)
				mursDesturictibles.remove(i);
		}
		
		
		
		//Alors on les tie par distance
		for(int i=mursDesturictibles.size();i>2;i--){
			abc.checkInterruption();
			for(int j=0;j<i-1;j++){
				abc.checkInterruption();
				if(tileControleur.calculerDistance(mursDesturictibles.get(j).getTile())>tileControleur.calculerDistance(mursDesturictibles.get(j+1).getTile())){
					
					temp=mursDesturictibles.get(j+1);
					mursDesturictibles.remove(j+1);
					mursDesturictibles.add(j+1,mursDesturictibles.get(j));
					mursDesturictibles.remove(j);
					mursDesturictibles.add(j,temp);
				}
			}
		}
		if(mursDesturictibles.isEmpty()){
			
			System.out.println("duvar yokmus :D haa");
			return null;
		}
		else{
			System.out.println("duvarlar m z var beya :D :D :D :");
			for(int i=0;i<mursDesturictibles.size();i++){
				System.out.println(i+": "+mursDesturictibles.get(i).getLine()+","+mursDesturictibles.get(i).getCol());
			}
			return mursDesturictibles.get(0).getTile();
		}
		
	}

	public AiTile bonusPlusProche(int[][]matrice,AiZone gameZone){
		List<AiItem> lesBonus=gameZone.getItems();
		AiItem temp;
		for(int i=lesBonus.size();i>2;i--){
			for(int j=0;j<i-1;j++){
				if(tileControleur.calculerDistance(lesBonus.get(j).getTile())>tileControleur.calculerDistance(lesBonus.get(j+1).getTile())){
					temp=lesBonus.get(j+1);
					lesBonus.remove(j+1);
					lesBonus.add(j+1,lesBonus.get(j));
					lesBonus.remove(j);
					lesBonus.add(j,temp);
				}
			}
		}
		return lesBonus.get(0).getTile();

	}
}
