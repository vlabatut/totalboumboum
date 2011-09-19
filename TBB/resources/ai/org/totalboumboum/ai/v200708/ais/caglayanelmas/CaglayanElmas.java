package org.totalboumboum.ai.v200708.ais.caglayanelmas;

import java.util.Iterator;
import java.util.Vector;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;





/**
 * Projet d'Intelligence Artificielle (2007-2008) : Bomberman
 * Equipe : CAGLAYAN Ozan, ELMAS Can
 * 
 * @author Ozan Caglayan
 * @author Arif Can Elmas
 *
 */
public class CaglayanElmas extends ArtificialIntelligence
{
	private static final long serialVersionUID = 1L;
	
	// Constantes qui définissent l'état du bomberman
	private final static int DOING_NOTHING = 0;
	private final static int RUNNING_FROM_BOMB = 1;
	private final static int COLLECTING_BONUS = 2;
	private final static int DESTRUCTING_WALLS = 3;
	private final static int ATTACKING = 4;
	
	// Contient la zone du tour précédente
	private int[][] lastMatrix;
	// Représente une destination à aller
	private int[] target;
	// Contient la position de la dernière bombe
	private int[] lastBomb;
	// Représente l'état du bomberman avec les constantes définies ci-dessus
	private int state;
	// Combien de fois on a mis une bombe pour détruire des murs
	private int destructionCount;
	// Contient la dernière action renvoyée par call()
	private Integer lastAction;
	// Le vecteur contenant des liens entre les cases
	// qui définissent le chemin le plus court entre A et B
	private Vector<SearchLink> links;
	// Le vecteur qui contient les cases accessibles
	private Vector<int[]> playableCases;
	// Si vrai, il existe une action qui reste des tours précédentes
	private boolean savedMove;
	private boolean startedToAttack;

	public CaglayanElmas()
	{
		// Notre IA est appelée "Smart"
		super("CaglynElms");
		
		// Initialisation nécessaires
		target = new int[2];
		target[0] = target[1] = -1;
		
		lastBomb = new int[2];
		lastBomb[0] = lastBomb[1] = -1;
		
		links = null;
		savedMove = false;
		
		playableCases = new Vector<int[]>();
		lastMatrix = new int[17][15];
		
		state = DOING_NOTHING;
		startedToAttack = false;
		
		destructionCount = 0;
	}
	
	
	/** indicateur de première invocation (pour la compatibilité */
	private boolean firstTime = true;

	/**
	 * détermine la prochaine action que l'IA va effectuer
	 * (Bouger, ne rien faire, poser une bombe)
	 * 
	 * @return AI_ACTION_XXXX
	 */
	public Integer call() throws Exception
	{
		if(firstTime)
			firstTime = false;
		else
		{	
		// Notre position
		int px = getOwnPosition()[0];
		int py = getOwnPosition()[1];
		
		// Genere la liste des cases accessibles
		if (isZoneMatrixChanged())
		{
			playableCases.clear();
			findPlayableCases(px, py);
		}
		
		// On garde toujours la zone du tour dernier
		lastMatrix = getZoneMatrix().clone();
		
		// Est-ce qu'il existe une bombe qui nous menace?
		int[] bomb = bombCanKillMe(px, py);
		
		if (bomb[0] != -1)
		{
			// Oui, il y a une bombe!
			if ( !(lastBomb[0] == bomb[0] && lastBomb[1] == bomb[1]) )
			{
				state = RUNNING_FROM_BOMB;
				
				// Nouvelle bombe.
				lastBomb = bomb.clone();
				
				// On essaie de trouver une case sure.
				int[] whereToEscape = escapeFromAllBombs(px, py);

				// Initialize le "PathFinder"
				PathFinder pf = new PathFinder(this);
				pf.setStates(getOwnPosition(), whereToEscape);
				links = pf.findShortestPath();

				if ( savedMove = (links.size() != 0) )
				{
					target = links.get(0).getTarget().getState().clone();
					lastAction = links.get(0).getAction();			
					return lastAction;
				}

			}
		}
		
		// On effectue les actions qui restent des tours précédentes.
		if (savedMove)
			return handleSavedMove(px, py);
			
		// On attend jusqu'a ce que la bombe déjà posée s'explose.
		if (!isBombExploded())
		{
			state = DOING_NOTHING;
			return AI_ACTION_DO_NOTHING;
		}
		else
			lastBomb[0] = lastBomb[1] = -1;
		
		// Prends les bonus s'il existe.
		if (!startedToAttack)
		{
			int[] bonus = getNearestBonusPosition(px, py);
			if (bonus[0] != -1)
			{
				state = COLLECTING_BONUS;
				PathFinder pf = new PathFinder(this);
				pf.setStates(getOwnPosition(), bonus);
				links = pf.findShortestPath();
				
				if ( savedMove = (links.size() != 0) )
				{
					target = links.get(0).getTarget().getState().clone();
					lastAction = links.get(0).getAction();
					return lastAction;
				}
			}
		}
		
		// Prends la position de l'ennemi plus proche
		int[] opponent = getNearestOpponentPosition(px, py, false);
		if (opponent[0] != -1)
		{	
			// Modifie notre état.
			state = ATTACKING;
			startedToAttack = true;
			
			if (opponent[0] == px && opponent[1] == py)
			{
				// On est sur la meme case.
				lastAction = AI_ACTION_PUT_BOMB;
				return lastAction;
			}
			
			PathFinder pf = new PathFinder(this);
			pf.setStates(getOwnPosition(), opponent);
			links = pf.findShortestPath();
			
			if ( savedMove = (links.size() != 0) )
			{
				target = links.get(0).getTarget().getState().clone();
				lastAction = links.get(0).getAction();
				return lastAction;
			}
		}
		else
		{
			// On ne peut pas atteindre l'ennemi le plus proche.
			// Donc, collectons un peu de bonus en détruisant
			// les murs proches.
			state = DESTRUCTING_WALLS;
			int[] block = getNearestBlockToPutBomb(px, py);
			
			if (block[0] == -1)
				return AI_ACTION_DO_NOTHING;
			
			else if (block[0] == px && block[1] == py)
			{
				// On est déjà là.
				savedMove = true;
				target = block.clone();
				return AI_ACTION_DO_NOTHING;
			}
			else
			{	
				PathFinder pf = new PathFinder(this);
				pf.setStates(getOwnPosition(), block);
				links = pf.findShortestPath();
				
				if ( savedMove = (links.size() != 0) )
				{
					target = links.get(0).getTarget().getState().clone();
					lastAction = links.get(0).getAction();
					return lastAction;
				}
			}
		}
		}
		return AI_ACTION_DO_NOTHING;
	}
	
	/**
	 * détermine l'action suivante pour arriver
	 * à la destination définie dans les tours précédentes.
	 * Renvoie -1 si le bomberman est bloqué dans une case
	 * par une bombe confondue et ne peut pas aller vers
	 * la destination.
	 * @param px	position de notre personnage
	 * @param py	position de notre personnage
	 * @return		L'action qu'il faut commettre
	 */
	public Integer handleSavedMove(int px, int py)
	{
		// Pas encore arrivé à la destination "target".
		if ( !(target[0] == px && target[1] == py))	
			return lastAction;
		
		else
		{		
			// On est arrivé à une destination intermédiaire.
			// On change l'action qui va nous guider vers notre nouvelle
			// destination.
			if (links.size() != 0)
				links.remove(0);
			
			// On prend la nouvelle destination et action.
			if (links.size() != 0)
			{
				target = links.get(0).getTarget().getState().clone();
				lastAction = links.get(0).getAction();
				return lastAction;
			}
			else
			{				
				// L'action finale dépend de notre but. Si on attaque ou
				// détruit des murs on retourne AI_ACTION_PUT_BOMB sinon
				// AI_ACTION_DO_NOTHING
				lastAction = decideNextAction(px, py);			
				state = DOING_NOTHING;
				return lastAction;
			}
		}
	}
	
	/**
	 * détermine l'action suivante selon
	 * l'état du bomberman.
	 * @param px	position de notre personnage
	 * @param py	position de notre personnage
	 * @return		L'action qu'il faut commettre
	 */
	public Integer decideNextAction(int px, int py)
	{
		Integer result = AI_ACTION_DO_NOTHING;
		
		// Nettoyage
		savedMove = false;
		target[0] = target[1] = -1;

		switch (state)
		{
			case DESTRUCTING_WALLS:
				result = AI_ACTION_PUT_BOMB;
				destructionCount++;
				break;
				
			case ATTACKING:
				// On va tester si on pose une bombe ou pas.
				int[] opponent = getNearestOpponentPosition(px, py, false);
				
				// On pose une bombe ici car elle menace bien l'ennemi.
				if ( (px == opponent[0] && getOwnFirePower() >= Math.abs(py - opponent[1])) ||
					 (py == opponent[1] && getOwnFirePower() >= Math.abs(px - opponent[0])))
					result = AI_ACTION_PUT_BOMB;

				// On se rapproche.
				else
				{
					PathFinder pf = new PathFinder(this);
					pf.setStates(getOwnPosition(), opponent);
					links = pf.findShortestPath();
					
					if ( savedMove = (links.size() != 0) )
					{
						target = links.get(0).getTarget().getState().clone();
						lastAction = links.get(0).getAction();
						result = lastAction;
					}
				}
				break;
				
			default:
				result = AI_ACTION_DO_NOTHING;
				break;
		}
		return result;
	}
	
	/**
	 * Détérmine les cases accessibles à notre personnage
	 * et les gardent dans le vecteur playableCases.
	 * @param px	position de notre personnage
	 * @param py	position de notre personnage
	 */
	public void findPlayableCases(int px, int py)
	{
		Vector<int[]> positions = getPossiblePositions(px, py);
		Iterator<int[]> i = positions.iterator();
		
		while (i.hasNext())
		{
			int[] current = i.next();
			if (!playableCasesContains(current[0], current[1]))
			{
				playableCases.add(current);
				findPlayableCases(current[0], current[1]);
			}
		}
	}
	
	/**
	 * Indique si la case (x,y) est accessible
	 * à notre personnage.
	 * @param x		position de la case qu'on cherche
	 * @param y		position de la case qu'on cherche
	 * @return		vrai si le vecteur playableCases contient la case (x,y)
	 */
	public boolean playableCasesContains(int x, int y)
	{
		Iterator<int[]> i = playableCases.iterator();
		while (i.hasNext())
		{
			int[] temp = i.next();
			if (temp[0] == x && temp[1] == y)
				return true;
		}
		return false;
	}
	
	/**
	 * Indique si la zone est changée depuis la dernière copie.
	 * @return vrai si la zone a subit une modification
	 */
	public boolean isZoneMatrixChanged()
	{
		int dimX = getZoneMatrixDimX();
		int dimY = getZoneMatrixDimY();
		for (int i = 1; i < dimX-1; i++)
		{
			for (int j = 1; j < dimY-1; j++)
			{
				if (lastMatrix[i][j] != getZoneMatrix()[i][j])
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Indique si la dernière bombe posée est explosée ou non.
	 * @return vrai si la bombe n'est pas encore explosée
	 */
	public boolean isBombExploded()
	{
		// Attention, quand la bombe s'explose, la case correspondante
		// devient d'abord AI_BLOCK_FIRE et après tous devient AI_BLOCK_EMPTY!
		return !(lastBomb[0] != -1 &&
				((getZoneMatrix()[lastBomb[0]][lastBomb[1]] == AI_BLOCK_BOMB) ||
				(getZoneMatrix()[lastBomb[0]][lastBomb[1]] == AI_BLOCK_FIRE)));
				
	}
	
	/**
	 * détermine la case à la quelle on doit aller pour fuire de la bombe.
	 * Retourne (-1,-1) si c'est impossible de fuire. 
	 * @param px 	position de notre personnage
	 * @param py	position de notre personnage
	 * @return la case à la quelle on doit aller pour fuire de la bombe
	 */
	public int[] escapeFromAllBombs(int px, int py)
	{
		int[] result = {-1, -1};
		int dist = 100;
		int power = -1;
		
		Vector<int[]> bombs = new Vector<int[]>();
		
		for (int i = 1; i <= 15; i++)
			for (int j = 1; j <= 13; j++)
				if ( (power = getBombPowerAt(i, j)) != -1 )
				{
					int[] bomb = {i, j, power};
					bombs.add(bomb);
				}
		
		Iterator<int[]> it = playableCases.iterator();
		while (it.hasNext())
		{
			boolean canBeSolution = true;
			int[] c = it.next();
			// Est-ce que c[] est dangereux?
			for (int i = 0; i < bombs.size() && canBeSolution; i++)
			{
				// La position et la portée de la bombe courante.
				int x = bombs.get(i)[0];
				int y = bombs.get(i)[1];
				int p = bombs.get(i)[2];
				
				// Les limites de la recherche
				int sx = (x-p<1)?1:x-p;
				int ex = (x+p>15)?15:x+p;
				int sy = (y-p<1)?1:y-p;
				int ey = (y+p>13)?13:y+p;
				
				for (int j = sx; j <= ex & canBeSolution; j++)
					canBeSolution = !(c[0] == j && c[1] == y);
				
				for (int j = sy; j <= ey & canBeSolution; j++)
					canBeSolution = !(c[1] == j && c[0] == x);
			}
			
			// Si cette case est une solution, on trouve
			// la chemin plus courte vers elle.
			if (canBeSolution)
			{
				PathFinder pf = new PathFinder(this);
				pf.setStates(getOwnPosition(), c);
				Vector<SearchLink> t = pf.findShortestPath();
				if (t.size() < dist)
				{
					dist = t.size();
					result = c.clone();
				}
			}
		}
		return result;
	}
	
	/**
	 * détermine si il existe une bombe qui peut nous tuer en traversant
	 * l'horizontale et la verticale qui coupe notre personnage.
	 * @param px	position de notre personnage
	 * @param py	position de notre personnage
	 * @return		position de la bombe ou (-1,-1)
	 */
	public int[] bombCanKillMe(int px, int py)
	{
		int[] result = {-1, -1};
		
		int bombPower = 0;
		
		int dimX = getZoneMatrixDimX();
		int dimY = getZoneMatrixDimY();
		
		for (int i = 1; i < dimY; i++)
		{
			// Cherche une bombe sur la meme ligne verticale que nous..
			if ((bombPower = getBombPowerAt(px, i)) != -1)
			{
				int min = Math.min(i, py);
				int max = Math.max(i, py);
				boolean wallExists = false;
				
				// Est-ce qu'il y a un mur entre nous?
				for (int k = min+1; k < max && !wallExists; k++)
					wallExists = getZoneMatrix()[px][k] == AI_BLOCK_WALL_SOFT || getZoneMatrix()[px][k] == AI_BLOCK_WALL_HARD;
				
				if ( !wallExists && Math.abs(py-i) <= bombPower)
				{
					result[0] = px;
					result[1] = i;
					return result;
				}
			}
		}
		
		for (int j = 1; j < dimX; j++)
		{	
			// Cherche une bombe sur la meme ligne horizontale que nous..
			if ((bombPower = getBombPowerAt(j, py)) != -1)
			{
				int min = Math.min(j, px);
				int max = Math.max(j, px);
				boolean wallExists = false;
				
				// Est-ce qu'il y a un mur entre nous?
				for (int k = min+1; k < max && !wallExists; k++)
					wallExists = getZoneMatrix()[k][py] == AI_BLOCK_WALL_SOFT || getZoneMatrix()[k][py] == AI_BLOCK_WALL_HARD;
				
				if ( !wallExists && Math.abs(px-j) <= bombPower)
				{
					result[0] = j;
					result[1] = py;
					return result;
				}
			}
		}
		return result;
	}
	
	/**
	 * détermine la position du bonus plus proche et accessible à nous.
	 * @param px	position de notre personnage
	 * @param py	position de notre personnage
	 * @return		position du bonus ou (-1,-1)
	 */
	public int[] getNearestBonusPosition(int px, int py)
	{
		// Si la distance au bonus plus proche est > 10,
		// on ne fatigue pas aller là-bas.
		int dist = 11;
		int[] result = {-1, -1};
		
		for(int i = 0; i < playableCases.size(); i++)
		{
			int[] to = {playableCases.get(i)[0], playableCases.get(i)[1]};
			
			if (getZoneMatrix()[to[0]][to[1]] == AI_BLOCK_ITEM_FIRE || getZoneMatrix()[to[0]][to[1]] == AI_BLOCK_ITEM_BOMB)
			{
				PathFinder pf = new PathFinder(this);
				pf.setStates(getOwnPosition(), to);
				Vector<SearchLink> t = pf.findShortestPath();
				if (t.size() < dist)
				{
					dist = t.size();
					result = to.clone();
				}
			}
		}
		return result;
	}
	
	/**
	 * détermine le nombre de murs destructibles au voisinage
	 * de la case (cx, cy).
	 * @param cx	position de la case
	 * @param cy	position de la case
	 * @return		nombre de murs destructibles au voisinage de (cx,cy) 
	 */
	public int evaluateBlock(int cx, int cy)
	{
		int blocks = 0;
		
		if (getZoneMatrix()[cx+1][cy] == AI_BLOCK_WALL_SOFT)
			blocks++;
		if (getZoneMatrix()[cx-1][cy] == AI_BLOCK_WALL_SOFT)
			blocks++;
		if (getZoneMatrix()[cx][cy+1] == AI_BLOCK_WALL_SOFT)
			blocks++;
		if (getZoneMatrix()[cx][cy-1] == AI_BLOCK_WALL_SOFT)
			blocks++;
		
		return blocks;
	}
	
	/**
	 * détermine la case la plus proche à mettre une bombe.
	 * Si le bomberman a suffisamment pris le bonus qui
	 * augmente la portée, cette méthode retourne des cases
	 * qui vont faciliter l'accès à l'ennemi quand les murs
	 * au voisinage sont détruits.
	 * @param px	position de notre personnage
	 * @param py	position de notre personnage
	 * @return		position de la case à poser la bombe
	 */
	public int[] getNearestBlockToPutBomb(int px, int py)
	{
		int[] result = {-1, -1};
		
		if (evaluateBlock(px, py) != 0)
		{
			// On peut poser la bombe directement ici, à (px,py)
			// car il y a au moins un mur destructible à coté de nous.
			result[0] = px;
			result[1] = py;
		}
		else
		{
			int dist = 100;
			
			playableCases.clear();
			findPlayableCases(px, py);
			
			for(int i = 0; i < playableCases.size(); i++)
			{
				int[] from = {px, py};
				int[] to = {playableCases.get(i)[0], playableCases.get(i)[1]};
				
				if (evaluateBlock(to[0], to[1]) >= 1)
				{			
					// Si on a suffisamment détruit les murs,
					// on décide alors à attaquer directement vers l'ennemi
					// plus proche. Pour cela, on va maintenant détruire les murs
					// qui vont nous ouvrir la chemin.
					if (destructionCount > Math.random()*5+3)
					{
						from = getNearestOpponentPosition(px, py, true);
						if (distance(from[0], from[1], to[0], to[1]) < dist)
						{
							dist = distance(from[0], from[1], to[0], to[1]);
							result = to.clone();
						}
					}
					else
					{
						// Continue à détruire les murs plus proches.
						PathFinder pf = new PathFinder(this);
						pf.setStates(from, to);
						Vector<SearchLink> t = pf.findShortestPath();
						if (t.size() < dist)
						{
							dist = t.size();
							result = to.clone();
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * détermine la position de l'ennemi plus proche à nous.
	 * @param px	position de notre personnage
	 * @param py	position de notre personnage
	 * @param 		returnWhatever Si vrai, retourne la position de l'ennemi
	 * 				meme s'il est inaccessible.
	 * @return		position de l'ennemi plus proche
	 */
	public int[] getNearestOpponentPosition(int px, int py, boolean returnWhatever)
	{
		Vector<int[]> opponents = getAllPlayerPositions();
		int dist = 100;
		int[] result = {-1, -1};
		for (int i = 0; i < opponents.size(); i++)
		{
			int ox = opponents.get(i)[0];
			int oy = opponents.get(i)[1];
			if ( (returnWhatever || playableCasesContains(ox, oy)) && distance(px, py, ox, oy) < dist )
			{
				dist = distance(px, py, ox, oy);
				result = opponents.get(i).clone();
			}
		}
		return result;
	}
	
	/**
	 * détermine les nouveaux positions à partir d'un case initial
	 * (x,y) et 4 actions.
	 * @param x		position à étudier
	 * @param y		position à étudier
	 * @return		vecteur contenant les nouveaux positions
	 */
	public Vector<int[]> getPossiblePositions(int x, int y)
	{	
		Vector<int[]> result = new Vector<int[]>();
		
		for(int move = AI_ACTION_GO_UP; move <= AI_ACTION_GO_RIGHT; move++)
			if(isMovePossible(x, y, move))
				result.add(applyAction(x, y, move));
		return result;
	}
	
	/**
	 * détermine la nouvelle position qui résulte de l'application
	 * de l'action "move" à la position (x,y).
	 * @param x		position à étudier
	 * @param y		position à étudier
	 * @return		la nouvelle position qui est le résultat de l'action
	 * 				appliquée à la position (x,y)
	 */
	public int[] applyAction(int x, int y, int move)
	{
		int[] result = {x, y};
		switch (move)
		{
			case AI_ACTION_GO_LEFT:
				result[0]--;
				break;

			case AI_ACTION_GO_RIGHT:
				result[0]++;
				break;

			case AI_ACTION_GO_UP:
				result[1]--;
				break;

			case AI_ACTION_GO_DOWN:
				result[1]++;
				break;	
		}
		return result;
	}
	
	/**
	 * détermine l'action qui s'oppose à la direction d.
	 * @param d		direction (AI_DIR_XXXX)
	 * @return		l'action qui s'oppose à la direction d
	 */
	public Integer getOppositeDirection(int d)
	{
		Integer result = 0;
		switch (d)
		{
			case AI_DIR_LEFT:
				result = AI_ACTION_GO_RIGHT;
				break;
			case AI_DIR_RIGHT:
				result = AI_ACTION_GO_LEFT;
				break;
			case AI_DIR_UP:
				result = AI_ACTION_GO_DOWN;
				break;
			case AI_DIR_DOWN:
				result = AI_ACTION_GO_UP;
				break;
		}
		return result;
	}
	
	/**
	 * Indique si le déplacement dont le code a été passé en paramètre 
	 * est possible pour un personnage situé en (x,y).
	 * @param x	position du personnage
	 * @param y position du personnage
	 * @param move	le déplacement à étudier
	 * @return	vrai si ce déplacement est possible
	 */
	public boolean isMovePossible(int x, int y, int move)
	{	
		boolean result;
		switch(move)
		{	
			case ArtificialIntelligence.AI_ACTION_GO_UP:
				result = y > 0 && !isObstacle(x, y-1);
				break;
			case ArtificialIntelligence.AI_ACTION_GO_DOWN:
				result = y < (getZoneMatrixDimY()-1) && !isObstacle(x, y+1);
				break;
			case ArtificialIntelligence.AI_ACTION_GO_LEFT:
				result = x > 0 && !isObstacle(x-1, y);
				break;
			case ArtificialIntelligence.AI_ACTION_GO_RIGHT:
				result = x < (getZoneMatrixDimX()-1) && !isObstacle(x+1, y);
				break;
			default:
				result = false;
				break;
		}
		return result;
	}
	
	/**
	 * Indique si la case située à la position passée en paramètre
	 * constitue un obstacle pour un personnage : bombe, feu, mur.
	 * @param x	position à étudier
	 * @param y	position à étudier
	 * @return	vrai si la case contient un obstacle
	 */
	public boolean isObstacle(int x, int y)
	{	
		boolean result = false;
		
		// bombes
		result = result || getZoneMatrix()[x][y] == AI_BLOCK_BOMB;
		
		// feu
		result = result || getZoneMatrix()[x][y] == AI_BLOCK_FIRE;
		
		// murs
		result = result || getZoneMatrix()[x][y] == AI_BLOCK_WALL_HARD;
		result = result || getZoneMatrix()[x][y] == AI_BLOCK_WALL_SOFT;
		
		// on ne sait pas quoi
		result = result || getZoneMatrix()[x][y] == AI_BLOCK_UNKNOWN;
		
		// shrink
		result = result || (getTimeBeforeShrink() == -1 && x == getNextShrinkPosition()[0]&& y == getNextShrinkPosition()[1]);
		return result;
	}

	/**
	 * Calcule et renvoie la distance de Manhattan 
	 * entre le point de coordonnées (x1,y1) et celui de coordonnées (x2,y2). 
	 * @param x1	position du premier point
	 * @param y1	position du premier point
	 * @param x2	position du second point
	 * @param y2	position du second point
	 * @return		la distance de Manhattan entre ces deux points
	 */
	public int distance(int x1, int y1, int x2, int y2)
	{
		return (Math.abs(x1-x2) + Math.abs(y1-y2));
	}
	

	
	/**
	 * Renvoie les positions de tous les personnages sauf nous.
	 * CAGLAYAN+ELMAS
	 * @return	Vecteur contenant les positions de tous les personnages
	 */
	protected Vector<int[]> getAllPlayerPositions()
	{	
		 Vector<int[]> result = new Vector<int[]>();
         for (int i = 0; i < getPlayerCount(); i++)
        	 if (isPlayerAlive(i))
                 result.add(getPlayerPosition(i));
         return result;
	}
}
