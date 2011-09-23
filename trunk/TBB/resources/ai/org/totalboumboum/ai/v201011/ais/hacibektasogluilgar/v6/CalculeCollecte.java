package org.totalboumboum.ai.v201011.ais.hacibektasogluilgar.v6;

import java.awt.Color;

import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;

/**
 * @author Engin Hacıbektaşoğlu
 * @author Elif Nurdan İlgar
 */
public class CalculeCollecte {
	
	HacibektasogluIlgar hi;
	AiHero notreHeros;
	CalculeZone cz;
	Action act;
	
	public CalculeCollecte(HacibektasogluIlgar hi,CalculeZone cz,Action act) throws StopRequestException
	{
		hi.checkInterruption();
		this.hi=hi;
		notreHeros=cz.zone.getOwnHero();
		this.cz=cz;
		this.act=act;
	}

/**
 * Dans le mode collecte on calcule s'il est necessaire de poser une bomb ou pas
 * 
 * @param matriceZone
 * @param matriceTotal
 * @param chemin
 * @return
 * @throws StopRequestException
 * @throws LimitReachedException 
 */
private AiAction decisionBombPourCollecte(Etats[][] matriceZone,double matriceTotal[][],AiPath chemin) throws StopRequestException, LimitReachedException
{
	hi.checkInterruption();
	
	AiAction result=null;

	int heroLine = notreHeros.getLine();
	int heroColonne = notreHeros.getCol();


	if(matriceTotal[heroLine][heroColonne]>0)
	{

		if(cz.zone.getHiddenItemsCount()>0 || cz.nbBonus>0)
		{
			Path path=new Path(hi,notreHeros, cz.adversairesActuelles);
			
			AiPath tilesSecure=new AiPath();
			boolean control=path.voisineControl(matriceTotal,tilesSecure);

			if(control)	
			{
				double[][] matriceImaginaire= new double[cz.hauteur][cz.largeur];
				int control2=act.matricePourImaginaireBombe(heroLine, heroColonne,  matriceTotal,matriceImaginaire);

				if(control2!=-1 )
				{
					int longueur=(int) ( cz.bombeExplosionVitesse/notreHeros.getWalkingSpeed());
					
					// on calcule tous les tiles qui sont accesible dans le cas de poser un bomb
					path.plusProcheAccesible(heroLine, heroColonne, matriceZone, matriceImaginaire, longueur+2,chemin);

					if(chemin.getLength()>0)
					{
						result= new AiAction(AiActionName.DROP_BOMB);
					}	
					else
					{
						matriceTotal[notreHeros.getLine()][notreHeros.getCol()]/=2;
						matriceTotal[notreHeros.getLine()][notreHeros.getCol()]--;
					}
				}

			}

		}
	}

	return result;	
}

/**
 * cette methode calcule le matrice collecte
 * 
 * @param matrice
 * @param matriceTotal
 * @return une action
 * @throws StopRequestException
 * @throws LimitReachedException 
 */
public AiAction calculeMatriceCollecte(Etats matrice[][],double matriceTotal[][]) throws StopRequestException, LimitReachedException
{
	hi.checkInterruption();

	AiPath chemin=new AiPath();
	AiAction result=null;
	
	for(int i=0;i<cz.nbAdversaire;i++)
	{
		hi.checkInterruption();
		int line= cz.adversairesActuelles[i].getLine();
		int col= cz.adversairesActuelles[i].getCol();
		
		cz.matriceTotal[line][col]+=20;
		
		Color ennemi= new Color(236,53,232); 
		act.calculeDapresPorte(cz.adversairesActuelles[i].getTile(), cz.matriceTotal, 20, ennemi);
	}

	
	for(int i=0;i< cz.hauteur;i++)
	{
		hi.checkInterruption();
		
		for(int j=0;j< cz.largeur;j++)
		{
			hi.checkInterruption();
			
			// on donne couleurs aux tiles flammable vert, flammable rouge et inaccesible
			Color flammable_vert= new Color(64,225,68);
			Color flammable_rouge= new Color(251,11,47); 
			Color case_inaccesible=new Color(0,255,0);
			Color mur= new Color(0, 191, 255);
			
			
			AiOutput output = hi.getOutput();
			
			switch (matrice[i][j]) {
			case VIDE:
				 matriceTotal[i][j]+=0;
				break;
			case MUR_DESTRUCTIBLE:
				 matriceTotal[i][j]=-100;
				 act.calculeDapresPorte(cz.zone.getTile(i, j), matriceTotal, 10, mur);
				break;
			case MUR_NON_DESTRUCTIBLE:
				 matriceTotal[i][j]=-100;
				break;
			case BOMBE:
				 matriceTotal[i][j]=-100;
				break;
			case FLAMMABLE_VERT:
				 matriceTotal[i][j]=-50;
				output.setTileColor(i, j,flammable_vert);
				break;
			case FLAMMABLE_ROUGE:
				 matriceTotal[i][j]=-100;
				output.setTileColor(i, j,flammable_rouge);
				break;
			case FLAMMABLE_NOIR:
				matriceTotal[i][j]=-150;
				break;
			case FEU:
				 matriceTotal[i][j]=-150;
				break;				
			case BONUS:
				/*
				 * on calcule tous les distances entre les heros et les bonus
				 * a la fin on calcule la matrice totale comme 
				 *   matriceTotal[i][j]+=100*(minDistance/distance1);
				 */
				int distance1=Math.abs(notreHeros.getLine()-i)+Math.abs(notreHeros.getCol()-j);
				int distance2[]= new int[ cz.nbAdversaire];
				int minDistance=100;
				for(int x=0;x< cz.nbAdversaire;x++)
				{
					hi.checkInterruption();
					distance2[x]=Math.abs(cz.adversairesActuelles[x].getLine()-i)+Math.abs(cz.adversairesActuelles[x].getCol()-j);
					if(distance2[x]<minDistance) minDistance=distance2[x];
				}
				 matriceTotal[i][j]+=100*((double)minDistance/distance1);
				break;
			case FLAMMABLE_VERT_BONUS:
				matriceTotal[i][j]=-50;
				break;
			case FLAMMABLE_ROUGE_BONUS:
				matriceTotal[i][j]=-100;
				break;
			case FLAMMABLE_NOIR_BONUS:
				matriceTotal[i][j]=-150;
				break;
			default:
				break;
			}
			
			// on donne le couleur vert aux tiles inaccesible en mode collecte par notre héro
			if( matriceTotal[i][j]<=-100) output.setTileColor(i, j,case_inaccesible);

		}	
	}
	Path path=new Path(hi, notreHeros, cz.adversairesActuelles);
	
	result=decisionBombPourCollecte( matrice, matriceTotal,chemin);
	


	if(result==null)
	{
		AiPath pathCible=path.trouverCible(notreHeros.getLine(), notreHeros.getCol(),matriceTotal);
		
		if(matriceTotal[notreHeros.getLine()][notreHeros.getCol()]>=0 && !act.pathCibleControl(pathCible))
		{
	
			pathCible=new AiPath();
			pathCible.addTile(notreHeros.getTile());
		}

		if(matriceTotal[notreHeros.getLine()][notreHeros.getCol()]>=0)
		 act.pathCibleControl(pathCible);
		
		AiPath tilesSecure=new AiPath();
		
		if(matriceTotal[notreHeros.getLine()][notreHeros.getCol()]<=-100)
		{
			if(path.voisineControl(matriceTotal,tilesSecure) && tilesSecure!=null)
			{
				pathCible=new AiPath();
				pathCible.addTile(notreHeros.getTile());
				pathCible.addTile(tilesSecure.getFirstTile());
			}
		}
		
		if(pathCible!=null && pathCible.getLength()>0)
			result=path.nouvelleAction(pathCible, notreHeros,matriceTotal);
	}
	
	return result;
}

}
