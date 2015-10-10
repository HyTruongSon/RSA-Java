#include <iostream>
#include <cstring>
#include <string>
#include <cstdio>
#include <cstdlib>
#include <cmath>
#include <vector>
#include <set>
#include <map>
#include <queue>
#include <iterator>
#include <algorithm>

using namespace std;

struct BigInt {
	int n;
	vector<int> digit;
};

void Write(BigInt A){
	int i;
	for (i = A.n - 1; i >= 0; i--)
		cout << A.digit[i];
	cout << endl;
}

BigInt ConvertToBigInt(int B){
	BigInt A;
	
	A.n = 0;
	A.digit.clear();
	
	while (B > 0){
		A.digit.push_back(B % 10);
		A.n++;
		B /= 10;
	}
	
	return A;
}

BigInt ConvertToBigInt(string B){
	BigInt A;
	
	A.n = 0;
	A.digit.clear();
	
	for (int i = B.length() - 1; i >= 0; i--){
		A.digit.push_back((int)(B[i]) - (int)('0'));
		A.n++;
	}
	
	return A;
}

bool Equal(BigInt A, BigInt B){
	if (A.n != B.n) return false;
	
	int i;
	for (i = A.n - 1; i >= 0; i--)
		if (A.digit[i] != B.digit[i]) return false;
		
	return true;
}

bool LargerOrEqual(BigInt A, BigInt B){
	if (A.n > B.n) return true;
	if (A.n < B.n) return false;
	
	int i;
	for (i = A.n - 1; i >= 0; i--){
		if (A.digit[i] > B.digit[i]) return true;
		if (A.digit[i] < B.digit[i]) return false;
	}
	
	return true;
}

BigInt Addition(BigInt A, BigInt B){
	int i, j, u, v, r;
	BigInt C;
	
	C.digit.clear();
	
	r = 0;
	for (i = 0; i < max(A.n, B.n); i++){
		if (i < A.n) u = A.digit[i]; else u = 0;
		if (i < B.n) v = B.digit[i]; else v = 0;
		j = u + v + r;
		C.digit.push_back(j % 10);
		r = j / 10;
	}
	
	C.n = max(A.n, B.n);
	if (r > 0){
		C.digit.push_back(r);
		C.n++;
	}
	
	return C;
}

BigInt Subtraction(BigInt A, BigInt B){
	int i, j, u, v, r;
	BigInt C;
	
	C.digit.clear();
	
	r = 0;
	for (i = 0; i < A.n; i++){
		u = A.digit[i];
		if (i < B.n) v = B.digit[i]; else v = 0;
		if (u < v + r){
			C.digit.push_back(10 + u - v - r);
			r = 1;
		}else{
			C.digit.push_back(u - v - r);
			r = 0;
		}
	}
	
	C.n = A.n;
	while ((C.n > 0) && (C.digit[C.n - 1] == 0)){
		C.digit.pop_back();
		C.n--;
	}
	
	return C;
}

BigInt Multiplication(BigInt A, int B){
	int i, j, r;
	BigInt C;
	
	C.digit.clear();
	
	r = 0;
	for (i = 0; i < A.n; i++){
		j = A.digit[i] * B + r;
		C.digit.push_back(j % 10);
		r = j / 10;
	}
	
	C.n = A.n;
	while (r > 0){
		C.digit.push_back(r % 10);
		C.n++;
		r /= 10;
	}
	
	while ((C.n > 0) && (C.digit[C.n - 1] == 0)){
		C.digit.pop_back();
		C.n--;
	}
	
	return C;
}

BigInt Multiplication(BigInt A, BigInt B){
	int i, j, v, r;
	BigInt C, D;
	
	C.n = 0;
	C.digit.clear();
	
	for (i = 0; i < A.n; i++){
		D.n = 0;
		D.digit.clear();
		for (j = 0; j < i; j++){
			D.digit.push_back(0);
			D.n++;
		}
		
		r = 0;
		for (j = 0; j < B.n; j++){
			v = A.digit[i] * B.digit[j] + r;
			D.digit.push_back(v % 10);
			D.n++;
			r = v / 10;
		}
		
		while (r > 0){
			D.digit.push_back(r % 10);
			D.n++;
			r /= 10;
		}
		
		C = Addition(C, D);
	}
	
	return C;
}

BigInt Division(BigInt A, BigInt B){
	int i, j;
	BigInt C, D, E;
	
	C.n = 0;
	C.digit.clear();
	D.n = 0;
	D.digit.clear();
	
	for (i = A.n - 1; i >= 0; i--){
		D = Multiplication(D, 10);
		D = Addition(D, ConvertToBigInt(A.digit[i]));
		
		if (D.n == 0){
			C.digit.push_back(0);
			C.n++;
		}else
			for (j = 9; j >= 0; j--){
				E = Multiplication(B, j);
			
				if (LargerOrEqual(D, E)){
					C.digit.push_back(j);
					C.n++;
					D = Subtraction(D, E);
					break;
				}
			}
	}
	
	for (i = 0; i < C.n / 2; i++)
		swap(C.digit[i], C.digit[C.n - i - 1]);
		
	while ((C.n > 0) && (C.digit[C.n - 1] == 0)){
		C.digit.pop_back();
		C.n--;
	}
	
	return C;
}

int main(){
	BigInt A = ConvertToBigInt("2305843009213693951");
	BigInt B = ConvertToBigInt("2147483647");
	
	BigInt N = Multiplication(A, B);
	
	BigInt C = Subtraction(A, ConvertToBigInt(1));
	BigInt D = Subtraction(B, ConvertToBigInt(1));
	
	BigInt Phi = Multiplication(C, D);
	
	cout << "N = ";
	Write(N);
	
	cout << "Phi(N) = ";
	Write(Phi);
}
