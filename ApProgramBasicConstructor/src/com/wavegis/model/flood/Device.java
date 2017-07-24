/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wavegis.model.flood;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * POJO representing a User stored in the Firebase Database.
 */
// [START user_class]
@IgnoreExtraProperties
public class Device {

	private String fcm_token;
	private String device_OS;

    public Device() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    
    public Device(String token, String device_os) {
		super();
		this.setFcm_token(token);
		this.device_OS = device_os;
	}
    
	public String getDevice_os() {
		return device_OS;
	}

	public void setDevice_os(String device_os) {
		this.device_OS = device_os;
	}

	public String getFcm_token() {
		return fcm_token;
	}

	public void setFcm_token(String fcm_token) {
		this.fcm_token = fcm_token;
	}
}
// [END user_class]