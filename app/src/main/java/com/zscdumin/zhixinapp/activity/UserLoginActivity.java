package com.zscdumin.zhixinapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.isnc.facesdk.SuperID;
import com.isnc.facesdk.common.Cache;
import com.isnc.facesdk.common.SDKConfig;
import com.isnc.facesdk.common.SuperIDUtils;
import com.zscdumin.zhixinapp.R;
import com.zscdumin.zhixinapp.bean.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class UserLoginActivity extends AppCompatActivity {

    /*@BindView(R.id.face_login)
    TextView faceLogin;*/
    @BindView(R.id.user_account_login)
    EditText userAccount;
    @BindView(R.id.user_password_login)
    EditText userPassword;
    @BindView(R.id.user_login)
    Button userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bmob.initialize(this, "eee4dbbb9638202f9861efe29a7968d1");

        setContentView(R.layout.user_login);
        ButterKnife.bind(this);
        /*SuperID.initFaceSDK(this);*/

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager
                        .PERMISSION_GRANTED) {
            //申请照相机权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);//最后一个参数表示请求码
        }*/
    }

    /*@OnClick({R.id.user_login, R.id.add_new_user, R.id.face_login})*/
    //用户选择登录还是注册
    @OnClick({R.id.user_login, R.id.add_new_user})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_login:
                isLoginSucess();
                break;
            case R.id.add_new_user:
                Intent intent = new Intent(this, UserRegisterActivity.class);
                startActivity(intent);
                break;
            /*case R.id.face_login:
                SuperID.faceLogin(this);
                break;*/
        }
    }

    //账号登陆模块主要代码
    public void isLoginSucess() {
        if (userAccount.length() > 0 && userPassword.length() > 0) {
            BmobQuery<Person> query = new BmobQuery<Person>();
            query.findObjects(this, new FindListener<Person>() {

                String user_account = userAccount.getText().toString().trim();
                String user_password = userPassword.getText().toString().trim();
                int flag = 0;

                @Override
                public void onSuccess(List<Person> list) {
                    for (int i = 0; i < list.size(); i++) {
                        String account = list.get(i).getUserAccount();
                        String password = list.get(i).getUserPassword();
                        if (account.equals(user_account) && password.equals(user_password)) {
                            flag = 1;
                            Toast.makeText(UserLoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("login_type", "with_account");
                            intent.putExtras(bundle);
                            startActivity(intent);
                            break;
                        }
                    }
                    if (flag == 0) {
                        userAccount.setText("");
                        userPassword.setText("");
                        Toast.makeText(UserLoginActivity.this, "登录失败,账号或密码错误！", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        } else {
            Toast.makeText(UserLoginActivity.this, "账号或密码不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    /*刷脸登录模块主要代码
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case SDKConfig.LOGINSUCCESS:
                // 使用人脸登录后获取的一登用户信息
                String user_info = Cache.getCached(UserLoginActivity.this, SDKConfig.KEY_APPINFO);
                String phone_num = "";
                String user_icon = "";
                String user_name = "";
                try {
                    JSONObject obj = new JSONObject(user_info);
                    phone_num = obj.getString(SDKConfig.KEY_PHONENUM);
                    user_icon = obj.getString(SDKConfig.KEY_AVATAR);
                    user_name = SuperIDUtils.judgeChina(obj.getString(SDKConfig.KEY_NAME), 10);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("login_type", "with_face");
                bundle.putString("user_name", user_name);
                bundle.putString("user_icon", user_icon);
                bundle.putString("phone_num", phone_num);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            default:
                Toast.makeText(UserLoginActivity.this, "刷脸登录失败,请尝试用账号密码登陆!", Toast.LENGTH_SHORT).show();
                break;
        }
    }*/
}