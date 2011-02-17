package org.totalboumboum.ai.v201011.ais.ozdokerozen.v2;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;

/**
 * Class qui determine de poser bombe
 * @author G.Lara Ozdoker && Sercan Özen
 *
 */
public class TileControleur {
	AiZone gameZone;
	ArtificialIntelligence IA;
	int[][] matriceImaginaire;
	int MURDESTRUCTIBLE=5;
	int MURINDESTRUCTIBLE=0;
	int FIRE=-2;
	int SECURE=1;
	//variable temporiel
	AiTile temp;
	
	public TileControleur(OzdokerOzen ai,int matrice[][]) {
		IA=ai;
		gameZone=ai.getPercepts();
		matriceImaginaire=matrice;
	}
	
	/**
	 * Methode pose les valeurs dans matrice imaginaire
	 * @param placeABombe
	 */
	public void placerBombe(AiTile placeABombe){
		//on place le bombe où on pense.
		System.out.println("Matris boyutu: "+matriceImaginaire.length);
		System.out.println("bomba line: "+placeABombe.getLine());
		System.out.println("bomba colon: "+placeABombe.getCol());

		matriceImaginaire[placeABombe.getLine()][placeABombe.getCol()]=-1;
		System.out.println("Matris boyutu: "+matriceImaginaire.length);

		//on prend le range de notre bombe
		int bombeFire=gameZone.getOwnHero().getBombRange();
		
		//les cordinant de bomb imaginaire
		int bombX=placeABombe.getCol();
		int bombY=placeABombe.getLine();
		System.out.println("Matrisin length: "+matriceImaginaire.length+" matris clone: "+matriceImaginaire.clone());
		//On precise le domaine de bombe avec 4 for loop
		//on a utilisé 4 loop parceque on ne veut pas de tomber dans une exception (IndexBoundException par exemple.)
		for(int i=bombY+1;i<=bombY+bombeFire;i++){
			if(i>matriceImaginaire.length-1)
				break;
			if(matriceImaginaire[bombY][i]==MURDESTRUCTIBLE | matriceImaginaire[bombY][i]==MURINDESTRUCTIBLE)
				break;
			else
				matriceImaginaire[bombY][i]=-2;
		}
		
		for(int i=bombY-1;i<=bombY-bombeFire;i--){
			if(i<0)
				break;
			if(matriceImaginaire[bombY][i]==MURDESTRUCTIBLE | matriceImaginaire[bombY][i]==MURINDESTRUCTIBLE)
				break;
			else
				matriceImaginaire[bombY][i]=-2;
		}
		for(int i=bombX+1;i<=bombX+bombeFire;i++){
			System.out.println("BOM Y: "+bombY+" BomX: "+bombX);
			System.out.println("HERO Y: "+gameZone.getOwnHero().getLine()+" X: "+gameZone.getOwnHero().getCol());
			if(i>matriceImaginaire.length-1)
				break;
			if(matriceImaginaire[i][bombX]==MURDESTRUCTIBLE | matriceImaginaire[i][bombX]==MURINDESTRUCTIBLE)
				break;
			else
				matriceImaginaire[i][bombX]=-2;
		}
		
		for(int i=bombX-1;i<=bombX-bombeFire;i--){
			if(i<0)
				break;
			if(matriceImaginaire[i][bombX]==MURDESTRUCTIBLE | matriceImaginaire[i][bombX]==MURINDESTRUCTIBLE)
				break;
			else
				matriceImaginaire[i][bombX]=-2;
		}
		
	}


	public List<AiTile> dfsPlaces(AiTile start,AiTile tilePrecedent,int[][] matrice){
		List<AiTile> onPeutAller=new ArrayList<AiTile>();
		List<AiTile> voisins=start.getNeighbors();

		//DFS search algorithm
		for(int i=0;i<voisins.size();i++){
			if(voisins.get(i).equals(tilePrecedent)){
				voisins.remove(i);
				System.out.println("hata1");}
			else if(matrice[voisins.get(i).getLine()][voisins.get(i).getCol()]<=0){
				voisins.remove(i);
				System.out.println("hata2");
				}
			else if(matrice[voisins.get(i).getLine()][voisins.get(i).getCol()]==5){
				voisins.remove(i);
				System.out.println("hata3");
				}
			else if(matrice[voisins.get(i).getLine()][voisins.get(i).getCol()]==1){
				System.out.println("hata4");
				List<AiTile> onPeutAller2 = onPeutAller;
				onPeutAller2.add(0,voisins.get(i));
				onPeutAller.add(voisins.get(i));
				onPeutAller.addAll(dfsPlaces(voisins.get(i),start,matrice));
				}
			}
		
		return onPeutAller;
		}
		
	
	
	/**
	 * Methode qui decide facilement qu'il y a une path pour notre cible ou pas!   
	 * on utilise cette recusif fonction pour trouver bonus rapidment 
	 * @param hero
	 * @param hedef
	 * @return
	 */
	public boolean facileExistePathBonus(AiTile start,AiTile cible,int valeurDeCas,int[][] matrice,AiTile tilePrecedent){
		//baþlangýç deðerlerini alýyoruz!
		int hX=start.getCol();
		int hY=start.getLine();
		int cX=cible.getCol();
		int cY=cible.getLine();
		List<AiTile> voisins=start.getNeighbors();
		boolean retour=false;
		//on efface les tiles d'apres notre cas 
		//(le voisin doit etre dans le rectangle(hX,hY,cX,cY) et ne doit pas etre le Tile precedent)
		// et le voisin doit avoir le valeur 1(libre tile) ou 10(bonus tile)
		for(int i=0;i<voisins.size();i++){
			if(!((voisins.get(i).getCol()<=hX && voisins.get(i).getCol()>=cX)|| (voisins.get(i).getCol()>=hX && voisins.get(i).getCol()<=cX)))
				voisins.remove(i);
			else if(!((voisins.get(i).getLine()<=hY && voisins.get(i).getLine()>=cY)|| (voisins.get(i).getLine()>=hY && voisins.get(i).getLine()<=cY)))
				voisins.remove(i);
			else if(voisins.get(i)==tilePrecedent)
				voisins.remove(i);
			else if(matrice[voisins.get(i).getLine()][voisins.get(i).getCol()]<=0)
				voisins.remove(i);
			else if(matrice[voisins.get(i).getLine()][voisins.get(i).getCol()]==5)
				voisins.remove(i);
		}
		
		//Si le path ne se ramifie plus
		if(voisins.size()==0){
			retour=false;
		}else{
			for(int i=0;i<voisins.size();i++){
				if(matrice[voisins.get(i).getLine()][voisins.get(i).getCol()]==1){
					retour=facileExistePathBonus(voisins.get(i), cible, valeurDeCas, matrice, start);
					if(retour=true)
						break;
				}
				else{
					//on trouver bonus
					retour=true;
					break;
				}
			}
			
		}
		
		return retour;
	}
	
	
	/**
	 * Methode trouve tous les cas secure(sans danger)
	 * @return list des tiles
	 */
	public List<AiTile> trouverTileSecure(){
		List<AiTile> tilesSecure=new ArrayList<AiTile>();
		for(int i=0;i<matriceImaginaire.length;i++){
			for(int j=0;j<matriceImaginaire.length;j++){
				if(matriceImaginaire[j][i]==SECURE){
					tilesSecure.add(gameZone.getTile(j,i));
				}
			}
		}
		return tilesSecure;
	}

	/**
	 * Methode qui tie les path accecible 
	 * @param secureTiles
	 * @throws StopRequestException
	 * @throws LimitReachedException
	 */
	public void tierLesTilesParDistanceEtAccebilite(List<AiTile> secureTiles) throws StopRequestException, LimitReachedException{
		////////////////////////////////////
		//NOT:burada yoldaki diðer bombalarý ateþleri felan katmadýn!
		//////////////////////////////////////
		//premierement on remove les cible qu'on ne peut pas arriver
		for(int i=0;i<secureTiles.size();i++){
			if((calculateShortestPath(gameZone.getOwnHero(),gameZone.getOwnHero().getTile(),secureTiles.get(i))).isEmpty()){
				secureTiles.remove(i);
				i=i-1;
			}
		}
		//Alors on les tie par distance
		for(int i=secureTiles.size();i>2;i--){
			for(int j=0;j<i-1;j++){
				if(calculerDistance(secureTiles.get(j))>calculerDistance(secureTiles.get(j+1))){
					temp=secureTiles.get(j+1);
					secureTiles.remove(j+1);
					secureTiles.add(j+1,secureTiles.get(j));
					secureTiles.remove(j);
					secureTiles.add(j,temp);
				}
			}
		}
		
	}
	
	/**
	 * Methode qui calcule le path plus court
	 * @param ownHero
	 * @param startPoint
	 * @param endPoint
	 * @return les path plus court d'apres que A*
	 * @throws StopRequestException
	 * @throws LimitReachedException
	 */
	public AiPath calculateShortestPath(AiHero ownHero, AiTile startPoint,AiTile endPoint) throws StopRequestException, LimitReachedException {
		// le chemin le plus court possible
		AiPath shortestPath = null;
		// L'objet pour implementer l'algo A*
		Astar astar;
		// Calcul du cout et d'heuristic par la classe de l'API
		BasicCostCalculator cost=new BasicCostCalculator();
		HeuristicCalculator heuristic = new org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator();
		
		astar=new Astar(IA, gameZone.getOwnHero(), cost, heuristic);
		shortestPath = astar.processShortestPath(startPoint, endPoint);
		
		return shortestPath;
	}
	
	/**
	 * Methode qui calcule le path plus court pour les murs
	 * @param ownHero
	 * @param startPoint
	 * @param endPoint
	 * @return les path plus court d'apres que A*
	 * @throws StopRequestException
	 * @throws LimitReachedException
	 */
	public AiPath calculateShortestPath(AiHero ownHero, AiTile startPoint,AiTile endPoint,int[][] matrice) throws StopRequestException, LimitReachedException {
		// le chemin le plus court possible
		AiPath shortestPath = null;
		// L'objet pour implementer l'algo A*
		Astar astar;
		// Calcul du cout et d'heuristic par la classe de l'API
		BasicCostCalculator cost=new BasicCostCalculator();
		HeuristicCalculator heuristic = new org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator();
		astar=new Astar(IA, gameZone.getOwnHero(), cost, heuristic);	
		List<AiTile> neighboors = endPoint.getNeighbors();
		
		for (int i = 0; i < neighboors.size(); i++) {
			if(!(calculateShortestPath(gameZone.getOwnHero(),gameZone.getOwnHero().getTile(),neighboors.get(i)).isEmpty())){
				shortestPath = astar.processShortestPath(startPoint, neighboors.get(i));
				break;
			}else{
				shortestPath = astar.processShortestPath(startPoint, endPoint);
			}
		}
		return shortestPath;
	}
	/**
	 * Methode calculant plus court distance entre hero et un cible
	 * @param tile
	 * @param uneHero
	 * @return la distance
	 */		
	public int calculerDistance(AiTile tile){
		/** A* kullanan versiyon
		//les coordinant de hero
		AiHero hero=gameZone.getOwnHero();
		AiPath path;
		int result=99;
		try {
			//si le cible est un mur, on analyse neighboor de ce mur.
			if(matriceImaginaire[tile.getLine()][tile.getCol()]==5){
				
				//BURDA PROBLEM VAR, Tam olarak doðru hesaplamýyor.ilk ulaþýlan path i alýyor ama þimdilik idare eder.
				path=calculateShortestPath(hero, hero.getTile(),tile, matriceImaginaire);
			}else{
				path = calculateShortestPath(hero,hero.getTile(),tile);
			}
			result=path.getTiles().size();
			
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		} catch (LimitReachedException e) {
			// 
			e.printStackTrace();
		}
		return result;
		A* kullanan versiyon bitti */
		
		//Basit versiyon
		//les coordinant de hero
		int heroX=gameZone.getOwnHero().getCol();
		int heroY=gameZone.getOwnHero().getLine();
		//les coordinant de cible
		int cibleX=tile.getCol();
		int cibleY=tile.getLine();
		int result;
		result=(Math.abs(heroX-cibleX))+(Math.abs(heroY-cibleY))-1;
		return result;
	}

	public int possibleDeFuir(List<AiTile> espacePourFuir,AiTile placeBombe) throws StopRequestException, LimitReachedException{
		AiPath temp;
		AiHero hero=gameZone.getOwnHero();
		int result=-1;
		for(int i=0;i<espacePourFuir.size();i++){
			temp=calculateShortestPath(hero,hero.getTile(),espacePourFuir.get(i));
			//NOT:burada temp.getDuration() da kullanýlabilir.
			double distance=temp.getPixelDistance();
			//Bu deðeri alamadýgýmýz için sabit
			double durationBombe=640;
			if(durationBombe>(distance/hero.getWalkingSpeed())){
				result=i;
				break;
			}
			
		}
		return result;
	}
	
	/**
	 * 
	 * @param placeABombe
	 * @return
	 */
	public boolean estQueOnPoseBombe(AiTile placeABombe){
		placerBombe(placeABombe);
		boolean result=false;
		List<AiTile> cibleSecure=trouverTileSecure();
		try {
			tierLesTilesParDistanceEtAccebilite(cibleSecure);
			if(possibleDeFuir(cibleSecure, placeABombe)>=0){
				//oui,secure à poser a bombe dans le tile "placeABombe"
				System.out.println("bomba koyup kaçabiliriz");
				result=true;
			}else{
				System.out.println("bomba koymayalým cunku guvenli yer yok");
				//non,on doit faire une autre chose
				result=false;
			}
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		} catch (LimitReachedException e) {
			// 
			e.printStackTrace();
		}
		return result;
		
	}

}
