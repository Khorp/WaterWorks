import java.time.LocalDate;
import java.util.Scanner;

/***
 * @author Group 7
 * @since 2020-10-21
 *
 */
public class main {
    public static void main(String[] args) {
        Menu();//call menu method
    }

    /***
     *
     * @param MeterID Passing around a meterID
     * @return return result for difference
     */
    public static double consumptionData(int MeterID)
    {
        return calculateDiff(MeterID);
    }

    /***
     * Here we insert new Readings from Meters into the Database
     */
    public static void newReadingValue()
    {
        Scanner in = new Scanner(System.in);
        LocalDate date = LocalDate.now(); //we use a localDate.class to automatically generate a date.
        System.out.println("Insert Reading Info");
        System.out.println("Insert MeterID: ");
        int MeterID = loop();//we call open the loop method to make sure it's a Integer we get back
        System.out.println("Insert Reading: ");
        int reading = loop();
        DB.insertSQL("insert into tbl_Reading(fld_MeterID,fld_Reading,fld_Date) Values("+MeterID+","+reading+",'"+date+"')");
        //we insert it here
    }

    /***
     *
     * @param MeterID we use the meterID for finding the data we need to calculate
     * @return Here we return the Difference in consumption from old and new readings
     */
    public static double calculateDiff(int MeterID)
    {
        double DiffConsumption = 0.0;
        DB.selectSQL("Select top 2 fld_ReadingID from tbl_Reading where fld_MeterID="+MeterID+" order by fld_ReadingID desc");
        String stringReadingID = DB.getData(); //we save the first ID in a string
        String stringReadingID2 = DB.getData();
        int readingID = Integer.parseInt(stringReadingID); // we convert it into a Integer
        int readingID2 = 0;
        if (!(stringReadingID2.equals("|ND|")))//if second ID is not a null we convert it into a Integer
        {
            readingID2 = Integer.parseInt(stringReadingID2);
        }
        DB.selectSQL("Select top 2 fld_Reading from tbl_Reading where fld_MeterID="+MeterID+" order by fld_ReadingID desc ");
        //we take the readings here
        String Reading1 = DB.getData();
        String Reading2 = DB.getData();
        if (Reading2.equals("|ND|"))
        {
            Reading2 = "0";
        }
        double read1 = Double.parseDouble(Reading1);
        double read2 = Double.parseDouble(Reading2);
        if (readingID>readingID2) { DiffConsumption = read1-read2; } //here we calculate the Difference
        else if (readingID2>readingID){ DiffConsumption = read2-read1; }
        return DiffConsumption; // and it gets returned
    }

    /***
     *
     * @param MeterID We use the MeterID to find the right Data from the Database
     */
    public static void GenerateBill(int MeterID)
    {
        int billID = 0;
        DB.selectSQL("Select Max(fld_BillNumber) from tbl_Bill");
        String billIdString = DB.getData();
        if (billIdString.equals("null"))// we check if there is any bills in the system
        {
            billID++;//if there are we give at the value 1
        }
        else { //we plus 1 onto the highest BillID
            billID = Integer.parseInt(billIdString) + 1;
        }
        NoMoreData(); //we call upon a method that makes sure that there isn't any leftover data from select
        double amount = generatePrize(MeterID);//we call upon the method that calculates the prize
        LocalDate date = LocalDate.now();
        DB.selectSQL("Select fld_SegmentID from tbl_Meter where fld_MeterID="+MeterID+"");
        String segmentId = DB.getData();
        DB.selectSQL("Select Max(fld_ReadingID) from tbl_Reading where fld_MeterID="+MeterID+"");
        String stringReadingID = DB.getData();
        int readingID = Integer.parseInt(stringReadingID);
        NoMoreData();
        final double readingFee = 500.0; //the fee for the waterwork if they have check readings
        System.out.println("BillID   Amount  Date      SegmentID ReadingFee ReadingID"); //output of data
        DB.insertSQL("insert into tbl_Bill(fld_BillNumber,fld_TotalAmount,fld_Date,fld_SegmentID,fld_ReadingFee,fld_ReadingID) " +
                "Values("+billID+","+amount+",'"+date+"','"+segmentId+"',"+readingFee+","+readingID+")"); //inserting a new Bill
        DB.selectSQL("Select * from tbl_Bill");//output
        do{
            String data = DB.getDisplayData();
            if (data.equals(DB.NOMOREDATA)){
                break;
            }else{
                System.out.printf("|%7s",data);
            }
        } while(true);
    }

    /***
     *
     * @param MeterID Using the meterID to find the right Data
     * @return returns the total amount of money
     */
    public static double generatePrize(int MeterID)
    {
        double consumption = consumptionData(MeterID); // getting the difference in consumption
        double Drainage = consumption*0.8; //Creating the Drainage

        DB.selectSQL("Select fld_SegmentID from tbl_Meter where fld_MeterID="+MeterID+"");
        String segmentId = DB.getData();//finding the segmentID to find the taxes for different Segments
        DB.selectSQL("Select fld_WaterTax from tbl_Segments where fld_SegmentID='"+segmentId+"'");
        String consumptionTaxString = DB.getData();
        double consumptionTax = Double.parseDouble(consumptionTaxString);//here we get the WaterTax
        DB.selectSQL("Select fld_DrainageTax from tbl_Segments where fld_SegmentID='"+segmentId+"'");
        String drainageTaxString = DB.getData();
        NoMoreData();
        double drainageTax = Double.parseDouble(drainageTaxString);//here we get the DrainageTax
        double WaterPrize = 7.96;//the prize of Water
        double DrainagePrize = 16.88;//the prize of the drainage

        return CaclulateTaxes(consumption,consumptionTax,drainageTax,WaterPrize,DrainagePrize,Drainage); //final calculations method
    }

    /***
     *Here is the Menu we use to get around
     */
    public static void Menu()
    {
        //newValue(); if we choose it.
        //other thing to.
        Scanner in = new Scanner(System.in);
        boolean done = false;
        while (!done) {
            System.out.println("Pick your menu" +
                    "\nn1. Manage Customer \nn2. Statistics Calculator \nn3. Total Taxes for the Segments \nn4. Generate Bill \nn5. Insert reading \nn6. Check" +
                    "payment status \nn7. Insert payment(csv) \nn8. Exit Program");
            System.out.println("select the number between 1-8");
            int input = loop(); // our input for the menu
            switch (input) {
                case 1:
                    CustomerManagement();
                    break;
                case 2:
                    StatisticCalculator();
                    break;
                case 3:
                    Taxes();
                    break;
                case 4:
                    System.out.println("Input a MeterID: ");
                    int input2 = loop();
                    GenerateBill(input2);
                    break;
                case 5:
                    newReadingValue();
                    break;
                case 6:
                    checkPaymentStatus();
                    break;
                case 7:
                    insertPayment();
                    break;
                case 8:
                    System.out.println("Exiting program");
                    done = true;
                    break;
                default:
                    System.out.println("Choice not recognized please insert number 1-8");
            }
        }
    }

    /***
     *
     * @param con the difference in consumption
     * @param cTax the waterTax
     * @param dTax the Drainage Tax
     * @param WPrize the prize of water
     * @param DPrize the drainage prize
     * @param drainage the amount of drainage
     * @return returns the total amount of money
     */
    public static double CaclulateTaxes(double con,double cTax,double dTax,double WPrize,double DPrize,double drainage)
    {
        double WaterPrize = (con*WPrize);
        double DrainagePrize =(drainage*DPrize);
        double WaterTax = WaterPrize*((cTax/100)+1);
        double DrainageTax = DrainagePrize*((dTax/100)+1);
        return WaterTax+DrainageTax;

    }

    /***
     * the method where we make sure there isn't more data from the sql select
     */
    public static void NoMoreData()
    {
        do{
            String data = DB.getDisplayData();
            if (data.equals(DB.NOMOREDATA)){
                break;
            }else{
                System.out.print(data);
            }
        } while(true);
    }

    /***
     * Here we can update, create or see all customers
     */
    public static void CustomerManagement()
    {
        Scanner in = new Scanner(System.in);
        String cprNo,firstName,lastName,address,email;
        int postCode,phoneNumber;
        System.out.println("Customer Management \nn1. Update customer Info \nn2. Create new customer \nn3. " +
                "See customer info");
        System.out.println("select the number between 1-3");
        switch (in.nextInt()) {
            case 1:
                    System.out.println("UPDATING CUSTOMER MENU \nPlease enter customers cpr number");
                    cprNo = in.next();
                    System.out.println("Please enter customers first name");
                    firstName = in.next();
                    System.out.println("Please enter customers last name");
                    lastName = in.next();
                    System.out.println("Please enter customers address");
                    address = in.next();
                    System.out.println("Please enter customers post code");
                    postCode = loop();
                    System.out.println("Please enter customers phone number");
                    phoneNumber = loop();
                    System.out.println("Please enter customers email");
                    email = in.next();

                    DB.updateSQL("UPDATE tbl_customer  set fld_FirstName='" + firstName + "',fld_lst_Name='" + lastName +
                            "',fld_Address='" + address + "',fld_PostCode=" + postCode + ",fld_PhoneNo=" + phoneNumber + ",fld_email='" + email +
                            "' where fld_CprNo='" + cprNo + "'");
                break;


            case 2:
                System.out.println("CREATING NEW CUSTOMER MENU \nPlease enter customers cpr number");
                cprNo = in.next();
                System.out.println("Please enter customers first name");
                firstName = in.next();
                System.out.println("Please enter customers last name");
                lastName = in.next();
                System.out.println("Please enter customers address");
                address = in.next();
                System.out.println("Please enter customers post code");
                postCode = loop();
                System.out.println("Please enter customers phone number");
                phoneNumber =loop();
                System.out.println("Please enter customers email");
                email = in.next();

                DB.insertSQL("INSERT into tbl_customer VALUES('" + cprNo + "' ,'" + firstName + "','" + lastName + "','" + address + "'," + postCode + "," + phoneNumber + ",'" + email + "')");
                break;

            case 3:
                System.out.println(" CUSTOMER MENU \nPlease enter customers cpr number");
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

    /***
     * method that does noting
     */
    public static void StatisticCalculator()
    {
        System.out.println("Not available\n");
    }

    /***
     * here it displays unpaid bills
     */
    public static void checkPaymentStatus()
    {
        DB.selectSQL("select * from tbl_Bill where fld_BillNumber not in(select fld_BillNumber from tbl_Payment)");
        System.out.println("        BillID    Amount   Date        SegmentID    ReadingFee ReadingID");
        do{
            String data = DB.getDisplayData();
            if (data.equals(DB.NOMOREDATA)){
                break;
            }else{
                System.out.printf("%12s",data);
            }
        } while(true);
        System.out.println();

    }

    /***
     * prints out the sum of all the segmented taxes
     */
    public static void Taxes()
    {
      System.out.println("\nHouseholds Taxes Due");
      DB.selectSQL("Select sum(fld_Amount) from tbl_Payment inner join tbl_Bill on tbl_Bill.fld_BillNumber = tbl_Payment.fld_BillNumber where fld_SegmentID='H'");
      String hTaxes = DB.getData();
      NoMoreData();
      System.out.println(hTaxes);
      System.out.println("Agriculture Taxes Due");
      DB.selectSQL("Select sum(fld_Amount) from tbl_Payment inner join tbl_Bill on tbl_Bill.fld_BillNumber = tbl_Payment.fld_BillNumber where fld_SegmentID='A'");
      String aTaxes = DB.getData();
      NoMoreData();
      System.out.println(aTaxes);
      System.out.println("Industry Taxes Due");
      DB.selectSQL("Select sum(fld_Amount) from tbl_Payment inner join tbl_Bill on tbl_Bill.fld_BillNumber = tbl_Payment.fld_BillNumber where fld_SegmentID='I'");
      String iTaxes = DB.getData();
      NoMoreData();
      System.out.println(iTaxes+"\n");

    }

    /****
     * we insert the paid bills
     */
    public static void insertPayment()
    {
        int paymentID = 0;
        DB.selectSQL("Select Max(fld_PaymentID) from tbl_Payment");
        String paymentIdString = DB.getData();
        if (paymentIdString.equals("null"))
        {
            paymentID++;
        }
        else {
            paymentID = Integer.parseInt(paymentIdString) + 1;
        }
        NoMoreData();
        Scanner in = new Scanner(System.in);
        System.out.println("Input BillNumber: ");
        int billID = loop();
        System.out.println("Input Amount: ");
        int amount = loop();
        System.out.println("Input Date: ");
        String Date = in.next();
        System.out.println("Input Payment Type: ");
        String paymentType = in.next();
        DB.insertSQL("insert into tbl_Payment(fld_PaymentID,fld_BillNumber,fld_Amount,fld_Date,fld_PaymantType) " +
                "Values("+paymentID+","+billID+","+amount+",'"+Date+"','"+paymentType+"')");
    }

    /***
     * here we have the loop that makes sures it returns a Integer when we need it
     * @return A int
     */
    public static int loop()
    {
        Scanner in = new Scanner(System.in);
        String sInput = "";
        boolean done = false;
        while (!done) {
            if (in.hasNextInt())
            {
                sInput = in.next();
                done = true;
            }
            else {
                System.out.println("try again");
                sInput = in.next();
            }
        }
        int input = Integer.parseInt(sInput);
        return input;
    }
}
