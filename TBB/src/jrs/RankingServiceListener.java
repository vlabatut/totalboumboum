/*
 * RankingServiceListener.java
 *
 */

package jrs;

/** An interface objects must implement in order to register with the
  * RankingService to be notified when rating periods begin or end.
  *
  * @author Derek Hilder
  */
public interface RankingServiceListener<T> {
 
    /** Perform activities at the beginning of a rating period.
      * 
      * @param source 
      *     The RankingService whose rating period has begun.
      * @param periodNumber 
      *     The number of the rating period that has begun.
      */
    public void beginRatingPeriod(RankingService<T> source, int periodNumber);
    
    /** Perform activities at the end of a rating period.
      * 
      * @param source 
      *     The RankingService whose rating period has ended.
      * @param periodNumber 
      *     The number of the rating period that has ended.
     */
    public void endRatingPeriod(RankingService<T> source, int periodNumber);
    
}
