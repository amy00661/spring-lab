package com.example.common.core.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class PasswordHashUtil {

    private static final int DEFAULT_STRENGTH = 10; // 4~31，越大越慢
    private static final BCryptPasswordEncoder DEFAULT_ENCODER =
            new BCryptPasswordEncoder(DEFAULT_STRENGTH);

    private PasswordHashUtil() {
        // 工具類不需要實例化
    }

    /**
     * 使用預設強度(10)產生 BCrypt 雜湊字串。
     * @param raw 原始密碼（不可為 null）
     * @return BCrypt 雜湊字串（包含版本、成本、鹽值與雜湊）
     */
    public static String hash(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("raw password must not be null");
        }
        return DEFAULT_ENCODER.encode(raw);
    }

    /**
     * 使用自訂強度產生 BCrypt 雜湊字串。
     * @param raw 原始密碼
     * @param strength 成本參數(4~31)，越大運算越慢也越安全
     */
    public static String hash(String raw, int strength) {
        if (raw == null) {
            throw new IllegalArgumentException("raw password must not be null");
        }
        if (strength < 4 || strength > 31) {
            throw new IllegalArgumentException("strength must be between 4 and 31");
        }
        return new BCryptPasswordEncoder(strength).encode(raw);
    }

    /**
     * 驗證原始密碼與雜湊是否匹配。
     */
    public static boolean matches(String raw, String encoded) {
        if (raw == null || encoded == null) {
            return false;
        }
        return DEFAULT_ENCODER.matches(raw, encoded);
    }

    /**
     * 方便本地快速產生雜湊：直接執行 main 輸入明文即可得到 BCrypt。
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("用法: java PasswordHashUtil <plainPassword> [strength]");
            return;
        }
        String plain = args[0];
        String hashed = args.length >= 2
                ? hash(plain, Integer.parseInt(args[1]))
                : hash(plain);
        System.out.println("BCrypt: " + hashed);
    }
}
