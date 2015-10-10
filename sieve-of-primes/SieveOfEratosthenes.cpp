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

void Sieve_Of_Eratosthenes(){
	int i, j;
	
	for (i = 2; i <= Limit; i++) isPrime[i] = true;
	isPrime[0] = false;
	isPrime[1] = false;
	
	nPrime = 0;
	for (i = 2; i <= Limit; i++)
		if (isPrime[i]){
			nPrime++;
			j = 2 * i;
			while (j <= Limit){
				isPrime[j] = false;
				j += i;
			}
		}
		
	cout << nPrime << endl;
	for (i = 1; i <= Limit; i++)
		if (isPrime[i]) cout << i << endl;
}

int main(){
	freopen("Limit.txt", "r", stdin);
	freopen("SieveOfEratosthenes.txt", "w", stdout);
	
	cin >> Limit;
	Sieve_Of_Eratosthenes();
}
