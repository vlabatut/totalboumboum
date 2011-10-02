package org.totalboumboum.ai.v201011.ais.kesimalvarol.v4;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.ais.kesimalvarol.v4.KesimalVarol;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * La classe qui conduisse les op√©rations concernant la zone du jeu et les matrices.
 * 
 * @author Ali Baran Kesimal
 * @author Işıl Varol
 *
 */
@SuppressWarnings("deprecation")
public class GameZone
{	/** classe principale de l'IA, permet d'accÔøΩder ÔøΩ checkInterruption() */
	private static KesimalVarol monIA;
	public static void setMonIA(KesimalVarol monIA) {
		GameZone.monIA = monIA;
		Matrix.setMonIA(monIA);
	}
	
	/** ctr. principal
	 * @param gOld Une objet GameZone -probablement celui de l'iteration precedente- pour initialisation . Peut etre null si c'est la premiere fois qu'on l'utilise (valeurs statiques ne sont pas utilisees comme on aura obtenu des resultats fausses aux manches suivantes)
	 */
	public GameZone(GameZone gOld)
	{
		if(gOld!=null)
		{
		searchtreeLatestTarget=gOld.searchtreeLatestTarget;
		searchtreeLatestCumulativeVariable=gOld.searchtreeLatestCumulativeVariable;
		}
	}
	
	/** Structure interne pour gerer les explosions enchain√©s */
	private final class bombDetailed
	{
		public AiBomb bomb;
		public double delay;
		public ArrayList<bombDetailed> triggeringBombs;
		public bombDetailed(AiBomb b,double d)
		{
			bomb=b;
			delay=d;
			triggeringBombs=new ArrayList<bombDetailed>();
		}
	}

	/** Et une liste pour les contenir */
	private ArrayList<bombDetailed> bombsCurrent;
	/** Liste des murs dans le cas ou on a besoin de les mettre dans la matrice dans le mode attaque */
	private ArrayList<AiBlock> murList;
	
	/** PAS utilisee maintenant. */
	@SuppressWarnings("unused")
	private TreeMap<AiTile, Integer> proximityMap;
	
	/** Classe utilisee lors du gestion d'arbre de recherche*/
	private class caseNode {
		public AiTile relatedCase,parentCase;
		public int depth;
		public double selfImportance,cumulativeImportance;
		public caseNode(AiTile relatedCase,AiTile parentCase,double d,double cimportance,int depth)
		{
			this.relatedCase=relatedCase;
			this.selfImportance=d;
			this.cumulativeImportance=cimportance;
			this.depth=depth;
			this.parentCase=parentCase;
		}
	}
	
	/** Les mechanismes internes de Java ne peuvent pas iterer sur les objets de type AiBomb, donc on va utiliser notre propre methode*/
	private bombDetailed getBombDetailedInfo(AiBomb b)
	{
		for(bombDetailed bdet : bombsCurrent)
		{
			if (bdet.bomb==b)
				return bdet;
		}
		return null;
	}
	
	/** La cible trouvee apres le recherche precedent  */
	private AiTile searchtreeLatestTarget;
	/** La valeur de la cible trouvee apres le recherche precedent pour l'emplacement des bombes */
	private int searchtreeLatestCumulativeVariable;
	/** La cible trouvee apres le recherche precedent  */
	//private AiTile searchtreeLatestTargetForBomb;
	/** La valeur de la cible trouvee apres le recherche precedent pour l'emplacement des bombes */
	//private int searchtreeLatestCumulativeVariableForBomb;
	
	/** Methode pour construire la matrice */
	public Matrix constructInterestMatrix() throws StopRequestException
	{
		monIA.checkInterruption();
		Matrix m=new Matrix(monIA.getZone());
		bombsCurrent=new ArrayList<bombDetailed>();
		murList=new ArrayList<AiBlock>();
		List<AiBomb> cbombs=monIA.getZone().getBombs();
		
		/*
		//Ensuite, on fait des calcules pour le reste des entit√©s
		for(int i=0;i<m.height;i++)
		{
			for(int j=0;j<m.width;j++)
			{
				assignBasicNums(m,i,j);
			}
		}
		*/
		
		for(int i=0;i<m.height;i++)
		{
			for(int j=0;j<m.width;j++)
			{
				AiTile currtile=monIA.getZone().getTile(i,j);
				List<AiFire> fires=currtile.getFires();
				if (fires.size()>0) {
					m.representation[i][j]=-300;
				}
			}
		}
		
		/*
		 * On doit gerer les bombes du maniere different.
		 * On va iterer les bombes quatre fois, pour : Initializer -> Determiner lesquels sont effectu√©s par les explosions -> Choisir les valeurs -> Ecrire les valeurs dans la matrice
		 */
		//D'abord, on initialize le liste avec toutes les bombes pour qu'elles soient presentes.
		for (AiBomb bomb : cbombs)
		{
			double delay=((double)bomb.getNormalDuration()-bomb.getTime());
			if (delay<0) { //√áa veut dire que bombe est tomb√© en panne
				delay=0;
			}
			AiTile bombpos=bomb.getTile();
			double lowestNeighboringValue=m.representation[bombpos.getLine()][bombpos.getCol()];
			for(AiTile voisines : bombpos.getNeighbors())
			{
				double tmp=m.representation[voisines.getLine()][voisines.getCol()];
				AiTile voisinDevoisin=voisines.getNeighbor(monIA.getZone().getDirection(bombpos, voisines));
				if(voisinDevoisin!=null)
					if(m.representation[voisinDevoisin.getLine()][voisinDevoisin.getCol()]==tmp)
					{
						if(tmp<lowestNeighboringValue)
							lowestNeighboringValue=tmp;
						if(lowestNeighboringValue==-300)
							break;
					}
			}
			double bombPassTolerance=lowestNeighboringValue-(monIA.getCurrentToleranceCofficient()/monIA.getUsualBombNormalDuration());
			if(bombPassTolerance<=-300)
				delay=-300;//lowestNeighboringValue;//m.representation[bombpos.getLine()][bombpos.getCol()];//-300 
			else
				delay=-300+100.0*(delay/(double)bomb.getNormalDuration());
			bombsCurrent.add(new bombDetailed(bomb,delay));
		}
		//Puis, on change les valeurs des bombes qui seront effectu√©s par les autres.(oın change -> on remplisse le liste ?)
		for (AiBomb bomb : cbombs)
		{
			bombDetailed bombdet=getBombDetailedInfo(bomb);
			List<AiTile>affectedCases=bombdet.bomb.getBlast();
			for (AiTile t : affectedCases)
			{
				List<AiBomb> tilebombs=t.getBombs();
				for (AiBomb bombCase:tilebombs)
				{
					if (bombCase!=bombdet.bomb) {
						bombDetailed beff=getBombDetailedInfo(bombCase);
						beff.triggeringBombs.add(bombdet);
					}
				}
			}
		}
		//Et on choisit la valeur la plus suitable
		for (bombDetailed bomb : bombsCurrent)
		{
			for (bombDetailed bombCompare : bomb.triggeringBombs)
			{
				if (bombCompare.delay<bomb.delay)
					bomb.delay=bombCompare.delay;
			}	
		}
		//Dernierement, comme les valeurs sont mises a jour, on les effectue sur la matrice
		for (AiBomb bomb : cbombs)
		{
			bombDetailed bdet=getBombDetailedInfo(bomb);
			List<AiTile>affectedCases=bomb.getBlast();
			for (AiTile t : affectedCases)
			{
				int i=t.getLine(),j=t.getCol();
				if(m.representation[i][j]>(int)bdet.delay) {
					/*
					if(bdet.delay>-280)
						m.representation[i][j]+=(int)bdet.delay;
					else
						m.representation[i][j]=(int)bdet.delay;
						*/
					m.representation[i][j]=(int)bdet.delay;
				}
			}
		}
		
		//Ensuite, on fait des calcules pour le reste des entit√©s
		for(int i=0;i<m.height;i++)
		{
			for(int j=0;j<m.width;j++)
			{
				assignBasicNums(m,i,j);
			}
		}
		
		handleWallAndEnemies(m);
		
		return m;
	}
	
	/** Pour attribuer des valeurs elementaires 
	 * 
	 * @param m Matrice necessaire pour les calculs
	 * @param i Ligne courante
	 * @param j Colonne courante
	 */
	private void assignBasicNums(Matrix m,int i,int j) throws StopRequestException
	{
		monIA.checkInterruption();
		AiTile currtile=monIA.getZone().getTile(i,j);
		
		//Flamme
		/*
		List<AiFire> fires=currtile.getFires();
		if (fires.size()>0) {
			m.representation[i][j]=-300;
			//S'il y en a du feu, on n'a rien autre op√©ration a faire : Il faut √©viter cette case.
			return;
		}*/
		
		int modifier;
		int a=currtile.getLine(),b=currtile.getCol();
		
		//Bonus
		if(monIA.getMode()==Mode.COLLECTE)
			modifier=50;
		else {
			modifier=20;
		}

		//Ustunden gecme geyigiyle ilgiliydi bu, ~20 tolerans degil de yurume suresi falan yap iste
		if(m.representation[i][j]>-275) //Notre limite minimale est -275 : On peut tol√©rer une bombe laiss√© de nouveau, mais si la valeur est trop inferieure, on ne le modifie pas.
			for (int c=0;c<currtile.getItems().size();c++)
				m.representation[a][b]+=modifier;
		
		//Murs, on va seulement les indiquer pour traitement des valeurs dynamiques.
		for(AiBlock block : currtile.getBlocks())
		{
			if(block.isDestructible()) {
				murList.add(block);
			}
		}
		
		//Distance -- on pense a changer le systeme, on n'utilise pas a ce moment
		/*
		AiZone azone=monIA.getZone();
		AiTile ourtile=monIA.getSelfHero().getTile();
		if (m.representation[i][j]>=0 && currtile.isCrossableBy(monIA.getSelfHero()))
		{
			int awardedval=2*((azone.getHeight()+azone.getWidth())-azone.getTileDistance(ourtile, currtile))/3;
			if(m.representation[i][j]==0)
				m.representationDistanceParameters[i][j]=awardedval;
			else
				m.representation[i][j]+=awardedval; //>=0
		}*/
		
		AiZone azone=monIA.getZone();
		AiTile ourtile=monIA.getSelfHero().getTile();
		if (m.representation[i][j]>=0 && currtile.isCrossableBy(monIA.getSelfHero()))
		{
			int awardedval=2*((azone.getHeight()+azone.getWidth())-azone.getTileDistance(ourtile, currtile))/3;
			m.representationDistanceParameters[i][j]=awardedval;
		}
			
	}
	
	/** Mur et adversaires sont gerees ici 
	 * @param m Matrice necessaire pour les calculs
	 */
	private void handleWallAndEnemies(Matrix m) throws StopRequestException
	{
		monIA.checkInterruption();
		int modifier=0;
		
		//Adversaires
		List<AiHero> adversaires=monIA.getZone().getHeroes();
		if(monIA.getMode()==Mode.COLLECTE)
			modifier=1;
		else {
			modifier=5;
		}
		for(AiHero adv : adversaires)
		{
			double interest=0;
			if(adv!=monIA.getSelfHero())
			{
				//adv.getTile();
				AiTile ctile=adv.getTile();
				List<AiTile> tAll=ctile.getNeighbors();
				tAll.add(ctile.getNeighbor(Direction.DOWN).getNeighbor(Direction.RIGHT));
				tAll.add(ctile.getNeighbor(Direction.DOWN).getNeighbor(Direction.LEFT));
				tAll.add(ctile.getNeighbor(Direction.UP).getNeighbor(Direction.RIGHT));
				tAll.add(ctile.getNeighbor(Direction.UP).getNeighbor(Direction.LEFT));
				for(AiTile t : tAll)
				{
					if(!t.isCrossableBy(adv))
					{
						interest+=1.25*modifier;
					}
				}
				for(AiTile at : ctile.getNeighbors())
				{
					if(at.isCrossableBy(adv)) {
						m.markForBombPlacementCandidate(adv,at,interest);
					}
				}
			}
		}
		
		if(monIA.getMode()==Mode.COLLECTE)
			handleWalls(m,40);
		
		//La direction la plus preferable
		m.bestDirectionOptimisation();

	}
	
	/** Les murs peuvent etre geree separement d'adversaires (mode attaque), celle-ci nous permet de le faire 
	 * 
	 * @param m Matrice pour determiner les valeurs
	 * @param modifier La valeur a utiliser lors de affectation des valeurs.
	 */
	public void handleWalls(Matrix m,int modifier) throws StopRequestException
	{
		monIA.checkInterruption();
		//Murs
		for(AiBlock ablock : murList)
		{
			for (AiTile t : ablock.getTile().getNeighbors()) {
				if(t.getCol()>=0 && t.getLine()>=0 && m.representation[t.getLine()][t.getCol()]>=0) {
					m.representation[t.getLine()][t.getCol()]+=modifier;
					for(int c=0;c<t.getBlocks().size();c++) {
						m.representation[t.getLine()][t.getCol()]-=modifier; //Les murs resteront neutres.
					}
					if (true)
					{
						m.markForBombPlacementCandidate(ablock,t.getLine(), t.getCol(), 0);
					}
				}
			}
		}
	}
	
	/** Pour prevoir la matrice ayant une bombe 
	 * @param m La matrice qui sera modifiee  
	 */
	public void modifyMatrixWithFutureBomb(Matrix m) throws StopRequestException
	{
		monIA.checkInterruption();
		AiTile bombloc=monIA.getSelfHero().getTile();
		int bombrange=monIA.getSelfHero().getBombRange();
		AiZone zone=monIA.getZone();
		
		double bombval=-200;
		int i=bombloc.getLine(),j=bombloc.getCol();
		if(bombval>m.representation[i][j])
			bombval=m.representation[i][j];
		
		if(monIA.verbose) System.out.println(i+","+j);
		if(monIA.verbose) System.out.println(bombrange);
		
		for(int iC=i-1;iC>=i-bombrange;iC--)
		{
			if(iC<monIA.getZone().getHeight() && iC>=0) {
				if(monIA.verbose) System.out.println(zone.getTile(iC,j));
				try {
					if(zone.getTile(iC, j).getBlocks().size()==0)
					{
						if(monIA.verbose) System.out.println("M1 : "+zone.getTile(iC,j));
						if(m.representation[iC][j]>bombval)
							m.representation[iC][j]=bombval;
					}
					else break;
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					continue;
				}
			}
		}
		for(int jC=j-1;jC>=j-bombrange;jC--)
		{
			if(jC<monIA.getZone().getWidth() && jC>=0) {
				if(monIA.verbose) System.out.println(zone.getTile(i,jC));
				try {
					if(zone.getTile(i, jC).getBlocks().size()==0)
					{
						if(monIA.verbose) System.out.println("M2 : "+zone.getTile(i,jC));
						if(m.representation[i][jC]>bombval)
							m.representation[i][jC]=bombval;
					}
					else break;
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					continue;
				}
			}
		}
		for(int iC=i;iC<=i+bombrange;iC++)
		{
			if(iC<monIA.getZone().getHeight() && iC>=0) {
				try {
					if(zone.getTile(iC, j).getBlocks().size()==0)
					{
						if(monIA.verbose) System.out.println("M3 : "+zone.getTile(iC,j));
						if(m.representation[iC][j]>bombval)
							m.representation[iC][j]=bombval;
					}
					else break;
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					continue;
				}
			}
		}
		for(int jC=j;jC<=j+bombrange;jC++)
		{
			if(jC<monIA.getZone().getWidth() && jC>=0) {
				try {
					if(zone.getTile(i, jC).getBlocks().size()==0)
					{
						if(monIA.verbose) System.out.println("M4 : "+zone.getTile(i,jC));
						if(m.representation[i][jC]>bombval)
							m.representation[i][jC]=bombval;
					}
					else break;
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					continue;
				}
			}
		}
		if(monIA.verbose) System.out.println("Bombval set to "+bombval);
		//m.representation[i][j]=-300;

	}
	
	/** Pour ne pas detruire les bonus si elles sont a cote d'un mur destructible 
	 * 
	 * @param m Matrice necessaire pour les calculs
	 * @return Oui s'il y a des bonus en proximite.
	 * */
	public boolean areBonusAdjacent(Matrix m) throws StopRequestException
	{
		monIA.checkInterruption();
		int i=monIA.getSelfHero().getTile().getLine(),j=monIA.getSelfHero().getTile().getCol(),bombrange=monIA.getSelfHero().getBombRange();
		if(m.regionImportanceMatrix[i][j]>=2)
			m.representation[i][j]-=(m.regionImportanceMatrix[i][j]-1)*40;
		AiZone zone=monIA.getZone();
		for(int iC=i-1;iC>i-bombrange;iC--)
		{
			try {
				if(zone.getTile(iC, j).getBlocks().size()==0)
				{
					if(zone.getTile(iC, j).getItems().size()>0) {
						monIA.setPrBonusTile(zone.getTile(iC, j));
						return true;
					}
				}
				else break;
			}
			catch(Exception e)
			{
				continue;
			}
		}
		for(int iC=i;iC<i+bombrange;iC++)
		{
			try {
				if(zone.getTile(iC, j).getBlocks().size()==0)
				{
					if(zone.getTile(iC, j).getItems().size()>0) {
						monIA.setPrBonusTile(zone.getTile(iC, j));
						return true;
					}
				}
				else break;
			}
			catch(Exception e)
			{
				continue;
			}
		}
		for(int jC=j-1;jC>j-bombrange;jC--)
		{
			try {
				if(zone.getTile(i, jC).getBlocks().size()==0)
				{
					if(zone.getTile(i, jC).getItems().size()>0) {
						monIA.setPrBonusTile(zone.getTile(i, jC));
						return true;
					}
				}
				else break;
			}
			catch(Exception e)
			{
				continue;
			}
		}
		for(int jC=j;jC<j+bombrange;jC++)
		{
			try {
				if(zone.getTile(i, jC).getBlocks().size()==0)
				{
					if(zone.getTile(i, jC).getItems().size()>0) {
						monIA.setPrBonusTile(zone.getTile(i, jC));
						return true;
					}
				}
				else break;
			}
			catch(Exception e)
			{
				continue;
			}
		}
		return false;
		
	}
	
	/**
	 * Renvoie la case accesible et plus preferable dans le cas ou on n'utilise pas seuls les valeurs de matrice (ex: Nous sommes bloquees par un chemin)
	 * 
	 * @param m Matrice necessaire pour les calculs
	 * @return La case souhaitee
	 */
	public AiTile constructAccesibleCases(Matrix m) throws StopRequestException
	{
		monIA.checkInterruption();
		
		ArrayDeque<caseNode> importancelist=new ArrayDeque<caseNode>();
		//ArrayDeque<caseNode> allNodes=new ArrayDeque<caseNode>();
		
		double variablestable[][][]=new double[m.height][m.width][3];
		for(int i=0;i<m.height;i++)
		{
			for(int j=0;j<m.width;j++)
			{
				variablestable[i][j][0]=m.representation[i][j];

				variablestable[i][j][1]=0; //treede olmasa da bu dursun, 4 defa gezilirse yine bitir.
				variablestable[i][j][2]=0; //distance level.
				//variablestable[i][j][3]=0; //total values up until that location
 			}
		}
		//proximityMap=new TreeMap<AiTile, Integer>();

		//@SuppressWarnings("unused")
		//int tolerval=25;
		/* todo : later.
		try {
			if(monIA.getZone().getBombs().size()>0) {
				AiBomb b=monIA.getZone().getBombs().get(0);
				tolerval=(int)(monIA.getCurrentToleranceCofficient()/b.getExplosionDuration());
			}
		}
		catch(Exception e)
		{
			tolerval=25;
		}*/
		int tolerval=(int)(monIA.getCurrentToleranceCofficient()/monIA.getUsualBombNormalDuration());
		
		//System.out.println("tolerval potential "+monIA.getCurrentToleranceCofficient()+" & "+monIA.getUsualBombNormalDuration()+" & "+monIA.getCurrentToleranceCofficient()/monIA.getUsualBombNormalDuration());
		
		if(monIA.verbose) System.out.println("Coeff "+monIA.getCurrentToleranceCofficient()+", Tolerval : "+tolerval);
		if(tolerval==0)
			return monIA.getSelfHero().getTile();
		
		//Future usage.
		//int iTD=monIA.getSelfHero().getTile().getLine(),jTD=monIA.getSelfHero().getTile().getCol(),currentGreatestD=0;
		ArrayDeque<caseNode> research=new ArrayDeque<caseNode>();
		if(monIA.verbose) System.out.println(monIA.getSelfHero().getTile().getLine()+","+monIA.getSelfHero().getTile().getCol());
		for(AiTile t : monIA.getSelfHero().getTile().getNeighbors())
		{
			if(t.getBlocks().size()==0 && t.getBombs().size()==0) {
				//int expval=variablestable[monIA.getSelfHero().getTile().getLine()][monIA.getSelfHero().getTile().getCol()][0];
				double expval=variablestable[t.getLine()][t.getCol()][0];
				if(monIA.verbose) System.out.print("Expval before & after : "+expval+" ");
				int tolerMod=tolerval;
				if(variablestable[t.getLine()][t.getCol()][0]>=0)
					tolerMod=0;
				if(expval>-200 && expval<0) //~err de norte
					expval=-200;
				if(monIA.verbose) System.out.println(expval);
				research.push(new caseNode(t,monIA.getSelfHero().getTile(),expval-tolerMod,expval-tolerMod,0));
				if(monIA.verbose) System.out.println("Becomes "+t.getLine()+","+t.getCol()+" = "+(expval-tolerMod));
			}
		}
		
		//variablestable[monIA.getSelfHero().getTile().getLine()][monIA.getSelfHero().getTile().getCol()][1]=1;
		boolean oneSafeLocFound=false;
		if(monIA.verbose) System.out.println("Stack find begins, ispredicting:"+monIA.isPredicting());
		//int researchNodesLeft=4;
		while(research.size()>0 && (!monIA.isPredicting() || (!oneSafeLocFound && monIA.isPredicting())))
		{
			//if(monIA.verbose) System.out.println(research.size());
			caseNode n=research.poll();
			AiTile current=n.relatedCase;
			double[] matval=variablestable[current.getLine()][current.getCol()];
			if(monIA.verbose) System.out.println("Current : "+current.getLine()+","+current.getCol()+",="+n.selfImportance+" , "+matval[1]+","+n.cumulativeImportance+","+current.getBlocks().size()+","+current.getBombs().size());
			if(true)//(!bombpresent || blockcount<=2)
			{
				//n.cumulativeImportance>-300 ve -300
				if(n.depth<8 && matval[1]<5 && n.selfImportance>-290 && (!monIA.isPredicting() || 
						(monIA.isPredicting() && monIA.getSelfHero().getTile()!=current && current.getHeroes().size()==0))
				)			
				{
					if(n.selfImportance>=0 && monIA.getSelfHero().getTile()!=current)
					{
						caseNode c=new caseNode(current,n.parentCase,n.selfImportance,n.cumulativeImportance,n.depth);
						importancelist.add(c);
						oneSafeLocFound=true;
						if(monIA.verbose) System.out.println("Safe location found");
					}

					if(!oneSafeLocFound)// || (!monIA.isPredicting() && variablestable[current.getLine()][current.getCol()][0]>-200))
					{
						for(AiTile t : current.getNeighbors())
						{
							//
							if(t!=n.parentCase && t.getBlocks().size()==0 && t.getBombs().size()==0) //variablestable[t.getLine()][t.getCol()][1]==0 
							{
								if(monIA.verbose) System.out.println(t.getLine()+","+t.getCol()+" = "+variablestable[t.getLine()][t.getCol()][0]);
								variablestable[t.getLine()][t.getCol()][2]=matval[2]+1;
								int tolerMod=tolerval;
								if(variablestable[t.getLine()][t.getCol()][0]>=0)
									tolerMod=0;
								if(monIA.verbose) System.out.println("Tolerval : "+tolerMod);
								double cimportance=n.cumulativeImportance-tolerMod;
								//n.depth+2
								if(variablestable[t.getLine()][t.getCol()][0]<=n.cumulativeImportance-tolerMod)
								{
									cimportance=variablestable[t.getLine()][t.getCol()][0]-(n.depth*tolerMod);
								}
								double currentInterest=variablestable[t.getLine()][t.getCol()][0]-(n.depth+2)*tolerMod;
								if(currentInterest>-300) {
									if(monIA.verbose) System.out.println("Is added");
									research.push(new caseNode(t,current,variablestable[t.getLine()][t.getCol()][0]-(n.depth+2)*tolerMod,cimportance,n.depth+1));
								}
								if(monIA.verbose) System.out.println("Becomes "+t.getLine()+","+t.getCol()+" = "+(variablestable[t.getLine()][t.getCol()][0]-(n.depth+2)*tolerMod));
							}
						}
					}
					matval[1]++;
				}
			}
		}
		
		int iT=monIA.getSelfHero().getTile().getLine(),jT=monIA.getSelfHero().getTile().getCol();
		double currentGreatest=-1;//m.representation[monIA.getSelfHero().getTile().getLine()][monIA.getSelfHero().getTile().getCol()];
		caseNode selectedCaseNode=null;
		for(caseNode n : importancelist)
		{
			if(n.selfImportance>currentGreatest)
			{
				currentGreatest=n.selfImportance;
				iT=n.relatedCase.getLine();
				jT=n.relatedCase.getCol();
				selectedCaseNode=n;
			}
			if(monIA.verbose) System.out.println(n.relatedCase+" ,"+n.selfImportance+" "+n.cumulativeImportance);
		}
		/*
		if(selectedCaseNode==null)
			selectedCaseNode=new caseNode(monIA.getZone().getTile(iT, jT), variablestable[iT][jT][0], tolerval,0);*/
		
		if(selectedCaseNode!=null) {
			if(monIA.verbose) System.out.println("Stack find ended "+selectedCaseNode.cumulativeImportance+" : "+monIA.getZone().getTile(iT, jT));
			if(monIA.isPredicting()) {
				monIA.requestSpecialBombEvasiveTarget(monIA.getZone().getTile(iT, jT));
				if(monIA.verbose) System.out.println("Bomb special req made : "+monIA.getZone().getTile(iT, jT));
			}
		}
		else {
			if(monIA.verbose) System.out.println("Stack find failed to find secure loc");
		}
		AiTile ret=monIA.getZone().getTile(iT, jT);
		
		return ret;
	}
}
