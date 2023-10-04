/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package fees_management_system;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.Date;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author hp
 */
public class GenerateReport extends javax.swing.JFrame {

    /**
     * Creates new form GenerateReport
     */
    DefaultTableModel model;
    public GenerateReport() {
        initComponents();
        scrl_pane.getVerticalScrollBar().setUnitIncrement(20);
        fillComboBox();
    }
    
    public void fillComboBox(){
        try{
            Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement("select course_name from course");
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                combo_course.addItem(rs.getString("course_name"));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void clearTable(){
        DefaultTableModel model=(DefaultTableModel)tbl_feesdetails.getModel();
        model.setRowCount(1);
    }
    
    public void setRecordsToTable(){
        String courseName=combo_course.getSelectedItem().toString();
        
        SimpleDateFormat dateFormat=new SimpleDateFormat("YYYY-MM-dd");
        String fromDate=dateFormat.format(date_fromdate.getDate());
        String toDate=dateFormat.format(date_todate.getDate());
        
        Float totalAmount=0.0f;
        try{
            Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement("select * from fees_details where date between ? and ? and course_name=?");
            ps.setString(1, fromDate);
            ps.setString(2, toDate);
            ps.setString(3, courseName);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                String receiptNo=rs.getString("receipt_no");
                String rollNo=rs.getString("roll_no");
                String studentName=rs.getString("student_name");
                String course=rs.getString("course_name");
                float amount=rs.getFloat("total_amount");
                String remark=rs.getString("remark");
                
                totalAmount=totalAmount+amount;
                
                Object []obj={receiptNo,rollNo,studentName,course,amount,remark};
                model=(DefaultTableModel)tbl_feesdetails.getModel();
                model.addRow(obj);
            }
            lbl_course.setText(courseName);
            lbl_totalamount.setText(totalAmount.toString());
            lbl_amountinwords.setText(NumberToWordsConverter.convert(totalAmount.intValue()));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void exportToExcel(){
        XSSFWorkbook workbook=new XSSFWorkbook();
        XSSFSheet worksheet=workbook.createSheet();
        DefaultTableModel model=(DefaultTableModel)tbl_feesdetails.getModel();
        
        TreeMap<String,Object[]> map=new TreeMap<>();
        map.put("0", new Object[]{model.getColumnName(0),model.getColumnName(1),model.getColumnName(2),model.getColumnName(3),model.getColumnName(4),model.getColumnName(5)});
        
//        for(Map.Entry<String, Object[]> entry : map.entrySet()){
//            String key=entry.getKey();
//            Object[] value=entry.getValue();
//            System.out.println(Arrays.toString(value));    
//        }
         

        for(int i=1;i<model.getRowCount();i++){
            map.put(Integer.toString(i), new Object[]{model.getValueAt(i,0),model.getValueAt(i,1),model.getValueAt(i,2),model.getValueAt(i,3),model.getValueAt(i,4),model.getValueAt(i,5)});
        }
        
        Set<String> id=map.keySet();
        XSSFRow fRow;
        int rowId=0;
        for(String key : id){
            fRow=worksheet.createRow(rowId++);
            Object[] value=map.get(key);
            int cellId=0;
            for(Object object : value){
                XSSFCell cell=fRow.createCell(cellId++);
                cell.setCellValue(object.toString());
            }
        }
        try{
            FileOutputStream fos=new FileOutputStream(new File(txt_filepath.getText()));
            workbook.write(fos);
            fos.close();
            JOptionPane.showMessageDialog(this, "File Exported Successfully: "+txt_filepath.getText());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        //this is (try with resource) in which we don't need to close fos it will automatically get closeed
//        try(FileOutputStream fos=new FileOutputStream(new File(txt_filepath.getText()))){
//            workbook.write(fos);
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrl_pane = new javax.swing.JScrollPane();
        panel_main = new javax.swing.JPanel();
        panel_sidebar = new javax.swing.JPanel();
        panel_Logout = new javax.swing.JPanel();
        btn_Logout = new javax.swing.JLabel();
        panel_Home = new javax.swing.JPanel();
        btn_Home = new javax.swing.JLabel();
        panel_SearchRecord = new javax.swing.JPanel();
        btn_SearchRecord = new javax.swing.JLabel();
        panel_EditCourses = new javax.swing.JPanel();
        btn_EditCourses = new javax.swing.JLabel();
        panel_CheckBalance = new javax.swing.JPanel();
        btn_CheckBalance = new javax.swing.JLabel();
        panel_ViewAllRecord = new javax.swing.JPanel();
        btn_ViewAllRecord = new javax.swing.JLabel();
        panel_Back = new javax.swing.JPanel();
        btn_Back = new javax.swing.JLabel();
        panel_parent = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        combo_course = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        txt_filepath = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_feesdetails = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        lbl_amountinwords = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lbl_course = new javax.swing.JLabel();
        lbl_totalamount = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        date_fromdate = new com.toedter.calendar.JDateChooser();
        date_todate = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Anudip Foundation");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        scrl_pane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrl_pane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        panel_main.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel_sidebar.setBackground(new java.awt.Color(255, 0, 51));
        panel_sidebar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panel_Logout.setBackground(new java.awt.Color(255, 0, 51));
        panel_Logout.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.white, null, null));
        panel_Logout.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_Logout.setFont(new java.awt.Font("MS UI Gothic", 0, 36)); // NOI18N
        btn_Logout.setForeground(new java.awt.Color(255, 255, 255));
        btn_Logout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/Fees_Management_System_images/logout.png"))); // NOI18N
        btn_Logout.setText(" Logout");
        btn_Logout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_LogoutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_LogoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_LogoutMouseExited(evt);
            }
        });
        panel_Logout.add(btn_Logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 310, 70));

        panel_sidebar.add(panel_Logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 630, 330, 70));

        panel_Home.setBackground(new java.awt.Color(255, 0, 51));
        panel_Home.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.white, null, null));
        panel_Home.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_Home.setFont(new java.awt.Font("MS UI Gothic", 0, 36)); // NOI18N
        btn_Home.setForeground(new java.awt.Color(255, 255, 255));
        btn_Home.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/Fees_Management_System_images/home.png"))); // NOI18N
        btn_Home.setText(" Home");
        btn_Home.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_HomeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_HomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_HomeMouseExited(evt);
            }
        });
        panel_Home.add(btn_Home, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 310, 60));

        panel_sidebar.add(panel_Home, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 330, 70));

        panel_SearchRecord.setBackground(new java.awt.Color(255, 0, 51));
        panel_SearchRecord.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.white, null, null));
        panel_SearchRecord.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_SearchRecord.setFont(new java.awt.Font("MS UI Gothic", 0, 36)); // NOI18N
        btn_SearchRecord.setForeground(new java.awt.Color(255, 255, 255));
        btn_SearchRecord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/Fees_Management_System_images/search2.png"))); // NOI18N
        btn_SearchRecord.setText("Search Record");
        btn_SearchRecord.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_SearchRecord.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_SearchRecordMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_SearchRecordMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_SearchRecordMouseExited(evt);
            }
        });
        panel_SearchRecord.add(btn_SearchRecord, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 6, 310, -1));

        panel_sidebar.add(panel_SearchRecord, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 330, 70));

        panel_EditCourses.setBackground(new java.awt.Color(255, 0, 51));
        panel_EditCourses.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.white, null, null));
        panel_EditCourses.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_EditCourses.setFont(new java.awt.Font("MS UI Gothic", 0, 36)); // NOI18N
        btn_EditCourses.setForeground(new java.awt.Color(255, 255, 255));
        btn_EditCourses.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/Fees_Management_System_images/edit2.png"))); // NOI18N
        btn_EditCourses.setText(" Edit Courses");
        btn_EditCourses.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_EditCourses.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_EditCoursesMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_EditCoursesMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_EditCoursesMouseExited(evt);
            }
        });
        panel_EditCourses.add(btn_EditCourses, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 310, 70));

        panel_sidebar.add(panel_EditCourses, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 330, 70));

        panel_CheckBalance.setBackground(new java.awt.Color(255, 0, 51));
        panel_CheckBalance.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.white, null, null));
        panel_CheckBalance.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_CheckBalance.setFont(new java.awt.Font("MS UI Gothic", 0, 36)); // NOI18N
        btn_CheckBalance.setForeground(new java.awt.Color(255, 255, 255));
        btn_CheckBalance.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/Fees_Management_System_images/list.png"))); // NOI18N
        btn_CheckBalance.setText("Check Balance");
        btn_CheckBalance.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_CheckBalance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_CheckBalanceMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_CheckBalanceMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_CheckBalanceMouseExited(evt);
            }
        });
        panel_CheckBalance.add(btn_CheckBalance, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 310, -1));

        panel_sidebar.add(panel_CheckBalance, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, 330, 70));

        panel_ViewAllRecord.setBackground(new java.awt.Color(255, 0, 51));
        panel_ViewAllRecord.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.white, null, null));
        panel_ViewAllRecord.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_ViewAllRecord.setFont(new java.awt.Font("MS UI Gothic", 0, 36)); // NOI18N
        btn_ViewAllRecord.setForeground(new java.awt.Color(255, 255, 255));
        btn_ViewAllRecord.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/Fees_Management_System_images/view all record.png"))); // NOI18N
        btn_ViewAllRecord.setText("View All Record");
        btn_ViewAllRecord.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_ViewAllRecord.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_ViewAllRecordMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_ViewAllRecordMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_ViewAllRecordMouseExited(evt);
            }
        });
        panel_ViewAllRecord.add(btn_ViewAllRecord, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 320, 70));

        panel_sidebar.add(panel_ViewAllRecord, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, 330, 70));

        panel_Back.setBackground(new java.awt.Color(255, 0, 51));
        panel_Back.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, java.awt.Color.white, null, null));
        panel_Back.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_Back.setFont(new java.awt.Font("MS UI Gothic", 0, 36)); // NOI18N
        btn_Back.setForeground(new java.awt.Color(255, 255, 255));
        btn_Back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fees_management_system/Fees_Management_System_images/left-arrow.png"))); // NOI18N
        btn_Back.setText("Back");
        btn_Back.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_BackMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_BackMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_BackMouseExited(evt);
            }
        });
        panel_Back.add(btn_Back, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 320, 70));

        panel_sidebar.add(panel_Back, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 540, 330, 70));

        panel_main.add(panel_sidebar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 790));

        panel_parent.setBackground(new java.awt.Color(255, 204, 204));
        panel_parent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("MS UI Gothic", 0, 34)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 51, 51));
        jLabel1.setText("Report");
        panel_parent.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 0, 140, 50));

        jSeparator1.setBackground(new java.awt.Color(255, 102, 102));
        jSeparator1.setForeground(new java.awt.Color(255, 51, 51));
        panel_parent.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 160, 20));

        combo_course.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        combo_course.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_courseActionPerformed(evt);
            }
        });
        panel_parent.add(combo_course, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 400, 30));

        jLabel3.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 102, 102));
        jLabel3.setText("Select Course :");
        panel_parent.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 140, 30));

        jLabel4.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 102, 102));
        jLabel4.setText("Select Date :");
        panel_parent.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 120, 30));

        jLabel6.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 102, 102));
        jLabel6.setText("From Date :");
        panel_parent.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 110, 30));

        jButton1.setBackground(new java.awt.Color(255, 0, 51));
        jButton1.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 204, 255));
        jButton1.setText("Export To Excel");
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(0, 0, 102), null, null));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        panel_parent.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 240, 210, 40));

        jButton2.setBackground(new java.awt.Color(255, 0, 51));
        jButton2.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 204, 255));
        jButton2.setText("Submit");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(0, 0, 102), null, null));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        panel_parent.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, 110, 40));

        txt_filepath.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        panel_parent.add(txt_filepath, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 250, 490, 30));

        jButton3.setBackground(new java.awt.Color(255, 0, 51));
        jButton3.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 204, 255));
        jButton3.setText("Print");
        jButton3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(0, 0, 102), null, null));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        panel_parent.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 190, 110, 40));

        jButton4.setBackground(new java.awt.Color(255, 0, 51));
        jButton4.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 204, 255));
        jButton4.setText("Browse");
        jButton4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(0, 0, 102), null, null));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        panel_parent.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 240, 110, 40));

        tbl_feesdetails.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        tbl_feesdetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "Receipt No", "Roll No", "Student Name", "Course", "Amount", "Remark"
            }
        ));
        tbl_feesdetails.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tbl_feesdetails);

        panel_parent.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 300, 870, 460));

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbl_amountinwords.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        lbl_amountinwords.setForeground(new java.awt.Color(255, 102, 102));
        jPanel1.add(lbl_amountinwords, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 360, 30));

        jLabel8.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 102, 102));
        jLabel8.setText("Course Selected :");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 160, 30));

        jLabel9.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 102, 102));
        jLabel9.setText("Total Amount Collected :");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 210, 30));

        jLabel10.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 102, 102));
        jLabel10.setText("Total Amount In Words :");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 210, 30));

        lbl_course.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        lbl_course.setForeground(new java.awt.Color(255, 102, 102));
        jPanel1.add(lbl_course, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 200, 30));

        lbl_totalamount.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        lbl_totalamount.setForeground(new java.awt.Color(255, 102, 102));
        jPanel1.add(lbl_totalamount, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, 160, 30));

        panel_parent.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 20, 360, 190));

        jLabel7.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 102, 102));
        jLabel7.setText("To Date :");
        panel_parent.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 130, 90, 30));
        panel_parent.add(date_fromdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, 110, -1));
        panel_parent.add(date_todate, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 130, 100, -1));

        panel_main.add(panel_parent, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, 1090, 790));

        scrl_pane.setViewportView(panel_main);

        getContentPane().add(scrl_pane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1501, 801));

        setSize(new java.awt.Dimension(1515, 808));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_LogoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_LogoutMouseEntered
        Color c=new Color(255,102,102);
        panel_Logout.setBackground(c);
    }//GEN-LAST:event_btn_LogoutMouseEntered

    private void btn_LogoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_LogoutMouseExited
        Color c=new Color(255,0,51);
        panel_Logout.setBackground(c);
    }//GEN-LAST:event_btn_LogoutMouseExited

    private void btn_HomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_HomeMouseClicked
        Home home=new Home();
        home.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_HomeMouseClicked

    private void btn_HomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_HomeMouseEntered
        Color c=new Color(255,102,102);
        panel_Home.setBackground(c);
    }//GEN-LAST:event_btn_HomeMouseEntered

    private void btn_HomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_HomeMouseExited
        Color c=new Color(255,0,51);
        panel_Home.setBackground(c);
    }//GEN-LAST:event_btn_HomeMouseExited

    private void btn_SearchRecordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_SearchRecordMouseClicked
        SearchRecords search=new SearchRecords();
        search.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_SearchRecordMouseClicked

    private void btn_SearchRecordMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_SearchRecordMouseEntered
        Color c=new Color(255,102,102);
        panel_SearchRecord.setBackground(c);
    }//GEN-LAST:event_btn_SearchRecordMouseEntered

    private void btn_SearchRecordMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_SearchRecordMouseExited
        Color c=new Color(255,0,51);
        panel_SearchRecord.setBackground(c);
    }//GEN-LAST:event_btn_SearchRecordMouseExited

    private void btn_EditCoursesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_EditCoursesMouseClicked
        EditCourse edit=new EditCourse();
        edit.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_EditCoursesMouseClicked

    private void btn_EditCoursesMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_EditCoursesMouseEntered
        Color c=new Color(255,102,102);
        panel_EditCourses.setBackground(c);
    }//GEN-LAST:event_btn_EditCoursesMouseEntered

    private void btn_EditCoursesMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_EditCoursesMouseExited
        Color c=new Color(255,0,51);
        panel_EditCourses.setBackground(c);
    }//GEN-LAST:event_btn_EditCoursesMouseExited

    private void btn_CheckBalanceMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_CheckBalanceMouseEntered
        Color c=new Color(255,102,102);
        panel_CheckBalance.setBackground(c);
    }//GEN-LAST:event_btn_CheckBalanceMouseEntered

    private void btn_CheckBalanceMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_CheckBalanceMouseExited
        Color c=new Color(255,0,51);
        panel_CheckBalance.setBackground(c);
    }//GEN-LAST:event_btn_CheckBalanceMouseExited

    private void btn_ViewAllRecordMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ViewAllRecordMouseEntered
        Color c=new Color(255,102,102);
        panel_ViewAllRecord.setBackground(c);
    }//GEN-LAST:event_btn_ViewAllRecordMouseEntered

    private void btn_ViewAllRecordMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ViewAllRecordMouseExited
        Color c=new Color(255,0,51);
        panel_ViewAllRecord.setBackground(c);
    }//GEN-LAST:event_btn_ViewAllRecordMouseExited

    private void btn_BackMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_BackMouseEntered
        Color c=new Color(255,102,102);
        panel_Back.setBackground(c);
    }//GEN-LAST:event_btn_BackMouseEntered

    private void btn_BackMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_BackMouseExited
        Color c=new Color(255,0,51);
        panel_Back.setBackground(c);
    }//GEN-LAST:event_btn_BackMouseExited

    private void combo_courseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_courseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_combo_courseActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clearTable();
        setRecordsToTable();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
        String dateFrom=dateFormat.format(date_fromdate.getDate());
        String dateTo=dateFormat.format(date_todate.getDate());
            MessageFormat header=new MessageFormat("Report From "+dateFrom+" To "+dateTo);
            MessageFormat footer=new MessageFormat("Page{0,number,integer}");
            try{
                tbl_feesdetails.print(JTable.PrintMode.FIT_WIDTH,header,footer);
            }
            catch(Exception e){
                e.getMessage();
            }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        JFileChooser fileChooser=new JFileChooser();
        fileChooser.showOpenDialog(this);
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
        Date currentDate=new Date();
        String date=dateFormat.format(currentDate);
        try{
            File f=fileChooser.getSelectedFile();
            String path=f.getAbsolutePath();
            path=path+"_"+date+".xlsx";
            txt_filepath.setText(path);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        exportToExcel();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btn_ViewAllRecordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ViewAllRecordMouseClicked
        ViewAllRecords records=new ViewAllRecords();
        records.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_ViewAllRecordMouseClicked

    private void btn_LogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_LogoutMouseClicked
        Login login=new Login();
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_LogoutMouseClicked

    private void btn_BackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_BackMouseClicked
        Home home=new Home();
        home.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_BackMouseClicked

    private void btn_CheckBalanceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_CheckBalanceMouseClicked
        // TODO add your handling code here:
        CheckBalance balance=new CheckBalance();
        balance.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_CheckBalanceMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GenerateReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GenerateReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GenerateReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GenerateReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GenerateReport().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btn_Back;
    private javax.swing.JLabel btn_CheckBalance;
    private javax.swing.JLabel btn_EditCourses;
    private javax.swing.JLabel btn_Home;
    private javax.swing.JLabel btn_Logout;
    private javax.swing.JLabel btn_SearchRecord;
    private javax.swing.JLabel btn_ViewAllRecord;
    private javax.swing.JComboBox<String> combo_course;
    private com.toedter.calendar.JDateChooser date_fromdate;
    private com.toedter.calendar.JDateChooser date_todate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbl_amountinwords;
    private javax.swing.JLabel lbl_course;
    private javax.swing.JLabel lbl_totalamount;
    private javax.swing.JPanel panel_Back;
    private javax.swing.JPanel panel_CheckBalance;
    private javax.swing.JPanel panel_EditCourses;
    private javax.swing.JPanel panel_Home;
    private javax.swing.JPanel panel_Logout;
    private javax.swing.JPanel panel_SearchRecord;
    private javax.swing.JPanel panel_ViewAllRecord;
    private javax.swing.JPanel panel_main;
    private javax.swing.JPanel panel_parent;
    private javax.swing.JPanel panel_sidebar;
    private javax.swing.JScrollPane scrl_pane;
    private javax.swing.JTable tbl_feesdetails;
    private javax.swing.JTextField txt_filepath;
    // End of variables declaration//GEN-END:variables
}
