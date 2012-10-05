package org.totalboumboum.ai.v200708.ais.ciritmutlu.problem;

import java.util.Iterator;
import java.util.Vector;

/**
 * Represente et permet d'initialiser le probleme.
 */
public class Problem
{	// ensemble des etats initiaux du probleme
	private Vector<State> initialStates;
	// ensemble des actions possibles
	private Vector<Action> actions;
	
	/**
	 * Constructeur. 
	 */
	public Problem()
	{	initialStates = new Vector<State>();
		initStates();
		actions = new Vector<Action>();
	}

	/**
	 * Initialise les etats initiaux 
	 */
	public void initStates()
	{	// etat initial
		State initialState = new State(false, false, false,false, false, false,false,false,false,false,false,false,false,false); 
		addInitialState(initialState);
	}

	/**
	 * Rajoute un nouvel etat initial au probleme. 
	 * @param state	le nouvel etat initial
	 */
	public void addInitialState(State state)
	{	if(!initialStates.contains(state))
			initialStates.add(state);
	}

	/**
	 * Renvoie le premier etat initial de la liste. 
	 * @return	le premier etat initial
	 */
	public State getInitialState()
	{	return initialStates.lastElement();	
	}

	/**
	 * Renvoie un iterateur sur la liste des actions possibles.
	 * return un itertateur sur une liste d'actions
	 * @return
	 * 		? 
	 */
	public Iterator<Action> getActionsIterator()
	{	return actions.iterator();	
	}
	
}
