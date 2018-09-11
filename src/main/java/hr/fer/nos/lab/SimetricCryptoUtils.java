package hr.fer.nos.lab;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p>Created: 2018-05-20 10:42:26 AM</p>
 *
 * @author marin
 */
public class SimetricCryptoUtils {

    private static final String AES_INIT_VECTOR = "RandomInitVector";
    private static final String DES_INIT_VECTOR = "InitialV";

    public static String encrypt(final byte[] key, final byte[] value, final String method,
            final String cryptWay) {
        try {
            final IvParameterSpec iv = ("AES".equals(method)) ? new IvParameterSpec(AES_INIT_VECTOR.getBytes("UTF-8"))
                    : new IvParameterSpec(DES_INIT_VECTOR.getBytes("UTF-8"));
            final SecretKeySpec skeySpec = new SecretKeySpec(key, method);

            final Cipher cipher = Cipher.getInstance(method + "/" + cryptWay + "/PKCS5PADDING");
            if ("ECB".equals(cryptWay)) {
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            }
            else {
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            }
            final byte[] encrypted = cipher.doFinal(value);

            return Base64.getEncoder().encodeToString(encrypted);
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(final byte[] key, final byte[] encrypted, final String method,
            final String cryptWay) {
        try {
            final IvParameterSpec iv = ("AES".equals(method)) ? new IvParameterSpec(AES_INIT_VECTOR.getBytes("UTF-8"))
                    : new IvParameterSpec(DES_INIT_VECTOR.getBytes("UTF-8"));

            final SecretKeySpec skeySpec = new SecretKeySpec(key, method);

            final Cipher cipher = Cipher.getInstance(method + "/" + cryptWay + "/PKCS5PADDING");
            if ("ECB".equals(cryptWay)) {
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            }
            else {
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            }

            final byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            return new String(original);
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String execute(final byte[] value, final byte[] key, final String method,
            final String cryptWay, final boolean decrypt) {
        String result = "";
        if (decrypt) {
            result = decrypt(key, value, method, cryptWay);
        }
        else {
            result = encrypt(key, value, method, cryptWay);
        }

        return result;
    }

    public static byte[] generateKey(final String method, final int length) throws NoSuchAlgorithmException {
        final KeyGenerator keyGen = KeyGenerator.getInstance(method);
        keyGen.init(length);
        return keyGen.generateKey().getEncoded();
    }

    public static void main(final String[] args) {
        final String key = "Bar12345Bar12345Bar12345"; // 128 bit key Bar12345Bar12345

        System.out.println(decrypt(key.getBytes(), encrypt(key.getBytes(), "Hello World".getBytes(), "DESede", "CBC").getBytes(), "DESede", "CBC"));
    }

}
