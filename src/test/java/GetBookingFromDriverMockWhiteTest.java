import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Booking;
import domain.Driver;
import domain.Ride;
import domain.Traveler;

public class GetBookingFromDriverMockWhiteTest {
static DataAccess sut;
	
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;
	
	@Mock
	TypedQuery<Double> typedQuery;

	@Before
    public  void init() {
        MockitoAnnotations.openMocks(this);
        persistenceMock = Mockito.mockStatic(Persistence.class);
		persistenceMock.when(() -> Persistence.createEntityManagerFactory(Mockito.any()))
        .thenReturn(entityManagerFactory);
        
        Mockito.doReturn(db).when(entityManagerFactory).createEntityManager();
		Mockito.doReturn(et).when(db).getTransaction();
	    sut=new DataAccess(db);
    }
	@After
    public  void tearDown() {
		persistenceMock.close();
    }
	@Test
	public void test1() {
	List<Booking> erreserbak=null;
		
		try {
			String driverUsername="Jon";
			Mockito.when(db.find(Driver.class, driverUsername)).thenReturn(null);
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
		
		try {
			String driverUsername="Pablo";
			Driver driver=new Driver(driverUsername,"2");
			Mockito.when(db.find(Driver.class, driver.getUsername())).thenReturn(driver);
			sut.open();
			erreserbak=sut.getBookingFromDriver(driverUsername);
			sut.close();
			
			assertTrue(erreserbak.isEmpty());
			
			
		}catch(Exception e) {
			fail();
		}
	}
	@Test
	public void test3() {
	List<Booking> erreserbak=null;
		
		try {
			String driverUsername="Joritz";
			  Driver driver = new Driver(driverUsername, "pass");
		        driver.setCreatedRides(new ArrayList<>());

		        Traveler t = new Traveler("Joritz", "4");
		        Ride r = new Ride("Donostia", "Zarautz", new Date(), 0, 0, driver);
		        r.setActive(false);
		        r.setBookings(new ArrayList<>());

		        Booking b = new Booking(r, t, 2);
		        r.getBookings().add(b);   
		        driver.getCreatedRides().add(r);
		       
			Mockito.when(db.find(Driver.class, driver.getUsername())).thenReturn(driver);
			
			sut.open();
			erreserbak=sut.getBookingFromDriver(driverUsername);
			sut.close();
			
			assertTrue(erreserbak.isEmpty());
			
			
		}catch(Exception e) {
			fail();
		}
	}
	  @Test
	    public void test4() throws Exception {
	        // Arrange
	        String driverUsername = "Markel";
	        Driver driver = new Driver(driverUsername, "pass");
	        driver.setCreatedRides(new ArrayList<>());

	        Traveler t = new Traveler("Joritz", "4");
	        Ride r = new Ride("Donostia", "Zarautz", new Date(), 0, 0, driver);
	        r.setBookings(new ArrayList<>());

	        Booking b = new Booking(r, t, 2);
	        r.getBookings().add(b);   
	        driver.getCreatedRides().add(r);


	        Mockito.when(db.find(Driver.class, driverUsername)).thenReturn(driver);

	        // Act
	        List<Booking> erreserbak = sut.getBookingFromDriver(driverUsername);

	        // Assert
	        assertNotNull(erreserbak);
	        assertEquals(1, erreserbak.size());
	        assertEquals("Joritz", erreserbak.get(0).getTraveler().getUsername());
	    }
}
