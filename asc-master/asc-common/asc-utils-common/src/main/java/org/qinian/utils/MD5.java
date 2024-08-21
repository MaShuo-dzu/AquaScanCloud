/*
 * Copyright (c) 2024 Leyramu. All rights reserved.
 * This project (Digitalization Education), including its source code, documentation, and any associated materials, is the intellectual property of Leyramu. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, including photocopying, recording, or other electronic or mechanical methods, without the prior written permission of the copyright owner, Miraitowa_zcx, except in the case of brief quotations embodied in critical reviews and certain other noncommercial uses permitted by copyright law.
 * For inquiries related to licensing or usage outside the scope of this notice, please contact the copyright holder at 2038322151@qq.com.
 * The author disclaims all warranties, express or implied, including but not limited to the warranties of merchantability and fitness for a particular purpose. Under no circumstances shall the author be liable for any special, incidental, indirect, or consequential damages arising from the use of this software.
 * By using this project, users acknowledge and agree to abide by these terms and conditions.
 */

package org.qinian.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5 {

    /**
     * MD5 方法
     *
     * @param text 明文
     * @return 密文
     */
    public static String md5(String text) {
        return DigestUtils.md5Hex(text);
    }

    /**
     * MD5 方法
     *
     * @param text 明文
     * @param key  盐
     * @return 密文
     */
    public static String md5(String text, String key) {
        return DigestUtils.md5Hex(text + key);
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param key  密钥/盐
     * @param md5  密文
     * @return 验证结果
     */
    public static boolean verify(String text, String key, String md5) {
        String md5Text = md5(text, key);
        return md5Text.equalsIgnoreCase(md5);
    }
}
