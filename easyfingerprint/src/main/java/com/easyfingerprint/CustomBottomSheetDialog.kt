package com.easyfingerprint

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.support.annotation.ColorRes
import android.support.design.widget.BottomSheetDialog
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

/**
 * created at 26/07/2018
 * @author rafaelbarbosatec
 * @version 1.0
 *
 */
class CustomBottomSheetDialog(val context: Activity?){

    private val STATE_SET_ON = intArrayOf(R.attr.state_on, -R.attr.state_off, -R.attr.state_error)
    private val STATE_SET_OFF = intArrayOf(-R.attr.state_on, R.attr.state_off, -R.attr.state_error)
    private val STATE_SET_ERROR = intArrayOf(-R.attr.state_on, -R.attr.state_off, R.attr.state_error)

    private var bottomSheetDialog: BottomSheetDialog? = null
    private var listern: CustomBottomSheetListern? = null
    private var msgError: String = ""
    private var msgInstruction: String = ""
    private var textview_tittle: TextView? = null
    private var textview_subtittle: TextView? = null
    private var textview_description: TextView? = null
    private var textview_finger_touch: TextView? = null
    private var imageview_icon: ImageView? = null
    private var imageview_finger: ImageView? = null
    private var button_cancel_finger: Button? = null

    init {
        if (context != null) {
            bottomSheetDialog = BottomSheetDialog(context)
            initDialog()
        }
    }

    /**
     * Method used to initialize CustonDialog
     */
    @SuppressLint("InflateParams")
    private fun initDialog() {
        val view = context?.layoutInflater?.inflate(R.layout.bottom_sheet_finger_print, null)
        bottomSheetDialog?.setContentView(view)
        textview_tittle = bottomSheetDialog?.findViewById<TextView>(R.id.textview_tittle)
        textview_subtittle = bottomSheetDialog?.findViewById<TextView>(R.id.textview_subtittle)
        textview_description = bottomSheetDialog?.findViewById<TextView>(R.id.textview_description)
        textview_finger_touch = bottomSheetDialog?.findViewById<TextView>(R.id.textview_finger_touch)
        imageview_icon = bottomSheetDialog?.findViewById<ImageView>(R.id.imageview_icon)
        imageview_finger = bottomSheetDialog?.findViewById<ImageView>(R.id.imageview_finger)
        button_cancel_finger = bottomSheetDialog?.findViewById<Button>(R.id.button_cancel_finger)
        bottomSheetDialog?.findViewById<Button>(R.id.button_cancel_finger)?.setOnClickListener {
            close()
        }
        bottomSheetDialog?.setOnDismissListener {
            listern?.closed()
        }

        if (context != null) {
            if (Util.isAndroidGraterLOLLIPOP()) {
                imageview_finger?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.asl_fingerprint))
            } else {
                imageview_finger?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fingerprint_black_24dp))
            }
        }
    }

    /**
     * Method used to initialize listern for close and open
     * @param listern
     * @see CustomBottomSheetListern
     */
    fun setListern(listern: CustomBottomSheetListern): CustomBottomSheetDialog{
        this.listern = listern
        return this
    }

    /**
     * Method used to set Tittle in BottomDialog
     * @param tittle
     */
    fun setTittle(tittle:String): CustomBottomSheetDialog{
        textview_tittle?.text = tittle
        return this
    }

    /**
     * Method used to set SubTittle in BottomDialog
     * @param subTittle
     */
    fun setSubTittle(subTittle:String): CustomBottomSheetDialog{
        textview_subtittle?.text = subTittle
        return this
    }

    /**
     * Method used to set Description in BottomDialog
     * @param description
     */
    fun setDescription(description:String): CustomBottomSheetDialog{
        textview_description?.text = description
        return this
    }

    /**
     * Method used to set Instruction in BottomDialog
     * @param instruction
     */
    fun seFingerprintInstruction(instruction:String): CustomBottomSheetDialog{
        msgInstruction = instruction
        textview_finger_touch?.text = instruction
        return this
    }

    /**
     * Method used to set error mensage when finger not reconized
     * @param error
     */
    fun setMsgError(error:String): CustomBottomSheetDialog{
        msgError = error
        return this
    }

    /**
     * Method used to set icon to BottomDialog
     * @param icon
     */
    fun setIcon(icon: Drawable?): CustomBottomSheetDialog{

        imageview_icon?.setImageDrawable(icon)

        if (icon != null) {
            imageview_icon?.visibility = View.VISIBLE
        }else{
            imageview_icon?.visibility = View.GONE
        }

        return this
    }

    /**
     * Method used to set color primary to BottomDialog
     * @param color
     */
    fun setColorPrimary(@ColorRes color:Int){
        try {

            if (context != null){

                button_cancel_finger?.setTextColor(ContextCompat.getColor(context, color))

                val gd =  imageview_finger?.background as GradientDrawable
                gd.setColor(ContextCompat.getColor(context, color))

            }

        }catch (e: Exception){

        }
    }

    /**
     * Method used to showError in customDialog
     */
    fun showError(){

        if (msgError.isEmpty()){
            textview_finger_touch?.text = context?.getString(R.string.text_erro_not_reconized)
        }else{
            textview_finger_touch?.text = msgError
        }
        if (context != null)
            textview_finger_touch?.setTextColor(ContextCompat.getColor(context,R.color.color_error))

        imageview_finger?.setImageState(STATE_SET_ERROR, true)

        startTimer()
    }

    /**
     * Method used to start timer tha return normal visualization
     */
    private fun startTimer() {
        val handle = Handler()
        handle.postDelayed({ showIntructionDefault() }, 1500)
    }

    /**
     * Method used to show normal visualization
     */
    private fun showIntructionDefault() {

        if (msgInstruction.isEmpty()){
            textview_finger_touch?.text = context?.getString(R.string.text_default_instruction)
        }else{
            textview_finger_touch?.text = msgInstruction
        }

        if (context != null)
            textview_finger_touch?.setTextColor(ContextCompat.getColor(context,R.color.color_text))

        if (Util.isAndroidGraterLOLLIPOP())
            imageview_finger?.setImageState(STATE_SET_ON, true)
    }

    /**
     * Method used to close dialog
     */
    fun close() {

        if (bottomSheetDialog?.isShowing == true){
            try{
                bottomSheetDialog?.dismiss()
                listern?.closed()
            }catch (e: Exception){

            }
        }
    }

    /**
     * Method used to show dialog
     */
    fun show(){

        try {

            if (Util.isAndroidGraterLOLLIPOP())
                imageview_finger?.setImageState(STATE_SET_OFF, true)

            bottomSheetDialog?.show()
            listern?.open()

            if (Util.isAndroidGraterLOLLIPOP()){
                val handle = Handler()
                handle.postDelayed({ imageview_finger?.setImageState(STATE_SET_ON, true) }, 1000)
            }

        } catch (e: Exception){

        }

    }

    /**
     * Method used to check if dialog is showing
     */
    fun isShowing():Boolean{
        return bottomSheetDialog?.isShowing ?: false
    }

    /**
     * Interface responsible to return if close or open dialog
     */
    interface CustomBottomSheetListern{
        fun open()
        fun closed()
    }

}