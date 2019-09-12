package com.maven.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FTP操作类
 * 
 * @author chenjian
 * @createDate 2019-08-01
 */
public class FtpUtils {

	private Logger logger = LoggerFactory.getLogger(FtpUtils.class);

	// 默认FTP地址
	private String host = "";

	// 默认FTP端口
	private int port = 21;

	// 默认FTP用户名
	private String userName = "";

	// 默认FTP密码
	private String password = "";

	// 默认FTP编码
	private String encoding = "utf-8";

	// 远程访问路径
	private String visitPath = "";

	private FTPClient ftpClient = null;

	public FtpUtils() {
		LoadPropertiesUtils loadPropertiesUtils = LoadPropertiesUtils
				.getInstance("ftp.properties");

		this.host = loadPropertiesUtils.getKey("host");
		this.port = Integer.valueOf(loadPropertiesUtils.getKey("port"))
				.intValue();
		this.userName = loadPropertiesUtils.getKey("userName");
		this.password = loadPropertiesUtils.getKey("password");
	}

	public FtpUtils(String host, int port, String userName, String password) {
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}

	/**
	 * 上传文件
	 * 
	 * @param remotePath
	 *            目录路径
	 * @param fileMap
	 *            文件集合
	 */
	public Map<String, Object> uploadFile(String remotePath,
			Map<String, Object> fileMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			connectFTP();
			createDir(remotePath);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.changeWorkingDirectory(remotePath);

			Iterator<Entry<String, Object>> iterator = fileMap.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Object> entry = iterator.next();

				String fileName = entry.getKey();
				Object object = entry.getValue();

				InputStream input = null;
				if (object instanceof byte[]) {
					byte[] byt = (byte[]) object;
					input = new ByteArrayInputStream(byt);
				} else if (object instanceof File) {
					File file = (File) object;
					input = new FileInputStream(file);
				} else if (object instanceof InputStream) {
					input = (InputStream) object;
				}

				if (StringUtils.isNotEmpty(input)) {
					ftpClient.storeFile(fileName, input);
					input.close();

					map.put(fileName, this.visitPath + fileName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnectFTP();
		}

		return map;
	}

	/**
	 * 下载文件
	 * 
	 * @param localPath
	 *            本地路径
	 * @param fileList
	 *            文件集合
	 * @return
	 */
	public Map<String, Object> downloadFile(String localPath,
			List<String> fileList) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			connectFTP();

			for (String filePath : fileList) {
				int index = filePath.lastIndexOf("\\");

				String remotePath = filePath.substring(0, index);
				String fileName = filePath.substring(index + 1);

				ftpClient.changeWorkingDirectory(remotePath);

				FTPFile[] ftpFiles = ftpClient.listFiles();
				for (FTPFile file : ftpFiles) {
					if (fileName.equalsIgnoreCase(file.getName())) {
						File localFile = new File(localPath + "/"
								+ file.getName());
						OutputStream os = new FileOutputStream(localFile);
						ftpClient.retrieveFile(file.getName(), os);
						os.close();

						InputStream inputStream = ftpClient
								.retrieveFileStream(file.getName());
						byte[] bytes = new byte[inputStream.available()];
						inputStream.read(bytes);

						map.put(filePath, bytes);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnectFTP();
		}

		return map;
	}

	/**
	 * 删除文件
	 * 
	 * @param fileList
	 *            文件集合
	 */
	public void deleteFile(List<String> fileList) {
		try {
			connectFTP();

			for (String filePath : fileList) {
				int index = filePath.lastIndexOf("\\");

				String remotePath = filePath.substring(0, index);
				String fileName = filePath.substring(index + 1);

				ftpClient.changeWorkingDirectory(remotePath);
				ftpClient.dele(fileName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnectFTP();
		}
	}

	/**
	 * 创建FTP目录
	 * 
	 * @param remotePath
	 *            目录路径
	 */
	private void createDir(String remotePath) {
		try {
			ftpClient.makeDirectory(remotePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 连接FTP
	 */
	private void connectFTP() {
		try {
			ftpClient = new FTPClient();
			ftpClient.setControlEncoding(this.encoding);

			ftpClient.connect(this.host, this.port);
			ftpClient.login(this.userName, this.password);
			int replyCode = ftpClient.getReplyCode();

			if (FTPReply.isPositiveCompletion(replyCode)) {
				logger.info("FTP connect success");
			} else {
				disconnectFTP();
				logger.info("FTP connect fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 断开FTP
	 */
	private void disconnectFTP() {
		if (ftpClient.isConnected()) {
			try {
				ftpClient.disconnect();
				logger.info("FTP disconnect");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}