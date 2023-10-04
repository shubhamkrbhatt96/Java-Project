/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package fees_management_system;
import java.awt.Color;
import javax.swing.JOptionPane;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author hp
 */
public class AddFees extends javax.swing.JFrame{

    /**
     * Creates new form AddFees
     */
    public AddFees() {
        initComponents();
        scrl_pane.getVerticalScrollBar().setUnitIncrement(20);
        displayCashFirst();
        courseCombo();
        
        int receiptNo=getReceiptNo();
        txt_receiptno.setText(Integer.toString(receiptNo));
    }
    
    public void displayCashFirst(){
        lbl_ddno.setVisible(false);
        lbl_chequeno.setVisible(false);
        lbl_bankname.setVisible(false);
        
        txt_ddno.setVisible(false);
        txt_chequeno.setVisible(false);
        txt_bankname.setVisible(false);
    }
    
    public boolean validation(){
        if(txt_receivedfrom.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Please enter user name in received from section");
            return false;
        }
        if(date_datechooser.getDate()==null){
            JOptionPane.showMessageDialog(this,"Please select the date");
            return false;
        }
        if(txt_amount.getText().equals("") || txt_amount.getText().matches("[0-9]+")==false){
            JOptionPane.showMessageDialog(this, "Please enter amount (in numbers)");
            return false;
        }
        if(combo_paymentmode.getSelectedItem().toString().equalsIgnoreCase("cheque")){
            if(txt_chequeno.getText().equals("") || txt_chequeno.getText().matches("[0-9]+")==false){
                JOptionPane.showMessageDialog(this, "Please enter cheque number");
                return false;
            }
            if(txt_bankname.getText().equals("")){
                JOptionPane.showMessageDialog(this, "Please enter bank name");
                return false;
            }
        }
        if(combo_paymentmode.getSelectedItem().toString().equalsIgnoreCase("dd")){
            if(txt_ddno.getText().equals("")){
                JOptionPane.showMessageDialog(this, "Please enter dd number");
                return false;
            }
            if(txt_bankname.getText().equals("")){
                JOptionPane.showMessageDialog(this, "Please enter bank name");
                return false;
            }
        }
        if(combo_paymentmode.getSelectedItem().toString().equalsIgnoreCase("card")){
            if(txt_bankname.getText().equals("")){
                JOptionPane.showMessageDialog(this, "Please enter bank name");
                return false;
            }
        }
        return true;
    }
    
    public void courseCombo(){
        try{
            Connection con=DBConnection.getConnection();
           // System.out.println(con.getClass().getName());
           PreparedStatement ps=con.prepareStatement("select course_name from course");
           ResultSet rs=ps.executeQuery();
           while(rs.next()){
               combo_course.addItem(rs.getString("course_name"));
           }
        }catch(Exception e){
            //e.toString();
            //System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public int getReceiptNo(){
        int receiptNo=0;
        try{
            Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement("select max(receipt_no) from fees_details");
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                receiptNo=rs.getInt(1);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return receiptNo+1;
    }
    
    public float payable(){
        float payable = 0;
        String courseName=txt_coursename.getText();
        try{
            Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement("Select * from course");
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                String course=rs.getString("course_name");
                    if(courseName.equalsIgnoreCase(course)){
                        float course_cost=rs.getFloat("course_cost");
                        payable=course_cost;
                        return payable;
                    }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return payable;
    }
    
    public String insertData(){
        
        String status="";
        
        int receiptNo=Integer.parseInt(txt_receiptno.getText());
        String studentName=txt_receivedfrom.getText();
        String rollNo=txt_rollno.getText();
        String paymentMode=combo_paymentmode.getSelectedItem().toString();
        String chequeNo=txt_chequeno.getText();
        String bankName=txt_bankname.getText();
        String ddNo=txt_ddno.getText();
        String courseName=txt_coursename.getText();
        String gstin=gstno.getText();
        float totalAmount=Float.parseFloat(txt_total.getText());
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String date=dateFormat.format(date_datechooser.getDate());
        float initialAmount=Float.parseFloat(txt_amount.getText());
        float payable=payable();
        float balance=payable-initialAmount;
        float cgst=Float.parseFloat(txt_cgst.getText());
        float sgst=Float.parseFloat(txt_sgst.getText());
        String totalInWords=txt_totalinwords.getText();
        String remark=txt_remark.getText();
        int year1=Integer.parseInt(txt_year1.getText());
        int year2=Integer.parseInt(txt_year2.getText());
        String fees_status;
        if(balance==0){
            fees_status="Paid";
        }
        else if(balance==payable){
            fees_status="Unpaid";
        }
        else{
            fees_status="Partial Paid";
        }
        
        try{
            Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement("insert into fees_details values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, receiptNo);
            ps.setString(2, studentName);
            ps.setString(3, rollNo);
            ps.setString(4, paymentMode);
            ps.setString(5, chequeNo);
            ps.setString(6, bankName);
            ps.setString(7, ddNo);
            ps.setString(8, courseName);
            ps.setString(9, gstin);
            ps.setFloat(10, totalAmount);
            ps.setString(11, date);
            ps.setFloat(12, initialAmount);
            ps.setFloat(13, payable);
            ps.setFloat(14,balance);
            ps.setString(15,fees_status);
            ps.setFloat(16, cgst);
            ps.setFloat(17, sgst);
            ps.setString(18, totalInWords);
            ps.setString(19, remark);
            ps.setInt(20, year1);
            ps.setInt(21, year2);
            
//            if we use execute method
//            boolean b=ps.execute();
//            ResultSet rs=ps.getResultSet();
            
            int rowCount=ps.executeUpdate();      
            if(rowCount == 1){
                status="success";
            }
            else{
                status="failed";
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return status;
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
        panel_parent = new javax.swing.JPanel();
        gstno = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lbl_chequeno = new javax.swing.JLabel();
        lbl_ddno = new javax.swing.JLabel();
        lbl_bankname = new javax.swing.JLabel();
        date_label = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txt_bankname = new javax.swing.JTextField();
        txt_receiptno = new javax.swing.JTextField();
        txt_ddno = new javax.swing.JTextField();
        txt_chequeno = new javax.swing.JTextField();
        combo_paymentmode = new javax.swing.JComboBox<>();
        panel_child = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txt_rollno = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txt_total = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        combo_course = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txt_year1 = new javax.swing.JTextField();
        txt_totalinwords = new javax.swing.JTextField();
        txt_amount = new javax.swing.JTextField();
        txt_cgst = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        txt_sgst = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txt_coursename = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_remark = new javax.swing.JTextArea();
        jLabel20 = new javax.swing.JLabel();
        btn_print = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txt_receivedfrom = new javax.swing.JTextField();
        txt_year2 = new javax.swing.JTextField();
        date_datechooser = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("IICS Fees");
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

        panel_sidebar.add(panel_Logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 590, 330, 70));

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

        panel_sidebar.add(panel_SearchRecord, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 330, 70));

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

        panel_sidebar.add(panel_EditCourses, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, 330, 70));

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

        panel_sidebar.add(panel_CheckBalance, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 390, 330, 70));

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

        panel_sidebar.add(panel_ViewAllRecord, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, 330, 70));

        panel_main.add(panel_sidebar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 790));

        panel_parent.setBackground(new java.awt.Color(255, 204, 204));
        panel_parent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        gstno.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        gstno.setForeground(new java.awt.Color(255, 102, 102));
        gstno.setText("255ESFAQ56OP113");
        panel_parent.add(gstno, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 80, 170, 40));

        jLabel2.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 102, 102));
        jLabel2.setText("Receipt no :");
        panel_parent.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 100, 40));

        jLabel3.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 102, 102));
        jLabel3.setText("Mode of payment :");
        panel_parent.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 150, 40));

        lbl_chequeno.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        lbl_chequeno.setForeground(new java.awt.Color(255, 102, 102));
        lbl_chequeno.setText("Cheque no :");
        panel_parent.add(lbl_chequeno, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 140, 40));

        lbl_ddno.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        lbl_ddno.setForeground(new java.awt.Color(255, 102, 102));
        lbl_ddno.setText("DD no :");
        panel_parent.add(lbl_ddno, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 100, 40));

        lbl_bankname.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        lbl_bankname.setForeground(new java.awt.Color(255, 102, 102));
        lbl_bankname.setText("Bank name :");
        panel_parent.add(lbl_bankname, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 100, 40));

        date_label.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        date_label.setForeground(new java.awt.Color(255, 102, 102));
        date_label.setText("Date :");
        panel_parent.add(date_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 40, 80, 40));

        jLabel9.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 102, 102));
        jLabel9.setText("GSTIN :");
        panel_parent.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 80, 80, 40));

        txt_bankname.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        panel_parent.add(txt_bankname, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 170, 180, 30));

        txt_receiptno.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        panel_parent.add(txt_receiptno, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 180, 30));

        txt_ddno.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        txt_ddno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_ddnoActionPerformed(evt);
            }
        });
        panel_parent.add(txt_ddno, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 120, 180, 30));

        txt_chequeno.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        panel_parent.add(txt_chequeno, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 120, 180, 30));

        combo_paymentmode.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        combo_paymentmode.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DD", "Cheque", "Cash", "Card" }));
        combo_paymentmode.setSelectedIndex(2);
        combo_paymentmode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_paymentmodeActionPerformed(evt);
            }
        });
        panel_parent.add(combo_paymentmode, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 70, 180, 30));

        panel_child.setBackground(new java.awt.Color(255, 204, 204));
        panel_child.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 102, 102));
        jLabel10.setText("to");
        panel_child.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, 30, 40));

        jLabel11.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 102, 102));
        jLabel11.setText("The following payment in the college office for the year");
        panel_child.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 450, 40));

        txt_rollno.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        panel_child.add(txt_rollno, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 100, 110, 30));

        jLabel12.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 102, 102));
        jLabel12.setText("Roll no :");
        panel_child.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 100, 130, 40));

        txt_total.setEditable(false);
        txt_total.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        txt_total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_totalActionPerformed(evt);
            }
        });
        panel_child.add(txt_total, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 360, 240, 30));

        jLabel13.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 102, 102));
        jLabel13.setText("Received from :");
        panel_child.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 130, 40));

        combo_course.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        combo_course.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_courseActionPerformed(evt);
            }
        });
        panel_child.add(combo_course, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, 490, 30));

        jLabel14.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 102, 102));
        jLabel14.setText("Amount");
        panel_child.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 160, 70, 40));

        jSeparator1.setBackground(new java.awt.Color(0, 0, 51));
        jSeparator1.setForeground(new java.awt.Color(255, 102, 102));
        panel_child.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 480, 260, 20));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 51));
        jSeparator2.setForeground(new java.awt.Color(255, 102, 102));
        panel_child.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 1010, 20));

        jLabel15.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 102, 102));
        jLabel15.setText("Remark :");
        panel_child.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 430, 130, 40));

        jLabel16.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 102, 102));
        jLabel16.setText("Sr No");
        panel_child.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 70, 40));

        jLabel17.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 102, 102));
        jLabel17.setText("Head");
        panel_child.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 160, 70, 40));

        txt_year1.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        panel_child.add(txt_year1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 60, 110, 30));

        txt_totalinwords.setEditable(false);
        txt_totalinwords.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        panel_child.add(txt_totalinwords, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 370, 550, 30));

        txt_amount.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        txt_amount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_amountActionPerformed(evt);
            }
        });
        panel_child.add(txt_amount, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 220, 240, 30));

        txt_cgst.setEditable(false);
        txt_cgst.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        panel_child.add(txt_cgst, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 260, 240, 30));

        jSeparator3.setBackground(new java.awt.Color(0, 0, 51));
        jSeparator3.setForeground(new java.awt.Color(255, 102, 102));
        panel_child.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 1010, 20));

        txt_sgst.setEditable(false);
        txt_sgst.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        panel_child.add(txt_sgst, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 300, 240, 30));

        jLabel18.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 102, 102));
        jLabel18.setText("SGST 9%");
        panel_child.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 290, 90, 40));

        txt_coursename.setEditable(false);
        txt_coursename.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        panel_child.add(txt_coursename, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 220, 550, 30));

        jSeparator4.setBackground(new java.awt.Color(0, 0, 51));
        jSeparator4.setForeground(new java.awt.Color(255, 102, 102));
        panel_child.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 340, 260, 20));

        jLabel19.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 102, 102));
        jLabel19.setText("Receiver Signature");
        panel_child.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 480, 160, 40));

        txt_remark.setColumns(20);
        txt_remark.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        txt_remark.setRows(5);
        jScrollPane1.setViewportView(txt_remark);

        panel_child.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 430, 550, 110));

        jLabel20.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 102, 102));
        jLabel20.setText("Total in words :");
        panel_child.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, 130, 40));

        btn_print.setBackground(new java.awt.Color(255, 0, 51));
        btn_print.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        btn_print.setForeground(new java.awt.Color(255, 204, 255));
        btn_print.setText("Print");
        btn_print.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, new java.awt.Color(255, 102, 255), null, null));
        btn_print.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_printActionPerformed(evt);
            }
        });
        panel_child.add(btn_print, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 520, 80, 30));

        jLabel21.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 102, 102));
        jLabel21.setText("Course :");
        panel_child.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 130, 40));

        jLabel22.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 102, 102));
        jLabel22.setText("CGST 9%");
        panel_child.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 260, 90, 40));

        txt_receivedfrom.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        panel_child.add(txt_receivedfrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 310, 30));

        txt_year2.setFont(new java.awt.Font("MS UI Gothic", 0, 18)); // NOI18N
        panel_child.add(txt_year2, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 60, 110, 30));

        panel_parent.add(panel_child, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 220, 1130, 570));
        panel_parent.add(date_datechooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 50, 110, 30));

        panel_main.add(panel_parent, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, 1130, 790));

        scrl_pane.setViewportView(panel_main);

        getContentPane().add(scrl_pane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1540, 800));

        setSize(new java.awt.Dimension(1556, 808));
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

    private void btn_HomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_HomeMouseEntered
        Color c=new Color(255,102,102);
        panel_Home.setBackground(c);
    }//GEN-LAST:event_btn_HomeMouseEntered

    private void btn_HomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_HomeMouseExited
        Color c=new Color(255,0,51);
        panel_Home.setBackground(c);
    }//GEN-LAST:event_btn_HomeMouseExited

    private void btn_SearchRecordMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_SearchRecordMouseEntered
        Color c=new Color(255,102,102);
        panel_SearchRecord.setBackground(c);
    }//GEN-LAST:event_btn_SearchRecordMouseEntered

    private void btn_SearchRecordMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_SearchRecordMouseExited
        Color c=new Color(255,0,51);
        panel_SearchRecord.setBackground(c);
    }//GEN-LAST:event_btn_SearchRecordMouseExited

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

    private void txt_ddnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_ddnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_ddnoActionPerformed

    private void combo_paymentmodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_paymentmodeActionPerformed
        if(combo_paymentmode.getSelectedIndex()==0){
            lbl_ddno.setVisible(true);
            txt_ddno.setVisible(true);
            lbl_chequeno.setVisible(false);
            txt_chequeno.setVisible(false);
            lbl_bankname.setVisible(true);
            txt_bankname.setVisible(true);
        }
        if(combo_paymentmode.getSelectedIndex()==1){
            lbl_ddno.setVisible(false);
            txt_ddno.setVisible(false);
            lbl_chequeno.setVisible(true);
            txt_chequeno.setVisible(true);
            lbl_bankname.setVisible(true);
            txt_bankname.setVisible(true);
        }
        if(combo_paymentmode.getSelectedIndex()==2){
            lbl_ddno.setVisible(false);
            txt_ddno.setVisible(false);
            lbl_chequeno.setVisible(false);
            txt_chequeno.setVisible(false);
            lbl_bankname.setVisible(false);
            txt_bankname.setVisible(false);
        }
        if(combo_paymentmode.getSelectedItem().equals("Card")){
            lbl_ddno.setVisible(false);
            txt_ddno.setVisible(false);
            lbl_chequeno.setVisible(false);
            txt_chequeno.setVisible(false);
            lbl_bankname.setVisible(true);
            txt_bankname.setVisible(true);
        }
    }//GEN-LAST:event_combo_paymentmodeActionPerformed

    private void txt_totalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_totalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalActionPerformed

    private void btn_printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_printActionPerformed
        if(validation()==true){
            //JOptionPane.showMessageDialog(this, "Validation Successful");
            String result=insertData();
            if(result.equals("success")){
                JOptionPane.showMessageDialog(this, "Record inserted successfully");
                PrintReceipt print=new PrintReceipt();
                print.setVisible(true);
                this.dispose();
            }
            else{
                JOptionPane.showMessageDialog(this, "Record insertion failed");
            }
        }
    }//GEN-LAST:event_btn_printActionPerformed

    private void txt_amountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_amountActionPerformed
        Float amount=Float.parseFloat(txt_amount.getText());
        Float cgst=(amount * 0.09f);
        Float sgst=(float)(amount * 0.09);
        
        txt_cgst.setText(cgst.toString());
        txt_sgst.setText(sgst.toString());
        
        float total=amount+cgst+sgst;
        txt_total.setText(Float.toString(total));
        txt_totalinwords.setText(NumberToWordsConverter.convert((int)total)+" Only");
    }//GEN-LAST:event_txt_amountActionPerformed

    private void combo_courseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_courseActionPerformed
        txt_coursename.setText(combo_course.getSelectedItem().toString());
    }//GEN-LAST:event_combo_courseActionPerformed

    private void btn_HomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_HomeMouseClicked
        Home home=new Home();
        home.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_HomeMouseClicked

    private void btn_SearchRecordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_SearchRecordMouseClicked
        SearchRecords search=new SearchRecords();
        search.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_SearchRecordMouseClicked

    private void btn_EditCoursesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_EditCoursesMouseClicked
        EditCourse edit=new EditCourse();
        edit.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_EditCoursesMouseClicked

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
            java.util.logging.Logger.getLogger(AddFees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddFees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddFees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddFees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddFees().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btn_CheckBalance;
    private javax.swing.JLabel btn_EditCourses;
    private javax.swing.JLabel btn_Home;
    private javax.swing.JLabel btn_Logout;
    private javax.swing.JLabel btn_SearchRecord;
    private javax.swing.JLabel btn_ViewAllRecord;
    private javax.swing.JButton btn_print;
    private javax.swing.JComboBox<String> combo_course;
    private javax.swing.JComboBox<String> combo_paymentmode;
    private com.toedter.calendar.JDateChooser date_datechooser;
    private javax.swing.JLabel date_label;
    private javax.swing.JLabel gstno;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel lbl_bankname;
    private javax.swing.JLabel lbl_chequeno;
    private javax.swing.JLabel lbl_ddno;
    private javax.swing.JPanel panel_CheckBalance;
    private javax.swing.JPanel panel_EditCourses;
    private javax.swing.JPanel panel_Home;
    private javax.swing.JPanel panel_Logout;
    private javax.swing.JPanel panel_SearchRecord;
    private javax.swing.JPanel panel_ViewAllRecord;
    private javax.swing.JPanel panel_child;
    private javax.swing.JPanel panel_main;
    private javax.swing.JPanel panel_parent;
    private javax.swing.JPanel panel_sidebar;
    private javax.swing.JScrollPane scrl_pane;
    private javax.swing.JTextField txt_amount;
    private javax.swing.JTextField txt_bankname;
    private javax.swing.JTextField txt_cgst;
    private javax.swing.JTextField txt_chequeno;
    private javax.swing.JTextField txt_coursename;
    private javax.swing.JTextField txt_ddno;
    private javax.swing.JTextField txt_receiptno;
    private javax.swing.JTextField txt_receivedfrom;
    private javax.swing.JTextArea txt_remark;
    private javax.swing.JTextField txt_rollno;
    private javax.swing.JTextField txt_sgst;
    private javax.swing.JTextField txt_total;
    private javax.swing.JTextField txt_totalinwords;
    private javax.swing.JTextField txt_year1;
    private javax.swing.JTextField txt_year2;
    // End of variables declaration//GEN-END:variables
}
