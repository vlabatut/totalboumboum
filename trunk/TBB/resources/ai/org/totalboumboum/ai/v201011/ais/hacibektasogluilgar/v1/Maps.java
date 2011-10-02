package org.totalboumboum.ai.v201011.ais.hacibektasogluilgar.v1;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

/**
 * @author Engin Hacıbektaşoğlu
 * @author Elif Nurdan İlgar
 */
@SuppressWarnings("deprecation")
public class Maps 
{
	private HacibektasogluIlgar notreIA;
	AiZone zone;
	Etats matriceCollecte[][];
	float matriceTotal[][];
	int height;
	int width;
	AiHero ourHero;
	int nbAdversaire = 0;
	AiHero AdversairesActuelles[]; 
	
	public Maps(HacibektasogluIlgar notreIA, AiZone zone,Etats matriceCollecte[][])
	{
		this.notreIA=notreIA;
		this.matriceCollecte=matriceCollecte;
		this.zone=zone;
		height=zone.getHeight();
		width=zone.getWidth();
		ourHero= zone.getOwnHero();
		matriceTotal= new float[height][width];
		AdversairesActuelles = new AiHero[10];
	}
	
	/**
	 * on initialise la matrice en lui pensent comme tous ses tiles sont vide 
	 */
	public void initialiserMatrices(Etats matrice[][]) throws StopRequestException 
	{
		notreIA.checkInterruption();

		for(int i=0;i<height;i++)
		{
			notreIA.checkInterruption();
			for(int j=0;j< width;j++)
			{
				notreIA.checkInterruption();	
				matrice[i][j]=Etats.vide;
				matriceTotal[i][j]=0;
			}
		}
	}
	
	public void calculeMurs(Etats matrice[][])throws StopRequestException 
	{
		notreIA.checkInterruption();
		
		Collection<AiBlock> murs=zone.getBlocks();
		Iterator<AiBlock> iterMur=murs.iterator();
		
		while(iterMur.hasNext())
		{
			notreIA.checkInterruption();
			AiBlock mur = iterMur.next();
			if(mur.isDestructible())
			{
				matrice[mur.getLine()][mur.getCol()]=Etats.murFaible;
			}
			else matrice[mur.getLine()][mur.getCol()]=Etats.murFort;
			
		}
	
	}
	


	public void calculeBombs(Etats matrice[][]) throws StopRequestException
	{
		notreIA.checkInterruption();
		Collection<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> iterBombs = bombs.iterator();
		
		while(iterBombs.hasNext())
		{
			notreIA.checkInterruption();
			AiBomb bomb = iterBombs.next();
			int x=bomb.getLine();
			int y=bomb.getCol();


			List<AiTile> inScopeTiles = bomb.getBlast();
			for (int i = 0; i < inScopeTiles.size(); i++) {
				notreIA.checkInterruption();
				/*
				 *  on controle la duree de la bomb pour pouvoir comprendre si
				 *  c'est bien de passer par ce tile ou non   
				 */
				
				if(bomb.getTime()<bomb.getExplosionDuration()/2)
				matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()] = Etats.flammableVert;
				else if(bomb.getTime()<bomb.getExplosionDuration())
				matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()] = Etats.flammableRouge;
			}
			
			if(matrice[x][y]==Etats.adversaire) 
				matrice[x][y]=Etats.adversaireBomb;
			else 
				if(matrice[x][y]==Etats.ourHero)
					matrice[x][y]=Etats.ourHeroBomb;
				else
					matrice[x][y]=Etats.bomb;

		}
	}
	
	public void calculeFeu(Etats matrice[][]) throws StopRequestException
	{
		notreIA.checkInterruption();
		Collection<AiFire> feux= zone.getFires();
		Iterator<AiFire> iterFeux = feux.iterator();
		
		while(iterFeux.hasNext())
		{
			notreIA.checkInterruption();
			AiFire feu=iterFeux.next();
			matrice[feu.getLine()][feu.getCol()]=Etats.feu;
			
		}
		
		
	}
	
	public void calculeBonus (Etats matrice[][]) throws StopRequestException
	{
		notreIA.checkInterruption();
		
		Collection<AiItem> items = zone.getItems();
		Iterator<AiItem> iterItems = items.iterator();
		while (iterItems.hasNext()) {
			notreIA.checkInterruption();
			AiItem item = iterItems.next();
			int x=item.getLine();
			int y=item.getCol();
			
			if(matrice[x][y]==Etats.adversaire) 
				matrice[x][y]=Etats.adversaireBonus;
			else 
				if(matrice[x][y]==Etats.ourHero)
					matrice[x][y]=Etats.ourHeroBonus;
				else
					matrice[x][y] = Etats.bonus;
		}
	}
	
	public void calculeAdversaire(Etats matrice[][]) throws StopRequestException
	{
		notreIA.checkInterruption();
		
		Collection<AiHero> adversaires = zone.getRemainingHeroes();
		Iterator<AiHero> iterAdversaires = adversaires.iterator();
		
		while(iterAdversaires.hasNext())
		{
			notreIA.checkInterruption();
			int i=0;
			AiHero adversaire = iterAdversaires.next();
			if(adversaire!=ourHero)
				{
					nbAdversaire++;
					matrice[adversaire.getLine()][adversaire.getCol()]=Etats.adversaire;
					AdversairesActuelles[i]=adversaire;
					i++;
				}
			else 
				matrice[adversaire.getLine()][adversaire.getCol()]=Etats.ourHero;
		}
	}
	
	public Etats[][] misAJourEtatsMatrice(Etats matrice[][]) throws StopRequestException
	{
		notreIA.checkInterruption();
		
		initialiserMatrices(matrice);
		calculeMurs(matrice);
		calculeAdversaire(matrice);
		calculeBombs(matrice);
		calculeBonus(matrice);
		calculeFeu(matrice);
		return matrice;
		
	}
	
	/**
	 * cette methode nous aidera pour comparer l'etat dans la matrice et ajouter des valeurs
	 * d'apres son situation a la matrice totale
	 * 
	 * @param matrice = matrice d'etats qui est deja construit pour la zone actuelle
	 * @param i = ligne de la matrice
	 * @param j = colone de la matrice
	 * @param etat = l'etat qu'on veux compaire avec notre matrice d'etats
	 * @param note = la valeur qu'on va ajouter a la matriceTotal
	 * @param matriceTotal = c'est une matrice qui garde les calcules de tiles
	 * @return
	 */
	private void calculeOperationMatrice(Etats matrice[][],int i,int j,Etats etat,float note,float matriceTotal[][])
	{
		
			if(matrice[i][j]==etat)
			matriceTotal[i][j]+=note;
		}
	
	/**
	 * 
	 * @param matrice
	 * @return 
	 * @throws StopRequestException
	 */
	public AiAction calculeTotalDeMatrice(Etats matrice[][]) throws StopRequestException
	{
		notreIA.checkInterruption();
	
		AiAction result;
		
		for(int i=0;i<height;i++)
		{
			notreIA.checkInterruption();
			
			for(int j=0;j<width;j++)
			{
				notreIA.checkInterruption();
				
				switch (matrice[i][j]) {
				case vide:
					matriceTotal[i][j]=0;
					break;
				case murFaible:
					matriceTotal[i][j]+=-100;
					if(i-1<height && i-1>0)calculeOperationMatrice(matrice, i-1, j, Etats.vide, 10, matriceTotal);
					if(i+1<height && i+1>0)calculeOperationMatrice(matrice, i+1, j, Etats.vide, 10, matriceTotal);
					if(j-1<width && j-1>0)calculeOperationMatrice(matrice, i, j-1, Etats.vide, 10, matriceTotal);
					if(j+1<width && j+1>0)calculeOperationMatrice(matrice, i, j+1, Etats.vide, 10, matriceTotal);
					break;
				case murFort:
					matriceTotal[i][j]+=-100;
					break;
				case bomb:
					matriceTotal[i][j]+=-100;
					break;
				case adversaireBomb:
					matriceTotal[i][j]+=-100;
					break;
				case flammableVert:
					matriceTotal[i][j]+=-50;
					break;
				case flammableRouge:
					matriceTotal[i][j]+=-100;
					break;
				case feu:
					matriceTotal[i][j]+=-100;
					break;
				case adversaire:
					matriceTotal[i][j]+=20;
					if(i-2<height && i-2>0) calculeOperationMatrice(matrice, i-2, j, Etats.vide, 20, matriceTotal);
					if(i-1<height && i-1>0) calculeOperationMatrice(matrice, i-1, j, Etats.vide, 20, matriceTotal);
					if(i+1<height && i+1>0) calculeOperationMatrice(matrice, i+1, j, Etats.vide, 20, matriceTotal);
					if(i+2<height && i+2>0) calculeOperationMatrice(matrice, i+2, j, Etats.vide, 20, matriceTotal);
					if(j-2<width && j-2>0) calculeOperationMatrice(matrice, i, j-2, Etats.vide, 20, matriceTotal);
					if(j-1<width && j-1>0) calculeOperationMatrice(matrice, i, j-1, Etats.vide, 20, matriceTotal);
					if(j+1<width && j+1>0) calculeOperationMatrice(matrice, i, j+1, Etats.vide, 20, matriceTotal);
					if(j+2<width && j+2>0) calculeOperationMatrice(matrice, i, j+2, Etats.vide, 20, matriceTotal);
					break;
				case bonus:
					/*
					 * on calcule tous les distances entre les heros et les bonus
					 * a la fin on calcule la matrice totale comme 
					 *   matriceTotal[i][j]+=100*(minDistance/distance1);
					 */
					int distance1 = zone.getTileDistance(ourHero.getLine(), ourHero.getCol(), i, j);
					int distance2[]= new int[nbAdversaire];
					int minDistance=10000;
					for(int x=0;x<nbAdversaire;x++)
					{
						notreIA.checkInterruption();
						distance2[x]=zone.getTileDistance(AdversairesActuelles[x].getLine(), AdversairesActuelles[x].getCol(), i, j);
						if(distance2[x]>minDistance) minDistance=distance2[x];
					}
						matriceTotal[i][j]+=100*(minDistance/distance1);
					break;
				default:
					break;
				}
			}	
		}
		
		if(ourHero.getBombNumberMax()>0 && matriceTotal[ourHero.getLine()][ourHero.getCol()]>10)
		{
			result= new AiAction(AiActionName.DROP_BOMB);
		}
		else result= new AiAction(AiActionName.NONE);

		return result;
	}

	public void afficherMatrice(float matriceTotal[][]) throws StopRequestException
	{
		notreIA.checkInterruption();
		
		for(int i=0;i<height;i++)
		{
			notreIA.checkInterruption();
			
			for(int j=0;j<width;j++)
			{
				notreIA.checkInterruption();
				
				System.out.print(matriceTotal[i][j]+"  ");

			}
			
			System.out.println("");
		}
		System.out.println("");

	}

	
}
