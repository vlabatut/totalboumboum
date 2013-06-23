package org.totalboumboum.ai.v200708.ais.ciritmutlu.problem;

/**
 * Representation d'un etat du probleme. 
 */
public class State 
{	
	private boolean playerInMiddle; 	// le joueur est au milieu de la zone de jeu
    private boolean playerFarPlayer; 	// le joueur est loin des autres joueurs
    private boolean playerFarBomb;		// le joueur est loin de bombe
    private boolean playerClosePlayer;	// le joueur est pres d'un autre joueur 
    private boolean playerCloseBomb;	// le joueur est pres d'une bombe
    private boolean playerAlone;		// le joueur est seul dans la zone de jeu
    private boolean bonusClose;			// pres du joueur se trouve un bonus
    private boolean shrinkClose;		// le shrink est proche du joueur
    private boolean blockEmpty;			// il s'agit d'un bloc vide 
    private boolean wallSoft;			// il s'agit d'un bloc de mur destructible  
    private boolean wallHard;			// il s'agit d'un bloc de mur indestructible 
    private boolean playerCount2;		// il s'agit de deux joueurs dans la zone de jeu
    private boolean playerCount3;		// il s'agit de trois joueurs dans la zone de jeu
    private boolean playerCount4;		// il s'agit de quatre joueurs dans la zone de jeu
    @SuppressWarnings("unused")
	private double point;				// le point obtenu selon les etats 

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
	public boolean isBlockEmpty() {
		return blockEmpty;
	}
	public void setBlockEmpty(boolean blockEmpty) {
		this.blockEmpty = blockEmpty;
	}
	public boolean isPlayerCount2() {
		return playerCount2;
	}
	public void setPlayerCount2(boolean playerCount2) {
		this.playerCount2 = playerCount2;
	}
	public boolean isPlayerCount3() {
		return playerCount3;
	}
	public void setPlayerCount3(boolean playerCount3) {
		this.playerCount3 = playerCount3;
	}
	public boolean isPlayerCount4() {
		return playerCount4;
	}
	public void setPlayerCount4(boolean playerCount4) {
		this.playerCount4 = playerCount4;
	}
	public boolean isWallHard() {
		return wallHard;
	}
	public void setWallHard(boolean wallHard) {
		this.wallHard = wallHard;
	}
	public boolean isWallSoft() {
		return wallSoft;
	}
	public void setWallSoft(boolean wallSoft) {
		this.wallSoft = wallSoft;
	}
	public boolean isBonusClose() {
		return bonusClose;
	}
	public void setBonusClose(boolean bonusClose) {
		this.bonusClose = bonusClose;
	}
	public boolean isShrinkClose() {
		return shrinkClose;
	}
	
	public void setShrinkClose(boolean shrinkClose) {
		this.shrinkClose = shrinkClose;
	}
	public boolean isPlayerAlone() {
		return playerAlone;
	}

	public void setPlayerAlone(boolean playerAlone) {
		this.playerAlone = playerAlone;
	}
	public boolean isPlayerInMiddle() {
		return playerInMiddle;
	}
	public void setPlayerInMiddle(boolean playerInMiddle) {
		this.playerInMiddle = playerInMiddle;
	}
	
	
public boolean isPlayerCloseBomb() {
		return playerCloseBomb;
	}

	public void setPlayerCloseBomb(boolean playerCloseBomb) {
		this.playerCloseBomb = playerCloseBomb;
	}

	public boolean isPlayerClosePlayer() {
		return playerClosePlayer;
	}

	public void setPlayerClosePlayer(boolean playerClosePlayer) {
		this.playerClosePlayer = playerClosePlayer;
	}

	public boolean isPlayerFarBomb() {
		return playerFarBomb;
	}

	public void setPlayerFarBomb(boolean playerFarBomb) {
		this.playerFarBomb = playerFarBomb;
	}

	public boolean isPlayerFarPlayer() {
		return playerFarPlayer;
	}

	public void setPlayerFarPlayer(boolean playerFarPlayer) {
		this.playerFarPlayer = playerFarPlayer;
	}

	public void setHeuristic(double point){
		this.point=point;
	}
}
