package hxm.com.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class SetGuideActivity4 extends GeneralGuideActivity {
	private CheckBox chk;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setguide4);
		chk = findViewById(R.id.protect_chk);
		//
		sp = getSharedPreferences("config",MODE_PRIVATE);
		Boolean select = sp.getBoolean("protect",false);
		if (select){
			chk.setChecked(true);
			chk.setText("手机防盗已经开启");
		}else {
			chk.setChecked(false);
			chk.setText("手机防盗没有开启");
		}
		chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					chk.setText("手机防盗已经开启");
				}else {
					chk.setText("手机防盗没有开启");
				}
				SharedPreferences.Editor editor = sp.edit();
				editor.putBoolean("protect",isChecked);
				editor.commit();
			}
		});
	}

	@Override
	public void showPrev() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, SetGuideActivity3.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.transtlate_prev_in, R.anim.transtlate_prev_out);

	}

	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean("hasSet",true);
		editor.commit();
		finish();
		overridePendingTransition(R.anim.transtlate_next_in, R.anim.transtlate_next_out);
		Toast.makeText(this, "设置完成", Toast.LENGTH_SHORT).show();
	}
}
