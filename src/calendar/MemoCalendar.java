
package calendar;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
//import static java.lang.Thread.sleep;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.ImageIcon;
import javax.swing.JLabel;


class CalendarDataManager{ 
	static final int CAL_WIDTH = 7;
	final static int CAL_HEIGHT = 6;
	int calDates[][] = new int[CAL_HEIGHT][CAL_WIDTH];
	int calYear;
	int calMonth;
	int calDayOfMon;
	final int calLastDateOfMonth[]={31,28,31,30,31,30,31,31,30,31,30,31};
	int calLastDate;
	Calendar today = Calendar.getInstance();
	Calendar cal;
        
	
	public CalendarDataManager(){ 
		setToday(); 
	}
	public void setToday(){
		calYear = today.get(Calendar.YEAR); 
		calMonth = today.get(Calendar.MONTH);
		calDayOfMon = today.get(Calendar.DAY_OF_MONTH);
		makeCalData(today);
	}
	private void makeCalData(Calendar cal){
	
		int calStartingPos = (cal.get(Calendar.DAY_OF_WEEK)+7-(cal.get(Calendar.DAY_OF_MONTH))%7)%7;
		if(calMonth == 1) calLastDate = calLastDateOfMonth[calMonth] + leapCheck(calYear);
		else calLastDate = calLastDateOfMonth[calMonth];

		for(int i = 0 ; i<CAL_HEIGHT ; i++){
			for(int j = 0 ; j<CAL_WIDTH ; j++){
				calDates[i][j] = 0;
			}
		}
		for(int i = 0, num = 1, k = 0 ; i<CAL_HEIGHT ; i++){
			if(i == 0) k = calStartingPos;
			else k = 0;
			for(int j = k ; j<CAL_WIDTH ; j++){
				if(num <= calLastDate) calDates[i][j]=num++;
			}
		}
	}
	private int leapCheck(int year){
		if(year%4 == 0 && year%100 != 0 || year%400 == 0) return 1;
		else return 0;
	}
	public void moveMonth(int mon){ 
		calMonth += mon;
		if(calMonth>11) while(calMonth>11){
			calYear++;
			calMonth -= 12;
		} else if (calMonth<0) while(calMonth<0){
			calYear--;
			calMonth += 12;
		}
		cal = new GregorianCalendar(calYear,calMonth,calDayOfMon);
		makeCalData(cal);
	}
}

public class MemoCalendar extends CalendarDataManager{ 
	
	JFrame mainFrame;
		ImageIcon icon = new ImageIcon ( Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));
//------------------------------------------
		ImageIcon good_icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/good_icon.png")));
		ImageIcon soso_icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/soso_icon.png")));
		ImageIcon sad_icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sad_icon.png")));
		ImageIcon bed_icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/bed_icon.png")));
	
		Image good_icon_img = good_icon.getImage().getScaledInstance(40,  40, Image.SCALE_SMOOTH);
		ImageIcon good_icon_change = new ImageIcon(good_icon_img);
		
		Image soso_icon_img = soso_icon.getImage().getScaledInstance(40,  40, Image.SCALE_SMOOTH);
		ImageIcon soso_icon_change = new ImageIcon(soso_icon_img);
		
		Image sad_icon_img = sad_icon.getImage().getScaledInstance(40,  40, Image.SCALE_SMOOTH);
		ImageIcon sad_icon_change = new ImageIcon(sad_icon_img);
		
		Image bed_icon_img = bed_icon.getImage().getScaledInstance(40,  40, Image.SCALE_SMOOTH);
		ImageIcon bed_icon_change = new ImageIcon(bed_icon_img);
		
		JButton good_icon_btn = new JButton(good_icon_change);
		JButton soso_icon_btn = new JButton(soso_icon_change);
		JButton sad_icon_btn = new JButton(sad_icon_change);
		JButton bed_icon_btn = new JButton(bed_icon_change);
//------------------------------------------
		
		
		
		
	JPanel calOpPanel;
		JButton todayBut;
		JLabel todayLab;
		JButton lYearBut;
		JButton lMonBut;
		JLabel curMMYYYYLab;
		JButton nMonBut;
		JButton nYearBut;
		ListenForCalOpButtons lForCalOpButtons = new ListenForCalOpButtons();
	
	JPanel calPanel;
		JButton weekDaysName[];
		JButton dateButs[][] = new JButton[6][7];
		listenForDateButs lForDateButs = new listenForDateButs(); 
	
	JPanel infoPanel;
		JLabel infoClock;
	
	JPanel memoPanel;
		JLabel selectedDate;
		JTextArea memoArea;
		JScrollPane memoAreaSP;
		JPanel memoSubPanel;
		JPanel memoSub2Panel;
		JButton saveBut; 
		JButton delBut; 
		JButton clearBut;
	
	JPanel frameBottomPanel;
		JLabel bottomInfo = new JLabel("Welcome to Memo Calendar!");


               
                
	// 요일 이름 배열
	final String WEEK_DAY_NAME[] = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
	final String title = "메모 캘린더 ver 1.0";
	final String SaveButMsg1 = "메모 데이터를 저장했습니다.";
	final String SaveButMsg2 = "메모를 입력해주세요.";
	final String SaveButMsg3 = "<html><font color=red>ERROR : 저장 중 오류 발생 </html>";
	final String DelButMsg1 = "메모를 삭제했습니다.";
	final String DelButMsg2 = "저장된 메모가 없어서 삭제할 메모가 없습니다.";
	final String DelButMsg3 = "<html><font color=red>ERROR : 삭제 중 오류 발생 </html>";
	final String ClrButMsg1 = "입력한 메모를 삭제했습니다.";

	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				new MemoCalendar();
			}
                        
		});
	}
	public MemoCalendar(){    // MemoCalendar 클래스의 생성자

		mainFrame = new JFrame(title);    // mainFrame 변수에 새로운 JFrame 인스턴스를 생성하여 할당
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // 프레임이 닫힐 때 프로그램 종료 설정
		mainFrame.setSize(1200,700);    // 프레임의 크기 설정
		mainFrame.setLocationRelativeTo(null);    // 프레임을 화면 중앙에 위치시키기
		mainFrame.setResizable(false);    // 프레임의 크기 조정 불가능하도록 설정
		mainFrame.setIconImage(icon.getImage());    // 프레임 아이콘 설정
                

	try {
            UIManager.put("Panel.background", new ColorUIResource(252, 252, 239)); // 패널의 배경색 변경
            UIManager.put("OptionPane.background", new ColorUIResource(252, 252, 239)); // 옵션 패널의 배경색 변경
            UIManager.put("TextField.background", new ColorUIResource(252, 252, 239)); // 텍스트 필드의 배경색 변경
            // 원하는 컴포넌트의 배경색을 변경하고 싶은 경우 해당 컴포넌트의 키를 사용하여 배경색을 설정.
            // UIManager.put("Button.background", new ColorUIResource(252, 252, 239)); // 버튼의 배경색 변경
             UIManager.put("Button.focus", new ColorUIResource(255, 255, 0)); // 클릭 포커스 테두리 색상 설정
             UIManager.put("TextField.focus", new ColorUIResource(255, 255, 0)); // 클릭 포커스 테두리 색상 설정


//    SwingUtilities.updateComponentTreeUI(mainFrame); // UI 업데이트
} catch (Exception e) {
    bottomInfo.setText("ERROR: Failed to set background color");
}

                
		calOpPanel = new JPanel();
			todayBut = new JButton("Today");
			todayBut.setToolTipText("Today");
			todayBut.addActionListener(lForCalOpButtons);
			todayLab = new JLabel(today.get(Calendar.MONTH)+1+"월 "+today.get(Calendar.DAY_OF_MONTH)+"일 "+today.get(Calendar.YEAR)+"년");
			lYearBut = new JButton("<<");
			lYearBut.setToolTipText("Previous Year");
			lYearBut.addActionListener(lForCalOpButtons);
			lMonBut = new JButton("<");
			lMonBut.setToolTipText("Previous Month");
			lMonBut.addActionListener(lForCalOpButtons);
			curMMYYYYLab = new JLabel("<html><table width=150><tr><th><font size=5>"+((calMonth+1)<10?"&nbsp;":"")+(calMonth+1)+"월 "+calYear+"년"+"</th></tr></table></html>"); // 달력 가운데 월, 
			nMonBut = new JButton(">");
			nMonBut.setToolTipText("Next Month");
			nMonBut.addActionListener(lForCalOpButtons);
			nYearBut = new JButton(">>");
			nYearBut.setToolTipText("Next Year");
			nYearBut.addActionListener(lForCalOpButtons);
			calOpPanel.setLayout(new GridBagLayout());
			GridBagConstraints calOpGC = new GridBagConstraints();

                        
			calOpGC.gridx = 1;
			calOpGC.gridy = 1;
			calOpGC.gridwidth = 2;
			calOpGC.gridheight = 1;
			calOpGC.weightx = 1;
			calOpGC.weighty = 1;
			calOpGC.insets = new Insets(5,5,0,0);
			calOpGC.anchor = GridBagConstraints.WEST;
			calOpGC.fill = GridBagConstraints.NONE;
			calOpPanel.add(todayBut,calOpGC);
			calOpGC.gridwidth = 3;
			calOpGC.gridx = 2;
			calOpGC.gridy = 1;
			calOpPanel.add(todayLab,calOpGC);
			calOpGC.anchor = GridBagConstraints.CENTER;
			calOpGC.gridwidth = 1;
			calOpGC.gridx = 1;
			calOpGC.gridy = 2;
			calOpPanel.add(lYearBut,calOpGC);
			calOpGC.gridwidth = 1;
			calOpGC.gridx = 2;
			calOpGC.gridy = 2;
			calOpPanel.add(lMonBut,calOpGC);
			calOpGC.gridwidth = 2;
			calOpGC.gridx = 3;
			calOpGC.gridy = 2;
			calOpPanel.add(curMMYYYYLab,calOpGC);
			calOpGC.gridwidth = 1;
			calOpGC.gridx = 5;
			calOpGC.gridy = 2;
			calOpPanel.add(nMonBut,calOpGC);
			calOpGC.gridwidth = 1;
			calOpGC.gridx = 6;
			calOpGC.gridy = 2;
			calOpPanel.add(nYearBut,calOpGC);
		
		calPanel=new JPanel();
			weekDaysName = new JButton[7];
			for(int i=0 ; i<CAL_WIDTH ; i++){
				weekDaysName[i]=new JButton(WEEK_DAY_NAME[i]);
				weekDaysName[i].setBorderPainted(false);
				weekDaysName[i].setContentAreaFilled(false);
				weekDaysName[i].setForeground(Color.WHITE);
				if(i == 0) weekDaysName[i].setBackground(new Color(200, 50, 50));
				else if (i == 6) weekDaysName[i].setBackground(new Color(50, 100, 200));
				else weekDaysName[i].setBackground(new Color(150, 150, 150));
				weekDaysName[i].setOpaque(true);
				weekDaysName[i].setFocusPainted(false);
				calPanel.add(weekDaysName[i]);
                                
			}
			for(int i=0 ; i<CAL_HEIGHT ; i++){
				for(int j=0 ; j<CAL_WIDTH ; j++){
                                    
					dateButs[i][j]=new JButton();
                                        
                                        // 날짜 부분의 텍스트를 왼쪽 위 모서리에 배치
                                        dateButs[i][j].setHorizontalAlignment(SwingConstants.LEFT);
                                        dateButs[i][j].setVerticalAlignment(SwingConstants.TOP);
                                        
                                        // 여백 크기 조정
					dateButs[i][j].setBorderPainted(true); // 날짜 부분 테두리
                                      dateButs[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
					dateButs[i][j].setContentAreaFilled(true);
					dateButs[i][j].setBackground(Color.WHITE);
					dateButs[i][j].setOpaque(true);
					dateButs[i][j].addActionListener(lForDateButs);
                                        dateButs[i][j].setFocusPainted(true);


                                        
					calPanel.add(dateButs[i][j]);
				}
			}
                        
			calPanel.setLayout(new GridLayout(0,7,2,2));
			calPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
                        calPanel.setBackground(new Color(252, 252, 239));

               

			showCal(); 
						
		infoPanel = new JPanel();
			infoPanel.setLayout(new BorderLayout());
			infoClock = new JLabel("", SwingConstants.RIGHT);
			infoClock.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 1));
			infoPanel.add(infoClock, BorderLayout.NORTH);
			selectedDate = new JLabel("<Html><font size=5>"+(today.get(Calendar.MONTH)+1)+"월 "+today.get(Calendar.DAY_OF_MONTH)+"일 "+today.get(Calendar.YEAR)+"&nbsp;(Today)</html>", SwingConstants.LEFT);
			selectedDate.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
						
		memoPanel=new JPanel();
			memoPanel.setBorder(BorderFactory.createTitledBorder("Memo"));
			memoArea = new JTextArea();
			memoArea.setLineWrap(true);
			memoArea.setWrapStyleWord(true);
			memoAreaSP = new JScrollPane(memoArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			readMemo();
			
			memoSubPanel=new JPanel();
			memoSub2Panel=new JPanel();
			saveBut = new JButton("저장"); 
			saveBut.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					try {
						File f= new File("MemoData");
						if(!f.isDirectory()) f.mkdir();
						
						String memo = memoArea.getText();
						if(memo.length()>0){
							BufferedWriter out = new BufferedWriter(new FileWriter("MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon+".txt"));
							String str = memoArea.getText();
							out.write(str);  
							out.close();
							bottomInfo.setText(calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon+".txt"+SaveButMsg1);
						}
						else 
							bottomInfo.setText(SaveButMsg2);
					} catch (IOException e) {
						bottomInfo.setText(SaveButMsg3);
					}
					showCal();
				}					
			});
			delBut = new JButton("삭제");
			delBut.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					memoArea.setText("");
					File f =new File("MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon+".txt");
					if(f.exists()){
						f.delete();
						showCal();
						bottomInfo.setText(DelButMsg1);
					}
					else 
						bottomInfo.setText(DelButMsg2);					
				}					
			});
			clearBut = new JButton("지우기");
			clearBut.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					memoArea.setText(null);
					bottomInfo.setText(ClrButMsg1);
				}
			});
			memoSubPanel.add(saveBut);
			memoSubPanel.add(delBut);
			memoSubPanel.add(clearBut);
			
			good_icon_btn.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					memoArea.setText("good");
				}
			});
//---------------------------------------------------------
			good_icon_btn.setBorderPainted(false);
			good_icon_btn.setContentAreaFilled(false);
			soso_icon_btn.setBorderPainted(false);
			soso_icon_btn.setContentAreaFilled(false);
			sad_icon_btn.setBorderPainted(false);
			sad_icon_btn.setContentAreaFilled(false);
			bed_icon_btn.setBorderPainted(false);
			bed_icon_btn.setContentAreaFilled(false);
			
			memoSub2Panel.add(good_icon_btn);		
			memoSub2Panel.add(soso_icon_btn);
			memoSub2Panel.add(sad_icon_btn);
			memoSub2Panel.add(bed_icon_btn);
			
			memoPanel.setLayout(new BorderLayout());
			
			memoPanel.add(selectedDate, BorderLayout.NORTH);
			memoPanel.add(memoAreaSP,BorderLayout.CENTER);
			memoPanel.add(memoSubPanel,BorderLayout.SOUTH);
			memoSubPanel.add(memoSub2Panel, BorderLayout.SOUTH);
			
                        memoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 30));
//---------------------------------------------------------
                        
                        


		JPanel frameSubPanelWest = new JPanel();
		Dimension calOpPanelSize = calOpPanel.getPreferredSize();
		calOpPanelSize.height = 90;
		calOpPanel.setPreferredSize(calOpPanelSize);
		frameSubPanelWest.setLayout(new BorderLayout());
		frameSubPanelWest.add(calOpPanel,BorderLayout.NORTH);
		frameSubPanelWest.add(calPanel,BorderLayout.CENTER);

	
		JPanel frameSubPanelEast = new JPanel();
		Dimension infoPanelSize=infoPanel.getPreferredSize();
		infoPanelSize.height = 65;
		infoPanel.setPreferredSize(infoPanelSize);
		frameSubPanelEast.setLayout(new BorderLayout());
		frameSubPanelEast.add(infoPanel,BorderLayout.NORTH);
		frameSubPanelEast.add(memoPanel,BorderLayout.CENTER);

		Dimension frameSubPanelWestSize = frameSubPanelWest.getPreferredSize();
		frameSubPanelWestSize.width = 810; // 달력 크기
		frameSubPanelWest.setPreferredSize(frameSubPanelWestSize);


                frameBottomPanel = new JPanel();
		frameBottomPanel.add(bottomInfo);
		
                
		mainFrame.setLayout(new BorderLayout());
                mainFrame.setBackground(Color.black);
		mainFrame.add(frameSubPanelWest, BorderLayout.WEST);
		mainFrame.add(frameSubPanelEast, BorderLayout.CENTER);
		mainFrame.add(frameBottomPanel, BorderLayout.SOUTH);
		mainFrame.setVisible(true);


		focusToday(); 
		

		ThreadConrol threadCnl = new ThreadConrol();
		threadCnl.start();	
	}
	private void focusToday(){
		if(today.get(Calendar.DAY_OF_WEEK) == 1)
			dateButs[today.get(Calendar.WEEK_OF_MONTH)][today.get(Calendar.DAY_OF_WEEK)-1].requestFocusInWindow();
		else
			dateButs[today.get(Calendar.WEEK_OF_MONTH)-1][today.get(Calendar.DAY_OF_WEEK)-1].requestFocusInWindow();
	}
	
	private void readMemo(){
		try{
			File f = new File("MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon+".txt");
			if(f.exists()){
				BufferedReader in = new BufferedReader(new FileReader("MemoData/"+calYear+((calMonth+1)<10?"0":"")+(calMonth+1)+(calDayOfMon<10?"0":"")+calDayOfMon+".txt"));
				String memoAreaText= new String();
				while(true){
					String tempStr = in.readLine();
					if(tempStr == null) break;
					memoAreaText = memoAreaText + tempStr + System.getProperty("line.separator");
				}
				memoArea.setText(memoAreaText);
				in.close();	
			}
			else memoArea.setText("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void showCal() {
	    for (int i = 0; i < CAL_HEIGHT; i++) {
	        for (int j = 0; j < CAL_WIDTH; j++) {
	            String fontColor = "black";
	            if (j == 0)
	                fontColor = "red";
	            else if (j == 6)
	                fontColor = "blue";

	            File f = new File("MemoData/" + calYear + ((calMonth + 1) < 10 ? "0" : "") + (calMonth + 1) + (calDates[i][j] < 10 ? "0" : "") + calDates[i][j] + ".txt");
	            if (f.exists()) {
	                dateButs[i][j].setText("<html><b><font color=" + fontColor + ">" + calDates[i][j] + "</font></b></html>");
	            } else {
	                dateButs[i][j].setText("<html><font color=" + fontColor + ">" + calDates[i][j] + "</font></html>");
	            }
	            
	            JLabel todayMark = new JLabel("<html><font color=red>Today</html>");
	            todayMark.setHorizontalAlignment(SwingConstants.CENTER);
	            //                   JPanel todayMark = new JPanel();
	            //                   todayMark.setBackground(new Color(255, 255, 0, 128)); // 노란색 반투명 배경 설정
	            //                   todayMark.setPreferredSize(new Dimension(20, 20)); // 네모의 크기 설정

	            dateButs[i][j].removeAll();
	            if (calMonth == today.get(Calendar.MONTH) && calYear == today.get(Calendar.YEAR) && calDates[i][j] == today.get(Calendar.DAY_OF_MONTH)) {
	                dateButs[i][j].setLayout(new BorderLayout()); // 날짜 버튼의 레이아웃을 BorderLayout으로 설정
	                dateButs[i][j].add(todayMark, BorderLayout.NORTH); // 'todayMark' 라벨을 날짜 버튼의 상단에 추가
	                dateButs[i][j].setToolTipText("Today");
	            } else {

	            }

	            if (calDates[i][j] == 0)
	                dateButs[i][j].setVisible(false);
	            else
	                dateButs[i][j].setVisible(true);
	        }
	    }
	}
	
	private class ListenForCalOpButtons implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == todayBut){
				setToday();
				lForDateButs.actionPerformed(e);
				focusToday();
			}
			else if(e.getSource() == lYearBut) moveMonth(-12);
			else if(e.getSource() == lMonBut) moveMonth(-1);
			else if(e.getSource() == nMonBut) moveMonth(1);
			else if(e.getSource() == nYearBut) moveMonth(12);
			
			curMMYYYYLab.setText("<html><table width=100><tr><th><font size=5>"+((calMonth+1)<10?"&nbsp;":"")+(calMonth+1)+" / "+calYear+"</th></tr></table></html>");
			showCal();
		}
	}
	private class listenForDateButs implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int k=0,l=0;
			for(int i=0 ; i<CAL_HEIGHT ; i++){
				for(int j=0 ; j<CAL_WIDTH ; j++){
					if(e.getSource() == dateButs[i][j]){ 
						k=i;
						l=j;
					}
				}
			}
	
			if(!(k ==0 && l == 0)) calDayOfMon = calDates[k][l]; 

			cal = new GregorianCalendar(calYear,calMonth,calDayOfMon);
			
			String dDayString = new String();
			int dDay=((int)((cal.getTimeInMillis() - today.getTimeInMillis())/1000/60/60/24));
			if(dDay == 0 && (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR)) 
					&& (cal.get(Calendar.MONTH) == today.get(Calendar.MONTH))
					&& (cal.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH))) dDayString = "Today"; 
			else if(dDay >=0) dDayString = "D-"+(dDay+1);
			else if(dDay < 0) dDayString = "D+"+(dDay)*(-1);
			
			selectedDate.setText("<Html><font size=5>"+(calMonth+1)+"월 "+calDayOfMon+"일 "+calYear+"년"+"&nbsp;("+dDayString+")</html>");
			
			readMemo();
		}
	}
	private class ThreadConrol extends Thread{
		public void run(){
			boolean msgCntFlag = false;
			int num = 0;
			String curStr = new String();
			while(true){
				try{
					today = Calendar.getInstance();
					String amPm = (today.get(Calendar.AM_PM)==0?"AM":"PM");
					String hour;
							if(today.get(Calendar.HOUR) == 0) hour = "12"; 
							else if(today.get(Calendar.HOUR) == 12) hour = " 0";
							else hour = (today.get(Calendar.HOUR)<10?" ":"")+today.get(Calendar.HOUR);
					String min = (today.get(Calendar.MINUTE)<10?"0":"")+today.get(Calendar.MINUTE);
					String sec = (today.get(Calendar.SECOND)<10?"0":"")+today.get(Calendar.SECOND);
					infoClock.setText(amPm+" "+hour+":"+min+":"+sec);

					sleep(1000);
					String infoStr = bottomInfo.getText();
					
					if(infoStr != " " && (msgCntFlag == false || curStr != infoStr)){
						num = 5;
						msgCntFlag = true;
						curStr = infoStr;
					}
					else if(infoStr != " " && msgCntFlag == true){
						if(num > 0) num--;
						else{
							msgCntFlag = false;
							bottomInfo.setText(" ");
						}
					}		
				}
				catch(InterruptedException e){
					System.out.println("Thread:Error");
				}
			}
		}
	}
}