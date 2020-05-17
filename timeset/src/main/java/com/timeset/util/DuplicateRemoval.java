package com.timeset.util;

/**
 * @ClassName DuplicateRemoval
 * @Description
 * @Author SkySong
 * @Date 2020-05-13 20:57
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

public class DuplicateRemoval {
    private String fileName;
    private String duplicate;
    public DuplicateRemoval(String fileName,String duplicate){
        this.fileName = fileName;
        this.duplicate = duplicate;
    }

    /**
     * @param
     * @throws IOException
     */
    public  void start() throws IOException {

        // TODO Auto-generated method stub
        //File f1=new File("E:\\mt3");
        File f2 = new File(fileName);
        //File f2=new File("E:\\mt2");
        try {
            long startTime = System.currentTimeMillis();
            //delDoubleFile(f1,f2);
            //findSingleFile(f2);
            findSingleFile(f2, duplicate);//参数一是被检测的文件不能带有文件夹   参数二是重复元素复制到哪个文件夹中
            //reName(f1);
            //reName(f2);
            long endTime = System.currentTimeMillis();
            timechange(endTime - startTime);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private  void timechange(long ls) {
        // TODO Auto-generated method stub
        int second = (int) (ls / 1000);
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        System.out.println("用时" + h + "时" + d + "分" + s + "秒");

    }

	/*private  synchronized void fileCopyDemo(File file1,File file2,int name) throws Exception{//将两个文件复制到同一个文件夹中

		File objectDir1=new File("E:\\mt2"+"\\"+name);
		objectDir1.mkdir();//创建文件夹

		File objectPath1=new File ("E:\\mt2"+"\\"+name+"\\"+file1.getName()+".jpg");
		FileInputStream fis1=new FileInputStream(file1);
		FileOutputStream fos1=new FileOutputStream(objectPath1);
		byte []byt1=new byte[1024];

		int len1=0;

		while((len1=fis1.read(byt1))!=-1){
			fos1.write(byt1);
			fos1.flush();
		}
		fis1.close();
		fos1.close();
		System.out.println(name);


		File objectPath2=new File ("E:\\mt2"+"\\"+name+"\\"+file2.getName()+".jpg");
		FileInputStream fis2=new FileInputStream(file2);
		FileOutputStream fos2=new FileOutputStream(objectPath2);
		byte []byt2=new byte[1024];
		int len2=0;
		while((len2=fis2.read(byt2))!=-1){
			fos2.write(byt2);
			fos2.flush();
		}

		fis2.close();
		fos2.close();
		System.out.println(name);

	}
	*/

    private  void fileCopyDemo(File file, int name) throws Exception {//按数字重命名

        File objectPath = new File(duplicate + "\\" + name + ".jpg");
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(objectPath);
        byte[] byt = new byte[1024];
        int len = 0;
        while ((len = fis.read(byt)) != -1) {
            fos.write(byt);
            fos.flush();
        }
        fos.close();
        System.out.println(name);
    }

    private  void fileCopyDemo(File file, String object) throws Exception {//打印到目标文件地址

        File objectPath = new File( object + "\\" + file.getName());
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(objectPath);
        byte[] byt = new byte[1024];
        int len = 0;
        while ((len = fis.read(byt)) != -1) {

            fos.write(byt);
            fos.flush();
        }
        fos.close();
        fis.close();
        System.out.println("正在将重复元素" + file.getName() + "复制到" + object + "文件夹");

    }


    private  void findSingleFile(File file1, String copyobjectpath) throws Exception {
        // TODO Auto-generated method stub

        File[] files1 = file1.listFiles();
        TreeSet<File> tchongfu = new TreeSet<File>();
        TreeSet<File> tdayin = new TreeSet<File>();
        int count = 0;

        for (int x = 0; x < files1.length; x++) {
            for (int y = x + 1; y < files1.length; y++) {
                if (files1[x].length() == files1[y].length()) {
                    synchronized (file1.getClass()) {

                        byte[] data1 = new byte[(int) files1[x].length()];
                        byte[] data2 = new byte[(int) files1[y].length()];
                        Boolean flag = false;
                        FileInputStream rfis1 = new FileInputStream(files1[x]);
                        FileInputStream rfis2 = new FileInputStream(files1[y]);

                        //分别将两个文件的内容读入缓冲区
                        rfis1.read(data1);
                        rfis2.read(data2);

                        for (int i = 0; i < data1.length; i++) {
                            //只要有一个字节不同，两个文件就不一样
                            if (data1[i] == data2[i]) {
                                flag = true;
                                continue;

                            } else {
                                flag = false;
                                break;

                            }
                        }

                        if (flag) {
                            count++;
                            rfis1.close();
                            rfis2.close();
                            tchongfu.add(files1[y]);
                            tdayin.add(files1[x]);
                            tdayin.add(files1[y]);


                            //fileCopyDemo(files1[y],files1[x],count);
                            //fileCopyDemo(files1[x],count);//操作流的时候不能删除文件
                            //System.out.println(files1[y].getName()+"与"+files1[x].getName()+"重复，已复制到文件夹");
                            //files1[y].delete();
                            //System.out.println(files1[y].getName()+"已经删除");

                        }
                    }
                }
            }
        }

        System.out.println("共搜索到" + tchongfu.size() + "个重复");
        printFile(tdayin, copyobjectpath);
        System.out.println("重复元素复制到" + copyobjectpath + "文件夹成功");
        System.out.println("寻找完毕，1秒后开始删除重复元素");
        Thread.sleep(1000);
        delFile(tchongfu);
        System.out.println("已删除所有重复的元素");

    }


    private  void printFile(TreeSet<File> tdayin, String object) throws Exception {
        // TODO Auto-generated method stub

        Iterator it = tdayin.iterator();
        while (it.hasNext()) {
            String f = ((File) it.next()).getName();
            fileCopyDemo((new File(fileName + f)), object);

        }
    }

    private  void delFile(TreeSet<File> ts) throws Exception {

        Iterator it = ts.iterator();
        while (it.hasNext()) {
            String sss = ((File) it.next()).getName();
            System.out.println(sss + "删除" + ((new File(fileName + sss)).delete() ? "成功" : "失败"));
        }
    }


    private  int getNumber(File file) {//得到文件个数

        // TODO Auto-generated method stub
        int i = 0;
        File[] fis = new File[1];
        File[] files = file.listFiles();
        for (int x = 0; x < files.length; x++) {

            if (files[x].isDirectory()) {
                getNumber(files[x]);
            } else {
                i++;
            }
        }
        return i;
    }


    private void reName(File file) throws Exception {

        // TODO Auto-generated method stub
        File[] fis = getFileArray(file);
        System.out.println(fis.length);
        for (int x = 0; x < fis.length; x++) {
            fileCopyDemo(fis[x], x);
        }
    }


    private  File[] getFileArray(File file) throws Exception {//得到文件数组
        File[] fis = new File[getNumber(file)];
        File[] files = file.listFiles();
        for (int x = 0; x < files.length; x++) {
            if (files[x].isDirectory()) {
                reName(files[x]);
            } else {
                fis[x] = files[x];
            }
        }
        return fis;
    }

}

