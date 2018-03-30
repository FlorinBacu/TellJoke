package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.example.com.tellingjoke.TellJoke;
import android.os.AsyncTask;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.TextUtils;
import android.util.Pair;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AsyncTaskTest {
    
    public static class EndpointsAsyncTaskTestVersion extends EndpointsAsyncTask
    {
        TaskListener mListener;
        private InterruptedException mError;

        public EndpointsAsyncTaskTestVersion setListener(TaskListener listener) {
            this.mListener = listener;
            return this;
        }
        @Override
        protected void onPostExecute(String s) {
            if (this.mListener != null)
                this.mListener.onComplete(s, mError);
        }

        @Override
        protected void onCancelled() {
            if (this.mListener != null) {
                mError = new InterruptedException("AsyncTask cancelled");
                this.mListener.onComplete(null, mError);
            }
        }
        public static interface TaskListener {
            public void onComplete(String jsonString, Exception e);
        }
    };
    CountDownLatch signal = null;

    public Exception mError;
    public String jokeString;
    @Test
    public void loadJokeTest() throws InterruptedException {
        signal = new CountDownLatch(1);
        Context appContext = InstrumentationRegistry.getTargetContext();
        Pair<Context, String> pairTest = new Pair<Context, String>(appContext, "Manfred");
        EndpointsAsyncTaskTestVersion testTask=new EndpointsAsyncTaskTestVersion();
        testTask.setListener(new EndpointsAsyncTaskTestVersion.TaskListener() {
            @Override
            public void onComplete(String textString, Exception e) {
                jokeString = textString;
                mError = e;
                signal.countDown();
            }
        }).execute(pairTest);
        signal.await();
       assertNull(mError);
        TellJoke tj=new TellJoke();
        assertEquals(tj.tellJoke(),jokeString);
       

    }
}
