package com.futang.livestreaming.app.module;

import android.app.Application;

import com.futang.livestreaming.BuildConfig;
import com.futang.livestreaming.R;
import com.futang.livestreaming.data.api.LiveApi;
import com.futang.livestreaming.data.LiveManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import dagger.producers.ProducerModule;
import dagger.producers.Produces;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/4/7.
 */
@ProducerModule
public class ApiProducerModule {

    @Produces
    public OkHttpClient produceOkHttpClient() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        builder.connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(60 * 1000, TimeUnit.MILLISECONDS);

        return builder.build();
    }

    @Produces
    public Retrofit produceRestAdapter(Application application, OkHttpClient okHttpClient) {
        final Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .serializeNulls()
                .create();

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(okHttpClient)
                .baseUrl(application.getString(R.string.host_url))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));
        return builder.build();
    }

    @Produces
    public LiveApi produceGithubApiService(Retrofit restAdapter) {
        return restAdapter.create(LiveApi.class);
    }

    @Produces
    public LiveManager produceLiveManager(LiveApi liveapi) {
        return new LiveManager(liveapi);
    }

    @Produces
    public UserModule.Factory produceUserModuleFactory(LiveApi liveapi) {
        return new UserModule.Factory(liveapi);
    }
}
