package com.min.utils.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * md5加密算法
 * 用于网站服务器请求的合法性验证
 * 其中CHANNEL_KEY为服务器和请求服务方协定的密匙，用以判断请求体的合法性
 * 
 * @author chengmin
 *
 */
public class Md5 {
  
  private static final Logger logger = LogManager.getLogger();
  private static final String CHANNEL_KEY = "X5dtxerPeg9eWuTk4NdAYN1awkeB4Srl";
  
  public static String md5(String body) {
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      logger.error(e.getLocalizedMessage());
    }
    md.update(body.concat(CHANNEL_KEY).getBytes());
    byte byteData[] = md.digest();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < byteData.length; i++) {
      sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
    }

    return sb.toString();
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    System.out.println(md5("123"));

  }

}
