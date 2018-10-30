package hxm.com.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private GridView glist;
	private MyAdapter adapter;
	private static String [] names = {
		"手机防盗","通讯卫士","软件管理",
		"进程管理","流量统计","手机杀毒",
		"缓存清理","高级工具","设置中心"
		
	};
	private static int[] icons = {
		R.mipmap.safe,R.mipmap.callmsgsafe,R.mipmap.app,
		R.mipmap.taskmanager,R.mipmap.netmanager,R.mipmap.trojan,
		R.mipmap.sysoptimize,R.mipmap.atools,R.mipmap.settings
		
	};
	private SharedPreferences sp;
	private Button confirm;
	private EditText pwd1;
	private EditText pwd2;
	private Button cancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		glist = (GridView) findViewById(R.id.glist);
		adapter = new MyAdapter();
		glist.setAdapter(adapter);
		glist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> prent, View view, int position,
					long id) {
				switch (position) {
				//手机防盗
				case 0:
					showLockDialog();
					break;
				
				//设置 
				case 8:
					Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}
				
			}
		});
	}
	//手机防盗对话框
	protected void showLockDialog() {
		//是否设置过密码
		if(isSetPwd()){
			//显示密码输入对话框
			showEnterPwdDialog();
		}else{
			////显示密码设置对话框
			showSetPwdDialog();
		}
		
	}
	//密码输入对话框
	private void showEnterPwdDialog() {
		// TODO Auto-generated method stub
		Builder builder = new Builder(this);
		builder.setCancelable(false);
		View view = View.inflate(HomeActivity.this, R.layout.lock_enter_pwd_dialog, null);
		///得到密码1
		pwd1 = (EditText)view.findViewById(R.id.pwd);
		
		//得到确定按钮
		confirm = (Button)view.findViewById(R.id.ok);
		//得到取消按钮
		cancel = (Button)view.findViewById(R.id.cancel);
		
		final AlertDialog dialog = builder.create();
		dialog.setView(view);
		dialog.show();
		
		confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String spwd1 = pwd1.getText().toString().trim();
				//判空
				if(TextUtils.isEmpty(spwd1)){
					Toast.makeText(HomeActivity.this, "SB密码不能为空！", Toast.LENGTH_LONG).show();
					return;
				}
				//判断是否与存储的密码相同
				String pwd = sp.getString("password", "");
				if(spwd1.equals(pwd)){
					dialog.dismiss();
					//进入向导页面
					Intent intent = new Intent(HomeActivity.this, FindActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(HomeActivity.this, "SB密码错误！", Toast.LENGTH_LONG).show();
					pwd1.setText("");
					return ;
				}
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}
	//密码设置对话框
	private void showSetPwdDialog() {
		// TODO Auto-generated method stub
		Builder builder = new Builder(this);
		builder.setCancelable(false);
		View view = View.inflate(HomeActivity.this, R.layout.lock_set_pwd_dialog, null);
		///得到密码1
		pwd1 = (EditText)view.findViewById(R.id.pwd);
		//得到密码2
		pwd2 = (EditText)view.findViewById(R.id.repwd);
		//得到确定按钮
		confirm = (Button)view.findViewById(R.id.ok);
		//得到取消按钮
		cancel = (Button)view.findViewById(R.id.cancel);
		
		final AlertDialog dialog = builder.create();
		dialog.setView(view);
		dialog.show();
		
		confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String spwd1 = pwd1.getText().toString().trim();
				String spwd2 = pwd2.getText().toString().trim();
				//判空
				if(TextUtils.isEmpty(spwd1)||TextUtils.isEmpty(spwd2)){
					Toast.makeText(HomeActivity.this, "SB密码不能为空！", Toast.LENGTH_LONG).show();
					return;
				}
				//判一致否
				if(spwd1.equals(spwd2)){
					//一致保存
					Editor editor = sp.edit();
					editor.putString("password", spwd1);
					editor.commit();
					dialog.dismiss();
					//进入向导页面
					Intent intent = new Intent(HomeActivity.this, FindActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(HomeActivity.this, "SB两次密码不一致！", Toast.LENGTH_LONG).show();
				}
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}
	//判断密码是否存在
	private boolean isSetPwd() {
		String pwd = sp.getString("password", "");
		/*if(pwd.length()>0&&!"".equals(pwd))
			return true;
		else
			return false;*/
		return !TextUtils.isEmpty(pwd);
	}

	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return names[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view ;
			if(convertView == null)
				view = View.inflate(getApplicationContext(), R.layout.home_list_item, null);
			else
				view = convertView;
			
			ImageView iv = (ImageView) view.findViewById(R.id.item_iv);
			TextView tv = (TextView) view.findViewById(R.id.item_tv);
			iv.setImageResource(icons[position]);
			tv.setText(names[position]);
			return view;
		}
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
