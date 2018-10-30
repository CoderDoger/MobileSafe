package hxm.com.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {


	protected static final int SHOW_UPDATE_DIALOG = 1;
	protected static final int NET_ERROR = 2;
	protected static final int JSON_ERROR = 3;
	protected static final int URL_ERROR = 4;
	private TextView verText;
	private TextView process_tv;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		verText = findViewById(R.id.version_tv);
		process_tv = findViewById(R.id.process_tv);
		//自动升级的开关
		boolean autoUpdate = sp.getBoolean("update", false);		
		//显示版本号
		verText.setText("版本："+getVersion());
		//
		if(autoUpdate){
			//检查升级
			checkUpdate();
		}
		else{
			//自动升级关闭时，延时2s进入主页
			mHandle.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					//进入主页
					goHome();
				}
			}, 2000);
		}	
	}
	private  Handler mHandle = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case SHOW_UPDATE_DIALOG:
					Toast.makeText(getApplicationContext(), "准备升级中", Toast.LENGTH_LONG).show();
					update();
					break;
				case NET_ERROR:
					Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_LONG).show();
					goHome();
					break;
				case JSON_ERROR:
					Toast.makeText(getApplicationContext(), "JSON解析出错", Toast.LENGTH_LONG).show();
					break;
				case URL_ERROR:
					Toast.makeText(getApplicationContext(), "URL出错", Toast.LENGTH_LONG).show();
					break;
				default :
					break;
					
			}
		}
	};
	protected String apkUrl;
	
	//检查升级
	private void checkUpdate() {
		new Thread(){
			@Override
			public void run() {
				//访问服务器
				Message msg = Message.obtain();
				long startTime = System.currentTimeMillis();
				try {
					String uri = "http://192.168.1.105/mobileServ/data.html";
					//String uri = "http://192.168.1.105/UpdateConfig/servlet/GetUpdateConfig";
					URL url = new URL(uri);
					try {
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod("GET");
						conn.setConnectTimeout(4000);
						conn.connect();
						int rspCode = conn.getResponseCode();
						
						if(rspCode==200){
							//联网成功
							//将Inputstram转成String 
							InputStream is = conn.getInputStream();
							
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							byte[] buff = new byte[1024];
							int len = -1;
							while((len = is.read(buff))!=-1){
								bos.write(buff, 0, len);
							}
							is.close();
							//String resultStr = new String(bos.toByteArray(), "UTF-8");
							String resultStr = bos.toString();
							bos.close();
							//将String 转成Json
							JSONObject json = new JSONObject(resultStr);
							//得到vesion
							String version = (String) json.get("version");
							apkUrl = json.getString("link");
							
							System.out.println("=====>"+version);
							
							//对比版本号
							if(getVersion().equals(version)){
								//版本相同，直接进入主页
								System.out.println("====>go home");
								goHome();
							}else{
								//有新版本，弹出升级对话框
								System.out.println("====>go update");
								msg.what = SHOW_UPDATE_DIALOG;
							}	
						}
						
					} catch (IOException e) {
						//网络异常
						msg.what = NET_ERROR;
						e.printStackTrace();
					} catch (JSONException e) {
						//JSON解析出错
						msg.what = JSON_ERROR;
						e.printStackTrace();
					}
				} catch (MalformedURLException e) {
					//URL错误
					msg.what = URL_ERROR;
					e.printStackTrace();
				}finally {
					long endTime = System.currentTimeMillis();
					long dTime = endTime - startTime;
					if(dTime<2000){
						try {
							sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					mHandle.sendMessage(msg);
				}
			}
		}.start();
		
	}
	//升级
	protected void update() {
		// TODO Auto-generated method stub
		//弹出升级确认对话框 
		Builder builder = new Builder(this);
		builder.setCancelable(false);
		builder.setTitle("升级确认");
		builder.setMessage("发现新版本，是否更新？");
		//确定
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//下载APK
				//判断是否有SD卡
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					FinalHttp finalHttp = new FinalHttp();
					finalHttp.download(apkUrl, Environment.getExternalStorageDirectory()+"/MobileSafe.apk", new AjaxCallBack<File>() {
						//下载失败
						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							// TODO Auto-generated method stub
							t.printStackTrace();
							Toast.makeText(getApplicationContext(), "apk下载失败", Toast.LENGTH_LONG).show();
							super.onFailure(t, errorNo, strMsg);
						}
						@Override
						public void onLoading(long count, long current) {
							// TODO Auto-generated method stub
							super.onLoading(count, current);
							process_tv.setVisibility(View.VISIBLE);
							int process = (int) (current*100/count);
							process_tv.setText("下载进度："+process+"%");
						}
						@Override
						public void onSuccess(File t) {
							super.onSuccess(t);
							installApk(t);
						}
						
						/**
						 * @param t
						 * install APk
						 */
						private void installApk(File t) {
							// TODO Auto-generated method stub
							  Intent intent = new Intent();
							  intent.setAction("android.intent.action.VIEW");
							  intent.addCategory("android.intent.category.DEFAULT");
							  intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
							  startActivity(intent);
						}
					});
				}
				
			}
		});
		//取消
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				goHome();
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	//进入主页
	protected void goHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();	
	}
	//获取版本号
	public String getVersion(){
		// 用来管理手机的APK
		PackageManager pkgM = getPackageManager();
		try {
			// 得到知道APK的功能清单文件
			PackageInfo  pkgInfo = pkgM.getPackageInfo(getPackageName(), 0);
			String versionName = pkgInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}	
	}
}
