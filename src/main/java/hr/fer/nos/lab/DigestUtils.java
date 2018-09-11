package hr.fer.nos.lab;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * <p>Created: 2018-05-21 5:12:36 PM</p>
 *
 * @author marin
 */
public class DigestUtils {

    public static String getDigestedMessage(final byte[] value, final String method)
            throws NoSuchAlgorithmException, NoSuchProviderException {
        final MessageDigest md = MessageDigest.getInstance(method);
//        final MessageDigest md = MessageDigest.getInstance(method, "BC");

        md.update(value);

        final byte[] mdbytes = md.digest();

        final StringBuffer hexString = new StringBuffer();
        for (final byte mdbyte : mdbytes) {
            hexString.append(Integer.toHexString(0xFF & mdbyte));
        }

        System.out.println("Hex format : " + hexString.toString());

        return hexString.toString();
    }

    public static void main(final String[] args) throws Exception {
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        System.out.println("Hex format : "
                + getDigestedMessage(FileUtils.readFile("/home/marin/dat.txt"), "SHA3-256"));

    }

}
