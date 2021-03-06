package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v6;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.data.*;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Burcu Kantarcı
 * @author Ayça Taşkın
 */
@SuppressWarnings("deprecation")
public class DropBomb 
{
	/** */
	ArtificialIntelligence ai;
	/** */
	AiZone zone;
	/** */
	int mode;
	
	
	/**
	 * Constructeur de la classe DropBomb
	 * @param zone 
	 * 		la zone du jeu
	 * @param mode 
	 * 		mode de l'agent
	 * @param ai
	 * 		AI correspondant
	 * @throws StopRequestException 
	 * 		description manquante !
	 * */
	public DropBomb(AiZone zone, int mode, ArtificialIntelligence ai) throws StopRequestException
	{	
		ai.	checkInterruption();
		this.ai = ai;
		this.zone=zone;
		this.mode=mode;
	}
	
	/**
	 * Methode qui decide de poser une bombe ou pas. la decision est pris en regardant deux modes 
	 * different et en trouvant les cases sur. s'il n'y a pas de cases sur on ne pose jamais 
	 * de bombe. Sinon on essai de poser une bombe dans le cadre de but de mode.
	 * 
	 * @return result
	 * 		vrai si la decision est poser une bombe faux sinon
	 * @throws StopRequestException 
	 * 		description manquante !
	 * */
	public boolean decisionOfBomb() throws StopRequestException
	{
		ai.checkInterruption();
		boolean result=false; // Decision
		int range = zone.getOwnHero().getBombRange();//la portée de nos bombes
		List<AiTile> tempBlast =zone.getOwnHero().getBombPrototype().getBlast();//La portée virtuelle de notre bombe de futur 
		
		if(zone.getOwnHero().getBombNumberMax() - zone.getOwnHero().getBombNumberCurrent() != 0 && isSafe(zone.getOwnHero().getTile()))
		{
			if(mode == 0)
			{
					if(safeArea(zone.getOwnHero(),range,tempBlast).size() >= 1)
					{
						if(enemyAvaible(tempBlast)!=null)
						{
							result = true;
						}
						else
						{
							if(zone.getOwnHero().getBombNumberMax()==1 && zone.getOwnHero().getBombRange()<6 && (wallAvaible(tempBlast))>=2 || (blockade(zone.getOwnHero()) && wallAvaible(tempBlast)>=1))
									result = true;
							else if(blockade(zone.getOwnHero()) || wallAvaible(tempBlast)>=1)
							{
								result = true;
							}
						}
					}	
			}
			else
			{	
				if(isSafe(zone.getOwnHero().getTile()))
				{				
						if(safeArea(zone.getOwnHero(),range,tempBlast).size() > 1)
						{
							if(enemyAvaible(tempBlast)!=null)
							{
								result = true;
							}
							else
							{
								
									if(zone.getOwnHero().getBombNumberMax()==1 && zone.getOwnHero().getBombRange()<6 && (wallAvaible(tempBlast))>=2 || (blockade(zone.getOwnHero()) && wallAvaible(tempBlast)>=1))
											result = true;
									else if(blockade(zone.getOwnHero()) || wallAvaible(tempBlast)>=1)
									{
										result = true;
									}
								
							}	
						}	
				}
			}		
		}	
		return result;
	}
	

	/**
	 * Regarde s'il y a des bonus dans notre area d'effet de bombe qui est plus proche a un 
	 * adversaire 
	 * 
	 * @param tempBlast
	 * 		la liste des cases de la porté de notre bombe virtuelle
	 * @return result
	 * 		vrai s'il y'en a un, faux sinon
	 * @throws StopRequestException 
	 * */
	/*private boolean bonusRiskAvailbe(List<AiTile> tempBlast) throws StopRequestException
	{
		ai.	checkInterruption();
		boolean result = false;
		Iterator<AiItem> bonusList = ((KantarciTaskin) this.ai).bonus.iterator();
		AiItem bns;
		while (bonusList.hasNext())
		{
			ai.	checkInterruption();
			bns = bonusList.next();
			if(tempBlast.contains(bns))
			{		
				Iterator<AiHero> heroList = ((KantarciTaskin) this.ai).heroes.iterator();
				AiHero hero;
				while (heroList.hasNext()) 
				{
					ai.	checkInterruption();
					hero = heroList.next();
					if(zone.getTileDistance(bns.getTile(), hero.getTile()) < zone.getTileDistance(bns.getTile(), zone.getOwnHero().getTile()))
					{	
						result = true;
					}
				}
			}
		}
		return result;
	}*/
	
	
	/**
	 * Regarde si l'agent est bloque et si oui dans combien de coté.
	 * 
	 * @param ownHero
	 * 		agent qu'on veut tester s'il est bloquer
	 * 
	 * @return result
	 * 		vraie s'il es bloque, faux sinon
	 * @throws StopRequestException 
	 * 		description manquante !
	 * */
	private boolean blockade(AiHero ownHero) throws StopRequestException 
	{
		ai.	checkInterruption();
		boolean result = false;
		List<AiTile> neig = new ArrayList<AiTile>();
		neig = ownHero.getTile().getNeighbors();
		neig.remove(zone.getOwnHero().getTile());
		Iterator<AiTile> neigbours = neig.iterator();
		AiTile tile;
		int control=0;
		boolean control2=false;
		while(neigbours.hasNext())
		{
			ai.	checkInterruption();
			tile=neigbours.next();
			if(!(tile.isCrossableBy(ownHero)))
				control = control+1;
			if(((KantarciTaskin) this.ai).destructibles.contains(tile))
				control2= true;
		}
		if(control>=3 && control2)
			result = true;
		
		return result;
	}
		
	/**
	 * Teste si la case qu'on a envoye se trouve dans le danger d'une bombe dans la zone.
	 * 
	 * @param tile
	 * 		la case qu'on veut tester
	 * 
	 * @return result
	 * 		vrai s'elle est en danger, faux sinon
	 * @throws StopRequestException 
	 * 		description manquante !
	 * */
	public boolean isSafe(AiTile tile) throws StopRequestException
	{
		ai.checkInterruption();
		boolean result = true;
		List<AiBomb> bombs = ((KantarciTaskin) this.ai).bombs;
		Iterator<AiBomb> bombList = bombs.iterator();
		AiBomb bomb;
		while (bombList.hasNext())
		{
			ai.	checkInterruption();
			bomb = bombList.next();
			List<AiTile> tempBlast = bomb.getBlast();
			Iterator<AiTile> blasts = tempBlast.iterator();
			AiTile blast;
			while(blasts.hasNext())
			{
				ai.	checkInterruption();
				blast=blasts.next();
				if(blast.getLine() == tile.getLine() && blast.getCol() == tile.getCol())
					result = false;
			}	
		}	
		return result;
	}
	
	
	/**
	 * Teste s'il se trouve de mur destructible dans la porté d'une bombe virtuelle
	 * 
	 * @param tempBlast
	 *		 la liste des cases de la porté de notre bombe virtuelle
	 *
	 *@return result
	 *		le nombre de murs accesible
	 * @throws StopRequestException 
	 * 		description manquante !
	 * */
	public int wallAvaible(List<AiTile> tempBlast) throws StopRequestException
	{
		ai.checkInterruption();
		int resultat = 0;
			
		AiTile temp;
		AiBlock wall;
		Iterator<AiBlock> wallsit = ((KantarciTaskin) this.ai).destructibles.iterator();
		while(wallsit.hasNext())
		{
			ai.	checkInterruption();
			wall=wallsit.next();
			Iterator<AiTile> blast = tempBlast.iterator();
			while(blast.hasNext())
			{
				ai.	checkInterruption();
				temp=blast.next();
				if(temp == wall.getTile())
				{
					if(isSafe(wall.getTile())  && wall.isDestructible())
						resultat=resultat+1;
				}
			}
			
		}
		return resultat;		
	}	

	/**
	 * Teste s'il se trouve des adversaire dans la porté d'une bombe virtuelle
	 * 
	 * @param tempBlast
	 *		 la liste des cases de la porté de notre bombe virtuelle
	 *
	 *@return result
	 *		l'hero accesible
	 * @throws StopRequestException 
	 * 		description manquante !
	 * */
	public AiHero enemyAvaible(List<AiTile> tempBlast) throws StopRequestException
	{
		ai.	checkInterruption();
		AiHero result = null;
		boolean control = true;
		AiTile temp;
		List<AiHero> heroes = zone.getHeroes();
		heroes.remove(zone.getOwnHero());
		Iterator<AiHero> heroesit = heroes.iterator();
		AiHero hero = null;
		while(heroesit.hasNext() && control)
		{
			ai.	checkInterruption();
			hero = heroesit.next();
			Iterator<AiTile> blast = tempBlast.iterator();
			while(hero!=zone.getOwnHero() && blast.hasNext())
			{
				ai.	checkInterruption();
				temp=blast.next();
				if(temp == hero.getTile())
				{
					control=false;
				}
			}	
		}
		if(control == false)
			result = hero;
		
		return result;
	}
	/***
	 * Regarde s'il existe des obstacles entre deux tile passées en parametre. Retourne vrai s'il 
	 * n'y en a pas, faux sinon 
	 * 
	 * @param tile1
	 * 		description manquante !
	 * @param tile2
	 * 		description manquante !
	 * @return result
	 * 		vrai s'il n'y a pas d'obstacle entre deux tile, faux sinon.
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public boolean noObstacle(AiTile tile1, AiTile tile2) throws StopRequestException
	{
		ai.	checkInterruption();
		boolean result = true;
		AiTile tempo = tile1.getNeighbor(zone.getDirection(tile1, tile2));
		while(tempo!=tile2)
		{
			ai.	checkInterruption();
			if(tempo!=zone.getOwnHero().getTile() && !(tempo.isCrossableBy(zone.getOwnHero())))
			{					
				result = false;
			}
			tempo = tempo.getNeighbor(zone.getDirection(tempo, tile2));
		}
		return result;
	}
	
	
	/**
	 * Teste s'il se trouve des cases sur à alles apres avois poser une bombe.
	 * il prend une carée au tille de la range et enleve les cases de la portée virtuelle. puis 
	 * s'il se trouve des cases sur il calcule les cases surs. Alors elle retourne une liste
	 * des cases sur ou bien une liste null.
	 * 
	 * @param hero
	 * 		hero qu'on veut teste
	 * @param range
	 * 		la porté de la bombe de hero
	 * @param blast
	 * 		la liste des cases de la bombe virtuelle
	 * 
	 * @return restult
	 * 		la liste des cases sur, sinon une liste null
	 * @throws StopRequestException 
	 * 		description manquante !
	 * */
	public List<AiTile> safeArea(AiHero hero, int range, List<AiTile> blast) throws StopRequestException 
	{
		ai.	checkInterruption();
		
		List<AiTile> result = new ArrayList<AiTile>();
		List<AiTile> area = new ArrayList<AiTile>();
		
		area = calculeArea(5);
		List<AiTile> safe = new ArrayList<AiTile>();
		result = accesibleArea(zone.getOwnHero().getTile(),area,safe);
		result.remove(zone.getOwnHero().getTile());
		Iterator<AiTile> blastList = blast.iterator();
		AiTile blastTempo;
		while(blastList.hasNext())
		{
			ai.	checkInterruption();
			blastTempo = blastList.next();
			if(result.contains(blastTempo))
				result.remove(blastTempo);
			
		}
		List<AiTile> preResult = result;
		Iterator<AiTile> pre = preResult.iterator();
		AiTile preTempo;
		while(pre.hasNext())
		{
			ai.	checkInterruption();
			preTempo = pre.next();
			if(!isSafe(preTempo))
				result.remove(preTempo);
		}
		return result;
	}
	
	
	
	/**
	 * Calcule la liste des cases de la portée de bombe d'un hero
	 * 
	 * @param range
	 * 		la portee de la bombe d'un hero
	 * 
	 * @return result
	 * 		la liste des cases de la portee
	 * @throws StopRequestException 
	 * 		description manquante !
	 * */
	
	public List<AiTile> calculeBlast(int range) throws StopRequestException
	{
		ai.	checkInterruption();
		List<AiTile> blast = new ArrayList<AiTile>();
		AiTile tempo;
		int i=1;
		tempo = zone.getOwnHero().getTile().getNeighbor(Direction.RIGHT); 
		while(i<range && (tempo.isCrossableBy(zone.getOwnHero()) || ((KantarciTaskin) this.ai).fires.contains(tempo)))
		{	
			ai.	checkInterruption();
			blast.add(tempo);
			tempo =tempo.getNeighbor(Direction.RIGHT);
			i++;
		}	
		i=1;
		tempo = zone.getOwnHero().getTile().getNeighbor(Direction.UP); 
		while(i<range && (tempo.isCrossableBy(zone.getOwnHero()) || ((KantarciTaskin) this.ai).fires.contains(tempo)))
		{	
			ai.	checkInterruption();
			blast.add(tempo);
			tempo =tempo.getNeighbor(Direction.UP);
			i++;	
		}	
		i=1;
		tempo = zone.getOwnHero().getTile().getNeighbor(Direction.LEFT); 
		while(i<range && (tempo.isCrossableBy(zone.getOwnHero()) || ((KantarciTaskin) this.ai).fires.contains(tempo)))
		{	
			ai.	checkInterruption();
			blast.add(tempo);
			tempo =tempo.getNeighbor(Direction.LEFT);
			i++;
		}	
		i=1;
		tempo = zone.getOwnHero().getTile().getNeighbor(Direction.DOWN); 
		while(i<range && (tempo.isCrossableBy(zone.getOwnHero()) || ((KantarciTaskin) this.ai).fires.contains(tempo)))
		{	
			ai.	checkInterruption();
			blast.add(tempo);
			tempo =tempo.getNeighbor(Direction.DOWN);
			i++;
		}	
		blast.add(zone.getOwnHero().getTile());
		
		return blast;
	}
	

	/**
	 * Calcule la carée de taille range*range
	 * 
	 * @param range
	 * 		la portée de la bombe d'un hero
	 * @return tempo 
	 *		la liste des cases de l'area
	 * @throws StopRequestException 
	 * 		description manquante !
	 * */
	public List<AiTile> calculeArea(int range) throws StopRequestException
	{
		ai.	checkInterruption();
		List<AiTile> blast = new ArrayList<AiTile>();
		AiTile tempo;
		List<AiTile> blastTempo = new ArrayList<AiTile>();
		blastTempo.add(zone.getOwnHero().getTile());
		tempo = zone.getOwnHero().getTile().getNeighbor(Direction.RIGHT); 
		for(int i=1;i<range;i++)
		{	
			ai.	checkInterruption();
			blastTempo.add(tempo);
			tempo =tempo.getNeighbor(Direction.RIGHT);
		}	
		tempo = zone.getOwnHero().getTile().getNeighbor(Direction.LEFT); 
		for(int i=1;i<range;i++)
		{	
			ai.	checkInterruption();
			blastTempo.add(tempo);
			tempo =tempo.getNeighbor(Direction.LEFT);
		}	
		Iterator<AiTile> blastList = blastTempo.iterator();
		AiTile temp;
		while(blastList.hasNext())
		{
			ai.	checkInterruption();
			temp=blastList.next();
			tempo = temp;
			tempo =tempo.getNeighbor(Direction.DOWN); 
			for(int i=1;i<range;i++)
			{	
				ai.	checkInterruption();
				blast.add(tempo);
				tempo =tempo.getNeighbor(Direction.DOWN);
			}	
			tempo = temp;
			tempo = tempo.getNeighbor(Direction.UP); 
			for(int i=1;i<range;i++)
			{	
				ai.	checkInterruption();
				blast.add(tempo);
				tempo =tempo.getNeighbor(Direction.UP);
			}		
		}
		Iterator<AiTile> blastSecond = blast.iterator();
		while(blastSecond.hasNext())
		{
			ai.	checkInterruption();
			temp=blastSecond.next();
			blastTempo.add(temp);
		}
		return blastTempo;
		
	}
	
	
	/*public List<AiTile> calculeArea1(int range) throws StopRequestException
	{
		ai.	checkInterruption();
		List<AiTile> result = new ArrayList<AiTile>();
		
		result.add(zone.getOwnHero().getTile().getNeighbor(Direction.DOWN).getNeighbor(Direction.LEFT));
		result.add(zone.getOwnHero().getTile().getNeighbor(Direction.DOWN).getNeighbor(Direction.RIGHT));
		result.add(zone.getOwnHero().getTile().getNeighbor(Direction.UP).getNeighbor(Direction.LEFT));
		result.add(zone.getOwnHero().getTile().getNeighbor(Direction.UP).getNeighbor(Direction.RIGHT));

		return result;
	}*/ 
	
	
	
	/**
	 * Calcules recursivement les cases qu'un hero peut passer dans une carée déjà determineé. 
	 * Alors elle elemine les mur non destructible et puis les chemins que le hero associé ne
	 * peut pas acceder. Donc elle retourne une liste des cases que le hero associé peut 
	 * acceder. S'il n'y a pas de case alors elle retourne null.
	 * 
	 * @param tile 
	 * 		tile du hero qu'on veut trouve son chemin
	 * @param area
	 * 		une carée qu'on estime pour un hero pour qu'il puisse acceder à ses cases dans le temps d'explosion d'une bombe
	 * @param safe
	 * 		liste des cases sur. Au début c'est null.
	 * @return result
	 * 		une liste des cases accesible, sinon une liste null
	 * @throws
	 * 		StopRequestException
	 * 		description manquante !
	 */
	public List<AiTile> accesibleArea(AiTile tile, List<AiTile> area, List<AiTile> safe) throws StopRequestException
	{
		ai.	checkInterruption();
		List<AiTile> result = new ArrayList<AiTile>();
		List<AiTile> neigs = new ArrayList<AiTile>();
		AiTile tempoNeig;
		if(area.contains(tile))
		{
			neigs = tile.getNeighbors();
			Iterator<AiTile> neigList = neigs.iterator();
			while (neigList.hasNext())
			{
				ai.	checkInterruption();
				tempoNeig = neigList.next();
				if(safe.contains(tempoNeig))
					result = safe;
				else
				{
					if(tempoNeig.isCrossableBy(zone.getOwnHero()) && isSafe(tempoNeig))
					{
						safe.add(tempoNeig);
						result = accesibleArea(tempoNeig, area, safe);
					}
				}
			}
		}
		return result;
	}
}
