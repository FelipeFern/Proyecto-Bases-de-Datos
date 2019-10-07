package Vuelos;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import quick.dbtable.DBTable;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.MaskFormatter;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JFormattedTextField;

@SuppressWarnings("serial")
public class EmployeePanel extends MainPanel {
	private DBTable table;
	private JList<String> cityToList, cityFromList;
	private DataBaseConnection dbConnection;
	private JFormattedTextField ftfDateSince, ftfDateTo;
	private String cityTo, cityFrom;
	private boolean to_from=false;

	/**
	 * Create the panel.
	 */
	public EmployeePanel(DBTable table) {
		this.table = table;
		setBounds(100, 100, 1024, 600);
		setLayout(null);
		dbConnection = DataBaseConnection.getInstance();
		initGUI();
		fillCityToList();
		fillCityFromList();
	}

	private void initGUI() {
		add(table, BorderLayout.CENTER);
		table.setBounds(12, 155, 494, 433);

		JLabel lblCityFrom = new JLabel("Ciudad origen");
		lblCityFrom.setBounds(12, 12, 125, 25);
		lblCityFrom.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblCityFrom);

		JLabel lblCityTo = new JLabel("Ciudad destino");
		lblCityTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblCityTo.setBounds(149, 12, 125, 25);
		add(lblCityTo);

		JRadioButton rdbtnGoFlights = new JRadioButton("Ida");
		rdbtnGoFlights.setBounds(282, 45, 110, 25);
		add(rdbtnGoFlights);

		JRadioButton rdbtnRoundTrip = new JRadioButton("Ida y Vuelta");
		rdbtnRoundTrip.setBounds(282, 73, 110, 25);
		add(rdbtnRoundTrip);
	
		ButtonGroup bGroup = new ButtonGroup();
		bGroup.add(rdbtnGoFlights);
		bGroup.add(rdbtnRoundTrip);

		JLabel lblOption = new JLabel("Opciones vuelo");
		lblOption.setHorizontalAlignment(SwingConstants.CENTER);
		lblOption.setBounds(284, 12, 108, 25);
		add(lblOption);

		JLabel lblDateSince = new JLabel("Fecha desde");
		lblDateSince.setHorizontalAlignment(SwingConstants.CENTER);
		lblDateSince.setBounds(400, 12, 125, 25);
		add(lblDateSince);

		JLabel lblDateTo = new JLabel("Fecha hasta");
		lblDateTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblDateTo.setBounds(537, 12, 125, 25);
		add(lblDateTo);

		JButton btnShowFlights = new JButton("Mostrar vuelos");
		btnShowFlights.setBounds(672, 12, 139, 58);
		add(btnShowFlights);
		btnShowFlights.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(to_from) {
					refrescarTablaIdaYVuelta();					
				}
				else {
					refrescarTablaIda();
				}
			}
		});

		JSeparator separator = new JSeparator();
		separator.setBounds(12, 141, 1000, 2);
		add(separator);

		JScrollPane scrollPaneCityFrom = new JScrollPane();
		scrollPaneCityFrom.setBounds(12, 49, 125, 80);
		add(scrollPaneCityFrom);

		cityFromList = new JList<String>();
		scrollPaneCityFrom.setViewportView(cityFromList);

		JScrollPane scrollPaneCityTo = new JScrollPane();
		scrollPaneCityTo.setBounds(149, 49, 125, 80);
		add(scrollPaneCityTo);

		cityToList = new JList<String>();
		scrollPaneCityTo.setViewportView(cityToList);

		initJFormattedTextFields();

		cityToList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()){		           
		            cityTo = cityToList.getSelectedValue().toString();
		        }
			}
		});
		
		cityFromList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting()){		           
		            cityFrom = cityFromList.getSelectedValue().toString();
		        }

			}
		});		
		
		rdbtnGoFlights.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	            to_from = false;

	        }
	    });		
		
		rdbtnRoundTrip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
	            to_from = true;

	        }
	    });
		
		//Agregar listener a los botones que cuando se active el de ida y vuelta, se ponga true la variable to_from;
	}

	private void initJFormattedTextFields() {
		MaskFormatter dateMask = null;
		try {
			dateMask = new MaskFormatter("##/##/####");
			dateMask.setPlaceholderCharacter('-');
			dateMask.setValidCharacters("0123456789");
		} catch (Exception e) {
			e.printStackTrace();
		}
		ftfDateSince = new JFormattedTextField(dateMask);
		ftfDateSince.setHorizontalAlignment(JTextField.RIGHT);
		ftfDateTo = new JFormattedTextField(dateMask);
		ftfDateTo.setHorizontalAlignment(JTextField.RIGHT);
		ftfDateSince.setBounds(400, 45, 124, 25);
		ftfDateTo.setBounds(537, 45, 124, 25);
		add(ftfDateTo);
		add(ftfDateSince);
	}

	public DBTable getDBTable() {
		return table;
	}

	public void fillCityFromList() {
		String message = "SELECT ASalida_ciudad FROM vuelos_disponibles GROUP BY ASalida_ciudad;";
		dbConnection.refreshExcecute(message, cityFromList, this);
	}

	public void fillCityToList() {
		String message = "SELECT ALlegada_ciudad FROM vuelos_disponibles GROUP BY ALlegada_ciudad";
		dbConnection.refreshExcecute(message, cityToList, this);
	}
	
	private void refrescarTablaIda(){	   
		String fechaSalida = this.ftfDateSince.getText().trim();		
		String consult = "SELECT vp.Numero, vp.ASalida_nombre, vp.HoraSalida, vp.ALlegada_nombre, vp.HoraLlegada, vp.Modelo, vp.duracion \n" +
                         "FROM vuelos_disponibles as vp \n" +
                         "WHERE vp.ASalida_ciudad = " + cityTo + " \n" +
                         "AND vp.ALlegada.ciudad = " + cityFrom + " \n" +
                         "AND vp.Fecha = " + fechaSalida + " \n" +
                         "ORDER BY vp.Fecha, vp.ASalida_ciudad, vp.ALlegada_ciudad ";
		dbConnection.refreshTable(table, consult);	    	 	    
	}
	

	private void refrescarTablaIdaYVuelta(){	   
		String fechaSalida = this.ftfDateSince.getText().trim();
		String fechaVuelta = this.ftfDateTo.getText().trim();
		String consult = "SELECT vp.Numero, vp.ASalida_nombre, vp.HoraSalida, vp.ALlegada_nombre, vp.HoraLlegada, vp.Modelo, vp.duracion \n" +
                         "FROM vuelos_disponibles as vp \n" +
                         "WHERE (vp.ASalida_ciudad = " + cityTo + " \n" +
                         "AND vp.ALlegada.ciudad = " + cityFrom + " \n" +
                         "AND vp.Fecha = " + fechaSalida + ") \n" +
                         "OR (vp.ASalida_ciudad = " + cityTo + " \n" +
                         "AND vp.ALlegada.ciudad = " + cityFrom + " \n" +
                         "AND vp.Fecha = " + fechaVuelta + ") \n" +
                         "ORDER BY vp.Fecha, vp.ASalida_ciudad, vp.ALlegada_ciudad ";
		dbConnection.refreshTable(table, consult);	    	 	    
	}
}
