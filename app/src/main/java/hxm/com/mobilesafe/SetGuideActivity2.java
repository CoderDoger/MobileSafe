package hxm.com.mobilesafe;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import hxm.com.mobilesafe.ui.SettingItemView;


public class SetGuideActivity2 extends GeneralGuideActivity {
	private SettingItemView bindSim_v ;
	private SharedPreferences sp;
	private TelephonyManager tm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setguide2);

		//得到sharePreferences
		sp = getSharedPreferences("config", MODE_PRIVATE);
		//
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		bindSim_v = findViewById(R.id.sim);
		String simb = sp.getString("simcard",null);
		if(TextUtils.isEmpty(simb)){
			bindSim_v.setChecked(false);
		}else{
			bindSim_v.setChecked(true);
		}
		bindSim_v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Editor  editor = sp.edit();
				if(bindSim_v.isChecked()){
					bindSim_v.setChecked(false);
					bindSim_v.setContent("SIM卡未绑定");
					editor.putString("simcard", null);
				}else{

					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
						if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
							requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 111);
						}
					}
					String serialNum = tm.getSimSerialNumber();
					bindSim_v.setChecked(true);
					bindSim_v.setContent("SIM卡已经绑定");
					editor.putString("simcard",serialNum);
					Toast.makeText(getApplicationContext(),serialNum,Toast.LENGTH_LONG).show();
				}
				editor.commit();
			}
		});
	}

	@Override
	public void showPrev() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, SetGuideActivity1.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.transtlate_prev_in, R.anim.transtlate_prev_out);
	}

	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		String simb = sp.getString("simcard",null);
		if(TextUtils.isEmpty(simb)){
			Toast.makeText(this,"sim卡没有绑定",Toast.LENGTH_LONG).show();
			return;
		}

		Intent intent = new Intent(this, SetGuideActivity3.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.transtlate_next_in, R.anim.transtlate_next_out);

	}
}
