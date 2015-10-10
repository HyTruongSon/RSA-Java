import java.io.*;
import java.lang.*;
import java.math.BigInteger;
import java.util.Random;

public class CrackRSA {
	
	static BigInteger ZERO = new BigInteger("0");
	static BigInteger ONE = new BigInteger("1"); 
	static BigInteger TWO = new BigInteger("2");
	static BigInteger LimitSRMT = new BigInteger("341550071728321");
	
	static BigInteger n, e;
	
	public static void GetPublicKey(String PublicInfo) throws IOException {
		String s;
		BufferedReader file = new BufferedReader(new FileReader(PublicInfo));
		n = new BigInteger(file.readLine());
		e = new BigInteger(file.readLine());
		file.close();
	}
	
	public static void main(String args[]) throws IOException {
		GetPublicKey(args[0]);
		BigInteger p = TWO;
		boolean found = false;
		while (true){
			if (n.compareTo(ONE) <= 0) break;
			while (n.mod(p).equals(ZERO)){
				n = n.divide(p);
				found = true;
			}
			System.out.println(p);
			if (found){
				System.out.println("Found the first prime factor!");
				break;
			}
			p = p.nextProbablePrime();
		}
	}	
	
}
