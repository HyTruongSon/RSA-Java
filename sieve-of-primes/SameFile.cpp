#include <iostream>
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
#include <fstream>

using namespace std;

string FileName1 = "SieveOfEratosthenes.txt";
string FileName2 = "SieveOfAtkin.txt";
string FileName3 = "SieveOfSundaram.txt";

int CountLine(string FileName){
	string s;
	int res = 0;
	
	ifstream file(FileName.c_str(), ios::in);
	
	while (getline(file, s)) res++;
	
	file.close();
	
	return res;
}

pair<bool, int> Check(string FileName1, string FileName2, int nLine){
	string s1, s2;
	
	ifstream file1(FileName1.c_str(), ios::in);
	ifstream file2(FileName2.c_str(), ios::in);
	
	for (int i = 1; i <= nLine; i++){
		getline(file1, s1);
		getline(file2, s2);
		if (s1 != s2) return make_pair(false, i);
	}
	
	file1.close();
	file2.close();
	
	return make_pair(true, 0);
}

bool Check(string FileOutput, string FileAnswer){
	bool same = true;
	
	cout << FileOutput << " - " << FileAnswer << endl;
	
	int nLine = CountLine(FileOutput);
	if (nLine != CountLine(FileAnswer)){
		same = false;
		cout << "Difference in the number of lines!" << endl;
	}
	
	if (same){
		pair<bool, int> res = Check(FileOutput, FileAnswer, nLine);
		same = res.first;
		if (!same){
			cout << "Difference in the content of these files!" << endl;
			cout << "Line " << res.second << endl;
		}
	}
	
	if (same) cout << "OK!" << endl;
	
	return same;
}

int main(){
	bool res = Check(FileName1, FileName2);
	
	if (res)
		res = Check(FileName2, FileName3);
		
	if (res)
		cout << "Accepted!" << endl;
}
