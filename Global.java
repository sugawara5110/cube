
package com.Puzzle_GL;

import javax.microedition.khronos.opengles.GL10;
import java.util.Random;

public class Global {
	//GL�R���e�L�X�g�ێ�
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
	//�����_���l����
	public static Random rand = new Random(System.currentTimeMillis());
}
