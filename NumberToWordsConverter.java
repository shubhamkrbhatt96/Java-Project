/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fees_management_system;
import java.text.NumberFormat;
import java.util.Scanner;

/**
 *
 * @author hp
 */
public class NumberToWordsConverter {
    public static final String[] ones={"","One","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten","Eleven","Twelve","Thirteen","Fourteen","Fifteen","Sixteen","Seventeen","Eighteen","Nineteen"};
    public static final String[] tens={"","","Twenty","Thirty","Forty","Fifty","Sixty","Seventy","Eighty","Ninety"};
    public static String convert(final int n){
        if(n<0){
            return "Minus "+convert(-n);
        }
        if(n<20){
            return ones[n];
        }
        if(n<100){
            return tens[n/10]+((n%10!=0)?" ":"")+ones[n%10];
        }
        if(n<1000){
            return ones[n/100]+" Hundred"+((n%100!=0)?" ":"")+convert(n%100);
        }
        if(n<100000){
            return convert(n/1000)+" Thousand"+((n%1000)!=0?" ":"")+convert(n%1000);
        }
        if(n<10000000){
            return convert(n/100000)+" Lakh"+((n%100000)!=0?" ":"")+convert(n%100000);
        }
        return convert(n/10000000)+" Crore"+((n%10000000)!=0?" ":"")+convert(n%10000000);
    }
    public static void main(final String[] args){
        Scanner scan=new Scanner(System.in);
        System.out.println("Enter the amount: ");
        int num=scan.nextInt();
        System.out.println(convert(num)+" Only");
    }
}
