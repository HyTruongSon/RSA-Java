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

#define MaxN 25

using namespace std;

int nPrime = 0;
long long Exp[2 * MaxN + 1];
long long Prime[2 * MaxN + 1];
int ind[2 * MaxN + 1];

int main(){
	long long Number;
	bool isPrime;
	
	Exp[0] = 1;
	for (int i = 1; i <= 2 * MaxN; i++)
		Exp[i] = Exp[i - 1] + Exp[i - 1];
	
	for (int i = 2; i <= 2 * MaxN; i++){
		Number = Exp[i] - 1;
		
		isPrime = true;
		for (int j = 2; j <= sqrt(Number); j++)
			if (Number % j == 0){
				isPrime = false;
				break;
			}
			
		if (isPrime){
			Prime[nPrime] = Number;
			ind[nPrime] = i;
			nPrime++;
		}
	}
	
	cout << "Number of Primes: " << nPrime << endl;
	for (int i = 0; i < nPrime; i++)
		cout << "2 ^ " << ind[i] << " - 1 = " << Prime[i] << endl;
}
