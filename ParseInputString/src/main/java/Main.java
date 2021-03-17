import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
//        String strInput1 = "3[xyz]4[xy]z";                   //xyzxyzxyzxyxyxyxyz
        String strInput1 = "2[3[x]y]";

        Pattern pattern = Pattern.compile("\\d+\\[[a-zA-Z]+\\][a-zA-Z]*");
        Matcher matcher = pattern.matcher(strInput1);

        System.out.println(strInput1);
        while (matcher.find()) {
            strInput1 = matcher.replaceFirst(inBracket(strInput1.substring(matcher.start(), matcher.end())));
            Matcher matcher1 = pattern.matcher(strInput1);
            if (matcher1.find()) {
                strInput1 = matcher1.replaceFirst(inBracket(strInput1.substring(matcher1.start(), matcher1.end())));
                break;
            }
        }
        System.out.println(strInput1);
    }

    public static String inBracket(String str) {
        Pattern pattern = Pattern.compile("\\d*");
        Matcher matcher = pattern.matcher(str);
        int countRepeat = 0;
        if (matcher.find()) {
            countRepeat = Integer.parseInt(str.substring(matcher.start(), matcher.end()));
        }

        String tempStr = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
        String returnStr = "";
        for (int i = 0; i < countRepeat; i++) {
            returnStr += tempStr;
        }
        if (str.indexOf("]") != str.length() - 1) {
            returnStr += str.substring(str.indexOf("]") + 1);
        }
        return returnStr;
    }
}