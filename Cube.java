
package com.Puzzle_GL;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import java.lang.Math;
import java.util.Random;

public class Cube {

	private int b_width;
	private int b_pcs;
	private int b_sur_pcs;
	private boolean creating;
	private boolean drawing;
	private boolean touching;
	private boolean shuffling;
	private int shuffleCnt;

	private class BlockArray {
		public int Block_No;
		public int directionX;
		public int directionY;
		public int directionZ;

		public BlockArray() {
			directionX = directionY = directionZ = -1;
		}
	}

	private Block[] B_obj;//各ブロックobj
	private BlockArray[] B_array;//cube配列からの各ブロックアクセス
	private int[] B_arrayTemp;
	private Matrix3D BlockAll = new Matrix3D();//cube現回転行列
	private Matrix3D BlockAllX = new Matrix3D();//X軸回転行列
	private Matrix3D BlockAllY = new Matrix3D();//Y軸回転行列
	private float BlockAllThetaX, BlockAllThetaY;
	private Matrix3D thetaCube = new Matrix3D();//合成後回転行列

	//タッチ計算用マトリックス
	private Matrix3D Look;//視点
	private Matrix3D ThetaTemp;
	private Matrix3D viewPort;
	private Matrix3D Per;

	//タッチ順格納
	private class TouchOrder {
		float z;
		int no;
	}

	private TouchOrder[] touchOrder;
	private int tc;

	//ブロックカウント用
	private int[] count;

	public Cube() {
		creating = false;
		drawing = false;
		touching = false;
		shuffling = false;
		Look = new Matrix3D();
		Look.MatrixIdentity();
		ThetaTemp = new Matrix3D();
		ThetaTemp.MatrixIdentity();
		viewPort = new Matrix3D();
		viewPort.MatrixIdentity();
		Per = new Matrix3D();
		Per.MatrixIdentity();
		BlockAll.MatrixIdentity();
		BlockAllX.MatrixIdentity();
		BlockAllY.MatrixIdentity();
		BlockAllThetaX = BlockAllThetaY = 0.0f;
	}

	public void CubeCreate(int b_w) {
		while (drawing || touching) {
		}
		creating = true;
		if (b_w != -1)
			b_width = b_w;
		b_sur_pcs = 0;
		for (int z = 0; z < b_width; z++) {
			for (int y = 0; y < b_width; y++) {
				for (int x = 0; x < b_width; x++) {
					if (z == 0 || z == b_width - 1 || y == 0 || y == b_width - 1 || x == 0 || x == b_width - 1)
						b_sur_pcs++;
				}
			}
		}

		b_pcs = b_width * b_width * b_width;

		B_obj = new Block[b_sur_pcs];
		B_array = new BlockArray[b_pcs];
		B_arrayTemp = new int[b_pcs];
		touchOrder = new TouchOrder[b_sur_pcs];
		count = new int[b_width * 3];

		int cn = -1;
		int zp = b_width * b_width;
		int yp = b_width;
		for (int z = 0; z < b_width; z++) {
			for (int y = 0; y < b_width; y++) {
				for (int x = 0; x < b_width; x++) {
					B_array[z * zp + y * yp + x] = new BlockArray();
					if (z == 0 || z == b_width - 1 || y == 0 || y == b_width - 1 || x == 0 || x == b_width - 1) {
						cn++;
						B_array[z * zp + y * yp + x].Block_No = cn;
					} else
						B_array[z * zp + y * yp + x].Block_No = -1;//無いとこは-1
				}
			}
		}
		for (int i = 0; i < b_sur_pcs; i++) {
			B_obj[i] = new Block();
			touchOrder[i] = new TouchOrder();
			touchOrder[i].no = -1;
			touchOrder[i].z = -1f;
		}
		tc = 0;
		float b_size = 1.8f / (float) b_width;
		float h_size = 0.0f;
		if ((b_width % 2) == 0)
			h_size = b_size / 2.0f;
		//ローカル座標設定
		for (int z = 0; z < b_width; z++) {
			for (int y = 0; y < b_width; y++) {
				for (int x = 0; x < b_width; x++) {
					float zz = (z - b_width / 2) * b_size + h_size;
					float yy = (y - b_width / 2) * b_size + h_size;
					float xx = (x - b_width / 2) * b_size + h_size;
					if (B_array[z * zp + y * yp + x].Block_No == -1)
						continue;
					B_obj[B_array[z * zp + y * yp + x].Block_No].CreateVertices(xx, yy, zz, b_size / 2.2f, 1.0f, 0.0f,
							0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f,
							1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
				}
			}
		}
		//ブロックX軸回転方向設定
		int di = 0;
		for (int y = b_width - 1; y >= 0; y--) {
			int i = (b_width - 1) * zp + y * yp + 0;
			for (int p = 0; p < b_width; p++)
				B_array[i + p].directionX = di;
			di++;
		}
		for (int z = b_width - 2; z >= 0; z--) {
			int i = z * zp + 0 * yp + 0;
			for (int p = 0; p < b_width; p++)
				B_array[i + p].directionX = di;
			di++;
		}
		for (int y = 1; y < b_width; y++) {
			int i = 0 * zp + y * yp + 0;
			for (int p = 0; p < b_width; p++)
				B_array[i + p].directionX = di;
			di++;
		}
		for (int z = 1; z < b_width - 1; z++) {
			int i = z * zp + (b_width - 1) * yp + 0;
			for (int p = 0; p < b_width; p++)
				B_array[i + p].directionX = di;
			di++;
		}
		//ブロックY軸回転方向設定
		di = 0;
		for (int x = 0; x < b_width; x++) {
			int i = (b_width - 1) * zp + (b_width - 1) * yp + x;
			for (int p = 0; p < b_width; p++)
				B_array[i - p * yp].directionY = di;
			di++;
		}
		for (int z = b_width - 2; z >= 0; z--) {
			int i = z * zp + (b_width - 1) * yp + (b_width - 1);
			for (int p = 0; p < b_width; p++)
				B_array[i - p * yp].directionY = di;
			di++;
		}
		for (int x = b_width - 2; x >= 0; x--) {
			int i = 0 * zp + (b_width - 1) * yp + x;
			for (int p = 0; p < b_width; p++)
				B_array[i - p * yp].directionY = di;
			di++;
		}
		for (int z = 1; z < b_width - 1; z++) {
			int i = z * zp + (b_width - 1) * yp + 0;
			for (int p = 0; p < b_width; p++)
				B_array[i - p * yp].directionY = di;
			di++;
		}
		//ブロックZ軸回転方向設定
		di = 0;
		for (int y = 0; y < b_width; y++) {
			int i = 0 * zp + y * yp + (b_width - 1);
			for (int p = 0; p < b_width; p++)
				B_array[i + p * zp].directionZ = di;
			di++;
		}
		for (int x = b_width - 2; x >= 0; x--) {
			int i = 0 * zp + (b_width - 1) * yp + x;
			for (int p = 0; p < b_width; p++)
				B_array[i + p * zp].directionZ = di;
			di++;
		}
		for (int y = b_width - 2; y >= 0; y--) {
			int i = 0 * zp + y * yp + 0;
			for (int p = 0; p < b_width; p++)
				B_array[i + p * zp].directionZ = di;
			di++;
		}
		for (int x = 1; x < b_width - 1; x++) {
			int i = 0 * zp + 0 * yp + x;
			for (int p = 0; p < b_width; p++)
				B_array[i + p * zp].directionZ = di;
			di++;
		}

		thetaCube.MatrixIdentity();
		creating = false;
	}

	public void TouchInside(float dx, float dy, float mx, float my, float ux, float uy, long touchTime, boolean mov) {
		if (creating || shuffling)
			return;
		touching = true;
		Look.MatrixLookAtRH(Global.eyeX, Global.eyeY, Global.eyeZ, Global.centerX, Global.centerY, Global.centerZ,
				Global.upX, Global.upY, Global.upZ);
		viewPort.MatrixViewPort();
		Per.MatrixPerspectiveFovRH(Global.fovy, Global.width / Global.height, Global.zNear, Global.zFar);

		Log.i(getClass().toString(), "CubeTouchInside");
		boolean cubeTouchSuccess = false;

		//タッチ領域検出
		for (int i = 0; i < b_pcs; i++) {
			if (B_array[i].Block_No == -1)
				continue;
			ThetaTemp.MatrixMultiply(B_obj[B_array[i].Block_No].ThetaMat(), BlockAll);
			B_obj[B_array[i].Block_No].TouchInside(ThetaTemp, Look, viewPort, Per, mx, my);
			if (B_obj[B_array[i].Block_No].touch_on) {
				cubeTouchSuccess = true;
				boolean f = false;
				//同じ要素が有ればtrue
				for (int i1 = 0; i1 <= tc; i1++) {
					if (touchOrder[i1].no == i) {
						f = true;
						break;
					}
				}
				//falseの場合ブロック№格納
				if (!f) {
					touchOrder[tc].no = i;
					touchOrder[tc++].z = B_obj[B_array[i].Block_No].squareZ;
				}
			}
		}

		//回転中のブロック検出(検出の場合ブロック回転中止、キューブ回転は有り)
		boolean ch = false;
		for (int i = 0; i < b_sur_pcs; i++) {
			if (B_obj[i].thetamov_f_check()) {
				ch = true;
				break;
			}
		}

		//タッチから離れた場合の処理
		int zp = b_width * b_width;
		int yp = b_width;
		if (mov) {
			boolean b_mov = false;
			//回転中ブロック無し, ブロックタッチ有の場合実行
			if (!ch && cubeTouchSuccess) {

				for (int i = 0; i < b_width * 3; i++)
					count[i] = 0;
				int cInd = 0;
				//ブロックの各回転領域毎にタッチ数カウント(XYZ各b_width個)
				//X
				for (int x = 0; x < b_width; x++) {
					for (int z = 0; z < b_width; z++) {
						for (int y = 0; y < b_width; y++) {
							if (B_array[z * zp + y * yp + x].Block_No == -1)
								continue;
							if (B_obj[B_array[z * zp + y * yp + x].Block_No].touch_on)
								count[cInd]++;
						}
					}
					cInd++;
				}
				//Y
				for (int y = 0; y < b_width; y++) {
					for (int z = 0; z < b_width; z++) {
						for (int x = 0; x < b_width; x++) {
							if (B_array[z * zp + y * yp + x].Block_No == -1)
								continue;
							if (B_obj[B_array[z * zp + y * yp + x].Block_No].touch_on)
								count[cInd]++;
						}
					}
					cInd++;
				}
				//Z
				for (int z = 0; z < b_width; z++) {
					for (int y = 0; y < b_width; y++) {
						for (int x = 0; x < b_width; x++) {
							if (B_array[z * zp + y * yp + x].Block_No == -1)
								continue;
							if (B_obj[B_array[z * zp + y * yp + x].Block_No].touch_on)
								count[cInd]++;
						}
					}
					cInd++;
				}

				//touch_on初期化
				for (int i = 0; i < b_pcs; i++) {
					if (B_array[i].Block_No == -1)
						continue;
					B_obj[B_array[i].Block_No].touch_on = false;
				}
				//カウント判定, タッチ数が一番多い領域を回転対象とするmIndに対象番号格納(XYZ各b_width個)
				int mcnt = b_width;//Maxカウント数
				int mInd = -1;//Maxカウント要素インデックス
				for (int i = 0; i < b_width * 3; i++) {
					if (count[i] > mcnt) {
						mcnt = count[i];
						mInd = i;
					}
					Log.i(getClass().toString(), String.format("count[i] = %d", count[i]));
				}
				//回転対象無しの場合スキップ
				if (mInd != -1) {
					b_mov = true;
					//タッチ順探索
					float[] tfl = new float[tc];
					for (int i = 0; i < tc; i++) {
						tfl[i] = touchOrder[i].z;
					}
					//z値順に並び替え
					tfl = Vector3D.bsort(tfl, tc);
					//タッチ数個分の配列用意
					int[] tch = new int[tc];
					int tchI = 0;
					//z座標手前&&回転方向用数値0より大きいブロックを元の順で検索格納
					for (int i = 0; i < tc; i++) {
						Log.i(getClass().toString(), String.format("tfl[%d] =  %f", i, tfl[i]));
						if (tfl[b_width - 2] <= touchOrder[i].z && (mInd < b_width && B_array[i].directionX > 0
								|| mInd >= b_width && mInd <= (b_width * 2 - 1) && B_array[i].directionY > 0
								|| mInd >= b_width * 2 && mInd <= (b_width * 3 - 1) && B_array[i].directionZ > 0))
							tch[tchI++] = touchOrder[i].no;
					}

					boolean TX = false;
					boolean TY = false;
					boolean TZ = false;

					if (B_array[tch[0]].directionX < B_array[tch[1]].directionX)
						TX = true;
					if (B_array[tch[0]].directionY < B_array[tch[1]].directionY)
						TY = true;
					if (B_array[tch[0]].directionZ < B_array[tch[1]].directionZ)
						TZ = true;

					RotateInit(mInd, TX, TY, TZ);
				}
			}
			//タッチはされたがブロックを動かさない状態、でありキューブを動かす
			if (!b_mov) {
				float tx, ty, tx1, ty1;
				tx = (ux - dx);
				ty = (uy - dy);
				tx1 = Math.abs(tx);
				if (tx1 < 3.0f)
					tx1 = 0.0f;
				ty1 = Math.abs(ty);
				if (ty1 < 3.0f)
					ty1 = 0.0f;

				if (tx1 > ty1) {
					ty1 /= tx1;
					tx1 = 1.0f;
				}
				if (ty1 > tx1) {
					tx1 /= ty1;
					ty1 = 1.0f;
				}
				if (tx1 == ty1 && tx1 != 0) {
					tx1 = ty1 = 0.0f;
				}

				float f = (float) Global.time / 20.0f;
				if (f == 0.0f)
					f = 0.001f;//完全停止防止

				BlockAllThetaX = tx1 * f;
				BlockAllThetaY = ty1 * f;

				if (tx < 0)
					BlockAllThetaX *= -1.0f;
				if (ty < 0)
					BlockAllThetaY *= -1.0f;
			}
			tc = 0;
		}
		touching = false;
	}

	private void RotateInit(int mInd, boolean TX, boolean TY, boolean TZ) {
		int cInd = 0;
		int zp = b_width * b_width;
		int yp = b_width;
		//回転準備mIndと一致する領域を回転させる
		//X
		for (int x = 0; x < b_width; x++) {
			if (mInd != cInd) {
				cInd++;
				continue;
			}
			for (int z = 0; z < b_width; z++) {
				for (int y = 0; y < b_width; y++) {
					if (B_array[z * zp + y * yp + x].Block_No == -1)
						continue;
					B_obj[B_array[z * zp + y * yp + x].Block_No].thetaMoveInit(0, TX);
					Log.i(getClass().toString(), String.format("BX = %d", B_array[z * zp + y * yp + x].Block_No));
				}
			}
			cInd++;
		}
		//Y
		for (int y = 0; y < b_width; y++) {
			if (mInd != cInd) {
				cInd++;
				continue;
			}
			for (int z = 0; z < b_width; z++) {
				for (int x = 0; x < b_width; x++) {
					if (B_array[z * zp + y * yp + x].Block_No == -1)
						continue;
					B_obj[B_array[z * zp + y * yp + x].Block_No].thetaMoveInit(1, TY);
					Log.i(getClass().toString(), String.format("BY = %d", B_array[z * zp + y * yp + x].Block_No));
				}
			}
			cInd++;
		}
		//Z
		for (int z = 0; z < b_width; z++) {
			if (mInd != cInd) {
				cInd++;
				continue;
			}
			for (int y = 0; y < b_width; y++) {
				for (int x = 0; x < b_width; x++) {
					if (B_array[z * zp + y * yp + x].Block_No == -1)
						continue;
					B_obj[B_array[z * zp + y * yp + x].Block_No].thetaMoveInit(2, TZ);
					Log.i(getClass().toString(), String.format("BZ = %d", B_array[z * zp + y * yp + x].Block_No));
				}
			}
			cInd++;
		}

		for (int i = 0; i < b_pcs; i++)
			B_arrayTemp[i] = B_array[i].Block_No;

		//回転後cube配列回転
		//X
		if (mInd < b_width) {
			for (int z = 0; z < b_width; z++) {
				for (int y = 0; y < b_width; y++) {
					if (TX)
						B_array[y * zp + (b_width - 1 - z) * yp + mInd].Block_No = B_arrayTemp[z * zp + y * yp + mInd];
					else
						B_array[(b_width - 1 - y) * zp + z * yp + mInd].Block_No = B_arrayTemp[z * zp + y * yp + mInd];
				}
			}
		}
		for (int i = 0; i < b_pcs; i++)
			Log.i(getClass().toString(), String.format("B_array[%d].Block_No = %d", i, B_array[i].Block_No));
		//Y
		if (mInd >= b_width && mInd <= b_width * 2 - 1) {
			int mI = mInd - b_width;
			for (int z = 0; z < b_width; z++) {
				for (int x = 0; x < b_width; x++) {
					if (TY)
						B_array[(b_width - 1 - x) * zp + mI * yp + z].Block_No = B_arrayTemp[z * zp + mI * yp + x];
					else
						B_array[x * zp + mI * yp + (b_width - 1 - z)].Block_No = B_arrayTemp[z * zp + mI * yp + x];
				}
			}
		}
		//Z
		if (mInd >= b_width * 2 && mInd <= b_width * 3 - 1) {
			int mI = mInd - b_width * 2;
			for (int y = 0; y < b_width; y++) {
				for (int x = 0; x < b_width; x++) {
					if (TZ)
						B_array[mI * zp + x * yp + (b_width - 1 - y)].Block_No = B_arrayTemp[mI * zp + y * yp + x];
					else
						B_array[mI * zp + (b_width - 1 - x) * yp + y].Block_No = B_arrayTemp[mI * zp + y * yp + x];
				}
			}
		}
	}

	public void shuffleOn() {
		shuffling = true;
		shuffleCnt = 0;
	}

	private void shuffle() {
		if (!shuffling)
			return;
		Random rand = Global.rand;
		if (shuffleCnt == 0) {
			for (int i = 0; i < b_sur_pcs; i++)
				B_obj[i].shuffle(true);
		}

		int mi = rand.nextInt(3 * b_width);
		boolean ch = false;
		for (int i1 = 0; i1 < b_sur_pcs; i1++) {
			if (B_obj[i1].thetamov_f_check()) {
				ch = true;
				break;
			}
		}
		if (!ch) {
			RotateInit(mi, true, true, true);
			shuffleCnt++;
		}

		if (shuffleCnt >= 100) {
			for (int i = 0; i < b_sur_pcs; i++)
				B_obj[i].shuffle(false);
			shuffling = false;
		}
	}

	public void Rotate() {
		BlockAllX.MatrixRotationY(BlockAllThetaX);
		BlockAllY.MatrixRotationX(BlockAllThetaY);
		BlockAll.MatrixMultiply(BlockAll, BlockAllX);
		BlockAll.MatrixMultiply(BlockAll, BlockAllY);
		BlockAll.MatrixNormalize();
	}

	public void draw(GL10 gl, int texture) {
		if (creating)
			return;
		drawing = true;
		shuffle();
		gl.glPushMatrix();
		{
			Rotate();
			for (int i = 0; i < b_sur_pcs; i++) {
				B_obj[i].Rotate();
				thetaCube.MatrixMultiply(B_obj[i].ThetaMat(), BlockAll);
				gl.glLoadMatrixf(thetaCube.m, 0);
				B_obj[i].draw(gl, texture);
			}
		}
		gl.glPopMatrix();
		drawing = false;
	}
}
