/*
 * Copyright (c) 2018 Altimit Community Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or imp
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package systems.altimit.libgoogleplay.handlers;

import android.app.Activity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;

/**
 * Created by mgjus on 3/7/2018.
 */
abstract class AbstractHandler<T> {
    Activity mParentActivity;

    T mClient = null;

    AbstractHandler(Activity activity) {
        mParentActivity =  activity;
    }

    public void setClient(T achievementsClient) {
        mClient = achievementsClient;
    }

    boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(mParentActivity) != null;
    }
}
