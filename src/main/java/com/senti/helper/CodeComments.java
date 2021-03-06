package com.senti.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  提取代码中的注释
 */
public class CodeComments {
    private final static String reg="[^0-9a-zA-Z\\u4e00-\\u9fa5.,!?: \\n]";//去掉特殊字符

    public List<String> getComment(List<String> list) {// get all comments in list
        List<String> res = new ArrayList<>();

        String line = null;
        boolean isComment = false;
        boolean isAdd = false;

        for (int w = 0; w < list.size(); w++) {
            line = list.get(w);
            if (isComment == true && (!line.contains("*/"))) {
                int index0 = line.indexOf("*");

                String toAddLine = line.substring(index0 + 1).trim();
                if (toAddLine.length() != 0)
                    res.add(toAddLine);
                isAdd = false;
                continue;
            }
            if (line.contains("//")) {
                int intIndex = line.indexOf("//");
                String str = line.substring(intIndex + 2).trim();
                if (str.length() != 0)
                    res.add(str);
            }
            if (line.contains("System.out.print")) {
                String[] temp = line.split(";");
                for (int j = 0; j < temp.length; j++) {
                    if (temp[j].contains("System.out.print")) {
                        int priIndex = temp[j].indexOf("System.out.print");
                        String priStr = temp[j].substring(priIndex);

                        Pattern p = Pattern.compile("\"(.*?)\"");
                        Matcher m = p.matcher(priStr);

                        StringBuilder complete = new StringBuilder("");
                        while (m.find()) {
                            String temppart = m.group();
                            complete.append(temppart.substring(1, temppart.length() - 1));
                        }
                        if (complete.length() != 0)
                            res.add("" + complete);

                    }
                }

            }
            if (line.contains("/*")) {
                isComment = true;
                int allIndex = line.indexOf("/*");
                String allStr = "";
                if (line.contains("/**"))
                    allStr = line.substring(allIndex + 3).trim();
                else
                    allStr = line.substring(allIndex + 2).trim();
                if (allStr.length() != 0)
                    res.add(allStr);
                isAdd = true;
            }
            if (line.contains("*/")) {
                if (isAdd == false) {
                    int endIndex = line.indexOf("*/");
                    String endStr = line.substring(0, endIndex).trim();
                    if (endStr.length() != 0)
                        res.add(endStr);
                }
                isComment = false;
                continue;
            }

        }

        return res;

    }

    /**
     *
     * @param list
     * @return 仅考虑以//开头的注释
     */
    public List<String> getCommentOnlyInMethod(List<String> list) {
        List<String> res = new ArrayList<>();
        String line = null;
        boolean isAdd = false;

        for (int w = 0; w < list.size(); w++) {
            line = list.get(w).trim();
            if (line.contains("//")&&!line.startsWith("*")&&!line.endsWith(";")&&!line.endsWith("}")) {//筛掉注释掉的代码，代码几乎都会以}或;结尾

                int intIndex = line.indexOf("//");
                String str = line.substring(intIndex+2).replaceAll(reg,"").trim();
                if (str.length() != 0)
                    res.add(str);


            }
            else if (line.contains("System.out.print")) {
                String[] temp = line.split(";");
                for (int j = 0; j < temp.length; j++) {
                    if (temp[j].contains("System.out.print")) {
                        int priIndex = temp[j].indexOf("System.out.print");
                        String priStr = temp[j].substring(priIndex);

                        Pattern p = Pattern.compile("\"(.*?)\"");//将输出的“”中的内容进行拼接
                        Matcher m = p.matcher(priStr);

                        StringBuilder complete = new StringBuilder("");
                        while (m.find()) {
                            String temppart = m.group();
                            complete.append(temppart.substring(1, temppart.length() - 1));
                        }
                        String comp=complete.toString().replaceAll(reg,"").trim();
                        if (comp.length() != 0)
                            res.add(comp);

                    }
                }

            }

        }

        return res;
    }
}
