package com.rain.zhihu_example.util;

import android.graphics.Color;

import java.util.Random;

public class ColorUtil {
	/**
	 * 随机生成漂亮的颜色
	 * @return
	 */
	public static int randomColor(){
		Random random = new Random();
		//如果三原色的值过大会偏白色，过小会偏黑色，所以应该随机一个中间的颜色的值
		int red = random.nextInt(150)+50;//50-199
		int green = random.nextInt(150)+50;//50-199
		int blue = random.nextInt(150)+50;//50-199
		return Color.rgb(red, green, blue);//在rgb三原色的基础上混合出一种新的颜色
	}
}
