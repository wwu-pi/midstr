/**
 * 
 */
package de.wwu.pi.patecaru.mq;

import de.wwu.pi.patecaru.mq.listener.PatecaruRunListener;

/**
 * @author vincentvonhof
 *
 */
public interface IWorkDistributer {
	
	public boolean addListener(PatecaruRunListener listener);
	public boolean removeLinster(PatecaruRunListener listener);
	
}
