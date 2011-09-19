package org.totalboumboum.ai.v201011.ais.ozdokerozen.v5;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;

public class OutilesCollecte {

	TileControleur tileControleur;
	List<AiItem> lesBonus;
	ArtificialIntelligence abc;
	List<AiTile> tilesPossible=new ArrayList<AiTile>();
	AiHero notreHero;
	
	public OutilesCollecte(OzdokerOzen ai,AiZone gameZone,int[][] matrice) {
		tileControleur=new TileControleur(ai, matrice);
		abc=ai;
		notreHero=gameZone.getOwnHero();
		List<AiTile> scopes=new ArrayList<AiTile>();
		tileControleur.tilePossibleArriveAvecRisk(notreHero.getTile(),tilesPossible,scopes,matrice);
		tileControleur.tierLesTilesParDistanceEtAccebilite(tilesPossible);
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
	 * Methode qui precise le mur qu'on va detruire d'apres le cible
	 * @param bonus (cible)
	 * @param matrice
	 * @param gameZone
	 * @return
	 */
	public AiTile murQuOnVaDetruitre(AiTile bonus,int[][] matrice,AiZone gameZone) {
		try {
			abc.checkInterruption();
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}
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
			try {
				abc.checkInterruption();
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
			} catch (StopRequestException e) {
				// 				e.printStackTrace();
			}
			
		}
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
	public AiTile murPlusProche(int[][] matrice,AiZone gameZone) throws StopRequestException{
		abc.checkInterruption();
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
			return null;
		}
		else{
			return mursDesturictibles.get(0).getTile();
		}
		
	}

	/**
	 * Methode qui retourne le bonus plus proche 
	 * @param matrice
	 * @param gameZone
	 * @return
	 */
	public AiTile bonusPlusProche(int[][]matrice,AiZone gameZone){
		List<AiItem> lesBonus=new ArrayList<AiItem>();
		lesBonus=gameZone.getItems();
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
	
	/**
	 * Methode qui retourne le bonus plus proche qu'on peut arrive 
	 * @param matrice
	 * @param gameZone
	 * @return
	 */
	public AiTile bonusArriveProche(int[][]matrice,AiZone gameZone){
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
		for(int i=lesBonus.size()-1;i>=0;i--){
			if(!tilesPossible.contains(lesBonus.get(i)))
				lesBonus.remove(i);
		}
		if(lesBonus.size()!=0)
			return lesBonus.get(0).getTile();
		else
			return null;

	}
}
