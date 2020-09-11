package rsa;

public class RSA {
	public int Algorithm(int data,int e,int n) {
		String result = Integer.toBinaryString(e);
		result=new StringBuffer(result).reverse().toString();
		char[] r=result.toCharArray(); 
		int[] a=new int[result.length()];
		int output=1;
		for(int i=0;i<result.length();++i) {
			a[i]=data%n;
			int tag=n-a[i];
			if(tag<a[i]) {
				a[i]=0-tag;
			}
			data=a[i]*a[i];
			if(r[i]=='0') {
				continue;
			}
			output=output*a[i];
			if(output>n||output<-n) {
				output=output%n;
			}
		}
		output=output%n;
		if(output<0)
			output=n+output;
		return output;	
	}

}


