package org.totalboumboum.ai.v201011.ais.ozdokerozen.v2;

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
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;


/**
 * >> ce texte est à remplacer par votre propre description de votre IA
 * >> remplacez aussi le nom de l'auteur.
 * 
 * classe principale de l'IA, qui définit son comportement.
 * n'hésitez pas à décomposer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile à
 * débugger, modifier, relire, comprendre, etc.
 * 
 * @author Vincent Labatut
 *
 */
public class OzdokerOzen extends ArtificialIntelligence
{	
	 //pour les sortie dans API
	private AiOutput ecran;
	
	//pour notre hero
	private AiHero notreHero;
	
	//pour notre path finder
	private Astar finder;
	
	//pour notre cible
	AiTile notreCible=null;
	
	//wait
	boolean wait=false;
	//risk
	boolean risk=false;
	
	//pour notre path
	private AiPath notrepath;
	ChoisirMode choisirMode;
	
	//autorisation de poser bombe
	private boolean permissionPoseBombe=false;
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	
		// avant tout : test d'interruption
		checkInterruption();		
		

		
		//on a re�u la zone du jeu
		AiZone gameZone = getPercepts();
		//Pour choisir mode
		
		//on painte l'ecran
		ecran=getOutput();
		
		AiAction result = new AiAction(AiActionName.NONE);
		
		// la longueur de la zone
		int width = gameZone.getWidth();
		// la largeur de la zone
		int height = gameZone.getHeight();
		// System.out.println(width+"X"+height);

		// initialisation de notre hero dans cette zone
		this.notreHero = gameZone.getOwnHero();
		System.out.println("hero coordinant: "+notreHero.getLine()+","+notreHero.getCol());
		// la matrice de la zone
		int[][] matrice = new int[height][width];
		
		//Cost calculateur
		BasicCostCalculator cost=new BasicCostCalculator();
		//Heuristic calculateur
		HeuristicCalculator heuristic = new org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator();
		
		// initialisation et calculation de matrice de la collecte avec des fonctions
		//de gorupe rouge l'annee dernier
		this.initialiseMatrice(matrice, gameZone);
		this.fillBombsMatrice(matrice, gameZone);
		this.fillBlocksMatrice(matrice, gameZone);
		this.fillFiresMatrice(matrice, gameZone);
		this.fillItemsBonus(matrice, gameZone);
		//this.fillHerosMatrice(matrice,gameZone);
	

		/** pour tester /on va effacer apres la version marche bien du notre code
		// On commence a trouver si le hero est arrive au voisinage
		// d'une case mur.
		List<AiTile> neighboors = notreHero.getTile().getNeighbors();
		for (int i = 0; i < neighboors.size(); i++) {
			checkInterruption();
			ecran.setTileColor(neighboors.get(i), Color.BLUE);
		}
		*/
		
		choisirMode=new ChoisirMode(this);
		TileControleur tileControleur=new TileControleur(OzdokerOzen.this, matrice);
		//on choisi le mode
		if(!choisirMode.modeChoisir()){
			//EN COLLECTE MODE//////////////////////
			if(guvendemiyiz(matrice, notreHero)){
				System.out.println("guvendeyiz");
				OutilesCollecte outiles=new OutilesCollecte(this, gameZone, matrice);
				
				if(outiles.existeBonus(gameZone)){ //S'il y de bonus(pas hidden)
					/** Yeni versiyon ama yalan oldu
					 * 	List<AiTile> tile=outiles.trouveBonusPath(10, matrice,notreHero.getTile(), gameZone,notreHero.getTile(),notreHero.getTile());
					 
						AiPath bPath=null;
						System.out.println("Tiles : ");
						for(int i=0;i<tile.size();i++){
							bPath.addTile(tile.get(i));
							System.out.println(tile.get(i).getLine()+","+tile.get(i).getCol()+" - ");
						}
					System.out.println("A��kta bonus g�rdum");
					result=newAction2(bPath,notreHero,matrice);
					*/
					System.out.println("A��kta bonus g�rdum");
					
					try {
						if(outiles.existePath(gameZone, matrice, outiles.bonusPlusProche(matrice, gameZone))){ //Et S'il y a de path pour cette bonus 
							System.out.println("yolu a��k");
							AiPath rotate=tileControleur.calculateShortestPath(notreHero, notreHero.getTile(),outiles.bonusPlusProche(matrice, gameZone));
							result=newAction2(rotate,notreHero,matrice);
							System.out.println("A��ktaki bonusu almaya gidiyorum");
						}else{//S'il y n'y pas de path, on va ouvrir notre path,on va poser les bomb

							System.out.println("yolu kapal�");
							AiTile murDet=outiles.murQuOnVaDetruitre(outiles.bonusPlusProche(matrice, gameZone), matrice, gameZone);
							if(tileControleur.estQueOnPoseBombe(murDet)){
								AiPath rotate=tileControleur.calculateShortestPath(notreHero, notreHero.getTile(),murDet,matrice);
								result=newAction2(rotate,notreHero,matrice);
								System.out.println("yol buldum gidiyorum");
							}else{
								System.out.println("bomba koymayal�m");
								permissionPoseBombe=false;
								
							}
						}
					} catch (LimitReachedException e) {
						// 
						e.printStackTrace();
					}
				}else{
					//S'il n'y a pas de bonus,on va detruit les murs pour chercher de bonus

					System.out.println("Bonus yok duvar k�r�cam");
					AiTile tile=null;
					
					try {
						System.out.println("yak�n duvar� b�lduk mu?");

						tile=outiles.murPlusProche(matrice, gameZone);
						System.out.println("bulduk gidiyoruz");
						if(tileControleur.estQueOnPoseBombe(tile)){
	
							AiPath rotate=tileControleur.calculateShortestPath(notreHero, notreHero.getTile(),tile,matrice);
							System.out.println("Rotam�z�da bulduk");
	
							result=newAction2(rotate,notreHero,matrice);
						}else{
							System.out.println("bomba koymayal�m");
							permissionPoseBombe=false;
							
						}
					} catch (LimitReachedException e) {
						// 
						e.printStackTrace();
					}
					
				}
			}else{
				System.out.println("guvende de�iliz");
				try {
					System.out.println("ka�al�m");
					notrepath=guvenliAlanaKac(matrice, notreHero, gameZone);
					result=newAction2(notrepath, notreHero,matrice);
					System.out.println("Day����! biz kaéték day�!");
				} catch (LimitReachedException e) {
					// 
					e.printStackTrace();
				}
			}
		}else{
			System.out.println("attaque");
			//ataque mode
			if(guvendemiyiz(matrice, notreHero)){
			
				System.out.println("attaque");
				
				//On prend les adversaire dans l'aire de zone.
				List<AiHero> dusman=new ArrayList<AiHero>();
				for(int i=0;i<gameZone.getHeroes().size();i++){
					if(gameZone.getHeroes().get(i)!=gameZone.getOwnHero())
						dusman.add(gameZone.getHeroes().get(i));
				}
				System.out.println("Toplam d��man say�s�: "+dusman.size());
				
				//plusCourtPathAdversaire(dusman, notreHero);
				for(int i=0;i<dusman.size();i++){
					System.out.println(dusman.get(i).getLine()+","+dusman.get(i).getCol());
				}
				AiTile yolDusman=dusman.get(0).getTile();
				System.out.println("Hedef d��man: "+yolDusman.getLine()+","+yolDusman.getCol());
				
				//on controler à arriver a l'adversaire ou pas
				
				if(tileControleur.dfsPlaces(notreHero.getTile(),notreHero.getTile(),matrice).contains(yolDusman))
				{//possible de arriver
					
				
					finder=new Astar(this,notreHero,cost, heuristic);
					try {
						notrepath=finder.processShortestPath(notreHero.getTile(), yolDusman);
						//Case de poser bombe pour attaquer
						if(notreHero.getLine()==yolDusman.getLine() || notreHero.getCol()==yolDusman.getCol()){
							System.out.println("AYNI H�ZADAYIZ");
							if(notreHero.getBombRange()==Math.abs(notreHero.getLine()-yolDusman.getLine()) || notreHero.getBombRange()==Math.abs(notreHero.getCol()-yolDusman.getCol()))
									permissionPoseBombe=true;	
						}
						
						// pour controler
						System.out.println("path: "+notrepath.getLength());
						System.out.println("path 2"+notrepath.getTile(0).getLine()+","+notrepath.getTile(0).getCol());
						result=newAction2(notrepath,notreHero,matrice);
				
					} catch (LimitReachedException e) {
						// 
						e.printStackTrace();
					}
				}
				else{//pas de possible arriver adversaire 
					OutilesCollecte outiles=new OutilesCollecte(this, gameZone, matrice);

					System.out.println("yolu kapal�");
					AiTile murDet;
					try {
						murDet = outiles.murQuOnVaDetruitre(yolDusman, matrice, gameZone);
						if(tileControleur.estQueOnPoseBombe(murDet)){
							AiPath rotate=tileControleur.calculateShortestPath(notreHero, notreHero.getTile(),murDet,matrice);
							result=newAction2(rotate,notreHero,matrice);
							System.out.println("yol buldum gidiyorum");
						}else{
							System.out.println("bomba koymayal�m");
							permissionPoseBombe=false;
							
						}
					} catch (LimitReachedException e) {
						// 
						e.printStackTrace();
					}
					
				}			
			}
		else{
				System.out.println("attaque mode da fakat guvende de�iliz!");
				try {
					System.out.println("ka�al�m");
					notrepath=guvenliAlanaKac(matrice, notreHero, gameZone);
					result=newAction2(notrepath, notreHero,matrice);
					System.out.println("Day����! biz kaéték day�!");
				} catch (LimitReachedException e) {
					// 
					e.printStackTrace();
				}
			result=newAction(notrepath, notreHero);
		
			}
		}
		permissionPoseBombe=true;
		return result;
		
		
	}
	
	private boolean guvendemiyiz(int[][] matrice,AiHero notreHero)throws StopRequestException{
		checkInterruption();
		if(matrice[notreHero.getLine()][notreHero.getCol()]<0)
			return false;
		else
			return true;
	}
	
	private AiPath guvenliAlanaKac(int[][] matrice,AiHero notreHero,AiZone gameZone)throws StopRequestException, LimitReachedException{
		checkInterruption();
		AiPath yol=null;
		AiTile guvenliYer=yakinyerbul2(matrice, notreHero, gameZone);
		try {
			yol=calculateShortestPath(notreHero, notreHero.getTile(),guvenliYer);
			
		} catch (LimitReachedException e) {
			// 
			e.printStackTrace();
		}
		return yol;
	}
	
	private AiTile yakinyerbul2(int[][] matrice,AiHero notreHero,AiZone gameZone) throws StopRequestException, LimitReachedException{
		checkInterruption();
		AiTile result;
		TileControleur controler=new TileControleur(this,matrice);
		List<AiTile> surePlace=new ArrayList<AiTile>();
		surePlace=controler.dfsPlaces(notreHero.getTile(),notreHero.getTile(), matrice);
		result=hesapla(surePlace, matrice, gameZone);
		return result;
	}
	private AiTile hesapla(List<AiTile> surPlaces,int[][] matrice,AiZone gameZone){
		AiTile temp;
		TileControleur outiles=new TileControleur(this, matrice);
		for(int i=surPlaces.size();i>2;i--){
			for(int j=0;j<i-1;j++){
				if(outiles.calculerDistance(surPlaces.get(j))>outiles.calculerDistance(surPlaces.get(j+1))){
					temp=surPlaces.get(j+1);
					surPlaces.remove(j+1);
					surPlaces.add(j+1,surPlaces.get(j));
					surPlaces.remove(j);
					surPlaces.add(j,temp);
				}
			}
		}
		if(surPlaces.get(0)==null)
			return surPlaces.get(0);
		else
			return null;
	}
	
	/**
	private AiTile yakinyerbul(int[][] matrice,AiHero notreHero,AiZone gameZone) throws StopRequestException{
		checkInterruption();
		boolean flag=false;
		//position
		int Hx=notreHero.getCol();
		int Hy=notreHero.getLine();
		System.out.println("HX: "+Hx);
		System.out.println("HY: "+Hy);
		AiTile aim=null;
		AiPath way;
		int plusX,plusY;
		if(Hx+1<matrice.length)
			plusX=1;
		else
			plusX=0;
		if(Hy+1<matrice.length)
			plusY=1;
		else
			plusY=0;
		for(int i=Hx-1;i<=Hx+plusX;i--){
			checkInterruption();
			System.out.println("i"+i+",Hx+plusX:"+Hx+plusX);
			for(int j=Hy-1;i<=Hy+plusY;j--){
				checkInterruption();
				//System.out.println("j:"+j+",Hy+plusY:"+Hx+plusY);
				if(matrice[j][i]==1){
					aim=gameZone.getTile(j,i);
					System.out.println("Alan :"+aim.getCol()+","+aim.getLine());
					try {
						way=calculateShortestPath(notreHero,notreHero.getTile(),aim);
						if(!way.isEmpty()){
							System.out.println("Guvenli alan :"+aim.getCol()+","+aim.getLine());
							flag=true;
							break;
						}
					} catch (LimitReachedException e) {
						// 
						e.printStackTrace();
					}
				}
				if(Hy+plusY<matrice.length){
					plusY++;
				}else{
					plusY=plusY+0;
				}
				if(j==0){
					j=1;
				}
			}
			if(flag)
				break;
			if(i==0){
				i=1;
			}
			if(Hx+plusX<matrice.length){
				plusX++;
			}else{
				plusX=plusX+0;
			}
		}

		
		return aim;
	}
	*/
	/**
	 *Methode tie les path de court à longue
	 * @param lesPaths
	 * @param uneHero
	 * @throws StopRequestException
	 */
	@SuppressWarnings("unused")
	private void plusCourtPathAdversaire(List<AiHero> lesPaths,AiHero uneHero)throws StopRequestException{
		//variable temporiel
		AiHero temp;
		//Tri à bulles
		for(int i=lesPaths.size();i>2;i--){
			for(int j=0;j<i-1;j++){
				if(calculerDistance(lesPaths.get(j).getTile(),uneHero)>calculerDistance(lesPaths.get(j+1).getTile(),uneHero)){
					temp=lesPaths.get(j+1);
					lesPaths.remove(j+1);
					lesPaths.add(j+1,lesPaths.get(j));
					lesPaths.remove(j);
					lesPaths.add(j,temp);
				}
			}
		}
		lesPaths.remove(0);
	}
	
	/**
	 *Methode tie les path de court à longue
	 * @param lesPaths
	 * @param uneHero
	 * @throws StopRequestException
	
	private void plusCourtPathMur(List<AiBlock> lesPaths,AiHero uneHero)throws StopRequestException{
		//variable temporiel
		AiBlock temp;
		//Tri à bulles
		for(int i=lesPaths.size();i>2;i--){
			for(int j=0;j<i-1;j++){
				if(calculerDistance(lesPaths.get(j).getTile(),uneHero)>calculerDistance(lesPaths.get(j+1).getTile(),uneHero)){
					temp=lesPaths.get(j+1);
					lesPaths.remove(j+1);
					lesPaths.add(j+1,lesPaths.get(j));
					lesPaths.remove(j);
					lesPaths.add(j,temp);
				}
			}
		}
		
	}
	 */
	/**
	 * Methode calculant plus court distance entre hero et un mur destructible
	 * @param tile
	 * @param uneHero
	 * @return la distance
	 */		
	private int calculerDistance(AiTile tile,AiHero uneHero){
		//les coordinant de hero
		int heroX=uneHero.getCol();
		int heroY=uneHero.getLine();
		//les coordinant de cible
		int cibleX=tile.getCol();
		int cibleY=tile.getLine();
		int result;
		result=(Math.abs(heroX-cibleX))+(Math.abs(heroY-cibleY))-1;
		return result;
	}
	
	/**
	/**
	 * Methode voissant le bonus dans l'aire de jeu�
	 * @param gameZone
	 * 			l'aire de jeu
	 * @throws StopRequestException
	 * @return true
	 * 			s'il y'a de bonus ou pas
	 
	private boolean bonusAireDeJeu(AiZone gameZone)throws StopRequestException{
		List<AiItem> lesBonus=gameZone.getItems();
		if(lesBonus.isEmpty())
			//pas de bonus dans l'aire jeu
			return false;
		else 
			//il y a des bonus
			return true;
	}
	*/
	
	/**
	/**
	 *Methode disant que le mur destructible existe ou pas
	 *@param gameZone
	 *			L'aire de jeu
	 *@throws StopRequestException
	 *@return true s'il y a de mur destructible ou pas
	
	private boolean murDestructableDeJeu(AiZone gameZone)throws StopRequestException{
		List<AiBlock> lesMur=prendBlock(gameZone);
		if(lesMur.isEmpty()){
			//pas de mur destructible
			return false;
		}else{
			//il'y de mur destruc.
			return true;
		}
	}
	 */
	
	/**
	 * Methode prend les murs destructible
	 * @param gameZone
	 * @return
	 * @throws StopRequestException
	 
	private List<AiBlock> prendBlock(AiZone gameZone)throws StopRequestException{
		List<AiBlock> lesMur=gameZone.getBlocks();
		for(int i=0;i<lesMur.size();i++){
			checkInterruption();
			if(!(lesMur.get(i).isDestructible())){
				//on efface le mur undestructible
				lesMur.remove(i);
			}
		}
		return lesMur;
	}
	*/
	
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
			/**
			//pour les API, on a donn� une color
			ecran.setTileColor(bomb.getLine(), bomb.getCol(), Color.BLACK);
			ecran.setTileText(bomb.getLine(), bomb.getCol(), "BOMBA");
			*/
			//la zone de dange
			List<AiTile> inScopeTiles = bomb.getBlast();
			for (int i = 1; i < inScopeTiles.size(); i++) {
				checkInterruption();
				matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()] = -3;
				/**
				ecran.setTileColor(inScopeTiles.get(i).getLine(),inScopeTiles.get(i).getCol(), Color.DARK_GRAY);
				ecran.setTileText(inScopeTiles.get(i),"FIRES");
				*/
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
		ecran.setTileColor(block.getLine(), block.getCol(), Color.LIGHT_GRAY);}
	else
		matrice[block.getLine()][block.getCol()] = 0;
		ecran.setTileColor(block.getLine(), block.getCol(), Color.orange);
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
	
	
	public AiPath calculateShortestPath(AiHero ownHero, AiTile startPoint,AiTile endPoint) throws StopRequestException, LimitReachedException {
		checkInterruption();
		// le chemin le plus court possible
		AiPath shortestPath = null;
		// L'objet pour implementer l'algo A*
		Astar astar;
		// Calcul du cout par la classe de l'API
		CostCalculator cost = new BasicCostCalculator();
		// Calcul de l'heuristic par la classe de l'API
		HeuristicCalculator heuristic = new org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator();
		
		//---HATA----
		astar = new Astar(this, ownHero, cost, heuristic);
		shortestPath = astar.processShortestPath(startPoint, endPoint);
		//-------------

		return shortestPath;
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
	private AiAction newAction(AiPath nextMove,AiHero uneHero) throws StopRequestException {
		checkInterruption();
		// les cases suivant pour le deplacement.
		List<AiTile> tiles = nextMove.getTiles();
		notreCible=nextMove.getLastTile();
		// deplacement sur l'abcisse
		int dx;
		// deplacement sur l'ordonne
		double dy;		
		AiAction result = new AiAction(AiActionName.NONE);
		boolean check = true;
		//Si on a arrive notre cible
		if(notreCible.equals(uneHero.getTile())){
			System.out.println("//HEDEFE ULA�TIK//");
			check=false;
			if(permissionPoseBombe){
				System.out.println("!!!Bombay� koyal�m m�?");
				
				result=new AiAction(AiActionName.DROP_BOMB);
				permissionPoseBombe=false;
			}

		}		
		if(tiles.size()>1){
			dx = (tiles.get(1).getLine()) - (uneHero.getLine());
			// calcul de deplacement sur l'ordonne par rapport a la position de
			// l'hero et la premiere
			// case du chemin le plus court.
			dy = (tiles.get(1).getCol()) - (uneHero.getCol());
			check = true;
		
		if (tiles.get(0).getBlocks().size() != 0) {

			notreCible= null;
			check = false;
		}
		/**
		if (pathTiles.size() != 0)
			if (!pathTiles.contains(tiles.get(0))) {
				check = false;
				result = new AiAction(AiActionName.NONE);
			}
		*/
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
		}
		
		return result;
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
	private AiAction newAction2(AiPath nextMove,AiHero uneHero,int[][] matrice) throws StopRequestException {
		checkInterruption();
		TileControleur control=new TileControleur(this,matrice);
		// les cases suivant pour le deplacement.
		List<AiTile> tiles = nextMove.getTiles();
		notreCible=nextMove.getLastTile();
			
		// deplacement sur l'abcisse
		int dx;
		// deplacement sur l'ordonne
		double dy;		
		
		//initilasition
		AiAction result = new AiAction(AiActionName.NONE);
		boolean check = true;
		
		
		
			//Si on a arrive notre cible
			System.out.println("Cible de hero: "+notreCible.getLine()+","+notreCible.getCol());
			System.out.println("Tile de hero: "+uneHero.getLine()+","+uneHero.getCol());
			if(notreCible.equals(uneHero.getTile())){			
				System.out.println("//HEDEFE ULA�TIK//");
				check=false;	
				if(permissionPoseBombe){
					System.out.println("!!!Bombay� koyal�m m�?");
					if(control.estQueOnPoseBombe(notreCible)){
						System.out.println("koyuyoruz");
						result=new AiAction(AiActionName.DROP_BOMB);
						permissionPoseBombe=false;
						
						System.out.println("koyduk ka�acag�z");
					}else{
						System.out.println("!!Hay�r bomba koymuyoruz");
	
					}
				}else{
					matrice[notreCible.getLine()][notreCible.getCol()]=-1;
	
				}
	
			}
			//case poser bombe pour attaque algorithm 
	
			if(tiles.size()>1){
				dx = (tiles.get(1).getLine()) - (uneHero.getLine());
				// calcul de deplacement sur l'ordonne par rapport a la position de
				// l'hero et la premiere
				// case du chemin le plus court.
				dy = (tiles.get(1).getCol()) - (uneHero.getCol());
				check = true;
			
			if (tiles.get(0).getBlocks().size() != 0) {
	
				notreCible= null;
				check = false;
			}
			/**
			if (pathTiles.size() != 0)
				if (!pathTiles.contains(tiles.get(0))) {
					check = false;
					result = new AiAction(AiActionName.NONE);
				}
			*/
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
			}
		
		return result;
	}
	
	/**
	 * 
	 
	private void trouveUneBonus(AiZone gameZone,int[][] collecteMatrice)throws StopRequestException{
		List<AiItem> lesBonus=gameZone.getItems();
		List<AiPath> lesPathBonus = null;
		HeuristicCalculator HeuristicCalculator = null;
		CostCalculator CostCalculator = null;
		System.out.println("----LES_BONUS-----");
		Astar astar = new Astar(this,notreHero,CostCalculator,HeuristicCalculator);
		/**
		for(int i=0;i<lesBonus.size();i++){
			checkInterruption();
			System.out.println(i+" bonus: ["+lesBonus.get(i).getLine()+","+lesBonus.get(i).getCol()+"]");
			try {
				lesPathBonus.add(astar.processShortestPath(notreHero.getTile(), lesBonus.get(i).getTile()));
			} catch (LimitReachedException e) {
				// 
				e.printStackTrace();
			}
		}
		
		for(int i=0;i<lesPathBonus.size();i++){
			checkInterruption();
			for(int j=0;j<lesPathBonus.get(i).getLength();j++){
				checkInterruption();
				ecran.setTileColor(lesPathBonus.get(i).getTile(j), Color.CYAN);
				ecran.setTileText(lesPathBonus.get(i).getTile(j), "git");
			}
		}
		System.out.println("---------");
	}*/
}
