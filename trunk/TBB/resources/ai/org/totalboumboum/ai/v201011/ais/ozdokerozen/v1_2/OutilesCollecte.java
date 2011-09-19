package org.totalboumboum.ai.v201011.ais.ozdokerozen.v1_2;

import java.util.ArrayList;
import java.util.List;

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

public class OutilesCollecte {

	TileControleur tileControleur;
	List<AiItem> lesBonus;
	ArtificialIntelligence abc;
	 //pour les sortie dans API
	@SuppressWarnings("unused")
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
					System.out.println("buraya bakm�yoruz.");
				}else{
					if(matrice[prochepath.get(i).getLine()][prochepath.get(i).getCol()]==hedef){
						System.out.println(" HEDEF bulundu ve ekleniyor: "+prochepath.get(i).getLine()+","+prochepath.get(i).getCol());
						tile.add(prochepath.get(i));
						System.out.println("////////////////////");
						System.out.println("Bonusu bulduk be abicim,�imdi geri d�n�yoruz");
						System.out.println("////////////////////");
						break;
					}else if(matrice[prochepath.get(i).getLine()][prochepath.get(i).getCol()]==0 || matrice[prochepath.get(i).getLine()][prochepath.get(i).getCol()]==5){
						System.out.println("Duvar var buralarda bu yol yalan oldu");
						tile = null;
					}else if(matrice[prochepath.get(i).getLine()][prochepath.get(i).getCol()]<0){
						System.out.println("Bu k�s�mlarda bomba var,ate�i var yalan olur bu yolda");
						tile = null;
					}else if(matrice[prochepath.get(i).getLine()][prochepath.get(i).getCol()]==1){
						System.out.println("Bu yol a��k devam edelim");
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
	 * Methode precise mur lequel on doit detruit pour arriver à bonus s'il n'existe pas de path pour arriver bonus 
	 * @param bonus
	 * @param matrice
	 * @param gameZone
	 * @return mur qu'on detruit
	 * @throws StopRequestException
	 * @throws LimitReachedException
	 */
	public AiTile murQuOnVaDetruitre(AiTile bonus,int[][] matrice,AiZone gameZone) throws StopRequestException, LimitReachedException{
		boolean trouverMur=false;
		AiTile cible=null;
		//on commence à scanner de ces valeur
		int i,j,plusX=1,plusY=1;
		System.out.println("Hata ar�yoruz");
		//on les initialise en faire attention matrice length pour ne pas tomber à l'exception
		if(bonus.getCol()!=matrice.length){
			if(bonus.getLine()!=matrice.length){
					i=bonus.getCol()-1;
					j=bonus.getLine()-1;
			}else{
				i=bonus.getCol()-1;
				j=bonus.getLine();
			}	
		}else{
			i=bonus.getCol();
			if(bonus.getLine()!=matrice.length)
				j=bonus.getLine()-1;
			else
				j=bonus.getLine();	
		}
		
		//SCANNER
		while(!trouverMur){
			if(bonus.getLine()<matrice.length){
				int jX=j;
				while(jX<=bonus.getLine()+plusY){

					if(bonus.getCol()<matrice.length){
						int iX=i;
						while(iX<=bonus.getCol()+plusX){
							if(matrice[jX][iX]==tileControleur.MURDESTRUCTIBLE){
								if(!tileControleur.calculateShortestPath(gameZone.getOwnHero(),gameZone.getOwnHero().getTile(),gameZone.getTile(jX, iX),matrice).isEmpty()){
									trouverMur=true;
									cible=gameZone.getTile(jX,iX);
									break;
									
								}else{
									iX++;
								}
							}else{
								iX++;
							}
							
						}
					}else{
						int iX=i;
						while(iX<=matrice.length){
							if(matrice[jX][iX]==tileControleur.MURDESTRUCTIBLE){
								if(!tileControleur.calculateShortestPath(gameZone.getOwnHero(),gameZone.getOwnHero().getTile(),gameZone.getTile(jX, iX),matrice).isEmpty()){
									trouverMur=true;
									cible=gameZone.getTile(jX,iX);
									break;
									
								}else{
									iX++;
								}
							}else{
								iX++;
							}
						}
					}
					if(trouverMur)
						break;
					jX++;
				}
			}else{
				int jX=j;
				System.out.println("Hata6");
				while(jX<=matrice.length){
					if(bonus.getCol()<matrice.length){
						int iX=i;
						while(iX<=bonus.getCol()+plusX){
							if(matrice[jX][iX]==tileControleur.MURDESTRUCTIBLE){
								if(!tileControleur.calculateShortestPath(gameZone.getOwnHero(),gameZone.getOwnHero().getTile(),gameZone.getTile(jX, iX),matrice).isEmpty()){
									trouverMur=true;
									cible=gameZone.getTile(jX,iX);
									break;
									
								}else{
									iX++;
								}
							}else{
								iX++;
							}
						}
					}else{
						int iX=i;
						System.out.println("Hata8");
						while(iX<=matrice.length){
							if(matrice[jX][iX]==tileControleur.MURDESTRUCTIBLE){
								if(!tileControleur.calculateShortestPath(gameZone.getOwnHero(),gameZone.getOwnHero().getTile(),gameZone.getTile(jX, iX),matrice).isEmpty()){
									trouverMur=true;
									cible=gameZone.getTile(jX,iX);
									break;
									
								}else{
									iX++;
								}
							}else{
								iX++;
							}
						}
					}
					if(trouverMur)
						break;
					jX++;
								}
			}
		if(i>0) i--;
		else i=0;
		if(j>0) j--;
		else j=0;
		if(plusX+bonus.getCol()<matrice.length)
			plusX++;
		if(plusY+bonus.getLine()<matrice.length)
			plusY++;
		
		}

		System.out.println("Hata9");
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

		//on remove les mur indestructible
		for(int i=0;i<murs.size();i++){
			if(murs.get(i).isDestructible()){
					mursDesturictibles.add(s,murs.get(i));
					s++;
			}
		}
		
		
		//on efface les murs pas d'accesible
		for(int i=0;i<mursDesturictibles.size();i++){
			System.out.println("kontrol edilecek duvar: "+mursDesturictibles.get(i).getLine()+","+mursDesturictibles.get(i).getCol());
			boolean effacer=true;
			onPeutAller=tileControleur.dfsPlaces(gameZone.getOwnHero().getTile(), gameZone.getOwnHero().getTile(), matrice);
			System.out.println("////buralara gidebiliriz////");
			for(int c=0;c<onPeutAller.size();c++){
				System.out.println(c+": "+onPeutAller.get(c).getLine()+","+onPeutAller.get(c).getCol());
			}
			System.out.println("///////////////////");
			List<AiTile> voisin=mursDesturictibles.get(i).getTile().getNeighbors();
			if(voisin.size()>0){
			for(int k=0;k<voisin.size();k++){
				if(onPeutAller.contains(voisin.get(k))){
					effacer=false;
					System.out.println("Yol var baboli");
					break;
				}
			}
			}
			if(effacer){
				mursDesturictibles.remove(i);
			}
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
			System.out.println("duvarlar�m�z var beya :D :D :D :");
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
