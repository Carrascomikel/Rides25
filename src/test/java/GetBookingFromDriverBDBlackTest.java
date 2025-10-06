import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import dataAccess.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;
import testOperations.TestDataAccess;

public class GetBookingFromDriverBDBlackTest {
	 //sut:system under test
	 static DataAccess sut=new DataAccess();
	 
	 //additional operations needed to execute the test 
	 static TestDataAccess testDA=new TestDataAccess();

	@SuppressWarnings("unused")
	private Driver driver; 
	   // 1. u ∉ DB, username = null  (6,4)
	@Test
	public void test1() {
		sut.open();
		List<Booking> result = sut.getBookingFromDriver(null);
		sut.close();
		assertTrue(result == null || result.isEmpty());
		}

	// 2. u ∉ DB, username = "Pablo" (4,7)
	@Test
	public void test2() {
		
		sut.open();
		List<Booking> result = sut.getBookingFromDriver("Pablo");
		sut.close();
		assertTrue(result == null || result.isEmpty());
	}

	// 3. u ∈ DB baina bidairik ez  (1,2,5)
	@Test
	public  void test3() {
		Driver d;
		List<Booking> erreserbak;
	try {	
		String driverUsername="Joritz";
		testDA.open();
		d=testDA.createDriver(driverUsername, "123");
		testDA.close();
		sut.open();
		erreserbak=sut.getBookingFromDriver(d.getUsername());
		sut.close();
		
		assertTrue(erreserbak.isEmpty());
		
		
	}catch(Exception e) {
		fail();
	}finally {
		try {
			testDA.open();
			testDA.removeDriver("Pablo");
			testDA.close();
		}catch(Exception e) {
			System.out.println("Ezinezkoa");
		}
	}
	}

	// 4. u ∈ DB, bidaiak ditu baina denak inaktiboak (1,2,5)
	@Test
	public void test4() {
		List<Booking> erreserbak=null;
		Ride r;
		try {
			String travelerUsername="Mikel";
			String driverUsername="Pablo";
			String rideFrom="Donostia";
			String rideTo="Zarautz";
			Date rideDate = new SimpleDateFormat("dd/MM/yyyy").parse("07/10/2025");
			try {
			testDA.open();
			 testDA.createDriver(driverUsername, "123");
			 r=testDA.createRide(rideFrom, rideTo, rideDate, 4, 5, driverUsername);
			 r.setActive(false);
			testDA.addTraveler(travelerUsername, "123");
			testDA.gauzatuEragiketa(travelerUsername, 15, true);
			testDA.bookRide(travelerUsername, r, 2, 0);
			}catch(Exception e) {
				   e.printStackTrace();
				    fail("Setup failed: " + e.getMessage());
			}
			testDA.close();
			sut.open();
			erreserbak=sut.getBookingFromDriver(driverUsername);
			sut.close();
			
			assertNotNull(erreserbak);
			assertTrue(erreserbak.isEmpty());

			
			
		}catch(Exception e) {
			fail();
		}
		finally {
			try {
				testDA.open();
				testDA.removeTraveler("Mikel");
				testDA.removeDriver("Pablo");
				testDA.close();
			
			}catch(Exception e){
				System.out.println("Ezinezkoa");
			}
		}
	}

	// 5. u ∈ DB, bidaiak ditu eta gutxienez aktibo bat (1,2,3)
	@Test
	public void test5() {
		List<Booking> erreserbak=null;
		Driver d;
		Ride r;
		try {
			String travelerUsername="Mikel";
			String driverUsername="Urko";
			String rideFrom="Zarautz";
			String rideTo="Donostia";
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date rideDate=null;
			try {
				rideDate = sdf.parse("07/10/2025");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			testDA.open();
			 d=testDA.createDriver(driverUsername, "123");
			 r=testDA.createRide(rideFrom, rideTo, rideDate, 4, 5, driverUsername);
			testDA.addTraveler(travelerUsername, "123");
			testDA.gauzatuEragiketa(travelerUsername, 15, true);
			testDA.bookRide(travelerUsername, r, 2, 0);
			testDA.close();
			sut.open();
			erreserbak=sut.getBookingFromDriver(driverUsername);
			sut.close();
			
			assertFalse(erreserbak.isEmpty());
			
			
		}catch(Exception e) {
			fail();
		}
		finally {
			try {
				testDA.open();
				testDA.removeTraveler("Mikel");
				testDA.removeDriver("Urko");
				testDA.close();
			
			}catch(Exception e){
				System.out.println("Ezinezkoa");
			}
		}
	}

}
