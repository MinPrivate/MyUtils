package com.min.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

  // Protect constructor since it is a static only class
  protected FileUtils() {}

  /*
   * get all files in the path
   */
  public static List<File> getFileList(final String path) {
    return getFileList(path, false);
  }
  
  /*
   * get all files in the path
   */
  public static List<File> getFileList(final File path) {
    return getFileList(path, false);
  }
  
  public static void copyFolder(String srcFolder, String destFolder)
      throws Exception {
    if (srcFolder == null || destFolder == null) {
      return;
    }
    copyFolder(new File(srcFolder), new File(destFolder));
  }
  
  /*
   * copy folder
   */
  public static void copyFolder(File srcFolder, File destFolder)
      throws Exception {
    if (srcFolder == null || destFolder == null) {
      return;
    }
    File[] files = srcFolder.listFiles();
    for (File file : files) {
      if (file.isFile()) {
        String pathname = getFilePath(destFolder.getPath(), file.getName());

        File dest = new File(pathname);
        File destPar = dest.getParentFile();
        destPar.mkdirs();
        if (!dest.exists()) {
          dest.createNewFile();
        }
        copyFile(file, dest);

      } else {
        copyFolder(file, destFolder);
      }
    }
  }
  
 /*
  * copy file
  * @param src
  * @param dest
  * @throws Exception
  */
 public static void copyFile(File src, File dest) throws Exception {
   if (src == null || dest == null || !src.isFile() || !dest.isFile()) {
     return;
   }
   FileInputStream input = null;
   FileOutputStream output = null;
   try {
     input = new FileInputStream(src);
     output = new FileOutputStream(dest);

     output.getChannel().transferFrom(input.getChannel(), 0, input.available());

   } catch (Exception e) {
     throw e;
   } finally {
     output.flush();
     output.close();
     input.close();
   }

 }
  
  /*
   * get all files in the path 
   * including the files in sub path if the subFloderFlag is true
   */
  public static List<File> getFileList(final String path, final boolean subFolderFlag) {
    File filePath = new File(path);
    return getFileList(filePath, subFolderFlag);
  }
  
  /*
   * get all files in the path 
   * including the files in sub path if the subFloderFlag is true
   */
  public static List<File> getFileList(final File path, final boolean subFolderFlag) {
    if (path == null) {
      return null;
    }
    List<File> fileList = new ArrayList<File>();
    File filePath = path;
    if (filePath.exists()) {
      File files[] = filePath.listFiles();
      if (subFolderFlag) {
        for (File file : files) {
          if (file.isFile()) {
            fileList.add(file);
          } else if (file.isDirectory()) {
            String subPath = getFilePath(filePath.getAbsolutePath(), file.getName());
            File subFilePath = new File(subPath);
            List<File> subFileList = getFileList(subFilePath, subFolderFlag);
            if (subFileList != null && subFileList.size() > 0) {
              fileList.addAll(subFileList);
            }
          }
        }
      } else {
        for (File file : files) {
          if (file.isFile()) {
            fileList.add(file);
          }
        }
      }
    }
    return fileList;
  }
  
  /*
   * list the files in current path which fileNames fit the FilenameFilter
   */
  public static List<File> getFileList(final String path, final FilenameFilter fileFilter) {
    File filePath = new File(path);
    return getFileList(filePath, fileFilter);
  }
  
  /*
   * list the files in current path which fileNames fit the FilenameFilter
   */
  public static List<File> getFileList(final File path, final FilenameFilter fileFilter) {
    if (path == null) {
      return null;
    }
    List<File> fileList = new ArrayList<File>();
    File filePath = path;
    if (filePath.exists()) {
      File files[] = filePath.listFiles(fileFilter);
      if (files != null) {
        for (File file : files) {
          if (file.isFile()) {
            fileList.add(file);
          }
        }
      }
    }
    return fileList;
  }
  
  /*
   * get total lines of the file
   */
  public static int getTotalLines(final File file) throws IOException {
    if (file == null) {
      return 0;
    }
    FileReader in = new FileReader(file);  
    LineNumberReader reader = new LineNumberReader(in);
    int lines = 0;  
    while (reader.readLine() != null) {  
      lines++;  
    }  
    reader.close();  
    in.close();  
    return lines;  
  }
  
  /*
   * get the full file path
   */
  public static String getFilePath(final String path, final String filename) {
    if (path == null || filename == null) {
      return null;
    }
    String filePath = null;
    if (path.endsWith(File.separator)) {
      filePath = path + filename;
    } else {
      filePath = path + File.separator + filename;
    }
    return filePath;
  }
  
  /*
   * delete a file
   */
  public static boolean deleteFile(final String path) {
    if (path == null) {
      return false;
    }
    File file = new File(path);
    return deleteFile(file);
  }
  
  /*
   * delete a file
   */
  public static boolean deleteFile(final File file) {
    if (file == null) {
      return false;
    }
    if (file.isFile() && file.exists()) {
      file.delete();
      return true;
    }
    return false;
  }
}
