package Vuelos;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import quick.dbtable.DBTable;

public class EmployeePanel extends JPanel {
	private DBTable table;

	/**
	 * Create the panel.
	 */
	public EmployeePanel() {
		initGUI();
	}
	
	private void initGUI() {
		
		table = new DBTable();
		this.add(table, BorderLayout.CENTER);
	}

}
