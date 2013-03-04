package a4;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class A4Tester {

	@Test
	public void testHourlyEmployeeMakingLessThan25K() {
		HourlyEmployee h = new HourlyEmployeeImpl("John", "Doe", "Mechanical Turk", 1111, 10.50, 20);
		
		assertEquals(h.getFirstName(), "John");
		assertEquals(h.getLastName(), "Doe");
		assertEquals(h.getJobTitle(), "Mechanical Turk");
		assertEquals(h.getID(), 1111);
		assertEquals(h.getHourlyRate(), 10.50, 0.005);
		assertEquals(h.getHoursPerWeek(), 20.0, 0.005);
		assertEquals(h.getGrossYearlyIncome(), 20*52*10.50, 0.005);
		assertEquals(h.getTaxableIncome(), 0.0, 0.005);
		assertEquals(h.getTaxesWithheld(), 0.0, 0.005);
		assertEquals(h.getNetYearlyIncome(), h.getGrossYearlyIncome()-h.getTaxesWithheld(), 0.005);
	}

	@Test
	public void testHourlyEmployeeMakingBetween25Kand75K() {
		HourlyEmployee h = new HourlyEmployeeImpl("John", "Doe", "Mechanical Turk", 1111, 25.25, 40);
		
		assertEquals(h.getFirstName(), "John");
		assertEquals(h.getLastName(), "Doe");
		assertEquals(h.getJobTitle(), "Mechanical Turk");
		assertEquals(h.getID(), 1111);
		assertEquals(h.getHourlyRate(), 25.25, 0.005);
		assertEquals(h.getHoursPerWeek(), 40.0, 0.005);
		assertEquals(h.getGrossYearlyIncome(), 40*52*25.25, 0.005);
		assertEquals(h.getTaxableIncome(), 27520.0, 0.005);
		assertEquals(h.getTaxesWithheld(), 2752.0, 0.005);
		assertEquals(h.getNetYearlyIncome(), h.getGrossYearlyIncome()-h.getTaxesWithheld(), 0.005);
	}

	@Test
	public void testHourlyEmployeeMakingBetween75Kand125K() {
		HourlyEmployee h = new HourlyEmployeeImpl("John", "Doe", "Mechanical Turk", 1111, 50.50, 40);
		
		assertEquals(h.getFirstName(), "John");
		assertEquals(h.getLastName(), "Doe");
		assertEquals(h.getJobTitle(), "Mechanical Turk");
		assertEquals(h.getID(), 1111);
		assertEquals(h.getHourlyRate(), 50.50, 0.005);
		assertEquals(h.getHoursPerWeek(), 40.0, 0.005);
		assertEquals(h.getGrossYearlyIncome(), 40*52*50.50, 0.005);
		assertEquals(h.getTaxableIncome(), 80040.0, 0.005);
		assertEquals(h.getTaxesWithheld(), 9506.0, 0.005);
		assertEquals(h.getNetYearlyIncome(), h.getGrossYearlyIncome()-h.getTaxesWithheld(), 0.005);
	}

	@Test
	public void testHourlyEmployeeMakingOver125K() {
		HourlyEmployee h = new HourlyEmployeeImpl("John", "Doe", "Mechanical Turk", 1111, 75.75, 40);
		
		assertEquals(h.getFirstName(), "John");
		assertEquals(h.getLastName(), "Doe");
		assertEquals(h.getJobTitle(), "Mechanical Turk");
		assertEquals(h.getID(), 1111);
		assertEquals(h.getHourlyRate(), 75.75, 0.005);
		assertEquals(h.getHoursPerWeek(), 40.0, 0.005);
		assertEquals(h.getGrossYearlyIncome(), 40*52*75.75, 0.005);
		assertEquals(h.getTaxableIncome(), 132560.0, 0.005);
		assertEquals(h.getTaxesWithheld(), 20640.0, 0.005);
		assertEquals(h.getNetYearlyIncome(), h.getGrossYearlyIncome()-h.getTaxesWithheld(), 0.005);
	}
	
	@Test
	public void testHourlyEmployeeSetters() {
		HourlyEmployee h = new HourlyEmployeeImpl("John", "Doe", "Mechanical Turk", 1111, 10.50, 20);

		assertEquals(h.getHourlyRate(), 10.50, 0.005);
		assertEquals(h.getHoursPerWeek(), 20, 0.005);
		assertEquals(h.getGrossYearlyIncome(), 20*52*10.50, 0.005);
		assertEquals(h.getTaxableIncome(), 0.0, 0.005);
		assertEquals(h.getTaxesWithheld(), 0.0, 0.005);

		h.setHourlyRate(50.50);
		h.setHoursPerWeek(40);
		
		assertEquals(h.getHourlyRate(), 50.50, 0.005);
		assertEquals(h.getHoursPerWeek(), 40.0, 0.005);
		assertEquals(h.getGrossYearlyIncome(), 40*52*50.50, 0.005);
		assertEquals(h.getTaxableIncome(), 80040.0, 0.005);
		assertEquals(h.getTaxesWithheld(), 9506.0, 0.005);

	}

	@Test
	public void testSalariedEmployeeMakingLessThan25K() {
		System.out.println("test<25k");
		SalariedEmployee h = new SalariedEmployeeImpl("John", "Doe", "Mechanical Turk", 1111, 2000);
		
		assertEquals(h.getFirstName(), "John");
		assertEquals(h.getLastName(), "Doe");
		assertEquals(h.getJobTitle(), "Mechanical Turk");
		assertEquals(h.getID(), 1111);
		assertEquals(h.getMonthlySalary(), 2000, 0.005);
		assertEquals(h.getGrossYearlyIncome(), 2000*12, 0.005);
		assertEquals(h.getTaxableIncome(), h.getGrossYearlyIncome(), 0.005);
		assertEquals(h.getTaxesWithheld(), 2400, 0.005);
		assertEquals(h.getNetYearlyIncome(), h.getGrossYearlyIncome()-h.getTaxesWithheld(), 0.005);
	}

	@Test
	public void testSalariedEmployeeMakingBetween50Kand100K() {
		SalariedEmployee h = new SalariedEmployeeImpl("John", "Doe", "Mechanical Turk", 1111, 6166.75);
		
		assertEquals(h.getFirstName(), "John");
		assertEquals(h.getLastName(), "Doe");
		assertEquals(h.getJobTitle(), "Mechanical Turk");
		assertEquals(h.getID(), 1111);
		assertEquals(h.getMonthlySalary(), 6166.75, 0.005);
		assertEquals(h.getGrossYearlyIncome(), 6166.75*12, 0.005);
		assertEquals(h.getTaxableIncome(), h.getGrossYearlyIncome(), 0.005);
		assertEquals(h.getTaxesWithheld(), 8600.15, 0.005);
		assertEquals(h.getNetYearlyIncome(), h.getGrossYearlyIncome()-h.getTaxesWithheld(), 0.005);
	}
	
	@Test
	public void testSalariedEmployeeMakingOver100K() {
		SalariedEmployee h = new SalariedEmployeeImpl("John", "Doe", "Mechanical Turk", 1111, 9166.75);
		
		assertEquals(h.getFirstName(), "John");
		assertEquals(h.getLastName(), "Doe");
		assertEquals(h.getJobTitle(), "Mechanical Turk");
		assertEquals(h.getID(), 1111);
		assertEquals(h.getMonthlySalary(), 9166.75, 0.005);
		assertEquals(h.getGrossYearlyIncome(), 9166.75*12, 0.005);
		assertEquals(h.getTaxableIncome(), h.getGrossYearlyIncome(), 0.005);
		assertEquals(h.getTaxesWithheld(), 15000.25, 0.005);
		assertEquals(h.getNetYearlyIncome(), h.getGrossYearlyIncome()-h.getTaxesWithheld(), 0.005);
	}
	
	@Test
	public void testSalariedEmployeeSetters() {
		SalariedEmployee h = new SalariedEmployeeImpl("John", "Doe", "Mechanical Turk", 1111, 6166.75);
		
		assertEquals(h.getMonthlySalary(), 6166.75, 0.005);
		assertEquals(h.getTaxesWithheld(), 8600.15, 0.005);
		
		h.setMonthlySalary(9166.75);

		assertEquals(h.getMonthlySalary(), 9166.75, 0.005);
		assertEquals(h.getTaxesWithheld(), 15000.25, 0.005);
	}
	
	@Test
	public void testAccountManager() {
		SalariedEmployee h = new AccountManager("John", "Doe", 1111);

		assertEquals(h.getFirstName(), "John");
		assertEquals(h.getLastName(), "Doe");
		assertEquals(h.getJobTitle(), "Account Manager");
		assertEquals(h.getID(), 1111);
		assertEquals(h.getMonthlySalary(), 7500, 0.005);
		assertEquals(h.getGrossYearlyIncome(), 7500*12+10000, 0.005);
		assertEquals(h.getTaxableIncome(), h.getGrossYearlyIncome(), 0.005);
		assertEquals(h.getTaxesWithheld(), 12500.00, 0.005);
		assertEquals(h.getNetYearlyIncome(), h.getGrossYearlyIncome()-h.getTaxesWithheld(), 0.005);
		
	}
	
	@Test
	public void testBaker() {
		HourlyEmployee h = new Baker("John", "Doe", 1111, 12.25, 25.0);
		
		assertEquals(h.getFirstName(), "John");
		assertEquals(h.getLastName(), "Doe");
		assertEquals(h.getJobTitle(), "Baker");
		assertEquals(h.getID(), 1111);
		assertEquals(h.getHourlyRate(), 12.25, 0.005);
		assertEquals(h.getHoursPerWeek(), 25.0, 0.005);
		assertEquals(h.getGrossYearlyIncome(), 25*52*12.25, 0.005);
		assertEquals(h.getTaxableIncome(), 0, 0.005);
		assertEquals(h.getTaxesWithheld(), 0, 0.005);
		assertEquals(h.getNetYearlyIncome(), h.getGrossYearlyIncome()-h.getTaxesWithheld(), 0.005);

		h.setHoursPerWeek(40.0);
		
		assertEquals(h.getHourlyRate(), 12.25, 0.005);
		assertEquals(h.getHoursPerWeek(), 40.0, 0.005);
		
		assertEquals(h.getGrossYearlyIncome(), 30*52*12.25 + 10*52*12.25*1.5, 0.005);
		assertEquals(h.getTaxableIncome(), 3665.0, 0.005);
		assertEquals(h.getTaxesWithheld(), 366.50, 0.005);
		assertEquals(h.getNetYearlyIncome(), h.getGrossYearlyIncome()-h.getTaxesWithheld(), 0.005);
		
	}
	

	@Test
	public void testButcher() {
		HourlyEmployee h = new Butcher("John", "Doe", 1111);
		
		assertEquals(h.getFirstName(), "John");
		assertEquals(h.getLastName(), "Doe");
		assertEquals(h.getJobTitle(), "Butcher");
		assertEquals(h.getID(), 1111);
		assertEquals(h.getHourlyRate(), 20, 0.005);
		assertEquals(h.getHoursPerWeek(), 40, 0.005);
		assertEquals(h.getGrossYearlyIncome(), 40*20*52, 0.005);
		assertEquals(h.getTaxableIncome(), 16600, 0.005);
		assertEquals(h.getTaxesWithheld(), 1660, 0.005);
		assertEquals(h.getNetYearlyIncome(), h.getGrossYearlyIncome()-h.getTaxesWithheld(), 0.005);
	}

	@Test
	public void testContractor() {
		SalariedEmployee h = new Contractor("John", "Doe", "Mechanical Turk", 1111, 9166.75);
		
		assertEquals(h.getFirstName(), "John");
		assertEquals(h.getLastName(), "Doe");
		assertEquals(h.getJobTitle(), "Mechanical Turk");
		assertEquals(h.getID(), 1111);
		assertEquals(h.getMonthlySalary(), 9166.75, 0.005);
		assertEquals(h.getGrossYearlyIncome(), 9166.75*12, 0.005);
		assertEquals(h.getTaxableIncome(), h.getGrossYearlyIncome(), 0.005);
		assertEquals(h.getTaxesWithheld(), 0, 0.005);
		assertEquals(h.getNetYearlyIncome(), h.getGrossYearlyIncome()-h.getTaxesWithheld(), 0.005);
	}
	
}
