package Vuelos;

import quick.dbtable.DBTable;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class AdminPanel extends MainPanel {
	private DBTable table;

	/**
	 * Create the panel.
	 */
	public AdminPanel(DBTable table) {
		this.table = table;
		initGUI();
	}

	private void initGUI() {
		setBounds(100,100,1024,600);
		setLayout(null);
		
		table.setBounds(12, 160, 1000, 384);
		this.add(table);
		table.setVisible(true);

		JLabel lblDescription = new JLabel("Ingrese la consulta");
		lblDescription.setBounds(10, 11, 150, 25);
		add(lblDescription);
		lblDescription.setVisible(true);

		JButton btnConsult = new JButton("Consultar");
		btnConsult.setBounds(882, 48, 130, 25);
		add(btnConsult);
		
		JTextArea textCommand = new JTextArea();
		textCommand.setBounds(12, 48, 858, 100);
		add(textCommand);
		btnConsult.setVisible(true);
	}
	
	public DBTable getDBTable() {
		return table;
	}
}
