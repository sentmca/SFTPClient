import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Vector;

import com.jcraft.jsch.*;

public class TestJSch {
    public static void main(String args[]) {
        JSch jsch = new JSch();
        Session session = null;
        try {
        	
        	String importFileLocation = ResourceMgr.getResourceFromConfigBundle("cm.sftp.import.files.location");
        	String ftpCMArchiveFilesLocation = ResourceMgr.getResourceFromConfigBundle("sftp.cm.archive.files.location");
        	File importFileDir = new File(importFileLocation);
        	String importFileExtension = ResourceMgr.getResourceFromConfigBundle("sftp_file_extension");
        	
        	String sftpHost = ResourceMgr.getResourceFromConfigBundle("sftp.host");
        	String sftpUser = ResourceMgr.getResourceFromConfigBundle("sftp.user");
        	String sftpPassword = ResourceMgr.getResourceFromConfigBundle("sftp.password");
        	int sftpPort = 22;
        	
            session = jsch.getSession(sftpUser, sftpHost, sftpPort);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(sftpPassword);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            
            //sftpChannel.get("/to_hh/remotefile.txt", "localfile.txt");
            
            System.out.println(sftpChannel.pwd());
            
            //Vector dirList = sftpChannel.ls("/");

            String[] fontPaths = null;
			String importFilePath;
			if (importFileDir.canRead()) {
			    fontPaths = importFileDir.list();
			    for (String importFile : fontPaths) {
			        if (importFile != null && importFile.endsWith( ".".concat(importFileExtension) )) {
			        	
			        	importFilePath = importFileDir.getAbsolutePath() + File.separatorChar +importFile;
			        	File txtFile = new File(importFilePath);
			        	System.out.println("txtFile: "+txtFile);
			        	
			        	String destinationFilename = ftpCMArchiveFilesLocation+"/"+importFile;
			        	
			        	InputStream inputStream= new FileInputStream(txtFile);
			        	
			        	sftpChannel.put(inputStream, destinationFilename);
			        				        	
			        	//txtFile.delete();
			        		
			        }
			    }
			}
            
			System.out.println("finishd");
            
            sftpChannel.exit();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } 
    }
}
