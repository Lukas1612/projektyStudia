package  com.example.readyreadingkotlin


import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.readyreadingkotlin.ui.home.HomeFragment
import com.example.readyreadingkotlin.ui.home.HomeViewModel
import com.example.readyreadingkotlin.ui.mainpage.MainPageFragment
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var mainPageFragment: MainPageFragment? = null




    private val homeViewModel: HomeViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainPageFragment= MainPageFragment()
        setCurrentFragment(mainPageFragment!!)




    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment, fragment)
            commit()
        }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == QUESTIONNAIRE_REQUEST) {
            if (resultCode == RESULT_OK) {

            }
        }
    }

    //json stored in the assets folder. but you can get it from wherever you like.
    private fun loadQuestionnaireJson(filename: String): String? {
        return try {
            val `is` = assets.open(filename)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }

    companion object {
        private const val QUESTIONNAIRE_REQUEST = 2018
    }

    override fun onBackPressed() {

        if(homeViewModel.selectedItem.value == null)
        {
            setCurrentFragment(MainPageFragment())
        }else
        {
            setCurrentFragment(HomeFragment())
        }

    }

}