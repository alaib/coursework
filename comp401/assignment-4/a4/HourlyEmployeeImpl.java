package a4;

public class HourlyEmployeeImpl implements HourlyEmployee {
	String firstName;
	String lastName;
	String jobTitle;
	int id;
	double hourlyRate, hoursPerWeek;

	public HourlyEmployeeImpl(String first_name, String last_name, String job_title, 
            int id, double hourly_rate, double hours_per_week){
		firstName = first_name;
		lastName = last_name;
		jobTitle = job_title;
		this.id = id;
		hourlyRate = hourly_rate;
		hoursPerWeek = hours_per_week;
	}
	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public String getJobTitle() {
		return jobTitle;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public double getGrossYearlyIncome() {
		double grossIncome = hoursPerWeek * hourlyRate * 52;
		return grossIncome;
	}

	@Override
	public double getNetYearlyIncome() {
		double netIncome = this.getGrossYearlyIncome() - this.getTaxesWithheld();
		return netIncome;
	}

	@Override
	public double getTaxableIncome() {
		double grossIncome = this.getGrossYearlyIncome();
		double taxableIncome = (grossIncome - 25000) > 0 ? grossIncome - 25000 : 0;
		return taxableIncome;
	}

	@Override
	public double getTaxesWithheld() {
		double taxableIncome = this.getTaxableIncome();
		if(dCompare(taxableIncome, 0)){
			return 0;
		}
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

	@Override
	public double getHourlyRate() {
		return this.hourlyRate;
	}

	@Override
	public void setHourlyRate(double rate) {
		this.hourlyRate = rate;
	}

	@Override
	public double getHoursPerWeek() {
		return this.hoursPerWeek;
	}

	@Override
	public void setHoursPerWeek(double hours_per_week) {
		this.hoursPerWeek = hours_per_week;

	}
	
	boolean dCompare(double v1, double v2){
		if(Math.abs(v1-v2) < 0.001){
			return true;
		}
		return false;
	}
}
