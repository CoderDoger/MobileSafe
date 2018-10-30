package hxm.com.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import hxm.com.mobilesafe.ui.SettingItemView;

public class SettingActivity extends Activity {
	private SettingItemView updateView;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		//得到sharePreferences
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//得到自定义Item set组件
		updateView =  (SettingItemView)findViewById(R.id.setting_update);

		boolean update = sp.getBoolean("update", false);


		//初始化设置
		if(update){
			//自动升级已经开启
			updateView.setChecked(true);
			updateView.setContent("自动升级已打开");
		}else{
			//自动升级已经关闭
			updateView.setChecked(false);
			updateView.setContent("自动升级已关闭");
		}

		updateView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				//得到编辑器
				Editor editor = sp.edit();
				if(updateView.isChecked()){
					updateView.setChecked(false);
					updateView.setContent("自动升级已关闭");
					editor.putBoolean("update", false);
				}else{
					updateView.setChecked(true);
					updateView.setContent("自动升级已打开");
					editor.putBoolean("update", true);
				}
				editor.commit();
			}
		});

	}
}
