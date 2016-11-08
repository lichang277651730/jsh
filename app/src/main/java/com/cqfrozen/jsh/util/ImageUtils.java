package com.cqfrozen.jsh.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageUtils {

	public static final int GET_IMAGE_BY_CAMERA = 5001;
	public static final int GET_IMAGE_FROM_PHONE = 5002;
	public static final int CROP_IMAGE = 5003;
	public static Uri imageUriFromCamera;
	public static Uri cropImageUri;
	/**通过拍照获取图片
	 * @param activity
	 */
	public static void openCameraImage(Activity activity) {
		//创建相机拍照后存放的数据库Uri地址
		ImageUtils.imageUriFromCamera = createImagePathUri(activity);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.imageUriFromCamera);
		activity.startActivityForResult(intent, ImageUtils.GET_IMAGE_BY_CAMERA);
	}

	/**创建一条图片地址Uri，用于保存拍照后的照片
	 */
	private static Uri createImagePathUri(Context context) {
		Uri imageFilePath = null;
		String state = Environment.getExternalStorageState();
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
		long currentTime = System.currentTimeMillis();
		String imageName = timeFormat.format(new Date(currentTime));
		ContentValues values = new ContentValues();
		values.put(Media.DISPLAY_NAME, imageName);
		values.put(Media.DATE_TAKEN, currentTime);
		values.put(Media.MIME_TYPE, "image/jpeg");
		//判断是否有SD卡 有就用SD卡  没就用手机存储
		if(state.equals(Environment.MEDIA_MOUNTED)){//有SD卡
			imageFilePath = context.getContentResolver()
					.insert(Media.EXTERNAL_CONTENT_URI, values);
		}else{//没有SD卡
			imageFilePath = context.getContentResolver()
					.insert(Media.INTERNAL_CONTENT_URI, values);
		}
		Log.d("imagecreatePath", imageFilePath.toString());
		return imageFilePath;
	}

	/**从相册中选择图片
	 * @param activity
	 */
	public static void openLocalImage(Activity activity) { 
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		activity.startActivityForResult(intent, ImageUtils.GET_IMAGE_FROM_PHONE);
	}

	//删除指定uri的图片
	public static void deleteImageUri(Context context, Uri uri) {
		context.getContentResolver().delete(uri, null, null);
	}

	
	/**裁剪拍照后的图片
	 * @param activity
	 * @param srcUri
	 */
	public static void cropImage(Activity activity, Uri srcUri) {
		ImageUtils.cropImageUri = ImageUtils.createImagePathUri(activity);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(srcUri, "image/*");
		intent.putExtra("crop", "true");
		//aspectX aspectY 是裁剪框宽高的比例
		//都不设置时，裁剪框可以自行调整
		//只设置宽高比 aspectX Y  裁剪框比例固定不变，只能调整大小
		//outputX Y 裁剪后生成图片宽高，和裁剪框无关，只决定最终生成图片大小
		//aspect可以和 output比例不同，此时以裁剪框的宽为准，按照裁剪宽高比例生成图片，
		//该图片和框选部分可能不同，不同的情况可能是截取框选的一部分，也可能超出框选部分，向下延伸补足
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
//		intent.putExtra("outputX", 300);
//		intent.putExtra("outputY", 100);
		//return-data 为true 会直接返回bitmap数据，但是大图裁剪会出问题，推荐false
		//return-data 未false 不会返回bitmap，但需要制定一个MediaStore.EXTRA_OUTPUT保存图片uri
		intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.cropImageUri);
		intent.putExtra("return-data", false);
		activity.startActivityForResult(intent, ImageUtils.CROP_IMAGE);
	}

	public static String getImageAbsolutePath(Context context, Uri uri) {
		Cursor cursor = Media.query(context.getContentResolver(), uri,
				new String[]{Media.DATA});
		if(cursor.moveToFirst()){
			return cursor.getString(0);
		}
		return null;
	}

}
