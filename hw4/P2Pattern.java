/** P2Pattern class
 *  @author Josh Hug & Vivant Sakore
 */

public class P2Pattern {
    /* Pattern to match a valid date of the form MM/DD/YYYY. Eg: 9/22/2019 */
    public static final String P1 = "(0?[0-9]|1[0-2])/([0-2]?[0-9]|3[0-1])/([1-9]\\d{3})";

    /** Pattern to match 61b notation for literal IntLists. */
    public static final String P2 = "\\((\\d.*)\\d\\)|\\(\\d\\)";
    //"((\\d,\\s)*(\\d)*)"

    /* Pattern to match a valid domain name. Eg: www.support.facebook-login.com */
    public static final String P3 = "(([a-z]|[A-Z]|\\d)([a-z]|[A-Z]|\\d|-)*)" +
            "*((([a-z]|[A-Z]|\\d))\\.([a-z]|[A-Z]|\\d|-)*)*([a-z]|[A-Z]|\\d)" +
            "\\.([a-z]|[A-Z]|\\d|-)?([a-z]|[A-Z]|\\d|-)?([a-z]|[A-Z]|\\d|-)?" +
            "([a-z]|[A-Z]|\\d|-)?([a-z]|[A-Z]|\\d|-)?([a-z]|[A-Z]|\\d|-)?" +
            "([a-z]|[A-Z]|\\d|-)([a-z]|[A-Z]|\\d|-)";

    /* Pattern to match a valid java variable name. Eg: _child13$ */
    public static final String P4 = "([A-z]|_|\\$)([A-z]|_|\\$|\\d)*";

    /* Pattern to match a valid IPv4 address. Eg: 127.0.0.1 */
    public static final String P5 = "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]|" +
            "[0-9]?[0-9]|[0-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]|[0-9]?" +
            "[0-9]|[0-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]|[0-9]?[0-9]|" +
            "[0-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]|[0-9]?[0-9]|[0-9])";
}
