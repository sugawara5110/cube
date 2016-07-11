
package com.Puzzle_GL;

import java.util.Map;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

public class TextureManager{

	//�e�N�X�`���ێ�
	private static Map<Integer, Integer>mTextures = new Hashtable<Integer, Integer>();

	//���[�h�����e�N�X�`���ǉ�
	public static final void addTexture(int resId, int texId){
	      mTextures.put(resId, texId);
	}

	//�e�N�X�`���폜
	public static final void deleteTexture(GL10 gl, int resId){
		if(mTextures.containsKey(resId)){
		   int[] texId = new int[1];
		   texId[0] = mTextures.get(resId);
		   gl.glDeleteTextures(1, texId, 0);
		   mTextures.remove(resId);
		}
	}

	//�S�e�N�X�`���폜
	public static final void deleteAll(GL10 gl){
	   List<Integer>keys = new ArrayList<Integer>(mTextures.keySet());
	   for(Integer key : keys){
	      deleteTexture(gl, key);
	   }
	}
}
