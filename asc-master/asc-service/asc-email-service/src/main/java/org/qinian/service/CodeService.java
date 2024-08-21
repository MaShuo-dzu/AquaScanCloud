package org.qinian.service;

public interface CodeService {
    public String generateVerifyCode(String mail, int length);

    public Integer checkVerifyCode(String email, String code);
}
