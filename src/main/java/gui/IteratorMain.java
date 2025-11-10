package gui;

import java.util.Locale;

import javax.swing.UIManager;

import businessLogic.*;
import configuration.ConfigXML;
import domain.ExtendedIterator;


public class IteratorMain {

	public static void main(String[] args) {
		ConfigXML c=ConfigXML.getInstance();
		
		Locale.setDefault(new Locale(c.getLocale()));

		try {
			
			BLFacade appFacadeInterface;
			IBLFactory factory = new BLFactoryImplementation();

			appFacadeInterface = factory.createBL(c.isBusinessLogicLocal());
			appFacadeInterface=factory.createBL(c.isBusinessLogicLocal());
			
			ExtendedIterator<String> i = appFacadeInterface.getDepartingCitiesIterator();
			String city;
			System.out.println("______________________");
			System.out.println("FROM LAST TO FIRST");
			i.goLast();
			while(i.hasPrevious()) {
				city = i.previous();
				System.out.println(city);
			}
			System.out.println();
			System.out.println("______________________");
			System.out.println("FROM FIRST TO LAST");
			i.goFirst();
			while(i.hasNext()) {
				city = i.next();
				System.out.println(city);
			}
		
			
		}catch (Exception e) {	
			System.out.println("Error in ApplicationLauncher: "+e.toString());
		}


	}

}
