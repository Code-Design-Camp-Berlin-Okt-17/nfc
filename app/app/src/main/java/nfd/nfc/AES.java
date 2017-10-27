package nfd.nfc;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    public static void encrypt(String keyStr, String toEncrypt) throws Exception {
        byte[] key = (keyStr).getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        key = sha.digest(key);
        // nur die ersten 128 bit nutzen
        key = Arrays.copyOf(key, 16);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

        // Verschluesseln
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());

    }
    public static void decrypt(String keyStr,byte[] encrypted) {
        try {
            byte[] key = new byte[0];
            key = (keyStr).getBytes("UTF-8");
            MessageDigest sha = null;
            sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            // nur die ersten 128 bit nutzen
            key = Arrays.copyOf(key, 16);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher2 = null;
            cipher2 = Cipher.getInstance("AES");
            cipher2.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] cipherData2 = new byte[0];
            cipherData2 = cipher2.doFinal(encrypted);
            String erg = new String(cipherData2);
        } catch (UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new EncryptionException(e);
        }

    }

}