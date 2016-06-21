/*
 * Copyright (C) 2013 yixia.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zshgif.laugh.acticty;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.a.a.V;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zshgif.laugh.R;
import com.zshgif.laugh.cache.DiskLruCacheUtil;
import com.zshgif.laugh.http.HttpPictureUtils;
import com.zshgif.laugh.listener.NetworkBitmapCallbackListener;
import com.zshgif.laugh.utils.Constant;
import com.zshgif.laugh.utils.LogUtils;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotManager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import io.vov.vitamio.MediaListener;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoViewActivty extends BaseActivity {

	/**
	 * TODO: Set the path variable to a streaming video URL or a local media file
	 * path.
	 */

	io.vov.vitamio.widget.VideoView mVideoView;

	FrameLayout startPage;

	ImageView picture;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Vitamio.isInitialized(getApplicationContext());

		
		setContentView(R.layout.videoview);

		startPage = (FrameLayout) findViewById(R.id.startPage);
		picture = (ImageView) findViewById(R.id.picture);

		geiBitmap(getIntent().getStringExtra("pictureurl"),picture);
		//插屏广告动画
		SpotManager.getInstance(this).setAnimationType(SpotManager.ANIM_ADVANCE);
		playfunction();	

	}

	
	void playfunction(){

//		 String path = "http://i.snssdk.com/neihan/video/playback/?video_id=42b6499198f84c0bbf2a72d1b87a9ea9&quality=720p&line=0&is_gif=0";

		String path =getIntent().getStringExtra("videourl");


		mVideoView = (io.vov.vitamio.widget.VideoView) findViewById(R.id.surface_view);

			/*
			 * Alternatively,for streaming media you can use
			 * mVideoView.setVideoURI(Uri.parse(URLstring));
			 */

			mVideoView.setVideoURI(Uri.parse(path));
		MediaController mediaController =new MediaController(this, new MediaListener() {
			@Override
			public void pause() {
				//插入广告
				SpotManager.getInstance(VideoViewActivty.this).showSpotAds(VideoViewActivty.this);
			}

			@Override
			public void start() {
				//关闭广告
				SpotManager.getInstance(VideoViewActivty.this).disMiss();
			}
		});

		mediaController.setAnchorView(mVideoView);
			mVideoView.setMediaController(mediaController);
			mVideoView.requestFocus();

			mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mediaPlayer) {
					// optional need Vitamio 4.0
					mediaPlayer.setPlaybackSpeed(1.0f);
					LogUtils.e("ddd","setOnPreparedListener");


					//进度
//					mVideoView.getDuration();
//					mVideoView.seekTo(42508);
				}
			});



//

	}

	public void closeVideo(View view){
			finish();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		/**
		 * 停止视频播放，并释放资源。
		 */
		mVideoView.stopPlayback();
		/**
		 * 关闭广告
		 */
		SpotManager.getInstance(VideoViewActivty.this).onDestroy();
	}

	/**
	 * 图片
	 * @param url
	 * @param imageView

	 */
	void geiBitmap(String url, final ImageView imageView) {
		byte[]  bytes = null;

		/**
		 * 先冲缓存中查找
		 */
		InputStream inputStream =  DiskLruCacheUtil.readFromDiskCache(url, ContextUtil.getInstance());
		if(inputStream!=null){
			try {
				bytes = HttpPictureUtils.toByteArray(inputStream);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		if(bytes!=null){
			WeakReference weakReference = new WeakReference(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));//弱引用
			imageView.setImageBitmap((Bitmap) weakReference.get());
		}

	}
}
