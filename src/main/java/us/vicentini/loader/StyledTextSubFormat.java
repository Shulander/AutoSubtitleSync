package us.vicentini.loader;

/**
 *
 * @author teras
 */
public abstract class StyledTextSubFormat extends ITextSubFormat {

    protected long parseNumber(String value) {
        long ret = 0;
        value = value.toLowerCase();
        try {
            if (value.startsWith("&h")) {
                value = value.substring(2);
                if (value.endsWith("&")) {
                    value = value.substring(0, value.length() - 1);
                }
                ret = Long.parseLong(value, 16);
            } else {
                ret = Long.parseLong(value);
            }
        } catch (NumberFormatException e) {
        }
        return ret;
    }

    protected String produceHexNumber(long number, boolean trailing_and, int length) {
        String n = Long.toHexString(number).toUpperCase();
        n = zeros.substring(0, length - n.length()) + n;
        return "&H" + n + (trailing_and ? "&" : "");
    }
    private final static String zeros = "0000000000000000";
}
