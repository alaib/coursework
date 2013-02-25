package a4;

public abstract class EmployeeImpl implements Employee {
	String firstName;
	String lastName;
	String jobTitle;
	int id;
	double monthlySalary;
	double hourlyRate, hoursPerWeek;
	
	public EmployeeImpl(String fName, String lName, String jTitle, int ID, double hourlyRate, double hoursPerWeek){
		firstName = new String(fName);
		lName = new String(lastName);
		jobTitle = new String(jTitle);
		id = ID;
		this.hourlyRate = hourlyRate;
		this.hoursPerWeek = hoursPerWeek;
		
		//not used
		this.monthlySalary = Double.MIN_VALUE;
	}
	
	public EmployeeImpl(String fName, String lName, String jTitle, int ID, double monthlySalary){
		firstName = new String(fName);
		lName = new String(lastName);
		jobTitle = new String(jTitle);
		id = ID;
		this.monthlySalary = monthlySalary;
		
		//not used
		this.hourlyRate = Double.MIN_VALUE;
		this.hoursPerWeek = Double.MIN_VALUE;
	}
	
	public EmployeeImpl(String fName, String lName, String jTitle, int ID){
		firstName = new String(fName);
		lName = new String(lastName);
		jobTitle = new String(jTitle);
		id = ID;
		
		//not used
		this.hourlyRate = Double.MIN_VALUE;
		this.hoursPerWeek = Double.MIN_VALUE;
		this.monthlySalary = Double.MIN_VALUE;
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getNetYearlyIncome() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTaxableIncome() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTaxesWithheld() {
		// TODO Auto-generated method stub
		return 0;
	}
}
