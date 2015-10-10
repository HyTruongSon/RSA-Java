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

#define MaxN 100000001

using namespace std;

int nPrime, Limit;
bool isPrime[MaxN];

void Sieve_Of_Atkin(){
	int i, j, x, y, n, Sqrt_Of_Limit;
	
	for (i = 0; i <= Limit; i++) isPrime[i] = false;
	isPrime[2] = true;
	isPrime[3] = true;
	
	Sqrt_Of_Limit = (int)(sqrt(Limit));
	for (x = 1; x <= Sqrt_Of_Limit; x++){
		for (y = 1; y <= Sqrt_Of_Limit; y++){
			n = 4 * x * x + y * y;
			if (n > Limit) break;
			if ((n % 12 == 1) || (n % 12 == 5))
				isPrime[n] = !isPrime[n];
		}
		
		for (y = 1; y <= Sqrt_Of_Limit; y++){
			n = 3 * x * x + y * y;
			if (n > Limit) break;
			if (n % 12 == 7)
				isPrime[n] = !isPrime[n];
		}
		
		for (y = 1; y < x; y++){
			n = 3 * x * x - y * y;
			if (n <= Limit)
				if (n % 12 == 11)
					isPrime[n] = !isPrime[n];
		}
	}
	
	for (i = 5; i <= Sqrt_Of_Limit; i++)
		if (isPrime[i]){
			j = i * i;
			while (j <= Limit){
				isPrime[j] = false;
				j += i * i;
			}
		}
		
	nPrime = 0;
	for (i = 1; i <= Limit; i++)
		if (isPrime[i]) nPrime++;
		
	cout << nPrime << endl;
	for (i = 1; i <= Limit; i++)
		if (isPrime[i]) cout << i << endl;
}

int main(){
	freopen("Limit.txt", "r", stdin);
	freopen("SieveOfAtkin.txt", "w", stdout);
	
	cin >> Limit;
	
	Sieve_Of_Atkin();
}
