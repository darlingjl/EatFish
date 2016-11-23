package com.example.unique.eatfish.factory;

import com.example.unique.eatfish.baseobject.GameObject;
import com.example.unique.eatfish.object.EnemyFish;
import com.example.unique.eatfish.object.MyFish;

import android.content.res.Resources;

//游戏对象的工厂类
public class GameObjectFactory {

	public GameObject createObject(Resources resources){
		return new GameObject(resources);
	}
	//创建中型敌机的方法
	public GameObject createEnemyFish(Resources resources){
		return new EnemyFish(resources);
	}
	//创建大型敌机的方法
	public GameObject createMyFish(Resources resources){
		return new MyFish(resources);
	}

}
