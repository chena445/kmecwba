package cn.edu.hnu.esnl.util;

import java.util.LinkedList;


//�Զ������
public class JdasQueue<T> extends LinkedList<T> {


	public void in(T o) {
		super.addLast(o);
	}

	public T out() {
		T t = null ;
		try{
		t = super.removeFirst();
		}catch(Exception e){
			
		}
		return t ;
	}

	public void getQueue() {
		for (int i = 0; i < super.size(); i++) {
			System.out.println(super.get(i));
		}
	}

	
}

