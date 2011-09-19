package org.totalboumboum.ai.v201011.ais.hacibektasogluilgar.v6;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiItemType;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

public class CalculeZone {
	
	private HacibektasogluIlgar hi;
	AiZone zone;
	int hauteur;
	int largeur;
	double[][] matriceTotal;
	Etats[][] matriceZone;
	int nbMurDestructible=0;
	long bombeExplosionVitesse=700;
	int nbBonus=0;
	int nbBonusBombe=0;
	int nbBonusFlamme=0;
	AiItem bonuses[];
	AiHero notreHero;
	AiHero adversairesActuelles[];
	int nbAdversaire=0;
	

	public CalculeZone(HacibektasogluIlgar hi) throws StopRequestException
	{
		hi.checkInterruption();
		this.hi=hi;
		zone=hi.getPercepts();
		hauteur=zone.getHeight();
		largeur=zone.getWidth();
		matriceTotal=new double[hauteur][largeur];
		matriceZone=new Etats[hauteur][largeur];
		notreHero=zone.getOwnHero();
		bonuses=new AiItem[zone.getItems().size()+zone.getHiddenItemsCount()];
		adversairesActuelles = new AiHero[10];
	}
	
	/**
	 * on initialise la matrice en lui pensent comme tous ses tiles sont vide
	 * @param matrice
	 * @throws StopRequestException
	 */
	public void initialiserMatrices(Etats matrice[][]) throws StopRequestException 
	{
		hi.checkInterruption();

		for(int i=0;i<hauteur;i++)
		{
			hi.checkInterruption();
			for(int j=0;j< largeur;j++)
			{
				hi.checkInterruption();	
				matrice[i][j]=Etats.VIDE;
				matriceTotal[i][j]=0;
			}
		}
	}

	
	/**
	 * 
	 * @param matrice
	 * @throws StopRequestException
	 */
	public void calculeMurs(Etats matrice[][])throws StopRequestException 
	{
		hi.checkInterruption();
		
		Collection<AiBlock> murs=zone.getBlocks();
		Iterator<AiBlock> iterMur=murs.iterator();
		
		while(iterMur.hasNext())
		{
			hi.checkInterruption();
			AiBlock mur = iterMur.next();
			if(mur.isDestructible())
			{
				matrice[mur.getLine()][mur.getCol()]=Etats.MUR_DESTRUCTIBLE;
				nbMurDestructible++;
			}
			else matrice[mur.getLine()][mur.getCol()]=Etats.MUR_NON_DESTRUCTIBLE;
			
		}
	
	}
	
	/**
	 * 
	 * @param source
	 * @param cible
	 * @param longueur
	 * @return
	 * @throws StopRequestException
	 */
	private Direction direction(AiTile source,AiTile cible,int longueur) throws StopRequestException
	{
		hi.checkInterruption();
		Direction resultat;
		
		
		if(source.getLine()-cible.getLine()==0)
		{
			longueur=source.getCol()-cible.getCol();
			if(longueur>0) resultat=Direction.LEFT;
			else resultat=Direction.RIGHT;
		}
		else 
		{
			longueur=source.getLine()-cible.getLine();
			if(longueur>0) resultat= Direction.UP;
			else 
				resultat=Direction.DOWN;
		}
		
		return resultat;
	}

	/**
	 * 
	 * @param matrice
	 * @throws StopRequestException
	 */
	public void calculeBombes(Etats matrice[][]) throws StopRequestException
	{
		hi.checkInterruption();
		Collection<AiBomb> bombes = zone.getBombs();
		Iterator<AiBomb> iterBombes = bombes.iterator();
		
		while(iterBombes.hasNext())
		{
			hi.checkInterruption();
			AiBomb bombe = iterBombes.next();
			int x=bombe.getLine();
			int y=bombe.getCol();

			bombeExplosionVitesse=bombe.getExplosionDuration();
			List<AiTile> inScopeTiles = bombe.getBlast();
			for (int i = 0; i < inScopeTiles.size(); i++) 
			{
				hi.checkInterruption();
				/*
				 *  on controle la duree de la bomb pour pouvoir comprendre si
				 *  c'est bien de passer par ce tile ou non   
				 */
				if(bombe.getTime()<bombe.getNormalDuration()/3
						&& matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()]!=Etats.FLAMMABLE_ROUGE
						 && matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()]!=Etats.FLAMMABLE_NOIR)
				{
					if(matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()]==Etats.BONUS)
					{
						matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()] = Etats.FLAMMABLE_VERT_BONUS;
						int longueur=0;
						Direction direction=direction(bombe.getTile(), inScopeTiles.get(i),longueur);
				
						for(int a=0;a<bombe.getRange()-Math.abs(longueur);a++)
						{
							hi.checkInterruption();
							AiTile voisine=inScopeTiles.get(i).getNeighbor(direction);
							matrice[voisine.getLine()][voisine.getCol()] = Etats.FLAMMABLE_VERT_BONUS;
							voisine=voisine.getNeighbor(direction);
						}
					}
					else 
						matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()] = Etats.FLAMMABLE_VERT;
					
				}
				else
					if(bombe.getTime()<bombe.getNormalDuration()*2/3
							 && matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()]!=Etats.FLAMMABLE_NOIR)
					{
						if(matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()]==Etats.BONUS)
						{
							matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()] = Etats.FLAMMABLE_ROUGE_BONUS;
							
							int longueur=0;
							Direction direction=direction(bombe.getTile(), inScopeTiles.get(i),longueur);
					
							for(int a=0;a<bombe.getRange()-Math.abs(longueur);a++)
							{
								hi.checkInterruption();
								AiTile voisine=inScopeTiles.get(i).getNeighbor(direction);
								matrice[voisine.getLine()][voisine.getCol()] = Etats.FLAMMABLE_ROUGE_BONUS;
								voisine=voisine.getNeighbor(direction);
							}
						}
						else
							matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()] = Etats.FLAMMABLE_ROUGE;
					}
					else
						{
							if(matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()]==Etats.BONUS)
							{
								matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()] = Etats.FLAMMABLE_NOIR_BONUS;
								
								int longueur=0;
								Direction direction=direction(bombe.getTile(), inScopeTiles.get(i),longueur);
						
								for(int a=0;a<bombe.getRange()-Math.abs(longueur);a++)
								{
									hi.checkInterruption();
									AiTile voisine=inScopeTiles.get(i).getNeighbor(direction);
									matrice[voisine.getLine()][voisine.getCol()] = Etats.FLAMMABLE_NOIR_BONUS;
									voisine=voisine.getNeighbor(direction);
								}
							}
							else
								matrice[inScopeTiles.get(i).getLine()][inScopeTiles.get(i).getCol()] = Etats.FLAMMABLE_NOIR;
						}
			}
			
			matrice[x][y]=Etats.BOMBE;

		}
	}
	
	
	/**
	 * 
	 * @param matrice
	 * @throws StopRequestException
	 */
	public void calculeFeu(Etats matrice[][]) throws StopRequestException
	{
		hi.checkInterruption();
		Collection<AiFire> feux= zone.getFires();
		Iterator<AiFire> iterFeux = feux.iterator();
		
		while(iterFeux.hasNext())
		{
			hi.checkInterruption();
			AiFire feu=iterFeux.next();
			matrice[feu.getLine()][feu.getCol()]=Etats.FEU;
			
		}
		
		
	}
	/**
	 * 
	 * @param matrice
	 * @throws StopRequestException
	 */
	public void calculeBonus (Etats matrice[][]) throws StopRequestException
	{
		hi.checkInterruption();
		
		Collection<AiItem> items = zone.getItems();
		Iterator<AiItem> iterItems = items.iterator();
		
		while (iterItems.hasNext()) {
			hi.checkInterruption();
			
			AiItem item = iterItems.next();
			bonuses[nbBonus]=item;
			if(AiItemType.EXTRA_BOMB==item.getType())
			{
				nbBonusBombe++;
			}
			if(AiItemType.EXTRA_FLAME==item.getType())
			{
				nbBonusFlamme++;
			}
			nbBonus++;
			int x=item.getLine();
			int y=item.getCol();

//			if(matrice[x][y] == Etats.FLAMMABLE_VERT)
//				matrice[x][y] = Etats.FLAMMABLE_VERT_BONUS;
//			else 
//				if(matrice[x][y] == Etats.FLAMMABLE_ROUGE)
//					matrice[x][y] = Etats.FLAMMABLE_ROUGE_BONUS;
//				else 
					matrice[x][y] = Etats.BONUS;
		}
	}
	
	/**
	 * 
	 * 
	 * @param matrice
	 * @throws StopRequestException
	 */
	public void calculeAdversaire(Etats matrice[][]) throws StopRequestException
	{
		hi.checkInterruption();
		
		Collection<AiHero> adversaires = zone.getRemainingHeroes();
		Iterator<AiHero> iterAdversaires = adversaires.iterator();
		
		while(iterAdversaires.hasNext())
		{
			hi.checkInterruption();
			AiHero adversaire = iterAdversaires.next();
			if(adversaire!=notreHero)
				{
					adversairesActuelles[nbAdversaire]=adversaire;
					nbAdversaire++;
				}
		}
	}
	
	/**
	 * on fait mis à jour de tous les matrices calculé pour avoir total matrice du zone
	 * 
	 * @param matrice
	 * @throws StopRequestException
	 */
	public void misAJourEtatsMatrice(Etats matrice[][]) throws StopRequestException
	{
		hi.checkInterruption();
		
		initialiserMatrices(matrice);
		calculeMurs(matrice);
		calculeBonus(matrice);
		calculeBombes(matrice);
		calculeFeu(matrice);
		calculeAdversaire(matrice);
	}
	
	/**
	 * methode affichante la matriceTotale en console et en zone de jeu à la fois
	 * 
	 * @param matriceTotal
	 * @throws StopRequestException
	 */
	public void afficherMatrice(double matriceTotal[][],Etats matriceEtat[][]) throws StopRequestException
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

		//		System.out.print(matriceEtat[i][j].ordinal()+" ");
			}
		//	System.out.println();
		}
		//System.out.println();
	}

	
}
