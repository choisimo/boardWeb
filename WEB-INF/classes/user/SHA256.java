/*
 * package board;
 * 
 * import java.security.*;
 * 
 * public class SHA256 {
 * 
 * static String salt = "Imnotcat";
 * 
 * static public String endcodSha256(String source) { String result =""; // 소스를
 * 바이트로 변경 byte[] a = source.getBytes(); byte[] salt = salt.getBytes(); byte[]
 * bytes = new byte[a.length + salt.length];
 * 
 * System.arraycopy(a, 0, bytes, 0, a.length); System.arraycopy(salt, 0, bytes,
 * 0, a.length, salt.length);
 * 
 * try { MessageDigest md = MessageDigest.getInstance("SHA-256");
 * md.update(bytes);
 * 
 * byte[] byteData = md.digest();
 * 
 * StringBuffer sb = new StringBuffer(); for (int i = 0; i < byteData.length;
 * i++) { sb.append(Integer.toString((byteData[i]&0xFF) +256, 16).substring(1));
 * } result = sb.toString();
 * 
 * } catch(Exception e) { e.printStackTrace(); } return result; } }
 */

package user;

import java.security.*;

public class SHA256 {

    static String Salt = 
    		"bjTQ&#fPq68E6vfA7QX%XJkYJ5u6E3WoyPEjpEL"
    		+ "dGAez#eGYPUF3V69R$i@*p"
    		+ "G7d6aZn8#VpVwehM2m@2@9GmM"
    		+ "y4JivUg#LH@rysKM7QdAsRW"
    		+ "ar$V3@ZoL8UgpJDxzBH"; 
    		// 사용할 솔트 값을 정의합니다.

    static public String endcodSha256(String source) {
        String result = ""; // 최종 해시 결과를 저장할 변수를 초기화합니다.

        // 입력된 문자열을 바이트 배열로 변환합니다.
        byte[] a = source.getBytes();

        // 솔트 값을 바이트 배열로 변환합니다.
        byte[] salt = Salt.getBytes();

        // 입력 문자열 바이트 배열과 솔트 바이트 배열을 합친 새로운 바이트 배열을 생성합니다.
        byte[] bytes = new byte[a.length + salt.length];

        // 입력 문자열 바이트 배열을 새로운 바이트 배열에 복사합니다.
        System.arraycopy(a, 0, bytes, 0, a.length);

        // 솔트 바이트 배열을 새로운 바이트 배열에 복사합니다.
        System.arraycopy(salt, 0, bytes, a.length, salt.length);

        try {
            // SHA-256 해시 알고리즘을 사용할 메시지 다이제스트 객체를 생성합니다.
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // 합쳐진 바이트 배열을 업데이트하고 해시를 계산합니다.
            md.update(bytes);

            // 해시된 데이터를 바이트 배열로 얻습니다.
            byte[] byteData = md.digest();

            // 해시된 데이터를 16진수 문자열로 변환하여 문자열을 생성합니다.
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xFF) + 256, 16).substring(1));
            }
            result = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result; // 최종 해시 값을 반환합니다.
    }
}
