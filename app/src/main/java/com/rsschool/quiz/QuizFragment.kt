package com.rsschool.quiz

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    var questions:ArrayList<Question> = arrayListOf(

        Question(1,"Who is the first man landed on moon?",
            arrayListOf("Neil Armstrong","Edwin Aldrin", "Michael Collins", "Yuri Gregory","Yuri Gagarin"),1),
        Question(2,"Socrates is best known for - ",
            arrayListOf("Philosophy","Mathmetics","Physiology","Astrology","Algebra"),1),
        Question(3,"How many states does USA have? ",
            arrayListOf("50","45","55","49","51"),1),
        Question(4,"Which is not an Europian Country? ",
            arrayListOf("Combodia","Estonia","Lithunia","Moldova","Russia"),1),
        Question(5,"Who is the first President of USA? ",
            arrayListOf("George Washington","William Henry Harrison","Abraham Lincoln","Franklin D. Roosevelt","Abama"),1),
        Question(6,"Which one is the largest ocean? ",
            arrayListOf("Pacific","Atlantic","Mediterian","Arctic","Indian"),1),
        Question(7,"What country has a town named Marathon? ",
            arrayListOf("USA","GREECE","ITALY","FRANCE","RUSSIA"),1),
        Question(8,"What well-known mountain pass connects Pakistan and Afghanistan? ",
            arrayListOf("Khyber Pass","Malakand Pass","Ahmad Pass","Shandar Pass","Abracadabra"),1),
        Question(9,"What country was formerly known as Ceylon?",
            arrayListOf("Sri Lanka","Sweden","Vietnam","Switzerland","Russia"),1),
        Question(10,"Which is the Independence day of Bangladesh?",
            arrayListOf("26 March","21 Feb","14th April","16 December","1 December"),1 ),
    )
    lateinit var currentQuestion:Question
    private var indexQuestion = 0
    private var result = 0

     private fun setQuestion(){
        currentQuestion = questions[indexQuestion]
        binding.toolbar.title = "Question ${currentQuestion.idQuestion}"

         when(sharedPreferences.getInt(currentQuestion.idQuestion.toString(),-1)) {
             -1 -> {
                 binding.radioGroup.clearCheck()
                 binding.nextButton.isEnabled=false
             }
             1 -> binding.radioGroup.check(R.id.option_one)
             2 -> binding.radioGroup.check(R.id.option_two)
             3 -> binding.radioGroup.check(R.id.option_three)
             4 -> binding.radioGroup.check(R.id.option_four)
             5 -> binding.radioGroup.check(R.id.option_five)
         }

         when (indexQuestion){
             0 -> binding.previousButton.isEnabled = false
             9 -> binding.nextButton.text="SUBMIT"
             else -> {
                 binding.previousButton.isEnabled=true
                 binding.nextButton.text="NEXT"
             }
         }
         binding.invalidateAll()
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences= activity?.getSharedPreferences("answer",MODE_PRIVATE) !!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        binding.quiz=this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fun clearRadioCheck() {
            for(i in 1..10)
                sharedPreferences.edit().putInt(i.toString(),-1).apply()
        }
         fun setTheme(){
            val typedValue = TypedValue()
             val arrayTheme = arrayOf(R.style.Theme_Quiz_First, R.style.Theme_Quiz_Second,
                 R.style.Theme_Quiz_Third, R.style.Theme_Quiz_Fourth, R.style.Theme_Quiz_Fifth)
            val currentTheme = context?.theme
            currentTheme?.resolveAttribute(android.R.attr.colorPrimary, typedValue, true)
            currentTheme?.resolveAttribute(android.R.attr.background, typedValue, true)
            currentTheme?.resolveAttribute(android.R.attr.colorPrimaryDark, typedValue, true)
            binding.toolbar.background = ColorDrawable(typedValue.data)
            val window = activity?.window
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window?.decorView?.setBackgroundColor(typedValue.data)
            window?.statusBarColor =typedValue.data
             currentTheme?.applyStyle(arrayTheme.random(), true)
//             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                 activity?.window?.statusBarColor = activity?.getColor(R.color.yellow_100_dark)!!
//             }

        }

        setTheme()
        clearRadioCheck()
        setQuestion()
        val intent = Intent(requireContext(),ResultActivity::class.java)
        intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
        intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK


        binding.radioGroup.setOnCheckedChangeListener { _, _ ->
            when (binding.radioGroup.checkedRadioButtonId) {
                R.id.option_one -> {
                    sharedPreferences.edit().putInt((currentQuestion.idQuestion).toString(),1).apply()
                    binding.nextButton.isEnabled=true
                }
                R.id.option_two -> {
                    sharedPreferences.edit().putInt((currentQuestion.idQuestion).toString(),2).apply()
                    binding.nextButton.isEnabled=true
                }
                R.id.option_three -> {
                    sharedPreferences.edit().putInt((currentQuestion.idQuestion).toString(),3).apply()
                    binding.nextButton.isEnabled=true
                }
                R.id.option_four -> {
                    sharedPreferences.edit().putInt((currentQuestion.idQuestion).toString(),4).apply()
                    binding.nextButton.isEnabled=true
                }
                R.id.option_five -> {
                    sharedPreferences.edit().putInt((currentQuestion.idQuestion).toString(),5).apply()
                    binding.nextButton.isEnabled=true
                    }
                else -> {
                    sharedPreferences.edit().putInt((currentQuestion.idQuestion).toString(), -1).apply()
                }
            }
        }

        binding.nextButton.setOnClickListener {
            when (indexQuestion) {
                in 0..8 -> {
                    indexQuestion += 1
                    setQuestion()
                }
                9 -> {
                    for(i in 1..10){
                        if (sharedPreferences.getInt(i.toString(),0)==questions[i-1].indexValidAnswer)
                        result += 1
                    }

                    val list = arrayListOf<String>()
                    for (i in 0..9){
                        list.add(i,"${questions[i].idQuestion}. ${questions[i].theQuestion}  " +
                                "\n  Your answer:${questions[i].theAnswer[sharedPreferences.getInt((i+1).toString(),0)-1]}"

                        )}
                    intent.putExtra("Answer", list)
                    intent.putExtra("Result", result)

                    startActivity(intent)
                    requireActivity().finish()
                }
            }
            setTheme()
            }

        binding.toolbar.setNavigationOnClickListener {
            if (indexQuestion!=0)
                indexQuestion -= 1
                setQuestion()
                setTheme()
            }

        binding.previousButton.setOnClickListener {
            indexQuestion -= 1
            setQuestion()
            setTheme()
            }
        }

    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }

