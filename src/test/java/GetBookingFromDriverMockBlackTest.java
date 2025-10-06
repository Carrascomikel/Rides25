import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import domain.*;

public class GetBookingFromDriverMockBlackTest {

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
        Mockito.when(db.find(Driver.class, "Pablo")).thenReturn(null);
        sut.open();
        List<Booking> result = sut.getBookingFromDriver("Pablo");
        sut.close();
        assertTrue(result == null || result.isEmpty());
    }

    // 3. u ∈ DB baina bidairik ez  (1,2,5)
    @Test
   public  void test3() {
    	String driverUsername="Joritz";
		Driver driver = new Driver(driverUsername, "pass");
	    driver.setCreatedRides(new ArrayList<>());
	       
		Mockito.when(db.find(Driver.class, driver.getUsername())).thenReturn(driver);
        sut.open();
        List<Booking> result = sut.getBookingFromDriver(driverUsername);
        sut.close();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // 4. u ∈ DB, bidaiak ditu baina denak inaktiboak (1,2,5)
    @Test
    public void test4() {
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
        List<Booking> result = sut.getBookingFromDriver(driverUsername);
        sut.close();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // 5. u ∈ DB, bidaiak ditu eta gutxienez aktibo bat (1,2,3)
    @Test
   public void test5() {
    	String driverUsername="Joritz";
		  Driver driver = new Driver(driverUsername, "pass");
	        driver.setCreatedRides(new ArrayList<>());

	        Traveler t = new Traveler("Joritz", "4");
	        Ride r = new Ride("Donostia", "Zarautz", new Date(), 0, 0, driver);
	       
	        r.setBookings(new ArrayList<>());

	        Booking b = new Booking(r, t, 2);
	        r.getBookings().add(b);   
	        driver.getCreatedRides().add(r);
	       
		Mockito.when(db.find(Driver.class, driver.getUsername())).thenReturn(driver);
        
        sut.open();
        List<Booking> result = sut.getBookingFromDriver(driverUsername);
        sut.close();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

}
