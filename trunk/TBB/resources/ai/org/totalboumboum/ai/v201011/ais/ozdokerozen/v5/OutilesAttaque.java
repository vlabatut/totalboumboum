package org.totalboumboum.ai.v201011.ais.ozdokerozen.v5;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;


/**
 * @author Sercan  zen & G.Lara  zd ker 
 *
 */
public class OutilesAttaque {

	/**
	 * Cette classe fait calculs pour attaque 
	 */
	ArtificialIntelligence AI;
	AiZone gameZone;
	AiHero notreHero;
	int[][] matrice;
	TileControleur tileControleur;
	List<AiTile> tilesPossible=new ArrayList<AiTile>();
	public OutilesAttaque(OzdokerOzen a,AiZone b,int[][] c) {
		AI=a;
		gameZone=b;
		matrice=c;
		notreHero=b.getOwnHero();
		tileControleur=new TileControleur(a,matrice);
		List<AiTile> scopes=new ArrayList<AiTile>();
		tileControleur.tilePossibleArriveAvecRisk(notreHero.getTile(),tilesPossible,scopes,matrice);
		// 
		}
	public AiTile bombBomMur(AiTile enemyTile){
		AiTile mur;
		List<AiBlock> murs=gameZone.getBlocks();
		System.out.println("MURS size :"+murs.size());
		List<AiTile> mursTiles=new ArrayList<AiTile>();
		
		for(int i=murs.size()-1;i>=0;i--){
			if(murs.get(i).isDestructible()){
				mursTiles.add(murs.get(i).getTile());
			}
		}
		for(int i=mursTiles.size()-1;i>=0;i--){
			if(!(tilesPossible.contains(mursTiles.get(i))))
				mursTiles.remove(i);
		}
		mur=mursTiles.get(0);
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
			System.out.println(herosTiles.get(i));
		}
		tileControleur.tierLesTilesParDistanceEtAccebilite(herosTiles);
		if((herosTiles.size())==0){
			return null;
		}
		else
			return heros.get(0);
	}
	
	
}
