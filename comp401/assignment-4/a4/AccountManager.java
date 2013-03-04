package a4;

public class AccountManager extends SalariedEmployeeImpl {

	public AccountManager(String first_name, String last_name, int id) {
		super(first_name, last_name, "Account Manager", id, 7500);
	}

	@Override
	public double getGrossYearlyIncome() {
		//Add 10,000 bonus as part of gross yearly salary
		double grossIncome = this.monthlySalary * 12 + 10000;
		return grossIncome;
	}
}
