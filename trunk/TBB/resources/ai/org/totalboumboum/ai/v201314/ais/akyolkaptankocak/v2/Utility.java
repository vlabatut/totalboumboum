package org.totalboumboum.ai.v201314.ais.akyolkaptankocak.v2;

/**
 * This class is the utility class for defining constants which are used in
 * various classes.
 * 
 * @author Fırat Akyol
 * @author Mustafa Kaptan
 * @author Gökberk Koçak
 */
public class Utility
{
    /**
     * Integer value of hidden item limit.
     */
    public static final int HIDDEN_ITEM_LIMIT = 4;

    /**
     * Integer value of limit of multiplied bomb range (number of bombs *
     * range).
     */
    public static final int MULTIPLIED_BOMB_RANGE_LIMIT = 7;

    /**
     * Double value of evaluated probability limit for hidden items.
     */
    public static final double EVALUATED_PROBABILITY_LIMIT = 0.35;

    /**
     * Double value of speed limit for the hero.
     */
    public static final double SPEED_LIMIT = 200;

    /**
     * Double value for golden items. It must be higher than 1, because normal
     * items values are 1.
     */
    public static final double GOLDEN_ITEM_VALUE = 1.5;

}
