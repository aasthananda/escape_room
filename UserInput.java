import java.util.Scanner;

public class UserInput
{
    public static String getValidInput(String[] validInputs)
    {
        String input = "";
        boolean valid = false;

        do
        {
            input = getLine().toLowerCase();

            for (String str : validInputs)
            {
                if (input.equals(str.toLowerCase()))
                {
                    valid = true;
                }
            }

            if (!valid)
            {
                System.out.print("Invalid input. Please try again\n>");
            }

        } while (!valid);

        return input;
    }

    public static String getLine()
    {
        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
}
