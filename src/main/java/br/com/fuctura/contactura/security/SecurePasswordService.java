package br.com.fuctura.contactura.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;


@Service
public class SecurePasswordService {
	
	public String encode(String password) {
		return this.get_SHA_256_SecurePassword(password, "");       

	}

	private String get_SHA_256_SecurePassword(String passwordToHash,
            String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
	
	// Add salt
    private String getSalt() throws NoSuchAlgorithmException {
    	 return "fcta"; 
    }
}
