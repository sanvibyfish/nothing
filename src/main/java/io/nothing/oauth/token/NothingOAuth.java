package io.nothing.oauth.token;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import io.nothing.oauth.AccessTokenKeeper;
import io.nothing.oauth.OAuthDialog;
import io.nothing.oauth.OAuthListener;
import io.nothing.oauth.config.OAuthConfig;

public class NothingOAuth {

	private static final String TAG = NothingOAuth.class.getSimpleName();

	private Context mContext;
	private OAuthConfig mConfig;
	private AccessTokenKeeper mKeeper;

	public NothingOAuth(Context context, OAuthConfig config) {
		mContext = context;
		mConfig = config;
		mKeeper = new AccessTokenKeeper(config.getClass().getSimpleName());
	}

	public OAuthConfig getConfig() {
		return mConfig;
	}

	public Token getToken() {
		return mKeeper.readAccessToken(mContext);
	}

	public void clearToken() {
		mKeeper.clear(mContext);
	}

	public void refreshToken(String refreshToken, final OAuthListener l) {
		AsyncHttpClient client = new AsyncHttpClient();
		String url = mConfig.getRefreshTokenUrl();
		if (url == null) {
			throw new UnsupportedOperationException();
		}
		RequestParams params = mConfig.getRefreshTokenParams(refreshToken);
		client.post(url, params, new AsyncHttpResponseHandler() {

      @Override
      public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Token token = Token.make(new String(responseBody), mConfig);
        if (token.isSessionValid()) {
          mKeeper.keepAccessToken(mContext, token);
        }
        if (l != null) {
          l.onSuccess(token);
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        if (l != null) {
          l.onError(new String(responseBody));
        }
      }

		});
	}

	public void startOAuth(final OAuthListener l) {
		OAuthDialog dialog = new OAuthDialog(mContext, mConfig);
		dialog.setOAuthListener(new OAuthListener() {
			@Override
			public void onSuccess(Token token) {
				Log.d(TAG, "token: " + token.toString());
				if (token.isSessionValid()) {
					mKeeper.keepAccessToken(mContext, token);
				}
				if (l != null) {
					l.onSuccess(token);
				}
			}

			@Override
			public void onError(String error) {
				Log.d(TAG, "error: " + error);
				if (l != null) {
					l.onError(error);
				}
			}

			@Override
			public void onCancel() {
				Log.d(TAG, "cancel");
				if (l != null) {
					l.onCancel();
				}
			}
		});
		dialog.show();
	}
}
