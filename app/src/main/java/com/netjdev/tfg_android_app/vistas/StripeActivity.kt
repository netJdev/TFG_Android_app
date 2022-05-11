package com.netjdev.tfg_android_app.vistas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
import com.netjdev.tfg_android_app.util.Utilities
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.android.synthetic.main.header.*
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
    private lateinit var cuotaActual: Calendar
    // Precio cuota
    // La pasarela de pago Stripe.com considera los dos últimos digitos como decimales, así que al
    // hacer el pago se añaden dos 0 al final del precio
    private var precioCuota = 45
    // Habilitar pago
    private var habilitarPago = false

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
        text_header.text = getString(R.string.pay_fee)
        btnHeader.setOnClickListener { onBackPressed() }

        binding.btnCancelPay.setOnClickListener { onBackPressed() }

        // Ocultar progressbar
        binding.includeProgressbar.progressbar.visibility = View.INVISIBLE

        // Inicia el prceso de obtención de credenciales para realizar el pago con Stripe
        //getCustomerID()

        // Realizar pago
        //binding.btnPay.setOnClickListener { paymentFlow() }
        binding.btnPay.setOnClickListener { getCustomerID() }
        binding.btnPay.isEnabled = false

        // Inicializar calendar
        //calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"))
        calendar = Calendar.getInstance()
        calendar.time = currentDate

        checkUserPays()

    }

    /**
     * Metodo para comprobar los pagos del usuario, si no está pagado el mes actual se permite
     * realizar el pago
     */
    private fun checkUserPays() {
        // Mes actual
        //cuotaActual = Calendar.getInstance(TimeZone.getTimeZone("GMT+0:00"))
        cuotaActual = Calendar.getInstance()
        cuotaActual.time = currentDate
        // Todos los pagos se registran como el día 1 del mes pagado
        cuotaActual.set(Calendar.DAY_OF_MONTH, 1)
        cuotaActual.set(Calendar.HOUR_OF_DAY, 1)
        cuotaActual.set(Calendar.MINUTE, 0)
        cuotaActual.set(Calendar.SECOND, 0)
        cuotaActual.set(Calendar.MILLISECOND, 0)
        // Cambiar mes pagado para hacer pruebas (0->enero, 1->febrero ...)
        //cuotaActual.set(Calendar.DAY_OF_MONTH, 0) // Cambiar mes para hacer pruebas
        // Nombre del mes
        val mes = Utilities.getMonthName(cuotaActual)
        // Obtener identificador del mes
        val identificador =
            this.resources.getIdentifier(mes.lowercase(), "string", this.packageName)

        firestore.collection("users").document(user_email).collection("payments")
            .orderBy("cuotaPagada", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { pagos ->
                val listaPagos = pagos.toObjects(Pago::class.java)
                if (listaPagos.isEmpty()) {
                    binding.txtPagoTop.text = getString(R.string.current_fee)
                    binding.txtPago.text = "${getString(identificador)} - ${cuotaActual.get(Calendar.YEAR)}"
                    binding.txtPrice.text = "${precioCuota.toString()} ${getString(R.string.currency_symbol)}"
                    binding.btnPay.isEnabled = true
                } else {
                    // Seleccionar el último pago realizado
                    val ultimaCuota: Calendar = Calendar.getInstance()
                    ultimaCuota.time = listaPagos[0].cuotaPagada!!
                    val compareDate = ultimaCuota.time.compareTo(cuotaActual.time)

                    // Caso en el que la última cuota pagada es anterior a la actual
                    if (compareDate == -1) {
                        binding.txtPagoTop.text = getString(R.string.current_fee)
                        binding.txtPago.text = "${getString(identificador)} - ${cuotaActual.get(Calendar.YEAR)}"
                        binding.txtPrice.text = "${precioCuota.toString()} ${getString(R.string.currency_symbol)}"
                        binding.btnPay.isEnabled = true
                        // Caso en el que la última cuota es la actual
                    } else if (compareDate == 0) {
                        // Informar al usuario que está al corriente de pagos
                        binding.txtPagoTop.text = ""
                        binding.txtPago.text = "La cuota actual (${getString(identificador)} - ${cuotaActual.get(Calendar.YEAR)}) ya está pagada"
                    }
                }
            }
    }

    private fun getCustomerID() {
        // Se muestra la barra de carga hasta que termine el proceso de pago
        binding.includeProgressbar.progressbar.visibility = View.VISIBLE

        val stringRequest = object : StringRequest(Request.Method.POST,
            "https://api.stripe.com/v1/customers",
            Response.Listener<String> { response ->
                if (response != null) {
                    try {
                        val responseJson = JSONObject(response)
                        customerID = responseJson.getString("id")

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

                        paymentFlow()
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
                // Añadir dos 0 al final del precio que son los decimales
                params["amount"] = precioCuota.toString() + "00"
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
                // Ocultar progressbar
                binding.includeProgressbar.progressbar.visibility = View.GONE
            }
            is PaymentSheetResult.Failed -> {
                // Ocultar progressbar
                binding.includeProgressbar.progressbar.visibility = View.GONE

                Toast.makeText(this, getString(R.string.payment_incomplete), Toast.LENGTH_SHORT)
                    .show()
            }
            is PaymentSheetResult.Completed -> {
                // Ocultar progressbar
                binding.includeProgressbar.progressbar.visibility = View.GONE
                // Guardar el pago en Firebase
                savePaymentFirebase(cuotaActual)
            }
        }
    }

    private fun savePaymentFirebase(cuotaPagar: Calendar) {
        // La numeración de los meses empieza en 0 (Enero = 0)
        val pago = Pago(
            // Convertir calendar a date para guardar en Firestore
            cuotaPagada = cuotaPagar.time,
            cantidad = precioCuota.toString()
        )
        firestore.collection("users").document(user_email).collection("payments")
            .document()
            .set(pago)
            .addOnSuccessListener {
                val intent = Intent(this, ConfirmPayActivity::class.java)
                intent.putExtra("estado", "correcto")
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                val intent = Intent(this, ConfirmPayActivity::class.java)
                intent.putExtra("estado", "incorrecto")
                startActivity(intent)
                finish()
            }
    }

    // Funcion para volver atras, al menú principal
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}