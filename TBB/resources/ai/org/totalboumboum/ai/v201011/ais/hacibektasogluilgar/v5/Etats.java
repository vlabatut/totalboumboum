package org.totalboumboum.ai.v201011.ais.hacibektasogluilgar.v5;

/**
 * @author Engin Hacıbektaşoğlu
 * @author Elif Nurdan İlgar
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
			BOMBE,
			ADVERSAIRE,
			ADVERSAIRE_BOMB,
			ADVERSAIRE_BONUS
}

