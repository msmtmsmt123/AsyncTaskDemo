package com.tsao.asynctasktest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;


//Ҫʹ��AsyncTask����Ҫ���໯AsyncTask<Params,Progress,Result>��������Ҫʵ��һЩ�ص�����
//Params:��������ʱ����Ĳ�������
//Progress����̨����ִ���з��صĽ���ֵ����
//Result����̨������ɺ󷵻ؽ��������

//doInBackground:����ʵ�֣�����д�ڴ˷�����
//onPreExecute:ִ�к�̨��ʱ����ǰ�����ã�ͨ���ڴ˷����н��г�ʼ������
//onPostExecute:doInBackground()��ɺ���ã����Ὣ���ص�ֵ�����÷���
//onProgressUpdate:��doInBackground()�����е���publishProgress()�������������ִ�н��Ⱥ󣬻ᴥ���÷���
public class MyAsyncTask extends AsyncTask<String, Integer, Bitmap> {
	
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		//ִ������ǰ������
		System.out.println("onPreExecute");
	}

	@Override
	protected Bitmap doInBackground(String... arg0) {
		//���ú�ִ������
		System.out.println("doInBackground");
		String url = arg0[0];//doInBackground�Ĳ�����һ���ɱ䳤���飬������Դ��execute()�����еĲ�������
		Bitmap bitmap = null;
		try {
			URLConnection connection = new URL(url).openConnection();
			InputStream is = connection.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bitmap = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//ģ��������ĸ��£�i��Ϊģ��Ļ�ȡ���Ľ���ֵ
		for(int i = 0; i< 100; i++){
			//����������ȣ����ø÷������ݽ��ȡ��ᴥ��onProgressUpdate()����
			publishProgress(i);
			try {
				Thread.sleep(100);//Ϊ�˲�ִ��̫�죬�������������ƶ�����������һ��ʱ��
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		//����ִ��������
		System.out.println("onPostExecute");
		MainActivity.mImageView.setImageBitmap(result);
		//ͼƬ����ʾ��ʹ���������ɼ�
		MainActivity.mProgressBar.setVisibility(MainActivity.mProgressBar.GONE);
		
		////////
		////////
		////////����ͼƬ
		String message = "to External Storage";
        //��ȡ���״̬������Ƿ���ã�������ص�״̬��MEDIA_MOUNTED,������Զ�д��
        String state = Environment.getExternalStorageState();
        
        if (Environment.MEDIA_MOUNTED.equals(state)) {
        	System.out.println("�ⲿ�洢����");
        	//����Public files
        	//����getExternalStoragePublicDirectory()��������ȡһ�� File ��������ʾ�洢��external storage��Ŀ¼
        	//����ΪĿ¼���ͣ�ϵͳ���ģ�
        	File file = new File(Environment.getExternalStoragePublicDirectory(
        			Environment.DIRECTORY_PICTURES), "pic");
        	//��file��Ϊһ��Ŀ¼����,��Ϊpic��û�����file����һ���ļ�������ֱ����file�Ͻ��ж�д
        	file.mkdir();
        	//String filePath = file.getAbsolutePath();
        	File file1 = new File(file,"pic.png");
        	
        	FileOutputStream fos;
			try {
				fos = new FileOutputStream(file1);
				result.compress(Bitmap.CompressFormat.PNG, 100, fos);//�ڶ�������Ϊѹ���ʣ�100��ʾ��ѹ��
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
        }
	}

	

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		System.out.println("onProgressUpdate");
		//publishProgress()ֻ������һ�������������±���0ȡֵ
		MainActivity.mProgressBar.setProgress(values[0]);
	}
	
	

}
