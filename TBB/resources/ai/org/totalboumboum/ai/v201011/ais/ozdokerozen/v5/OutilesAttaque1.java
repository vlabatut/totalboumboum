package org.totalboumboum.ai.v201011.ais.ozdokerozen.v5;

import java.util.ArrayList;
import java.util.List;


import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;


/**
 * @author Sercan �zen & G.Lara �zd�ker 
 *
 */
public class OutilesAttaque1 {

	/**
	 * Cette classe fait calculs pour attaque 
	 */
	ArtificialIntelligence AI;
	AiZone gameZone;
	AiHero notreHero;
	int[][] matrice;
	TileControleur tileControleur;
	List<AiTile> tilesPossible=new ArrayList<AiTile>();
	public OutilesAttaque1(OzdokerOzen a,AiZone b,int[][] c) {
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
	public AiTile bombBomMur(AiTile enemyTile){
		AiTile mur=null;
		List<AiBlock> murs=gameZone.getDestructibleBlocks();
		
		List<AiTile> tilesPossibleAdversaire=new ArrayList<AiTile>();
		List<AiTile> scopes=new ArrayList<AiTile>();
		tileControleur.tilePossibleArriveAvecRisk(enemyTile, tilesPossibleAdversaire, scopes, matrice);
		tileControleur.tierLesTilesParDistanceEtAccebilite(tilesPossibleAdversaire,notreHero.getTile());

		List<AiTile> mursTiles=new ArrayList<AiTile>();
		for(int i=0;i<murs.size();i++){
			List<AiTile> tileVoisins=murs.get(i).getTile().getNeighbors();
			for(int j=0;j<tileVoisins.size();j++){
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
	 */	
	public List<AiTile> bombsVoisin(AiTile cible){
		List<AiTile> bamBam=new ArrayList<AiTile>();
		List<AiTile> voisins=new ArrayList<AiTile>();

		voisins=cible.getNeighbors();
		
		for(int i=0;i<voisins.size();i++){
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
	 */	
	public List<AiTile> bombomPlace(AiTile cible){
		
		List<AiTile> bamBam=new ArrayList<AiTile>();
		int lenght=matrice.length;
		int cX=cible.getCol(),cY=cible.getLine();
		int i,range=notreHero.getBombRange();
		AiTile temp;

		int size=bamBam.size();
		int control=bamBam.size();
		for(i=1;i<=range;i++){
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

	public AiHero adversaireCible(){
		List<AiHero> heros=gameZone.getRemainingHeroes();

		List<AiTile> herosTiles=new ArrayList<AiTile>();
		for(int i=0;i<heros.size();i++){
			
			if(heros.get(i).equals(notreHero)){
				heros.remove(i);
				i--;
			}	
				
		}
		for(int i=0;i<heros.size();i++){
			herosTiles.add(heros.get(i).getTile());
		}
		tileControleur.tierLesTilesParDistanceEtAccebilite(herosTiles);
		if((herosTiles.size())==0){
			return null;
		}
		else
			return heros.get(0);
	}
	
	public void attackAlgorithm(AiTile adversaire,List<AiTile> cibles){
		int calculerDistance=tileControleur.calculerDistance(adversaire, notreHero.getTile());
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
		}else if(calculerDistance<4){
			//Si l'adversaire est proche de nous
			List<AiTile> voisins=adversaire.getNeighbors();
			if(onDansRange(adversaire)){
				cibles.add(notreHero.getTile());
			}
			for(int i=0;i<voisins.size();i++){
				if(tilesPossible.contains(voisins.get(i))){
					cibles.add(voisins.get(i));
				}
			}
		}else{
			//Si l'adversaire est plus loin de nous
			//on s'approche
			cibles.add(adversaire);
		}
		
	}
	
	public boolean onDansRange(AiTile adversaireTile){
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
				if(notreHero.getBombRange()-3>=Math.abs(cy-hy)){
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
	
	public boolean tilesEstPropre(AiTile adversaireTile,boolean line,boolean plus){
		int i;
		boolean flag=true;
		//si on est dans meme line
		if(line){
			if(plus){
				for(i=adversaireTile.getCol()-1;i>notreHero.getTile().getCol();i--){
					if(matrice[adversaireTile.getLine()][i]==tileControleur.MURDESTRUCTIBLE
							||matrice[adversaireTile.getLine()][i]==tileControleur.MURINDESTRUCTIBLE
							||matrice[adversaireTile.getLine()][i]==tileControleur.FIRE)
						flag=false;
				}
			}else{
				for(i=notreHero.getTile().getCol()-1;i>adversaireTile.getCol();i--){
					if(matrice[adversaireTile.getLine()][i]==tileControleur.MURDESTRUCTIBLE
							||matrice[adversaireTile.getLine()][i]==tileControleur.MURINDESTRUCTIBLE
							||matrice[adversaireTile.getLine()][i]==tileControleur.FIRE)
						flag=false;
				}
			}
		}else{//sinon, on est dans meme colon
			if(plus){
				for(i=adversaireTile.getLine()-1;i>notreHero.getTile().getLine();i--){
					if(matrice[adversaireTile.getLine()][i]==tileControleur.MURDESTRUCTIBLE
							||matrice[adversaireTile.getLine()][i]==tileControleur.MURINDESTRUCTIBLE
							||matrice[adversaireTile.getLine()][i]==tileControleur.FIRE)
						flag=false;
				}
			}else{
				for(i=notreHero.getTile().getLine()-1;i>adversaireTile.getLine();i--){
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
