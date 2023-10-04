/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package fees_management_system;

import java.sql.*;

/**
 *
 * @author hp
 */
public class Fees_Management_System {

    /**
     * @param args the command line arguments
     */
    static String name;
    
    public String welcomeName(String username,String password){
        try{
            Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement("select user_name,password,full_name from sign_up");
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                String user=rs.getString("user_name");
                String pass=rs.getString("password");
                if(username.equalsIgnoreCase(user) && password.equalsIgnoreCase(pass)){
                    name=rs.getString("full_name");
                    return name;
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public String setName(){
        return name;
    }
    
    static String verifyname;
    
    public void verifyUserName(String name){
        verifyname=name;
    }
    public String getVerifyName(){
        return verifyname;
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
