package dev.panwar.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var tvInput:TextView?=null
    private var lastNumeric: Boolean=false//for last number is numeric
    private var lastDot: Boolean=false//for last number is .

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvInput=findViewById(R.id.tvInput)
    }

    fun onDigit(view: View){
//       Toast.makeText(this,"Button Clicked",Toast.LENGTH_LONG).show()
//        in the on Click method on all buttons we created in activity main xml....we selected OnDigit means when we press that button
//        it will redirect to this function. This is Possible with this View parameter....this is single function for all the Parameters
        tvInput?.append((view as Button).text)
        lastNumeric=true
        lastDot=false
    }

    fun onClear(view: View){
        tvInput?.text=""
    }

    fun onDot(view: View){
        //for handling multiple dots like ..
        if(lastNumeric && !lastDot){
            tvInput?.append(".")
            lastNumeric=false
            lastDot=true
        }
    }

    fun onEqual(view: View){
        if(lastNumeric){   // to avoid cases like 99+=
           var tvValue=tvInput?.text.toString()
           var prefix=""
            try {

                if(tvValue.startsWith("-")){  //for the case -30-10 split value has one empty and then two 30-10 which will cause error further and crash app
                    prefix="-"
                    //so new tvValue will be 30-10 instead of -30-10
                    tvValue=tvValue.substring(1)
                }

                if(tvValue.contains("-")){

                    val splitValue=tvValue.split("-") //like if 99-1 it consider 99 as first part and 1 as other ...and put in Array
                    var one=splitValue[0]//99 //here one=30
                    var two=splitValue[1]//1  //two=10

                    if(prefix.isNotEmpty()){
                        one=prefix+one //adding the removed - sign from tvValue to one again like above we made tvValue to 30-10  from -30-10 now one=-30
                    }

                    tvInput?.text=removeZeroAfterDot((one.toDouble()-two.toDouble()).toString())//for 99.0 to 99

                }else  if(tvValue.contains("+")){

                    val splitValue=tvValue.split("+") //like if 99-1 it consider 99 as first part and 1 as other ...and put in Array
                    var one=splitValue[0]//99 //here one=30
                    var two=splitValue[1]//1  //two=10

                    if(prefix.isNotEmpty()){
                        one=prefix+one //adding the removed - sign from tvValue to one again like above we made tvValue to 30-10  from -30-10 now one=-30
                    }

                    tvInput?.text=removeZeroAfterDot((one.toDouble()+two.toDouble()).toString())

                } else if(tvValue.contains("/")){

                    val splitValue=tvValue.split("/") //like if 99-1 it consider 99 as first part and 1 as other ...and put in Array
                    var one=splitValue[0]//99 //here one=30
                    var two=splitValue[1]//1  //two=10

                    if(prefix.isNotEmpty()){
                        one=prefix+one //adding the removed - sign from tvValue to one again like above we made tvValue to 30-10  from -30-10 now one=-30
                    }

                    tvInput?.text=removeZeroAfterDot((one.toDouble()/two.toDouble()).toString())

                }
                else  if(tvValue.contains("*")){

                    val splitValue=tvValue.split("*") //like if 99-1 it consider 99 as first part and 1 as other ...and put in Array
                    var one=splitValue[0]//99 //here one=30
                    var two=splitValue[1]//1  //two=10

                    if(prefix.isNotEmpty()){
                        one=prefix+one //adding the removed - sign from tvValue to one again like above we made tvValue to 30-10  from -30-10 now one=-30
                    }

                    tvInput?.text=removeZeroAfterDot((one.toDouble()*two.toDouble()).toString())

                }

            }
            catch (e: java.lang.ArithmeticException){
                e.printStackTrace()
            }
        }
    }

   private fun removeZeroAfterDot(result: String) : String{
       var value=result
        if(value.contains(".0")){
            value=result.substring(0,result.length-2)
        }
        return value
    }

    fun onOperator(view: View){
        tvInput?.text?.let {//if they are true then this(it contains this value tvInput?.text) statement execute for no errors we directly cannot write isOperatorAdded(tvInput?text.toString())...this will give error
            if(lastNumeric && !isOperatorAdded(it.toString())){//for 9/9/ error if there is already a operator we don't add another */-+ we cannot add more than one operator eg ++ -+ /+ 9/9+ etc
               tvInput?.append((view as Button).text)
                lastNumeric=false
                lastDot=false
            }
        }

    }

    fun onBack(view: View){
       if(tvInput?.text?.length!=0){
           var value=tvInput?.text.toString()
           value=value.substring(0,value.length-1)
           tvInput?.text=value
       }
        if(!lastNumeric){
            lastNumeric=true
        }
    }

    private fun isOperatorAdded(value: String): Boolean{
        return if(value.startsWith("-")){ //this will ignore - ....like when -36 it will treat as 36 only....
            false
        }
        else{ //returns true/false
            value.contains("/") || value.contains("*") || value.contains("-") || value.contains("+")
        }
    }

}