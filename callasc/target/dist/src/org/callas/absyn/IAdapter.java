package org.callas.absyn;

public interface IAdapter<F, T> {
	T adapt(F from);
}
