package ru.somarov.templates.java.pattern.state_machine;

public class Test {
}
//Not a good example, see spring state machine
class CombinationLock
{
    private int [] combination;
    private int digitsEntered;
    public String status;

    public boolean failed = false;

    public CombinationLock(int[] combination)
    {
        this.combination = combination;
        reset();
    }

    public void reset() {
        status = "LOCKED";

        failed = false;
        digitsEntered = 0;
    }

    public void enterDigit(int digit)
    {
        if (status.equals("LOCKED")) status = "";
        status += digit;
        if (combination[digitsEntered] != digit)
        {
            failed = true;
        }
        digitsEntered++;

        if (digitsEntered == combination.length)
            status = failed ? "ERROR" : "OPEN";

    }


}

