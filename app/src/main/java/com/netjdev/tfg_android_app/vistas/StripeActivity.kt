package com.netjdev.tfg_android_app.vistas

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.netjdev.tfg_android_app.R
import com.netjdev.tfg_android_app.databinding.ActivityStripeBinding
import com.netjdev.tfg_android_app.modelos.Pago
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap

class StripeActivity : AppCompatActivity() {

    // Variable para la vinculacion de vistas
    private lateinit var binding: ActivityStripeBinding

    // Instancia de Firebase Storage
    private var firestore = Firebase.firestore

    // Id de usuario (email)
    private var user_email = ""

    // Fecha actual
    private val currentDate: Date = Date()
    private lateinit var calendar: Calendar

    // Fecha cuota a pagar
    private lateinit var cuotaPagar: Calendar

    val SECRET_KEY =
        "sk_test_51Kuho7GH8t9oyfbUPyOwI7oM0ywqiiHL3MikRUZhOvnFGyCw3AXAxVUJMGuk95EfRoroY8WCvSZiXIHXCmtze5eS00kIJOsDB6"
    val PUBLISHABLE_KEY =
        "pk_test_51Kuho7GH8t9oyfbUlFJP9Zlg0ttFE25h2FjiAG1rn24JdBiyPwaGW6QxJpAyDN1Hpsb5WggqcS03au6C3xQRZ7lh00n0zo3fOn"

    lateinit var paymentSheet: PaymentSheet

    private lateinit var customerID: String
    private lateinit var EphemeralKey: String
    private lateinit var ClientSecret: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStripeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PaymentConfiguration.init(this, PUBLISHABLE_KEY)

        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)

        intent.getStringExtra("user_email")?.let { user_email = it }

        initComponents()


    }

    private fun initComponents() {
        // Texto de la cabecera
        val text_header: TextView = findViewById(R.id.txtHeader)
        text_header.text = getString(R.string.payments)

        // Inicia el prceso de obtención de credenciales para realizar el pago
        //getCustomerID()

        // Realizar pago
        binding.btnPay.setOnClickListener { paymentFlow() }

        // Inicializar calendar
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"))
        calendar.time = currentDate

        // Spinner
        val meses = mutableListOf<String>(
            "1 mes - 35€",
            "2 meses - 65€",
            "3 meses - 95€"
        )

        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            meses
        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView =
                    super.getDropDownView(position, convertView, parent) as TextView
                // Fijar estilo y fuente de los items
                view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)

                //return super.getDropDownView(position, convertView, parent)
                return view
            }
        }

        // Vincular los datos al spinner
        binding.spinner2.adapter = adapter


        checkUserPays()

        //BORRAR
        //savePaymentFirebase()

    }

    /**
     * Clase para comprobar los pagos del usuario
     *  - Si no está pagado el mes actual se ofrecen las opciones de pagar el mes actual o el proximo.
     *  - Si el mes actual está pagado solo se ofrece la opción de pagar el proximo mes.
     *  - Si el mes proximo está pagado se informa al usuario que está al corriente de pagos.
     */
    private fun checkUserPays() {
        //BORRAR
        binding.button.isEnabled = true
        binding.button.setOnClickListener { savePaymentFirebase(cuotaPagar) }

        // Mes actual
        //val mesActual: Date = Date()
        val mesActual: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"))
        mesActual.time = currentDate
        // Fecha de la cuota que se va a pagar
        cuotaPagar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"))
        cuotaPagar.time = currentDate
        // Todos los pagos se registran como el 1 del mes pagado
        cuotaPagar.set(Calendar.DAY_OF_MONTH, 1)
        // Cambiar mes pagado para hacer pruebas
        cuotaPagar.set(Calendar.MONTH, 0) // Enero de 2022

        firestore.collection("users").document(user_email).collection("payments")
            .orderBy("cuotaPagada", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { pagos ->
                val listaPagos = pagos.toObjects(Pago::class.java)
                if (listaPagos.isEmpty()) {
                    Log.d("Sport", "Lista de pagos vacia")
                } else {
                    Log.d("Sport", "Lista de pagos NO vacia: ${listaPagos.size}")
                    // Seleccionar el último pago
                    val ultimaCuota: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"))
                    ultimaCuota.time = listaPagos[0].cuotaPagada
                    if (ultimaCuota < mesActual){
                        Log.d("Sport", "Se puede pagar: $cuotaPagar")
                        Log.d("Sport", "Ultima cuota: ${ultimaCuota.time}")
                        Log.d("Sport", "Cuota actual: ${mesActual.time}")
                        //binding.button.isEnabled = true
                    }
                }
            }
    }

    private fun getCustomerID() {
        val stringRequest = object : StringRequest(Request.Method.POST,
            "https://api.stripe.com/v1/customers",
            Response.Listener<String> { response ->
                if (response != null) {
                    try {
                        val responseJson = JSONObject(response)
                        customerID = responseJson.getString("id")

                        //Toast.makeText(this, customerID, Toast.LENGTH_SHORT).show()

                        // Obtener la ephemeral key
                        getEphemeralKey(customerID)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            },
            Response.ErrorListener { error -> error.printStackTrace() }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $SECRET_KEY"
                return headers
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun getEphemeralKey(customerID: String) {
        val stringRequest = object : StringRequest(Request.Method.POST,
            "https://api.stripe.com/v1/ephemeral_keys",
            Response.Listener<String> { response ->
                if (response != null) {
                    try {
                        val responseJson = JSONObject(response)
                        EphemeralKey = responseJson.getString("id")

                        //Toast.makeText(this, EphemeralKey, Toast.LENGTH_SHORT).show()

                        // Obtener la ephemeral key
                        getClientSecret(customerID, EphemeralKey)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            },
            Response.ErrorListener { error -> error.printStackTrace() }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $SECRET_KEY"
                headers["Stripe-Version"] = "2020-08-27"
                return headers
            }

            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["customer"] = customerID
                return params
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun getClientSecret(customerID: String, ephemeralKey: String) {
        val stringRequest = object : StringRequest(Request.Method.POST,
            "https://api.stripe.com/v1/payment_intents",
            Response.Listener<String> { response ->
                if (response != null) {
                    try {
                        val responseJson = JSONObject(response)
                        ClientSecret = responseJson.getString("client_secret")

                        //Toast.makeText(this, ClientSecret, Toast.LENGTH_SHORT).show()

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            },
            Response.ErrorListener { error -> error.printStackTrace() }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $SECRET_KEY"
                return headers
            }

            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["customer"] = customerID
                // En el parametro amount (cantidad del pago) los dos últimos ceros son para decimales:
                // 1225 = 12.25 EUR
                params["amount"] = "1225"
                params["currency"] = "eur"
                params["automatic_payment_methods[enabled]"] = "true"
                return params
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private fun paymentFlow() {
        paymentSheet.presentWithPaymentIntent(
            ClientSecret,
            PaymentSheet.Configuration(
                "Sport Center",
                PaymentSheet.CustomerConfiguration(customerID, EphemeralKey)
            )
        )
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                //print("Canceled")
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show()
            }
            is PaymentSheetResult.Failed -> {
                //print("Error: ${paymentSheetResult.error}")
                Toast.makeText(this, "Error: ${paymentSheetResult.error}", Toast.LENGTH_SHORT)
                    .show()
            }
            is PaymentSheetResult.Completed -> {
                // Display for example, an order confirmation screen
                //print("Completed")
                Toast.makeText(this, "Pago completado", Toast.LENGTH_SHORT).show()

                //savePaymentFirebase()
            }
        }
    }

    private fun savePaymentFirebase(cuotaPagar: Calendar) {
        // La numeración de los meses empieza en 0 (Enero = 0)
        Log.d("Sport", calendar.get(Calendar.MONTH).toString())
        Log.d("Sport", calendar.get(Calendar.YEAR).toString())
        Log.d("Sport", "Cuota: ${cuotaPagar.time}")
        val pago = Pago(
            // Convertir calendar a date para guardar en Firebase
            cuotaPagada = cuotaPagar.time
        )
        firestore.collection("users").document(user_email).collection("payments")
            .document()
            .set(pago)
            .addOnSuccessListener {
                Log.d("Sport", "Pago guardado en Firestore")
            }
            .addOnFailureListener {
                Log.d("Sport", "Error: pago Firestore $it")
            }
    }
}