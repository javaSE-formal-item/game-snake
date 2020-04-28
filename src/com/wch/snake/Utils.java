package com.wch.snake;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CH W
 * @description
 * @date 2020年3月3日 下午3:41:43
 * @version 1.0
 */
public class Utils {
	
	public static boolean writeFile(List<String> account_list, String fileName, String folder) {
		File file = new File(folder + fileName);
		try {
			file.delete();
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			if(account_list==null || account_list.size()<1) {
				bw.close();
				return false;
			}
			for(int i=0; i<account_list.size(); i++) {
				bw.write(account_list.get(i));
				bw.newLine();
				bw.flush();
			}
			bw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static List<String> readAccountInfo(File file) throws IOException {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			List<String> account_list = new ArrayList<String>();
			String line = "";
			while((line = br.readLine())!=null) {
				account_list.add(line);
			}
			br.close();
			return account_list;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
 