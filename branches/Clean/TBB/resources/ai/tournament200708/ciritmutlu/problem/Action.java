package tournament200708.ciritmutlu.problem;


/**
 * Classe representant une action applicable a un etat
 * d'origine et permettant d'obtenir un etat cible.
 */
public class Action 
{	
	@SuppressWarnings("unused")
	private int action;
	private double cost;
	
	public Action(int  action, double cost) {
		this.action = action;
		this.cost = cost;
	}
	
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
