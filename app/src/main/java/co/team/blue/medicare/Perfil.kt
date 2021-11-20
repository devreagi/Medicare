package co.team.blue.medicare

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class Perfil : AppCompatActivity() {


    private var bottomNavigationView: BottomNavigationView? = null
    private var fotoPerfil: CircleImageView? = null
    private var nombrePerfil: TextView? = null
    private var dbReference: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        mAuth = FirebaseAuth.getInstance()
        dbReference = FirebaseDatabase.getInstance().reference.child("usuario")
        val user = mAuth!!.currentUser

        bottomNavigationView = findViewById(R.id.footer)
        fotoPerfil = findViewById(R.id.imgFotoPerfil)
        nombrePerfil = findViewById(R.id.txtNombreUsuario)

        //carga inforusuario
        if (user != null) {
            infoUsuario()
        }

        bottomNavigationView?.run {
            isSelected = true
            setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener setOnItemSelectedListener@{ item: MenuItem ->
                when (item.itemId) {
                    R.id.pedidos -> {
                        startActivity(Intent(applicationContext, Pedido::class.java))
                        overridePendingTransition(0, 0)
                        return@setOnItemSelectedListener true
                    }
                    R.id.carrito -> {
                        startActivity(Intent(applicationContext, Carrito::class.java))
                        overridePendingTransition(0, 0)
                        return@setOnItemSelectedListener true
                    }
                    R.id.home -> {
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        overridePendingTransition(0, 0)
                        return@setOnItemSelectedListener true
                    }
                    else -> false
                }
            })
        }
    }

    private fun infoUsuario() {
        mAuth!!.currentUser?.let {
            dbReference!!.child(it.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.childrenCount > 0) {
                            val nombre = (dataSnapshot.child("nombre").value).toString()
                            nombrePerfil?.text = nombre
                            if (dataSnapshot.hasChild("imagen")) {
                                val image = (dataSnapshot.child("imagen").value).toString()
                                Picasso.get().load(image).into(fotoPerfil)
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }
    }

    //ir  a editar campos del perfil
    fun irEditarPerfil(@Suppress("UNUSED_PARAMETER") view: View?) {
        val editarPerfil = Intent(this, EditarPerfil::class.java)
        startActivity(editarPerfil)
    }

    //boton cerrar sesion
    fun cerrarSesion(@Suppress("UNUSED_PARAMETER") view: View?) {
        val home = Intent(applicationContext, MainActivity::class.java)
        MaterialDialog(this).show {
            icon(R.drawable.power_off)
            title(R.string.cerrar_sesi_n)
            message(R.string.msg_cerrar_sesion)
            positiveButton(R.string.si) {
                startActivity(home)
            }
            negativeButton(R.string.no) { dialog ->
                dialog.dismiss()
            }
        }
    }

    //boton abrir whatsapp/soporte
    fun abrirWhatsApp(@Suppress("UNUSED_PARAMETER") view: View?) {
        val number = "+57 3105888139"
        val url = "https://api.whatsapp.com/send?phone=$number"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    //Abrir play store
    fun abrirPlayStore(@Suppress("UNUSED_PARAMETER") view: View?) {
        val playStore = packageManager.getLaunchIntentForPackage("com.android.vending")
        if (playStore != null) {
            startActivity(playStore)
        } else {
            Toast.makeText(this, "Error al abrir la tienda de  aplicaciones", Toast.LENGTH_LONG)
                .show()
        }
    }

    //boton mis compras
    fun irPedidos(@Suppress("UNUSED_PARAMETER") view: View?) {
        val pedidos = Intent(this, Pedido::class.java)
        startActivity(pedidos)
    }
}