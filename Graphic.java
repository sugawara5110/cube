
package com.Puzzle_GL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.graphics.Bitmap.Config;

import javax.microedition.khronos.opengles.GL10;

public class Graphic {

	public static final void drawRectangle(GL10 gl, float x, float y, float width, float height, 
			                                float r, float g, float b, float a){
	 float[] vertices = {
        	-0.5f * width  + x, -0.5f * height + y,
        	0.5f * width  + x,  -0.5f * height + y,
        	-0.5f * width  + x, 0.5f * height + y,
        	0.5f * width  + x, 0.5f * height + y,
        };
		
        float[] colors = {
        	r, g, b, a,
        	r, g, b, a,
        	r, g, b, a,
        	r, g, b, a,
        };

        FloatBuffer polygonVertices = makeFloatBuffer(vertices);
        FloatBuffer polygonColors = makeFloatBuffer(colors);

		//頂点バッファ転送, 第一引数:頂点毎のデータ数2D=2 3D=3 
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, polygonVertices);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glColorPointer(4, GL10.GL_FLOAT, 0, polygonColors);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		//描画, 第四引数頂点数
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}

	public static final void drawBlock(GL10 gl, float x, float y, float z, float size, int texture, float r0, float g0,
			float b0, float a0, float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2,
			float r3, float g3, float b3, float a3, float r4, float g4, float b4, float a4, float r5, float g5,
			float b5, float a5) {

		float[] vertices = createBlockVer(x, y, z, size);

		float[] colors = createBlockClr(r0, g0, b0, a0, r1, g1, b1, a1, r2, g2, b2, a2, r3, g3, b3, a3, r4, g4, b4, a4,
				r5, g5, b5, a5);

		byte[] index = createBlockInd();

		float[] coords = createBlockCoord();

		FloatBuffer polygonVertices = makeFloatBuffer(vertices);
		FloatBuffer polygonColors = makeFloatBuffer(colors);
		FloatBuffer texCoords = makeFloatBuffer(coords);
		ByteBuffer indexBf = makeByteBuffer(index);

		draw(gl, texture, polygonVertices, polygonColors, texCoords, indexBf, 3, 24);
	}

	public static final float[] createBlockVer(float x, float y, float z, float size) {

		float[] vertices = {
				// 前 
				-size + x, -size + y, size + z, size + x, -size + y, size + z, -size + x, size + y, size + z, size + x,
				size + y, size + z,
				// 後 
				-size + x, -size + y, -size + z, size + x, -size + y, -size + z, -size + x, size + y, -size + z,
				size + x, size + y, -size + z,
				// 左 
				-size + x, -size + y, size + z, -size + x, -size + y, -size + z, -size + x, size + y, size + z,
				-size + x, size + y, -size + z,
				// 右
				size + x, -size + y, size + z, size + x, -size + y, -size + z, size + x, size + y, size + z, size + x,
				size + y, -size + z,
				// 上 
				-size + x, size + y, size + z, size + x, size + y, size + z, -size + x, size + y, -size + z, size + x,
				size + y, -size + z,
				// 底 
				-size + x, -size + y, size + z, size + x, -size + y, size + z, -size + x, -size + y, -size + z,
				size + x, -size + y, -size + z, };

		return vertices;
	}

	public static final float[] createBlockClr(float r0, float g0, float b0, float a0, float r1, float g1, float b1,
			float a1, float r2, float g2, float b2, float a2, float r3, float g3, float b3, float a3, float r4,
			float g4, float b4, float a4, float r5, float g5, float b5, float a5) {

		float[] colors = {

				r0, g0, b0, a0, r0, g0, b0, a0, r0, g0, b0, a0, r0, g0, b0, a0,

				r1, g1, b1, a1, r1, g1, b1, a1, r1, g1, b1, a1, r1, g1, b1, a1,

				r2, g2, b2, a2, r2, g2, b2, a2, r2, g2, b2, a2, r2, g2, b2, a2,

				r3, g3, b3, a3, r3, g3, b3, a3, r3, g3, b3, a3, r3, g3, b3, a3,

				r4, g4, b4, a4, r4, g4, b4, a4, r4, g4, b4, a4, r4, g4, b4, a4,

				r5, g5, b5, a5, r5, g5, b5, a5, r5, g5, b5, a5, r5, g5, b5, a5, };
		return colors;
	}

	public static final byte[] createBlockInd() {

		byte[] index = {

				0, 1, 2, 1, 3, 2, 4, 5, 6, 5, 7, 6, 8, 9, 10, 9, 11, 10, 12, 13, 14, 13, 15, 14, 16, 17, 18, 17, 19, 18,
				20, 21, 22, 21, 23, 22, };
		return index;
	}

	public static final float[] createBlockCoord() {

		float[] coords = {

				0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

				0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

				0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

				0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

				0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,

				0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, };
		return coords;
	}

	public static final void drawSquare(GL10 gl, float x, float y, float width, float height, int texture, float r,
			float g, float b, float a) {

		float[] vertices = { -0.5f * width + x, -0.5f * height + y, 0.5f * width + x, -0.5f * height + y,
				-0.5f * width + x, 0.5f * height + y, 0.5f * width + x, 0.5f * height + y, };

		byte[] index = { 0, 1, 2, 1, 3, 2 };

		float[] colors = { r, g, b, a, r, g, b, a, r, g, b, a, r, g, b, a, };

		float[] coords = { 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, };

		FloatBuffer polygonVertices = makeFloatBuffer(vertices);
		FloatBuffer polygonColors = makeFloatBuffer(colors);
		FloatBuffer texCoords = makeFloatBuffer(coords);
		ByteBuffer indexBf = makeByteBuffer(index);

		draw(gl, texture, polygonVertices, polygonColors, texCoords, indexBf, 2, 4);
	}

	public static final void draw(GL10 gl, int texture, FloatBuffer polygonVertices, FloatBuffer polygonColors,
			FloatBuffer texCoords, ByteBuffer indexBf, int d, int ver) {

		//テクスチャ有効化
		gl.glEnable(GL10.GL_TEXTURE_2D);
		//テクスチャオブジェクトの指定
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
		//頂点バッファ転送, 第一引数:頂点毎のデータ数2D=2 3D=3 
		gl.glVertexPointer(d, GL10.GL_FLOAT, 0, polygonVertices);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glColorPointer(4, GL10.GL_FLOAT, 0, polygonColors);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoords);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		//描画, 第四引数頂点数(インデックス無し描画)
		//gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, ver);
		//インデックス有り描画
		gl.glDrawElements(GL10.GL_TRIANGLES, (int) (ver * 1.5), GL10.GL_UNSIGNED_BYTE, indexBf);

		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//バッファリセット, 次回描画時影響でないようにする為
		//テクスチャ無効化
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}

	public static final int loadTexture(GL10 gl, Resources resources, int resId) {

		int[] textures = new int[1];

		//Bitmap作成
		Bitmap bmp = BitmapFactory.decodeResource(resources, resId, options);
		if (bmp == null)
			return 0;

		//openGL用テクスチャ生成
		gl.glGenTextures(1, textures, 0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);

		//openGLへ転送完了, VMメモリ上に作成したbitmap破棄
		bmp.recycle();

		//TextureManagerに登録
		TextureManager.addTexture(resId, textures[0]);

		return textures[0];
	}

	private static final BitmapFactory.Options options = new BitmapFactory.Options();

	static {
		//リソース自動リサイズ無し
		options.inScaled = false;
		//32bit画像といて読み込む
		options.inPreferredConfig = Config.ARGB_8888;
	}

	public static final FloatBuffer makeFloatBuffer(float[] arr) {
		//システムメモリ領域確保
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(arr);//配列転送
		fb.position(0);
		return fb;
	}

	public static ByteBuffer makeByteBuffer(byte[] array) {
		ByteBuffer bb = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
		bb.put(array).position(0);
		return bb;
	}
}
