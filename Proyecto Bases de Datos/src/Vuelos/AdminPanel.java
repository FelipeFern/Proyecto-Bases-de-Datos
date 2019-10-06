package Vuelos;

import quick.dbtable.DBTable;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class AdminPanel extends MainPanel {
	private DBTable table;
	private DataBaseConnection dataConnection;
	private JLabel lblDescription;
	private JButton btnConsult;
	private JTextArea textCommand;
	private JList<String> listTables;
	private JList<String> listAttributes;

	/**
	 * Create the panel.
	 */
	public AdminPanel(DBTable table) {
		this.table = table;
		initGUI();
		dataConnection = DataBaseConnection.getInstance();
		llenarListaTablas();
	}

	private void initGUI() {
		setBounds(100, 100, 1024, 600);
		setLayout(null);

		table.setBounds(312, 160, 682, 384);
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
		//textCommand.setBounds(10, 48, 858, 100);
		textCommand.setBounds(scrollPane.getBounds());
		// add(textCommand);
		textCommand.setVisible(true);
		scrollPane.setViewportView(textCommand);

		btnConsult.setVisible(true);

		listTables = new JList<String>();
		listAttributes = new JList<String>();
		add(listTables);
		add(listAttributes);
		listTables.setVisible(true);
		listAttributes.setVisible(true);
		listTables.setBounds(12, 197, 150, 347);
		listAttributes.setBounds(174, 197, 120, 347);

		JLabel lblTables = new JLabel("Tablas");
		lblTables.setBounds(10, 160, 150, 25);
		add(lblTables);

		JLabel lblAttributes = new JLabel("Atributos");
		lblAttributes.setBounds(174, 160, 120, 25);
		add(lblAttributes);

		btnConsult.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refrescarTabla();
			}
		});

		listTables.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				String nombreTabla = listTables.getSelectedValue().toString();
				llenarListaAtributos(nombreTabla);
			}
		});

		listAttributes.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				String message = "select " + listAttributes.getSelectedValue() + " from "
						+ listTables.getSelectedValue() + ";";
				dataConnection.refreshTable(table, message);
			}
		});

	}

	private void llenarListaTablas() {
		String message = "show tables;";
		dataConnection.refreshExcecute(message, listTables, this);
	}

	private void llenarListaAtributos(String tabla) {
		String message = "Describe " + tabla + ";";
		dataConnection.refreshExcecute(message, listAttributes, this);
	}

	public DBTable getDBTable() {
		return table;
	}

	private void refrescarTabla() {
		String consult = this.textCommand.getText().trim();
		dataConnection.refreshTable(table, consult);
	}
}
