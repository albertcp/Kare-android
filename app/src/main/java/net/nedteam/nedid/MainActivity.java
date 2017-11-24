package net.nedteam.nedid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private TextView mTextMessage;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    File fileImage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Button tarea1Button = (Button) findViewById(R.id.buttonTarea1);
        Button tarea2Button = (Button) findViewById(R.id.buttonTarea2);
        Button tareaAtrasButton = (Button) findViewById(R.id.buttonAtras);
        tarea1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAudioURIToWS("https://github.com/albertcp/Kare/blob/master/tarea1.flac?raw=true");
            }
        });
        tarea2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAudioURIToWS("https://github.com/albertcp/Kare/blob/master/tarea2.flac?raw=true");
            }
        });
        tareaAtrasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAudioURIToWS("https://github.com/albertcp/Kare/blob/master/atras.flac?raw=true");
            }
        });

        Button elviraButton = (Button) findViewById(R.id.buttonElvira);
        if (elviraButton != null) {
            elviraButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File file = new File(Environment.getExternalStorageDirectory().getPath(), "/NED/pablo.jpeg");
                    Uri uri = Uri.fromFile(file);
                    Bitmap bitmap;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        Log.d("Bitmap", "imagen:"+bitmap.getHeight());
                        bitmap = crupAndScale(bitmap, 1500); // if you mindx scaling
                        Log.d("Bitmap", "imagen:"+bitmap.getHeight());
                        Log.d("Bitmap", "imagen:"+bitmap);
                        sendImageToWS(bitmap);
//                    pofileImageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        Log.d("Error", e.getMessage());
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.d("Error", e.getMessage());
                    }
                }
            });
        }
        Button pabloButton = (Button) findViewById(R.id.buttonPablo);
        if (pabloButton != null) {
            pabloButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    File file = new File(Environment.getExternalStorageDirectory().getPath(), "/NED/pablo.jpeg");
                    Uri uri = Uri.fromFile(file);
                    Bitmap bitmap;
                    try {

                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        Log.d("Bitmap", "imagen:"+bitmap.getHeight());
                        bitmap = crupAndScale(bitmap, 1500); // if you mindx scaling
                        Log.d("Bitmap", "imagen:"+bitmap.getHeight());
                        Log.d("Bitmap", "imagen:"+bitmap);
                        sendImageToWS(bitmap);
//                    pofileImageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        Log.d("Error", e.getMessage());
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.d("Error", e.getMessage());
                    }
                }
            });
        }
        Button nedIDButton = (Button) findViewById(R.id.buttonNEDID);
        if (nedIDButton != null) {
            nedIDButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("NEDID", "NED ID Button");
//                    Bitmap a = null;
//                    sendImageToWS(a);
//                    dispatchTakePictureIntent();
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    //Uri uri  = Uri.parse("content:///sdcard/photo.jpg");
//                    fileImage = new File(Environment.getExternalStorageDirectory().getPath(), "/NED/pablo.jpeg");
                    fileImage = createImageFile();
                    Log.d("el log", "file: "+fileImage);
                    Log.d("el log", "file2: "+createImageFile());
                    Uri uri = FileProvider.getUriForFile(getApplicationContext(),
                            getApplicationContext().getApplicationContext().getPackageName() + ".my.package.name.provider",
                            fileImage
//                            createImageFile()
                    );
                    cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);

                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            });
        }
    }
//    private void dispatchTakePictureIntent() {
//        Log.d("NEDID", "NED ID Button");
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        Log.d("NEDID", "NED ID Button");
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            Log.d("NEDID", "NED ID Button");
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            Log.d("NEDID", "NED ID Button");
//        }
//    }

    String mCurrentPhotoPath;

    private File createImageFile() {
        // Create an image file name
        String timeStamp = null;

        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            Log.e("createerror", e.getMessage());
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception ex) {
                // Error occurred while creating the File
                Log.d("DispatchIntent", "Error en crear imagen");
            }
            // Continue only if the File was successfully created
            Log.d("DispatchIntent", "photofile"+photoFile);
            if (photoFile != null) {
                Log.d("DispatchIntent", "dentro if");
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Onactivityresult", "resultado");
        Log.d("Codigos", "rq: "+requestCode+". rsc:"+resultCode);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
//            File file = new File(Environment.getExternalStorageDirectory().getPath(), "/NED/pablo.jpeg");
            File file = fileImage;
            Uri uri = Uri.fromFile(file);
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                bitmap = crupAndScale(bitmap, 1500); // if you mindx scaling
                Log.d("Bitmap", "imagen:"+bitmap);
                sendImageToWS(bitmap);
//                    pofileImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                Log.d("Error", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("Error", e.getMessage());
            }
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            Log.d("La foto es", "foto:"+photo);
        } else {
            Log.d("NO", "PUes va a ser que no");
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//
//            Log.d("NEDID", "Imagen aquí");
//            sendImageToWS(imageBitmap);
//        }
//    }

    public void sendImageToWS(final Bitmap imageBitmap) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Bitmap, Object, Object> sendPhoto = new AsyncTask<Bitmap, Object, Object>() {
            @Override
            protected Object doInBackground(Bitmap... objects) {
                Log.d("Async", "Convert image to Base64");
                String bitmapIn64 = getBase64(imageBitmap);
                try {

                    Log.d("Async", "Connect to WS");
                    WebSocket ws = new WebSocketFactory().createSocket("ws://k-are.eu-gb.mybluemix.net/ws/data");
                    ws.connect();
                    Log.d("Async", "WebSocket is: "+ws.isOpen());
    //                WebSocketFrame firstFrame = WebSocketFrame
    //                        .createTextFrame()
    //                        .setFin(true);
                    Log.d("Async", "foto:"+bitmapIn64);
                    if(ws.isOpen()) {
                        ws.sendText("{\"type\":\"IMAGE_CLASSIFY\", \"payload\":\"data:image/jpeg;base64,"+bitmapIn64+"\"}");
                    }
                    else {
                        Log.wtf("Async", "The server is not open");
                    }
    //                ws.addListener(new WebSocketAdapter() {
    //                    @Override
    //                    public void onTextMessage(WebSocket websocket, String message) throws Exception {
    //                        // Received a text message.
    //                        Log.d("WebSocketMessage", "message:"+message);
    //                    }
    //                });

                } catch (Exception e) {

                    System.out.println(e.getMessage());
                    Log.e("Error:", "err: "+e.getMessage());


                }
                return null;
            }
        };
        sendPhoto.execute(imageBitmap);
    }


    public void sendAudioURIToWS(final String audioURI) {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<String, Object, Object> sendPhoto = new AsyncTask<String, Object, Object>() {
            @Override
            protected Object doInBackground(String... objects) {
                String audiofile = objects[0];
                Log.d("AsyncAudio", "audio file:"+audiofile);
                try {

                    Log.d("Async", "Connect to WS");
                    WebSocket ws = new WebSocketFactory().createSocket("ws://k-are.eu-gb.mybluemix.net/ws/data");
                    ws.connect();
                    Log.d("Async", "WebSocket is: "+ws.isOpen());
                    //                WebSocketFrame firstFrame = WebSocketFrame
                    //                        .createTextFrame()
                    //                        .setFin(true);

                    if(ws.isOpen()) {
                        ws.sendText("{\"type\":\"VOICE_RECOGNITION\", \"payload\":\""+audiofile+"\"}");
                    }
                    else {
                        Log.wtf("Async", "The server is not open");
                    }
                    //                ws.addListener(new WebSocketAdapter() {
                    //                    @Override
                    //                    public void onTextMessage(WebSocket websocket, String message) throws Exception {
                    //                        // Received a text message.
                    //                        Log.d("WebSocketMessage", "message:"+message);
                    //                    }
                    //                });

                } catch (Exception e) {

                    System.out.println(e.getMessage());
                    Log.e("Error:", "err: "+e.getMessage());


                }
                return null;
            }
        };
        sendPhoto.execute(audioURI);
    }

    public static String getBase64(Bitmap bitmap)
    {
        try{
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(byteArray, Base64.NO_WRAP);
        }
        catch(Exception e)
        {
            Log.e("GetBase64", e.getMessage());
            return null;
        }
    }

    public static  Bitmap crupAndScale (Bitmap source,int scale){
        int factor = source.getHeight() <= source.getWidth() ? source.getHeight(): source.getWidth();
        int longer = source.getHeight() >= source.getWidth() ? source.getHeight(): source.getWidth();
        int x = source.getHeight() >= source.getWidth() ?0:(longer-factor)/2;
        int y = source.getHeight() <= source.getWidth() ?0:(longer-factor)/2;
        source = Bitmap.createBitmap(source, x, y, factor, factor);
        source = Bitmap.createScaledBitmap(source, scale, scale, false);
        return source;
    }
//    public static Bitmap getBitmap(String base64){
//        byte[] decodedString = Base64.decode(base64, Base64.NO_WRAP);
//        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//    }
}
