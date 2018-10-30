package hxm.com.mobilesafe;
import android.content.Intent;
import android.os.Bundle;


public class SetGuideActivity1 extends GeneralGuideActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setguide1);
		
		
	}
	

	@Override
	public void showPrev() {
	
	}

	@Override
	public void showNext() {
		Intent intent = new Intent(this, SetGuideActivity2.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.transtlate_next_in, R.anim.transtlate_next_out);
	}
}
