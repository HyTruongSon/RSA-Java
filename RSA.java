// Software: RSA
// Author: Hy Truong Son
// Major: BSc. Computer Science
// Class: 2013 - 2016
// Institution: Eotvos Lorand University, Budapest, Hungary
// Email: sonpascal93@gmail.com
// Website: http://people.inf.elte.hu/hytruongson/
// Last update: October 10, 2015
// Copyright 2015 (c) Hy Truong Son. All rights reserved.

import java.io.*;
import java.math.BigInteger;
import java.util.Random;

public class RSA {
	
	static BigInteger ZERO = new BigInteger("0");
	static BigInteger ONE = new BigInteger("1"); 
	static BigInteger TWO = new BigInteger("2");
	static BigInteger ByteValue = new BigInteger("256");
	static BigInteger LimitSRMT = new BigInteger("341550071728321");
	
	static int nSteps = 100;
	static int nDigits = 14;
	static int nChosenPrimes = 50;
	static int ByteArrayLength = 128;
	static int LimitOfSmallPrimes = 10000000;
	
	static BigInteger n, e, d, phi;
	
	static BigInteger ChosenPrime[] = new BigInteger [nChosenPrimes];
	
	static int ByteArray[] = new int [nDigits * nChosenPrimes];
	
	static int nSmallPrimes;
	static int SmallPrime[] = new int [LimitOfSmallPrimes];
	
	static BigInteger Power256[] = new BigInteger[nDigits * nChosenPrimes];
	
	static Random generator = new Random();
	
	public static int randomInt(int n){
		return generator.nextInt(n);
	}
	
	public static boolean isPowerOfTen(BigInteger number){
		String s = number.toString();
		if (s.charAt(0) != '1') return false;
		for (int i = 1; i < s.length(); i++)
			if (s.charAt(i) != '0') return false;
		return true;
	}
	
	public static BigInteger RandomBigInteger(int Length){
		int i, j;
		String s = "";
		for (i = 0; i < Length; i++){
			if (i == 0) j = randomInt(9) + 1; else
				j = randomInt(10);
			s += (char)('0' + j);
		}
		return new BigInteger(s);
	}
	
	// +-------------------------------+
	// | Randomize a "good" BigInteger |
	// +-------------------------------+
	
	public static BigInteger RandomBigInteger(BigInteger limit){
		int i, j, Length;
		int MaxLength = limit.toString().length();
		String s, p;
		
		if (isPowerOfTen(limit))
			Length = randomInt(MaxLength - 1) + 1;
		else
			Length = randomInt(MaxLength) + 1;
		
		if (Length == MaxLength){
			p = limit.toString();
			while (true){
				i = randomInt(Length);
				if ((i == 0) && (p.charAt(i) == '1')) continue;
				if (p.charAt(i) == '0') continue;
				break;
			}
			s = "";
			for (j = 0; j < i; j++) s += p.charAt(j);
			while (true){
				j = randomInt((int)(p.charAt(i)) - (int)('0'));
				if ((i == 0) && (j == 0)) continue;
				s += (char)('0' + j);
				break;
			}
			for (j = i + 1; j < Length; j++)
				s += (char)('0' + randomInt(10));
			return new BigInteger(s);
		}
		
		return RandomBigInteger(Length);
	}
	
	// +-------------------------------------------------------+
	// | Compute fast exponential of BigInteger a^N in O(logN) |
	// +-------------------------------------------------------+
	
	public static BigInteger FastExponential(BigInteger base, BigInteger exponent, BigInteger module){
		if (exponent.equals(ZERO)) return ONE;
		BigInteger res = ONE;
		base = base.mod(module);
		while (exponent.compareTo(ZERO) > 0){
			if (exponent.mod(TWO).equals(ONE)){
				res = res.multiply(base);
				res = res.mod(module);
			}
			base = base.multiply(base);
			base = base.mod(module);
			exponent = exponent.divide(TWO);
		} 
		return res;
	}
	
	// +------------------------+
	// | Fermat Test for Primes |
	// +------------------------+
	
	public static boolean FermatTest(BigInteger number){
		if (number.compareTo(ONE) <= 0) return false;
		if (number.equals(TWO)) return true;
		if (number.mod(TWO).equals(ZERO)) return false;
		BigInteger exponent = number.subtract(ONE);
		BigInteger base, remainder;
		for (int i = 0; i < nSteps; i++){
			base = RandomBigInteger(number);
			remainder = FastExponential(base, exponent, number);
			if (remainder.compareTo(ONE) != 0) return false;
		}
		return true;
	}
	
	// +------------------------------+
	// | Rabin Miller Test for Primes |
	// +------------------------------+
	
	public static boolean RabinMillerTest(BigInteger n){
		if (n.compareTo(ONE) <= 0) return false;
		if (n.equals(TWO)) return true;
		if (n.mod(TWO).equals(ZERO)) return false;
		
		BigInteger base, remainder;
		BigInteger d = n.subtract(ONE);
		int k = 0;
		boolean check;
		
		while (true)
			if (d.mod(TWO).equals(ZERO)){
				k++; 
				d = d.divide(TWO);
			}else break;
		
		for (int i = 0; i < nSteps; i++){
			base = RandomBigInteger(n);
			remainder = FastExponential(base, d, n);
			if (remainder.equals(ONE)) continue;
			check = false;
			for (int j = 0; j < k; j++){
				if (remainder.add(ONE).mod(n).compareTo(ZERO) == 0){
					check = true;
					break;
				}
				if (j < k - 1)
					remainder = remainder.multiply(remainder);
			}
			if (!check) return false;
		}
		
		return true;
	}
	
	// +---------------------------------------+
	// | Specific Rabin Miller Test for Primes |
	// +---------------------------------------+
	
	public static boolean SpecificRabinMillerTest(BigInteger n){
		if (n.compareTo(LimitSRMT) >= 0) return false;
		
		if (n.compareTo(ONE) <= 0) return false;
		if (n.equals(TWO)) return true;
		if (n.mod(TWO).equals(ZERO)) return false;
		
		BigInteger base, remainder;
		BigInteger d = n.subtract(ONE);
		int k = 0;
		boolean check;
		
		while (true)
			if (d.mod(TWO).equals(ZERO)){
				k++; 
				d = d.divide(TWO);
			}else break;
		
		BigInteger base3 = new BigInteger("3");
		BigInteger base5 = new BigInteger("5");
		BigInteger base7 = new BigInteger("7");
		BigInteger base11 = new BigInteger("11");
		BigInteger base13 = new BigInteger("13");
		BigInteger base17 = new BigInteger("17");
		
		for (int i = 0; i < 7; i++){
			base = TWO;
			if (i == 1) base = base3;
			if (i == 2) base = base5;
			if (i == 3) base = base7;
			if (i == 4) base = base11;
			if (i == 5) base = base13;
			if (i == 6) base = base17;
			
			if (base.equals(n)) continue;
			
			remainder = FastExponential(base, d, n);
			if (remainder.equals(ONE)) continue;
			check = false;
			for (int j = 0; j < k; j++){
				if (remainder.add(ONE).mod(n).compareTo(ZERO) == 0){
					check = true;
					break;
				}
				if (j < k - 1)
					remainder = remainder.multiply(remainder);
			}
			if (!check) return false;
		}
		
		return true;
	}
	
	public static boolean EnsureTest(BigInteger number){
		int i;
		for (i = 0; i < nSmallPrimes; i++)
			if (number.mod(new BigInteger(Integer.toString(SmallPrime[i]))).equals(ZERO))
				return false;
		return true;
	}
	
	public static boolean CheckOtherChosenPrimes(int i, BigInteger number){
		int j;
		for (j = 0; j < i; j++)
			if (ChosenPrime[j].equals(number)) return false;
		return true;
	}
	
	// +-----------------------+
	// | Big Primes Generation |
	// +-----------------------+
	
	public static void GenerateBigPrimes(){
		BigInteger number;
		boolean found, isPrime;
		
		for (int i = 0; i < nChosenPrimes; i++){
			found = false;
			while (true){
				number = RandomBigInteger(nDigits);
				if (number.mod(TWO).equals(ZERO)) number = number.add(ONE);
				while (true){
					if (number.toString().length() > nDigits) break;
					
					isPrime = false;
					if (number.compareTo(LimitSRMT) < 0)
						isPrime = SpecificRabinMillerTest(number);
					else
						if (RabinMillerTest(number))
							if (EnsureTest(number))
								isPrime = true;
								
					if (isPrime)
						if (CheckOtherChosenPrimes(i, number)){
							ChosenPrime[i] = number;
							found = true;
							break;
						}
					
					number = number.add(TWO);
				}
				if (found) break;
			}
		}
	}
	
	// +-------------------------+
	// | Small Primes Generation |
	// +-------------------------+
	
	public static void InitSmallPrimes(){
		int i, j;
		boolean check;
		SmallPrime[0] = 2;
		nSmallPrimes = 1;
		for (i = 3; i <= LimitOfSmallPrimes; i++){
			check = true;
			for (j = 0; j < nSmallPrimes; j++){
				if (i % SmallPrime[j] == 0){
					check = false;
					break;
				}
				if (SmallPrime[j] > (int)(Math.sqrt(i))) break;
			}
			if (check){
				SmallPrime[nSmallPrimes] = i;
				nSmallPrimes++;
			}
		}
	}
	
	// +------------------------------+
	// | Extended Euclidean Algorithm |
	// +------------------------------+
	
	public static BigInteger ExtendedEuclideanAlgorithm(BigInteger A, BigInteger B){
		BigInteger x1, x2, x3;
		BigInteger y1, y2, y3;
		BigInteger r1, r2, r3, q;
		
		x1 = ONE;
		y1 = ZERO;
		
		x2 = ZERO;
		y2 = ONE;
		
		r1 = A;
		r2 = B;
		
		while (true){
			q = r1.divide(r2);
			r3 = r1.subtract(q.multiply(r2));
			if (r3.equals(ZERO)) break;
			
			x3 = x1.subtract(q.multiply(x2));
			y3 = y1.subtract(q.multiply(y2));
			
			r1 = r2;
			x1 = x2;
			y1 = y2;
			
			r2 = r3;
			x2 = x3;
			y2 = y3;
		}
		
		if (x2.compareTo(ZERO) >= 0) return x2;
		
		q = x2.abs().divide(B).add(ONE);
		x2 = x2.add(q.multiply(B));
		return x2;
	}
	
	// +---------------------+
	// | Weak Key Generation |
	// +---------------------+
	
	public static boolean GenerateWeakKey(){
		int i, j;
		BigInteger temp;
		
		for (i = 0; i < nChosenPrimes; i++)
			for (j = i + 1; j < nChosenPrimes; j++)
				if (ChosenPrime[i].compareTo(ChosenPrime[j]) > 0){
					temp = ChosenPrime[i];
					ChosenPrime[i] = ChosenPrime[j];
					ChosenPrime[j] = temp;
				}
		
		n = ONE;
		e = ONE;
		phi = ONE;
		for (i = 0; i < nChosenPrimes; i++)
			if (i % 2 == 0) e = e.multiply(ChosenPrime[i]); else{
				n = n.multiply(ChosenPrime[i]);
				phi = phi.multiply(ChosenPrime[i].subtract(ONE));
			}
			
		if (e.gcd(n).compareTo(ONE) != 0) return false;
		
		d = ExtendedEuclideanAlgorithm(e, phi);
		return true;
	}
	
	// +-----------------------+
	// | Strong Key Generation |
	// +-----------------------+
	
	public static boolean GenerateStrongKey(){
		int i, j;
		BigInteger temp;
		boolean found;
		
		for (i = 0; i < 4; i++){
			temp = RandomBigInteger(nDigits * nChosenPrimes / 4);
			ChosenPrime[i] = temp.nextProbablePrime();
		}
		
		for (i = 0; i < 4; i++)
			for (j = i + 1; j < 4; j++)
				if (ChosenPrime[i].compareTo(ChosenPrime[j]) > 0){
					temp = ChosenPrime[i];
					ChosenPrime[i] = ChosenPrime[j];
					ChosenPrime[j] = temp;
				}
		
		e = ChosenPrime[0].multiply(ChosenPrime[2]); 
		n = ChosenPrime[1].multiply(ChosenPrime[3]);
		phi = ChosenPrime[1].subtract(ONE).multiply(ChosenPrime[3].subtract(ONE));
			
		if (e.gcd(n).compareTo(ONE) != 0) return false;
		
		d = ExtendedEuclideanAlgorithm(e, phi);
		return true;
	}
	
	public static void KeySaving(String PublicInfo, String PrivateInfo) throws IOException {
		System.out.println("[Saving public key to " + PublicInfo + "]");
		
		FileWriter file = new FileWriter(PublicInfo);
		PrintWriter writer = new PrintWriter(file);
		writer.println(n.toString());
		writer.println(e.toString());
		writer.close();
		file.close();
		
		System.out.println("[Saving private key to " + PrivateInfo + "]");
		
		file = new FileWriter(PrivateInfo);
		writer = new PrintWriter(file);
		writer.println(n.toString());
		writer.println(d.toString());
		writer.close();
		file.close();
	}
	
	public static void GetPublicKey(String PublicInfo) throws IOException {
		String s;
		BufferedReader file = new BufferedReader(new FileReader(PublicInfo));
		n = new BigInteger(file.readLine());
		e = new BigInteger(file.readLine());
		file.close();
	}
	
	// +------------------------+
	// | Binary File Encryption |
	// +------------------------+
	
	public static void Encrypt(String InputName, String OutputName) throws IOException {
		int nBytes1, nBytes2, aByte;
		BigInteger m, c;
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(InputName));
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(OutputName));
		
		Power256[0] = ONE;
		for (int i = 1; i <= 2 * ByteArrayLength; i++)
			Power256[i] = Power256[i - 1].multiply(ByteValue).mod(n);
		
		while (true){
			m = ZERO;
			nBytes1 = 0;
			while (input.available() > 0){
				aByte = input.read();
				m = m.add(Power256[nBytes1].multiply(new BigInteger(Integer.toString(aByte))));
				
				nBytes1++;
				if (nBytes1 == ByteArrayLength) break;
			}
			if (nBytes1 == 0) break;
			
			c = FastExponential(m, e, n);
			nBytes2 = 0;
			while (c.compareTo(ZERO) > 0){
				ByteArray[nBytes2] = Integer.parseInt(c.mod(ByteValue).toString());
				nBytes2++;
				c = c.divide(ByteValue);
			}
			
			output.write(nBytes1);
			output.write(0);
			output.write(nBytes2);
			output.write(0);
			
			for (int i = 0; i < nBytes2; i++)
				output.write(ByteArray[i]);
		}
		
		input.close();
		output.close();
	}
	
	public static void GetPrivateKey(String PrivateInfo) throws IOException {
		String s;
		BufferedReader file = new BufferedReader(new FileReader(PrivateInfo));
		n = new BigInteger(file.readLine());
		d = new BigInteger(file.readLine());
		file.close();
	}
	
	// +------------------------+
	// | Binary File Decryption |
	// +------------------------+
	
	public static void Decrypt(String InputName, String OutputName) throws IOException {
		int nBytes1, nBytes2, aByte;
		BigInteger m, c;
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(InputName));
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(OutputName));
		
		Power256[0] = ONE;
		for (int i = 1; i <= 2 * ByteArrayLength; i++)
			Power256[i] = Power256[i - 1].multiply(ByteValue).mod(n);
		
		while (input.available() > 0){
			nBytes1 = input.read();
			input.read();
			nBytes2 = input.read();
			input.read();
			
			c = ZERO;
			for (int i = 0; i < nBytes2; i++){
				aByte = input.read();
				c = c.add(Power256[i].multiply(new BigInteger(Integer.toString(aByte))));
			}
			
			m = FastExponential(c, d, n);
			for (int i = 0; i < nBytes1; i++){
				output.write(Integer.parseInt(m.mod(ByteValue).toString()));
				m = m.divide(ByteValue);
			}
		}
		
		input.close();
		output.close();
	}
	
	public static String getType(String FileName){
		int i, j;
		String res;
		j = -1;
		for (i = 0; i < FileName.length(); i++)
			if (FileName.charAt(i) == '.'){
				j = i;
				break;
			}
		res = "";
		if (j == -1) return res;
		for (i = j + 1; i < FileName.length(); i++) res += FileName.charAt(i);
		return res;
	}
	
	// +--------------+
	// | Main Program |
	// +--------------+
	
	public static void main(String args[]) throws IOException {
		if (args.length == 0) return;
		
		if (args[0].equals("-ft")){
			if (args.length < 2) return;
			
			for (int i = 1; i < args.length; i++)
				System.out.println(args[i] + " " + FermatTest(new BigInteger(args[i])));
			return;
		}
		
		if (args[0].equals("-rmt")){
			if (args.length < 2) return;
			
			for (int i = 1; i < args.length; i++)
				System.out.println(args[i] + " " + RabinMillerTest(new BigInteger(args[i])));
			return;
		}
		
		if (args[0].equals("-srmt")){
			if (args.length < 2) return;
			boolean res = false;
			for (int i = 1; i < args.length; i++){
				BigInteger number = new BigInteger(args[i]);
				if (number.compareTo(LimitSRMT) < 0)
					res = SpecificRabinMillerTest(number);
				else
					res = RabinMillerTest(number);
				System.out.println(args[i] + " " + res);
			}
			return;
		}
		
		if ((args[0].equals("-wg")) || (args[0].equals("-sg"))){
			String PublicInfo = "Public.key", PrivateInfo = "Private.key";
			if (args.length > 1) PublicInfo = args[1];
			if (args.length > 2) PrivateInfo = args[2];
			
			if (args[0].equals("-wg")){
				if (LimitSRMT.toString().length() <= nDigits)
					InitSmallPrimes();
				
				GenerateBigPrimes();
				
				if (!GenerateWeakKey()){
					System.out.println("[Fail to generate a key]");
					return;
				}
			}else
				if (!GenerateStrongKey()){
					System.out.println("[Fail to generate a key]");
					return;
				}
			
			KeySaving(PublicInfo, PrivateInfo);
			System.out.println("[Key generating is successful]");
			return;
		}
		
		if (args[0].equals("-e")){
			if (args.length < 3) return;
			GetPublicKey(args[2]);
			System.out.println("[Public key is loaded]");
			
			String FileName, type = getType(args[1]);
			if (args.length == 3){
				FileName = "EncryptedFile";
				if (type.length() > 0) FileName += "." + type;
			}else
				FileName = args[3];
			
			Encrypt(args[1], FileName);
			System.out.println("[Successfully encrypted " + args[1] + " to " + FileName + "]");
			return;
		}
		
		if (args[0].equals("-d")){
			if (args.length < 3) return;
			GetPrivateKey(args[2]);
			System.out.println("[Private key is loaded]");
			
			String FileName, type = getType(args[1]);
			if (args.length == 3){
				FileName = "DecryptedFile";
				if (type.length() > 0) FileName += "." + type;
			}else
				FileName = args[3];
			
			Decrypt(args[1], FileName);
			System.out.println("[Successfully decrypted " + args[1] + " to " + FileName + "]");
			return;
		}
	}	
	
}
