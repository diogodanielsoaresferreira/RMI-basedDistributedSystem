/**
 * This package contains the five shared regions used in the program:
 * Betting Center, Control Center, Paddock, Racing Track and Stable.
 * All these classes have functions to be used by multiple threads:
 * the broker, the spectators and the horse/jockey.
 * Some functions will also change the state of the entities and call the logger.
 */
package SharedRegions;
