package cs455.scaling.util;

import java.security.MessageDigest;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {

    private MessageDigest digest = null;
    
    public HashGenerator() {
	try {
	    digest = MessageDigest.getInstance("SHA1");
	} catch (NoSuchAlgorithmException nsae) {
	    System.out.println(nsae.getMessage());
	}
    }

    public String SHA1FromBytes(byte[] data) {
	byte[] hash = digest.digest(data);
	BigInteger hashInt = new BigInteger(1, hash);
	return hashInt.toString(16);
    }
    
}
