package org.totalboumboum.ai.v201011.ais.ozdokerozen.v5;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * GROUPE ROUGE
 * Lara �zd�ker && Sercan �zen
 * 
 * classe principale de l'IA, qui définit son comportement.
 * 
 * 
 * @author Lara Ozdoker
 * @author Sercan Ozen
 *
 */
public class OzdokerOzen extends ArtificialIntelligence
{	
	//pour l'aire
	AiZone gameZone;
	
	 //pour les sortie dans API
	private AiOutput ecran;
	
	//pour notre hero
	private AiHero notreHero;
	
	//pour nos cibles
	AiTile notreCible=null;
	AiTile notreCibleMur=null;
	AiTile notreCibleBonus=null;
	AiTile notreCibleEnemy=null;
	AiTile notreCibleEnemyFirstTile=null;
	
	/** les listes pour on va poser bombe */
	int notreCibleSize;
	List<AiTile> bomBom=new ArrayList<AiTile>();
	/** tiles pour fuir! */
	List<AiTile> tilesFuir=new ArrayList<AiTile>(); 
	/** tiles pour attaquer adversaire */
	List<AiTile> tilesAttaqueAdv =new ArrayList<AiTile>();
	/** l'index de notre cible dans cette list */
	int indexCibleAdversaire=0;
	
	//wait
	boolean wait=false;
	//risk
	boolean risk=false;
	
	/** longueur de la zone */
	int width;
	
	/** largeur de la zone */
	int height;
	
	/** la matrice de la zone */
	int[][] matrice;
	
	/** la tile de notre hero */
	AiTile debut;
	
	/** autorisations de poser bombe et deplacement */
	private boolean permissionPoseBombe=false;
	boolean attackAdversaire=false;
	boolean detruireMur=false;
	boolean collecteBonus=false;
	boolean arriverACible=false;
	
	/**Cost calculateur*/
	BasicCostCalculator cost;
	/**Heuristic calculateur*/
	HeuristicCalculator heuristic;
	
	/**on prend tile possible*/
	List<AiTile> tilesPossible;
	
	/**un object de class TileControleur*/
	TileControleur tileControleur;
	
	/** un object de class ChoisirMode*/
	ChoisirMode choisirMode;

	/** un object de class OutilesCollecte*/
	OutilesCollecte outilesCollecte;
	
	/**un object de class OutilesAttaque1 */
	OutilesAttaque1 outilesAttaque;
	
	/**un object de class OutilesMur */
	OutilesMur outilesMur;
	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	
		// avant tout : test d'interruption
		checkInterruption();		
		//on a reçu la zone du jeu
		gameZone = getPercepts();

		//on painte l'ecran
		ecran=getOutput();
		
		//pour l'inisilation
		AiAction result = new AiAction(AiActionName.NONE);
		
		width = gameZone.getWidth();
		height = gameZone.getHeight();

		// initialisation de notre hero dans cette zone
		notreHero = gameZone.getOwnHero();
		debut=notreHero.getTile();
		
		// la matrice de la zone
		matrice = new int[height][width];
		
		//Cost calculateur
		cost=new BasicCostCalculator();
		//Heuristic calculateur
		heuristic = new BasicHeuristicCalculator();
		
		// initialisation et calculation de matrice de la collecte avec des fonctions
		//de gorupe rouge l'annee dernier.
		//ATTETION: cettes initialitions ne sont pas compliqu�s
		//elles seulement donnent les valeurs à la matrice
		this.initialiseMatrice(matrice, gameZone);
		this.fillBombsMatrice(matrice, gameZone);
		this.fillBlocksMatrice(matrice, gameZone);
		this.fillFiresMatrice(matrice, gameZone);
		this.fillItemsBonus(matrice, gameZone);
		//this.fillHerosMatrice(matrice,gameZone);

		//les tiles qu'on peut aller
		tilesPossible =new ArrayList<AiTile>();
		
		//Pour initialisation pour les class
		choisirMode=new ChoisirMode(this);
		tileControleur=new TileControleur(OzdokerOzen.this, matrice);
		outilesAttaque=new OutilesAttaque1(this, gameZone, matrice);
		outilesCollecte=new OutilesCollecte(this, gameZone, matrice);
		outilesMur=new OutilesMur(this, matrice);
		
		//on cree les lists des tiles pour utiliser apres
		List<AiTile> scopes=new ArrayList<AiTile>();
		List<AiTile> passable=new ArrayList<AiTile>();
		AiPath pathAmeliorer=new AiPath();
		
		tileControleur.tilePossibleArriveAvecRisk(debut, tilesPossible, scopes, matrice);
		
		//on cree une liste qui include les tiles qu'on peux passe 
		for(int i=0;i<scopes.size();i++){
			checkInterruption();
			passable.add(scopes.get(i));
		}
		for(int i=0;i<tilesPossible.size();i++){
			checkInterruption();
			passable.add(tilesPossible.get(i));
		}
		if(!(passable.contains(notreHero.getTile())))
			passable.add(notreHero.getTile());


		//Est-ce qu'on pose une Bombe?
		if(tileControleur.estQueOnPoseBombe(debut)
				&& permissionPoseBombe
				&& (attackAdversaire||detruireMur)
				&& guvendemiyiz(matrice, notreHero)
				&& arriverACible
		){  //oui,on pose une bombe

			result = new AiAction(AiActionName.DROP_BOMB);
			permissionPoseBombe=false;
			arriverACible=false;
			if(!attackAdversaire){
				try{
				tilesFuir.add(tileControleur.tileBFS(debut));
				}catch (NullPointerException e) {
				}
			}
		}else{ //non, on ne la pose pas
			//on choisi le mode
			if(!choisirMode.modeChoisir()){	//EN COLLECTE MODE//////////////////////
				if(guvendemiyiz(matrice, notreHero)){ 
					risk=false;
					////on est dans cas sure
					if(outilesCollecte.existeBonus(gameZone)){	//System.out.println("1");
						//S'il y de bonus(pas hidden)
						if(tilesPossible.contains(outilesCollecte.bonusPlusProche(matrice, gameZone))){ //System.out.println("2");
							//Et S'il y a de path pour cette bonus 	
							detruireMur=false;
							collecteBonus=true;
							attackAdversaire=false;
							notreCibleBonus=outilesCollecte.bonusPlusProche(matrice, gameZone);
							//on calcule le path avec astar
							pathAmeliorer=tileControleur.calculateShortestPath(notreHero, debut, outilesCollecte.bonusPlusProche(matrice, gameZone));
							if(pathAmeliorer!=null){//on peinte
								for(int i=0;i<pathAmeliorer.getTiles().size();i++){
									checkInterruption();
									ecran.setTileColor(pathAmeliorer.getTiles().get(i), Color.BLUE);
								}			
							}
							result=collecteAction(pathAmeliorer,notreHero,matrice);
						}else{  //System.out.println("3");
							//S'il y n'y pas de path, on va ouvrir notre path,on va poser les bomb
							detruireMur=true;
							collecteBonus=false;
							attackAdversaire=false;
							AiTile bombMur=outilesAttaque.bombBomMur(outilesCollecte.bonusPlusProche(matrice, gameZone));
							AiTile bonusBonus=outilesCollecte.bonusArriveProche(matrice, gameZone);
							//on precise le mur
							if(bonusBonus!=null){
								notreCibleBonus=outilesCollecte.bonusArriveProche(matrice, gameZone);
								collecteBonus=true;
								detruireMur=false;
								pathAmeliorer=tileControleur.calculateShortestPath(notreHero, debut, notreCibleBonus);
							}else{	

							AiTile cibleMur=outilesMur.cibleAstar(bombMur, tilesPossible);
							//on calcule le path
							pathAmeliorer=tileControleur.calculateShortestPath(notreHero, debut, cibleMur);
							}
							if(pathAmeliorer!=null){ //on peinte
								for(int i=0;i<pathAmeliorer.getTiles().size();i++){
									checkInterruption();
									ecran.setTileColor(pathAmeliorer.getTiles().get(i), Color.YELLOW);
								}
							}
							result=collecteAction(pathAmeliorer,notreHero,matrice);
						}
					}else{	//System.out.println("5");
						//Detruitre mur pour chercher bonus
						detruireMur=true;
						collecteBonus=true;
						attackAdversaire=false;
						permissionPoseBombe=true;
						AiTile bombMur=outilesAttaque.bombBomMur( outilesAttaque.adversaireCible().getTile());
						//on precise le mur
						AiTile cibleDeAstar=outilesMur.cibleAstar(bombMur, tilesPossible);
						//on calcule le path
						pathAmeliorer=tileControleur.calculateShortestPath(notreHero, debut, cibleDeAstar);
						if(pathAmeliorer!=null){ //on peinte
							for(int i=0;i<pathAmeliorer.getTiles().size();i++){ 
								checkInterruption();
								ecran.setTileColor(pathAmeliorer.getTiles().get(i), Color.CYAN);
							}	
						}
						result=collecteAction(pathAmeliorer,notreHero,matrice);
					}
				}else{	//System.out.println("7");
					risk=true;
					detruireMur=false;
					collecteBonus=false;
					attackAdversaire=false;
					permissionPoseBombe=false;
					result=fuir();
				}
			}else{
				///////////////
				//ATAQUE MODE
				//////////////
				if(guvendemiyiz(matrice, notreHero)){	//System.out.println("8");
					risk=false;
					detruireMur=false;
					collecteBonus=false;
					attackAdversaire=true;
					permissionPoseBombe=true;
					tilesFuir=null;
					AiHero enemy=adversairePlusProche();
					if(enemy!=null){ //System.out.println("9");
						//On peut arriver à l'adversaire
						//Attaquer adversaire 
						
						//Si on doit calcule nouveaux cibles
						if(		tilesAttaqueAdv==null
								|| indexCibleAdversaire>=tilesAttaqueAdv.size() 
								|| tileControleur.calculerDistance(enemy.getTile(), debut)>tileControleur.calculerDistance(notreCibleEnemyFirstTile, debut)
								|| indexCibleAdversaire==0){ //System.out.println("9-A Blok");
							indexCibleAdversaire=0;
							tilesAttaqueAdv=new ArrayList<AiTile>();
							outilesAttaque.attackAlgorithm(enemy.getTile(),tilesAttaqueAdv);
							if(tilesAttaqueAdv.size()!=0){
								notreCibleEnemy=tilesAttaqueAdv.get(indexCibleAdversaire);
								notreCibleEnemyFirstTile=tilesAttaqueAdv.get(indexCibleAdversaire);
							}else
								notreCibleEnemy=null;
							pathAmeliorer=tileControleur.calculateShortestPath(notreHero, debut,notreCibleEnemy);
							indexCibleAdversaire++;
							result=attackAction(pathAmeliorer,notreHero,matrice);

						}else{ 	//System.out.println("9-B Blok");
							notreCibleEnemy=tilesAttaqueAdv.get(indexCibleAdversaire);
							pathAmeliorer=tileControleur.calculateShortestPath(notreHero, debut,notreCibleEnemy);
							indexCibleAdversaire++;
							result=attackAction(pathAmeliorer,notreHero,matrice);
						}
						
					
						
					}else{	//System.out.println("12");
						//on ne l'arrive pas
						risk=false;
						detruireMur=true;
						collecteBonus=false;
						if(notreHero.getBombNumberCurrent()>0){
							attackAdversaire=false;
						}else
							attackAdversaire=true;
						OutilesAttaque1 outilesAttaque=new OutilesAttaque1(this, gameZone, matrice);
						permissionPoseBombe=true;
						//detruitre mur
						AiHero enemy1=outilesAttaque.adversaireCible();
						AiTile mur=null;
						if(enemy1!=null){
							mur=outilesAttaque.bombBomMur(enemy1.getTile());
						}
						//System.out.println("MUR: "+mur);
	
						notreCibleMur=mur;
						AiTile bamBam=outilesMur.cibleAstar(mur, tilesPossible);
						pathAmeliorer=tileControleur.calculateShortestPath(notreHero, debut, bamBam);
						if(pathAmeliorer!=null){
							for(int i=0;i<pathAmeliorer.getTiles().size();i++){
								checkInterruption();
								ecran.setTileColor(pathAmeliorer.getTiles().get(i), Color.PINK);
							}	
						}
						result=attackAction(pathAmeliorer,notreHero,matrice);
					}
					
					
				}else{	//System.out.println("13");
					detruireMur=false;
					collecteBonus=false;
					attackAdversaire=true;
					permissionPoseBombe=false;
					result=fuir();
				}
				
			}
			
		}	
		return result;		
		
	}
	
	/**
	 * 
	 * @param matrice
	 * @param notreHero
	 * @return
	 * @throws StopRequestException
	 */
	private boolean guvendemiyiz(int[][] matrice,AiHero notreHero)throws StopRequestException{
		checkInterruption();
		if(matrice[notreHero.getLine()][notreHero.getCol()]<0)
			return false;
		else
			return true;
	}
	
	
	/**
	 * Methode initialisant notre matrice de zone avant la remplissage. Chaque
	 * case est initialement considere comme 1.
	 * 
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */	
	private void initialiseMatrice(int[][] matrice, org.totalboumboum.ai.v201011.adapter.data.AiZone gameZone)
	throws StopRequestException {
		checkInterruption();
		int height = gameZone.getHeight();
		int width = gameZone.getWidth();
		for (int i = 0; i < height; i++) {
			checkInterruption();
				for (int j = 0; j < width; j++) {
					checkInterruption();
					//butun alanlar� guvenli dusunuyoruz! 
					matrice[i][j] = 1;
				}
		}
	}
	
	/**
	* Methode remplissant les cases de notre matrice de zone possedant une
	 * bombe et les cases qui sont dans la portee de ces bombes. Ces nouveaux
	 * cases sont represente par -1.
	 * 
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	private void fillBombsMatrice(int[][] matrice,AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		List<AiBomb> bombs = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		while (iteratorBombs.hasNext()) {
			checkInterruption();
			AiBomb bomb = iteratorBombs.next();
			matrice[bomb.getLine()][bomb.getCol()] = -1;
			
			//pour les API, on a donné une color
			ecran.setTileColor(bomb.getLine(), bomb.getCol(), Color.CYAN);
			ecran.setTileText(bomb.getLine(), bomb.getCol(), "BOMBA");
			
			//la zone de dange
			List<AiTile> inScopeTiles = bomb.getBlast();
			for (int i = 1; i < inScopeTiles.size(); i++) {
				checkInterruption();
				matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()] = -3;
				
				ecran.setTileColor(inScopeTiles.get(i).getLine(),inScopeTiles.get(i).getCol(), Color.DARK_GRAY);
				
				ecran.setTileText(inScopeTiles.get(i),"SCOPE");
				
			}
		}
	}
	
	/**
	 * Methode remplissant les cases de notre matrice de zone possedant une mur.
	 * Les cases des mures non-destructibles sont representees par
	 * 0 et destructibles par 5.
	 * 
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	private void fillBlocksMatrice(int[][] matrice, AiZone gameZone)
	throws StopRequestException {
	checkInterruption();
	List<AiBlock> blocks = gameZone.getBlocks();
	Iterator<AiBlock> iteratorBlocks = blocks.iterator();
	while (iteratorBlocks.hasNext()) {
	checkInterruption();
	AiBlock block = iteratorBlocks.next();
	if (block.isDestructible()){
		matrice[block.getLine()][block.getCol()] = 5;
		//pour les API, on a donné une color
		ecran.setTileColor(block.getLine(), block.getCol(), Color.LIGHT_GRAY);}else{
			matrice[block.getLine()][block.getCol()] = 0;
		}
	}
}
	
	/**
	 * Methode remplissant les cases de notre matrice de zone possedant une
	 * flamme. Les cases des flammes sont represente par -2
	 * 
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	private void fillFiresMatrice(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		List<AiFire> fires = gameZone.getFires();
		Iterator<AiFire> iteratorFires = fires.iterator();
		while (iteratorFires.hasNext()) {
			checkInterruption();
			AiFire fire = iteratorFires.next();
			matrice[fire.getLine()][fire.getCol()] = -2;
			/**
			//pour les API, on a donné une color
			ecran.setTileColor(fire.getLine(), fire.getCol(), Color.white);
			ecran.setTileText(fire.getTile(), "BOMB!");
			*/
		}
	}

	/**
	 * Methode remplissant les cases de notre matrice de zone possedant une
	 * item. Les cases des items sont represente par 10.
	 * 
	 * @param matrice
	 *            La Matrice de Zone
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	private void fillItemsBonus(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		checkInterruption();
		List<AiItem> items = gameZone.getItems();
		Iterator<AiItem> iteratorItems = items.iterator();
		while (iteratorItems.hasNext()) {
			checkInterruption();
			AiItem item = iteratorItems.next();
			matrice[item.getLine()][item.getCol()] = 10;
			/**
			//pour les API, on a donné une color
			ecran.setTileColor(item.getLine(), item.getCol(), Color.CYAN);
			ecran.setTileText(item.getTile(), "BONUS!");
			*/
		}
	}
	
	/**
	 * Methode qui retuorne la duration de bombe
	 * @param tile
	 * @param matrice
	 * @param gameZone
	 * @return
	 * @throws StopRequestException 
	 */
	long bombTime(AiTile tile,int[][] matrice,AiZone gameZone) throws StopRequestException{
		checkInterruption();
		long time=0;
		int dx=tile.getCol();
		int dy=tile.getLine();
		List<AiBomb> bombs=gameZone.getBombs();
		for(int j=0;j<bombs.size();j++){
			this.checkInterruption();
			if(bombs.get(j).getTile().getLine()==dy || bombs.get(j).getTile().getCol()==dx){
				time=bombs.get(j).getExplosionDuration();
				break;
			}
		}

		return time;
	}
	
	
	private AiAction attackAction(AiPath nextMove,AiHero uneHero,int[][] matrice) throws StopRequestException {
		checkInterruption();
		
		//initilasition
		AiAction result = new AiAction(AiActionName.NONE);
		arriverACible=false;
		
		// deplacement sur l'abcisse
		int dx=0;
		// deplacement sur l'ordonne	
		double dy=0;	
		
		//hero cordinants
		//int hX=notreHero.getCol();
		//int hY=notreHero.getLine();
	
		//pour controler le path
		AiTile controlTile=null;
		
		//pour notre cible
		@SuppressWarnings("unused")
		AiTile cible=null;
		
		//notre bomb range
		//int bombRange=notreHero.getBombRange();

		//permsion de mouvement
		boolean check=false;
		
		//S'il n'y a pas de path pour deplacer
		if(nextMove==null){
			result = new AiAction(AiActionName.NONE);
			check=false;
			permissionPoseBombe=false;
			//System.out.println("Path Boù Boù BO�");

		}
		else{ //Oui il y a de path,mais est-ce que on est arrivé à notre cible? 
			if(nextMove.getTiles().size()==1){// oui, on l'est arrive
				permissionPoseBombe=true;
				arriverACible=true;
				check=false;
			}else{//Non, on n'est pas arrivé à notre cible
				cible=nextMove.getLastTile();
				//on prend le tile avant pour controler des dangeurs
				controlTile=nextMove.getTile(1);
				if(!risk && matrice[controlTile.getLine()][controlTile.getCol()]==tileControleur.SCOPE){
					//arreter car il y a des risk
					check=false;
				}else{
					//Alez-y
					if(attackAdversaire){//on deplace à notre adversaire 
						check=true;
						dx = (controlTile.getLine()) - (notreHero.getLine());
						dy = (controlTile.getCol()) - (notreHero.getCol());
					}else{//on deplace pour detruire le mur
						check=true;
						dx = (controlTile.getLine()) - (notreHero.getLine());
						dy = (controlTile.getCol()) - (notreHero.getCol());
					}
				}
			}		
		}
		
		// Determine la direction ou le hero va se deplacer.
			if (check) {
				//System.out.println("Q");
					if (dx < 0 && dy == 0) {
						result = new AiAction(AiActionName.MOVE, Direction.UP);
					} else if (dx < 0 && dy < 0) {
						result = new AiAction(AiActionName.MOVE, Direction.UPLEFT);
					} else if (dx == 0 && dy < 0) {
						result = new AiAction(AiActionName.MOVE, Direction.LEFT);
					} else if (dx > 0 && dy == 0) {
						result = new AiAction(AiActionName.MOVE, Direction.DOWN);
					} else if (dx > 0 && dy > 0) {
						result = new AiAction(AiActionName.MOVE, Direction.DOWNRIGHT);
					} else if (dx == 0 && dy > 0) {
						result = new AiAction(AiActionName.MOVE, Direction.RIGHT);
					} else if (dx > 0 && dy < 0) {
						result = new AiAction(AiActionName.MOVE, Direction.DOWNLEFT);
					} else if (dx < 0 && dy > 0) {
						result = new AiAction(AiActionName.MOVE, Direction.UPRIGHT);
					} else{
						result = new AiAction(AiActionName.NONE);
					}
			}			
			return result;		
	}
	
	private AiAction collecteAction(AiPath nextMove,AiHero uneHero,int[][] matrice) throws StopRequestException {
		checkInterruption();
		
		//initilasition
		AiAction result = new AiAction(AiActionName.NONE);
		arriverACible=false;
		
		// deplacement sur l'abcisse
		int dx=0;
		// deplacement sur l'ordonne	
		double dy=0;			
		//hero cordinants
		//int hX=notreHero.getCol();
		//int hY=notreHero.getLine();
		
		//pour controler le path
		AiTile controlTile=null;	
		//notre bomb range
		//int bombRange=notreHero.getBombRange();

		//permsion de mouvement
		boolean check=false;
		
		//S'il n'y a pas de path pour deplacer
		if(nextMove==null){
			result = new AiAction(AiActionName.NONE);
			check=false;
			permissionPoseBombe=false;
			//System.out.println("Path Boù Boù BO�");

		}
		else{ //Oui il y a de path,mais est-ce que on est arrivé à notre cible? 
			if(nextMove.getTiles().size()==1){// oui, on l'est arrive
				if(!collecteBonus){
					permissionPoseBombe=true;
				}
				arriverACible=true;
				check=false;
			}else{//Non, on n'est pas arrivé à notre cible
				@SuppressWarnings("unused")
				AiTile cible=nextMove.getLastTile();
				//on prend le tile avant pour controler des dangeurs
				controlTile=nextMove.getTile(1);
				if(!risk && matrice[controlTile.getLine()][controlTile.getCol()]==tileControleur.SCOPE){
					//arreter car il y a des risk
					//ge�er ge�mez hesaab� yap�lcak unutma///
					check=false;
				}else{
					//Alez-y
					if(collecteBonus){//on deplace à notre adversaire 
						check=true;
						dx = (controlTile.getLine()) - (notreHero.getLine());
						dy = (controlTile.getCol()) - (notreHero.getCol());
					}else{//on deplace pour detruire le mur
						check=true;
						dx = (controlTile.getLine()) - (notreHero.getLine());
						dy = (controlTile.getCol()) - (notreHero.getCol());
					}
				}
			}		
		}
		
		// Determine la direction ou le hero va se deplacer.
			if (check) {
				//System.out.println("Q");
					if (dx < 0 && dy == 0) {
						result = new AiAction(AiActionName.MOVE, Direction.UP);
					} else if (dx < 0 && dy < 0) {
						result = new AiAction(AiActionName.MOVE, Direction.UPLEFT);
					} else if (dx == 0 && dy < 0) {
						result = new AiAction(AiActionName.MOVE, Direction.LEFT);
					} else if (dx > 0 && dy == 0) {
						result = new AiAction(AiActionName.MOVE, Direction.DOWN);
					} else if (dx > 0 && dy > 0) {
						result = new AiAction(AiActionName.MOVE, Direction.DOWNRIGHT);
					} else if (dx == 0 && dy > 0) {
						result = new AiAction(AiActionName.MOVE, Direction.RIGHT);
					} else if (dx > 0 && dy < 0) {
						result = new AiAction(AiActionName.MOVE, Direction.DOWNLEFT);
					} else if (dx < 0 && dy > 0) {
						result = new AiAction(AiActionName.MOVE, Direction.UPRIGHT);
					} else{
						result = new AiAction(AiActionName.NONE);
					}
			}		
			return result;		
	}
	
	
	private AiAction fuirAction(AiPath nextMove,AiHero uneHero,int[][] matrice) throws StopRequestException {
		checkInterruption();
		
		//initilasition
		AiAction result = new AiAction(AiActionName.NONE);
		arriverACible=false;
		
		// deplacement sur l'abcisse
		int dx=0;
		// deplacement sur l'ordonne	
		double dy=0;	
		
		//pour notre cible
		@SuppressWarnings("unused")
		AiTile cible=null;
		
		//notre bomb range
		//int bombRange=notreHero.getBombRange();

		//permsion de mouvement
		boolean check=false;
		
		//S'il n'y a pas de path pour deplacer
		if(nextMove==null){
			result = new AiAction(AiActionName.NONE);
			check=false;
			permissionPoseBombe=false;
			//System.out.println("Path Boù Boù BO�");

		}
		else{ //Oui il y a de path,mais est-ce que on est arrivé à notre cible? 
			if(nextMove.getTiles().size()==1){// oui, on l'est arrive, on a fuir			
				permissionPoseBombe=false;			
				arriverACible=true;
				check=false;
			}else{//Non, on n'est pas arrivé à notre cible,Depecer!
				cible=nextMove.getLastTile();
				AiTile controlTile=nextMove.getTile(1);
				//on prend le tile avant pour controler des dangeurs
				//Alez-y
				check=true;
				dx = (controlTile.getLine()) - (notreHero.getLine());
				dy = (controlTile.getCol()) - (notreHero.getCol());								
			}		
		}
		
		// Determine la direction ou le hero va se deplacer.
			if (check) {
				//System.out.println("Q");
					if (dx < 0 && dy == 0) {
						result = new AiAction(AiActionName.MOVE, Direction.UP);
					} else if (dx < 0 && dy < 0) {
						result = new AiAction(AiActionName.MOVE, Direction.UPLEFT);
					} else if (dx == 0 && dy < 0) {
						result = new AiAction(AiActionName.MOVE, Direction.LEFT);
					} else if (dx > 0 && dy == 0) {
						result = new AiAction(AiActionName.MOVE, Direction.DOWN);
					} else if (dx > 0 && dy > 0) {
						result = new AiAction(AiActionName.MOVE, Direction.DOWNRIGHT);
					} else if (dx == 0 && dy > 0) {
						result = new AiAction(AiActionName.MOVE, Direction.RIGHT);
					} else if (dx > 0 && dy < 0) {
						result = new AiAction(AiActionName.MOVE, Direction.DOWNLEFT);
					} else if (dx < 0 && dy > 0) {
						result = new AiAction(AiActionName.MOVE, Direction.UPRIGHT);
					} else{
						result = new AiAction(AiActionName.NONE);
					}
			}			
			return result;
		
	}
	
	private AiAction fuir() throws StopRequestException{
		checkInterruption();
		risk=true;
		//System.out.println("Kacma fonksiyonu �al�été");
		permissionPoseBombe=false;		
		//on cree les lists des tiles pour utiliser apres
		List<AiTile> tilesAvecRisk=new ArrayList<AiTile>();
		List<AiTile> tilesPasseble=new ArrayList<AiTile>();
		List<AiTile> scopes=new ArrayList<AiTile>();	
		//on prend les tiles secures, mais cettes tiles avant qu'on pose bombe 
		tileControleur.tilePossibleArriveAvecRisk(debut, tilesAvecRisk, scopes, matrice);	
		//On cree une list tile qui on peut passer
		//pour �a, on ajouter le tile de notre hero(Si on est dessus le bomb,on n'est pas dans secure!)
		//Et tiles scopes,et tiles possible
		
		for(int i=0;i<scopes.size();i++){
			checkInterruption();
			tilesPasseble.add(scopes.get(i));
		}
		for(int j=0;j<tilesAvecRisk.size();j++){
			checkInterruption();
			tilesPasseble.add(tilesAvecRisk.get(j));
		}
		if(!(tilesPasseble.contains(notreHero.getTile())))
			tilesPasseble.add(notreHero.getTile());
		AiPath pathAmeliorer=new AiPath();		
		//on tie les tiles secures
		tileControleur.tierLesTilesParDistanceEtAccebilite(tilesAvecRisk);
		
		if(tilesFuir==null){
			if(tilesAvecRisk.size()==0){
				AiAction b=new AiAction(AiActionName.NONE);
				return b;
			}else{
			//on cree un path 
			pathAmeliorer=tileControleur.calculateShortestPath(notreHero, debut, tilesAvecRisk.get(0));
			p("g�venliyer",tilesAvecRisk.get(0));
			AiAction a=fuirAction(pathAmeliorer,notreHero,matrice);
			return a;}
		}else{
			if(tilesFuir.size()==0){
				AiAction b=new AiAction(AiActionName.NONE);
				return b;
			}else{
			pathAmeliorer=tileControleur.calculateShortestPath(notreHero, debut, tilesAvecRisk.get(0));
			AiAction a=fuirAction(pathAmeliorer,notreHero,matrice);
			return a;}
		}
	}
	
	
	public AiHero adversairePlusProche() throws StopRequestException{
		checkInterruption();

		List<AiHero> heros=gameZone.getHeroes();
		AiHero temp;
		for(int i=0;i<heros.size();i++){
			if(heros.get(i).equals(notreHero)){
				heros.remove(i);
				i--;
			}
			else if(!tilesPossible.contains(heros.get(i).getTile())){
				heros.remove(i);
				i--;
			}
			
		}

		if((heros.size())>1){
			for(int i=1;i<heros.size();i++){
				if(tileControleur.calculerDistance(heros.get(i-1).getTile())>tileControleur.calculerDistance(heros.get(i).getTile())){
					temp=heros.get(i-1);
					heros.remove(i-1);
					heros.add(i,temp);
				}
			}
		}
		if((heros.size())==0)
			return null;
		else
			return heros.get(0);
	}
	
	public void p(String title,AiTile a){
		//System.out.println(title+": "+a);
	}
}


