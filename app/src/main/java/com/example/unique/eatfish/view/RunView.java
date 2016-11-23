package com.example.unique.eatfish.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.example.unique.eatfish.R;
import com.example.unique.eatfish.baseobject.GameObject;
import com.example.unique.eatfish.constant.ConstantUtil;
import com.example.unique.eatfish.factory.GameObjectFactory;
import com.example.unique.eatfish.object.EnemyFish;
import com.example.unique.eatfish.object.MyFish;
import com.example.unique.eatfish.sounds.GameSoundPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/*游戏进行的主界面*/
public class RunView extends BaseView{

	private boolean isPlay;			// 标记游戏运行状态
	private boolean isTouchPlane;	// 判断玩家是否按下屏幕
	private Bitmap background; 		// 背景图片
	private GameObject lifeCount;		// 生命值
	private GameObject level;			// 等级
	private GameObject score;			//分数
	private GameObject pauseButton;		//暂停按钮
	private String pauseStr;			//
	private GameObjectFactory factory;
	/**********************************/
	private List<EnemyFish> enemyFishs;
	private MyFish			myFish=null;
	public RunView(Context context,GameSoundPool sounds) {
		super(context,sounds);

		isPlay = true;

		pauseStr = "暂停";
		factory = new GameObjectFactory();						  //工厂类
		enemyFishs = new ArrayList<EnemyFish>();


		thread = new Thread(this);
	}
	// 视图改变的方法
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		super.surfaceChanged(arg0, arg1, arg2, arg3);
	}
	// 视图创建的方法
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceCreated(arg0);
		initBitmap(); // 初始化图片资源

		if(thread.isAlive()){
			thread.start();
		}
		else{
			thread = new Thread(this);
			thread.start();
		}
	}
	// 视图销毁的方法
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		super.surfaceDestroyed(arg0);
		release();
	}
	// 响应触屏事件的方法
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP)
		{
			isTouchPlane = false;
			return true;
		}
		else if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			float x = event.getX();
			float y = event.getY();
			if(pauseButton.getRect().contains(x,y))
			{
				if(isPlay){
					isPlay = false;
					pauseStr = "继续";
				}
				else{
					isPlay = true;
					pauseStr = "暂停";
					synchronized(thread){
						thread.notify();
					}
				}
				return true;
			}
			//判断玩家飞机是否被按下
			else if(myFish.getRect().contains(x, y))
			{
				if(isPlay){
					isTouchPlane = true;
				}
				return true;
			}

		}
		//响应手指在屏幕移动的事件
		else if(event.getAction() == MotionEvent.ACTION_MOVE && event.getPointerCount() == 1){
			//判断触摸点是否为玩家的飞机
			if(isTouchPlane){
				float x = event.getX();
				float y = event.getY();

				if(x > myFish.getObject_x()+myFish.getObject_width()/2 + 20){
					myFish.setForward(1);
					myFish.setRotate(true);
					if(myFish.getObject_x()+myFish.getObject_width()/2 + myFish.getSpeed() <= screen_width){
						myFish.setObject_x(myFish.getObject_x() + myFish.getSpeed());
					}
				}
				else if(x < myFish.getObject_x()+myFish.getObject_width()/2 - 20){
					myFish.setForward(3);
					myFish.setRotate(false);
					if(myFish.getObject_x()+myFish.getObject_width()/2 - myFish.getSpeed() >= 0){
						myFish.setObject_x(myFish.getObject_x() - myFish.getSpeed());
					}
				}
				if(y > myFish.getObject_y()+myFish.getObject_height()/2 + 20){
					myFish.setForward(2);
					if(myFish.getObject_y()+myFish.getObject_height()/2 + myFish.getSpeed() <= screen_height){
						myFish.setObject_y(myFish.getObject_y() + myFish.getSpeed());
					}
				}
				else if(y < myFish.getObject_y()+myFish.getObject_height()/2 - 20){
					myFish.setForward(0);
					if(myFish.getObject_y()+myFish.getObject_height()/2 - myFish.getSpeed() >= 0){
						myFish.setObject_y(myFish.getObject_y() - myFish.getSpeed());
					}
				}
//				return true;
			}
		}
		return true;
	}
	//	// 初始化图片资源方法
//	@Override
	public void initBitmap() {
		// TODO Auto-generated method stub

		background = BitmapFactory.decodeResource(getResources(), R.drawable.back1);

		scalex = screen_width / background.getWidth();
		scaley = screen_height / background.getHeight();

		lifeCount = (GameObject) factory.createObject(getResources());
		lifeCount.setScreenWH(screen_width, screen_height);
		lifeCount.initBitmap(0.5f, 1, 1, R.drawable.info0);
		lifeCount.setObject_x(20);
		lifeCount.setObject_y(20);

		level = (GameObject) factory.createObject(getResources());
		level.setScreenWH(screen_width, screen_height);
		level.initBitmap(0.5f, 1, 1, R.drawable.info1);
		level.setObject_x((screen_width-level.getObject_width())/2-60);
		level.setObject_y(20);

		score = (GameObject) factory.createObject(getResources());
		score.setScreenWH(screen_width, screen_height);
		score.initBitmap(0.5f, 1, 1, R.drawable.info2);
		score.setObject_x(screen_width-80-score.getObject_width());
		score.setObject_y(20);
//		score.setText(score.toString());
//		score.getText();

		pauseButton = (GameObject) factory.createObject(getResources());
		pauseButton.setScreenWH(screen_width, screen_height);
		pauseButton.initBitmap(0.3f, 1, 2, R.drawable.buble2);
		pauseButton.setObject_x(screen_width-10-pauseButton.getObject_width());
		pauseButton.setObject_y(screen_height-10-pauseButton.getObject_height());

	}


	public void collide()
	{
		if(myFish.isAlive())
		{
			for(int i = enemyFishs.size()-1; i>0; i--)
			{
				EnemyFish ef = enemyFishs.get(i);
				if(ef.isBeEate(myFish))
				{
					sounds.playSound(2, 0);
					sounds.playSound(11, 0);
					myFish.setScore(myFish.getScore()+ef.getScore());


					if(myFish.getScore()>7000)
					{
						sounds.playSound(12, 0);
						mainActivity.getHandler().sendEmptyMessage(ConstantUtil.GAME_WIN);
					}
					if(myFish.getLevel()*1000 <= myFish.getScore())
					{
						myFish.setLevel(myFish.getLevel()+1);
						sounds.playSound(10, 0);
						myFish.setScale(myFish.getScale()+0.1f);
						myFish.initBitmap(myFish.getScale());
					}
					enemyFishs.remove(ef);
				}
				else if(myFish.isBeEate(ef))
				{

					sounds.playSound(8, 0);

					if(myFish.getLifeCount()==0)
					{
						myFish.setAlive(false);
						this.threadFlag = false;

					}

				}
			}
		}
	}

	public void makeFishes()
	{
		if(myFish == null)
		{
			myFish = (MyFish) factory.createMyFish(getResources());//生产玩家的飞机
			myFish.setScreenWH(screen_width, screen_height);
			myFish.initBitmap((float)0.3);
			myFish.initial();
		}
		for(int i = enemyFishs.size()-1; i>0; i--)
		{
			EnemyFish ef = enemyFishs.get(i);
			if(ef.isAlive()==false)
			{
				enemyFishs.remove(ef);
			}
		}
		if(enemyFishs.size()<10){

			Random ran = new Random();
			int s = ran.nextInt(5);
			EnemyFish ef;
			switch(s)
			{
				case 0:
					ef = (EnemyFish) factory.createEnemyFish(getResources());
					ef.setScreenWH(screen_width,screen_height);
					ef.initBitmap((float)0.5, 4, 2, R.drawable.fish0);
					ef.initial(100, 1, 10, 0);
					enemyFishs.add(ef);
					break;
				case 1:
					ef = (EnemyFish) factory.createEnemyFish(getResources());
					ef.setScreenWH(screen_width,screen_height);
					ef.initBitmap((float)0.5, 5, 3, R.drawable.fish1);
					ef.initial(50, 1, 10, 1);

					enemyFishs.add(ef);
					break;
				case 2:
					ef = (EnemyFish) factory.createEnemyFish(getResources());
					ef.setScreenWH(screen_width,screen_height);
					ef.initBitmap((float)0.5, 4, 2, R.drawable.fish2);
					ef.initial(200, 1, 10, 2);

					enemyFishs.add(ef);
					break;
				case 3:
					ef = (EnemyFish) factory.createEnemyFish(getResources());
					ef.setScreenWH(screen_width,screen_height);
					ef.initBitmap((float)0.5, 4, 2, R.drawable.fish3);
					ef.initial(70, 1, 10, 3);

					enemyFishs.add(ef);
					break;
				case 4:
					ef = (EnemyFish) factory.createEnemyFish(getResources());
					ef.setScreenWH(screen_width,screen_height);
					ef.initBitmap((float)0.6, 5, 2, R.drawable.fish4);
					ef.initial(250, 1, 10, 4);

					enemyFishs.add(ef);
					break;
				case 5:
					ef = (EnemyFish) factory.createEnemyFish(getResources());
					ef.setScreenWH(screen_width,screen_height);
					ef.initBitmap((float)0.6, 4, 2, R.drawable.fish5);
					ef.initial(300, 1, 10, 5);

					enemyFishs.add(ef);
					break;
				default:
					break;
			}

		}
	}

	// 绘图方法
	@Override
	public void drawSelf() {

		try {
			canvas = sfh.lockCanvas();
			canvas.drawColor(Color.BLACK); // 绘制背景色
			canvas.save();
			// 计算背景图片与屏幕的比例
			canvas.scale(scalex, scaley, 0, 0);
			canvas.drawBitmap(background, 0, 0/*bg_y*/, paint);   // 绘制背景图

			canvas.restore();


			for(EnemyFish obj:enemyFishs){
				if(obj.isAlive()){
					obj.drawSelf(canvas);
					obj.play();
					obj.move();
					//检测敌机是否与玩家的飞机碰撞

				}
			}

			myFish.drawSelf(canvas);	//绘制玩家的飞机
			myFish.play();

			lifeCount.drawSelf(canvas);
			level.drawSelf(canvas);
			score.drawSelf(canvas);
			pauseButton.drawSelf(canvas);
			//绘制积分文字
			paint.setTextSize(12);
			paint.setColor(Color.rgb(235, 161, 1));
			canvas.drawText(String.valueOf(myFish.getLifeCount()), lifeCount.getObject_x()+lifeCount.getObject_width(), 33, paint);
			canvas.drawText(String.valueOf(myFish.getLevel()), level.getObject_x()+level.getObject_width(), 33, paint);
			canvas.drawText(String.valueOf(myFish.getScore()), score.getObject_x()+score.getObject_width(), 33, paint);
			paint.setColor(Color.RED);
			canvas.drawText(pauseStr, pauseButton.getObject_x()+pauseButton.getObject_width()/2-12, pauseButton.getObject_y()+pauseButton.getObject_height()/2, paint);

		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}

	@Override
	public void run() {

		while (threadFlag) {
			long startTime = System.currentTimeMillis();
//			initObject();
			makeFishes();
			drawSelf();
			collide();
//			viewLogic();		//背景移动的逻辑

			long endTime = System.currentTimeMillis();
			if(!isPlay){
				synchronized (thread) {
					try {
						thread.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				if (endTime - startTime < 100)
					Thread.sleep(100 - (endTime - startTime));
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sounds.playSound(4, 0);
		Message message = new Message();
		message.what = 	ConstantUtil.GAME_OVER;
		message.arg1 = Integer.valueOf(myFish.getScore());
		mainActivity.getHandler().sendMessage(message);
	}
}
