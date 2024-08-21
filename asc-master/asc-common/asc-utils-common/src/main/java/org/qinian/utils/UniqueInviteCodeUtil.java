package org.qinian.utils;

import java.util.Random;

public class UniqueInviteCodeUtil {
    private static final char[] CHARPOOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final Integer STRING_LENGTH = 10;


    public static String generateInviteCode(String username) {
        Random random = new Random();

        StringBuilder sb = new StringBuilder(STRING_LENGTH);
        for (int i = 0; i < STRING_LENGTH; i++) {
            int index = random.nextInt(CHARPOOL.length);
            sb.append(CHARPOOL[index]);
        }
        String randomString = sb.toString();

        return "INV" + randomString + Base64Util.encode(username.getBytes());
    }

    public static void main(String[] args) {
        String inviteCode = generateInviteCode("admin");
        System.out.println("Generated Invite Code: " + inviteCode);
    }
}
