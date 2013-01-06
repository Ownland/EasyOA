package cn.edu.nju.software.ui;

import java.util.List;

import cn.edu.nju.software.mgr.ContactManager;
import cn.edu.nju.software.model.Contact;
import cn.edu.nju.software.service.IContactService;
import cn.edu.nju.software.serviceConfig.ClientServiceHelper;
import cn.edu.nju.software.ui.R;
import edu.emory.mathcs.backport.java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContactActivity extends Activity {

	private Context context;
	private ImageButton imbPerson;
	private RelativeLayout rlProgress;;
	private ListView lstContact;
	private EditText edtSearchContact;
	private List<Contact> contacts;
	private ContactManager contactMgr;
	private Thread mThread;

	public static final int REQUEST_CONTACT_ITEM_CLICK = 1;
	public static final int REQUEST_PERSON_CLICK = 2;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {
				MyAdapter adapter = new MyAdapter(context);
				lstContact.setAdapter(adapter);
				rlProgress.setVisibility(View.GONE);
				lstContact.setVisibility(View.VISIBLE);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_activity);

		init();

		lstContact.setOnItemClickListener(new MyItemClickListener());

		imbPerson.setOnClickListener(new PersonClickListener());

		edtSearchContact.addTextChangedListener(new MyTextWatcher());

	}

	public void init() {
		context = this;
		contactMgr = new ContactManager(context, ((NowUser)getApplication()).getUser().getKey());
		rlProgress = (RelativeLayout) findViewById(R.id.rl_progress);
		lstContact = (ListView) findViewById(R.id.Lsv_contacts);
		imbPerson = (ImageButton) findViewById(R.id.imbPerson);
		edtSearchContact = (EditText) findViewById(R.id.edtFindContact);

		initList();
	}

	public void initList() {
		mThread = new Thread(runnable);
		mThread.start();
	}

	public List<Contact> getContactsByGroupPosition() {
		return contactMgr.getAllContacts();
	}

	public void refleshLsvContact() {
		contacts = this.getContactsByGroupPosition();
		Collections.sort(contacts);// 按姓名拼音进行排序
		if (contacts.size() == 0)
			Toast.makeText(context, "当前分组无联系人", Toast.LENGTH_SHORT).show();
		MyAdapter adapter = new MyAdapter(context);
		lstContact.setAdapter(adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == REQUEST_PERSON_CLICK
				&& resultCode == PersonalActivity.RESULT_BTN_BACK)
			refleshLsvContact();
	}

	Runnable runnable = new Runnable() {
		public void run() {
			IContactService cs = ClientServiceHelper.getContactService();
			contacts = cs.getContactList();
			for (int i = 0; i < contacts.size(); i++) {
				contactMgr.addContact(contacts.get(i));
			}
			contacts=contactMgr.getAllContacts();
			Collections.sort(contacts);

			Message msg = new Message();
			msg.arg1 = 1;
			ContactActivity.this.handler.sendMessage(msg);
		}
	};

	class MyItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			int contactId = (int) arg3;
			Intent intent = new Intent(context, ContactDetailActivity.class);
			intent.putExtra("id", contactId);
			startActivity(intent);
		}
	}

	class PersonClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(context, PersonalActivity.class);
			startActivityForResult(intent, REQUEST_PERSON_CLICK);
		}
	}

	class MyTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			String str = s.toString();
			if (str.equals(""))
				refleshLsvContact();
			else {
				contacts = contactMgr.getContactsByNamePinyin(str);
				Collections.sort(contacts);
				MyAdapter adapter = new MyAdapter(context);
				lstContact.setAdapter(adapter);
			}
		}

	}

	class MyAdapter extends BaseAdapter {

		private LayoutInflater inflater;

		public MyAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			edtSearchContact.setHint("联系人搜索 | 共" + contacts.size() + "人");
			return contacts.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return contacts.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return contacts.get(arg0).getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = inflater.inflate(R.layout.contact_list, null);
			TextView txtName = (TextView) convertView
					.findViewById(R.id.txtName);
			final TextView txtTel = (TextView) convertView
					.findViewById(R.id.txtTel);
			ImageButton imbCall = (ImageButton) convertView
					.findViewById(R.id.imb_call);
			final Contact contact = contacts.get(position);
			txtName.setText(contact.getName());
			txtTel.setText(contact.getPhone());
			imbCall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" + contact.getPhone()));
					startActivity(intent);
				}

			});
			return convertView;
		}

	}
}
