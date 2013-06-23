package org.totalboumboum.ai.v201011.ais.hacibektasogluilgar.v4;

/**
 * @author Elif Nurdan ÝLGAR 
 * @author Engin Hacýbektaþoðlu
 *
 */
public enum Etats {
			VIDE,
			VIDE_AU_MUR_DESTRUCTIBLE,
			VIDE_PRIORITAIRE,
			MUR_DESTRUCTIBLE,
			MUR_NON_DESTRUCTIBLE,
			FEU,
			FLAMMABLE_VERT, // l'etat qui a moin de risque
			FLAMMABLE_ROUGE, // l'etat qui a plus de risque
			BONUS,
			BOMB,
			ADVERSAIRE,
			ADVERSAIRE_BOMB,
			ADVERSAIRE_BONUS
}
