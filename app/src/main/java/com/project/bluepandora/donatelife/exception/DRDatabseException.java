package com.project.bluepandora.donatelife.exception;

/*
 * Copyright (C) 2014 The Blue Pandora Project Group
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
public class DRDatabseException extends SQliteDataBaseException {
    public static final String DRDATABASEEXCEPTION_TAG = "DonationRecordDatabaseException";
    private String message = "Problem occurred During Donation Record Table";

    public DRDatabseException() {

    }

    public DRDatabseException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + message;
    }
}
