package org.totalboumboum.ai.v201011.ais.ozdokerozen.v4;

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
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;

/**
 * Class qui determine de poser bombe
 * @author Gizem Lara Özdöker
 * @author Sercan Özen
 */
@SuppressWarnings("deprecation")
public class TileControleur {
	AiZone gameZone;
	ArtificialIntelligence IA;
	int[][] matriceImaginaire;
	
	int MURDESTRUCTIBLE=5;
	int MURINDESTRUCTIBLE=0;
	int FIRE=-2;
	int SECURE=1;
	int BONUS=10;
	int SCOPE=-3;
	
	//variable temporiel
	AiTile temp;
	
	public TileControleur(OzdokerOzen ai,int matrice[][]) {
		IA=ai;
		gameZone=ai.getPercepts();
		matriceImaginaire=matrice;
	}
	/**
	 * Methode qui calcule les valeurs dans la matrice apres posage le bomb
	 * @param placeABombe
	 */
	public void placerBombe(AiTile placeABombe){
		if(placeABombe!=null){
			//on place le bombe où on pense.			
			matriceImaginaire[placeABombe.getLine()][placeABombe.getCol()]=-1;
	
			//on prend le range de notre bombe
			int bombeFire=gameZone.getOwnHero().getBombRange();
			
			//les cordinant de bomb imaginaire
			int bombX=placeABombe.getCol();
			int bombY=placeABombe.getLine();
			//On precise le domaine de bombe avec 4 for loop
			//on a utilisé 4 loop parceque on ne veut pas de tomber dans une exception (IndexBoundException par exemple.)
			
			for(int i=bombY+1;i<=bombY+bombeFire;i++){
				if(i>matriceImaginaire.length-1)
					break;
				if(matriceImaginaire[i][bombX]==MURDESTRUCTIBLE | matriceImaginaire[i][bombX]==MURINDESTRUCTIBLE)
					break;
				else
					matriceImaginaire[i][bombX]=-3;
			}
			
			for(int i=bombY-1;i>=bombY-bombeFire;i--){
				if(i<0)
					break;
				if(matriceImaginaire[i][bombX]==MURDESTRUCTIBLE | matriceImaginaire[i][bombX]==MURINDESTRUCTIBLE)
					break;
				else
					matriceImaginaire[i][bombX]=-3;
			}
			for(int i=bombX+1;i<=bombX+bombeFire;i++){
				if(i>matriceImaginaire.length-1)
					break;
				if(matriceImaginaire[bombY][i]==MURDESTRUCTIBLE | matriceImaginaire[bombY][i]==MURINDESTRUCTIBLE)
					break;
				else
					matriceImaginaire[bombY][i]=-3;
			}
			
			for(int i=bombX-1;i>=bombX-bombeFire;i--){
				if(i<0)
					break;
				if(matriceImaginaire[bombY][i]==MURDESTRUCTIBLE | matriceImaginaire[bombY][i]==MURINDESTRUCTIBLE)
					break;
				else
					matriceImaginaire[bombY][i]=-3;
			}
		}
		
	}


	
	/**
	 * dfs algoritmas  kullanarak gidilebilecek tile'lar  buluyor.
	 * TEST: S PER sadece bomba hizalar n  alm yor.
	 * onun i in oralar  R SK'e alan fonksiyon yazmal y z!
	 * @return
	 * @throws StopRequestException 
	 */
	public void tilePossibleArrive(AiTile debut,List<AiTile> tilesPossible,int[][] matrice) {

		for(int i=0;i<debut.getNeighbors().size();i++){		
			//Y k labilen duvara kars l k geliyorsa Salla
			if(matrice[debut.getNeighbors().get(i).getLine()][debut.getNeighbors().get(i).getCol()]==SECURE){
				if(!(tilesPossible.contains(debut.getNeighbors().get(i)))){
					tilesPossible.add(debut.getNeighbors().get(i));
					tilePossibleArrive(debut.getNeighbors().get(i),tilesPossible,matrice);
				}
			}
			if(matrice[debut.getNeighbors().get(i).getLine()][debut.getNeighbors().get(i).getCol()]==BONUS){
				if(!(tilesPossible.contains(debut.getNeighbors().get(i)))){
					tilesPossible.add(debut.getNeighbors().get(i));
					tilePossibleArrive(debut.getNeighbors().get(i),tilesPossible,matrice);

				}
			}
		}
	}
	
	/**
	 *Methode qui retourne les tiles qu'on peut arrive
	 * @return les tiles qu'on peut arrive
	 * @throws StopRequestException 
	 */
	public void tilePossibleArriveAvecRisk(AiTile debut,List<AiTile> tilesPossible,List<AiTile> scopes,int[][] matrice) {
		for(int i=0;i<debut.getNeighbors().size();i++){	
			try {
				IA.checkInterruption();
				//eklenen k s m
				if(matrice[debut.getNeighbors().get(i).getLine()][debut.getNeighbors().get(i).getCol()]==SCOPE){
					if(!(scopes.contains(debut.getNeighbors().get(i)))){
						scopes.add(debut.getNeighbors().get(i));
						tilePossibleArriveAvecRisk(debut.getNeighbors().get(i),tilesPossible,scopes,matrice);
					}
				}
				//Y k labilen duvara kars l k geliyorsa Salla
				if(matrice[debut.getNeighbors().get(i).getLine()][debut.getNeighbors().get(i).getCol()]==SECURE){
					if(!(tilesPossible.contains(debut.getNeighbors().get(i)))){
						tilesPossible.add(debut.getNeighbors().get(i));
						tilePossibleArriveAvecRisk(debut.getNeighbors().get(i),tilesPossible,scopes,matrice);
					}
				}
				if(matrice[debut.getNeighbors().get(i).getLine()][debut.getNeighbors().get(i).getCol()]==BONUS){
					if(!(tilesPossible.contains(debut.getNeighbors().get(i)))){
						tilesPossible.add(debut.getNeighbors().get(i));
						tilePossibleArrive(debut.getNeighbors().get(i),tilesPossible,matrice);

					}
				}
				
			} catch (StopRequestException e) {
				// 
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Methode qui decide facilement qu'il y a une path pour notre cible ou pas!   
	 * on utilise cette recusif fonction pour trouver bonus rapidment 
	 * @param hero
	 * @param hedef
	 * @return
	 */
	public boolean facileExistePathBonus(AiTile start,AiTile cible,int valeurDeCas,int[][] matrice,AiTile tilePrecedent){
		//ba lang   de erlerini al yoruz!
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
	 * Methode qui tie les path accecible 
	 * @param secureTiles
	 * @throws StopRequestException
	 * @throws LimitReachedException
	 */
	public void tierLesTilesParDistanceEtAccebilite(List<AiTile> secureTiles) {
		////////////////////////////////////
		//NOT:burada yoldaki di er bombalar  ate leri felan katmad n!
		//////////////////////////////////////
		//premierement on remove les cible qu'on ne peut pas arriver
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
	public AiPath calculateShortestPath(AiHero notreHero, AiTile startPoint,AiTile endPoint) throws StopRequestException {
		// le chemin le plus court possible
		AiPath shortestPath = null;
		// L'objet pour implementer l'algo A*
		Astar astar;
		// Calcul du cout et d'heuristic par la classe de l'API
		
		BasicCostCalculator cost=new BasicCostCalculator();
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		astar=new Astar(IA, notreHero, cost, heuristic);
		try {
			shortestPath = astar.processShortestPath(startPoint, endPoint);
			return shortestPath;
		} catch (LimitReachedException e) {
			// 
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	/**
	 * Methode qui calcule un path d'apres algorithm DFS(depth-first-search)
	 * @param debut
	 * @param fin
	 * @param onAVu
	 * @param path
	 * @param tilesPossibles
	 * @return
	 */
	boolean pathFinder(AiTile debut,AiTile fin,List<AiTile> onAVu,List<AiTile> path,List<AiTile> tilesPossibles){
		boolean retour=false;
		for(int i=0;i<debut.getNeighbors().size();i++){
			if(debut.getNeighbors().get(i).equals(fin)){
				retour=true;
				if(!(path.contains(fin)) && !(path.contains(debut))){
					path.add(fin);
					path.add(debut);
					}
				break;
			}
			else if(tilesPossibles.contains(debut.getNeighbors().get(i)) && !(onAVu.contains(debut.getNeighbors().get(i)))){
				onAVu.add(debut.getNeighbors().get(i));
				retour=pathFinder(debut.getNeighbors().get(i), fin, onAVu, path, tilesPossibles);
				if(retour==true){
					path.add(debut);
					break;
				}
			}else if(!(tilesPossibles.contains(debut.getNeighbors().get(i)))){
				retour=false;
			}
			
		}
	return retour;
	}
	
	/**
	 * Mehotde qui cree une path meilleur selon les informations 
	 * de list de tile de recherche DFS(depth first search)
	 * @param path
	 * @param pathOptimise
	 * @param fin
	 */
	void ameliorePath(List<AiTile> path,AiPath pathOptimise,AiTile fin){
		int j=0;
		List<AiTile> dejavu=new ArrayList<AiTile>();
		for(int i=(path.size()-1);i>=0;i--){
			if(path.get(i).equals(gameZone.getOwnHero().getTile()) && !(dejavu.contains(path.get(i)))){
				pathOptimise.addTile(path.get(i));
				dejavu.add(path.get(i));

				j++;
				}
			else if(calculerDistance(fin,path.get(i))==0){
				pathOptimise.addTile(path.get(i));
				dejavu.add(path.get(i));
				break;
			}else{
				AiTile temp;
				List<AiTile> list=new ArrayList<AiTile>();
				list=pathOptimise.getTiles().get(j-1).getNeighbors();
				for(int k=0;k<list.size();k++){
					if(!(path.contains(list.get(k))))
					{
						list.remove(k);
						k--;
					}else if(dejavu.contains(list.get(k))){
						//ecran.setTileColor(list.get(k), Color.ORANGE);
						list.remove(k);
						k--;
					}
				}
				if(list.size()==1){
					pathOptimise.addTile(path.get(i));
					dejavu.add(path.get(i));
					j++;
				}else if(list.size()>1){
					temp=list.get(0);
					for(int k=1;k<list.size();k++){
						if(calculerDistance(fin,list.get(k-1))>calculerDistance(fin,list.get(k)))
							temp=list.get(k);
						
					}
					pathOptimise.addTile(temp);
					i=path.indexOf(temp);
					dejavu.add(temp);
					j++;
				}
				
			}
		}
		//Si on passe le meme tile deux fois, on peux les effacer
		AiTile tileControl;
		
		for(int i=0;i<pathOptimise.getTiles().size();i++){
			tileControl=pathOptimise.getTiles().get(i);
			for(j=i+1;j<pathOptimise.getTiles().size();j++){
				if(tileControl.equals(pathOptimise.getTiles().get(j))){
					for(int k=j;k>i;k--){
						pathOptimise.removeTile(k);
						
					}
				}
			}
			
		}
	}
	
	/**
	 * Methode qui calculent entre deux tile simplement
	 * @param tile
	 * @param debut
	 * @return
	 */
	public int calculerDistance(AiTile tile,AiTile debut){
		//Basit versiyon
		int hX=debut.getCol();
		int hY=debut.getLine();
		//les coordinant de cible
		int cibleX=tile.getCol();
		int cibleY=tile.getLine();
		int result;
		result=(Math.abs(hX-cibleX))+(Math.abs(hY-cibleY));
		return result;
	}
	
	
	/**
	 * Methode calculant plus court distance entre hero et un cible
	 * @param tile
	 * @param uneHero
	 * @return la distance
	 */		
	public double calculerDistance(AiTile tile){
		//Basit versiyon
		//les coordinant de hero
		double heroX=gameZone.getOwnHero().getPosX();
		double heroY=gameZone.getOwnHero().getPosY();
		
		//les coordinant de cible
		double cibleX=tile.getPosX();
		double cibleY=tile.getPosY();
		double result;
		result=(Math.abs(heroX-cibleX))+(Math.abs(heroY-cibleY))-1;
		return result;
	}

	/**
	 * Methode qui calcule possiblite de fuir
	 * @param espacePourFuir
	 * @param placeBombe
	 * @return si vrai retourne 0 sinon -1 
	 * @throws StopRequestException
	 * @throws LimitReachedException
	 */
	public int possibleDeFuir(List<AiTile> espacePourFuir,AiTile placeBombe) throws StopRequestException, LimitReachedException{
		AiPath temp;
		AiHero hero=gameZone.getOwnHero();

		if(espacePourFuir.size()==0)
			return -1;
		else{
			temp=calculateShortestPath(hero,hero.getTile(),espacePourFuir.get(0));
			//Bu de eri alamad g m z i in sabit
			double durationBombe=640;
			if(durationBombe>temp.getDuration(hero)){
				return 0;	
			}else{
			return -1;			
					}
		
		}
	}
	
	/**
	 * Methode qui nous dit la decision la posage bombe à un tile
	 * @param placeABombe
	 * @return la decision
	 */
	public boolean estQueOnPoseBombe(AiTile placeABombe){
		placerBombe(placeABombe);
		boolean result=false;
		List<AiTile> cibleSecure=new ArrayList<AiTile>();
		List<AiTile> scopes=new ArrayList<AiTile>();
		tilePossibleArriveAvecRisk(gameZone.getOwnHero().getTile(), cibleSecure, scopes, matriceImaginaire);
		try {
			tierLesTilesParDistanceEtAccebilite(cibleSecure);
			if(possibleDeFuir(cibleSecure, placeABombe)>=0){
				//oui,secure à poser a bombe dans le tile "placeABombe"
				//System.out.println("bomba koyup ka abiliriz");
				result=true;
			}else{
				//System.out.println("bomba koymayal m cunku guvenli yer yok");
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
