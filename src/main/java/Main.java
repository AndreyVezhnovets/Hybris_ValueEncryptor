import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

public class Main {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException,
            InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, NoSuchProviderException, IllegalBlockSizeException {

        String decr0 = "12345678";

        ValueEncryptor valueEncryptor = new ValueEncryptor();
        String encr1 = valueEncryptor.encrypt(decr0);
        String decr = valueEncryptor.decrypt(encr1);
        System.out.println(decr.equals(decr0));

    }
}
