package cs455.scaling.util;

import java.security.MessageDigest;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {
   
    public byte[] SHA1FromBytes(byte[] data) {
	MessageDigest digest = null;
	try {
	    digest = MessageDigest.getInstance("SHA1");
	} catch (NoSuchAlgorithmException nsae) {
	    System.out.println(nsae.getMessage());
	}
	return digest.digest(data);	
    }

    public String convertToString(byte[] hashBytes) {
	BigInteger hashInt = new BigInteger(1, hashBytes);
	return hashInt.toString(16);
    }
    
}
