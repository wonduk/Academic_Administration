import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Student extends JPanel{
	
	JTextField tfId=null;
	JTextField tfName=null;
	JTextField tfDepartment=null;
	JTextField tfAddress=null;
	
	JButton btnInsert=null;
	JButton btnSelect=null;
	JButton btnUpdate=null;
	JButton btnDelete=null;	
	JButton btnSearch=null;
	
	DefaultTableModel model=null;
	JTable table=null;
	
	Connection conn=Haksa.conn;
	Statement stmt=Haksa.stmt;
	ResultSet rs=Haksa.rs;
	
	public Student() {			
		
		setLayout(new FlowLayout());
		
		JLabel lblId=new JLabel("학번");
		add(lblId);
		tfId=new JTextField(14);
		add(tfId);
		
		btnSearch=new JButton("검색");
		add(btnSearch);
		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {					
					rs=stmt.executeQuery("select * from student2 where id='"+tfId.getText()+"'");
					
					model.setNumRows(0); //초기화
					if(rs.next()) {
						tfId.setText(rs.getString("id"));
						tfName.setText(rs.getString("name"));
						tfDepartment.setText(rs.getString("dept"));
						
						String[] row=new String[4];
						row[0]=rs.getString("id");
						row[1]=rs.getString("name");
						row[2]=rs.getString("dept");
						
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
		add(lblName);
		tfName=new JTextField(20);
		add(tfName);
		
		JLabel lblDepartment=new JLabel("학과");
		add(lblDepartment);
		tfDepartment=new JTextField(20);
		add(tfDepartment);
		
		JLabel lblAddress=new JLabel("주소");
		add(lblAddress);
		tfAddress=new JTextField(20);
		add(tfAddress);
				
		String[] colName = {"학번", "이름", "학과"};
		model = new DefaultTableModel(colName, 0);
		table = new JTable(model);
		table.setPreferredScrollableViewportSize(new Dimension(250, 200));
		
		add(new JScrollPane(table));
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				table = (JTable)e.getComponent();
				model = (DefaultTableModel)table.getModel();
				tfId.setText((String)model.getValueAt(table.getSelectedRow(), 0));
				tfName.setText((String)model.getValueAt(table.getSelectedRow(), 1));
				tfDepartment.setText((String)model.getValueAt(table.getSelectedRow(), 2));
			}
			
		});
		
		
		btnInsert=new JButton("등록");
		btnInsert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				try {				
					stmt.executeUpdate("insert into student2 values('"+tfId.getText()+"','"+tfName.getText()+"','"+tfDepartment.getText()+"')");
										
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
					stmt.executeUpdate("update student2 set name='"+tfName.getText()+"',dept='"+tfDepartment.getText()+"' where id='"+tfId.getText()+"'");
										
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
						stmt.executeUpdate("delete from student2 where id='"+tfId.getText()+"'");
						tfId.setText("");
						tfName.setText("");
						tfDepartment.setText("");
											
						list();
						
					}catch(Exception e1) {
						e1.printStackTrace();
					}
				}
				
			}});
		add(btnInsert);
		add(btnSelect);
		add(btnUpdate);
		add(btnDelete);
		
		this.setSize(285, 430);
		this.setVisible(true);
	}

	//목록
	public void list() {		
		try {
			rs=stmt.executeQuery("select * from student2");
			model.setNumRows(0); //초기화
			while(rs.next()) {
				String[] row=new String[4];
				row[0]=rs.getString("id");
				row[1]=rs.getString("name");
				row[2]=rs.getString("dept");

				model.addRow(row);
			}				
		}catch(Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	

}
