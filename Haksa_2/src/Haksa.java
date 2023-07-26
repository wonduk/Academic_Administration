import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Haksa extends JFrame{
	
	JTextField tfId=null;
	JTextField tfName=null;
	JTextField tfDepartment=null;
	JTextField tfAddress=null;
	JTextArea taList=null;
	JButton btnInsert=null;
	JButton btnSelect=null;
	JButton btnUpdate=null;
	JButton btnDelete=null;
	
	JButton btnSearch=null;
	
	DefaultTableModel model=null;
	JTable table=null;
	
	Connection conn=null;
	Statement stmt=null;
	ResultSet rs=null;
	
	public Haksa() {
		this.setTitle("학사관리");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//db connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/sampledb","root","1234");					
			stmt=conn.createStatement();	
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//db close. 윈도우 종료시.
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					conn.close();
					stmt.close();
				}catch(Exception e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
		Container c=this.getContentPane();
		c.setLayout(new FlowLayout());
		
		JLabel lblId=new JLabel("학번");
		c.add(lblId);
		tfId=new JTextField(19);
		c.add(tfId);
		
		btnSearch=new JButton("검색");
		c.add(btnSearch);
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				try {
					rs=stmt.executeQuery("select * from student where id='"+tfId.getText()+"'");
					
					model.setNumRows(0); //초기화
					if(rs.next()) {
						tfId.setText(rs.getString("id"));
						tfName.setText(rs.getString("name"));
						tfDepartment.setText(rs.getString("dept"));
						tfAddress.setText(rs.getString("address"));
						
						String[] row=new String[4];
						row[0]=rs.getString("id");
						row[1]=rs.getString("name");
						row[2]=rs.getString("dept");
						row[3]=rs.getString("address");
						model.addRow(row);
					}	
					
				}catch(Exception e1) {
					e1.printStackTrace();
				}finally {
					try {
						rs.close();
					} catch (SQLException e2) {						
						e2.printStackTrace();
					}
				}
				
			}});
		
		
		JLabel lblName=new JLabel("이름");
		c.add(lblName);
		tfName=new JTextField(25);
		c.add(tfName);
		
		JLabel lblDepartment=new JLabel("학과");
		c.add(lblDepartment);
		tfDepartment=new JTextField(25);
		c.add(tfDepartment);
		
		JLabel lblAddress=new JLabel("주소");
		c.add(lblAddress);
		tfAddress=new JTextField(25);
		c.add(tfAddress);
				
		String[] colName = {"학번", "이름", "학과","주소"};
		model = new DefaultTableModel(colName, 0);
		table = new JTable(model);
		table.setPreferredScrollableViewportSize(new Dimension(250, 200));
//		c.add(table);
		c.add(new JScrollPane(table));
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				table =(JTable)e.getComponent();
				model =(DefaultTableModel)table.getModel();
				tfId.setText((String)model.getValueAt(table.getSelectedRow(), 0));
				tfName.setText((String)model.getValueAt(table.getSelectedRow(), 1));
				tfDepartment.setText((String)model.getValueAt(table.getSelectedRow(), 2));
				tfAddress.setText((String)model.getValueAt(table.getSelectedRow(), 3));
			}
		});
		
		
		
		
		btnInsert=new JButton("등록");
		btnInsert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					stmt.executeUpdate("insert into student values('"+tfId.getText()+"','"+tfName.getText()+"','"+tfDepartment.getText()+"','"+tfAddress.getText()+"')");
										
					list();
					
				}catch(Exception e1) {
					e1.printStackTrace();
				}
			}});
		
		btnSelect=new JButton("목록");
		btnSelect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				list();				
			}});
		
		btnUpdate=new JButton("수정");
		btnUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					stmt.executeUpdate("update student set name='"+tfName.getText()+"',dept='"+tfDepartment.getText()+"',address='"+tfAddress.getText()+"' where id='"+tfId.getText()+"'");
										
					list();
					
				}catch(Exception e1) {
					e1.printStackTrace();
				}
				
			}});
		btnDelete=new JButton("삭제");
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?","알림",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
					try {
						stmt.executeUpdate("delete from student where id='"+tfId.getText()+"'");
						tfId.setText("");
						tfName.setText("");
						tfDepartment.setText("");
						tfAddress.setText("");
											
						list();
						
					}catch(Exception e1) {
						e1.printStackTrace();
					}
				}
				
			}});
		c.add(btnInsert);
		c.add(btnSelect);
		c.add(btnUpdate);
		c.add(btnDelete);
		
		this.setSize(300, 430);
		this.setVisible(true);
	}

	//목록
	public void list() {
		try {
			rs=stmt.executeQuery("select * from student");
			model.setNumRows(0); //초기화
			while(rs.next()) {
				String[] row=new String[4];
				row[0]=rs.getString("id");
				row[1]=rs.getString("name");
				row[2]=rs.getString("dept");
				row[3]=rs.getString("address");
				model.addRow(row);
			}				
		}catch(Exception e1) {
			e1.printStackTrace();
		}finally {
			try {
				rs.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new Haksa();

	}

}
