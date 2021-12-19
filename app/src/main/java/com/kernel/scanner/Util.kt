package com.kernel.scanner

import android.app.Activity
import androidx.core.text.isDigitsOnly
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat


fun findCodeInString(input:String):String{
    var processedStr=""
    for (ch in input.toCharArray()){
        if (ch.isLetter()||ch.isDigit()){
            processedStr+=ch
        }
    }
    var index=0;
    var savedC:Char?=null

    for (ch in processedStr.toCharArray()){

        if (ch.isLetter()){
            savedC=ch;
        }

        if (ch.isDigit() && savedC!=null){

            if (index+8>=processedStr.length) return ""

            val tmp=processedStr.substring(index,index+8)

            if (tmp.digitsOnly()){
                processedStr=processedStr.substring(index-1,index+8)
                return processedStr
            }
        }
        index++
    }

   return ""
}
fun String.digitsOnly() = all(Char::isDigit) && isNotEmpty()
fun openProjectLink(activity: Activity){
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/infinitum1984/KernelScanner"))
    activity.startActivity(browserIntent)
}

//Методы для тестрования
fun getRandomNumber() : String {
        val allowedLetters = ('А'..'Н')
        val allowedNums=('0'..'9')
        var outStr=""+allowedLetters.random()+allowedLetters.random()
        for (i in 0..3){
            outStr+=allowedNums.random()
        }
        outStr+=""+allowedLetters.random()+allowedLetters.random()
        return outStr
    }
fun getRandomPhone():String{
    val allowedChars = ('0'..'9')
    return (1..10)
        .map { allowedChars.random() }
        .joinToString("")
}

val listNames= listOf("Веснин Терентий Артамонович","Алачев Ионафан Милославович", "Борщов Валаам Саввич", "Кашкаров Вукол Орестович",
    "Евреинов Наркисс Корнеевич",
"Державин Мартын Мартинович", "Осокин Артемидор Всеславович","Самойлов Архипп Зенонович",
    "Щербань Валентин Павлинович",
    "Дубровин Митрий Евлампиевич", "Буяльский Тимофей Аристархович",
   "Лукин Иуст Мстиславович", "Крашевский Вукол Феоктистович","Ширков Карл Аркадьевич"


    )
fun getRandomName()= listNames.random()
