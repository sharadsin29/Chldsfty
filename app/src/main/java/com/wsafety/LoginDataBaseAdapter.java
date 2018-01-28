package com.wsafety;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LoginDataBaseAdapter 
{
		static final String DATABASE_NAME = "cacs.db";
		static final int DATABASE_VERSION = 1;
		public static final int NAME_COLUMN = 1;
		// TODO: Create public field for each column in your table.
		// SQL Statement to create a new database.
		static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS user(name VARCHAR,age VARCHAR,sex VARCHAR,phone1 VARCHAR,phone2 VARCHAR,height VARCHAR,weight VARCHAR, agetype VARCHAR); ";
		
		// Variable to hold the database instance
		public  SQLiteDatabase db;
		// Context of the application using the database.
		private final Context context;
		// Database open/upgrade helper
		private DataBaseHelper dbHelper;
		public  LoginDataBaseAdapter(Context _context) 
		{
			context = _context;
			dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		public  LoginDataBaseAdapter open() throws SQLException 
		{
			db = dbHelper.getWritableDatabase();
			
			return this;
		}
		public void close() 
		{
			db.close();
		}

		public  SQLiteDatabase getDatabaseInstance()
		{
			return db;
		}

		public void insertEntry(String name, String age, String sex, String s1, String s2, String height, String weight, String agetype)
		{
	       ContentValues newValues = new ContentValues();
			System.out.println("======================nansdfsdfs=========="+name+"=========="+agetype);
			// Assign values for each row.
			newValues.put("name", name);
			newValues.put("age", age);
			newValues.put("sex", sex);
			newValues.put("phone1", s1);
			newValues.put("phone2", s2);
			newValues.put("height", height);
			newValues.put("weight",weight);
			newValues.put("agetype", agetype);
			
			// Insert the row into your table
			db.insert("user", null, newValues);
			///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
		}
		public int deleteEntry(String UserName)
		{
			//String id=String.valueOf(ID);
		    String where="PACKAGE=?";
		    int numberOFEntriesDeleted= db.delete("PRIVACY", where, new String[]{UserName}) ;
	       // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
	        return numberOFEntriesDeleted;
		}	
		public String getSinlgeEntry(String userName)
		{
			Cursor cursor=db.query("PRIVACY", null, " PACKAGE=?", new String[]{userName}, null, null, null);
	        if(cursor.getCount()<1) // UserName Not Exist
	        {
	        	cursor.close();
	        	return "NOT EXIST";
	        }
		    cursor.moveToFirst();
			String password= cursor.getString(cursor.getColumnIndex("LOCATION"));
			cursor.close();
			return password;				
		}
		public int selectAll()
		{
			int num=0;
			Cursor cursor=db.rawQuery("SELECT * FROM user", null);
			if(cursor.moveToFirst()){
				do{
					num=cursor.getCount();
				}
				while(cursor.moveToNext());
			}
			//System.out.println("List of data's installed"+data);
			cursor.close();
			return num;
		}
		public ArrayList getselctAll()
		{
			ArrayList data=new ArrayList();
			Cursor cursor=db.rawQuery("SELECT * FROM user", null);
			if(cursor.moveToFirst()){
				do{
					data.add(cursor.getString(cursor.getColumnIndex("name")));
					data.add(cursor.getString(cursor.getColumnIndex("age")));
					data.add(cursor.getString(cursor.getColumnIndex("phone1")));
					data.add(cursor.getString(cursor.getColumnIndex("phone2")));
					data.add(cursor.getString(cursor.getColumnIndex("agetype")));
				}
				while(cursor.moveToNext());
			}
			System.out.println("List of data's installed"+data);
			cursor.close();
			return data;
		}
		public void  updateEntry(String userName,String password)
		{
			// Define the updated row content.
			ContentValues updatedValues = new ContentValues();
			// Assign values for each row.
			updatedValues.put("USERNAME", userName);
			updatedValues.put("PASSWORD",password);
			
	        String where="USERNAME = ?";
		    db.update("LOGIN",updatedValues, where, new String[]{userName});			   
		}	
		
		
}

