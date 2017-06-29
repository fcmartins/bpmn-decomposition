package org.tyco.common.util;

import java.util.Iterator;

/**
 * Utility class with functional-related operations.
 * 
 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
 * @date Jul 31, 2008
 *
 */
public class Functional {
	/**
	 * An iterator for the join function.
	 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
	 * @date Jul 31, 2008
	 *
	 * @param <L>
	 * @param <R>
	 */
	private static final class JoinIterator<L, R> implements
			Iterator<Pair<L, R>> {

		private Iterator<L> left;
		private Iterator<R> right;

		public JoinIterator(Iterator<L> left, Iterator<R> right) {
			this.left = left;
			this.right = right;
		}

		public boolean hasNext() {
			return left.hasNext() && right.hasNext();
		}

		public Pair<L, R> next() {
			return new Pair<L, R>(left.next(), right.next());
		}

		public void remove() {
			left.remove();
			right.remove();
		}
	}
	
	/**
	 * Creates an iterable for function 'join'.
	 * 
	 * @author Tiago Cogumbreiro (cogumbreiro@di.fc.ul.pt)
	 * @date Jul 31, 2008
	 *
	 * @param <L>
	 * @param <R>
	 */
	private static final class JoinIterable<L,R> implements Iterable<Pair<L,R>> {

		private Iterable<L> left;
		private Iterable<R> right;
		
		public JoinIterable(Iterable<L> left, Iterable<R> right) {
			this.left = left;
			this.right = right;
		}

		public Iterator<Pair<L, R>> iterator() {
			return new JoinIterator<L, R>(left.iterator(), right.iterator());
		}
		
	}

	/**
	 * The function join creates pairs with one element of each iterable.
	 * The number of elements generated is equal to the smallest
	 * iterable.
	 * @param <L>
	 * @param <R>
	 * @param left
	 * @param right
	 * @return
	 */
	public static <L, R> Iterable<Pair<L, R>> join(Iterable<L> left,
			Iterable<R> right) {
		return new JoinIterable<L, R>(left, right);
	}
}
