package com.zolon.maxstore.emm.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
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
                val paramVariables = withContext(Dispatchers.IO) {
                    EMMSDK.getInstance().paramVariableApi.paramVariables
                }
                val paramVariableStr = JsonUtils.toJson(paramVariables)
                Log.d(TAG, "paramVariableStr: $paramVariableStr")
                txv.text = paramVariableStr
            }
        }

        findViewById<Button>(R.id.btn2).setOnClickListener {
            lifecycleScope.launch {
                val identifier = withContext(Dispatchers.IO) {
                    EMMSDK.getInstance().paramVariableApi.identifier
                }
                val identifierStr = JsonUtils.toJson(identifier)
                Log.d(TAG, "identifierStr: $identifierStr")
                txv.text = identifierStr
            }
        }
    }
}