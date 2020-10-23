import java.util.Scanner;

public class ExerciseInDB {
    public static void main(String[] args) {
        /*
        Scanner in = new Scanner(System.in);
        int input1 = in.nextInt();
        String input2 = in.next();
        String input3 = in.next();
        String input4 = in.next();
        String comIn = input1+",'"+input2+"','"+input3+"','"+input4+"')";
        DB.insertSQL("insert into employee values("+comIn);
         */
        DB.selectSQL("Select * from employee");


        do{
            String data = DB.getDisplayData();
            if (data.equals(DB.NOMOREDATA)){
                break;
            }else{
                System.out.printf("%10s",data);
            }
        } while(true);
    }
}
