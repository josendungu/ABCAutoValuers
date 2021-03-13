package com.example.abcautovaluers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.abcautovaluers.*
import kotlinx.android.synthetic.main.valuation_item.view.*
import java.io.File

class ValuationRecyclerAdapter(
    private val context: Context,

) : RecyclerView.Adapter<ValuationRecyclerAdapter.ViewHolder>() {

    private val activity = context as Activity
    private lateinit var valuationData: HashMap<String, String>
    private lateinit var valuationList: ArrayList<String>
    private val list = ValuationInstance.getItemList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.valuation_item, parent, false))


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        valuationData = ValuationInstance(context).valuationPresentState

        val key = list[position]
        Log.d("Identifier: Name", key)
        Log.d("Identifier: Id", position.toString())
        val filePath = valuationData[key]

        if (filePath != null) {

            holder.imageView.setImageResource(R.drawable.ic_check_green)
            holder.outline.setBackgroundResource(R.drawable.pic_item_background_added)

        }
        holder.setIsRecyclable(false)

        holder.displayText.text = key

    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView: ImageView = itemView.findViewById<ImageView>(R.id.ic_image)
        val displayText: TextView = itemView.findViewById<TextView>(R.id.display_text)
        val outline: RelativeLayout = itemView.findViewById<RelativeLayout>(R.id.outline)

        private val takePicIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        private lateinit var photoFile: File

        init {

            imageView.setOnClickListener {

                val text = itemView.display_text.text
                val code = getCode(text.toString())
                val name = getName(text.toString())

                if (code != null && name != null) {

                    initializeCamIntent(code, name, text.toString())

                } else {

                    Toast.makeText(context, "There was an error", Toast.LENGTH_LONG).show()

                }

            }

        }

        private fun getName(key: String): String? {

            when (key) {
                ValuationInstance.KEY_CHASSIS -> {

                    return CHASSIS_NAME

                }
                ValuationInstance.KEY_DASHBOARD -> {

                    return DASHBOARD_NAME

                }
                ValuationInstance.KEY_FRONT -> {

                    return FRONT_NAME

                }
                ValuationInstance.KEY_ENGINE -> {

                    return ENGINE_NAME

                }
                ValuationInstance.KEY_FRONT_LEFT -> {

                    return FRONT_LEFT_NAME

                }
                ValuationInstance.KEY_FRONT_RIGHT -> {

                    return FRONT_RIGHT_NAME

                }
                ValuationInstance.KEY_HEAD_LIGHT -> {

                    return HEAD_LIGHT_NAME

                }
                ValuationInstance.KEY_ID -> {

                    return ID_NAME

                }
                ValuationInstance.KEY_REAR -> {

                    return REAR_NAME

                }
                ValuationInstance.KEY_REAR_LEFT -> {

                    return REAR_LEFT_NAME

                }
                ValuationInstance.KEY_REAR_RIGHT -> {

                    return REAR_RIGHT_NAME

                }
                ValuationInstance.KEY_INSTRUCTIONS -> {

                    return INSTRUCTIONS_NAME

                }
                ValuationInstance.KEY_INSURANCE -> {

                    return INSURANCE_NAME

                }
                ValuationInstance.KEY_KRA -> {

                    return KRA_NAME

                }
                ValuationInstance.KEY_LOG_BOOK -> {

                    return LOG_BOOK_NAME

                }
                ValuationInstance.KEY_MILLAGE -> {

                    return MILLAGE_NAME

                }
                ValuationInstance.KEY_RADIO -> {

                    return RADIO_NAME

                }
                else -> {

                    return null
                }

            }

        }

        private fun getCode(key: String): Int? {

            when (key) {
                ValuationInstance.KEY_CHASSIS -> {

                    return CHASSIS_CODE

                }
                ValuationInstance.KEY_DASHBOARD -> {

                    return DASHBOARD_CODE

                }
                ValuationInstance.KEY_FRONT -> {

                    return FRONT_CODE

                }

                ValuationInstance.KEY_FRONT_LEFT -> {

                    return FRONT_LEFT_CODE

                }ValuationInstance.KEY_ENGINE -> {

                    return ENGINE_CODE

                }
                ValuationInstance.KEY_FRONT_RIGHT -> {

                    return FRONT_RIGHT_CODE

                }
                ValuationInstance.KEY_HEAD_LIGHT -> {

                    return HEAD_LIGHT_CODE

                }
                ValuationInstance.KEY_ID -> {

                    return ID_CODE

                }
                ValuationInstance.KEY_REAR -> {

                    return REAR_CODE

                }
                ValuationInstance.KEY_REAR_LEFT -> {

                    return REAR_LEFT_CODE

                }
                ValuationInstance.KEY_REAR_RIGHT -> {

                    return REAR_RIGHT_CODE

                }
                ValuationInstance.KEY_INSTRUCTIONS -> {

                    return INSTRUCTIONS_CODE

                }
                ValuationInstance.KEY_INSURANCE -> {

                    return INSURANCE_CODE

                }
                ValuationInstance.KEY_KRA -> {

                    return KRA_CODE

                }
                ValuationInstance.KEY_LOG_BOOK -> {

                    return LOG_BOOK_CODE

                }
                ValuationInstance.KEY_MILLAGE -> {

                    return MILLAGE_CODE

                }
                ValuationInstance.KEY_RADIO -> {

                    return RADIO_CODE

                }
                else -> {

                    return null
                }

            }

        }

        private fun initializeCamIntent(code: Int, fileName: String, key: String) {

            photoFile = getPhotoFile(fileName)
            val fileProvider = FileProvider.getUriForFile(
                context,
                "com.example.abcautovaluers.fileprovider",
                photoFile
            )

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedIntentActivities: List<ResolveInfo> = context.packageManager
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
            for (resolvedIntentInfo in resolvedIntentActivities) {
                val packageName: String = resolvedIntentInfo.activityInfo.packageName
                context.grantUriPermission(
                    packageName,
                    fileProvider,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }


            ValuationInstance(context).addValuationItem(
                key,
                photoFile.absolutePath
            )

            takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            activity.startActivityForResult(takePicIntent, code)

        }

        private fun getPhotoFile(fileName: String): File {

            val storageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(fileName, ".jpg", storageDirectory)

        }

    }
}