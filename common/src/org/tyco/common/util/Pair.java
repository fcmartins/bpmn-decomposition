package org.tyco.common.util;

/**
 * Represents a pair. A tuple with two entries.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Jul 31, 2008
 *
 * @param <L>
 * @param <R>
 */
public class Pair<L, R> {
	private L left;
	private R right;

	/**
	 * Defaults left and right to null.
	 */
	public Pair() {
		left = null;
		right = null;
	}

	/**
	 * Specify the left and right members.
	 * @param left
	 * @param right
	 */
	public Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}

	/**
	 * Gets the left member.
	 * @return
	 */
	public L getLeft() {
		return left;
	}

	/**
	 * Sets the left member.
	 * @param left
	 */
	public void setLeft(L left) {
		this.left = left;
	}

	/**
	 * Gets the right member.
	 * @return
	 */
	public R getRight() {
		return right;
	}

	/**
	 * Sets the right member.
	 * @param right
	 */
	public void setRight(R right) {
		this.right = right;
	}

}
