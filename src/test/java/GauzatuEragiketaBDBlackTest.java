import static org.junit.Assert.*;

import java.lang.System.Logger;

import org.junit.Test;

import dataAccess.DataAccess;
import domain.User;
import testOperations.TestDataAccess;

public class GauzatuEragiketaBDBlackTest {
	//sut:system under test
		 static DataAccess sut=new DataAccess();
		 
	//additional operations needed to execute the test 
		 static TestDataAccess testDA=new TestDataAccess();
	
	@Test
	//sut.gauzatuEragiketa: The user("TestU") exists in DB and the amount of money is higher than 0.
	//deposit is true so the transaction should be succesful.
	public void test1() {
		String username="TestU";
		String pass="R34";
		double amount=11.0;
		boolean deposit=true;
		try {
		testDA.open();
			testDA.addDriver(username, pass);
		testDA.close();
		
		sut.open();
			boolean gauzatuDa=sut.gauzatuEragiketa(username, amount, deposit);
		sut.close();
		testDA.open();
		double diru=testDA.getActualMoney(username);
		testDA.close();
		assertTrue(gauzatuDa);
		assertEquals(diru,11,0.001);
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
	//sut.gauzatuEragiketa: The user("TestU") exists in DB and the amount of money is higher than 0 and the currentMoney is higher than the cash out money.
	//deposit is false so the transaction should be succesful.
	public void test2() {
		String username="TestU";
		String pass="R34";
		double amount=11.0;
		boolean deposit=false;
		try {
		testDA.open();
			testDA.addDriver(username, pass);
		testDA.close();
		
		sut.open();
			sut.gauzatuEragiketa(username,14.0,true);
			boolean gauzatuDa=sut.gauzatuEragiketa(username, amount, deposit);
		sut.close();
		testDA.open();
		double diru=testDA.getActualMoney(username);
		testDA.close();
		assertTrue(gauzatuDa);
		assertEquals(diru,3.0,0.001);
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
	//sut.gauzatuEragiketa: The user(null) does not exists in DB.
	//The transaction should be unsuccesful.
	public void test3() {
		String username=null;
		double amount=11.0;
		boolean deposit=false;
		try {
		sut.open();
			sut.gauzatuEragiketa(username,14.0,true);
			boolean gauzatuDa=sut.gauzatuEragiketa(username, amount, deposit);
		sut.close();
		assertFalse(gauzatuDa);
		}catch(Exception e) {
			fail();
			
		}
	}
	@Test
	//sut.gauzatuEragiketa: The user(TestU) does not exists in DB.
	//The transaction should be unsuccesful.
	public void test4() {
		String username="TestU";
		double amount=11.0;
		boolean deposit=false;
		try {
		sut.open();
			boolean gauzatuDa=sut.gauzatuEragiketa(username, amount, deposit);
		sut.close();
		assertFalse(gauzatuDa);
		}catch(Exception e) {
			fail();
			
		}
	}
	@Test
	//sut.gauzatuEragiketa: The user("TestU")exists in DB. However, the amount is negative.
	//The transaction should be unsuccesful.
	public void test5() {
		String username="TestU";
		String pass="R34";
		double amount=-11.0;
		boolean deposit=true;
		try {
			testDA.open();
				testDA.addDriver(username, pass);
			testDA.close();
			sut.open();
				boolean gauzatuDa=sut.gauzatuEragiketa(username, amount, deposit);
			sut.close();
			testDA.open();
				testDA.close();
			assertFalse(gauzatuDa);
		}catch(Exception e) {
				fail();
				
			}finally{
				testDA.open();
				testDA.deleteUser(testDA.getUser(username));
					
				testDA.close();
			}
	}
	@Test
	//sut.gauzatuEragiketa: The user("TestU") exists in DB and the amount of money is higher than 0 and the currentMoney is higher than the cash out money.
	//deposit is false so the transaction should be succesful.
	public void test6() {
		String username="TestU";
		String pass="R34";
		double amount=11.0;
		boolean deposit=false;
		try {
		testDA.open();
			testDA.addDriver(username, pass);
		testDA.close();
		
		sut.open();
			boolean gauzatuDa=sut.gauzatuEragiketa(username, amount, deposit);
		sut.close();
		testDA.open();
		double diru=testDA.getActualMoney(username);
		testDA.close();
		assertFalse(gauzatuDa);
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
