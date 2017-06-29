package org.callas.vm;

/**
 * Obtains the current time of the system.
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 *
 */
public interface IClock {
	/**
	 * Get the current time in milliseconds.
	 * @return
	 */
	long currentTimeMillis();
	
	/**
	 * Uses {@link System#currentTimeMillis()} to get the time.
	 */
	IClock SYSTEM = new IClock() {
		public long currentTimeMillis() {
			return System.currentTimeMillis();
		}
	};
}
