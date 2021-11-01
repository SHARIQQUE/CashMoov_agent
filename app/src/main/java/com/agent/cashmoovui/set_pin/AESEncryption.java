package com.agent.cashmoovui.set_pin;



import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Vijay Gupta on 8/8/20.
 */

public class AESEncryption {

  //  private static final Logger LOGGER = LoggerFactory.getLogger(AESEncryption.class);
    static String masterKey = "MTIzNDU2Nzg5MDEyMzQ1Nw==";

    public AESEncryption(){}

    private static SecretKey buildKey() throws Exception {
        byte[] base64decodedBytes = android.util.Base64.decode(masterKey.getBytes(), android.util.Base64.DEFAULT);//
        // Base64.getDecoder().decode(masterKey);
        String base64decodedBytesValue = new String(base64decodedBytes, "utf-8");
        return new SecretKeySpec(base64decodedBytesValue.getBytes(), "AES");
    }

    public static SecretKey buildKey(String key) throws Exception {
        byte[] base64decodedBytes =  android.util.Base64.decode(key.getBytes(), android.util.Base64.DEFAULT);
        //Base64.getDecoder().decode(key);
        String base64decodedBytesValue = new String(base64decodedBytes, "utf-8");
        return new SecretKeySpec(base64decodedBytesValue.getBytes(), "AES");
    }

    public static String asHex(byte[] buf) {
        StringBuilder strbuf = new StringBuilder(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)
                strbuf.append("0");
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }

    public static String getAESEncryption(String plainText){
        String str = null;
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(256);
            SecretKey skey = buildKey();
            byte[] raw = skey.getEncoded();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            // Instantiate the cipher
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            String encHex = asHex(encrypted);
            str = encHex.toUpperCase();
        }
        catch (Exception e){
            //throw e.printStackTrace();
        }
        return str;
    }

    public static String getAESEncryption(String plainText, String skeyVaue) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(256);
        SecretKey skey = buildKey(skeyVaue); //kgen.generateKey();
        byte[] raw = skey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        String encHex = asHex(encrypted);
        return encHex.toUpperCase();
    }

    public static String getAESEncryption(String plainText, SecretKey skey) throws Exception {
        byte[] raw = skey.getEncoded();
        System.out.println(raw);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes());
        String encHex = asHex(encrypted);
        return encHex.toUpperCase();
    }

    public static SecretKey keyGenerator(){
        SecretKey secretKey = null;
        try{
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(256);
            secretKey = kgen.generateKey();
        }catch(Exception e){
            e.getMessage();
        }
        return secretKey;
    }

    public static String getAESDecryption(String encryptedHex){
        String str = null;
        try {
            byte[] pinkey = hexToBytes(encryptedHex);
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(256);
            SecretKey skey = buildKey();
            byte[] raw = skey.getEncoded();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] original = cipher.doFinal(pinkey);
            str = new String(original);
        }
        catch (Exception e){
          //  throw new BaseRuntimeException(BaseRuntimeException.MessageKey.TECHNICAL_FAILURE.getKey(), ResultCodeDescription.MessageDescKey.TECHNICAL_FAILURE.getKey());
        }
        return str;
    }

    public static String getAESDecryption(String encryptedHex , SecretKey skey) throws Exception {
        byte[] pinkey = hexToBytes(encryptedHex);
        byte[] raw = skey.getEncoded();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] original = cipher.doFinal(pinkey);
        return new String(original);
    }

    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(
                        str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    public static void main(String[] args) {
        try{
            for(int i = 0; i <= 10; i++){
            //String str1 = AESEncryption.getAESEncryption("password");
            //String str2 = AESEncryption.getAESEncryption("estel");
            //String str3 = AESEncryption.getAESEncryption(new CommonUtility().systemGeneratedPin());
            //System.out.println("Encrypted==>"+ str1 + ":: " + AESEncryption.getAESDecryption(str1));
            //System.out.println("Encrypted==>"+ str2 + ":: " + AESEncryption.getAESDecryption(str2));
            //System.out.println("Encrypted==>:: " + new CommonUtility().generateRandomNumber(4));
            }

        }catch (Exception ex) {
           // LOGGER.error(ex.getMessage(), ex);
            ex.getMessage();

        }
    }

}
