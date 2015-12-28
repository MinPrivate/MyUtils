package com.min.utils.auth;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AuthTest {

  public static void main(String[] args) {
    // TODO Auto-generated method stub

    byte[] key = {(byte) 0x90,0x4F,(byte) 0x87,(byte) 0xF3,(byte) 0xF8,(byte) 0xFF,0x4E,0x28,(byte) 0xF1,0x5B,0x2D,0x4C,0x4A,0x6F,(byte) 0xCF,(byte) 0xCE};
    byte[] opc = {(byte) 0x99,(byte) 0xE1,0x08,0x3A,(byte) 0xB5,0x45,(byte) 0xB2,0x79,0x50,0x5D,(byte) 0xDA,0x38,0x09,(byte) 0xA0,(byte) 0x89,(byte) 0x9F};
    byte[] rand = {0x1C,0x1B,0x39,(byte) 0xAF,(byte) 0xA6,(byte) 0xF8,0x27,(byte) 0xB5,(byte) 0x9D,(byte) 0x82,(byte) 0xF1,(byte) 0x81,(byte) 0x89,0x2C,(byte) 0xA9,0x43};
    byte[] autn = {(byte) 0x91,(byte) 0xAC,0x57,0x78,(byte) 0xEB,0x7A,0x00,0x00,(byte) 0xE2,(byte) 0xBF,(byte) 0xA9,0x0F,0x67,0x5D,(byte) 0xC6,(byte) 0xBE};
  
    Authenticator authenticator = Authenticator.getInstance(key, opc);
    try {
      authenticator.authenticate(rand, autn);
      
      byte[] f2res = authenticator.getRes();
      System.out.print("f2res= ");
      for (int i = 0; i < f2res.length; i++) {
        System.out.print((f2res[i] & 0x0FF) + " ");
      }
      System.out.println();
      
      byte[] f3ck = authenticator.getCk();
      System.out.print("f3ck= ");
      for (int i = 0; i < f3ck.length; i++) {
        System.out.print((f3ck[i] & 0x0FF) + " ");
      }
      System.out.println();
      
      byte[] f4ik = authenticator.getIk();
      System.out.print("f4ik= ");
      for (int i = 0; i < f4ik.length; i++) {
        System.out.print((f4ik[i] & 0x0FF) + " ");
      }
      System.out.println();
      
      
      System.out.println(authenticator.isAuthenticated());
    } catch (InvalidKeyException | ArrayIndexOutOfBoundsException | NoSuchAlgorithmException
        | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
