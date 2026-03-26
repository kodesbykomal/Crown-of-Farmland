package farmland;

/**
 * Handles strict output formatting rules.
 */
public class OutputFormatter {

    public static final int LINE_WIDTH = 31;

    /* ================= 31 CHAR RULE ================= */

    public static String formatToWidth(String input) {

        if (input.length() > LINE_WIDTH) {
            return input.substring(0, LINE_WIDTH);
        }

        StringBuilder builder = new StringBuilder(input);

        while (builder.length() < LINE_WIDTH) {
            builder.append(" ");
        }

        return builder.toString();
    }

    /* ================= ERROR ================= */

    public static void error(String msg) {
        System.out.println("ERROR: " + msg);
    }

    /* ================= HEADER ================= */

    public static void printHeader(String content) {
        System.out.println(formatToWidth(content));
    }
}