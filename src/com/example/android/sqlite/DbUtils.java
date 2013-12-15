package com.example.android.sqlite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import android.os.Environment;
import android.util.Log;

public class DbUtils
{

	public static void backupDb()
	{
		try
		{
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite())
			{
				String currentDBPath = "/data/com.example.android.wardrobe/databases/wardrobe.db";
				String backupDBPath = Environment.getExternalStorageDirectory().toString() + "/Wordrobe/Backup/wordrobeDB.bak";
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				if (currentDB.exists())
				{
					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
					Log.d("BackUpTask", "Completed taking whole backup");
				}
				else
				{
					Log.d("BackUpTask2", "Backup Path wrong");
				}
			}
		}
		catch (Exception e)
		{
		}
	}

	public static void restoreDB()
	{
		try
		{
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite())
			{
				String currentDBPath = "/data/com.example.android.wardrobe/databases/wardrobe.db";
				String backupDBPath = "/Wordrobe/Backup/wordrobeDB.bak";
				File currentDB = new File(data, currentDBPath);
				File backupedDB = new File(sd, backupDBPath);
				if (currentDB.exists())
				{
					if (backupedDB.exists())
					{
						FileChannel src = new FileInputStream(backupedDB).getChannel();
						FileChannel dst = new FileOutputStream(currentDB).getChannel();
						dst.transferFrom(src, 0, src.size());
						src.close();
						dst.close();

						Log.d("BackUpTask2", "Database Restored successfully");
					}
					else
					{
						Log.d("BackUpTask", "currentDB does not exists : " + currentDB.getAbsolutePath());
					}
				}
				else
				{
					Log.d("BackUpTask", "currentDB does not exists : " + currentDB.getAbsolutePath());
				}
			}
		}
		catch (Exception e)
		{
			Log.e("BackUpTask2", "Backup Exception : ", e);
		}
	}
}
