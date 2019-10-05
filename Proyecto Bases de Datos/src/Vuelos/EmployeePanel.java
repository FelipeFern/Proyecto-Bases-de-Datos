package Vuelos;

import java.awt.BorderLayout;
import quick.dbtable.DBTable;

@SuppressWarnings("serial")
public class EmployeePanel extends MainPanel {
	private DBTable table;

	/**
	 * Create the panel.
	 */
	public EmployeePanel(DBTable table) {
		this.table = table;
		initGUI();
	}
	
	private void initGUI() {
		
		this.add(table, BorderLayout.CENTER);
	}

	public DBTable getDBTable() {
		return table;
	}

}
