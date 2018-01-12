package de.wwu.pi.patecaru.mq;

import de.wwu.pi.patecaru.datatypes.mq.PatecaruResult;

/*
 * 
 * @author Vincent von Hof
 */
public abstract class AbstractWorkDistributer implements IWorkDistributer {

	abstract public void notifyListeners(PatecaruResult result);

}
