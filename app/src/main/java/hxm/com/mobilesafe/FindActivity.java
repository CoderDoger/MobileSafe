package hxm.com.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import hxm.com.mobilesafe.ui.SettingItemView;

public class FindActivity extends Activity {
	private TextView safeNum;
	private ImageView protectStatus;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//
		sp = getSharedPreferences("config",MODE_PRIVATE);
		Boolean hasSet = sp.getBoolean("hasSet",false);
		if(hasSet){
			//
			setContentView(R.layout.activity_find);
			String num = sp.getString("safeNum",null);
			Boolean status = sp.getBoolean("protect",false);
			//
			safeNum = findViewById(R.id.safePhoneNum);
			protectStatus = findViewById(R.id.protectStatus);
			safeNum.setText(num);
			if (status)
				protectStatus.setImageResource(R.mipmap.lock);
			else
				protectStatus.setImageResource(R.mipmap.unlock);
		}else {
			//启动向导
			Intent intent = new Intent(this, SetGuideActivity1.class);
			startActivity(intent);
			finish();
		}

	}
	public void reSetup(View v){
		//启动向导
		Intent intent = new Intent(this, SetGuideActivity1.class);
		startActivity(intent);
		finish();
	}
}
