package com.common.http;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.common.base.BaseValue;
import com.cqfrozen.jsh.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpForVolley {

	private StringRequest request;
	private Activity activity;
	private Fragment fragment;
	private Context context;

	public HttpForVolley(Activity activity) {
		this.activity = activity;
		this.context = activity;
	}
	public HttpForVolley(Context context) {
		this.context = context;
	}

	public HttpForVolley(Fragment fragment) {
		this.fragment = fragment;
		this.context = fragment.getActivity();
	}

	public Context getContext() {
		return context;
	}

	public void goTo(int method, Integer which,
			HashMap<String, String> httpMap, String url, HttpTodo todo) {
		if (request != null && url.equals(request.getUrl())) {
			request.cancel();
		}
		toHttp(method, which, httpMap, url, todo);
	}

	private void toHttp(int method, final Integer which,
			final HashMap<String, String> httpMap, String url,
			final HttpTodo todo) {
		HashMap<String, String> map = new HashMap<String, String>();
		if (httpMap != null) {
			map = httpMap;
		}

		if (BaseValue.isDebug) {
			try {
				String httpMethod = "";
				if (method == Request.Method.GET)
					httpMethod = "GET";
				if (method == Request.Method.POST)
					httpMethod = "POST";
				if (activity != null) {
					Logger.json(new JSONObject(map).put("url", url)
							.put("activity", activity.getClass().getName())
							.put("Method", httpMethod).toString());
				} else {
					Logger.json(new JSONObject(map)
							.put("url", url)
							.put("activity",
									fragment.getActivity().getClass().getName())
							.put("Method", httpMethod).toString());
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		// 对所有参数进行url编码，防止中文服务器无法解析
		if (httpMap != null && method == Request.Method.GET) {
			for (String key : httpMap.keySet()) {
				try {
					if (!key.equals("token")) {
						httpMap.put(key,
								URLEncoder.encode(httpMap.get(key), "utf-8"));
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}

		// 拼接url的get请求参数
		if (method == Request.Method.GET) {
			if (httpMap != null && httpMap.size() > 0) {
				url = url + "?";
				for (String key : httpMap.keySet()) {
					url = url + key + "=" + httpMap.get(key) + "&";
				}
			}

			if (url.endsWith("&")) {
				url = url.substring(0, url.length() - 1);
			}
		}

		// 正式请求网络
		request = new StringRequest(method, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						try {
							if (BaseValue.isDebug) {
								Logger.json(response);
							}
							JSONObject result = new JSONObject(response);
							todo.httpTodo(which, result);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						try {
							JSONObject object = new JSONObject();
							object.put("msg", "网络错误");
							object.put("code", "404");
							todo.httpTodo(which, object);
							if (BaseValue.isDebug) {
								Logger.json(object.toString());
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				if (httpMap == null) {
					return new HashMap<String, String>();
				} else {
					return httpMap;
				}
			}
		};

		if (activity != null) {
			request.setTag(activity);
		} else {
			request.setTag(fragment);
		}
		request.setRetryPolicy(new DefaultRetryPolicy(
				DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//		request.setRetryPolicy(new DefaultRetryPolicy(0,
//				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		BaseValue.mQueue.add(request);
	}

	public void postBase64(int method, Integer which,
			HashMap<String, String> httpMap, String imgPath, String url,
			HttpTodo todo) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(imgPath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			String encodeToString = Base64.encodeToString(buffer,
					Base64.DEFAULT);
			httpMap.put("head_data", encodeToString);
			if (request == null) {
				toHttp(method, which, httpMap, url, todo);
			}else{
				if(url.equals(request.getUrl())){
					request.cancel();
				}
				toHttp(method, which, httpMap, url, todo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject object = new JSONObject();
			try {
				object.put("msg", "发生错误");
				object.put("code", "404");
				todo.httpTodo(which, object);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return;
		}finally{
			try {
				if(fis != null){
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public interface HttpTodo {
		void httpTodo(Integer which, JSONObject response);
	}

}
