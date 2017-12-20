package fr.bs_tech.vps;

/**
 * Created by cpellerin on 28/11/2017.
 */

import android.Manifest;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.bs_tech.vps.bindings.CurrentMission;
import fr.bs_tech.vps.utils.SendHTTPPost;

public class Login extends BaseActivity
{
    @BindView(R.id.input_username)
    EditText _usernameText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_transport);
        ButterKnife.bind(this);



        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        _loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                login();
            }
        });
    }

    public void login()
    {
        String username;
        String password;
        SendHTTPPost post;
        String json = "";
        String result;

        if (!validate())
        {
            loginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        username = _usernameText.getText().toString();
        password = _passwordText.getText().toString();
        json = "{\"login\":\"" + username +"\", ";
        json += "\"password\":\"" + MD5(password) +"\"}\n";

        curMiss = new CurrentMission();

        post = new SendHTTPPost();
        post.execute("http://vpsmobile.pierrel.social/json.php", json);
        result = null;
        while(result == null)
        {
            result = post.getResult();
        }

        Log.v("HTTP", result);
        if (result.equals("LoginOk"))
        {
            // TODO: Implement your own authentication logic here.
            _loginButton.setEnabled(true);
            curMiss.setLogin(username);
            curMiss.setPwdHash(md5(password));
            Intent intent = new Intent(Login.this, Transport.class);
//            intent.putExtra("currentmission", curMiss);
            startActivity(intent);
        } else
        {
            loginFailed();
            return;
        }
    }

    @Override
    public void onBackPressed()
    {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess()
    {
        _loginButton.setEnabled(true);
        finish();
    }

    public void loginFailed()
    {
        Toast.makeText(getBaseContext(), R.string.login_failed, Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate()
    {
        boolean valid = true;

        String login = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (login.isEmpty())
        {
            _usernameText.setError(getString(R.string.invalid_user));
            valid = false;
        } else
        {
            _usernameText.setError(null);
        }

        int max = 10;
        int min = 4;
        if (password.isEmpty() || password.length() < min || password.length() > max)
        {
            _passwordText.setError(getString(R.string.invalid_password));
            valid = false;
        } else
        {
            _passwordText.setError(null);
        }

        return valid;
    }

    public static String md5(String s)
    {
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(Charset.forName("US-ASCII")),0,s.length());
            byte[] magnitude = digest.digest();
            BigInteger bi = new BigInteger(1, magnitude);
            String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
            return hash;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }
}