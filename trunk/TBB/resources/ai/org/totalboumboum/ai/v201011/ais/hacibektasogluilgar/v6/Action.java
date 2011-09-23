package org.totalboumboum.ai.v201011.ais.hacibektasogluilgar.v6;

import java.awt.Color;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItemType;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Engin Hacıbektaşoğlu
 * @author Elif Nurdan İlgar
 */
public class Action {
	
	HacibektasogluIlgar hi;
	int hauteur;
	int largeur;
	AiZone zone;
	AiHero notreHeros;
	Etats[][] matriceZone;
	double[][] matriceTotal;
	CalculeZone cz;
	
	
	public Action(HacibektasogluIlgar hi,Etats[][] matriceZone,double[][] matriceTotal,CalculeZone cz) throws StopRequestException
	{
		hi.checkInterruption();
		this.hi=hi;

		zone=hi.getPercepts();
		notreHeros=zone.getOwnHero();
		this.hauteur=zone.getHeight();
		this.largeur=zone.getWidth();
		this.matriceZone=matriceZone;
		this.matriceTotal=matriceTotal;
		this.cz=cz;
	}
	

	/**
	 * 
	 *  on va utiliser cette methode pour calculer les points des cases autour d'un mur 
	 * desctructible ou bien autour d'une adversaire   
	 *
	 * @param tileActuelle
	 * @param matrice
	 * @param porte
	 * @param direct
	 * @param note
	 * @param color
	 * @param control
	 * @return
	 * @throws StopRequestException
	 */
	private int voisinesParDirection(AiTile tileActuelle,double[][] matrice,int porte,Direction direct,int note,Color color,int control) throws StopRequestException  
	{
		hi.checkInterruption();
		
		if(porte==0 || !tileActuelle.isCrossableBy(notreHeros) || control==-1)
		{
		}
		else
		{
			if(matrice[tileActuelle.getLine()][tileActuelle.getCol()]>=0)
			{
				if(note<0)
				{
					matrice[tileActuelle.getLine()][tileActuelle.getCol()]=note;
				}
				else matrice[tileActuelle.getLine()][tileActuelle.getCol()]+=note;
			}
			else
			{
				if(matrice[tileActuelle.getLine()][tileActuelle.getCol()]<=-100 && note<0)
				{
					control=-1;
				}
				if(note>=0)
				{
					
				}
			}
			
			AiOutput output=hi.getOutput();
			output.setTileColor(tileActuelle, color);

			control=voisinesParDirection(tileActuelle.getNeighbor(direct), matrice, porte-1, direct, note, color,control);
		}
		
		return control;
	}
	
	
	/**
	 * 
	 * @param tileActuelle
	 * @param matrice
	 * @param note
	 * @param color
	 * @return
	 * @throws StopRequestException
	 */
	public int calculeDapresPorte(AiTile tileActuelle,double[][] matrice,int note,Color color) throws StopRequestException
	{
		hi.checkInterruption();
		int control=0;
		int porte=notreHeros.getBombRange();
		
		control=voisinesParDirection(tileActuelle.getNeighbor(Direction.UP), matrice, porte, Direction.UP, note,color,control);
		control=voisinesParDirection(tileActuelle.getNeighbor(Direction.RIGHT), matrice, porte, Direction.RIGHT, note,color,control);
		control=voisinesParDirection(tileActuelle.getNeighbor(Direction.DOWN), matrice, porte, Direction.DOWN, note,color,control);
		control=voisinesParDirection(tileActuelle.getNeighbor(Direction.LEFT), matrice, porte, Direction.LEFT, note,color,control);
		
		return control;
	}

	/**
	 * cette methode calcule nouvelle matrice d'apres un bomb non-reel qu'on veut y mettre en position(line,col)
	 * @param line
	 * @param col
	 * @param matriceTotal matrice reel qui est deja calcule pour jeu
	 * @return matrice imaginaire
	 * @throws StopRequestException
	 */
	public int matricePourImaginaireBombe(int line,int col, double[][] matriceTotal,double[][] matriceImaginaire) throws StopRequestException
	{
		hi.checkInterruption();
		int control=0;
		
		for(int i=0;i<hauteur;i++)
		{
			hi.checkInterruption();
			for(int j=0;j<largeur;j++)
			{
				hi.checkInterruption();
				matriceImaginaire[i][j]=matriceTotal[i][j];
			}
		}
		
		Color imaginaire=new Color(200, 180, 120);
		
		control=calculeDapresPorte(zone.getTile(line, col), matriceImaginaire, -10, imaginaire);
		matriceImaginaire[line][col]=-10;
		
		return control;
	}
	
	/**
	 * cette methode controle le chemin pour voir s'il y en a des tiles dangeraux, 
	 * s'il les trouve alors il va nous dire "réfléchissez encore une fois avant y aller"
	 * 
	 * @param pathCible le chemin à controller
	 * @return la resultat du control (true ou false)
	 * @throws StopRequestException
	 */
	public boolean pathCibleControl(AiPath pathCible) throws StopRequestException
	{
		hi.checkInterruption();
		boolean control=true;
		int i;
		
		int longueur=pathCible.getLength();
		
		for(i=0;i<longueur;i++)
		{
			hi.checkInterruption();
			
			int line=pathCible.getTile(i).getLine();
			int col=pathCible.getTile(i).getCol();
			if(matriceTotal[line][col]<0)
			{
				control=false;
				break;
			}
		}
		
		if(!control)
		{
			int j;
			for(j=i;j<longueur;j++)
			{
				hi.checkInterruption();
				pathCible.removeTile(pathCible.getTile(i));
			}
		}
		
		return control;
		
	}

	/**
	 * 
	 * @param matrice
	 * @return
	 * @throws StopRequestException
	 * @throws LimitReachedException
	 */
	public AiAction choisirLeMode(Etats matrice[][]) throws StopRequestException, LimitReachedException
	{
		hi.checkInterruption();
		AiAction result;
		int maxNbBonus=0;
		CalculeAttaque ca=new CalculeAttaque(hi,cz, this);
		CalculeCollecte cc=new CalculeCollecte(hi,cz,this);
		
		
		for(int x=0;x<cz.nbAdversaire;x++)
		{
			hi.checkInterruption();
			 
			int nbBonusDeAdversaire=cz.adversairesActuelles[x].getBombNumberMax();
			if(nbBonusDeAdversaire>maxNbBonus) maxNbBonus=nbBonusDeAdversaire;
		}

		if(notreHeros.getBombNumberMax()<maxNbBonus)
		{
			if(cz.nbBonus>0)
			{
				int nbFlammeExtra=0;
				int nbBonusExtra=0;
				for(int i=0;i<cz.nbBonus;i++)
				{
					hi.checkInterruption();
					if(cz.bonuses[i].getType().equals(AiItemType.EXTRA_FLAME))
					{
						if(notreHeros.getBombRange()<5)
						{
							nbFlammeExtra++;
						}
					}
					else if(cz.bonuses[i].getType().equals(AiItemType.EXTRA_BOMB))
					{
						if(notreHeros.getBombNumberMax()<5)
						{
							nbBonusExtra++;
						}
					}
				}
				if(nbFlammeExtra>0 || nbBonusExtra>0)
				{
					result=cc.calculeMatriceCollecte(matrice, matriceTotal);
				}
				else result=ca.calculeMatriceAttaque(matrice, matriceTotal);
			}
			else if(cz.nbMurDestructible>0)
			{
				result=cc.calculeMatriceCollecte(matrice, matriceTotal);
			}else 
				result=ca.calculeMatriceAttaque(matrice, matriceTotal);
		}
		else result=ca.calculeMatriceAttaque(matrice, matriceTotal);

//		result=cc.calculeMatriceCollecte(matrice, matriceTotal);
		
		return result;
	}

	
	
	/**
	 * methode affichante la matriceTotale en console et en zone de jeu à la fois
	 * 
	 * @param matriceTotal
	 * @throws StopRequestException
	 */
	public void afficherMatrice(double matriceTotal[][]) throws StopRequestException
	{
		hi.checkInterruption();
		
		for(int i=0;i<hauteur;i++)
		{
			hi.checkInterruption();
			
			for(int j=0;j<largeur;j++)
			{
				hi.checkInterruption();
				AiOutput output = hi.getOutput();
				output.setTileText(i, j, ""+matriceTotal[i][j]);

			}
		}
	}

	
}
