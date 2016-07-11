
package com.Puzzle_GL;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.view.ViewGroup.LayoutParams;
import android.view.Gravity;
import android.view.View;
import java.lang.String;

public class Puzzle_GL extends Activity {

	//各ボタン
	private Button[] Button = new Button[11];
	//各ボタンレイアウト
	private FrameLayout.LayoutParams[] params = new FrameLayout.LayoutParams[11];

	//レンダラー
	private GLRenderer renderer;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//fullscreenON
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		//titlebarOFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		renderer = new GLRenderer(this);//this(Activity)でコンテキストを渡したことになる

		GLView glView = new GLView(this);

		//GLView to GLRenderer app
		glView.setRenderer(renderer);

		//View the GLView Configration
		setContentView(glView);

		//各ボタン設定
		//スタートボタン
		LayoutButton("Start", 0, 0, 0, 0, 0);
		showButton(0);
		//イベント追加
		this.Button[0].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideButton(0);

				for (int i = 1; i < 11; i++)
					showButton(i);

				renderer.CubeCreate(3);
			}
		});

		//リセットボタン
		LayoutButton("Reset", 1, 0, 350, 0, 0);
		//イベント追加
		this.Button[1].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeReset();
			}
		});

		//3個ボタン
		LayoutButton("3", 2, -200, 400, 0, 0);
		//イベント追加
		this.Button[2].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeCreate(3);
			}
		});

		//4個ボタン
		LayoutButton("4", 3, -100, 400, 0, 0);
		//イベント追加
		this.Button[3].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeCreate(4);
			}
		});

		//5個ボタン
		LayoutButton("5", 4, 0, 400, 0, 0);
		//イベント追加
		this.Button[4].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeCreate(5);
			}
		});

		//6個ボタン
		LayoutButton("6", 5, 100, 400, 0, 0);
		//イベント追加
		this.Button[5].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeCreate(6);
			}
		});

		//7個ボタン
		LayoutButton("7", 6, 200, 400, 0, 0);
		//イベント追加
		this.Button[6].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeCreate(7);
			}
		});

		//8個ボタン
		LayoutButton("8", 7, -200, 450, 0, 0);
		//イベント追加
		this.Button[7].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeCreate(8);
			}
		});

		//9個ボタン
		LayoutButton("9", 8, -100, 450, 0, 0);
		//イベント追加
		this.Button[8].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeCreate(9);
			}
		});

		//10個ボタン
		LayoutButton("10", 9, 0, 450, 0, 0);
		//イベント追加
		this.Button[9].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeCreate(10);
			}
		});

		//シャッフルボタン
		LayoutButton("Sufflie", 10, 100, 350, 0, 0);
		//イベント追加
		this.Button[10].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeSufflie();
			}
		});
	}

	//ボタンレイアウト
	private void LayoutButton(String str, int no, int left, int top, int right, int bottom) {
		//ボタンレイアウト
		params[no] = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params[no].gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
		params[no].setMargins(left, top, right, bottom);
		//ボタンの配置
		this.Button[no] = new Button(this);
		this.Button[no].setText(str);
		Button[no].setVisibility(View.GONE);//メモリを詰めて消す,INVISIBLEだとうまくいかないおそらく端末の処理速度差の影響
		addContentView(Button[no], params[no]);
	}

	//スタートボタン表示
	public void showButton(int no) {
		Button[no].setVisibility(View.VISIBLE);//表示
	}

	//スタートボタン非表示
	public void hideButton(int no) {
		Button[no].setVisibility(View.INVISIBLE);//消す
	}

	//Activityがバックグラウンドに移動するときに呼び出される。
	@Override
	public void onPause() {
		super.onPause();

		//テクスチャ削除
		TextureManager.deleteAll(Global.gl);
	}
}
