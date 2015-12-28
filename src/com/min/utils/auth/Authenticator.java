package com.min.utils.auth;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * 
 * @author chengmin
 *
 */
public final class Authenticator {

  private final byte[] key;
  private final byte[] opc;

  private byte[] res;
  private byte[] ck;
  private byte[] ik;

  private boolean isAuthenticated;

  private Authenticator(byte[] key, byte[] opc) {
    this.key = key;
    this.opc = opc;
  }

  public static Authenticator getInstance(byte[] key, byte[] opc) {
    return new Authenticator(key, opc);
  }

  public void authenticate(byte[] rand, byte[] autn) throws InvalidKeyException,
      ArrayIndexOutOfBoundsException, NoSuchAlgorithmException, NoSuchPaddingException,
      IllegalBlockSizeException, BadPaddingException {

    if (rand.length != 16 || autn.length != 16) {
      return;
    }

    isAuthenticated = false;

    byte[] autnfix6 = new byte[6];
    for (int i = 0; i < autnfix6.length; i++) {
      autnfix6[i] = autn[i];
    }
    byte[] amf = new byte[2];
    for (int i = 0; i < amf.length; i++) {
      amf[i] = autn[i + 6];
    }

    byte[] inputmac_a = new byte[8];
    for (int i = 0; i < inputmac_a.length; i++) {
      inputmac_a[i] = autn[i + 8];
    }

    byte[] ak = Milenage.f5(key, opc, rand);
    byte[] sqn = new byte[6];
    for (int i = 0; i < ak.length; i++) {
      sqn[i] = (byte) (autnfix6[i] ^ ak[i]);
    }

    byte[] mac_a = Milenage.f1(key, opc, rand, sqn, amf);
    res = Milenage.f2(key, opc, rand);
    ck = Milenage.f3(key, opc, rand);
    ik = Milenage.f4(key, opc, rand);

    isAuthenticated = checkIsAuthenticated(inputmac_a, mac_a);
  }

  protected boolean checkIsAuthenticated(byte[] inputmac_a, byte[] mac_a) {
    for (int i = 0; i < mac_a.length; i++) {
      if (mac_a[i] != inputmac_a[i]) {
        return false;
      }
    }
    return true;
  }

  public byte[] getRes() {
    return res;
  }

  public byte[] getCk() {
    return ck;
  }

  public byte[] getIk() {
    return ik;
  }

  public boolean isAuthenticated() {
    return isAuthenticated;
  }

}
