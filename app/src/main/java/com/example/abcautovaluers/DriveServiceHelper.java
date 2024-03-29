package com.example.abcautovaluers;
/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.example.abcautovaluers.ValuationInstance.*;

/**
 * A utility for performing read/write operations on Drive files via the REST API and opening a
 * file picker UI via Storage Access Framework.
 */
public class DriveServiceHelper {
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Drive mDriveService;
    private Context mContext;

    private String tag = "DriveServiceHelper";


    public static String TYPE_AUDIO = "application/vnd.google-apps.audio";
    public static String TYPE_GOOGLE_DOCS = "application/vnd.google-apps.document";
    public static String TYPE_GOOGLE_DRAWING = "application/vnd.google-apps.drawing";
    public static String TYPE_GOOGLE_DRIVE_FILE = "application/vnd.google-apps.file";
    public static String TYPE_GOOGLE_DRIVE_FOLDER = "application/vnd.google-apps.folder";
    public static String TYPE_GOOGLE_FORMS = "application/vnd.google-apps.form";
    public static String TYPE_GOOGLE_FUSION_TABLES = "application/vnd.google-apps.fusiontable";
    public static String TYPE_GOOGLE_MY_MAPS = "application/vnd.google-apps.map";
    public static String TYPE_PHOTO = "application/vnd.google-apps.photo";
    public static String TYPE_GOOGLE_SLIDES = "application/vnd.google-apps.presentation";
    public static String TYPE_GOOGLE_APPS_SCRIPTS = "application/vnd.google-apps.script";
    public static String TYPE_GOOGLE_SITES = "application/vnd.google-apps.site";
    public static String TYPE_GOOGLE_SHEETS = "application/vnd.google-apps.spreadsheet";
    public static String TYPE_UNKNOWN = "application/vnd.google-apps.unknown";
    public static String TYPE_VIDEO = "application/vnd.google-apps.video";
    public static String TYPE_3_RD_PARTY_SHORTCUT = "application/vnd.google-apps.drive-sdk";


    public DriveServiceHelper(Drive driveService) {

        mDriveService = driveService;
//        mContext = context;
        Log.d(tag, "DriveServiceHelper in");

    }
    /**
     * Creates a text file in the user's My Drive folder and returns its file ID.
     */
    public Task<String> createFile(final String folderId, final java.io.File file) {
        return Tasks.call(mExecutor, new Callable<String>() {
            @Override
            public String call()  {

                try {

                    List<String> root;

                    if (folderId == null) {
                        root = Collections.singletonList("root");
                    } else {

                        root = Collections.singletonList(folderId);

                    }

                    File metadata = new File()
                            .setParents(root)
                            .setMimeType("text/plain")
                            .setName("Valuation Details");

                    FileContent fileContent = new FileContent("text/plain", file);
                    File fileMeta = mDriveService.files().create(metadata, fileContent).setFields("id").execute();

                    if (fileMeta == null) {
                        throw new IOException("Null result when requesting file creation.");
                    }

                    return fileMeta.getId();
                } catch (Exception e){

                    Log.d(tag,"Exception thrown: "+e.toString());
                    return null;

                }
            }
        });
    }

    public Task<GoogleDriveFileHolder> searchFolder(final String folderName) {
        return Tasks.call(mExecutor, new Callable<GoogleDriveFileHolder>() {
            @Override
            public GoogleDriveFileHolder call() throws Exception {

                Log.d(tag, "We starting the search");
                FileList result = mDriveService.files().list()
                        .setQ("mimeType = '" + TYPE_GOOGLE_DRIVE_FOLDER + "' and name = '" + folderName + "' ")
                        .setSpaces("drive")
                        .execute();
                GoogleDriveFileHolder googleDriveFileHolder = new GoogleDriveFileHolder();
                if (result.getFiles().size() > 0) {
                    googleDriveFileHolder.setId(result.getFiles().get(0).getId());
                    googleDriveFileHolder.setName(result.getFiles().get(0).getName());

                    Log.d(tag, "ID: "+googleDriveFileHolder.getId());
                    Log.d(tag, "Name: "+googleDriveFileHolder.getName());

                }
                return googleDriveFileHolder;
            }
        });
    }
    /**
     * Opens the file identified by {@code fileId} and returns a {@link Pair} of its name and
     * contents.
     */
    public Task<Pair<String, String>> readFile(final String fileId) {
        return Tasks.call(mExecutor, new Callable<Pair<String, String>>() {
            @Override
            public Pair<String, String> call() throws Exception {
                // Retrieve the metadata as a File object.
                File metadata = mDriveService.files().get(fileId).execute();
                String name = metadata.getName();

                // Stream the file contents to a String.
                try (InputStream is = mDriveService.files().get(fileId).executeMediaAsInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    String contents = stringBuilder.toString();

                    return Pair.create(name, contents);
                }
            }
        });
    }
    /**
     * Updates the file identified by {@code fileId} with the given {@code name} and {@code
     * content}.
     */
    public Task<Void> saveFile(final String fileId, final String folderId, final String name, final String content) {
        return Tasks.call(mExecutor, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                // Create a File containing any metadata changes.
                File metadata = new File()
                        .setName(name)
                        .setParents(Collections.singletonList(folderId))
                        .setMimeType(TYPE_PHOTO);

                // Convert content to an AbstractInputStreamContent instance.
                ByteArrayContent contentStream = ByteArrayContent.fromString("text/plain", content);

                // Update the metadata and contents.
                mDriveService.files().update(fileId, metadata, contentStream).execute();
                return null;
            }
        });
    }

    public Task<List<GoogleDriveFileHolder>> uploadImages(final HashMap<String, java.io.File> valuationData, final String folderId) {
        return Tasks.call(mExecutor, new Callable<List<GoogleDriveFileHolder>>() {
            @Override
            public List<GoogleDriveFileHolder> call() throws Exception {
                // Retrieve the metadata as a File object.

                boolean state = true;
                List<String> root = Collections.singletonList(folderId);
                List<GoogleDriveFileHolder> googleDriveFileHolders = new ArrayList<>();

                for (java.util.Map.Entry<String, java.io.File> stringFileEntry : valuationData.entrySet()) {

                    if (stringFileEntry.getValue() != null){

                        String name = stringFileEntry.getKey();
                        java.io.File file = stringFileEntry.getValue();
                        File metadata = new File()
                                .setParents(root)
                                .setName(name);

                        FileContent fileContent = new FileContent("image/jpeg", file);

                        File fileMeta = mDriveService.files().create(metadata, fileContent).setFields("id").execute();
                        GoogleDriveFileHolder googleDriveFileHolder = new GoogleDriveFileHolder();
                        googleDriveFileHolder.setId(fileMeta.getId());
                        googleDriveFileHolder.setName(fileMeta.getName());


                        googleDriveFileHolders.add(googleDriveFileHolder);

                    }


                }

                return googleDriveFileHolders;
            }
        });
    }

    /**
     * Returns a {@link FileList} containing all the visible files in the user's My Drive.
     *
     * <p>The returned list will only contain files visible to this app, i.e. those which were
     * created by this app. To perform operations on files not created by the app, the project must
     * request Drive Full Scope in the <a href="https://play.google.com/apps/publish">Google
     * Developer's Console</a> and be submitted to Google for verification.</p>
     */
    public Task<FileList> queryFiles() {
        return Tasks.call(mExecutor, new Callable<FileList>() {
            @Override
            public FileList call() throws Exception {
                return mDriveService.files().list().setSpaces("drive").execute();
            }
        });
    }

    /**
     * Returns an {@link Intent} for opening the Storage Access Framework file picker.
     */
    public Intent createFilePickerIntent() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");

        return intent;
    }

    /**
     * Opens the file at the {@code uri} returned by a Storage Access Framework {@link Intent}
     * created by {@link #createFilePickerIntent()} using the given {@code contentResolver}.
     */
    public Task<Pair<String, String>> openFileUsingStorageAccessFramework(
            final ContentResolver contentResolver, final Uri uri) {
        return Tasks.call(mExecutor, new Callable<Pair<String, String>>() {
            @Override
            public Pair<String, String> call() throws Exception {
                // Retrieve the document's display name from its metadata.
                String name;
                try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        name = cursor.getString(nameIndex);
                    } else {
                        throw new IOException("Empty cursor returned for file.");
                    }
                }

                // Read the document's contents as a String.
                String content;
                try (InputStream is = contentResolver.openInputStream(uri);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    content = stringBuilder.toString();
                }

                return Pair.create(name, content);
            }
        });
    }


    public Task<GoogleDriveFileHolder> createFolder(final String folderName, @Nullable final String folderId) {
        return Tasks.call(mExecutor, new Callable<GoogleDriveFileHolder>() {
            @Override
            public GoogleDriveFileHolder call() throws Exception {


                Log.d(tag, "we starting the upload code");
                try {
                    GoogleDriveFileHolder googleDriveFileHolder = new GoogleDriveFileHolder();

                    List<String> root;
                    if (folderId == null) {
                        root = Collections.singletonList("root");
                    } else {

                        root = Collections.singletonList(folderId);

                    }

                    File metadata = new File()
                            .setParents(root)
                            .setMimeType(TYPE_GOOGLE_DRIVE_FOLDER)
                            .setName(folderName);

                    File googleFile = mDriveService.files().create(metadata).execute();
                    if (googleFile == null) {

                        Log.d(tag, "is null");
                        throw new IOException("Null result when requesting file creation.");

                    }

                    String id = googleFile.getId();

                    Log.d(tag, "Id: "+id);
                    googleDriveFileHolder.setId(id);
                    googleDriveFileHolder.setName(folderName);
                    return googleDriveFileHolder;

                } catch (UserRecoverableAuthIOException e) {

                    Log.d(tag,"Exception thrown");
                    return null;

                } catch (Exception e) {

                    Log.d(tag, "Error " + e.toString());
                    throw new IOException(e);


                }

            }
        });
    }
}