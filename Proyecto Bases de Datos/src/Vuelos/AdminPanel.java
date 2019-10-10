package Vuelos;

import quick.dbtable.DBTable;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class AdminPanel extends MainPanel {
	private DBTable table;
	private DataBaseConnection dataConnection;
	private JLabel lblDescription;
	private JButton btnConsult;
	private JTextArea textCommand;
	private JComboBox<String> cbTables, cbAttributes;

	/**
	 * Create the panel.
	 */
	public AdminPanel(DBTable table) {
		this.table = table;
		initGUI();
		dataConnection = DataBaseConnection.getInstance();
		fillComboBoxTable();
	}

	private void initGUI() {
		setBounds(100, 100, 1024, 600);
		setLayout(null);

		table.setBounds(204, 160, 808, 384);
		this.add(table);
		table.setVisible(true);

		lblDescription = new JLabel("Ingrese la consulta");
		lblDescription.setBounds(10, 11, 150, 25);
		add(lblDescription);
		lblDescription.setVisible(true);

		btnConsult = new JButton("Consultar");
		btnConsult.setBounds(882, 48, 130, 25);
		add(btnConsult);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 48, 858, 100);
		scrollPane.setVisible(true);
		add(scrollPane);

		textCommand = new JTextArea();
		textCommand.setBounds(scrollPane.getBounds());
		textCommand.setVisible(true);
		scrollPane.setViewportView(textCommand);

		btnConsult.setVisible(true);

		JLabel lblTables = new JLabel("Tablas");
		lblTables.setBounds(10, 160, 150, 25);
		add(lblTables);

		JLabel lblAttributes = new JLabel("Atributos");
		lblAttributes.setBounds(12, 271, 120, 25);
		add(lblAttributes);

		cbTables = new JComboBox<String>();
		cbTables.setBounds(12, 197, 180, 24);
		add(cbTables);

		cbAttributes = new JComboBox<String>();
		cbAttributes.setBounds(12, 308, 180, 24);
		add(cbAttributes);
		
		btnConsult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String query = textCommand.getText().trim();
				DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
				cbAttributes.setModel(model);
				dataConnection.refreshTable(table, query);
				fillComboBoxTable();
			}
		});

		cbTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String msj = "Describe " + cbTables.getSelectedItem().toString() + ";";
				fillComboBoxAttributes(msj);
			}
		});

		cbAttributes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String message = "select " + cbAttributes.getSelectedItem().toString() + " from "
						+ cbTables.getSelectedItem().toString() + ";";
				dataConnection.refreshTable(table, message);
			}
		});
	}

	private void fillComboBoxAttributes(String msj) {
		dataConnection.refreshExcecute(msj, cbAttributes, this);
	}

	private void fillComboBoxTable() {
		String message = "show tables;";
		dataConnection.refreshExcecute(message, cbTables, this);
	}

	public DBTable getDBTable() {
		return table;
	}
}
