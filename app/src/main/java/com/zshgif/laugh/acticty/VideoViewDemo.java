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
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zshgif.laugh.R;
import com.zshgif.laugh.utils.LogUtils;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoViewDemo extends Activity {

	/**
	 * TODO: Set the path variable to a streaming video URL or a local media file
	 * path.
	 */

	VideoView mVideoView;
	/** 当前进度 */
	private long mProgress = 0;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Vitamio.isInitialized(getApplicationContext());
		
		setContentView(R.layout.videoview);

		playfunction();	

	}

	
	void playfunction(){
		 String path = "http://i.snssdk.com/neihan/video/playback/?video_id=fea5e92a71a048ea9719839b293ddab9&quality=480p&line=0&is_gif=0.mp4";




		mVideoView = (VideoView) findViewById(R.id.surface_view);

			/*
			 * Alternatively,for streaming media you can use
			 * mVideoView.setVideoURI(Uri.parse(URLstring));
			 */

			mVideoView.setVideoURI(Uri.parse(path));
		MediaController mediaController =new MediaController(this);

		mediaController.setAnchorView(mVideoView);
			mVideoView.setMediaController(mediaController);
			mVideoView.requestFocus();

			mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mediaPlayer) {
					// optional need Vitamio 4.0
					mediaPlayer.setPlaybackSpeed(1.0f);
					mVideoView.getDuration();
					mVideoView.seekTo(42508);
				}
			});
//

	}

	public void openVideo(View view){
		mVideoView.pause();
		mProgress = mVideoView.getCurrentPosition();

		LogUtils.e("进度",mProgress+"");




	}
	
}
