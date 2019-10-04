package Vuelos;


import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import java.sql.*;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;


public class VentanaPrincipal extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnAdmin;
	private java.sql.Connection conexion;
	private JLabel lblVuelos;
	private VentanaEmpleado ventanaEmpleado;
	private VentanaAdministrador ventanaAdmin;
	private JButton btnEmpleado;
	
	
/*
 * La idea es que en esta clase se conecte a la base de datos y te mueste un frame para ver en que opcion
 * te queres conectar, si para hacer consultas como administrador o como Empleado. Entonces le metemos dos 
 * botones y que vayan a los distintas clases (Paneles) donde hagan lo que tengan que hacer.
 * 
 */
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaPrincipal frame = new VentanaPrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public VentanaPrincipal() {
		setTitle("Vuelos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		setResizable(false);

		agregarComponentes();
		setListeners();
		//conectarBD();

	}
	
	
	
	private void agregarComponentes() {
		btnAdmin = new JButton("Administrador");
		btnAdmin.setFont(new Font("Calibri", Font.BOLD, 14));
		btnAdmin.setFocusPainted(false);
		
		lblVuelos = new JLabel("Sistema de Vuelos");
		lblVuelos.setFont(new Font("Calibri", Font.BOLD, 30));
		
		btnEmpleado = new JButton("Empleado");
		btnEmpleado.setFont(new Font("Calibri", Font.BOLD, 14));
		btnEmpleado.setFocusPainted(false);
		
		
				
		
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(216)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							//.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
							//	.addComponent(lblAdmin)
							//	.addComponent(lblInspector))
							.addGap(33)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnEmpleado, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnAdmin, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblVuelos)
							.addGap(59))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(110)
							.addComponent(lblVuelos)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(29)
									.addComponent(btnAdmin, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)))										
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(61)
									.addComponent(btnEmpleado, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()))
					)
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	
	
	private void setListeners() {
		//agreagmos oyente para el login de admin
		btnAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JPasswordField pf = new JPasswordField();
	
				int okCxl = JOptionPane.showConfirmDialog(null, pf, "Ingrese su password", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);

				if (okCxl == JOptionPane.OK_OPTION) {
					String password = new String(pf.getPassword());
					consultaAdmin(password);
				}
				
			}
		});
		
		//agreamos oyente para el login del empleado
		btnEmpleado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JTextField user = new JTextField();
				int ok = JOptionPane.showConfirmDialog(null, user, "Ingrese su legajo", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE);

				if (ok == JOptionPane.OK_OPTION) {
					String usuario = new String(user.getText());
					JPasswordField pf = new JPasswordField();
					int ok1 = JOptionPane.showConfirmDialog(null, pf, "Ingrese su password", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE);

					if (ok1 == JOptionPane.OK_OPTION) {
						String password = new String(pf.getPassword());
						consultaEmpleado(usuario,password);
					}
				}
				
				
				
			}
		});
	}
		
	
	
	private void conectarBD() {
		try
        {
		//Intento de conectar a la base de datos
		   String servidor = "localhost:3306";
           String baseDatos = "vuelos";
           String url = "jdbc:mysql://" + servidor + "/" + baseDatos;
  
          conexion= DriverManager.getConnection(url, "admin", "admin");
        }
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(this,
                                          "Se produjo un error al intentar conectarse a la base de datos.\n" + ex.getMessage(),
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);
            System.out.println("SQLExcepcion: " + ex.getMessage());
            System.out.println("SQLEstado: " + ex.getSQLState());
            System.out.println("CodigoError: " + ex.getErrorCode());
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }
	}
		
		

	private void consultaAdmin (String pass) {
		 try
         {
        	String servidor = "localhost:3306";
            String baseDatos = "vuelos";
            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos;
   
            //Intento de conexion a la base con las password ingresada para el admin
           conexion =DriverManager.getConnection(uriConexion, "admin", pass);
           //Cambio a la ventana para el admin si la contraseña y ususario son correctos, sino voy a excepcion
           ventanaAdmin= new VentanaAdministrador(this,pass);
           ventanaAdmin.setVisible(true);
          // cerrarVistaActual();
         }
         /*catch (Exception ex) {
        	 String msg= "Password incorrecta - Intente nuevamente";
        	 JOptionPane.showMessageDialog(new JFrame(), msg, "Error",JOptionPane.ERROR_MESSAGE);
         }
         */
		catch (SQLException ex)
		{
			// en caso de error, se muestra la causa en la consola
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                                     ex.getMessage() + "\n", 
                                     "Error al Querer acceder.",
                                     JOptionPane.ERROR_MESSAGE);
       
    }
		 
		
	}
	

	private boolean consultarLoginEmpleado(String legajo, String pass){
		String consulta="select * from empleados where legajo='"+legajo+"' and password=md5('"+pass+"');";
		boolean respuesta=true;
		try{
			Statement s=(Statement) conexion.createStatement();
			s.executeQuery(consulta);
			ResultSet rs= s.getResultSet();
			respuesta=rs.next();
			
		}catch (SQLException ex)
	      {
	         // en caso de error, se muestra la causa en la consola
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	         JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
	                                       ex.getMessage() + "\n", 
	                                       "Error al Querer acceder.",
	                                       JOptionPane.ERROR_MESSAGE);
	         
	      }
		return respuesta;
	}

	private void consultaEmpleado (String legajo, String pass) {
	}
	/*	 try
         {
        	String servidor = "localhost:3306";
            String baseDatos = "vuelos";
            String uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos;
   
            //Intento de conexion a la base con las password ingresada para el admin
            conexion = DriverManager.getConnection(uriConexion, "empleado", "empleado");
           
           	if(consultarLoginEmpleado(legajo,pass)){
        	   	cerrarVistaActual();
	           	//Cambio a la ventana para el admin si el usuario y contraseña son correctos
        	   	//Crear ventana empleado
	           	ventanaEmpleado= new VentanaEmpleado();1
	           	// Esta el error para darme cuenta que flta hacer la clase empleado.
	           	ventanaEmpleado.setVisible(true);
         	}else{
          	 	String msg= "Legajo/Password incorrectos - Intente nuevamente";
          	 	JOptionPane.showMessageDialog(new JFrame(), msg, "Error",JOptionPane.ERROR_MESSAGE);        	   
           }
         }
         catch (Exception ex) {
        	 String msg= "Error al conectar la base de datos - Intente nuevamente";
        	 JOptionPane.showMessageDialog(new JFrame(), msg, "Error",JOptionPane.ERROR_MESSAGE);
         }
		
	}
	*/
	private void cerrarVistaActual() {
		cerrarBD();
		this.setVisible(false);
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
	
	public void restaurar() {
		setVisible(true);
		conectarBD();
	}
	
	

}
