/**
 * 
 */
package org.callas.absyn.processes;

public enum BinaryOperator {
	LONG_ADDITION {
		@Override
		public String toString() {
			return "+";
		}
	},
	DOUBLE_ADDITION {
		@Override
		public String toString() {
			return "+.";
		}
	},
	LONG_SUBTRACTION {
		@Override
		public String toString() {
			return "-";
		}
	},
	DOUBLE_SUBTRACTION {
		@Override
		public String toString() {
			return "-.";
		}
	},
	LONG_MULTIPLICATION {
		@Override
		public String toString() {
			return "*";
		}
	},
	DOUBLE_MULTIPLICATION {
		@Override
		public String toString() {
			return "*.";
		}
	},
	DOUBLE_DIVISION {
		@Override
		public String toString() {
			return "/";
		}
	},
	LONG_DIVISION {
		@Override
		public String toString() {
			return "//";
		}
	},
	LONG_MODULUS {
		@Override
		public String toString() {
			return "%";
		}
	},
	/*
	DOUBLE_STAR {
		@Override
		public String toString() {
			return "**";
		}
	},
	*/
	LONG_SHIFT_RIGHT {
		@Override
		public String toString() {
			return ">>";
		}
	},
	LONG_SHIFT_LEFT {
		@Override
		public String toString() {
			return "<<";
		}
	},
	LONG_AND {
		@Override
		public String toString() {
			return "&";
		}
	},
	LONG_OR {
		@Override
		public String toString() {
			return "|";
		}
	},
	LONG_EXCLUSIVE_OR {
		@Override
		public String toString() {
			return "^";
		}
	},
	BOOL_EXCLUSIVE_OR {
		@Override
		public String toString() {
			return "xor";
		}
	},
	BOOL_AND {
		@Override
		public String toString() {
			return "and";
		}
	},
	BOOL_OR {
		@Override
		public String toString() {
			return "or";
		}
	},
	LONG_LESS_THAN {
		@Override
		public String toString() {
			return "<";
		}
	},
	DOUBLE_LESS_THAN {
		@Override
		public String toString() {
			return "<.";
		}
	},
	LONG_GREATER_THAN {
		@Override
		public String toString() {
			return ">";
		}
	},
	DOUBLE_GREATER_THAN {
		@Override
		public String toString() {
			return ">.";
		}
	},
	LONG_EQUALS {
		@Override
		public String toString() {
			return "==";
		}
	},
	LONG_GREATER_THAN_EQUALS {
		@Override
		public String toString() {
			return ">=";
		}
	},
	LONG_LESS_THAN_EQUALS {
		@Override
		public String toString() {
			return "<=";
		}
	},
	LONG_DIFFERENT {
		@Override
		public String toString() {
			return "!=";
		}
	},
}