
package com.Puzzle_GL;

import javax.microedition.khronos.opengles.GL10;
import java.util.Random;

public class Global {
	//GLコンテキスト保持
	public static GL10 gl;
	public static float width;
	public static float height;
	public static float fovy;
	public static float zNear;
	public static float zFar;
	public static float eyeX, eyeY, eyeZ;
	public static float centerX, centerY, centerZ;
	public static float upX, upY, upZ;
	public static long time;
	//ランダム値生成
	public static Random rand = new Random(System.currentTimeMillis());
}
