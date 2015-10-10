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

int Limit, nPrime;
bool isPrime[MaxN];

void Sieve_Of_Sundaram(){
	int i, j, BoundaryI, BoundaryJ;
	
	for (i = 3; i <= Limit; i++)
		if (i % 2 == 1) isPrime[i] = true; else
			isPrime[i] = false;
			
	isPrime[0] = false;
	isPrime[1] = false;
	isPrime[2] = true;
	
	BoundaryI = (int)(sqrt(Limit / 4));
	
	for (i = 1; i <= BoundaryI; i++){
		BoundaryJ = ((Limit - 1) / 2 - i) / (2 * i + 1);
		for (j = i; j <= BoundaryJ; j++)
			isPrime[2 * i + 2 * j + 4 * i * j + 1] = false;
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
	freopen("SieveOfSundaram.txt", "w", stdout);
	
	cin >> Limit;
	Sieve_Of_Sundaram();
}
