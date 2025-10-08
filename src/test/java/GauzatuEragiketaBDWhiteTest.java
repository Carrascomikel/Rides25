import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.booleanThat;

import org.junit.Test;

import dataAccess.DataAccess;
import domain.Driver;
import testOperations.TestDataAccess;

public class GauzatuEragiketaBDWhiteTest {
	
	 //sut:system under test
	 static DataAccess sut=new DataAccess();
	//additional operations needed to execute the test 
		 static TestDataAccess testDA=new TestDataAccess();

	//sut.gauzatuEragiketa: There is no User with that username, so User(Traveler or Driver) is null.
	//The test must return False.
	/*
	public void test1() {
		try {
		String username="Pepe";
		double amount=13.1;
		boolean deposit=true;
		//invoke System Under Test(sut)
		sut.open();
			boolean emaitza=sut.gauzatuEragiketa(username, amount, deposit);
		sut.close();
		assertFalse(emaitza);
		}catch(Exception e) {
			fail();
		}
	}
	*/
	@Test
	//sut.gauzatuEragiketa: The User exists on the DB and deposit is true,so the amount should be added to the currentMoney.
	//The test supposes that the Driver "Maria" does not exists in the DB before. The test must return True.
	public void test2() {
		String username="Maria";
		String pass="Dolores";
		double amount=40;
		boolean deposit=true;
		try {
			testDA.open();
			testDA.addDriver(username, pass);
		testDA.close();
		sut.open();
			boolean burutuDa=sut.gauzatuEragiketa(username, amount, deposit);
		sut.close();
		testDA.open();
		double diru=testDA.getActualMoney(username);
		testDA.close();
			
			assertTrue(burutuDa);
			assertEquals(diru,40,0.001);
		}catch(Exception e) {
			fail();
			}
		finally {   

			testDA.open();
				testDA.deleteUser(testDA.getUser(username));
			testDA.close();
			
		        }
	}
	@Test
	//sut.gauzatuEragiketa: The user exists on the DB but the amount of money we want to cash out is higher than the current money.
	//The test must return true.
	public void test3() {
		String username="Maria";
		String pass="Dolores";
		double amount=30;
		boolean deposit=false;
		
		try {
			testDA.open();
			testDA.addDriver(username, pass);
		testDA.close();
		
		sut.open();
			boolean burutuDa=sut.gauzatuEragiketa(username, amount, deposit);
		sut.close();
		testDA.open();
		double diru=testDA.getActualMoney(username);
		testDA.close();
			assertTrue(burutuDa);
			assertEquals(diru,0,0.001);
		}catch(Exception e) {
			fail();
		}
		finally {   

			testDA.open();
				testDA.deleteUser(testDA.getUser(username));
				
			testDA.close();
			
		        }
		
	}
	@Test
	//sut.gauzatuEragiketa: The user exists on the DB and the currentMoney is higher than the amount we want to cash out.
	//The test must return true.
	public void test4() {
		String username="Maria";
		String pass="Dolores";
		double amount1=30;
		double amount2=20;
		boolean deposit1=true;
		boolean deposit2=false;
		try {
			testDA.open();
			testDA.addDriver(username, pass);
		testDA.close();
		
		sut.open();
			sut.gauzatuEragiketa(username, amount1, deposit1);
			boolean burutuDa=sut.gauzatuEragiketa(username, amount2, deposit2);
		sut.close();
		testDA.open();
		double diru=testDA.getActualMoney(username);
		testDA.close();
			assertTrue(burutuDa);
			assertEquals(diru,10,0.001);
		}catch(Exception e) {
			fail();
		}
		finally {   

			testDA.open();
			testDA.deleteUser(testDA.getUser(username));
				
			testDA.close();
			
		        }
		
	}
}