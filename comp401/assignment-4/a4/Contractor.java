package a4;

public class Contractor extends SalariedEmployeeImpl {

	public Contractor(String first_name, String last_name, String job_title, int id, double monthly_salary) {
		super(first_name, last_name, job_title, id, monthly_salary);
	}
	
	@Override
	public double getTaxesWithheld() {
		return 0.0;
	}

}
