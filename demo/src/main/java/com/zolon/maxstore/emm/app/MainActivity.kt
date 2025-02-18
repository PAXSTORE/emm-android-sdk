package com.zolon.maxstore.emm.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.zolon.maxstore.emm.sdk.EMMSDK
import com.zolon.maxstore.emm.sdk.java.base.util.JsonUtils

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn1).setOnClickListener {
            val paramVariables = EMMSDK.getInstance().paramVariableApi.paramVariables
            Toast.makeText(this, JsonUtils.toJson(paramVariables), Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btn2).setOnClickListener {
            val identifier = EMMSDK.getInstance().paramVariableApi.identifier
            Toast.makeText(this, JsonUtils.toJson(identifier), Toast.LENGTH_SHORT).show()
        }
    }
}