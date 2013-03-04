package a4;

public class SalariedEmployeeImpl extends EmployeeImpl implements SalariedEmployee {

	public SalariedEmployeeImpl(String first_name, String last_name, String job_title, int id, double monthly_salary) {
		super(first_name, last_name, job_title, id, monthly_salary);
	}
	
	@Override
	public double getMonthlySalary() {
		return this.monthlySalary;
	}

	@Override
	public void setMonthlySalary(double salary) {
		this.monthlySalary = salary;
	}
	
	@Override
	public double getGrossYearlyIncome() {
		double grossIncome = this.monthlySalary * 12;
		return grossIncome;
	}

	@Override
	public double getNetYearlyIncome() {
		double netIncome = this.getGrossYearlyIncome() - this.getTaxesWithheld();
		return netIncome;
	}

	@Override
	public double getTaxableIncome() {
		double taxableIncome = this.getGrossYearlyIncome();
		return taxableIncome;
	}

	@Override
	public double getTaxesWithheld() {
		double taxableIncome = this.getTaxableIncome();
		double taxesHeld = 0.0;
		
		// 10% on first 50,000
		taxesHeld += (taxableIncome <= 50000) ? 0.1 * taxableIncome : 0.1 * 50000;
		if(taxableIncome > 50000){
			taxableIncome -= 50000;
		}else{
			return taxesHeld;
		}
		
		// 15% on next 50,000
		taxesHeld += (taxableIncome <= 50000) ? 0.15 * taxableIncome : 0.15 * 50000;
		if(taxableIncome > 50000){
			taxableIncome -= 50000;
		}else{
			return taxesHeld;
		}
		
		// 25% on any income over 100,000
		taxesHeld += 0.25 * taxableIncome;
		return taxesHeld;
	}
}
