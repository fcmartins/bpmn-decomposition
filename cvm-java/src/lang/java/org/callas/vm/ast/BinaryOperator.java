/**
 * 
 */
package org.callas.vm.ast;

public enum BinaryOperator {
	LONG_ADDITION {
		@Override
		public String toString() {
			return "ladd";
		}
	},
	DOUBLE_ADDITION {
		@Override
		public String toString() {
			return "dadd";
		}
	},
	LONG_SUBTRACTION {
		@Override
		public String toString() {
			return "lsub";
		}
	},
	DOUBLE_SUBTRACTION {
		@Override
		public String toString() {
			return "dsub";
		}
	},
	LONG_MULTIPLICATION {
		@Override
		public String toString() {
			return "lmul";
		}
	},
	DOUBLE_MULTIPLICATION {
		@Override
		public String toString() {
			return "dmul";
		}
	},
	LONG_DIVISION {
		@Override
		public String toString() {
			return "ldiv";
		}
	},
	DOUBLE_DIVISION {
		@Override
		public String toString() {
			return "ddiv";
		}
	},
	LONG_REMAINDER {
		@Override
		public String toString() {
			return "lrem";
		}
	},
	LONG_SHIFT_RIGHT {
		@Override
		public String toString() {
			return "lshr";
		}
	},
	LONG_SHIFT_LEFT {
		@Override
		public String toString() {
			return "lshl";
		}
	},
	LONG_AND {
		@Override
		public String toString() {
			return "land";
		}
	},
	BOOL_AND {
		@Override
		public String toString() {
			return "band";
		}
	},
	LONG_OR {
		@Override
		public String toString() {
			return "lor";
		}
	},
	BOOL_OR {
		@Override
		public String toString() {
			return "bor";
		}
	},
	LONG_EXCLUSIVE_OR {
		@Override
		public String toString() {
			return "lxor";
		}
	},
	BOOL_EXCLUSIVE_OR {
		@Override
		public String toString() {
			return "bxor";
		}
	},
	LONG_EQUALS {
		@Override
		public String toString() {
			return "le";
		}
	},
	LONG_LESS_THAN {
		@Override
		public String toString() {
			return "llt";
		}
	},
	LONG_GREATER_THAN {
		@Override
		public String toString() {
			return "lgt";
		}
	},
	DOUBLE_EQUALS {
		@Override
		public String toString() {
			return "de";
		}
	},
	DOUBLE_LESS_THAN {
		@Override
		public String toString() {
			return "dlt";
		}
	},
	DOUBLE_GREATER_THAN {
		@Override
		public String toString() {
			return "dgt";
		}
	},
}