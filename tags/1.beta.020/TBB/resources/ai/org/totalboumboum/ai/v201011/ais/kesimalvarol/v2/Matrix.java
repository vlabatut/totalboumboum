package org.totalboumboum.ai.v201011.ais.kesimalvarol.v2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

/**
 * Representation de la zone du jeu
 * 
 * @author Ali Baran Kesimal
 * @author Işıl Varol
 *
 */
public class Matrix implements Cloneable
{
	/** Pour checkInterruption. */
	private static KesimalVarol monIA;
	public static void setMonIA(KesimalVarol monIA) {
		Matrix.monIA = monIA;
	}
	
	/** Notre matrice des valeurs */
	public int representation[][];
	/** Une deuxieme matrice a utiliser lors de prediction d'emplacement d'une nouvelle bombe  */
	public int representationBackupForBombPlacement[][]; //emplacement.
	/** Matrice a utiliser si on aura besoin de ajouter les parametres de distance aux cases neutres (valeur=0)   */
	public int representationDistanceParameters[][]; //distance for neutrals.
	/** Utilisee lors du calcul de posage des bombes  */
	public List<AiTile> bombRegionNodes;
	/** L'endroit d'emplacement pour laisser les bombes  */
	public int regionImportanceMatrix[][];
	/** Dimensions de la zone du jeu  */
	public int height,width;
	
	/*
	private AiTile highestImportance;
	public AiTile getHighestImportance() {
		return highestImportance;
	}

	public void setHighestImportance(AiTile highestImportance) {
		this.highestImportance = highestImportance;
	}
	*/
	
	/**
	 * Sauvegarde une copie de matrice courant pour qu'on peut faire des predictions sur la posage de bombe
	 * 
	 */
	public void makeBackup()
	{
		representationBackupForBombPlacement=new int[height][width];
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				representationBackupForBombPlacement[i][j]=representation[i][j];
			}
		}
	}
	
	/**
	 * Renvoie la matrice reel apres la prediction
	 */
	public void restoreBackup()
	{
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
		representation=new int[height][width];
		regionImportanceMatrix=new int[height][width]; 
		representationDistanceParameters=new int[height][width];
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
		AiTile target=targetBlock.getTile();
		this.representation[l][c]+=val;
		
		int portee=monIA.getSelfHero().getBombRange();
		////!!System.out.println(portee);
		int i=target.getLine(),j=target.getCol(),x=0,y=0;
		int vX=(l-i)*portee,vY=(c-j)*portee;
		int stepX=(l-i),stepY=(c-j);
		////!!System.out.println(x+" "+y+" "+vX+" "+vY);
		do
		{
			do
			{
				int position[]=new int[2];
				position[0]=l+x;
				position[1]=c+y;
				try {
					if(!monIA.getZone().getTile(position[0], position[1]).isCrossableBy(monIA.getSelfHero()))
						return;
					bombRegionNodes.add(monIA.getZone().getTile(position[0], position[1]));
					regionImportanceMatrix[position[0]][position[1]]+=1;
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					
				}
				y+=stepY;
			} while(Math.abs(y)<Math.abs(vY));
			x+=stepX;
		} while(Math.abs(x)<Math.abs(vX));
	}
	
	/**
	 * Indiquer une case dans l'endroit d'emplacement
	 * @param targetBlock Le mur sur lequel le calcul sera basee
	 * @param l Le ligne courante
	 * @param c Le colonne courante
	 * @param val Le valeur a affecter
	 */
	public void markForBombPlacementCandidate(AiHero targetHero,int l,int c,double val) throws StopRequestException
	{
		monIA.checkInterruption();
		AiTile target=targetHero.getTile();
		this.representation[l][c]+=val;
		
		int portee=monIA.getSelfHero().getBombRange();
		////!!System.out.println(portee);
		int i=target.getLine(),j=target.getCol(),a=0,b=0;
		int vA=(l-i)*portee,vB=(c-j)*portee;
		int stepA=(l-i),stepB=(c-j);
		////!!System.out.println(x+" "+y+" "+vX+" "+vY);
		do
		{
			do
			{
				int position[]=new int[2];
				position[0]=l+a;
				position[1]=c+b;
				try {
					if(!monIA.getZone().getTile(position[0], position[1]).isCrossableBy(targetHero))
						return;
					bombRegionNodes.add(monIA.getZone().getTile(position[0], position[1]));
					regionImportanceMatrix[position[0]][position[1]]+=1;
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					
				}
				b+=stepB;
			} while(Math.abs(b)<Math.abs(vB));
			a+=stepA;
		} while(Math.abs(a)<Math.abs(vA));
	}
	
	/**
	 * Optimisation des endroit d'emplacement
	 * @throws StopRequestException
	 */
	public void bestDirectionOptimisation() throws StopRequestException
	{
		monIA.checkInterruption();
		ArrayList<AiTile> tmp=new ArrayList<AiTile>();
		for(AiTile t : bombRegionNodes)
		{
			int rimx=regionImportanceMatrix[t.getLine()][t.getCol()];
			boolean shallAdd=true;
			if(rimx>=1)
			{
				for(AiTile t2 : t.getNeighbors())
				{
					if(regionImportanceMatrix[t2.getLine()][t2.getCol()]<rimx)
					{
						regionImportanceMatrix[t2.getLine()][t2.getCol()]=0;
					}
					else if(regionImportanceMatrix[t2.getLine()][t2.getCol()]>rimx)
					{
						regionImportanceMatrix[t.getLine()][t.getCol()]=0;
						shallAdd=false;
						break;
					}
				}
				if(shallAdd)
				{
					representation[t.getLine()][t.getCol()]+=regionImportanceMatrix[t.getLine()][t.getCol()]*3;
					tmp.add(t);
				}
			}
		}
		bombRegionNodes=tmp;
	}
	
	/*
	public void requestBonusImportanceIncrease()
	{
		for(AiItem i : monIA.getZone().getItems())
		{
			representationBackupForBombPlacement[i.getLine()][i.getCol()]+=100;
		}
	}*/
	/*
	public List<AiTile> getReachableCasesOnSameColOrLine(int l,int c,int portee) throws StopRequestException
	{
		monIA.checkInterruption();
		List<AiTile> tmp=new ArrayList<AiTile>();
		for(AiTile t : bombRegionNodes)
		{
			if((Math.abs(t.getCol()-c)-portee+1>0) || (Math.abs(t.getLine()-l)-portee+1>0))
				tmp.add(t);
		}
		return tmp;
	}
	
	public void applyDistanceToAll()
	{
		for(int i=0;i<this.height;i++)
		{
			for(int j=0;j<this.width;j++)
			{
				representation[i][j]+=representationDistanceParameters[i][j];
			}
		}
	}
	*/
	
	//---------------------
	// Methodes a utiser lors de debogage
	//---------------------
	/**
	 * Coloriser l'ecran pour debogage
	 */
	public void colorizeMapAccordingly()
	{
		AiOutput out=monIA.getOutput();
		for(int i=0;i<this.height;i++)
		{
			for(int j=0;j<this.width;j++)
			{
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
			out.setTileColor(t, new Color(0,0,255));
		}
	}
}