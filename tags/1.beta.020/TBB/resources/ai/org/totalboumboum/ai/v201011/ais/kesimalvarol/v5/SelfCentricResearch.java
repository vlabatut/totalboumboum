package org.totalboumboum.ai.v201011.ais.kesimalvarol.v5;

import java.util.ArrayDeque;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.ais.kesimalvarol.v5.KesimalVarol;

/**
 * La classe de recherche supplementaire, aidant notre recherche concernant la matrice et A*, utilisee dans les cas ou cherche par A* et matrice nous semble inefficace.
 * En particulier, si les valeurs les plus grandes de matrice sont inaccessibles a nous, on ne veut pas iterer toutes les cases avec A* ; il est mieux 
 * de faire une recherche qui commence par la case actuelle de notre agent.
 * 
 * Celle-ci ne remplace pas A* completement ; elle est utilisee pour trouver une case, mais pas un chemin.
 * 
 * @author Ali Baran Kesimal
 * @author Işıl Varol
 *
 */
public class SelfCentricResearch 
{
	/** classe principale de l'IA, permet d'accÔøΩder ÔøΩ checkInterruption() */
	private static KesimalVarol monIA;
	public static void setMonIA(KesimalVarol monIA) throws StopRequestException {
		monIA.checkInterruption();
		SelfCentricResearch.monIA = monIA;
	}
	
	/** Classe utilisee lors du gestion d'arbre de recherche*/
	private static class caseNode {
		public AiTile relatedCase,parentCase;
		public int depth;
		public double selfImportance,cumulativeImportance;
		public caseNode(AiTile relatedCase,AiTile parentCase,double d,double cimportance,int depth) throws StopRequestException
		{
			monIA.checkInterruption();
			this.relatedCase=relatedCase;
			this.selfImportance=d;
			this.cumulativeImportance=cimportance;
			this.depth=depth;
			this.parentCase=parentCase;
		}
	}
	
	/**
	 * Renvoie la case accesible et plus preferable dans le cas ou on n'utilise pas seuls les valeurs de matrice (ex: Nous sommes bloquees par un chemin)
	 * 
	 * @param m Matrice necessaire pour les calculs
	 * @return La case souhaitee
	 */
	public static AiTile getPreferableAccesibleCase(Matrix m) throws StopRequestException
	{
		monIA.checkInterruption();
		
		ArrayDeque<caseNode> importancelist=new ArrayDeque<caseNode>();
		//ArrayDeque<caseNode> allNodes=new ArrayDeque<caseNode>();
		
		double variablestable[][][]=new double[m.height][m.width][3];
		for(int i=0;i<m.height;i++)
		{
			monIA.checkInterruption();
			for(int j=0;j<m.width;j++)
			{
				monIA.checkInterruption();
				variablestable[i][j][0]=m.representation[i][j];

				variablestable[i][j][1]=0;
				variablestable[i][j][2]=0; //distance level.
 			}
		}
		int tolerval=(int)(monIA.getCurrentToleranceCofficient()/monIA.getUsualBombNormalDuration());
		
		if(monIA.verbose) System.out.println("Coeff "+monIA.getCurrentToleranceCofficient()+", Tolerval : "+tolerval);
		if(tolerval==0)
			return monIA.getSelfHero().getTile();

		ArrayDeque<caseNode> research=new ArrayDeque<caseNode>();
		if(monIA.verbose) System.out.println(monIA.getSelfHero().getTile().getLine()+","+monIA.getSelfHero().getTile().getCol());
		for(AiTile t : monIA.getSelfHero().getTile().getNeighbors())
		{
			monIA.checkInterruption();
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
			monIA.checkInterruption();
			//if(monIA.verbose) System.out.println(research.size());
			caseNode n=research.poll();
			AiTile current=n.relatedCase;
			double[] matval=variablestable[current.getLine()][current.getCol()];
			if(monIA.verbose) System.out.println("Current : "+current.getLine()+","+current.getCol()+",="+n.selfImportance+" , "+matval[1]+","+n.cumulativeImportance+","+current.getBlocks().size()+","+current.getBombs().size());
			if(true)//(!bombpresent || blockcount<=2)
			{
				//n.cumulativeImportance>-300 & -300
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

					if(!oneSafeLocFound)
					{
						for(AiTile t : current.getNeighbors())
						{
							monIA.checkInterruption();
							if(t!=n.parentCase && t.getBlocks().size()==0 && t.getBombs().size()==0)
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
			monIA.checkInterruption();
			if(n.selfImportance>currentGreatest)
			{
				currentGreatest=n.selfImportance;
				iT=n.relatedCase.getLine();
				jT=n.relatedCase.getCol();
				selectedCaseNode=n;
			}
			if(monIA.verbose) System.out.println(n.relatedCase+" ,"+n.selfImportance+" "+n.cumulativeImportance);
		}
		
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
