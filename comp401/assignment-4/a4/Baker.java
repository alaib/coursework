package a4;

public class Baker extends HourlyEmployeeImpl {

	public Baker(String first_name, String last_name, String job_title, int id,
			double hourly_rate, double hours_per_week) {
		super(first_name, last_name, "Baker", id, hourly_rate, hours_per_week);
	}
	
	@Override
	public double getGrossYearlyIncome() {
		double normalRate = this.hourlyRate;
		double normalHours = (this.hoursPerWeek >= 30) ? 30 : this.hoursPerWeek;
		double extraRate = 1.5 * this.hourlyRate;
		double extraHours = (this.hoursPerWeek > 30) ? (this.hoursPerWeek - 30) : 0;
		double grossIncome = (normalRate * normalHours + extraRate * extraHours) * 52;
		return grossIncome;
	}

}
