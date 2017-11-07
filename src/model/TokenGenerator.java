package model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

class TokenGenerator {

        private static final String KEY_TOKEN = "u(4TY-dwFg60";

        private static SecureRandom random = new SecureRandom();
        
        static synchronized String generateToken() {
                long longToken = Math.abs( random.nextLong() );
                return md5Custom(KEY_TOKEN)+Long.toString( longToken, 10 );
        }

        public static String md5Custom(String st) {
                MessageDigest messageDigest = null;
                byte[] digest = new byte[0];

                try {
                        messageDigest = MessageDigest.getInstance("MD5");
                        messageDigest.reset();
                        messageDigest.update(st.getBytes());
                        digest = messageDigest.digest();
                } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                }

                BigInteger bigInt = new BigInteger(1, digest);
                String md5Hex = bigInt.toString(16);

                while( md5Hex.length() < 32 ){
                        md5Hex = "0" + md5Hex;
                }

                return md5Hex;
        }
}