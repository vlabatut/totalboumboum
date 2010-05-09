package tournament200809v1.demiragsagar;

public class LienRecherche {
	// noeud parent
	private Node origin;
	// noeud fils
	private Node target;

	/**
	 * Constructeur.
	 * 
	 * @param origin noeud parent
	 * @param target noeud fils
	 * @param action action de transition
	 */
	public LienRecherche(Node origin, Node target) {
		this.origin = origin;
		this.target = target;
	}

	/**
	 * Renvoie le noeud parent du lien
	 * 
	 * @return le noeud parent
	 */
	public Node getOrigin() {
		return origin;
	}

	/**
	 * Renvoie le noeud fils du lien
	 * 
	 * @return le noeud fils
	 */
	public Node getTarget() {
		return target;
	}

	/**
	 * Renvoie l'action de transition
	 * 
	 * @return l'action associ�e au lien
	 */

	@Override
	public boolean equals(Object object) {
		boolean result;
		if (object == null)
			result = false;
		else if (!(object instanceof LienRecherche))
			result = false;
		else {
			LienRecherche temp = (LienRecherche) object;
			result = temp.getOrigin() == getOrigin()
					&& temp.getTarget() == getTarget();
		}
		return result;
	}

	@Override
	public String toString() {
		String result = "[";
		result = result + getOrigin().toString();
		result = result + "] )->";
		result = result + "[";
		result = result + getTarget().toString();
		result = result + "]";
		return result;
	}
}
