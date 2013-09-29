package org.totalboumboum.ai.v201314.adapter.path.successor;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

/**
 * Valeurs que le mode de recherche peut prendre,
 * pour certaines fonctions successeurs comme
 * {@link TimeFullSuccessorCalculator} et {@link TimePartialSuccessorCalculator}.
 * En règle générale, plus un mode de recherche
 * considère de possibilités, et plus il est lent.
 * En contrepartie, moins il considère de possibilités
 * et plus il risque de ne pas détecter des chemins valides.
 * Les valeurs sont classées du mode le moins restrictif au
 * mode la plus restrictif.
 * 
 * @author Vincent Labatut
 */
public enum SearchMode
{	/** Toutes les cases sont considérées (lent) */
	MODE_ALL,
	/** Seules les cases pas encore traitées dans la branche courante sont considérées */
	MODE_NOBRANCH,
	/** Seules les cases pas encore traitées, ou alors juste une seule fois, dans la branche courante sont considérées */
	MODE_ONEBRANCH,
	/** Seules les cases pas encore traitées du tout son considérées (rapide) */
	MODE_NOTREE;
}
