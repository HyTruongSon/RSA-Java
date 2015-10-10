import java.io.*;
import java.lang.*;
import java.math.BigInteger;
import java.util.Random;

public class BinFile {
	
	public static void main(String args[]) throws IOException {
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(args[1]));
		int aByte;
		while (input.available() > 0){
			aByte = input.read();
			if (args[0].equals("-byte"))
				System.out.print(aByte + " ");
			if (args[0].equals("-bit"))
				for (int i = 0; i < 8; i++){
					System.out.print(aByte % 2);
					aByte /= 2;
				}
			if (args[0].equals("-char"))
				System.out.print((char)(aByte));
		}
		input.close();
	}	
	
}
