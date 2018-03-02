package cs455.scaling.util;

import java.security.MessageDigest;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {
    
    public HashGenerator() {
        
    }

    public String SHA1FromBytes(byte[] data) {
	MessageDigest digest = null;
	try {
	    digest = MessageDigest.getInstance("SHA1");
	} catch (NoSuchAlgorithmException nsae) {
	    System.out.println(nsae.getMessage());
	}
	byte[] hash = digest.digest(data);
	BigInteger hashInt = new BigInteger(1, hash);
	return hashInt.toString(16);
    }
    
}
