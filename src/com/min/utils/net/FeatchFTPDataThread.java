package com.min.utils.net;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FTP文件下载线程 持续从FTP服务器下载文件到指定的local目录
 * @author chengmin
 *
 */
public class FeatchFTPDataThread extends Thread {
  
  private static final Logger logger = LogManager.getLogger();

  String server = "192.168.1.121";
  int port = 21;

  String username = "anonymous";
  String password = "";

  String remote = "123/";
  String local = "/Users/chengmin/Documents/testDir";
  
  DataTransService dataTransService;
  
  public FeatchFTPDataThread() {
    dataTransService = new DataTransServiceImpl();
    dataTransService.setServerConfig(server, port, username, password);
  }
  
  public void run() {
    logger.info("Begin to featch remote FTP files ...");
    while(true) {
      try {
        dataTransService.downloadRemoteData(remote, local);
        Thread.sleep(1 * 60 * 1000);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
  
}
