package org.totalboumboum.ai.v200708.ais.ciritmutlu.tree;

import java.util.Iterator;
import java.util.Vector;

import org.totalboumboum.ai.v200708.ais.ciritmutlu.exceptions.AbsentNodeException;
import org.totalboumboum.ai.v200708.ais.ciritmutlu.exceptions.ExistingNodeException;
import org.totalboumboum.ai.v200708.ais.ciritmutlu.problem.Action;
import org.totalboumboum.ai.v200708.ais.ciritmutlu.problem.Problem;
import org.totalboumboum.ai.v200708.ais.ciritmutlu.problem.State;




/**
 * Representation d'un arbre de recherche.
 */
public class SearchTree
{	
	/** */
	public static final long serialVersionUID = 1L;
	/** probleme traite */
	private Problem problem;
	/** ensemble des noeuds composant l'arbre */
	private Vector<SearchNode> nodes;
	/** ensemble des liens composant l'arbre */
	private Vector<SearchLink> links;
	/** dernier noeud traite lors de la recherche */
	@SuppressWarnings("unused")
	private SearchNode lastNode;
	/** iteration courante lors de la recherche */
	private int iteration;
	
	/**
	 * Constructeur. 
	 * @param problem	le probleme a traiter
	 */
	public SearchTree(Problem problem)
	{	this.problem = problem;
		init();
	}
	
	/**
	 * Initialise l'arbre (a faire avant de commencer une recherche)
	 */
	public void init()
	{	
		nodes = new Vector<SearchNode>();
		iteration = 0;
		SearchNode initialNode = new SearchNode(problem.getInitialState(),0);
		nodes.add(initialNode);
		lastNode = initialNode;
		links = new Vector<SearchLink>();
	}
	
	/**
	 * Renvoie la racine de l'arbre (un noeud contenant un etat initial)
	 * @return	la racine de l'arbre de recherche
	 */
	public SearchNode getRoot()
	{	return nodes.get(0);		
	}
	
	/**
	 * Determine si l'arbre contient le noeud passe en parametre.
	 * @param node	le noeud a rechercher
	 * @return	vrai si l'arbre contient le noeud
	 */
	public synchronized boolean containsNode(SearchNode node)
	{	boolean result=false;
		Iterator<SearchNode> i = nodes.iterator();
		while(i.hasNext() && !result)
			result = node == i.next();
		return result;		
	}
	
	
	/**
	 * Renvoie le lien liant le noeud passe en parametre e son pere.
	 * @param node	le noeud  a traiter
	 * @return	un lien contenant le noeud en position target
	 * @throws AbsentNodeException
	 * 		Description manquante !
	 */
	public SearchLink getParentLink(SearchNode node) throws AbsentNodeException
	{	SearchLink result=null;
		if(!containsNode(node))
			throw new AbsentNodeException(node);
		if(node != getRoot())
		{	Iterator<SearchLink> i = links.iterator();
			while(i.hasNext() && result==null)
			{	SearchLink temp = i.next();
				if(temp.getTarget() == node)
					result = temp;
			}
		}
		return result;
	}

	/**
	 * Renvoie tous les liens partant du noeud passe en parametre.
	 * @param node	le noeud a traiter
	 * @return	tous les liens oe ce noeud est en position origin
	 * @throws AbsentNodeException
	 * 		Description manquante !
	 */
	public synchronized Vector<SearchLink> getChildrenLinks(SearchNode node) throws AbsentNodeException
	{	Vector<SearchLink> result = new Vector<SearchLink>();
		if(!containsNode(node))
			throw new AbsentNodeException(node);
		Iterator<SearchLink> i = links.iterator();
		while(i.hasNext())
		{	SearchLink temp = i.next();
			if(temp.getOrigin() == node)
				result.add(temp);
		}
		return result;
	}

	/**
	 * 
	 * Renvoie une sequence de liens representant un chemin allant de la racine
	 * au noeud passe en parametre.
	 * @param node	le noeud e traiter
	 * @return	un vecteur de liens representant le chemin de puis la racine
	 * @throws AbsentNodeException
	 * 		Description manquante !
	 */
	public Vector<SearchLink> getPath(SearchNode node) throws AbsentNodeException
	{	Vector<SearchLink> result;
		SearchLink parentLink = getParentLink(node);
		if(parentLink==null)
			result = new Vector<SearchLink>();
		else
		{	result = getPath(parentLink.getOrigin());
			result.add(parentLink);
		}
		return result;
	}
	
	/**
	 * Cree un nouveau lien dans l'arbre. Le noeud origin doit etre present dans le graphe.
	 * Le noeud target doit etre absent de l'arbre.
	 * Si le noeud origin n'appartient pas au graphe, une AbsentNodeException est levee.
	 * Si le noeud target appartient deje au graphe, une ExistingNodeException est levee.
	 * @param	link	le lien a rajouter dans l'arbre
	 * @throws AbsentNodeException
	 * 		Description manquante !
	 * @throws ExistingNodeException
	 * 		Description manquante !
	 */
	public void addLink(SearchLink link) throws AbsentNodeException, ExistingNodeException
	{	addLinkSynch(link);
	}
	/**
	 * Methode utilisee par addLink pour des operations devant etre effectuees
	 * en mode synchronise.  
	 * @param link	le lien a ajouter
	 * @throws AbsentNodeException
	 * 		Description manquante !
	 * @throws ExistingNodeException
	 * 		Description manquante !
	 */
	private synchronized void addLinkSynch(SearchLink link) throws AbsentNodeException, ExistingNodeException
	{	SearchNode target = link.getTarget();
		if(containsNode(target))
			throw new ExistingNodeException(target);
		links.add(link);
		nodes.add(target);
	}
	

	/**
	 * Developpe le noeud passe en parametre, c'est a dire :
	 * 1) determine les actions applicables au noeud passe en parametre,
	 * 2) applique ces actions de maniere a obtenir les noeuds fils correspondants,
	 * 3) cree les liens adequats, et 4) ajoute ces liens e l'arbre.
	 * La liste des liens crees est renvoyee par la fonction. Une 
	 * AbsentNodeException est levee si le noeud passe en parametre n'appartient pas a l'arbre.
	 * @param targetState 
	 * 		Description manquante !
	 * 
	 * @param node	le noeud a developpe
	 * @return	un iterateur sur les liens vers les fils du noeud. 
	 * @throws AbsentNodeException 
	 * 		Description manquante !
	 */
	public Iterator<SearchLink> developNode(State targetState,SearchNode node) throws AbsentNodeException
	{	if(!containsNode(node))
			throw new AbsentNodeException(node);
		Vector<SearchLink> result = new Vector<SearchLink>();
		Iterator<Action> i = problem.getActionsIterator();
		while(i.hasNext())
		{	Action action = i.next();
			try
			{	
				double targetCost = node.getCost()+action.getCost();
				SearchNode target = new SearchNode(targetState,targetCost);
				SearchLink link = new SearchLink(node,target,action);
				result.add(link);
				addLink(link);
			}
			catch (ExistingNodeException e)
			{	//e.printStackTrace();
			}
		}
		return result.iterator();
	}

	/**
	 * Renvoie l'iteration courante de l'algorithme qui parcourt l'arbre.
	 * Si l'iteration vaut 0, c'est que le parcours n'a pas encore commence.
	 * @return	l'iteration courante
	 */
	public synchronized int getIteration()
	{	return iteration;
	}
	
	/**
	 * Renvoie le chemin du dernier noeud visite.
	 * Ce chemin prend la forme d'un vecteur de noeuds de recherche.
	 * @return	le chemin du dernier noeud visite
	 * @throws AbsentNodeException
	 * 		Description manquante !
	 */
	public synchronized Vector<SearchNode> getIterationPath() throws AbsentNodeException{	Vector<SearchNode> result = new Vector<SearchNode>();
		SearchNode node = null;
		Iterator<SearchNode> i = nodes.iterator();
		while(i.hasNext() && node==null)
		{	SearchNode temp = i.next();
			if(temp.getIteration()==iteration)
				node = temp;
		}
		Vector<SearchLink> temp = getPath(node);
		Iterator<SearchLink> j = temp.iterator();
		while(j.hasNext())
			result.add(j.next().getOrigin());
		result.add(node);
		return result;
	}
}
