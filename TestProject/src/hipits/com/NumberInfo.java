package hipits.com;

import java.io.ObjectOutputStream.PutField;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NumberInfo extends Activity {
	TextView text_name, text_number, text_date, text_massage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.numberinfomation);
		
		text_name = (TextView) findViewById(R.id.textView_name);
		text_number = (TextView) findViewById(R.id.textView_number);
		text_date = (TextView) findViewById(R.id.textView_date);
		text_massage = (TextView) findViewById(R.id.textView_massage);
		
		Intent intent = getIntent();
		text_number.setText(intent.getStringExtra("number"));
	}
}
