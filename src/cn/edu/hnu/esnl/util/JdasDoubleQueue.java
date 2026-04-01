package cn.edu.hnu.esnl.util;

import java.util.LinkedList;

//�Զ������˫�����
class JdasDoubleQueue<T> extends LinkedList<T> {
	

	public void inTail(T o) {
		super.addLast(o);
	}

	public void inHead(T o) {
		super.addFirst(o);
	}

	public T outHead() {
		return super.removeFirst();
	}

	public T outTail() {
		return super.removeLast();
	}

	public void getQueueFromHead() {
		for (int i = 0; i < super.size(); i++) {
			System.out.println(super.get(i));
		}
	}

	public void getQueueFromTail() {
		for (int i = super.size() - 1; i >= 0; i--) {
			System.out.println(super.get(i));
		}
	}

	
}