package org.totalboumboum.ai.v201011.ais.hacibektasogluilgar.v5;

import java.awt.Color;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItemType;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;

/**
 * @author Engin Hacıbektaşoğlu
 * @author Elif Nurdan İlgar
 */
public class Action {
	
	HacibektasogluIlgar hi;
	int hauteur;
	int largeur;
	AiZone zone;
	AiHero notreHero;
	Etats[][] matriceZone;
	double[][] matriceTotal;
	CalculeZone cz;
	
	
	public Action(HacibektasogluIlgar hi,Etats[][] matriceZone,double[][] matriceTotal,CalculeZone cz) throws StopRequestException
	{
		hi.checkInterruption();
		this.hi=hi;

		zone=hi.getPercepts();
		notreHero=zone.getOwnHero();
		this.hauteur=zone.getHeight();
		this.largeur=zone.getWidth();
		this.matriceZone=matriceZone;
		this.matriceTotal=matriceTotal;
		this.cz=cz;
	}
	

	/**
	 * on va utiliser cette methode pour calculer les points des cases autour d'un mur 
	 * desctructible ou bien autour d'une adversaire   
	 *
	 * @param matriceTotal 
	 * @param i
	 * @param j
	 * @param note
	 * @param type si on utilise cette methode pour les murs alors type=1, si pour les adversaires type=2 , si pour le bombe imaginaire type=3  
	 * @throws StopRequestException
	 */
	public void calculeDapresPortee(double matriceTotal[][],AiHero notreHero, int i,int j,int note,int type) throws StopRequestException
	{
		hi.checkInterruption();
		
		int porteeDuBomb=notreHero.getBombRange();
		int c=1,d;
		
		Color mur= new Color(0, 191, 255);
		Color ennemi= new Color(236,53,232); 
		AiOutput output = hi.getOutput();
		
		
		// calcule pour les tiles verticales
		for(int b=0;b<2;b++)
		{
			hi.checkInterruption();
			
			for(int a=1;a<porteeDuBomb+1;a++)
			{
				hi.checkInterruption();
				
				d=c*a;
				int x=i-d;
				if(x<0) x+=hauteur;
				else if(x>=hauteur) x-=hauteur;


				if((matriceZone[x][j]==Etats.VIDE 
						|| matriceZone[x][j]==Etats.VIDE_PRIORITAIRE
						|| matriceZone[x][j]==Etats.VIDE_AU_MUR_DESTRUCTIBLE
						|| matriceZone[x][j]==Etats.ADVERSAIRE))
				{
					
					// pour les murs et les adversaires 
					if(type==1 || type==2)
					{
						// on calcule le point d'une telle tile comme (point/distance_entre_l'héro_et_la_case				
										
						if(type==2) 
						{
//							int distance=zone.getTileDistance(notreHero.getLine(),notreHero.getCol(), x, j);
//							if(distance==0) distance=1;
//							matriceTotal[x][j]+=note/distance;
							matriceTotal[x][j]+=note;
							
							output.setTileColor(x, j,ennemi);
							if(matriceZone[x][j]!=Etats.ADVERSAIRE)
								matriceZone[x][j]=Etats.VIDE_PRIORITAIRE;
						}
						else 
						{
							matriceTotal[x][j]+=note;
							output.setTileColor(x, j,mur);
							if(matriceZone[x][j]!=Etats.ADVERSAIRE)
								matriceZone[x][j]=Etats.VIDE_AU_MUR_DESTRUCTIBLE;
						}
					}
					// pour l'utilisation d'imaginaire bomb
					else
						if(type==3) matriceTotal[x][j]=note;
					
				}
				else break;
			}
			c=-1;
		}		

		// calcule pour les tiles horizontales
		c=1;
		for(int b=0;b<2;b++)
		{
			hi.checkInterruption();
			
			for(int a=1;a<porteeDuBomb+1;a++)
			{
				hi.checkInterruption();
				
				d=c*a;
				int y=j-d;
				if(y<0) y+=hauteur;
				else if(y>=hauteur) y-=hauteur;
				if((matriceZone[i][y]==Etats.VIDE 
						|| matriceZone[i][y]==Etats.VIDE_PRIORITAIRE
						|| matriceZone[i][y]==Etats.VIDE_AU_MUR_DESTRUCTIBLE
						|| matriceZone[i][y]==Etats.ADVERSAIRE))
				{
					
					// pour les murs et adversaires
					if(type==1 || type==2)
					{
						// on calcule le point d'une telle tile comme (point/distance_entre_l'héro_et_la_case


						
						if(type==2) 
						{
						
//							int distance=zone.getTileDistance(notreHero.getLine(),notreHero.getCol(), i, y);
//							if(distance==0) distance=1;
//							matriceTotal[i][y]+=note/distance;
							matriceTotal[i][y]+=note;
							
							output.setTileColor(i, y,ennemi);
							if(matriceZone[i][y]!=Etats.ADVERSAIRE)
								matriceZone[i][y]=Etats.VIDE_PRIORITAIRE;
						}
						else 
						{
							matriceTotal[i][y]+=note;
							output.setTileColor(i, y,mur);
							if(matriceZone[i][y]!=Etats.ADVERSAIRE)
								matriceZone[i][y]=Etats.VIDE_AU_MUR_DESTRUCTIBLE;
						}
					}
					// pour l'utilisation d'imaginaire bomb
					else 
						if(type==3) matriceTotal[i][y]=note;
					
				}
				else break;
			}
			c=-1;
		}		

	}

	/**
	 * cette methode calcule nouvelle matrice d'apres un bomb non-reel qu'on veut y mettre en position(line,col)
	 * @param line
	 * @param col
	 * @param matriceTotal matrice reel qui est deja calcule pour jeu
	 * @return matrice imaginaire
	 * @throws StopRequestException
	 */
	public double[][] matricePourImaginaireBombe(int line,int col, double[][] matriceTotal) throws StopRequestException
	{
		hi.checkInterruption();
		double[][] matriceImaginaire= new double[hauteur][largeur];
		for(int i=0;i<hauteur;i++)
		{
			hi.checkInterruption();
			for(int j=0;j<largeur;j++)
			{
				hi.checkInterruption();
				matriceImaginaire[i][j]=matriceTotal[i][j];
			}
		}
		
		calculeDapresPortee(matriceImaginaire,notreHero,line,col,-10,3);
		matriceImaginaire[line][col]=-10;
		
		return matriceImaginaire;
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

		if(notreHero.getBombNumberMax()<maxNbBonus)
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
						if(notreHero.getBombRange()<5)
						{
							nbFlammeExtra++;
						}
					}
					else if(cz.bonuses[i].getType().equals(AiItemType.EXTRA_BOMB))
					{
						if(notreHero.getBombNumberMax()<5)
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
