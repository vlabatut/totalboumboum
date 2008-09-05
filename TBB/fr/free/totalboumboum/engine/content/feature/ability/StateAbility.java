package fr.free.totalboumboum.engine.content.feature.ability;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.loop.Loop;

public class StateAbility extends AbstractAbility
{	
	/*************************
	 * STATES ABILITIES
	 *************************/
	/** 
	 * sprite compl�tement plat, sans relief 
	 */
	public static final String SPRITE_FLAT = "sprite-flat";
	/** 
	 * sprite situ� en hauteur, a dessiner par dessus tous les autres graphismes 
	 */
	public static final String SPRITE_ABOVE= "sprite-above";
	/** 
	 * capacit� � traverser les bombes
	 */
	public static final String SPRITE_TRAVERSE_BOMB = "sprite-traverse-bomb";
	/** 
	 * capacit� � traverser les bombes
	 */
	public static final String SPRITE_TRAVERSE_ITEM = "sprite-traverse-item";
	/** 
	 * capacit� � traverser les murs
	 */
	public static final String SPRITE_TRAVERSE_WALL = "sprite-traverse-wall";
	/** 
	 * capacit� � repousser pour un bloc
	 */
	public static final String BLOCK_SPAWN = "block-spawn";
	/** 
	 * d�finit la dur�e que met une bombe � exploser quand elle est consum�e par une
	 * explosion ext�rieure (pour les r�actions en cha�ne). 
	 */
	public static final String BOMB_EXPLOSION_LATENCY = "bomb-explosion-latency";
	/** 
	 * dur�e maximale de la panne de la bombe 
	 */
	public static final String BOMB_FAILURE_MAXDURATION = "bomb-failure-maxduration";
	/** 
	 * dur�e minimale de la panne de la bombe 
	 */
	public static final String BOMB_FAILURE_MINDURATION = "bomb-failure-minduration";
	/** 
	 * probabilit� qu'une bombe tombe en panne quand elle re�oit un ordre d'explosion :
	 * que ce soit temps, contact ou t�l�commande (ou autre) 
	 */
	public static final String BOMB_FAILURE_PROBABILITY = "bomb-failure-probability";
	/** 
	 * dur�e pendant laquelle la bombe doit �tre pouss�e avant de se mettre � glisser 
	 */
	public static final String BOMB_PUSH_LATENCY = "bomb-push-latency";
	/** 
	 * bombe d�clench�e par une collision 
	 */
	public static final String BOMB_TRIGGER_COLLISION = "bomb-trigger-collision";
	/** 
	 * bombe d�clench�e � la combustion 
	 */
	public static final String BOMB_TRIGGER_COMBUSTION = "bomb-trigger-combustion";
	/** 
	 * bombe t�l�command�e par le joueur 
	 */
	public static final String BOMB_TRIGGER_CONTROL = "bomb-trigger-control";
	/** 
	 * bombe � retardement, l'explosion est d�clench�e au bout d'un certain temps 
	 */
	public static final String BOMB_TRIGGER_TIMER = "bomb-trigger-timer";
	/** 
	 * nombre de bombes pouvant �tre pos�es en m�me temps 
	 */
	public static final String BOMB_NUMBER = "bomb-number";
	/** 
	 * port�e des bombes 
	 */
	public static final String BOMB_RANGE = "bomb-range";
	/** 
	 * dur�e pendant laquelle le hero doit pleurer/exulter � la fin du round 
	 */
	public static final String HERO_CELEBRATION_DURATION = "hero-celebration-duration";
	/** 
	 * dur�e du coup de poing donn� par le hero 
	 */
	public static final String HERO_PUNCH_DURATION = "hero-punch-duration";
	/** 
	 * dur�e pendant laquelle le hero doit �tre inactif avant de passer en gesture waiting 
	 */
	public static final String HERO_WAIT_DURATION = "hero-wait-duration";
	
	
	/** 
	 * name of the ability 
	 */
	private String name;
	
	public StateAbility(String name, Level level)
	{	super(level);
		this.name = name;
	}
	
	public String getName()
	{	return name;
	}
	public void setName(String name)
	{	this.name = name;
	}

	public boolean equals(Object ability)
	{	boolean result = false;
		if(ability instanceof StateAbility)
		{	StateAbility ab = (StateAbility)ability;
			// name
			result = name.equalsIgnoreCase(ab.getName());
		}
		return result;
	}
	
	public AbstractAbility copy()
	{	StateAbility result;
		result = new StateAbility(name,level);
		result.setStrength(strength);
		result.setUses(uses);
		result.setTime(time);
		return result;
	}

	public String toString()
	{	String result = name+"("+")";
		result = result+"["+strength+"]";
		return result;
	}
	
	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}

}
