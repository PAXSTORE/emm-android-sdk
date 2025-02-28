package com.zolon.maxstore.emm.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.zolon.maxstore.emm.sdk.EMMSDK
import com.zolon.maxstore.emm.sdk.java.base.util.JsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txv = findViewById<TextView>(R.id.txv)

        findViewById<Button>(R.id.btn1).setOnClickListener {
            lifecycleScope.launch {
                val paramVariables = try {
                    withContext(Dispatchers.IO) {
                        EMMSDK.getInstance().paramVariableApi.paramVariables
                    }
                } catch (t: Throwable) {
                    Toast.makeText(this@MainActivity, "初始化异常，请重启应用", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                val paramVariableStr = JsonUtils.toJson(paramVariables)
                Log.d(TAG, "paramVariableStr: $paramVariableStr")
                txv.text = paramVariableStr
            }
        }

        findViewById<Button>(R.id.btn2).setOnClickListener {
            lifecycleScope.launch {
                val identifier = try {
                    withContext(Dispatchers.IO) {
                        EMMSDK.getInstance().paramVariableApi.identifier
                    }
                } catch (t: Throwable) {
                    Toast.makeText(this@MainActivity, "初始化异常，请重启应用", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                val identifierStr = JsonUtils.toJson(identifier)
                Log.d(TAG, "identifierStr: $identifierStr")
                txv.text = identifierStr
            }
        }
    }
}