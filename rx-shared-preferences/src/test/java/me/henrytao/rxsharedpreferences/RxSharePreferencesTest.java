/*
 * Copyright 2015 "Henry Tao <hi@henrytao.me>"
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.henrytao.rxsharedpreferences;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;

import rx.Subscription;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = RobolectricGradleTestRunner.SDK)
public class RxSharePreferencesTest {

  private static final String PREFERENCES_KEY = RxSharePreferencesTest.class.getName();

  private static final String TEST_KEY = "key";

  private static final String TEST_KEY_2 = "key_2";

  private RxSharedPreferences mRxSharedPreferences;

  private SharedPreferences mSharedPreferences;

  @After
  public void after() {
    mRxSharedPreferences.reset();
  }

  @Before
  public void before() {
    Context context = RuntimeEnvironment.application;
    mSharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    mRxSharedPreferences = new RxSharedPreferences(mSharedPreferences);
  }

  @Test
  public void getBooleanTest() {
    assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, true), equalTo(true));
    assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, false), equalTo(false));
  }

  @Test
  public void getFloatTest() {
    assertThat(mRxSharedPreferences.getFloat(TEST_KEY, 10f), equalTo(10f));
    assertThat(mRxSharedPreferences.getFloat(TEST_KEY, 20f), equalTo(20f));
  }

  @Test
  public void getIntTest() {
    assertThat(mRxSharedPreferences.getInt(TEST_KEY, 10), equalTo(10));
    assertThat(mRxSharedPreferences.getInt(TEST_KEY, 20), equalTo(20));
  }

  @Test
  public void observeBooleanTest() {
    TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
    Subscription subscription = mRxSharedPreferences.observeBoolean(TEST_KEY, false).subscribe(testSubscriber);
    mRxSharedPreferences.putBoolean(TEST_KEY, true);
    mRxSharedPreferences.putBoolean(TEST_KEY, false);
    mRxSharedPreferences.putBoolean(TEST_KEY_2, true);
    subscription.unsubscribe();
    mRxSharedPreferences.putBoolean(TEST_KEY, false);
    testSubscriber.assertReceivedOnNext(Arrays.asList(false, true, false));
  }

  @Test
  public void observeFloatTest() {
    TestSubscriber<Float> testSubscriber = new TestSubscriber<>();
    Subscription subscription = mRxSharedPreferences.observeFloat(TEST_KEY, 10f).subscribe(testSubscriber);
    mRxSharedPreferences.putFloat(TEST_KEY, 20f);
    mRxSharedPreferences.putFloat(TEST_KEY, 30f);
    mRxSharedPreferences.putFloat(TEST_KEY_2, 40f);
    subscription.unsubscribe();
    mRxSharedPreferences.putFloat(TEST_KEY, 50f);
    testSubscriber.assertReceivedOnNext(Arrays.asList(10f, 20f, 30f));
  }

  @Test
  public void observeIntTest() {
    TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
    Subscription subscription = mRxSharedPreferences.observeInt(TEST_KEY, 10).subscribe(testSubscriber);
    mRxSharedPreferences.putInt(TEST_KEY, 20);
    mRxSharedPreferences.putInt(TEST_KEY, 30);
    mRxSharedPreferences.putInt(TEST_KEY_2, 40);
    subscription.unsubscribe();
    mRxSharedPreferences.putInt(TEST_KEY, 50);
    testSubscriber.assertReceivedOnNext(Arrays.asList(10, 20, 30));
  }

  @Test
  public void putBooleanTest() {
    assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, false), equalTo(false));
    mRxSharedPreferences.putBoolean(TEST_KEY, true);
    assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, false), equalTo(true));
    mRxSharedPreferences.putBoolean(TEST_KEY, false);
    assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, true), equalTo(false));
  }

  @Test
  public void putFloatTest() {
    assertThat(mRxSharedPreferences.getFloat(TEST_KEY, 10f), equalTo(10f));
    mRxSharedPreferences.putFloat(TEST_KEY, 20f);
    assertThat(mRxSharedPreferences.getFloat(TEST_KEY, 30f), equalTo(20f));
  }

  @Test
  public void putIntTest() {
    assertThat(mRxSharedPreferences.getInt(TEST_KEY, 10), equalTo(10));
    mRxSharedPreferences.putInt(TEST_KEY, 20);
    assertThat(mRxSharedPreferences.getInt(TEST_KEY, 30), equalTo(20));
  }

  @Test
  public void resetTest() {
    mRxSharedPreferences.putBoolean(TEST_KEY, true);
    assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, false), equalTo(true));
    mRxSharedPreferences.reset();
    assertThat(mRxSharedPreferences.getBoolean(TEST_KEY, false), equalTo(false));
  }
}