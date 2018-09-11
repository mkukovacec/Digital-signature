package hr.fer.nos.lab;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * <p>Created: 2018-05-20 9:39:44 PM</p>
 *
 * @author marin
 */
public class AsimetricCryptoUtils {

    public static KeyPair buildKeyPair(final String method, final int keysize) throws NoSuchAlgorithmException {
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(method);
        keyPairGenerator.initialize(keysize);
        return keyPairGenerator.genKeyPair();
    }

    public static byte[] encrypt(final Key key, final byte[] message, final String method) throws Exception {
        final Cipher cipher = Cipher.getInstance(method);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        return cipher.doFinal(message);
    }

    public static byte[] decrypt(final Key key, final byte[] encrypted, final String method) throws Exception {
        final Cipher cipher = Cipher.getInstance(method);
        cipher.init(Cipher.DECRYPT_MODE, key);

        return cipher.doFinal(encrypted);
    }

    public static PrivateKey getPrivateKey(final String filename, final String method)
            throws Exception {

        final byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

        final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        final KeyFactory kf = KeyFactory.getInstance(method);
        return kf.generatePrivate(spec);
    }

    public static PublicKey getPublicKey(final String filename, final String method)
            throws Exception {

        final byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

        final X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        final KeyFactory kf = KeyFactory.getInstance(method);
        return kf.generatePublic(spec);
    }

    public static byte[] execute(final byte[] value, final Key key, final String method,
            final boolean decrypt) throws Exception {
        if (decrypt) {
            return decrypt(key, value, method);
        }

        return encrypt(key, value, method);

    }

    public static void main(final String[] args) throws Exception {
        final KeyPair keyPair = buildKeyPair("RSA", 1024);
        final PublicKey pubKey = keyPair.getPublic();
        final PrivateKey privateKey = keyPair.getPrivate();

        final byte[] encrypted = encrypt(pubKey, "This is a secret message".getBytes(), "RSA");

        final byte[] secret = decrypt(privateKey, encrypted, "RSA");
        System.out.println(new String(secret));
    }
}
