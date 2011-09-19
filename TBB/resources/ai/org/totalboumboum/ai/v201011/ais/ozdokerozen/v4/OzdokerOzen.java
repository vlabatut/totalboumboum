package org.totalboumboum.ai.v201011.ais.ozdokerozen.v4;

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
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
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
	
	//pour notre path finder
	@SuppressWarnings("unused")
	private Astar finder;
	
	//pour notre cible
	AiTile notreCible=null;
	
	//wait
	boolean wait=false;
	//risk
	boolean risk=false;
	
	//pour notre path
	@SuppressWarnings("unused")
	private AiPath notrepath;
	ChoisirMode choisirMode;
	
	// longueur de la zone
	int width;
	
	// largeur de la zone
	int height;
	
	// la matrice de la zone
	int[][] matrice;
	
	//la tile de notre hero
	AiTile debut;
	
	//autorisation de poser bombe
	private boolean permissionPoseBombe=false;
	/**Cost calculateur*/
	BasicCostCalculator cost;
	/**Heuristic calculateur*/
	HeuristicCalculator heuristic;
	
	/**on prend tile possible*/
	List<AiTile> tilesPossible;
	
	/**les tiles qu'on peut aller*/
	TileControleur tileControleur;
	
	/**
	 * booleans values pour permision de deplacement
	 */
	boolean attackAdversaire=false;
	boolean detruireMur=false;
	boolean collecteBonus=false;
	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	
		// avant tout : test d'interruption
		checkInterruption();		
		//on a re�u la zone du jeu
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
		//de gorupe rouge l'annee dernier
		this.initialiseMatrice(matrice, gameZone);
		this.fillBombsMatrice(matrice, gameZone);
		this.fillBlocksMatrice(matrice, gameZone);
		this.fillFiresMatrice(matrice, gameZone);
		this.fillItemsBonus(matrice, gameZone);
		//this.fillHerosMatrice(matrice,gameZone);

		//les tiles qu'on peut aller
		tilesPossible =new ArrayList<AiTile>();
		
		//Pour choisir mode
		choisirMode=new ChoisirMode(this);
		tileControleur=new TileControleur(OzdokerOzen.this, matrice);
		
		//on cree les lists des tiles pour utiliser apres
		List<AiTile> scopes=new ArrayList<AiTile>();
		List<AiTile> passable=new ArrayList<AiTile>();
		List<AiTile> dejavu=new ArrayList<AiTile>();
		List<AiTile> pathDFS=new ArrayList<AiTile>();
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
		
		
		//on choisi le mode
		if(!choisirMode.modeChoisir()){
			////////////////////////////////////////
			//EN COLLECTE MODE//////////////////////
			//////////////////////////////////////////
			if(guvendemiyiz(matrice, notreHero)){
				////on est dans cas sure
				//System.out.println("COLLECTE MODE-(guvenli)!");
				OutilesCollecte outiles=new OutilesCollecte(this, gameZone, matrice);
				
				if(outiles.existeBonus(gameZone)){
					//S'il y de bonus(pas hidden)
					if(tilesPossible.contains(outiles.bonusPlusProche(matrice, gameZone))){ 
						//Et S'il y a de path pour cette bonus 						
						detruireMur=false;
						collecteBonus=true;
						attackAdversaire=false;
						permissionPoseBombe=false;

						tileControleur.pathFinder(debut, outiles.bonusPlusProche(matrice, gameZone), dejavu, pathDFS, passable);
						tileControleur.ameliorePath(pathDFS, pathAmeliorer, outiles.bonusPlusProche(matrice, gameZone));
						
						//on peinte
						for(int i=0;i<pathAmeliorer.getTiles().size();i++){
							checkInterruption();
							ecran.setTileColor(pathAmeliorer.getTiles().get(i), Color.BLUE);
						}			
						
						result=newAction2(pathAmeliorer,notreHero,matrice);
						
					}else{
						//S'il y n'y pas de path, on va ouvrir notre path,on va poser les bomb
						if(notreHero.getBombNumberCurrent()>0){
							detruireMur=false;
						}else
							detruireMur=true;

						collecteBonus=true;
						attackAdversaire=false;
						permissionPoseBombe=true;
						//System.out.println("Yol a�mak i�in bomba koymal�y�m");
						tileControleur.pathFinder(debut, outiles.murPlusProche(matrice, gameZone), dejavu, pathDFS, passable);
						//System.out.println("Collecte BONUS DUVAR: "+outiles.murPlusProche(matrice, gameZone));
						tileControleur.ameliorePath(pathDFS, pathAmeliorer, outiles.murPlusProche(matrice, gameZone));
						
						for(int i=0;i<pathAmeliorer.getTiles().size();i++){
							checkInterruption();
							ecran.setTileColor(pathAmeliorer.getTiles().get(i), Color.YELLOW);
						}	
						result=newAction2(pathAmeliorer,notreHero,matrice);
					}
				}else{
					//Detruitre bombe pour posage bombe
					if(notreHero.getBombNumberCurrent()>0){
						detruireMur=false;
					}else
						detruireMur=true;
					collecteBonus=true;
					attackAdversaire=false;
					permissionPoseBombe=true;
					tileControleur.pathFinder(debut, outiles.murPlusProche(matrice, gameZone), dejavu, pathDFS, passable);
					//System.out.println("Collecte DUVAR DUVAR: "+outiles.murPlusProche(matrice, gameZone));
					tileControleur.ameliorePath(pathDFS, pathAmeliorer, outiles.murPlusProche(matrice, gameZone));
					for(int i=0;i<pathAmeliorer.getTiles().size();i++){
						checkInterruption();
						ecran.setTileColor(pathAmeliorer.getTiles().get(i), Color.ORANGE);
					}	
					result=newAction2(pathAmeliorer,notreHero,matrice);
				}
			}else{
				detruireMur=false;
				collecteBonus=true;
				attackAdversaire=false;
				permissionPoseBombe=false;
				result=fuir();
			}
		}else{
			OutilesCollecte outiles=new OutilesCollecte(this, gameZone, matrice);
			///////////////
			//ATAQUE MODE
			//////////////
			if(guvendemiyiz(matrice, notreHero)){
				detruireMur=false;
				collecteBonus=false;
				attackAdversaire=true;
				permissionPoseBombe=true;
				AiHero enemy=adversairePlusProche();
				if(enemy!=null){
					//Attaquer adversaire 
					tileControleur.pathFinder(debut, enemy.getTile(), dejavu, pathDFS, passable);
					tileControleur.ameliorePath(pathDFS, pathAmeliorer, enemy.getTile());
					for(int i=0;i<pathAmeliorer.getTiles().size();i++){
						checkInterruption();
						ecran.setTileColor(pathAmeliorer.getTiles().get(i), Color.RED);
					}	
					result=newAction2(pathAmeliorer,notreHero,matrice);
				}else{
					
					detruireMur=false;
					collecteBonus=false;
				if(notreHero.getBombNumberCurrent()>0){
					attackAdversaire=false;
				}else
					attackAdversaire=true;
					permissionPoseBombe=true;
					//detruitre mur
					AiTile mur=outiles.murPlusProche(matrice, gameZone);
					tileControleur.pathFinder(debut, mur, dejavu, pathDFS, passable);
					tileControleur.ameliorePath(pathDFS, pathAmeliorer, mur);
					for(int i=0;i<pathDFS.size();i++){
						checkInterruption();
						pathAmeliorer.addTile(pathDFS.get(i));
						ecran.setTileColor(pathDFS.get(i), Color.PINK);
					}	
					result=newAction2(pathAmeliorer,notreHero,matrice);
					
				}
				
				
			}else{
				detruireMur=false;
				collecteBonus=false;
				attackAdversaire=true;
				permissionPoseBombe=false;
				result=fuir();
			}
			
		}
		return result;
		
		
	}
	
	
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
			
			//pour les API, on a donn� une color
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
		//pour les API, on a donn� une color
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
			//pour les API, on a donn� une color
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
			//pour les API, on a donn� une color
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
	
	/**
	 * Methode calculant la nouvelle action a effectuer
	 * (methode de groupe rouge de l'annee dernier mais on l'a modifié)
	 * @param nextMove
	 * 			Le chemin precis a suivre.
	 *  
	 * @return la nouvelle action de notre hero dans ce chemin
	 * 
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private AiAction newAction1v2(AiPath nextMove,AiHero uneHero,int[][] matrice) throws StopRequestException {
		checkInterruption();		
		//initilasition
		AiAction result = new AiAction(AiActionName.NONE);
		TileControleur control=new TileControleur(this,matrice);
		// deplacement sur l'abcisse
		int dx=0;
		// deplacement sur l'ordonne
		double dy=0;	
		boolean check=false;
		//Il n'y a pas de path pour deplacer
		if(nextMove==null){
			result = new AiAction(AiActionName.NONE);
			check=false;

		}
		else{ // les cases suivant pour le deplacement.
			//on prend les tiles de path
			List<AiTile> tiles = nextMove.getTiles();
			System.out.println("---Path---");
			for(int j=0;j<tiles.size();j++){
				System.out.println(tiles.get(j));
			}
			System.out.println("---///***/////---");
			//on precise notre cible
			notreCible=nextMove.getLastTile();
			//case poser bombe pour attaque algorithm 
			if(tiles.size()>1){
				if(tiles.size()==2){
					if(permissionPoseBombe){
						check=false;
						System.out.println("galatasary");
						result = new AiAction(AiActionName.DROP_BOMB);
					}
				}
				dx = (tiles.get(1).getLine()) - (uneHero.getLine());
				// calcul de deplacement sur l'ordonne par rapport a la position de
				// l'hero et la premiere
				// case du chemin le plus court.
				dy = (tiles.get(1).getCol()) - (uneHero.getCol());
				
				if(matrice[tiles.get(1).getLine()][tiles.get(1).getCol()]==control.FIRE){//bir sonraki tile'da Ate� varsa dural�m
					result = new AiAction(AiActionName.NONE);
					check=false;
					}
				else if(matrice[tiles.get(1).getLine()][tiles.get(1).getCol()]==control.SCOPE){
					AiPath pathControl=new AiPath();
					pathControl.addTile(notreHero.getTile());
					pathControl.addTile(tiles.get(1));
					long duration=pathControl.getDuration(notreHero);
					if(bombTime(tiles.get(1),matrice,gameZone)>duration){
						check=false;
						result = new AiAction(AiActionName.NONE);
					}else{
						check=true;
					}
				}
				else if(matrice[tiles.get(1).getLine()][tiles.get(1).getCol()]==control.SECURE || matrice[tiles.get(1).getLine()][tiles.get(1).getCol()]==control.BONUS){
					check=true;
					}
				}else{
					if(permissionPoseBombe){
						check=false;
						result = new AiAction(AiActionName.DROP_BOMB);
					}
				}
			
			}

			

			
				// Determine la direction ou le hero va se deplacer.
				if (check) {
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
					} else {
						result = new AiAction(AiActionName.NONE);
					}
				}
				
				
			return result;
		
	}
	

	@SuppressWarnings("unused")
	private AiAction newAction2v3(AiPath nextMove,AiHero uneHero,int[][] matrice) throws StopRequestException {
		checkInterruption();		
		//initilasition
		AiAction result = new AiAction(AiActionName.NONE);
		// deplacement sur l'abcisse
		int dx=0;
		// deplacement sur l'ordonne
		double dy=0;	
		
		int bombRange=notreHero.getBombRange();
		boolean check=false;
		
		//Il n'y a pas de path pour deplacer
		if(nextMove==null){
			result = new AiAction(AiActionName.NONE);
			check=false;
			System.out.println("Burdam�y�z?");
			permissionPoseBombe=false;
		}
		else{
			// les cases suivant pour le deplacement.
			//on prend les tiles de path
			List<AiTile> tiles = nextMove.getTiles();
			
			//hero cordinant
			int hX=notreHero.getCol();
			int hY=notreHero.getLine();
			//cible coordinant
			int cX=0,cY=0;
			
			//path'i yazd�r�yoruz!
			System.out.println("---Path---");
			for(int j=0;j<tiles.size();j++){
				checkInterruption();
				System.out.println(tiles.get(j));
			}
			System.out.println("---///***/////---");
			
			if(detruireMur){
				notreCible=tiles.get(tiles.size()-1);
			}else{
				notreCible=nextMove.getLastTile();

			}
			//on precise notre cible
			cX=notreCible.getCol();
			cY=notreCible.getLine();
			
			if(tiles.size()>1){
				System.out.println("Path > 1 den!!!");
				if(matrice[tiles.get(1).getLine()][tiles.get(1).getCol()]<0 && !risk){//bir sonraki tiles risk li ise beklerriz (riske girmemiz gerekmiyorsa)
					System.out.println("Bekliyoruz Risk var agam");
					result=new AiAction(AiActionName.NONE);
				}else if(attackAdversaire && (hX==cX||hY==cY)){ //de�ilse, e�er hedefle hizalanm��sak bomba range'imize g�re bomba koyar�z
					if(hX==cX){
						if(Math.abs(hY-cY)<=bombRange){
							if(tileControleur.estQueOnPoseBombe(notreHero.getTile()) && permissionPoseBombe)
								result = new AiAction(AiActionName.DROP_BOMB);
							else
								check=true;
						}
					}else if(hY==cY){
						if(Math.abs(hX-cX)<=bombRange){
							if(tileControleur.estQueOnPoseBombe(notreHero.getTile()) && permissionPoseBombe)
								result = new AiAction(AiActionName.DROP_BOMB);
							else
								check=true;
							}
					}
					if(permissionPoseBombe){
						result = new AiAction(AiActionName.DROP_BOMB);
					}
				}else{
					//di�er hareketlerden yapacaksak buraya eklenecek durumlar var
					System.out.println("Ba�ka �eyler yapaca��m!Y�r�yece�im!");
					check=true;				
					//on precise la direction
					dx = (tiles.get(1).getLine()) - (notreHero.getLine());
					// calcul de deplacement sur l'ordonne par rapport a la position de
					// l'hero et la premiere
					// case du chemin le plus court.
					dy = (tiles.get(1).getCol()) - (notreHero.getCol());
					/**
					if(matrice[tiles.get(1).getLine()][tiles.get(1).getCol()]==control.SCOPE){
						AiPath pathControl=new AiPath();
						pathControl.addTile(notreHero.getTile());
						pathControl.addTile(tiles.get(1));
						long duration=pathControl.getDuration(notreHero);
						if(bombTime(tiles.get(1),matrice,gameZone)>duration){
							check=false;
							result = new AiAction(AiActionName.NONE);
						}else{
							check=true;
						}
					}
					else if(matrice[tiles.get(1).getLine()][tiles.get(1).getCol()]==control.SECURE || matrice[tiles.get(1).getLine()][tiles.get(1).getCol()]==control.BONUS){
						check=true;
						}
					*/
				}
			}else{
				if(tileControleur.estQueOnPoseBombe(notreHero.getTile()) && permissionPoseBombe){
					System.out.println("Be�ika�");
					check=false;
					result = new AiAction(AiActionName.DROP_BOMB);
				}else
					check=true;
				}
			
			
			}
		
				// Determine la direction ou le hero va se deplacer.
				if (check) {
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
					} 
				}
				
				
			return result;
		
	}
	
	private AiAction newAction2(AiPath nextMove,AiHero uneHero,int[][] matrice) throws StopRequestException {
		checkInterruption();		
		//initilasition
		AiAction result = new AiAction(AiActionName.NONE);
		// deplacement sur l'abcisse
		int dx=0;
		// deplacement sur l'ordonne
		double dy=0;	
		//hero cordinant
		int hX=notreHero.getCol();
		int hY=notreHero.getLine();
		//cible coordinant
		int cX=0,cY=0;
		notreCible=nextMove.getLastTile();

		int bombRange=notreHero.getBombRange();
		boolean check=false;
		
		//Il n'y a pas de path pour deplacer
		if(nextMove.getTiles().size()==0){
			result = new AiAction(AiActionName.NONE);
			check=false;
			permissionPoseBombe=false;
		}
		else{
			List<AiTile> tiles = nextMove.getTiles();
			
			cX=notreCible.getCol();
			cY=notreCible.getLine();
			dx = (tiles.get(1).getLine()) - (notreHero.getLine());
			// calcul de deplacement sur l'ordonne par rapport a la position de
			// l'hero et la premiere
			// case du chemin le plus court.
			dy = (tiles.get(1).getCol()) - (notreHero.getCol());			
			
			if(collecteBonus){ //Mouvement pour collecette mode
				if(detruireMur){
					if(hX==cX||hY==cY){ //Posage bombe
						//System.out.println("A");
						if(hX==cX){
							//System.out.println("B");
							if(Math.abs(hY-cY)<=bombRange){
								//System.out.println("C");
								if(tileControleur.estQueOnPoseBombe(notreHero.getTile()) && permissionPoseBombe)
									result = new AiAction(AiActionName.DROP_BOMB);
								else
									check=true;
									
							}
						}else{
							if(Math.abs(hX-cX)<=bombRange){
								//System.out.println("D");
								if(tileControleur.estQueOnPoseBombe(notreHero.getTile()) && permissionPoseBombe)
									result = new AiAction(AiActionName.DROP_BOMB);
								else
									check=true;
									
								}
						}
					}else{

						//System.out.println("F");
						check=true;
					}
				}else{
					//System.out.println("G");
					//Deplacement
					check=true;									
				}
			}else if(attackAdversaire){ //Mouvement pour attaque mode
				//System.out.println("H");

				if(attackAdversaire){
					//System.out.println("I");

					//Poasge bombe
					if(hX==cX||hY==cY){ //Posage bombe
						//System.out.println("J");
						if(hX==cX){
							//System.out.println("K");
							if(Math.abs(hY-cY)<=bombRange){
								//System.out.println("L");
								if(tileControleur.estQueOnPoseBombe(notreHero.getTile()) && permissionPoseBombe)
									result = new AiAction(AiActionName.DROP_BOMB);
								else
									check=true;
							}
						}else{
							//System.out.println("M");
							if(Math.abs(hX-cX)<=bombRange){
								//System.out.println("N");
								if(tileControleur.estQueOnPoseBombe(notreHero.getTile()) && permissionPoseBombe)
									result = new AiAction(AiActionName.DROP_BOMB);
								else
									check=true;

								}
						}
					}else{
						//System.out.println("O");
						check=true;
					}
				}else{
					//System.out.println("P");
					//Deplacement
					//Deplacement
					check=true;				
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
	
	private AiAction fuir() throws StopRequestException{
		checkInterruption();
		risk=true;
		//System.out.println("Kacma fonksiyonu �al�été");
		permissionPoseBombe=false;
		
		//on cree les lists des tiles pour utiliser apres
		List<AiTile> tilesAvecRisk=new ArrayList<AiTile>();
		List<AiTile> tilesPasseble=new ArrayList<AiTile>();
		List<AiTile> scopes=new ArrayList<AiTile>();
		
		//on prend les tiles secures
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


		//pour autres choses
		List<AiTile> dejavu=new ArrayList<AiTile>();
		List<AiTile> pathDFS=new ArrayList<AiTile>();
		AiPath pathAmeliorer=new AiPath();
		
		//on tie les tiles secures
		tileControleur.tierLesTilesParDistanceEtAccebilite(tilesAvecRisk);
				
		if(tilesAvecRisk.size()==0){
			AiAction b=new AiAction(AiActionName.NONE);
			return b;
		}else{
		//on cree un path d'apres algorithme de DFS 
		tileControleur.pathFinder(debut, tilesAvecRisk.get(0), dejavu, pathDFS, tilesPasseble);
		//on ameliorer le path
		tileControleur.ameliorePath(pathDFS, pathAmeliorer, tilesAvecRisk.get(0));
				
		AiAction a=newAction2(pathAmeliorer,notreHero,matrice);
		return a;}
	}
	
	public AiHero adversairePlusProche(){
		List<AiHero> heros=gameZone.getHeroes();
		AiHero temp;
		for(int i=0;i<heros.size();i++){
			if(heros.get(i).equals(notreHero)){
				heros.remove(i);
				i--;
			}else if(!tilesPossible.contains(heros.get(i))){
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
}

 
