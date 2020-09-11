package rsa;

public class MillerRabin {

	//���ٻ�����(a*b) mod c
	public static int pow(int a, int b, int c) {
		int ans = 0;
		int base = a;
		while(b!=0) {
			if((b&1)==1)ans = (base+ans)%c;
			base = (base+base)%c;
			b >>=1;
		}
		return ans;
	}
	
	//����,����(a^b) mod c
	public static int powmod(int a, int b, int c) {
		int ans = 1;
		int base = a;
		while(b!=0) {
			if((b&1)==1) ans = pow(ans,base,c);
			base = pow(base,base,c);
			b >>= 1;
		}
		return ans;
	}
	
	public static boolean millerRabinTest(int n) {
		
		//Random rand = new Random();
		int s = 0, d = n-1;
		int k;
		int p[] = {2,3,5,7,11,13,17,19};
		if(n == 2) return true;
		if(n<2||(n&1)==0) return false;
		for(int i : p) if(n==i) return true;
		//�ֽ⵽tΪ����
		while((d&1)==0){
			s++;
			d >>= 1;
		}
		for(int i = 0;i < 10;i++) {
			int a = p[i];
			//����a^d mod x
			int b = powmod(a,d,n);
			k = b;
			//ѭ�����㵽d = n-1,��s-1��
			for(int j = 1;j < s; j++) {
					k = pow(b, b, n);//����(b*b) mod n
					if(k==1&&b!=n-1&&b!=1) return false;
					b = k;
			}
			if(b!=1) return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		
		int n = 83;
		if(millerRabinTest(n)) {
			System.out.println("y");
		}
		else 
			System.out.println("n");
	}

}
