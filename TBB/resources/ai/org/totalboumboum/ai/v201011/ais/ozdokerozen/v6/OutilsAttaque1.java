package org.totalboumboum.ai.v201011.ais.ozdokerozen.v6;

import java.util.ArrayList;
import java.util.List;


import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;


/**
 * @author Gizem Lara Özdöker
 * @author Sercan Özen
 */
public class OutilsAttaque1 {

	/**
	 * Cette classe fait calculs pour attaque 
	 */
	ArtificialIntelligence AI;
	AiZone gameZone;
	AiHero notreHero;
	int[][] matrice;
	TileControleur tileControleur;
	List<AiTile> tilesPossible=new ArrayList<AiTile>();
	public OutilsAttaque1(OzdokerOzen a,AiZone b,int[][] c) throws StopRequestException {
		a.checkInterruption();
		AI=a;
		gameZone=b;
		matrice=c;
		notreHero=b.getOwnHero();
		tileControleur=new TileControleur(a,matrice);
		List<AiTile> scopes=new ArrayList<AiTile>();
		tileControleur.tilePossibleArriveAvecRisk(notreHero.getTile(),tilesPossible,scopes,matrice);
		tileControleur.tierLesTilesParDistanceEtAccebilite(tilesPossible);
		// 
	}
	public AiTile bombBomMur(AiTile enemyTile) throws StopRequestException{
		AI.checkInterruption();
		AiTile mur=null;
		List<AiBlock> murs=gameZone.getDestructibleBlocks();
		
		List<AiTile> tilesPossibleAdversaire=new ArrayList<AiTile>();
		List<AiTile> scopes=new ArrayList<AiTile>();
		tileControleur.tilePossibleArriveAvecRisk(enemyTile, tilesPossibleAdversaire, scopes, matrice);
		tileControleur.tierLesTilesParDistanceEtAccebilite(tilesPossibleAdversaire,notreHero.getTile());

		List<AiTile> mursTiles=new ArrayList<AiTile>();
		for(int i=0;i<murs.size();i++){
			AI.checkInterruption();
			List<AiTile> tileVoisins=murs.get(i).getTile().getNeighbors();
			for(int j=0;j<tileVoisins.size();j++){
				AI.checkInterruption();
				if(tilesPossible.contains(tileVoisins.get(j))){
					mursTiles.add(murs.get(i).getTile());
					break;
				}
			}		
		}

		if(mursTiles.size()!=0){
			if(tilesPossibleAdversaire.size()>0){
			tileControleur.tierLesTilesParDistanceEtAccebilite(mursTiles,tilesPossibleAdversaire.get(0));
			mur=mursTiles.get(0);
			}
		}
		return mur;
	}
	/**
	 * Method qui precise les places d'apres le cible qu'on attaque
	 * @param cible
	 * @return les places qu'on va aller! et poser les bombes.
	 * @throws StopRequestException 
	 */	
	public List<AiTile> bombsVoisin(AiTile cible) throws StopRequestException{
		AI.checkInterruption();
		List<AiTile> bamBam=new ArrayList<AiTile>();
		List<AiTile> voisins=new ArrayList<AiTile>();

		voisins=cible.getNeighbors();
		
		for(int i=0;i<voisins.size();i++){
			AI.checkInterruption();
			if(tilesPossible.contains(voisins.get(i))){
				bamBam.add(voisins.get(i));
			}
		}
		
		return bamBam;
	}
	
	/**
	 * Method qui precise les places d'apres le cible qu'on attaque
	 * @param cible
	 * @return les places qu'on va aller! et poser les bombes.
	 * @throws StopRequestException 
	 */	
	public List<AiTile> bombomPlace(AiTile cible) throws StopRequestException{
		AI.checkInterruption();
		List<AiTile> bamBam=new ArrayList<AiTile>();
		int lenght=matrice.length;
		int cX=cible.getCol(),cY=cible.getLine();
		int i,range=notreHero.getBombRange();
		AiTile temp;

		int size=bamBam.size();
		int control=bamBam.size();
		for(i=1;i<=range;i++){
			AI.checkInterruption();
			if(cX+i<=lenght){
				temp=gameZone.getTile(cY, cX+i);
				if(tilesPossible.contains(temp)){
						if(tileControleur.estQueOnPoseBombe(temp)){
							if(size!=control){
								bamBam.remove(size-1);
								bamBam.add(temp);
							}else{
								bamBam.add(temp);
								size++;
							}
						}	
				}else{break;}
			}else{break;}
		}
		size=bamBam.size();
		control=bamBam.size();
		for(i=1;i<=range;i++){
			AI.checkInterruption();
			if(cX-i>=0){
				temp=gameZone.getTile(cY, cX-i);
				if(tilesPossible.contains(temp)){
						if(tileControleur.estQueOnPoseBombe(temp)){
							if(size!=control){
								bamBam.remove(size-1);
								bamBam.add(temp);
							}else{
								bamBam.add(temp);
								size++;
							}
						}	
				}else{break;}
			}else{break;}
		}
		control=bamBam.size();
		size=bamBam.size();
		for(i=1;i<=range;i++){
			AI.checkInterruption();
			if(cY+i<=lenght){
				temp=gameZone.getTile(cY+i, cX);
				if(tilesPossible.contains(temp)){
						if(tileControleur.estQueOnPoseBombe(temp)){
							if(size!=control){
								bamBam.remove(size-1);
								bamBam.add(temp);
							}else{
								bamBam.add(temp);
								size++;
							}
						}	
				}else{break;}
			}else{break;}
		}
		control=bamBam.size();
		size=bamBam.size();
		for(i=1;i<=range;i++){
			AI.checkInterruption();
			if(cY-i>=0){
				temp=gameZone.getTile(cY-i, cX);
				if(tilesPossible.contains(temp)){
						if(tileControleur.estQueOnPoseBombe(temp)){
							if(size!=control){
								bamBam.remove(size-1);
								bamBam.add(temp);
							}else{
								bamBam.add(temp);
								size++;
							}
						}	
				}else{break;}
			}else{break;}
		}
		
		return bamBam;
	} 

	public AiHero adversaireCible() throws StopRequestException{
		AI.checkInterruption();
		List<AiHero> heros=gameZone.getRemainingHeroes();

		List<AiTile> herosTiles=new ArrayList<AiTile>();
		for(int i=0;i<heros.size();i++){
			AI.checkInterruption();
			if(heros.get(i).equals(notreHero)){
				heros.remove(i);
				i--;
			}	
				
		}
		for(int i=0;i<heros.size();i++){
			AI.checkInterruption();
			herosTiles.add(heros.get(i).getTile());
		}
		tileControleur.tierLesTilesParDistanceEtAccebilite(herosTiles);
		if((herosTiles.size())==0){
			return null;
		}
		else
			return heros.get(0);
	}
	
	public void attackAlgorithm(AiTile adversaire,List<AiTile> cibles) throws StopRequestException{
		AI.checkInterruption();
		int calculerDistance=(int) tileControleur.calculerDistance(adversaire, notreHero.getTile());		
		//Algoritm 1: Si l'adversaire est le meme tile avec notre hero!
		if(calculerDistance<1){
			//pour éloigner l'adversiare,on pose bombe notre tile
			//mais on ne peut pas poser bombe à otre tile, alors on aller une autre tile possible
			if(tileControleur.estQueOnPoseBombe(notreHero.getTile())){
				//Si on pose une bombe
				cibles.add(notreHero.getTile());

			}else{
				//Sinon
				cibles.add(tilesPossible.get(0));
			}
		}else if(calculerDistance<3){
			//Si l'adversaire est proche de nous
			List<AiTile> voisins=adversaire.getNeighbors();
			if(onDansRange(adversaire)){
				cibles.add(notreHero.getTile());
			}
			for(int i=0;i<voisins.size();i++){
				AI.checkInterruption();
				if(tilesPossible.contains(voisins.get(i))){
					cibles.add(voisins.get(i));
				}
			}
		}else{
			if(onDansRange(adversaire)){
				cibles.add(notreHero.getTile());
			}else{
			//Si l'adversaire est plus loin de nous
			//on s'approche
				cibles.add(adversaire);
			}
		}
			
		
	}
	
	/**
	 * Methode qui retourne que l'adversaire est sur un baril
	 * @param adversaireTile
	 * @param special
	 * @return
	 * @throws StopRequestException
	 */
	public boolean avoirSurUnBaril(AiTile adversaireTile,int special) throws StopRequestException{
		AI.checkInterruption();
		int coin=0;
		if(adversaireTile!=null && special==0){
			List<AiTile> voisin=adversaireTile.getNeighbors();	
			if(onDansRange(adversaireTile)){
				for(int i=0;i<voisin.size();i++){
					AI.checkInterruption();
					if(matrice[voisin.get(i).getLine()][voisin.get(i).getCol()]==-1
							||matrice[voisin.get(i).getLine()][voisin.get(i).getCol()]==tileControleur.MURDESTRUCTIBLE
							||matrice[voisin.get(i).getLine()][voisin.get(i).getCol()]==tileControleur.MURINDESTRUCTIBLE)
						coin++;
				}
				if(coin>=3){

					return true;
				}else
					return false;
			}else
				return false;
			}
		else
			return false;
	}
	
	/**
	 * Methode qui retourne que  l'adversaire est un cooridor
	 * @param adversaireTile
	 * @param special
	 * @return
	 * @throws StopRequestException
	 */
	public boolean avoirSurUnCoridor(AiTile adversaireTile,int special) throws StopRequestException{
		AI.checkInterruption();
		int coin=0;
		if(adversaireTile!=null && special==0){
			List<AiTile> voisin=adversaireTile.getNeighbors();	
			if(onDansRange(adversaireTile)){
				for(int i=0;i<voisin.size();i++){
					AI.checkInterruption();
					if(matrice[voisin.get(i).getLine()][voisin.get(i).getCol()]==-1
							||matrice[voisin.get(i).getLine()][voisin.get(i).getCol()]==tileControleur.MURDESTRUCTIBLE
							||matrice[voisin.get(i).getLine()][voisin.get(i).getCol()]==tileControleur.MURINDESTRUCTIBLE
							||matrice[voisin.get(i).getLine()][voisin.get(i).getCol()]==tileControleur.SCOPE)
						coin++;
				}
				if(coin>=2){

					return true;
				}else
					return false;
			}else
				return false;
			}
		else
			return false;
	}
	
	/**
	 * Methode qui retourne qu'adversaire est dans notre portée ou pas
	 * @param adversaireTile
	 * @return boolean
	 * @throws StopRequestException
	 */
	public boolean onDansRange(AiTile adversaireTile) throws StopRequestException{
		AI.checkInterruption();
		boolean flag=false;
		boolean line=false;
		int hx,hy,cx,cy;
		hx=notreHero.getTile().getCol();
		hy=notreHero.getTile().getLine();
		cx=adversaireTile.getCol();
		cy=adversaireTile.getLine();
		
		if(hx==cx || hy==cy){
			if(hx==cx){//meme colon
				line=false;
				if(notreHero.getBombRange()>=Math.abs(cy-hy)){
					if(tilesEstPropre(adversaireTile, line, cy>hy))
						flag=true;
				}
			}else{
				line=true;
				if(notreHero.getBombRange()-3>=Math.abs(cx-hx)){
					if(tilesEstPropre(adversaireTile, line, cx>hx))
						flag=true;
				}
			}
		}
		return flag;
	}
	
	/**
	 * Methode qui retourne qu'il y a une obstacle entre notre hero et adversaire
	 * @param adversaireTile
	 * @param line
	 * @param plus
	 * @return
	 * @throws StopRequestException
	 */
	public boolean tilesEstPropre(AiTile adversaireTile,boolean line,boolean plus) throws StopRequestException{
		AI.checkInterruption();
		int i;
		boolean flag=true;
		//si on est dans meme line
		if(line){
			if(plus){
				for(i=adversaireTile.getCol()-1;i>notreHero.getTile().getCol();i--){
					AI.checkInterruption();
					if(matrice[adversaireTile.getLine()][i]==tileControleur.MURDESTRUCTIBLE
							||matrice[adversaireTile.getLine()][i]==tileControleur.MURINDESTRUCTIBLE
							||matrice[adversaireTile.getLine()][i]==tileControleur.FIRE)
						flag=false;
				}
			}else{
				for(i=notreHero.getTile().getCol()-1;i>adversaireTile.getCol();i--){
					AI.checkInterruption();
					if(matrice[adversaireTile.getLine()][i]==tileControleur.MURDESTRUCTIBLE
							||matrice[adversaireTile.getLine()][i]==tileControleur.MURINDESTRUCTIBLE
							||matrice[adversaireTile.getLine()][i]==tileControleur.FIRE)
						flag=false;
				}
			}
		}else{//sinon, on est dans meme colon
			if(plus){
				for(i=adversaireTile.getLine()-1;i>notreHero.getTile().getLine();i--){
					AI.checkInterruption();
					if(matrice[adversaireTile.getLine()][i]==tileControleur.MURDESTRUCTIBLE
							||matrice[adversaireTile.getLine()][i]==tileControleur.MURINDESTRUCTIBLE
							||matrice[adversaireTile.getLine()][i]==tileControleur.FIRE)
						flag=false;
				}
			}else{
				for(i=notreHero.getTile().getLine()-1;i>adversaireTile.getLine();i--){
					AI.checkInterruption();
					if(matrice[i][adversaireTile.getCol()]==tileControleur.MURDESTRUCTIBLE
							||matrice[i][adversaireTile.getCol()]==tileControleur.MURINDESTRUCTIBLE
							||matrice[i][adversaireTile.getCol()]==tileControleur.FIRE)
						flag=false;
				}
			}
			
		}
		return flag;
	}
	
	
}
