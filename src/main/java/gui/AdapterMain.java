package gui;

import businessLogic.BLFacade;
import businessLogic.BLFactoryImplementation;
import domain.Driver;

public class AdapterMain {
	public static void main(String[]	args) throws Exception	{
	//		the	BL	is	local
		boolean isLocal =	true;
		BLFacade	blFacade =	new BLFactoryImplementation().createBL(true);
		Driver	d= blFacade.getDriver("Urtzi");
		DriverTable	dt=new	DriverTable(d);
		dt.setVisible(true);
	}
}
