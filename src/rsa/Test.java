package rsa;

import java.util.Iterator;
import java.util.Stack;

public class Test {

	public static int test(int a,int b) {
		//a*d-b*k=1
		Stack<int[]> list = new Stack<int[]>();
		int[] t = {a,b};
		list.push(t);
		while(a!=1&&b!=1) {
			int[] temp = new int[2];
			if(a<b) {
				temp[0] = a;
				temp[1] = Math.floorMod(b, a);
				list.push(temp);
			}else {
				temp[0] = Math.floorMod(a, b);
				temp[1] = b;
				list.push(temp);
			}
			a = temp[0];
			b = temp[1];
		}
		//a=1µÄ•rºò,y=1;b=1•r,x=1¡£
		//a*x-b*y=1
		Iterator<int[]> iter = list.iterator();
		int x = 0,y = 0;
		if(b==1) {
			x = 1;
			int f = 1;
			while(iter.hasNext()) {
				int[] temp = list.pop();
				if((f&1)==1) {
					y = (temp[0]*x-1)/temp[1];
					x = (temp[1]*y+1)/temp[0];
				} else {
					x = (temp[1]*y+1)/temp[0];
					y = (temp[0]*x-1)/temp[1];
				}
				f++;
			}
		}
		if(a==1) {
			y = 0;
			int f = 1;
			while(iter.hasNext()) {
				int[] temp = list.pop();
				if((f&1)==0) {
					y = (temp[0]*x-1)/temp[1];
					x = (temp[1]*y+1)/temp[0];
				} else {
					x = (temp[1]*y+1)/temp[0];
					y = (temp[0]*x-1)/temp[1];
				}
				f++;
			}
		}
		return x;
	}

	public static void main(String[] args) {
		//int t = test(101,4620);
		System.out.println();
	}

}
