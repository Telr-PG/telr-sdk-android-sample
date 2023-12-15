package com.telr.telrsdk

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.telr.mobile.sdk.activity.WebviewActivity
import com.telr.mobile.sdk.entity.request.payment.*
import com.telr.mobile.sdk.entity.response.payment.MobileResponse
import com.telr.mobile.sdk.entity.response.status.StatusResponse
import com.telr.telrsdk.MainActivity
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.math.BigInteger
import java.net.URLEncoder
import java.util.*

@Keep
class MainActivity : AppCompatActivity() {
    var text_language: EditText? = null
    var text_currency: EditText? = null
    var phone: EditText? = null
    var email: EditText? = null
    private var amount = "1000" // Just for testing
    var webView: WebView? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQ_PERMISSION)
        }
        //        FL.init(new FLConfig.Builder(this)
//                .minLevel(FLConst.Level.V)
//                .logToFile(true)
//                .dir(new File(Environment.getExternalStorageDirectory(), "file_logger_demo"))
//                .retentionPolicy(FLConst.RetentionPolicy.FILE_COUNT)
//                .build());
//        FL.setEnabled(true);
        text_language = findViewById(R.id.text_language)
        text_currency = findViewById(R.id.text_currency)
        phone = findViewById(R.id.phone)
        email = findViewById(R.id.email)
        webView = findViewById(R.id.webview)
        webView?.setWebViewClient(MyWebViewClient())
        getSavedCardDetails()
    }

    private fun getSavedCardDetails() {

            val queue: RequestQueue? = SingletonRequestQueue.getInstance(applicationContext)?.requestQueue
            VolleyLog.DEBUG = true
            val uri = appurl.listlinkurl
            val jsonObject = JSONObject()
            val loginObj = JSONObject()
            try {
                jsonObject.put("storeid", "<YOUR_STORE_ID>") //TODO: insert your store id
                jsonObject.put("authkey", "<YOUR_STORE_AUTH_KEY>") //TODO: insert your store auth key
                jsonObject.put("custref", "777") //556 789
                jsonObject.put("testmode", "1") //1
                loginObj.put("SavedCardListRequest", jsonObject)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(uri, loginObj, Response.Listener { response ->
                VolleyLog.wtf(response.toString())
                //   Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                var jsonloginresponse: JSONObject? = null
                try {
                    jsonloginresponse = JSONObject(response.toString())
                    val MerchantLoginResponse = jsonloginresponse.getJSONObject("SavedCardListResponse")
                    val status = MerchantLoginResponse.getString("Status")
                    if (status.equals("Success", ignoreCase = true)) {
                        val data = MerchantLoginResponse.getString("data").toString()
                        Log.e("Success", "-$data")
                        //                        FL.v(data);
                        var url: String? = null
                        //                        try {
                        url = "https://secure.telr.com/jssdk/v2/token_frame.html?sdk=ios&store_id=" + STORE_ID + "&currency=" + text_currency!!.text.toString() + "&test_mode=1&saved_cards=" + URLEncoder.encode(data)
                        val strNew = url.replace("+", " ")
                        //                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                            Log.e("Encode Error:",e.toString());
//                        }
                        Log.e("URL:", strNew)
                        //                        FL.v("tokenurl:"+strNew);
                        val webSettings = webView!!.settings
                        webSettings.javaScriptEnabled = true
                        webSettings.useWideViewPort = true
                        webSettings.loadWithOverviewMode = true
                        webSettings.defaultTextEncodingName = "utf-8"
                        webView!!.loadUrl(strNew)
                        webView!!.requestFocus()
                        webView!!.webChromeClient = object : WebChromeClient() {
                            override fun onConsoleMessage(cmsg: ConsoleMessage): Boolean {

                                /* process JSON */
                                cmsg.message()
                                Log.e("Message:", cmsg.message().toString())
                                //                                FL.e(cmsg.message());
                                return true
                            }
                        }
                    } else {
                        token = "null"
                        tokenFlag = "false"
                        Toast.makeText(applicationContext, status, Toast.LENGTH_LONG).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    //                    FL.e(e);
                    writeLogToFile(this@MainActivity)
                } catch (ne: NullPointerException) {
                    ne.printStackTrace()
                    //                    FL.e(ne);
                }
            }, errorListener) {}
        queue?.add(jsonObjectRequest)


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_PERMISSION) {
            if (grantResults.size > 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish()
            }
        }
    }//                    FL.e(ne);//                    FL.e(e);/* process JSON */

    //                                FL.e(cmsg.message());
//                        FL.v(data);
    //                        try {
    //                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                            Log.e("Encode Error:",e.toString());
//                        }
    //                        FL.v("tokenurl:"+strNew);
//   Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
//    private getSavedCardDetails() {
//        val queue: RequestQueue = SingletonRequestQueue.getInstance(applicationContext).getRequestQueue()
//        VolleyLog.DEBUG = true
//        val uri = appurl.listlinkurl
//        val jsonObject = JSONObject()
//        val loginObj = JSONObject()
//        try {
//            jsonObject.put("storeid", "<YOUR_STORE_ID>") //TODO: insert your store id
//            jsonObject.put("authkey", "<YOUR_STORE_AUTH_KEY>") //TODO: insert your store auth key
//            jsonObject.put("custref", "777") //556 789
//            jsonObject.put("testmode", "1") //1
//            loginObj.put("SavedCardListRequest", jsonObject)
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(uri, loginObj, Response.Listener { response ->
//            VolleyLog.wtf(response.toString())
//            //   Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
//            var jsonloginresponse: JSONObject? = null
//            try {
//                jsonloginresponse = JSONObject(response.toString())
//                val MerchantLoginResponse = jsonloginresponse.getJSONObject("SavedCardListResponse")
//                val status = MerchantLoginResponse.getString("Status")
//                if (status.equals("Success", ignoreCase = true)) {
//                    val data = MerchantLoginResponse.getString("data").toString()
//                    Log.e("Success", "-$data")
//                    //                        FL.v(data);
//                    var url: String? = null
//                    //                        try {
//                    url = "https://secure.telr.com/jssdk/v2/token_frame.html?sdk=ios&store_id=" + STORE_ID + "&currency=" + text_currency!!.text.toString() + "&test_mode=1&saved_cards=" + URLEncoder.encode(data)
//                    val strNew = url.replace("+", " ")
//                    //                        } catch (UnsupportedEncodingException e) {
////                            e.printStackTrace();
////                            Log.e("Encode Error:",e.toString());
////                        }
//                    Log.e("URL:", strNew)
//                    //                        FL.v("tokenurl:"+strNew);
//                    val webSettings = webView!!.settings
//                    webSettings.javaScriptEnabled = true
//                    webSettings.useWideViewPort = true
//                    webSettings.loadWithOverviewMode = true
//                    webSettings.defaultTextEncodingName = "utf-8"
//                    webView!!.loadUrl(strNew)
//                    webView!!.requestFocus()
//                    webView!!.webChromeClient = object : WebChromeClient() {
//                        override fun onConsoleMessage(cmsg: ConsoleMessage): Boolean {
//
//                            /* process JSON */
//                            cmsg.message()
//                            Log.e("Message:", cmsg.message().toString())
//                            //                                FL.e(cmsg.message());
//                            return true
//                        }
//                    }
//                } else {
//                    token = "null"
//                    tokenFlag = "false"
//                    Toast.makeText(applicationContext, status, Toast.LENGTH_LONG).show()
//                }
//            } catch (e: JSONException) {
//                e.printStackTrace()
//                //                    FL.e(e);
//                writeLogToFile(this@MainActivity)
//            } catch (ne: NullPointerException) {
//                ne.printStackTrace()
//                //                    FL.e(ne);
//            }
//        }, errorListener) {}
//        queue.add(jsonObjectRequest)
//    }
    var errorListener = Response.ErrorListener { error ->
        if (error is NetworkError) {
            Toast.makeText(applicationContext, "No network available", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun sendMessage(view: View?) {


//        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        val editText = findViewById<View>(R.id.text_amount) as EditText
        amount = editText.text.toString()
        Log.e("tokenflag", tokenFlag)
        if (tokenFlag === "false") {
            val intent = Intent(this@MainActivity, WebviewActivity::class.java)
            intent.putExtra(WebviewActivity.EXTRA_MESSAGE, getMobileRequest())
            intent.putExtra("tokenFlag", tokenFlag)
            intent.putExtra(WebviewActivity.SUCCESS_ACTIVTY_CLASS_NAME, "com.telr.SuccessTransationActivity")
            intent.putExtra(WebviewActivity.FAILED_ACTIVTY_CLASS_NAME, "com.telr.FailedTransationActivity")
            intent.putExtra(WebviewActivity.IS_SECURITY_ENABLED, isSecurityEnabled)
            //        startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE)
        } else {
            val intent = Intent(this@MainActivity, WebviewActivity::class.java)
            intent.putExtra(WebviewActivity.EXTRA_MESSAGE, getMobileRequestSeamless())
            intent.putExtra("tokenFlag", tokenFlag)
            intent.putExtra(WebviewActivity.SUCCESS_ACTIVTY_CLASS_NAME, "com.telr.SuccessTransationActivity")
            intent.putExtra(WebviewActivity.FAILED_ACTIVTY_CLASS_NAME, "com.telr.FailedTransationActivity")
            intent.putExtra(WebviewActivity.IS_SECURITY_ENABLED, isSecurityEnabled)
            //        startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        //writeLogToFile(MainActivity.this);
        if (requestCode == REQUEST_CODE &&
                resultCode == RESULT_OK) {
            val paymentMethod = intent!!.getStringExtra("auth")
            if (paymentMethod.equals("yes", ignoreCase = true)) {
                val status = intent.getParcelableExtra<Parcelable>(WebviewActivity.PAYMENT_RESPONSE) as MobileResponse?
                val builder1 = AlertDialog.Builder(this@MainActivity)
                builder1.setMessage("Thank you! The transaction is " + status!!.getAuth().getMessage())
                builder1.setCancelable(true)
                builder1.setPositiveButton(
                        "OK"
                ) { dialog, id ->
                    dialog.cancel()
                    //                                webView.clearCache(true);
//                                getSavedCardDetails();
//                                webView.setVisibility(View.VISIBLE);
                }
                val alert11 = builder1.create()
                alert11.show()
                //                TextView textView = (TextView)findViewById(R.id.text_payment_result);
//                TextView txt_code=(TextView)findViewById(R.id.txt_code);
//                txt_code.setText("Code : "+intent.getStringExtra("Code"));
//                textView.setText(textView.getText() +" : " + status.getTrace());
                //  Log.e("CODEZZZ",":"+ TelrSharedPreference.getInstance(this).getDataFromPreference("Code"));
                if (status.getAuth() != null) {
                    val builder2 = AlertDialog.Builder(this@MainActivity)
                    builder2.setMessage("Thank you! The transaction is " + status.getAuth().getMessage())
                    builder2.setCancelable(true)
                    builder2.setPositiveButton(
                            "OK"
                    ) { dialog, id ->
                        dialog.cancel()
                        //                                    webView.clearCache(true);
//                                    getSavedCardDetails();
//                                    webView.setVisibility(View.VISIBLE);
                    }
                    val alert12 = builder2.create()
                    alert12.show()
                    //
//                    FL.d(status.getAuth().getStatus());
                    status.getAuth().getStatus() // Authorisation status. A indicates an authorised transaction. H also indicates an authorised transaction, but where the transaction has been placed on hold. Any other value indicates that the request could not be processed.
                    status.getAuth().getAvs() /* Result of the AVS check:
                                            Y = AVS matched OK
                                            P = Partial match (for example, post-code only)
                                            N = AVS not matched
                                            X = AVS not checked
                                            E = Error, unable to check AVS */
                    status.getAuth().getCode() // If the transaction was authorised, this contains the authorisation code from the card issuer. Otherwise it contains a code indicating why the transaction could not be processed.
                    status.getAuth().getMessage() // The authorisation or processing error message.
                    status.getAuth().getCa_valid()
                    status.getAuth().getCardcode() // Code to indicate the card type used in the transaction. See the code list at the end of the document for a list of card codes.
                    status.getAuth().getCardlast4() // The last 4 digits of the card number used in the transaction. This is supplied for all payment types (including the Hosted Payment Page method) except for PayPal.
                    status.getAuth().getCvv() /* Result of the CVV check:
                                           Y = CVV matched OK
                                           N = CVV not matched
                                           X = CVV not checked
                                           E = Error, unable to check CVV */
                    status.getAuth().getTranref() //The payment gateway transaction reference allocated to this request.
                    status.getAuth().getCard().getFirst6() // The first 6 digits of the card number used in the transaction, only for version 2 is submitted in Tran -> Version
                    status.getAuth().getCard().getCountry()
                    status.getAuth().getCard().getExpiry().getMonth()
                    status.getAuth().getCard().getExpiry().getYear()
                }
            } else {
                val status = intent.getParcelableExtra<Parcelable>(WebviewActivity.PAYMENT_RESPONSE) as StatusResponse?
                val builder11 = AlertDialog.Builder(this@MainActivity)
                builder11.setMessage("Thank you! The transaction is " + status!!.getAuth().getMessage())
                builder11.setCancelable(true)
                builder11.setPositiveButton(
                        "OK"
                ) { dialog, id ->
                    dialog.cancel()
                    //                                webView.clearCache(true);
//                                getSavedCardDetails();
//                                webView.setVisibility(View.VISIBLE);
                }
                val alert111 = builder11.create()
                alert111.show()
                //
//                TextView textView = (TextView) findViewById(R.id.text_payment_result);
//                TextView txt_code = (TextView) findViewById(R.id.txt_code);
//                txt_code.setText("Code : " + intent.getStringExtra("Code"));
//                textView.setText(textView.getText() + " : " + status.getTrace());
                //  Log.e("CODEZZZ",":"+ TelrSharedPreference.getInstance(this).getDataFromPreference("Code"));
                if (status.getAuth() != null) {
//                    FL.d(status.getAuth().getStatus());
                    status.getAuth().getStatus() // Authorisation status. A indicates an authorised transaction. H also indicates an authorised transaction, but where the transaction has been placed on hold. Any other value indicates that the request could not be processed.
                    status.getAuth().getAvs() /* Result of the AVS check:
                                            Y = AVS matched OK
                                            P = Partial match (for example, post-code only)
                                            N = AVS not matched
                                            X = AVS not checked
                                            E = Error, unable to check AVS */
                    status.getAuth().getCode() // If the transaction was authorised, this contains the authorisation code from the card issuer. Otherwise it contains a code indicating why the transaction could not be processed.
                    status.getAuth().getMessage() // The authorisation or processing error message.
                    status.getAuth().getCa_valid()
                    status.getAuth().getCardcode() // Code to indicate the card type used in the transaction. See the code list at the end of the document for a list of card codes.
                    status.getAuth().getCardlast4() // The last 4 digits of the card number used in the transaction. This is supplied for all payment types (including the Hosted Payment Page method) except for PayPal.
                    status.getAuth().getCvv() /* Result of the CVV check:
                                           Y = CVV matched OK
                                           N = CVV not matched
                                           X = CVV not checked
                                           E = Error, unable to check CVV */
                    status.getAuth().getTranref() //The payment gateway transaction reference allocated to this request.
                    status.getAuth().getCard().getFirst6() // The first 6 digits of the card number used in the transaction, only for version 2 is submitted in Tran -> Version
                    status.getAuth().getCard().getCountry()
                    status.getAuth().getCard().getExpiry().getMonth()
                    status.getAuth().getCard().getExpiry().getYear()
                }
            }
            //            StatusResponse status = intent.getParcelableExtra(WebviewActivity.PAYMENT_RESPONSE);
//            if(status.getAuth()!= null) {
//                Log.d("DataVal:",  status.getAuth().getCard().getFirst6());
//                Log.d("Code", intent.getStringExtra("Code"));
//
//
//            }
        }
    }

    fun writeLogToFile(context: Context?) {
        var bufferedWriter: BufferedWriter? = null
        try {
            val file = File("ddddd")
            bufferedWriter = BufferedWriter(FileWriter(file, true))
            val process = Runtime.getRuntime().exec("logcat -d")
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            var oneLine: String?
            while (bufferedReader.readLine().also { oneLine = it } != null) {
                bufferedWriter.write(oneLine)
                bufferedWriter.newLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getMobileRequest(): MobileRequest {
        val mobile = MobileRequest()
        mobile.setStore(STORE_ID) // Store ID
        mobile.setKey(KEY) // Authentication Key : The Authentication Key will be supplied by Telr as part of the Mobile API setup process after you request that this integration type is enabled for your account. This should not be stored permanently within the App.
        val app = App()
        app.setId("123456789") // Application installation ID
        app.setName("TelrSDK") // Application name
        app.setUser("123456") // Application user ID : Your reference for the customer/user that is running the App. This should relate to their account within your systems.
        app.setVersion("0.0.1") // Application version
        app.setSdk("123")
        mobile.setApp(app)
        val tran = Tran()
        tran.setTest("1") //1                        // Test mode : Test mode of zero indicates a live transaction. If this is set to any other value the transaction will be treated as a test.
        tran.setType("auth") /* auth Transaction type
                                                            'auth'   : Seek authorisation from the card issuer for the amount specified. If authorised, the funds will be reserved but will not be debited until such time as a corresponding capture command is made. This is sometimes known as pre-authorisation.
                                                            'sale'   : Immediate purchase request. This has the same effect as would be had by performing an auth transaction followed by a capture transaction for the full amount. No additional capture stage is required.
                                                            'verify' : Confirm that the card details given are valid. No funds are reserved or taken from the card.
                                                        */
        tran.setClazz("paypage") // Transaction class only 'paypage' is allowed on mobile, which means 'use the hosted payment page to capture and process the card details'
        tran.setCartid(BigInteger(128, Random()).toString()) //// Transaction cart ID : An example use of the cart ID field would be your own transaction or order reference.
        tran.setDescription("Test Mobile API") // Transaction description
        tran.setLanguage(text_language!!.text.toString())
        tran.setCurrency(text_currency!!.text.toString()) // Transaction currency : Currency must be sent as a 3 character ISO code. A list of currency codes can be found at the end of this document. For voids or refunds, this must match the currency of the original transaction.
        tran.setAmount(amount) // Transaction amount : The transaction amount must be sent in major units, for example 9 dollars 50 cents must be sent as 9.50 not 950. There must be no currency symbol, and no thousands separators. Thedecimal part must be separated using a dot.
        //  tran.setRef("030026794329");                                // (Optinal) Previous transaction reference : The previous transaction reference is required for any continuous authority transaction. It must contain the reference that was supplied in the response for the original transaction.

        //040023303844  //030023738912
        // tran.setFirstref("030023738912");             // (Optinal) Previous user transaction detail reference : The previous transaction reference is required for any continuous authority transaction. It must contain the reference that was supplied in the response for the original transaction.
        mobile.setTran(tran)
        val billing = Billing()
        val address = Address()
        address.setCity("Kerala") // City : the minimum required details for a transaction to be processed
        address.setCountry("AE") // Country : Country must be sent as a 2 character ISO code. A list of country codes can be found at the end of this document. the minimum required details for a transaction to be processed
        address.setRegion("Kottayam") // Region
        address.setLine1("WTC") // Street address â€“ line 1: the minimum required details for a transaction to be processed
        //address.setLine2("SIT G=Towe");               // (Optinal)
        //address.setLine3("SIT G=Towe");               // (Optinal)
        //address.setZip("SIT G=Towe");                 // (Optinal)
        billing.setAddress(address)
        val name = Name()
        name.setFirst("Divya") // Forename : .1the minimum required details for a transaction to be processed
        name.setLast("Thampi") // Surname : the minimum required details for a transaction to be processed
        name.setTitle("Mrs") // Title
        billing.setName(name)
        billing.setEmail("test@telr.com") //Replace with email id//stackfortytwo@gmail.com : the minimum required details for a transaction to be processed.
        billing.setPhone("1234567890") //Replace with phone number
        mobile.setBilling(billing)
        mobile.setCustref("777") //556 789new parameter added for saved card feature
        //        Paymethod paymethod=new Paymethod();
////        if(isSeamlessEnabled){
//        paymethod.setType("card");
//   if(token.toString().length()>1)
//   {
//       paymethod.setCardtoken(token);
//   }
//   else
//   {
//       paymethod.setCardtoken("null");
//   }
//        mobile.setPaymethod(paymethod);
//        }
        Log.e("REQUEST-----:", mobile.toString())
        //        FL.d("Request:"+mobile.toString());
        return mobile
    }

    private fun getMobileRequestSeamless(): MobileRequestSeamless {
        val mobiles = MobileRequestSeamless()
        mobiles.setStore(STORE_ID) // Store ID
        mobiles.setKey(KEY) // Authentication Key : The Authentication Key will be supplied by Telr as part of the Mobile API setup process after you request that this integration type is enabled for your account. This should not be stored permanently within the App.
        val app = App()
        app.setId("123456789") // Application installation ID
        app.setName("TelrSDK") // Application name
        app.setUser("123456") // Application user ID : Your reference for the customer/user that is running the App. This should relate to their account within your systems.
        app.setVersion("0.0.1") // Application version
        app.setSdk("123")
        mobiles.setApp(app)
        val tran = Tran()
        tran.setTest("1") //1                        // Test mode : Test mode of zero indicates a live transaction. If this is set to any other value the transaction will be treated as a test.
        tran.setType("Sale") /* auth Transaction type
                                                            'auth'   : Seek authorisation from the card issuer for the amount specified. If authorised, the funds will be reserved but will not be debited until such time as a corresponding capture command is made. This is sometimes known as pre-authorisation.
                                                            'sale'   : Immediate purchase request. This has the same effect as would be had by performing an auth transaction followed by a capture transaction for the full amount. No additional capture stage is required.
                                                            'verify' : Confirm that the card details given are valid. No funds are reserved or taken from the card.
                                                        */
        tran.setClazz("paypage") // Transaction class only 'paypage' is allowed on mobile, which means 'use the hosted payment page to capture and process the card details'
        tran.setCartid(BigInteger(128, Random()).toString()) //// Transaction cart ID : An example use of the cart ID field would be your own transaction or order reference.
        tran.setDescription("Test Mobile API") // Transaction description
        tran.setLanguage(text_language!!.text.toString())
        tran.setCurrency(text_currency!!.text.toString()) // Transaction currency : Currency must be sent as a 3 character ISO code. A list of currency codes can be found at the end of this document. For voids or refunds, this must match the currency of the original transaction.
        tran.setAmount(amount) // Transaction amount : The transaction amount must be sent in major units, for example 9 dollars 50 cents must be sent as 9.50 not 950. There must be no currency symbol, and no thousands separators. Thedecimal part must be separated using a dot.
        //  tran.setRef("030026794329");                                // (Optinal) Previous transaction reference : The previous transaction reference is required for any continuous authority transaction. It must contain the reference that was supplied in the response for the original transaction.

        //040023303844  //030023738912
        // tran.setFirstref("030023738912");             // (Optinal) Previous user transaction detail reference : The previous transaction reference is required for any continuous authority transaction. It must contain the reference that was supplied in the response for the original transaction.
        mobiles.setTran(tran)
        val billing = Billing()
        val address = Address()
        address.setCity("Kerala") // City : the minimum required details for a transaction to be processed
        address.setCountry("AE") // Country : Country must be sent as a 2 character ISO code. A list of country codes can be found at the end of this document. the minimum required details for a transaction to be processed
        address.setRegion("Kottayam") // Region
        address.setLine1("WTC") // Street address â€“ line 1: the minimum required details for a transaction to be processed
        //address.setLine2("SIT G=Towe");               // (Optinal)
        //address.setLine3("SIT G=Towe");               // (Optinal)
        //address.setZip("SIT G=Towe");                 // (Optinal)
        billing.setAddress(address)
        val name = Name()
        name.setFirst("Divya") // Forename : .1the minimum required details for a transaction to be processed
        name.setLast("Thampi") // Surname : the minimum required details for a transaction to be processed
        name.setTitle("Mrs") // Title
        billing.setName(name)
        billing.setEmail("test@telr.com") //Replace with email id//stackfortytwo@gmail.com : the minimum required details for a transaction to be processed.
        billing.setPhone("1234567890") //Replace with phone number
        mobiles.setBilling(billing)
        mobiles.setCustref("777") // 556 789 new parameter added for saved card feature
        val paymethod = Paymethod()
        //        if(isSeamlessEnabled){
        paymethod.setType("card")
        paymethod.setCardtoken(token)
        mobiles.setPaymethod(paymethod)
        //        }
        Log.e("REQUEST-----:", mobiles.toString())
        //        FL.d("Request:"+mobiles.toString());
        return mobiles
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            view.visibility = View.GONE
            Log.e("Token url-------:", url)
            val segments = url.split("=").toTypedArray()
            // Grab the last segment
            val document = segments[segments.size - 1]
            token = document
            Log.e("Token -------:", token)
            //            workwithNewFlow(token);
            return true
        }
    }

    companion object {
        private const val REQ_PERMISSION = 1233
        const val KEY = "<YOUR_STORE_AUTH_KEY>"		//TODO: insert your store auth key
        const val STORE_ID = "<YOUR_STORE_ID>" //TODO: insert your store id
        const val isSecurityEnabled = true // Mark false to test on simulator, True to test on actual device and Production
        const val REQUEST_CODE = 100
        var token = ""
        var tokenFlag = "true"
    }
}
