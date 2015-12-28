package com.min.utils.auth;
//import com.google.common.base.Preconditions;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * A sample implementation of the f1, f1*, f2, f3, f4, f5, f5* algorithms, as defined by 3GPP TS
 * 35.206 (MILENAGE Algorithm Set)
 */
public final class Milenage {

    private static byte[] c1 = {  // 128 bits
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };
    private static byte[] c2 = {  // 128 bits
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01
    };
    private static byte[] c3 = {  // 128 bits
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02
    };
    private static byte[] c4 = {  // 128 bits
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04
    };
    private static byte[] c5 = {  // 128 bits
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08};

    private static byte r1 = 0x40;
    private static byte r2 = 0x00;
    private static byte r3 = 0x20;
    private static byte r4 = 0x40;
    private static byte r5 = 0x60;

    /**
     * Algorithm f1. Computes network authentication code MAC-A from key K, random challenge RAND,
     * sequence number SQN and authentication management field AMF.
     *
     * @param key
     * @param rand
     * @param opc
     * @param sqn
     * @param amf
     * @return MAC-A
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] f1(byte[] key, byte[] opc, byte[] rand, byte[] sqn, byte[] amf)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {
//        Preconditions.checkArgument(key.length == 16 && opc.length == 16
//                && rand.length == 16 && sqn.length == 6 && amf.length == 2);

        byte[] sqnAmf = new byte[sqn.length + amf.length];
        System.arraycopy(sqn, 0, sqnAmf, 0, sqn.length);
        System.arraycopy(amf, 0, sqnAmf, sqn.length, amf.length);

        byte[] input = new byte[sqnAmf.length + sqnAmf.length];
        System.arraycopy(sqnAmf, 0, input, 0, sqnAmf.length);
        System.arraycopy(sqnAmf, 0, input, sqnAmf.length, sqnAmf.length);

        byte[] temp = aes(key, xor(rand, opc));
        temp = xor(temp, xor(c1, rotate(r1, xor(input, opc))));

        byte[] MAC_Atemp = xor(opc, aes(key, temp));
        byte[] MAC_A = new byte[8];
        for (int i = 0; i < MAC_A.length; i++) {
            MAC_A[i] = MAC_Atemp[i];
        }
        return MAC_A;
    }

    /**
     *
     * @param key
     * @param rand
     * @param opc
     * @return RES
     * @throws InvalidKeyException
     * @throws ArrayIndexOutOfBoundsException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] f2(byte[] key, byte[] opc, byte[] rand)
            throws InvalidKeyException, ArrayIndexOutOfBoundsException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
//        Preconditions.checkArgument(key.length == 16 && opc.length == 16 && rand.length == 16);
        byte[] out2 = xor(opc, aes(key, xor(c2, rotate(r2, xor(opc, aes(key, xor(opc, rand)))))));
        byte[] res = new byte[8];
        for (int i = 0; i < res.length; i++) {
            res[i] = out2[i + 8];
        }
        return res;

    }

    /**
     * Algorithm f3. Takes key K and random challenge RAND, and returns confidentiality key CK.
     *
     * @param key
     * @param rand
     * @param opc
     * @return CK
     * @throws InvalidKeyException
     * @throws ArrayIndexOutOfBoundsException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] f3(byte[] key, byte[] opc, byte[] rand)
            throws InvalidKeyException, ArrayIndexOutOfBoundsException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
//        Preconditions.checkArgument(key.length == 16 && opc.length == 16 && rand.length == 16);
        byte[] out3 = xor(opc, aes(key, xor(c3, rotate(r3, xor(opc, aes(key, xor(opc, rand)))))));
        byte[] CK = new byte[16];
        for (int i = 0; i < CK.length; i++) {
            CK[i] = out3[i];
        }
        return CK;

    }

    /**
     * Algorithm f4. Takes key K and random challenge RAND, and returns integrity key IK and anonymity
     * key AK.
     *
     * @param key
     * @param rand
     * @param opc
     * @return IK
     * @throws InvalidKeyException
     * @throws ArrayIndexOutOfBoundsException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] f4(byte[] key, byte[] opc, byte[] rand)
            throws InvalidKeyException, ArrayIndexOutOfBoundsException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
//        Preconditions.checkArgument(key.length == 16 && opc.length == 16 && rand.length == 16);
        byte[] out4 = xor(opc, aes(key, xor(c4, rotate(r4, xor(opc, aes(key, xor(opc, rand)))))));
        byte[] IK = new byte[16];
        for (int i = 0; i < IK.length; i++) {
            IK[i] = out4[i];
        }
        return IK;
    }

    /**
     * Algorithm f5. Takes key K and random challenge RAND, and returns anonymity key AK.
     *
     * @param key
     * @param rand
     * @param opc
     * @return AK anonymity key
     * @throws InvalidKeyException
     * @throws ArrayIndexOutOfBoundsException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] f5(byte[] key, byte[] opc, byte[] rand)
            throws InvalidKeyException, ArrayIndexOutOfBoundsException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
//        Preconditions.checkArgument(key.length == 16 && opc.length == 16 && rand.length == 16);
        byte[] out2 = xor(opc, aes(key, xor(c2, rotate(r2, xor(opc, aes(key, xor(opc, rand)))))));
        byte[] AK = new byte[6];
        for (int i = 0; i < AK.length; i++) {
            AK[i] = out2[i];
        }
        return AK;
    }

    /**
     * Algorithm f1*. Computes resynch authentication code MAC-S from key K, random challenge RAND,
     * sequence number SQN and authentication management field AMF.
     *
     * @param key
     * @param rand
     * @param opc
     * @param sqn
     * @param amf
     * @return MAC_S[8]
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] f1star(byte[] key, byte[] opc, byte[] rand, byte[] sqn, byte[] amf)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {
//        Preconditions.checkArgument(key.length == 16 && opc.length == 16
//                && rand.length == 16 && sqn.length == 6 && amf.length == 2);

        byte[] inputtemp = new byte[sqn.length + amf.length];
        System.arraycopy(sqn, 0, inputtemp, 0, sqn.length);
        System.arraycopy(amf, 0, inputtemp, sqn.length, amf.length);
        byte[] input = new byte[inputtemp.length + inputtemp.length];
        System.arraycopy(inputtemp, 0, input, 0, inputtemp.length);
        System.arraycopy(inputtemp, 0, input, inputtemp.length, inputtemp.length);

        byte[] temp = aes(key, xor(rand, opc));
        temp = xor(temp, xor(c1, rotate(r1, xor(input, opc))));

        byte[] MAC_Stemp = xor(opc, aes(key, temp));
        byte[] MAC_S = new byte[8];
        for (int i = 0; i < MAC_S.length; i++) {
            MAC_S[i] = MAC_Stemp[i + 8];
        }
        return MAC_S;
    }

    /**
     * Algorithm f5*. Takes key K and random challenge RAND, and return resynch anonymity key AK
     *
     * @param key
     * @param rand
     * @param opc
     * @return AK
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] f5star(byte[] key, byte[] opc, byte[] rand)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {
//        Preconditions.checkArgument(key.length == 16 && opc.length == 16 && rand.length == 16);
        byte[] out5 = xor(opc, aes(key, xor(c5, rotate(r5, xor(opc, aes(key, xor(opc, rand)))))));
        byte[] AK = new byte[6];
        for (int i = 0; i < AK.length; i++) {
            AK[i] = out5[i];
        }
        return AK;
    }

    private static byte[] rotate(byte r, byte[] s) {
        r = (byte) ((r >> 3) & 0x1f);
        byte[] res = new byte[s.length];
        for (int i = 0; i < s.length; i++) {
            res[i] = s[(i + r) % s.length];
        }
        return res;
    }

    private static byte[] xor(byte[] r, byte[] s) {
//        Preconditions.checkArgument(r.length == s.length);
        byte[] res = new byte[r.length];
        for (int i = 0; i < r.length; i++) {
            res[i] = (byte) (r[i] ^ s[i]);
        }
        return res;
    }

    // TODO: Bug about the bit length
    private static byte[] aes(byte[] key, byte[] data)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        SecretKey keySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] temp = cipher.doFinal(data);
        byte[] result = new byte[temp.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = temp[i];
        }
        return result;
    }

}
