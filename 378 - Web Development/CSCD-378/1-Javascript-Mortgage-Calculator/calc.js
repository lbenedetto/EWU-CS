function calculateMortgage() {
	var principle = parseFloat(document.getElementById("principle").value);
	var interest = parseFloat(document.getElementById("interest").value);
	var payment = parseFloat(document.getElementById("payment").value);
	var table = document.getElementById("outputTable");

	while (table.rows.length > 1) {
		table.deleteRow(1);
	}

	var balance = principle;
	var i = 1;
	//While principal > 0
	while (principle > 0) {
		//compute monthly interest - principal * ((rate / 12) / 100)
		var monthlyInterest = principle * ((interest / 12) / 100);
		//if principal - monthly payment + interest less than zero (loan paid off - last line of table)
		if (principle - payment + monthlyInterest <= 0) {
			//monthly payment becomes principal + interest
			payment = principle + monthlyInterest;
			//ending balance becomes zero
			insertRows(table, i, principle, monthlyInterest, payment, 0);
			principle = 0;
		} else {
			balance = principle + monthlyInterest - payment;
			//print columns
			insertRows(table, i, principle, monthlyInterest, payment, balance);
			//update principle
			principle = balance;
		}
		i++;
	}
}

function insertRows(table, i, principle, monthlyInterest, payment, balance) {
	var row = table.insertRow(i);
	row.insertCell(0).innerHTML = "$" + principle.toFixed(2).toString();
	row.insertCell(1).innerHTML = "$" + monthlyInterest.toFixed(2).toString();
	row.insertCell(2).innerHTML = "$" + payment.toFixed(2).toString();
	row.insertCell(3).innerHTML = "$" + balance.toFixed(2).toString();
}