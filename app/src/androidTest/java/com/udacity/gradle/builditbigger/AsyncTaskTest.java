package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.example.com.tellingjoke.TellJoke;
import android.os.AsyncTask;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Pair;

import com.udacity.gradle.builditbigger.EndpointsAsyncTask;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AsyncTaskTest {
    @Test
    public void loadJokeTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Context of the app under test.
        //http://tutorials.jenkov.com/java-reflection/private-fields-and-methods.html
        Context appContext = InstrumentationRegistry.getTargetContext();
        AsyncTask<Pair<Context, String>, Void, String> endpoint = new EndpointsAsyncTask();
        Class<? extends AsyncTask> endclass = endpoint.getClass();
        Method endmethod = EndpointsAsyncTask.class.getDeclaredMethod("doInBackground", Pair[].class);
        endmethod.setAccessible(true);
        Pair[][] param = new Pair[1][1];
        param[0]= new Pair[1];
        param[0][0]=new Pair<Context, String>(appContext, "Manfred");
        String jokeOutput;
        Runnable r=new Runnable() {
            @Override
            public void run() {
                jokeOutput = (String)endmethod.invoke(endpoint,param );
            }
        }
        Thread t=new Thread(r);
        t.start();
        t.join();
        TellJoke tj=new TellJoke();
        assertEquals(tj.tellJoke(),jokeOutput);

    }
}
