package org.qinian.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Data
public class ValidateIdCardUtil {
    /**
     * 每一位的权重
     */
    private static final int[] WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    /**
     * 校验码对应表
     */
    private static final char[] CHECK_CODE = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    /**
     * 正则表达式匹配澳门身份证号码
     */
    private static final Pattern MACAU_ID_PATTERN = Pattern.compile("[1|5|7][0-9]{6}\\(?[0-9A-Z]\\)?");

    private static final Map<Character, Integer> LETTER_TO_NUMBER_MAP = new HashMap<>();

    static {
        LETTER_TO_NUMBER_MAP.put('A', 10);
        LETTER_TO_NUMBER_MAP.put('B', 11);
        LETTER_TO_NUMBER_MAP.put('C', 12);
        LETTER_TO_NUMBER_MAP.put('D', 13);
        LETTER_TO_NUMBER_MAP.put('E', 14);
        LETTER_TO_NUMBER_MAP.put('F', 15);
        LETTER_TO_NUMBER_MAP.put('G', 16);
        LETTER_TO_NUMBER_MAP.put('H', 17);
        LETTER_TO_NUMBER_MAP.put('J', 18);
        LETTER_TO_NUMBER_MAP.put('K', 19);
        LETTER_TO_NUMBER_MAP.put('L', 20);
        LETTER_TO_NUMBER_MAP.put('M', 21);
        LETTER_TO_NUMBER_MAP.put('N', 22);
        LETTER_TO_NUMBER_MAP.put('P', 23);
        LETTER_TO_NUMBER_MAP.put('Q', 24);
        LETTER_TO_NUMBER_MAP.put('R', 25);
        LETTER_TO_NUMBER_MAP.put('S', 26);
        LETTER_TO_NUMBER_MAP.put('T', 27);
        LETTER_TO_NUMBER_MAP.put('U', 28);
        LETTER_TO_NUMBER_MAP.put('V', 29);
        LETTER_TO_NUMBER_MAP.put('W', 30);
        LETTER_TO_NUMBER_MAP.put('X', 31);
        LETTER_TO_NUMBER_MAP.put('Y', 32);
        LETTER_TO_NUMBER_MAP.put('Z', 33);
    }

    /**
     * 大陆居民身份证号码为18位数字，前17位为出生日期、地区代码等信息，第18位为校验码。校验码根据前17位数字计算得出
     *
     * @param idCard17
     * @return
     */
    private static char getCheckCode(String idCard17) {
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (idCard17.charAt(i) - '0') * WEIGHT[i];
        }
        return CHECK_CODE[sum % 11];
    }

    public static boolean validateMainlandId(String idCard) {
        if (idCard == null || !idCard.matches("[1-9][0-9]{5}(19[0-9]{2}|20[0-9]{2})(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[0-9]{3}[0-9Xx]")) {
            return false;
        }

        String idCard17 = idCard.substring(0, 17);
        char checkCode = getCheckCode(idCard17);

        // 大陆身份证校验码为最后一位
        return Character.toUpperCase(idCard.charAt(17)) == checkCode;
    }

    public static boolean validateTaiwanId(String id) {
        if (id == null || !id.matches("[A-Z][0-9]{9}")) {
            return false;
        }

        char letter = id.charAt(0);
        int letterValue = LETTER_TO_NUMBER_MAP.get(letter);

        int letter1 = letterValue / 10;
        int letter2 = letterValue % 10;

        int sum = letter1 + letter2 * 9;
        int[] weights = {8, 7, 6, 5, 4, 3, 2, 1};
        for (int i = 0; i < 8; i++) {
            sum += Character.getNumericValue(id.charAt(i + 1)) * weights[i];
        }
        sum += Character.getNumericValue(id.charAt(9));
        return sum % 10 == 0;
    }

    public static boolean validateMacauId(String id) {
        if (id == null || !MACAU_ID_PATTERN.matcher(id).matches()) {
            return false;
        }

        int[] weights = {8, 7, 6, 5, 4, 3, 2, 1};
        int sum = 0;
        for (int i = 0; i < 7; i++) {
            char c = id.charAt(i);
            sum += Character.getNumericValue(c) * weights[i];
        }

        char checkChar = id.charAt(7);
        int checkCode;
        if (checkChar == 'A') {
            checkCode = 10;
        } else {
            checkCode = Character.getNumericValue(checkChar);
        }

        return (sum + checkCode) % 11 == 0;
    }

    public static boolean validateHongKongId(String id) {
        if (id == null || !id.matches("[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?")) {
            return false;
        }
        id = id.replace("(", "").replace(")", "");

        if (id.length() == 8) {
            id = " " + id;
        }

        if (id.length() != 9) {
            return false;
        }

        int[] weights = {9, 8, 7, 6, 5, 4, 3, 2, 1};
        int total = 0;
        for (int i = 0; i < 9; i++) {
            total += charToValue(id.charAt(i)) * weights[i];
        }

        int remainder = total % 11;
        char checkDigit = id.charAt(id.length() - 1);

        if (remainder == 0) {
            return checkDigit == '0';
        } else if (remainder == 1) {
            return Character.toUpperCase(checkDigit) == 'A';
        } else {
            return checkDigit == Character.forDigit(11 - remainder, 10);
        }
    }

    private static int charToValue(char c) {
        if (c == ' ') {
            return 36;
        } else if (Character.isLetter(c)) {
            return Character.toUpperCase(c) - 55;
        } else {
            return Character.getNumericValue(c);
        }
    }

    private static IDCardType determineIdCardType(String idCard) {
        if (idCard.matches("[1-9][0-9]{5}(19[0-9]{2}|20[0-9]{2})(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[0-9]{3}[0-9Xx]")) {
            return IDCardType.MAINLAND;
        } else if (idCard.matches("[A-Z][0-9]{9}")) {
            return IDCardType.TAIWAN;
        } else if (idCard.matches("[1|5|7][0-9]{6}\\(?[0-9A-Z]\\)?")) {
            return IDCardType.MACAU;
        } else if (idCard.matches("[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?")) {
            return IDCardType.HONGKONG;
        } else {
            return IDCardType.UNKNOWN;
        }
    }

    public static Boolean doMain(String idCard) {
        IDCardType idCardType = determineIdCardType(idCard);
        System.out.println("idCardType = " + idCardType);
        switch (idCardType) {
            case MAINLAND:
                return validateMainlandId(idCard);
            case TAIWAN:
                return validateTaiwanId(idCard);
            case MACAU:
                return validateMacauId(idCard);
            case HONGKONG:
                return validateHongKongId(idCard);
            default:
                return Boolean.FALSE;
        }
    }

    public static void main(String[] args) {
        System.out.println(doMain("37131220020802673x"));
        System.out.println(doMain("X219100167"));
//        System.out.println(doMain("37131220020802673x"));
    }

    enum IDCardType {
        MAINLAND,
        TAIWAN,
        MACAU,
        HONGKONG,
        UNKNOWN
    }
}
