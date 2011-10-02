package org.totalboumboum.ai.v201011.ais.kesimalvarol.v6;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Representation de la zone du jeu.
 * 
 * @author Ali Baran Kesimal
 * @author Işıl Varol
 *
 */
@SuppressWarnings("deprecation")
public class Matrix implements Cloneable
{
	/** Pour checkInterruption. */
	private static KesimalVarol monIA;
	public static void setMonIA(KesimalVarol monIA) throws StopRequestException {
		monIA.checkInterruption();
		Matrix.monIA = monIA;
	}
	
	/** Notre matrice des valeurs */
	public double representation[][];
	/** Une deuxieme matrice a utiliser lors de prediction d'emplacement d'une nouvelle bombe  */
	public double representationBackupForBombPlacement[][]; //emplacement.
	/** Matrice a utiliser si on aura besoin de ajouter les parametres de distance aux cases neutres (valeur=0)   */
	public double representationDistanceParameters[][]; //distance for neutrals.
	/** Utilisee lors du calcul de posage des bombes  */
	public List<AiTile> bombRegionNodes;
	public static class regionEmplacementImportance {
		public int importance;
		public boolean forEnemy;
		public regionEmplacementImportance(int i,boolean f) throws StopRequestException
		{
			monIA.checkInterruption();
			importance=i;
			forEnemy=f;
		}
	}
	/** L'endroit d'emplacement pour laisser les bombes  */
	public regionEmplacementImportance regionEmplacementImportanceMatrix[][];
	/** Dimensions de la zone du jeu  */
	public int height,width;
	
	/**
	 * Sauvegarde une copie de matrice courant pour qu'on peut faire des predictions sur la posage de bombe
	 * @throws StopRequestException 
	 * 
	 */
	public void makeBackup() throws StopRequestException
	{
		monIA.checkInterruption();
		representationBackupForBombPlacement=new double[height][width];
		for(int i=0;i<height;i++)
		{
			monIA.checkInterruption();
			for(int j=0;j<width;j++)
			{
				monIA.checkInterruption();
				representationBackupForBombPlacement[i][j]=representation[i][j];
			}
		}
	}
	
	/**
	 * Renvoie la matrice reel apres la prediction
	 * @throws StopRequestException 
	 */
	public void restoreBackup() throws StopRequestException
	{
		monIA.checkInterruption();
		representation=representationBackupForBombPlacement;
	}

	/**
	 * Ctr. de la classe Matrix
	 * @param azone La zone du jeu dont les dimensions seront memorisees
	 */
	public Matrix(AiZone azone) throws StopRequestException
	{
		monIA.checkInterruption();
		height=azone.getHeight();
		width=azone.getWidth();
		representation=new double[height][width];
		regionEmplacementImportanceMatrix=new regionEmplacementImportance[height][width]; 
		for(int i=0;i<height;i++)
		{
			monIA.checkInterruption();
			for(int j=0;j<width;j++)
			{
				monIA.checkInterruption();
				regionEmplacementImportanceMatrix[i][j]=new regionEmplacementImportance(0,false);
			}
		}
		representationDistanceParameters=new double[height][width];
		bombRegionNodes=new ArrayList<AiTile>();
	}
	
	/**
	 * Indiquer une case dans l'endroit d'emplacement
	 * @param targetBlock Le mur sur lequel le calcul sera basee
	 * @param l Le ligne courante
	 * @param c Le colonne courante
	 * @param val Le valeur a affecter
	 */
	public void markForBombPlacementCandidate(AiBlock targetBlock,int l,int c,double val) throws StopRequestException
	{
		monIA.checkInterruption();
		
		if(targetBlock.getLine()<height && targetBlock.getCol()<width && targetBlock.getCol()>=0 && targetBlock.getLine()>=0)
		{	
			AiTile target=targetBlock.getTile();
			
			if(this.representation[l][c]>=0)
				this.representation[l][c]+=val;
			
			int portee=monIA.getSelfHero().getBombRange();
			int i=target.getLine(),j=target.getCol(),x=0,y=0;
			int vX=(l-i)*portee,vY=(c-j)*portee;
			int stepX=(l-i),stepY=(c-j);
			do
			{
				monIA.checkInterruption();
				do
				{
					monIA.checkInterruption();
					int position[]=new int[2];
					position[0]=l+x;
					position[1]=c+y;
					try {
						AiTile targtile=monIA.getZone().getTile(position[0], position[1]);
						if(targtile.getBlocks().size()>0 || targtile.getItems().size()>0)
							return;
						bombRegionNodes.add(monIA.getZone().getTile(position[0], position[1]));
						regionEmplacementImportanceMatrix[position[0]][position[1]].importance+=1;
					}
					catch(ArrayIndexOutOfBoundsException e)
					{
						
					}
					y+=stepY;
				} while(Math.abs(y)<Math.abs(vY));
				x+=stepX;
			} while(Math.abs(x)<Math.abs(vX));
		
		}
	}
	
	/**
	 * Indiquer une case dans l'endroit d'emplacement
	 * @param targetBlock Le mur sur lequel le calcul sera basee
	 * @param l Le ligne courante
	 * @param c Le colonne courante
	 * @param val Le valeur a affecter
	 */
	public void markForBombPlacementCandidate(AiHero targetHero,AiTile neighboringCase,double val) throws StopRequestException //,int l,int c,double val) throws StopRequestException
	{
		monIA.checkInterruption();
		AiTile target=targetHero.getTile();
		Direction propogationDirection=monIA.getZone().getDirection(target, neighboringCase);
		int portee=monIA.getSelfHero().getBombRange(),i=0;
		while(portee-i>0 && target.getBlocks().size()==0 && target.getItems().size()==0)
		{
			monIA.checkInterruption();
			target=target.getNeighbor(propogationDirection);
			bombRegionNodes.add(target);
			regionEmplacementImportanceMatrix[target.getLine()][target.getCol()].importance+=1;
			regionEmplacementImportanceMatrix[target.getLine()][target.getCol()].forEnemy=true;
			i++;
		}
		if(this.representation[neighboringCase.getLine()][neighboringCase.getCol()]>=0 && neighboringCase.getBlocks().size()==0)
			this.representation[neighboringCase.getLine()][neighboringCase.getCol()]+=val;
		
		if(this.representation[target.getLine()][target.getCol()]>=0 && target.getBlocks().size()==0)
			this.representation[target.getLine()][target.getCol()]+=(val*1.5); //Les extremités des endroits ont plus d'importance.
	}
	
	/**
	 * Optimisation des endroit d'emplacement
	 * @throws StopRequestException
	 */
	public void bestDirectionOptimisation() throws StopRequestException
	{
		monIA.checkInterruption();
		Set<AiTile> toIgnore=monIA.getTemporarilyIgnoredCases().keySet();
		for(AiTile k : toIgnore)
		{
			monIA.checkInterruption();
			regionEmplacementImportanceMatrix[k.getLine()][k.getCol()].importance=0;
		}
		monIA.checkInterruption();
		ArrayList<AiTile> tmp=new ArrayList<AiTile>();
		for(AiTile t : bombRegionNodes)
		{
			monIA.checkInterruption();
			int rimx=regionEmplacementImportanceMatrix[t.getLine()][t.getCol()].importance;
			boolean shallAdd=true;
			if(rimx>=1)
			{
				for(AiTile t2 : t.getNeighbors())
				{
					monIA.checkInterruption();
					if(!regionEmplacementImportanceMatrix[t2.getLine()][t2.getCol()].forEnemy && regionEmplacementImportanceMatrix[t2.getLine()][t2.getCol()].importance<rimx)
					{
						regionEmplacementImportanceMatrix[t2.getLine()][t2.getCol()].importance=0;
					}
					else if(!regionEmplacementImportanceMatrix[t.getLine()][t.getCol()].forEnemy && regionEmplacementImportanceMatrix[t2.getLine()][t2.getCol()].importance>rimx)
					{
						regionEmplacementImportanceMatrix[t.getLine()][t.getCol()].importance=0;
						shallAdd=false;
						break;
					}
				}
				if(shallAdd && t.getBlocks().size()==0 && t.isCrossableBy(monIA.getSelfHero()))
				{
					if(this.representation[t.getLine()][t.getCol()]>=0)
					{
						representation[t.getLine()][t.getCol()]+=regionEmplacementImportanceMatrix[t.getLine()][t.getCol()].importance*3;
					}
					tmp.add(t);
				}
			}
		}
		bombRegionNodes=tmp;
	}
	
	//---------------------
	// Methodes a utiser lors de debogage
	//---------------------
	/**
	 * Coloriser l'ecran pour debogage
	 * @throws StopRequestException 
	 */
	public void colorizeMapAccordingly() throws StopRequestException
	{
		monIA.checkInterruption();
		AiOutput out=monIA.getOutput();
		for(int i=0;i<this.height;i++)
		{
			monIA.checkInterruption();
			for(int j=0;j<this.width;j++)
			{
				monIA.checkInterruption();
				int interet=(int)representation[i][j];
				if(interet!=0) {
					out.setTileText(i, j, Integer.toString(interet));
					int colR=(-255*interet)/300,colG=(255*interet)/100;
					if (colR>255)
						colR=255;
					if (colR<0)
						colR=0;
					if (colG>255)
						colG=255;
					if (colG<0)
						colG=0;
					out.setTileColor(i, j, new Color(colR, colG, 0));
				}
			}
		}
		for(AiTile t : bombRegionNodes)
		{
			monIA.checkInterruption();
			out.setTileColor(t, new Color(0,0,255));
		}
	}
}