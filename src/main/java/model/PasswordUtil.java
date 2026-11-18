package model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Gera e confere hashes de senha (PBKDF2 com sal aleatório) usando apenas o JDK.
 * Formato armazenado: iteracoes:salBase64:hashBase64
 */
public class PasswordUtil {

	private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
	private static final int ITERATIONS = 65000;
	private static final int KEY_LENGTH_BITS = 256;
	private static final int SALT_BYTES = 16;

	public static String hash(String plain) {
		byte[] salt = new byte[SALT_BYTES];
		new SecureRandom().nextBytes(salt);
		byte[] hash = pbkdf2(plain, salt, ITERATIONS);
		return ITERATIONS + ":" + Base64.getEncoder().encodeToString(salt)
				+ ":" + Base64.getEncoder().encodeToString(hash);
	}

	public static boolean verify(String plain, String stored) {
		if (plain == null || stored == null)
			return false;

		String[] parts = stored.split(":");
		if (parts.length != 3)
			return false;

		try {
			int iterations = Integer.parseInt(parts[0]);
			byte[] salt = Base64.getDecoder().decode(parts[1]);
			byte[] expected = Base64.getDecoder().decode(parts[2]);
			byte[] actual = pbkdf2(plain, salt, iterations);
			return MessageDigest.isEqual(expected, actual);
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private static byte[] pbkdf2(String plain, byte[] salt, int iterations) {
		try {
			PBEKeySpec spec = new PBEKeySpec(plain.toCharArray(), salt, iterations, KEY_LENGTH_BITS);
			return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new IllegalStateException("Falha ao gerar hash de senha", e);
		}
	}

	// Usado apenas para gerar os hashes do script SQL: java model.PasswordUtil <senha>
	public static void main(String[] args) {
		System.out.println(hash(args[0]));
	}
}
