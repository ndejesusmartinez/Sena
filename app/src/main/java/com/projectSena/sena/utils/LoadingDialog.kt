package com.projectSena.sena.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.projectSena.sena.R

class LoadingDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.loading_dialog)
    }
}