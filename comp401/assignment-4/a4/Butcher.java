package a4;

public class Butcher extends HourlyEmployeeImpl {

	public Butcher(String first_name, String last_name, int id) {
		super(first_name, last_name, "Butcher", id, 20, 40);
	}
	
	@Override
	public void setHourlyRate(double rate) {
		//throw exception
		throw new RuntimeException("Cannot change setHourlyRate for Butcher class");
	}
	
	@Override
	public void setHoursPerWeek(double hours_per_week) {
		//throw exception
		throw new RuntimeException("Cannot change setHoursPerWeek for Butcher class");
	}

}
