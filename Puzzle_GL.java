
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

	//�e�{�^��
	private Button[] Button = new Button[11];
	//�e�{�^�����C�A�E�g
	private FrameLayout.LayoutParams[] params = new FrameLayout.LayoutParams[11];

	//�����_���[
	private GLRenderer renderer;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//fullscreenON
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		//titlebarOFF
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		renderer = new GLRenderer(this);//this(Activity)�ŃR���e�L�X�g��n�������ƂɂȂ�

		GLView glView = new GLView(this);

		//GLView to GLRenderer app
		glView.setRenderer(renderer);

		//View the GLView Configration
		setContentView(glView);

		//�e�{�^���ݒ�
		//�X�^�[�g�{�^��
		LayoutButton("Start", 0, 0, 0, 0, 0);
		showButton(0);
		//�C�x���g�ǉ�
		this.Button[0].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				hideButton(0);

				for (int i = 1; i < 11; i++)
					showButton(i);

				renderer.CubeCreate(3);
			}
		});

		//���Z�b�g�{�^��
		LayoutButton("Reset", 1, 0, 350, 0, 0);
		//�C�x���g�ǉ�
		this.Button[1].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeReset();
			}
		});

		//3�{�^��
		LayoutButton("3", 2, -200, 400, 0, 0);
		//�C�x���g�ǉ�
		this.Button[2].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeCreate(3);
			}
		});

		//4�{�^��
		LayoutButton("4", 3, -100, 400, 0, 0);
		//�C�x���g�ǉ�
		this.Button[3].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeCreate(4);
			}
		});

		//5�{�^��
		LayoutButton("5", 4, 0, 400, 0, 0);
		//�C�x���g�ǉ�
		this.Button[4].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeCreate(5);
			}
		});

		//6�{�^��
		LayoutButton("6", 5, 100, 400, 0, 0);
		//�C�x���g�ǉ�
		this.Button[5].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeCreate(6);
			}
		});

		//7�{�^��
		LayoutButton("7", 6, 200, 400, 0, 0);
		//�C�x���g�ǉ�
		this.Button[6].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeCreate(7);
			}
		});

		//8�{�^��
		LayoutButton("8", 7, -200, 450, 0, 0);
		//�C�x���g�ǉ�
		this.Button[7].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeCreate(8);
			}
		});

		//9�{�^��
		LayoutButton("9", 8, -100, 450, 0, 0);
		//�C�x���g�ǉ�
		this.Button[8].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeCreate(9);
			}
		});

		//10�{�^��
		LayoutButton("10", 9, 0, 450, 0, 0);
		//�C�x���g�ǉ�
		this.Button[9].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeCreate(10);
			}
		});

		//�V���b�t���{�^��
		LayoutButton("Sufflie", 10, 100, 350, 0, 0);
		//�C�x���g�ǉ�
		this.Button[10].setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				renderer.CubeSufflie();
			}
		});
	}

	//�{�^�����C�A�E�g
	private void LayoutButton(String str, int no, int left, int top, int right, int bottom) {
		//�{�^�����C�A�E�g
		params[no] = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params[no].gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
		params[no].setMargins(left, top, right, bottom);
		//�{�^���̔z�u
		this.Button[no] = new Button(this);
		this.Button[no].setText(str);
		Button[no].setVisibility(View.GONE);//���������l�߂ď���,INVISIBLE���Ƃ��܂������Ȃ������炭�[���̏������x���̉e��
		addContentView(Button[no], params[no]);
	}

	//�X�^�[�g�{�^���\��
	public void showButton(int no) {
		Button[no].setVisibility(View.VISIBLE);//�\��
	}

	//�X�^�[�g�{�^����\��
	public void hideButton(int no) {
		Button[no].setVisibility(View.INVISIBLE);//����
	}

	//Activity���o�b�N�O���E���h�Ɉړ�����Ƃ��ɌĂяo�����B
	@Override
	public void onPause() {
		super.onPause();

		//�e�N�X�`���폜
		TextureManager.deleteAll(Global.gl);
	}
}
