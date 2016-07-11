
package com.Puzzle_GL;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.lang.System;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.content.Context;
import android.util.Log;
import android.content.res.Resources;

public class GLRenderer implements GLSurfaceView.Renderer {

	//コンテキスト
	private Context mContext;

	//時間
	private long time;

	private float fovy;//画角
	private float zNear;
	private float zFar;
	private float eyeX, eyeY, eyeZ;//視点
	private float centerX, centerY, centerZ;//注視点
	private float upX, upY, upZ;//上方向

	private int mWidth;
	private int mHeight;

	//テクスチャID
	private int mRengaTexture;//レンガ
	private Cube cube;

	public GLRenderer(Context context) {
		this.mContext = context;
		cube = new Cube();
		fovy = 45f;//Ｙ方向の画角
		zNear = 1f;
		zFar = 50f;
		eyeX = 0.0f;
		eyeY = 0.0f;
		eyeZ = 8.0f;
		centerX = centerY = centerZ = 0.0f;
		upX = upZ = 0.0f;
		upY = 1.0f;
		time = System.currentTimeMillis();
	}

	public void CubeCreate(int pcs) {
		cube.CubeCreate(pcs);
	}

	public void CubeReset() {
		cube.CubeCreate(-1);
	}

	public void CubeSufflie() {
		cube.shuffleOn();
	}

	private void getTime() {
		long time2 = System.currentTimeMillis();
		Global.time = time2 - time;
		time = time2;
	}

	private void render2D(GL10 gl) {

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(-1.0f, 1.0f, -1.5f, 1.5f, 0.5f, -0.5f);//これの前に上記2行を書く正射影
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();

		//アルファブレンド有効
		gl.glEnable(GL10.GL_BLEND);
		//合成アルゴリズム, これから描画する画像係数, すでに描画した画像係数
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glPushMatrix();
		{
			gl.glTranslatef(0.0f, 0.0f, 0.0f);
			//Graphic.drawSquare(gl, 0.0f, 0.0f, 1.0f, 1.0f, mRengaTexture, 1.0f, 1.0f, 1.0f, 1.0f);
		}
		gl.glPopMatrix();

		//アルファブレンド無効
		gl.glDisable(GL10.GL_BLEND);
	}

	private void render3D(GL10 gl) {

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, fovy, (float) mWidth / mHeight, zNear, zFar);
		GLU.gluLookAt(gl, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		//アルファブレンド有効
		gl.glEnable(GL10.GL_BLEND);
		//合成アルゴリズム, これから描画する画像係数, すでに描画した画像係数
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		cube.draw(gl, mRengaTexture);

		//アルファブレンド無効
		gl.glDisable(GL10.GL_BLEND);
	}

	@Override
	public void onDrawFrame(GL10 gl) {

		//DepthTest有効
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);

		//描画範囲
		gl.glViewport(0, 0, mWidth, mHeight);

		//クリア
		gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		render3D(gl);
		render2D(gl);

		Global.gl = gl;
		Global.fovy = fovy;
		Global.zNear = zNear;
		Global.zFar = zFar;
		Global.eyeX = eyeX;
		Global.eyeY = eyeY;
		Global.eyeZ = eyeZ;
		Global.centerX = centerX;
		Global.centerY = centerY;
		Global.centerZ = centerZ;
		Global.upX = upX;
		Global.upY = upY;
		Global.upZ = upZ;
		getTime();
	}

	//サーフェスが作成された時やサイズが変わったときに呼ばれる
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

		this.mWidth = width;
		Global.width = width;
		this.mHeight = height;
		Global.height = height;

		//テクスチャ生成
		//レンガ
		//クラス名文字列から取得
		Resources res = mContext.getResources();
		int texId = res.getIdentifier("wall1", "drawable", mContext.getPackageName());
		this.mRengaTexture = Graphic.loadTexture(gl, res, texId);
		if (mRengaTexture == 0) {
			Log.e(getClass().toString(), "texture Load ERROR!");
		}
	}

	public void touched(float dx, float dy, float mx, float my, float ux, float uy, long touchTime, boolean mov) {
		cube.TouchInside(dx, dy, mx, my, ux, uy, touchTime, mov);//openglの座標系に合わせる
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

	}
}
