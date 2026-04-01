package cn.edu.hnu.esnl.util;

import java.util.LinkedList;

class JdasStack<T> extends LinkedList<T> {
	

	public void in(T o) {
		super.addLast(o);
	}

	public T out() {
		return super.removeLast();
	}

	public T get() {
		return super.getLast();
	}

	public void getStack() {
		for (int i = 0; i < super.size(); i++) {
			System.out.println(super.get(i));
		}
	}

	

}

