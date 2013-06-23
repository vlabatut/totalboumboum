package org.totalboumboum.ai.v201011.ais.kesimalvarol.v6;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.AstarNode;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
//import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
//import org.totalboumboum.ai.v201011.adapter.path.astar.cost.MatrixCostCalculator;
//import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
//import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.PixelHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.successor.SuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Gestion de deplacement.
 * 
 * @author Ali Baran Kesimal
 * @author Işıl Varol
 *
 */
public class MovementCommitter {

	/**Pour checkInterruption.*/
	private static KesimalVarol monIA;
	public static void setMonIA(KesimalVarol monIA) throws StopRequestException {
		monIA.checkInterruption();
		MovementCommitter.monIA = monIA;
		PathSafetyDeterminators.setMonIA(monIA);
		FastWallDestruction.setMonIA(monIA);
	}
	
	/** Chemin a suivre */
	private AiPath pathToFollow;
	
	/** Utilisee pour indiquer qu'on voudrait faire une operation distincte pour poser des bombes */
	private AiTile bombSpecialRequest;
	public AiTile getBombSpecialRequest() throws StopRequestException {
		monIA.checkInterruption();
		return bombSpecialRequest;
	}
	public void setBombSpecialRequest(AiTile bombSpecialRequest) throws StopRequestException {
		monIA.checkInterruption();
		this.bombSpecialRequest = bombSpecialRequest;
	}
	
	/**
	 * On n'etait pas sur s'il y avait un bug dans A* et on n'avait pas trop du temps pour finaliser l'IA, donc,
	 * on avait developpée notre version de fonction successeur, qui est, en effet, une altération de BasicSuccessorCalculator.
	 * 
	 * Bien qu'on sache que chacune des noeuds representent un etat distincte et pas seulement une "case", on avait observee que la recherche nous prenait une temps
	 * absolument intolerable : Avant d'avoir implementé celle-ci, on pouvait observer un temps de calcul d'environ 15-20 secondes pour trouver un chemin vers une case 
	 * complétement réguliere, notamment s'il existait des bombes ou des flammes. Maintenant, on ne voit aucune delai de calcul.
	 * 
	 * La difference est qu'on ne permette pas de chercher les cases auquels on avait arrivee déja par la case actuelle. On veut que A* nous renvoie seulement une chemin ; 
	 * les significations des "états" nous empechent de faire un calcul vite. On sait que celle-ci peut détériorer l'éxecution des autres types de recherche, mais on avait 
	 * vu une amélioration enorme pour notre agent.
	 * 
	 * par exemple, si la noeud courant symbolise un état de la case (10;10), on va chercher ses voisines qu'on peut transgresser.
	 * soit (9;10) l'une des telle voisines. Dans les iterations suivantes, si on arrive a (10;10) une deuxieme fois, on ne permettra pas de developper vers (9;10).
	 * 
	 */
	private class KesimalVarolSuccessor extends SuccessorCalculator
	{
		private HashMap<AiTile, ArrayList<AiTile> > discoveredByCases;
		private Matrix m;
		@Override
		public List<AiTile> processSuccessors(AstarNode node) throws StopRequestException {
			monIA.checkInterruption();
			List<AiTile> result = new ArrayList<AiTile>();
		
			if (m.representation[node.getTile().getLine()][node.getTile().getCol()]>-300 &&
					node.getDepth()<(monIA.getZone().getHeight()+monIA.getZone().getWidth())*2)
			{
				if(discoveredByCases.get(node.getTile())==null)
				{
					discoveredByCases.put(node.getTile(),new ArrayList<AiTile>());
				}
				ArrayList<AiTile> currTileList=discoveredByCases.get(node.getTile());
				AiTile tile = node.getTile();
				AiHero hero = node.getHero();
				
				// pour chaque case voisine : on la rajoute si elle est traversable
				for(Direction direction: Direction.getPrimaryValues())
				{	
					monIA.checkInterruption();
					AiTile neighbor = tile.getNeighbor(direction);
					if(!currTileList.contains(neighbor))
					{
						if((node.getParent()==null || neighbor!=node.getParent().getTile()) && neighbor.isCrossableBy(hero))
						{
							result.add(neighbor);			
							currTileList.add(neighbor);
						}
					}
				}
			}	
			return result;
		}
		/**
		 * Constructeur.
		 * @param m
		 * @throws StopRequestException
		 */
		public KesimalVarolSuccessor(Matrix m) throws StopRequestException
		{
			monIA.checkInterruption();
			this.m=m;
		}
		/**
		 * Methode utilisee pour (re)initialiser la fonction
		 * @throws StopRequestException
		 */
		public void initNodeHack() throws StopRequestException
		{
			monIA.checkInterruption();
			discoveredByCases=new HashMap<AiTile, ArrayList<AiTile>>();
		}
	}
	
	/**
	 * Methode invoquee pour determiner l'action suivante, explicitement pour prevoir une bombe
	 * 
	 * @param m La matrice sur laquelle nos calculs se baseront
	 * 
	 * @return L'action a utiliser
	 */
	public AiAction commitMovementForFutureBomb(Matrix m) throws StopRequestException
	{
		monIA.checkInterruption();
		return commitMovement(m,true);
	}
	
	/**
	 * Methode invoquee pour determiner l'action suivante
	 * 
	 * @param m La matrice sur laquelle nos calculs se baseront
	 * 
	 * @return L'action a utiliser
	 */
	public AiAction commitMovement(Matrix m) throws StopRequestException
	{
		monIA.checkInterruption();
		return commitMovement(m,false);
	}
	
	/**
	 * Methode invoquee pour determiner l'action suivante
	 * 
	 * @param m La matrice sur laquelle nos calculs se baseront
	 * @param invokedForSafeBombPlacement Oui si commitMovement est appelee pour prevoir si on peut laisser une bombe ou non
	 * 
	 * @return L'action a utiliser
	 */
	public AiAction commitMovement(Matrix m,boolean invokedForSafeBombPlacement) throws StopRequestException
	{
		monIA.checkInterruption();
		double[][] costMatrix=new double[m.height][m.width];
		for(int i=0;i<m.height;i++)
		{
			monIA.checkInterruption();
			for(int j=0;j<m.width;j++)
			{
				monIA.checkInterruption();
				costMatrix[i][j]=-m.representation[i][j];//300-m.representation[i][j];
			}
		}
	
		//Si nous sommes dans le mode attaque, on veut savoir si on peut trouver au moins une chemin vers une adversaire
		if(m.representation[monIA.getSelfHero().getLine()][monIA.getSelfHero().getCol()]>=0 && !monIA.isPredicting() && monIA.getMode()==Mode.ATTAQUE)
		{
			boolean enablewalls=true;
			if(monIA.verbose) System.out.println("Bottleneck1");
			BasicCostCalculator cc=new BasicCostCalculator();
			PixelHeuristicCalculator hc=new PixelHeuristicCalculator();
			KesimalVarolSuccessor sc=new KesimalVarolSuccessor(m);
			Astar astar=new Astar(monIA, monIA.getSelfHero(), cc, hc, sc);
			if(m.representation[monIA.getSelfHero().getLine()][monIA.getSelfHero().getCol()]>=0)
			{
				for(AiHero ah : monIA.getZone().getRemainingHeroes())
				{
					monIA.checkInterruption();
					if(!ah.hasEnded() && ah!=monIA.getSelfHero())
					{
						AiTile tileToEvaluate=null;
						/**
						 * Ce n'est pas vraiment nécesssaire ; on l'avait utilisée avant notre version de fonction successeur, pour éviter les cases ayant des
						 * bombes car la recherche ne terminerait jamais.
						 * Mais notre utilisation du processeur n'est pas mal du tout et les resultats ne sont pas trop differentes du tout, donc on va le laisser tomber.
						 */
						for(AiTile atrgt : ah.getTile().getNeighbors())
						{
							monIA.checkInterruption();
							if(atrgt.getBombs().size()==0 && atrgt.getFires().size()==0 && 
									atrgt.getBlocks().size()==0 && m.representation[atrgt.getLine()][atrgt.getCol()]>=0 &&
									(atrgt.getHeroes().size()==0 || (atrgt.getHeroes().size()==1 && atrgt.getHeroes().get(0)==monIA.getSelfHero())))
								tileToEvaluate=atrgt;
						}
						if(tileToEvaluate!=null)
						{
							//A* : Si l'adversaire avait laissee une bombe au moment de calcul, on aura une calcul immensement lent.
							if(tileToEvaluate.getBombs().size()==0 && tileToEvaluate.getFires().size()==0) 
							{
								AiPath path=null;
								try {
									sc.initNodeHack();
									path=astar.processShortestPath(monIA.getSelfHero().getTile(),ah.getTile());//tileToEvaluate);
								}
								catch (LimitReachedException e) {
									if(monIA.verbose) System.out.println("sdasdadad "+e);
								}
								catch (NullPointerException e) {
									if(monIA.verbose) System.out.println("sdasdadad2 "+e);
								}
								if(path!=null && path.getLength()>0)
								{
									enablewalls=false;
									if(monIA.getZone().getRemainingHeroes().size()<=2) //sinon, on veut le tuer tout de suite. 
									{
										if(monIA.lastPathChosenParameters!=null)
										{
										AiTile lastTile=monIA.lastPathChosenParameters.getLastPathChosen().getLastTile();
										if(lastTile!=ah.getTile() && lastTile.getCol()!=ah.getCol() && lastTile.getLine()!=ah.getLine() )
											monIA.requestNewPath(path,0);
										}
									}
									break;
								}
							}
						}
					}
				}
			}
			if(monIA.verbose) System.out.println("Bottleneck2");
			//Si on n'a aucune chemin, alors on peut détruire les murs pour créer une nous-meme.
			if(enablewalls)
			{
				//si on voudrait trouver une chemin probable vers le dernier adversaire
				boolean willPlayNaturally=FastWallDestruction.canFindAndCreateBetterPathsIfThereIsOneEnemy(m,cc,hc,sc); 
				
				if(willPlayNaturally) //sinon, on peut chercher l'environnement naturellement
				{					
					if(monIA.verbose) System.out.println("Wall addon made");
					monIA.requestWallEffects(m, 10);
					m.bestDirectionOptimisation();
				}
			}
			if(monIA.verbose) System.out.println("Bottleneck3");
		}
		
		
		if(!invokedForSafeBombPlacement) //si appel est pour emplacement des bombes, on laisse celle-ci a tomber
		{
			//Trouves-moi sur une case d'emplacement ?
			AiTile moniacurrtile=monIA.getSelfHero().getTile();
			if(moniacurrtile.getBombs().size()==0)
			{
				if(m.regionEmplacementImportanceMatrix[moniacurrtile.getLine()][moniacurrtile.getCol()].importance>0)
				{
					if(monIA.verbose) System.out.println("Is bomb permitted");
					if(monIA.isBombDropPermitted(m)) {
						monIA.willReleaseBomb();
						return new AiAction(AiActionName.NONE); //KesimalVarol va faire la posage ; on veut qu'on la pose dans la case exacte.
					}
					
					else {
						bombSpecialRequest=null;
					}
					if(monIA.verbose) System.out.println("Bomb is not permitted");
				}
			}
			
			//On ne considere pas le chemin courante de meme.
			if(bombSpecialRequest==null && !PathSafetyDeterminators.hasLastPathBecomeDangerousOrObsolete(m))
			{
				if(monIA.verbose) System.out.println("Nope, not obsolete");
				AiTile dest=monIA.lastPathChosenParameters.getLastPathChosen().getTile(monIA.lastPathChosenParameters.getCurrentPosition()+1);
				/*if(dest==monIA.getSelfHero().getTile()) {
					monIA.updatePathIfChangedCase();
					dest=monIA.lastPathChosenParameters.getLastPathChosen().getTile(monIA.lastPathChosenParameters.getCurrentPosition()+1);
				}*/
				Direction dir=monIA.getZone().getDirection(monIA.getSelfHero().getTile(), dest);
				AiAction movement=new AiAction(AiActionName.MOVE,dir);
				return movement;
			}
		}
		
		//if(invokedForSafeBombPlacement)
		//	changedUpTo=0;

		AiAction result=choosePath(m,invokedForSafeBombPlacement);
		
		if(result==null)
		{
			if(monIA.verbose) System.out.println("Couldn't do anything.");
			if(!monIA.isPredicting() && m.representation[monIA.getSelfHero().getLine()][monIA.getSelfHero().getCol()]<0) {
				if(monIA.verbose) System.out.println("In danger, no way.");
				monIA.iAmInDanger(true);
			}
			return new AiAction(AiActionName.NONE); 
		}
		else
			return result;
	}
	
	/**
	 * La partie de commitMovement qui determine une chemin pour nous deplacer (et eventuellement, renvoie l'action suivante).
	 * @param m
	 * @param a
	 * @param changedUpTo
	 * @param usedClosestCase
	 * @return null si on ne peut pas decider sur une chemin, une action concernant une deplacement vers la direction sinon.
	 */
	private AiAction choosePath(Matrix m,boolean invokedForSafeBombPlacement) throws StopRequestException
	{
		monIA.checkInterruption();
		
		short changedUpTo=0; // La limite de recherche de notre matrice.
		double[] hval=new double[5];
		int[] hI=new int[5];
		int[] hJ=new int[5];
		
		if(true)//(!invokedForSafeBombPlacement)
		{
			for(int i=0;i<m.height;i++)
			{
				monIA.checkInterruption();
				for(int j=0;j<m.width;j++)
				{
					monIA.checkInterruption();
					for(int a=0;a<5;a++)
					{
						monIA.checkInterruption();
						if(m.representation[i][j]>hval[a])
						{
							double vTmp=hval[a];
							int iTmp=hI[a],jTmp=hJ[a];
							hval[a]=m.representation[i][j];
							hI[a]=i; hJ[a]=j;
							for(int offset=a+1;offset<5;offset++)
							{
								monIA.checkInterruption();
								double vTmp2=hval[offset];
								int iTmp2=hI[offset],jTmp2=hJ[offset];
								hval[offset]=vTmp; hI[offset]=iTmp; hJ[offset]=jTmp;
								vTmp=vTmp2;iTmp=iTmp2;jTmp=jTmp2;
							}
							changedUpTo++;
							break;
						}
					}
				}
			}
		}
		if(changedUpTo>5)
			changedUpTo=5;
		
		if(monIA.verbose) System.out.println("Greatest variables found : ");
		for(int i=0;i<5;i++) {
			monIA.checkInterruption();
			if(monIA.verbose) System.out.println(hI[i]+","+hJ[i]+":"+hval[i]);
		}
		
		//a=4;
		//changedUpTo=4;
		if(monIA.verbose) System.out.println("Changedupto var : "+changedUpTo);
		
		BasicCostCalculator cc=new BasicCostCalculator();
		PixelHeuristicCalculator hc=new PixelHeuristicCalculator();
		KesimalVarolSuccessor sc=new KesimalVarolSuccessor(m);
		sc.initNodeHack();
		Astar astar=new Astar(monIA, monIA.getSelfHero(), cc, hc, sc);
		
		boolean usedClosestCase=false;
		int a=0;
		while(a<changedUpTo || !usedClosestCase)
		{
			monIA.checkInterruption();
			@SuppressWarnings("unused")
			double currhval=0;
			pathToFollow=null;
			AiTile target=null; //
			
			if(a==changedUpTo) {
				if(monIA.verbose) System.out.println("Will look for last chance");
				
				if(monIA.verbose) System.out.println("Preferable for bomb");
				target=monIA.getReachablePreferableLocation(m);
				usedClosestCase=true;
				if(target==null) {
					return new AiAction(AiActionName.NONE);
				}
				if(monIA.verbose) System.out.println("Bomb target found ");
				
				currhval=m.representation[target.getLine()][target.getCol()];
				if(monIA.verbose) System.out.println("Target is closest now, "+target);
			}
			else {
				if(monIA.verbose) System.out.println("Bomb special req : "+bombSpecialRequest);
				if(bombSpecialRequest!=null)
				{
					target=bombSpecialRequest;
					bombSpecialRequest=null;
					a--;
				}
				else {
					AiTile b=monIA.getPrBonusTile();
					if(b!=null)
					{
						if(monIA.verbose) System.out.println("Bonus tile before bomb "+target);
						target=b;
						a--;
						monIA.setPrBonusTile(null);
						//m.requestBonusImportanceIncrease();
					}
					else {
						target=monIA.getZone().getTile(hI[a], hJ[a]);
						currhval=m.representation[target.getLine()][target.getCol()];
					}
				}
			}
			
			try {
				if(monIA.verbose) System.out.println("Processing for target : "+target);
				if(monIA.getZone().getTileDistance(monIA.getSelfHero().getTile(), target)==1) {
					pathToFollow=new AiPath();
					pathToFollow.addTile(monIA.getSelfHero().getTile());
					pathToFollow.addTile(target);
				}
				else if(target.getBombs().size()==0 && target.getFires().size()==0) {//Pas vraiment necessaire mais meme raison que celui pour "chemin aux adversaires"
					pathToFollow=astar.processShortestPath(monIA.getSelfHero().getTile(),target);
					sc.initNodeHack();
				}
				short retY=2;
				if(PathSafetyDeterminators.isThisPathDangerous(pathToFollow,m))
					retY=0;
				
				if(monIA.verbose) System.out.println("Processed for target : "+target);
				
				if(retY==2)
				{
					try {
						if(pathToFollow.getLength()>=2) { //2
							AiTile dest=pathToFollow.getTile(1);
							if(monIA.verbose) System.out.println("Astar for target "+pathToFollow.getLastTile());
							Direction dir=monIA.getZone().getDirection(monIA.getSelfHero().getTile(), dest);
							
							if(!invokedForSafeBombPlacement)
								monIA.requestNewPath(pathToFollow,0);
							
							else if(monIA.isPredicting()) {
								monIA.requestSpecialBombEvasiveTarget(target);
							}
							monIA.iAmInDanger(false);
							return new AiAction(AiActionName.MOVE,dir);
						}
						else if (pathToFollow.getLength()==1) {
							if(monIA.verbose) System.out.println("Path too short for "+target);
						}
					}
					catch (NullPointerException e) {
						System.out.println("Case Nullptr "+e);
					}
				}
			}
			catch (LimitReachedException e) {
				if(monIA.verbose) System.out.println("Astar limit "+e);
			}
			catch (NullPointerException e) {
				if(monIA.verbose) System.out.println("Astar Nullptr "+e);
			}
			a++;
		}
		return null;
	}
	
	//---------------------
	// Methodes a utiser lors de debogage
	//---------------------
	/**
	 * Dessin d'un chemin sur ecran
	 * @throws StopRequestException 
	 */
	public void drawPathOnScreen() throws StopRequestException
	{
		monIA.checkInterruption();
		AiOutput out = monIA.getOutput();
		out.addPath(pathToFollow, new Color(0, 255, 0));
	}
}
