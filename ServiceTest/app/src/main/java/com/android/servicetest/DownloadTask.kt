package com.android.servicetest

import android.os.AsyncTask
import android.widget.Toast

class DownloadTask : AsyncTask<Unit, Int, Boolean>() {
    override fun doInBackground(vararg params: Unit?) = try {
        while (true){
            val downloadPercent = 20
            publishProgress(downloadPercent)
            if(downloadPercent >= 100){
                break
            }
        }
        true
    }catch (e : Exception){
        false
    }

    override fun onPreExecute() {
        //显示进度对话框
        //progressDialog.show()
    }

    override fun onProgressUpdate(vararg values: Int?) {
        //更新下载进度
        //progressDialog.setMessage("Downloaded ${values[0]}%")
    }

    override fun onPostExecute(result: Boolean) {
        //progressDialog.dismiss()
        //提示下载结果
        if (result){
            //Toast.makeText(context, "Download succeeded", Toast.LENGTH_SHORT).show()
        }else{
            //Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()

        }
    }
}