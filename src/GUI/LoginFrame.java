package GUI;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import DBSystem.MyDatabase;
import JDBC.MyDriver;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.JPasswordField;
import javax.swing.JLabel;

public class LoginFrame extends JFrame {

	private JPanel contentPane;
	private JTextField directory;
	private JTextField username;
	private JPasswordField passwordField;
	private JButton btnLogin;
	private JButton btnCancel;
	private JButton btnNewButton;
	private Connection conn;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public LoginFrame() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 307, 218);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		

		directory = new JTextField();
		directory.setEditable(false);
		directory.setBounds(77, 26, 125, 28);
		contentPane.add(directory);
		directory.setColumns(10);

		username = new JTextField();
		username.setColumns(10);
		username.setBounds(77, 66, 184, 28);
		contentPane.add(username);

		btnLogin = new JButton("Login");
		btnLogin.setBounds(6, 146, 117, 29);
		contentPane.add(btnLogin);

		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(184, 146, 117, 29);
		contentPane.add(btnCancel);
		btnLogin.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!directory.getText().equals("")) {
					// Set path in mydatabase
					// call my dabase connect to verify login
					// create the UI of your application
					MyDatabase.path = directory.getText();
					Properties info = new Properties();
					info.put("username", username.getText());
					info.put("password", passwordField.getText());
					String url = "jdbc:odbc:Mydriver";
					MyDriver dv = new MyDriver();
					try {
						conn = dv.connect(url , info);
						Main m = new Main (conn); 
						m.setVisible(true);
					} catch (SQLException e1) {
					}
					if(conn!=null)
					hide();
				}
			}
		});
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		passwordField = new JPasswordField();
		passwordField.setBounds(77, 106, 184, 28);
		contentPane.add(passwordField);

		JLabel lblWorkspace = new JLabel("Workspace");
		lblWorkspace.setBounds(6, 32, 73, 16);
		contentPane.add(lblWorkspace);

		JLabel lblUser = new JLabel("Username");
		lblUser.setBounds(4, 72, 75, 16);
		contentPane.add(lblUser);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(4, 112, 61, 16);
		contentPane.add(lblPassword);
		btnLogin.setEnabled(false);
		btnNewButton = new JButton("Browse");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = fileChooser();
				if (s != null)
					directory.setText(s);
			}
		});
		btnNewButton.setBounds(203, 27, 83, 29);
		contentPane.add(btnNewButton);

	}

	public String fileChooser() {
		JFileChooser jfc = new JFileChooser("C:\\");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = jfc.showDialog(this, "Select Target Directory");
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			String sfilename = file.getPath();
			btnLogin.setEnabled(true);
			return sfilename + "\\";
		}
		return null;
	}
}

