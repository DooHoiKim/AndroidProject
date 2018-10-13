package com.dh.pro1822.pwassistance;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class DataBaseFileManager {

    public String exportDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                File BackupDir = new File(sd, ContractDB.DB_EXPORT_DEST);
                BackupDir.mkdir();

                String currentDBPath = "//data//" + TestApplication.PACKAGE_NAME
                        + "//databases//" + ContractDB.DB_NAME;
                String backupDBPath = ContractDB.DB_EXPORT_DEST + "/" + ContractDB.DB_NAME;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
//                Toast.makeText(context, "Backup Successful!", Toast.LENGTH_SHORT).show();
                return "Backup Successful!";
            } else {
//                Toast.makeText(context, "SD can not write!", Toast.LENGTH_SHORT).show();
                return "External Storage can not write!";
            }

        } catch (Exception e) {
//            Toast.makeText(context, "Backup Failed!", Toast.LENGTH_SHORT).show();
            return "Backup Failed!";
        }
    }

    public String importDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {

                String currentDBPath = "//data//" + TestApplication.PACKAGE_NAME
                        + "//databases//" + ContractDB.DB_NAME;
                String backupDBPath = ContractDB.DB_EXPORT_DEST + "/" + ContractDB.DB_NAME;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB).getChannel();

                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();

//                Toast.makeText(context, "Restore OK", Toast.LENGTH_SHORT).show();
                return "Restore OK!";
            } else {
//                Toast.makeText(context, "SD can not read!", Toast.LENGTH_SHORT).show();
                return "External Storage can not read!";
            }
        } catch (Exception e) {
//            Toast.makeText(context, "Restore Fail", Toast.LENGTH_SHORT).show();
            return "Restore Fail!";
        }
    }
}
