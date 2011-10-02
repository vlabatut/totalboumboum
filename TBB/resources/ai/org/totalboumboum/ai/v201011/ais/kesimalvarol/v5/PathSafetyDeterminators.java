package org.totalboumboum.ai.v201011.ais.kesimalvarol.v5;

import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;

/**
 * Gestion de verification de sûreté des chemins parcourus.
 * 
 * @author Ali Baran Kesimal
 * @author Işıl Varol
 *
 */
@SuppressWarnings("deprecation")
public class PathSafetyDeterminators {
	/**Pour checkInterruption.*/
	private static KesimalVarol monIA;
	public static void setMonIA(KesimalVarol monIA) throws StopRequestException {
		monIA.checkInterruption();
		PathSafetyDeterminators.monIA = monIA;
	}
	
	/**
	 * 
	 * @param p Le chemin a utiliser
	 * @param m La matrice a utiliser
	 * @return Si le chemin n'est pas en surete
	 * @throws StopRequestException
	 */
	public static boolean isThisPathDangerous(AiPath p, Matrix m) throws StopRequestException
	{
		monIA.checkInterruption();
		return isThisPathDangerous(p, m,0);
	}
	
	/**
	 * 
	 * @param p Le chemin a utiliser
	 * @param m La matrice a utiliser
	 * @param offset Si on ne commence pas de la case 1er du chemin
	 * @return Si le chemin n'est pas en surete
	 * @throws StopRequestException
	 */
	public static boolean isThisPathDangerous(AiPath p, Matrix m, int offset) throws StopRequestException
	{
		monIA.checkInterruption();
		List<AiTile> tiles=p.getTiles();
		int CumulativeInterest=0;
		int stepsTaken=0;
		int safePlaceFoundBeforeDanger=-1;
		if(offset==p.getLength()-1)
			return true;
		if(tiles.size()>0) {
			if(monIA.verbose) System.out.println("Path security check , start from "+p.getTile(offset));
		}
		if(monIA.verbose) System.out.println("Am I in danger anyway ?"+monIA.isInDangerAndWontMoveAnyway());
		for(int i=offset;i<tiles.size();i++)
		{
			monIA.checkInterruption();
			AiTile t=p.getTile(i);
			if(stepsTaken>=1 && m.representation[t.getLine()][t.getCol()]-(monIA.getCurrentToleranceCofficient()/monIA.getUsualBombNormalDuration())*stepsTaken>=0) //-280
			{
				safePlaceFoundBeforeDanger=stepsTaken;
			}
			if(t.getBombs().size()>0 && i!=offset)
			{
				if(monIA.verbose) System.out.println("BOMB SIZE");
				return true;
			}
			if(m.representation[t.getLine()][t.getCol()]<0)
			{
				if(monIA.verbose) System.out.println(stepsTaken+" steps, "+t+" case danger val : "+m.representation[t.getLine()][t.getCol()]+" minus : "+(monIA.getCurrentToleranceCofficient()/monIA.getUsualBombNormalDuration())+" is "+(m.representation[t.getLine()][t.getCol()]-(monIA.getCurrentToleranceCofficient()/monIA.getUsualBombNormalDuration())*stepsTaken));
				int cautionMargin=0;
				if(monIA.isPredicting())
					cautionMargin=10;
				if(m.representation[t.getLine()][t.getCol()]-(monIA.getCurrentToleranceCofficient()/monIA.getUsualBombNormalDuration())*stepsTaken<=-300+cautionMargin && i!=offset)
				{
					if(monIA.verbose) System.out.println("DANGER");
					if(monIA.verbose) System.out.println(m.representation[t.getLine()][t.getCol()]-stepsTaken*(monIA.getCurrentToleranceCofficient()/monIA.getUsualBombNormalDuration())+" and step count "+stepsTaken+","+p.getTile(stepsTaken));
					if(safePlaceFoundBeforeDanger!=-1)
					{
						if(monIA.verbose) System.out.println("But found an empty case before : "+stepsTaken+","+p.getTile(stepsTaken));
					}
					else if(!monIA.isInDangerAndWontMoveAnyway() ||
							(monIA.isInDangerAndWontMoveAnyway() && m.representation[t.getLine()][t.getCol()]<m.representation[monIA.getSelfHero().getLine()][monIA.getSelfHero().getCol()]))
						return true;
				}
			}

			if(monIA.getMode()==Mode.COLLECTE)
				if(stepsTaken>0 && t.getHeroes().size()>=2 || (t.getHeroes().size()==1 && t.getHeroes().get(0)!=monIA.getSelfHero()))
				{
					if(monIA.verbose) System.out.println("HEROES");
					if(!monIA.isInDangerAndWontMoveAnyway())
						return true;
				}			
			if((monIA.isPredicting() || !monIA.isInDangerAndWontMoveAnyway() && monIA.getSelfHero().getTile().getBombs().size()>0) && (t.getHeroes().size()==1 && t.getHeroes().get(0)!=monIA.getSelfHero()))
			{
				if(monIA.verbose) System.out.println("POTENTIAL TRAP INCOMING");
				if(!monIA.isInDangerAndWontMoveAnyway())
					return true;
			}
			CumulativeInterest+=m.representation[t.getLine()][t.getCol()];
			stepsTaken++;
		}
		return false;
	}
	
	/**
	 * Determine si le chemin recemment choisi est devenu dangereux
	 * @param m La matrice d'interet
	 * @return Oui si le chemin recemment choisi est devenu dangereux.
	 * @throws StopRequestException
	 */
	public static boolean hasLastPathBecomeDangerousOrObsolete(Matrix m) throws StopRequestException
	{
		monIA.checkInterruption();
		if(monIA.verbose) System.out.println("Looking for obselete check");
		
		if (monIA.lastPathChosenParameters==null)
			return true;
		
		AiPath p=monIA.lastPathChosenParameters.getLastPathChosen();
		int offset=monIA.lastPathChosenParameters.getCurrentPosition();
		return isThisPathDangerous(p,m,offset);
	}
}
