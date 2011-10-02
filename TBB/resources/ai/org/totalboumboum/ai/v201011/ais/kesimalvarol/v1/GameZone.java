package org.totalboumboum.ai.v201011.ais.kesimalvarol.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.ais.kesimalvarol.v1.KesimalVarol;


/**
 * La classe qui conduisse les opérations concernant la zone du jeu et les matrices.
 * 
 * @author Ali Baran Kesimal
 * @author Işıl Varol
 *
 */
@SuppressWarnings({ "unused", "deprecation" })
public class GameZone
{	/** classe principale de l'IA, permet d'accéder à checkInterruption() */
	private static KesimalVarol monIA;
	public static void setMonIA(KesimalVarol monIA) {
		GameZone.monIA = monIA;
		Matrix.setMonIA(monIA);
	}
	
	/** Structure interne pour gerer les explosions enchainés */
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
	
	/** Methode pour construire la matrice */
	public Matrix constructInterestMatrix() throws StopRequestException
	{
		monIA.checkInterruption();
		Matrix m=new Matrix(monIA.getZone());
		bombsCurrent=new ArrayList<bombDetailed>();
		List<AiBomb> cbombs=monIA.getZone().getBombs();
		
		
		/*
		 * On doit gerer les bombes du maniere different.
		 * On va iterer les bombes quatre fois, pour : Initializer -> Determiner lesquels sont effectués par les explosions -> Choisir les valeurs -> Ecrire les valeurs dans la matrice
		 */
		//D'abord, on initialize l'arbre avec toutes les bombes pour qu'elles soient presentes.
		for (AiBomb bomb : cbombs)
		{
			double delay=((double)bomb.getNormalDuration()-bomb.getTime());
			if (delay<0) { //Ça veut dire que bombe est tombé en panne
				delay=0;
			}
			delay=-300+100.0*(delay/(double)bomb.getNormalDuration());
			bombsCurrent.add(new bombDetailed(bomb,delay));
		}
		//Puis, on change les valeurs des bombes qui seront effectués par les autres.
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
				if(m.representation[i][j]>(int)bdet.delay)
					m.representation[i][j]=(int)bdet.delay;
			}
		}
		
		//Ensuite, on fait des calcules pour le reste des entités
		for(int i=0;i<m.height;i++)
		{
			for(int j=0;j<m.width;j++)
			{
				assignBasicNums(m,i,j);
			}
		}
		
		return m;
	}
	/** Pour attribuer des valeurs */
	private void assignBasicNums(Matrix m,int i,int j) throws StopRequestException
	{
		monIA.checkInterruption();
		AiTile currtile=monIA.getZone().getTile(i,j);
		List<AiFire> fires=currtile.getFires();
		if (fires.size()>0) {
			m.representation[i][j]=-300;
			//S'il y en a du feu, on n'a rien autre opération a faire : Il faut éviter cette case.
			return;
		}
		
		int modifier;
		int a=currtile.getLine(),b=currtile.getCol();
		
		//Bonus
		if(monIA.getMode()==Mode.COLLECTE)
			modifier=50;
		else
			modifier=15;
		if(m.representation[i][j]>-265) //Notre limite minimale est -265 : On peut tolérer une bombe laissé de nouveau, mais si la valeur est trop inferieure, on ne le modifie pas.
			for (int c=0;c<currtile.getItems().size();c++)
				m.representation[a][b]+=modifier;
		
		//Adversaires
		int neighbModifier=-10;
		if(monIA.getMode()==Mode.COLLECTE)
			modifier=-40;
		else {
			modifier=-5;
			//neighbModifier=25; pour mode attaque
		}
		for(AiHero hero : currtile.getHeroes())
		{
			if(hero!=GameZone.monIA.getSelfHero()) //Si celle-ci n'est pas notre propre agent
			{
				m.representation[a][b]+=modifier;
				/*for (AiTile t : hero.getTile().getNeighbors())
				{
					if(t.getCol()>=0 && t.getLine()>=0) {
						m.representation[t.getLine()][t.getCol()]+=neighbModifier;
					}
				}*/
			}
		}
		
		//Murs
		if(monIA.getMode()==Mode.COLLECTE)
			modifier=10;
		else
			return; //modifier=0 dans ce cas, on évite le boucle.
		for(AiBlock block : currtile.getBlocks())
		{
			if(block.isDestructible()) {
				for (AiTile t : currtile.getNeighbors()) {
					if(t.getCol()>=0 && t.getLine()>=0) {
						m.representation[t.getLine()][t.getCol()]+=modifier;
						for(int c=0;c<t.getBlocks().size();c++) {
							m.representation[t.getLine()][t.getCol()]-=modifier; //Les murs resteront neutres.
						}
					}
				}
			}
		}
		
	}
	
	/*
	public Matrix constuctInterestMatrixForEnemy(AiHero enemy) throws StopRequestException
	{
		monIA.checkInterruption();
		return new Matrix(monIA.getZone());
	}*/
}
