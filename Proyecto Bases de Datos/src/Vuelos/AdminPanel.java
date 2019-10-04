package Vuelos;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;

import quick.dbtable.DBTable;

public class AdminPanel extends JPanel {
	private DBTable table;

	/**
	 * Create the panel.
	 */
	public AdminPanel() {
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout(0, 0));
	
		table = new DBTable();
		this.add(table, BorderLayout.CENTER);
		
		
	}

}
