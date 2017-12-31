package com.developer.fabian.feedrss.generics;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class XmlRequest<T> extends Request<T> {

    private static final String TAG = XmlRequest.class.getSimpleName();

    private final Class<T> genericClass;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;
    private final Serializer serializer = new Persister();

    public XmlRequest(String url, Class<T> clazz, Map<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);

        this.genericClass = clazz;
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String xml = new String(response.data, "UTF-8");
            Log.d(TAG, xml);

            return Response.success(serializer.read(genericClass, xml), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}
