package domain;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class DriverAdapter extends AbstractTableModel {
	List<Ride> rideList;
	private final String[] columnNames = {"NONDIK", "NORA", "DATA", "LEKUAK", "PREZIOA"};
	
	public DriverAdapter(Driver d) {
		this.rideList = d.getCreatedRides();
	}
	
	@Override
	public int getRowCount() {
		return rideList.size();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if(columnIndex==0) return this.rideList.get(rowIndex).getFrom();
		else if(columnIndex==1) return this.rideList.get(rowIndex).getTo();
		else if(columnIndex==2) return this.rideList.get(rowIndex).getDate();
		else if(columnIndex==3) return this.rideList.get(rowIndex).getnPlaces();
		else  return this.rideList.get(rowIndex).getPrice();
	}
	  @Override
	    public String getColumnName(int column) {
	        return columnNames[column];
	    }
}
