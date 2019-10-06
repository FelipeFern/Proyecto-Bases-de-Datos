package Vuelos;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import quick.dbtable.DBTable;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JSeparator;

@SuppressWarnings("serial")
public class EmployeePanel extends MainPanel {
	private DBTable table;
	private JTextField tFieldDateSince;
	private JTextField tFieldDateTo;

	/**
	 * Create the panel.
	 */
	public EmployeePanel(DBTable table) {
		this.table = table;
		setBounds(100, 100, 1024, 600);
		setLayout(null);

		initGUI();
	}

	private void initGUI() {
		add(table, BorderLayout.CENTER);
		table.setBounds(12, 155, 494, 433);

		JLabel lblCityFrom = new JLabel("Ciudad origen");
		lblCityFrom.setBounds(12, 12, 125, 25);
		lblCityFrom.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblCityFrom);

		JList<String> cityFromList = new JList<String>();
		cityFromList.setBounds(12, 49, 125, 80);
		add(cityFromList);

		JLabel lblCityTo = new JLabel("Ciudad destino");
		lblCityTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblCityTo.setBounds(149, 12, 125, 25);
		add(lblCityTo);

		JList<String> cityToList = new JList<String>();
		cityToList.setBounds(149, 49, 125, 80);
		add(cityToList);

		JRadioButton rdbtnGoFlights = new JRadioButton("Ida");
		rdbtnGoFlights.setBounds(282, 45, 110, 25);
		add(rdbtnGoFlights);

		JRadioButton rdbtnRoundTrip = new JRadioButton("Ida y Vuelta");
		rdbtnRoundTrip.setBounds(282, 73, 110, 25);
		add(rdbtnRoundTrip);

		ButtonGroup bGroup = new ButtonGroup();
		bGroup.add(rdbtnGoFlights);
		bGroup.add(rdbtnRoundTrip);

		tFieldDateSince = new JTextField();
		tFieldDateSince.setBounds(400, 45, 124, 25);
		add(tFieldDateSince);
		tFieldDateSince.setColumns(10);

		tFieldDateTo = new JTextField();
		tFieldDateTo.setBounds(536, 45, 124, 25);
		add(tFieldDateTo);
		tFieldDateTo.setColumns(10);

		JLabel lblOption = new JLabel("Opciones vuelo");
		lblOption.setBounds(284, 12, 108, 25);
		add(lblOption);

		JLabel lblDateSince = new JLabel("Fecha desde");
		lblDateSince.setBounds(400, 12, 125, 25);
		add(lblDateSince);

		JLabel lblDateTo = new JLabel("Fecha hasta");
		lblDateTo.setBounds(537, 12, 125, 25);
		add(lblDateTo);

		JButton btnShowFlights = new JButton("Mostrar vuelos");
		btnShowFlights.setBounds(672, 12, 139, 58);
		add(btnShowFlights);
		btnShowFlights.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});

		JSeparator separator = new JSeparator();
		separator.setBounds(12, 141, 1000, 2);
		add(separator);

	}

	public DBTable getDBTable() {
		return table;
	}
}
