import java.io.*;
import java.lang.*;
import java.math.BigInteger;
import java.util.Random;

public class Testing {
	
	static int INF = 100000;
	static int nPrimes;
	static int Prime[] = new int [INF + 1]; 
	static boolean isPrime[] = new boolean [INF + 1];
	
	public static boolean NormalTest(int number){
		int i;
		if (number <= 1) return false;
		for (i = 0; i < nPrimes; i++)
			if (Prime[i] <= (int)(Math.sqrt(number))){
				if (number % Prime[i] == 0) return false;
			}else break;
		return true;
	}
	
	public static void main(String args[]) throws IOException {
		int i, j;
		boolean res1, res2;
		
		for (i = 2; i <= INF; i++) isPrime[i] = true;
		nPrimes = 0;
		for (i = 2; i <= INF; i++)
			if (isPrime[i]){
				Prime[nPrimes] = i;
				nPrimes++;
				j = i + i;
				while (j <= INF){
					isPrime[j] = false;
					j += i;
				}
			}
		
		BigInteger number;
		for (i = 1100000; i <= 10000000; i++){
			number = new BigInteger(Integer.toString(i));
			res1 = RSA.RabinMillerTest(number);
			res2 = NormalTest(i);
			if (res1 != res2){
				System.out.println(number);
				System.out.println(res1+" "+res2);
				break;
			}
			if (i % 10000 == 0)
				System.out.println(i);
		}
	}	
	
}
