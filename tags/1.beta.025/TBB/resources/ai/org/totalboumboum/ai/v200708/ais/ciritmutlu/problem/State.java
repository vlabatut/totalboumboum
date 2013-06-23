package org.totalboumboum.ai.v200708.ais.ciritmutlu.problem;

/**
 * Representation d'un etat du probleme. 
 */
public class State 
{	
	/** */
	private boolean playerInMiddle; 	// le joueur est au milieu de la zone de jeu
	/** */
    private boolean playerFarPlayer; 	// le joueur est loin des autres joueurs
	/** */
    private boolean playerFarBomb;		// le joueur est loin de bombe
	/** */
    private boolean playerClosePlayer;	// le joueur est pres d'un autre joueur 
	/** */
    private boolean playerCloseBomb;	// le joueur est pres d'une bombe
	/** */
    private boolean playerAlone;		// le joueur est seul dans la zone de jeu
	/** */
    private boolean bonusClose;			// pres du joueur se trouve un bonus
	/** */
    private boolean shrinkClose;		// le shrink est proche du joueur
	/** */
    private boolean blockEmpty;			// il s'agit d'un bloc vide 
	/** */
    private boolean wallSoft;			// il s'agit d'un bloc de mur destructible  
	/** */
    private boolean wallHard;			// il s'agit d'un bloc de mur indestructible 
	/** */
    private boolean playerCount2;		// il s'agit de deux joueurs dans la zone de jeu
	/** */
    private boolean playerCount3;		// il s'agit de trois joueurs dans la zone de jeu
	/** */
    private boolean playerCount4;		// il s'agit de quatre joueurs dans la zone de jeu
	/** */
    @SuppressWarnings("unused")
	private double point;				// le point obtenu selon les etats 

    /**
     * 
     * @param playerInMiddle
	 * 		Description manquante !
     * @param playerFarPlayer
	 * 		Description manquante !
     * @param playerFarBomb
	 * 		Description manquante !
     * @param playerClosePlayer
	 * 		Description manquante !
     * @param playerCloseBomb
	 * 		Description manquante !
     * @param playerAlone
	 * 		Description manquante !
     * @param bonusClose
	 * 		Description manquante !
     * @param shrinkClose
	 * 		Description manquante !
     * @param blockEmpty
	 * 		Description manquante !
     * @param wallSoft
	 * 		Description manquante !
     * @param wallHard
	 * 		Description manquante !
     * @param playerCount2
	 * 		Description manquante !
     * @param playerCount3
	 * 		Description manquante !
     * @param playerCount4
	 * 		Description manquante !
     */
	public State(boolean playerInMiddle, boolean playerFarPlayer, boolean playerFarBomb, boolean playerClosePlayer, boolean playerCloseBomb, boolean playerAlone, boolean bonusClose, boolean shrinkClose, boolean blockEmpty, boolean wallSoft, boolean wallHard, boolean playerCount2, boolean playerCount3, boolean playerCount4) {
		super();
		this.playerInMiddle = playerInMiddle;
		this.playerFarPlayer = playerFarPlayer;
		this.playerFarBomb = playerFarBomb;
		this.playerClosePlayer = playerClosePlayer;
		this.playerCloseBomb = playerCloseBomb;
		this.playerAlone = playerAlone;
		this.bonusClose = bonusClose;
		this.shrinkClose = shrinkClose;
		this.blockEmpty = blockEmpty;
		this.wallSoft = wallSoft;
		this.wallHard = wallHard;
		this.playerCount2 = playerCount2;
		this.playerCount3 = playerCount3;
		this.playerCount4 = playerCount4;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public boolean isBlockEmpty() {
		return blockEmpty;
	}
	/**
	 * 
	 * @param blockEmpty
	 * 		Description manquante !
	 */
	public void setBlockEmpty(boolean blockEmpty) {
		this.blockEmpty = blockEmpty;
	}
	/**
	 * 
	 * @return
	 * 		? 
	 */
	public boolean isPlayerCount2() {
		return playerCount2;
	}
	/**
	 * 
	 * @param playerCount2
	 * 		Description manquante !
	 */
	public void setPlayerCount2(boolean playerCount2) {
		this.playerCount2 = playerCount2;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public boolean isPlayerCount3() {
		return playerCount3;
	}
	/**
	 * 
	 * @param playerCount3
	 * 		Description manquante !
	 */
	public void setPlayerCount3(boolean playerCount3) {
		this.playerCount3 = playerCount3;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public boolean isPlayerCount4() {
		return playerCount4;
	}
	/**
	 * 
	 * @param playerCount4
	 * 		Description manquante !
	 */
	public void setPlayerCount4(boolean playerCount4) {
		this.playerCount4 = playerCount4;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public boolean isWallHard() {
		return wallHard;
	}
	/**
	 * 
	 * @param wallHard
	 * 		Description manquante !
	 */
	public void setWallHard(boolean wallHard) {
		this.wallHard = wallHard;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public boolean isWallSoft() {
		return wallSoft;
	}
	/**
	 * 
	 * @param wallSoft
	 * 		Description manquante !
	 */
	public void setWallSoft(boolean wallSoft) {
		this.wallSoft = wallSoft;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public boolean isBonusClose() {
		return bonusClose;
	}
	/**
	 * 
	 * @param bonusClose
	 * 		Description manquante !
	 */
	public void setBonusClose(boolean bonusClose) {
		this.bonusClose = bonusClose;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public boolean isShrinkClose() {
		return shrinkClose;
	}
	/**
	 * 
	 * @param shrinkClose
	 * 		Description manquante !
	 */
	public void setShrinkClose(boolean shrinkClose) {
		this.shrinkClose = shrinkClose;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public boolean isPlayerAlone() {
		return playerAlone;
	}

	/**
	 * 
	 * @param playerAlone
	 * 		Description manquante !
	 */
	public void setPlayerAlone(boolean playerAlone) {
		this.playerAlone = playerAlone;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public boolean isPlayerInMiddle() {
		return playerInMiddle;
	}
	/**
	 * 
	 * @param playerInMiddle
	 * 		Description manquante !
	 */
	public void setPlayerInMiddle(boolean playerInMiddle) {
		this.playerInMiddle = playerInMiddle;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public boolean isPlayerCloseBomb() {
		return playerCloseBomb;
	}
	/**
	 * 
	 * @param playerCloseBomb
	 * 		Description manquante !
	 */
	public void setPlayerCloseBomb(boolean playerCloseBomb) {
		this.playerCloseBomb = playerCloseBomb;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public boolean isPlayerClosePlayer() {
		return playerClosePlayer;
	}
	/**
	 * 
	 * @param playerClosePlayer
	 * 		Description manquante !
	 */
	public void setPlayerClosePlayer(boolean playerClosePlayer) {
		this.playerClosePlayer = playerClosePlayer;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public boolean isPlayerFarBomb() {
		return playerFarBomb;
	}
	/**
	 * 
	 * @param playerFarBomb
	 * 		Description manquante !
	 */
	public void setPlayerFarBomb(boolean playerFarBomb) {
		this.playerFarBomb = playerFarBomb;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public boolean isPlayerFarPlayer() {
		return playerFarPlayer;
	}
	/**
	 * 
	 * @param playerFarPlayer
	 * 		Description manquante !
	 */
	public void setPlayerFarPlayer(boolean playerFarPlayer) {
		this.playerFarPlayer = playerFarPlayer;
	}
	/**
	 * 
	 * @param point
	 * 		Description manquante !
	 */
	public void setHeuristic(double point){
		this.point=point;
	}
}
