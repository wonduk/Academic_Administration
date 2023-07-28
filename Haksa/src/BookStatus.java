import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
//대출현황 패널*****************************************
public class BookStatus extends JPanel {
	DefaultTableModel model=null;
	JTable table=null;	
	
	Connection conn=Haksa.conn;
	Statement stmt=Haksa.stmt;
	ResultSet rs =Haksa.rs;
	
	
	String query; //sql문
	
	public BookStatus() {
		query=" select b.bookNo, b.title, nvl(br.rDate,'미대출') as rDate, br.rDate+7 as lDate "
				+ " from bookRent br,books b "
				+ " where  br.bookNo(+) = b.bookno ";	
		
	
	    setLayout(null);//레이아웃설정. 레이아웃 사용 안함.
	    
	    JLabel l_dept=new JLabel("대출현황");
	    l_dept.setBounds(10, 10, 60, 20);
	    add(l_dept);
	    
	    String[] dept={"전체","대출중","미대출"};
	    JComboBox cb_dept=new JComboBox(dept);
	    cb_dept.setBounds(70, 10, 100, 20);
	    add(cb_dept);
	    cb_dept.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) { 
				query="select b.bookNo, b.title, nvl(br.rDate,'미대출') as rDate, br.rDate+7 as lDate"
						+ " from bookRent br,books b "
						+ " where  br.bookNo(+) = b.bookno ";
				
				JComboBox cb=(JComboBox)e.getSource(); //이벤트가 발생한 콤보박스 구하기
				//동적쿼리
				if(cb.getSelectedIndex()==0) {
					//전체
					query+=" order by b.bookno asc, br.rdate asc ";
				}else if(cb.getSelectedIndex()==1) {
					//대출중
					query+=" and br.rdate is not null order by b.bookno asc, br.rdate asc";
				}else if(cb.getSelectedIndex()==2) {
					//미대출
					query+=" and br.rdate is null order by b.bookno asc, br.rdate asc";
				}
				
				//목록출력
				list();
				
				
			}});
	    
	    
	    String colName[]={"책번호","책제목","대출일","반납일",};
	    model=new DefaultTableModel(colName,0);
	    table = new JTable(model);
	    table.setPreferredScrollableViewportSize(new Dimension(470,200));
	    add(table);
	    JScrollPane sp=new JScrollPane(table);
	    sp.setBounds(10, 40, 460, 250);
	    add(sp);  
	    
	    
	    
	    setSize(490, 400);//화면크기
	    setVisible(true);
	}

	public void list(){
	    try{
//		     System.out.println("연결되었습니다.....");
//		     System.out.println(model);  //디버깅. 쿼리문을 출력.     
		     // Select문 실행     
		     rs=stmt.executeQuery(query);
		    
	     //JTable 초기화
	     model.setNumRows(0);
	    
	     while(rs.next()){
	      String[] row=new String[4];//컬럼의 갯수가 4
	      row[0]=rs.getString("bookNo");
	      row[1]=rs.getString("title");
	      row[2]=rs.getString("rdate");
	      row[3]=rs.getString("lDate");
	      model.addRow(row);
	     }
	    }
	    catch(Exception e1){
	     //e.getStackTrace();
	     System.out.println(e1.getMessage());
////	    }finally {
////	    	try {
////	    		rs.close();
////	    	}catch(Exception e) {
////	    		e.printStackTrace();
////	    	}	    	
	    }							
	 }
}