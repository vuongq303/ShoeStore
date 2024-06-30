package com.example.appbangiayonline.convert;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ConvertImage {
    public static byte[] ImageToByte(Context context, int drawableResource) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableResource);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    public static Bitmap ByteToBitmap(byte[] o) {
        return BitmapFactory.decodeByteArray(o, 0, o.length);
    }

    public static byte[] FileImageToByte(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            return convertInputStreamToByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] convertInputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        while ((length = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }

        return byteArrayOutputStream.toByteArray();
    }
    public static byte[] ImageDrawableToByte(ImageView imageView) {
        if (imageView.getDrawable() != null) {
            try {
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] result = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.close();
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
