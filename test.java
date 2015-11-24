/**
 * Created by Stefan on 20-03-2015.
 */
public class test {
    public static void main(String[] args) {
        String what = "/nick whats up ";
        String cmd = what.split("\\s+")[0]; // filters first part before whitespace as command
        what = what.split("\\s+", 2)[1]; // sets the rest of the message as the new message
        System.out.println(cmd + " ::: " + what);
    }
}
