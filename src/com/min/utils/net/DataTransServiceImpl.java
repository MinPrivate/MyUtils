package com.min.utils.net;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.min.utils.file.FileUtils;

public class DataTransServiceImpl implements DataTransService {

  private static final Logger logger = LogManager.getLogger();
  private static final String POSTFIX = ".processed";
  
  private FtpClient ftp;
  
  private String server;
  private int port = 21;
  
  private String username;
  private String password;
  
  public void setFtp(FtpClient ftp) {
    this.ftp = ftp;
  }
  
  public DataTransServiceImpl() {
	this.ftp = new FtpClientImpl();
  }

  @Override
  public void setServerConfig(String server, int port, String username, String password) {
    this.server = server;
    if (port > 0) {
      this.port = port; 
    }
    this.username = username;
    this.password = password;
  };

  @Override
  public boolean downloadRemoteData(String remote, String local) {
    boolean hasError = false;
    List<String> errorFiles = new LinkedList<String>();
    
    try{
      //establish connection
      connectToFtp();

      // get the remote files
//      FTPFile[] files = ftp.listFiles(remote, new FTPFileFilter() {
//        
//        @Override
//        public boolean accept(FTPFile f) {
//          return !f.getName().contains(POSTFIX);
//        }
//      });
      FTPFile[] files = ftp.listFiles(remote);
      List<FTPFile> needToDownloadFiles = new ArrayList<FTPFile>();
      for (FTPFile ftpFile : files) {
        if (!ftpFile.getName().contains(POSTFIX)) {
          needToDownloadFiles.add(ftpFile);
        }
      }
      
      logger.info(needToDownloadFiles.size() + " files are ready to be downloaded");
      
      for(FTPFile f : needToDownloadFiles){
        String fileName = f.getName();

        String remoteFile = FileUtils.getFilePath(remote, fileName);
        String localFile = FileUtils.getFilePath(local, fileName);
        
        try {
          boolean downloaded =ftp.download(remoteFile, localFile);
          if(downloaded) {
            ftp.renameRemoteFile(remoteFile, remoteFile + POSTFIX);
          }
        } catch (Exception e) {
          logger.error("Error happened when " + fileName + " was being downloaded", e);
          File file = new File(localFile);
          if(file.exists()) {
            file.delete();
          }
          hasError = true;
          errorFiles.add(fileName);
        }   
      }
    } catch (Exception e) {
      logger.error("FTP download error \n", e);
      return hasError;
    } finally {
      // logout
      ftp.logout();
    }
    
    logger.info("Download ftp remote data done!");
    return hasError;
  }

  @Override
  public boolean uploadLocalData(String remote, String local) {
    boolean hasError = false;
    List<String> errorFiles = new LinkedList<String>();
    
    try {
      connectToFtp();
      
      // get the local files
      File[] files = null;
      File localPath = new File(local);
      if (localPath.isDirectory()) {
        files = localPath.listFiles();
      } else if (localPath.isFile()) {
        files = new File[1];
        files[0] = localPath;
      }


      for (File file : files) {
        String fileName = file.getName();

        String localFile = file.getAbsolutePath();
        String remoteFile = FileUtils.getFilePath(remote, fileName);
        logger.info("uploading file from "+localFile +" to " + remoteFile);
        try { 
          ftp.upload(remoteFile, localFile);
          file.delete();
        } catch (IOException e) {
          logger.error("Error happened on uploading file "+ file.getName(), e);
          hasError = true;
          errorFiles.add(fileName);
        }
      }
      
    } catch (Exception e) {
      logger.error("Error happened on connection", e);
      return hasError;
    } finally {
      // logout
      ftp.logout();
    }

    logger.info("Upload local data to ftp done!");
    return hasError;
  }

  private void connectToFtp() throws SocketException, IOException {
    logger.info("Connecting to server " + server);
    ftp.createFTPClient();
    ftp.setFTPClientConfig();
    ftp.connect(server, port, username, password);
    ftp.connectedSetting();
  }
}
