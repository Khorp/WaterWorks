import java.util.Scanner;

public class TestP43
{

    public static void main(String[] args)
    {
        menu();
    }

    public static void menu()
    {
        System.out.println("Pick your menu" +
                "/n1. Manage Customer /n2. Statistics Calculator /n3. Taxes /n4. insert payments /n5. Insert reading");
        System.out.println("select the number between 1-5");
        Scanner in = new Scanner(System.in);
        int input = in.nextInt();
        {
            while (input > 0 && input < 6) {
                switch (input) {
                    case 1:
                        CustomerManagement();
                        break;
                    case 2:
                        StatisticCalculator();
                        break;
                    case 3:
                        //Taxes();
                        break;
                    case 4:
                        //Payment();
                        break;
                    case 5:
                        Reading();
                        break;
                    default:
                        System.exit(0);
                }
            }
            System.out.println("choice of menu not reckognized pick 1 to 5");
        }

        }
        public static void CustomerManagement()
        {
            Scanner in = new Scanner(System.in);
            System.out.println("Customer Managment /n1. Update customer Info /n2. Create new customer /n3. " +
                    "See customer info");
            System.out.println("select the number between 1-3");


            switch (in.nextInt()) {
                case 1:
                    System.out.println("UPDATING CUSTOMER MENU /n please enter cutomers cpr number");
                    String cprNo = in.next();
                    System.out.println("Please enter customers first name");
                    String firstName = in.next();
                    System.out.println("Please enter customers last name");
                    String lastName = in.next();
                    System.out.println("Please enter customers address");
                    String address = in.next();
                    System.out.println("Please enter customers post code");
                    int postCode = in.nextInt();
                    System.out.println("Please enter customers phone number");
                    int phoneNumber = in.nextInt();
                    System.out.println("Please enter customers email");
                    String email = in.next();

                    DB.updateSQL("UPDATE tbl_customer WHERE fld_CprNo = " + cprNo + " VALUES(" + cprNo + " ,'" + firstName + "','" + lastName + "','" + address + "'" + postCode + "," + phoneNumber + ",'" + email + "')");
                    break;


                case 2:
                    System.out.println("CREATING NEW CUSTOMER MENU /n please enter cutomers cpr number");
                    cprNo = in.next();
                    System.out.println("Please enter customers first name");
                    firstName = in.next();
                    System.out.println("Please enter customers last name");
                    lastName = in.next();
                    System.out.println("Please enter customers address");
                    address = in.next();
                    System.out.println("Please enter customers post code");
                    postCode = in.nextInt();
                    System.out.println("Please enter customers phone number");
                    phoneNumber = in.nextInt();
                    System.out.println("Please enter customers email");
                    email = in.next();

                    DB.insertSQL("INSERT into tbl_customer VALUES(" + cprNo + " ,'" + firstName + "','" + lastName + "','" + address + "'," + postCode + "," + phoneNumber + ",'" + email + "')");
                    break;

                case 3:
                    System.out.println(" CUSTOMER MENU /n please enter cutomers cpr number");
                    cprNo = in.next();

                    DB.selectSQL("Select * From tbl_Customer WHERE fld_CprNo = '" + cprNo + "' ");
                    do {
                        String data = DB.getDisplayData();
                        if (data.equals(DB.NOMOREDATA)) {
                            break;
                        } else {
                            System.out.print(data);
                        }
                    } while (true);

            }
        }
        public static void StatisticCalculator()
        {
            System.out.println(" Tax statistics by sector");
            int cprNo =0;
            DB.selectSQL("Select SUM of  From tbl_Customer WHERE fld_CprNo = " + cprNo + " ");
            System.out.println(DB.getDisplayData());

            do {
                String data = DB.getDisplayData();
                if (data.equals(DB.NOMOREDATA)) {
                    break;
                } else {
                    System.out.print(data);
                }
            } while (true);

        }
        public static void Reading()
        {
            Scanner in = new Scanner(System.in);
            System.out.println(" INPUT NEW READING /N input MeterID");
            int MeterID = in.nextInt();
            System.out.println("input new reading");
            int reading = in.nextInt();
            System.out.println("input date of reading");
            int Date = in.nextInt();

            DB.selectSQL("");
            DB.insertSQL("INSERT INTO tbl_Reading VALUES(" + null + ", '" + MeterID + "', '" + reading + "', '" + Date + "')");

        }
    }
