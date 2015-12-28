package com.min.utils.net;

public interface DataTransService {

  /**
   * 
   * @param server
   * @param port
   * @param username
   * @param password
   */
  public void setServerConfig(String server, int port, String username, String password);
  
  /**
   * 
   * @param remote  the remote FTPServer file path
   * @param local   the local file path
   * @return
   */
  public boolean downloadRemoteData(String remote, String local);
  
  /**
   * 
   * @param remote  the remote FTPServer file path
   * @param local   the local file path
   * @return
   */
  public boolean uploadLocalData(String remote, String local);
  
}
