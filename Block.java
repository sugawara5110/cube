
package com.Puzzle_GL;

import javax.microedition.khronos.opengles.GL10;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import android.util.Log;

public class Block {

	//回転
	private float theta;
	private float theta_distance;
	private boolean thetamov_f[] = new boolean[3];
	private int select;

	//シャッフル用
	private float sfTheta;

	//回転方向±
	private boolean Plus;

	//手前の領域確認用
	public float squareZ;

	//回転合成行列
	private Matrix3D thetaMat3 = new Matrix3D();//保持
	private Matrix3D thetaMat3mov = new Matrix3D();//移動中
	private Matrix3D thetaM1 = new Matrix3D();//1方向回転行列生成用

	//頂点バッファ
	private FloatBuffer polygonVertices;
	private FloatBuffer polygonColors;
	private FloatBuffer polygonColorsTrue;
	private FloatBuffer texCoords;
	private ByteBuffer indexBf;

	//ローカル座標
	private float[] vertices = new float[24];

	//タッチ計算用全面座標
	private Vector3D[] ver = new Vector3D[8];

	//タッチ有無確認用
	public boolean touch_on;

	public Block() {
		for (int i = 0; i < 3; i++)
			thetamov_f[i] = false;
		theta = 0.0f;
		theta_distance = 0.0f;

		for (int i = 0; i < 8; i++)
			ver[i] = new Vector3D();

		thetaMat3.MatrixIdentity();
		thetaMat3mov.MatrixIdentity();
		thetaM1.MatrixIdentity();
		touch_on = false;
		sfTheta = 0.0f;
	}

	//2D座標計算,タッチ位置判別
	public void TouchInside(Matrix3D ThetaTemp, Matrix3D Look, Matrix3D viewPort, Matrix3D Per, float x, float y) {
		for (int i = 0; i < 8; i++) {
			ver[i].as(vertices[i * 3], vertices[i * 3 + 1], vertices[i * 3 + 2]);
			ver[i].VectorMatrixMultiply(ThetaTemp);
			ver[i].VectorMatrixMultiply(Look);
			ver[i].VectorMatrixMultiply(Per);
			ver[i].VectorMatrixMultiply(viewPort);
			ver[i].VectorViewAfter();
		}

		float MaxX = 0.0f;
		float MaxY = 0.0f;
		float MinX = Global.width;
		float MinY = Global.height;
		float MaxZ = -10.0f;
		float MinZ = 20.0f;
		for (int i = 0; i < 8; i++) {
			if (ver[i].x > MaxX)
				MaxX = ver[i].x;
			if (ver[i].x < MinX)
				MinX = ver[i].x;
			if (ver[i].y > MaxY)
				MaxY = ver[i].y;
			if (ver[i].y < MinY)
				MinY = ver[i].y;
			if (ver[i].z > MaxZ)
				MaxZ = ver[i].z;
			if (ver[i].z < MinZ)
				MinZ = ver[i].z;
		}
		squareZ = (MaxZ + MinZ) / 2;
		if (MinX < x && x < MaxX && MinY < y && y < MaxY) {
			Log.i(getClass().toString(), String.format("TX = %f  TY = %f", x, y));
			touch_on = true;
		}
	}

	//Cube側から呼び出され他のマトリックスと掛け合わせる
	public Matrix3D ThetaMat() {
		return thetaMat3mov;
	}

	public void draw(GL10 gl, int texture) {
		if (touch_on)
			Graphic.draw(gl, texture, polygonVertices, polygonColorsTrue, texCoords, indexBf, 3, 24);
		else
			Graphic.draw(gl, texture, polygonVertices, polygonColors, texCoords, indexBf, 3, 24);
	}

	public void CreateVertices(float x, float y, float z, float size, float r0, float g0, float b0, float a0, float r1,
			float g1, float b1, float a1, float r2, float g2, float b2, float a2, float r3, float g3, float b3,
			float a3, float r4, float g4, float b4, float a4, float r5, float g5, float b5, float a5) {
		float vert[] = Graphic.createBlockVer(x, y, z, size);
		for (int i = 0; i < 24; i++)
			vertices[i] = vert[i];

		polygonVertices = Graphic.makeFloatBuffer(vert);
		polygonColors = Graphic.makeFloatBuffer(Graphic.createBlockClr(r0, g0, b0, a0, r1, g1, b1, a1, r2, g2, b2, a2,
				r3, g3, b3, a3, r4, g4, b4, a4, r5, g5, b5, a5));
		polygonColorsTrue = Graphic.makeFloatBuffer(Graphic.createBlockClr(1.0f, 0.0f, 0.0f, 0.4f, 0.0f, 1.0f, 0.0f,
				0.4f, 0.0f, 0.0f, 1.0f, 0.4f, 1.0f, 1.0f, 0.0f, 0.4f, 0.0f, 1.0f, 1.0f, 0.4f, 1.0f, 1.0f, 1.0f, 0.4f));
		texCoords = Graphic.makeFloatBuffer(Graphic.createBlockCoord());
		indexBf = Graphic.makeByteBuffer(Graphic.createBlockInd());
	}

	public void Rotate() {
		if (thetamov_f[select]) {
			switch (select) {
			case 0:
				thetaM1.MatrixRotationX(theta);
				break;
			case 1:
				thetaM1.MatrixRotationY(theta);
				break;
			case 2:
				thetaM1.MatrixRotationZ(theta);
				break;
			}
			thetaMat3mov.MatrixMultiply(thetaMat3, thetaM1);
			thetaMove(select);
		}
	}

	public void thetaMoveInit(int sel, boolean plus) {
		select = sel;
		Plus = plus;
		if (thetamov_f[sel] == false) {
			if (plus) {
				theta_distance = 90.0f;
			} else {
				theta_distance = -90.0f;
			}
			thetamov_f[sel] = true;
		}
	}

	public boolean thetamov_f_check() {
		boolean ch = false;
		for (int i = 0; i < 3; i++) {
			if (thetamov_f[i])
				ch = true;
		}
		if (ch)
			return true;
		return false;
	}

	public void shuffle(boolean f) {
		if (f)
			sfTheta = 90.0f;
		else
			sfTheta = 0.0f;
	}

	private void thetaMove(int sel) {
		if (thetamov_f[sel]) {
			boolean thetamovFin = false;
			float f = (float) Global.time / 20.0f + sfTheta;
			if (f == 0.0f)
				f = 0.001f;//完全停止防止
			Log.i(getClass().toString(), String.format("f = %f", f));
			if (Plus) {
				if ((theta += f) > theta_distance)
					thetamovFin = true;
			} else {
				if ((theta -= f) < theta_distance)
					thetamovFin = true;
			}
			if (thetamovFin) {
				theta = 0.0f;
				switch (sel) {
				case 0:
					thetaM1.MatrixRotationX(theta_distance);
					break;
				case 1:
					thetaM1.MatrixRotationY(theta_distance);
					break;
				case 2:
					thetaM1.MatrixRotationZ(theta_distance);
					break;
				}
				thetaMat3mov.MatrixMultiply(thetaMat3, thetaM1);
				for (int i = 0; i < 16; i++)
					thetaMat3.m[i] = thetaMat3mov.m[i];
				thetaM1.MatrixIdentity();
				thetamov_f[sel] = false;
			}
		}
	}
}
