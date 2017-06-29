/**
 * 
 */
package org.callas.absyn.processes;

public enum UnaryOperator {
	BOOL_NOT {
		@Override
		public String toString() {
			return "not";
		}
	},
	LONG_NOT {
		@Override
		public String toString() {
			return "~";
		}
	},
	LONG_NEGATION {
		@Override
		public String toString() {
			return "-";
		}
	},
	DOUBLE_NEGATION {
		@Override
		public String toString() {
			return "-.";
		}
	},
}