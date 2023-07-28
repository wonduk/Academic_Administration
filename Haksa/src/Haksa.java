import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
//메인 패널*************************************
public class Haksa extends JFrame{
	JPanel panel; // 메뉴별 화면이 출력되는 패널
	
	static Connection conn=null;
	static Statement stmt=null;
	static ResultSet rs=null;
	boolean pageAppearing = false;
	
	public Haksa() {
		this.setTitle("학사관리");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		
		//db connection
		try {
			//DB연결
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// oracle 11g enterprise edtion 의 기본 sid명은 orcl
			// oracle 11g express edtion 의 기본 sid명은 xe
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "ora_user", "hong");				
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
		
		
		
		JMenuBar bar=new JMenuBar();
		//학생관리 네비탭 추가
		JMenu m_student=new JMenu("학생관리");
		bar.add(m_student);
		JMenuItem mi_list=new JMenuItem("학생정보");
		m_student.add(mi_list);
		mi_list.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel.removeAll(); // 모든컴포넌트삭제
				panel.revalidate(); // 다시활성화
				panel.repaint(); // 다시그림
				panel.add(new Student()); // 학생정보패널을 생성.추가.
				panel.setLayout(null); // 레이아웃은 사용 안함				
			}			
		});
		
		
				//도서관리네비
		JMenu m_book=new JMenu("도서관리");
		bar.add(m_book);
		//대출목록 네비탭 추가
		JMenuItem mi_bookRent=new JMenuItem("대출목록");
		m_book.add(mi_bookRent);
		mi_bookRent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.removeAll(); // 모든컴포넌트삭제
				panel.revalidate(); // 다시활성화
				panel.repaint(); // 다시그림
				panel.add(new BookRent()); // 대출목록패널을 생성.추가.
				panel.setLayout(null); // 레이아웃은 사용 안함	
				
			}});
		//대출현황목록 네비
		JMenuItem mi_bookLoanStatus=new JMenuItem("대출현황");
		m_book.add(mi_bookLoanStatus);
		mi_bookLoanStatus.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.removeAll(); // 모든컴포넌트삭제
				panel.revalidate(); // 다시활성화
				panel.repaint(); // 다시그림
				panel.add(new BookStatus()); // 대출목록패널을 생성.추가.
				panel.setLayout(null); // 레이아웃은 사용 안함	
			}
		});
		
		
		this.setJMenuBar(bar);
		
		
		panel=new JPanel();
		this.add(panel);
		
		
		this.setSize(500, 500);
		this.setVisible(false);
	}

//	public static void main(String[] args) {
//		new Haksa();
//
//	}

}
