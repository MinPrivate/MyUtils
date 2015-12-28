package com.min.utils.net;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
import org.apache.commons.net.util.TrustManagerUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class FtpClientImpl implements FtpClient {
  
  private static final Logger logger = LogManager.getLogger();
  
  private FTPClient ftp;
  
  public FtpClientImpl() {
    ftp = null;
  }
  
  /**
   * create a FTPClient
   */
  @Override
  public void createFTPClient() {
    String protocol = null;
    String proxyHost = null;
    int proxyPort = 80;
    String proxyUser = null;
    String proxyPassword = null;
    createFTPClient(protocol, proxyHost, proxyPort, proxyUser, proxyPassword);
  }
  
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
  @Override
  public void createFTPClient(String protocol) {
    String proxyHost = null;
    int proxyPort = 80;
    String proxyUser = null;
    String proxyPassword = null;
    createFTPClient(protocol, proxyHost, proxyPort, proxyUser, proxyPassword);
  }
  
  /**
   * create FTPHTTPClient server using HTTP proxy
   * 
   * @param proxyHost
   * @param proxyPort
   * @param proxyUser
   * @param proxyPassword
   */
  @Override
  public void createFTPClient(String proxyHost,int proxyPort, String proxyUser, String proxyPassword) {
    String protocol = null;
    createFTPClient(protocol, proxyHost, proxyPort, proxyUser, proxyPassword);
  }
  
  private void createFTPClient(String protocol, 
      String proxyHost, int proxyPort, String proxyUser, String proxyPassword) {
    
    if (protocol == null) {
      if (proxyHost != null) {
        logger.info("Using HTTP proxy server: " + proxyHost);
        ftp = new FTPHTTPClient(proxyHost, proxyPort, proxyUser, proxyPassword);
      } else {
        ftp = new FTPClient();
      }
    } else {
      FTPClient ftps;
      if (protocol.equals("true")) {
        ftps = new FTPSClient(true);
      } else if (protocol.equals("false")) {
        ftps = new FTPSClient(false);
      } else {
        String[] prot = protocol.split(",");
        if (prot.length == 1) { // Just protocol
          ftps = new FTPSClient(protocol);
        } else {
          ftps = new FTPSClient(prot[0], Boolean.parseBoolean(prot[1]));
        }
      }
      ftp = ftps;
    }
  }
  
  /**
   * the setting before the FTPClient connected
   * 
   * using the default setting
   */
  @Override
  public void setFTPClientConfig() {
    TrustMgr trustMgr = TrustMgr.NOSET;
    boolean printHash = false;
    long keepAliveTimeout = -1;
    int controlKeepAliveReplyTimeout = -1;
    boolean hidden = false;
    setFTPClientConfig(trustMgr, printHash, 
        keepAliveTimeout, controlKeepAliveReplyTimeout, hidden);
  }
  
  /**
   * the setting before the FTPClient connected
   * 
   * @param trustMgr
   * @param printHash
   * @param keepAliveTimeout
   * @param controlKeepAliveReplyTimeout
   * @param hidden
   */
  @Override
  public void setFTPClientConfig(TrustMgr trustMgr, boolean printHash,
      long keepAliveTimeout, int controlKeepAliveReplyTimeout, boolean hidden) {
    if (ftp instanceof FTPSClient) {
      FTPSClient ftps = (FTPSClient) ftp;
      if (trustMgr.name().equals(TrustMgr.ALL.name())) {
        ftps.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
      } else if (trustMgr.name().equals(TrustMgr.VALID.name())) {
        ftps.setTrustManager(TrustManagerUtils.getValidateServerCertificateTrustManager());
      } else if (trustMgr.name().equals(TrustMgr.NONE.name())) {
        ftps.setTrustManager(null);
      }
    }
    
    if (printHash) {
      ftp.setCopyStreamListener(createCopyStreamListener());
    }
    if (keepAliveTimeout >= 0) {
      ftp.setControlKeepAliveTimeout(keepAliveTimeout);
    }
    if (controlKeepAliveReplyTimeout >= 0) {
      ftp.setControlKeepAliveReplyTimeout(controlKeepAliveReplyTimeout);
    }
    ftp.setListHiddenFiles(hidden);
  }
  
  /**
   * connect to the remote FTPServer
   * 
   * @param server
   * @param username
   * @param password
   * @throws SocketException
   * @throws IOException
   */
  @Override
  public void connect(String server, String username, String password) throws SocketException, IOException {
    int port = 0;
    connect(server, port, username, password);
  }
  
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
  @Override
  public void connect(String server, int port, String username, String password) throws SocketException, IOException {
    int reply;
    if (port > 0) {
      ftp.connect(server, port);
    } else {
      ftp.connect(server);
    }
    logger.info("Connected to " + server + " on "
        + (port > 0 ? port : ftp.getDefaultPort()));
    // After connection attempt check the reply code to verify success
    reply = ftp.getReplyCode();
    
    if (!FTPReply.isPositiveCompletion(reply)) {
      ftp.disconnect();
      logger.error("FTP server refused connection.");
      // exit
      return;
    }
    
    __main:
      if (!ftp.login(username, password)) {
        ftp.logout();
        break __main;
      }
    logger.info("Remote system is " + ftp.getSystemType());
  }
  
  /**
   * the setting after the FTPClient connected
   * 
   * using the default setting
   * 
   * @throws IOException
   */
  @Override
  public void connectedSetting() throws IOException {
    boolean binaryTransfer = true;      // use binaryTransfer
    boolean localActive = false;        // use passive mode
    boolean useEpsvWithIPv4 = false;
    connectedSetting(binaryTransfer, localActive, useEpsvWithIPv4);
  }
  
  /**
   * the setting after the FTPClient connected
   * 
   * @param binaryTransfer
   * @param localActive
   * @param useEpsvWithIPv4
   * @throws IOException
   */
  @Override
  public void connectedSetting(boolean binaryTransfer, boolean localActive, boolean useEpsvWithIPv4) throws IOException {
    if (binaryTransfer) {
      ftp.setFileType(FTP.BINARY_FILE_TYPE);
    } else {
      // in theory this should not be necessary as servers should default to ASCII
      // but they don't all do so - see NET-500
      ftp.setFileType(FTP.ASCII_FILE_TYPE);
    }
    // Use passive mode as default because most of us are
    // behind firewalls these days.
    if (localActive) {
      ftp.enterLocalActiveMode();
    } else {
      ftp.enterLocalPassiveMode();
    }
    
    ftp.setUseEPSVwithIPv4(useEpsvWithIPv4);
  }
  
  /**
   * download file from FTPServer
   * 
   * @param remote      remote file in FTPServer
   * @param local       local file
   * @return
   * @throws IOException
   */
  @Override
  public boolean download(String remote, String local) throws IOException {
    boolean isDownload = false;
    OutputStream output;
    output = new FileOutputStream(local);
    isDownload = ftp.retrieveFile(remote, output);
    output.close();
    
    ftp.noop();   // check that control connection is working OK
   
    return isDownload;
  }
  
  /**
   * upload file to FTPServer
   * 
   * @param remote      remote file in FTPServer
   * @param local       local file
   * @return
   * @throws IOException
   */
  @Override
  public boolean upload(String remote, String local) throws IOException {
    boolean isUpload = false;
    InputStream input;
    
    input = new FileInputStream(local);
    
    isUpload = ftp.storeFile(remote, input);
    input.close();
      
    ftp.noop();   // check that control connection is working OK
    
    return isUpload;
  }
  
  /**
   * list all files in remote FTPServer path
   * 
   * using the default setting
   * 
   * @param remote      remote path or file in FTPServer
   * @return
   * @throws IOException
   */
  @Override
  public FTPFile[] listFiles(String remote) throws IOException {
    boolean lenient = false;
    FTPFile[] ftpFiles = listFiles(remote, lenient);
    
    return ftpFiles;
  }
  
  /**
   * list all files in remote FTPServer path
   * 
   * @param remote      remote path or file in FTPServer
   * @param lenient
   * @return
   * @throws IOException
   */
  @Override
  public FTPFile[] listFiles(String remote, boolean lenient) throws IOException {
    FTPFile[] ftpFiles = null;
    
    if (lenient) {
      FTPClientConfig config = new FTPClientConfig();
      config.setLenientFutureDates(true);
      ftp.configure(config);
    }
    
    ftpFiles = ftp.listFiles(remote);
    
    ftp.noop();   // check that control connection is working OK
    
    return ftpFiles;
  }
  
  @Override
  public FTPFile[] listFiles(String remote, FTPFileFilter filter) throws IOException {
    FTPClientConfig config = new FTPClientConfig();
    config.setLenientFutureDates(true);
    ftp.configure(config);
    
    FTPFile[] ftpFiles = ftp.listFiles(remote, filter);
        
    ftp.noop(); 
    
    return ftpFiles;
  }
  
  /**
   * delete the remoteFile from FTPServer
   * 
   * @param remoteFile      remoteFile to be deleted in FTPServer
   * @return
   * @throws IOException
   */
  @Override
  public boolean deleteRemoteFile(String remoteFile) throws IOException {
    boolean isDeleted = false;
    
    isDeleted = ftp.deleteFile(remoteFile);
    
    return isDeleted;
  }
  
  /**
   * logout from FTPServer
   */
  @Override
  public void logout() {
    try {
      ftp.logout();
    } catch (IOException e) {
      logger.error(e.getLocalizedMessage());
    } finally {
      if (ftp.isConnected()) {
        try {
          ftp.disconnect();
        } catch (Exception e2) {
          // do nothing
        }
      }
    }
  }
  
  private CopyStreamListener createCopyStreamListener() {
    return new CopyStreamListener() {
      private long megsTotal = 0;

      // @Override
      public void bytesTransferred(CopyStreamEvent event) {
        bytesTransferred(event.getTotalBytesTransferred(), event.getBytesTransferred(),
            event.getStreamSize());
      }

      // @Override
      public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
        long megs = totalBytesTransferred / 1000000;
        for (long l = megsTotal; l < megs; l++) {
          logger.error("#");
        }
        megsTotal = megs;
      }
    };
  }

  @Override
  public boolean renameRemoteFile(String remoteFile, String newName) throws IOException {
    
    return ftp.rename(remoteFile, newName);
  }

  
}
