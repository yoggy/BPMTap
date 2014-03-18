package net.sabamiso.android.bpmtap;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

public class BPMTapActivity extends Activity {

	long last_t;
	double bpm;
	double dulation;
	double height;
	
	TextView text_view_bpm;
	TextView text_view_dulation;
	TextView text_view_height;
	
	Handler handler = new Handler();
	Runnable check_task = new Runnable() {
		@Override
		public void run() {
			long now_t = System.currentTimeMillis();
			long diff = now_t - last_t;
			if (diff > 3000) {
				clearBPM();
			}
			handler.postDelayed(check_task, 1000);
		}
	};
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bpmtap);
	
		text_view_bpm = (TextView)findViewById(R.id.textViewBPM);
		text_view_dulation = (TextView)findViewById(R.id.textViewDulation);
		text_view_height = (TextView)findViewById(R.id.textViewHeight);
		
		handler.postDelayed(check_task, 1000);
	}

	@Override
	protected void onResume(){
		super.onResume();
		clearBPM();
	}
	
	@Override
	protected  void onPause() {
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_bpmtap, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.clear:
	            clearBPM();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
    public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
	    case MotionEvent.ACTION_DOWN:
			calcBPM();
	        break;
	    case MotionEvent.ACTION_UP:
	        break;
	    case MotionEvent.ACTION_MOVE:
	        break;
	    case MotionEvent.ACTION_CANCEL:
	        break;
	    }
        return true;
    }

	protected void calcBPM() {
		long now_t = System.currentTimeMillis();
		if (last_t != 0) {
			dulation = (now_t - last_t) / 1000.0;
			bpm = 60.0 / dulation;
			
			double t = dulation / 2;
			height = 1/2.0 * SensorManager.GRAVITY_EARTH * (t * t);
		}		
		last_t = now_t;
		
		updateView();
	}
	
	protected void clearBPM() {
		last_t = 0;
		bpm = 0.0;
		dulation = 0.0;
		height = 0.0;
		
		updateView();
	}

	protected void updateView() {
		if (text_view_bpm != null) {
			text_view_bpm.setText(String.format("%.2f", bpm));
		}
		
		if (text_view_dulation != null) {
			text_view_dulation.setText(String.format("%.2f", dulation));
		}

		if (text_view_height != null) {
			text_view_height.setText(String.format("%.3f", height));
		}
	}
}
