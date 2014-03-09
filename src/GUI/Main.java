package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;

import java.awt.TextArea;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import jxl.write.WriteException;

public class Main extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private ResultSet rs = null;
	private String[][] arr;
	private String[] arr2;
	private JTable table_1;
	private JScrollPane pane;
	private Connection conn;

	/**
	 * Launch the application.
	 */
	// public static void main(String[] args) {
	// EventQueue.invokeLater(new Runnable() {
	// public void run() {
	// try {
	// Main frame = new Main();
	// frame.setVisible(true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// }

	/**
	 * Create the frame.
	 * 
	 * @throws SQLException
	 */

	public Main(Connection con) throws SQLException {
		conn = con;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 724, 485);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmChangeToExcel = new JMenuItem("change to excel");
		mntmChangeToExcel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog fd = new FileDialog(new Shell(), SWT.OPEN);
				fd.setFilterNames(new String[]{"XML Files"});
				fd.setFilterExtensions(new String[]{"*.xml"});
				String name = fd.open();
			String name2 = name.substring(name.lastIndexOf("\\")+1);
				name2 = name2.substring(0, name2.indexOf('.'));
//				System.out.println(name);
				try {
					Statement st = conn.createStatement();
					ResultSet rs = st.executeQuery("select * from "+name2);
					EXCEL excel = new EXCEL();
					try {
						excel.writeXCEL(rs, name2,name.replace(name2+".xml",""));
					} catch (WriteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
//					e1.printStackTrace();
				} 
			}
		});
		mnFile.add(mntmChangeToExcel);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		//
		// try {
		// Class.forName("JDBC.MyDriver");
		// } catch (ClassNotFoundException e1) {
		// // TODO Auto-generated catch block
		// System.out.println("7alawa");
		// }
		// Connection conn = null;
		// Driver dv = new MyDriver();
		// Properties information = new Properties();
		// information.put("username", "");
		// information.put("password", "");
		// conn = dv.connect("jdbc:odbc:Mydriver", information);
		final Statement st = conn.createStatement();

		final TextArea textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setBounds(10, 258, 698, 120);
		contentPane.add(textArea);

		textField = new JTextField();
		textField.setFocusable(true);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String command = textField.getText();
					textArea.setText(textArea.getText() + "\n" + command);
					textField.setText("");
					try {
						if (st.execute(command)) {
							rs = st.executeQuery(command);
							arr = changeToArray();
							arr2 = getColName();
							table_1 = new JTable(arr, arr2);
							table_1.setBounds(139, 11, 559, 241);
							pane = new JScrollPane(table_1);
							pane.setBounds(139, 11, 559, 241);
							contentPane.add(pane);
							// p.repaint();
						}
						st.close();

					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						textArea.setText(textArea.getText() + "\n"
								+ e1.getMessage());
					}
				}
			}
		});
		textField.setBounds(10, 384, 698, 42);
		contentPane.add(textField);
		textField.setColumns(10);

		// table_1 = new JTable();
		// table_1.setBounds(139, 11, 559, 241);
		// contentPane.add(table_1);

	}

	public String[] getColName() throws SQLException {
		ResultSetMetaData ms = rs.getMetaData();
		int nCols = ms.getColumnCount();
		String[] colNames = new String[nCols];
		for (int i = 0; i < nCols; i++) {
			colNames[i] = ms.getColumnName(i + 1);
		}
		return colNames;
	}

	public String[][] changeToArray() throws SQLException {
		ResultSetMetaData ms = rs.getMetaData();
		ArrayList<String[]> table = new ArrayList<String[]>();
		int nCols = ms.getColumnCount();
		String[] row = new String[nCols];
		while (rs.next()) {
			for (int i = 0; i < nCols; i++) {
				row[i] = rs.getObject(i + 1) + "";
			}

			table.add(Arrays.copyOf(row, nCols));
		}

		String[][] result = new String[table.size()][];
		int length = result.length;
		for (int i = 0; i < length; i++) {
			result[i] = Arrays.copyOf(table.get(i), table.get(i).length);

		}

		return result;
	}
}
