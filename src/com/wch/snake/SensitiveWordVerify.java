package com.wch.snake;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author CH W
 * @description
 * @date 2020年1月9日 下午3:29:53
 * @version 1.0
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class SensitiveWordVerify {
	private Map sensitiveWordMap;
	public final int MIN_MATCH = 1;      //最小匹配规则
    public final int MAX_MATCH = 2;      //最大匹配规则
    
    /**
     * --构造函数	传入敏感词库文件路径初始化敏感词库
     * @param sensitiveWordFilePath
     */
    public SensitiveWordVerify(String sensitiveWordFilePath) {
    	Set<String> lexicon = this.getLexicon(sensitiveWordFilePath);
    	sensitiveWordMap = this.addSensitiveWordToHashMap(lexicon);
    }
    /**
    - * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
    - * 中 = {isEnd = 0
    - *      国 = {isEnd = 1
    - *           人 = {isEnd = 0
    - *                民 = {isEnd = 1}
     *     }
    - *           男  = {isEnd = 0
    - *                   人 = {isEnd = 1}
     *     }
     *   }
     * }
     */
    private Map addSensitiveWordToHashMap(Set<String> keyWordSet) {
    	Map sensitiveWordMap = new HashMap(keyWordSet.size());
        String key = null;
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        Iterator<String> iterator = keyWordSet.iterator();
        while(iterator.hasNext()){
            key = iterator.next();
            nowMap = sensitiveWordMap;
            for(int i = 0 ; i < key.length() ; i++){
                char keyChar = key.charAt(i);
                Object wordMap = nowMap.get(keyChar);
                if(wordMap != null){        //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else {     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<String,String>();
                    newWorMap.put("isEnd", "0");     //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }
                if(i == key.length() - 1){
                    nowMap.put("isEnd", "1");    //最后一个
                }
            }
        }
        return sensitiveWordMap;
    }
	/**
	 * --初始化敏感词库
	 * @param sensitiveWordFilePath
	 * @return
	 */
	private Set<String> getLexicon(String sensitiveWordFilePath){
		Set<String> sensitiveword = new HashSet<String>();
		try {
			File file = new File(sensitiveWordFilePath);
			if(file.exists() && file.isFile()) {
				FileReader fileReader = new FileReader(file);
				BufferedReader br = new BufferedReader(fileReader);
				String lineText = null;
				while ((lineText = br.readLine()) !=null) {
					sensitiveword.add(lineText);
				}
				fileReader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sensitiveword;
	}
	/**
     * --获取文字中的敏感词
     * @param matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
     */
    public Set<String> getSensitiveWord(String txt , int matchType){
        Set<String> sensitiveWordList = new HashSet<String>();
        for(int i = 0 ; i < txt.length() ; i++){
            int length = this.CheckSensitiveWord(txt, i, matchType);
            if(length > 0){
                sensitiveWordList.add(txt.substring(i, i+length));
                i = i + length - 1;    //减1的原因，是因为for会自增
            }
        }
        return sensitiveWordList;
    }
    /**
     * --检查文字中是否包含敏感字符，检查规则如下：<br>
     */
    private int CheckSensitiveWord(String txt,int beginIndex,int matchType){
        boolean flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0;     //匹配标识数默认为0
        Map nowMap = sensitiveWordMap;
        for(int i=beginIndex; i<txt.length(); i++){
        	char word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);
            if(nowMap != null){
                matchFlag++;
                if("1".equals(nowMap.get("isEnd"))){
                    flag = true;
                    if(MIN_MATCH == matchType){    //最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }
            }else{     //不存在，直接返回
                break;
            }
        }
        if(matchFlag < 2 || !flag){        //长度必须大于等于1，为词
            matchFlag = 0;
        }
        return matchFlag;
    }
    /**
	 * --内容敏感词替换
	 * @param content
	 * @param set
	 * @return
	 */
	public String replaceStr(String content, Set<String> set) {
		Iterator<String> iterator = set.iterator();
        String word = null;
        while (iterator.hasNext()) {
            word = iterator.next();
            String resultReplace = "*";
            for(int i=1; i <word.length(); i++){
                resultReplace += "*";
            }
            content = content.replaceAll(word, resultReplace);
        }
		return content;
	}

}
