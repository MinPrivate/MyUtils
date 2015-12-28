package com.min.utils.net;

import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

public interface FtpClient {
  
  /**
   * create a FTPClient
   */
  public void createFTPClient();
  
  /**
   * create a FTPSClient
   * 
   * the param protocol format is follow:
   * protocol: "true"
   * protocol: "false"
   * protocol: host
   * Protocol: host,port
   * @param protocol
   * @return
   */
  public void createFTPClient(String protocol);
  
  /**
   * create FTPHTTPClient server using HTTP proxy
   * 
   * @param proxyHost
   * @param proxyPort
   * @param proxyUser
   * @param proxyPassword
   */
  public void createFTPClient(String proxyHost,int proxyPort, String proxyUser, String proxyPassword);
  
  /**
   * the setting before the FTPClient connected
   * 
   * using the default setting
   */
  public void setFTPClientConfig();
  
  /**
   * the setting before the FTPClient connected
   * 
   * @param trustMgr
   * @param printHash
   * @param keepAliveTimeout
   * @param controlKeepAliveReplyTimeout
   * @param hidden
   */
  public void setFTPClientConfig(TrustMgr trustMgr, boolean printHash,
      long keepAliveTimeout, int controlKeepAliveReplyTimeout, boolean hidden);
  
  /**
   * connect to the remote FTPServer
   * 
   * @param server
   * @param username
   * @param password
   * @throws SocketException
   * @throws IOException
   */
  public void connect(String server, String username, String password) throws SocketException, IOException;
  
  /**
   * connect to the remote FTPServer
   * 
   * @param server
   * @param port
   * @param username
   * @param password
   * @throws SocketException
   * @throws IOException
   */
  public void connect(String server, int port, String username, String password) throws SocketException, IOException;
  
  /**
   * the setting after the FTPClient connected
   * 
   * using the default setting
   * 
   * @throws IOException
   */
  public void connectedSetting() throws IOException;
  
  /**
   * the setting after the FTPClient connected
   * 
   * @param binaryTransfer
   * @param localActive
   * @param useEpsvWithIPv4
   * @throws IOException
   */
  public void connectedSetting(boolean binaryTransfer, boolean localActive, boolean useEpsvWithIPv4) throws IOException;
  
  /**
   * download file from FTPServer
   * 
   * @param remote      remote file in FTPServer
   * @param local       local file
   * @return
   * @throws IOException
   */
  public boolean download(String remote, String local) throws IOException;
  
  /**
   * upload file to FTPServer
   * 
   * @param remote      remote file in FTPServer
   * @param local       local file
   * @return
   * @throws IOException
   */
  public boolean upload(String remote, String local) throws IOException;
  
  /**
   * list all files in remote FTPServer path
   * 
   * using the default setting
   * 
   * @param remote      remote path or file in FTPServer
   * @return
   * @throws IOException
   */
  public FTPFile[] listFiles(String remote) throws IOException;
  
  /**
   * list all files in remote FTPServer path
   * 
   * @param remote      remote path or file in FTPServer
   * @param lenient
   * @return
   * @throws IOException
   */
  public FTPFile[] listFiles(String remote, boolean lenient) throws IOException;
  
  /**
   * 
   * @param remote
   * @param filter
   * @return
   * @throws IOException
   */
  public FTPFile[] listFiles(String remote, FTPFileFilter filter) throws IOException;
  
  
  /**
   * delete the remoteFile from FTPServer
   * 
   * @param remoteFile      remoteFile to be deleted in FTPServer
   * @return
   * @throws IOException
   */
  public boolean deleteRemoteFile(String remoteFile) throws IOException;
  
  /**
   * 
   * @param remoteFile
   * @param newName
   * @return
   * @throws IOException
   */
  public boolean renameRemoteFile(String remoteFile, String newName) throws IOException;
  
  /**
   * logout from FTPServer
   */
  public void logout();
  
  public enum TrustMgr {
    ALL, VALID, NONE, NOSET
  }
}
