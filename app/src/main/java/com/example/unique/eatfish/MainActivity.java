package com.example.unique.eatfish;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.example.unique.eatfish.constant.ConstantUtil;
import com.example.unique.eatfish.sounds.GameSoundPool;
import com.example.unique.eatfish.view.OverView;
import com.example.unique.eatfish.view.ReadyView;
import com.example.unique.eatfish.view.RunView;

public class MainActivity extends Activity {
	private ReadyView	readyView;
	private RunView		runView;
	private OverView	overView;
	private GameSoundPool sounds;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what == ConstantUtil.GAME_READY){
				toReadyView();
			}
			else
			if(msg.what == ConstantUtil.GAME_RUNING){
				toRunView();
			}
			else  if(msg.what == ConstantUtil.GAME_PAUSE){
				toRunView();
			}

			else  if(msg.what == ConstantUtil.GAME_OVER){
				toOverView(msg.arg1);
			}
			else  if(msg.what == ConstantUtil.GAME_END){
				endGame();
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		sounds = new GameSoundPool(this);
		sounds.initGameSound();
		readyView = new ReadyView(this,sounds);
		setContentView(readyView);

	}

	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	//显示游戏的准备界面
	public void toReadyView(){
		if(readyView == null){
			readyView = new ReadyView(this,sounds);
		}
		setContentView(readyView);
		readyView = null;
		overView = null;
	}
	//显示游戏的主界面
	public void toRunView(){
		if(runView == null){
			runView = new RunView(this,sounds);
		}
		setContentView(runView);
		readyView = null;
		overView = null;
	}

	//显示游戏结束的界面
	public void toOverView(int score){
		if(overView == null){
			overView = new OverView(this,sounds);
			overView.setScore(score);
		}
		setContentView(overView);
		runView = null;
	}
	//结束游戏
	public void endGame(){
		if(readyView != null){
			readyView.setThreadFlag(false);
		}
		else if(runView != null){
			runView.setThreadFlag(false);
		}

		else if(overView != null){
			overView.setThreadFlag(false);
		}
		this.finish();
	}
	//getter和setter方法
	public Handler getHandler() {
		return handler;
	}
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
}
