#include <iostream>
#include <string>
#include "InfoRepository.h"
using namespace std;
int main(){
	InfoRepository<int, string, double> infoRepository("Company");
	infoRepository.addInfo(3010731, "Bowen, David", 60000.00);
	infoRepository.addInfo(456901, "Donnelly, Tom", 89000.00);
	infoRepository.addInfo(783421, "Mason, James", 76000.00);
	infoRepository.addInfo(564313, "White, Anne", 89000.00);
	infoRepository.addInfo(345671, "Austen, Jane", 90000.00);
	infoRepository.printInformation();
	int id = 3010731;
	infoRepository.retrieveInfoNode(id);
	InfoRepository<string, string, double> infoRepositoryOne("BookStore");
	infoRepositoryOne.addInfo("0-201-70353-X", "C++ Programming Language", 20.00);
	infoRepositoryOne.addInfo("0-201-82470-1", "The Indispensable Guide to C", 25.00);
	infoRepositoryOne.addInfo("0-201-88954-4", "C++ Primer", 40.00);
	infoRepositoryOne.addInfo("0-399-82477-1", "The C Programming Language", 30.00);
	infoRepositoryOne.printInformation();
	string isbn("0-201-70353-X");
	infoRepositoryOne.retrieveInfoNode(isbn);
	InfoRepository<string, string, string> infoRepositoryTwo("Census Data");
	infoRepositoryTwo.addInfo("703-45-789", "Adams, Jane", "12-09-1971");
	infoRepositoryTwo.addInfo("659-43-259", "Collins, Joan", "01-12-1960");
	infoRepositoryTwo.addInfo("201-84-4464", "Foster, John", "02-12-1975");
	infoRepositoryTwo.printInformation();
	string ssn("659-40-259");
	infoRepositoryTwo.retrieveInfoNode(ssn);
	return 0;
}