package com.maven.common.ftp;

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

import com.maven.common.StringUtils;
import com.maven.common.properties.LoadPropertiesUtils;

/**
 * FTP操作类
 * 
 * @author chenjian
 * @createDate 2019-08-01
 */
public class FtpUtils {

	private Logger logger = LoggerFactory.getLogger(FtpUtils.class);

	// FTP服务器地址
	private String host;

	// FTP服务器端口
	private int port = 0;

	// FTP用户名
	private String userName;

	// FTP密码
	private String password;

	// 远程访问地址
	private String visitPath;

	// FTP编码
	private String encoding;

	// FTP默认服务器地址
	private final String DEFAULT_HOST = "127.0.0.1";

	// FTP默认服务器端口
	private final int DEFAULT_PORT = 21;

	// FTP默认用户名
	private final String DEFAULT_USERNAME = "messi";

	// FTP默认密码
	private final String DEFAULT_PASSWORD = "chenjian";

	// FTP默认远程访问地址
	private final String DEFAULT_VISITPATH = "http://www.baidu.com";

	// FTP编码
	private final String DEFAULT_ENCODING = "utf-8";

	// FTPClient对象
	private FTPClient ftpClient = null;

	/**
	 * 构造函数
	 */
	public FtpUtils() {
		init();
	}

	/**
	 * 构造函数
	 * 
	 * @param host
	 *            FTP服务器地址
	 * @param port
	 *            FTP服务器端口
	 * @param userName
	 *            FTP用户名
	 * @param password
	 *            FTP密码
	 */
	public FtpUtils(String host, int port, String userName, String password) {
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		LoadPropertiesUtils loadPropertiesUtils = LoadPropertiesUtils
				.getInstance("ftp.properties");

		this.host = StringUtils.isNotEmpty(this.host) ? this.host
				: StringUtils.isNotEmpty(loadPropertiesUtils.getKey("host")) ? loadPropertiesUtils
						.getKey("host") : DEFAULT_HOST;
		this.port = this.port > 0 ? this.port : StringUtils
				.isNotEmpty(loadPropertiesUtils.getKey("port")) ? Integer
				.valueOf(loadPropertiesUtils.getKey("port")).intValue()
				: DEFAULT_PORT;
		this.userName = StringUtils.isNotEmpty(this.userName) ? this.userName
				: StringUtils
						.isNotEmpty(loadPropertiesUtils.getKey("userName")) ? loadPropertiesUtils
						.getKey("userName") : DEFAULT_USERNAME;
		this.password = StringUtils.isNotEmpty(this.password) ? this.password
				: StringUtils
						.isNotEmpty(loadPropertiesUtils.getKey("password")) ? loadPropertiesUtils
						.getKey("password") : DEFAULT_PASSWORD;
		this.visitPath = StringUtils.isNotEmpty(loadPropertiesUtils
				.getKey("visitPath")) ? loadPropertiesUtils.getKey("visitPath")
				: DEFAULT_VISITPATH;
		this.encoding = StringUtils.isNotEmpty(loadPropertiesUtils
				.getKey("encoding")) ? loadPropertiesUtils.getKey("encoding")
				: DEFAULT_ENCODING;
	}

	/**
	 * 上传文件
	 * 
	 * @param remotePath
	 *            目录路径
	 * @param fileMap
	 *            文件集合
	 * @return Map对象
	 */
	public Map<String, Object> uploadFile(String remotePath,
			Map<String, Object> fileMap) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(remotePath)
					&& StringUtils.isNotEmpty(fileMap) && fileMap.size() > 0) {
				connect();
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

				message = "Upload file success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Upload file error");
		} finally {
			disconnect();
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
	 * @return Map对象
	 */
	public Map<String, Object> downloadFile(String localPath,
			List<String> fileList) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(localPath)
					&& StringUtils.isNotEmpty(fileList) && fileList.size() > 0) {
				connect();

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

				message = "Download file success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Download file error");
		} finally {
			disconnect();
		}

		return map;
	}

	/**
	 * 删除文件
	 * 
	 * @param fileList
	 *            文件集合
	 * @return 是否删除成功
	 */
	public boolean deleteFile(List<String> fileList) {
		boolean result = false;

		try {
			String message = "Parameter error";

			// 判断传入参数
			if (StringUtils.isNotEmpty(fileList) && fileList.size() > 0) {
				connect();

				for (String filePath : fileList) {
					int index = filePath.lastIndexOf("\\");

					String remotePath = filePath.substring(0, index);
					String fileName = filePath.substring(index + 1);

					ftpClient.changeWorkingDirectory(remotePath);
					ftpClient.dele(fileName);
				}

				message = "Delete file success";
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Delete file error");
		} finally {
			disconnect();
		}

		return result;
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

			logger.info("Create dir success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Create dir error");
		}
	}

	/**
	 * 连接FTP
	 */
	private void connect() {
		try {
			String message = "FTP connect failure";

			ftpClient = new FTPClient();
			ftpClient.setControlEncoding(this.encoding);

			ftpClient.connect(this.host, this.port);
			ftpClient.login(this.userName, this.password);
			int replyCode = ftpClient.getReplyCode();

			if (FTPReply.isPositiveCompletion(replyCode)) {
				message = "FTP connect success";
			} else {
				disconnect();
			}

			logger.info(message);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("FTP connect error");
		}
	}

	/**
	 * 断开FTP
	 */
	private void disconnect() {
		if (ftpClient.isConnected()) {
			try {
				ftpClient.disconnect();
				logger.info("FTP disconnect success");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("FTP disconnect error");
			}
		}
	}
}