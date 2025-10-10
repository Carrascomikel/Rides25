import static org.junit.Assert.*;

import java.util.Collections;

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
import domain.Driver;
import domain.User;

public class GauzatuEragiketaMockBlackTest {
	static DataAccess sut;
	
	protected MockedStatic<Persistence> persistenceMock;

	@Mock
	protected  EntityManagerFactory entityManagerFactory;
	@Mock
	protected  EntityManager db;
	@Mock
    protected  EntityTransaction  et;
	@Mock
	protected TypedQuery<User> typedQueryUser;
	
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
	//sut.gauzatuEragiketa: The user("TestU") exists in DB and the amount of money is higher than 0.
	//deposit is true so the transaction should be succesful.
	public void test1() {
		String username="TestU";
		String password="pass";
		
		Driver driver=new Driver(username,password);
		
		 Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(User.class)))
         .thenReturn(typedQueryUser);

		 Mockito.when(typedQueryUser.setParameter(Mockito.anyString(), Mockito.any()))
         .thenReturn(typedQueryUser);

		 Mockito.when(typedQueryUser.getResultList())
         .thenReturn(Collections.singletonList(driver));
		
		 //invoke System Under Test
		    sut.open();
		    boolean burutuDa = sut.gauzatuEragiketa(username, 11, true);
		    try {
		        assertTrue(burutuDa);
		        assertEquals(driver.getMoney(),11,0.01);
		    } catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		        sut.close();
		    }
	}
	@Test
	//sut.gauzatuEragiketa: The user("TestU") exists in DB and the amount of money is higher than 0 and the currentMoney is higher than the cash out money.
	//deposit is false so the transaction should be succesful.
	public void test2() {
		String username="TestU";
		String password="pass";
		
		Driver driver=new Driver(username,password);
		
		 Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(User.class)))
         .thenReturn(typedQueryUser);

		 Mockito.when(typedQueryUser.setParameter(Mockito.anyString(), Mockito.any()))
         .thenReturn(typedQueryUser);

		 Mockito.when(typedQueryUser.getResultList())
         .thenReturn(Collections.singletonList(driver));
		
		 //invoke System Under Test
		    sut.open();
		    boolean burutuDa = sut.gauzatuEragiketa(username, 11,false);
		    try {
		        assertTrue(burutuDa);
		        assertEquals(driver.getMoney(),0,0.01);
		    } catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		        sut.close();
		    }
	}
	/*
	@Test
	//sut.gauzatuEragiketa: The user(null) does not exists in DB.
	//The transaction should be unsuccesful.
	public void test3() {
		String username=null;
		double amount=11.0;
		boolean deposit=false;
		
		
		try {
			 //invoke System Under Test
		    sut.open();
		    boolean burutuDa = sut.gauzatuEragiketa(username, 11,false);
		        assertFalse(burutuDa);
		 } catch (Exception e) {
		        e.printStackTrace();
		 } finally {
		        sut.close();
		  }
	}
	*/
	/*
	@Test
	//sut.gauzatuEragiketa: The user(TestU) does not exists in DB.
	//The transaction should be unsuccesful.
	public void test4() {
	    String username = "TestU";

	    Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(User.class)))
	           .thenReturn(typedQueryUser);

	    Mockito.when(typedQueryUser.setParameter(Mockito.anyString(), Mockito.any()))
	           .thenReturn(typedQueryUser);

	    Mockito.when(typedQueryUser.getResultList())
	           .thenReturn(Collections.singletonList(null));

	    //invoke System Under Test
	    sut.open();
	    boolean burutuDa = sut.gauzatuEragiketa(username, 11, true);
	    try {
	        assertFalse(burutuDa);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        sut.close();
	    }
	}
	*/
	@Test
	//sut.gauzatuEragiketa: The user("TestU")exists in DB. However, the amount is negative.
	//The transaction should be unsuccesful.
	public void test5() {
		String username="TestU";
		String password="pass";
		
		Driver driver=new Driver(username,password);
		
		 Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(User.class)))
         .thenReturn(typedQueryUser);

		 Mockito.when(typedQueryUser.setParameter(Mockito.anyString(), Mockito.any()))
         .thenReturn(typedQueryUser);

		 Mockito.when(typedQueryUser.getResultList())
         .thenReturn(Collections.singletonList(driver));
		
		 //invoke System Under Test
		    sut.open();
		    boolean burutuDa = sut.gauzatuEragiketa(username, -11, true);
		    try {
		        assertFalse(burutuDa);
		       
		    } catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		        sut.close();
		    }
	}
	@Test
	//sut.gauzatuEragiketa: The user("TestU") exists in DB and the amount of money is higher than 0 and the currentMoney is higher than the cash out money.
	//deposit is false so the transaction should be unsuccesful.
	public void test6() {
		String username="TestU";
		String password="pass";
		
		Driver driver=new Driver(username,password);
		
		 Mockito.when(db.createQuery(Mockito.anyString(), Mockito.eq(User.class)))
         .thenReturn(typedQueryUser);

		 Mockito.when(typedQueryUser.setParameter(Mockito.anyString(), Mockito.any()))
         .thenReturn(typedQueryUser);

		 Mockito.when(typedQueryUser.getResultList())
         .thenReturn(Collections.singletonList(driver));
		
		 //invoke System Under Test
		    sut.open();
		    boolean burutuDa = sut.gauzatuEragiketa(username, 11,false);
		    try {
		        assertFalse(burutuDa);
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		    } finally {
		        sut.close();
		    }
	}
		
	

}
