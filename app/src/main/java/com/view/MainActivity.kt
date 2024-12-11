package com.view


import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.controller.AccessDone
import com.controller.Bridge
import com.controller.Switcher
import com.controller.Utils
import com.view.control_view.fragments.AccountFragment
import com.view.databinding.ActivityMainBinding
import com.view.control_view.fragments.HistoryFragment
import com.view.control_view.fragments.HomeFragment


class MainActivity : AppCompatActivity(), Utils {

    private lateinit var binding: ActivityMainBinding

    private val homeFragment: HomeFragment = HomeFragment()
    private var historyFragment: HistoryFragment =
        HistoryFragment()  //evita le continue ricreazioni dei frammenti
    private var accountFragment: AccountFragment = AccountFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()   //fa partire lo Splash creato in splash.xml

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        makeCurrentFragment(homeFragment)  //si parte con la home
        binding = ActivityMainBinding.inflate(layoutInflater)
        this.setContentView(binding.root)
        readyForOperations()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {  //qui viene creato mentre nel frammento viene gestito
        val menuInf: MenuInflater = menuInflater
        menuInf.inflate(R.menu.upper_bar, menu)
        return true
    }

    override fun onDestroy() {
        Bridge.writeObjectToFile(this)   //WRITE alla chiusura
        super.onDestroy()
    }

    override fun onPause() {                    //WRITE background in teoria
        Bridge.writeObjectToFile(this)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Switcher.newCurrentActivity(this)  //si specifica allo Switcher l'attività corrente
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {  //vale solo per questo frammento

        /*
        override solo del Log Out che fa la stessa funzionein tutti i frammenti
         */
        return when (item.itemId) {
            R.id.logout_item -> {
                Switcher.switchToLoginPage()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }

    private fun readyForOperations() {

        if (!AccessDone.firstAccessMain) {  //lo fa solo se è il primo accesso
            try {
                Bridge.readObjectFromFile(this)  //legge in apertura
                AccessDone.firstAccessMain = true
                /*
                 se non vogliamo che siano salvate le nostre credenziali,dopo la lettura andiamo a pulirle
                 */
                if (!Bridge.database.access) {
                    Bridge.database.clearCredentials()
                }
            } catch (e: Exception) {
                shortToast(this, "Errore nel caricamento dati")
            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeID -> makeCurrentFragment(homeFragment)
                R.id.historyID -> makeCurrentFragment(historyFragment)
                R.id.accountID -> makeCurrentFragment(accountFragment)
            }
            true
        }

    }


}










