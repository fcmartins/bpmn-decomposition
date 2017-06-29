/**
 * 
 */
package org.callas.vm.ast;

public enum UnaryOperator {
	LONG_NEGATION {
		@Override
		public String toString() {
			return "lneg";
		}
	},
	DOUBLE_NEGATION {
		@Override
		public String toString() {
			return "dneg";
		}
	},
	BOOL_NOT {
		@Override
		public String toString() {
			return "bnot";
		}
	},
	LONG_NOT {
		@Override
		public String toString() {
			return "lnot";
		}
	},
}