package com.bnm.timesheet.export

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ajts.androidmads.library.SQLiteToExcel
import com.ajts.androidmads.library.SQLiteToExcel.ExportListener
import com.bnm.timesheet.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class SQLite2ExcelActivity : AppCompatActivity() {
    var edtUser: EditText? = null
    var edtContactNo: EditText? = null
    var btnSaveUser: Button? = null
    var btnExport: Button? = null
    var btnExportExclude: Button? = null
    var lvUsers: ListView? = null
    var lvUserAdapter: CustomAdapter? = null
    var usersList: List<Users> = ArrayList()
    private var dbHelper: DBHelper? = null
    private var dbQueries: DBQueries? = null
    var directory_path = Environment.getExternalStorageDirectory().path + "/KBBackup/"
    var sqliteToExcel: SQLiteToExcel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sqlite_2_xl)
        initViews()
        requestPermission(this)
        btnSaveUser!!.setOnClickListener { view ->
            if (validate(edtUser) && validate(edtContactNo)) {
                dbQueries!!.open()
                val d = resources.getDrawable(R.drawable.clockout)
                val bitmap = (d as BitmapDrawable).bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                val bitmapdata = stream.toByteArray()
                val users = Users(edtUser!!.text.toString(), edtContactNo!!.text.toString(), bitmapdata)
                dbQueries!!.insertUser(users)
                usersList = dbQueries!!.readUsers()
                lvUserAdapter = CustomAdapter(applicationContext, usersList)
                lvUsers!!.adapter = lvUserAdapter
                dbQueries!!.close()
                Utils.showSnackBar(view, "Successfully Inserted")
            }
        }
        btnExport!!.setOnClickListener { view -> // Export SQLite DB as EXCEL FILE
            sqliteToExcel = SQLiteToExcel(applicationContext, DBHelper.DB_NAME, directory_path)
            sqliteToExcel!!.exportAllTables("users.xls", object : ExportListener {
                override fun onStart() {}
                override fun onCompleted(filePath: String) {
                    Utils.showSnackBar(view, filePath)
                }

                override fun onError(e: Exception) {
                    Utils.showSnackBar(view, e.message)
                }
            })
        }
        btnExportExclude!!.setOnClickListener { view -> // Export SQLite DB as EXCEL FILE
            sqliteToExcel = SQLiteToExcel(applicationContext, DBHelper.DB_NAME, directory_path)
            // Exclude Columns
            val excludeColumns: MutableList<String> = ArrayList()
            excludeColumns.add("contact_id")

            // Prettify or Naming Columns
            val prettyNames = HashMap<String, String>()
            prettyNames["contact_person_name"] = "Person Name"
            prettyNames["contact_no"] = "Mobile Number"
            sqliteToExcel!!.setExcludeColumns(excludeColumns)
            sqliteToExcel!!.setPrettyNameMapping(prettyNames)
            sqliteToExcel!!.setCustomFormatter { columnName, value ->
                var value = value
                when (columnName) {
                    "contact_person_name" -> value = "Sale"
                }
                value
            }
            sqliteToExcel!!.exportAllTables("users.xls", object : ExportListener {
                override fun onStart() {}
                override fun onCompleted(filePath: String) {
                    Utils.showSnackBar(view, filePath)
                }

                override fun onError(e: Exception) {
                    Utils.showSnackBar(view, e.message)
                }
            })
        }
    }

    private fun requestPermission(context: Activity) {
        val hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        if (!hasPermission) {
            ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_WRITE_STORAGE)
        } else {
            // You are allowed to write external storage:
            val path = Environment.getExternalStorageDirectory().absolutePath + "/new_folder"
            val storageDir = File(path)
            if (!storageDir.exists() && !storageDir.mkdirs()) {
                // This should never happen - log handled exception!
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WRITE_STORAGE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "The app was allowed to write to your storage!", Toast.LENGTH_LONG).show()
                    // Reload the activity with permission granted or use the features what required the permission
                    val file = File(directory_path)
                    if (!file.exists()) {
                        Log.v("File Created", file.mkdirs().toString())
                    }
                } else {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun validate(editText: EditText?): Boolean {
        if (editText!!.text.toString().length == 0) {
            editText.error = "Field Required"
            editText.requestFocus()
        }
        return editText.text.toString().length > 0
    }

    fun initViews() {
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        dbHelper = DBHelper(applicationContext)
        dbQueries = DBQueries(applicationContext)
        edtUser = findViewById<View>(R.id.edt_user) as EditText
        edtContactNo = findViewById<View>(R.id.edt_c_no) as EditText
        btnSaveUser = findViewById<View>(R.id.btn_save_user) as Button
        btnExport = findViewById<View>(R.id.btn_export) as Button
        btnExportExclude = findViewById<View>(R.id.btn_export_with_exclude) as Button
        lvUsers = findViewById<View>(R.id.lv_users) as ListView
        dbQueries!!.open()
        usersList = dbQueries!!.readUsers()
        lvUserAdapter = CustomAdapter(applicationContext, usersList)
        lvUsers!!.adapter = lvUserAdapter
        dbQueries!!.close()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return true
    }

    companion object {
        const val REQUEST_WRITE_STORAGE = 112
    }
}