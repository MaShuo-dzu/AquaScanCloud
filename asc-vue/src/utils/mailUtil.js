// emailUtil.js
export default function checkEmailType(email) {
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    const qqEmailRegex = /^1[0-9]{5,11}@qq\.com$/;

    if (emailRegex.test(email)) {
        if (qqEmailRegex.test(email)) {
            return 2;
        }
        return 1;
    }
    return 0;
}