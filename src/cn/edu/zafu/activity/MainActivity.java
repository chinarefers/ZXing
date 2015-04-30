package cn.edu.zafu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.edu.zafu.library.android.BeepManager;
import cn.edu.zafu.library.android.CaptureActivity;
import cn.edu.zafu.zxingtest.R;

public class MainActivity extends Activity {

	private static final int REQUEST_CODE_SCAN = 0x0000;
	private static final String DECODED_CONTENT_KEY = "codedContent";

	private Button scanner;
	private TextView result;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		
	}

	private void initView() {
		result = (TextView) findViewById(R.id.result);
		scanner = (Button) findViewById(R.id.scan);
		scanner.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this,
						CaptureActivity.class);
				startActivityForResult(intent, REQUEST_CODE_SCAN);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 扫描二维码/条码回传
		if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
			if (data != null) {
				String content = data.getStringExtra(DECODED_CONTENT_KEY);
				if (content.contains("uuid")) {
					int uuidIndex = content.indexOf("uuid=");
					int uuidEnd = content.indexOf("&", uuidIndex);
					String uuid = content.substring(uuidIndex + 5, uuidEnd);

					int majorIndex = content.indexOf("major=", uuidEnd);
					int majorEnd = content.indexOf("&", majorIndex);
					String major = content.substring(majorIndex + 6, majorEnd);

					int minorIndex = content.indexOf("minor=", majorEnd);
					int minorEnd = content.length();
					String minor = content.substring(minorIndex + 6, minorEnd);

					result.setText("解码结果： \n" + " uuid = " + uuid
							+ "\n major = " + major + "\n minor = " + minor);
				} else {
					result.setText("解码结果： \n未能识别出ibeacon模块，请重新扫描!");
				}
			}
		}
	}
}
