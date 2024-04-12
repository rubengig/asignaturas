package asignaturas;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class ConnectionSingleton {
	
	private static Connection con;
	 public static Connection getConnection() throws SQLException {
	String url = "jdbc:mysql://127.0.0.1:3307/clases";
	String user = "alumno";
	String password = "alumno";
	if (con==null || con.isClosed()) {
	con=DriverManager.getConnection(url, user, password);
	}
	return con;
	}
	}


public class Asignaturas {

	private JFrame frame;
	private JTable table;
	private JTextField textFieldCodigo;
	private JTextField textFieldNombre;
	private JTextField textFieldHoras;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Asignaturas window = new Asignaturas();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Asignaturas() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 544, 404);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textFieldCodigo = new JTextField();
		textFieldCodigo.setBounds(181, 237, 105, 19);
		frame.getContentPane().add(textFieldCodigo);
		textFieldCodigo.setColumns(10);
		
		textFieldNombre = new JTextField();
		textFieldNombre.setColumns(10);
		textFieldNombre.setBounds(181, 268, 105, 19);
		frame.getContentPane().add(textFieldNombre);
		
		textFieldHoras = new JTextField();
		textFieldHoras.setColumns(10);
		textFieldHoras.setBounds(181, 299, 105, 19);
		frame.getContentPane().add(textFieldHoras);
		
		DefaultTableModel model = new DefaultTableModel();
		
		JTable table_1 = new JTable(model);
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				textFieldCodigo.setEditable(false);
				int indice = table_1.getSelectedRow();
				textFieldCodigo.setText(model.getValueAt(indice, 0).toString());
				textFieldNombre.setText(model.getValueAt(indice, 1).toString());
				textFieldHoras.setText(model.getValueAt(indice, 2).toString());
				
			}
		});
		table_1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		JScrollPane scrollPane = new JScrollPane(table_1);
		scrollPane.setBounds(118, 47, 297, 164);
		
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setBounds(118, 47, 297, 164);
		frame.getContentPane().add(table);
		
		
		
		model.addColumn("Codigo");
		model.addColumn("Nombre");
		model.addColumn("Horas");
		
		try {
			Connection con=ConnectionSingleton.getConnection();
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM asignaturas");
			while (rs.next()) {
			 Object[] row = new Object[3];
			 row[0] = rs.getInt("codas");
			 row[1] = rs.getString("nomas");
			 row[2] = rs.getInt("horas");
			 model.addRow(row);
			}
			
			rs.close();
			stmt.close();
			con.close();
			
		} catch (SQLException exc) {
			exc.printStackTrace();
		}
		
		
		
		JLabel lblNewLabel = new JLabel("Codigo:");
		lblNewLabel.setBounds(118, 239, 70, 15);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(118, 270, 70, 15);
		frame.getContentPane().add(lblNombre);
		
		JLabel lblHoras = new JLabel("Horas:");
		lblHoras.setBounds(118, 301, 70, 15);
		frame.getContentPane().add(lblHoras);
		
		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnGuardar.addMouseListener(new MouseAdapter() {			
			@Override
			public void mouseClicked(MouseEvent e) {
				String nombre=textFieldNombre.getText();
				int id=Integer.parseInt(textFieldCodigo.getText());
				int horas =Integer.parseInt(textFieldHoras.getText());
				try {
					Connection con=ConnectionSingleton.getConnection();
					
					PreparedStatement ins_pstmt = con.prepareStatement("INSERT INTO asignaturas (nomas, horas) VALUES (?, ?)");
					
					ins_pstmt.setString(1, nombre);
					ins_pstmt.setInt(2, horas);
					
					ins_pstmt.executeUpdate();
					ins_pstmt.close();
					con.close();
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				JOptionPane.showMessageDialog(null, "Producto añadido!"); 
				
			}
			
		});
		btnGuardar.setBounds(298, 234, 117, 25);
		frame.getContentPane().add(btnGuardar);
		
		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				String nombre=textFieldNombre.getText();
				int id = Integer.parseInt(textFieldCodigo.getText());
				int horas=Integer.parseInt(textFieldHoras.getText());
				
				try {
					Connection con=ConnectionSingleton.getConnection();
					
					PreparedStatement upd_pstmt = con.prepareStatement("UPDATE asignaturas SET nomas = ? WHERE codas = ?");
					upd_pstmt.setString(1, nombre);
					upd_pstmt.setInt(2, id);
					
					PreparedStatement upd_pstmt2 = con.prepareStatement("UPDATE asignaturas SET horas = ? WHERE codas = ?");
					upd_pstmt2.setInt(1, horas);
					upd_pstmt2.setInt(2, id);
					
					
					upd_pstmt.executeUpdate();
					upd_pstmt.close();
					upd_pstmt2.executeUpdate();
					upd_pstmt2.close();
					con.close();
					
					JOptionPane.showMessageDialog(null, "Datos cambiados!"); 
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		btnActualizar.setBounds(298, 265, 117, 25);
		frame.getContentPane().add(btnActualizar);
		
		JButton btnBorrar = new JButton("Borrar");
		btnBorrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Connection con=ConnectionSingleton.getConnection();
					
					int id =Integer.parseInt(textFieldCodigo.getText());
					PreparedStatement del_pstmt = con.prepareStatement("DELETE FROM asignaturas WHERE codas = ?");
					del_pstmt.setInt(1, id);
					
					del_pstmt.executeUpdate();
					del_pstmt.close();
		
					con.close();
					
					JOptionPane.showMessageDialog(null, "Producto borrado!"); 
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				
			}
		});
		btnBorrar.setBounds(298, 296, 117, 25);
		frame.getContentPane().add(btnBorrar);
		
		JButton btnMostrar = new JButton("Mostrar");
		btnMostrar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					Connection con=ConnectionSingleton.getConnection();
					
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM asignaturas");
					model.setRowCount(0);
					while (rs.next()) {
					 Object[] row = new Object[3];
					 row[0] = rs.getInt("codas");
					 row[1] = rs.getString("nomas");
					 row[2] = rs.getInt("horas");
					 model.addRow(row);
					}
					
					rs.close();
					stmt.close();
					con.close();
					
				} catch (SQLException exc) {
					exc.printStackTrace();
				}
				
				
				
			}
		});
		btnMostrar.setBounds(208, 10, 117, 25);
		frame.getContentPane().add(btnMostrar);
	}
}
