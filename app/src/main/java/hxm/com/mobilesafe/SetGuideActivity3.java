package hxm.com.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class SetGuideActivity3 extends GeneralGuideActivity {
	private EditText safeNum;
	private SharedPreferences sp ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setguide3);
		//
		sp = getSharedPreferences("config",MODE_PRIVATE);
		String phoneNum = sp.getString("safeNum",null);
		//
		safeNum = findViewById(R.id.safeNum);
		safeNum.setText(phoneNum);
	}

	//选择联系人
	public void chooseContact(View v){
		Intent intent = new Intent(this,ChooseContactActivity.class);
		startActivityForResult(intent,200);
	}
	//对返回值进行处理
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data==null)
			return;
		if (resultCode==200){
			String num = data.getStringExtra("phoneNum");
			safeNum.setText(num);
		}
	}

	@Override
	public void showPrev() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, SetGuideActivity2.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.transtlate_prev_in, R.anim.transtlate_prev_out);

	}

	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		SharedPreferences.Editor editor = sp.edit();
		String num = safeNum.getText().toString().trim();
		if (TextUtils.isEmpty(num)){
			Toast.makeText(this,"安全号码还没有设置",Toast.LENGTH_SHORT).show();
			return;
		}
		//保存安全号码
		editor.putString("safeNum",num)	;
		editor.commit();

		Intent intent = new Intent(this, SetGuideActivity4.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.transtlate_next_in, R.anim.transtlate_next_out);

	}
}
