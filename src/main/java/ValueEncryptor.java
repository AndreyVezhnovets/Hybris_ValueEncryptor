import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

public class ValueEncryptor {

    private static final SecureRandom random = new SecureRandom();
    private static final String keyPath = "E:\\SpringExamples\\dfvbdfb\\src\\main\\java\\default-128-bit-aes-key.hybris";
    private static final String signature = "BC";
    private static final String symmetricCipher = "AES";
    private static final String symmetricAlgorithm = "PBEWITHSHA-256AND256BITAES-CBC-BC";
    private static final String password = "1234567";


    protected Cipher getCipher() throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
        return Cipher.getInstance(symmetricCipher, signature);
    }

    public static SecretKey loadKey(String keyPath) throws IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Security.addProvider(new BouncyCastleProvider());
        FileInputStream fis = new FileInputStream(new File(keyPath));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int index;
        while ((index = fis.read()) != -1) {
            outputStream.write(index);
        }
        fis.close();
        byte[] saltAndKeyBytes = outputStream.toByteArray();
        outputStream.close();
        byte[] salt = new byte[8];
        System.arraycopy(saltAndKeyBytes, 0, salt, 0, 8);
        int length = saltAndKeyBytes.length - 8;
        byte[] encryptedKeyBytes = new byte[length];
        System.arraycopy(saltAndKeyBytes, 8, encryptedKeyBytes, 0, length);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(symmetricAlgorithm);
        SecretKey pbeKey = keyFactory.generateSecret(pbeKeySpec);
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 1000);
        Cipher cipher = Cipher.getInstance(symmetricAlgorithm);
        cipher.init(2, pbeKey, pbeParamSpec);
        byte[] decryptedKeyBytes = cipher.doFinal(encryptedKeyBytes);
        return new SecretKeySpec(decryptedKeyBytes, symmetricCipher);
    }


    public String decrypt(String text) {
        if (text.length() < 24) {
            throw new InvalidParameterException("Corrupted ciphertext! (too short)");
        } else {
            try {
                int index = text.indexOf(":");
                String text0 = text.substring(index + 1);
                Cipher cipher = this.getCipher();
                SecretKey key = loadKey(keyPath);


                String salt = text0.substring(0, 24);
                byte[] saltArray = Base64.decode(salt);
                String cipherText = text0.substring(24);
                IvParameterSpec spec = new IvParameterSpec(saltArray);
                cipher.init(2, key, spec);
                byte[] cipherTextArray = Base64.decode(cipherText);
                byte[] plaintextArray = cipher.doFinal(cipherTextArray);
                return new String(plaintextArray);
            } catch (Exception var10) {
                throw new IllegalStateException("error in decryption", var10);
            }
        }
    }

    public String encrypt(String plaintext) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidKeySpecException {
        SecretKey secretKey = loadKey(keyPath);
        return encrypt(secretKey, plaintext);
    }

    private String encrypt(SecretKey key, String plaintext) throws NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] byteArray = new byte[16];
        random.nextBytes(byteArray);
        Cipher cipher = this.getCipher();
        IvParameterSpec spec = new IvParameterSpec(byteArray);
        cipher.init(1, key, spec);
        byte[] cipherText = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        String saltString = Base64.encodeBytes(byteArray, 8);
        String cipherTextString = Base64.encodeBytes(cipherText, 8);
        return "1" + ":" + saltString + cipherTextString;
    }
}
