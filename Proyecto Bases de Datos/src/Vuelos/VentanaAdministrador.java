package Vuelos;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.sql.*;

import quick.dbtable.DBTable;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;


public class VentanaAdministrador extends JFrame{
	
	
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelTablas;
	private VentanaPrincipal ventanaPrincipal;
	// private JTable tablaResultado;
	private String password;
	private JButton btnVolver;
	private DBTable tablaResultado;
	private java.sql.Connection conexion;
	private JList<String> listaTablas;
	private JLabel lblAtributos;
	private JList<String> listaAtributos;
	private JTextArea textoSentenciaSql;
    
	 
	   
	   
	public VentanaAdministrador(VentanaPrincipal padre, String pass) {
		setTitle("Ventana Admin");
		ventanaPrincipal = padre;
		password = pass;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 519);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		setResizable(false);

		agregarComponentes();
	}
	
	   private void agregarComponentes() {

			// Label
			JLabel lblSentencia = new JLabel("Ingrese su sentencia SQL");
			lblSentencia.setFont(new Font("Calibri", Font.BOLD, 18));
			JLabel lblResultado = new JLabel("Resultado");
			lblResultado.setFont(new Font("Calibri", Font.BOLD, 18));

			// Tabla resultado
			tablaResultado = new DBTable();
			tablaResultado.setEditable(false);
			
			// Boton Volver
			btnVolver = new JButton("Volver");
			btnVolver.setFocusPainted(false);


			// Panel tablas y atributos
			panelTablas = new JPanel();

			textoSentenciaSql = new JTextArea("SELECT vp.numero, vp.aeropuerto_salida, vp.aeropuerto_llegada, s.dia \n" +
											  "FROM vuelos_programados as vp, salidas as s \n" +
											  "WHERE vp.numero = s.vuelo \n" +											
											  "ORDER BY vp.numero, vp.aeropuerto_salida,vp.aeropuerto_llegada, s.dia");
			
			
			JButton btnConfirmar = new JButton("Confirmar");
			btnConfirmar.setFocusPainted(false);

			btnConfirmar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					actualizarTablaResultado();
				}
			});
			btnConfirmar.setFont(new Font("Calibri", Font.BOLD, 12));
			GroupLayout gl_contentPane = new GroupLayout(contentPane);
			gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_contentPane.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addComponent(btnVolver)
								.addGap(333))
							.addComponent(lblSentencia)
							.addComponent(btnConfirmar)
							.addComponent(lblResultado)
							.addComponent(textoSentenciaSql, GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
							.addComponent(tablaResultado, GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE))
						.addGap(18)
						.addComponent(panelTablas, GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE)
						.addContainerGap())
			);
			gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.TRAILING)
					.addGroup(gl_contentPane.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
							.addComponent(panelTablas, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
							.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
								.addContainerGap()
								.addComponent(lblSentencia)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(textoSentenciaSql, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(btnConfirmar)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblResultado)
								.addGap(8)
								.addComponent(tablaResultado, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(btnVolver)
						.addContainerGap())
			);

			JLabel lblTablas = new JLabel("Tablas");
			lblTablas.setFont(new Font("Calibri", Font.BOLD, 18));

			listaTablas = new JList<String>();

			lblAtributos = new JLabel("Atributos");
			lblAtributos.setFont(new Font("Calibri", Font.BOLD, 18));

			listaAtributos = new JList<String>();
			GroupLayout gl_panelTablas = new GroupLayout(panelTablas);
			gl_panelTablas.setHorizontalGroup(
				gl_panelTablas.createParallelGroup(Alignment.TRAILING)
					.addGroup(gl_panelTablas.createSequentialGroup()
						.addContainerGap(186, Short.MAX_VALUE)
						.addComponent(lblTablas)
						.addGap(88))
					.addGroup(gl_panelTablas.createSequentialGroup()
						.addContainerGap(182, Short.MAX_VALUE)
						.addComponent(lblAtributos)
						.addGap(65))
					.addGroup(Alignment.LEADING, gl_panelTablas.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panelTablas.createParallelGroup(Alignment.LEADING)
							.addComponent(listaAtributos, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
							.addComponent(listaTablas, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE))
						.addContainerGap())
			);
			gl_panelTablas.setVerticalGroup(
				gl_panelTablas.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panelTablas.createSequentialGroup()
						.addGap(12)
						.addComponent(lblTablas)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(listaTablas, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblAtributos)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(listaAtributos, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
			);
			panelTablas.setLayout(gl_panelTablas);
			contentPane.setLayout(gl_contentPane);

			// Seteo listeners botones
			setListeners();

			// Conexion a la base de datos y carga de datos en el JList listaTabla
			conectarBD();
			llenarListaTablas();

		}
	
	private void setListeners() {
		// Listener boton volver
		btnVolver.setFont(new Font("Calibri", Font.BOLD, 12));
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cerrarBD();
				dispose();
				ventanaPrincipal.restaurar();
			}
		});

		// Lista JList listaTablas
		listaTablas.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				String nombreTabla = listaTablas.getSelectedValue().toString();
				llenarListaAtributos(nombreTabla);
			}
		});
	}

	
	private void llenarListaTablas() {
		try {
			Statement s = (Statement) conexion.createStatement();
			s.executeQuery("show tables;");
			ResultSet rs = s.getResultSet();
			DefaultListModel<String> model = new DefaultListModel<String>();
			while (rs.next()) {
				model.addElement(rs.getString(1));
			}
			listaTablas.setModel(model);
		} catch (SQLException ex) {
			System.out.println("SQLExcepcion: " + ex.getMessage());
			System.out.println("SQLEstado: " + ex.getSQLState());
			System.out.println("CodigoError: " + ex.getErrorCode());
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getMessage() + "\n",
					"Error en el acceso.", JOptionPane.ERROR_MESSAGE);
		}
	}

	
	
	private void llenarListaAtributos(String tabla) {
		try {
			DefaultListModel<String> model = new DefaultListModel<String>();
			Statement s = (Statement) conexion.createStatement();
			s.executeQuery("Describe " + tabla + ";"); // Se obtienen los
														// atributos de la tabla
			ResultSet rs = s.getResultSet();
			while (rs.next()) {
				model.addElement(rs.getString(1));
			}
			listaAtributos.setModel(model);

		} catch (SQLException ex) {
			System.out.println("SQLExcepcion: " + ex.getMessage());
			System.out.println("SQLEstado: " + ex.getSQLState());
			System.out.println("CodigoError: " + ex.getErrorCode());
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getMessage() + "\n",
					"Error en el acceso.", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	private void conectarBD()
	   {
	         try
	         {
	            String driver ="com.mysql.cj.jdbc.Driver";
	        	String servidor = "localhost:3306";
	        	String baseDatos = "vuelos"; 
	        	String usuario = "admin";
	        	String uriConexion = "jdbc:mysql://" + servidor + "/" + 
	        	                     baseDatos +"?serverTimezone=America/Argentina/Buenos_Aires";
	   
	       //establece una conexión con la  B.D. "vuelos"  usando directamante una tabla DBTable    
	            tablaResultado.connectDatabase(driver, uriConexion, usuario, password);
	           
	         }
	         catch (SQLException ex)
	         {
	            JOptionPane.showMessageDialog(this,
	                           "Se produjo un error al intentar conectarse a la base de datos.\n" 
	                            + ex.getMessage(),
	                            "Error",
	                            JOptionPane.ERROR_MESSAGE);
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
	         }
	         catch (ClassNotFoundException e)
	         {
	            e.printStackTrace();
	         }
	      
	   }

	
	private void cerrarBD() {
		if (this.conexion != null)
	      {
	         try
	         {
	            conexion.close();
	            conexion = null;
	         }
	         catch (SQLException ex)
	         {
	            System.out.println("SQLExcepcion: " + ex.getMessage());
	            System.out.println("SQLEstado: " + ex.getSQLState());
	            System.out.println("CodigoError: " + ex.getErrorCode());
	         }
	      }
	}

	 
	   
	   
	   private void actualizarTablaResultado() {
			String sentencia=textoSentenciaSql.getText(); //Texto ingresado en el jtextfield (insert,update,etc)
			
			try
		      {  
				Statement s=(Statement) conexion.createStatement();
				if(s.execute(sentencia)){
		    	  tablaResultado.setSelectSql(sentencia.trim()); //Se intenta hacer la consulta
		    	  tablaResultado.createColumnModelFromQuery();    	    
		    	  for (int i = 0; i < tablaResultado.getColumnCount(); i++)
		    	  { // Se ajusta el formato de la hora 		   		  
		    		 if	 (tablaResultado.getColumn(i).getType()==Types.TIME)  
		    		 {    		 
		    		  tablaResultado.getColumn(i).setType(Types.CHAR);  
		  	       	 }
		    		 // Se ajusta el formato de la fechas
		    		 if	 (tablaResultado.getColumn(i).getType()==Types.DATE)
		    		 {
		    		    tablaResultado.getColumn(i).setDateFormat("dd/MM/YYYY");
		    		 }
		          }   	     	  
		    	  tablaResultado.refresh();
		       }
		      }catch (SQLException ex)
			      {
			         System.out.println("SQLExcepcion: " + ex.getMessage());
			         System.out.println("SQLEstado: " + ex.getSQLState());
			         System.out.println("CodigoError: " + ex.getErrorCode());
			         JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
			                                       ex.getMessage() + "\n", 
			                                       "Error al ejecutar la consulta.",
			                                       JOptionPane.ERROR_MESSAGE);
			         
			      }
}
	   

}
