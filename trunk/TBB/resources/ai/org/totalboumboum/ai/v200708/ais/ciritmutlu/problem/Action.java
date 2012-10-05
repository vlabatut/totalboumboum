package org.totalboumboum.ai.v200708.ais.ciritmutlu.problem;


/**
 * Classe representant une action applicable a un etat
 * d'origine et permettant d'obtenir un etat cible.
 */
public class Action 
{	
	@SuppressWarnings("unused")
	private int action;
	private double cost;
	
	/**
	 * 
	 * @param action
	 * @param cost
	 */
	public Action(int  action, double cost) {
		this.action = action;
		this.cost = cost;
	}
	
	/**
	 * 
	 * @param action
	 */
	public Action(int action) {
		this.action = action;
		this.cost = 0;
	}

	/**
	 * Renvoie le cout de l'action. 
	 * @return	le cout de l'action.
	 */
	public double getCost()
	{
		return cost;
	}
}
