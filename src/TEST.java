import java.util.Scanner;

public class TEST {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int input = in.nextInt();
        Menu(input);
    }
    public static void Menu(int input)
    {
        switch (input)
        {
            case 1:
                System.out.println("hello");
                break;
            case 2:
                MenuPayment();
                break;
            default:
                break;
        }
    }
    public static void MenuPayment()
    {
        System.out.println("Payment menu");
    }
}

