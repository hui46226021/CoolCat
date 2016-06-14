/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zshgif.laugh.wechat.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.zshgif.laugh.R;
import com.zshgif.laugh.wechat.adapter.NewFriendsMsgAdapter;
import com.zshgif.laugh.wechat.db.InviteMessgeDao;
import com.zshgif.laugh.wechat.domain.InviteMessage;

import java.util.List;

/**
 * 申请与通知
 *
 */
public class NewFriendsMsgActivity extends EaseBaseActivity {
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_new_friends_msg);

		listView = (ListView) findViewById(R.id.list);
		InviteMessgeDao dao = new InviteMessgeDao(this);
		List<InviteMessage> msgs = dao.getMessagesList();
		//设置adapter
		NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
		listView.setAdapter(adapter);
		dao.saveUnreadMessageCount(0);
		
	}

	public void back(View view) {
		finish();
	}
	
	
}
