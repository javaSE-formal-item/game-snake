package com.wch.snake;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.mysql.cj.util.StringUtils;
import com.mysql.jdbc.Driver;

/**
 * @author CH W
 * @description
 * @date 2020年1月5日 下午5:02:49
 * @version 1.0
 */
public class OperationDB {
	private Statement statement = null;
	
	public OperationDB() {
		try {
			Driver driver = new Driver();
			Properties properties = new Properties();
			properties.put("user", "root");
			properties.put("password", "root");
			Connection connect = driver.connect("jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8", properties);
			statement = connect.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean queryAccountIsExit(String accountName, String password) {
		boolean isExit = false;
		try {
			String sql = "SELECT * FROM account WHERE account_name='"+accountName+"' AND password='"+password+"'";
			ResultSet queryRt = statement.executeQuery(sql);
			while (queryRt.next()) {
				String account_name = queryRt.getNString("account_name");
				isExit = StringUtils.isNullOrEmpty(account_name) ? false : true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isExit;
	}
	
}
