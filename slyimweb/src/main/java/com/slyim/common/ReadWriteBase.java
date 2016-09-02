package com.slyim.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Base64Utils;

public class ReadWriteBase {
	public enum FileType{  
	    AVATAR("avatar"),PIC("pic"),VIDEO("video"),VOICE("voice");
		private String name;
		private FileType(String name) {
			this.name=name;
		}
		public String getName() {
            return name;
        }
	}
//	private static String randFileName() {
//		int fileName;
//		Date date = new Date();
//		long now=date.getTime();
//		Random random = new Random(now);
//		fileName=(Math.abs(random.nextInt()));
//		return String.valueOf(fileName);
//	}
	private static String _dirPath=null;
	private static String _relaDirPath=null;
	private static String getDirPath(HttpServletRequest request, FileType fileType) {
//		if (ReadWriteBase._dirPath==null) {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//			ReadWriteBase._relaDirPath="storage/"+fileType.getName()+sdf.format(System.currentTimeMillis());
//			ReadWriteBase._dirPath=request.getSession().getServletContext().getRealPath("/slyim/www/storage/"+fileType.getName()+'/'+sdf.format(System.currentTimeMillis()));
//		}
		if (ReadWriteBase._dirPath==null) {
			ReadWriteBase._relaDirPath="storage/"+fileType.getName();
			ReadWriteBase._dirPath=request.getSession().getServletContext().getRealPath("/slyim/www/storage/"+fileType.getName());
		}
		return ReadWriteBase._dirPath;
	}
	private static String _relaFilePath=null;
	private static String getFilePath(HttpServletRequest request, FileType fileType,String fileName) {
		//String fileName=ReadWriteBase.randFileName();
		ReadWriteBase._relaFilePath=ReadWriteBase._relaDirPath+"/"+fileName;
		return ReadWriteBase.getDirPath(request, fileType)+"/"+fileName;
		
	}
	public static boolean readBase64ByBytes(HttpServletRequest request){
		return false;
	}
	public static String writeBytesByBase64(HttpServletRequest request, FileType fileType, String src) throws NoSuchAlgorithmException{
		File dir =new File(ReadWriteBase.getDirPath(request, fileType));
		if (!dir.exists()) {
			dir.mkdir();
		}
		String srcData[]=src.split(";base64,");
		String fileLastType[]=srcData[0].split("data:image/");
		byte[] b=Base64Utils.decodeFromString(srcData[1]);
		MessageDigest md5 = MessageDigest.getInstance("MD5");
	    byte[] digest = md5.digest(b);
	    String hashString = new BigInteger(1, digest).toString(16);
		File file =new File(ReadWriteBase.getFilePath(request, fileType, hashString)+'.'+fileLastType[1]);
		//System.out.println(file.getAbsolutePath());
			if (!file.exists()) {		
				try {
					OutputStream out =new FileOutputStream(file);
					out.write(b);
					out.flush();
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
		return ReadWriteBase._relaFilePath+'.'+fileLastType[1];
	}
}
