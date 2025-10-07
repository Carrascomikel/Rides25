import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import dataAccess.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import testOperations.TestDataAccess;

public class GetBookingFromDriverBDWhiteTest {
	 //sut:system under test
	 static DataAccess sut=new DataAccess();
	 
	 //additional operations needed to execute the test 
	 static TestDataAccess testDA=new TestDataAccess();

	@SuppressWarnings("unused")
	private Driver driver; 
	@Test
	public void test1() {
		List<Booking> erreserbak=null;
		
		try {
			String driverUsername="Jon";
			sut.open();
			erreserbak=sut.getBookingFromDriver(driverUsername);
			sut.close();
			
			assertNull(erreserbak);
			
			
		}catch(Exception e) {
			fail();
		}
	}
	@Test
	public void test2() {
		List<Booking> erreserbak=null;
		Ride r=null;
		Driver d=null;
		try {
			
			String driverUsername="Pablo";
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
	@Test
	public void test3() {
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
	@Test
	public void test4() {
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
